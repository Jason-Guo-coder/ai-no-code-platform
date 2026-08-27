<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import BasicLayout from '@/layouts/BasicLayout.vue'

const route = useRoute()
const isStandalonePage = computed(() => route.meta.layout === 'standalone')
</script>

<template>
  <RouterView v-if="isStandalonePage" />
  <BasicLayout v-else />
</template>
