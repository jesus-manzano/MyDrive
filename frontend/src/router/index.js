import {createRouter, createWebHistory} from 'vue-router'
import HomeView from '../views/HomeView.vue'
import FileManagerView from "@/views/FileManagerView";
import ErrorView from "@/views/ErrorView";

const routes = [
    {
        path: '/',
        name: 'home',
        component: HomeView
    },
    {
        path: '/filemanager/:folderId',
        name: 'filemanager',
        component: FileManagerView,
    },
    {
        path: '/filemanager/recent',
        name: 'recent',
        component: FileManagerView,
    },
    {
        path: '/filemanager/bin',
        name: 'bin',
        component: FileManagerView,
    },
    {
        path: '/error',
        name: 'ErrorView',
        component: ErrorView,
        props: route => ({errorCode: route.query.code, errorMessage: route.query.message})
    },
    // Add a catch-all route for 404 errors
    {
        path: '/:catchAll(.*)',
        redirect: {name: 'ErrorView', query: {code: 404, message: 'Page Not Found'}}
    }
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

export default router
