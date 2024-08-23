package com.mydrive.backend.services;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.fileproperties.*;
import com.dropbox.core.v2.files.*;
import com.dropbox.core.v2.users.FullAccount;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mydrive.backend.dtos.FileDTO;
import com.mydrive.backend.exceptions.CloudLimitationException;
import com.mydrive.backend.services.utils.FileEncryptionUtil;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("dropbox")
@Scope(WebApplicationContext.SCOPE_SESSION)
@PropertySource("classpath:application-secrets.properties")
public class DropboxService implements CloudStorageService {

    @Value("${dropbox.clientId}")
    private String clientId;

    @Value("${dropbox.clientSecret}")
    private String clientSecret;

    @Value("${dropbox.oauth.redirectUri}")
    private String redirectUri;

    DbxClientV2 client = null;

    String templateId = null;

    private static final Logger logger = LoggerFactory.getLogger(DropboxService.class);

    public String redirectToAuthorization() {
        String authorizeUrl = "https://www.dropbox.com/oauth2/authorize"
                + "?client_id=" + clientId
                + "&token_access_type=offline"
                + "&redirect_uri=" + redirectUri
                + "&response_type=code";

        return authorizeUrl; // Redirect to Dropbox authorization URL
    }

    public void authenticateUser(String code) throws Exception {
        // Exchange the code for an access token
        String tokenUrl = "https://www.dropbox.com/oauth2/token";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("code", code);
        map.add("grant_type", "authorization_code");
        map.add("redirect_uri", redirectUri);
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, request, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            // Parse the JSON response to extract the access token
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response.getBody());
            String accessToken = rootNode.get("access_token").asText();

            // Use the access token to authenticate with Dropbox
            DbxRequestConfig config = new DbxRequestConfig("MyDrive");
            client = new DbxClientV2(config, accessToken);

