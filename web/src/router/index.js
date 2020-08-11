/* eslint-disable */
import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/views/layout/Layout'

/** note: Submenu only appear when children.length>=1
 *  detail see  https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 **/

/**
* hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
* alwaysShow: true               if set true, will always show the root menu, whatever its child routes length
*                                if not set alwaysShow, only more than one route under the children
*                                it will becomes nested mode, otherwise not show the root menu
* redirect: noredirect           if `redirect:noredirect` will no redirect in the breadcrumb
* name:'router-name'             the name is used by <keep-alive> (must set!!!)
* meta : {
    perms: ['GET /aaa','POST /bbb']     will control the page perms (you can set multiple perms)
    title: 'title'               the name show in submenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar,
    noCache: true                if true ,the page will no be cached(default is false)
  }
**/
export const constantRouterMap = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path*',
        component: () => import('@/views/redirect/index')
      }
    ]
  },
  {
    path: '/broadcast',
    component: () => import('@/views/broadcast/manage'),
    hidden: true
  },
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/auth-redirect',
    component: () => import('@/views/login/authredirect'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/errorPage/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/errorPage/401'),
    hidden: true
  },
  {
    path: '',
    component: Layout,
    redirect: 'dashboard',
    children: [
      {
        path: 'dashboard',
        component: () => import('@/views/dashboard/index'),
        name: 'Dashboard',
        meta: { title: 'dashboard', icon: 'dashboard', noCache: true }
      }
    ]
  }
]

export default new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})

