<template>
  <!-- Barra de navegación de directorios -->
  <nav aria-label="breadcrumb">
    <ol class="breadcrumb breadcrumb-chevron rounded-3 m-0">
      <li class="breadcrumb-item">
        <router-link class="link-body-emphasis text-decoration-none d-flex align-items-center"
                     :to="`/filemanager/root`">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
               class="bi bi-house-door-fill" viewBox="0 0 16 16">
            <path
                d="M6.5 14.5v-3.505c0-.245.25-.495.5-.495h2c.25 0 .5.25.5.5v3.5a.5.5 0 0 0 .5.5h4a.5.5 0 0 0 .5-.5v-7a.5.5 0 0 0-.146-.354L13 5.793V2.5a.5.5 0 0 0-.5-.5h-1a.5.5 0 0 0-.5.5v1.293L8.354 1.146a.5.5 0 0 0-.708 0l-6 6A.5.5 0 0 0 1.5 7.5v7a.5.5 0 0 0 .5.5h4a.5.5 0 0 0 .5-.5"/>
          </svg>
          <span class="fw-bold">Inicio</span>
        </router-link>
      </li>
      <li class="breadcrumb-item" v-for="(folder, index) in currentFolderPath" :key="index">
        <router-link :to="`/filemanager/${folder.id}`" class="link-body-emphasis fw-semibold text-decoration-none">
          {{ folder.name }}
        </router-link>
      </li>
    </ol>
  </nav>
</template>

<script>
import axios from "axios";

export default {
  name: "NavBarDir",
  data() {
    return {
      currentFolderPath: []
    };
  },
  props: {
    currentFolderId: {
      type: String,
      required: true
    }
  },
  watch: {
    currentFolderId() {
      this.getFolderPath(); // Si cambia de directorio obtiene la ruta de dicho directorio
    }
  },
  mounted() {
    this.getFolderPath();
  },
  methods: {
    getFolderPath() {
      this.currentFolderPath = [];
      if (this.$route.name === 'filemanager') {
        axios.get(`/api/google-drive/path/` + this.currentFolderId)
            .then(response => {
              this.currentFolderPath = response.data;
              this.currentFolderPath.shift(); // Eliminamos el directorio raíz para que se muestre correctamente en el html
            })
            .catch(error => {
              console.error('Error fetching folders:', error);
            });
      }
    }
  }
}
</script>

<style scoped>

</style>