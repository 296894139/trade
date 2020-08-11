
<template>
  <div class="app-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <span>酒庄选择</span>
      <el-select v-model="shopId" @change="changeShop">
        <el-option v-for="item in shopIds" :key="item.id" :label="item.name" :value="item.id"/>
      </el-select>
      <el-button class="filter-item" type="primary" icon="el-icon-edit" @click="handleCreate">添加</el-button>
      <!--<el-button :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="handleDownload">导出</el-button>-->
    </div>

    <!-- 查询结果 -->
    <el-table  :data="list" size="small" border fit highlight-current-row>
      <el-table-column align="center" label="id" prop="id"/>
      <el-table-column align="center" label="等级名称" prop="name"/>
       <el-table-column align="center" label="所需消费金额" prop="discount"/>
      <el-table-column align="center" property="url" label="等级图标">
        <template slot-scope="scope">
          <img v-if="scope.row.url" :src="scope.row.url" width="40">
        </template>
      </el-table-column>


<!--

      <el-table-column align="center" min-width="100" label="显示数据" prop="data">
    <template slot-scope="scope">
          <el-tag>{{ scope.row.type | moduleStatusFilter }}</el-tag>
        </template>
        </el-table-column>
-->






      <el-table-column align="center" label="操作" width="200" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handleUpdate(scope.row)">编辑</el-button>
          <el-button type="danger" size="mini" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog

     :title="textMap[dialogStatus]"
  :visible.sync="dialogVisible"
  width="30%"
  :before-close="handleClose">
  <el-form ref="editForm" label-width="100px" status-icon label-position="left"  style="width: 400px; margin-left:50px;">
    	<el-form-item label="等级名称"  >
         <el-input v-model="editForm.name" style="width: 200px;"/>
        		</el-form-item>
                	<el-form-item label="所需消费金额" >
         <el-input v-model="editForm.discount" style="width: 200px;"/>
        		</el-form-item>

                <el-form-item label="等级图标" prop="editForm.url">
          <el-upload
                      :action="uploadPath"
            :show-file-list="false"
            :on-success="uploadUrl"
            class="avatar-uploader"
            accept=".jpg,.jpeg,.png,.gif">
            <img v-if="editForm.url" :src="editForm.url" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"/>
          </el-upload>
        </el-form-item>




  </el-form>

  <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible = false">取 消</el-button>
    <el-button  v-if="dialogStatus=='update'" type="primary" @click="handleSubmit">确 定</el-button>
    <el-button v-else type="primary" @click="handleCreateData">确 定</el-button>


  </span>
</el-dialog>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <el-tooltip placement="top" content="返回顶部">
      <back-to-top :visibility-height="100" />
    </el-tooltip>

  </div>
</template>

<style>
.avatar-uploader .el-upload {
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
  .table-expand {
    font-size: 0;
  }
  .table-expand label {
    width: 100px;
    color: #99a9bf;
  }
  .table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
  }
  .gallery {
    width: 80px;
    margin-right: 10px;
  }
  .el-dialog img {
    display: block;
    width: 100%;
    height: 100%;
	}
</style>

