import Vue from 'vue'
import App from './App.vue'
import { router } from './router'
import { store } from './store'
import './registerServiceWorker'

// Import Bootstrap + styles
import BootstrapVue from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

Vue.use(BootstrapVue)

// import FontAwesome
import Icon from 'vue-awesome/components/Icon.vue'

Vue.component('v-icon', Icon)

// Filter anmelden
Vue.filter('formatEuro', (priceInCent: number) => {
  if (!priceInCent) return ''
  const euroString = `${Math.floor(priceInCent / 100)}`
  const centString = `0${priceInCent % 100}`.slice(-2)
  return `${euroString},${centString} €`
})
Vue.filter('formatToWeekday', (date: Date) => {
  if (!date) return ''
  const options = { weekday: 'long' }
  return date.toLocaleDateString(undefined, options)
})
Vue.filter('formatToDate', (date: Date) => {
  if (!date) return ''
  return date.toLocaleDateString()
})

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app')
