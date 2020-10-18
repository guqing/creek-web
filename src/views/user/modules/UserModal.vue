<template>
  <a-modal title="操作" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form" :label-col="labelCol" :wrapper-col="wrapperCol">
        <a-form-item label="ID" v-show="false">
          <a-input v-decorator="['id']" />
        </a-form-item>
        <a-form-item label="用户名" has-feedback>
          <a-input
            v-decorator="[
              'username',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入用户名',
                    trigger: 'blur',
                  },
                  {
                    validator: validateUsername,
                    trigger: 'blur',
                  },
                ],
              },
            ]"
          />
        </a-form-item>
        <a-form-item label="密码" has-feedback>
          <a-input
            v-decorator="[
              'password',
              {
                rules: [
                  { required: rules.passwordRequired, message: '请输入密码', whitespace: true },
                  { min: 3, message: '字符长度必须大于3' },
                ],
              },
            ]"
          />
        </a-form-item>
        <a-form-item label="邮箱" has-feedback>
          <a-input
            v-decorator="[
              'email',
              {
                rules: [
                  {
                    type: 'email',
                    message: '邮箱地址格式不正确',
                  },
                  {
                    validator: validateEmail,
                    trigger: 'blur',
                  },
                ],
              },
            ]"
          />
        </a-form-item>

        <a-form-item label="角色">
          <a-select
            v-decorator="['roleIds', { rules: [{ required: true, validator: rules.roles }] }]"
            mode="multiple"
            style="width: 100%;"
            placeholder="选择角色"
          >
            <a-select-option :value="role.id" label="roleName" v-for="(role, index) in roles" :key="index">
              {{ role.roleName }}
            </a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
import roleApi from '@/api/role'
import userApi from '@/api/user'
import pick from 'lodash.pick'

const validateRoles = (rule, value, callback) => {
  if (value.length < 1) {
    callback(new Error('请选择角色'))
  } else {
    callback()
  }
}

export default {
  name: 'UserModal',
  data () {
    return {
      rules: {
        passwordRequired: true,
        roles: validateRoles
      },
      roles: [],
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 }
      },
      visible: false,
      editParam: {},
      confirmLoading: false
    }
  },
  beforeCreate () {
    this.form = this.$form.createForm(this)
    this.$log.debug('form::', this.form)
  },
  created () {
    this.handleRoleList()
  },
  methods: {
    handleRoleList () {
      roleApi.options().then(res => {
        this.roles = res.data
      })
    },
    add () {
      this.visible = true
    },
    edit (record) {
      this.visible = true
      // 编辑时不校验密码
      this.rules.passwordRequired = false
      this.editParam = Object.assign({}, record)
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.editParam, 'id', 'username', 'password', 'email'))
        this.form.setFieldsValue({ 'roleIds': this.editParam.roleIds.map(Number) || [] })
      })
    },
    close () {
      this.$emit('close')
      this.visible = false
    },
    handleOk () {
      const that = this
      // 触发表单验证
      this.form.validateFields((err, values) => {
        if (err) {
          return
        }
        // 验证表单没错误
        that.$log.debug('form values', values)

        that.confirmLoading = true
        if (values.id) {
          that.handleUpdateUser(values)
        } else {
          that.handleCreateUser(values)
        }
      })
    },
    handleUpdateUser (values) {
      userApi.update(values).then(res => {
        this.$message.success('更新成功')
        this.$emit('ok')
      }).finally(() => {
        setTimeout(() => {
          this.confirmLoading = false
        }, 1500)
        this.close()
      })
    },
    handleCreateUser (values) {
      userApi.create(values).then(res => {
        this.$message.success('保存成功')
        this.$emit('ok')
      }).finally(() => {
        setTimeout(() => {
          this.confirmLoading = false
        }, 1500)
        this.close()
      })
    },
    handleCancel () {
      this.close()
    },
    validateEmail (rule, value, callback) {
      // 没有做任何修改
      if (this.editParam.email === value) {
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
    },
    validateUsername (rule, value, callback) {
      if (!/^[A-Za-z0-9]+$/.test(value)) {
        callback(new Error('只能输入字母或数字'))
      } else if (this.editParam.username === value) {
        callback()
      } else {
        userApi.checkUsername(value).then(res => {
          if (res.data) {
            callback(new Error('用户名已经被使用'))
          } else {
            callback()
          }
        })
      }
    }
  }
}
</script>
