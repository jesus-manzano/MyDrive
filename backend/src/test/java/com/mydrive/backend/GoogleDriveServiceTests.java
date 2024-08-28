package com.mydrive.backend;

import com.google.api.client.util.DateTime;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.mydrive.backend.dtos.FileDTO;
import com.mydrive.backend.services.GoogleDriveService;
import com.mydrive.backend.services.utils.FileEncryptionUtil;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Clase de pruebas unitarias para {@link GoogleDriveService}.
 *
 * <p>Esta clase contiene pruebas para los métodos de {@link GoogleDriveService}. Utiliza mocks de la API de Google Drive
 * para simular la interacción con el servicio de almacenamiento en la nube sin necesidad de realizar llamadas reales a la API.</p>
 *
 * <p>La clase está anotada con {@link SpringBootTest} para cargar el contexto de la aplicación Spring Boot y permitir
 * la inyección de dependencias.</p>
 *
 * @see GoogleDriveService
 */
@SpringBootTest
public class GoogleDriveServiceTests {

    /**
     * Mock de la clase {@link Drive} de la API de Google Drive.
     *
     * <p>Simula la interacción con el servicio principal de Google Drive.</p>
     */
    @Mock
    private Drive drive;

    /**
     * Mock de la clase {@link Drive.Files} de la API de Google Drive.
     *
     * <p>Simula el componente que maneja las operaciones relacionadas con archivos en Google Drive.</p>
     */
    @Mock
    private Drive.Files files;

    /**
     * Mock de la clase {@link Drive.Files.List} para listar archivos.
     *
     * <p>Simula la operación de listar archivos dentro de Google Drive.</p>
     */
    @Mock
    private Drive.Files.List list;

    /**
     * Instancia de {@link GoogleDriveService} que se está probando.
     *
     * <p>El servicio que se está probando con los mocks proporcionados.</p>
     */
    @InjectMocks
    private GoogleDriveService googleDriveService;

    /**
     * Prueba unitaria para el método {@link GoogleDriveService#getFoldersInFolder(String, String)} en caso de éxito.
     *
     * <p>Esta prueba verifica que el método puede recuperar correctamente las carpetas desde Google Drive cuando la API responde
     * exitosamente.</p>
     *
     * @throws Exception Si ocurre algún error durante la prueba.
     */
    @Test
    void testGetFoldersSuccess() throws Exception {
        // Datos simulados
        File mockFolder1 = new File();
        mockFolder1.setId("1").setName("folder1").setThumbnailLink("thumbnailLink1")
                .setViewedByMeTime(DateTime.parseRfc3339("2024-08-20T11:00:00")).setSize(0L);
        File mockFolder2 = new File();
        mockFolder2.setId("2").setName("folder2").setThumbnailLink("thumbnailLink2")
                .setViewedByMeTime(DateTime.parseRfc3339("2024-08-20T12:00:00")).setSize(0L);

        List<File> folderList = new ArrayList<>();
        folderList.add(mockFolder1);
        folderList.add(mockFolder2);

        FileList mockFileList = new FileList();
        mockFileList.setFiles(folderList);

        // Configurar mocks
        when(drive.files()).thenReturn(files);
        when(files.list()).thenReturn(list);
        when(list.setQ(anyString())).thenReturn(list);
        when(list.setFields(anyString())).thenReturn(list);
        when(list.execute()).thenReturn(mockFileList);

        // Ejecutar el método
        List<FileDTO> result = googleDriveService.getFoldersInFolder("root", "");

        // Verificaciones
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        assertEquals("folder1", result.get(0).getName());
        assertEquals("folder2", result.get(1).getName());
    }

    /**
     * Prueba unitaria para el método {@link GoogleDriveService#getFoldersInFolder(String, String)} en caso de fallo.
     *
     * <p>Esta prueba verifica que el método maneja correctamente las excepciones cuando la API de Google Drive responde con
     * un error.</p>
     *
     * @throws Exception Si ocurre algún error durante la prueba.
     */
    @Test
    void testGetFoldersFailure() throws Exception {
        // Configurar el mock para que lance una excepción
        when(drive.files()).thenReturn(files);
        when(files.list()).thenReturn(list);
        when(list.setQ(anyString())).thenReturn(list);
        when(list.setFields(anyString())).thenReturn(list);
        when(list.execute()).thenThrow(new RuntimeException("API Error"));

        // Verifica que el método maneje la excepción correctamente
        Exception exception = assertThrows(RuntimeException.class, () ->
                googleDriveService.getFoldersInFolder("root", ""));
        assertEquals("API Error", exception.getMessage());
    }

