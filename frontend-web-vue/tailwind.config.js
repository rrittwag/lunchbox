// eslint-disable-next-line @typescript-eslint/no-var-requires
const defaultTheme = require('tailwindcss/defaultTheme')

module.exports = {
  purge: ['/public/index.html', './src/**/*.html', './src/**/*.vue'],
  theme: {
    extend: {
      colors: {
        primary: {
          '100': 'var(--color-primary-100)',
          '200': 'var(--color-primary-200)',
          '300': 'var(--color-primary-300)',
          '400': 'var(--color-primary-400)',
          '500': 'var(--color-primary-500)',
          '600': 'var(--color-primary-600)',
          '700': 'var(--color-primary-700)',
          '800': 'var(--color-primary-800)',
          '900': 'var(--color-primary-900)',
        },
        accent: {
          '100': '#FFFBEA',
          '200': 'var(--color-accent-200)',
          '300': '#FCE588',
          '400': '#FADB5F',
          '500': '#F7C948',
          '600': '#F0B429',
          '700': '#DE911D',
          '800': '#CB6E17',
          '900': '#B44D12',
        },
        neutral: {
          '100': 'var(--color-neutral-100)',
          '200': 'var(--color-neutral-200)',
          '300': 'var(--color-neutral-300)',
          '400': 'var(--color-neutral-400)',
          '500': 'var(--color-neutral-500)',
          '600': 'var(--color-neutral-600)',
          '700': 'var(--color-neutral-700)',
          '800': 'var(--color-neutral-800)',
          '900': 'var(--color-neutral-900)',
        },
        success: {
          '200': 'var(--color-success-200)',
        },
        card: 'var(--color-card)',
      },
      maxWidth: {
        '4md': '112rem', // 4 x OfferBox mit max-w-md (siehe OfferBoxGroup)
      },
      fontFamily: {
        sans: ['Inter', ...defaultTheme.fontFamily.sans],
        display: ['Chewy', ...defaultTheme.fontFamily.serif],
      },
    },
  },
  variants: {
    textColor: ['responsive', 'hover', 'focus', 'disabled', 'active'],
    opacity: ['responsive', 'hover', 'focus', 'disabled'],
    cursor: ['responsive', 'disabled'],
  },
  plugins: [],
}