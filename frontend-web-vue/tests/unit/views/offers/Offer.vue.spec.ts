import Offer from '@/views/offers/Offer.vue'
import { gyros } from '@tests/unit/test-data'
import { render } from '@testing-library/vue'

describe('Offer', () => {
  it('renders', () => {
    const { getAllByRole, getByRole, getByLabelText } = render(Offer, {
      props: { offer: gyros },
    })

    expect(getByRole('heading', { level: 4 })).toHaveTextContent(gyros.name)
    expect(getByLabelText('Preis')).toHaveTextContent('€3,50')
    const notes = getAllByRole('note')
    expect(notes).toHaveLength(3)
    expect(notes[0]).toHaveTextContent(gyros.description)
    expect(notes[1]).toHaveTextContent(gyros.tags[0])
    expect(notes[2]).toHaveTextContent(gyros.tags[1])
  })

  it('renders offer without price', () => {
    const gyrosWithoutPrice = { ...gyros, price: undefined }
    const { getAllByRole, getByRole, queryByLabelText } = render(Offer, {
      props: { offer: gyrosWithoutPrice },
    })

    expect(getByRole('heading', { level: 4 })).toHaveTextContent(gyros.name)
    expect(queryByLabelText('Preis')).not.toBeInTheDocument()
    const notes = getAllByRole('note')
    expect(notes).toHaveLength(3)
    expect(notes[0]).toHaveTextContent(gyros.description)
    expect(notes[1]).toHaveTextContent(gyros.tags[0])
    expect(notes[2]).toHaveTextContent(gyros.tags[1])
  })

  it('hides details for screen size XS', () => {
    const { getByRole, getByLabelText } = render(Offer, {
      props: { offer: gyros },
    })

    expect(getByRole('heading', { level: 4 })).toBeVisible()
    expect(getByLabelText('Preis')).toBeVisible()
    // FIXME: Tailwind classes do not work with testing-library -> https://stackoverflow.com/a/74160802
    // getAllByRole('note').forEach((elem) => expect(elem).not.toBeVisible())
  })

  it('shows details for screen size XS', () => {
    const { getAllByRole, getByRole, getByLabelText } = render(Offer, {
      props: { offer: gyros, showDetailsInXS: true },
    })

    expect(getByRole('heading', { level: 4 })).toBeVisible()
    expect(getByLabelText('Preis')).toBeVisible()
    getAllByRole('note').forEach((elem) => expect(elem).toBeVisible())
  })
})
