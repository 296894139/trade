/* eslint-disable */
<template>
  <div class="app-container">

    <el-card class="box-card">
      <h3>商家编辑</h3>
      <el-form :model="shop" label-width="150px">
        <!-- <el-form-item label="行业分类" prop="industryClassify">
      		<el-checkbox-group v-model="checkindust">
      			<el-checkbox :label="1" >123</el-checkbox>
      			<el-checkbox :label="2" >213</el-checkbox>
      			<el-checkbox :label="3" >12qwe3</el-checkbox>
      		</el-checkbox-group>
        </el-form-item> -->
				<el-form-item label="行业分类">
				  <el-cascader :options="industryClassify" expand-trigger="hover" v-model="categoryIds" @change="handleCategoryChange"
					placeholder="请选择行业类别"/>
				</el-form-item>
        <el-form-item label="所属区域" prop="region">
					   <v-distpicker :province="formData.province" :city="formData.city" :area="formData.area" @selected="onSelected" @province="onChangeProvince" @city="onChangeCity" @area="onChangeArea" ></v-distpicker>
       
					<!--<el-input v-model="shop.region"/>-->
        </el-form-item>
      <!--	<el-form-item label="店内设施" prop="shopFacility">
      			<el-checkbox-group v-model="checklist">
      				<el-checkbox :label="1" >123</el-checkbox>
      				<el-checkbox :label="2" >213</el-checkbox>
      				<el-checkbox :label="3" >12qwe3</el-checkbox>
      			</el-checkbox-group>
      	</el-form-item>
		  -->
      	<el-form-item label="详细地址" prop="address">
      	  <el-input v-model="shop.address"/>
      	</el-form-item>
				<el-form-item label="logo">
				  <el-upload
				    :headers="headers"
				    :action="uploadPath"
				    :show-file-list="false"
				    :on-success="uploadPicUrla"
				    class="avatar-uploader"
				    accept=".jpg,.jpeg,.png,.gif">
				    <img v-if="shop.url" :src="shop.url" class="avatar">
				    <i v-else class="el-icon-plus avatar-uploader-icon"/>
				  </el-upload>
				</el-form-item>
      	<el-form-item label="店铺名称" prop="shopname">
      	  <el-input v-model="shop.shopname"/>
      	</el-form-item>
      	<el-form-item label="负责人名字" prop="username">
      	  <el-input v-model="shop.username"/>
      	</el-form-item>
      	<el-form-item label="门店介绍" prop="shopIntroduce">
      	  <el-input v-model="shop.shopIntroduce"/>
      	</el-form-item>
      	<el-form-item label="客服电话" prop="serviceMobile">
      	  <el-input v-model="shop.serviceMobile"/>
      	</el-form-item>
      	<el-form-item label="营业时间" prop="workTime">
      	  <el-input v-model="shop.workTime"/>
      	</el-form-item>

				<el-form-item label="入驻资质">
				  <el-upload
				    :headers="headers"
				    :action="uploadPath"
				    :show-file-list="false"
				    :on-success="uploadPicUrlb"
				    class="avatar-uploader"
				    accept=".jpg,.jpeg,.png,.gif">
				    <img v-if="shop.workimgUrl" :src="shop.workimgUrl" class="avatar">
				    <i v-else class="el-icon-plus avatar-uploader-icon"/>
				  </el-upload>
				</el-form-item>

				<el-form-item label="门头图片">
				  <el-upload
				    :headers="headers"
				    :action="uploadPath"
				    :show-file-list="false"
				    :on-success="uploadPicUrlc"
				    class="avatar-uploader"
				    accept=".jpg,.jpeg,.png,.gif">
				    <img v-if="shop.storefrontimgUrl" :src="shop.storefrontimgUrl" class="avatar">
				    <i v-else class="el-icon-plus avatar-uploader-icon"/>
				  </el-upload>
				</el-form-item>
      	<el-form-item label="密码" prop="password">
      	  <el-input v-model="password" placeholder="请输入密码至少6位数"/>
      	</el-form-item>
				<!--
				     <el-form-item   label="确认密码"  prop="repassword">
        <el-input :type="passwordType"  placeholder="请再次输入您的密码" name="password" v-model="shop.repassword" />
      </el-form-item>
			-->
				<el-form-item label="头像">
				  <el-upload
				    :headers="headers"
				    :action="uploadPath"
				    :show-file-list="false"
				    :on-success="uploadPicUrld"
				    class="avatar-uploader"
				    accept=".jpg,.jpeg,.png,.gif">
				    <img v-if="shop.avatar" :src="shop.avatar" class="avatar">
				    <i v-else class="el-icon-plus avatar-uploader-icon"/>
				  </el-upload>
				</el-form-item>

	 

        <el-form-item label="法人手持身份证">
				
	<el-upload
        :action="uploadPath"
	 :file-list="fileList"
        list-type="picture-card"
        :show-file-list="true"
        :on-success="uploadCardWithManUrl"
        :on-remove="handleRemove" style="margin-left:10px;margin-top:10px">
        <i class="el-icon-plus"></i>
      </el-upload>
	  </el-form-item>

    <el-form-item label="代理授权书" >
		<el-upload
        :action="uploadPath"
		 :file-list="fileListPro"
		
        list-type="picture-card"
        :show-file-list="true"
        :on-success="uploadproAuthorizationUrl"
        :on-remove="handleRemove" style="margin-left:10px;margin-top:10px">
        <i class="el-icon-plus"></i>
      </el-upload>
	  	</el-form-item>
      <!--  <el-form-item :label-width="formLabelWidths" v-for="(main, index) in ProductFrom.fabric" :key="index"> -->
      				<el-form-item label="资质类型" v-for="(item,index) in qualificationArr" :key="index" >
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
			 :file-list="item.fileList"
		   
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
  
    

      </el-form>
			<div class="op-container">
			  <el-button @click="handleCancel">取消</el-button>
			  <el-button type="primary" @click="handleEdit">保存</el-button>
			</div>
    </el-card>
  </div>
