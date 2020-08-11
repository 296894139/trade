/* eslint-disable */
import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

import Layout from '@/views/layout/Layout'

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
    path: '',
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
    path: '/dashboard',
    component: Layout,
    redirect: 'dashboard',
    children: [
      {
        path: '/dashboard',
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
          title: '员工管理',
          noCache: true
        }
      },
      {
        path: 'shareholder',
        component: () => import('@/views/user/shareholder'),
        name: 'shareholder',
        meta: {
          perms: ['GET /admin/user/list', 'POST /admin/user/create', 'POST /admin/user/update'],
          title: '股东管理',
          noCache: true
        }
      },
      {
        path: 'manager',
        component: () => import('@/views/user/manager'),
        name: 'manager',
        meta: {
          perms: ['GET /admin/user/list', 'POST /admin/user/create', 'POST /admin/user/update'],
          title: '庄主管理',
          noCache: true
        }
      }
    ]
  },

   //等级管理
   {
	  path: '/module',
	  component: Layout,
	  redirect: 'noredirect',
    alwaysShow: true,
    hidden:false,
	  name: 'module',
	  meta: {
	    title: '等级管理',
	    icon: 'chart'
	  },
	  children: [
	    {
	      path: 'levelmanage',
	      component: () => import('@/views/module/levelmanage'),
	      name: 'levelmanage',
	      meta: {
	        // perms: ['GET /admin/stat/user'],
	        title: '等级管理',
	        noCache: true
	      }
	    }
	  ]
  },
  {
	  path: '/shopmanage',
	  component: Layout,
	  redirect: 'noredirect',
	  alwaysShow: true,
	  name: 'shopmanage',
	  meta: {
	    title: '酒庄管理',
	    icon: 'chart'
	  },
	  children: [
	    {
	      path: 'shoplist',
	      component: () => import('@/views/shopmanage/shoplist'),
	      name: 'shoplist',
	      meta: {
	        title: '酒庄管理',
	        noCache: true
	      }
	    },
      {
        path: 'deduct',
        component: () => import('@/views/shopmanage/deduct'),
        name: 'deduct',
        meta: {
          title: '酒庄分成管理',
          noCache: true
        }
      },
      {
        path: 'credit',
        component: () => import('@/views/shopmanage/credit'),
        name: 'credit',
        meta: {
          title: '赊账管理',
          noCache: true
        }
      },
      {
        path: 'stock',
        component: () => import('@/views/shopmanage/stock'),
        name: 'stock',
        meta: {
          title: '进货请求',
          noCache: true
        }
      },
      {
        path: 'apply',
        component: () => import('@/views/shopmanage/apply'),
        name: 'apply',
        meta: {
          title: '报销明细',
          noCache: true
        }
      }

	  ],
  },
  {
    path: '/goods',
    component: Layout,
    redirect: 'noredirect',
    alwaysShow: true,
    name: 'goodsManage',
    meta: {
      title: '商品管理',
      icon: 'chart'
    },
    children: [
      {
        path: 'list',
        component: () => import('@/views/goods/list'),
        name: 'goodsList',
        meta: {
          perms: ['GET /admin/goods/list', 'POST /admin/goods/delete'],
          title: '热销产品列表',
          noCache: true
        }
      },
      {
        path: 'integralGoods',
        component: () => import('@/views/goods/integralGoods'),
        name: 'integralGoods',
        meta: {
          perms: ['GET /admin/goods/list', 'POST /admin/goods/delete'],
          title: '积分商品列表',
          noCache: true
        }
      },
      {
        path: 'recommendGoods',
        component: () => import('@/views/goods/recommendGoods'),
        name: 'recommendGoods',
        meta: {
          perms: ['GET /admin/goods/list', 'POST /admin/goods/delete'],
          title: '精品推荐商品列表',
          noCache: true
        }
      }
    ]
  },


  {
    path: '/promotion',
    component: Layout,
    redirect: 'noredirect',
    alwaysShow: true,
    name: 'promotionManage',
    meta: {
      title: '优惠券管理',
      icon: 'chart'
    },
    children: [
      {
        path: 'discount',
        component: () => import('@/views/promotion/discount'),
        name: 'discount',
        meta: {
          perms: ['GET /admin/coupon/list', 'POST /admin/coupon/create', 'POST /admin/coupon/update', 'POST /admin/coupon/delete'],
          title: '优惠券管理',
          noCache: true
        }
      },
      {
        path: 'coupon',
        component: () => import('@/views/promotion/coupon'),
        name: 'coupon',
        meta: {
          perms: ['GET /admin/coupon/list', 'POST /admin/coupon/create', 'POST /admin/coupon/update', 'POST /admin/coupon/delete'],
          title: '用户优惠卷管理',
          noCache: true
        }
      }
    ]
  },

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
      },
      {
        path: 'ad',
        component: () => import('@/views/sys/ad'),
        name: 'ad',
        meta: {
          perms: ['GET /admin/admin/list', 'POST /admin/admin/create', 'POST /admin/admin/update', 'POST /admin/admin/delete'],
          title: '广告位管理',
          noCache: true
        }
      },
      {
        path: 'culture',
        component: () => import('@/views/sys/culture'),
        name: 'culture',
        meta: {
          perms: ['GET /admin/admin/list', 'POST /admin/admin/create', 'POST /admin/admin/update', 'POST /admin/admin/delete'],
          title: '文化推广',
          noCache: true
        }
      },
      {
        path: 'complain',
        component: () => import('@/views/sys/complain'),
        name: 'complain',
        meta: {
          perms: ['GET /admin/admin/list', 'POST /admin/admin/create', 'POST /admin/admin/update', 'POST /admin/admin/delete'],
          title: '投诉建议列表',
          noCache: true
        }
      }
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
      {
        path: 'recommend',
        component: () => import('@/views/recommend/recommend'),
        name: 'recommend',
        meta: {
          // perms: ['GET /admin/stat/user'],
          title: '会员推广管理',
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
      title: '订单管理',
      icon: 'chart'
    },
    children: [
      {
        path: 'order',
        component: () => import('@/views/order/order'),
        name: 'order',
        meta: {
          // perms: ['GET /admin/stat/user'],
          title: '订单列表',
          noCache: true
        }
      }
    ]
  },

  {
    path: '/stat',
    component: Layout,
    redirect: 'noredirect',
    alwaysShow: true,
    name: 'statManage',
    meta: {
      title: '统计',
      icon: 'chart'
    },
    children: [
      {
        path: 'user',
        component: () => import('@/views/stat/user'),
        name: 'statUser',
        meta: {
          perms: ['GET /admin/stat/user'],
          title: '会员消费',
          noCache: true
        }
      },
      {
        path: 'order',
        component: () => import('@/views/stat/order'),
        name: 'statOrder',
        meta: {
          perms: ['GET /admin/stat/order'],
          title: '股东收入',
          noCache: true
        }
      },
      {
        path: 'goods',
        component: () => import('@/views/stat/goods'),
        name: 'statGoods',
        meta: {
          perms: ['GET /admin/stat/goods'],
          title: '酒庄收发存',
          noCache: true
        }
      }
    ]
  },







  { path: '*', redirect: '/404', hidden: true }
]