    /**
     * Prueba unitaria para el método {@link GoogleDriveService#getRecentFiles(String, String)}.
     *
     * <p>Esta prueba verifica que el método puede recuperar correctamente los archivos recientes desde Google Drive basándose
     * en una fecha máxima.</p>
     *
     * @throws Exception Si ocurre algún error durante la prueba.
     */
    @Test
    public void testGetRecentFiles() throws Exception {
        // Datos simulados
        String maxDate = "2024-08-20T10:00:00";
        File mockFile = new File();
        mockFile.setId("1").setName("testFile").setThumbnailLink("thumbnailLink")
                .setViewedByMeTime(DateTime.parseRfc3339("2024-08-18T10:00:00")).setSize(0L);
        FileList mockFileList = new FileList();
        mockFileList.setFiles(Collections.singletonList(mockFile));

        // Configurar mocks
        when(drive.files()).thenReturn(files);
        when(files.list()).thenReturn(list);
        when(list.setQ(anyString())).thenReturn(list);
        when(list.setFields(anyString())).thenReturn(list);
        when(list.execute()).thenReturn(mockFileList);

        // Ejecutar el método
        List<FileDTO> result = googleDriveService.getRecentFiles(maxDate, "");

        // Verificaciones
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("testFile", result.get(0).getName());
    }

    /**
     * Prueba unitaria para el método {@link GoogleDriveService#searchFiles(String)}.
     *
     * <p>Esta prueba verifica que el método puede buscar archivos en Google Drive basándose en un término de búsqueda.</p>
     *
     * @throws Exception Si ocurre algún error durante la prueba.
     */
    @Test
    public void testSearchFiles() throws Exception {
        // Datos simulados
        File mockFile1 = new File();
        mockFile1.setId("1").setName("searchFile1").setThumbnailLink("thumbnailLink1")
                .setViewedByMeTime(DateTime.parseRfc3339("2024-08-20T11:00:00")).setSize(0L);
        File mockFile2 = new File();
        mockFile2.setId("2").setName("searchFile2").setThumbnailLink("thumbnailLink2")
                .setViewedByMeTime(DateTime.parseRfc3339("2024-08-20T12:00:00")).setSize(0L);

        List<File> folderList = new ArrayList<>();
        folderList.add(mockFile1);
        folderList.add(mockFile2);

        FileList mockFileList = new FileList();
        mockFileList.setFiles(folderList);

        // Configurar mocks
        when(drive.files()).thenReturn(files);
        when(files.list()).thenReturn(list);
        when(list.setQ(anyString())).thenReturn(list);
        when(list.setFields(anyString())).thenReturn(list);
        when(list.execute()).thenReturn(mockFileList);

        // Ejecutar el método
        List<FileDTO> result = googleDriveService.searchFiles("searchFile");

        // Verificaciones
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("searchFile1", result.get(0).getName());
        assertEquals("searchFile2", result.get(1).getName());
    }

    /**
     * Prueba unitaria para el método {@link GoogleDriveService#downloadEncryptedFile(String, String)}.
     *
     * <p>Esta prueba verifica que el método puede descargar un archivo cifrado y descifrarlo correctamente.</p>
     *
     * @throws Exception Si ocurre algún error durante la prueba.
     */
    @Test
    public void testDownloadEncryptedFile() throws Exception {
        String fileId = "testFileId";
        String password = "testPassword";
        byte[] fileContent = "testContent".getBytes();  // Contenido esperado

        // Mock el método get y la descarga
        Drive.Files.Get get = mock(Drive.Files.Get.class);
        when(drive.files()).thenReturn(files);
        when(files.get(fileId)).thenReturn(get);
        doAnswer(invocation -> {
            OutputStream outputStream = invocation.getArgument(0);
            outputStream.write(fileContent);  // Escribir contenido simulado
            return null;
        }).when(get).executeMediaAndDownloadTo(any(OutputStream.class));

        // Mock la utilidad de descifrado utilizando mockStatic
        try (MockedStatic<FileEncryptionUtil> mockedEncryptionUtil = mockStatic(FileEncryptionUtil.class)) {
            mockedEncryptionUtil.when(() -> FileEncryptionUtil.decryptFile(any(java.io.File.class),
                            any(java.io.File.class), eq(password)))
                    .thenAnswer(invocation -> {
                        // Simula la escritura del contenido descifrado en el archivo de salida
                        java.io.File inputFile = invocation.getArgument(0);  // Archivo cifrado (temporal)
                        java.io.File outputFile = invocation.getArgument(1);  // Archivo descifrado (temporal)

                        // Simular descifrado copiando contenido del archivo de entrada al de salida
                        try (InputStream fis = new FileInputStream(inputFile);
                             OutputStream fos = new FileOutputStream(outputFile)) {
                            byte[] buffer = new byte[1024];
                            int bytesRead;
                            while ((bytesRead = fis.read(buffer)) != -1) {
                                fos.write(buffer, 0, bytesRead);
                            }
                        }
                        return null;
                    });

            // Ejecutar el método
            byte[] result = googleDriveService.downloadEncryptedFile(fileId, password);

            // Verificaciones
            assertNotNull(result);
            assertArrayEquals(fileContent, result);
        }
    }
}
