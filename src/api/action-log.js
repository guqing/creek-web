import { axios } from '@/utils/request'

const actionLogApi = {}

actionLogApi.list = function (parameter) {
  return axios({
    url: '/log/action',
    method: 'get',
    params: parameter
  })
}
export default actionLogApi
