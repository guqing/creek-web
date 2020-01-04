<template>
  <a-modal
    title="操作"
    :width="800"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
  >
    <a-form
      :form="permissionForm"
    >
      <a-form-item
        label="标题"
        :labelCol="{lg: {span: 7}, sm: {span: 7}}"
        :wrapperCol="{lg: {span: 10}, sm: {span: 17} }"
      >
        <a-input
          v-decorator="[
            'name',
            {rules: [{ required: true, message: '请输入标题' }]}
          ]"
          name="name"
          placeholder="给目标起个名字"
        />
      </a-form-item>
      <a-form-item
        label="目标描述"
        :labelCol="{lg: {span: 7}, sm: {span: 7}}"
        :wrapperCol="{lg: {span: 10}, sm: {span: 17} }"
      >
        <a-textarea
          rows="4"
          placeholder="请输入你阶段性工作目标"
          v-decorator="[
            'description',
            {rules: [{ required: true, message: '请输入目标描述' }]}
          ]"
        />
      </a-form-item>
      <a-form-item
        label="客户"
        :labelCol="{lg: {span: 7}, sm: {span: 7}}"
        :wrapperCol="{lg: {span: 10}, sm: {span: 17} }"
      >
        <a-input
          placeholder="请描述你服务的客户，内部客户直接 @姓名／工号"
          v-decorator="[
            'customer',
            {rules: [{ required: true, message: '请描述你服务的客户' }]}
          ]"
        />
      </a-form-item>
      <a-form-item
        label="邀评人"
        :labelCol="{lg: {span: 7}, sm: {span: 7}}"
        :wrapperCol="{lg: {span: 10}, sm: {span: 17} }"
        :required="false"
      >
        <a-input placeholder="请直接 @姓名／工号，最多可邀请 5 人" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
import { getPermissions } from '@/api/manage'
import { actionToObject } from '@/utils/permissions'
import pick from 'lodash.pick'

export default {
  name: 'PermisionModal',
  data () {
    return {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 }
      },
      visible: false,
      confirmLoading: false,
      mdl: {},

      permissionForm: this.$form.createForm(this)
    }
  },
  methods: {
    add () {
      this.edit({ id: 0 })
    },
    edit (record) {
      this.mdl = Object.assign({}, record)
      this.visible = true

      // this.$nextTick(() => {
      //   this.permissionForm.setFieldsValue(pick(this.mdl, 'id', 'name', 'status', 'describe'))
      // })
      // console.log('this.mdl', this.mdl)
    },
    close () {
      this.$emit('close')
      this.visible = false
    },
    handleOk () {
      const _this = this
      // 触发表单验证
      this.permissionForm.validateFields((err, values) => {
        // 验证表单没错误
        if (!err) {
          console.log('form values', values)

          _this.confirmLoading = true
          // 模拟后端请求 2000 毫秒延迟
          new Promise((resolve) => {
            setTimeout(() => resolve(), 2000)
          }).then(() => {
            // Do something
            _this.$message.success('保存成功')
            _this.$emit('ok')
          }).catch(() => {
            // Do something
          }).finally(() => {
            _this.confirmLoading = false
            _this.close()
          })
        }
      })
    },
    handleCancel () {
      this.close()
    }

  }
}
</script>

<style scoped>
</style>
