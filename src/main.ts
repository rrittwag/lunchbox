import Vue from 'vue'
import App from './App.vue'
import { router } from './router'
import { store } from './store'

// register PWA app
import '@/plugins/registerServiceWorker'

// Tailwind CSS styles
import '@/assets/tailwind.scss'

// register filters
import '@/plugins/filters'

// configuration
Vue.config.productionTip = false

// create root Vue instance
new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app')
