<script setup lang="ts">
import { computed, onMounted, onUnmounted, ref } from 'vue'
import { message, type MenuProps } from 'ant-design-vue'
import { LogoutOutlined } from '@ant-design/icons-vue'
import { useRoute, useRouter, type RouteRecordRaw } from 'vue-router'
import { useLoginUserStore } from '@/stores/loginUser.ts'
import { userLogout } from '@/api/userController.ts'
import checkAccess from '@/access/checkAccess.ts'

const props = defineProps<{
  routes: RouteRecordRaw[]
}>()

const router = useRouter()
const route = useRoute()
const loginUserStore = useLoginUserStore()
const selectedKeys = ref<string[]>([route.path])
const scrolled = ref(false)
const darkMode = ref(false)

function updateScrollState() {
  scrolled.value = window.scrollY > 10
}

onMounted(() => {
  updateScrollState()
  window.addEventListener('scroll', updateScrollState)
})

onUnmounted(() => {
  window.removeEventListener('scroll', updateScrollState)
})

function toggleTheme() {
  darkMode.value = !darkMode.value
  document.documentElement.setAttribute('data-theme', darkMode.value ? 'dark' : 'light')
}

const items = computed<MenuProps['items']>(() => {
  return props.routes
    .filter((routeRecord) => {
      return (
        routeRecord.name &&
        !routeRecord.meta?.hideInMenu &&
        checkAccess(loginUserStore.loginUser, routeRecord.meta?.access as string)
      )
    })
    .map((routeRecord) => ({
      key: routeRecord.path,
      label: String(routeRecord.name),
    }))
})

// 路由变化时同步菜单高亮，覆盖点击、刷新和浏览器前进后退场景。
router.afterEach((to) => {
  selectedKeys.value = [to.path]
})

/** 根据菜单路径跳转到对应页面。 */
const handleMenuClick: MenuProps['onClick'] = ({ key }) => {
  const path = String(key)
  if (path.startsWith('/')) {
    router.push(path)
  }
}

/** 退出登录并清除全局用户状态。 */
async function doLogout() {
  try {
    const res = await userLogout()
    if (res.data.code === 0) {
      loginUserStore.setLoginUser({
        userName: '未登录',
        userRole: 'notLogin',
      })
      message.success('退出登录成功')
      await router.push('/user/login')
    } else {
      message.error('退出登录失败，' + res.data.message)
    }
  } catch {
    message.error('退出登录请求失败')
  }
}
</script>

<template>
  <nav class="global-header" :class="{ scrolled }">
    <div class="header-inner">
      <RouterLink class="brand" to="/" aria-label="AI零代码应用生成平台首页">
        <img class="brand-logo" src="@/assets/logo.png" alt="AI零代码应用生成平台 logo" />
        <span class="brand-title">AI零代码应用生成平台</span>
      </RouterLink>

      <a-menu
        class="navigation"
        mode="horizontal"
        v-model:selectedKeys="selectedKeys"
        :items="items"
        @click="handleMenuClick"
      />

      <div class="account-area">
        <button class="theme-toggle" type="button" aria-label="切换主题" @click="toggleTheme">
          <svg
            v-if="darkMode"
            width="18"
            height="18"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
            aria-hidden="true"
          >
            <circle cx="12" cy="12" r="4"></circle>
            <path d="M12 2v2M12 20v2M4.93 4.93l1.41 1.41M17.66 17.66l1.41 1.41M2 12h2M20 12h2M6.34 17.66l-1.41 1.41M19.07 4.93l-1.41 1.41"></path>
          </svg>
          <svg
            v-else
            width="18"
            height="18"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
            aria-hidden="true"
          >
            <path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"></path>
          </svg>
        </button>
        <a-dropdown v-if="loginUserStore.loginUser.id">
          <button class="user-status" type="button">
            <a-avatar :src="loginUserStore.loginUser.userAvatar">
              {{ loginUserStore.loginUser.userName?.slice(0, 1) }}
            </a-avatar>
            <span>{{ loginUserStore.loginUser.userName ?? '无名' }}</span>
          </button>
          <template #overlay>
            <a-menu>
              <a-menu-item @click="doLogout">
                <LogoutOutlined />
                退出登录
              </a-menu-item>
            </a-menu>
          </template>
        </a-dropdown>
        <a-button v-else type="primary" class="login-button" @click="router.push('/user/login')">
          登录
        </a-button>
      </div>
    </div>
  </nav>
