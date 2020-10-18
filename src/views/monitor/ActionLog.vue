<template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="48">
          <a-col :md="4" :sm="24">
            <a-form-item label="操作人">
              <a-input v-model="queryParam.username" placeholder="" />
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="24">
            <a-form-item label="操作内容">
              <a-input-number v-model="queryParam.callNo" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :md="5" :sm="24">
            <a-form-item label="操作时间">
              <a-range-picker style="width: 100%" @change="onDatePickerChange" />
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="24">
            <a-form-item label="操作地点">
              <a-input v-model="queryParam.location" placeholder="" />
            </a-form-item>
          </a-col>
          <a-col :md="4" :sm="24">
            <span class="table-page-search-submitButtons">
              <a-button type="primary" @click="$refs.table.refresh(true), 150">查询</a-button>
              <a-button style="margin-left: 8px" @click="resetSearchForm">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>

    <div class="table-operator">
      <a-dropdown v-action:edit v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1"><a-icon type="delete" />删除</a-menu-item>
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
      :alert="{
        show: true,
        clear: () => {
          this.selectedRowKeys = []
        }
      }"
      :rowSelection="{
        selectedRowKeys: this.selectedRowKeys,
        onChange: this.onSelectChange
      }"
    >
      <span slot="action" slot-scope="text, record">
        <template>
          <a href="javascript:;" @click="$refs.modal.detail(record)">详情</a>
          <a-divider type="vertical" v-action:delete />
          <a @click="handleDelete(record)" v-action:delete>删除</a>
        </template>
      </span>
    </s-table>

    <action-modal ref="modal"></action-modal>
  </a-card>
</template>

<script>
import { STable } from '@/components'
import actionLogApi from '@/api/action-log'
import ActionModal from './modules/ActionModal'

export default {
  name: 'ActionLog',
  components: {
    STable,
    ActionModal
  },
  data () {
    return {
      mdl: {},
      // 高级搜索 展开/关闭
      advanced: false,
      // 查询参数
      queryParam: {},
      // 表头
      columns: [
        {
          title: '操作人',
          dataIndex: 'username'
        },
        {
          title: '内容',
          dataIndex: 'operation',
          ellipsis: true
        },
        {
          title: '方法',
          dataIndex: 'method',
          ellipsis: true
        },
        {
          title: '入参',
          dataIndex: 'params',
          ellipsis: true
        },
        {
          title: '执行耗时',
          dataIndex: 'executionTime',
          sorter: true,
          customRender: (text) => {
            return <a-tag color="green">{text} ms</a-tag>
          }
        },
        {
          title: 'IP',
          dataIndex: 'ip'
        },
        {
          title: '位置',
          dataIndex: 'location'
        },
        {
          title: '操作时间',
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
        const queryRequest = {}
        queryRequest.current = parameter.pageNo
        queryRequest.pageSize = parameter.pageSize
        Object.assign(queryRequest, this.queryParam)
        this.$log.debug('loadData.parameter', queryRequest)
        return actionLogApi.list(queryRequest).then(res => {
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
      selectedRowKeys: [],
      selectedRows: []
    }
  },
  methods: {
    onDatePickerChange (dates, dateStrings) {
      this.queryParam.createFrom = dateStrings[0]
      this.queryParam.createTo = dateStrings[1]
    },
    handleDelete (record) {
      this.$log.debug('删除记录:', record)
    },
    handleOk () {

    },
    onSelectChange (selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
    resetSearchForm () {
      this.queryParam = {
        createFrom: null,
        createTo: null
      }
      this.$refs.table.refresh()
    }
  }
}
</script>
