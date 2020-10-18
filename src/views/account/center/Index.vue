<template>
  <div class="page-header-index-wide page-header-wrapper-grid-content-main">
    <a-row :gutter="24">
      <a-col :md="24" :lg="8">
        <a-card :bordered="false">
          <div class="account-center-avatarHolder">
            <div class="avatar" title="点击可以修改头像">
              <a-upload name="file" :showUploadList="false" :beforeUpload="handleBeforeUpload">
                <div class="ant-upload-preview" type="upload">
                  <a-avatar size="large" icon="user" :src="avatar" class="avatar-preview" />
                </div>
              </a-upload>
            </div>
            <div class="username">{{ userInfo.nickname }}</div>
            <div class="bio">{{ userInfo.description }}</div>
          </div>
          <div class="account-center-detail">
            <p>
              <a-icon type="crown" />
              <a-tag v-for="(item, index) in userInfo.roleNames" :key="index">{{ item }}</a-tag>
            </p>
            <p v-if="userInfo.email" title="邮箱地址"><a-icon type="mail" />{{ userInfo.email }}</p>
            <p v-if="userInfo.mobile" title="手机号"><a-icon type="phone" />{{ userInfo.mobile }}</p>
            <p v-if="userInfo.createTime" title="账号年龄">
              <a-icon type="calendar" />{{ userInfo.createTime | dateToNow }}
            </p>
            <p v-if="userInfo.lastLoginTime" title="上次登录时间">
              <a-icon type="safety" />{{ userInfo.lastLoginTime | formatDate }}
            </p>
          </div>
          <a-divider />

          <div class="social-account">
            <div class="socail-account-title">关联第三方账号</div>
            <a-list class="social-account-list" item-layout="horizontal" :data-source="socailAccounts">
              <a-list-item slot="renderItem" slot-scope="item, index" :key="index">
                <a slot="actions" v-if="item.isConnected" @click="handleUnbindSocial(item.provider)">取消关联</a>
                <a slot="actions" v-else @click="handleBindSocialAccount(item.provider)">关联</a>
                <a-list-item-meta>
                  <a slot="title" href="https://www.antdv.com/">{{ item.name }}</a>
                  <a-avatar slot="avatar" :src="item.logo" />
                  <div>{{ item.isConnected ? '已关联' : '未关联' }}</div>
                </a-list-item-meta>
              </a-list-item>
            </a-list>
          </div>
          <a-divider :dashed="true" />

          <div class="account-center-tags">
            <div class="tagsTitle">标签</div>
            <div>
              <template v-for="(tag, index) in tags">
                <a-tooltip v-if="tag.length > 20" :key="tag" :title="tag">
                  <a-tag :key="tag" :closable="index !== 0" :close="() => handleTagClose(tag)">
                    {{ `${tag.slice(0, 20)}...` }}
                  </a-tag>
                </a-tooltip>
                <a-tag v-else :key="tag" :closable="index !== 0" :close="() => handleTagClose(tag)">{{ tag }}</a-tag>
              </template>
              <a-input
                v-if="tagInputVisible"
                ref="tagInput"
                type="text"
                size="small"
                :style="{ width: '78px' }"
                :value="tagInputValue"
                @change="handleInputChange"
                @blur="handleTagInputConfirm"
                @keyup.enter="handleTagInputConfirm"
              />
              <a-tag v-else @click="showTagInput" style="background: #fff; borderstyle: dashed">
                <a-icon type="plus" />New Tag
              </a-tag>
            </div>
          </div>

          <a-divider :dashed="true" />
        </a-card>
      </a-col>
      <a-col :md="24" :lg="16">
        <a-card
          style="width: 100%"
          :bordered="false"
          :tabList="tabListNoTitle"
          :activeTabKey="noTitleKey"
          @tabChange="(key) => handleTabChange(key, 'noTitleKey')"
        >
          <base-setting v-if="noTitleKey === 'base'"></base-setting>
          <password-page v-else-if="noTitleKey === 'password'"></password-page>
        </a-card>
      </a-col>
    </a-row>

    <avatar-modal ref="modal" @success="handleUploadAvatar"> </avatar-modal>
  </div>
</template>

<script>
import moment from 'moment'
import { PageView, RouteView } from '@/layouts'
import BaseSetting from './page/BaseSetting'
import PasswordPage from './page/PasswordPage'
import AvatarModal from './page/AvatarModal'
import { mapGetters } from 'vuex'
import {
  listSupportSocail,
  socialLoginApi,
  bindSocial,
  listUserConnectedSocail,
  unbindSocial
} from '@/api/login'
import { getSocailInfo } from '@/utils/socailInfo'
import userApi from '@/api/user'

