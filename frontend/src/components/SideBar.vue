<template>
  <!-- Barra lateral -->
  <div class="sidebar-box d-flex flex-column flex-shrink-0 p-3">
    <router-link :to="`/filemanager/root`"
                 class="d-flex align-items-center justify-content-center w-100 link-dark text-decoration-none">
      <span class="fs-4">Accesos Rápidos</span>
    </router-link>
    <hr>
    <ul class="nav nav-pills flex-column">
      <li class="nav-item">
        <router-link :to="`/filemanager/root`" class="nav-link sidebar-link bg-d-flex align-items-center fs-5 p-2 my-1"
                     :class="{'link-dark': !$route.params.folderId}">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor"
               class="bi bi-house-door-fill mx-2" viewBox="0 0 16 16">
            <path
                d="M6.5 14.5v-3.505c0-.245.25-.495.5-.495h2c.25 0 .5.25.5.5v3.5a.5.5 0 0 0 .5.5h4a.5.5 0 0 0 .5-.5v-7a.5.5 0 0 0-.146-.354L13 5.793V2.5a.5.5 0 0 0-.5-.5h-1a.5.5 0 0 0-.5.5v1.293L8.354 1.146a.5.5 0 0 0-.708 0l-6 6A.5.5 0 0 0 1.5 7.5v7a.5.5 0 0 0 .5.5h4a.5.5 0 0 0 .5-.5"/>
          </svg>
          Inicio
        </router-link>
      </li>
      <li>
        <router-link :to="`/filemanager/recent`"
                     class="nav-link sidebar-link d-flex align-items-center fs-5 p-2 my-1"
                     :class="{'link-dark': $route.name !== 'recent'}">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor"
               class="bi bi-clock-history mx-2" viewBox="0 0 16 16">
            <path
                d="M8.515 1.019A7 7 0 0 0 8 1V0a8 8 0 0 1 .589.022zm2.004.45a7 7 0 0 0-.985-.299l.219-.976q.576.129 1.126.342zm1.37.71a7 7 0 0 0-.439-.27l.493-.87a8 8 0 0 1 .979.654l-.615.789a7 7 0 0 0-.418-.302zm1.834 1.79a7 7 0 0 0-.653-.796l.724-.69q.406.429.747.91zm.744 1.352a7 7 0 0 0-.214-.468l.893-.45a8 8 0 0 1 .45 1.088l-.95.313a7 7 0 0 0-.179-.483m.53 2.507a7 7 0 0 0-.1-1.025l.985-.17q.1.58.116 1.17zm-.131 1.538q.05-.254.081-.51l.993.123a8 8 0 0 1-.23 1.155l-.964-.267q.069-.247.12-.501m-.952 2.379q.276-.436.486-.908l.914.405q-.24.54-.555 1.038zm-.964 1.205q.183-.183.35-.378l.758.653a8 8 0 0 1-.401.432z"/>
            <path d="M8 1a7 7 0 1 0 4.95 11.95l.707.707A8.001 8.001 0 1 1 8 0z"/>
            <path
                d="M7.5 3a.5.5 0 0 1 .5.5v5.21l3.248 1.856a.5.5 0 0 1-.496.868l-3.5-2A.5.5 0 0 1 7 9V3.5a.5.5 0 0 1 .5-.5"/>
          </svg>
          Recientes
        </router-link>
      </li>
      <li>
        <router-link :to="`/filemanager/bin`" class="nav-link sidebar-link d-flex align-items-center fs-5 p-2 my-1"
                     :class="{'link-dark': $route.name !== 'bin'}">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor"
               class="bi bi-trash3 mx-2" viewBox="0 0 16 16">
            <path
                d="M6.5 1h3a.5.5 0 0 1 .5.5v1H6v-1a.5.5 0 0 1 .5-.5M11 2.5v-1A1.5 1.5 0 0 0 9.5 0h-3A1.5 1.5 0 0 0 5 1.5v1H1.5a.5.5 0 0 0 0 1h.538l.853 10.66A2 2 0 0 0 4.885 16h6.23a2 2 0 0 0 1.994-1.84l.853-10.66h.538a.5.5 0 0 0 0-1zm1.958 1-.846 10.58a1 1 0 0 1-.997.92h-6.23a1 1 0 0 1-.997-.92L3.042 3.5zm-7.487 1a.5.5 0 0 1 .528.47l.5 8.5a.5.5 0 0 1-.998.06L5 5.03a.5.5 0 0 1 .47-.53Zm5.058 0a.5.5 0 0 1 .47.53l-.5 8.5a.5.5 0 1 1-.998-.06l.5-8.5a.5.5 0 0 1 .528-.47M8 4.5a.5.5 0 0 1 .5.5v8.5a.5.5 0 0 1-1 0V5a.5.5 0 0 1 .5-.5"/>
          </svg>
          Papelera
        </router-link>
      </li>
    </ul>

    <!-- Selección de la nube actual para trabajar -->
    <hr>
    <span class="text-center fs-4">Nubes disponibles</span>
    <hr>
    <div class="mb-auto" style="max-height: 108px; overflow-y: auto;">
      <ul class="nav nav-pills flex-column">
        <li>
          <a class="nav-link sidebar-link d-flex align-items-center fs-5 p-2 my-1"
             :class="{'link-dark': cloudService !== 'google-drive'}"
             @click="handleClickCloudService('google-drive')">
            <img src="@/assets/google-drive.png" alt="Google Drive" class="cloud-img me-2">
            Google Drive
          </a>
        </li>
        <li>
          <a class="nav-link sidebar-link d-flex align-items-center fs-5 p-2 my-1"
             :class="{'link-dark': cloudService !== 'dropbox'}"
             @click.prevent="handleClickCloudService('dropbox')">
            <img src="@/assets/dropbox.png" alt="Dropbox" class="cloud-img me-2">
            Dropbox
          </a>
        </li>
      </ul>
    </div>

    <div v-if="isAuthenticated[cloudService] && $route.name === 'filemanager'">
      <button class="btn btn-warning" @click="openCreateFolderOverlay">
        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-folder-plus"
             viewBox="0 0 16 16">
          <path
              d="m.5 3 .04.87a2 2 0 0 0-.342 1.311l.637 7A2 2 0 0 0 2.826 14H9v-1H2.826a1 1 0 0 1-.995-.91l-.637-7A1 1 0 0 1 2.19 4h11.62a1 1 0 0 1 .996 1.09L14.54 8h1.005l.256-2.819A2 2 0 0 0 13.81 3H9.828a2 2 0 0 1-1.414-.586l-.828-.828A2 2 0 0 0 6.172 1H2.5a2 2 0 0 0-2 2m5.672-1a1 1 0 0 1 .707.293L7.586 3H2.19q-.362.002-.683.12L1.5 2.98a1 1 0 0 1 1-.98z"/>
          <path
              d="M13.5 9a.5.5 0 0 1 .5.5V11h1.5a.5.5 0 1 1 0 1H14v1.5a.5.5 0 1 1-1 0V12h-1.5a.5.5 0 0 1 0-1H13V9.5a.5.5 0 0 1 .5-.5"/>
        </svg>
        Crear carpeta
      </button>
      <button class="btn btn-warning mt-2" @click="openUploadFileOverlay">
        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
             class="bi bi-cloud-upload" viewBox="0 0 16 16">
          <path fill-rule="evenodd"
                d="M4.406 1.342A5.53 5.53 0 0 1 8 0c2.69 0 4.923 2 5.166 4.579C14.758 4.804 16 6.137 16 7.773 16 9.569 14.502 11 12.687 11H10a.5.5 0 0 1 0-1h2.688C13.979 10 15 8.988 15 7.773c0-1.216-1.02-2.228-2.313-2.228h-.5v-.5C12.188 2.825 10.328 1 8 1a4.53 4.53 0 0 0-2.941 1.1c-.757.652-1.153 1.438-1.153 2.055v.448l-.445.049C2.064 4.805 1 5.952 1 7.318 1 8.785 2.23 10 3.781 10H6a.5.5 0 0 1 0 1H3.781C1.708 11 0 9.366 0 7.318c0-1.763 1.266-3.223 2.942-3.593.143-.863.698-1.723 1.464-2.383"/>
          <path fill-rule="evenodd"
                d="M7.646 4.146a.5.5 0 0 1 .708 0l3 3a.5.5 0 0 1-.708.708L8.5 5.707V14.5a.5.5 0 0 1-1 0V5.707L5.354 7.854a.5.5 0 1 1-.708-.708z"/>
        </svg>
        Subir archivos
      </button>
    </div>
    <hr>
    <div v-if="isAuthenticated[cloudService]" class="dropdown">
      <a href="#" class="d-flex align-items-center link-dark text-decoration-none dropdown-toggle"
         id="dropdownUser2" data-bs-toggle="dropdown" aria-expanded="false">
        <img v-if="profilePhotoUrl" :src="profilePhotoUrl" alt="Profile Photo" width="32" height="32"
             class="rounded-circle me-2">
        <strong>{{ userName }}</strong>
      </a>
      <ul class="dropdown-menu text-small shadow" aria-labelledby="dropdownUser2" style="">
        <li>
          <a class="dropdown-item" @click.prevent="logout">
            Cerrar sesión
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                 class="bi bi-box-arrow-right" viewBox="0 0 16 16">
              <path fill-rule="evenodd"
                    d="M10 12.5a.5.5 0 0 1-.5.5h-8a.5.5 0 0 1-.5-.5v-9a.5.5 0 0 1 .5-.5h8a.5.5 0 0 1 .5.5v2a.5.5 0 0 0 1 0v-2A1.5 1.5 0 0 0 9.5 2h-8A1.5 1.5 0 0 0 0 3.5v9A1.5 1.5 0 0 0 1.5 14h8a1.5 1.5 0 0 0 1.5-1.5v-2a.5.5 0 0 0-1 0z"/>
              <path fill-rule="evenodd"
                    d="M15.854 8.354a.5.5 0 0 0 0-.708l-3-3a.5.5 0 0 0-.708.708L14.293 7.5H5.5a.5.5 0 0 0 0 1h8.793l-2.147 2.146a.5.5 0 0 0 .708.708z"/>
            </svg>
          </a>
        </li>
      </ul>
    </div>
  </div>

  <!-- Overlay para crear una carpeta nueva -->
  <div v-show="showCreateFolderOverlay" class="overlay-area">
    <div class="popup-area">
      <h2 class="mb-3">Nueva carpeta</h2>
      <div class="d-flex justify-content-between align-items-center mb-3">
        <div class="bold me-3">Nombre:</div>
        <input type="text" class="form-control" placeholder="Introduce nombre..." v-model="folderName"
               @keyup.enter="createFolder">
      </div>
      <div class="d-flex justify-content-between align-items-center">
        <button class="btn btn-danger" @click="closeCreateFolderOverlay" id="closeBtn-search-setting">Cancelar</button>
        <button class="btn btn-success" @click="createFolder" id="closeBtn-search-setting">Crear</button>
      </div>
    </div>
  </div>

  <!-- Overlay para subir archivos -->
  <div v-show="showUploadFileOverlay" class="overlay-area">
    <div class="popup-area">
      <h2 class="mb-4">Subir archivos</h2>

      <!-- Área para arrastrar y soltar para subir los archivos -->
      <form>
        <fieldset id="upload_area" class="text-center mb-3 p-4" :class="{ 'dragover': isDraggingUploadFile }"
                  @dragover.prevent="onDragOverUploadFile" @dragleave.prevent="onDragLeaveUploadFile($event)"
                  @drop.prevent="handleFileDrop">
          <svg xmlns="http://www.w3.org/2000/svg" width="60" height="60" fill="currentColor"
               class="bi bi-cloud-upload-fill" viewBox="0 0 16 16">
            <path fill-rule="evenodd"
                  d="M8 0a5.53 5.53 0 0 0-3.594 1.342c-.766.66-1.321 1.52-1.464 2.383C1.266 4.095 0 5.555 0 7.318 0 9.366 1.708 11 3.781 11H7.5V5.707L5.354 7.854a.5.5 0 1 1-.708-.708l3-3a.5.5 0 0 1 .708 0l3 3a.5.5 0 0 1-.708.708L8.5 5.707V11h4.188C14.502 11 16 9.57 16 7.773c0-1.636-1.242-2.969-2.834-3.194C12.923 1.999 10.69 0 8 0m-.5 14.5V11h1v3.5a.5.5 0 0 1-1 0"/>
          </svg>
          <p class="small my-2">Arrastra y suelta los archivos para subirlos<br><i>or</i></p>
          <input type="file" id="upload_file_btn" style="display: none;" multiple @change="handleFileSelection">
          <label for="upload_file_btn" class="btn btn-warning mb-3">Seleccionar archivo(s)</label>
        </fieldset>
      </form>

      <!-- Lista de archivos seleccionados -->
      <div v-if="selectedFiles.length > 0">
        <h6>Archivos Seleccionados:</h6>
        <div class="d-flex mt-2 mb-2">
          <div v-for="(file, index) in selectedFiles" :key="index" class="file-selected me-2 ps-1">
            {{ file.name }}
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-x"
                 viewBox="0 0 16 16" @click="removeFile(index)">
              <path
                  d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708"/>
            </svg>
          </div>
        </div>
      </div>

      <!-- Barra de progreso -->
      <h6 v-show="uploading" class="mt-2">Archivos subidos:</h6>
      <div v-if="uploading" class="progress mt-2 mb-3 d-flex justify-content-between">
        <div class="progress-bar" role="progressbar" :style="{ width: uploadProgress + '%' }"
             aria-valuenow="uploadProgress" aria-valuemin="0" aria-valuemax="100">{{ uploadProgress }}%
        </div>
      </div>

      <div class="d-flex justify-content-between align-items-center">
        <button class="btn btn-danger" @click="closeUploadFileOverlay" id="closeBtn-search-setting">Cancelar</button>
        <button class="btn btn-success" @click="uploadFiles" id="closeBtn-search-setting">Subir</button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import {mapState, mapMutations} from 'vuex';

