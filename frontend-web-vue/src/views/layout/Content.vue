<template>
  <main class="w-full h-full">
    <RouterView v-slot="{ Component }">
      <transition
        mode="out-in"
        leave-active-class="transition-opacity duration-150"
        leave-to-class="opacity-0"
        enter-active-class="transition-opacity duration-150"
        enter-from-class="opacity-0"
      >
        <component :is="Component" />
      </transition>
    </RouterView>
  </main>
</template>
