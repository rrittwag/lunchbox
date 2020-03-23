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
      meta: {
        title: 'Mittagsangebote',
      },
    },
    {
      path: '/about',
      component: () => import(/* webpackChunkName: "about" */ '@/views/About.vue'),
      meta: {
        title: 'Info',
      },
    },
    {
      path: '/settings',
      component: () => import(/* webpackChunkName: "settings" */ '@/views/Settings.vue'),
      meta: {
        title: 'Einstellungen',
      },
    },
    {
      path: '*',
      redirect: '/',
    },
  ],
})
