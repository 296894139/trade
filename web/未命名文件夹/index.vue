<template>
  <div class="login-container">
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" auto-complete="on" label-position="left">
      <div class="title-container">
        <h3 class="title">管理员登录</h3>
      </div>
      <el-form-item prop="username">
        <span class="svg-container svg-container_login">
          <svg-icon icon-class="user" />
        </span>
        <el-input v-model="loginForm.username" name="username" type="text" auto-complete="on" placeholder="username" style="color:white!important;"/>
      </el-form-item>
   v
      <el-form-item prop="password">
        <span class="svg-container">
          <svg-icon icon-class="password" />
        </span>
        <el-input :type="passwordType" v-model="loginForm.password" name="password" auto-complete="on" placeholder="password" @keyup.enter.native="handleLogin" />
        <span class="show-pwd" @click="showPwd">
          <svg-icon icon-class="eye" />
        </span>
      </el-form-item>

      <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:30px;" @click.native.prevent="handleLogin">登录</el-button>

      	  <!--注册dialog-->
        	<el-dialog :visible.sync="registerVisible" class='register'  :modal = "false" title="注册">

				  <el-form v-model="registerData" ref="registerData" class="table-expand">

				<el-form-item label-width="120px" label="账号" prop="username" >
					<el-input  style="background-color:white"  v-model="registerData.username"></el-input>
				</el-form-item>
         <el-form-item label-width="120px" label="密码"  prop="password">
        <el-input :type="passwordType" name="password"   style="background-color:white" v-model="registerData.password" />
      </el-form-item>

       <el-form-item label-width="120px" label="店铺名称" prop="shopname">
        <el-input v-model="registerData.shopname"   style="background-color:white"/>
      </el-form-item>
       <el-form-item label-width="120px" label="客服电话" prop="serviceMobile">
        <el-input v-model="registerData.serviceMobile"   style="background-color:white" />
      </el-form-item>
        <el-form-item label="所属区域" prop="region">
					   <v-distpicker :province="registerData.province" :city="registerData.city" :area="registerData.area"
              @selected="onSelected" @province="onChangeProvince" @city="onChangeCity" @area="onChangeArea" >
              </v-distpicker>

					<!--<el-input v-model="shop.region"/>-->
        </el-form-item>
         <el-form-item label-width="120px" label="详细地址"  prop="address">
        <el-input v-model="registerData.address"    style="background-color:white"/>
      </el-form-item>
      <el-form-item label="银行卡账号" prop="workTime" label-width="120px">
        <el-input v-model="registerData.workTime"   style="background-color:white" />
      </el-form-item>
      				<el-form-item label="入驻资质" label-width="120px">
				</el-form-item>
<el-upload
        :action="uploadPath"
        list-type="picture-card"
        :show-file-list="true"
        :on-success="uploadWorkImg"
        :on-remove="handleRemove" style="margin-left:10px;margin-top:10px">
        <i class="el-icon-plus"></i>
      </el-upload>

          				<el-form-item label="法人手持身份证" label-width="120px">
				</el-form-item>
<el-upload
        :action="uploadPath"
        list-type="picture-card"
        :show-file-list="true"
        :on-success="uploadCardWithManUrl"
        :on-remove="handleRemove" style="margin-left:10px;margin-top:10px">
        <i class="el-icon-plus"></i>
      </el-upload>

           				<el-form-item label="代理授权书" label-width="120px">
				</el-form-item>
<el-upload
        :action="uploadPath"
        list-type="picture-card"
        :show-file-list="true"
        :on-success="uploadproAuthorizationUrl"
        :on-remove="handleRemove" style="margin-left:10px;margin-top:10px">
        <i class="el-icon-plus"></i>
      </el-upload>
      <!--  <el-form-item :label-width="formLabelWidths" v-for="(main, index) in ProductFrom.fabric" :key="index"> -->
      				<el-form-item label="资质类型" v-for="(item,index) in qualificationArr" :key="index"  label-width="120px">
                <el-select  placeholder="请选择" v-model="item.name">
                    <el-option
      v-for="item in options"
      :key="item.id"
      :label="item.name"
      :value="item.name">
    </el-option>
  </el-select>
      <div @click="getImageTypeIndex(index)">
        <el-upload
        :action="uploadPath"
        list-type="picture-card"
        :data="item"
        :show-file-list="true"
        :before-upload="beforeUpload"

        :on-success="uploadPicUrl"
        :on-remove="handleRemove" style="margin-left:10px;margin-top:10px">
        <i class="el-icon-plus"></i>
      </el-upload>
      </div>
          <el-button type="primary" size="mini" @click="addDomain(item)">新增资质</el-button>
           <el-button type="primary" size="mini"  v-if="index != 0" @click="removeDomain(item)">减少资质</el-button>

				</el-form-item>






					  <el-button @click="registerVisible=false">取消</el-button>
           <el-button type="primary" @click="handleSumbit">确定</el-button>


				  </el-form>
			</el-dialog>

    </el-form>

  </div>
