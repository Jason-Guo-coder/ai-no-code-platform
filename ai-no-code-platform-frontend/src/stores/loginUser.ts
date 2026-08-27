import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getLoginUser } from '@/api/userController.ts'

export const useLoginUserStore = defineStore('loginUser', () => {
  // 默认未登录
  const loginUser = ref<API.LoginUserVO>({
    userName: '未登录',
  })

  // 获取当前登录用户
  async function fetchLoginUser() {
    try {
      const res = await getLoginUser()
      if (res.data.code === 0 && res.data.data) {
        loginUser.value = res.data.data
      } else {
        loginUser.value = { userName: '未登录', userRole: 'notLogin' }
      }
    } catch {
      loginUser.value = { userName: '未登录', userRole: 'notLogin' }
    }
  }

  // 更新当前登录用户
  function setLoginUser(newLoginUser: API.LoginUserVO) {
    loginUser.value = newLoginUser
  }

  return { loginUser, fetchLoginUser, setLoginUser }
})