</template>

<script>
import {
  getAll,
  postData,
  getDataByID,
  putData,
	deleteData,
	getSearch
} from '@/api/dbhelper'
import VDistpicker from 'v-distpicker'
import { detailshop, editshop } from '@/api/shop'
import { listCategory, listCatL1, createCategory, updateCategory, deleteCategory } from '@/api/category'
import { createStorage, uploadPath,filePath } from '@/api/storage'
import Editor from '@tinymce/tinymce-vue'
import { MessageBox } from 'element-ui'
import { getToken } from '@/utils/auth'
import { mapGetters } from "vuex";
import { all } from 'q';


export default {
  name: 'modiinfo',
  data() {
		const validatePassCheck = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.shop.password) {
        callback(new Error('两次输入的密码不一样'));
      } else {
        callback();
      }
    };
    return {
			passwordType: 'password',
			qualificationArr:[],
			fileList:[],//法人手持身份证
			fileListPro:[],//代理授权书
			uploadImageType:'',//上传图片时的下标
			password:'',
			province:'',
			city:'',
			area:'',
			uploadPath,
			filePath,
			shop:{
				"cardWithMan":[]
			},
			shopId:'',
      checklist:[],
      checkindust:[],
			categoryIds: [],
			selectData:{},//选中的数据
			formData: {
        province: '',
        city: '',
        area: ''
      },
			listQuery:{
				page:1,
				limit:1000
			},
			 repassword: [
          { validator: validatePassCheck, trigger: 'blur' }
        ],
			industryClassify:[
				{
					value:'生活服务',label: '生活服务',
					children: [
						{value: '0',label: '旅行社'},{value: '1',label: '培训'},{value: '2',label: '宠物'},{value: '3',label: '齿科'},
						{value: '4',label: '快照'},{value: '5',label: '冲印'},{value: '6',label: '家政'},{value: '7',label: '婚纱摄影'},
						{value: '8',label: '婚庆服务'},{value: '9',label: '儿童摄影'},{value: '10',label: '汽车服务'},{value: '11',label: '丽人'},
						{value: '12',label: '更多生活服务'}
					],
				},
				{
					value:'购物',label: '购物',
					children: [
						{value: '0',label: '综合商场'},{value: '1',label: '食品茶酒'},{value: '2',label: '服饰鞋包'},{value: '3',label: '珠宝饰品'},
						{value: '4',label: '化妆品'},{value: '5',label: '运动户外'},{value: '6',label: '母婴儿童'},{value: '7',label: '数码家电'},
						{value: '8',label: '家具家居'},{value: '9',label: '书店'},{value: '10',label: '眼镜店'},{value: '11',label: '药店'},
						{value: '12',label: '超市'},{value: '13',label: '便利店'},{value: '14',label: '其他'}
					],
				},
				{
					value:'运动健身',label: '运动健身',
					children: [
						{value: '0',label: '健身中心'},{value: '1',label: '游泳馆'},{value: '2',label: '羽毛球馆'},{value: '3',label: '瑜伽'},
						{value: '4',label: '舞蹈'},{value: '5',label: '篮球场'},{value: '6',label: '网球场'},{value: '7',label: '足球场'},
						{value: '8',label: '高尔夫场'},{value: '9',label: '保龄球馆'},{value: '10',label: '乒乓球馆'},{value: '11',label: '体育场馆'},
						{value: '12',label: '更多运动场馆'}
					],
				},
				{
					value:'运动健身',label: '运动健身',
					children: [
						{value: '0',label: '川菜'},{value: '1',label: '粤菜'},{value: '2',label: '茶馆'},{value: '3',label: '火锅'},
						{value: '4',label: '烧烤'},{value: '5',label: '海鲜'},{value: '6',label: '特色小吃'},{value: '7',label: '日韩料理'},
						{value: '8',label: '西餐'},{value: '9',label: '自助餐'},{value: '10',label: '东南亚菜'},{value: '11',label: '面包甜点'},
						{value: '12',label: '其他'}
					],
				},
				{
					value:'休闲娱乐',label: '休闲娱乐',
					children: [
						{value: '0',label: '咖啡厅'},{value: '1',label: '酒吧'},{value: '2',label: '茶馆'},{value: '3',label: 'KTV'},
						{value: '4',label: '电影院'},{value: '5',label: '文化艺术'},{value: '6',label: '景点、郊游'},{value: '7',label: '公园'},
						{value: '8',label: '足疗按摩'},{value: '9',label: '洗浴'},{value: '10',label: '游乐游艺'},{value: '11',label: '台球馆'},
						{value: '12',label: '桌面游戏'},{value: '13',label: '更多休闲娱乐'}
					],
				},
					{
					value:'服装',label: '服装',
					children: [
						{value: '0',label: '女装'},{value: '1',label: '男装'}
					],
				}
			],
		}
	},
	 components: {
    VDistpicker
  },

  created() {
		this.shopId = this.$route.query.id;
		this.init()
		this.getAllCategoryList();
	
  },
    computed: {
				//  ...mapGetters(["shopId"]),
    headers() {
      return {
        'X-Litemall-Admin-Token': getToken()
      }
    }
  },
  methods: {
    init: function() {
			console.log(this.$route.query.id ,'idid',	this.shopId)
      if (this.$route.query.id == null) {
				
        return
      }
		//this.shopId= this.$route.query.id
		this.getShopInfo();
		this.getAllQualification();//获取所有资质
		this.getShopQualification();//获取商家资质

	

		},
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
		    //上传之前
    beforeUpload(file){
      console.log(file,7144)
    },
		   getImageTypeIndex:function (index) {
			this.uploadImageType = index 
			console.log(index,'当前')
			},
					//获取商家个人信息
			getShopInfo(){
				getAll("shop/readOnlyShop?id="+this.shopId).then(response => {
					if(response.data.errno==0){
						
						this.shop = response.data.data;
						/*for(var i=){

						}*/
						this.categoryIds= response.data.data.industryClassify
						var a =  this.shop.region//.replace("[","").replace("]","").split(','); 
						this.formData.province = a[0]//.replace(" ","");
						this.formData.city =a[1]//.replace(" ","");
						this.formData.area =a[2]//.replace(" ","");

						  this.fileList = []
						  this.fileListPro =[];
						for (var i = 0; i < this.shop.cardWithMan.length; i++) {
						this.fileList.push({
							url: this.shop.cardWithMan[i],
						})
						}
							for (var i = 0; i < this.shop.proAuthorization.length; i++) {
						this.fileListPro.push({
							url: this.shop.proAuthorization[i],
						})
						}
					
					
		
					}
					console.log(this.fileList,'userinfo1', this.shop.region[0],this.shop)
					
				});
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
	    getShopQualification(){
       getAll("qualification/listByShop?shopId="+this.shopId).then(response => {
         console.log(response,98333)
         if(response.data.errno==0){
			this.qualificationArr =[];//= response.data.data.items
		//	var length =  fileList
			var length  = response.data.data.items.length;
			var list = response.data.data.items;
			for(var i=0;i<length;i++){
				this.qualificationArr.push({
					"name":list[i].name,
					"fileList":[],
					"picUrl":list[i].picUrl
				});
				for(var j=0;j<list[i].picUrl.length;j++){
						this.qualificationArr[i].fileList.push({
							url: list[i].picUrl[j]
						})
					//this.qualificationArr.fileList.push("")
				}
			}
			console.log(this.qualificationArr,98545,response.data.data.items)
         }

       });

	},
	
	  //保存资质信息
    createQualification(){
      
      for(var i=0;i<this.qualificationArr.length;i++ ){
        this.qualificationArr[i].shopId=	this.shopId
      }
      postData("qualification/create",this.qualificationArr).then(response=>{
           if(response.data.errno==0){
			  /*   this.$notify.success({
            title: '成功',
            message: '资质信息上传成功'
		  })*/
		   }
      })

    },
		//获取所有分类信息
		getAllCategoryList(){
			  listCategory(this.listQuery)
        .then(response => {
					if(response.data.errno==0){
					console.log(response.data.data.items,714)
					var list = response.data.data.items;
					var length = list.length;
					this.industryClassify.length  =0
					var listP = [];
	     	 listP= list.filter(item => {
          return item.pid == 0
				})
				for(var i=0;i<listP.length;i++){
				   this.industryClassify.push({"value":listP[i].id,"label": listP[i].name,children:[]})
				}
					for(var j=0;j<length;j++){
				for(var i=0;i<this.industryClassify.length;i++){
					  if(list[j].pid==this.industryClassify[i].value){
							this.industryClassify[i].children.push({"value":list[j].id,"label": list[j].name})
						}
				}
				}}
        })
        .catch( response => {
						this.$notify.error({
							title: '失败',
							message: response.data.errmsg
						})
				})
      
       
			

		},
    handleCancel: function() {
      this.$router.push({ path: '/shopmanage/shoplist' })
    },
    handleEdit: function() {
			const allshop = this.shop
			console.log(this.shop,7023)
			allshop.region.length = 0
			allshop.region.push(this.province) 
		    allshop.region.push(this.city) 
			allshop.region.push(this.area) //= this.province+','+this.city+','+this.area;
			if(this.password!=''){
	     this.shop.password = this.password
			}else{
					this.shop.password =''
			}
				 console.log(997,this.formData,this.area)
			if(this.area==''||this.area==undefined){
				 MessageBox.alert('请补全地址信息', '警告', {
            confirmButtonText: '确定',
            type: 'error'
					})
								return ;
			}
		console.log(allshop,7444)
	//	return;
      editshop(allshop).then(response => {
          this.$notify.success({
            title: '成功',
            message: '创建成功'
		  })
		  this.createQualification();
          // this.$router.push({ path: '/shopmanage/shoplist' })
        })
        .catch(response => {
          MessageBox.alert('业务错误：' + response.data, '警告', {
            confirmButtonText: '确定',
            type: 'error'
          })
        })
    },
    handleClose(tag) {
      this.keywords.splice(this.keywords.indexOf(tag), 1)
      this.goods.keywords = this.keywords.toString()
	},
	   //上传资质
    uploadPicUrl: function(response) {
      
    
      console.log(response,'成功',response.data.url.split(':')[2], this.qualificationArr)
       this.qualificationArr[this.uploadImageType].picUrl.push(   response.data.url.replace("http://localhost:8080/wx/storage/fetch",this.filePath));
    },
		//上传图片 logo
    uploadPicUrla: function(response) {
       this.shop.url =  response.data.url.replace("http://localhost:8080/wx/storage/fetch",this.filePath)
    },
		uploadPicUrlb: function(response) {
		   this.shop.workimgUrl =  response.data.url.replace("http://localhost:8080/wx/storage/fetch",this.filePath)
		},
		uploadPicUrlc: function(response) {
		   this.shop.storefrontimgUrl =  response.data.url.replace("http://localhost:8080/wx/storage/fetch",this.filePath)
		},
		uploadPicUrld: function(response) {

			this.shop.avatar =    response.data.url.replace("http://localhost:8080/wx/storage/fetch",this.filePath)
		  // this.shop.avatar = 'http://120.79.250.63:'+response.data.url.split(':')[2]
		},
		   //法人手持身份证
    uploadCardWithManUrl:function(response){
	  var result =      response.data.url.replace("http://localhost:8080/wx/storage/fetch",this.filePath)
	  console.log(result,'7899')
      this.shop.cardWithMan.push(result);
	},
	    //代理授权书
    uploadproAuthorizationUrl:function(response){
        var result =      response.data.url.replace("http://localhost:8080/wx/storage/fetch",this.filePath)
    
      this.shop.proAuthorization.push(result);
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
		
    uploadOverrun: function() {
      this.$message({
        type: 'error',
        message: '上传文件个数超出限制!最多上传5张图片!'
      })
    },
    handleRemove: function(file, fileList) {
		console.log(this.qualificationArr,file,7977,fileList)
		for(var j=0;j< this.qualificationArr.length;j++){
      for (var i = 0; i < this.qualificationArr[j].picUrl.length; i++) {
        var url
        if (file.response === undefined) {
          url = file.url
        } else {
          url = file.response.data.url
        }

      /*  if (this.goods.gallery[i] === url) {
          this.goods.gallery.splice(i, 1)
		}*/
		 if (this.qualificationArr[j].picUrl[i]=== url) {
          this.qualificationArr[j].picUrl.splice(i, 1)
		}
      }}
		},
		//
		handleCategoryChange (value) {
			this.shop.industryClassify = value
			console.log(value,4444)
			
		},
    specChanged: function(label) {
      if (label === false) {
        this.specifications = [
          { specification: '规格', value: '标准', picUrl: '' }
        ]
        this.products = [
          { id: 0, specifications: ['标准'], price: 0.0, number: 0, url: '' }
        ]
      } else {
        this.specifications = []
        this.products = []
      }
    },
    uploadSpecPicUrl: function(response) {
      this.specForm.picUrl = response.data.url
    },
  }
}
</script>

<style scoped="scoped">
	.avatar{
		display: block;
		width: 150px;
		height: 150px;
		object-fit: contain;
		border: 1px dashed #dcdfe6;
    border-radius: 8px;
    outline: none;
	}
</style>
