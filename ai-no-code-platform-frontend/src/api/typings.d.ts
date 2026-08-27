declare namespace API {
  type BaseResponseString = {
    code?: number
    data?: string
    message?: string
  }

  type BaseResponseLong = {
    code?: number
    data?: string
    message?: string
  }

  type BaseResponseLoginUserVO = {
    code?: number
    data?: LoginUserVO
    message?: string
  }

  type BaseResponseBoolean = {
    code?: number
    data?: boolean
    message?: string
  }

  type BaseResponsePageUserVO = {
    code?: number
    data?: PageUserVO
    message?: string
  }

  type DeleteRequest = {
    id?: string
  }

  type LoginUserVO = {
    id?: string
    userAccount?: string
    userName?: string
    userAvatar?: string
    userProfile?: string
    userRole?: string
  }

  type UserLoginRequest = {
    userAccount?: string
    userPassword?: string
  }

  type UserRegisterRequest = {
    userAccount?: string
    userPassword?: string
    checkPassword?: string
  }

  type UserQueryRequest = {
    id?: string
    userAccount?: string
    userName?: string
    userProfile?: string
    userRole?: string
    pageNum?: number
    pageSize?: number
    sortField?: string
    sortOrder?: string
  }

  type UserVO = {
    id?: string
    userAccount?: string
    userName?: string
    userAvatar?: string
    userProfile?: string
    userRole?: string
    createTime?: string
  }

  type PageUserVO = {
    pageNumber?: number
    pageSize?: number
    totalPage?: number
    totalRow?: number
    records?: UserVO[]
  }
}
