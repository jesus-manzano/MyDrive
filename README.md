# ☁️ MyDrive – Gestión MultiProveedor de Archivos en la Nube

MyDrive es una aplicación web que permite gestionar múltiples servicios de almacenamiento en la nube (como Google Drive y Dropbox) de manera centralizada. La aplicación está dividida en dos partes principales: **backend** y **frontend**.

- **Backend:** API REST desarrollada en Java con Spring Boot que unifica las operaciones de distintos servicios de nube.  
- **Frontend:** Interfaz web desarrollada en Vue.js que permite al usuario interactuar con la API y gestionar sus archivos de manera intuitiva.

---

## 🌟 Características

### Gestión MultiProveedor
- Soporte para múltiples servicios de almacenamiento en la nube.
- Operaciones unificadas: subir, descargar, renombrar, mover, eliminar, restaurar, etc.
- Autenticación OAuth para cada proveedor.

### Operaciones Avanzadas
- Mover archivos entre distintos servicios de nube.
- Papelera de reciclaje y recuperación de archivos.
- Subida y descarga de archivos cifrados.
- Visualización de archivos recientes y carpetas.

### Interfaz Web
- Navegación intuitiva con barra lateral, barra de rutas y panel de archivos.
- Componentes principales:
  - `FileList.vue`: Lista de archivos.
  - `FolderList.vue`: Lista de carpetas.
  - `NavBar.vue`: Barra de navegación superior.
  - `NavBarDir.vue`: Barra de rutas (breadcrumb) de navegación de carpetas.
  - `SideBar.vue`: Menú lateral con acceso rápido a carpetas y servicios.
- Previsualización de archivos y descarga segura.
- Búsqueda de archivos y carpetas por nombre.

---

## 🛠 Instalación

### Backend

1. Clonar el repositorio y entrar en el backend:
   ```bash
   git clone https://github.com/tuusuario/mydrive.git
   cd mydrive/backend
   ```

2. Configurar `application.properties`:

   ```properties
   server.port=8080
   spring.datasource.url=jdbc:mysql://localhost:3306/mydrive
   spring.datasource.username=root
   spring.datasource.password=1234
   ```

- Añadir credenciales y claves OAuth de los proveedores de nube. 

### Frontend

1. Entrar en el directorio del frontend:

   ```bash
   cd ../frontend
   ```

2. Instalar dependencias:

   ```bash
   npm install
   ```

3. Ejecutar servidor de desarrollo:

   ```bash
   npm run serve
   ```

4. Acceder a la aplicación:

   ```arduino
   http://localhost:8081
   ```

## 🧪 Ejemplos de uso

### OAuth y autenticación

- Redirigir al proveedor para autorizar:

   ```swift
   GET /api/google-drive/oauth/authorize
   ```

- Callback después de autorización:

   ```
   GET /api/google-drive/oauth/callback?code=XXXX
   ```

- Verificar autenticación:

   ```swift
   GET /api/google-drive/oauth/check
   ```

### Gestión de archivos y carpetas

- Obtener carpetas:

   ```swift
   GET /api/google-drive/folders/root
   ```

- Obtener archivos:

   ```swift
   GET /api/google-drive/files/root
   ```

- Subir archivo:

   ```swift
   POST /api/google-drive/uploadFile/root
   ```

- Descargar archivo:

   ```swift
   GET /api/google-drive/download/12345
   ```

- Renombrar archivo:

   ```swift
   PUT /api/google-drive/renameFile/12345?name=nuevoNombre.txt
   ```

### Operaciones avanzadas

- Mover archivo entre nubes:

   ```swift
   POST /api/google-drive/moveFile/12345/dropbox/root
   ```

- Subida de archivo cifrado:

   ```swift
   POST /api/google-drive/uploadEncryptedFile/root
   ```

- Descarga de archivo cifrado:

   ```swift
   POST /api/google-drive/downloadEncryptedFile/12345
   ```

### Papelera de reciclaje

- Mover archivo a papelera:

   ```swift
   PUT /api/google-drive/throwAway/12345
   ```

- Restaurar archivo:

   ```swift
   PUT /api/google-drive/restore/12345
   ```

- Eliminar archivo permanentemente:

   ```swift
   DELETE /api/google-drive/delete/12345
   ```

## 📚 Documentación completa

La documentación final de MyDrive, correspondiente a mi Trabajo Fin de Grado, está publicada en el portal CREA de la Universidad de Jaén.  
Puedes consultarla en el siguiente enlace:

[📄 Documentación MyDrive en CREA](https://crea.ujaen.es/items/395803bd-dce3-4b8c-809f-3e66a1622082)

**Autor:** Jesús Manzano

**Universidad:** Universidad de Jaén
