import Vue from 'vue'
import * as filters from '@/filters'

type FunctionMap = { [key: string]: Function }
const typedFilters: FunctionMap = filters // bugfixing https://github.com/Microsoft/TypeScript/issues/16248#issuecomment-306034585
Object.keys(typedFilters).forEach((key: string) => Vue.filter(key, typedFilters[key]))
