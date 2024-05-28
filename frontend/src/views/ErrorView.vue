<template>
  <div class="error-view d-flex flex-column justify-content-center align-items-center text-center">
    <div class="error-container">
      <h1 class="display-1">Error: {{ errorCode }}</h1>
      <p class="lead">{{ errorMessage }}</p>
      <div>
        <svg xmlns="http://www.w3.org/2000/svg" width="200" height="200" fill="rgba(0, 0, 0, 0.3)" class="bi bi-bug mt-3"
             viewBox="0 0 16 16">
          <path
              d="M4.355.522a.5.5 0 0 1 .623.333l.291.956A5 5 0 0 1 8 1c1.007 0 1.946.298 2.731.811l.29-.956a.5.5 0 1 1 .957.29l-.41 1.352A5 5 0 0 1 13 6h.5a.5.5 0 0 0 .5-.5V5a.5.5 0 0 1 1 0v.5A1.5 1.5 0 0 1 13.5 7H13v1h1.5a.5.5 0 0 1 0 1H13v1h.5a1.5 1.5 0 0 1 1.5 1.5v.5a.5.5 0 1 1-1 0v-.5a.5.5 0 0 0-.5-.5H13a5 5 0 0 1-10 0h-.5a.5.5 0 0 0-.5.5v.5a.5.5 0 1 1-1 0v-.5A1.5 1.5 0 0 1 2.5 10H3V9H1.5a.5.5 0 0 1 0-1H3V7h-.5A1.5 1.5 0 0 1 1 5.5V5a.5.5 0 0 1 1 0v.5a.5.5 0 0 0 .5.5H3c0-1.364.547-2.601 1.432-3.503l-.41-1.352a.5.5 0 0 1 .333-.623M4 7v4a4 4 0 0 0 3.5 3.97V7zm4.5 0v7.97A4 4 0 0 0 12 11V7zM12 6a4 4 0 0 0-1.334-2.982A3.98 3.98 0 0 0 8 2a3.98 3.98 0 0 0-2.667 1.018A4 4 0 0 0 4 6z"/>
        </svg>
      </div>
      <router-link :to="`/filemanager/root`" class="btn btn-primary mt-4">Ir al gestor de archivos</router-link>
    </div>
  </div>
</template>

<script>
import {mapMutations} from 'vuex';

export default {
  name: "ErrorView",
  props: {
    errorCode: {
      type: Number,
      required: true
    },
    errorMessage: {
      type: String,
      required: true
    }
  },
  mounted() {
    this.setCloudService(''); // Quitamos la nube seleccionada
  },
  methods: {
    ...mapMutations(['setCloudService'])
  }
}
</script>

<style scoped>
.error-view {
  height: 100vh;
  background-color: #f8f9fa;
}

.error-container {
  max-width: 600px;
}

.display-1 {
  font-size: 6rem;
}

.lead {
  font-size: 1.6rem;
  color: #6c757d;
}
</style>