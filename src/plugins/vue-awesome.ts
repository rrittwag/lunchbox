import Vue from 'vue'
import Icon from 'vue-awesome/components/Icon.vue'

export const applyAwesome = (vue: typeof Vue) => {
  vue.component('v-icon', Icon)
}

applyAwesome(Vue)