            initializeTemplateId();
        }
    }

    public boolean checkAuthentication() {
        logger.info("Dropbox: " + (client != null));
        return client != null;
    }

    public void logout() {
        this.client = null;
    }

    public String getProfilePhoto() throws Exception {
        // Obtener información de la cuenta del usuario
        FullAccount account = client.users().getCurrentAccount();

        return account.getProfilePhotoUrl();
    }

    public String getUserName() throws Exception {
        // Obtener información de la cuenta del usuario
        FullAccount account = client.users().getCurrentAccount();

        return account.getName().getDisplayName();
    }

    public List<FileDTO> getPathFolder(String folderId) throws Exception {
        List<FileDTO> fullPath = new ArrayList<>();

        // Añadir la carpeta raíz
        FileDTO rootFolderDTO = new FileDTO();
        rootFolderDTO.setId("");  // ID vacío para la raíz
        rootFolderDTO.setName("root");  // Nombre de la carpeta raíz
        fullPath.add(rootFolderDTO);

        if (folderId.equals("root")) return fullPath;

        // Obtener metadata de la carpeta
        Metadata metadata = client.files().getMetadata(folderId);
        String pathDisplay = metadata.getPathDisplay();

        // Eliminar la primera barra '/' de la cadena
        if (pathDisplay.startsWith("/")) {
            pathDisplay = pathDisplay.substring(1);
        }

        // Dividir la ruta en segmentos
        List<String> segments = Arrays.asList(pathDisplay.split("/"));
        String currentFolderId = "";

        // Crear un FileDTO para cada carpeta en la ruta
        StringBuilder currentPath = new StringBuilder();
        for (String segment : segments) {
            // Agregar segmento al path actual
            currentPath.append("/").append(segment);

            // Obtener el ID del segmento actual
            String segmentId = getFolderIdByName(currentFolderId, segment);

            // Crear un FileDTO para la carpeta actual
            FileDTO folderDTO = new FileDTO();
            folderDTO.setId(segmentId);
            folderDTO.setName(segment);
            fullPath.add(folderDTO);

            // Actualizar el ID actual para el próximo segmento
            currentFolderId = segmentId;
        }

        return fullPath;
    }

    public List<FileDTO> getFoldersInFolder(String folderId, String folderName) throws Exception {
        if (folderId.equals("root")) folderId = ""; // root para dropbox es cadena vacía
        // Realizar la consulta para obtener todas las carpetas en esta carpeta
        ListFolderResult result = client.files().listFolder(folderId);

        List<FileDTO> foldersDTOList = new ArrayList<>();

        while (true) {
            for (Metadata metadata : result.getEntries()) {
                if (metadata instanceof FolderMetadata && metadata.getName().contains(folderName)) {
                    FolderMetadata folderMetadata = (FolderMetadata) metadata;
                    FileDTO folderDTO = convertToFileDTO(folderMetadata);
                    foldersDTOList.add(folderDTO);
                }
            }

            if (!result.getHasMore()) break;
            result = client.files().listFolderContinue(result.getCursor());
        }

        return foldersDTOList;
    }

    public List<FileDTO> getFilesInFolder(String folderId, String fileName) throws Exception {
        if (folderId.equals("root")) folderId = ""; // root para dropbox es cadena vacía

        // Realizar la consulta para obtener todos los archivos en esta carpeta
        ListFolderResult result = client.files().listFolder(folderId);

        List<FileDTO> filesDTOList = new ArrayList<>();

        while (true) {
            for (Metadata metadata : result.getEntries()) {
                if (metadata instanceof FileMetadata && metadata.getName().contains(fileName)) {
                    FileMetadata fileMetadata = (FileMetadata) metadata;
                    FileDTO fileDTO = convertToFileDTO(fileMetadata);
                    filesDTOList.add(fileDTO);
                }
            }

            if (!result.getHasMore()) break;
            result = client.files().listFolderContinue(result.getCursor());
        }

        return filesDTOList;
    }

    public List<FileDTO> getRecentFiles(String maxDate, String fileName) throws Exception {
        // Convertir la cadena de texto a un objeto Date
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date parsedDate = formatter.parse(maxDate);

        // Obtener la lista de archivos del usuario
        ListFolderResult result = client.files().listFolderBuilder("")
                .withIncludeDeleted(false)
                .withRecursive(true)
                .start();

        List<FileDTO> filesDTOList = new ArrayList<>();

        // Recorremos todas las páginas de la paginación para obtener una lista con todos los archivos
        while (true) {
            for (Metadata metadata : result.getEntries()) {
                if (metadata instanceof FileMetadata && metadata.getName().contains(fileName)) {
                    FileMetadata fileMetadata = (FileMetadata) metadata;
                    if (fileMetadata.getServerModified().after(parsedDate)) {
                        FileDTO fileDTO = convertToFileDTO(fileMetadata);
                        filesDTOList.add(fileDTO);
                    }
                }
            }

            if (!result.getHasMore()) break;
            result = client.files().listFolderContinue(result.getCursor());
        }

        return filesDTOList;
    }

    public List<FileDTO> getFilesInBin(String fileName) throws Exception {
        ListFolderResult result = client.files().listFolderBuilder("")
                .withIncludeDeleted(true)
                .withIncludeHasExplicitSharedMembers(false)
                .withRecursive(true)
                .start();

        List<FileDTO> filesDTOList = new ArrayList<>();

        while (true) {
            for (Metadata metadata : result.getEntries()) {
                if (metadata instanceof DeletedMetadata && metadata.getName().contains(fileName)) {
                    DeletedMetadata deletedMetadata = (DeletedMetadata) metadata;
                    FileDTO fileDTO = convertToFileDTO(deletedMetadata);
                    filesDTOList.add(fileDTO);
                }
            }

            if (!result.getHasMore()) break;
            result = client.files().listFolderContinue(result.getCursor());
        }

        return filesDTOList;
    }

    public List<FileDTO> searchFolders(String folderName) throws Exception {
        ListFolderResult result = client.files().listFolderBuilder("")
                .withRecursive(true)
                .start();

        List<FileDTO> foldersDTOList = new ArrayList<>();

        while (true) {
            for (Metadata metadata : result.getEntries()) {
                if (metadata instanceof FolderMetadata && metadata.getName().contains(folderName)) {
                    FolderMetadata folderMetadata = (FolderMetadata) metadata;
                    FileDTO folderDTO = convertToFileDTO(folderMetadata);
                    foldersDTOList.add(folderDTO);
                }
            }

            if (!result.getHasMore()) break; // Condición de parada
            result = client.files().listFolderContinue(result.getCursor());
        }

        return foldersDTOList;
    }

    public List<FileDTO> searchFiles(String fileName) throws Exception {
        ListFolderResult result = client.files().listFolderBuilder("")
                .withRecursive(true)
                .start();

        List<FileDTO> filesDTOList = new ArrayList<>();

        while (true) {
            for (Metadata metadata : result.getEntries()) {
                if (metadata instanceof FileMetadata && metadata.getName().contains(fileName)) {
                    FileMetadata fileMetadata = (FileMetadata) metadata;
                    FileDTO fileDTO = convertToFileDTO(fileMetadata);
                    filesDTOList.add(fileDTO);
                }
            }

            if (!result.getHasMore()) break; // Condición de parada
            result = client.files().listFolderContinue(result.getCursor());
        }

        return filesDTOList;
    }

    public void createFolder(String folderId, String folderName) throws Exception {
        // Construir la ruta completa de la nueva carpeta dentro de la carpeta padre
        String folderPath = getPathMetadata(folderId);
        String fullPath = folderPath + "/" + folderName;

        client.files().createFolderV2(fullPath);
    }

    public void uploadFile(MultipartFile file, String folderId) throws Exception {
        if (file.isEmpty()) {
            throw new Exception("File is empty");
        }

        // Preparar el nombre y la ruta del archivo en Dropbox
        String fileName = file.getOriginalFilename();
        String dropboxFilePath = getPathMetadata(folderId) + "/" + fileName;

        // Crear un InputStream para el archivo
        InputStream fileInputStream = new ByteArrayInputStream(file.getBytes());

        // Subir el archivo a Dropbox
        client.files().uploadBuilder(dropboxFilePath)
                .withMode(WriteMode.ADD) // Sobrescribir si ya existe un archivo con el mismo nombre
                .uploadAndFinish(fileInputStream);

        fileInputStream.close();
    }

    public void uploadFile(InputStream inputStream, String fileName, String folderId) throws Exception {
        if (inputStream == null) {
            throw new Exception("Input stream is null");
        }

        // Preparar la ruta del archivo en Dropbox
        String dropboxFilePath = getPathMetadata(folderId) + "/" + fileName;

        // Subir el archivo a Dropbox
        client.files().uploadBuilder(dropboxFilePath)
                .withMode(WriteMode.ADD)
                .uploadAndFinish(inputStream);
    }

    public void uploadEncryptedFile(MultipartFile file, String password, String folderId) throws Exception {
        java.io.File tempFile = java.io.File.createTempFile("upload", null);
        java.io.File encryptedTempFile = java.io.File.createTempFile("encryptedUpload", null);

        try {
            file.transferTo(tempFile);

            // Encriptar el archivo temporal
            FileEncryptionUtil.encryptFile(tempFile, encryptedTempFile, password);

            // Preparar la ruta del archivo en Dropbox
            String fileName = file.getOriginalFilename();
            String dropboxFilePath = getPathMetadata(folderId) + "/" + fileName;


            // Subir el archivo cifrado a Dropbox
            InputStream inputStream = new FileInputStream(encryptedTempFile);
            FileMetadata fileMetadata = client.files().uploadBuilder(dropboxFilePath)
                    .withMode(WriteMode.ADD)
                    .uploadAndFinish(inputStream);

            // Configurar las propiedades personalizadas del archivo
            List<PropertyGroup> propertyGroups = new ArrayList<>();
            List<PropertyField> encryptionProperties = new ArrayList<>();
            encryptionProperties.add(new PropertyField("encrypted", "true"));
            propertyGroups.add(new PropertyGroup(templateId, encryptionProperties));

            // Añadir propiedades personalizadas al archivo después de la subida
            client.fileProperties().propertiesAdd(fileMetadata.getId(), propertyGroups);
        } finally {
            tempFile.delete();
            encryptedTempFile.delete();
        }
    }

    public String getPreviewLink(String fileId) throws Exception {
        if (fileId.equals("null"))
            throw new CloudLimitationException("No se puede obtener la preview del archivo indicado");

        // Obtener metadata del archivo
        Metadata metadata = client.files().getMetadata(fileId);
        return "https://www.dropbox.com/home" + metadata.getPathDisplay();
    }

    public FileDTO getFileDetails(String fileId) throws Exception {
        // Obtener los detalles completos del archivo usando el fileId
        FileMetadata fileMetadata = (FileMetadata) client.files().getMetadata(fileId);

        // Convertir el archivo a un DTO
        return convertToFileDTO(fileMetadata);
    }

    public byte[] downloadFile(String fileId) throws Exception {
        // Crear un OutputStream para almacenar el contenido del archivo
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Descargar el archivo de Dropbox
        client.files().download(fileId)
                .download(outputStream);

        // Obtener el contenido del archivo como un array de bytes
        byte[] fileContent = outputStream.toByteArray();

        return fileContent;
    }

    public byte[] downloadEncryptedFile(String fileId, String password) throws Exception {
        java.io.File tempFile = java.io.File.createTempFile("download", null);
        java.io.File decryptedTempFile = java.io.File.createTempFile("decryptedDownload", null);

        try (OutputStream tempFileStream = new FileOutputStream(tempFile)) {
            // Descargar el archivo directamente a un OutputStream
            try (OutputStream outputStream = new ByteArrayOutputStream()) {
                // Descargar el archivo desde Dropbox
                client.files().download(fileId).download(outputStream);

                // Escribir el contenido descargado en el archivo temporal
                byte[] fileContent = ((ByteArrayOutputStream) outputStream).toByteArray();
                tempFileStream.write(fileContent);
            }

            // Descifrar el archivo temporal
            FileEncryptionUtil.decryptFile(tempFile, decryptedTempFile, password);

            // Leer el archivo descifrado en un byte array
            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                 InputStream inputStream = new FileInputStream(decryptedTempFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                }
                return byteArrayOutputStream.toByteArray();
            }
        } finally {
            tempFile.delete();
            decryptedTempFile.delete();
        }
    }

    public void moveFile(String fileId, String targetFolderId) throws Exception {
        // Mover el archivo o carpeta a la carpeta de destino
        String targetFolderPath = getPathMetadata(targetFolderId);
        Metadata file = client.files().getMetadata(fileId);

        client.files().moveV2(file.getPathDisplay(), targetFolderPath + "/" + file.getName());
    }

    public void renameFile(String fileId, String newName) throws Exception {
        // Mover el archivo a la misma carpeta actual pero cambiando el nombre
        Metadata fileMetadata = client.files().getMetadata(fileId);

        // Obtener la ruta de la carpeta contenedora del archivo
        String parentFolderPath = fileMetadata.getPathDisplay()
                .substring(0, fileMetadata.getPathDisplay().lastIndexOf("/"));

        // Construir la nueva ruta con el nuevo nombre
        String newFilePath = parentFolderPath + "/" + newName;

        // Realizar la operación de mover el archivo a la nueva ruta
        client.files().moveV2(fileMetadata.getPathDisplay(), newFilePath);
    }

    public void throwAwayFile(String fileId) throws Exception {
        // Enviar solicitud para eliminar el archivo
        client.files().deleteV2(fileId);
    }

    public void restoreFile(String fileId) {
        // Lanza excepción para indicar que hay limitación por parte de la api
        throw new CloudLimitationException("No se puede restaurar el archivo debido a limitaciones en la nube");
    }

    public void deleteFile(String fileId) {
        // Lanza excepción para indicar que hay limitación por parte de la api
        throw new CloudLimitationException("No se puede restaurar el archivo debido a limitaciones en la nube");
    }

    private void initializeTemplateId() throws Exception {
        String templateName = "encryption";

        // Verificar si el template ya existe
        List<String> templateIds = client.fileProperties().templatesListForUser().getTemplateIds();
        for (String id : templateIds) {
            String currentTemplateName = client.fileProperties().templatesGetForUser(id).getName();
            if (currentTemplateName.equals(templateName)) {
                this.templateId = id;
                return;
            }
        }

        // Crear el PropertyFieldTemplate si no existe
        PropertyFieldTemplate fieldTemplate = new PropertyFieldTemplate("encrypted", "Encrypted", PropertyType.STRING);
        AddTemplateResult result = client.fileProperties().templatesAddForUser(templateName,
                "Template for encryption metadata", Collections.singletonList(fieldTemplate));
        this.templateId = result.getTemplateId();
    }

    public boolean isFileEncrypted(FileMetadata fileMetadata) throws Exception {
        // Obtener las propiedades personalizadas del archivo
        TemplateFilterBase filter = TemplateFilterBase.filterSome(Collections.singletonList(templateId));
        FileMetadata fileMetadataGroups = (FileMetadata) client.files().getMetadataBuilder(fileMetadata.getPathLower())
                .withIncludePropertyGroups(filter)
                .start();

        for (PropertyGroup group : Objects.requireNonNull(fileMetadataGroups.getPropertyGroups())) {
            for (PropertyField field : group.getFields()) {
                if (field.getName().equals("encrypted") && field.getValue().equals("true")) {
                    return true;
                }
            }
        }
        return false;
    }

    private FileDTO convertToFileDTO(FolderMetadata folderMetadata) {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setId(folderMetadata.getId());
        fileDTO.setName(folderMetadata.getName());
        fileDTO.setLastTimeViewed("");
        fileDTO.setSize(0L);
        fileDTO.setEncrypted(false);
        return fileDTO;
    }

    private FileDTO convertToFileDTO(FileMetadata fileMetadata) throws Exception {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setId(fileMetadata.getId());
        fileDTO.setName(fileMetadata.getName());
        fileDTO.setLastTimeViewed(formatToISO8601(fileMetadata.getServerModified()));
        fileDTO.setSize(fileMetadata.getSize());
        fileDTO.setEncrypted(isFileEncrypted(fileMetadata));
        return fileDTO;
    }

    private FileDTO convertToFileDTO(DeletedMetadata deletedMetadata) {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setId("null");
        fileDTO.setName(deletedMetadata.getName());
        fileDTO.setLastTimeViewed("Archivo eliminado");
        fileDTO.setSize(0L);
        fileDTO.setEncrypted(false);
        return fileDTO;
    }

    private String formatToISO8601(Date date) {
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return isoFormat.format(date);
    }

    private String getPathMetadata(String fileId) throws Exception {
        if (fileId.equals("root")) return "";

        // Obtener metadata del path de la carpeta
        Metadata metadata = client.files().getMetadata(fileId);
        return metadata.getPathDisplay();
    }

    private String getFolderIdByName(String parentFolderId, String folderName) throws Exception {
        // Obtener la lista de archivos y carpetas en la carpeta padre
        ListFolderResult result = client.files().listFolder(parentFolderId);

        for (Metadata metadata : result.getEntries()) {
            if (metadata instanceof FolderMetadata && metadata.getName().equals(folderName)) {
                return ((FolderMetadata) metadata).getId();
            }
        }

        // Si no se encuentra la carpeta, lanzar una excepción
        throw new Exception("Folder not found: " + folderName);
    }
}
