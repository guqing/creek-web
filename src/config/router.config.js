// eslint-disable-next-line
import {
  UserLayout,
  BasicLayout,
  RouteView,
  BlankLayout,
  PageView
} from '@/layouts'

export const asyncRouterMap = [
  {
    path: '/',
    name: 'index',
    component: BasicLayout,
    meta: { title: '首页' },
    redirect: '/dashboard/workplace',
    children: [
      // dashboard
      {
        path: '/dashboard/workplace',
        name: 'dashboard',
        component: () => import('@/views/dashboard/Workplace'),
        meta: { title: '仪表盘', keepAlive: true, permission: ['dashboard'] }
      },
      // forms
      {
        path: '/personnel',
        redirect: '/personnel/users',
        component: PageView,
        meta: { title: '人员管理', icon: 'form', permission: ['personnel'] },
        children: [
          {
            path: '/personnel/users',
            name: 'personnelUsers',
            component: () => import('@/views/personnel/users'),
            meta: { title: '用户', keepAlive: true, permission: ['personnel'] }
          },
          {
            path: '/personnel/groups',
            name: 'personnelGroups',
            component: () => import('@/views/personnel/groups'),
            meta: {
              title: '用户组',
              keepAlive: true,
              permission: ['personnel']
            }
          },
          {
            path: '/personnel/settings',
            name: 'personnelSettings',
            component: () => import('@/views/personnel/settings'),
            meta: { title: '设置', keepAlive: true, permission: ['personnel'] }
          }
        ]
      },

      // permissions
      {
        path: '/permissions',
        name: 'permissions',
        component: PageView,
        redirect: '/permissions/permission-list',
        meta: { title: '权限管理', icon: 'table', permission: ['permissions'] },
        children: [
          {
            path: '/permissions/permission-list/:pageNo([1-9]\\d*)?',
            name: 'permissionList',
            hideChildrenInMenu: true, // 强制显示 MenuItem 而不是 SubMenu
            component: () => import('@/views/permissions/PermissionList'),
            meta: {
              title: '权限列表',
              keepAlive: true,
              permission: ['permissions']
            }
          }
        ]
      },
      // roles
      {
        path: '/roles',
        name: 'rolesPage',
        component: PageView,
        meta: { title: '角色管理', icon: 'slack', permission: ['dashboard'] },
        redirect: '/roles/icon-selector',
        children: [
          {
            path: '/roles/icon-selector',
            name: 'TestIconSelect',
            component: () => import('@/views/roles/IconSelectorView'),
            meta: {
              title: 'IconSelector',
              icon: 'tool',
              keepAlive: true,
              permission: ['dashboard']
            }
          },
          {
            path: '/roles/list/role-list',
            name: 'RoleList',
            component: () => import('@/views/roles/RoleList'),
            meta: { title: '角色列表', keepAlive: true, permission: ['roles'] }
          }
        ]
      },
      // profile
      {
        path: '/profile',
        name: 'profile',
        component: RouteView,
        redirect: '/profile/basic',
        meta: { title: '详情页', icon: 'profile', permission: ['profile'] },
        children: [
          {
            path: '/profile/basic',
            name: 'ProfileBasic',
            component: () => import('@/views/profile/basic/Index'),
            meta: { title: '基础详情页', permission: ['profile'] }
          },
          {
            path: '/profile/advanced',
            name: 'ProfileAdvanced',
            component: () => import('@/views/profile/advanced/Advanced'),
            meta: { title: '高级详情页', permission: ['profile'] }
          }
        ]
      },

      // Exception
      {
        path: '/exception',
        name: 'exception',
        component: RouteView,
        redirect: '/exception/403',
        meta: { title: '异常页', icon: 'warning', permission: ['exception'] },
        children: [
          {
            path: '/exception/403',
            name: 'Exception403',
            component: () =>
              import(/* webpackChunkName: "fail" */ '@/views/exception/403'),
            meta: { title: '403', permission: ['exception'] }
          },
          {
            path: '/exception/404',
            name: 'Exception404',
            component: () =>
              import(/* webpackChunkName: "fail" */ '@/views/exception/404'),
            meta: { title: '404', permission: ['exception'] }
          },
          {
            path: '/exception/500',
            name: 'Exception500',
            component: () =>
              import(/* webpackChunkName: "fail" */ '@/views/exception/500'),
            meta: { title: '500', permission: ['exception'] }
          }
        ]
      },

      // account
      {
        path: '/account',
        component: RouteView,
        redirect: '/account/center',
        name: 'account',
        meta: {
          title: '个人页',
          icon: 'user',
          keepAlive: true,
          permission: ['user']
        },
        children: [
          {
            path: '/account/center',
            name: 'center',
            component: () => import('@/views/account/center/Index'),
            meta: { title: '个人中心', keepAlive: true, permission: ['user'] }
          },
          {
            path: '/account/settings',
            name: 'settings',
            component: () => import('@/views/account/settings/Index'),
            meta: { title: '个人设置', hideHeader: true, permission: ['user'] },
            redirect: '/account/settings/base',
            hideChildrenInMenu: true,
            children: [
              {
                path: '/account/settings/base',
                name: 'BaseSettings',
                component: () => import('@/views/account/settings/BaseSetting'),
                meta: { title: '基本设置', hidden: true, permission: ['user'] }
              },
              {
                path: '/account/settings/security',
                name: 'SecuritySettings',
                component: () => import('@/views/account/settings/Security'),
                meta: {
                  title: '安全设置',
                  hidden: true,
                  keepAlive: true,
                  permission: ['user']
                }
              },
              {
                path: '/account/settings/custom',
                name: 'CustomSettings',
                component: () => import('@/views/account/settings/Custom'),
                meta: {
                  title: '个性化设置',
                  hidden: true,
                  keepAlive: true,
                  permission: ['user']
                }
              },
              {
                path: '/account/settings/binding',
                name: 'BindingSettings',
                component: () => import('@/views/account/settings/Binding'),
                meta: {
                  title: '账户绑定',
                  hidden: true,
                  keepAlive: true,
                  permission: ['user']
                }
              },
              {
                path: '/account/settings/notification',
                name: 'NotificationSettings',
                component: () =>
                  import('@/views/account/settings/Notification'),
                meta: {
                  title: '新消息通知',
                  hidden: true,
                  keepAlive: true,
                  permission: ['user']
                }
              }
            ]
          }
        ]
      },

      // result
      {
        path: '/result',
        name: 'result',
        component: PageView,
        redirect: '/result/success',
        meta: {
          title: '结果页',
          icon: 'check-circle-o',
          permission: ['result']
        },
        children: [
          {
            path: '/result/success',
            name: 'ResultSuccess',
            component: () =>
              import(/* webpackChunkName: "result" */ '@/views/result/Success'),
            meta: {
              title: '成功',
              keepAlive: false,
              hiddenHeaderContent: true,
              permission: ['result']
            }
          },
          {
            path: '/result/fail',
            name: 'ResultFail',
            component: () =>
              import(/* webpackChunkName: "result" */ '@/views/result/Error'),
            meta: {
              title: '失败',
              keepAlive: false,
              hiddenHeaderContent: true,
              permission: ['result']
            }
          }
        ]
      }
    ]
  },
  {
    path: '*',
    redirect: '/404',
    hidden: true
  }
]

/**
 * 基础路由
 * @type { *[] }
 */
export const constantRouterMap = [
  {
    path: '/user',
    component: UserLayout,
    redirect: '/user/login',
    hidden: true,
    children: [
      {
        path: 'login',
        name: 'login',
        component: () =>
          import(/* webpackChunkName: "user" */ '@/views/user/Login')
      },
      {
        path: 'register',
        name: 'register',
        component: () =>
          import(/* webpackChunkName: "user" */ '@/views/user/Register')
      },
      {
        path: 'register-result',
        name: 'registerResult',
        component: () =>
          import(/* webpackChunkName: "user" */ '@/views/user/RegisterResult')
      }
    ]
  },

  {
    path: '/test',
    component: BlankLayout,
    redirect: '/test/home',
    children: [
      {
        path: 'home',
        name: 'TestHome',
        component: () => import('@/views/Home')
      }
    ]
  },

  {
    path: '/404',
    component: () =>
      import(/* webpackChunkName: "fail" */ '@/views/exception/404')
  }
]
