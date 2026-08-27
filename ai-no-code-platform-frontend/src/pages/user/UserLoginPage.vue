<script setup lang="ts">
import { reactive, ref } from 'vue'
import { message } from 'ant-design-vue'
import { HomeFilled } from '@ant-design/icons-vue'
import { useRouter } from 'vue-router'
import { userLogin } from '@/api/userController.ts'
import { useLoginUserStore } from '@/stores/loginUser.ts'

const router = useRouter()
const loginUserStore = useLoginUserStore()
const submitting = ref(false)

const formState = reactive<API.UserLoginRequest>({
  userAccount: '',
  userPassword: '',
})

/** 提交登录表单。 */
async function handleSubmit(values: API.UserLoginRequest) {
  submitting.value = true
  try {
    const res = await userLogin(values)
    if (res.data.code === 0 && res.data.data) {
      await loginUserStore.fetchLoginUser()
      message.success('登录成功')
      router.replace('/')
    } else {
      message.error('登录失败，' + res.data.message)
    }
  } catch {
    message.error('登录请求失败')
  } finally {
    submitting.value = false
  }
}
</script>

<template>
  <main id="userLoginPage" class="login-hero">
    <RouterLink class="home-button" to="/" aria-label="返回主页" title="返回主页">
      <HomeFilled />
    </RouterLink>

    <section class="hero-content" aria-labelledby="login-title">
      <RouterLink class="hero-brand" to="/" aria-label="返回首页">
        <img src="@/assets/logo.png" alt="" />
        <span>AI零代码应用生成平台</span>
      </RouterLink>
      <p class="hero-eyebrow">用户登录</p>
      <h1 id="login-title" class="hero-title">用户登录</h1>
      <p class="hero-subtitle">不写一行代码，生成完整应用</p>

      <section class="login-form-area" aria-labelledby="form-title">
        <h2 id="form-title">登录</h2>
        <a-form :model="formState" autocomplete="off" layout="vertical" @finish="handleSubmit">
          <a-form-item
            label="账号"
            name="userAccount"
            :rules="[{ required: true, message: '请输入账号' }]"
          >
            <a-input
              v-model:value="formState.userAccount"
              size="large"
              autocomplete="username"
              placeholder="请输入账号"
            />
          </a-form-item>
          <a-form-item
            label="密码"
            name="userPassword"
            :rules="[
              { required: true, message: '请输入密码' },
              { min: 8, message: '密码不能小于 8 位' },
            ]"
          >
            <a-input-password
              v-model:value="formState.userPassword"
              size="large"
              autocomplete="current-password"
              placeholder="请输入密码"
            />
          </a-form-item>
          <a-button type="primary" html-type="submit" size="large" block :loading="submitting">
            登录
          </a-button>
        </a-form>
        <p class="register-tip">
          还没有账号？
          <RouterLink to="/user/register">立即注册</RouterLink>
        </p>
      </section>
    </section>
    <p class="hero-footer">AI零代码应用生成平台ByJasonGuo</p>
  </main>
</template>

<style scoped>
.login-hero {
  position: relative;
  min-height: 100vh;
  overflow: hidden;
  color: var(--label-primary);
  text-align: center;
  background: var(--bg-primary);
}

.hero-content {
  width: min(100%, 1200px);
  margin: 0 auto;
  padding: 120px 24px 80px;
}

.home-button {
  position: absolute;
  top: 20px;
  left: 24px;
  z-index: 2;
  display: inline-flex;
  width: 36px;
  height: 36px;
  align-items: center;
  justify-content: center;
  color: var(--label-primary);
  font-size: 16px;
  text-decoration: none;
  border-radius: 50%;
  background: var(--bg-secondary);
  transition: background-color 0.2s ease;
}

.home-button:hover { background: var(--label-quaternary); }

.hero-brand {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 32px;
  color: var(--label-primary);
  font-size: 18px;
  font-weight: 600;
  text-decoration: none;
}

.hero-brand img {
  width: 24px;
  height: 24px;
  border-radius: 7px;
  object-fit: contain;
}

.hero-eyebrow {
  display: inline-block;
  margin-bottom: 12px;
  color: var(--ios-blue);
  font-size: 17px;
  font-weight: 600;
}

.hero-title {
  margin: 0 0 16px;
  color: var(--label-primary);
  font-size: 80px;
  font-weight: 700;
  letter-spacing: -0.02em;
  line-height: 1.05;
}

.hero-subtitle {
  margin: 0 0 32px;
  color: var(--label-secondary);
  font-size: 28px;
  font-weight: 500;
  letter-spacing: -0.01em;
  line-height: 1.3;
}

.login-form-area {
  width: min(100%, 400px);
  margin: 0 auto;
  text-align: left;
}

.login-form-area h2 {
  margin: 0 0 20px;
  color: var(--label-primary);
  font-size: 24px;
  font-weight: 600;
  text-align: center;
}

.register-tip {
  margin: 20px 0 0;
  color: var(--label-secondary);
  font-size: 14px;
  text-align: center;
}

.register-tip a {
  color: var(--ios-blue);
  text-decoration: none;
}

:deep(.ant-form-item-label > label) {
  color: var(--label-secondary);
  font-size: 14px;
}

:deep(.ant-input),
:deep(.ant-input-affix-wrapper) {
  border-color: #86868b;
  border-radius: var(--radius-sm);
  background: var(--bg-primary);
}

:deep(.ant-input:focus),
:deep(.ant-input-affix-wrapper-focused) {
  border-color: var(--ios-blue);
  box-shadow: 0 0 0 3px rgba(0, 122, 255, 0.12);
}

:deep(.ant-btn) {
  height: 48px;
  border: 0;
  border-radius: 980px;
  background: var(--ios-blue);
  box-shadow: none;
}

:deep(.ant-btn:hover) { background: var(--ios-blue-dark); }

.hero-footer {
  position: absolute;
  right: 24px;
  bottom: 24px;
  left: 24px;
  margin: 0;
  color: var(--label-tertiary);
  font-size: 12px;
}

@media (max-width: 768px) {
  .hero-content {
    padding-top: 88px;
    padding-bottom: 60px;
  }

  .hero-title { font-size: 48px; }
  .hero-subtitle { font-size: 20px; }
  .hero-brand { margin-bottom: 24px; }
}
</style>
