<template>
  <main class="w-full h-full">
    <transition
      mode="out-in"
      leave-active-class="transition-opacity duration-150"
      leave-to-class="opacity-0"
      enter-active-class="transition-opacity duration-150"
      enter-class="opacity-0"
    >
      <router-view />
    </transition>
  </main>
</template>
