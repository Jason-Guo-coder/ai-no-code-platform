<script setup lang="ts">
import { reactive, ref } from 'vue'
import { message } from 'ant-design-vue'
import { useRouter } from 'vue-router'
import { userRegister } from '@/api/userController.ts'

const router = useRouter()
const submitting = ref(false)

const formState = reactive<API.UserRegisterRequest>({
  userAccount: '',
  userPassword: '',
  checkPassword: '',
})

/** 校验两次输入的密码是否一致。 */
function validateCheckPassword(
  _rule: unknown,
  value: string,
  callback: (error?: Error) => void,
) {
  if (value && value !== formState.userPassword) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

/** 提交注册表单，成功后跳转到登录页。 */
async function handleSubmit(values: API.UserRegisterRequest) {
  submitting.value = true
  try {
    const res = await userRegister(values)
    if (res.data.code === 0) {
      message.success('注册成功')
      router.replace('/user/login')
    } else {
      message.error('注册失败，' + res.data.message)
    }
  } catch {
    message.error('注册请求失败')
  } finally {
    submitting.value = false
  }
}
</script>

<template>
  <section id="userRegisterPage" class="auth-page">
    <div class="auth-panel">
      <div class="brand-mark">
        <img src="@/assets/logo.png" alt="AI零代码应用生成平台 logo" />
      </div>
      <h1 class="title">创建账号</h1>
      <p class="desc">注册后即可开始生成你的应用</p>

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
            autocomplete="new-password"
            placeholder="至少 8 位密码"
          />
        </a-form-item>

        <a-form-item
          label="确认密码"
          name="checkPassword"
          :rules="[
            { required: true, message: '请确认密码' },
            { min: 8, message: '密码不能小于 8 位' },
            { validator: validateCheckPassword },
          ]"
        >
          <a-input-password
            v-model:value="formState.checkPassword"
            size="large"
            autocomplete="new-password"
            placeholder="请再次输入密码"
          />
        </a-form-item>

        <div class="tips">
          <span>已有账号？</span>
          <RouterLink to="/user/login">返回登录</RouterLink>
        </div>

        <a-button type="primary" html-type="submit" size="large" block :loading="submitting">
          注册
        </a-button>
      </a-form>
    </div>
  </section>
</template>

<style scoped>
.auth-page {
  display: flex;
  min-height: 640px;
  align-items: center;
  justify-content: center;
  padding: 120px 0 80px;
  background: var(--bg-primary);
}

.auth-panel {
  width: min(100%, 400px);
  padding: 0;
  background: transparent;
}

.brand-mark {
  display: flex;
  justify-content: center;
  margin-bottom: 24px;
}

.brand-mark img {
  width: 24px;
  height: 24px;
  border-radius: 7px;
  object-fit: contain;
}

.title {
  margin: 0 0 8px;
  color: var(--ios-text);
  font-size: 52px;
  font-weight: 700;
  letter-spacing: -0.02em;
  text-align: center;
}

.desc {
  margin: 0 0 28px;
  color: var(--ios-secondary);
  font-size: 19px;
  line-height: 1.4;
  text-align: center;
}

.tips {
  display: flex;
  justify-content: flex-end;
  gap: 4px;
  margin: -4px 0 24px;
  color: var(--label-secondary);
  font-size: 14px;
}

.tips a {
  color: var(--ios-blue);
  font-weight: 600;
  text-decoration: none;
}

:deep(.ant-input),
:deep(.ant-input-affix-wrapper) {
  border-color: #86868b;
  border-radius: var(--radius-sm);
  background: var(--bg-primary);
}

:deep(.ant-btn) {
  height: 48px;
  border: 0;
  border-radius: 980px;
  background: var(--ios-blue);
  box-shadow: none;
}

:deep(.ant-btn:hover) { background: var(--ios-blue-dark); }

@media (max-width: 768px) {
  .auth-page {
    min-height: 560px;
    padding: 88px 16px 60px;
  }

  .auth-panel {
    width: min(100%, 400px);
  }

  .title { font-size: 40px; }
  .desc { font-size: 17px; }
}
</style>
