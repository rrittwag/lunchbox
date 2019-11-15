const tailwindcss = require('tailwindcss')
const autoprefixer = require('autoprefixer')

// Only add purgecss in production
const purgecss =
  process.env.NODE_ENV === 'production'
    ? require('@fullhuman/postcss-purgecss')({
        content: ['./src/**/*.html', './src/**/*.vue'],
      })
    : ''

module.exports = {
  plugins: [tailwindcss, autoprefixer, purgecss],
}
