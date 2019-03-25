import {
  createLocalVue as createLocalVue_vtu,
  shallowMount,
  Wrapper,
  VueClass
} from '@vue/test-utils'
import Vue from 'vue'

import BootstrapVue from 'bootstrap-vue'
import { applyFilters } from '@/plugins/filters'
import { applyAwesome } from '@/plugins/vue-awesome'

/**
 * Create a testable Vue instance (instead of using global 'Vue' object),
 * including global Vue components & filters (see main.ts).
 */
function createLocalVue(): typeof Vue {
  const localVue = createLocalVue_vtu()

  localVue.use(BootstrapVue)
  applyFilters(localVue)
  applyAwesome(localVue)

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
export function mountUnit<V extends Vue>(
  component: VueClass<V>,
  props: object = {}): Wrapper<V> {

  const localVue = createLocalVue()

  return shallowMount(component, {
    localVue,
    propsData: { ...props },
  })
}
