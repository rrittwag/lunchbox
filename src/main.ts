import Vue from 'vue'
import App from './App.vue'
import { router } from './router'
import { store } from './store'
import * as filters from './filters'
import './registerServiceWorker'

// register Bootstrap components + styles
import BootstrapVue from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

Vue.use(BootstrapVue)

// register FontAwesome
import Icon from 'vue-awesome/components/Icon.vue'

Vue.component('v-icon', Icon)

// register filters
type FunctionMap = { [key: string]: Function }
const typedFilters: FunctionMap = filters // bugfixing https://github.com/Microsoft/TypeScript/issues/16248#issuecomment-306034585
Object.keys(typedFilters).forEach((key: string) => Vue.filter(key, typedFilters[key]))

// configuration
Vue.config.productionTip = false

// create root Vue instance
new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app')
