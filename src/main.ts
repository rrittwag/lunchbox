import Vue from 'vue'
import App from './App.vue'
import { router } from './router'
import { store } from './store'

// register PWA app
import '@/plugins/registerServiceWorker'

// register filters
import '@/plugins/filters'

// register Bootstrap components + styles
import '@/plugins/bootstrap-vue'

// register FontAwesome icons
import '@/plugins/vue-awesome'

// configuration
Vue.config.productionTip = false

// create root Vue instance
new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app')
