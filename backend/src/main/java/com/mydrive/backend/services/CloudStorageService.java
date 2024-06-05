package com.mydrive.backend.services;

import com.mydrive.backend.dtos.FileDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CloudStorageService {
    String redirectToAuthorization() throws Exception;

    void authenticateUser(String code) throws Exception;

    boolean checkAuthentication() throws Exception;

    void logout() throws Exception;

    String getProfilePhoto() throws Exception;

    String getUserName() throws Exception;

    List<FileDTO> getPathFolder(String folderId) throws Exception;

    List<FileDTO> getFoldersInFolder(String folderId, String folderName) throws Exception;

    List<FileDTO> getFilesInFolder(String folderId, String fileName) throws Exception;

    List<FileDTO> getRecentFiles(String maxDate, String fileName) throws Exception;

    List<FileDTO> getFilesInBin(String fileName) throws Exception;

    List<FileDTO> searchFolders(String folderName) throws Exception;

    List<FileDTO> searchFiles(String fileName) throws Exception;

    void createFolder(String folderId, String folderName) throws Exception;

    void uploadFile(MultipartFile file, String folderId) throws Exception;

    String getPreviewLink(String fileId) throws Exception;

    byte[] downloadFile(String fileId) throws Exception;

    void moveFile(String fileId, String targetFolderId) throws Exception;

    void renameFile(String fileId, String newName) throws Exception;

    void throwAwayFile(String fileId) throws Exception;

    void restoreFile(String fileId) throws Exception;

    void deleteFile(String fileId) throws Exception;
}
