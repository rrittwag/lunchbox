import Offer from '@/views/offers/Offer.vue'
import { render } from '@testing-library/vue'
import { gyros } from '@tests/test-data'

it('renders', () => {
  const { getByRole, getByLabelText, queryAllByRole } = render(Offer, {
    props: { offer: gyros, showDetailsInXS: true },
  })

  expect(getByRole('heading', { level: 4 })).toHaveTextContent(gyros.name)
  expect(getByLabelText('Preis')).toHaveTextContent('€3,50')
  expectNotes(queryAllByRole('note'))
})

it('renders offer without price', () => {
  const gyrosWithoutPrice = { ...gyros, price: undefined }
  const { getByRole, queryByLabelText, queryAllByRole } = render(Offer, {
    props: { offer: gyrosWithoutPrice, showDetailsInXS: true },
  })

  expect(getByRole('heading', { level: 4 })).toHaveTextContent(gyros.name)
  expect(queryByLabelText('Preis')).not.toBeInTheDocument()
  expectNotes(queryAllByRole('note'))
})

it('hides details for screen size XS', () => {
  const { getByRole, getByLabelText, queryAllByRole } = render(Offer, {
    props: { offer: gyros },
  })

  expect(getByRole('heading', { level: 4 })).toBeVisible()
  expect(getByLabelText('Preis')).toBeVisible()
  expect(queryAllByRole('note')).toHaveLength(0)
})

it('shows details for screen size XS', () => {
  const { getByRole, getByLabelText, queryAllByRole } = render(Offer, {
    props: { offer: gyros, showDetailsInXS: true },
  })

  expect(getByRole('heading', { level: 4 })).toBeVisible()
  expect(getByLabelText('Preis')).toBeVisible()
  expectNotes(queryAllByRole('note'))
})

function expectNotes(notes: HTMLElement[]) {
  expect(notes).toHaveLength(3)
  expect(notes[0]).toHaveTextContent(gyros.description)
  expect(notes[1]).toHaveTextContent(gyros.tags[0])
  expect(notes[2]).toHaveTextContent(gyros.tags[1])
}
