package com.mydrive.backend.controllers;

import com.mydrive.backend.dtos.FileDTO;
import com.mydrive.backend.services.DropboxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RestController
@Scope(WebApplicationContext.SCOPE_SESSION)
@RequestMapping("/api/dropbox")
public class DropboxRestController {

    @Autowired
    private DropboxService dropboxService;

    private static final Logger logger = LoggerFactory.getLogger(DropboxRestController.class);

    @GetMapping("/oauth/authorize")
    public ModelAndView handleAuthorization() {
        String authorizeUrl = dropboxService.redirectToAuthorization();
        return new ModelAndView(new RedirectView(authorizeUrl));
    }

    @GetMapping("/oauth/callback")
    public ModelAndView handleAuthorizationCallback(@RequestParam("code") String code) throws Exception {
        dropboxService.authenticateUser(code);
        return new ModelAndView(new RedirectView("http://localhost:8081/filemanager/root"));
    }

    @GetMapping("/oauth/check")
    public ResponseEntity<Boolean> checkAuthentication() {
        return dropboxService.checkAuthentication();
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return dropboxService.logout();
    }

    @GetMapping("/profilePhoto")
    public ResponseEntity<String> getProfilePhoto() throws Exception {
        String profilePhotoUrl = dropboxService.getProfilePhoto();
        return ResponseEntity.ok(profilePhotoUrl);
    }

    @GetMapping("/userName")
    public ResponseEntity<String> getUserName() throws Exception {
        String userName = dropboxService.getUserName();
        return ResponseEntity.ok(userName);
    }

    @GetMapping("/folders/{folderId}")
    public ResponseEntity<List<FileDTO>> getFoldersInFolder(@PathVariable String folderId,
                                                            @RequestParam(required = false, defaultValue = "") String q) throws Exception {
        List<FileDTO> folders = dropboxService.getFoldersInFolder(folderId, q);
        return ResponseEntity.ok(folders);
    }

    @GetMapping("/files/{fileId}")
    public ResponseEntity<List<FileDTO>> getFilesInFolder(@PathVariable String fileId,
                                                          @RequestParam(required = false, defaultValue = "") String q) throws Exception {
        List<FileDTO> files = dropboxService.getFilesInFolder(fileId, q);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/recentFiles")
    public ResponseEntity<List<FileDTO>> getRecentFiles(@RequestParam String maxDate,
                                                        @RequestParam(required = false, defaultValue = "") String q) throws Exception {
        List<FileDTO> files = dropboxService.getRecentFiles(maxDate, q);
        return ResponseEntity.ok(files);
    }

    @GetMapping(value = {"/searchFolder/{folderName}"})
    public ResponseEntity<List<FileDTO>> searchFolders(@PathVariable String folderName) throws Exception {
        List<FileDTO> folders = dropboxService.searchFolders(folderName);
        return ResponseEntity.ok(folders);
    }

    @GetMapping(value = {"/searchFile/{fileName}"})
    public ResponseEntity<List<FileDTO>> searchFile(@PathVariable String fileName) throws Exception {
        List<FileDTO> files = dropboxService.searchFiles(fileName);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/path/{folderId}")
    public ResponseEntity<List<FileDTO>> getPathFolder(@PathVariable String folderId) throws Exception {
        List<FileDTO> fullPath = dropboxService.getPathFolder(folderId);
        return ResponseEntity.ok(fullPath);
    }

    @PostMapping("/createFolder/{folderId}")
    public ResponseEntity<String> createFolder(@PathVariable String folderId, @RequestParam String name) throws Exception {
        return dropboxService.createFolder(folderId, name);
    }

    @PutMapping("/moveFile/{fileId}")
    public ResponseEntity<String> moveFile(@PathVariable String fileId, @RequestParam String folderId) throws Exception {
        return dropboxService.moveFile(fileId, folderId);
    }

    @PutMapping("/renameFile/{fileId}")
    public ResponseEntity<String> renameFile(@PathVariable String fileId, @RequestParam String name) throws Exception {
        return dropboxService.renameFile(fileId, name);
    }

    @PostMapping("/uploadFile/{folderId}")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @PathVariable String folderId) throws Exception {
        return dropboxService.uploadFile(file, folderId);
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileId) throws Exception {
        return dropboxService.downloadFile(fileId);
    }

    @PutMapping("/throwAway/{fileId}")
    public ResponseEntity<String> throwAwayFile(@PathVariable String fileId) throws Exception {
        return dropboxService.throwAwayFile(fileId);
    }

    @DeleteMapping("/delete/{fileId}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileId) throws Exception {
        return dropboxService.throwAwayFile(fileId);
    }

    @GetMapping("/files/bin")
    public ResponseEntity<List<FileDTO>> getFilesInBin(@RequestParam(required = false, defaultValue = "") String q) throws Exception {
        List<FileDTO> files = dropboxService.getFilesInBin(q);
        return ResponseEntity.ok(files);
    }

    @PutMapping("/restore/{fileId}")
    public ResponseEntity<String> restoreFile(@PathVariable String fileId) throws Exception {
        return dropboxService.restoreFile(fileId);
    }

    @GetMapping("/preview-link/{fileId}")
    public ResponseEntity<String> getPreviewUrl(@PathVariable String fileId) throws Exception {
        return dropboxService.getPreviewLink(fileId);
    }
}