export const asyncRouterMap = [
  {
    path: '/user',
    component: Layout,
    redirect: 'noredirect',
    alwaysShow: true,
    name: 'userManage',
    meta: {
      title: '人员管理',
      icon: 'chart'
    },
    children: [
      {
        path: 'member',
        component: () => import('@/views/user/member'),
        name: 'member',
        meta: {
          perms: ['GET /admin/user/list', 'POST /admin/user/create', 'POST /admin/user/update'],
          title: '会员管理',
          noCache: true
        }
      },
      {
        path: 'staff',
        component: () => import('@/views/user/staff'),
        name: 'staff',
        meta: {
          perms: ['GET /admin/user/list', 'POST /admin/user/create', 'POST /admin/user/update'],
          title: '企业管理',
          noCache: true
        }
      }
      // {
      //   path: 'shareholder',
      //   component: () => import('@/views/user/shareholder'),
      //   name: 'shareholder',
      //   meta: {
      //     perms: ['GET /admin/user/list', 'POST /admin/user/create', 'POST /admin/user/update'],
      //     title: '股东管理',
      //     noCache: true
      //   }
      // },
      // {
      //   path: 'manager',
      //   component: () => import('@/views/user/manager'),
      //   name: 'manager',
      //   meta: {
      //     perms: ['GET /admin/user/list', 'POST /admin/user/create', 'POST /admin/user/update'],
      //     title: '庄主管理',
      //     noCache: true
      //   }
      // },
      // {
      //   path: 'levelmanage',
      //   component: () => import('@/views/module/levelmanage'),
      //   name: 'levelmanage',
      //   meta: {
      //     // perms: ['GET /admin/stat/user'],
      //     title: '等级管理',
      //     noCache: true
      //   }
      // }
    ]
  },


  {
	  path: '/shopmanage',
	  component: Layout,
	  redirect: 'noredirect',
	  alwaysShow: true,
	  name: 'shopmanage',
	  meta: {
	    title: '行业分类管理',
	    icon: 'chart'
	  },
	  children: [
	    {
	      path: 'shoplist',
	      component: () => import('@/views/shopmanage/shoplist'),
	      name: 'shoplist',
	      meta: {
	        title: '行业分类管理',
	        noCache: true
	      }
	    },
      // {
      //   path: 'deduct',
      //   component: () => import('@/views/shopmanage/deduct'),
      //   name: 'deduct',
      //   meta: {
      //     title: '酒庄分成管理',
      //     noCache: true
      //   }
      // },
      // {
      //   path: 'credit',
      //   component: () => import('@/views/shopmanage/credit'),
      //   name: 'credit',
      //   meta: {
      //     title: '赊账管理',
      //     noCache: true
      //   }
      // },
      // {
      //   path: 'stock',
      //   component: () => import('@/views/shopmanage/stock'),
      //   name: 'stock',
      //   meta: {
      //     title: '进货请求',
      //     noCache: true
      //   }
      // },
      // {
      //   path: 'apply',
      //   component: () => import('@/views/shopmanage/apply'),
      //   name: 'apply',
      //   meta: {
      //     title: '报销明细',
      //     noCache: true
      //   }
      // }
	  ],
  },
  {
    path: '/goods',
    component: Layout,
    redirect: 'noredirect',
    alwaysShow: true,
    name: 'goodsManage',
    meta: {
      title: '信息管理',
      icon: 'chart'
    },
    children: [
      {
        path: 'list',
        component: () => import('@/views/goods/list'),
        name: 'goodsList',
        meta: {
          perms: ['GET /admin/goods/list', 'POST /admin/goods/delete'],
          title: '需求信息管理',
          noCache: true
        }
      },
      // {
      //   path: 'integralGoods',
      //   component: () => import('@/views/goods/integralGoods'),
      //   name: 'integralGoods',
      //   meta: {
      //     perms: ['GET /admin/goods/list', 'POST /admin/goods/delete'],
      //     title: '积分商品列表',
      //     noCache: true
      //   }
      // },
      {
        path: 'recommendGoods',
        component: () => import('@/views/goods/recommendGoods'),
        name: 'recommendGoods',
        meta: {
          perms: ['GET /admin/goods/list', 'POST /admin/goods/delete'],
          title: '新闻信息管理',
          noCache: true
        }
      }
    ]
  },

  // {
  //   path: '/promotion',
  //   component: Layout,
  //   redirect: 'noredirect',
  //   alwaysShow: true,
  //   name: 'promotionManage',
  //   meta: {
  //     title: '优惠券管理',
  //     icon: 'chart'
  //   },
  //   children: [
  //     {
  //       path: 'discount',
  //       component: () => import('@/views/promotion/discount'),
  //       name: 'discount',
  //       meta: {
  //         perms: ['GET /admin/coupon/list', 'POST /admin/coupon/create', 'POST /admin/coupon/update', 'POST /admin/coupon/delete'],
  //         title: '优惠券管理',
  //         noCache: true
  //       }
  //     },
  //     {
  //       path: 'coupon',
  //       component: () => import('@/views/promotion/coupon'),
  //       name: 'coupon',
  //       meta: {
  //         perms: ['GET /admin/coupon/list', 'POST /admin/coupon/create', 'POST /admin/coupon/update', 'POST /admin/coupon/delete'],
  //         title: '用户优惠卷管理',
  //         noCache: true
  //       }
  //     },
  //   ]
  // },

  {
    path: '/sys',
    component: Layout,
    redirect: 'noredirect',
    alwaysShow: true,
    name: 'sysManage',
    meta: {
      title: '系统管理',
      icon: 'chart'
    },
    children: [
      {
        path: 'admin',
        component: () => import('@/views/sys/admin'),
        name: 'admin',
        meta: {
          perms: ['GET /admin/admin/list', 'POST /admin/admin/create', 'POST /admin/admin/update', 'POST /admin/admin/delete'],
          title: '管理员',
          noCache: true
        }
      }
      // {
      //   path: 'ad',
      //   component: () => import('@/views/sys/ad'),
      //   name: 'ad',
      //   meta: {
      //     perms: ['GET /admin/admin/list', 'POST /admin/admin/create', 'POST /admin/admin/update', 'POST /admin/admin/delete'],
      //     title: '广告位管理',
      //     noCache: true
      //   }
      // },
      // {
      //   path: 'complain',
      //   component: () => import('@/views/sys/complain'),
      //   name: 'complain',
      //   meta: {
      //     perms: ['GET /admin/admin/list', 'POST /admin/admin/create', 'POST /admin/admin/update', 'POST /admin/admin/delete'],
      //     title: '投诉建议列表',
      //     noCache: true
      //   }
      // }
    ]
  },

  {
    path: '/recommend',
    component: Layout,
    redirect: 'noredirect',
    alwaysShow: true,
    hidden:false,
    name: 'order',
    meta: {
      title: '推广管理',
      icon: 'chart'
    },
    children: [
      // {
      //   path: 'culture',
      //   component: () => import('@/views/sys/culture'),
      //   name: 'culture',
      //   meta: {
      //     perms: ['GET /admin/admin/list', 'POST /admin/admin/create', 'POST /admin/admin/update', 'POST /admin/admin/delete'],
      //     title: '文化推广',
      //     noCache: true
      //   }
      // },
      {
        path: 'recommend',
        component: () => import('@/views/recommend/recommend'),
        name: 'recommend',
        meta: {
          // perms: ['GET /admin/stat/user'],
          title: '宣传信息管理',
          noCache: true
        }
      }
    ]
  },
  {
    path: '/order',
    component: Layout,
    redirect: 'noredirect',
    alwaysShow: true,
    hidden:false,
    name: 'order',
    meta: {
      title: '活动管理',
      icon: 'chart'
    },
    children: [
      {
        path: 'order',
        component: () => import('@/views/order/order'),
        name: 'order',
        meta: {
          // perms: ['GET /admin/stat/user'],
          title: '活动列表',
          noCache: true
        }
      }
    ]
  },

  {
    path: '/feed',
    component: Layout,
    redirect: 'noredirect',
    alwaysShow: true,
    hidden:false,
    name: 'feed',
    meta: {
      title: '通知管理',
      icon: 'chart'
    },
    children: [
      {
        path: 'feed',
        component: () => import('@/views/feed/feed'),
        name: 'feed',
        meta: {
          // perms: ['GET /admin/stat/user'],
          title: '通知列表',
          noCache: true
        }
      }
    ]
  },

  // {
  //   path: '/stat',
  //   component: Layout,
  //   redirect: 'noredirect',
  //   alwaysShow: true,
  //   name: 'statManage',
  //   meta: {
  //     title: '统计',
  //     icon: 'chart'
  //   },
  //   children: [
  //     {
  //       path: 'user',
  //       component: () => import('@/views/stat/user'),
  //       name: 'statUser',
  //       meta: {
  //         perms: ['GET /admin/stat/user'],
  //         title: '会员消费',
  //         noCache: true
  //       }
  //     }
  //   ]
  // },

  { path: '*', redirect: '/404', hidden: true }
]
