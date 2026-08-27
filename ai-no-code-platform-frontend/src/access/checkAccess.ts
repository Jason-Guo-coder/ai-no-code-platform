import ACCESS_ENUM from '@/access/accessEnum.ts'

/** 判断当前登录用户是否具有页面所需权限。 */
function checkAccess(loginUser: API.LoginUserVO, needAccess: string = ACCESS_ENUM.NOT_LOGIN) {
  const loginUserAccess = loginUser.userRole ?? ACCESS_ENUM.NOT_LOGIN

  if (needAccess === ACCESS_ENUM.NOT_LOGIN) {
    return true
  }
  if (needAccess === ACCESS_ENUM.USER && loginUserAccess === ACCESS_ENUM.NOT_LOGIN) {
    return false
  }
  if (needAccess === ACCESS_ENUM.ADMIN && loginUserAccess !== ACCESS_ENUM.ADMIN) {
    return false
  }
  return true
}

export default checkAccess
