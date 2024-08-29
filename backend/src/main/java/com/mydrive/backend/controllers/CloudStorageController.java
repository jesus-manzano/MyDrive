package com.mydrive.backend.controllers;

import com.mydrive.backend.annotations.ProviderParam;
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

/**
 * Controlador que gestiona las operaciones de almacenamiento en la nube para múltiples
 * proveedores, como Google Drive y Dropbox. Esta clase unifica las operaciones
 * comunes de diferentes servicios de almacenamiento en la nube, permitiendo interactuar
 * con ellos a través de una única interfaz.
 *
 * <p>
 * El controlador utiliza un parámetro de ruta llamado {@code provider} para determinar
 * el servicio de almacenamiento en la nube que se va a utilizar. Este parámetro se
 * pasa a una fábrica de servicios ({@link CloudStorageServiceFactory}) que retorna
 * la implementación adecuada de {@link CloudStorageService}.
 * </p>
 *
 * @see CloudStorageServiceFactory
 * @see CloudStorageService
 * @see MultiCloudService
 */
@RestController
@Scope(WebApplicationContext.SCOPE_REQUEST)
@RequestMapping("/api/{provider}")
public class CloudStorageController {

    /**
     * Fábrica que proporciona la implementación adecuada de {@link CloudStorageService}
     * según el proveedor de almacenamiento en la nube especificado.
     */
    @Autowired
    private CloudStorageServiceFactory factory;

    /**
     * Servicio para manejar operaciones entre múltiples nubes.
     */
    @Autowired
    private MultiCloudService multiCloudService;

    /**
     * Servicio de almacenamiento en la nube que se inicializa en función del proveedor
     * especificado en la ruta.
     */
    CloudStorageService cloudService = null;

    /**
     * Logger para registrar mensajes de información y errores.
     */
    private static final Logger logger = LoggerFactory.getLogger(CloudStorageService.class);

    /**
     * Método que se ejecuta antes de cualquier manejador de solicitudes en este controlador.
     * Inicializa el servicio de almacenamiento en la nube adecuado según el parámetro
     * {@code provider} recibido en la ruta.
     *
     * @param provider El proveedor de almacenamiento en la nube, como "google-drive" o "dropbox".
     */
    @ModelAttribute
    private void initCloudService(@PathVariable("provider") String provider) {
        cloudService = factory.getCloudService(provider);
    }

    /**
     * Maneja la autorización OAuth para el proveedor de almacenamiento en la nube.
     *
     * @return Un {@link ModelAndView} que redirige al usuario a la URL de autorización.
     * @throws Exception Si ocurre un error durante el proceso de autorización.
     */
    @ProviderParam
    @GetMapping("/oauth/authorize")
    public ModelAndView handleAuthorization() throws Exception {
        String authorizeUrl = cloudService.redirectToAuthorization();
        return new ModelAndView(new RedirectView(authorizeUrl));
    }

    /**
     * Maneja la respuesta del proceso de autorización OAuth. Este método se invoca
     * cuando el proveedor de almacenamiento en la nube redirige al usuario de vuelta a
     * la aplicación después de autorizarla.
     *
     * @param code El código de autorización recibido como parámetro en la solicitud de callback.
     * @return Un {@link ModelAndView} que redirige al usuario a la interfaz principal de la
     * aplicación después de la autenticación.
     * @throws Exception Si ocurre un error durante el proceso de autenticación.
     */
    @ProviderParam
    @GetMapping("/oauth/callback")
    public ModelAndView handleAuthorizationCallback(@RequestParam("code") String code) throws Exception {
        cloudService.authenticateUser(code);
        return new ModelAndView(new RedirectView("http://localhost:8081/filemanager/root"));
    }

    /**
     * Verifica si el usuario está autenticado en el servicio de almacenamiento en la nube
     * seleccionado. Este método es útil para comprobar el estado de la sesión.
     *
     * @return Un {@link ResponseEntity} que contiene un booleano indicando si el usuario está autenticado.
     * @throws Exception Si ocurre un error durante la verificación.
     */
    @ProviderParam
    @GetMapping("/oauth/check")
    public ResponseEntity<Boolean> checkAuthentication() throws Exception {
        return ResponseEntity.ok(cloudService.checkAuthentication());
    }

    /**
     * Cierra la sesión del usuario en el servicio de almacenamiento en la nube seleccionado.
     *
     * @return Un {@link ResponseEntity} que contiene un mensaje indicando que la sesión se ha cerrado correctamente.
     * @throws Exception Si ocurre un error durante el proceso de cierre de sesión.
     */
    @ProviderParam
    @PostMapping("/logout")
    public ResponseEntity<String> logout() throws Exception {
        cloudService.logout();
        return ResponseEntity.ok("Logout successfully");
    }

