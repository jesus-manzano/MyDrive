import {createStore} from 'vuex'

export default createStore({
    state: {
        cloudService: localStorage.getItem('selectedCloudService') || '',
        searchInFolder: false,
        hasFolders: true,
        hasFiles: true,
        isAuthenticated: {
            'google-drive': false,
            'dropbox': false,
        },
    },
    mutations: {
        setCloudService(state, value) {
            state.cloudService = value;
            localStorage.setItem('selectedCloudService', state.cloudService); // Permanece en caso de recargas
        },
        setAuthentication(state, {service, status}) {
            state.isAuthenticated[service] = status;
        },
        setSearchInFolder(state, value) {
            state.searchInFolder = value;
        },
        setHasFolders(state, value) {
            state.hasFolders = value;
        },
        setHasFiles(state, value) {
            state.hasFiles = value;
        },
    },
    getters: {
        isNotAuthenticatedInAnyCloud(state) {
            return !Object.values(state.isAuthenticated).some(status => status);
        }
    }
});
