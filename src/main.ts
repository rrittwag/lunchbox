import Vue from 'vue'
import App from './App.vue'
import { router } from './router'
import { store } from './store'
import './registerServiceWorker'

import BootstrapVue from 'bootstrap-vue'

// Import the styles directly. (Or you could add them via script tags.)
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

Vue.use(BootstrapVue)

// TODO: Filter auslagern
Vue.filter('formatEuro', (priceInCent: number) => {
  if (!priceInCent) return ''
  const centString = ('0' + (priceInCent % 100)).slice(-2)
  return Math.floor(priceInCent / 100) + ',' + centString + ' €'
})

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app')
