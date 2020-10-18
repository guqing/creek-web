import { constantRouterMap } from '@/config/router.config'
import { generatorDynamicRouter } from '@/router/generator-routers'
import { ROUTER_MAP } from '@/store/mutation-types'
import storage from 'store'
import menuApi from '@/api/menu'

const permission = {
  state: {
    routers: constantRouterMap,
    addRouters: []
  },
  mutations: {
    SET_ROUTERS: (state, routers) => {
      state.addRouters = routers
      state.routers = constantRouterMap.concat(routers)
    }
  },
  actions: {
    GenerateRoutes ({ commit }) {
      return new Promise((resolve, reject) => {
        var routerMap = storage.get(ROUTER_MAP)
        if (routerMap) {
          getDnamicRouter(routerMap, commit)
          resolve()
        } else {
          menuApi.listRouterMap().then(res => {
            var result = res.data
            // 设置到localStorage中
            storage.set(ROUTER_MAP, result)
            getDnamicRouter(result, commit)
            resolve()
          }).catch(err => {
            reject(err)
          })
        }
      })
    }
  }
}

function getDnamicRouter (data, commit) {
  generatorDynamicRouter(data).then(res => {
    commit('SET_ROUTERS', res)
    commit('SET_ROUTERMAP', res)
  })
}
export default permission
