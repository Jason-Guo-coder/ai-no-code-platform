import router from '@/router'
import { useLoginUserStore } from '@/stores/loginUser.ts'
import ACCESS_ENUM from '@/access/accessEnum.ts'
import checkAccess from '@/access/checkAccess.ts'

router.beforeEach(async (to) => {
  const loginUserStore = useLoginUserStore()
  let loginUser = loginUserStore.loginUser

  // 首次进入页面时获取一次登录用户信息。
  if (!loginUser.userRole) {
    await loginUserStore.fetchLoginUser()
    loginUser = loginUserStore.loginUser
  }

  const needAccess = (to.meta.access as string) ?? ACCESS_ENUM.NOT_LOGIN
  if (needAccess === ACCESS_ENUM.NOT_LOGIN) {
    return true
  }

  if (loginUser.userRole === ACCESS_ENUM.NOT_LOGIN) {
    return {
      path: '/user/login',
      query: { redirect: to.fullPath },
    }
  }

  if (!checkAccess(loginUser, needAccess)) {
    return '/noAuth'
  }

  return true
})
