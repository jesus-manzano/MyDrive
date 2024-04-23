<template>
  <div v-if="files.length > 0">
    <div class="d-flex justify-content-between  align-items-center mt-4 mb-4">
      <div class="display-6">Archivos</div>
      <button v-if="$route.name === 'bin'" class="btn btn-primary">
        Vaciar papelera
        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
             class="bi bi-trash3 mx-1" viewBox="0 0 16 16">
          <path
              d="M6.5 1h3a.5.5 0 0 1 .5.5v1H6v-1a.5.5 0 0 1 .5-.5M11 2.5v-1A1.5 1.5 0 0 0 9.5 0h-3A1.5 1.5 0 0 0 5 1.5v1H1.5a.5.5 0 0 0 0 1h.538l.853 10.66A2 2 0 0 0 4.885 16h6.23a2 2 0 0 0 1.994-1.84l.853-10.66h.538a.5.5 0 0 0 0-1zm1.958 1-.846 10.58a1 1 0 0 1-.997.92h-6.23a1 1 0 0 1-.997-.92L3.042 3.5zm-7.487 1a.5.5 0 0 1 .528.47l.5 8.5a.5.5 0 0 1-.998.06L5 5.03a.5.5 0 0 1 .47-.53Zm5.058 0a.5.5 0 0 1 .47.53l-.5 8.5a.5.5 0 1 1-.998-.06l.5-8.5a.5.5 0 0 1 .528-.47M8 4.5a.5.5 0 0 1 .5.5v8.5a.5.5 0 0 1-1 0V5a.5.5 0 0 1 .5-.5"/>
        </svg>
      </button>
    </div>

    <div class="d-flex flex-wrap justify-content-center text-center">
      <div class="mx-4 mb-5 rounded-4" style="width: 11rem;" v-for="(file, index) in files" :key="file.id">
        <a id="file-card" href="" class="card" :class="{ 'transform-card-selected': file.isSelected }"
           @click.prevent="toggleCardStyle(file)">
          <img :src="file.thumbnailLink ? file.thumbnailLink : require('@/assets/file.png')"
               class="card__image bg-light" alt="Imagen Archivo"/>
          <div class="card__overlay" :class="{ 'transform-card-up': file.showOverlay }">
            <div class="card__header" :class="{ 'transform-card-up': file.showOverlay }">
              <svg class="card__arc" xmlns="http://www.w3.org/2000/svg">
                <path/>
              </svg>
              <div class="card__header-text">
                <h3 class="card__title">{{ file.name }}</h3>
                <span class="card__status">{{ file.lastTimeViewed }}</span>
              </div>
            </div>
            <button class="btn btn-light mb-1 boton-abrir" style="width: 8rem;"
                    @click.prevent="openFile(file.id, $event)">
              Abrir
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                   class="bi bi-box-arrow-right" viewBox="0 0 16 16">
                <path fill-rule="evenodd"
                      d="M10 12.5a.5.5 0 0 1-.5.5h-8a.5.5 0 0 1-.5-.5v-9a.5.5 0 0 1 .5-.5h8a.5.5 0 0 1 .5.5v2a.5.5 0 0 0 1 0v-2A1.5 1.5 0 0 0 9.5 2h-8A1.5 1.5 0 0 0 0 3.5v9A1.5 1.5 0 0 0 1.5 14h8a1.5 1.5 0 0 0 1.5-1.5v-2a.5.5 0 0 0-1 0z"/>
                <path fill-rule="evenodd"
                      d="M15.854 8.354a.5.5 0 0 0 0-.708l-3-3a.5.5 0 0 0-.708.708L14.293 7.5H5.5a.5.5 0 0 0 0 1h8.793l-2.147 2.146a.5.5 0 0 0 .708.708z"/>
              </svg>
            </button>
            <button class="btn btn-light my-1 boton-descargar" style="width: 8rem;"
                    @click="downloadFile(file.id, file.name)">
              Descargar
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                   class="bi bi-download" viewBox="0 0 16 16">
                <path
                    d="M.5 9.9a.5.5 0 0 1 .5.5v2.5a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1v-2.5a.5.5 0 0 1 1 0v2.5a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2v-2.5a.5.5 0 0 1 .5-.5"/>
                <path
                    d="M7.646 11.854a.5.5 0 0 0 .708 0l3-3a.5.5 0 0 0-.708-.708L8.5 10.293V1.5a.5.5 0 0 0-1 0v8.793L5.354 8.146a.5.5 0 1 0-.708.708z"/>
              </svg>
            </button>
            <button class="btn btn-light mt-1 mb-2 boton-papelera" style="width: 8rem;"
                    @click="throwAwayFile(file.id, index)">
              Papelera
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                   class="bi bi-trash3" viewBox="0 0 16 16">
                <path
                    d="M6.5 1h3a.5.5 0 0 1 .5.5v1H6v-1a.5.5 0 0 1 .5-.5M11 2.5v-1A1.5 1.5 0 0 0 9.5 0h-3A1.5 1.5 0 0 0 5 1.5v1H1.5a.5.5 0 0 0 0 1h.538l.853 10.66A2 2 0 0 0 4.885 16h6.23a2 2 0 0 0 1.994-1.84l.853-10.66h.538a.5.5 0 0 0 0-1zm1.958 1-.846 10.58a1 1 0 0 1-.997.92h-6.23a1 1 0 0 1-.997-.92L3.042 3.5zm-7.487 1a.5.5 0 0 1 .528.47l.5 8.5a.5.5 0 0 1-.998.06L5 5.03a.5.5 0 0 1 .47-.53Zm5.058 0a.5.5 0 0 1 .47.53l-.5 8.5a.5.5 0 1 1-.998-.06l.5-8.5a.5.5 0 0 1 .528-.47M8 4.5a.5.5 0 0 1 .5.5v8.5a.5.5 0 0 1-1 0V5a.5.5 0 0 1 .5-.5"/>
              </svg>
            </button>
          </div>
        </a>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: "FileList",
  data() {
    return {
      files: []
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
      this.getFiles();
    },
    searchText() {
      this.getFiles();
    },
    orderBy() {
      this.sortFiles();
    },
    '$store.state.searchInFolder'(newValue, oldValue) {
      if (newValue !== oldValue) {
        this.getFiles();
      }
    }
  },
  mounted() {
    this.getFiles();
  },
  methods: {
    // Método para definir el endpoint según la ruta en la que nos encontramos
    // y dependiendo de la configuración del cliente
    getEndpoint() {
      let searching = (this.$route.query.q || '') != ''; // Si tiene algún valor q considera que se está buscando
      let endpoint;

      if (this.$route.name != 'bin') { // Si no estamos en la papelera
        endpoint = '/api/google-drive/files/' + this.currentFolderId + '?q=' + this.searchText;
      } else endpoint = `/api/google-drive/files/bin?q=` + this.searchText;

      if (searching && !this.$store.state.searchInFolder) { // Si está buscando de forma global
        endpoint = '/api/google-drive/searchFile/' + this.searchText;
      }

      return endpoint;
    },
    // Método para obtener todos los archivos que no son directorios
    getFiles() {
      this.files = []; // Limpiamos los archivos actuales
      const endpoint = this.getEndpoint();

      axios.get(endpoint)
          .then(response => {
            this.files = response.data;
            this.sortFiles();
          })
          .catch(error => {
            console.error('Error fetching files:', error);
          });
    },
    // Método para abrir un archivo en el navegador sin descargar
    openFile(fileId, event) {
      const url = `https://drive.google.com/file/d/${fileId}/view`;
      // Si la tecla Control está presionada, abrir en una nueva pestaña
      if (event.ctrlKey || event.metaKey) {
        window.open(url, '_blank');
      } else {
        window.location.href = url;
      }
    },
    // Método para descargar un archivo
    downloadFile(fileId, fileName) {
      axios.get(`/api/google-drive/download/${fileId}`, {
        responseType: 'blob' // Indica que la respuesta será un blob (binario)
      })
          .then(response => {
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', fileName);
            document.body.appendChild(link);
            link.click();
          })
          .catch(error => {
            console.error('Error al descargar el archivo:', error);
          });
    },
    // Método para enviar un archivo a la papelera
    throwAwayFile(fileId, index) {
      axios.delete(`/api/google-drive/throwAway/${fileId}`)
          .then(response => {
            this.files.splice(index, 1);
            console.log('Archivo enviado a la papelera:', response.data);
          })
          .catch(error => {
            console.error('Error al enviar el archivo a la papelera:', error);
          });
    },
    // Método para ordenar los archivos
    sortFiles() {
      // Ordenar los archivos según el método seleccionado
      if (this.orderBy === 'name') {
        this.files.sort((a, b) => a.name.localeCompare(b.name));
      } else if (this.orderBy === 'date') {
        this.files.sort((a, b) => {
          const dateA = new Date(a.lastTimeViewed);
          const dateB = new Date(b.lastTimeViewed);
          return dateB - dateA;
        });
      } else if (this.orderBy === 'tam') {
        this.files.sort((a, b) => b.size - a.size);
      }
    },
    // Método para mostrar el overlay de opciones de un archivo
    toggleCardStyle(file) {
      file.showOverlay = !file.showOverlay;
      file.isSelected = !file.isSelected;
    }
  }
}
</script>

