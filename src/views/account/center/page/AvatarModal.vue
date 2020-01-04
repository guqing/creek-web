<template>
  <a-modal
    title="修改头像"
    :visible="visible"
    :maskClosable="false"
    :confirmLoading="confirmLoading"
    :width="800"
    @cancel="cancelHandle">
    <a-row>
      <a-col :xs="24" :md="12" :style="{height: '350px'}">
        <vue-cropper
          ref="cropper"
          :img="options.img"
          :info="true"
          :autoCrop="options.autoCrop"
          :autoCropWidth="options.autoCropWidth"
          :autoCropHeight="options.autoCropHeight"
          :fixedBox="options.fixedBox"
          @realTime="realTime"
        >
        </vue-cropper>
      </a-col>
      <a-col :xs="24" :md="12" :style="{height: '350px'}">
        <div class="avatar-upload-preview">
          <img :src="previews.url" :style="previews.img"/>
        </div>
      </a-col>
    </a-row>

    <template slot="footer">
      <a-button key="back" @click="cancelHandle">取消</a-button>
      <a-button key="submit" type="primary" :loading="confirmLoading" @click="okHandle">保存</a-button>
    </template>
  </a-modal>
</template>
<script>
import userApi from '@/api/user'
export default {
  data () {
    return {
      visible: false,
      confirmLoading: false,

      options: {
        img: '',
        autoCrop: true,
        autoCropWidth: 200,
        autoCropHeight: 200,
        fixedBox: true
      },
      previews: {},
      avatarFile: {}
    }
  },
  methods: {
    edit (imageFile) {
      var that = this
      this.avatarFile = imageFile
      var reader = new FileReader()
      reader.readAsDataURL(imageFile)
      reader.onloadend = function () {
        that.options.img = this.result
      }
      this.visible = true
    },
    close () {
      this.visible = false
    },
    cancelHandle () {
      this.close()
    },
    okHandle (type) {
      var that = this
      this.$refs.cropper.getCropData(data => {
        that.uploadAvatar(data)
      })
    },
    realTime (data) {
      this.previews = data
    },

    uploadAvatar (data) {
      var file = this.dataUrlToFile(data)
      var formData = new FormData()
      formData.append('file', file)

      userApi.uploadAvatar(formData).then(res => {
        this.$message.success('头像上传成功')
        this.$emit('success', res.data)
      })

      // 关闭裁剪窗口
      this.close()
    },

    dataUrlToFile (dataurl) {
      var arr = dataurl.split(',')
      // var mime = arr[0].match(/:(.*?);/)[1]
      var bstr = atob(arr[1])
      var n = bstr.length
      var u8arr = new Uint8Array(n)
      while (n--) {
        u8arr[n] = bstr.charCodeAt(n)
      }
      return new File([u8arr], this.avatarFile.name, { type: this.avatarFile.type })
    }
  }
}
</script>

<style lang="less" scoped>

  .avatar-upload-preview {
    position: absolute;
    top: 50%;
    transform: translate(50%, -50%);
    width: 180px;
    height: 180px;
    border-radius: 50%;
    box-shadow: 0 0 4px #ccc;
    overflow: hidden;

    img {
      width: 100%;
      height: 100%;
    }
  }
</style>
