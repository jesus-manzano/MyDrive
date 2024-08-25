import {shallowMount} from '@vue/test-utils';
import axios from 'axios';
import MockAdapter from 'axios-mock-adapter';
import FolderList from '@/components/FolderList.vue';
import Vuex from 'vuex';

// Mockear la importación de archivos estáticos
jest.mock('@/assets/folder.png', () => 'test-file-stub', {virtual: true});

describe('FolderList', () => {
    let wrapper;
    let mock;
    let store;

    beforeEach(() => {
        // Crear una instancia de MockAdapter antes de cada prueba
        mock = new MockAdapter(axios);
        mock.onGet('/api/dropbox/folders/root?q=').reply(200, []);

        // Configurar el store de Vuex
        store = new Vuex.Store({
            state: {
                cloudService: 'dropbox',
                searchInFolder: false,
                hasFolders: false,
                hasFiles: false,
                isAuthenticated: {
                    'google-drive': false,
                    'dropbox': true,
                },
            },
            mutations: {
                setHasFolders(state, hasFolders) {
                    state.hasFolders = hasFolders;
                }
            }
        });

        // Montar el componente con shallowMount
        wrapper = shallowMount(FolderList, {
            propsData: {
                currentFolderId: 'root',
                searchText: ''
            },
            global: {
                plugins: [store],
                stubs: {
                    bold: true,
                    routerLink: true, // Stub para router-link
                    routerView: true  // Stub para router-view
                },
                mocks: {
                    $route: {
                        name: 'filemanager',
                        query: {q: ''}
                    },
                    $router: {
                        push: jest.fn()
                    }
                }
            }
        });

        // Mockear el $router objeto
        wrapper.vm.$router = {push: jest.fn()};
    });

    afterEach(() => {
        // Limpiar los mocks después de cada prueba
        mock.restore();
        jest.clearAllMocks();
    });

    it('fetches folders from the backend and updates the folder list', async () => {
        const mockFolders = [
            {
                id: 'folder1',
                name: 'Folder 1',
                path: '/Folder 1',
            },
            {
                id: 'folder2',
                name: 'Folder 2',
                path: '/Folder 2',
            },
        ];

        // Configurar el mock adapter para que devuelva una respuesta exitosa
        mock.onGet('/api/dropbox/folders/root?q=').reply(200, mockFolders);

        // Llamar al método getFolders
        await wrapper.vm.getFolders();
        await wrapper.vm.$nextTick(); // espera a que se complete la solicitud HTTP
        await wrapper.vm.$nextTick(); // espera a que se renderice el componente

        // Verificar que el array de carpetas se haya actualizado correctamente
        expect(wrapper.vm.folders).toEqual(expect.arrayContaining(mockFolders));
    });

    it('handles error when fetching folders from the backend', async () => {
        jest.spyOn(console, 'error').mockImplementation(() => {
        });

        // Simular una respuesta de error en la petición GET a la API
        const endpoint = wrapper.vm.getEndpoint();
        mock.onGet(endpoint).reply(500);

        // Llamar al método getFolders y manejar el error
        try {
            await wrapper.vm.getFolders();
        } catch (error) {
            // Si se lanza un error, capturarlo
        }

        // Verificar que las carpetas no se han cargado (la lista debería estar vacía)
        expect(wrapper.vm.folders).toEqual([]);
    });
});