<template>
  <div class="home">
    <DaySelector/>
    <MessageBox/>
    <Offers/>
  </div>
</template>



<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import DaySelector from './components/DaySelector.vue'
import MessageBox from './components/MessageBox.vue'
import Offers from './offers/Offers.vue'

@Component({
  components: {
    DaySelector,
    MessageBox,
    Offers,
  },
})
export default class Home extends Vue {}
</script>
