import { mountWithChildren } from '@tests/unit/test-util'
import NavLink from '@/views/layout/header/NavLink.vue'
import VueRouter from 'vue-router'

describe('NavLink', () => {
  test('renders snapshot', () => {
    const router = new VueRouter({ routes })

    const wrapper = mountWithChildren(
      NavLink,
      {
        to: mockRoute.path,
        exact: true,
      },
      { applyRouter: true, router }
    )

    expect(wrapper.element).toMatchSnapshot()
  })

  test('renders link tag for route', () => {
    const router = new VueRouter({ routes })

    const wrapper = mountWithChildren(
      NavLink,
      {
        to: mockRoute.path,
        exact: true,
      },
      { applyRouter: true, router }
    )

    const link = wrapper.find('a')
    expect(link.exists()).toBeTrue()
    expect(link.attributes()['href']).toBe(mockRoute.path)
    expect(link.attributes()['title']).toBe(mockRoute.meta.title)
    expect(link.attributes()['aria-label']).toBe(mockRoute.meta.title)
  })

  test('adds aria-current if route is active', async () => {
    const router = new VueRouter({ routes })

    const wrapper = mountWithChildren(
      NavLink,
      {
        to: mockRoute.path,
        exact: true,
      },
      { applyRouter: true, router }
    )

    await router.push(mockRoute.path)
    await wrapper.vm.$nextTick()

    const link = wrapper.find('a')
    expect(link.exists()).toBeTrue()
    expect(link.attributes()['aria-current']).toBe('page')
  })
})

// --- mocks 'n' stuff

const mockRoute = {
  path: '/mock-route',
  meta: {
    title: 'Mock-Route',
  },
}

const routes = [mockRoute]
