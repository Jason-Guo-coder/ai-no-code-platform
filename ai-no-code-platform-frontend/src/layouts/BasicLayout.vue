<script setup lang="ts">
import GlobalFooter from '@/components/GlobalFooter.vue'
import GlobalHeader from '@/components/GlobalHeader.vue'
import { routes } from '@/router'
</script>

<template>
  <a-layout class="basic-layout">
    <GlobalHeader :routes="routes" />
    <a-layout-content class="layout-content">
      <main class="content-inner">
        <RouterView />
      </main>
    </a-layout-content>
    <GlobalFooter />
  </a-layout>
</template>

<style>
:root {
  --font-stack: -apple-system, BlinkMacSystemFont, "SF Pro Display", "SF Pro Text", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-family: var(--font-stack);
  color: #000000;
  background: #ffffff;
  --ios-blue: #007aff;
  --ios-blue-dark: #0062cc;
  --ios-green: #34c759;
  --ios-orange: #ff9500;
  --ios-red: #ff3b30;
  --ios-purple: #af52de;
  --ios-pink: #ff2d55;
  --ios-teal: #30b0c7;
  --ios-yellow: #ffcc00;
  --ios-indigo: #5856d6;
  --bg-primary: #ffffff;
  --bg-secondary: #f2f2f7;
  --bg-tertiary: #ffffff;
  --bg-elevated: #ffffff;
  --bg-glass: rgba(255, 255, 255, 0.72);
  --bg-glass-strong: rgba(255, 255, 255, 0.85);
  --label-primary: #000000;
  --label-secondary: #86868b;
  --label-tertiary: #c7c7cc;
  --label-quaternary: #e8e8ed;
  --separator: rgba(60, 60, 67, 0.18);
  --separator-opaque: #c6c6c8;
  --shadow-card: 0 4px 28px rgba(0, 0, 0, 0.06);
  --shadow-card-hover: 0 12px 40px rgba(0, 0, 0, 0.1);
  --shadow-nav: 0 1px 0 rgba(0, 0, 0, 0.06);
  --radius-sm: 10px;
  --radius-md: 16px;
  --radius-lg: 22px;
  --radius-xl: 30px;
  --ios-text: var(--label-primary);
  --ios-secondary: var(--label-secondary);
  --ios-tertiary: var(--label-tertiary);
  --ios-line: var(--separator);
  --ios-surface: var(--bg-glass);
  --ios-surface-solid: var(--bg-elevated);
  --ios-background: #f2f2f7;
  font-synthesis: none;
  text-rendering: optimizeLegibility;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

[data-theme='dark'] {
  --bg-primary: #000000;
  --bg-secondary: #1c1c1e;
  --bg-tertiary: #2c2c2e;
  --bg-elevated: #1c1c1e;
  --bg-glass: rgba(22, 22, 23, 0.72);
  --bg-glass-strong: rgba(22, 22, 23, 0.85);
  --label-primary: #ffffff;
  --label-secondary: #8e8e93;
  --label-tertiary: #48484a;
  --label-quaternary: #3a3a3c;
  --separator: rgba(84, 84, 88, 0.6);
  --separator-opaque: #38383a;
  --shadow-card: 0 4px 28px rgba(0, 0, 0, 0.4);
  --shadow-card-hover: 0 12px 40px rgba(0, 0, 0, 0.5);
  --shadow-nav: 0 1px 0 rgba(255, 255, 255, 0.06);
}

*,
*::before,
*::after {
  box-sizing: border-box;
}

html,
body,
#app {
  min-width: 320px;
  min-height: 100%;
  margin: 0;
}

html {
  scroll-behavior: smooth;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  min-height: 100vh;
  overflow-x: hidden;
  background-color: var(--bg-primary);
  font-family: inherit;
  line-height: 1.5;
  transition: background-color 0.3s ease, color 0.3s ease;
}

.basic-layout {
  min-height: 100vh;
  background: var(--bg-primary);
}

.layout-content {
  flex: 1;
  min-height: 0;
  padding: 0;
  border: 0;
  box-shadow: none;
  background: var(--bg-primary);
}

.content-inner {
  width: 100%;
  min-height: 100%;
  margin: 0;
  padding: 0;
}

button,
input,
textarea,
select {
  font: inherit;
}

::selection {
  color: #ffffff;
  background: var(--ios-blue);
}

@media (max-width: 768px) {
  .layout-content {
    padding: 0;
  }

  .content-inner { padding: 0; }
}
</style>
