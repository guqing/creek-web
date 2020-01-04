<template>
  <a-modal
    title="操作"
    style="top: 20px;"
    :width="800"
    v-model="visible"
    @ok="handleOk"
  >
    <a-card :bordered="false" :style="{ height: '100%' }">
      <a-row :gutter="24">
        <a-col :md="4">
          <a-list itemLayout="vertical" :dataSource="roles">
            <a-list-item
              slot="renderItem"
              slot-scope="item, index"
              :key="index"
            >
              <a-list-item-meta :style="{ marginBottom: '0' }">
                <span
                  slot="description"
                  style="text-align: center; display: block"
                >
                  {{ item.description }}
                </span>
                <a
                  slot="title"
                  style="text-align: center; display: block"
                  @click="edit(item)"
                >
                  {{ item.name }}
                </a>
              </a-list-item-meta>
            </a-list-item>
          </a-list>
        </a-col>
        <a-col :md="20">
          <div style="max-width: 800px">
            <a-divider v-if="isMobile()" />
            <div v-if="mdl.id">
              <h3>角色：{{ mdl.name }}</h3>
            </div>
            <a-form
              :form="form"
              :layout="isMobile() ? 'vertical' : 'horizontal'"
            >
              <a-form-item label="唯一键">
                <a-input
                  v-decorator="['id', { rules: [] }]"
                  :disabled="true"
                  placeholder="请填写唯一键"
                />
              </a-form-item>

              <a-form-item label="角色名称">
                <a-input
                  v-decorator="[
                    'name',
                    {
                      rules: [
                        { required: true, message: 'Please input role name!' }
                      ]
                    }
                  ]"
                  placeholder="请填写角色名称"
                />
              </a-form-item>

              <a-form-item label="状态">
                <a-select v-decorator="['available', { rules: [] }]">
                  <a-select-option value="1">正常</a-select-option>
                  <a-select-option value="2">禁用</a-select-option>
                </a-select>
              </a-form-item>

              <a-form-item label="备注说明">
                <a-textarea
                  :row="3"
                  v-decorator="[
                    'description',
                    {
                      rules: [
                        { required: true, message: 'Please input role name!' }
                      ]
                    }
                  ]"
                  placeholder="请填写角色名称"
                />
              </a-form-item>

              <a-form-item label="拥有权限">
                <a-row
                  :gutter="16"
                  v-for="(permission, index) in permissions"
                  :key="index"
                >
                  <a-col :xl="4" :lg="24">
                    {{ permission.permissionName }}：
                  </a-col>
                  <a-col :xl="20" :lg="24">
                    <a-checkbox
                      v-if="permission.actionsOptions.length > 0"
                      :indeterminate="permission.indeterminate"
                      :checked="permission.checkedAll"
                      @change="onChangeCheckAll($event, permission)"
                    >
                      全选
                    </a-checkbox>
                    <a-checkbox-group
                      :options="permission.actionsOptions"
                      v-model="permission.selected"
                      @change="onChangeCheck(permission)"
                    />
                  </a-col>
                </a-row>
              </a-form-item>
            </a-form>
          </div>
        </a-col>
      </a-row>
    </a-card>
  </a-modal>
</template>

<script>
import permissionApi from '@/api/permission'
import roleApi from '@/api/role'
import { mixinDevice } from '@/utils/mixin'
import { actionToObject } from '@/utils/permissions'
import pick from 'lodash.pick'

export default {
  name: 'RoleList',
  mixins: [mixinDevice],
  components: {},
  data () {
    return {
      confirmLoading: false,
      visible: false,
      mdl: {},
      roles: [],
      permissions: []
    }
  },
  beforeCreate () {
    this.form = this.$form.createForm(this)
  },
  created () {
    roleApi.list.then((res) => {
      this.roles = res.data.list
      this.roles.push({
        id: '-1',
        name: '新增角色',
        describe: '新增一个角色'
      })
      console.log('this.roles', this.roles)
    })
    this.loadPermissions()
  },
  methods: {
    handleOk () {
      const _this = this
      // 触发表单验证
      this.form.validateFields((err, values) => {
        // 验证表单没错误
        if (!err) {
          // 为value添加一个permissionIds字段
          this.$set(values, 'permissionIds', [])
          // 将选择框中的id赋值到permissionIds中
          this.permissions.forEach(permission => {
            permission.selected.forEach(selected => {
              values.permissionIds.push(selected)
            })
          })
          _this.confirmLoading = true
          // 模拟后端请求 2000 毫秒延迟
          //   new Promise((resolve) => {
          //     // 保存数据到数据库
          //     console.log('form values:', values)
          //     saveRole(values).then(res => {
          //       _this.$message.success('保存成功')
          //       _this.close()
          //       // 向父组件传递成功事件
          //       _this.$emit('ok')
          //     }).catch(err => {
          //       _this.$message.error('保存失败，error:' + err.message)
          //     })
          //   }).finally(() => {
          //     _this.confirmLoading = false
          //     _this.close()
          //   })
        }
      })
    },
    handleCancel () {
      this.close()
    },
    close () {
      this.$emit('close')
      this.visible = false
    },
    callback (val) {
      console.log(val)
    },
    add () {
      this.edit({ id: 0 })
    },
    edit (record) {
      this.mdl = Object.assign({}, record)
      this.visible = true
      // 有权限表，处理勾选
      if (this.mdl.permissions && this.permissions) {
        // 先处理要勾选的权限结构
        const permissionsAction = {}
        this.mdl.permissions.forEach(permission => {
          permissionsAction[permission.id] = permission.actionEntitySet.map(entity => entity.id)
        })
        // 把权限表遍历一遍，设定要勾选的权限 action
        this.permissions.forEach(permission => {
          permission.selected = permissionsAction[permission.id] || []
          this.onChangeCheck(permission)
        })
      }
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(this.mdl, 'id', 'name', 'available', 'description'))
      })
      console.log('this.mdl', this.mdl)
    },
    onChangeCheck (permission) {
      permission.indeterminate = !!permission.selected.length && (permission.selected.length < permission.actionsOptions.length)
      permission.checkedAll = permission.selected.length === permission.actionsOptions.length
    },
    onChangeCheckAll (e, permission) {
      Object.assign(permission, {
        selected: e.target.checked ? permission.actionsOptions.map(obj => obj.value) : [],
        indeterminate: false,
        checkedAll: e.target.checked
      })
    },
    loadPermissions () {
      permissionApi.listAll().then(res => {
        const result = res.data.list
        this.permissions = result.map(permission => {
          const options = actionToObject(permission.actions)
          permission.checkedAll = false
          permission.selected = []
          permission.indeterminate = false
          permission.actionsOptions = options.map(option => {
            return {
              name: option.action,
              label: option.description,
              value: option.id
            }
          })
          return permission
        })
      })
    }
  }
}
</script>

<style scoped>
</style>
