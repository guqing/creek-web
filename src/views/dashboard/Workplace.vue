
<template>
  <page-view :avatar="avatar" :title="false">
    <div slot="headerContent">
      <div class="title">
        {{ timeFix }}，{{ user.nickname }}
        <span class="welcome-text">，{{ welcome() }}</span>
      </div>
      <div>人间四月芳菲尽，山寺桃花始盛开。</div>
    </div>
    <div>
      <a-row :gutter="24">
        <a-col :xl="24" :lg="24" :md="24" :sm="24" :xs="24">
          <a-card
            class="project-list"
            :loading="loading"
            style="margin-bottom: 24px;"
            :bordered="false"
            title="我的账户"
            :body-style="{ padding: 0 }"
          >
            <!-- <a slot="extra">全部项目</a> -->
            <div>
              <a-card-grid
                class="project-card-grid"
                :key="i"
                v-for="(item, i) in myCountDashboard"
              >
                <a-card :bordered="false" :body-style="{ padding: 0 }">
                  <a-card-meta>
                    <div slot="title" class="card-description">
                      {{ item.count }}
                    </div>
                    <div slot="title" class="card-title">
                      <a>
                        <img :src="item.icon" class="card-title-icon" />
                        {{ item.title }}
                      </a>
                    </div>
                  </a-card-meta>
                </a-card>
              </a-card-grid>
            </div>
          </a-card>

          <a-card title="日志" :bordered="false">
            <a-list itemLayout="horizontal" :dataSource="activities">
              <a-list-item
                slot="renderItem"
                slot-scope="item, index"
                :key="index"
              >
                <template slot="actions">
                  <div>{{ item.createTime | timeAgo }}</div>
                </template>
                <a-list-item-meta :description="item.content">
                  <a-avatar slot="avatar" :src="item.user.avatar" />
                  <div slot="title">
                    <span>{{ item.user.nickname }}</span
                    >&nbsp;
                    <a href="#">{{ item.name }}</a>
                  </div>
                </a-list-item-meta>
              </a-list-item>
            </a-list>
            <a-pagination
              @change="onPageChange"
              v-model="pagination.current"
              :total="pagination.total"
              style="text-align:right;"
            />
          </a-card>
        </a-col>
      </a-row>
    </div>
  </page-view>
</template>

<script>
import { timeFix } from '@/utils/util'
import { mapGetters } from 'vuex'

import { PageView } from '@/layouts'
import HeadInfo from '@/components/tools/HeadInfo'

import dashboardApi from '@/api/dashboard'
import moment from 'moment'

export default {
  name: 'Workplace',
  components: {
    PageView,
    HeadInfo
  },
  data () {
    return {
      timeFix: timeFix(),
      avatar: '',
      user: {},

      myCountDashboard: [],
      projectsCount: {},
      activities: [],
      pagination: {
        current: 1,
        pageSize: 10,
        total: 0
      },
      loading: false
    }
  },
  computed: {
    userInfo () {
      return this.$store.getters.userInfo
    }
  },
  created () {
    this.user = this.userInfo
    this.avatar = this.userInfo.avatar
  },
  mounted () {
    this.getProjects()
    this.loadLog()
  },
  methods: {
    ...mapGetters(['nickname', 'welcome']),
    getProjects () {
      this.loading = true
      this.loadData().then(res => {
        this.myCountDashboard = [
          {
            id: 1,
            title: '用户',
            icon: '/dashboard/user.png',
            count: res.userCount
          },
          {
            id: 2,
            title: '用户组',
            icon: '/dashboard/usergroup.png',
            count: 1
          },
          {
            id: 3,
            title: 'RAM角色',
            icon: '/dashboard/RAM.png',
            count: res.roleCount
          }
        ]
      })

      this.loading = false
    },
    loadData () {
      return new Promise((resolve, reject) => {
        dashboardApi.countRam().then(res => {
          resolve(res.data)
        }).catch(callback => {
          reject(callback)
        })
      })
    },
    loadLog () {
      dashboardApi.listLogByPage(this.pagination).then(res => {
        this.activities = res.data.list
        this.pagination.total = res.data.total
      })
    },
    onPageChange () {
      this.loadLog()
    }
  },
  filters: {
    timeAgo (time) {
      var currentTime = new Date().getTime()
      var between = currentTime - time
      var days = Math.floor(between / (24 * 3600 * 1000))
      if (days === 0) {
        var leave1 = between % (24 * 3600 * 1000)
        var hours = Math.floor(leave1 / (3600 * 1000))
        if (hours === 0) {
          var leave2 = leave1 % (3600 * 1000)
          var minutes = Math.floor(leave2 / (60 * 1000))
          if (minutes === 0) {
            var leave3 = leave2 % (60 * 1000)
            var seconds = Math.round(leave3 / 1000)
            return seconds + ' 秒前'
          }
          return minutes + ' 分钟前'
        }
        return hours + ' 小时前'
      }
      if (days < 0) return '刚刚'
      if (days < 5) {
        return days + ' 天前'
      } else {
        return moment(time).format('YYYY-MM-DD HH:mm')
      }
    }

  }
}
</script>

<style lang="less" scoped>
.project-list {
  .card-title {
    font-size: 0;
    a {
      color: rgba(0, 0, 0, 0.85);
      line-height: 24px;
      height: 24px;
      margin-left: 12px;
      display: inline-block;
      vertical-align: top;
      font-size: 14px;

      &:hover {
        color: #1890ff;
      }
    }
  }
  .card-title-icon {
    width: 14px;
    margin-right: 8px;
  }
  .card-description {
    color: #262626;
    font-size: 28px;
    margin-left: 12px;
    height: 44px;
    line-height: 22px;
    overflow: hidden;
  }
  .project-item {
    display: flex;
    margin-top: 8px;
    overflow: hidden;
    font-size: 12px;
    height: 20px;
    line-height: 20px;
    a {
      color: rgba(0, 0, 0, 0.45);
      display: inline-block;
      flex: 1 1 0;

      &:hover {
        color: #1890ff;
      }
    }
    .datetime {
      color: rgba(0, 0, 0, 0.25);
      flex: 0 0 auto;
      float: right;
    }
  }
  .ant-card-meta-description {
    color: rgba(0, 0, 0, 0.45);
    height: 44px;
    line-height: 22px;
    overflow: hidden;
  }
}

.item-group {
  padding: 20px 0 8px 24px;
  font-size: 0;
  a {
    color: rgba(0, 0, 0, 0.65);
    display: inline-block;
    font-size: 14px;
    margin-bottom: 13px;
    width: 25%;
  }
}

.members {
  a {
    display: block;
    margin: 12px 0;
    line-height: 24px;
    height: 24px;
    .member {
      font-size: 14px;
      color: rgba(0, 0, 0, 0.65);
      line-height: 24px;
      max-width: 100px;
      vertical-align: top;
      margin-left: 12px;
      transition: all 0.3s;
      display: inline-block;
    }
    &:hover {
      span {
        color: #1890ff;
      }
    }
  }
}

.mobile {
  .project-list {
    .project-card-grid {
      width: 100%;
    }
  }

  .more-info {
    border: 0;
    padding-top: 16px;
    margin: 16px 0 16px;
  }

  .headerContent .title .welcome-text {
    display: none;
  }
}
</style>
