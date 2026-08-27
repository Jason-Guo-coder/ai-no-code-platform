<template>
  <section class="no-auth-page">
    <div class="status-icon" aria-hidden="true">401</div>
    <h1>没有访问权限</h1>
    <p>当前账号无法访问这个页面。</p>
    <a-button type="primary" href="/">返回主页</a-button>
  </section>
</template>

<style scoped>
.no-auth-page {
  display: flex;
  min-height: 560px;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px 0;
  text-align: center;
}

.status-icon {
  display: grid;
  width: 96px;
  height: 96px;
  margin-bottom: 24px;
  place-items: center;
  color: var(--ios-blue);
  font-size: 22px;
  font-weight: 700;
  letter-spacing: 0.04em;
  border-radius: var(--radius-lg);
  background: var(--bg-secondary);
}

h1 {
  margin: 0 0 8px;
  color: var(--ios-text);
  font-size: 40px;
  font-weight: 700;
  letter-spacing: -0.02em;
}

p {
  margin: 0 0 24px;
  color: var(--ios-secondary);
}

:deep(.ant-btn) {
  height: 48px;
  padding-inline: 28px;
  border: 0;
  border-radius: 980px;
  background: var(--ios-blue);
  box-shadow: none;
}

:deep(.ant-btn:hover) { background: var(--ios-blue-dark); }
</style>
