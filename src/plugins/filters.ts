import Vue from 'vue'
import * as filters from '@/filters'

interface FunctionMap {
  // tslint:disable-next-line ban-types
  [key: string]: Function
}

export const applyFilters = (vue: typeof Vue) => {
  // bugfixing https://github.com/Microsoft/TypeScript/issues/16248#issuecomment-306034585
  const typedFilters: FunctionMap = filters
  Object.keys(typedFilters).forEach(
    (key: string) => vue.filter(key, typedFilters[key])
  )
}

applyFilters(Vue)
