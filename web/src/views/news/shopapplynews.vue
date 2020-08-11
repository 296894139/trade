/* eslint disable */
<template>
	<div class="app-container">
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
				<!--
				<el-form-item label="密码" prop="password">
					<el-input v-model="detail.password" readonly/>
				</el-form-item>
				-->
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
				 				   <el-form-item label="法人手持身份证">
						    <img v-for="item in fileList" :key="item.url" :src="item.url" class="shopimg"  >			
					

	 
	  </el-form-item>

    <el-form-item label="代理授权书" >
		 <img v-for="item in fileListPro" :key="item.url" :src="item.url" class="shopimg"  >			
					
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
				<el-button type="primary" size="mini" @click="returndetail(-1)">驳回</el-button>
				<el-button type="primary" size="mini" @click="returndetail(1)">审核通过</el-button>
					</el-form>
				</div>
		</el-dialog>
		
		<el-table v-loading="listLoading" :data="list" size="small" element-loading-text="正在查询中。。。" border fit highlight-current-row>
			<el-table-column align="center" width="100px" label="商家id" prop="id"/>
			<el-table-column align="center" label="店铺名称" prop="shopname"/>
			<el-table-column align="center" property="logo" label="logo">
			  <template slot-scope="scope">
			    <img :src="scope.row.url" width="40">
			  </template>
			</el-table-column>
			<el-table-column align="center" label="负责人" prop="username"/>
			<el-table-column align="center" label="客服电话" prop="serviceMobile"/>
			<el-table-column align="center" label="详细地址" prop="address"/>
			<el-table-column  align="center" width="180px" label="操作">
			<template slot-scope="scope">
				<el-button type="primary" size="mini" @click="toDetail(scope.row)">详情</el-button>
			    <el-button type="primary" v-if="scope.row.status==2" size="mini" @click="handleLock(scope.row)">解锁</el-button>
			<!--	<el-button type="primary" size="mini" @click="handleUpdate(scope.row)">审核通过</el-button> -->
			</template>
				
			</el-table-column>
		</el-table>
		  <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getShopList" />

	</div> 
	
	
	
</template>

<script>
import {
  getAll,
  postData,
  getDataByID,
  putData,

} from '@/api/dbhelper'
import BackToTop from '@/components/BackToTop'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
	export default{
		name : 'shopapplynews',
		components: { BackToTop, Pagination },
		data () {
			return{
			
			fileList:[],//法人手持身份证
			fileListPro:[],//代理授权书
				detail:[],
				list: [],
				detailDialogVisible:false,
				total:0,
				listQuery:{
					page:1,
					limit:10
				},
				listLoading: false
			}
		},
		created(){
			this.getShopList();
		},
		methods:{
			getShopList(){
				getAll("shop/Querylist?status=0&page="+this.listQuery.page+"&limit="+this.listQuery.limit).then(response=>{
                  if(response.data.errno===0){
					  this.list = response.data.data.items
					  this.total =  response.data.data.total
				//	 console.log(r)
				  }
				})
				},
				//查看详情
			toDetail(row){
				this.detail.length = 0
				this.detailDialogVisible = true
				 this.fileList = []
						  this.fileListPro =[];
						for (var i = 0; i < row.cardWithMan.length; i++) {
						this.fileList.push({
							url: row.cardWithMan[i],
						})
						}
							for (var i = 0; i < row.proAuthorization.length; i++) {
						this.fileListPro.push({
							url: row.proAuthorization[i],
						})
						}  
						var str = ''
						for(var i =0;i<row.region.length;i++){
								str = str+row.region[i]
						}
							this.detail = row
							this.detail.region = str
    			console.log(row.region,7499, this.detail)
			},
			//审核
			returndetail(status){
				this.detail.status = status
				this.detail.region = [];//置空地址 对后端不影响
				putData("shop/InputShopLogin",this.detail).then(response=>{
					if(response.data.errno==0){
						this.getShopList();
					}
						console.log(response,'605')
				})
				this.detailDialogVisible=false
				
			},
				//加锁
			handleLock(row){
				row.status = 2;
				console.log(row,'sxxxx')
					this.detail.region = [];//置空地址 对后端不影响
				putData("shop/InputShopLogin",row).then(response=>{
					if(response.data.errno==0){
						  this.$notify({
							title: '成功',
							message: '锁定成功',
							type: 'success',
							duration: 2000
							})
						this.getShopList();
					}
						console.log(response,'锁定')
				})
				
			},
		},
	}
</script>

<style>
.shopimg{
	display: block;
	widows: 100px;
	height: 100px;
}
</style>
