package com.mydrive.backend.services;

import com.mydrive.backend.dtos.FileDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CloudStorageService {
    String redirectToAuthorization() throws Exception;

    void authenticateUser(String code) throws Exception;

    ResponseEntity<Boolean> checkAuthentication() throws Exception;

    ResponseEntity<String> logout() throws Exception;

    String getProfilePhoto() throws Exception;

    String getUserName() throws Exception;

    List<FileDTO> getFoldersInFolder(String folderId, String folderName) throws Exception;

    List<FileDTO> getFilesInFolder(String folderId, String fileName) throws Exception;

    List<FileDTO> getRecentFiles(String maxDate, String fileName) throws Exception;

    List<FileDTO> getFilesInBin(String fileName) throws Exception;

    List<FileDTO> searchFolders(String folderName) throws Exception;

    List<FileDTO> searchFiles(String fileName) throws Exception;

    ResponseEntity<String> createFolder(String folderId, String folderName) throws Exception;

    ResponseEntity<String> moveFile(String fileId, String targetFolderId) throws Exception;

    ResponseEntity<String> renameFile(String fileId, String newName) throws Exception;

    ResponseEntity<String> uploadFile(MultipartFile file, String folderId) throws Exception;

    ResponseEntity<byte[]> downloadFile(String fileId) throws Exception;

    ResponseEntity<String> throwAwayFile(String fileId) throws Exception;

    ResponseEntity<String> restoreFile(String fileId) throws Exception;

    ResponseEntity<String> deleteFile(String fileId) throws Exception;

    ResponseEntity<String> getPreviewLink(String fileId) throws Exception;

    List<FileDTO> getPathFolder(String folderId) throws Exception;
}
