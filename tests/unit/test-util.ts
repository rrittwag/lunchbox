import { createLocalVue, shallowMount, Wrapper, VueClass } from '@vue/test-utils'
import Vue from 'vue'

import BootstrapVue from 'bootstrap-vue'
import * as filters from '@/filters'

/**
 * Create a testable Vue instance (instead of using global 'Vue' object),
 * including global Vue components & filters (see main.ts).
 */
function createLunchLocalVue(): typeof Vue {
  // register Bootstrap components + styles
  const localVue = createLocalVue()
  localVue.use(BootstrapVue)

  // register filters
  type FunctionMap = { [key: string]: Function }
  const typedFilters: FunctionMap = filters // bugfixing https://github.com/Microsoft/TypeScript/issues/16248#issuecomment-306034585
  Object.keys(typedFilters).forEach((key: string) => localVue.filter(key, typedFilters[key]))

  return localVue
}

/**
 * Create a testable shallowMount-ed component.
 * <p>
 * Note: shallowMount replaces child components with stubs. Stubs are named to '<component-name>-stub'.
 * <p>
 * @param component
 * @param props
 */
export function createComponent<V extends Vue>(
  component: VueClass<V>,
  props: object = {}): Wrapper<V> {

  const localVue = createLunchLocalVue()

  return shallowMount(component, {
    localVue,
    propsData: { ...props },
  })
}
