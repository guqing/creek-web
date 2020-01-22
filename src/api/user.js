import { axios } from '@/utils/request'

const userAPI = {}

userAPI.getInfo = function() {
  return axios({
    url: '/user/info',
    method: 'get'
  })
}

userAPI.updatePassword = function(parameter) {
  return axios({
    url: '/user/updatePassword',
    method: 'post',
    data: parameter,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

userAPI.hasOldPassword = function() {
  return axios({
    url: '/user/hasInitPassword',
    method: 'get'
  })
}

export default userAPI