    /**
     * Obtiene la URL de la foto de perfil del usuario desde el servicio de almacenamiento en la nube.
     *
     * @return Un {@link ResponseEntity} que contiene la URL de la foto de perfil.
     * @throws Exception Si ocurre un error al obtener la foto de perfil.
     */
    @ProviderParam
    @GetMapping("/profilePhoto")
    public ResponseEntity<String> getProfilePhoto() throws Exception {
        String profilePhotoUrl = cloudService.getProfilePhoto();
        return ResponseEntity.ok(profilePhotoUrl);
    }

    /**
     * Obtiene el nombre de usuario desde el servicio de almacenamiento en la nube.
     *
     * @return Un {@link ResponseEntity} que contiene el nombre de usuario.
     * @throws Exception Si ocurre un error al obtener el nombre de usuario.
     */
    @ProviderParam
    @GetMapping("/userName")
    public ResponseEntity<String> getUserName() throws Exception {
        String userName = cloudService.getUserName();
        return ResponseEntity.ok(userName);
    }

    /**
     * Obtiene la ruta completa de una carpeta especificada por su ID.
     *
     * @param folderId El ID de la carpeta.
     * @return Un {@link ResponseEntity} que contiene una lista de {@link FileDTO} representando la ruta completa.
     * @throws Exception Si ocurre un error al obtener la ruta de la carpeta.
     */
    @ProviderParam
    @GetMapping("/path/{folderId}")
    public ResponseEntity<List<FileDTO>> getPathFolder(@PathVariable String folderId) throws Exception {
        List<FileDTO> fullPath = cloudService.getPathFolder(folderId);
        return ResponseEntity.ok(fullPath);
    }

    /**
     * Obtiene las carpetas dentro de una carpeta especificada por su ID.
     *
     * @param folderId El ID de la carpeta.
     * @param q        Un parámetro opcional de búsqueda para filtrar las carpetas.
     * @return Un {@link ResponseEntity} que contiene una lista de carpetas en la carpeta especificada.
     * @throws Exception Si ocurre un error al obtener las carpetas.
     */
    @ProviderParam
    @GetMapping("/folders/{folderId}")
    public ResponseEntity<List<FileDTO>> getFoldersInFolder(@PathVariable String folderId,
                                                            @RequestParam(required = false,
                                                                    defaultValue = "") String q) throws Exception {
        List<FileDTO> folders = cloudService.getFoldersInFolder(folderId, q);
        return ResponseEntity.ok(folders);
    }

    /**
     * Obtiene los archivos dentro de una carpeta especificada por su ID.
     *
     * @param folderId El ID de la carpeta.
     * @param q        Un parámetro opcional de búsqueda para filtrar los archivos.
     * @return Un {@link ResponseEntity} que contiene una lista de archivos en la carpeta especificada.
     * @throws Exception Si ocurre un error al obtener los archivos.
     */
    @ProviderParam
    @GetMapping("/files/{folderId}")
    public ResponseEntity<List<FileDTO>> getFilesInFolder(@PathVariable String folderId,
                                                          @RequestParam(required = false,
                                                                  defaultValue = "") String q) throws Exception {
        List<FileDTO> files = cloudService.getFilesInFolder(folderId, q);
        return ResponseEntity.ok(files);
    }

    /**
     * Obtiene una lista de archivos recientes en el servicio de almacenamiento en la nube.
     *
     * @param maxDate La fecha máxima para filtrar los archivos recientes.
     * @param q       Un parámetro opcional de búsqueda para filtrar los archivos.
     * @return Un {@link ResponseEntity} que contiene una lista de archivos recientes.
     * @throws Exception Si ocurre un error al obtener los archivos recientes.
     */
    @ProviderParam
    @GetMapping("/recentFiles")
    public ResponseEntity<List<FileDTO>> getRecentFiles(@RequestParam String maxDate,
                                                        @RequestParam(required = false,
                                                                defaultValue = "") String q) throws Exception {
        List<FileDTO> files = cloudService.getRecentFiles(maxDate, q);
        return ResponseEntity.ok(files);
    }

    /**
     * Obtiene los archivos que están en la papelera de reciclaje.
     *
     * @param q Un parámetro opcional de búsqueda para filtrar los archivos en la papelera.
     * @return Un {@link ResponseEntity} que contiene una lista de archivos en la papelera de reciclaje.
     * @throws Exception Si ocurre un error al obtener los archivos en la papelera.
     */
    @ProviderParam
    @GetMapping("/files/bin")
    public ResponseEntity<List<FileDTO>> getFilesInBin(@RequestParam(required = false,
            defaultValue = "") String q) throws Exception {

        List<FileDTO> files = cloudService.getFilesInBin(q);
        return ResponseEntity.ok(files);
    }

