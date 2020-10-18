<template>
  <div class="main user-layout-register">
    <h3>
      <span>{{ pageTitle }}</span>
    </h3>
    <a-form ref="formRegister" :form="form" id="formRegister">
      <a-form-item has-feedback>
        <a-input
          size="large"
          type="text"
          placeholder="邮箱"
          v-decorator="[
            'email',
            {
              rules: [
                { required: true, type: 'email', message: '请输入邮箱地址' },
                {
                  validator: validateEmail
                }
              ],
              validateTrigger: ['blur']
            }
          ]"
        ></a-input>
      </a-form-item>

      <a-popover
        placement="rightTop"
        :trigger="['focus']"
        :getPopupContainer="trigger => trigger.parentElement"
        v-model="state.passwordLevelChecked"
      >
        <template slot="content">
          <div :style="{ width: '240px' }">
            <div :class="['user-register', passwordLevelClass]">
              强度：<span>{{ passwordLevelName }}</span>
            </div>
            <a-progress :percent="state.percent" :showInfo="false" :strokeColor="passwordLevelColor" />
            <div style="margin-top: 10px;">
              <span>请至少输入 6 个字符。请不要使用容易被猜到的密码。</span>
            </div>
          </div>
        </template>
        <a-form-item>
          <a-input
            size="large"
            type="password"
            @click="handlePasswordInputClick"
            autocomplete="false"
            placeholder="至少6位密码，区分大小写"
            v-decorator="[
              'password',
              {
                rules: [
                  { required: true, message: '至少6位密码，区分大小写' },
                  { validator: this.handlePasswordLevel }
                ],
                validateTrigger: ['change', 'blur']
              }
            ]"
          ></a-input>
        </a-form-item>
      </a-popover>

      <a-form-item>
        <a-input
          size="large"
          type="password"
          autocomplete="false"
          placeholder="确认密码"
          v-decorator="[
            'password2',
            {
              rules: [{ required: true, message: '至少6位密码，区分大小写' }, { validator: this.handlePasswordCheck }],
              validateTrigger: ['change', 'blur']
            }
          ]"
        ></a-input>
      </a-form-item>

      <a-row :gutter="16">
        <a-col class="gutter-row" :span="16">
          <a-form-item>
            <a-input
              size="large"
              type="text"
              placeholder="验证码"
              v-decorator="[
                'captcha',
                { rules: [{ required: true, message: '请输入验证码' }], validateTrigger: 'blur' }
              ]"
            >
              <a-icon slot="prefix" type="mail" :style="{ color: 'rgba(0,0,0,.25)' }" />
            </a-input>
          </a-form-item>
        </a-col>
        <a-col class="gutter-row" :span="8">
          <a-button
            class="getCaptcha"
            size="large"
            :disabled="state.smsSendBtn"
            @click.stop.prevent="getCaptcha"
            v-text="(!state.smsSendBtn && '获取验证码') || state.time + ' s'"
          ></a-button>
        </a-col>
      </a-row>

      <a-form-item>
        <a-button
          size="large"
          type="primary"
          htmlType="submit"
          class="register-button"
          :loading="registerBtn"
          @click.stop.prevent="handleSubmit"
          :disabled="registerBtn"
        >
          {{ createBtnTitle }}
        </a-button>
        <router-link class="login" :to="{ name: 'login' }">
          使用已有账户登录
        </router-link>
      </a-form-item>
    </a-form>
  </div>
</template>

<script>
import { baseMixin } from '@/store/app-mixin'
import { timeFix } from '@/utils/util'
import userApi from '@/api/user'
import supportApi from '@/api/support'
import { mapActions } from 'vuex'
import { isEmpty } from 'lodash'

const validateEmail = (rule, value, callback) => {
  if (!value) {
    callback()
    return
  }

  userApi.checkEmail(value).then(res => {
    if (res.data) {
      callback(new Error('邮箱地址已经被使用'))
    } else {
      callback()
    }
  })
}

