/* eslint-disable @typescript-eslint/no-var-requires */
const tailwindcss = require('tailwindcss')
const autoprefixer = require('autoprefixer')
const focusvisible = require('postcss-focus-visible')

// Only add purgecss in production
const purgecss =
  process.env.NODE_ENV === 'production'
    ? require('@fullhuman/postcss-purgecss')({
        content: ['./public/index.html', './src/**/*.html', './src/**/*.vue'],
        // Include any special characters you're using in this regular expression.
        // See: https://tailwindcss.com/docs/controlling-file-size/#understanding-the-regex
        defaultExtractor: content => content.match(/[\w-/:]+(?<!:)/g) || [],
        // Whitelist auto generated classes for transitions and router links.
        // From: https://github.com/ky-is/vue-cli-plugin-tailwind
        whitelistPatterns: [
          /-(leave|enter|appear)(|-(to|from|active))$/,
          /^(?!(|.*?:)cursor-move).+-move$/,
          /^router-link(|-exact)-active$/,
          /^theme-.*$/,
          /^js-focus-visible$/,
        ],
        variables: true,
      })
    : ''

module.exports = {
  plugins: [tailwindcss, autoprefixer, focusvisible, purgecss],
}
