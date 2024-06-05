package com.mydrive.backend.services;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.*;
import com.dropbox.core.v2.users.FullAccount;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mydrive.backend.dtos.FileDTO;
import com.mydrive.backend.exceptions.CloudLimitationException;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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
        }
    }

    public boolean checkAuthentication() {
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

        // Modificar los parámetros que desees antes de convertirlos en DTO
        while (true) {
            for (Metadata metadata : result.getEntries()) {
                if (metadata instanceof FolderMetadata) {
                    FolderMetadata folderMetadata = (FolderMetadata) metadata;
                    // Verificar si el nombre de la carpeta contiene folderName
                    if (folderMetadata.getName().contains(folderName)) {
                        FileDTO folderDTO = new FileDTO();
                        folderDTO.setId(folderMetadata.getId());
                        folderDTO.setName(folderMetadata.getName());
                        folderDTO.setLastTimeViewed("");
                        folderDTO.setSize(0L);
                        foldersDTOList.add(folderDTO);
                    }
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

        // Modificar los parámetros que desees antes de convertirlos en DTO
        while (true) {
            for (Metadata metadata : result.getEntries()) {
                if (metadata instanceof FileMetadata) {
                    FileMetadata fileMetadata = (FileMetadata) metadata;
                    // Verificar si el nombre del archivo contiene fileName
                    if (fileMetadata.getName().contains(fileName)) {
                        FileDTO fileDTO = new FileDTO();
                        fileDTO.setId(fileMetadata.getId());
                        fileDTO.setName(fileMetadata.getName());
                        fileDTO.setLastTimeViewed(formatToISO8601(fileMetadata.getServerModified()));
                        fileDTO.setSize(fileMetadata.getSize());
                        filesDTOList.add(fileDTO);
                    }
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
                if (metadata instanceof FileMetadata) {
                    FileMetadata fileMetadata = (FileMetadata) metadata;
                    if (fileMetadata.getServerModified().after(parsedDate) &&
                            fileMetadata.getName().contains(fileName)) {
                        FileDTO fileDTO = new FileDTO();
                        fileDTO.setId(fileMetadata.getId());
                        fileDTO.setName(fileMetadata.getName());
                        fileDTO.setLastTimeViewed(formatToISO8601(fileMetadata.getServerModified()));
                        fileDTO.setSize(fileMetadata.getSize());
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
                if (metadata instanceof DeletedMetadata) {
                    DeletedMetadata deletedMetadata = (DeletedMetadata) metadata;
                    if (deletedMetadata.getName().contains(fileName)) {
                        FileDTO fileDTO = new FileDTO();
                        fileDTO.setId("null");
                        fileDTO.setName(deletedMetadata.getName());
                        fileDTO.setLastTimeViewed("Archivo eliminado");
                        fileDTO.setSize(0L);
                        filesDTOList.add(fileDTO);
                    }
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
                if (metadata instanceof FolderMetadata) {
                    FolderMetadata folderMetadata = (FolderMetadata) metadata;
                    if (folderMetadata.getName().toLowerCase().contains(folderName.toLowerCase())) {
                        FileDTO fileDTO = new FileDTO();
                        fileDTO.setId(folderMetadata.getId());
                        fileDTO.setName(folderMetadata.getName());
                        fileDTO.setLastTimeViewed("");
                        fileDTO.setSize(0L);
                        foldersDTOList.add(fileDTO);
                    }
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
                if (metadata instanceof FileMetadata) {
                    FileMetadata fileMetadata = (FileMetadata) metadata;
                    if (fileMetadata.getName().toLowerCase().contains(fileName.toLowerCase())) {
                        FileDTO fileDTO = new FileDTO();
                        fileDTO.setId(fileMetadata.getId());
                        fileDTO.setName(fileMetadata.getName());
                        fileDTO.setLastTimeViewed(formatToISO8601(fileMetadata.getServerModified()));
                        fileDTO.setSize(fileMetadata.getSize());
                        filesDTOList.add(fileDTO);
                    }
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

    public String getPreviewLink(String fileId) throws Exception {
        if (fileId.equals("null"))
            throw new CloudLimitationException("No se puede obtener la preview del archivo indicado");

        // Obtener metadata del archivo
        Metadata metadata = client.files().getMetadata(fileId);
        return "https://www.dropbox.com/home" + metadata.getPathDisplay();
    }

    public byte[] downloadFile(String fileId) throws Exception {
        // Crear un OutputStream para almacenar el contenido del archivo
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Descargar el archivo de Dropbox
        FileMetadata metadata = client.files().downloadBuilder(fileId)
                .download(outputStream);

        // Obtener el contenido del archivo como un array de bytes
        byte[] fileContent = outputStream.toByteArray();

        return fileContent;
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
