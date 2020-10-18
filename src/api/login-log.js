import { axios } from '@/utils/request'

const loginLogApi = {}

loginLogApi.list = function (parameter) {
  return axios({
    url: '/log/login',
    method: 'get',
    params: parameter
  })
}
export default loginLogApi
