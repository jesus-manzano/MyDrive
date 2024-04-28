import {createStore} from 'vuex'

export default createStore({
    state: {
        searchInFolder: false,
        hasFolders: true,
        hasFiles: true
    },
    mutations: {
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
