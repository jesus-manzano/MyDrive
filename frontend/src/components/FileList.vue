<template>
  <div v-if="files.length > 0">
    <div class="d-flex justify-content-between  align-items-center mt-4 mb-4">
      <div class="display-6">Archivos</div>
      <div class="d-flex justify-content-end flex-column">
        <button v-if="$route.name === 'bin'" class="btn btn-danger" @click="deleteAllFiles">
          Vaciar papelera
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
               class="bi bi-trash3 mx-1" viewBox="0 0 16 16">
            <path
                d="M6.5 1h3a.5.5 0 0 1 .5.5v1H6v-1a.5.5 0 0 1 .5-.5M11 2.5v-1A1.5 1.5 0 0 0 9.5 0h-3A1.5 1.5 0 0 0 5 1.5v1H1.5a.5.5 0 0 0 0 1h.538l.853 10.66A2 2 0 0 0 4.885 16h6.23a2 2 0 0 0 1.994-1.84l.853-10.66h.538a.5.5 0 0 0 0-1zm1.958 1-.846 10.58a1 1 0 0 1-.997.92h-6.23a1 1 0 0 1-.997-.92L3.042 3.5zm-7.487 1a.5.5 0 0 1 .528.47l.5 8.5a.5.5 0 0 1-.998.06L5 5.03a.5.5 0 0 1 .47-.53Zm5.058 0a.5.5 0 0 1 .47.53l-.5 8.5a.5.5 0 1 1-.998-.06l.5-8.5a.5.5 0 0 1 .528-.47M8 4.5a.5.5 0 0 1 .5.5v8.5a.5.5 0 0 1-1 0V5a.5.5 0 0 1 .5-.5"/>
          </svg>
        </button>
        <!-- Dropdown de opciones varias carpetas -->
        <div class="btn-group mt-2" v-if="selectionMode">
          <button type="button" class="btn btn-primary d-flex align-items-center"
                  @click="toggleSelectionMode">
            Deseleccionar
            <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="currentColor"
                 class="bi bi-file-earmark-excel ms-1" viewBox="0 0 16 16">
              <path
                  d="M5.884 6.68a.5.5 0 1 0-.768.64L7.349 10l-2.233 2.68a.5.5 0 0 0 .768.64L8 10.781l2.116 2.54a.5.5 0 0 0 .768-.641L8.651 10l2.233-2.68a.5.5 0 0 0-.768-.64L8 9.219l-2.116-2.54z"/>
              <path
                  d="M14 14V4.5L9.5 0H4a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h8a2 2 0 0 0 2-2M9.5 3A1.5 1.5 0 0 0 11 4.5h2V14a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1h5.5z"/>
            </svg>
          </button>
          <button class="btn btn-primary dropdown-toggle dropdown-toggle-split" type="button" id="dropdownMenuButton"
                  data-bs-toggle="dropdown"
                  aria-expanded="false">
          </button>
          <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="dropdownMenuButton">
            <div v-if="$route.name === 'bin'">
              <li>
                <a class="dropdown-item" @click.prevent="restoreAllSelected">
                  Restaurar
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                       class="bi bi-arrow-counterclockwise" viewBox="0 0 16 16">
                    <path fill-rule="evenodd" d="M8 3a5 5 0 1 1-4.546 2.914.5.5 0 0 0-.908-.417A6 6 0 1 0 8 2z"/>
                    <path
                        d="M8 4.466V.534a.25.25 0 0 0-.41-.192L5.23 2.308a.25.25 0 0 0 0 .384l2.36 1.966A.25.25 0 0 0 8 4.466"/>
                  </svg>
                </a>
              </li>
              <li>
                <a class="dropdown-item" @click.prevent="deleteAllSelected">
                  Eliminar
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                       class="bi bi-trash3" viewBox="0 0 16 16">
                    <path
                        d="M6.5 1h3a.5.5 0 0 1 .5.5v1H6v-1a.5.5 0 0 1 .5-.5M11 2.5v-1A1.5 1.5 0 0 0 9.5 0h-3A1.5 1.5 0 0 0 5 1.5v1H1.5a.5.5 0 0 0 0 1h.538l.853 10.66A2 2 0 0 0 4.885 16h6.23a2 2 0 0 0 1.994-1.84l.853-10.66h.538a.5.5 0 0 0 0-1zm1.958 1-.846 10.58a1 1 0 0 1-.997.92h-6.23a1 1 0 0 1-.997-.92L3.042 3.5zm-7.487 1a.5.5 0 0 1 .528.47l.5 8.5a.5.5 0 0 1-.998.06L5 5.03a.5.5 0 0 1 .47-.53Zm5.058 0a.5.5 0 0 1 .47.53l-.5 8.5a.5.5 0 1 1-.998-.06l.5-8.5a.5.5 0 0 1 .528-.47M8 4.5a.5.5 0 0 1 .5.5v8.5a.5.5 0 0 1-1 0V5a.5.5 0 0 1 .5-.5"/>
                  </svg>
                </a>
              </li>
            </div>
            <div v-else>
              <li>
                <a class="dropdown-item" @click.prevent="downloadAllSelected">
                  Descargar
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                       class="bi bi-download" viewBox="0 0 16 16">
                    <path
                        d="M.5 9.9a.5.5 0 0 1 .5.5v2.5a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1v-2.5a.5.5 0 0 1 1 0v2.5a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2v-2.5a.5.5 0 0 1 .5-.5"/>
                    <path
                        d="M7.646 11.854a.5.5 0 0 0 .708 0l3-3a.5.5 0 0 0-.708-.708L8.5 10.293V1.5a.5.5 0 0 0-1 0v8.793L5.354 8.146a.5.5 0 1 0-.708.708z"/>
                  </svg>
                </a>
              </li>
              <li>
                <a class="dropdown-item" @click.prevent="openMoveSelectedOverlay">
                  Mover
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                       class="bi bi-folder-symlink" viewBox="0 0 16 16">
                    <path
                        d="m11.798 8.271-3.182 1.97c-.27.166-.616-.036-.616-.372V9.1s-2.571-.3-4 2.4c.571-4.8 3.143-4.8 4-4.8v-.769c0-.336.346-.538.616-.371l3.182 1.969c.27.166.27.576 0 .742"/>
                    <path
                        d="m.5 3 .04.87a2 2 0 0 0-.342 1.311l.637 7A2 2 0 0 0 2.826 14h10.348a2 2 0 0 0 1.991-1.819l.637-7A2 2 0 0 0 13.81 3H9.828a2 2 0 0 1-1.414-.586l-.828-.828A2 2 0 0 0 6.172 1H2.5a2 2 0 0 0-2 2m.694 2.09A1 1 0 0 1 2.19 4h11.62a1 1 0 0 1 .996 1.09l-.636 7a1 1 0 0 1-.996.91H2.826a1 1 0 0 1-.995-.91zM6.172 2a1 1 0 0 1 .707.293L7.586 3H2.19q-.362.002-.683.12L1.5 2.98a1 1 0 0 1 1-.98z"/>
                  </svg>
                </a>
              </li>
              <li>
                <a class="dropdown-item" @click.prevent="throwAwayAllSelected">
                  Papelera
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                       class="bi bi-trash3" viewBox="0 0 16 16">
                    <path
                        d="M6.5 1h3a.5.5 0 0 1 .5.5v1H6v-1a.5.5 0 0 1 .5-.5M11 2.5v-1A1.5 1.5 0 0 0 9.5 0h-3A1.5 1.5 0 0 0 5 1.5v1H1.5a.5.5 0 0 0 0 1h.538l.853 10.66A2 2 0 0 0 4.885 16h6.23a2 2 0 0 0 1.994-1.84l.853-10.66h.538a.5.5 0 0 0 0-1zm1.958 1-.846 10.58a1 1 0 0 1-.997.92h-6.23a1 1 0 0 1-.997-.92L3.042 3.5zm-7.487 1a.5.5 0 0 1 .528.47l.5 8.5a.5.5 0 0 1-.998.06L5 5.03a.5.5 0 0 1 .47-.53Zm5.058 0a.5.5 0 0 1 .47.53l-.5 8.5a.5.5 0 1 1-.998-.06l.5-8.5a.5.5 0 0 1 .528-.47M8 4.5a.5.5 0 0 1 .5.5v8.5a.5.5 0 0 1-1 0V5a.5.5 0 0 1 .5-.5"/>
                  </svg>
                </a>
              </li>
            </div>
          </ul>
        </div>
        <button v-else type="button" class="btn btn-primary d-flex align-items-center mt-2 justify-content-center"
                @click="toggleSelectionMode">
          Seleccionar
          <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="currentColor"
               class="bi bi-file-earmark-check ms-1" viewBox="0 0 16 16">
            <path
                d="M10.854 7.854a.5.5 0 0 0-.708-.708L7.5 9.793 6.354 8.646a.5.5 0 1 0-.708.708l1.5 1.5a.5.5 0 0 0 .708 0z"/>
            <path
                d="M14 14V4.5L9.5 0H4a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h8a2 2 0 0 0 2-2M9.5 3A1.5 1.5 0 0 0 11 4.5h2V14a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1h5.5z"/>
          </svg>
        </button>
      </div>
    </div>

    <div class="d-flex flex-wrap justify-content-center text-center">
      <div class="mx-4 mb-5 rounded-4" style="width: 11rem;" v-for="(file, index) in files" :key="file.id">
        <a id="file-card" href="" class="card" :class="{ 'transform-card-selected': file.selected }"
           @click.prevent="handleClickFile(file, $event)">
          <img :src="file.thumbnailLink ? file.thumbnailLink : require('@/assets/file.png')"
               class="card__image bg-light" alt="Imagen" :class="{ 'saturate-img': file.selected }"/>
          <div v-show="selectionMode" class="circle-icon ps-2 pb-2 pt-3 pe-3"
               @click.prevent.stop="toggleSelected(file)">
            <svg v-if="file.selected" xmlns="http://www.w3.org/2000/svg" width="24" height="24"
                 fill="rgba(var(--bs-primary-rgb)" class="bi bi-check2-circle" viewBox="0 0 16 16">
              <path
                  d="M2.5 8a5.5 5.5 0 0 1 8.25-4.764.5.5 0 0 0 .5-.866A6.5 6.5 0 1 0 14.5 8a.5.5 0 0 0-1 0 5.5 5.5 0 1 1-11 0"/>
              <path
                  d="M15.354 3.354a.5.5 0 0 0-.708-.708L8 9.293 5.354 6.646a.5.5 0 1 0-.708.708l3 3a.5.5 0 0 0 .708 0z"/>
            </svg>
            <svg v-else xmlns="http://www.w3.org/2000/svg" width="21" height="21" fill="rgba(var(--bs-primary-rgb)"
                 class="bi bi-circle"
                 viewBox="0 0 16 16">
              <path d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14m0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16"/>
            </svg>
          </div>
          <div class="card__overlay" :class="{ 'transform-card-up': file.showOverlay }"
               @click.prevent.stop="toggleCardStyle(file)">
            <div class="card__header" :class="{ 'transform-card-up': file.showOverlay }">
              <svg class="card__arc" xmlns="http://www.w3.org/2000/svg"
                   @click.prevent.stop="handleClickFile(file, $event)">
                <path/>
              </svg>
              <div class="card__header-text">
                <div class="d-flex flex-row justify-content-center align-items-center">
                  <div class="card__title h3">{{ file.name }}</div>
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="rgb(50, 50, 50, 0.6)"
                       class="bi bi-caret-down-fill" viewBox="0 0 16 16">
                    <path
                        d="M7.247 11.14 2.451 5.658C1.885 5.013 2.345 4 3.204 4h9.592a1 1 0 0 1 .753 1.659l-4.796 5.48a1 1 0 0 1-1.506 0z"/>
                  </svg>
                </div>
                <span class="card__status">{{ file.lastTimeViewed }}</span>
              </div>
            </div>
            <div class="options-container" style="max-height: 140px; overflow-y: auto;">
              <button class="btn btn-light mb-1" style="width: 8rem;"
                      @click.prevent="openFile(file, $event)">
                Abrir
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                     class="bi bi-box-arrow-right" viewBox="0 0 16 16">
                  <path fill-rule="evenodd"
                        d="M10 12.5a.5.5 0 0 1-.5.5h-8a.5.5 0 0 1-.5-.5v-9a.5.5 0 0 1 .5-.5h8a.5.5 0 0 1 .5.5v2a.5.5 0 0 0 1 0v-2A1.5 1.5 0 0 0 9.5 2h-8A1.5 1.5 0 0 0 0 3.5v9A1.5 1.5 0 0 0 1.5 14h8a1.5 1.5 0 0 0 1.5-1.5v-2a.5.5 0 0 0-1 0z"/>
                  <path fill-rule="evenodd"
                        d="M15.854 8.354a.5.5 0 0 0 0-.708l-3-3a.5.5 0 0 0-.708.708L14.293 7.5H5.5a.5.5 0 0 0 0 1h8.793l-2.147 2.146a.5.5 0 0 0 .708.708z"/>
                </svg>
              </button>
              <div v-if="$route.name != 'bin'">
                <button class="btn btn-light my-1" style="width: 8rem;"
                        @click.prevent.stop="handleDownloadFile(file)">
                  Descargar
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                       class="bi bi-download" viewBox="0 0 16 16">
                    <path
                        d="M.5 9.9a.5.5 0 0 1 .5.5v2.5a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1v-2.5a.5.5 0 0 1 1 0v2.5a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2v-2.5a.5.5 0 0 1 .5-.5"/>
                    <path
                        d="M7.646 11.854a.5.5 0 0 0 .708 0l3-3a.5.5 0 0 0-.708-.708L8.5 10.293V1.5a.5.5 0 0 0-1 0v8.793L5.354 8.146a.5.5 0 1 0-.708.708z"/>
                  </svg>
                </button>
                <button class="btn btn-light mt-1 mb-2" style="width: 8rem;"
                        @click.prevent.stop="openMoveFileOverlay(file)">
                  Mover
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                       class="bi bi-folder-symlink" viewBox="0 0 16 16">
                    <path
                        d="m11.798 8.271-3.182 1.97c-.27.166-.616-.036-.616-.372V9.1s-2.571-.3-4 2.4c.571-4.8 3.143-4.8 4-4.8v-.769c0-.336.346-.538.616-.371l3.182 1.969c.27.166.27.576 0 .742"/>
                    <path
                        d="m.5 3 .04.87a2 2 0 0 0-.342 1.311l.637 7A2 2 0 0 0 2.826 14h10.348a2 2 0 0 0 1.991-1.819l.637-7A2 2 0 0 0 13.81 3H9.828a2 2 0 0 1-1.414-.586l-.828-.828A2 2 0 0 0 6.172 1H2.5a2 2 0 0 0-2 2m.694 2.09A1 1 0 0 1 2.19 4h11.62a1 1 0 0 1 .996 1.09l-.636 7a1 1 0 0 1-.996.91H2.826a1 1 0 0 1-.995-.91zM6.172 2a1 1 0 0 1 .707.293L7.586 3H2.19q-.362.002-.683.12L1.5 2.98a1 1 0 0 1 1-.98z"/>
                  </svg>
                </button>
                <button class="btn btn-light mt-1 mb-2" style="width: 8rem;"
                        @click.prevent.stop="openRenameFileOverlay(file)">
                  Renombrar
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                       class="bi bi-pencil" viewBox="0 0 16 16">
                    <path
                        d="M12.146.146a.5.5 0 0 1 .708 0l3 3a.5.5 0 0 1 0 .708l-10 10a.5.5 0 0 1-.168.11l-5 2a.5.5 0 0 1-.65-.65l2-5a.5.5 0 0 1 .11-.168zM11.207 2.5 13.5 4.793 14.793 3.5 12.5 1.207zm1.586 3L10.5 3.207 4 9.707V10h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.293zm-9.761 5.175-.106.106-1.528 3.821 3.821-1.528.106-.106A.5.5 0 0 1 5 12.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.468-.325"/>
                  </svg>
                </button>
                <button class="btn btn-light mt-1 mb-2" style="width: 8rem;"
                        @click.prevent.stop="throwAwayFile(file.id, index)">
                  Papelera
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                       class="bi bi-trash3" viewBox="0 0 16 16">
                    <path
                        d="M6.5 1h3a.5.5 0 0 1 .5.5v1H6v-1a.5.5 0 0 1 .5-.5M11 2.5v-1A1.5 1.5 0 0 0 9.5 0h-3A1.5 1.5 0 0 0 5 1.5v1H1.5a.5.5 0 0 0 0 1h.538l.853 10.66A2 2 0 0 0 4.885 16h6.23a2 2 0 0 0 1.994-1.84l.853-10.66h.538a.5.5 0 0 0 0-1zm1.958 1-.846 10.58a1 1 0 0 1-.997.92h-6.23a1 1 0 0 1-.997-.92L3.042 3.5zm-7.487 1a.5.5 0 0 1 .528.47l.5 8.5a.5.5 0 0 1-.998.06L5 5.03a.5.5 0 0 1 .47-.53Zm5.058 0a.5.5 0 0 1 .47.53l-.5 8.5a.5.5 0 1 1-.998-.06l.5-8.5a.5.5 0 0 1 .528-.47M8 4.5a.5.5 0 0 1 .5.5v8.5a.5.5 0 0 1-1 0V5a.5.5 0 0 1 .5-.5"/>
                  </svg>
                </button>
              </div>
              <div v-if="$route.name === 'bin'">
                <button class="btn btn-primary mt-1 mb-2" style="width: 8rem;"
                        @click.prevent.stop="restoreFile(file.id, index)">
                  Restaurar
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                       class="bi bi-arrow-counterclockwise" viewBox="0 0 16 16">
                    <path fill-rule="evenodd" d="M8 3a5 5 0 1 1-4.546 2.914.5.5 0 0 0-.908-.417A6 6 0 1 0 8 2z"/>
                    <path
                        d="M8 4.466V.534a.25.25 0 0 0-.41-.192L5.23 2.308a.25.25 0 0 0 0 .384l2.36 1.966A.25.25 0 0 0 8 4.466"/>
                  </svg>
                </button>
                <button class="btn btn-danger mt-1 mb-2" style="width: 8rem;"
                        @click.prevent.stop="deleteFile(file.id, index)">
                  Eliminar
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                       class="bi bi-trash3" viewBox="0 0 16 16">
                    <path
                        d="M6.5 1h3a.5.5 0 0 1 .5.5v1H6v-1a.5.5 0 0 1 .5-.5M11 2.5v-1A1.5 1.5 0 0 0 9.5 0h-3A1.5 1.5 0 0 0 5 1.5v1H1.5a.5.5 0 0 0 0 1h.538l.853 10.66A2 2 0 0 0 4.885 16h6.23a2 2 0 0 0 1.994-1.84l.853-10.66h.538a.5.5 0 0 0 0-1zm1.958 1-.846 10.58a1 1 0 0 1-.997.92h-6.23a1 1 0 0 1-.997-.92L3.042 3.5zm-7.487 1a.5.5 0 0 1 .528.47l.5 8.5a.5.5 0 0 1-.998.06L5 5.03a.5.5 0 0 1 .47-.53Zm5.058 0a.5.5 0 0 1 .47.53l-.5 8.5a.5.5 0 1 1-.998-.06l.5-8.5a.5.5 0 0 1 .528-.47M8 4.5a.5.5 0 0 1 .5.5v8.5a.5.5 0 0 1-1 0V5a.5.5 0 0 1 .5-.5"/>
                  </svg>
                </button>
              </div>
            </div>
          </div>
        </a>
      </div>
    </div>
  </div>

  <!-- Overlay para descargar un archivo cifrado -->
  <div v-show="showDownloadEncryptedFileOverlay" class="overlay-area">
    <div class="popup-area">
      <h2 class="mb-3">Introduce la contraseña del archivo</h2>
      <div class="d-flex justify-content-between align-items-center mb-3">
        <div class="bold me-3">Contraseña:</div>
        <input type="password" class="form-control" placeholder="Contraseña" v-model="password"
               @keyup.enter="downloadEncryptedFile">
      </div>
      <div class="d-flex justify-content-between align-items-center">
        <button class="btn btn-danger" @click="closeDownloadEncryptedFileOverlay">Cancelar</button>
        <button class="btn btn-success" @click="downloadEncryptedFile">Descargar</button>
      </div>
    </div>
  </div>

  <!-- Overlay para renombrar un archivo -->
  <div v-show="showRenameFileOverlay" class="overlay-area">
    <div class="popup-area">
      <h2 class="mb-3">Renombrar archivo</h2>
      <div class="mb-3 text-muted">
        <bold class="bold fs-5 text-dark">Info:</bold>
        Escriba el nombre sin la extensión. Esta seguirá siendo igual que la anterior.
      </div>
      <div class="d-flex justify-content-between align-items-center mb-3">
        <div class="bold me-3">Nombre:</div>
        <input type="text" class="form-control" placeholder="Nuevo nombre..." v-model="fileName"
               @keyup.enter="renameFile">
      </div>
      <div class="d-flex justify-content-between align-items-center">
        <button class="btn btn-danger" @click="closeRenameFileOverlay">Cancelar</button>
        <button class="btn btn-success" @click="renameFile">Aceptar</button>
      </div>
    </div>
  </div>

  <!-- Overlay para mover un archivo -->
  <div v-show="showMoveFileOverlay" class="overlay-area">
    <div class="popup-area">
      <h3 v-if="showMoveFileOverlay" class="mb-3">Mover archivo: "{{ fileSelected.name }}"</h3>
      <div class="d-flex flex-row justify-content-between align-items-center">
        <div class="d-flex flex-row align-items-center">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-folder-fill"
               viewBox="0 0 16 16">
            <path
                d="M9.828 3h3.982a2 2 0 0 1 1.992 2.181l-.637 7A2 2 0 0 1 13.174 14H2.825a2 2 0 0 1-1.991-1.819l-.637-7a2 2 0 0 1 .342-1.31L.5 3a2 2 0 0 1 2-2h3.672a2 2 0 0 1 1.414.586l.828.828A2 2 0 0 0 9.828 3m-8.322.12q.322-.119.684-.12h5.396l-.707-.707A1 1 0 0 0 6.172 2H2.5a1 1 0 0 0-1 .981z"/>
          </svg>
          <div v-if="showMoveFileOverlay" class="ms-1 bold">
            {{ this.fileMovePath[this.fileMovePath.length - 1].name }}
          </div>
        </div>
        <div class="d-flex flex-row align-items-center" style="cursor: pointer;" @click="backFolder">
          <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="rgb(50, 50, 50, 0.9)"
               class="bi bi-arrow-left-circle"
               viewBox="0 0 16 16">
            <path fill-rule="evenodd"
                  d="M1 8a7 7 0 1 0 14 0A7 7 0 0 0 1 8m15 0A8 8 0 1 1 0 8a8 8 0 0 1 16 0m-4.5-.5a.5.5 0 0 1 0 1H5.707l2.147 2.146a.5.5 0 0 1-.708.708l-3-3a.5.5 0 0 1 0-.708l3-3a.5.5 0 1 1 .708.708L5.707 7.5z"/>
          </svg>
          <div class="m-0 h6 ms-1" style="color: rgb(50, 50, 50, 0.9);">Atrás</div>
        </div>
      </div>
      <hr class="my-2">
      <div class="d-flex flex-column mb-2" style="max-height: 296px; overflow-y: auto;">
        <div v-if="folders.length === 0" class="text-secondary">No hay ninguna carpeta en la carpeta actual</div>
        <div v-for="folder in folders" :key="folder.id"
             class="d-flex justify-content-between align-items-center px-2 py-1 mb-1 me-2 move-file-row">
          <div class="d-flex flex-row align-items-center me-4">
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-folder-fill"
                 viewBox="0 0 16 16">
              <path
                  d="M9.828 3h3.982a2 2 0 0 1 1.992 2.181l-.637 7A2 2 0 0 1 13.174 14H2.825a2 2 0 0 1-1.991-1.819l-.637-7a2 2 0 0 1 .342-1.31L.5 3a2 2 0 0 1 2-2h3.672a2 2 0 0 1 1.414.586l.828.828A2 2 0 0 0 9.828 3m-8.322.12q.322-.119.684-.12h5.396l-.707-.707A1 1 0 0 0 6.172 2H2.5a1 1 0 0 0-1 .981z"/>
            </svg>
            <div class="ms-1 bold">{{ folder.name }}</div>
          </div>
          <div class="d-flex flex-row align-items-center">
            <button class="btn btn-primary move-here me-2" @click="moveFile(folder.id)">Mover</button>
            <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" fill="currentColor"
                 class="bi bi-arrow-bar-right move-to" viewBox="0 0 16 16" @click="moveToFolder(folder)">
              <path fill-rule="evenodd"
                    d="M6 8a.5.5 0 0 0 .5.5h5.793l-2.147 2.146a.5.5 0 0 0 .708.708l3-3a.5.5 0 0 0 0-.708l-3-3a.5.5 0 0 0-.708.708L12.293 7.5H6.5A.5.5 0 0 0 6 8m-2.5 7a.5.5 0 0 1-.5-.5v-13a.5.5 0 0 1 1 0v13a.5.5 0 0 1-.5.5"/>
            </svg>
          </div>
        </div>
      </div>
      <hr class="my-2">
      <span class="text-center fs-4">Nubes disponibles</span>
      <div class="btn-group my-3" role="group" aria-label="Basic radio toggle button group">
        <input type="radio" class="btn-check">
        <label class="btn" :class="{ active: moveCloudService === 'google-drive',
               'btn-outline-secondary': !isAuthenticated['google-drive'],
               'btn-outline-primary': isAuthenticated['google-drive'] }"
               @click="selectMoveCloudService('google-drive')">
          Google Drive
        </label>

        <input type="radio" class="btn-check">
        <label class="btn" :class="{ active: moveCloudService === 'dropbox',
               'btn-outline-secondary': !isAuthenticated['dropbox'],
               'btn-outline-primary': isAuthenticated['dropbox'] }"
               @click="selectMoveCloudService('dropbox')">
          Dropbox
        </label>
      </div>
      <div class="d-flex justify-content-between align-items-center">
        <button class="btn btn-danger" @click="closeMoveFileOverlay">Cancelar</button>
        <button class="btn btn-success" @click="moveFile(this.fileMovePath[this.fileMovePath.length - 1].id)">
          Mover Aquí
        </button>
      </div>
    </div>
  </div>

  <!-- Overlay para mover los archivos seleccionados -->
  <div v-show="showMoveSelectedOverlay" class="overlay-area">
    <div class="popup-area">
      <h3 v-if="showMoveSelectedOverlay" class="mb-3">Mover archivos seleccionados</h3>
      <div class="d-flex flex-row justify-content-between align-items-center">
        <div class="d-flex flex-row align-items-center">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-folder-fill"
               viewBox="0 0 16 16">
            <path
                d="M9.828 3h3.982a2 2 0 0 1 1.992 2.181l-.637 7A2 2 0 0 1 13.174 14H2.825a2 2 0 0 1-1.991-1.819l-.637-7a2 2 0 0 1 .342-1.31L.5 3a2 2 0 0 1 2-2h3.672a2 2 0 0 1 1.414.586l.828.828A2 2 0 0 0 9.828 3m-8.322.12q.322-.119.684-.12h5.396l-.707-.707A1 1 0 0 0 6.172 2H2.5a1 1 0 0 0-1 .981z"/>
          </svg>
          <div v-if="showMoveSelectedOverlay" class="ms-1 bold">
            {{ this.fileMovePath[this.fileMovePath.length - 1].name }}
          </div>
        </div>
        <div class="d-flex flex-row align-items-center" style="cursor: pointer;" @click="backFolder">
          <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="rgb(50, 50, 50, 0.9)"
               class="bi bi-arrow-left-circle"
               viewBox="0 0 16 16">
            <path fill-rule="evenodd"
                  d="M1 8a7 7 0 1 0 14 0A7 7 0 0 0 1 8m15 0A8 8 0 1 1 0 8a8 8 0 0 1 16 0m-4.5-.5a.5.5 0 0 1 0 1H5.707l2.147 2.146a.5.5 0 0 1-.708.708l-3-3a.5.5 0 0 1 0-.708l3-3a.5.5 0 1 1 .708.708L5.707 7.5z"/>
          </svg>
          <div class="m-0 h6 ms-1" style="color: rgb(50, 50, 50, 0.9);">Atrás</div>
        </div>
      </div>
      <hr class="my-2">
      <div class="d-flex flex-column mb-2" style="max-height: 296px; overflow-y: auto;">
        <div v-if="folders.length === 0" class="text-secondary">No hay ninguna carpeta en la carpeta actual</div>
        <div v-for="folder in folders" :key="folder.id"
             class="d-flex justify-content-between align-items-center px-2 py-1 mb-1 me-2 move-file-row">
          <div class="d-flex flex-row align-items-center me-4">
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-folder-fill"
                 viewBox="0 0 16 16">
              <path
                  d="M9.828 3h3.982a2 2 0 0 1 1.992 2.181l-.637 7A2 2 0 0 1 13.174 14H2.825a2 2 0 0 1-1.991-1.819l-.637-7a2 2 0 0 1 .342-1.31L.5 3a2 2 0 0 1 2-2h3.672a2 2 0 0 1 1.414.586l.828.828A2 2 0 0 0 9.828 3m-8.322.12q.322-.119.684-.12h5.396l-.707-.707A1 1 0 0 0 6.172 2H2.5a1 1 0 0 0-1 .981z"/>
            </svg>
            <div class="ms-1 bold">{{ folder.name }}</div>
          </div>
          <div class="d-flex flex-row align-items-center">
            <button class="btn btn-primary move-here me-2" @click="moveAllSelected(folder.id)">Mover</button>
            <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" fill="currentColor"
                 class="bi bi-arrow-bar-right move-to" viewBox="0 0 16 16" @click="moveToFolder(folder)">
              <path fill-rule="evenodd"
                    d="M6 8a.5.5 0 0 0 .5.5h5.793l-2.147 2.146a.5.5 0 0 0 .708.708l3-3a.5.5 0 0 0 0-.708l-3-3a.5.5 0 0 0-.708.708L12.293 7.5H6.5A.5.5 0 0 0 6 8m-2.5 7a.5.5 0 0 1-.5-.5v-13a.5.5 0 0 1 1 0v13a.5.5 0 0 1-.5.5"/>
            </svg>
          </div>
        </div>
      </div>
      <hr class="my-2">
      <span class="text-center fs-4">Nubes disponibles</span>
      <div class="btn-group my-3" role="group" aria-label="Basic radio toggle button group">
        <input type="radio" class="btn-check">
        <label class="btn" :class="{ active: moveCloudService === 'google-drive',
               'btn-outline-secondary': !isAuthenticated['google-drive'],
               'btn-outline-primary': isAuthenticated['google-drive'] }"
               @click="selectMoveCloudService('google-drive')">
          Google Drive
        </label>

        <input type="radio" class="btn-check">
        <label class="btn" :class="{ active: moveCloudService === 'dropbox',
               'btn-outline-secondary': !isAuthenticated['dropbox'],
               'btn-outline-primary': isAuthenticated['dropbox'] }"
               @click="selectMoveCloudService('dropbox')">
          Dropbox
        </label>
      </div>
      <div class="d-flex justify-content-between align-items-center">
        <button class="btn btn-danger" @click="closeMoveSelectedOverlay">Cancelar</button>
        <button class="btn btn-success" @click="moveAllSelected(this.fileMovePath[this.fileMovePath.length - 1].id)">
          Mover Aquí
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import {mapMutations, mapState} from 'vuex';
import VsToast from '@vuesimple/vs-toast';

