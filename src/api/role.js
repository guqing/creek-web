import { axios } from '@/utils/request'

const roleApi = {}

roleApi.list = function(parameter) {
  return axios({
    url: '/role/list',
    method: 'get',
    params: parameter
  })
}

export default roleApi
