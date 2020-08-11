import request from '@/utils/requestOther'
import axios from 'axios'

export function uploadDetailPic(data){
  return request({
    url: '/upload',
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

