<template>
  <div class="offers">
    <DaySelector/>
    <MessageBox/>
    <OfferBoxGroup/>
  </div>
</template>



<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import { DaySelector, MessageBox, OfferBoxGroup } from './offers/'

@Component({
  components: {
    DaySelector,
    MessageBox,
    OfferBoxGroup,
  },
})
export default class Offers extends Vue {}
</script>
