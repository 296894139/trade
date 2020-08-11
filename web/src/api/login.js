import request from '@/utils/request'

export function loginByUsername(username, password,loginType) {

  return request({
    url: 'admin/login',
    method: 'get',
    params: { username, password }
  })
}

export function logout(id) {
  return request({
    url: 'admin/find/id',
    method: 'get',
    params: { id }
  })
}

export function getUserInfo(id) {
  return request({
    url: 'admin/find/id',
    method: 'get',
    params: { id }
  })
}