<style scoped>
.card {
  position: relative;
  display: block;
  height: 246px;
  border-radius: 40px;
  overflow: hidden;
  text-decoration: none;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.card:hover {
  margin-top: -5px;
}

.transform-card-up {
  transform: translateY(0%) !important;
}

.transform-card-selected {
  box-shadow: 0 10px 16px rgba(0, 0, 0, 0.7) !important;
}

.card__image {
  width: 100%;
}

.card__overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  z-index: 1;
  border-bottom-left-radius: 40px;
  border-bottom-right-radius: 40px;
  background-color: #fff;
  transform: translateY(100%);
  transition: .2s ease-in-out;
}

.card__header {
  position: relative;
  display: flex;
  align-items: center;
  gap: 0.5em;
  padding: 0.5em;
  border-radius: 40px 0 0 0;
  background-color: #fff;
  transform: translateY(-100%);
  transition: .2s ease-in-out;
}

.card__header-text {
  margin-left: auto;
  margin-right: auto;
}

.card__title {
  font-size: 1em;
  margin: 0;
  color: #6A515E;
  width: 9rem;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.card__status {
  font-size: .8em;
  color: #D7BDCA;
}

.card__arc {
  width: 80px;
  height: 80px;
  position: absolute;
  bottom: 100%;
  right: 0;
  z-index: 1;
}

.card__arc path {
  fill: #fff;
  d: path("M 40 80 c 22 0 40 -22 40 -40 v 40 Z");
}
</style>