    /**
     * Busca carpetas por nombre en el servicio de almacenamiento en la nube.
     *
     * @param folderName El nombre de la carpeta a buscar.
     * @return Un {@link ResponseEntity} que contiene una lista de carpetas que coinciden con el nombre de búsqueda.
     * @throws Exception Si ocurre un error durante la búsqueda.
     */
    @ProviderParam
    @GetMapping(value = {"/searchFolder/{folderName}"})
    public ResponseEntity<List<FileDTO>> searchFolders(@PathVariable String folderName) throws Exception {
        List<FileDTO> folders = cloudService.searchFolders(folderName);
        return ResponseEntity.ok(folders);
    }

    /**
     * Busca archivos por nombre en el servicio de almacenamiento en la nube.
     *
     * @param fileName El nombre del archivo a buscar.
     * @return Un {@link ResponseEntity} que contiene una lista de archivos que coinciden con el nombre de búsqueda.
     * @throws Exception Si ocurre un error durante la búsqueda.
     */
    @ProviderParam
    @GetMapping(value = {"/searchFile/{fileName}"})
    public ResponseEntity<List<FileDTO>> searchFile(@PathVariable String fileName) throws Exception {
        List<FileDTO> files = cloudService.searchFiles(fileName);
        return ResponseEntity.ok(files);
    }

    /**
     * Crea una nueva carpeta en el servicio de almacenamiento en la nube.
     *
     * @param folderId El ID de la carpeta en la que se creará la nueva carpeta.
     * @param name     El nombre de la nueva carpeta.
     * @return Un {@link ResponseEntity} que confirma que la carpeta ha sido creada exitosamente.
     * @throws Exception Si ocurre un error durante la creación de la carpeta.
     */
    @ProviderParam
    @PostMapping("/createFolder/{folderId}")
    public ResponseEntity<String> createFolder(@PathVariable String folderId,
                                               @RequestParam String name) throws Exception {
        cloudService.createFolder(folderId, name);
        return ResponseEntity.ok("Folder has been created successfully");
    }

