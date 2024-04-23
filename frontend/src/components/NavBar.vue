<template>
  <!-- Barra de navegación -->
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark" style="height: 80px;">
    <!-- Parte izquierda de la barra de navegación -->
    <div class="d-flex align-items-center justify-content-center" style="width: 280px;">
      <router-link :to="`/filemanager/root`" class="d-flex align-items-center text-decoration-none text-white">
        <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" fill="currentColor"
             class="bi bi-hdd-rack" viewBox="0 0 16 16">
          <path
              d="M4.5 5a.5.5 0 1 0 0-1 .5.5 0 0 0 0 1M3 4.5a.5.5 0 1 1-1 0 .5.5 0 0 1 1 0m2 7a.5.5 0 1 1-1 0 .5.5 0 0 1 1 0m-2.5.5a.5.5 0 1 0 0-1 .5.5 0 0 0 0 1"/>
          <path
              d="M2 2a2 2 0 0 0-2 2v1a2 2 0 0 0 2 2h1v2H2a2 2 0 0 0-2 2v1a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2v-1a2 2 0 0 0-2-2h-1V7h1a2 2 0 0 0 2-2V4a2 2 0 0 0-2-2zm13 2v1a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V4a1 1 0 0 1 1-1h12a1 1 0 0 1 1 1m0 7v1a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1v-1a1 1 0 0 1 1-1h12a1 1 0 0 1 1 1m-3-4v2H4V7z"/>
        </svg>
        <span class="fs-4" style="padding-left: 5px;">MyDrive</span>
      </router-link>
    </div>

    <!-- Parte derecha de la barra de navegación -->
    <div class="d-flex align-items-center justify-content-between ms-auto" style="flex-grow: 1;">
      <!-- Formulario de búsqueda -->
      <div class="d-flex align-items-center flex-grow-1 justify-content-center">
        <div class="search_box">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor"
               class="bi bi-binoculars-fill" viewBox="0 0 16 16">
            <path
                d="M4.5 1A1.5 1.5 0 0 0 3 2.5V3h4v-.5A1.5 1.5 0 0 0 5.5 1zM7 4v1h2V4h4v.882a.5.5 0 0 0 .276.447l.895.447A1.5 1.5 0 0 1 15 7.118V13H9v-1.5a.5.5 0 0 1 .146-.354l.854-.853V9.5a.5.5 0 0 0-.5-.5h-3a.5.5 0 0 0-.5.5v.793l.854.853A.5.5 0 0 1 7 11.5V13H1V7.118a1.5 1.5 0 0 1 .83-1.342l.894-.447A.5.5 0 0 0 3 4.882V4zM1 14v.5A1.5 1.5 0 0 0 2.5 16h3A1.5 1.5 0 0 0 7 14.5V14zm8 0v.5a1.5 1.5 0 0 0 1.5 1.5h3a1.5 1.5 0 0 0 1.5-1.5V14zm4-11H9v-.5A1.5 1.5 0 0 1 10.5 1h1A1.5 1.5 0 0 1 13 2.5z"/>
          </svg>
          <div class="first_line"></div>

          <div class="text_and_icon">
            <input type="text" class="search_text" id="search_text"
                   placeholder="Buscar archivos por nombre ..." v-model="searchText" @keyup.enter="searchFiles">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor"
                 class="bi bi-search search_icon" viewBox="0 0 16 16" id="search_button" @click="searchFiles">
              <path
                  d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001q.044.06.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1 1 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0"/>
            </svg>
          </div>

          <div class="second_line"></div>
          <div class="search_setting d-flex align-items-center" @click="openSearchSettingOverlay">
            <svg xmlns="http://www.w3.org/2000/svg" width="26" height="26" fill="currentColor" class="bi bi-sliders2"
                 viewBox="0 0 16 16">
              <path fill-rule="evenodd"
                    d="M10.5 1a.5.5 0 0 1 .5.5v4a.5.5 0 0 1-1 0V4H1.5a.5.5 0 0 1 0-1H10V1.5a.5.5 0 0 1 .5-.5M12 3.5a.5.5 0 0 1 .5-.5h2a.5.5 0 0 1 0 1h-2a.5.5 0 0 1-.5-.5m-6.5 2A.5.5 0 0 1 6 6v1.5h8.5a.5.5 0 0 1 0 1H6V10a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5M1 8a.5.5 0 0 1 .5-.5h2a.5.5 0 0 1 0 1h-2A.5.5 0 0 1 1 8m9.5 2a.5.5 0 0 1 .5.5v4a.5.5 0 0 1-1 0V13H1.5a.5.5 0 0 1 0-1H10v-1.5a.5.5 0 0 1 .5-.5m1.5 2.5a.5.5 0 0 1 .5-.5h2a.5.5 0 0 1 0 1h-2a.5.5 0 0 1-.5-.5"/>
            </svg>
          </div>
        </div>

        <!-- Icono de perfil -->
        <div class="dropdown dropdown-start ms-4">
          <a href="#" class="d-flex align-items-center link-dark text-decoration-none dropdown-toggle"
             id="dropdownUser2" data-bs-toggle="dropdown" aria-expanded="false">
            <img v-if="profilePhotoUrl" :src="profilePhotoUrl" alt="Profile Photo" width="40" height="40"
                 class="rounded-circle me-2">
          </a>
          <ul class="dropdown-menu text-small shadow dropdown-menu-end" aria-labelledby="dropdownUser2"
              style="">
            <li><a class="dropdown-item" href="#">New project...</a></li>
            <li><a class="dropdown-item" href="#">Settings</a></li>
            <li><a class="dropdown-item" href="#">Profile</a></li>
            <li>
              <hr class="dropdown-divider">
            </li>
            <li><a class="dropdown-item" href="#">Sign out</a></li>
          </ul>
        </div>
      </div>
    </div>
  </nav>

  <div v-show="showSearchSettingOverlay" id="overlay-search-setting">
    <div id="popup-search-setting">
      <h2 class="mb-4">Opciones de búsqueda</h2>
      <div class="search-option mb-3 d-flex flex-row justify-content-between align-items-center">
        <div class="bold me-3">Ubicación:</div>
        <select class="form-select" aria-label="Default select example" v-model="searchInFolder">
          <option :value="true">En la propia carpeta</option>
          <option :value="false">Cualquiera</option>
        </select>
      </div>
      <button class="btn btn-danger" @click="closeSearchSettingOverlay" id="closeBtn-search-setting">Cerrar</button>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      profilePhotoUrl: null,
      searchText: '',
      showSearchSettingOverlay: false
    };
  },
  computed: {
    searchInFolder: {
      get() {
        return this.$store.state.searchInFolder;
      },
      set(value) {
        this.$store.commit('setSearchInFolder', value);
      },
    },
  },
  mounted() {
    this.getProfilePhoto();
  },
  methods: {
    // Método para indicar que se buscarán archivos
    searchFiles() {
      // Navegar a la misma ruta pero con el parámetro de consulta searchText
      this.$router.push({query: {q: this.searchText}});
      this.searchText = '';
    },
    // Método para obtener la foto de perfil
    getProfilePhoto() {
      axios.get('/api/google-drive/profilePhoto')
          .then(response => {
            // Al recibir la respuesta, asignar la URL de la foto de perfil a profilePhotoUrl
            this.profilePhotoUrl = response.data;
          })
          .catch(error => {
            // Manejar errores de la petición
            console.error('Error al obtener la foto de perfil:', error);
          });
    },
    // Método para abrir el overlay de ajustes de búsqueda
    openSearchSettingOverlay() {
      this.showSearchSettingOverlay = true;
    },
    // Método para cerrar el overlay de ajustes de búsqueda
    closeSearchSettingOverlay() {
      this.showSearchSettingOverlay = false;
    }
  }
};
</script>

