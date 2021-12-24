import Nav from '@/views/layout/header/Nav.vue'
import { mount } from '@vue/test-utils'
import { describe, test, expect } from 'vitest'

describe('Nav', () => {
  test('renders snapshot', () => {
    const wrapper = mount(Nav, {
      slots: { default: '<li><a href="/">link1</a></li>  <li><a href="/">link2</a></li>' },
    })

    expect(wrapper.element).toMatchSnapshot()
  })

  test('is nav tag with aria-label (a11y)', () => {
    const wrapper = mount(Nav, {
      slots: { default: '<li><a href="/">link1</a></li>  <li><a href="/">link2</a></li>' },
    })

    const navTag = wrapper.get('nav')
    expect(navTag.attributes()['aria-label']).toBeDefined()
    wrapper.get('nav > ul') // throws error if not existing
  })

  test('renders slot NavLinks', () => {
    const wrapper = mount(Nav, {
      slots: { default: '<li><a href="/">link1</a></li>  <li><a href="/">link2</a></li>' },
    })

    const navLinks = wrapper.findAll('ul > li')
    expect(navLinks).toHaveLength(2)
    expect(navLinks[0].text()).toBe('link1')
    expect(navLinks[1].text()).toBe('link2')
  })
})
