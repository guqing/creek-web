import axios from 'axios'
import store from '@/store'
import storage from 'store'
import notification from 'ant-design-vue/es/notification'
import { VueAxios } from './axios'
import { ACCESS_TOKEN } from '@/store/mutation-types'

// 创建 axios 实例
const request = axios.create({
  // API 请求的默认前缀
  baseURL: process.env.VUE_APP_API_BASE_URL,
  timeout: 6000 // 请求超时时间
})
// 异常拦截处理器
const errorHandler = error => {
  const res = error.response
  if (res) {
    const data = res.data
    // 从 localstorage 获取 token
    const token = storage.get(ACCESS_TOKEN)
    if (token.expireTime < Date.now()) {
      // token 过期换取token
      return store
          .dispatch('RefreshToken', token.refresh_token)
          .then(() => {
            console.log('token过期, 自动刷新token')
            // 重新请求
            return request(res.config)
          })
          .catch(() => {
            notification.error({
              message: 'Unauthorized',
              description: '登录已过期,请重新登录'
            })
            store.dispatch('Logout').then(() => {
              setTimeout(() => {
                window.location.reload()
              }, 1500)
            })
          })
    } else if (data.code === 'A0320') {
      notification.error({
        message: 'Forbidden',
        description: data.message
      })
    } else if (data.code === 'A0300') {
      notification.error({
        message: 'Unauthorized',
        description: '登录已过期,请重新登录'
      })

      if (token) {
        store.dispatch('Logout').then(() => {
          setTimeout(() => {
            window.location.reload()
          }, 1500)
        })
      }
    } else {
      notification.error({
        message: '请求失败',
        description: data.message
      })
    }
  }
  return Promise.reject(error)
}

// request interceptor
request.interceptors.request.use(config => {
  const token = storage.get(ACCESS_TOKEN)
  if (token) {
    // 其他请求带认证服务器的token
    config.headers['Authorization'] = 'bearer ' + token.access_token
  }

  return config
}, errorHandler)

// response interceptor
request.interceptors.response.use(response => {
  return response.data
}, errorHandler)

const installer = {
  vm: {},
  install (Vue) {
    Vue.use(VueAxios, request)
  }
}

export default request

export { installer as VueAxios, request as axios }
