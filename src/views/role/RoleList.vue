<template>
  <a-card :bordered="false">
    <a-row :gutter="8" type="flex" justify="center">
      <a-col :lg="8" :md="24" :order="isMobile ? 1 : 0">
        <a-form-model :model="roleForm" :label-col="labelCol" :wrapper-col="wrapperCol">
          <a-form-model-item label="角色名称">
            <a-input v-model="roleForm.roleName" />
          </a-form-model-item>
          <a-form-model-item label="角色描述">
            <a-textarea
              v-model="roleForm.remark"
              placeholder="角色描述最大长度不能超过150字符"
              :maxLength="150"
              allowClear
              :auto-size="{ minRows: 3, maxRows: 5 }"
            />
          </a-form-model-item>
          <a-form-model-item label="角色权限">
            <a-tree
              v-model="checkedMenuKeys"
              checkable
              :expanded-keys="expandedMenuKeys"
              :auto-expand-parent="autoExpandParent"
              :selected-keys="selectedMenuKeys"
              :tree-data="menuTreeData"
              @expand="onTreeMenuExpand"
              @select="onMenuSelect"
              @check="onTreeMenuCheck"
            />
          </a-form-model-item>
          <a-form-model-item :wrapper-col="roleFormButtonWrapperCol">
            <a-button type="primary" @click="handleSaveOrUpdateRole" :loading="loadingState.save">保存</a-button>
            <a-button :style="{ marginLeft: '15px' }" @click="handleResetRoleForm">重置</a-button>
          </a-form-model-item>
        </a-form-model>
      </a-col>

      <a-col :lg="16" :md="24">
        <div class="table-operator">
          <a-form layout="inline">
            <a-row :gutter="15">
              <a-col :md="8" :sm="24">
                <a-form-item label="角色名称">
                  <a-input placeholder="角色名称" v-model="queryParam.roleName" />
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="24">
                <a-form-item label="角色描述">
                  <a-input placeholder="角色描述" v-model="queryParam.remark" />
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="24">
                <span class="table-page-search-submitButtons">
                  <a-button type="primary" @click="handleSearch" :loading="loadingState.query">查询</a-button>
                  <a-button style="margin-left: 8px" @click="handleResetSearchForm" :loading="loadingState.reset">
                    重置
                  </a-button>
                </span>
              </a-col>
            </a-row>
          </a-form>
          <a-dropdown v-if="selectedRoleKeys.length > 0" style="margin-top: 15px;">
            <a-menu slot="overlay">
              <a-menu-item key="1" @click="handleBatchDeleteRole"><a-icon type="delete" />删除</a-menu-item>
            </a-menu>
            <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /> </a-button>
          </a-dropdown>
        </div>

        <s-table
          ref="table"
          size="default"
          rowKey="id"
          :columns="columns"
          :data="loadData"
          showPagination="auto"
          :alert="false"
          :rowSelection="{ selectedRoleKeys: selectedRoleKeys, onChange: onRoleSelectChange }"
        >
          <span slot="action" slot-scope="text, record">
            <template>
              <a @click="handleRoleEdit(record)">编辑</a>
              <a-divider type="vertical" />
            </template>
            <a-dropdown>
              <a class="ant-dropdown-link"> 更多 <a-icon type="down" /> </a>
              <a-menu slot="overlay">
                <a-menu-item>
                  <a href="javascript:;">详情</a>
                </a-menu-item>
                <a-menu-item>
                  <a href="javascript:;">删除</a>
                </a-menu-item>
              </a-menu>
            </a-dropdown>
          </span>
        </s-table>
      </a-col>
    </a-row>
  </a-card>
</template>

<script>
import { STable } from '@/components'
import menuApi from '@/api/menu'
import roleApi from '@/api/role'
import { baseMixin } from '@/store/app-mixin'

