<template>
  <div class="bg-light" style="height: 100vh;">
    <NavBar v-if="authenticationChecked"/>
    <div class="d-flex flex-row">
      <SideBar v-if="authenticationChecked" :current-folder-id="currentFolderId" :droppedFiles="droppedFiles"/>
      <!-- Contenido principal -->
      <main id="droparea" :class="{ 'dragover': isDragging }" class="flex-grow-1 p-3"
            style="height: calc(100vh - 80px); overflow-y: auto;" @dragover.prevent="onDragOver"
            @dragleave.prevent="onDragLeave($event)" @drop.prevent="handleFileDrop">
        <div class="d-flex align-items-center">
          <!-- Barra de navegación de directorios -->
          <NavBarDir v-if="authenticationChecked" :current-folder-id="currentFolderId"/>

          <!-- Dropdown de orden -->
          <div v-if="$route.name != 'recent'" class="dropdown ms-auto">
            <button class="btn btn-dark dropdown-toggle" type="button" id="dropdownMenuButton"
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
          <div v-if="$route.name === 'recent'" class="dropdown ms-auto">
            <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton"
                    data-bs-toggle="dropdown"
                    aria-expanded="false">
              Periodo max.
            </button>
            <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="dropdownMenuButton">
              <li><a class="dropdown-item" :class="{ active: period === 'day' }"
                     @click="setPeriod('day')">1 día</a></li>
              <li><a class="dropdown-item" :class="{ active: period === 'week' }"
                     @click="setPeriod('week')">1 semana</a></li>
              <li><a class="dropdown-item" :class="{ active: period === 'month' }"
                     @click="setPeriod('month')">1 mes</a></li>
              <li><a class="dropdown-item" :class="{ active: period === 'year' }"
                     @click="setPeriod('year')">1 año</a></li>
            </ul>
          </div>
        </div>
        <hr>
        <!-- Mensaje de ayuda para subir archivos arrastrando y soltando -->
        <div id="dragdropmessage" class="upload-message text-info text-center"
             v-show="isDragging && $route.name === 'filemanager'">
          <h2>Suelta el archivo para subirlo a la nube</h2>
        </div>

        <!-- Mensaje en caso de no estar autenticado en ninguna nube -->
        <div v-show="authenticationChecked && isNotAuthenticatedInAnyCloud">
          <div class="display-6 my-5"><b>Inicia Sesión</b> en alguna de las nubes disponibles en la barra lateral</div>
        </div>

        <!-- Mensaje en caso de no mostrarse ningún archivo -->
        <div v-if="!hasFolders && !hasFiles && $route.name === 'filemanager'"
             class="d-flex flex-column justify-content-center align-items-center">
          <div class="display-6 my-5">No se ha encontrado ningún archivo en esta carpeta</div>
          <div>
            <svg xmlns="http://www.w3.org/2000/svg" width="260" height="260" fill="rgba(0, 0, 0, 0.4)"
                 class="bi bi-file-earmark" viewBox="0 0 16 16">
              <path
                  d="M14 4.5V14a2 2 0 0 1-2 2H4a2 2 0 0 1-2-2V2a2 2 0 0 1 2-2h5.5zm-3 0A1.5 1.5 0 0 1 9.5 3V1H4a1 1 0 0 0-1 1v12a1 1 0 0 0 1 1h8a1 1 0 0 0 1-1V4.5z"/>
            </svg>
          </div>
        </div>
        <div v-if="!hasFiles && $route.name === 'recent'"
             class="d-flex flex-column justify-content-center align-items-center">
          <div class="display-6 my-5">No se has abierto o modificado recientemente ningún archivo</div>
          <div>
            <svg xmlns="http://www.w3.org/2000/svg" width="260" height="260" fill="rgba(0, 0, 0, 0.4)"
                 class="bi bi-clock-history" viewBox="0 0 16 16">
              <path
                  d="M8.515 1.019A7 7 0 0 0 8 1V0a8 8 0 0 1 .589.022zm2.004.45a7 7 0 0 0-.985-.299l.219-.976q.576.129 1.126.342zm1.37.71a7 7 0 0 0-.439-.27l.493-.87a8 8 0 0 1 .979.654l-.615.789a7 7 0 0 0-.418-.302zm1.834 1.79a7 7 0 0 0-.653-.796l.724-.69q.406.429.747.91zm.744 1.352a7 7 0 0 0-.214-.468l.893-.45a8 8 0 0 1 .45 1.088l-.95.313a7 7 0 0 0-.179-.483m.53 2.507a7 7 0 0 0-.1-1.025l.985-.17q.1.58.116 1.17zm-.131 1.538q.05-.254.081-.51l.993.123a8 8 0 0 1-.23 1.155l-.964-.267q.069-.247.12-.501m-.952 2.379q.276-.436.486-.908l.914.405q-.24.54-.555 1.038zm-.964 1.205q.183-.183.35-.378l.758.653a8 8 0 0 1-.401.432z"/>
              <path d="M8 1a7 7 0 1 0 4.95 11.95l.707.707A8.001 8.001 0 1 1 8 0z"/>
              <path
                  d="M7.5 3a.5.5 0 0 1 .5.5v5.21l3.248 1.856a.5.5 0 0 1-.496.868l-3.5-2A.5.5 0 0 1 7 9V3.5a.5.5 0 0 1 .5-.5"/>
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
        <FolderList v-if="authenticationChecked" :current-folder-id="currentFolderId" :searchText="searchText"
                    :order-by="orderBy"/>

        <!-- Listado de archivos -->
        <FileList v-if="authenticationChecked" :current-folder-id="currentFolderId" :searchText="searchText"
                  :order-by="orderBy" :period="period"/>
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
import {mapState, mapMutations, mapGetters} from 'vuex';
import axios from 'axios';

