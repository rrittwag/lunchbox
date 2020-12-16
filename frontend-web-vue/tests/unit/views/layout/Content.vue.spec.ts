import Content from '/@/views/layout/Content.vue'
import { mount } from '@vue/test-utils'
import { createMemoryHistory, createRouter, RouteRecordRaw } from 'vue-router'

describe('Content', () => {
  test('renders snapshot', () => {
    const wrapper = mount(Content, {
      global: { plugins: [mockRouter] },
    })

    expect(wrapper.element).toMatchSnapshot()
  })
})

// --- mocks 'n' stuff

const homeRoute = ({ path: '/', meta: { title: 'Home' } } as unknown) as RouteRecordRaw
const routes = [homeRoute]

const mockRouter = createRouter({
  history: createMemoryHistory(),
  routes,
})
