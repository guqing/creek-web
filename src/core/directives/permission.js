import Vue from 'vue'
import store from '@/store'

// 只要包含列出的任意一个权限，元素就会显示
export const hasAnyPermission = Vue.directive('hasAnyPermission', {
  inserted: function (el, binding, vnode) {
    const actionNames = binding.value
    const permissions = store.getters.userInfo.permissions
    const elVal = vnode.context.$route.meta.permission

    for (const actionName of actionNames) {
      var action = elVal + ':' + actionName
      if (permissions.includes(action)) {
        return
      } else {
        el.parentNode && el.parentNode.removeChild(el) || (el.style.display = 'none')
      }
    }
  }
})

// 当不包含列出的权限时，渲染该元素
export const hasNoPermission = Vue.directive('hasNoPermission', {
  inserted: function (el, binding, vnode) {
    const actionNames = binding.value
    const permissions = store.getters.userInfo.permissions
    const elVal = vnode.context.$route.meta.permission

    for (const actionName of actionNames) {
      var action = elVal + ':' + actionName
      if (permissions.includes(action)) {
        el.parentNode && el.parentNode.removeChild(el) || (el.style.display = 'none')
      }
    }
  }
})
