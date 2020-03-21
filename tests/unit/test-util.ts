import Vue, { ComponentOptions } from 'vue'
import Vuex from 'vuex'
import {
  createLocalVue,
  shallowMount,
  VueClass,
  Wrapper,
  mount,
  MountOptions,
} from '@vue/test-utils'
import VueRouter from 'vue-router'
import jestMock from 'jest-mock'

export interface LocalVueOptions {
  applyRouter?: boolean
  applyVuex?: boolean
}

function createLocalVueInternal(options: LocalVueOptions): typeof Vue {
  const localVue = createLocalVue()
  options.applyRouter && localVue.use(VueRouter)
  options.applyVuex && localVue.use(Vuex)
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
 * @param mountOptions
 */
export function mountUnit<V extends Vue>(
  component: VueClass<V> | ComponentOptions<V>,
  props: object = {},
  mountOptions: MountOptions<V> & LocalVueOptions = {}
): Wrapper<V> {
  return shallowMount(component as VueClass<V>, {
    localVue: createLocalVueInternal(mountOptions),
    propsData: { ...props },
    ...mountOptions,
  })
}

/**
 * Create a testable mount-ed component, including children.
 * <p>
 * @param component
 * @param props
 * @param mountOptions
 */
export function mountWithChildren<V extends Vue>(
  component: VueClass<V> | ComponentOptions<V>,
  props: object = {},
  mountOptions: MountOptions<V> & LocalVueOptions = {}
): Wrapper<V> {
  const localVue = createLocalVue()
  mountOptions.applyRouter && localVue.use(VueRouter)
  mountOptions.applyVuex && localVue.use(Vuex)

  return mount(component as VueClass<V>, {
    localVue: createLocalVueInternal(mountOptions),
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
  // eslint-disable-next-line @typescript-eslint/no-non-null-assertion
  const MockConstructor = jestMock.generateFromMetadata(jestMock.getMetadata(clazz)!)
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