export default {
  data() {
    return {
      selectedFiles: [],
      folderName: '',
      profilePhotoUrl: '',
      userName: '',
      showUploadFileOverlay: false,
      showCreateFolderOverlay: false,
      isDraggingUploadFile: false,
      uploading: false,
      uploadProgress: 0,
    };
  },
  props: {
    currentFolderId: {
      type: String,
      required: true
    },
    droppedFiles: {
      type: Array,
      default: () => []
    }
  },
  computed: {
    ...mapState(['cloudService']),
    ...mapState(['isAuthenticated'])
  },
  watch: {
    droppedFiles(newFiles) {
      this.selectedFiles = [...newFiles];
      this.openUploadFileOverlay();
    },
    cloudService() {
      if (this.$route.path === '/filemanager/root') {
        this.getProfilePhoto();
        this.getUserName();
      }
    }
  },
  mounted() {
    this.getProfilePhoto();
    this.getUserName();
  },
  methods: {
    // Método para obtener la foto de perfil
    getProfilePhoto() {
      if (this.cloudService === '' || !this.isAuthenticated[this.cloudService]) return; // No ejecutamos

      axios.get('/api/' + this.cloudService + '/profilePhoto')
          .then(response => {
            // Al recibir la respuesta, asignar la URL de la foto de perfil a profilePhotoUrl
            this.profilePhotoUrl = response.data;
          })
          .catch(error => {
            // Manejar errores de la petición
            console.error('Error al obtener la foto de perfil:', error);
          });
    },
    // Método para obtener el nombre del usuario
    getUserName() {
      if (this.cloudService === '' || !this.isAuthenticated[this.cloudService]) return; // No ejecutamos

      axios.get('/api/' + this.cloudService + '/userName')
          .then(response => {
            // Al recibir la respuesta, procesar el nombre completo
            const fullName = response.data;

            // Dividir el nombre completo en palabras
            const nameParts = fullName.split(' ');

            // Obtener el primer nombre y el primer apellido
            const firstName = nameParts[0];
            const lastName = nameParts.length > 1 ? nameParts[1] : '';

            // Asignar el primer nombre y el primer apellido a userName
            this.userName = `${firstName} ${lastName}`;
          })
          .catch(error => {
            // Manejar errores de la petición
            console.error('Error al obtener el nombre de usuario:', error);
          });
    },
    // Método para manejar la selección de archivos
    handleFileSelection(event) {
      const files = event.target.files;
      for (let i = 0; i < files.length; i++) {
        this.selectedFiles.push(files[i]);
      }
    },
    // Método para manejar el evento de soltar archivos
    handleFileDrop(event) {
      this.isDraggingUploadFile = false;

      const files = event.dataTransfer.files;
      for (let i = 0; i < files.length; i++) {
        this.selectedFiles.push(files[i]);
      }
    },
    // Método para eliminar un archivo de la lista de archivos seleccionados
    removeFile(index) {
      this.selectedFiles.splice(index, 1);
    },
    // Método para subir archivos seleccionados al backend
    uploadFiles() {
      if (this.selectedFiles.length > 0) {
        const totalFiles = this.selectedFiles.length;
        let uploadedFiles = 0;
        this.uploadProgress = 0;
        this.uploading = true;

        this.selectedFiles.forEach(file => {
          const formData = new FormData();
          formData.append('file', file);

          // Realizar una petición al backend para cada archivo
          axios.post(`/api/` + this.cloudService + `/uploadFile/${this.currentFolderId}`, formData)
              .then(response => {
                console.log(response.data);
                // Eliminar el archivo de la lista de archivos seleccionados si se cargó correctamente
                this.selectedFiles = this.selectedFiles.filter(selectedFile => selectedFile !== file);

                // Actualizar el progreso de la barra
                uploadedFiles++;
                this.uploadProgress = (uploadedFiles / totalFiles) * 100;

                // Cerrar el overlay si ya no hay archivos seleccionados
                if (this.selectedFiles.length === 0) {
                  this.uploading = false;
                  this.uploadProgress = 0;
                  this.closeUploadFileOverlay();
                  window.location.reload(); // Recarga de nuevo la página
                }
              })
              .catch(error => {
                console.error(`Error al subir archivo ${file.name}:`, error);
              });
        });
      }
    },
    // Método para crear nueva carpeta
    createFolder() {
      // Enviar una solicitud al backend para crear una nueva carpeta
      axios.post(`/api/` + this.cloudService + `/createFolder/` + this.currentFolderId + '?name=' + this.folderName)
          .then(response => {
            // Procesar la respuesta del backend, por ejemplo, mostrar un mensaje de éxito
            console.log('Carpeta creada exitosamente:', response.data);
            // Cerrar el overlay después de crear la carpeta
            this.closeCreateFolderOverlay();
            window.location.reload();
          })
          .catch(error => {
            // Manejar errores, por ejemplo, mostrar un mensaje de error
            console.error('Error al crear la carpeta:', error);
          });
    },
    // Método para abrir el overlay para crear una carpeta
    openCreateFolderOverlay() {
      this.showCreateFolderOverlay = true;
    },
    // Método para cerrar el overlay para crear una carpeta
    closeCreateFolderOverlay() {
      this.folderName = '';
      this.showCreateFolderOverlay = false;
    },
    // Método para abrir el overlay para subir un archivo
    openUploadFileOverlay() {
      this.showUploadFileOverlay = true;
    },
    // Método para cerrar el overlay para subir un archivo
    closeUploadFileOverlay() {
      this.selectedFiles = [];
      this.showUploadFileOverlay = false;
    },
    onDragOverUploadFile() {
      this.isDraggingUploadFile = true;
    },
    onDragLeaveUploadFile(event) {
      if (!event.relatedTarget || !event.relatedTarget.closest('#upload_area')) {
        this.isDraggingUploadFile = false;
      }
    },
    ...mapMutations(['setCloudService']),
    ...mapMutations(['setAuthentication']),
    handleClickCloudService(cloudService) {
      this.setCloudService(cloudService);

      if (!this.isAuthenticated[cloudService]) {
        window.location.href = `/api/${cloudService}/oauth/authorize`;
      } else {
        this.$router.push('/filemanager/root');
      }
    },
    // Método para cerrar sesión en la nube actual
    logout() {
      // Enviar una solicitud al backend para crear una nueva carpeta
      axios.post(`/api/` + this.cloudService + `/logout`)
          .then(response => {
            console.log('Sesión cerrada con éxito: ', response.data);
            window.location.reload(); // Recarga de nuevo la página
          })
          .catch(error => {
            // Manejar errores, por ejemplo, mostrar un mensaje de error
            console.error('Error al cerrar la sesión: ', error);
          });
    }
  }
};
</script>

<style scoped>
.sidebar-box {
  width: 280px;
  background-color: rgb(240, 240, 240);
}

.sidebar-link:hover {
  background-color: rgb(190, 190, 190);
}

#closeBtn-search-setting {
  margin-top: 10px;
}

#upload_area {
  border: 2px dashed rgb(76, 212, 245, 0.5);
  background-color: rgba(86, 222, 255, 0.1);
}

#upload_area.dragover {
  border: 2px dashed rgba(0, 102, 204, 0.5);
  background-color: rgba(86, 222, 255, 0.2);
}

.file-selected {
  background-color: rgb(190, 190, 190, 0.4);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  color: rgba(0, 0, 0, 0.8);
  border-radius: 4px;
}

.file-selected svg:hover {
  background-color: rgb(190, 190, 190, 0.6);
}

.cloud-img {
  max-height: 26px;
}
</style>