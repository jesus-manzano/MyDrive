package com.mydrive.backend.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mensajes")
public class GoogleDriveRestController {

    @GetMapping("/hola")
    public String hola() {
        return "Hola mundo";
    }
}
