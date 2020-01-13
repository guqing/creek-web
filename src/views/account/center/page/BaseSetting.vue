<template>
  <div class="account-settings-info-view">
    <a-row :gutter="16">
      <a-col :md="24" :lg="16">
        <a-form layout="vertical">
          <a-form-item label="昵称" :required="true">
            <a-input placeholder="给自己起个名字" v-model="user.nickname" />
          </a-form-item>
          <a-form-item label="Bio">
            <a-textarea
              rows="4"
              placeholder="You are not alone."
              v-model="user.description"
            />
          </a-form-item>

          <a-form-item label="电子邮件" :required="false">
            <a-input placeholder="exp@admin.com" v-model="user.email" />
          </a-form-item>

          <a-form-item label="手机号码" :required="false">
            <a-input placeholder="填写一个手机号" v-model="user.telephone" />
          </a-form-item>

          <a-form-item>
            <a-button type="primary" @click="handleUpdateUserInfo">
              保存
            </a-button>
          </a-form-item>
        </a-form>
      </a-col>
      <a-col :md="24" :lg="8" :style="{ minHeight: '180px' }">
        <a-upload
          name="file"
          :showUploadList="false"
          :beforeUpload="handleBeforeUpload"
        >
          <div class="ant-upload-preview" type="upload">
            <a-icon type="cloud-upload-o" class="upload-icon" />
            <div class="mask">
              <a-icon type="plus" />
            </div>
            <img :src="getAvatar" />
          </div>
        </a-upload>
      </a-col>
    </a-row>

    <avatar-modal ref="modal" @success="handleUploadAvatar"> </avatar-modal>
  </div>
</template>

<script>
import AvatarModal from './AvatarModal'
import userApi from '@/api/user'
import { mapActions } from 'vuex'

export default {
  components: {
    AvatarModal
  },
  data () {
    return {
      user: {}
    }
  },
  mounted () {
    this.loadUser()
  },
  methods: {
    ...mapActions(['GetInfo']),

    loadUser () {
      this.GetInfo().then(res => {
        this.user = res
      })
    },

    handleBeforeUpload (file) {
      console.log('before upload:', file)
      this.$refs.modal.edit(file)
      return false
    },

    handleUpdateUserInfo () {
      if (this.user.nickname === '') {
        this.$message.warning('昵称不能为空')
        return
      }
      if (this.user.description.length > 144) {
        this.$message.warning('个人说明不能超过144个字符')
        return
      }
      userApi.updateUserInfo(this.user).then(res => {
        this.$message.success('保存成功')
        this.loadUser()
        // 更新vuex状态
        this.GetInfo()
      }).catch(err => {
        this.$message.error(`更新用户信息出错,error: ${err.message}`)
      })
    },
    handleUploadAvatar (avatarUrl) {
      this.user['avatar'] = avatarUrl
    }
  },
  computed: {
    getAvatar () {
      if (this.user.avatar === '') {
        return '/placeholder.jpg'
      }
      return this.user.avatar
    }
  }
}
</script>

<style lang="less" scoped>
.avatar-upload-wrapper {
  height: 200px;
  width: 100%;
}

.ant-upload-preview {
  position: relative;
  margin: 0 auto;
  width: 100%;
  max-width: 180px;
  border-radius: 50%;
  box-shadow: 0 0 4px #ccc;

  .upload-icon {
    position: absolute;
    top: 0;
    right: 10px;
    font-size: 1.4rem;
    padding: 0.5rem;
    background: rgba(222, 221, 221, 0.7);
    border-radius: 50%;
    border: 1px solid rgba(0, 0, 0, 0.2);
  }
  .mask {
    opacity: 0;
    position: absolute;
    background: rgba(0, 0, 0, 0.4);
    cursor: pointer;
    transition: opacity 0.4s;

    &:hover {
      opacity: 1;
    }

    i {
      font-size: 2rem;
      position: absolute;
      top: 50%;
      left: 50%;
      margin-left: -1rem;
      margin-top: -1rem;
      color: #d6d6d6;
    }
  }

  img,
  .mask {
    width: 100%;
    max-width: 180px;
    height: 100%;
    border-radius: 50%;
    overflow: hidden;
  }
}
</style>
