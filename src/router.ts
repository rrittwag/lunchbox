import Vue from 'vue'
import Router from 'vue-router'
import Offers from './views/Offers.vue'
import About from './views/About.vue'
import Settings from './views/Settings.vue'

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
      component: About,
    },
    {
      path: '/settings',
      component: Settings,
    },
    {
      path: '*',
      redirect: '/',
    },
  ],
})
