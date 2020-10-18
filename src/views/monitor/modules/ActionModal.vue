<template>
  <a-modal title="日志详情" :visible="visible" :confirmLoading="confirmLoading" @ok="handleOk" @cancel="handleCancel">
    <a-spin :spinning="confirmLoading">
      <a-descriptions :column="1">
        <a-descriptions-item label="操作人">
          {{ actionLog.username }}
        </a-descriptions-item>
        <a-descriptions-item label="操作内容">
          {{ actionLog.operation }}
        </a-descriptions-item>
        <a-descriptions-item label="方法名">
          {{ actionLog.method }}
        </a-descriptions-item>
        <a-descriptions-item label="方法参数">
          {{ actionLog.params }}
        </a-descriptions-item>
        <a-descriptions-item label="耗时">
          {{ actionLog.executionTime }}
        </a-descriptions-item>
        <a-descriptions-item label="操作IP">
          {{ actionLog.ip }}
        </a-descriptions-item>
        <a-descriptions-item label="操作位置">
          {{ actionLog.location }}
        </a-descriptions-item>
        <a-descriptions-item label="操作时间">
          {{ actionLog.createTime }}
        </a-descriptions-item>
      </a-descriptions>
    </a-spin>
  </a-modal>
</template>

<script>
export default {
  name: 'UserModal',
  data () {
    return {
      visible: false,
      confirmLoading: false,
      actionLog: {}
    }
  },
  methods: {
    close () {
      this.$emit('close')
      this.visible = false
    },
    detail (record) {
      this.visible = true
      this.actionLog = Object.assign({}, record)
    },
    handleOk () {
      this.actionLog = {}
      this.close()
    },
    handleCancel () {
      this.actionLog = {}
      this.close()
    }
  }
}
</script>
