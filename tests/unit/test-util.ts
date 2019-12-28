import Vue, { ComponentOptions } from 'vue'
import Vuex from 'vuex'
import {
  createLocalVue as createLocalVue_vtu,
  shallowMount,
  VueClass,
  Wrapper,
  mount,
  MountOptions,
} from '@vue/test-utils'
import * as filters from '@/filters'
import Icon from 'vue-awesome/components/Icon.vue'
import VueRouter from 'vue-router'
import jestMock from 'jest-mock'

function applyRouter(localVue: typeof Vue) {
  localVue.use(VueRouter)
}

function applyVuex(localVue: typeof Vue) {
  localVue.use(Vuex)
}

function applyFilters(localVue: typeof Vue) {
  interface FunctionMap {
    // tslint:disable-next-line ban-types
    [key: string]: Function
  }
  // bugfixing https://github.com/Microsoft/TypeScript/issues/16248#issuecomment-306034585
  const typedFilters: FunctionMap = filters
  Object.keys(typedFilters).forEach((key: string) => localVue.filter(key, typedFilters[key]))
}

function applyAwesome(localVue: typeof Vue) {
  localVue.component('v-icon', Icon)
}

/**
 * Create a testable Vue instance (instead of using global 'Vue' object),
 * including global Vue components & filters (see main.ts).
 */
export function createLocalVue(): typeof Vue {
  const localVue = createLocalVue_vtu()

  applyRouter(localVue)
  applyVuex(localVue)
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
export function mountUnit<V extends Vue>(component: VueClass<V> | ComponentOptions<V>): Wrapper<V>

export function mountUnit<V extends Vue>(
  component: VueClass<V>,
  props: object,
  mountOptions?: MountOptions<V>
): Wrapper<V>

export function mountUnit<V extends Vue>(
  component: object,
  props: object = {},
  mountOptions: MountOptions<V> = {}
): Wrapper<V> {
  const localVue = createLocalVue()

  return shallowMount(component, {
    localVue,
    propsData: { ...props },
    ...mountOptions,
  })
}

/**
 * Create a testable mount-ed component, including children.
 * <p>
 * @param component
 * @param props
 */
export function mountWithChildren<V extends Vue>(
  component: VueClass<V> | ComponentOptions<V>
): Wrapper<V>

export function mountWithChildren<V extends Vue>(
  component: VueClass<V>,
  props: object,
  mountOptions?: MountOptions<V>
): Wrapper<V>

export function mountWithChildren<V extends Vue>(
  component: object,
  props: object = {},
  mountOptions: MountOptions<V> = {}
): Wrapper<V> {
  const localVue = createLocalVue()

  return mount(component, {
    localVue,
    propsData: { ...props },
    ...mountOptions,
  })
}

/**
 * Wait x millisecionds.
 * <p>
 * Useful in tests, that use implicit async calls.
 * <p>
 * @param millis delay by milliseconds
 * @param func function to be called
 */
export function delayBy(millis: number, func: () => void) {
  setTimeout(func, millis)
}

/**
 * Wait 1ms.
 * <p>
 * Useful in tests, that use implicit async calls.
 * <p>
 * @param func function to be called
 */
export function delay(func: () => void) {
  setTimeout(func, 1)
}

// eslint-disable-next-line @typescript-eslint/no-explicit-any
type Constructor<T> = new (...args: any[]) => T

/**
 * Create a mock instance of clazz.
 * <p>
 * Implementation is a mix of
 * - https://github.com/asvetliakov/jest-create-mock-instance
 * - https://github.com/asvetliakov/jest-create-mock-instance/issues/4#issuecomment-412713215
 * <p>
 * @param clazz Class to be mocked.
 */
export function createMock<T>(clazz: Constructor<T>): jest.Mocked<T> {
  var MockConstructor = jestMock.generateFromMetadata(jestMock.getMetadata(clazz)!)
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const mock: any = new MockConstructor()
  // Constructing by generateFromMetadata has 2 problems:
  // - it does not mock getters
  // - it does not work with jest.clearAllMocks()
  // Let's mock all functions manually!
  for (const key in Object.getOwnPropertyDescriptors(mock)) mock[key] = jest.fn()
  for (const key in Object.getOwnPropertyDescriptors(clazz.prototype)) mock[key] = jest.fn()
  return mock
}