export default {
  name: 'TreeList',
  mixins: [baseMixin],
  components: {
    STable
  },
  data () {
    return {
      loadingState: {
        save: false,
        query: false,
        reset: false
      },
      labelCol: { span: 4 },
      wrapperCol: { span: 18 },
      roleForm: {},
      // 查询参数
      queryParam: {},
      // 表头
      columns: [
        {
          title: '角色名称',
          dataIndex: 'roleName',
          needTotal: true
        },
        {
          title: '角色描述',
          dataIndex: 'remark'
        },
        {
          title: '创建时间',
          dataIndex: 'createTime',
          sorter: true
        },
        {
          title: '操作',
          dataIndex: 'action',
          width: '150px',
          scopedSlots: { customRender: 'action' }
        }
      ],
      // 加载数据方法 必须为 Promise 对象
      loadData: parameter => {
        const requestParameters = Object.assign({}, this.queryParam)
        requestParameters.current = parameter.pageNo
        requestParameters.pageSize = parameter.pageSize
        return roleApi.listRole(requestParameters).then(res => {
          return {
            pageSize: res.data.pageSize,
            pageNo: res.data.current,
            totalCount: res.data.total,
            totalPage: res.data.pages,
            data: res.data.list
          }
        }).catch(err => {
          this.$message.error(`查询出错:${err}`)
          return {
            pageSize: 0,
            pageNo: 1,
            totalCount: 0,
            totalPage: 0,
            data: []
          }
        })
      },
      expandedMenuKeys: [],
      autoExpandParent: false,
      checkedMenuKeys: [],
      selectedMenuKeys: [],
      menuTreeData: [],
      treeParentIds: [],
      editExpandedMenuKeys: [],
      selectedRoleKeys: [],
      selectedRoles: []
    }
  },
  created () {
    this.listTreeMenu()
  },
  computed: {
    roleFormButtonWrapperCol () {
      return {
        span: this.wrapperCol.span,
        offset: this.labelCol.span
      }
    }
  },
  methods: {
    handleRoleEdit (role) {
      // 先清除要展开的keys
      this.editExpandedMenuKeys = []

      var roleId = role.id
      roleApi.getById(roleId).then(res => {
        var menuIdArray = res.data.menuIds || []
        var menuIdStringArray = menuIdArray.map(String)
        var childrenMenuKeys = this.handleTreeChildrenIdsSelector(menuIdStringArray)
        this.checkedMenuKeys = childrenMenuKeys
        this.expandedMenuKeys = this.editExpandedMenuKeys
        // 回显示到表单
        Object.assign(this.roleForm, role)
      })
    },
    listTreeMenu () {
      menuApi.listTreeMenu().then(res => {
        this.menuTreeData = res.data
      })
    },
    onTreeMenuExpand (expandedKeys) {
      // if not set autoExpandParent to false, if children expanded, parent can not collapse.
      // or, you can remove all expanded children keys.
      this.expandedMenuKeys = expandedKeys
      this.autoExpandParent = false
    },
    onTreeMenuCheck (checkedMenuKeys) {
      console.log('onCheck', checkedMenuKeys)
    },
    onMenuSelect (selectedMenuKeys, info) {
      this.$log.debug('onMenuSelect', info.node.dataRef.key)
      var parentIds = this.findParentIdsById(this.menuTreeData, info.node.dataRef.key)
      this.$log.debug('on menu select find all parent ids:', parentIds)

      this.selectedMenuKeys = selectedMenuKeys
    },
    handleClick (e) {
      this.queryParam = {
        key: e.key
      }
      this.$refs.table.refresh(true)
    },
    onRoleSelectChange (selectedRoleKeys, selectedRoles) {
      this.selectedRoleKeys = selectedRoleKeys
      this.selectedRoles = selectedRoles
    },
    handleSaveOrUpdateRole () {
      this.loadingState.save = true
      var menuIds = this.handleRelatedParentRoleMenuKeys()
      this.roleForm.menuIds = menuIds
      roleApi.createOrUpdate(this.roleForm).then(res => {
        this.$message.success('保存成功')
        this.handleResetRoleForm()
        this.$refs.table.refresh()
      }).finally(() => { this.loadingState.save = false })
    },
    handleRelatedParentRoleMenuKeys () {
      var menuIds = []
      // 遍历每一个元素寻找其父元素添加到集合中，否则编辑后父元素的id不会自动关联导致父元元素无法显示
      for (var item of this.checkedMenuKeys) {
        var parentIds = this.findParentIdsById(this.menuTreeData, item)
        menuIds.push(...parentIds)
      }
      // 使用set集合去除重复元素在转为数组传递给后端保存
      var menuIdSet = new Set(menuIds)
      return Array.from(menuIdSet)
    },
    handleResetRoleForm () {
      this.roleForm = {}
      this.checkedMenuKeys = []
      this.expandedMenuKeys = []
    },
    handleTreeChildrenIdsSelector (menuIdArray) {
      this.handleTreeParentIdsSelector(this.menuTreeData)
      var that = this
      return menuIdArray.filter(function (item) {
        if (!that.treeParentIds.includes(item)) {
          return item
        } else {
          that.editExpandedMenuKeys.push(item)
        }
      })
    },
    handleTreeParentIdsSelector (treeList) {
      for (var i in treeList) {
        var data = treeList[i]
        if (data.hasChildren) {
          this.treeParentIds.push(data.id)
          this.handleTreeParentIdsSelector(data.children)
        }
      }
    },
    findParentIdsById (tree, id) {
      var temp = []
      var forFn = function (arr, id) {
        for (var i = 0; i < arr.length; i++) {
          var item = arr[i]
          if (item.id === id) {
            temp.push(item.id)
            forFn(tree, item.parentId)
            break
          } else if (item.children) {
            forFn(item.children, id)
          }
        }
      }
      forFn(tree, id)
      return temp
    },
    handleSearch () {
      this.loadingState.query = true
      this.$refs.table.refresh()
      setTimeout(() => {
        this.loadingState.query = false
      }, 1500)
    },
    handleResetSearchForm () {
      this.loadingState.reset = true
      this.queryParam = {}
      this.$refs.table.refresh()
      setTimeout(() => {
        this.loadingState.reset = false
      }, 1500)
    },
    handleBatchDeleteRole () {
      const that = this
      this.$confirm({
        title: '警告',
        content: `确定要删除所选中的角色吗?`,
        okText: '删除',
        okType: 'danger',
        cancelText: '取消',
        onOk () {
          that.$log.debug('批量删除角色', that.selectedRoleKeys)
          roleApi.deleteByIds(that.selectedRoleKeys).then(res => {
            that.$message.success('删除成功')
          })
        },
        onCancel () {
          that.$log.info('Cancel')
        }
      })
    }
  }
}
</script>