export default {
  components: {FileList, FolderList, NavBarDir, SideBar, NavBar},
  data() {
    return {
      authenticationChecked: false,
      isDragging: false,
      droppedFiles: [],
      orderBy: 'name',
      period: 'week'
    };
  },
  watch: {
    '$route.name'(newRouteName) {
      if (newRouteName === 'recent') {
        this.orderBy = 'date';
      } else {
        this.orderBy = 'name';
      }
    }
  },
  computed: {
    currentFolderId() {
      return this.$route.params.folderId;
    },
    searchText() {
      return this.$route.query.q || ''; // Si no hay parámetro 'q', devuelve una cadena vacía
    },
    ...mapState(['hasFolders', 'hasFiles']), // Mapea si hay carpetas o archivos en los componentes hijos
    ...mapState(['cloudService']), // Servicio que etá usando actualmente
    ...mapState(['isAuthenticated']), // Servicios en lo que está autenticado el usuario
    ...mapGetters(['isNotAuthenticatedInAnyCloud']), // Comprueba si no está autenticado en ninguna nube
  },
  async mounted() {
    await this.initialize();
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
    setPeriod(newPeriod) {
      this.period = newPeriod;
    },
    handleFileDrop(event) {
      if (this.$route.name === 'filemanager') {
        this.isDragging = false;
        const files = event.dataTransfer.files;
        this.droppedFiles = Array.from(files);
      }
    },
    ...mapMutations(['setCloudService']),
    ...mapMutations(['setAuthentication']),
    async initialize() {
      await this.checkAllAuthenticatedCloudService();
      if (this.isNotAuthenticatedInAnyCloud || !this.isAuthenticated[this.cloudService])
        this.setCloudService(''); // Reseteamos nube
      this.authenticationChecked = true; // Marcar como finalizada la verificación de autenticación
    },
    async checkAllAuthenticatedCloudService() {
      const services = ['google-drive', 'dropbox'];
      const promises = services.map(service =>
          axios.get(`/api/${service}/oauth/check`)
              .then(response => {
                this.setAuthentication({service, status: response.data});
              })
              .catch(error => {
                console.error(`Error al comprobar autenticación para ${service}:`, error);
                this.setAuthentication({service, status: false});
              })
      );
      await Promise.all(promises);
    },
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
