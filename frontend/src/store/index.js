import {createStore} from 'vuex'

export default createStore({
    state: {
        cloudService: 'dropbox',
        searchInFolder: false,
        hasFolders: true,
        hasFiles: true
    },
    mutations: {
        setCloudService(state, value) {
            state.cloudService = value;
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
    }
});
