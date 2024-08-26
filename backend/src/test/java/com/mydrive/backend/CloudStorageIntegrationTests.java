package com.mydrive.backend;

import com.mydrive.backend.dtos.FileDTO;
import com.mydrive.backend.exceptions.CloudLimitationException;
import com.mydrive.backend.services.DropboxService;
import com.mydrive.backend.services.GoogleDriveService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CloudStorageIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GoogleDriveService googleDriveService;

    @MockBean
    private DropboxService dropboxService;

    @Test
    void testGetFoldersFromGoogleDrive() throws Exception {
        // Configurar datos simulados
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        List<FileDTO> mockFolders = List.of(
                new FileDTO("1", "folder1", "thumbnailLink1",
                        sdf.format(new Date()), 0L, false),
                new FileDTO("2", "folder2", "thumbnailLink2",
                        sdf.format(new Date()), 0L, false)
        );

        when(googleDriveService.getFoldersInFolder(anyString(), anyString())).thenReturn(mockFolders);

        // Ejecutar la solicitud y verificar la respuesta
        mockMvc.perform(get("/api/google-drive/folders/root"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("folder1"))
                .andExpect(jsonPath("$[1].name").value("folder2"));
    }

    @Test
    void testGetFilesFromGoogleDriveThrowsException() throws Exception {
        // Configurar el comportamiento simulado para lanzar una excepción genérica
        doThrow(new RuntimeException("Error interno del servidor"))
                .when(googleDriveService).getFilesInFolder(anyString(), anyString());

        // Ejecutar la solicitud GET y verificar la respuesta
        mockMvc.perform(get("/api/google-drive/files/root"))
                .andExpect(status().isInternalServerError())  // Verifica que el estado HTTP es 500 INTERNAL SERVER ERROR
                .andExpect(MockMvcResultMatchers.content().string("Error interno del servidor"));
    }

    @Test
    void testGetFoldersFromDropbox() throws Exception {
        // Configurar datos simulados
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        List<FileDTO> mockFolders = List.of(
                new FileDTO("1", "folder1", "thumbnailLink1",
                        sdf.format(new Date()), 0L, false),
                new FileDTO("2", "folder2", "thumbnailLink2",
                        sdf.format(new Date()), 0L, false)
        );

        when(dropboxService.getFoldersInFolder(anyString(), anyString())).thenReturn(mockFolders);

        // Ejecutar la solicitud y verificar la respuesta
        mockMvc.perform(get("/api/dropbox/folders/root"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("folder1"))
                .andExpect(jsonPath("$[1].name").value("folder2"));
    }

    @Test
    void testDeleteFileFromDropboxThrowsCloudLimitationException() throws Exception {
        // Configurar el comportamiento simulado para lanzar la excepción
        doThrow(new CloudLimitationException("No se puede eliminar el archivo debido a limitaciones en la nube"))
                .when(dropboxService).deleteFile(anyString());

        // Ejecutar la solicitud DELETE y verificar la respuesta
        mockMvc.perform(delete("/api/dropbox/delete/{fileId}", "someFileId"))
                .andExpect(status().isConflict())  // Verifica que el estado HTTP es 409 CONFLICT
                .andExpect(MockMvcResultMatchers.content().string("No se puede eliminar " +
                        "el archivo debido a limitaciones en la nube"));
    }
}
