/* eslint disable */
<template>
	<div class="app-container">
		<!-- 查找 -->
		<div class="filter-container">
			<el-input v-model="listQuery.live_name" clearable class="filter-item" style="width: 200px;" placeholder="请输入直播间内容介绍"/>
			<el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">查找</el-button>
		<el-button class="filter-item" type="primary" v-if="list.length==0" icon="el-icon-edit" @click="handleCreate">添加</el-button> 
		</div>


		
			<el-dialog :visible.sync="detailUploadVisible" title="上传视频">
				  <el-form>

					  <el-form-item label="视频上传" prop="Video">
  <!-- action必选参数, 上传的地址 -->
    <el-upload class="avatar-uploader el-upload--text" :action="uploadPath" :show-file-list="false" :on-success="handleVideoSuccess" :before-upload="beforeUploadVideo" :on-progress="uploadVideoProcess">
        <video v-if="dataForm.picUrl !='' && videoFlag == false" :src="dataForm.picUrl" class="avatar" controls="controls">您的浏览器不支持视频播放</video>
        <i v-else-if="dataForm.picUrl=='' && videoFlag == false" class="el-icon-plus avatar-uploader-icon"></i>
        <el-progress v-if="videoFlag == true" type="circle" :percentage="videoUploadPercent" style="margin-top:30px;"></el-progress>
    </el-upload>
    <P class="text">请保证视频格式正确，且不超过10M</P>
</el-form-item>
 

		<!--
		 <el-form-item label="请选择视频" prop="videoUrl">						
          <el-upload
            :headers="headers"
            :action="uploadPath"
            :show-file-list="false"
            :on-success="uploadPicUrl"
            class="avatar-uploader"
            >
            <img v-if="dataForm.picUrl" :src="dataForm.picUrl" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"/>
          </el-upload>
        </el-form-item>
		-->
					  <el-button @click="this.detailUploadVisible=false">取消</el-button>
      <!--    <el-button type="primary" @click="handleSure">确定</el-button> -->
				
     
				  </el-form>
			</el-dialog>
		
		

   <!--<el-progress v-if="videoFlag == true" type="circle" :percentage="videoUploadPercent" style="margin-top:30px;"></el-progress>
	-->
		<!-- 直播间列表 -->
		<el-table v-loading="listLoading" :data="list" size="small" @row-click="handleClick" @expand-change="expandChange" element-loading-text="正在查询中。。。" border fit highlight-current-row>
			 
      <el-table-column type="expand" prop="names" width="100px" >
     <template slot-scope="scope" >
       <el-table  :data="scope.row.names" >
				  <el-table-column prop="goodsSn" label="商品编号" width=125   sortable align="center">
             <template slot-scope="scope">
               <span>{{scope.row.goodsSn}}</span>
             </template>
          </el-table-column>
          <el-table-column prop="name" label="名称" sortable align="center">
             <template slot-scope="scope">
               <span>{{scope.row.name}}</span>
             </template>
          </el-table-column>
					 <el-table-column prop="picUrl" label="商品图片" width=125  sortable align="center">
             <template slot-scope="scope">
							 	    <img :src="scope.row.picUrl" width="40">
             <!--  <span>{{scope.row.picUrl}}</span> -->
             </template>
          </el-table-column>
					<!--
						 <el-table-column prop="name" label="分享图片" width=125  sortable align="center">
             <template slot-scope="scope">
               <span>{{scope.row.name}}</span>
             </template>
          </el-table-column>
				
							 <el-table-column prop="name" label="详情" sortable align="center">
             <template slot-scope="scope">
               <span>{{scope.row.name}}</span>
             </template>
          </el-table-column>
						-->
						 <el-table-column prop="counterPrice" label="专柜价格"  width=125  sortable align="center">
             <template slot-scope="scope">
               <span>{{scope.row.counterPrice}}</span>
             </template>
          </el-table-column>
						 <el-table-column prop="retailPrice" label="当前价格" width=125  sortable align="center">
             <template slot-scope="scope">
               <span>{{scope.row.retailPrice}}</span>
             </template>
          </el-table-column>

					
					<!--
							 <el-table-column prop="retailPrice" label="操作" width=200  align="center">
           	  <template slot-scope="scope">
			    <el-button type="primary" size="mini" @click="handleShopUpdate(scope.row)">编辑</el-button>
			    <el-button type="danger" size="mini" @click="handleShopDelete(scope.row)">删除</el-button>
			  </template>
          </el-table-column>
					-->
      </el-table>
    </template>
  </el-table-column>

      
			<el-table-column align="center" width="100px" label="直播间密码" prop="password"/>
			<el-table-column align="center" width="100px" label="商家id" prop="id"/>
			<el-table-column align="center" label="直播流名称" prop="liveName"/>
			<el-table-column align="center" label="流地址" prop="link"/>
			<el-table-column align="center" label="分红介绍"  prop="giftName"  />
			
			<el-table-column align="center" label="礼物分成" prop="giftFencheng"/>
			<el-table-column align="center" label="宣传图片" prop="url">
				  <template slot-scope="scope">
			    <img :src="scope.row.url" width="40">
			  </template>
			</el-table-column>
			<el-table-column align="center" label="直播标题" prop="content"/>
			<el-table-column align="center" label="操作" width="280" class-name="small-padding fixed-width">
			  <template slot-scope="scope">
			    <el-button type="primary" size="mini" @click="handleUpdate(scope.row)">编辑</el-button>
			    <el-button type="primary" size="mini" @click="handleUpload(scope.row)">上传</el-button>
			    <el-button type="danger" size="mini" @click="handleDelete(scope.row)">删除</el-button>
			  </template>
			</el-table-column>
		</el-table>
		
		<el-dialog :visible.sync="detailDialogVisible" title="商家详情">
		  <div >
			<el-form label-position="left" class="table-expand" :data="detail">
				<el-form-item label="id" prop="id">
					<el-input v-model="detail.id" readonly/>
				</el-form-item>
				<el-form-item label="负责人名字" prop="username">
					<el-input v-model="detail.username" readonly/>
				</el-form-item>
				<el-form-item label="店铺名称" prop="shopname">
					<el-input v-model="detail.shopname" readonly/>
				</el-form-item>
				<el-form-item label="创建时间" prop="addTime">
					<el-input v-model="detail.addTime" readonly/>
				</el-form-item>
				<el-form-item label="详细地址" prop="address">
					<el-input v-model="detail.address" readonly/>
				</el-form-item>
				<el-form-item label="密码" prop="password">
					<el-input v-model="detail.password" readonly/>
				</el-form-item>
				<el-form-item label="位置区域" prop="region">
					<el-input v-model="detail.region" readonly/>
				</el-form-item>
				<el-form-item label="客服电话" prop="serviceMobile">
					<el-input v-model="detail.serviceMobile" readonly/>
				</el-form-item>
				<el-form-item label="营业时间" prop="workTime">
					<el-input v-model="detail.workTime" readonly/>
				</el-form-item>
				<el-form-item label="入驻资质" prop="workimgUrl">
					<img :src="detail.workimgUrl" alt="" class="shopimg" />
				</el-form-item>
				<el-form-item label="logo" prop="url">
					<img :src="detail.url" alt="" class="shopimg" />
				</el-form-item>
				<el-form-item label="更新时间" prop="updateTime">
					<el-input v-model="detail.updateTime" readonly/>
				</el-form-item>
				<el-form-item label="门头图片" prop="storefrontimgUrl">
					<img :src="detail.storefrontimgUrl" alt="" class="shopimg" />
				</el-form-item>
				<el-form-item label="头像图片" prop="avatar">
					<img :src="detail.avatar" alt="" class="shopimg" />
				</el-form-item>
				<el-form-item label="行业分类" prop="industryClassify">
					<el-input v-model="detail.industryClassify" readonly/>
				</el-form-item>
				<el-button type="primary" size="mini" @click="returndetail">返回</el-button>
				
					</el-form>
				</div>
		</el-dialog>
		<!-- 分页 -->
		<pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getlist" />
		
		<!-- 返回顶部 -->
		<el-tooltip placement="top" content="返回顶部">
		  <back-to-top :visibility-height="100" />
		</el-tooltip>
		
	</div>
