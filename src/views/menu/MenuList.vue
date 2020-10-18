<template>
  <a-card :bordered="false">
    <a-row :gutter="8" type="flex" justify="center">
      <a-col :lg="12" :md="24" :order="isMobile ? 1 : 0">
        <a-spin tip="Loading..." :spinning="treeDataLoading">
          <a-tree
            v-model="checkedMenuKeys"
            checkable
            blockNode
            :expanded-keys="expandedMenuKeys"
            :auto-expand-parent="autoExpandParent"
            :selected-keys="selectedKeys"
            :tree-data="menuTreeData"
            @expand="onTreeMenuExpand"
            @select="onSelect"
            @check="onTreeMenuCheck"
          />
          <a-empty v-if="menuTreeData.length == 0" />
        </a-spin>
      </a-col>
      <a-col :lg="12" :md="24">
        <a-form-model ref="menuForm" :model="menuForm" :rules="rules" :label-col="labelCol" :wrapper-col="wrapperCol">
          <a-form-model-item label="上级菜单">
            <a-tree-select
              allowClear
              v-model="menuForm.parentId"
              style="width: 100%"
              :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
              :tree-data="menuTreeData"
              tree-default-expand-all
            >
            </a-tree-select>
          </a-form-model-item>
          <a-form-model-item label="菜单/按钮标题" prop="title">
            <a-input placeholder="例如：用户管理" v-model="menuForm.title" />
          </a-form-model-item>
          <a-form-model-item label="资源类型">
            <a-radio-group default-value="0" button-style="solid" v-model="menuForm.type">
              <a-radio-button value="0"> 菜单 </a-radio-button>
              <a-radio-button value="1"> 按钮 </a-radio-button>
            </a-radio-group>
          </a-form-model-item>
          <a-form-model-item label="访问路径" v-show="showMenuFormItem" prop="path">
            <a-input v-model="menuForm.path" placeholder="以 / 开头，例如：/user/list" />
          </a-form-model-item>
          <a-form-model-item label="路由名称" v-show="showMenuFormItem" prop="name">
            <a-input v-model="menuForm.name" placeholder="只能是英文，例如：UserList" />
          </a-form-model-item>
          <a-form-model-item label="组件路径" v-show="showMenuFormItem" prop="component">
            <a-input v-model="menuForm.component" placeholder="相对于views下的组件，例如:user/UserList" />
          </a-form-model-item>
          <a-form-model-item label="权限标识">
            <a-input v-model="menuForm.perms" placeholder="使用冒号分割层级，例如user:list" />
          </a-form-model-item>
          <a-form-model-item label="图标">
            <a-input v-model="menuForm.icon" @click="handleSelectIcon" placeholder="选择一个图标可以展示在菜单标题左侧">
              <a-icon slot="suffix" :type="menuForm.icon || 'question-circle'" title="图标预览" />
            </a-input>
          </a-form-model-item>
          <a-col :style="{ display: moreFormItem ? 'block' : 'none' }">
            <a-form-model-item label="重定向地址" v-show="showMenuFormItem">
              <a-input v-model="menuForm.redirect" placeholder="如果填写，点击时会重定向到该地址" />
            </a-form-model-item>
            <a-form-model-item label="是否缓存组件" v-show="showMenuFormItem">
              <a-switch v-model="menuForm.keepAlive" checked-children="是" un-checked-children="否" default-checked />
            </a-form-model-item>
            <a-form-model-item label="是否隐藏菜单栏" v-show="showMenuFormItem">
              <a-switch v-model="menuForm.hidden" checked-children="显示" un-checked-children="隐藏" />
            </a-form-model-item>
            <a-form-model-item label="排序">
              <a-input v-model="menuForm.sortIndex" placeholder="菜单显示排序" />
            </a-form-model-item>
          </a-col>
          <a-form-model-item :wrapper-col="menuFormButtonWrapperCol">
            <a-button type="primary" @click="handleSaveOrUpdateMenu" :loading="loadingState.save" v-action:save>
              保存
            </a-button>
            <a-button :style="{ marginLeft: '8px' }" @click="handleResetMenuForm" :loading="loadingState.reset">
              重置
            </a-button>
            <a :style="{ marginLeft: '8px' }" @click="moreFormItem = !moreFormItem">
              更多 <a-icon :type="moreFormItem ? 'up' : 'down'" />
            </a>
          </a-form-model-item>
        </a-form-model>

        <a-modal
          title="选择图标"
          :visible="iconSelect.visible"
          @cancel="iconSelect.visible = false"
          @ok="handleIconSelectOk"
        >
          <icon-selector v-model="iconSelect.selected" @change="handleChangeIcon" />
        </a-modal>
      </a-col>
    </a-row>
  </a-card>
</template>

<script>
import menuApi from '@/api/menu'
import { baseMixin } from '@/store/app-mixin'
import { ROUTER_MAP } from '@/store/mutation-types'
import IconSelector from '@/components/IconSelector'
import storage from 'store'

