<template>
  <div v-if="folders.length > 0">
    <div class="d-flex justify-content-between align-items-center">
      <div class="display-6 mt-4 mb-4">Carpetas</div>
      <!-- Dropdown de opciones varias carpetas -->
      <div v-if="selectionMode" class="btn-group">
        <button type="button" class="btn btn-primary" @click="toggleSelectionMode">
          Deseleccionar
          <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="currentColor" class="bi bi-folder-x ms-1"
               viewBox="0 0 16 16">
            <path
                d="M.54 3.87.5 3a2 2 0 0 1 2-2h3.672a2 2 0 0 1 1.414.586l.828.828A2 2 0 0 0 9.828 3h3.982a2 2 0 0 1 1.992 2.181L15.546 8H14.54l.265-2.91A1 1 0 0 0 13.81 4H2.19a1 1 0 0 0-.996 1.09l.637 7a1 1 0 0 0 .995.91H9v1H2.826a2 2 0 0 1-1.991-1.819l-.637-7a2 2 0 0 1 .342-1.31zm6.339-1.577A1 1 0 0 0 6.172 2H2.5a1 1 0 0 0-1 .981l.006.139q.323-.119.684-.12h5.396z"/>
            <path
                d="M11.854 10.146a.5.5 0 0 0-.707.708L12.293 12l-1.146 1.146a.5.5 0 0 0 .707.708L13 12.707l1.146 1.147a.5.5 0 0 0 .708-.708L13.707 12l1.147-1.146a.5.5 0 0 0-.707-.708L13 11.293z"/>
          </svg>
        </button>
        <button class="btn btn-primary dropdown-toggle dropdown-toggle-split" type="button"
                data-bs-toggle="dropdown"
                aria-expanded="false">
        </button>
        <ul class="dropdown-menu dropdown-menu-end">
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
            <a class="dropdown-item" @click.prevent="showDeleteSelectedOverlay = true">
              Eliminar
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                   class="bi bi-trash3 ms-1" viewBox="0 0 16 16">
                <path
                    d="M6.5 1h3a.5.5 0 0 1 .5.5v1H6v-1a.5.5 0 0 1 .5-.5M11 2.5v-1A1.5 1.5 0 0 0 9.5 0h-3A1.5 1.5 0 0 0 5 1.5v1H1.5a.5.5 0 0 0 0 1h.538l.853 10.66A2 2 0 0 0 4.885 16h6.23a2 2 0 0 0 1.994-1.84l.853-10.66h.538a.5.5 0 0 0 0-1zm1.958 1-.846 10.58a1 1 0 0 1-.997.92h-6.23a1 1 0 0 1-.997-.92L3.042 3.5zm-7.487 1a.5.5 0 0 1 .528.47l.5 8.5a.5.5 0 0 1-.998.06L5 5.03a.5.5 0 0 1 .47-.53Zm5.058 0a.5.5 0 0 1 .47.53l-.5 8.5a.5.5 0 1 1-.998-.06l.5-8.5a.5.5 0 0 1 .528-.47M8 4.5a.5.5 0 0 1 .5.5v8.5a.5.5 0 0 1-1 0V5a.5.5 0 0 1 .5-.5"/>
              </svg>
            </a>
          </li>
        </ul>
      </div>
      <button v-else type="button" class="btn btn-primary" @click="toggleSelectionMode">
        Seleccionar
        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="currentColor"
             class="bi bi-folder-check ms-1" viewBox="0 0 16 16">
          <path
              d="m.5 3 .04.87a2 2 0 0 0-.342 1.311l.637 7A2 2 0 0 0 2.826 14H9v-1H2.826a1 1 0 0 1-.995-.91l-.637-7A1 1 0 0 1 2.19 4h11.62a1 1 0 0 1 .996 1.09L14.54 8h1.005l.256-2.819A2 2 0 0 0 13.81 3H9.828a2 2 0 0 1-1.414-.586l-.828-.828A2 2 0 0 0 6.172 1H2.5a2 2 0 0 0-2 2m5.672-1a1 1 0 0 1 .707.293L7.586 3H2.19q-.362.002-.683.12L1.5 2.98a1 1 0 0 1 1-.98z"/>
          <path
              d="M15.854 10.146a.5.5 0 0 1 0 .708l-3 3a.5.5 0 0 1-.707 0l-1.5-1.5a.5.5 0 0 1 .707-.708l1.146 1.147 2.646-2.647a.5.5 0 0 1 .708 0"/>
        </svg>
      </button>
    </div>

    <div class="d-flex flex-wrap justify-content-center text-center">
      <div v-for="(folder, index) in folders" :key="index" class="mx-4 mb-5" style="width: 11rem;">
        <a class="folder-card text-decoration-none" @click.prevent.stop="handleClickFolder(folder)">
          <div class="d-flex align-items-center position-relative" style="overflow: hidden;">
            <img src="@/assets/folder.png" class="card-img-top" :class="{ 'folder-selected': folder.selected }"
                 alt="Imagen">
            <div v-show="selectionMode" class="circle-icon" @click.prevent.stop="toggleSelected(folder)">
              <svg v-if="folder.selected" xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="currentColor"
                   class="bi bi-check2-circle" viewBox="0 0 16 16">
                <path
                    d="M2.5 8a5.5 5.5 0 0 1 8.25-4.764.5.5 0 0 0 .5-.866A6.5 6.5 0 1 0 14.5 8a.5.5 0 0 0-1 0 5.5 5.5 0 1 1-11 0"/>
                <path
                    d="M15.354 3.354a.5.5 0 0 0-.708-.708L8 9.293 5.354 6.646a.5.5 0 1 0-.708.708l3 3a.5.5 0 0 0 .708 0z"/>
              </svg>
              <svg v-else xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                   class="bi bi-circle"
                   viewBox="0 0 16 16">
                <path d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14m0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16"/>
              </svg>
            </div>
          </div>

          <div class="card-body">
            <div class="d-flex flex-row justify-content-center align-items-center">
              <div class="card-title p-2 h5 text-center"
                   style="white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
                {{ folder.name }}
              </div>
              <div class="dropdown" @click.prevent.stop>
                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20"
                     fill="rgb(50, 50, 50, 0.6)"
                     class="bi bi-gear-fill dropdown-toggle folder-option" id="dropdownFolder" data-bs-toggle="dropdown"
                     viewBox="0 0 16 16" aria-expanded="false">
                  <path
                      d="M9.405 1.05c-.413-1.4-2.397-1.4-2.81 0l-.1.34a1.464 1.464 0 0 1-2.105.872l-.31-.17c-1.283-.698-2.686.705-1.987 1.987l.169.311c.446.82.023 1.841-.872 2.105l-.34.1c-1.4.413-1.4 2.397 0 2.81l.34.1a1.464 1.464 0 0 1 .872 2.105l-.17.31c-.698 1.283.705 2.686 1.987 1.987l.311-.169a1.464 1.464 0 0 1 2.105.872l.1.34c.413 1.4 2.397 1.4 2.81 0l.1-.34a1.464 1.464 0 0 1 2.105-.872l.31.17c1.283.698 2.686-.705 1.987-1.987l-.169-.311a1.464 1.464 0 0 1 .872-2.105l.34-.1c1.4-.413 1.4-2.397 0-2.81l-.34-.1a1.464 1.464 0 0 1-.872-2.105l.17-.31c.698-1.283-.705-2.686-1.987-1.987l-.311.169a1.464 1.464 0 0 1-2.105-.872zM8 10.93a2.929 2.929 0 1 1 0-5.86 2.929 2.929 0 0 1 0 5.858z"/>
                </svg>
                <ul class="dropdown-menu text-small shadow" aria-labelledby="dropdownFolder">
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
                    <div class="dropdown-item" @click="openMoveFolderOverlay(folder)">
                      Mover
                      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                           class="bi bi-folder-symlink" viewBox="0 0 16 16">
                        <path
                            d="m11.798 8.271-3.182 1.97c-.27.166-.616-.036-.616-.372V9.1s-2.571-.3-4 2.4c.571-4.8 3.143-4.8 4-4.8v-.769c0-.336.346-.538.616-.371l3.182 1.969c.27.166.27.576 0 .742"/>
                        <path
                            d="m.5 3 .04.87a2 2 0 0 0-.342 1.311l.637 7A2 2 0 0 0 2.826 14h10.348a2 2 0 0 0 1.991-1.819l.637-7A2 2 0 0 0 13.81 3H9.828a2 2 0 0 1-1.414-.586l-.828-.828A2 2 0 0 0 6.172 1H2.5a2 2 0 0 0-2 2m.694 2.09A1 1 0 0 1 2.19 4h11.62a1 1 0 0 1 .996 1.09l-.636 7a1 1 0 0 1-.996.91H2.826a1 1 0 0 1-.995-.91zM6.172 2a1 1 0 0 1 .707.293L7.586 3H2.19q-.362.002-.683.12L1.5 2.98a1 1 0 0 1 1-.98z"/>
                      </svg>
                    </div>
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
        </a>
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
        <button class="btn btn-success"
                @click="deleteFolder(folderSelected.id, folders.findIndex(folder => folder.id === folderSelected.id))">
          Aceptar
        </button>
      </div>
    </div>
  </div>

  <!-- Overlay para confirmar eliminación de las carpetas seleccionadas -->
  <div v-show="showDeleteSelectedOverlay" class="overlay-area">
    <div class="popup-area">
      <h2 class="mb-3">¿Estás seguro de eliminar todas las carpetas?</h2>
      <div class="d-flex justify-content-between align-items-center mb-3">
        <div class="me-3">
          <bold class="bold fs-5">Info:</bold>
          Eliminar dichas carpetas también eliminará todos los elementos de su interior.
        </div>
      </div>
      <div class="d-flex justify-content-between align-items-center">
        <button class="btn btn-danger" @click="showDeleteSelectedOverlay = false">Cancelar</button>
        <button class="btn btn-success" @click="deleteAllSelected">Aceptar</button>
      </div>
    </div>
  </div>

  <!-- Overlay para mover una carpeta -->
  <div v-show="showMoveFolderOverlay" class="overlay-area">
    <div class="popup-area">
      <h3 v-if="showMoveFolderOverlay" class="mb-3">Mover carpeta: "{{ folderSelected.name }}"</h3>
      <div class="d-flex flex-row justify-content-between align-items-center">
        <div class="d-flex flex-row align-items-center">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-folder-fill"
               viewBox="0 0 16 16">
            <path
                d="M9.828 3h3.982a2 2 0 0 1 1.992 2.181l-.637 7A2 2 0 0 1 13.174 14H2.825a2 2 0 0 1-1.991-1.819l-.637-7a2 2 0 0 1 .342-1.31L.5 3a2 2 0 0 1 2-2h3.672a2 2 0 0 1 1.414.586l.828.828A2 2 0 0 0 9.828 3m-8.322.12q.322-.119.684-.12h5.396l-.707-.707A1 1 0 0 0 6.172 2H2.5a1 1 0 0 0-1 .981z"/>
          </svg>
          <div v-if="showMoveFolderOverlay" class="ms-1 bold">
            {{ this.folderMovePath[this.folderMovePath.length - 1].name }}
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
        <div v-if="foldersMoveOption.length === 0" class="text-secondary">
          No hay ninguna carpeta en la carpeta actual
        </div>
        <div v-for="folder in foldersMoveOption" :key="folder.id"
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
            <button class="btn btn-primary move-here me-2" @click="moveFolder(folder.id)">Mover</button>
            <svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" fill="currentColor"
                 class="bi bi-arrow-bar-right move-to" viewBox="0 0 16 16" @click="moveToFolder(folder)">
              <path fill-rule="evenodd"
                    d="M6 8a.5.5 0 0 0 .5.5h5.793l-2.147 2.146a.5.5 0 0 0 .708.708l3-3a.5.5 0 0 0 0-.708l-3-3a.5.5 0 0 0-.708.708L12.293 7.5H6.5A.5.5 0 0 0 6 8m-2.5 7a.5.5 0 0 1-.5-.5v-13a.5.5 0 0 1 1 0v13a.5.5 0 0 1-.5.5"/>
            </svg>
          </div>
        </div>
      </div>
      <div class="d-flex justify-content-between align-items-center">
        <button class="btn btn-danger" @click="closeMoveFolderOverlay">Cancelar</button>
        <button class="btn btn-success" @click="moveFolder(this.folderMovePath[this.folderMovePath.length - 1].id)">
          Mover Aquí
        </button>
      </div>
    </div>
  </div>

  <!-- Overlay para mover las carpetas seleccionadas -->
  <div v-show="showMoveSelectedOverlay" class="overlay-area">
    <div class="popup-area">
      <h3 v-if="showMoveSelectedOverlay" class="mb-3">Mover carpetas seleccionadas</h3>
      <div class="d-flex flex-row justify-content-between align-items-center">
        <div class="d-flex flex-row align-items-center">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-folder-fill"
               viewBox="0 0 16 16">
            <path
                d="M9.828 3h3.982a2 2 0 0 1 1.992 2.181l-.637 7A2 2 0 0 1 13.174 14H2.825a2 2 0 0 1-1.991-1.819l-.637-7a2 2 0 0 1 .342-1.31L.5 3a2 2 0 0 1 2-2h3.672a2 2 0 0 1 1.414.586l.828.828A2 2 0 0 0 9.828 3m-8.322.12q.322-.119.684-.12h5.396l-.707-.707A1 1 0 0 0 6.172 2H2.5a1 1 0 0 0-1 .981z"/>
          </svg>
          <div v-if="showMoveSelectedOverlay" class="ms-1 bold">
            {{ this.folderMovePath[this.folderMovePath.length - 1].name }}
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
        <div v-if="foldersMoveOption.length === 0" class="text-secondary">
          No hay ninguna carpeta en la carpeta actual
        </div>
        <div v-for="folder in foldersMoveOption" :key="folder.id"
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
      <div class="d-flex justify-content-between align-items-center">
        <button class="btn btn-danger" @click="closeMoveSelectedOverlay">Cancelar</button>
        <button class="btn btn-success"
                @click="moveAllSelected(this.folderMovePath[this.folderMovePath.length - 1].id)">
          Mover Aquí
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import {mapMutations, mapState} from "vuex";