</template>

<script>
import { createStorage, uploadPath,filePath } from '@/api/storage'
import { getToken } from '@/utils/auth'
import {
  getAll,
  postData,
  getDataByID,
  putData,
	deleteData,
	getSearch
} from '@/api/dbhelper'
import { mapGetters } from "vuex";


import BackToTop from '@/components/BackToTop'
import Pagination from '@/components/Pagination'
	export default{
		name: 'shoplist',
		components: { BackToTop, Pagination },
		data () {
			return {
		   videoFlag:false,
		   videoUploadPercent:0,
			uploadPath,
		    filePath,
			dataForm:{},
								//一行的数据
								rowData:{},
			detailUploadVisible:false,
				total: 0,
				listLoading: false,
				list: [
				],
				shopname:'111',
				listQuery: {
				  page: 1,
					limit: 20,
					userId:0
				},
				detailDialogVisible: false,
				detail:[],

      //添加商品
		  goods: { picUrl: '', shareUrl:'', gallery: [] },
      specVisiable: false,
			}
			
		},
		  computed: {
				  ...mapGetters(["shopId"]),
    headers() {
      return {
        'X-Litemall-Admin-Token': getToken()
      }
    }
  },
		created() {
		 if(this.$store.state.user.roles.indexOf("商家")>-1){
		   	this.listQuery.userId = this.shopId
		 }
		 this.getlist()
	
		},
		methods:{
			// 确认上传视频
			handleSure(){
		
			this.rowData.link = 	this.dataForm.picUrl
   

         postData("LiveStreaming/updateLiveVideo",this.rowData).then(response => {
			 if(response.data.errno==0){
					 this.$notify.success({
						title: '成功',
						message: '上传成功'
					})
			 
			 }
          
      })
				
       this.detailUploadVisible=false
			},
				getlist(){
				this.listLoading = true
				console.log(this.listQuery,8777)
					getAll('LiveStreaming/listlistLive?userId='+this.listQuery.userId).then(response => {
			//	listshops(this.listQuery).then(response => {
					console.log(response.data,'7898')
				  this.list = response.data.data.items
				  this.total = response.data.data.total
				  this.listLoading = false
				}).catch(() => {
				  this.list = []
				  this.total = 0
				  this.listLoading = false
				})
			},
				handleUpload(row){
					this.rowData = row
				this.detailUploadVisible =  true
				

        console.log('上传视频',row)
			},
			
beforeUploadVideo(file) {
    const isLt10M = file.size / 1024 / 1024  < 10;
    if (['video/mp4', 'video/ogg', 'video/flv','video/avi','video/wmv','video/rmvb'].indexOf(file.type) == -1) {
        this.$message.error('请上传正确的视频格式');
        return false;
    }
    if (!isLt10M) {
        this.$message.error('上传视频大小不能超过10MB哦!');
        return false;
	}
	},
	
uploadVideoProcess(event, file, fileList){
    this.videoFlag = true;
    this.videoUploadPercent = file.percentage.toFixed(0);
},
handleVideoSuccess(res, file) {                               //获取上传图片地址
    this.videoFlag = false;
	this.videoUploadPercent = 0;
	console.log(res,'44444')
    if(res.errno == 0){
			var result =      res.data.url.replace("http://localhost:8080/wx/storage/fetch",this.filePath)
		this.dataForm.picUrl =result;
			this.handleSure();
       // this.videoForm.videoUploadId = res.data.uploadId;
       // this.videoForm.Video = res.data.uploadUrl;
    }else{
        this.$message.error('视频上传失败，请重新上传！');
    }
},


			
			  uploadPicUrl: function(response) {
					var result =      response.data.url.replace("http://localhost:8080/wx/storage/fetch",this.filePath)
        console.log('回调成功',response)
		this.dataForm.picUrl =result
		this.handleSure();
       },
			handleClick(row, column, event){
       console.log(row,column,event)
			},
       //扩展行点击事件 获取该直播间下所有商品
			   expandChange: async function     (row) {
          console.log(row,74111)
				  getDataByID('LiveStreaming/QueryShopGoodsList?userId='+row.userId).then(response => {
			//	listshops(this.listQuery).then(response => {
					console.log(response.data.data.data.items,'8888')
					var shopGoodsInfo = response.data.data.data.items;
					var length = shopGoodsInfo.length;
					         for(var i=0;i<length;i++){
									row.names.push({
										goodsSn:shopGoodsInfo[i].goodsSn,
								name:shopGoodsInfo[i].name,
								picUrl:shopGoodsInfo[i].picUrl,
								retailPrice:shopGoodsInfo[i].retailPrice,
								counterPrice:shopGoodsInfo[i].counterPrice,
           });
          }

			
				}).catch(() => {
			
				})

					console.log('enter',row)
					row.names=[];
			/*		row.names.push({
            name: 'apple2',
            num:2,
					 });*/
      
				},
			//商品更新
			handleShopUpdate(row){

			},
			//商品删除
			handleShopDelete(row){

			},


			handleCreate () {
       console.log('enter cre',this.list)
				this.$router.push({ path: '/shopmanage/createShopLive' ,query: {id: this.shopId}})
			},
			handleUpdate(row) {
			  this.$router.push({ path: '/shopmanage/editShopLive', query: {id: row.id}})
			},
			handledetail (row) {
				this.detailDialogVisible = true
				const shopId = row.id
				detailshop(row.id).then(response => {
				  this.detail = response.data.data
				}).catch(() => {
				  
				})
			},
			returndetail(){
				this.detailDialogVisible = false
			},
			handleDelete (row) {
				var data  = {id:row.id}
				console.log(row.id,'delete')
				deleteData("LiveStreaming/deletelive",row.id).then(response => {
					this.$notify.success({
						title: '成功',
						message: '删除成功'
					})
					const index = this.list.indexOf(row)
					this.list.splice(index, 1)
				}).catch(response => {
						this.$notify.error({
							title: '失败',
							message: response.data.errmsg
						})
				})
			},
			//find
			handleFilter() {
			  this.listQuery.page = 1
			  this.getlist()
			},
			//添加商品
			handleAddShop(id){
				 //alert(id)
      
				 this.specVisiable=true;
			}
			
		}
	}
</script>

<style scoped="scoped">
.fixed-width .el-button--mini {
    padding: 7px 6px;
}
.shopimg{
	display: block;
	widows: 100px;
	height: 100px;
}
</style>
