import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import FileManagerView from "@/views/FileManagerView";

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
    path: '/filemanager/bin',
    name: 'bin',
    component: FileManagerView,
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
