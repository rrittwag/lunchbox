<template>
  <div class="offers">
    <DaySelector/>
    <MessageBox/>
    <OfferBoxGroup/>
  </div>
</template>



<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import DaySelector from './components/DaySelector.vue'
import MessageBox from './components/MessageBox.vue'
import OfferBoxGroup from './components/OfferBoxGroup.vue'

@Component({
  components: {
    DaySelector,
    MessageBox,
    OfferBoxGroup,
  },
})
export default class Offers extends Vue {}
</script>
