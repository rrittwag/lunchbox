import Content from '@/views/layout/Content.vue'
import { mount } from '@vue/test-utils'
import { createRouterMock, injectRouterMock } from 'vue-router-mock'
import { RouterView } from 'vue-router'

describe('Content', () => {
  const router = createRouterMock({})
  beforeEach(() => {
    injectRouterMock(router)
  })

  test('renders snapshot', () => {
    const wrapper = mount(Content)

    expect(wrapper.element).toMatchSnapshot()
  })

  test('contains main and RouterView', () => {
    const wrapper = mount(Content)

    const mainTag = wrapper.get('main')
    mainTag.getComponent(RouterView) // throws error if not existing
  })
})
