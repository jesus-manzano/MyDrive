<template>
  <div v-if="folders.length > 0">
    <div class="display-6 mt-4 mb-4">Carpetas</div>

    <div class="d-flex flex-wrap justify-content-center text-center">
      <div v-for="(folder, index) in folders" :key="index" class="mx-4 mb-5" style="width: 11rem;">
        <router-link :to="`/filemanager/${folder.id}`" class="folder-card text-decoration-none">
          <div class="d-flex align-items-center" style="overflow: hidden;">
            <img src="@/assets/folder.png" class="card-img-top" alt="Imagen">
          </div>

          <div class="card-body">
            <div class="d-flex flex-row justify-content-center align-items-center">
              <div class="card-title p-2 h5 text-center"
                   style="white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
                {{ folder.name }}
              </div>
              <div class="dropdown" @click.prevent="toggleDropdown(folder)">
                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="rgb(50, 50, 50, 0.6)"
                     class="bi bi-gear-fill dropdown-toggle folder-option" id="dropdownFolder" data-bs-toggle="dropdown"
                     viewBox="0 0 16 16">
                  <path
                      d="M9.405 1.05c-.413-1.4-2.397-1.4-2.81 0l-.1.34a1.464 1.464 0 0 1-2.105.872l-.31-.17c-1.283-.698-2.686.705-1.987 1.987l.169.311c.446.82.023 1.841-.872 2.105l-.34.1c-1.4.413-1.4 2.397 0 2.81l.34.1a1.464 1.464 0 0 1 .872 2.105l-.17.31c-.698 1.283.705 2.686 1.987 1.987l.311-.169a1.464 1.464 0 0 1 2.105.872l.1.34c.413 1.4 2.397 1.4 2.81 0l.1-.34a1.464 1.464 0 0 1 2.105-.872l.31.17c1.283.698 2.686-.705 1.987-1.987l-.169-.311a1.464 1.464 0 0 1 .872-2.105l.34-.1c1.4-.413 1.4-2.397 0-2.81l-.34-.1a1.464 1.464 0 0 1-.872-2.105l.17-.31c.698-1.283-.705-2.686-1.987-1.987l-.311.169a1.464 1.464 0 0 1-2.105-.872zM8 10.93a2.929 2.929 0 1 1 0-5.86 2.929 2.929 0 0 1 0 5.858z"/>
                </svg>
                <ul class="dropdown-menu text-small shadow" :class="{ 'dropdown-folder': folder.showOverlay }"
                    aria-labelledby="dropdownFolder">
                  <li>
                    <router-link :to="`/filemanager/${folder.id}`" class="dropdown-item">
                      Abrir
                      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                           class="bi bi-box-arrow-right ms-1" viewBox="0 0 16 16">
                        <path fill-rule="evenodd"
                              d="M10 12.5a.5.5 0 0 1-.5.5h-8a.5.5 0 0 1-.5-.5v-9a.5.5 0 0 1 .5-.5h8a.5.5 0 0 1 .5.5v2a.5.5 0 0 0 1 0v-2A1.5 1.5 0 0 0 9.5 2h-8A1.5 1.5 0 0 0 0 3.5v9A1.5 1.5 0 0 0 1.5 14h8a1.5 1.5 0 0 0 1.5-1.5v-2a.5.5 0 0 0-1 0z"/>
                        <path fill-rule="evenodd"
                              d="M15.854 8.354a.5.5 0 0 0 0-.708l-3-3a.5.5 0 0 0-.708.708L14.293 7.5H5.5a.5.5 0 0 0 0 1h8.793l-2.147 2.146a.5.5 0 0 0 .708.708z"/>
                      </svg>
                    </router-link>
                  </li>
                  <li>
                    <div class="dropdown-item" @click="openRenameFolderOverlay(folder)">
                      Renombrar
                      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                           class="bi bi-pencil ms-1" viewBox="0 0 16 16">
                        <path
                            d="M12.146.146a.5.5 0 0 1 .708 0l3 3a.5.5 0 0 1 0 .708l-10 10a.5.5 0 0 1-.168.11l-5 2a.5.5 0 0 1-.65-.65l2-5a.5.5 0 0 1 .11-.168zM11.207 2.5 13.5 4.793 14.793 3.5 12.5 1.207zm1.586 3L10.5 3.207 4 9.707V10h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.293zm-9.761 5.175-.106.106-1.528 3.821 3.821-1.528.106-.106A.5.5 0 0 1 5 12.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.468-.325"/>
                      </svg>
                    </div>
                  </li>
                  <li>
                    <div class="dropdown-item" @click="openDeleteFolderOverlay(folder)">
                      Eliminar
                      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                           class="bi bi-trash3 ms-1" viewBox="0 0 16 16">
                        <path
                            d="M6.5 1h3a.5.5 0 0 1 .5.5v1H6v-1a.5.5 0 0 1 .5-.5M11 2.5v-1A1.5 1.5 0 0 0 9.5 0h-3A1.5 1.5 0 0 0 5 1.5v1H1.5a.5.5 0 0 0 0 1h.538l.853 10.66A2 2 0 0 0 4.885 16h6.23a2 2 0 0 0 1.994-1.84l.853-10.66h.538a.5.5 0 0 0 0-1zm1.958 1-.846 10.58a1 1 0 0 1-.997.92h-6.23a1 1 0 0 1-.997-.92L3.042 3.5zm-7.487 1a.5.5 0 0 1 .528.47l.5 8.5a.5.5 0 0 1-.998.06L5 5.03a.5.5 0 0 1 .47-.53Zm5.058 0a.5.5 0 0 1 .47.53l-.5 8.5a.5.5 0 1 1-.998-.06l.5-8.5a.5.5 0 0 1 .528-.47M8 4.5a.5.5 0 0 1 .5.5v8.5a.5.5 0 0 1-1 0V5a.5.5 0 0 1 .5-.5"/>
                      </svg>
                    </div>
                  </li>
                </ul>
              </div>
            </div>
            <small class="text-muted p-1">{{ folder.lastTimeViewed }}</small>
          </div>
        </router-link>
      </div>
    </div>
  </div>

  <!-- Overlay para renombrar una carpeta -->
  <div v-show="showRenameFolderOverlay" class="overlay-area">
    <div class="popup-area">
      <h2 class="mb-3">Renombrar carpeta</h2>
      <div class="d-flex justify-content-between align-items-center mb-3">
        <div class="bold me-3">Nombre:</div>
        <input type="text" class="form-control" placeholder="Nuevo nombre..." v-model="folderName"
               @keyup.enter="renameFolder">
      </div>
      <div class="d-flex justify-content-between align-items-center">
        <button class="btn btn-danger" @click="closeRenameFolderOverlay">Cancelar</button>
        <button class="btn btn-success" @click="renameFolder">Crear</button>
      </div>
    </div>
  </div>

  <!-- Overlay para confirmar eliminación de una carpeta -->
  <div v-show="showDeleteFolderOverlay" class="overlay-area">
    <div class="popup-area">
      <h2 class="mb-3">¿Estás seguro de eliminar dicha carpeta?</h2>
      <div class="d-flex justify-content-between align-items-center mb-3">
        <div class="me-3">
          <bold class="bold fs-5">Info:</bold>
          Eliminar dicha carpeta también eliminará todos los elementos de su interior.
        </div>
      </div>
      <div class="d-flex justify-content-between align-items-center">
        <button class="btn btn-danger" @click="closeDeleteFolderOverlay">Cancelar</button>
        <button class="btn btn-success" @click="deleteFolder">Aceptar</button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import {mapMutations} from "vuex";

