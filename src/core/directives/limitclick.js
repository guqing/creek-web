import Vue from 'vue'
import { message } from 'ant-design-vue'
/**
 * 使用方式 v-limitclick="目标方法名"
 * 被包裹的目标方法将会被限定速率执行
 */
const limitclick = Vue.directive('limitclick', {
  inserted: function (el, binding) {
    let openDelay = false
    el.onclick = function (e) {
      if (openDelay) {
        message.warning('你点的太快了，手速慢一点')
        return
      }

      openDelay = !openDelay

      const time = binding.arg
      const func = binding.value
      if (!func) {
        throw new Error('请传入执行执行方法,例如：handleResetForm')
      }
      if (time && typeof time !== 'number') {
        throw new TypeError('延迟时间错误,不是数字类型')
      }
      setTimeout(() => {
        openDelay = !openDelay
      }, time || 1000)
      func.apply()
    }
  }
})

export default limitclick
