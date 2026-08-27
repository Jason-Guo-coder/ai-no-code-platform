<script setup lang="ts">
import { healthCheck } from '@/api/healthController.ts'

healthCheck().then((res) => {
  console.log(res)
})
</script>

<template>
  <section class="home-page">
    <div class="hero-gradient-orb hero-gradient-orb-1" aria-hidden="true"></div>
    <div class="hero-gradient-orb hero-gradient-orb-2" aria-hidden="true"></div>
    <div class="container">
      <h1>AI零代码应用生成平台</h1>
      <p>欢迎使用平台。</p>
    </div>
  </section>
</template>

<style scoped>
.home-page {
  position: relative;
  isolation: isolate;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 560px;
  padding: 120px 0 80px;
  overflow: hidden;
  text-align: center;
  width: 100%;
  margin: 0;
  border: 0;
  border-radius: 0;
  box-shadow: none;
  background-color: transparent;
}

.container {
  position: relative;
  z-index: 1;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
}

.hero-gradient-orb {
  position: absolute;
  z-index: -1;
  pointer-events: none;
  border-radius: 50%;
  filter: blur(80px);
  opacity: 0.35;
}

.hero-gradient-orb-1 {
  top: -100px;
  left: -100px;
  width: 400px;
  height: 400px;
  background: var(--ios-blue);
}

.hero-gradient-orb-2 {
  right: -80px;
  bottom: -80px;
  width: 350px;
  height: 350px;
  background: var(--ios-purple);
}

h1 {
  position: relative;
  z-index: 1;
  max-width: none;
  margin: 0 0 16px;
  color: var(--ios-text);
  font-size: clamp(40px, 7vw, 80px);
  font-weight: 700;
  letter-spacing: -0.02em;
  line-height: 1.05;
}

p {
  position: relative;
  z-index: 1;
  margin: 0;
  color: var(--ios-secondary);
  font-size: clamp(20px, 2.5vw, 28px);
  font-weight: 500;
  line-height: 1.3;
}

@media (max-width: 768px) {
  .home-page {
    min-height: 480px;
    padding-top: 88px;
    padding-bottom: 60px;
  }
}
</style>
