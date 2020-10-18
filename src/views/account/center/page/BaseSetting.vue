<template>
  <div class="account-settings-info-view">
    <a-row :gutter="16">
      <a-col :md="24" :lg="16">
        <a-form layout="vertical" :form="form">
          <a-form-item label="昵称">
            <a-input
              v-decorator="['nickname', { rules: [{ required: true, message: '昵称不能为空!' }] }]"
              placeholder="给自己起个名字"
            />
          </a-form-item>
          <a-form-item label="Bio">
            <a-textarea
              v-decorator="['description', { rules: [{ max: 144, message: '个人说明不能超过144字符!' }] }]"
              rows="4"
              placeholder="You are not alone."
            />
          </a-form-item>

          <a-form-item label="电子邮件">
            <a-input
              v-decorator="[
                'email',
                {
                  rules: [
                    { type: 'email', message: '邮箱地址格式不正确!' },
                    {
                      validator: validateDuplicateEmail,
                    },
                  ],
                },
              ]"
              placeholder="exp@admin.com"
            />
          </a-form-item>

          <a-form-item label="手机号码">
            <a-input v-decorator="['mobile']" placeholder="填写一个手机号" />
          </a-form-item>

          <a-form-item>
            <a-button type="primary" @click="handleUpdateUserInfo"> 保存 </a-button>
          </a-form-item>
        </a-form>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import userApi from '@/api/user'
import { mapGetters } from 'vuex'
import pick from 'lodash.pick'
import antiShake from '@/utils/antiShake'

export default {
  name: 'BaseSetting',
  beforeCreate () {
    this.form = this.$form.createForm(this, { name: 'user_profile' })
  },
  computed: {
    ...mapGetters(['userInfo']),
    currentUser () {
      return pick(this.userInfo, 'nickname', 'email', 'mobile', 'description')
    }
  },
  mounted () {
    this.initForm()
  },
  methods: {
    initForm () {
      this.form.setFieldsValue(this.currentUser)
    },
    validateDuplicateEmail (rule, value, callback) {
      if (this.currentUser.email === value) {
        callback()
      }
      antiShake(() => {
        // 防抖
        userApi.checkEmail(value).then(res => {
          if (res.data) {
            callback(new Error('邮箱地址已经存在'))
          } else {
            callback()
          }
        })
      }, 500)
    },
    handleUpdateUserInfo (e) {
      e.preventDefault()
      this.form.validateFields((err, values) => {
        if (!err) {
          userApi.updateProfile(values).then(res => {
            this.$message.success('保存成功')
            // 更新vuex状态
            this.$store.dispatch('GetInfo')
          }).catch(err => {
            this.$message.error(`更新用户信息出错,error: ${err.message}`)
          })
        }
      })
    }
  }
}
</script>
