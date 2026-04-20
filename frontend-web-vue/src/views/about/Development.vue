<script setup lang="ts">
import LinkButton from '@/views/about/components/LinkButton.vue'
import LunchLink from '@/views/about/components/LunchLink.vue'
</script>

<template>
  <div>
    <h2 class="text-3xl">
      Entwicklung
    </h2>
    <p>Lunchbox ist eine Spielwiese für Technik und Design! Die <LunchLink text="REST API" href="/api-docs" no-text-wrap inline /> ermöglicht verschiedenste Client- &amp; Server-Implementierungen.</p>
    <LinkButton text="Spiel mit! &raquo;" href="https://github.com/rori-dev/lunchbox" />
  </div>
</template>
