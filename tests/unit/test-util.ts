import { createLocalVue as createLocalVue_vtu,
  shallowMount as shallowMount_vtu, Wrapper, VueClass } from '@vue/test-utils'
import Vue from 'vue'

import BootstrapVue from 'bootstrap-vue'
import * as filters from '@/filters'

import Icon from 'vue-awesome/components/Icon.vue'

/**
 * Create a testable Vue instance (instead of using global 'Vue' object),
 * including global Vue components & filters (see main.ts).
 */
function createLocalVue(): typeof Vue {
  // register Bootstrap components + styles
  const localVue = createLocalVue_vtu()
  localVue.use(BootstrapVue)

  // register filters
  interface FunctionMap {
    // tslint:disable-next-line ban-types
    [key: string]: Function
  }

  // bugfixing https://github.com/Microsoft/TypeScript/issues/16248#issuecomment-306034585
  const typedFilters: FunctionMap = filters
  Object.keys(typedFilters).forEach(
    (key: string) => localVue.filter(key, typedFilters[key])
  )

  localVue.component('v-icon', Icon)

  return localVue
}

/**
 * Create a testable shallowMount-ed component.
 * <p>
 * Note: shallowMount replaces child components with stubs.
 * Stubs are named to '<component-name>-stub'.
 * <p>
 * @param component
 * @param props
 */
export function shallowMount<V extends Vue>(
  component: VueClass<V>,
  props: object = {}): Wrapper<V> {

  const localVue = createLocalVue()

  return shallowMount_vtu(component, {
    localVue,
    propsData: { ...props },
  })
}
