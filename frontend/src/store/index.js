import {createStore} from 'vuex'

export default createStore({
    state: {
        searchInFolder: true,
    },
    mutations: {
        setSearchInFolder(state, value) {
            state.searchInFolder = value;
        }
    }
});
