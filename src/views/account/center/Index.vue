<template>
  <div class="page-header-index-wide page-header-wrapper-grid-content-main">
    <a-row :gutter="24">
      <a-col :md="24" :lg="8">
        <a-card :bordered="false">
          <div class="account-center-avatarHolder">
            <div class="avatar">
              <img :src="avatar()" />
            </div>
            <div class="username">{{ nickname() }}</div>
            <div class="bio">{{ userInfo().description }}</div>
          </div>
          <div class="account-center-detail">
            <p v-if="userInfo().email" title="邮箱地址">
              <a-icon type="mail" />{{ userInfo().email }}
            </p>
            <p v-if="userInfo().telephone" title="手机号">
              <a-icon type="phone" />{{ userInfo().telephone }}
            </p>
            <p v-if="userInfo().createTime" title="账号年龄">
              <a-icon type="calendar" />{{ userInfo().createTime | dateToNow }}
            </p>
            <p v-if="userInfo().createTime" title="上次登录时间">
              <a-icon type="safety" />{{
                userInfo().lastLoginTime | formatDate
              }}
            </p>
          </div>
          <a-divider />
          <div class="account-center-tags">
            <a-list :loading="countsLoading" itemLayout="horizontal">
              <a-list-item>
                累计创建了
                {{ counts.presetCount || 0 }} 条预选卡口方案。
              </a-list-item>
              <a-list-item>
                累计创建了 {{ counts.actualCount || 0 }} 条布设卡口方案。
              </a-list-item>
              <a-list-item>
                累计创建了 {{ counts.viaCount || 0 }} 条车辆路径方案。
              </a-list-item>
              <a-list-item>
                累计创建了 {{ counts.routeCount || 0 }} 条车辆轨迹。
              </a-list-item>
              <a-list-item></a-list-item>
            </a-list>
          </div>
          <a-divider :dashed="true" />
        </a-card>
      </a-col>
      <a-col :md="24" :lg="16">
        <a-card
          style="width:100%"
          :bordered="false"
          :tabList="tabListNoTitle"
          :activeTabKey="noTitleKey"
          @tabChange="key => handleTabChange(key, 'noTitleKey')"
        >
          <base-setting v-if="noTitleKey === 'base'"></base-setting>
          <password-page v-else-if="noTitleKey === 'password'"></password-page>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import moment from 'moment'
import { PageView, RouteView } from '@/layouts'
import { BaseSetting, PasswordPage } from './page'

import { mapGetters } from 'vuex'

export default {
  components: {
    RouteView,
    PageView,
    BaseSetting,
    PasswordPage
  },
  data () {
    return {
      counts: {},
      countsLoading: false,

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
  mounted () {
    this.getCounts()
  },
  filters: {
    dateToNow (date) {
      return moment(date).toNow(true)
    },
    formatDate (date) {
      return moment(date).format('YYYY-MM-DD HH:mm:ss')
    }
  },
  methods: {
    ...mapGetters(['nickname', 'avatar', 'userInfo']),

    handleTabChange (key, type) {
      this[type] = key
    },

    getCounts () {
      this.countsLoading = true
      var counts = {}
      this.$set(counts, 'presetCount', 1)
      this.$set(counts, 'actualCount', 1)
      this.$set(counts, 'viaCount', 1)
      this.$set(counts, 'routeCount', 1)
      this.counts = counts
      this.countsLoading = false
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
}
</style>
