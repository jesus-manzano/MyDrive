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
            <h5 class="card-title p-2" style="white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
              {{ folder.name }}
            </h5>
            <small class="text-muted p-1">{{ folder.lastTimeViewed }}</small>
          </div>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: "FolderList",
  data() {
    return {
      folders: []
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
  }
}
</script>

<style scoped>
.folder-card:hover img {
  margin-top: -5px !important;
}
</style>
