package com.mydrive.backend.services;

import com.google.api.client.googleapis.auth.oauth2.*;
import com.mydrive.backend.dtos.FileDTO;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.model.About;
import com.google.api.services.drive.model.FileList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import com.google.api.services.drive.Drive;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.DriveScopes;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import com.google.api.services.drive.model.File;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service("google-drive")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class GoogleDriveService implements CloudStorageService {

    @Value("${google.oauth.redirectUri}")
    private String redirectUri;

    // Credenciales para usar la API de Google Drive
    @Value("${google.secret.key.path}")
    private Resource gdSecretKeys;

    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);

    private GoogleAuthorizationCodeFlow flow = null; // Flujo de autorización
    private Drive drive = null; // Servicio para hacer las peticiones

    private static final Logger logger = LoggerFactory.getLogger(GoogleDriveService.class);

    @PostConstruct
    public void init() throws Exception {
        GoogleClientSecrets secrets = GoogleClientSecrets.load(JSON_FACTORY,
                new InputStreamReader(gdSecretKeys.getInputStream()));
        flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, secrets, SCOPES)
                .build();
    }

    public String redirectToAuthorization() {
        GoogleAuthorizationCodeRequestUrl url = flow.newAuthorizationUrl();
        String redirectUrl = url.setRedirectUri(redirectUri).setAccessType("offline").build();
        return redirectUrl; // Redirect to Google Drive authorization URL
    }

    public void authenticateUser(String code) throws Exception {
        GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectUri).execute();
        Credential cred = flow.createAndStoreCredential(response, null);

        drive = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, cred)
                .setApplicationName("MyDrive").build();
    }

    public boolean checkAuthentication() {
        return drive != null;
    }

    public void logout() {
        this.drive = null;
    }

    public String getProfilePhoto() throws Exception {
        // Obtener información del usuario
        About about = drive.about().get().setFields("user").execute();

        return about.getUser().getPhotoLink();
    }

    public String getUserName() throws Exception {
        // Obtener información del usuario
        About about = drive.about().get().setFields("user").execute();

        return about.getUser().getDisplayName();
    }

    public List<FileDTO> getPathFolder(String folderId) throws Exception {
        // Obtener la información del archivo con el ID del folder
        File file = drive.files().get(folderId).setFields("id, name, parents").execute();
        List<FileDTO> fullPath = new ArrayList<>();

        // Agregamos el directorio actual
        FileDTO currentFolder = new FileDTO();
        currentFolder.setId(folderId);
        currentFolder.setName(file.getName());
        fullPath.add(0, currentFolder);

        // Obtener la lista de padres del archivo
        List<String> parents = file.getParents();

        // Verificar si hay padres
        if (parents != null && !parents.isEmpty()) {
            String parentId = parents.get(0);

            while (parentId != null) {
                // Obtener la información del archivo padre
                File parentFile = drive.files().get(parentId).setFields("id, name, parents").execute();

                // Agregar el directorio del padre a la lista
                FileDTO parentFolder = new FileDTO();
                parentFolder.setId(parentId);
                parentFolder.setName(parentFile.getName());
                fullPath.add(0, parentFolder);

                // Obtener el ID del siguiente padre
                List<String> nextParents = parentFile.getParents();
                if (nextParents != null && !nextParents.isEmpty()) {
                    parentId = nextParents.get(0);
                } else {
                    parentId = null;
                }
            }
        }

        return fullPath;
    }

    public List<FileDTO> getFoldersInFolder(String folderId, String folderName) throws Exception {

        String query = "'" + folderId
                + "' in parents and trashed=false and mimeType='application/vnd.google-apps.folder'";

        // Agregar filtro por nombre de carpeta si se proporciona
        if (!folderName.isEmpty()) {
            query += " and name contains '" + folderName + "'";
        }
        // Realizamos la consulta para obtener todas las carpetas en esta carpeta
        FileList allFolders = drive.files().list().setQ(query)
                .setFields("files(id,name,thumbnailLink,mimeType,viewedByMeTime,modifiedByMeTime,createdTime,size)")
                .execute();

        List<FileDTO> foldersDTOList = new ArrayList<>();

        // Modificamos los parámetros que deseemos antes de convertirlo en DTO
        for (File folder : allFolders.getFiles()) {
            folder.setSize(0L);
            foldersDTOList.add(convertToFileDTO(folder));
        }

        return foldersDTOList;
    }

    public List<FileDTO> getFilesInFolder(String folderId, String fileName) throws Exception {
        String query = "'" + folderId
                + "' in parents and trashed=false and mimeType != 'application/vnd.google-apps.folder' "
                + "and mimeType != 'application/vnd.google-apps.shortcut'";

        // Agregar filtro por nombre de archivo si se proporciona
        if (!fileName.isEmpty()) {
            query += " and name contains '" + fileName + "'";
        }
        // Realizamos la consulta para obtener todos los archivos
        // que no son ni carpetas ni enlaces a otros archivos
        FileList allFiles = drive.files().list().setQ(query)
                .setFields("files(id,name,thumbnailLink,mimeType,viewedByMeTime,modifiedByMeTime,createdTime,size)")
                .execute();

        List<FileDTO> filesDTOList = new ArrayList<>();

        // Modificamos los parámetros que deseemos antes de convertirlo en DTO
        for (File file : allFiles.getFiles()) {
            filesDTOList.add(convertToFileDTO(file));
        }

        return filesDTOList;
    }

    public List<FileDTO> getRecentFiles(String maxDate, String fileName) throws Exception {
        // Convertir la cadena de texto a un objeto Date
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date parsedDate = formatter.parse(maxDate);
        String formattedDate = formatter.format(parsedDate);

        String query = "trashed=false "
                + "and mimeType != 'application/vnd.google-apps.folder' "
                + "and mimeType != 'application/vnd.google-apps.shortcut' "
                + "and viewedByMeTime > '" + formattedDate + "'";

        // Agregar filtro por nombre de archivo si se proporciona
        if (!fileName.isEmpty()) {
            query += " and name contains '" + fileName + "'";
        }

        // Realizamos la consulta para obtener todos los archivos
        // que no son ni carpetas ni enlaces a otros archivos
        FileList allFiles = drive.files().list().setQ(query)
                .setFields("files(id,name,thumbnailLink,mimeType,viewedByMeTime,modifiedByMeTime,createdTime,size)")
                .execute();

        List<FileDTO> filesDTOList = new ArrayList<>();

        // Modificamos los parámetros que deseemos antes de convertirlo en DTO
        for (File file : allFiles.getFiles()) {
            filesDTOList.add(convertToFileDTO(file));
        }

        return filesDTOList;
    }

    public List<FileDTO> getFilesInBin(String fileName) throws Exception {
        String query = "trashed=true and mimeType != 'application/vnd.google-apps.folder' and mimeType != 'application/vnd.google-apps.shortcut'";

        // Agregar filtro por nombre de archivo si se proporciona
        if (!fileName.isEmpty()) {
            query += " and name contains '" + fileName + "'";
        }
        // Realizamos la consulta para obtener todos los archivos
        // que no son ni carpetas ni enlaces a otros archivos
        FileList allFiles = drive.files().list().setQ(query)
                .setFields("files(id,name,thumbnailLink,mimeType,viewedByMeTime,modifiedByMeTime,createdTime,size)")
                .execute();

        List<FileDTO> filesDTOList = new ArrayList<>();

        // Modificamos los parámetros que deseemos antes de convertirlo en DTO
        for (File file : allFiles.getFiles()) {
            filesDTOList.add(convertToFileDTO(file));
        }

        return filesDTOList;
    }

    public List<FileDTO> searchFolders(String folderName) throws Exception {
        // Realizamos la consulta para obtener todas las carpetas
        // que coinciden con este nombre
        FileList allFiles = drive.files().list()
                .setQ("trashed=false and name contains '"
                        + folderName
                        + "' and mimeType='application/vnd.google-apps.folder'")
                .setFields("files(id,name,thumbnailLink,mimeType,viewedByMeTime,modifiedByMeTime,createdTime,size)")
                .execute();

        List<FileDTO> foldersDTOList = new ArrayList<>();

        // Modificamos los parámetros que deseemos antes de convertirlo en DTO
        for (File folder : allFiles.getFiles()) {
            folder.setSize(0L);
            foldersDTOList.add(convertToFileDTO(folder));
        }

        return foldersDTOList;
    }

    public List<FileDTO> searchFiles(String fileName) throws Exception {
        // Realizamos la consulta para obtener todos los archivos
        // que coinciden con este nombre
        FileList allFiles = drive.files().list()
                .setQ("trashed=false and name contains '"
                        + fileName
                        + "' and mimeType != 'application/vnd.google-apps.folder' "
                        + "and mimeType != 'application/vnd.google-apps.shortcut'")
                .setFields("files(id,name,thumbnailLink,mimeType,viewedByMeTime,modifiedByMeTime,createdTime,size)")
                .execute();

        List<FileDTO> filesDTOList = new ArrayList<>();

        // Modificamos los parámetros que deseemos antes de convertirlo en DTO
        for (File file : allFiles.getFiles()) {
            filesDTOList.add(convertToFileDTO(file));
        }

        return filesDTOList;
    }

    public void createFolder(String folderId, String folderName) throws Exception {
        File folder = new File();
        folder.setName(folderName);
        folder.setMimeType("application/vnd.google-apps.folder");
        folder.setParents(Collections.singletonList(folderId));

        drive.files().create(folder).execute();
    }

    public void uploadFile(MultipartFile file, String folderId) throws Exception {
        if (file.isEmpty()) {
            throw new Exception("File is empty");
        }

        // Crear el metadata del archivo
        File fileMetadata = new File();
        fileMetadata.setName(file.getOriginalFilename());
        if (folderId != null) fileMetadata.setParents(Collections.singletonList(folderId));

        // Subir el archivo a Google Drive
        drive.files().create(fileMetadata, new InputStreamContent(file.getContentType(),
                new ByteArrayInputStream(file.getBytes()))).setFields("id").execute();
    }

    public String getPreviewLink(String fileId) {
        return "https://drive.google.com/file/d/" + fileId + "/view";
    }

    public byte[] downloadFile(String fileId) throws Exception {
        OutputStream outputStream = new ByteArrayOutputStream();
        drive.files().get(fileId).executeMediaAndDownloadTo(outputStream);

        byte[] fileContent = ((ByteArrayOutputStream) outputStream).toByteArray();

        return fileContent;
    }

    public void moveFile(String fileId, String targetFolderId) throws Exception {
        // Obtener la información del archivo para obtener el padre actual
        File file = drive.files().get(fileId)
                .setFields("parents")
                .execute();

        // Obtener los ID de los padres del archivo
        List<String> parents = file.getParents();
        if (parents == null || parents.isEmpty()) {
            throw new Exception("error moving file");
        }

        // Suponemos que el archivo solo tiene un padre, tomamos el primer elemento de la lista
        String sourceFolderId = parents.get(0);

        // Ejecutar la solicitud de actualización para mover el archivo
        drive.files().update(fileId, null)
                .setRemoveParents(sourceFolderId)
                .setAddParents(targetFolderId)
                .execute();
    }

    public void renameFile(String fileId, String newName) throws Exception {
        File file = new File();
        file.setName(newName);

        drive.files().update(fileId, file).execute();
    }

    public void throwAwayFile(String fileId) throws Exception {
        // Obtener el archivo para modificarlo y enviarlo a la papelera
        File file = new File();
        file.setTrashed(true);

        // Enviar solicitud para actualizar el archivo y enviarlo a la papelera
        drive.files().update(fileId, file).execute();
    }

    public void restoreFile(String fileId) throws Exception {
        // Obtener el archivo para modificarlo y restaurarlo de la papelera
        File file = new File();
        file.setTrashed(false);

        // Enviar solicitud para actualizar el archivo y restaurarlo de la papelera
        drive.files().update(fileId, file).execute();
    }

    public void deleteFile(String fileId) throws Exception {
        // Enviar solicitud para eliminar el archivo
        drive.files().delete(fileId).execute();
    }

    private String mostRecentLastTimeViewed(File file) {
        if (file.getViewedByMeTime() != null)
            return file.getViewedByMeTime().toString();
        if (file.getModifiedByMeTime() != null)
            file.getModifiedByMeTime().toString();
        return file.getCreatedTime().toString();
    }

    private FileDTO convertToFileDTO(File file) {
        FileDTO fileDTO = new FileDTO(
                file.getId(),
                file.getName(),
                file.getThumbnailLink(),
                mostRecentLastTimeViewed(file),
                file.getSize());
        return fileDTO;
    }
}

