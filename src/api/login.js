import { axios } from '@/utils/request'

/**
 * login func
 * parameter: {
 *     username: '',
 *     password: '',
 *     remember_me: true,
 *     captcha: '12345'
 * }
 * @param parameter
 * @returns {*}
 */
export function login (parameter) {
  return axios({
    url: '/authorize/token',
    method: 'post',
    data: parameter
  })
}

export function bindSocial (parameter) {
  return axios({
    url: '/social/bind',
    method: 'post',
    data: parameter
  })
}

export function unbindSocial (parameter) {
  return axios({
    url: `/social/unbind/${parameter}`,
    method: 'post'
  })
}

export function socailSignLogin (parameter) {
  return axios({
    url: '/social/sign/login',
    method: 'post',
    data: parameter
  })
}

export function listSupportSocail () {
  return axios({
    url: '/social/list',
    method: 'get'
  })
}

export function listUserConnectedSocail () {
  return axios({
    url: '/social/providers',
    method: 'get'
  })
}

export var socialLoginApi = axios.defaults.baseURL + `/social/login`

export function getInfo () {
  return axios({
    url: '/authorize/user',
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