const validatePath = (rule, value, callback) => {
  if (value !== '') {
    var result = /^[A-Za-z0-9/]+([\w.,@?^=%&:/~+#-]*[\w@?^=%&/~+#-])?$/.test(value)
    if (result) {
      callback()
    } else {
      callback(new Error('请输入合法的访问url'))
    }
  }
}
const validateComponentName = (rule, value, callback) => {
  if (value !== '') {
    if (/^[A-Za-z0-9]+$/.test(value)) {
      callback()
    } else {
      callback(new Error('只能输入字母或数字'))
    }
  }
}
const validateComponentPath = (rule, value, callback) => {
  if (value !== '') {
    var result = /^[A-Za-z0-9/]+([\w.,@?^=%&:/~+#-]*[\w@?^=%&/~+#-])?$/.test(value)
    if (result) {
      callback()
    } else {
      callback(new Error('请输入合法的组件路径'))
    }
  }
}
export default {
  name: 'TreeList',
  mixins: [baseMixin],
  components: {
    IconSelector
  },
  data () {
    return {
      iconSelect: {
        visible: false,
        selected: ''
      },
      iconSelectorVisible: false,
      loadingState: {
        save: false,
        reset: false
      },
      menuForm: {},
      treeDataLoading: false,
      rules: {
        title: [
          { required: true, message: '请输入菜单或按钮的名称', trigger: 'blur' },
          { max: 150, message: '长度不能超过150字符', trigger: 'blur' }
        ],
        path: [
          { validator: validatePath, trigger: 'change' }
        ],
        name: [
          { validator: validateComponentName, trigger: 'change' }
        ],
        component: [{ validator: validateComponentPath, trigger: 'change' }]
      },
      moreFormItem: false,
      menuFormButtonWrapperCol: { span: 14, offset: 4 },
      labelCol: { span: 4 },
      wrapperCol: { span: 18 },
      // 查询参数
      queryParam: {},
      expandedMenuKeys: [],
      autoExpandParent: false,
      checkedMenuKeys: [],
      selectedKeys: [],
      menuTreeData: []
    }
  },
  created () {
    this.listTreeMenu()
  },
  computed: {
    showMenuFormItem () {
      return (this.menuForm.type || '0') === '0'
    }
  },
  methods: {
    handleChangeIcon (type) {
      this.iconSelect.selected = type
    },
    handleSelectIcon () {
      this.iconSelect.visible = true
    },
    handleIconSelectOk () {
      this.menuForm.icon = this.iconSelect.selected
      this.iconSelect.visible = false
    },
    handleRoleEdit (role) {
      var menuIdArray = role.menuIds
      var menuIdStringArray = menuIdArray.map(String)
      this.checkedMenuKeys = menuIdStringArray
      this.expandedMenuKeys = menuIdStringArray
    },
    listTreeMenu () {
      this.treeDataLoading = true
      menuApi.listTreeMenu().then(res => {
        this.menuTreeData = res.data
      }).catch(err => {
        this.menuTreeData = []
        this.$message.error(`查询出错:${err}`)
      }).finally(() => { this.treeDataLoading = false })
    },
    onTreeMenuExpand (expandedKeys) {
      console.log('onExpand', expandedKeys)
      // if not set autoExpandParent to false, if children expanded, parent can not collapse.
      // or, you can remove all expanded children keys.
      this.expandedMenuKeys = expandedKeys
      this.autoExpandParent = false
    },
    onTreeMenuCheck (checkedMenuKeys) {
      console.log('onCheck', checkedMenuKeys)
    },
    onSelect (selectedKeys, event) {
      this.handleToggleTreeMenu(selectedKeys, event)

      this.selectedKeys = selectedKeys
      var id = selectedKeys[0]
      if (id) {
        menuApi.getById(id).then(res => {
          this.menuForm = res.data
          this.handleMenuFormValueConvert()
        })
      }
    },
    handleMenuFormValueConvert () {
      if (this.menuForm.keepAlive === 1) {
        this.menuForm.keepAlive = true
      } else {
        this.menuForm.keepAlive = false
      }
      if (this.menuForm.hidden === 1) {
        this.menuForm.hidden = true
      } else {
        this.menuForm.hidden = false
      }
      if (this.menuForm.parentId === 0) {
        this.menuForm.parentId = null
      }
    },
    handleToggleTreeMenu (selectedKeys, event) {
      // 控制树形菜单的展开与折叠
      if (event.node.dataRef.hasChildren) {
        this.expandedMenuKeys = selectedKeys
      }
    },
    handleSaveOrUpdateMenu () {
      this.loadingState.save = true
      this.$refs['menuForm'].validate(valid => {
        if (valid) {
          this.menuForm.type = '0'
          menuApi.saveOrUpdate(this.menuForm).then(res => {
            this.$message.success('保存成功')
            this.listTreeMenu()
            storage.remove(ROUTER_MAP)
            this.handleResetMenuForm()
          }).finally(() => {
            setTimeout(() => {
              this.loadingState.save = false
            }, 1500)
          })
        } else {
          setTimeout(() => {
            this.loadingState.save = false
          }, 1500)
        }
      })
    },
    handleResetMenuForm () {
      this.loadingState.reset = true
      console.log('清除表单执行')
      this.menuForm = {}
      this.checkedMenuKeys = []
      this.expandedMenuKeys = []
      setTimeout(() => {
        this.loadingState.reset = false
      }, 1500)
    }
  }
}
</script>

<style lang="less">
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
