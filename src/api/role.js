import { axios } from '@/utils/request'

const roleApi = {}

roleApi.list = function(parameter) {
  return axios({
    url: '/role/list',
    method: 'get',
    params: parameter
  })
}

roleApi.count = function() {
  return axios({
    url: '/role/count',
    method: 'get'
  })
}

export default roleApi
