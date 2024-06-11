package com.mydrive.backend.controllers;

import com.mydrive.backend.dtos.FileDTO;
import com.mydrive.backend.services.CloudStorageServiceFactory;
import com.mydrive.backend.services.CloudStorageService;
import com.mydrive.backend.services.MultiCloudService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RestController
@Scope(WebApplicationContext.SCOPE_REQUEST)
@RequestMapping("/api/{provider}")
public class CloudStorageController {

    @Autowired
    private CloudStorageServiceFactory factory;

    @Autowired
    private MultiCloudService multiCloudService;

    CloudStorageService cloudService = null;

    private static final Logger logger = LoggerFactory.getLogger(CloudStorageService.class);

    @ModelAttribute
    private void initCloudService(@PathVariable("provider") String provider) {
        cloudService = factory.getCloudService(provider);
    }

    @GetMapping("/oauth/authorize")
    public ModelAndView handleAuthorization() throws Exception {
        String authorizeUrl = cloudService.redirectToAuthorization();
        return new ModelAndView(new RedirectView(authorizeUrl));
    }

    @GetMapping("/oauth/callback")
    public ModelAndView handleAuthorizationCallback(@RequestParam("code") String code) throws Exception {
        cloudService.authenticateUser(code);
        return new ModelAndView(new RedirectView("http://localhost:8081/filemanager/root"));
    }

    @GetMapping("/oauth/check")
    public ResponseEntity<Boolean> checkAuthentication() throws Exception {
        return ResponseEntity.ok(cloudService.checkAuthentication());
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() throws Exception {
        cloudService.logout();
        return ResponseEntity.ok("Logout successfully");
    }

    @GetMapping("/profilePhoto")
    public ResponseEntity<String> getProfilePhoto() throws Exception {
        String profilePhotoUrl = cloudService.getProfilePhoto();
        return ResponseEntity.ok(profilePhotoUrl);
    }

    @GetMapping("/userName")
    public ResponseEntity<String> getUserName() throws Exception {
        String userName = cloudService.getUserName();
        return ResponseEntity.ok(userName);
    }

    @GetMapping("/path/{folderId}")
    public ResponseEntity<List<FileDTO>> getPathFolder(@PathVariable String folderId) throws Exception {
        List<FileDTO> fullPath = cloudService.getPathFolder(folderId);
        return ResponseEntity.ok(fullPath);
    }

    @GetMapping("/folders/{folderId}")
    public ResponseEntity<List<FileDTO>> getFoldersInFolder(@PathVariable String folderId,
                                                            @RequestParam(required = false, defaultValue = "") String q) throws Exception {
        List<FileDTO> folders = cloudService.getFoldersInFolder(folderId, q);
        return ResponseEntity.ok(folders);
    }

    @GetMapping("/files/{fileId}")
    public ResponseEntity<List<FileDTO>> getFilesInFolder(@PathVariable String fileId,
                                                          @RequestParam(required = false, defaultValue = "") String q) throws Exception {
        List<FileDTO> files = cloudService.getFilesInFolder(fileId, q);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/recentFiles")
    public ResponseEntity<List<FileDTO>> getRecentFiles(@RequestParam String maxDate,
                                                        @RequestParam(required = false, defaultValue = "") String q) throws Exception {
        List<FileDTO> files = cloudService.getRecentFiles(maxDate, q);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/files/bin")
    public ResponseEntity<List<FileDTO>> getFilesInBin(@RequestParam(required = false, defaultValue = "") String q) throws Exception {
        List<FileDTO> files = cloudService.getFilesInBin(q);
        return ResponseEntity.ok(files);
    }

    @GetMapping(value = {"/searchFolder/{folderName}"})
    public ResponseEntity<List<FileDTO>> searchFolders(@PathVariable String folderName) throws Exception {
        List<FileDTO> folders = cloudService.searchFolders(folderName);
        return ResponseEntity.ok(folders);
    }

    @GetMapping(value = {"/searchFile/{fileName}"})
    public ResponseEntity<List<FileDTO>> searchFile(@PathVariable String fileName) throws Exception {
        List<FileDTO> files = cloudService.searchFiles(fileName);
        return ResponseEntity.ok(files);
    }

    @PostMapping("/createFolder/{folderId}")
    public ResponseEntity<String> createFolder(@PathVariable String folderId,
                                               @RequestParam String name) throws Exception {
        cloudService.createFolder(folderId, name);
        return ResponseEntity.ok("Folder has been created successfully");
    }

    @PostMapping("/uploadFile/{folderId}")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @PathVariable String folderId) throws Exception {
        cloudService.uploadFile(file, folderId);
        return ResponseEntity.ok("File has been upload successfully");
    }

    @PostMapping("/uploadEncryptedFile/{folderId}")
    public ResponseEntity<String> uploadEncryptedFile(@RequestParam("file") MultipartFile file,
                                                      @RequestParam("password") String password,
                                                      @PathVariable String folderId) throws Exception {
        cloudService.uploadEncryptedFile(file, password, folderId);
        return ResponseEntity.ok("File has been upload successfully");
    }

    @GetMapping("/preview-link/{fileId}")
    public ResponseEntity<String> getPreviewUrl(@PathVariable String fileId) throws Exception {
        return ResponseEntity.ok(cloudService.getPreviewLink(fileId));
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileId) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        byte[] fileContent = cloudService.downloadFile(fileId);

        return ResponseEntity.ok().headers(headers).body(fileContent);
    }

    @PostMapping("/downloadEncryptedFile/{fileId}")
    public ResponseEntity<byte[]> downloadEncryptedFile(@PathVariable String fileId,
                                                        @RequestParam("password") String password) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        byte[] fileContent = cloudService.downloadEncryptedFile(fileId, password);

        return ResponseEntity.ok().headers(headers).body(fileContent);
    }

    @PutMapping("/moveFile/{fileId}")
    public ResponseEntity<String> moveFile(@PathVariable String fileId,
                                           @RequestParam String folderId) throws Exception {
        cloudService.moveFile(fileId, folderId);
        return ResponseEntity.ok("File has been moved successfully");
    }

    @PutMapping("/renameFile/{fileId}")
    public ResponseEntity<String> renameFile(@PathVariable String fileId,
                                             @RequestParam String name) throws Exception {
        cloudService.renameFile(fileId, name);
        return ResponseEntity.ok("File has been renamed successfully");
    }

    @PutMapping("/throwAway/{fileId}")
    public ResponseEntity<String> throwAwayFile(@PathVariable String fileId) throws Exception {
        cloudService.throwAwayFile(fileId);
        return ResponseEntity.ok("File has been thrown into the trash successfully");
    }

    @PutMapping("/restore/{fileId}")
    public ResponseEntity<String> restoreFile(@PathVariable String fileId) throws Exception {
        cloudService.restoreFile(fileId);
        return ResponseEntity.ok("File has been restored successfully");
    }

    @DeleteMapping("/delete/{fileId}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileId) throws Exception {
        cloudService.deleteFile(fileId);
        return ResponseEntity.ok("File has been deleted successfully");
    }

    @PostMapping("/moveFile/{fileId}/{destinationProvider}/{destinationFolderId}")
    public ResponseEntity<String> moveFile(@PathVariable("provider") String provider,
                                           @PathVariable String fileId,
                                           @PathVariable String destinationProvider,
                                           @PathVariable String destinationFolderId) throws Exception {
        logger.info("Mover entre nubes");
        multiCloudService.moveFileBetweenClouds(provider, fileId, destinationProvider, destinationFolderId);
        return ResponseEntity.ok("File has been moved successfully");
    }
}
