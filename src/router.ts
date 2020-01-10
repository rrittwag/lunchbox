import Vue from 'vue'
import Router from 'vue-router'
import Offers from '@/views/Offers.vue'

Vue.use(Router)

export const router = new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
    {
      path: '/',
      component: Offers,
    },
    {
      path: '/about',
      component: () => import(/* webpackChunkName: "about" */ '@/views/About.vue'),
    },
    {
      path: '/settings',
      component: () => import(/* webpackChunkName: "settings" */ '@/views/Settings.vue'),
    },
    {
      path: '*',
      redirect: '/',
    },
  ],
})