</template>

<style scoped>
.global-header {
  position: fixed;
  top: 0;
  right: 0;
  left: 0;
  z-index: 1000;
  height: 52px;
  padding: 0;
  line-height: normal;
  transition: all 0.3s cubic-bezier(0.25, 0.1, 0.25, 1);
}

.global-header.scrolled {
  background: var(--bg-glass);
  border-bottom: 1px solid var(--separator);
  box-shadow: var(--shadow-nav);
  -webkit-backdrop-filter: saturate(180%) blur(20px);
  backdrop-filter: saturate(180%) blur(20px);
}

.header-inner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  max-width: 1200px;
  padding: 0 24px;
  height: 100%;
  margin: 0 auto;
}

.brand {
  display: inline-flex;
  flex: 0 0 auto;
  align-items: center;
  gap: 8px;
  color: var(--ios-text);
  text-decoration: none;
}

.brand-logo {
  display: block;
  width: 24px;
  height: 24px;
  border-radius: 7px;
  object-fit: contain;
}

.brand-title {
  font-size: 18px;
  font-weight: 600;
  font-family: var(--font-stack);
  white-space: nowrap;
}

.navigation {
  flex: 0 0 auto;
  min-width: 0;
  margin: 0;
  border-bottom: 0;
  background: transparent;
}

.account-area {
  display: flex;
  flex: 0 0 auto;
  align-items: center;
  gap: 12px;
  margin-left: 24px;
}

.theme-toggle {
  display: inline-flex;
  width: 36px;
  height: 36px;
  align-items: center;
  justify-content: center;
  padding: 0;
  color: var(--label-primary);
  font-size: 17px;
  cursor: pointer;
  border: 0;
  border-radius: 50%;
  background: var(--bg-secondary);
  transition: background-color 0.2s ease;
}

.theme-toggle:hover { background: var(--label-quaternary); }

.user-status {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 4px;
  color: var(--ios-text);
  font-size: 14px;
  font-family: inherit;
  white-space: nowrap;
  cursor: pointer;
  background: transparent;
  border: 0;
}

.login-button {
  min-width: 0;
  height: auto;
  padding: 7px 16px;
  color: #ffffff;
  font-size: 13px;
  font-weight: 500;
  background: var(--ios-blue);
  border: 0;
  border-radius: 980px;
  box-shadow: none;
}

.login-button:hover { background: var(--ios-blue-dark); }

:deep(.ant-menu-horizontal) {
  height: 52px;
  line-height: 52px;
}

:deep(.ant-menu-overflow) {
  gap: 28px;
}

:deep(.ant-menu-horizontal > .ant-menu-item),
:deep(.ant-menu-horizontal > .ant-menu-submenu) {
  top: 0;
  height: 52px;
  margin: 0;
  padding-inline: 0;
  color: var(--ios-text);
  line-height: 52px;
  font-size: 14px;
  font-weight: 400;
  font-family: var(--font-stack);
  opacity: 0.85;
  border-bottom: 0;
}

:deep(.ant-menu-horizontal > .ant-menu-item:hover),
:deep(.ant-menu-horizontal > .ant-menu-submenu:hover) {
  color: var(--ios-text);
  opacity: 1;
  background: transparent;
}

:deep(.ant-menu-horizontal > .ant-menu-item-selected) {
  color: var(--ios-text);
  font-weight: 600;
  opacity: 1;
  background: transparent;
  border-bottom-color: transparent;
}

:deep(.ant-menu-horizontal > .ant-menu-item::after),
:deep(.ant-menu-horizontal > .ant-menu-submenu::after) {
  display: none;
}

:deep(.ant-avatar) {
  background: var(--bg-secondary);
  color: var(--label-primary);
}

@media (max-width: 768px) {
  .global-header {
    height: 52px;
    padding: 0;
  }

  .header-inner {
    padding: 0 16px;
  }

  .brand-title {
    display: none;
  }

  .navigation {
    display: none;
  }

  .navigation :deep(.ant-menu-overflow) {
    justify-content: flex-start;
  }

  .account-area { margin-left: 12px; }
  :deep(.ant-menu-horizontal > .ant-menu-item) { margin: 0 10px; }
}
</style>
