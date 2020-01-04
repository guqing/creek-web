import Vue from 'vue'
import axios from 'axios'
import store from '@/store'
import { VueAxios } from './axios'
import notification from 'ant-design-vue/es/notification'
import { ACCESS_TOKEN } from '@/store/mutation-types'

// 创建 axios 实例
const service = axios.create({
  baseURL: 'http://localhost:8081', // api base_url
  timeout: 6000 // 请求超时时间
})

const err = error => {
  if (error.response) {
    const data = error.response.data
    const token = Vue.ls.get(ACCESS_TOKEN)
    if (error.response.status === 403) {
      notification.error({
        message: 'Forbidden',
        description: data.message
      })
    }
    if (
      error.response.status === 401 &&
      !(data.result && data.result.isLogin)
    ) {
      notification.error({
        message: 'Unauthorized',
        description: 'Authorization verification failed'
      })
      if (token) {
        store.dispatch('Logout').then(() => {
          setTimeout(() => {
            window.location.reload()
          }, 1500)
        })
      }
    }
  }
  return Promise.reject(error)
}

// request interceptor
service.interceptors.request.use(config => {
  const token = Vue.ls.get(ACCESS_TOKEN)
  if (token) {
    config.headers['Authorization'] = token // 让每个请求携带自定义 token 请根据实际情况自行修改
  }
  return config
}, err)

// response interceptor
service.interceptors.response.use(response => {
  // 在这里检查后端是否有携带token在response中，如果有设置给token
  const token = response.headers['authorization']
  if (token) {
    // 让每个请求携带自定义 token 请根据实际情况自行修改
    Vue.ls.set(ACCESS_TOKEN, token, 7 * 24 * 60 * 60 * 1000)
    store.commit('SET_TOKEN', token)
  }

  if (response.data.code === 506) {
    notification.error({
      message: '没有权限,请刷新页面后重试',
      description: response.data.message
    })
  }
  return response.data
}, err)

const installer = {
  vm: {},
  install(Vue) {
    Vue.use(VueAxios, service)
  }
}

export { installer as VueAxios, service as axios }
