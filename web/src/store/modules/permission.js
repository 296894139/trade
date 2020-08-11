import { asyncRouterMap, constantRouterMap } from '@/router'

/**
 * 通过meta.perms判断是否与当前用户权限匹配
 * @param perms
 * @param route
 */
function hasPermission(perms, route) {
  console.log(perms,route,'--------------------------------------');
  console.log(route.meta,'--------------------ooooooooooooooo-------------');
  if (route.meta && route.meta.perms) {
    console.log("111111111111111111111111111111111111",route.meta.perms)
    return perms.some(perm => route.meta.perms.includes(perm))
  } else {
    console.log("2222222222222222",route)
    return true
  }
}

/**
 * 递归过滤异步路由表，返回符合用户角色权限的路由表
 * @param routes asyncRouterMap
 * @param perms
 */
function filterAsyncRouter(routes, perms) {
  const res = []

  routes.forEach(route => {

    const tmp = { ...route }
  /*  if (hasPermission(perms, tmp)) {
      res.push(tmp)
    }*/
    console.log("---------------------temp是啥----------------------,",tmp)

    if (tmp.children) {
      tmp.children = filterAsyncRouter(tmp.children, perms)

     if (tmp.children && tmp.children.length > 0&&hasPermission(perms, tmp.children)) {
        console.log('push的有父子节点的数据')
        res.push(tmp)
      }
    } else {
      if (hasPermission(perms, tmp)) {
      //  console.log("---------------------temp----------------------,",hasPermission(perms, tmp))
        console.log('push所有二级菜单',tmp)
        res.push(tmp)
      }
    }
  })
  console.log('最后的res',res)
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
    GenerateRoutes({ commit }, data) {
      return new Promise(resolve => {

     //  const { perms,level } = data
       const perms = data.data.perms
       let level  = data.data.level
       console.log(level,'data66',data)
        let accessedRouters
        //管理员 权限
        if (perms.includes('*')) {
          //管理员 去除缴纳保证金页面和vr
          accessedRouters = asyncRouterMap.filter(item => {
            return item.path !='/money'
          })
          console.log(accessedRouters,'管理员')
        }else {
          accessedRouters = asyncRouterMap.filter(item => {
            var result=''
            if(perms.includes('人员管理')){
              result=result||item.path =='/user'
            }
            if(perms.includes('酒庄管理')){
              result=result||item.path =='/shopmanage'
            }
            if(perms.includes('商品管理')){
              result=result||item.path =='/goods'
            }
            if(perms.includes('优惠券管理')){
              result=result||item.path =='/promotion'
            }
            if(perms.includes('系统管理')){
              result=result||item.path =='/sys'
            }
            if(perms.includes('推广管理')){
              result=result||item.path =='/recommend'
            }
            if(perms.includes('订单管理')){
              result=result||item.path =='/order'
            }
            if(perms.includes('统计')){
              result=result||item.path =='/stat'
            }
            return result
          })
        }
        commit('SET_ROUTERS', accessedRouters)
        resolve()
      })
    }
  }
}

export default permission
