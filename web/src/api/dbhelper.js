import request from '@/utils/request'

//------------------------------------------------------增删改查（开始）------------------------------------------------------/

/**************************************abb/查询所有(分页查询)************************************/
export function getAll(url,options){  
  var api = ''
  /*for(var item in options){
    let a = ""+options[item]
    if(a){
      api = api + "/" + options[item];
    }
  }*/
  return request({
    url:  url + api,
    method: 'get',
    params:options
  })
}


/**************************************abb/查询所有(分页查询)************************************/
export function getSearch(url,data){
  
  return request({
    url:  url,
    method: 'get',
    data
  })
}



/**************************************abb/POST传值************************************/
export function postData(url,data){
  return request({
    url:  url,
    method: 'post',
    data
  })
}

/**************************************abb/PUT修改************************************/
export function putData(url,data){
  return request({
    url:  url,
    method: 'put',
    data
  })
}

/**************************************add/DELETE删除************************************/
export function deleteData(table,id){
  return request({
    url:  table + "/" + id,
    method: 'post',
  })
}

/**************************************add根据id查询************************************/
export function getDataByID(table){
  return request({
    url: table ,
    method: 'get',
  })
}

/**************************************删除************************************/
export function deleteByID(table,data){
  return request({
    url:  table ,
    method: 'delete',
    data
  })
}

/**************************************批量删除************************************/
export function deleteAllByID(table,data){
  return request({
    url:  table + "/deleteByIds",
    method: 'put',
    data
  })
}
//------------------------------------------------------增删改查（结束）------------------------------------------------------/


export function upload() {
  var currentTimeStamp = new Date().getTime()/1000;
  if(this.uploadParams == null||(this.uploadParams.expire + 3 < currentTimeStamp)){
      this.$store.dispatch('GetUploadParams').then(req => {
          this.uploadParm = req.data
    }).catch(err => {
      this.$message({ message: err.message, type: 'warning' });
    })
  }else{
     this.uploadParm = this.uploadParams
  }
}

function S4() {
  return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
}
export function guid() {
  return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
}

export function bytesToSize(bytes) {
  if(bytes === 0) return '0 B';
  var k = 1024, // or 1024
    sizes = ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'],
    i = Math.floor(Math.log(bytes) / Math.log(k));

  return(bytes / Math.pow(k, i)).toPrecision(3) + ' ' + sizes[i];
}
