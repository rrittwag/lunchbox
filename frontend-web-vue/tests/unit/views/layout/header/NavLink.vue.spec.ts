import NavLink from '@/views/layout/header/NavLink.vue'
import { mount } from '@vue/test-utils'
import { createMemoryHistory, createRouter, RouteRecordRaw } from 'vue-router'

describe('NavLink', () => {
  test('renders snapshot', () => {
    const wrapper = mount(NavLink, {
      props: { to: mockRoute.path },
      global: { plugins: [mockRouter] },
    })

    expect(wrapper.element).toMatchSnapshot()
  })

  test('renders link tag for route', () => {
    const wrapper = mount(NavLink, {
      props: { to: mockRoute.path },
      global: { plugins: [mockRouter] },
    })

    const link = wrapper.find('a')
    expect(link.exists()).toBe(true)
    expect(link.attributes()['href']).toBe(mockRoute.path)
    expect(link.attributes()['title']).toBe(mockRoute.meta?.title)
    expect(link.attributes()['aria-label']).toBe(mockRoute.meta?.title)
  })

  test('adds aria-current if route is active', async () => {
    const wrapper = mount(NavLink, {
      props: { to: mockRoute.path },
      global: { plugins: [mockRouter] },
    })

    await mockRouter.push(mockRoute.path)
    await wrapper.vm.$nextTick()

    const link = wrapper.find('a')
    expect(link.exists()).toBe(true)
    expect(link.attributes()['aria-current']).toBe('page')
  })
})

// --- mocks 'n' stuff

const homeRoute = {
  path: '/',
  meta: { title: 'Home' },
  component: {},
} as unknown as RouteRecordRaw
const mockRoute = {
  path: '/mock-route',
  meta: {
    title: 'Mock-Route',
  },
  component: {},
} as unknown as RouteRecordRaw

const routes = [mockRoute, homeRoute]

const mockRouter = createRouter({
  history: createMemoryHistory(),
  routes,
})
