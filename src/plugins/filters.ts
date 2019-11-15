import Vue from 'vue'
import * as filters from '@/filters'

interface FunctionMap {
  [key: string]: Function
}

// bugfixing https://github.com/Microsoft/TypeScript/issues/16248#issuecomment-306034585
const typedFilters: FunctionMap = filters
Object.keys(typedFilters).forEach((key: string) => Vue.filter(key, typedFilters[key]))
