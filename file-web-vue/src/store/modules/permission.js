import { asyncRouterMap, constantRouterMap } from '@/config/router.config'
import { generatorStaticRouter } from '@/router/generator-routers'

/**
 * 通过meta.perms判断是否与当前用户权限匹配
 * @param perms
 * @param route
 */
function hasPermission(perms, route) {
  if (route.meta && route.meta.permission) {
    return perms.some(perm => route.meta.permission.includes(perm))
  } else {
    return true
  }
}

/**
 * 单账户多角色时，使用该方法可过滤角色不存在的菜单
 *
 * @param roles
 * @param route
 * @returns {*}
 */
// eslint-disable-next-line
function hasRole(roles, route) {
  if (route.meta && route.meta.roles) {
    return route.meta.roles.includes(roles.id)
  } else {
    return true
  }
}

function filterAsyncRouter (routerMap, perms) {
  const res = []
  routerMap.forEach(route => {
    const tmp = { ...route }
    if (tmp.children) {
      tmp.children = filterAsyncRouter(tmp.children, perms)
      if (tmp.children && tmp.children.length > 0) {
        res.push(tmp)
      }
    } else {
      if (hasPermission(perms, tmp)) {
        res.push(tmp)
      }
    }
  })

  return res
}

const permission = {
  state: {
    routers: constantRouterMap,
    addRouters: []
  },
  mutations: {
    SET_ROUTERS: (state, routers) => {
      state.addRouters = routers
      state.routers = constantRouterMap.concat(routers)
    }
  },
  actions: {
    GenerateRoutes ({ commit }, data) {
      return new Promise(resolve => {
        const { perms } = data
        let accessedRouters
        console.log('----perms:' + perms)
        if (perms.includes('*')) {
          asyncRouterMap.map(item => {
            if (item.name === 'MenuIndex.vue') {
              const items = generatorStaticRouter()
              let children = item.children
              items.forEach(item => {
                console.log('--->>>>' + JSON.stringify(item))
                if (children) {
                  children.push(item)
                } else {
                  children = []
                  children.push(item)
                }
              })
              item.children = children
            }
          })
          accessedRouters = asyncRouterMap
        } else {
          accessedRouters = filterAsyncRouter(asyncRouterMap, perms)
        }
        commit('SET_ROUTERS', accessedRouters)
        resolve()
      })
    }
  }
}

export default permission
