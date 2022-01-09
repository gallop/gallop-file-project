// eslint-disable-next-line
import { UserLayout, BasicLayout, RouteView, BlankLayout, PageView, Iframe } from '@/layouts'
// import { bxAnaalyse } from '@/core/icons'

export const asyncRouterMap = [
  {
    path: '/',
    name: 'MenuIndex.vue',
    component: BasicLayout,
    meta: { title: '首页' },
    redirect: '/welcome',
    children: [
      // dashboard
      {
        path: '/',
        name: 'dashboard',
        redirect: '/mgr_user',
        component: RouteView,
        // eslint-disable-next-line standard/object-curly-even-spacing
        meta: {
          title: '主控面板',
          keepAlive: true,
          icon: 'home'
          /* permission: [ 'dashboard' ] */
        },
        children: [
          {
            path: '/mgr_user',
            name: 'sys_user_mgr',
            component: () => import('@/views/system/user/index'),
            // eslint-disable-next-line standard/object-curly-even-spacing
            meta: {
              title: '用户管理',
              keepAlive: false,
              permission: [ 'GET /sysUser/list', 'GET /sysUser/detail', 'POST /sysUser/add', 'POST /sysUser/edit', 'POST /sysUser/delete', 'POST /sysUser/changeStatus', 'GET /sysUser/ownRole', 'POST /sysUser/grantRole', 'POST /sysUser/resetPwd' ]
            }
          },
          {
            path: '/role',
            name: 'role_mgr',
            component: () => import('@/views/system/role/index'),
            // eslint-disable-next-line standard/object-curly-even-spacing
            meta: {
              title: '角色管理',
              keepAlive: false,
              permission: [ 'GET /sysRole/page', 'POST /sysRole/add', 'POST /sysRole/edit', 'POST /sysRole/delete', 'GET /sysRole/getPermissions', 'GET /sysRole/dropDown', 'POST /sysRole/grantMenu' ]
            }
          },
          {
            path: 'doc',
            component: () => import('@/views/system/os/mydoc'),
            name: 'doc',
            meta: {
              permission: ['GET /admin/api/getFileList', 'POST /admin/api/operateFile', 'POST /admin/api/download', 'GET /admin/api/doPreview'],
              title: '我的文档',
              url: '',
              noCache: true
            }
          }
        ]
      }
    ]
  },
  {
    path: '*', redirect: '/404', hidden: true
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
        component: () => import(/* webpackChunkName: "user" */ '@/views/userLoginReg/Login')
      },
      {
        path: 'register',
        name: 'register',
        component: () => import(/* webpackChunkName: "user" */ '@/views/userLoginReg/Register')
      },
      {
        path: 'register-result',
        name: 'registerResult',
        component: () => import(/* webpackChunkName: "user" */ '@/views/userLoginReg/RegisterResult')
      },
      {
        path: 'recover',
        name: 'recover',
        component: undefined
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
    component: () => import(/* webpackChunkName: "fail" */ '@/views/system/exception/404')
  }

]