export default {
  name: "FolderList",
  data() {
    return {
      folders: [],
      showRenameFolderOverlay: false,
      showDeleteFolderOverlay: false,
      showDeleteSelectedOverlay: false,
      folderSelected: null,
      folderName: '',
      showMoveFolderOverlay: false,
      showMoveSelectedOverlay: false,
      foldersMoveOption: [],
      folderMovePath: [],
      selectionMode: false
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
  computed: {
    ...mapState(['cloudService']),
    ...mapState(['isAuthenticated']),
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
    cloudService() {
      this.getFolders();
    }
  },
  mounted() {
    this.getFolders();
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

      if (this.$route.name === 'filemanager') {
        if (searching && !this.$store.state.searchInFolder) { // Si está buscando de forma global
          endpoint = '/api/' + this.cloudService + '/searchFolder/' + this.searchText;
        } else endpoint = '/api/' + this.cloudService + '/folders/' + this.currentFolderId + '?q=' + this.searchText;
      }

      return endpoint;
    },
    // Método para obtener las carpetas
    getFolders() {
      if (this.cloudService === '' || !this.isAuthenticated[this.cloudService]) return; // No ejecutamos

      this.folders = []; // Limpiamos las carpetas actuales
      this.clearSelection(); // No hay ningún elemento seleccionado
      const endpoint = this.getEndpoint();

      if (endpoint !== '') {
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
    // Método para mover una carpeta
    moveFolder(folderId) {
      if (this.folderSelected) {
        // Enviar una solicitud al backend para mover dicho archivo a la carpeta indicada
        axios.put(`/api/` + this.cloudService + `/moveFile/` + this.folderSelected.id + '?folderId=' + folderId)
            .then(response => {
              console.log('Archivo movido exitosamente:', response.data);
              this.getFolders();

              this.folderSelected = null;
              this.folderMovePath = [];
              this.showMoveFolderOverlay = false;
              this.selectionMode = false;
            })
            .catch(error => {
              // Manejar errores, por ejemplo, mostrar un mensaje de error
              console.error('Error al renombrar el archivo:', error);
            });
      }
    },
    //Método para mover todas las carpetas seleccionadas
    moveAllSelected(targetFolderId) {
      let completedRequests = 0;
      const totalRequests = this.folders.filter(folder => folder.selected).length;
      this.folders.forEach(folder => {
        if (folder.selected) {
          // Enviar una solicitud al backend para mover dicho archivo a la carpeta indicada
          axios.put(`/api/` + this.cloudService + `/moveFile/` + folder.id + '?folderId=' + targetFolderId)
              .then(response => {
                console.log('Archivo movido exitosamente:', response.data);
              })
              .catch(error => {
                // Manejar errores, por ejemplo, mostrar un mensaje de error
                console.error('Error al renombrar el archivo:', error);
              })
              .finally(() => {
                completedRequests++;

                if (completedRequests === totalRequests) {
                  this.getFolders();
                  this.clearSelection();
                  this.showMoveSelectedOverlay = false;
                }
              });
        }
      });
    },
    // Método para renombrar una carpeta
    renameFolder() {
      if (this.folderSelected) {
        // Enviar una solicitud al backend para renombrar la carpeta
        axios.put(`/api/` + this.cloudService + `/renameFile/` + this.folderSelected.id + '?name=' + this.folderName)
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
      axios.delete(`/api/` + this.cloudService + `/delete/${folderId}`)
          .then(response => {
            this.folders.splice(index, 1);
            this.setHasFolders(this.folders.length > 0);
            this.closeDeleteFolderOverlay();
            console.log('Archivo eliminado de forma definitiva:', response.data);
          })
          .catch(error => {
            console.error('Error al eliminar el archivo de forma definitiva:', error);
          });
    },
    // Método para eliminar todas las carpetas seleccinadas
    deleteAllSelected() {
      let completedRequests = 0;
      const totalRequests = this.folders.filter(folder => folder.selected).length;

      this.folders.forEach(folder => {
        if (folder.selected) {
          // Enviar una solicitud al backend para eliminar la carpeta actual
          axios.delete(`/api/` + this.cloudService + `/delete/${folder.id}`)
              .then(response => {
                console.log('Carpeta eliminada permanentemente:', response.data);
              })
              .catch(error => {
                console.error('Error al eliminar la carpeta permanentemente:', error);
              })
              .finally(() => {
                completedRequests++;
                if (completedRequests === totalRequests) {
                  this.getFolders();
                  this.showDeleteSelectedOverlay = false;
                }
              });
        }
      });
    },
    // Método para obtener las carpetas dentro de una carpeta
    // para la opción de mover una carpeta
    getFoldersInFolder(folderId) {
      axios.get('/api/' + this.cloudService + '/folders/' + folderId)
          .then(response => {
            this.foldersMoveOption = response.data;
          })
          .catch(error => {
            console.error('Error fetching folders:', error);
          });
    },
    // Método para volver atrás en la ruta para mover una carpeta
    backFolder() {
      if (this.folderMovePath.length > 1)
        this.folderMovePath.pop();
      this.getFoldersInFolder(this.folderMovePath[this.folderMovePath.length - 1].id);
    },
    // Método para ir a la carpeta para mover una carpeta
    moveToFolder(folder) {
      this.folderMovePath.push(folder);
      this.getFoldersInFolder(this.folderMovePath[this.folderMovePath.length - 1].id);
    },
    // Método para ir a una carpeta
    navigateToFolder(folder) {
      this.$router.push(`/filemanager/${folder.id}`);
    },
    // Método para manejar el click sobre una carpeta
    handleClickFolder(folder) {
      if (this.selectionMode) {
        this.toggleSelected(folder);
      } else this.navigateToFolder(folder);
    },
    ...mapMutations(['setHasFolders']), // Establece a nivel global si hay carpetas
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
    // Método para abrir el overlay para mover una carpeta
    openMoveFolderOverlay(folder) {
      this.folderSelected = folder;
      this.getFoldersInFolder('root');
      this.folderMovePath.push({id: 'root', name: 'Inicio'});
      this.showMoveFolderOverlay = true;
    },
    // Método para cerrar el overlay para mover una carpeta
    closeMoveFolderOverlay() {
      this.foldersMoveOption = [];
      this.folderMovePath = [];
      this.showMoveFolderOverlay = false;
    },
    // Método para abrir el overlay para mover las carpetas seleccionadas
    openMoveSelectedOverlay() {
      this.getFoldersInFolder('root');
      this.folderMovePath.push({id: 'root', name: 'Inicio'});
      this.showMoveSelectedOverlay = true;
    },
    // Método para cerrar el overlay para mover las carpetas seleccionadas
    closeMoveSelectedOverlay() {
      this.foldersMoveOption = [];
      this.folderMovePath = [];
      this.showMoveSelectedOverlay = false;
    },
    // Método para cambiar estado seleccionado de una carpeta
    toggleSelected(folder) {
      folder.selected = !folder.selected;
    },
    // Método que elimina la selección de todas las carpetas
    clearSelection() {
      this.folders.forEach(folder => {
        folder.selected = false;
      });
      this.selectionMode = false;
    },
    // Método para activar o desactivar el modo de selección de carpetas
    toggleSelectionMode() {
      this.selectionMode = !this.selectionMode;
      if (!this.selectionMode) this.clearSelection();
    }
  }
}
</script>

<style scoped>
.folder-card:hover img {
  margin-top: -5px !important;
}

.folder-selected {
  filter: saturate(40%);
}

.circle-icon {
  position: absolute;
  top: 0;
  right: 0;
  margin-top: -5px !important;
}

.folder-option:hover {
  fill: rgb(50, 50, 50, 0.8);
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
