<template>
  <div class="bg-light" style="height: 100vh;">
    <NavBar/>
    <div class="d-flex flex-row">
      <SideBar :current-folder-id="currentFolderId" :droppedFiles="droppedFiles"/>
      <!-- Contenido principal -->
      <main id="droparea" :class="{ 'dragover': isDragging }" class="flex-grow-1 p-3"
            style="height: calc(100vh - 80px); overflow-y: auto;" @dragover.prevent="onDragOver"
            @dragleave.prevent="onDragLeave($event)" @drop.prevent="handleFileDrop">
        <div class="d-flex align-items-center">
          <!-- Barra de navegación de directorios -->
          <NavBarDir :current-folder-id="currentFolderId"/>

          <!-- Dropdown de orden -->
          <div class="dropdown ms-auto">
            <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton"
                    data-bs-toggle="dropdown"
                    aria-expanded="false">
              Ordenar por
            </button>
            <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="dropdownMenuButton">
              <li><a class="dropdown-item" :class="{ active: orderBy === 'name' }"
                     @click="setOrderBy('name')">Nombre</a></li>
              <li><a class="dropdown-item" :class="{ active: orderBy === 'date' }"
                     @click="setOrderBy('date')">Fecha</a></li>
              <li><a class="dropdown-item" :class="{ active: orderBy === 'tam' }"
                     @click="setOrderBy('tam')">Tamaño</a></li>
            </ul>
          </div>
        </div>
        <hr>
        <!-- Mensaje de ayuda para subir archivos arrastrando y soltando -->
        <div id="dragdropmessage" class="upload-message text-info text-center"
             v-show="isDragging && $route.name === 'filemanager'">
          <h2>Suelta el archivo para subirlo a la nube</h2>
        </div>

        <!-- Mensaje en caso de no mostrarse ningún archivo -->
        <div v-if="!hasFolders && !hasFiles && $route.name != 'bin'"
             class="d-flex flex-column justify-content-center align-items-center">
          <div class="display-6 my-5">No se ha encontrado ningún archivo</div>
          <div>
            <svg xmlns="http://www.w3.org/2000/svg" width="260" height="260" fill="rgba(0, 0, 0, 0.4)"
                 class="bi bi-file-earmark" viewBox="0 0 16 16">
              <path
                  d="M14 4.5V14a2 2 0 0 1-2 2H4a2 2 0 0 1-2-2V2a2 2 0 0 1 2-2h5.5zm-3 0A1.5 1.5 0 0 1 9.5 3V1H4a1 1 0 0 0-1 1v12a1 1 0 0 0 1 1h8a1 1 0 0 0 1-1V4.5z"/>
            </svg>
          </div>
        </div>
        <div v-if="!hasFiles && $route.name === 'bin'"
             class="d-flex flex-column justify-content-center align-items-center">
          <div class="display-6 my-5">No se ha encontrado ningún archivo en la papelera</div>
          <div>
            <svg xmlns="http://www.w3.org/2000/svg" width="260" height="260" fill="rgba(0, 0, 0, 0.4)"
                 class="bi bi-trash3" viewBox="0 0 16 16">
              <path
                  d="M6.5 1h3a.5.5 0 0 1 .5.5v1H6v-1a.5.5 0 0 1 .5-.5M11 2.5v-1A1.5 1.5 0 0 0 9.5 0h-3A1.5 1.5 0 0 0 5 1.5v1H1.5a.5.5 0 0 0 0 1h.538l.853 10.66A2 2 0 0 0 4.885 16h6.23a2 2 0 0 0 1.994-1.84l.853-10.66h.538a.5.5 0 0 0 0-1zm1.958 1-.846 10.58a1 1 0 0 1-.997.92h-6.23a1 1 0 0 1-.997-.92L3.042 3.5zm-7.487 1a.5.5 0 0 1 .528.47l.5 8.5a.5.5 0 0 1-.998.06L5 5.03a.5.5 0 0 1 .47-.53Zm5.058 0a.5.5 0 0 1 .47.53l-.5 8.5a.5.5 0 1 1-.998-.06l.5-8.5a.5.5 0 0 1 .528-.47M8 4.5a.5.5 0 0 1 .5.5v8.5a.5.5 0 0 1-1 0V5a.5.5 0 0 1 .5-.5"/>
            </svg>
          </div>
        </div>

        <!-- Listado de carpetas -->
        <FolderList :current-folder-id="currentFolderId" :searchText="searchText" :order-by="orderBy"/>

        <!-- Listado de archivos -->
        <FileList :current-folder-id="currentFolderId" :searchText="searchText" :order-by="orderBy"/>
      </main>
    </div>
  </div>
</template>

<script>
import NavBar from "@/components/NavBar";
import SideBar from "@/components/SideBar";
import NavBarDir from "@/components/NavBarDir";
import FolderList from "@/components/FolderList";
import FileList from "@/components/FileList";
import {mapState} from 'vuex';

export default {
  components: {FileList, FolderList, NavBarDir, SideBar, NavBar},
  data() {
    return {
      isDragging: false,
      droppedFiles: [],
      orderBy: 'name',
    };
  },
  computed: {
    currentFolderId() {
      return this.$route.params.folderId;
    },
    searchText() {
      return this.$route.query.q || ''; // Si no hay parámetro 'q', devuelve una cadena vacía
    },
    ...mapState(['hasFolders', 'hasFiles']) // Mapea si hay carpetas o archivos en los componentes hijos
  },
  methods: {
    onDragOver() {
      if (this.$route.name === 'filemanager') this.isDragging = true;
    },
    onDragLeave(event) {
      if (this.$route.name === 'filemanager') {
        if (!event.relatedTarget || !event.relatedTarget.closest('#droparea')) {
          this.isDragging = false;
        }
      }
    },
    setOrderBy(newOrderBy) {
      this.orderBy = newOrderBy;
    },
    handleFileDrop(event) {
      if (this.$route.name === 'filemanager') {
        this.isDragging = false;
        const files = event.dataTransfer.files;
        this.droppedFiles = Array.from(files);
      }
    }
  }
}
</script>

<style scoped>
#droparea.dragover {
  border: 2px solid rgb(86, 222, 255, 0.5);
  background-color: rgba(86, 222, 255, 0.1);
}

.dropdown-menu-end {
  left: auto !important;
  right: 0;
}
</style>
