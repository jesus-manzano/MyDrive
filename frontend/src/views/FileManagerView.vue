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
        <div id="dragdropmessage" class="upload-message text-info text-center" v-show="isDragging">
          <h2>Suelta el archivo para subirlo a la nube</h2>
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
    }
  },
  methods: {
    onDragOver() {
      this.isDragging = true;
    },
    onDragLeave(event) {
      if (!event.relatedTarget || !event.relatedTarget.closest('#droparea')) {
        this.isDragging = false;
      }
    },
    setOrderBy(newOrderBy) {
      this.orderBy = newOrderBy;
    },
    handleFileDrop(event) {
      this.isDragging = false;
      const files = event.dataTransfer.files;
      this.droppedFiles = Array.from(files);
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