</template>

<script>
import request from '@/utils/request'
import VDistpicker from 'v-distpicker'
import axios from 'axios'
import * as config from '../../../config'
import { MessageBox } from 'element-ui'
import { createStorage, uploadPath,filePath } from '@/api/storage'
import {
  getAll,
  postData,
  getDataByID,
  putData,
  deleteData
} from '@/api/dbhelper'
export default {
  name: 'Login',
  data() {

    const validateUsername = (rule, value, callback) => {
      if (validateUsername == null) {
        callback(new Error('请输入正确的管理员用户名'))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error('管理员密码长度应大于6'))
      } else {
        callback()
      }
    }
    return {
      selectData:'',
      province:'',
      city:'',
      area:'',
       qualificationArr:[

       ],
       uploadImageType:'',//上传图片时的下标

       options: [{
          value: '选项1',
          label: '黄金糕'
        }],
        value: '',
      uploadPath,
      filePath,//文件位置
      registerData:{
        username2:'',
        shopname:'',
        password:'',
        proAuthorization:[],
        cardWithMan:[],
        region:[],


      },
       registerVisible:false,
      loginForm: {
        username: 'admin123',
        password: 'admin123'
      },
      loginRules: {
        username: [{ required: true, trigger: 'blur', validator: validateUsername }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }]
      },

      passwordType: 'password',
      loading: false,
      loading2:false
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }

  },
  	 components: {
    VDistpicker
  },
  created() {

    // window.addEventListener('hashchange', this.afterQRScan)
  },
  destroyed() {
    // window.removeEventListener('hashchange', this.afterQRScan)
  },
  methods: {
      onSelected(data) {
			//alert(data.province + ' | ' + data.city + ' | ' + data.area)
			this.selectData = data
      console.log(data,844)
		},
		onChangeProvince(a){
			this.province = a.value;
			  console.log(a.value,911)
		},
		onChangeCity(a){
			this.city = a.value
  console.log(a,922)
		},
		onChangeArea(a){
			this.area = a.value
  console.log(a,933)
		},
    getImageTypeIndex:function (index) {
    this.uploadImageType = index
    console.log(index,'当前')
    },
    //成功
    uploadPicUrl: function(response) {


      console.log(response,'成功',response.data.url.split(':')[2], this.qualificationArr)
       this.qualificationArr[this.uploadImageType].picUrl.push(   response.data.url.replace("http://localhost:8080/wx/storage/fetch",this.filePath));
    },

    //法人手持身份证
    uploadCardWithManUrl:function(response){
      var result =      response.data.url.replace("http://localhost:8080/wx/storage/fetch",this.filePath)
      this.registerData.cardWithMan.push(result);
    },
    //代理授权书
    uploadproAuthorizationUrl:function(response){
        var result =      response.data.url.replace("http://localhost:8080/wx/storage/fetch",this.filePath)

      this.registerData.proAuthorization.push(result);
    },
    //入驻资质
    uploadWorkImg:function(response){
           var result =      response.data.url.replace("http://localhost:8080/wx/storage/fetch",this.filePath)

          this.registerData.workimgUrl = result
    },
    //上传之前
    beforeUpload(file){
      console.log(file,7144)
    },
    //新增资质
    addDomain(item){
      this.qualificationArr.push({name:'',picUrl:[]});
      console.log( this.qualificationArr,78944)
    },
    removeDomain(item){
        var index = this.qualificationArr.indexOf(item)
        if(index !== -1) {
          this.qualificationArr.splice(index, 1)
        }
    },
    handleRemove: function(response) {
    },
    //注册
    handleRegister(){
      this.registerVisible = true
      console.log('register11')
     // this.redirect = '/profile/register'
         //  this.$router.push({ path: this.redirect || '/' })
          //this.$router.push({ path: 'register' })
     //   this.$router.push({ path: 'broadcast' })
    },
    //获取所有资质信息
    getAllQualification(){
       getAll("qualification/listByAdmin").then(response => {
         console.log(response,98444)
         if(response.data.errno==0){
           this.options = response.data.data.items
           this.qualificationArr.push({value:this.options[0].name,picUrl:[]});
         }

       });

    },
    //上传前检验文件是否上传
    validateUpload(){
      //入驻资质
       if(this.registerData.workimgUrl==''||this.registerData.workimgUrl==undefined){
           this.$notify.error({
            title: '注册失败',
            message: '请上传入驻资质'
          })
          return false;
      }
      // 法人手持身份证
     /* if(this.registerData.cardWithManUrl.length==0){
          this.$notify.error({
            title: '注册失败',
            message: '请上传手持身份证'
          })
          return false;
      }
          // 代理授权书
      if(this.registerData.proAuthorization.length==0){
          this.$notify.error({
            title: '注册失败',
            message: '请上传代理授权书'
          })
          return false;
      }*/
      return true;
    },


    handleSumbit(){  // admin/shop/InputShop
    console.log(this.qualificationArr,99);
    if(!this.validateUpload()){
        return;
    }
        this.registerData.url ='';
        this.registerData.storefrontimgUrl = '';

        	if(this.area==''||this.area==undefined){
				 MessageBox.alert('请补全地址信息', '警告', {
            confirmButtonText: '确定',
            type: 'error'
					})
								return ;
      }
            this.registerData.length = 0;
             this.registerData.region.push(this.province); // = this.province+','+this.city+','+this.area;
             this.registerData.region.push(this.city);
             this.registerData.region.push(this.area);
             //刚注册是 为普通
             this.registerData.level = -1;
             console.log(this.registerData,7844441)
             //return;
        postData("shop/InputShop",this.registerData).then(response => {
           if(response.data.errno==0){
                  this.$notify.success({
                  title: '注册成功',
            }
            )
            this.registerVisible = false;
            this.createQualification(response.data.data.id);
           }
      }).catch(response => {
          this.$notify.error({
            title: '注册失败',
            message: response.data.errmsg
          })
        })
    },
    //保存资质信息
    createQualification(shopId){
      console.log(shopId,98744)
      for(var i=0;i<this.qualificationArr.length;i++ ){
        this.qualificationArr[i].shopId=shopId
      }
      postData("qualification/create",this.qualificationArr).then(response=>{

      })

    },
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid && !this.loading) {
          this.loading = true
          axios({
            method: 'get',
            url: config.baseApi + "admin/login/?username="+ this.loginForm.username+"&password="+ this.loginForm.password,
          }).then(res => {

            if(res.data.data.items){
              this.loading = false
              this.$router.push({ path: '/dashboard' })
              console.log(res.data.data.items)
            }
            else{
            }
          }).catch(error => {
            this.$notify.error({
              title: '失败',
              message: '用户名不存在或密码错误'
            })
            this.loading = false
          });
        } else {
          return false
        }
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
$bg:#2d3a4b;
$light_gray:#eee;

/* reset element-ui css */


.login-container /deep/ .el-button+.el-button{
  margin-left: 0px;
}
.login-container {
  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;
    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;

      height: 47px;
      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: #fff !important;
      }
    }
  }
  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
  }
}
</style>

<style rel="stylesheet/scss" lang="scss" scoped>
$bg:#2d3a4b;
$dark_gray:#889aa4;
$light_gray:#eee;
//$light_gray:#2d3a4b;;
/*.el-form-item{
      background-color: rgba(0,0,0,.1)
}
.login-container .el-input input{
  color:#303133!important
}*/
.login-container {
  position: fixed;
  height: 100%;
  width: 100%;
  background-color: $bg;
  .login-form {
    position: absolute;
    left: 0;
    right: 0;
    width: 520px;
    padding: 35px 35px 15px 35px;
    margin: 120px auto;
  }
  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;
    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }
  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
    &_login {
      font-size: 20px;
    }
  }
  .title-container {
    position: relative;
    .title {
      font-size: 26px;
      font-weight: 400;
      color: $light_gray;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
  }
  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }
}
</style>
