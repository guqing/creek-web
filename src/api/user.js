import { axios } from '@/utils/request'

const userAPI = {}

userAPI.getInfo = function() {
  return axios({
    url: '/user/info',
    method: 'get'
  })
}

export default userAPI
