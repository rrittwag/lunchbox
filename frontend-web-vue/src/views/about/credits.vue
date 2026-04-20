<script setup lang="ts">
import Tooltip from './components/Tooltip.vue'
</script>

<template>
  <div id="infobox-credits">
    <h2 class="text-3xl">
      Credits
    </h2>
    <table>
      <tbody class="align-top">
        <tr class="border-b">
          <td width="35%">
            Projekt
          </td>
          <td>Initiator</td>
          <td width="30%" />
          <td width="20%">
            rori
          </td>
        </tr>
        <tr class="border-b">
          <td>Server</td>
          <td>
            Programmierung<br>Hosting
          </td>
          <td>
            <Tooltip text="Scala" alignment="top">
              <a href="http://www.scala-lang.org/" target="_blank"><img alt="Scala" width="20px" height="20px" src="@/assets/images/credits/scala_logo.png"></a>
            </Tooltip>
            <Tooltip text="Play Framework" alignment="top">
              <a href="http://playframework.com/" target="_blank"><img alt="Akka" width="20px" height="20px" src="@/assets/images/credits/play_logo.png"></a>
            </Tooltip>
            <br>
            <Tooltip text="Docker" alignment="top">
              <a href="https://www.docker.com/" target="_blank"><img alt="Docker" width="20px" height="20px" src="@/assets/images/credits/docker_logo.png"></a>
            </Tooltip>
          </td>
          <td>rori</td>
        </tr>
        <tr class="border-b">
          <td>Web-App</td>
          <td>
            Programmierung<br>Design
          </td><td>
            <Tooltip text="Typescript" alignment="top">
              <a href="https://www.typescriptlang.org/" target="_blank"><img alt="Typescript" width="20px" height="20px" src="@/assets/images/credits/typescript_logo.png"></a>
            </Tooltip>
            <Tooltip text="Vue.js" alignment="top">
              <a href="https://vuejs.org/" target="_blank"><img alt="Vue.JS" width="20px" height="20px" src="@/assets/images/credits/vuejs_logo.png"></a>
            </Tooltip>
            <br>
            <Tooltip text="Tailwindcss" alignment="top">
              <a href="https://tailwindcss.com/" target="_blank"><img alt="Tailwindcss" width="20px" height="20px" src="@/assets/images/credits/tailwindcss_logo.png"></a>
            </Tooltip>
            <Tooltip text="Pinia" alignment="top">
              <a href="https://pinia.vuejs.org/" target="_blank"><img alt="Pinia" width="20px" height="20px" src="@/assets/images/credits/pinia_logo.png"></a>
            </Tooltip>
          </td>
          <td>rori</td>
        </tr>
        <tr>
          <td>Android-App</td>
          <td>
            Programmierung
          </td><td>
            <Tooltip text="Android" alignment="top">
              <a href="http://developer.android.com/" target="_blank"><img alt="Android" width="20px" height="20px" src="@/assets/images/credits/android_logo.png"></a>
            </Tooltip>
            <Tooltip text="Java" alignment="top">
              <a href="http://www.oracle.com/technetwork/java/index.html" target="_blank"><img alt="Java" width="20px" height="20px" src="@/assets/images/credits/java_logo.png"></a>
            </Tooltip>
          </td>
          <td>mbe, an</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<style>
td {
  padding: 4px;
}
</style>