<style scoped>
.search_box {
  padding-left: 1.5%;
  padding-right: 1.5%;
  display: flex;
  align-items: center;
  margin: auto;
  width: 80%;
  max-width: 36rem;
  height: 54px;
  background-color: white;
  border-radius: 25px;
}

.select_area {
  color: #4451FE;
  display: flex;
  align-items: center;
  font-family: 'Roboto', sans-serif;
  line-height: 24px;
}

.text {
  padding-left: 1%;
}

.first_line {
  margin-left: 1.5%;
  border-left: 1px solid #D8D8D8;
  height: 40px;
}

.second_line {
  margin-left: 1.5%;
  margin-right: 1.5%;
  border-left: 1px solid #D8D8D8;
  height: 40px;
}

.search_text {
  flex: 1;
  border: none;
  margin-left: 1%;
  font-size: 16px;
  font-family: 'Roboto', sans-serif;
  line-height: 24px;
  width: 100%;
}

::-webkit-input-placeholder {
  color: #ADB1B8;
}

.text_and_icon {
  width: 100%;
  display: flex;
  align-items: center;
}

.search_icon {
  cursor: pointer;
  color: #5E6573;
  transition: all .6s ease-in-out;
  margin-left: 1%;
}

.search_icon:hover {
  color: #4451FE;
}

.search_setting {
  color: #5E6573;
  cursor: pointer;
}

.search_setting svg:hover {
  color: #4451FE;
}

.search_text:focus {
  outline: none !important;
}

.search_text:focus + .search_icon {
  color: #4451FE;
  transform: translate(5px, 0);
}

#overlay-search-setting {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 1000;
}

#popup-search-setting {
  display: flex;
  flex-direction: column;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.5);
}

.search-option {
  font-weight: bold;
  line-height: 18px;
}

#closeBtn-search-setting {
  margin-top: 10px;
}
</style>