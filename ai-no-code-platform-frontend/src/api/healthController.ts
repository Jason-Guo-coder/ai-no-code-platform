import request from '@/request'
import type { AxiosRequestConfig } from 'axios'

/** 此处后端没有提供注释 GET /health/ */
export async function healthCheck(options?: AxiosRequestConfig) {
  return request<API.BaseResponseString>('/health/', {
    method: 'GET',
    ...(options || {}),
  })
}
