import type { RouteRecordRaw } from 'vue-router'
import userEvent from '@testing-library/user-event'
import { render, within } from '@testing-library/vue'
import { createRouter, createWebHistory } from 'vue-router'
import NavLink from '@/views/layout/header/NavLink.vue'

beforeEach(async () => {
  await router.push('/')
})

it('renders', () => {
  const { getByRole } = render(NavLink, {
    props: { to: mockRoute.path },
    slots: { default: 'Link Text' },
    global: {
      plugins: [router],
    },
  })

  const listitem = getByRole('listitem')
  const link = within(listitem).getByRole('link', { name: 'Mock-Route' })
  expect(link).toHaveAttribute('href', mockRoute.path)
  within(listitem).getByText('Link Text')
})

it('is marked as current page WHEN route is active', async () => {
  const user = userEvent.setup()
  const { getByRole, queryByRole } = render(NavLink, {
    props: { to: mockRoute.path },
    slots: { default: 'Link Text' },
    global: {
      plugins: [router],
    },
  })
  expect(queryByRole('link', { current: 'page' })).not.toBeInTheDocument()

  const link = getByRole('link', { name: 'Mock-Route' })
  await user.click(link)

  expect(queryByRole('link', { current: 'page' })).toHaveAccessibleName('Mock-Route')
})

// --- mocks 'n' stuff

const homeRoute = {
  path: '/',
  meta: { title: 'Home' },
  component: {},
} as unknown as RouteRecordRaw
const mockRoute = {
  path: '/mock-route',
  meta: { title: 'Mock-Route' },
  component: {},
} as unknown as RouteRecordRaw

const router = createRouter({
  history: createWebHistory(),
  routes: [homeRoute, mockRoute],
})
