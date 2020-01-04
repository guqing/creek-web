<template>
  <div class="account-settings-info-view">
    <a-row :gutter="16">
      <a-col :md="24" :lg="16">
        <a-form layout="vertical" :form="form">
          <a-form-item label="原始密码">
            <a-input-password
              v-decorator="[
                'oldPassword',
                {
                  rules: [{ required: true, message: '请输入原始密码', whitespace: false }]
                }
              ]"
            />
          </a-form-item>
          <a-form-item label="新密码">
            <a-input-password
              @blur="handleConfirmBlur"
              v-decorator="[
                'newPassword',
                {
                  rules: [
                    {
                      required: true,
                      message: '请输入新密码',
                      whitespace: false
                    },
                    {
                      validator: validateToNextPassword
                    }
                  ]
                }
              ]"
            />
          </a-form-item>

          <a-form-item label="确认密码">
            <a-input-password
              v-decorator="[
                'confirmPassword',
                {
                  rules: [
                    {
                      required: true,
                      message: '再次输入新密码'
                    },
                    {
                      validator: compareToFirstPassword
                    }
                  ]
                }
              ]"
            />
          </a-form-item>

          <a-form-item>
            <a-button type="primary" @click="handleSubmit">确认更改</a-button>
          </a-form-item>
        </a-form>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import userApi from '@/api/user'

export default {
  data () {
    return {
      confirmDirty: false,
      userId: this.$store.state.user.info.id,
      confirmPassword: '',
      validateMessage: {}
    }
  },
  beforeCreate () {
    this.form = this.$form.createForm(this, { name: 'updatePassword' })
  },
  methods: {
    handleSubmit (e) {
      this.form.validateFieldsAndScroll((err, values) => {
        const user = {
          id: this.userId,
          oldPassword: values.oldPassword,
          newPassword: values.newPassword
        }

        if (!err) {
          this.handleUpdatePassword(user)
        }
      })
    },
    handleConfirmBlur (e) {
      const value = e.target.value
      this.confirmDirty = this.confirmDirty || !!value
    },
    compareToFirstPassword (rule, value, validateCallback) {
      const form = this.form
      if (value && value !== form.getFieldValue('newPassword')) {
        validateCallback('两次密码不一致，请重新输入')
      } else {
        validateCallback()
      }
    },
    validateToNextPassword (rule, value, validateCallback) {
      const form = this.form
      if (value && this.confirmDirty) {
        form.validateFields(['confirmPassword'], { force: true })
        validateCallback()
      } else if (value.length < 6 || value.length > 16) {
        validateCallback('密码长度必须在6-16字符之间')
      }
    },
    handleUpdatePassword (formValues) {
      userApi
        .updatePassword(formValues)
        .then(res => {
          if (res.code === 402) {
            this.$message.warning('原始密码输入有误，请重新输入')
          } else if (res.code === 0) {
            // 重置form表单
            this.form.resetFields()
            this.$message.success('修改成功')
          }
        })
        .catch(err => {
          this.$message.error(`修改密码出错，error:${err}`)
        })
    }
  }
}
</script>

<style lang="less" scoped></style>