<script>
import { listGoods, deleteGoods ,listShopGoods} from '@/api/goods'
import { createStorage, uploadPath,filePath } from '@/api/storage'
import BackToTop from '@/components/BackToTop'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import { mapGetters } from "vuex";
import {
  getAll,
  postData,
  getDataByID,
  putData,
  deleteData,
  deleteByID,
	getSearch
} from '@/api/dbhelper'
import axios from 'axios'
import * as config from '../../../config'
const statusMap = {
  0: '商品',
  1: '直播间',
}
const areaMap = {
  0: 'PC端',
  1: 'APP端',
}
export default {
  name: 'levelmanage',
  components: { BackToTop, Pagination },
  filters: {

  },
  data() {
    return {
      dialogStatus:'',
         textMap: {
        update: '编辑',
        create: '创建'
      },
      uploadPath,
      filePath,
      dialogVisible:false,
      editForm:{
        id:'',
        name:'',
        discount:0,
        url:'',
        discountId:'',
        shopId:''
      },
      shopId:'',
      shopIds:[],
      isYesNo:'',//显示直播间或者商品
      list: [
      ],
      discount:[],
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        goodsSn: undefined,
        name: undefined,
        sort: 'add_time',
        order: 'desc'
      },
      goodsDetail: '',
      detailDialogVisible: false,
      downloadLoading: false,
      categoryList: [],
      statusMap,
      areaMap
    }
  },
  	  computed: {
				  ...mapGetters(["shopId","roles"]),
    headers() {
      /*return {
        'X-Litemall-Admin-Token': getToken()
      }*/
    }
  },
  created() {
    this.listQuery.shopId = this.shopId;
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true  //listShopGoods

      axios({
        method: 'get',
        url: config.baseApi + "level/find/all?&page="+ (this.listQuery.page-1)+"&size=20",
        headers:{
          "X-Litemall-Admin-Token":sessionStorage.getItem('token')
        }
      }).then(response => {

        this.list = response.data.data.items.content
        this.total = response.data.data.items.totalElements
        this.listLoading = false

      }).catch(error => {
        this.list = []
        this.total = 0
        this.listLoading = false
      });
      axios({
        method: 'get',
        url: config.baseApi + "discount/find/all?&page="+ (this.listQuery.page-1)+"&size=20",
        headers:{
          "X-Litemall-Admin-Token":sessionStorage.getItem('token')
        }
      }).then(response => {

        this.discount = response.data.data.items.content

      }).catch(error => {
        this.discount = []

      });

      axios({
        method: 'get',
        url: config.baseApi + "shop/find/all?&page="+ (this.listQuery.page-1)+"&size=100",
        headers:{
          "X-Litemall-Admin-Token":sessionStorage.getItem('token')
        }
      }).then(response => {

        this.shopIds = response.data.data.items.content

      }).catch(error => {
      });

    },

        uploadUrl: function(response) {
          if(response.code==0){
              this.editForm.url =  this.filePath+"/"+ response.data.filePath
          }

          console.log(response,11144, this.editForm.url );
    },

    changeShop(){
      var shopId=this.shopId
      axios({
        method: 'get',
        url: config.baseApi + "level/find/shopId?shopId="+this.shopId+"&page="+ (this.listQuery.page-1)+"&size=20",
        headers:{
          "X-Litemall-Admin-Token":sessionStorage.getItem('token')
        }
      }).then(response => {
        this.list = response.data.data.items.content
        this.total = response.data.data.items.totalElements
        this.listLoading = false

      }).catch(error => {
        this.list = []
        this.total = 0
        this.listLoading = false
      });
      axios({
        method: 'get',
        url: config.baseApi + "discount/find/shopId?shopId="+this.shopId+"&page="+ (this.listQuery.page-1)+"&size=20",
        headers:{
          "X-Litemall-Admin-Token":sessionStorage.getItem('token')
        }
      }).then(response => {

        this.discount = response.data.data.items.content

      }).catch(error => {
        this.discount = []

      });
    },

    handleClose(){
    this.dialogVisible = false
    },
    //提交编辑
    handleSubmit(){
      axios({
        method: 'put',
        url: config.baseApi + "level/update",
        headers:{
          "X-Litemall-Admin-Token":sessionStorage.getItem('token')
        },
        data:this.editForm
      }).then(response => {
        if(response.data.code==0){
          this.$notify.success({
            title: '成功',
            message: '修改成功'
          })
          this.listQuery.page = 0
          this.changeShop()
          this.dialogVisible = false
        }
      }).catch(error => {
        this.$notify.error({
          title: '失败',
          message: response.data.errmsg
        })
      });


    },
    //选择商品分类
    handleCategoryChange(value) {
      this.goods.categoryId = value[value.length - 1]
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleCreate() {
       this.dialogStatus  = 'create'
      this.dialogVisible = true
      this.editForm.name=''
      this.editForm.discount=0,
      this.editForm.id='',
        this.editForm.shopId=this.shopId
        this.editForm.discountId=''
    },
    handleCreateData() {
      axios({
        method: 'post',
        url: config.baseApi + "level/add",
        headers:{
          "X-Litemall-Admin-Token":sessionStorage.getItem('token')
        },
        data:this.editForm
      }).then(response => {
        if(response.data.code==0){
          this.$notify.success({
            title: '成功',
            message: '创建成功'
          })
          this.listQuery.page = 0
          this.changeShop()
          this.dialogVisible = false
        }
      }).catch(error => {
        this.$notify.error({
          title: '失败',
          message: response.data.errmsg
        })
      });

    },
    handleUpdate(row) {
      this.dialogStatus  = 'update'
      this.dialogVisible = true
      this.editForm.name = row.name;
      this.editForm.id = row.id;
      this.editForm.discount = row.discount;
      this.editForm.discountId = row.discountId;
      this.editForm.shopId=row.shopId
      this.editForm.url = row.url;
      //this.$router.push({ path: '/goods/edit', query: { id: row.id }})
    },
    showDetail(detail) {
      this.goodsDetail = detail
      this.detailDialogVisible = true
    },
    handleDelete(row) {
      axios({
        method: 'get',
        url: config.baseApi + "level/delete?id="+ row.id,
        headers:{
          "X-Litemall-Admin-Token":sessionStorage.getItem('token')
        }
      }).then(response => {

        if(response.data.code==0){
          this.$notify.success({
            title: '成功',
            message: '删除成功'
          })

          const index = this.list.indexOf(row)
          this.list.splice(index, 1)
        }

      }).catch(error => {
        this.$notify.error({
          title: '失败',
          message: response.data.message
        })
      });


      /*deleteGoods(row).then(response => {
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
      })*/
    }
  }
}
</script>