    /**
     * Sube un archivo a una carpeta especificada en el servicio de almacenamiento en la nube.
     *
     * @param file     El archivo a subir.
     * @param folderId El ID de la carpeta donde se subirá el archivo.
     * @return Un {@link ResponseEntity} que confirma que el archivo ha sido subido exitosamente.
     * @throws Exception Si ocurre un error durante la subida del archivo.
     */
    @ProviderParam
    @PostMapping("/uploadFile/{folderId}")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @PathVariable String folderId) throws Exception {
        cloudService.uploadFile(file, folderId);
        return ResponseEntity.ok("File has been upload successfully");
    }

    /**
     * Sube un archivo cifrado a una carpeta especificada en el servicio de almacenamiento en la nube.
     *
     * @param file     El archivo a subir.
     * @param password La contraseña para cifrar el archivo.
     * @param folderId El ID de la carpeta donde se subirá el archivo.
     * @return Un {@link ResponseEntity} que confirma que el archivo cifrado ha sido subido exitosamente.
     * @throws Exception Si ocurre un error durante la subida del archivo cifrado.
     */
    @ProviderParam
    @PostMapping("/uploadEncryptedFile/{folderId}")
    public ResponseEntity<String> uploadEncryptedFile(@RequestParam("file") MultipartFile file,
                                                      @RequestParam("password") String password,
                                                      @PathVariable String folderId) throws Exception {
        cloudService.uploadEncryptedFile(file, password, folderId);
        return ResponseEntity.ok("File has been upload successfully");
    }

    /**
     * Obtiene la URL de vista previa para un archivo especificado por su ID.
     *
     * @param fileId El ID del archivo.
     * @return Un {@link ResponseEntity} que contiene la URL de vista previa.
     * @throws Exception Si ocurre un error al obtener la URL de vista previa.
     */
    @ProviderParam
    @GetMapping("/preview-link/{fileId}")
    public ResponseEntity<String> getPreviewUrl(@PathVariable String fileId) throws Exception {
        return ResponseEntity.ok(cloudService.getPreviewLink(fileId));
    }

    /**
     * Descarga un archivo especificado por su ID desde el servicio de almacenamiento en la nube.
     *
     * @param fileId El ID del archivo a descargar.
     * @return Un {@link ResponseEntity} que contiene el contenido del archivo en formato de byte.
     * @throws Exception Si ocurre un error durante la descarga del archivo.
     */
    @ProviderParam
    @GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileId) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        byte[] fileContent = cloudService.downloadFile(fileId);

        return ResponseEntity.ok().headers(headers).body(fileContent);
    }

    /**
     * Descarga un archivo cifrado especificado por su ID desde el servicio de almacenamiento en la nube.
     *
     * @param fileId   El ID del archivo a descargar.
     * @param password La contraseña para descifrar el archivo.
     * @return Un {@link ResponseEntity} que contiene el contenido del archivo en formato de byte.
     * @throws Exception Si ocurre un error durante la descarga o descifrado del archivo.
     */
    @ProviderParam
    @PostMapping("/downloadEncryptedFile/{fileId}")
    public ResponseEntity<byte[]> downloadEncryptedFile(@PathVariable String fileId,
                                                        @RequestParam("password") String password) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        byte[] fileContent = cloudService.downloadEncryptedFile(fileId, password);

        return ResponseEntity.ok().headers(headers).body(fileContent);
    }

    /**
     * Mueve un archivo a una carpeta especificada en el servicio de almacenamiento en la nube.
     *
     * @param fileId   El ID del archivo a mover.
     * @param folderId El ID de la carpeta destino.
     * @return Un {@link ResponseEntity} que confirma que el archivo ha sido movido exitosamente.
     * @throws Exception Si ocurre un error durante el movimiento del archivo.
     */
    @ProviderParam
    @PutMapping("/moveFile/{fileId}")
    public ResponseEntity<String> moveFile(@PathVariable String fileId,
                                           @RequestParam String folderId) throws Exception {
        cloudService.moveFile(fileId, folderId);
        return ResponseEntity.ok("File has been moved successfully");
    }

    /**
     * Renombra un archivo en el servicio de almacenamiento en la nube.
     *
     * @param fileId El ID del archivo a renombrar.
     * @param name   El nuevo nombre del archivo.
     * @return Un {@link ResponseEntity} que confirma que el archivo ha sido renombrado exitosamente.
     * @throws Exception Si ocurre un error durante el renombrado del archivo.
     */
    @ProviderParam
    @PutMapping("/renameFile/{fileId}")
    public ResponseEntity<String> renameFile(@PathVariable String fileId,
                                             @RequestParam String name) throws Exception {
        cloudService.renameFile(fileId, name);
        return ResponseEntity.ok("File has been renamed successfully");
    }

    /**
     * Mueve un archivo a la papelera de reciclaje en el servicio de almacenamiento en la nube.
     *
     * @param fileId El ID del archivo a mover a la papelera.
     * @return Un {@link ResponseEntity} que confirma que el archivo ha sido movido a la papelera exitosamente.
     * @throws Exception Si ocurre un error durante el proceso.
     */
    @ProviderParam
    @PutMapping("/throwAway/{fileId}")
    public ResponseEntity<String> throwAwayFile(@PathVariable String fileId) throws Exception {
        cloudService.throwAwayFile(fileId);
        return ResponseEntity.ok("File has been thrown into the trash successfully");
    }

    /**
     * Restaura un archivo de la papelera de reciclaje en el servicio de almacenamiento en la nube.
     *
     * @param fileId El ID del archivo a restaurar.
     * @return Un {@link ResponseEntity} que confirma que el archivo ha sido restaurado exitosamente.
     * @throws Exception Si ocurre un error durante el proceso.
     */
    @ProviderParam
    @PutMapping("/restore/{fileId}")
    public ResponseEntity<String> restoreFile(@PathVariable String fileId) throws Exception {
        cloudService.restoreFile(fileId);
        return ResponseEntity.ok("File has been restored successfully");
    }

    /**
     * Elimina permanentemente un archivo en el servicio de almacenamiento en la nube.
     *
     * @param fileId El ID del archivo a eliminar.
     * @return Un {@link ResponseEntity} que confirma que el archivo ha sido eliminado exitosamente.
     * @throws Exception Si ocurre un error durante el proceso de eliminación.
     */
    @ProviderParam
    @DeleteMapping("/delete/{fileId}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileId) throws Exception {
        cloudService.deleteFile(fileId);
        return ResponseEntity.ok("File has been deleted successfully");
    }

    /**
     * Mueve un archivo de un servicio de almacenamiento en la nube a otro.
     *
     * @param provider            El proveedor de nube origen (por ejemplo, "google-drive" o "dropbox").
     * @param fileId              El ID del archivo a mover.
     * @param destinationProvider El proveedor de nube destino.
     * @param destinationFolderId El ID de la carpeta destino en el proveedor de nube destino.
     * @return Un {@link ResponseEntity} que confirma que el archivo ha sido movido exitosamente.
     * @throws Exception Si ocurre un error durante el proceso de mover el archivo entre nubes.
     */
    @ProviderParam
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