const levelNames = {
  0: '低',
  1: '低',
  2: '中',
  3: '强'
}
const levelClass = {
  0: 'error',
  1: 'error',
  2: 'warning',
  3: 'success'
}
const levelColor = {
  0: '#ff0000',
  1: '#ff0000',
  2: '#ff7e05',
  3: '#52c41a'
}
export default {
  name: 'Register',
  components: {
  },
  mixins: [baseMixin],
  data () {
    return {
      form: this.$form.createForm(this),

      state: {
        time: 60,
        smsSendBtn: false,
        passwordLevel: 0,
        passwordLevelChecked: false,
        percent: 10,
        progressColor: '#FF0000'
      },

      validateEmail: validateEmail,
      registerBtn: false,
      socialLoginAuthUser: {}
    }
  },
  computed: {
    passwordLevelClass () {
      return levelClass[this.state.passwordLevel]
    },
    passwordLevelName () {
      return levelNames[this.state.passwordLevel]
    },
    passwordLevelColor () {
      return levelColor[this.state.passwordLevel]
    },
    pageTitle () {
      if (!isEmpty(this.socialLoginAuthUser)) {
        return '第三方帐号未绑定，立即创建帐号完成绑定'
      }
      return '创建帐号'
    },
    createBtnTitle () {
      if (!isEmpty(this.socialLoginAuthUser)) {
        return '创建帐号'
      }
      return '注册'
    }
  },
  created () {
    this.socialLoginAuthUser = this.$route.params
    this.$log.debug(this.socialLoginAuthUser)
  },
  destroyed () {
    // this.socialLoginAuthUser = {}
    this.$log.debug('清空socialLoginAuthUser')
  },
  methods: {
    ...mapActions(['SocialSignLogin']),
    handlePasswordLevel (rule, value, callback) {
      let level = 0

      // 判断这个字符串中有没有数字
      if (/[0-9]/.test(value)) {
        level++
      }
      // 判断字符串中有没有字母
      if (/[a-zA-Z]/.test(value)) {
        level++
      }
      // 判断字符串中有没有特殊符号
      if (/[^0-9a-zA-Z_]/.test(value)) {
        level++
      }
      this.state.passwordLevel = level
      this.state.percent = level * 30
      if (level >= 2) {
        if (level >= 3) {
          this.state.percent = 100
        }
        callback()
      } else {
        if (level === 0) {
          this.state.percent = 10
        }
        callback(new Error('密码强度不够'))
      }
    },

    handlePasswordCheck (rule, value, callback) {
      const password = this.form.getFieldValue('password')
      console.log('value', value)
      if (value === undefined) {
        callback(new Error('请输入密码'))
      }
      if (value && password && value.trim() !== password.trim()) {
        callback(new Error('两次密码不一致'))
      }
      callback()
    },
    handlePasswordInputClick () {
      if (!this.isMobile) {
        this.state.passwordLevelChecked = true
        return
      }
      this.state.passwordLevelChecked = false
    },

    handleSubmit () {
      const { form: { validateFields }, state, $router } = this
      validateFields({ force: true }, (err, values) => {
        if (err) {
          return
        }
        state.passwordLevelChecked = false
        this.registerBtn = true
        if (this.socialLoginAuthUser) {
          const userParam = Object.assign({}, values)
          // 将authUser中的token置为null，否则其中后端authUser中的token没有空惨构造函数回导致绑定失败
          this.socialLoginAuthUser.token = null
          userParam.authUser = this.socialLoginAuthUser
          this.SocialSignLogin(userParam).then(res => {
            this.registerBtn = false
            this.loginSuccess(res)
          }).finally(() => {
            this.registerBtn = false
          })
        } else {
          $router.push({ name: 'registerResult', params: { ...values } })
          this.registerBtn = false
        }
      })
    },
    loginSuccess (res) {
      this.$router.push({ name: 'dashboard' })
      // 延迟 1 秒显示欢迎信息
      setTimeout(() => {
        this.$notification.success({
          message: '欢迎',
          description: `${timeFix()}，欢迎加入`
        })
      }, 1000)
    },
    getCaptcha (e) {
      e.preventDefault()
      const { form: { validateFields }, state, $message } = this

      validateFields(['email'], { force: true },
        (err, values) => {
          if (!err) {
            state.smsSendBtn = true

            const interval = window.setInterval(() => {
              if (state.time-- <= 0) {
                state.time = 60
                state.smsSendBtn = false
                window.clearInterval(interval)
              }
            }, 1000)

            $message.loading('验证码发送中..')

            supportApi.sendEmailCaptcha(values).then(res => {
              $message.success('验证码发送成功')
            })
          }
        }
      )
    },
    requestFailed (err) {
      this.$notification['error']({
        message: '错误',
        description: ((err.response || {}).data || {}).message || '请求出现错误，请稍后再试',
        duration: 4
      })
      this.registerBtn = false
    }
  },
  watch: {
    'state.passwordLevel' (val) {
      console.log(val)
    }
  }
}
</script>
<style lang="less">
.user-register {
  &.error {
    color: #ff0000;
  }

  &.warning {
    color: #ff7e05;
  }

  &.success {
    color: #52c41a;
  }
}

.user-layout-register {
  .ant-input-group-addon:first-child {
    background-color: #fff;
  }
}
</style>
<style lang="less" scoped>
.user-layout-register {
  & > h3 {
    font-size: 16px;
    margin-bottom: 20px;
  }

  .getCaptcha {
    display: block;
    width: 100%;
    height: 40px;
  }

  .register-button {
    width: 50%;
  }

  .login {
    float: right;
    line-height: 40px;
  }
}
</style>
