<template>
  <a-card :bordered="false" class="permission-wrapper">
    <a-row :gutter="8">
      <a-col :span="8">
        <s-tree
          :dataSource="orgTree"
          :openKeys.sync="openKeys"
          :search="true"
        ></s-tree>
      </a-col>
      <a-col :span="16">
        <a-form layout="horizontal" :form="permissionForm">
          <a-form-item
            label="昵称"
            :label-col="formLayoutOptions.labelCol"
            :wrapper-col="formLayoutOptions.wrapperCol"
          >
            <a-input placeholder="给自己起个名字" />
          </a-form-item>
          <a-form-item
            label="Bio"
            :label-col="formLayoutOptions.labelCol"
            :wrapper-col="formLayoutOptions.wrapperCol"
          >
            <a-textarea rows="4" placeholder="You are not alone." />
          </a-form-item>

          <a-form-item
            label="电子邮件"
            :required="false"
            :label-col="formLayoutOptions.labelCol"
            :wrapper-col="formLayoutOptions.wrapperCol"
          >
            <a-input placeholder="exp@admin.com" />
          </a-form-item>
          <a-form-item
            label="连接密码"
            :required="false"
            :label-col="formLayoutOptions.labelCol"
            :wrapper-col="formLayoutOptions.wrapperCol"
          >
            <a-input placeholder="h3gSbecd" />
          </a-form-item>
          <a-form-item
            label="登录密码"
            :required="false"
            :label-col="formLayoutOptions.labelCol"
            :wrapper-col="formLayoutOptions.wrapperCol"
          >
            <a-input placeholder="密码" />
          </a-form-item>

          <a-form-item :wrapper-col="formLayoutOptions.btnWrapperCol">
            <a-button type="primary">更新</a-button>
            <a-button style="margin-left: 8px">取消</a-button>
          </a-form-item>
        </a-form>
      </a-col>
    </a-row>
  </a-card>
</template>

<script>
import { STable } from '@/components'
import STree from '@/components/Tree/Tree'
import permissonApi from '@/api/permission'

export default {
  name: 'PermissionList',
  components: {
    STable,
    STree
  },
  data () {
    return {
      formLayoutOptions: {
        labelCol: { lg: { span: 4 }, sm: { span: 4 } },
        wrapperCol: { lg: { span: 16 }, sm: { span: 10 } },
        btnWrapperCol: { span: 14, offset: 4 }
      },
      permissionForm: this.$form.createForm(this),
      openKeys: ['key-01'],

      // 查询参数
      queryParam: {},

      orgTree: [],
      selectedRowKeys: [],
      selectedRows: []
    }
  },
  created () {
    permissonApi.listAll().then(res => {
      var permissions = res.data.list
      var permissionTree = []
      permissions.forEach(element => {
        var permission = {}
        permission.name = element.name
        permission.children = element.actionEntitySet
        permissionTree.push(permission)
      })
      this.orgTree = permissions
    })
  },
  watch: {
    checkedKeys (val) {
      console.log('onCheck', val)
    }
  },
  methods: {
    onExpand (expandedKeys) {
      console.log('onExpand', expandedKeys)
      // if not set autoExpandParent to false, if children expanded, parent can not collapse.
      // or, you can remove all expanded children keys.
      this.expandedKeys = expandedKeys
      this.autoExpandParent = false
    },
    onCheck (checkedKeys) {
      console.log('onCheck', checkedKeys)
      this.checkedKeys = checkedKeys
    },
    onSelect (selectedKeys, info) {
      console.log('onSelect', info)
      this.selectedKeys = selectedKeys
    },
    handleOk () {
      // 新增/修改 成功时，重载列表
      // this.$refs.table.refresh()
    }
  }
}
</script>

<style lang="less">
.permission-wrapper {
  min-height: 500px;
}
.custom-tree {
  /deep/ .ant-menu-item-group-title {
    position: relative;
    &:hover {
      .btn {
        display: block;
      }
    }
  }

  /deep/ .ant-menu-item {
    &:hover {
      .btn {
        display: block;
      }
    }
  }

  /deep/ .btn {
    display: none;
    position: absolute;
    top: 0;
    right: 10px;
    width: 20px;
    height: 40px;
    line-height: 40px;
    z-index: 1050;

    &:hover {
      transform: scale(1.2);
      transition: 0.5s all;
    }
  }
}
</style>
