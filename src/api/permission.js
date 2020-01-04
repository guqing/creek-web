import { axios } from '@/utils/request'

const permissionApi = {}

permissionApi.listAll = function() {
  return axios({
    url: '/permission/listAll',
    method: 'get'
  })
}

permissionApi.getByRoleId = function(parameter) {
  return axios({
    url: '/permission/getByRoleId',
    method: 'get',
    params: parameter
  })
}

export default permissionApi
