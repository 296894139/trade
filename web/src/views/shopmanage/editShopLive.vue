<template>
  <div class="app-container">
    <el-card class="box-card">
      <h3>编辑直播</h3>
      <el-form :model="shop" label-width="150px">
				  <el-form-item label="直播间密码" prop="password">
          <el-input v-model="shop.password"/>
        </el-form-item>
				<!--
        <el-form-item label="行业分类" prop="industryClassify">
					<el-checkbox-group v-model="checkindust">
						<el-checkbox :label="1" >123</el-checkbox>
						<el-checkbox :label="2" >213</el-checkbox>
						<el-checkbox :label="3" >12qwe3</el-checkbox>
					</el-checkbox-group>
        </el-form-item>
				-->
        <el-form-item label="直播流名称" prop="liveName">
          <el-input v-model="shop.liveName"/>
        </el-form-item>
		
				<el-form-item label="分红介绍" prop="giftName">
				  <el-input v-model="shop.giftName"/>
				</el-form-item>
				<el-form-item label="礼物分成" prop="giftFencheng">
				  <el-input v-model="shop.giftFencheng"/>
				</el-form-item>
			<!--	<el-form-item label="链接地址" prop="link">
				  <el-input v-model="shop.link"/>
				</el-form-item>
        -->
				<el-form-item label="直播标题" prop="content">
				  <el-input v-model="shop.content"/>
				</el-form-item>
				<el-form-item label="直播间封面">
					<span class="tip-pic">只能上传jpg/png文件，只有1张，且不超过500kb</span>
          <el-upload
            :action="uploadPath"
            :show-file-list="false"
            :on-success="uploadPicUrl"
            class="avatar-uploader"
            accept=".jpg,.jpeg,.png,.gif">
            <img v-if="shop.url" :src="shop.url" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"/>
          </el-upload>
        </el-form-item>
		
		
			</el-form>
    </el-card>
    <div class="op-container">
      <el-button @click="handleCancel">取消</el-button>
      <el-button type="primary" @click="handlecreatshop">编辑直播间</el-button>
    </div>

  </div>
</template>

<script>
import {
  getAll,
  postData,
  getDataByID,
  putData,
  deleteData
} from '@/api/dbhelper'

import { createStorage, uploadPath,filePath } from '@/api/storage'
import Editor from '@tinymce/tinymce-vue'
import { MessageBox } from 'element-ui'
import { getToken } from '@/utils/auth'
import axios from 'axios';
export default {
  name: 'createShopLive',
  data() {
    return {
			shop:{
		    url:'',
      },
      uploadPath,
      filePath,
			//		start_time:'',
			checklist:[1],
			checkindust:[0]
    }
  },
  created() {

    this.init()
    
  },

  methods: {
    init: function() {
      	
      if (this.$route.query.id == null) {
        return
      }
     
        
        getDataByID("LiveStreaming/readliveStraeming?id="+this.$route.query.id).then(response => {
          this.shop = response.data.data;
        })
      
      
      console.log(this.$route.query.id,'当前id',   this.shop )
      postData("LiveStreaming/updateLive",this.shop).then(response => {
        console.log(6666);
        //this.categoryId = response.data.data.categoryList
      //  this.brandList = response.data.data.brandList
      })
    },
    handleCancel: function() {
     this.$router.push({ path: '/shopmanage/shopLive' })
    },
     uploadPicUrl: function(response) {
      this.shop.url =response.data.url.replace("http://localhost:8080/wx/storage/fetch",this.filePath)
      console.log(this.shop.url,7744)
    },
    handlecreatshop: function() {
      //  this.shop.url=55555;
      this.shop.id=this.$route.query.id
      		this.shop.advertising_icon=0;
			this.shop.liveStreaming=0;
			this.shop.panoramaStreaming=0;
			this.shop.homeLive = 0;
    //  this.shop.url = 0;
      

         postData("LiveStreaming/updateLive",this.shop).then(response => {
           	this.$router.push({ path: '/shopmanage/shopLive' })
      })
			
			
		
    /*  creatshop(allshop).then(response => {
        this.$notify.success({
          title: '成功',
          message: '创建成功'
        })
				this.$router.push({ path: 'shopmanage/shoplist' })
			
      }).catch(response => {
        MessageBox.alert('业务错误：' + response, '警告', {
          confirmButtonText: '确定',
          type: 'error'
        })
      })*/
    },
    handleClose(tag) {
      this.keywords.splice(this.keywords.indexOf(tag), 1)
      this.goods.keywords = this.keywords.toString()
    }
	}
}
</script>
<style>
	.tip-pic{
		color: #cacdd4;
		font-size: 13px;
	}
  
  .avatar-uploader .el-upload {
    width: 145px;
    height: 145px;
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }

  .avatar-uploader .el-upload:hover {
    border-color: #20a0ff;
  }

  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 120px;
    height: 120px;
    line-height: 120px;
    text-align: center;
  }
    .avatar {
    width: 145px;
    height: 145px;
    display: block;
  }
</style>

