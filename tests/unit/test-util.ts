import Vue, { ComponentOptions } from 'vue'

import {
  createLocalVue as createLocalVue_vtu,
  shallowMount,
  VueClass,
  Wrapper
} from '@vue/test-utils'
import BootstrapVue from 'bootstrap-vue'
import * as filters from '@/filters'
import Icon from 'vue-awesome/components/Icon.vue'

function applyBootstrap(vue: typeof Vue) {
  vue.use(BootstrapVue)
}

function applyFilters(vue: typeof Vue) {
  interface FunctionMap {
    // tslint:disable-next-line ban-types
    [key: string]: Function
  }
  // bugfixing https://github.com/Microsoft/TypeScript/issues/16248#issuecomment-306034585
  const typedFilters: FunctionMap = filters
  Object.keys(typedFilters).forEach(
    (key: string) => vue.filter(key, typedFilters[key])
  )
}

function applyAwesome(vue: typeof Vue) {
  vue.component('v-icon', Icon)
}

/**
 * Create a testable Vue instance (instead of using global 'Vue' object),
 * including global Vue components & filters (see main.ts).
 */
export function createLocalVue(): typeof Vue {
  const localVue = createLocalVue_vtu()

  applyBootstrap(localVue)
  applyFilters(localVue)
  applyAwesome(localVue)

  return localVue
}

/**
 * Create a testable shallowMount-ed component.
 * <p>
 * Note: shallowMount replaces child components with stubs.
 * Stubs are renamed to '<component-name>-stub'.
 * <p>
 * @param component
 * @param props
 */
export function mountUnit<V extends Vue>(
  component: VueClass<V> | ComponentOptions<V>): Wrapper<V>

export function mountUnit<V extends Vue>(
  component: VueClass<V>,
  props: object): Wrapper<V>

export function mountUnit<V extends Vue>(
  component: object,
  props: object = {}): Wrapper<V> {

  const localVue = createLocalVue()

  return shallowMount(component, {
    localVue,
    propsData: { ...props },
  })
}
