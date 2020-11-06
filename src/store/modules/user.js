import storage from 'store'
import { login, socailSignLogin, getInfo } from '@/api/login'
import { ACCESS_TOKEN } from '@/store/mutation-types'
import { welcome } from '@/utils/util'

const user = {
  state: {
    token: '',
    name: '',
    welcome: '',
    avatar: '',
    roles: [],
    info: {},
    routerMap: []
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_NAME: (state, { name, welcome }) => {
      state.name = name
      state.welcome = welcome
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_INFO: (state, info) => {
      state.info = info
    },
    SET_ROUTERMAP: (state, routerMap) => {
      state.routerMap = routerMap
    }
  },

  actions: {
    // 登录
    Login ({ commit }, userInfo) {
      return new Promise((resolve, reject) => {
        login(userInfo)
          .then(response => {
            var token = getToken(response.data)
            console.log(response.data)
            storage.set(ACCESS_TOKEN, token, 7 * 24 * 60 * 60 * 1000)
            commit('SET_TOKEN', token)
            resolve()
          })
          .catch(error => {
            reject(error)
          })
      })
    },
    SocialLogin ({ commit }, data) {
      return new Promise((resolve, reject) => {
        console.log('social login:', data)
        var tokenInfo = data || ''
        if (tokenInfo === '') {
          console.log('socail login is null')
          reject(tokenInfo)
        }

        var token = getToken(tokenInfo)

        // 设置到localStorage中
        storage.set(ACCESS_TOKEN, token, 7 * 24 * 60 * 60 * 1000)
        console.log('vuex token:', token)
        // 设置到vuex中
        commit('SET_TOKEN', token)
        console.log('vuex get after set：', storage.get(ACCESS_TOKEN))
        resolve(tokenInfo)
      })
    },
    SocialSignLogin ({ commit }, data) {
      return new Promise(resolve => {
        socailSignLogin(data).then(res => {
          console.log('social sign login:', res)
          var token = getToken(res.data)

          // 设置到localStorage中
          storage.set(ACCESS_TOKEN, token, 7 * 24 * 60 * 60 * 1000)
          console.log('社交注册绑定的登录成功:', res.data)
          // 设置到vuex中
          commit('SET_TOKEN', token)
          // 设置用户信息
          setUserInfo(res.data.userInfo, commit)
          resolve(token)
        })
      })
    },
    // 获取用户信息
    GetInfo ({ commit }) {
      return new Promise((resolve, reject) => {
        getInfo()
          .then(response => {
            console.log('get user info:', response)
            const result = response.data
            setUserInfo(result, commit)
            resolve(result)
          })
          .catch(error => {
            reject(error)
          })
      })
    },

    // 登出
    Logout ({ commit }) {
      return new Promise(resolve => {
        commit('SET_TOKEN', '')
        commit('SET_ROLES', [])
        storage.remove(ACCESS_TOKEN)
        resolve()
      })
    }
  }
}

function getToken (tokenInfo) {
  const current = new Date()
  const expireTime = current.setTime(current.getTime() + 1000 * tokenInfo.expires_in)

  return {
    access_token: tokenInfo.access_token,
    refresh_token: tokenInfo.refresh_token,
    expireTime: expireTime,
    token_type: tokenInfo.token_type
  }
}

function setUserInfo (result, commit) {
  // commit('SET_ROLES', [result.roleName])
  commit('SET_INFO', result)
  commit('SET_ROLES', result.roleIds)
  commit('SET_NAME', { name: result.nickname || result.username, welcome: welcome() })
  if (!result.avatar) {
    var avatar = '/avatar.png'
    commit('SET_AVATAR', avatar)
    result.avatar = avatar
  } else {
    commit('SET_AVATAR', result.avatar)
  }
}
export default user
