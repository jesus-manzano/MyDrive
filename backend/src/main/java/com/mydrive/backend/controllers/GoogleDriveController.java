package com.mydrive.backend.controllers;

import com.mydrive.backend.dtos.FileDTO;
import com.mydrive.backend.services.GoogleDriveService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/google-drive")
public class GoogleDriveController {

    @Autowired
    private GoogleDriveService googleDriveService;

    @GetMapping("/signin")
    public void signIn(HttpServletResponse response) throws Exception {
        googleDriveService.redirectToAuthorization(response);
    }

    @GetMapping("/oauth")
    public String handleAuthorizationCallback(HttpServletRequest request) throws Exception {
        String code = request.getParameter("code");
        if (code != null) {
            googleDriveService.saveAuthorizationToken(code);
            return "redirect:/google-drive/listfolder/root";
        }
        return "redirect:/index";
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        return googleDriveService.uploadFile(file);
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileId) throws Exception {
        return googleDriveService.downloadFile(fileId);
    }

    @DeleteMapping("/throwAway/{fileId}")
    public ResponseEntity<String> throwAwayFile(@PathVariable String fileId) throws Exception {
        return googleDriveService.throwAwayFile(fileId);
    }

    @GetMapping(value = {"/listfolder/{folderId}"})
    public String listFolder(Model model, @PathVariable String folderId,
                             @RequestParam(defaultValue = "name") String orderBy) throws Exception {
        String profilePhotoUrl = googleDriveService.getProfilePhoto();
        List<FileDTO> folders = googleDriveService.getFoldersInFolder(folderId, orderBy);
        List<FileDTO> files = googleDriveService.getFilesInFolder(folderId, orderBy);
        List<FileDTO> fullPath = googleDriveService.getPathFolder(folderId);

        model.addAttribute("profilePhotoUrl", profilePhotoUrl);
        model.addAttribute("folders", folders);
        model.addAttribute("files", files);
        model.addAttribute("fullPath", fullPath);

        return "filemanager";
    }

    @GetMapping(value = {"/searchFile/{fileName}"})
    public String searchFile(Model model, @PathVariable String fileName,
                             @RequestParam(defaultValue = "name") String orderBy) throws Exception {
        String profilePhotoUrl = googleDriveService.getProfilePhoto();
        List<FileDTO> files = googleDriveService.searchFiles(fileName, orderBy);

        model.addAttribute("profilePhotoUrl", profilePhotoUrl);
        model.addAttribute("files", files);

        return "filemanager";
    }

}

