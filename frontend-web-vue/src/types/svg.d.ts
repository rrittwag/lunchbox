// module definition for svg file import
// https://vue-svg-loader.js.org/faq.html#how-to-use-this-loader-with-typescript
declare module '*.svg' {
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const content: any
  export default content
}
