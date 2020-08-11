import request from '@/utils/request'
import axios from 'axios'
export function listStorage(query) {
  return request({
    url: '/storage/list',
    method: 'post',
    params: query
  })
}
export function uploadDetailPic(data){
  return request({
    url: '/storage/upload',
    method: 'post',
    data
  })

}

export function createStorage(data) {
  return request({
    url: '/storage/create',
    method: 'post',
    data
  })
}
/*
export function createStorageNew(data) {
  return request({
    url: '/storage/create',
    method: 'post',
    data
  })
}*/

export function readStorage(data) {
  return request({
    url: '/storage/read',
    method: 'get',
    data
  })
}

export function updateStorage(data) {
  return request({
    url: '/storage/update',
    method: 'post',
    data
  })
}

export function deleteStorage(data) {
  return request({
    url: '/storage/delete',
    method: 'post',
    data
  })
}

// const uploadPath = 'https://sandc.xyz:8889/wx/storage/upload'
//const filePath = 'https://sandc.xyz:8889/wx/storage/fetch'
const filePath = 'http://47.106.171.65'
const uploadPath = 'https://www.shaoshanlu.com:5010/upload'
// const filePath = '../backend/record'
// const uploadPath = 'http://localhost:5010/upload'
const filePathNew =  'https://sandc.xyz:8889/wx/storage/fetchNew'

//const filePathNew = 'http://localhost:8082/wx/storage/fetchNew'

//const uploadPath = 'http://localhost:8082/wx/storage/upload'
/*const uploadPath =
        process.env.NODE_ENV === 'production'
          ? 'http://139.199.222.72:8889/wx/storage/upload'
          : 'http://192.168.1.102:8082/wx/storage/upload'
const filePath =     process.env.NODE_ENV === 'production'
? 'http://139.199.222.72:8889/wx/storage/fetch'
: 'http://192.168.1.102:8082/wx/storage/fetch'
*/
export { uploadPath,filePath,filePathNew }
