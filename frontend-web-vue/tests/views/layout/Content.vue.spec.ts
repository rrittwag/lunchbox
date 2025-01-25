import type { RouteRecordRaw } from 'vue-router'
import Content from '@/views/layout/Content.vue'
import { render, within } from '@testing-library/vue'
import { createRouter, createWebHistory } from 'vue-router'

it('renders', async () => {
  await router.push('/')
  const { getByRole } = render(Content, {
    global: {
      plugins: [router],
    },
  })

  const main = getByRole('main')
  expect(within(main).getByRole('heading')).toHaveTextContent('Some title')
})

// --- mocks 'n' stuff

const homeRoute = {
  path: '/',
  component: {
    template: '<h1>Some title</h1>',
  },
} as unknown as RouteRecordRaw

const router = createRouter({
  history: createWebHistory(),
  routes: [homeRoute],
})
