import Vue from 'vue'
import router from './router'
import store from './store'

import NProgress from 'nprogress' // progress bar
import '@/components/NProgress/nprogress.less' // progress bar custom style
import { setDocumentTitle, domTitle } from '@/utils/domUtil'
import { ACCESS_TOKEN, ALL_APPS_MENU } from '@/store/mutation-types'

import { notification } from 'ant-design-vue' // NProgress Configuration
import { timeFix } from '@/utils/util'/// es/notification

// permission judge function
/* function hasPermission(perms, permissions) {
  if (perms.indexOf('*') >= 0) return true // admin permission passed directly
  if (!permissions) {
    return true
  }
  return perms.some(perm => permissions.indexOf(perm) >= 0)
} */

NProgress.configure({ showSpinner: false })
const whiteList = ['login', 'register', 'registerResult'] // no redirect whitelist
// 无默认首页的情况
const defaultRoutePath = '/welcome'

router.beforeEach((to, from, next) => {
  NProgress.start() // start progress bar
  to.meta && (typeof to.meta.title !== 'undefined' && setDocumentTitle(`${to.meta.title} - ${domTitle}`))
  if (Vue.ls.get(ACCESS_TOKEN)) {
    /* has token */
    if (to.path === '/user/login') {
      next({ path: defaultRoutePath })
      NProgress.done()
    } else {
      if (store.getters.buttons.length === 0) {
        store
          .dispatch('GetInfo')
          .then(res => {
            // eslint-disable-next-line camelcase
            const all_app_menu = Vue.ls.get(ALL_APPS_MENU)
            const perms = res.permissions
            // eslint-disable-next-line camelcase
            if (all_app_menu == null) {
              /* const applocation = []
              const apps = { 'code': '', 'name': '', 'active': '', 'perms': '' }
              apps.code = 'system'
              apps.name = '系统应用'
              apps.active = true
              apps.perms = res.permissions */
              Vue.ls.set(ALL_APPS_MENU, res.apps, 7 * 24 * 60 * 60 * 1000)
              // 延迟 1 秒显示欢迎信息
              setTimeout(() => {
                notification.success({
                  message: '欢迎',
                  description: `${timeFix()}，欢迎回来`
                })
              }, 1000)
            }
            store.dispatch('GenerateRoutes', { perms }).then(() => {
              // 动态添加可访问路由表
              store.getters.addRouters.forEach(item => {
                console.log('<<<<<<<<<<<<<<<<<<before addRoutes:' + JSON.stringify(item))
              })
              router.addRoutes(store.getters.addRouters)
              next({ ...to, replace: true }, onComplete => { }, onAbort => { })
              /* // 请求带有 redirect 重定向时，登录自动重定向到该地址
              const redirect = decodeURIComponent(from.query.redirect || to.path)
              if (to.path === redirect) {
                next({ path: redirect })
                // hack方法 确保addRoutes已完成 ,set the replace: true so the navigation will not leave a history record
                next({ ...to, replace: true })
              } else {
                // 跳转到目的路由
                next({ path: redirect })
              } */
            })
          })
          .catch(() => {
            store.dispatch('Logout').then(() => {
              next({ path: '/user/login', query: { redirect: to.fullPath } })
            })
          })
      } else {
        next()
        /* // 没有动态改变权限的需求可直接next() 删除下方权限判断 ↓
        if (hasPermission(store.getters.buttons, to.meta.permission)) {
          next()
        } else {
          next({ path: '/401', replace: true, query: { noGoBack: true } })
        } */
      }
    }
  } else {
    if (whiteList.includes(to.name)) {
      // 在免登录白名单，直接进入
      next()
    } else {
      next({ path: '/user/login', query: { redirect: to.fullPath } })
      NProgress.done() // if current page is login will not trigger afterEach hook, so manually handle it
    }
  }
})

router.afterEach(() => {
  NProgress.done() // finish progress bar
})
