import {shallowMount} from '@vue/test-utils';
import axios from 'axios';
import MockAdapter from 'axios-mock-adapter';
import FileList from '@/components/FileList.vue';
import Vuex from 'vuex';
import VsToast from '@vuesimple/vs-toast';

describe('FileList', () => {
    let wrapper;
    let mock;
    let store;

    beforeEach(() => {
        // Crear una instancia de MockAdapter antes de cada prueba
        mock = new MockAdapter(axios);
        mock.onGet('/api/dropbox/files/root?q=').reply(200, []);

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
                setHasFiles(state, hasFiles) {
                    state.hasFiles = hasFiles;
                }
            }
        });

        // Montar el componente con shallowMount
        wrapper = shallowMount(FileList, {
            propsData: {
                currentFolderId: 'root',
                searchText: '',
                orderBy: 'name'
            },
            global: {
                plugins: [store],
                stubs: {
                    bold: true
                },
                mocks: {
                    $route: {
                        name: 'filemanager',
                        query: {q: ''}
                    },
                    $toast: VsToast
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

    it('fetches files from the backend and updates the file list', async () => {
        const mockFiles = [
            {
                id: '001',
                name: 'file1.txt',
                thumbnailLink: 'https://example.com/file1_thumbnail',
                lastTimeViewed: '2023-08-20T10:00:00Z',
                size: 1000,
                encrypted: false,
            },
            {
                id: '002',
                name: 'file2.txt',
                thumbnailLink: 'https://example.com/file2_thumbnail',
                lastTimeViewed: '2023-08-21T12:00:00Z',
                size: 2000,
                encrypted: false,
            },
        ];

        // Configurar el mock adapter para que devuelva una respuesta exitosa
        mock.onGet('/api/dropbox/files/root?q=').reply(200, mockFiles);

        // Llamar al método getFiles
        await wrapper.vm.getFiles();
        await wrapper.vm.$nextTick(); // espera a que se complete la solicitud HTTP
        await wrapper.vm.$nextTick(); // espera a que se renderice el componente

        // Verificar que el array de archivos se haya actualizado correctamente
        expect(wrapper.vm.files).toEqual(expect.arrayContaining(mockFiles));
    })

    it('handles error when fetching files from the backend', async () => {
        jest.spyOn(console, 'error').mockImplementation(() => {
        });

        // Simular una respuesta de error en la petición GET a la API
        const endpoint = wrapper.vm.getEndpoint();
        mock.onGet(endpoint).reply(500);

        // Llamar al método getFiles y manejar el error
        try {
            await wrapper.vm.getFiles();
        } catch (error) {
            // Si se lanza un error, capturarlo
        }

        // Verificar que los archivos no se han cargado (la lista debería estar vacía)
        expect(wrapper.vm.files).toEqual([]);
    });

    it('sorts files by name', async () => {
        // Datos de prueba con thumbnailLink
        const mockFiles = [
            {
                name: 'B File',
                lastTimeViewed: '2023-08-20T10:00:00Z',
                size: 200,
                thumbnailLink: 'https://example.com/thumbnails/bfile.png'
            },
            {
                name: 'A File',
                lastTimeViewed: '2023-08-19T10:00:00Z',
                size: 100,
                thumbnailLink: 'https://example.com/thumbnails/afile.png'
            },
            {
                name: 'C File',
                lastTimeViewed: '2023-08-21T10:00:00Z',
                size: 300,
                thumbnailLink: 'https://example.com/thumbnails/cfile.png'
            },
        ];

        // Asignar los archivos mock directamente
        wrapper.setData({files: mockFiles});

        // Llamar al método para ordenar los archivos
        await wrapper.vm.sortFiles();

        // Verificar que los archivos están ordenados por nombre
        expect(wrapper.vm.files).toEqual([
            {
                name: 'A File',
                lastTimeViewed: '2023-08-19T10:00:00Z',
                size: 100,
                thumbnailLink: 'https://example.com/thumbnails/afile.png'
            },
            {
                name: 'B File',
                lastTimeViewed: '2023-08-20T10:00:00Z',
                size: 200,
                thumbnailLink: 'https://example.com/thumbnails/bfile.png'
            },
            {
                name: 'C File',
                lastTimeViewed: '2023-08-21T10:00:00Z',
                size: 300,
                thumbnailLink: 'https://example.com/thumbnails/cfile.png'
            },
        ]);
    });

    it('downloads a file successfully', async () => {
        const file = {id: 1, name: 'example.txt'};
        const blob = new Blob(['Hello, world!'], {type: 'text/plain'});

        jest.spyOn(axios, 'get').mockResolvedValue({data: blob});

        // Mock completo de URL y su método createObjectURL
        const mockUrl = 'blob:https://example.com/123456';
        global.URL = {
            createObjectURL: jest.fn(() => mockUrl),
        };

        const url = '/api/dropbox/download/1';

        // Mock del método VsToast.show
        jest.spyOn(VsToast, 'show').mockImplementation(() => {
        });

        await wrapper.vm.downloadFile(file);

        expect(axios.get).toHaveBeenCalledTimes(1);
        expect(axios.get).toHaveBeenCalledWith(url, {responseType: 'blob'});

        expect(global.URL.createObjectURL).toHaveBeenCalledWith(blob);

        // Verificamos que se haya llamado VsToast
        expect(VsToast.show).toHaveBeenCalledTimes(1);
        expect(VsToast.show).toHaveBeenCalledWith({
            title: 'Archivo descargado con éxito',
            variant: 'success',
            position: 'bottom-center',
        });
    });
});