export default {
  name: "FolderList",
  data() {
    return {
      folders: [],
      showRenameFolderOverlay: false,
      showDeleteFolderOverlay: false,
      folderSelected: null,
      folderName: ''
    };
  },
  props: {
    currentFolderId: {
      type: String,
      required: true
    },
    searchText: {
      type: String,
      default: ''
    },
    orderBy: {
      type: String,
      default: 'name'
    },
  },
  watch: {
    currentFolderId() {
      this.getFolders();
    },
    searchText() {
      this.getFolders();
    },
    orderBy() {
      this.sortFolders();
    },
    '$store.state.searchInFolder'(newValue, oldValue) {
      if (newValue !== oldValue) {
        this.getFolders();
      }
    },
  },
  mounted() {
    this.getFolders();
  },
  methods: {
    // Método para definir el endpoint según la ruta en la que nos encontramos
    // y dependiendo de la configuración del cliente
    getEndpoint() {
      let searching = (this.$route.query.q || '') != ''; // Si tiene algún valor q considera que se está buscando
      let endpoint;

      if (searching && !this.$store.state.searchInFolder) { // Si está buscando de forma global
        endpoint = '/api/google-drive/searchFolder/' + this.searchText;
      } else endpoint = '/api/google-drive/folders/' + this.currentFolderId + '?q=' + this.searchText;

      return endpoint;
    },
    // Método para obtener las carpetas
    getFolders() {
      this.folders = []; // Limpiamos las carpetas actuales
      if (this.$route.name != 'bin') { // Si no estamos en la papelera
        const endpoint = this.getEndpoint();

        axios.get(endpoint)
            .then(response => {
              this.folders = response.data;
              this.sortFolders();
              this.setHasFolders(this.folders.length > 0);
            })
            .catch(error => {
              console.error('Error fetching folders:', error);
            });
      }
    },
    // Método para ordenar las carpetas
    sortFolders() {
      // Ordenar los archivos según el método seleccionado
      if (this.orderBy === 'name' || this.orderBy === 'tam') {
        this.folders.sort((a, b) => a.name.localeCompare(b.name));
      } else if (this.orderBy === 'date') {
        this.folders.sort((a, b) => {
          const dateA = new Date(a.lastTimeViewed);
          const dateB = new Date(b.lastTimeViewed);
          return dateB - dateA;
        });
      }
    },
    // Método para renombrar una carpeta
    renameFolder() {
      if (this.folderSelected) {
        // Enviar una solicitud al backend para renombrar la carpeta
        axios.put(`/api/google-drive/renameFile/` + this.folderSelected.id + '?name=' + this.folderName)
            .then(response => {
              this.folderSelected.name = this.folderName;
              console.log('Carpeta renombrada exitosamente:', response.data);
              this.sortFolders();

              this.folderName = '';
              this.folderSelected = null;
              this.showRenameFolderOverlay = false;
            })
            .catch(error => {
              // Manejar errores, por ejemplo, mostrar un mensaje de error
              console.error('Error al crear la carpeta:', error);
            });
      }
    },
    // Método para eliminar una carpeta y todos sus archivos de forma permanente
    deleteFolder(folderId, index) {
      axios.delete(`/api/google-drive/delete/${folderId}`)
          .then(response => {
            this.folders.splice(index, 1);
            this.setHasFolders(this.folders.length > 0);
            console.log('Archivo eliminado de forma definitiva:', response.data);
          })
          .catch(error => {
            console.error('Error al eliminar el archivo de forma definitiva:', error);
          });
    },
    ...mapMutations(['setHasFolders']), // Establece a nivel global si hay carpetas
    // Método para abrir o cerrar el menú de opciones de una carpeta
    toggleDropdown(folder) {
      folder.showDropdown = !folder.showDropdown;
    },
    // Método para abrir el overlay para renombrar una carpeta e indicar la carpeta
    // que vamos a modificar
    openRenameFolderOverlay(folder) {
      this.folderSelected = folder;
      this.showRenameFolderOverlay = true;
    },
    // Método para cerrar el overlay para renombrar una carpeta
    closeRenameFolderOverlay() {
      this.folderName = '';
      this.showRenameFolderOverlay = false;
    },
    // Método para abrir el overlay para eliminar una carpeta e indicar la carpeta
    // que vamos a eliminar
    openDeleteFolderOverlay(folder) {
      this.folderSelected = folder;
      this.showDeleteFolderOverlay = true;
    },
    // Método para cerrar el overlay para eliminar una carpeta
    closeDeleteFolderOverlay() {
      this.showDeleteFolderOverlay = false;
    },
  }
}
</script>

<style scoped>
.folder-card:hover img {
  margin-top: -5px !important;
}

.dropdown-folder {
  display: block;
}

.folder-option:hover {
  fill: rgb(50, 50, 50, 0.8);
}
</style>
