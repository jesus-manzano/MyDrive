package com.mydrive.backend.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mydrive.backend.dtos.FileDTO;
import com.mydrive.backend.services.GoogleDriveService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RestController
@RequestMapping("/api/google-drive")
public class GoogleDriveRestController {

    @Autowired
    private GoogleDriveService googleDriveService;

    @GetMapping("/signin")
    public void signIn(HttpServletResponse response) throws Exception {
        googleDriveService.redirectToAuthorization(response);
    }

    @GetMapping("/oauth")
    public ModelAndView handleAuthorizationCallback(HttpServletRequest request) throws Exception {
        String code = request.getParameter("code");
        if (code != null) {
            googleDriveService.saveAuthorizationToken(code);
            return new ModelAndView(new RedirectView("/filemanager/root"));
        }
        return new ModelAndView(new RedirectView("/error"));
    }

    @PostMapping("/createFolder/{folderId}")
    public ResponseEntity<String> createFolder(@PathVariable String folderId, @RequestParam String name) throws Exception {
        return googleDriveService.createFolder(folderId, name);
    }

    @PutMapping("/moveFile/{fileId}")
    public ResponseEntity<String> moveFile(@PathVariable String fileId, @RequestParam String folderId) throws Exception {
        return googleDriveService.moveFile(fileId, folderId);
    }

    @PutMapping("/renameFile/{fileId}")
    public ResponseEntity<String> renameFile(@PathVariable String fileId, @RequestParam String name) throws Exception {
        return googleDriveService.renameFile(fileId, name);
    }

    @PostMapping("/uploadFile/{folderId}")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @PathVariable String folderId) throws Exception {
        return googleDriveService.uploadFile(file, folderId);
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileId) throws Exception {
        return googleDriveService.downloadFile(fileId);
    }

    @PutMapping("/throwAway/{fileId}")
    public ResponseEntity<String> throwAwayFile(@PathVariable String fileId) throws Exception {
        return googleDriveService.throwAwayFile(fileId);
    }

    @PutMapping("/restore/{fileId}")
    public ResponseEntity<String> restoreFile(@PathVariable String fileId) throws Exception {
        return googleDriveService.restoreFile(fileId);
    }

    @DeleteMapping("/delete/{fileId}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileId) throws Exception {
        return googleDriveService.deleteFile(fileId);
    }

    @GetMapping("/folders/{folderId}")
    public ResponseEntity<List<FileDTO>> getFoldersInFolder(@PathVariable String folderId,
                                                            @RequestParam(required = false, defaultValue = "") String q) throws Exception {
        List<FileDTO> folders = googleDriveService.getFoldersInFolder(folderId, q);
        return ResponseEntity.ok(folders);
    }

    @GetMapping("/files/{folderId}")
    public ResponseEntity<List<FileDTO>> getFilesInFolder(@PathVariable String folderId,
                                                          @RequestParam(required = false, defaultValue = "") String q) throws Exception {
        List<FileDTO> files = googleDriveService.getFilesInFolder(folderId, q);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/allFiles")
    public ResponseEntity<List<FileDTO>> getAllFiles() throws Exception {
        List<FileDTO> files = googleDriveService.getAllFiles();
        return ResponseEntity.ok(files);
    }

    @GetMapping(value = {"/searchFolder/{folderName}"})
    public ResponseEntity<List<FileDTO>> searchFolders(@PathVariable String folderName) throws Exception {
        List<FileDTO> folders = googleDriveService.searchFolders(folderName);
        return ResponseEntity.ok(folders);
    }

    @GetMapping(value = {"/searchFile/{fileName}"})
    public ResponseEntity<List<FileDTO>> searchFile(@PathVariable String fileName) throws Exception {
        List<FileDTO> files = googleDriveService.searchFiles(fileName);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/folders/bin")
    public ResponseEntity<List<FileDTO>> getFoldersInFolder(@RequestParam(required = false, defaultValue = "") String q) throws Exception {
        List<FileDTO> folders = googleDriveService.getFoldersInBin(q);
        return ResponseEntity.ok(folders);
    }

    @GetMapping("/files/bin")
    public ResponseEntity<List<FileDTO>> getFilesInFolder(@RequestParam(required = false, defaultValue = "") String q) throws Exception {
        List<FileDTO> files = googleDriveService.getFilesInBin(q);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/path/{folderId}")
    public ResponseEntity<List<FileDTO>> getPathFolder(@PathVariable String folderId) throws Exception {
        List<FileDTO> fullPath = googleDriveService.getPathFolder(folderId);
        return ResponseEntity.ok(fullPath);
    }

    @GetMapping("/profilePhoto")
    public ResponseEntity<String> getProfilePhoto() throws Exception {
        String profilePhotoUrl = googleDriveService.getProfilePhoto();
        return ResponseEntity.ok(profilePhotoUrl);
    }
}
