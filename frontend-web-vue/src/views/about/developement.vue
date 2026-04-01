<script setup lang="ts">
import linkButton from './components/LinkButton.vue'
import lunchLink from './components/LunchLink.vue'
</script>

<template>
  <div>
    <h2 class="text-3xl">
      Entwicklung
    </h2>
    <p>Lunchbox ist eine Spielwiese für Technik und Design! Die <lunchLink text="REST API" href="/api-docs" no-text-wrap inline /> ermöglicht verschiedenste Client- &amp; Server-Implementierungen.</p>
    <linkButton text="Spiel mit! &raquo;" href="https://github.com/rori-dev/lunchbox" />
  </div>
</template>
