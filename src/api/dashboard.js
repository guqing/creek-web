import { axios } from '@/utils/request'

const dashboardApi = {}

dashboardApi.countRam = function() {
  return axios({
    url: '/dashboard/count-ram',
    method: 'get'
  })
}

dashboardApi.listLogByPage = function(parameter) {
  return axios({
    url: '/dashboard/log/list',
    method: 'get',
    params: parameter
  })
}

export default dashboardApi