export default {
  components: {
    RouteView,
    PageView,
    AvatarModal,
    BaseSetting,
    PasswordPage
  },
  data () {
    return {
      page: {
        width: window.screen.width * 0.5,
        height: window.screen.height * 0.5
      },
      userConnectedSocail: [],
      socailAccounts: [],
      tags: ['旧街凉风', '坐拥百态', '醉酒入梦', '晚风抚人', '无人及你'],
      tagInputVisible: false,
      tagInputValue: '',
      tabListNoTitle: [
        {
          key: 'base',
          tab: '基础设置'
        },
        {
          key: 'password',
          tab: '密码'
        }
      ],
      noTitleKey: 'base'
    }
  },
  filters: {
    dateToNow (date) {
      return moment(date).toNow(true)
    },
    formatDate (date) {
      return moment(date).format('YYYY-MM-DD HH:mm:ss')
    }
  },
  computed: {
    ...mapGetters(['avatar', 'userInfo'])
  },
  mounted () {
    this.handleListUserSocial()
  },
  destroyed () {
    window.removeEventListener('message', this.resolveBindResult)
  },
  methods: {
    handleBindSocialAccount (oauthType) {
      const url = `${socialLoginApi}/${oauthType.toLowerCase()}/bind`
      window.open(url,
        'Bind Social Account',
        `height=${this.page.height},width=${this.page.width}, top=10%, left=10%, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no`)
      window.addEventListener('message', this.resolveBindResult, false)
    },
    resolveBindResult (e) {
      this.$log.debug('解析得到绑定结果:', e.data, e.data.data)
      if (e.data.authUser) {
        bindSocial(e.data.authUser).then(res => {
          this.$message.success('绑定成功')
          this.handleListUserSocial()
        }).catch(err => {
          this.$message.error(`绑定失败:${err.message}`)
        })
      }
    },
    handleUnbindSocial (oauthType) {
      var that = this
      this.$confirm({
        title: '确定要解除绑定吗?',
        onOk () {
          unbindSocial(oauthType).then(res => {
            that.$message.success('解绑成功')
            that.handleListUserSocial()
          })
        },
        onCancel () { }
      })
    },
    handleListUserSocial () {
      listUserConnectedSocail().then(res => {
        this.userConnectedSocail = res.data
        this.handleListSupportSocial()
      })
    },
    handleListSupportSocial () {
      listSupportSocail().then(res => {
        var socailAccounts = []
        res.data.forEach(item => {
          var socailInfo = getSocailInfo(item)
          socailAccounts.push({
            name: socailInfo.name,
            url: socailInfo.url,
            provider: item,
            logo: `/icon/${item.toLowerCase()}.png`,
            isConnected: this.userConnectedSocail.indexOf(item) > -1
          })
          this.socailAccounts = socailAccounts
        })
      })
    },
    handleBeforeUpload (file) {
      console.log('before upload:', file)
      this.$refs.modal.edit(file)
      return false
    },
    handleUploadAvatar (avatarUrl) {
      // 更新用户头像
      userApi.updateAvatar(avatarUrl).then(res => {
        this.$message.success('更新成功')
        // 更新vue state
        this.$store.commit('SET_AVATAR', avatarUrl)
      })
    },
    handleTabChange (key, type) {
      this[type] = key
    },
    handleTagClose (removeTag) {
      const tags = this.tags.filter(tag => tag !== removeTag)
      this.tags = tags
    },
    // tag
    showTagInput () {
      this.tagInputVisible = true
      this.$nextTick(() => {
        this.$refs.tagInput.focus()
      })
    },
    handleInputChange (e) {
      this.tagInputValue = e.target.value
    },
    handleTagInputConfirm () {
      const inputValue = this.tagInputValue
      let tags = this.tags
      if (inputValue && !tags.includes(inputValue)) {
        tags = [...tags, inputValue]
      }
      Object.assign(this, {
        tags,
        tagInputVisible: false,
        tagInputValue: ''
      })
    }
  }
}
</script>

<style lang="less" scoped>
.page-header-wrapper-grid-content-main {
  width: 100%;
  height: 100%;
  min-height: 100%;
  transition: 0.3s;

  .account-center-avatarHolder {
    text-align: center;
    margin-bottom: 24px;

    & > .avatar {
      margin: 0 auto;
      width: 104px;
      height: 104px;
      margin-bottom: 20px;
      border-radius: 50%;
      overflow: hidden;
      img {
        height: 100%;
        width: 100%;
      }
    }

    .username {
      color: rgba(0, 0, 0, 0.85);
      font-size: 20px;
      line-height: 28px;
      font-weight: 500;
      margin-bottom: 4px;
    }
  }

  .account-center-detail {
    p {
      margin-bottom: 8px;
      padding-left: 26px;
      position: relative;
    }

    i {
      position: absolute;
      height: 14px;
      width: 14px;
      left: 0;
      top: 4px;
    }
  }

  .account-center-tags {
    .ant-tag {
      margin-bottom: 8px;
    }
  }

  .tagsTitle,
  .teamTitle {
    font-weight: 500;
    color: rgba(0, 0, 0, 0.85);
    margin-bottom: 12px;
  }
  .avatar {
    border: 1px solid #ccc;
  }
  .avatar-preview {
    width: 100%;
    height: 100%;
  }
}
</style>
