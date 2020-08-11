/* eslint disable */
<template>
	<div class="app-container">
		<!-- 查找 -->
		<div class="filter-container">
			<el-input v-model="listQuery.username" clearable class="filter-item" style="width: 200px;" placeholder="请输入负责人名字"/>
		
            <el-select v-model="listQuery.shopStatus" >
            <el-option label="全部" value=""/>
            <el-option label="未审核" value="1"/>
            <el-option label="已审核" value="2"/>
          </el-select>
          	<el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">查找</el-button>
		</div>
		
		<!-- 商家列表 -->
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
			<el-table-column align="center" label="操作" width="280" class-name="small-padding fixed-width">
			  <template slot-scope="scope">
			    <el-button type="primary" v-if="distId==undefined"  size="mini" @click="handleUpdate(scope.row)">编辑</el-button>
				<el-button type="primary" size="mini" @click="handledetail(scope.row)">查看详情</el-button>
			    <el-button type="danger"  v-if="distId==undefined"  size="mini" @click="handleDelete(scope.row)">删除</el-button>
				 <el-button type="primary" v-if="distId" size="mini" @click="apply(scope.row)">申请分销</el-button>
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
import { listshops, deleteshop, detailshop} from '@/api/shop'
import BackToTop from '@/components/BackToTop'
import Pagination from '@/components/Pagination'
import { mapGetters } from "vuex";
import {
  getAll,
  postData,
  getDataByID,
  putData,
	deleteData,
	getSearch
} from '@/api/dbhelper'
	export default{
		name: 'distShopList',
		components: { BackToTop, Pagination },
		data () {
			return {
				total: 0,
				listLoading: false,
				list: [],
				shopname:'111',
				listQuery: {
				  page: 1,
				  limit: 20,
				},
				detailDialogVisible: false,
				detail:[]
			}
		},
		created() {
		//如果是商家
		if(this.$store.state.user.roles.indexOf("商家")>-1){
			this.listQuery.username = this.$store.state.user.name
        }
        				if(this.roles.indexOf("门店")>-1){
		  console.log(this.roles,'当前角色  777',this.distId)
        this.listQuery.shopId = undefined;
        this.listQuery.distId = this.distId
		}
	
		console.log(this.shopId,this.roles,'mapgetter',   this.listQuery)
		  this.getlist()
		},
			  computed: {
				  ...mapGetters(["shopId","roles","distId"]),
    headers() {
      return {
        'X-Litemall-Admin-Token': getToken()
      }
    }
  },
		methods:{
			// 获取商家列表
			getlist () {
		
				this.listLoading = true
	
		
				
				this.listQuery.status = 1
						console.log(444,			this.listQuery.shopId )
				listshops(this.listQuery).then(response => {
					console.log(response.data)
				  this.list = response.data.data.items
				  this.total = response.data.data.total
				  this.listLoading = false
				}).catch(() => {
				  this.list = []
				  this.total = 0
				  this.listLoading = false
				})
			},
			handleCreate () {
				this.$router.push({ path: '/shopmanage/createshop' })
			},
			handleUpdate(row) {
				//this.$router.push({ path: '/shopmanage/edit', query: {id: row.id}})
				this.$router.push({ path: '/shopinfo/modiinfo',query: {id: row.id}})
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
			//申请分销
			apply(row){
               var obj = {
				   DistributionId:this.distId,
				   shopId:row.id
			   }
			   console.log(row,'4444')
				getAll('AdminDistribution/apply',obj).then(response=>{
					if(response.data.errno==0){
						this.$notify.success({
							title: '',
							message: response.data.data.item
						})
					}
					console.log(response,7055,row)
				})/*.catch(response=>{
					this.$notify.error({
							title: '失败',
							message: 222
						})
				})*/

			},
			handleDelete (row) {
				deleteshop(row).then(response => {
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
