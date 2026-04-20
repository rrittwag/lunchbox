<script setup lang="ts">
const props = defineProps<{
  text: string
  alignment?: 'top' | 'bottom'
}>()
const tooltipClass = ['tooltiptext']
if (props.alignment) {
  if (props.alignment === 'bottom') {
    tooltipClass.push('tooltipBottomAllignmet')
  }
  if (props.alignment === 'top') {
    tooltipClass.push('tooltipTopAllignmet')
  }
}
else {
  tooltipClass.push('tooltipBottomAllignmet ')
}
</script>

<template>
  <div class="tooltip">
    <slot />
    <span :class="tooltipClass.join(' ')"> {{ props.text }}</span>
  </div>
</template>

<!-- From https://www.w3schools.com/css/css_tooltip.asp -->
<style>
/* Tooltip container */
.tooltip {
  position: relative;
  display: inline-block;
  border-bottom: 0px dotted black; /* Add dots under the hoverable text */
}

/* Tooltip text */
.tooltiptext {
  visibility: hidden; /* Hidden by default */
  width: max-content;
  padding: 5px;
  max-width: 200px;
  background-color: black;
  color: #ffffff;
  text-align: center;
  border-radius: 6px;
  position: absolute;
  z-index: 1; /* Ensure tooltip is displayed above content */
  left: 50%;
  transform: translateX(-50%);
  opacity: 0;
  transition: opacity 500ms;
}

/* Show the tooltip text on hover */
.tooltip:hover .tooltiptext {
  visibility: visible;
  opacity: 1;
  /*transition-delay: 300ms;*/
}

.tooltiptext::after {
  content: " ";
  position: absolute;
  left: 50%;
  margin-left: -5px;
  border-width: 5px;
  border-style: solid;
  /*border-color: transparent transparent black transparent;*/
}

.tooltipTopAllignmet {
  bottom: 100%;
}

.tooltipTopAllignmet::after {
  top: 100%; /* At the top of the tooltip */
  border-color: black transparent transparent transparent;
}

.tooltipBottomAllignmet {
  top: 100%;
}

.tooltipBottomAllignmet::after {
  bottom: 100%; /* At the bottom of the tooltip */
  border-color: transparent transparent black transparent;
}
</style>