export default {
  name: "FileList",
  data() {
    return {
      files: [],
      showRenameFileOverlay: false,
      fileName: '',
      fileSelected: null,
      showMoveFileOverlay: false,
      folders: [],
      fileMovePath: [],
      selectionMode: false,
      showMoveSelectedOverlay: false,
      moveCloudService: '',
      showDownloadEncryptedFileOverlay: false,
      password: ''
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
    period: {
      type: String,
      default: 'week'
    },
  },
  computed: {
    ...mapState(['cloudService']),
    ...mapState(['isAuthenticated'])
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
    period() {
      this.getFiles();
    },
    '$store.state.searchInFolder'(newValue, oldValue) {
      if (newValue !== oldValue) {
        this.getFiles();
      }
    },
    '$route.name'() {
      this.getFiles();
    },
    cloudService() {
      if (this.$route.path === '/filemanager/root') {
        this.getFiles();
      }
    }
  },
  mounted() {
    this.getFiles();
  },
  methods: {
    // Método para saber si el usuario ya ha seleccionado una nube
    isSelectedCloudService() {
      return this.cloudService !== '';
    },
    // Método para definir el endpoint según la ruta en la que nos encontramos
    // y dependiendo de la configuración del cliente
    getEndpoint() {
      let searching = (this.$route.query.q || '') !== ''; // Si tiene algún valor q considera que se está buscando
      let endpoint = '';

      // Dependiendo de la vista en la que nos encontremos
      switch (this.$route.name) {
        case 'filemanager':
          if (searching && !this.$store.state.searchInFolder) { // Si está buscando de forma global
            endpoint = '/api/' + this.cloudService + '/searchFile/' + this.searchText;
          } else endpoint = '/api/' + this.cloudService + '/files/' + this.currentFolderId + '?q=' + this.searchText;
          break;
        case 'recent':
          endpoint = `/api/` + this.cloudService + `/recentFiles?maxDate=` + this.getMaxDateByPeriod() + '&q=' + this.searchText;
          break;
        case 'bin':
          endpoint = `/api/` + this.cloudService + `/files/bin?q=` + this.searchText;
      }

      return endpoint;
    },
    // Método para obtener todos los archivos que no son directorios
    getFiles() {
      this.files = []; // Limpiamos los archivos actuales
      this.clearSelection(); // Ningún archivo seleccionado
      if (this.cloudService === '' || !this.isAuthenticated[this.cloudService]) return; // No ejecutamos

      const endpoint = this.getEndpoint();
      if (this.cloudService !== '' && endpoint !== '') {
        axios.get(endpoint)
            .then(response => {
              this.files = response.data;

              this.sortFiles();
              this.setHasFiles(this.files.length > 0);
            })
            .catch(error => {
              console.error(error); // Mensaje del servidor
              this.$router.push({
                name: 'ErrorView',
                query: {code: 500, message: 'Error al obtener todos los archivos'}
              });
            });
      }
    },
    // Método para abrir un archivo en el navegador sin descargar
    openFile(file, event) {
      file.selected = false;
      file.showOverlay = false;

      // Llamar a tu backend para obtener el enlace de previsualización
      axios.get(`/api/` + this.cloudService + `/preview-link/${file.id}`)
          .then(response => {
            const url = response.data;

            console.log("URL: " + url);

            // Si la tecla Control está presionada, abrir en una nueva pestaña
            if (event.ctrlKey || event.metaKey) {
              window.open(url, '_blank');
            } else {
              window.location.href = url;
            }
          })
          .catch(error => {
            console.error(error); // Mensaje del servidor
            VsToast.show({title: 'Error al abrir el archivo', variant: 'error', position: 'bottom-center'});
          });
    },
    // Método para descargar un archivo
    downloadFile(file) {
      axios.get(`/api/` + this.cloudService + `/download/${file.id}`, {
        responseType: 'blob' // Indica que la respuesta será un blob (binario)
      })
          .then(response => {
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', file.name);
            document.body.appendChild(link);
            link.click();
            VsToast.show({title: 'Archivo descargado con éxito', variant: 'success', position: 'bottom-center'});
          })
          .catch(error => {
            console.error(error); // Mensaje del servidor
            VsToast.show({title: 'Error al descargar el archivo', variant: 'error', position: 'bottom-center'});
          });
    },
    downloadEncryptedFile() {
      const formData = new FormData();
      formData.append('password', this.password);

      axios.post(`/api/${this.cloudService}/downloadEncryptedFile/${this.fileSelected.id}`, formData, {
        responseType: 'blob' // Indica que la respuesta será un blob (binario)
      })
          .then(response => {
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', this.fileSelected.name);
            document.body.appendChild(link);
            link.click();
            this.fileSelected = null;
            this.password = '';
            this.showDownloadEncryptedFileOverlay = false;
            VsToast.show({title: 'Archivo descargado con éxito', variant: 'success', position: 'bottom-center'});
          })
          .catch(error => {
            console.error(error); // Mensaje del servidor
            VsToast.show({title: 'Error al descargar el archivo', variant: 'error', position: 'bottom-center'});
          });
    },
    // Método para descargar todos los archivos seleccionados
    downloadAllSelected() {
      this.files.forEach(file => {
        if (file.selected) {
          this.downloadFile(file);
        }
      });
      this.clearSelection();
    },
    handleDownloadFile(file) {
      if (file.encrypted)
        this.openDownloadEncryptedFileOverlay(file);
      else this.downloadFile(file);
    },
    moveFileToOtherCloud(folderId) {
      // Enviar una solicitud al backend para mover dicho archivo a la carpeta indicada en la otra nube
      axios.post(`/api/` + this.cloudService + `/moveFile/` + this.fileSelected.id
          + `/` + this.moveCloudService + `/` + folderId)
          .then(response => {
            console.log('Archivo movido exitosamente:', response.data);
            this.getFiles();

            this.fileSelected = null;
            this.fileMovePath = [];
            this.showMoveFileOverlay = false;
            VsToast.show({title: 'Archivo movido con éxito', variant: 'success', position: 'bottom-center'});
          })
          .catch(error => {
            console.error(error);
            VsToast.show({title: 'Error al mover el archivo', variant: 'error', position: 'bottom-center'});
          });
    },
    // Método para mover un archivo
    moveFile(folderId) {
      if (this.fileSelected) {
        // Detectar si hay que mover dentro de la misma nube o entre nubes
        if (this.moveCloudService !== this.cloudService) {
          this.moveFileToOtherCloud(folderId);
        } else {
          // Enviar una solicitud al backend para mover dicho archivo a la carpeta indicada dentro de la misma nube
          axios.put(`/api/` + this.moveCloudService + `/moveFile/` + this.fileSelected.id + '?folderId=' + folderId)
              .then(response => {
                console.log('Archivo movido exitosamente:', response.data);
                this.getFiles();

                this.fileSelected = null;
                this.fileMovePath = [];
                this.showMoveFileOverlay = false;
                VsToast.show({title: 'Archivo movido con éxito', variant: 'success', position: 'bottom-center'});
              })
              .catch(error => {
                console.error(error);
                VsToast.show({title: 'Error al mover el archivo', variant: 'error', position: 'bottom-center'});
              });
        }
      }
    },
    //Método para mover todos los archivos seleccionados
    moveAllSelected(targetFolderId) {
      let completedRequests = 0;
      const totalRequests = this.files.filter(file => file.selected).length;
      this.files.forEach(file => {
        if (file.selected) {
          if (this.moveCloudService !== this.cloudService) {
            this.moveFileToOtherCloud(targetFolderId);
          } else {
            // Enviar una solicitud al backend para mover dicho archivo a la carpeta indicada dentro de la misma nube
            axios.put(`/api/` + this.moveCloudService + `/moveFile/` + file.id + '?folderId=' + targetFolderId)
                .then(() => {
                  VsToast.show({title: 'Archivo movido con éxito', variant: 'success', position: 'bottom-center'});
                })
                .catch(error => {
                  console.error(error);
                  VsToast.show({title: 'Error al mover el archivo', variant: 'error', position: 'bottom-center'});
                })
                .finally(() => {
                  completedRequests++;

                  if (completedRequests === totalRequests) {
                    this.getFiles();
                    this.clearSelection();
                    this.showMoveSelectedOverlay = false;
                  }
                });
          }
        }
      });
    },
    // Método para renombrar un archivo
    renameFile() {
      if (this.fileSelected) {
        // Obtener el nombre original y la extensión del archivo seleccionado
        const originalFileName = this.fileSelected.name;
        const originalFileExtension = originalFileName.split('.').pop(); // Obtiene la extensión

        // Eliminar la extensión del nombre de archivo si existe
        let newFileName = this.fileName;
        const lastDotIndex = newFileName.lastIndexOf('.');
        if (lastDotIndex !== -1) {
          newFileName = newFileName.substring(0, lastDotIndex);
        }

        newFileName += '.' + originalFileExtension;

        // Enviar una solicitud al backend para renombrar la carpeta
        axios.put(`/api/` + this.cloudService + `/renameFile/` + this.fileSelected.id + '?name=' + newFileName)
            .then(() => {
              this.fileSelected.name = newFileName;
              this.sortFiles();

              this.fileName = '';
              this.fileSelected = null;
              this.showRenameFileOverlay = false;
              VsToast.show({title: 'Archivo renombrado con éxito', variant: 'success', position: 'bottom-center'});
            })
            .catch(error => {
              console.log(error);
              VsToast.show({title: 'Error al mover el archivo', variant: 'error', position: 'bottom-center'});
            });
      }
    },
    // Método para enviar un archivo a la papelera
    throwAwayFile(fileId, index) {
      axios.put(`/api/` + this.cloudService + `/throwAway/${fileId}`)
          .then(() => {
            console.log("Indice: " + index);
            this.files.splice(index, 1);
            this.setHasFiles(this.files.length > 0);
            VsToast.show({
              title: 'Archivo enviado a la papelera con éxito',
              variant: 'success',
              position: 'bottom-center'
            });
          })
          .catch(error => {
            console.error(error);
            VsToast.show({
              title: 'Error al enviar el archivo a la papelera',
              variant: 'error',
              position: 'bottom-center'
            });
          });
    },
    // Método para mover a la papelera todos los archivos seleccionados
    throwAwayAllSelected() {
      for (let i = this.files.length - 1; i >= 0; i--) {
        if (this.files[i].selected) {
          this.throwAwayFile(this.files[i].id, i);
        }
      }
      this.clearSelection();
    },
    // Método para restaurar un archivo que estaba en la papelera
    restoreFile(fileId, index) {
      axios.put(`/api/` + this.cloudService + `/restore/${fileId}`)
          .then(() => {
            this.files.splice(index, 1);
            this.setHasFiles(this.files.length > 0);
            VsToast.show({title: 'Archivo restaurado con éxito', variant: 'success', position: 'bottom-center'});
          })
          .catch(error => {
            console.error(error);
            if (error.response && error.response.status === 409) {
              // Si el código de estado es 409 (CONFLICT), muestra un mensaje de limitación en la nube
              VsToast.show({
                title: 'No se puede restaurar el archivo',
                message: 'Debido a limitaciones en la nube debe realizarlo de manera manual a través de la página web del servicio.',
                variant: 'warning',
                position: 'bottom-center'
              });
            } else {
              // Si hay otro error, muestra un mensaje genérico de error
              VsToast.show({title: 'Error al restaurar el archivo', variant: 'error', position: 'bottom-center'});
            }
          });
    },
    // Método para restaurar todos los archivos seleccionados
    restoreAllSelected() {
      const selectedFiles = this.files.filter(file => file.selected);
      selectedFiles.forEach(file => {
        const index = this.files.findIndex(f => f.id === file.id);
        this.restoreFile(file.id, index);
      });
      this.clearSelection();
    },
    // Método para eliminar un archivo de forma permanente
    deleteFile(fileId, index) {
      axios.delete(`/api/` + this.cloudService + `/delete/${fileId}`)
          .then(() => {
            this.files.splice(index, 1);
            this.setHasFiles(this.files.length > 0);
            VsToast.show({
              title: 'Archivo eliminado definitivamente con éxito',
              variant: 'success',
              position: 'bottom-center'
            });
          })
          .catch(error => {
            console.error(error);
            if (error.response && error.response.status === 409) {
              // Si el código de estado es 409 (CONFLICT), muestra un mensaje de limitación en la nube
              VsToast.show({
                title: 'No se puede eliminar de forma definitiva',
                message: 'Debido a limitaciones en la nube debe realizarlo de manera manual a través de la página web del servicio.',
                variant: 'warning',
                position: 'bottom-center'
              });
            } else {
              // Si hay otro error, muestra un mensaje genérico de error
              VsToast.show({
                title: 'Error al eliminar archivo de forma definitiva',
                variant: 'error',
                position: 'bottom-center'
              });
            }
          });
    },
    // Método para eliminar todos los archivos seleccionados
    deleteAllSelected() {
      const selectedFiles = this.files.filter(file => file.selected);
      selectedFiles.forEach(file => {
        const index = this.files.findIndex(f => f.id === file.id);
        this.deleteFile(file.id, index);
      });
      this.clearSelection();
    },
    // Método para eliminar todos los archivos de forma permanente
    deleteAllFiles() {
      const filesCopy = [...this.files];

      filesCopy.forEach(file => {
        this.deleteFile(file.id, this.files.findIndex(f => f.id === file.id));
      });
    },
    // Método para ordenar los archivos
    sortFiles() {
      // Ordenar los archivos según el método seleccionado
      if (this.orderBy === 'name') {
        this.files.sort((a, b) => a.name.localeCompare(b.name));
      } else if (this.orderBy === 'date') {
        this.files.sort((a, b) => {
          console.log(a.name + ": " + a.lastTimeViewed);
          console.log(b.name + ": " + b.lastTimeViewed);
          const dateA = new Date(a.lastTimeViewed);
          const dateB = new Date(b.lastTimeViewed);
          console.log("Resultado: " + (dateB - dateA));
          return dateB - dateA;
        });
      } else if (this.orderBy === 'tam') {
        this.files.sort((a, b) => b.size - a.size);
      }
    },
    // Método para establecer la fecha max. según el periodo establecido
    getMaxDateByPeriod() {
      let maxDate = new Date();

      switch (this.period) {
        case 'day':
          maxDate.setDate(maxDate.getDate() - 1); // Restar 1 día
          break;
        case 'month':
          maxDate.setMonth(maxDate.getMonth() - 1); // Restar 1 mes
          break;
        case 'year':
          maxDate.setFullYear(maxDate.getFullYear() - 1); // Restar 1 año
          break;
        default:
          maxDate.setDate(maxDate.getDate() - 7); // Por defecto, restar 7 días (1 semana)
          break;
      }
      return maxDate.toISOString();
    },
    // Método para obtener las carpetas dentro de una carpeta
    getFoldersInFolder(folderId) {
      axios.get('/api/' + this.moveCloudService + '/folders/' + folderId)
          .then(response => {
            this.folders = response.data;
          })
          .catch(error => {
            console.error(error);
            this.$router.push({
              name: 'ErrorView',
              query: {code: 500, message: 'Error al obtener las carpetas disponibles para mover los archivos'}
            });
          });
    },
    // Método para volver atrás en la ruta para mover un archivo
    backFolder() {
      if (this.fileMovePath.length > 1)
        this.fileMovePath.pop();
      this.getFoldersInFolder(this.fileMovePath[this.fileMovePath.length - 1].id);
    },
    // Método para ir a la carpeta para mover un archivo
    moveToFolder(folder) {
      this.fileMovePath.push(folder);
      this.getFoldersInFolder(this.fileMovePath[this.fileMovePath.length - 1].id);
    },
    // Método para abrir el overlay para renombrar un archivo e indicar cuál vamos a modificar
    openRenameFileOverlay(file) {
      this.fileSelected = file;
      this.showRenameFileOverlay = true;
      this.toggleCardStyle(file);
    },
    // Método para cerrar el overlay para renombrar un archivo
    closeRenameFileOverlay() {
      this.fileName = '';
      this.showRenameFileOverlay = false;
    },
    // Método para abrir el overlay para renombrar un archivo e indicar cuál vamos a modificar
    openDownloadEncryptedFileOverlay(file) {
      this.fileSelected = file;
      this.showDownloadEncryptedFileOverlay = true;
      this.toggleCardStyle(file);
    },
    // Método para cerrar el overlay para renombrar un archivo
    closeDownloadEncryptedFileOverlay() {
      this.fileName = '';
      this.showDownloadEncryptedFileOverlay = false;
    },
    // Método para abrir el overlay para mover un archivo
    openMoveFileOverlay(file) {
      this.fileSelected = file;
      this.moveCloudService = this.cloudService;
      this.getFoldersInFolder('root');
      this.fileMovePath.push({id: 'root', name: 'Inicio'});
      this.showMoveFileOverlay = true;
      this.toggleCardStyle(file);
    },
    // Método para cerrar el overlay para mover un archivo
    closeMoveFileOverlay() {
      this.folders = [];
      this.fileMovePath = [];
      this.moveCloudService = this.cloudService;
      this.showMoveFileOverlay = false;
    },
    // Método para mostrar el overlay de opciones de un archivo
    toggleCardStyle(file) {
      file.showOverlay = !file.showOverlay;
    },
    // Método para manejar el click sobre un archivo
    handleClickFile(file, event) {
      if (this.selectionMode) {
        this.toggleSelected(file);
      } else this.openFile(file, event);
    },
    // Método para cambiar estado de seleccionado de un archivo
    toggleSelected(file) {
      file.selected = !file.selected;
    },
    // Método que elimina la selección de todas las carpetas
    clearSelection() {
      this.files.forEach(file => {
        file.selected = false;
      });
      this.selectionMode = false;
    },
    // Método para activar o desactivar el modo de selección de archivos
    toggleSelectionMode() {
      this.selectionMode = !this.selectionMode;
      if (!this.selectionMode) this.clearSelection();
    },
    // Método para abrir el overlay para mover los archivos seleccionados
    openMoveSelectedOverlay() {
      this.moveCloudService = this.cloudService;
      this.getFoldersInFolder('root');
      this.fileMovePath.push({id: 'root', name: 'Inicio'});
      this.showMoveSelectedOverlay = true;
    },
    // Método para cerrar el overlay para mover los archivos seleccionados
    closeMoveSelectedOverlay() {
      this.foldersMoveOption = [];
      this.fileMovePath = [];
      this.moveCloudService = this.cloudService;
      this.showMoveSelectedOverlay = false;
    },
    selectMoveCloudService(cloudService) {
      if (this.isAuthenticated[cloudService]) {
        this.moveCloudService = cloudService;
        this.getFoldersInFolder('root');
      } else {
        // Mensaje para que inicie sesión en esa nube
        VsToast.show({
          title: 'Debe iniciar sesión en dicha nube',
          variant: 'info',
          position: 'bottom-center'
        });
      }
    },
    ...mapMutations(['setHasFiles']), // Establece a nivel global si hay archivos
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

.circle-icon {
  margin-top: -5px !important;
  position: absolute;
  top: 0;
  right: 0;
  border-radius: 0 0 0 10px;
  background-color: rgb(50, 50, 50, 0.4);
  box-shadow: 0 4px 4px rgba(0, 0, 0, 0.1);
}

.saturate-img {
  filter: saturate(50%);
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
  transform: translateY(1%);
}

.card__arc path {
  fill: #fff;
  d: path("M 40 80 c 22 0 40 -22 40 -40 v 40 Z");
}

.options-container button {
  margin-left: 8px;
}

.options-container::-webkit-scrollbar {
  width: 8px;
}

.options-container::-webkit-scrollbar-thumb {
  background-color: #888;
  border-radius: 4px;
}

.move-file-row {
  border-radius: 20px;
  background-color: rgb(50, 50, 50, 0.1);
}

.move-file-row:hover {
  background-color: rgb(50, 50, 50, 0.2);
}

.move-here {
  border: none;
  border-radius: 20px;
  font-family: 'Roboto', sans-serif
}

.move-to:hover {
  fill: #4451FE;
}
</style>