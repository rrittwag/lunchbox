module.exports = {
  theme: {
    extend: {
      colors: {
        // Farben sind
        primary: {
          '100': '#FFEEEE',
          '200': 'var(--color-primary-200)',
          '300': '#F79E9E',
          '400': 'var(--color-primary-400)',
          '500': '#D14545',
          '600': '#B83B3B',
          '700': '#993030',
          '800': '#7D1B1B',
          '900': '#610404',
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
          '200': '#E2F7DE',
        },
      },
    },
  },
  variants: {
    textColor: ['responsive', 'hover', 'focus', 'disabled'],
    cursor: ['responsive', 'disabled'],
  },
  plugins: [],
}
