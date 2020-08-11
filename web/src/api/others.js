/* eslint-disable */
import request from '@/utils/request'

//管理员查询门店申请列表
export function shoplist(query) {
  return request({
    url: '/AdminDistribution/list?CheckStatus=2',
    method: 'get',
    params: query,
    // paramsSerializer: function(params) {
    //   return Qs.stringify(params, { arrayFormat: 'repeat' })
    // }
  })
}

//商家查询门店申请列表
export function shoplistNew(query) {
  return request({
    url: '/AdminDistribution/getApplyDistInfo',
    method: 'get',
    params: query,
    // paramsSerializer: function(params) {
    //   return Qs.stringify(params, { arrayFormat: 'repeat' })
    // }
  })
}



export function deleteinfo(data) {
  return request({
    url: '/AdminDistribution/delete',
    method: 'delete',
    data
  })
}

export function refundOrder(data) {
  return request({
    url: '/order/refund',
    method: 'post',
    data
  })
}

export function replyComment(data) {
  return request({
    url: '/order/reply',
    method: 'post',
    data
  })
}


//商家查询订单的详情：
export function detailOrder(id) {
  return request({
    url: '/order/QueryShopOrder',
    method: 'get',
    params: { id }
  })
}
