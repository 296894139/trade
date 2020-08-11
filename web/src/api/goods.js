import request from '@/utils/request'
//查詢所有商家列表  超级管理员
export function listGoods(query) {
  return request({
    url: '/goods/QuerylistShop',
    method: 'get',
    params: query
  })
}
//查询当前商家   商家角色
export function listShopGoods(userId,query) {
  return request({
    url: '/goods/QueryShopGoodsList/'+userId,
    method: 'get',
    params: query
  })
}


export function deleteGoods(data) {
  return request({
    url: '/goods/deleteShopGoods',
    method: 'post',
    data
  })
}

export function publishGoods(data) {
  return request({
    url: '/goods/createGoods',
    method: 'post',
    data
  })
}

export function detailGoods(id) {
  return request({
    url: '/goods/detailGoodS',
    method: 'get',
    params: { id }
  })
}

export function editGoods(data) {
  return request({
    url: '/goods/updateShopGoods',
    method: 'post',
    data
  })
}

export function listCatAndBrand() {
  return request({
    url: '/goods/catAndBrand',
    method: 'get'
  })
}
