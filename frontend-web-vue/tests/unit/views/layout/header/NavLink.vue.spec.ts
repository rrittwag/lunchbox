import NavLink from '@/views/layout/header/NavLink.vue'
import { mount } from '@vue/test-utils'
import { RouteRecordRaw, RouterLink } from 'vue-router'
import { createRouterMock, injectRouterMock } from 'vue-router-mock'
import { describe, test, expect, beforeEach } from 'vitest'

describe('NavLink', () => {
  beforeEach(() => {
    injectRouterMock(mockRouter)
  })

  test('renders snapshot', () => {
    const wrapper = mount(NavLink, {
      props: { to: mockRoute.path },
      slots: { default: 'Link Text' },
      global: {
        stubs: { RouterLink }, // vue-router-mock defaults RouterLink to stub. But this test needs full render. -> https://github.com/posva/vue-router-mock#stubs
      },
    })

    expect(wrapper.element).toMatchSnapshot()
  })

  test('renders link tag for route', () => {
    const wrapper = mount(NavLink, {
      props: { to: mockRoute.path },
      slots: { default: 'Link Text' },
      global: {
        stubs: { RouterLink }, // vue-router-mock defaults RouterLink to stub. But this test needs full render. -> https://github.com/posva/vue-router-mock#stubs
      },
    })

    const link = wrapper.get('li > a')
    expect(link.attributes()['href']).toBe(mockRoute.path)
    expect(link.attributes()['title']).toBe(mockRoute.meta?.title)
    expect(link.attributes()['aria-label']).toBe(mockRoute.meta?.title)
    expect(link.text()).toBe('Link Text')
  })

  test('adds aria-current if route is active', async () => {
    const wrapper = mount(NavLink, {
      props: { to: mockRoute.path },
      slots: { default: 'Link Text' },
      global: {
        stubs: { RouterLink },
      },
    })

    await mockRouter.push(mockRoute.path)
    await wrapper.vm.$nextTick()

    const link = wrapper.get('a')
    expect(link.attributes()['aria-current']).toBe('page')
  })
})

// --- mocks 'n' stuff

const mockRoute = {
  path: '/mock-route',
  meta: { title: 'Mock-Route' },
  component: {},
} as unknown as RouteRecordRaw
const mockRouter = createRouterMock({ routes: [mockRoute] })
