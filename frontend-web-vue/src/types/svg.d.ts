// module definition for svg file import
// https://vue-svg-loader.js.org/faq.html#how-to-use-this-loader-with-typescript
declare module '*.svg' {
  import type { DefineComponent } from 'vue'
  const component: DefineComponent
  export default component
}
