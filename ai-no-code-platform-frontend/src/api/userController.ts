import request from '@/request'

/** 用户注册 POST /user/register */
export async function userRegister(body: API.UserRegisterRequest) {
  return request<API.BaseResponseLong>('/user/register', {
    method: 'POST',
    data: body,
  })
}

/** 用户登录 POST /user/login */
export async function userLogin(body: API.UserLoginRequest) {
  return request<API.BaseResponseLoginUserVO>('/user/login', {
    method: 'POST',
    data: body,
  })
}

/** 获取当前登录用户 GET /user/get/login */
export async function getLoginUser() {
  return request<API.BaseResponseLoginUserVO>('/user/get/login', {
    method: 'GET',
  })
}

/** 用户注销 POST /user/logout */
export async function userLogout() {
  return request<API.BaseResponseBoolean>('/user/logout', {
    method: 'POST',
  })
}

/** 分页获取用户列表 POST /user/list/page/vo */
export async function listUserVoByPage(body: API.UserQueryRequest) {
  return request<API.BaseResponsePageUserVO>('/user/list/page/vo', {
    method: 'POST',
    data: body,
  })
}

/** 删除用户 POST /user/delete */
export async function deleteUser(body: API.DeleteRequest) {
  return request<API.BaseResponseBoolean>('/user/delete', {
    method: 'POST',
    data: body,
  })
}
