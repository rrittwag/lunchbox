import OfferBox from '@/views/offers/OfferBox.vue'
import userEvent from '@testing-library/user-event'
import { render, within } from '@testing-library/vue'
import { gyros, mensa, soljanka } from '@tests/test-data'

it('renders', () => {
  const { getByRole } = render(OfferBox, {
    props: { provider: mensa, offers: [gyros, soljanka] },
  })

  const offerbox = getByRole('article')
  expect(within(offerbox).getByRole('heading', { level: 3 })).toHaveTextContent(mensa.name)
  const list = within(offerbox).getByRole('list')
  const items = within(list).getAllByRole('listitem')
  expect(items).toHaveLength(2)
  expect(items[0]).toHaveTextContent(/Gyros/)
  expect(items[1]).toHaveTextContent(/Soljanka/)
})

it('renders just title WHEN offers are empty', () => {
  const { getByRole, queryByRole } = render(OfferBox, {
    props: { provider: mensa, offers: [] },
  })

  expect(getByRole('heading', { level: 3 })).toHaveTextContent(mensa.name)
  expect(queryByRole('list')).not.toBeInTheDocument()
})

// FIXME: jsdom does not support Tailwind v4 generated CSS-File, maybe because of @property?
it.skip('hides details for screen size XS', () => {
  const { queryAllByRole } = render(OfferBox, {
    props: { provider: mensa, offers: [gyros, soljanka] },
  })

  // FIXME: "aria-expanded" is not supported on role "article"
  // expect(getByRole('article', { expanded: false })).toBeInTheDocument()
  expect(queryAllByRole('note')).toHaveLength(0)
})

// FIXME: jsdom does not support Tailwind v4 generated CSS-File, maybe because of @property?
it.skip('wHEN clicked  THEN show details for screen size XS', async () => {
  const user = userEvent.setup()
  const { getByRole, queryAllByRole } = render(OfferBox, {
    props: { provider: mensa, offers: [gyros, soljanka] },
  })
  expect(queryAllByRole('note')).toHaveLength(0)

  await user.click(getByRole('article'))

  expect(queryAllByRole('note')).not.toHaveLength(0)
})
