package com.mydrive.backend;

import com.mydrive.backend.dtos.FileDTO;
import com.mydrive.backend.services.DropboxService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DropboxServiceTests {

    @MockBean
    private DropboxService dropboxService;

    @Test
    void testGetFoldersSuccess() throws Exception {
        // Crear objetos FileDTO simulados
        FileDTO folder1 = new FileDTO("1", "folder1", "thumbnailLink1",
                "lastTimeViewed1", 0L, false);
        FileDTO folder2 = new FileDTO("2", "folder2", "thumbnailLink2",
                "lastTimeViewed2", 0L, false);

        // Configurar el mock para que devuelva la lista de FileDTO simulados
        when(dropboxService.getFoldersInFolder("root", ""))
                .thenReturn(Arrays.asList(folder1, folder2));

        // Llama al método y verifica el resultado
        List<FileDTO> result = dropboxService.getFoldersInFolder("root", "");
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        assertEquals("folder1", result.get(0).getName());
        assertEquals("folder2", result.get(1).getName());
    }

    @Test
    void testGetFoldersFailure() throws Exception {
        // Configurar el mock para que lance una excepción
        when(dropboxService.getFoldersInFolder("root", ""))
                .thenThrow(new RuntimeException("API Error"));

        // Verifica que el método maneje la excepción correctamente
        Exception exception = assertThrows(RuntimeException.class, () -> {
            dropboxService.getFoldersInFolder("root", "");
        });
        assertEquals("API Error", exception.getMessage());
    }
}
