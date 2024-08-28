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

/**
 * Pruebas de integración para los endpoints de almacenamiento en la nube.
 *
 * <p>Esta clase realiza pruebas de integración para los servicios de Google Drive y Dropbox a través de los endpoints
 * expuestos en la API REST. Verifica el comportamiento correcto de los controladores y maneja respuestas tanto exitosas
 * como fallidas.</p>
 */
@SpringBootTest
@AutoConfigureMockMvc
public class CloudStorageIntegrationTests {

    /**
     * MockMvc es utilizado para realizar peticiones HTTP y verificar las respuestas.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Mock del servicio de Google Drive.
     *
     * <p>Utilizado para simular el comportamiento del servicio de Google Drive en las pruebas.</p>
     */
    @MockBean
    private GoogleDriveService googleDriveService;

    /**
     * Mock del servicio de Dropbox.
     *
     * <p>Utilizado para simular el comportamiento del servicio de Dropbox en las pruebas.</p>
     */
    @MockBean
    private DropboxService dropboxService;

    /**
     * Prueba de integración para obtener carpetas desde Google Drive.
     *
     * <p>Configura datos simulados para las carpetas en Google Drive y verifica que la respuesta de la API es correcta,
     * incluyendo el estado HTTP y el contenido JSON.</p>
     */
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

    /**
     * Prueba de integración para manejar una excepción al obtener archivos desde Google Drive.
     *
     * <p>Simula una excepción generada al intentar obtener archivos desde Google Drive y verifica que la respuesta de la API
     * sea un error interno del servidor con el mensaje adecuado.</p>
     */
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

    /**
     * Prueba de integración para obtener carpetas desde Dropbox.
     *
     * <p>Configura datos simulados para las carpetas en Dropbox y verifica que la respuesta de la API es correcta,
     * incluyendo el estado HTTP y el contenido JSON.</p>
     */
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

    /**
     * Prueba de integración para manejar una excepción al intentar eliminar un archivo desde Dropbox.
     *
     * <p>Simula una excepción generada al intentar eliminar un archivo desde Dropbox y verifica que la respuesta de la API
     * sea un conflicto con el mensaje adecuado.</p>
     */
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
