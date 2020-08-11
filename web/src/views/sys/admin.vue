
<template>
  <div class="app-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input v-model="listQuery.key" clearable class="filter-item" style="width: 200px;" placeholder="请输入关键词"/>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">查找</el-button>
      <el-button class="filter-item" type="primary" icon="el-icon-edit" @click="handleCreate">添加</el-button>
      <!--<el-button :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="handleDownload">导出</el-button>-->
    </div>

    <!-- 查询结果 -->
    <el-table  :data="list" size="small" border fit highlight-current-row>
      <el-table-column align="center" label="id" prop="id"/>
      <el-table-column align="center" label="用户名" prop="username"/>
      <el-table-column align="center" label="密码" prop="password"/>

      <el-table-column align="center" label="创建时间" prop="date"/>
      <el-table-column align="center" property="face" label="管理员头像">
        <template slot-scope="scope">
          <img v-if="scope.row.face" :src="scope.row.face" width="40">
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
        <el-form-item label="用户名"  >
          <el-input v-model="editForm.username" style="width: 200px;"/>
        </el-form-item>
        <el-form-item label="密码" >
          <el-input v-model="editForm.password" style="width: 200px;"/>
        </el-form-item>


        <!--<el-form-item label="权限" >-->
          <!--<div>-->
            <!--<input type='checkbox' class='input-checkbox' :checked="fruitIds.length === fruits.length" @click='checkedAll()'>全选-->
            <!--<div v-for='(fruit, index) in fruits' :key="index">-->

              <!--&lt;!&ndash;判断fruitIds是否包含当前fruit，fruitIds.indexOf(fruit.fruitId)返回包含fruit的下标, 若不包含则返回-1&ndash;&gt;-->
              <!--<input type='checkbox' :checked="fruitIds.indexOf(fruit.fruitId) > -1" name='checkboxinput' class='input-checkbox' @click='checkedOne(fruit.fruitId)'>-->
              <!--{{fruit.value}}-->
            <!--</div>-->
          <!--</div>-->

        <!--</el-form-item>-->

        <el-form-item label="管理员头像" prop="editForm.face">
          <el-upload
            :action="uploadPath"
            :show-file-list="false"
            :on-success="uploadUrl"
            class="avatar-uploader"
            accept=".jpg,.jpeg,.png,.gif">
            <img v-if="editForm.face" :src="editForm.face" class="avatar">
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
        fruits:[{
          fruitId:'人员管理',
          value:'人员管理'
        },{
          fruitId:'酒庄管理',
          value:'酒庄管理'
        },{
          fruitId:'商品管理',
          value:'商品管理'
        },{
          fruitId:'优惠券管理',
          value:'优惠券管理'
        },{
          fruitId:'系统管理',
          value:'系统管理'
        },{
          fruitId:'推广管理',
          value:'推广管理'
        },{
          fruitId:'订单管理',
          value:'订单管理'
        },{
          fruitId:'统计',
          value:'统计'
        }

        ],
        fruitIds:[],
        isCheckedAll: false,
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
          username:'',
          password:'',
          limits: '',
          date: '',
          face:''
        },
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
      checkedOne (fruitId) {
        let idIndex = this.fruitIds.indexOf(fruitId)
        if (idIndex >= 0) {
          // 如果已经包含了该id, 则去除(单选按钮由选中变为非选中状态)
          this.fruitIds.splice(idIndex, 1)
        } else {
          // 选中该checkbox
          this.fruitIds.push(fruitId)
        }
        if(this.fruitIds.length==this.fruits.length){
          this.isCheckedAll=true;
        }
      },
      checkedAll () {
        this.isCheckedAll = !this.isCheckedAll
        if (this.isCheckedAll) {
          // 全选时
          this.fruitIds = []
          this.fruits.forEach(function (fruit) {
            this.fruitIds.push(fruit.fruitId)
          }, this)
        } else {
          this.fruitIds = []
        }
      },

      getList() {
        this.listLoading = true  //listShopGoods

        axios({
          method: 'get',
          url: config.baseApi + "admin/find/all?&page="+ (this.listQuery.page-1)+"&size=20",
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

      },

      uploadUrl: function(response) {
        if(response.code==0){
          this.editForm.face =  this.filePath+"/"+ response.data.filePath
        }

      },
      handleFilter() {
        this.listQuery.page = 1
        this.list=[]
        this.listLoading = true
        axios({
          method: 'get',
          url: config.baseApi + "admin/find/query?query="+this.listQuery.key+"&page="+ (this.listQuery.page-1)+"&size=20",
          headers:{
            "X-Litemall-Admin-Token":sessionStorage.getItem('token')
          }
        }).then(response => {
          if(response.data.code==0){
            this.list = response.data.data.items.content
            this.total = response.data.data.items.totalElements//response.data.data.total
            this.listLoading = false
          }
        }).catch(error => {
          this.list = []
          this.total = 0
          this.listLoading = false
        });
      },
      handleClose(){
        this.dialogVisible = false
      },
      //提交编辑
      handleSubmit(){
        var ids=this.fruitIds
        var limits=""
        for(var i=0;i<ids.length;i++){
          if(i!=(ids.length-1)){
            limits=limits+ids[i]+","
          }
          else{
            limits=limits+ids[i]
          }
        }
        this.editForm.limits = limits;
        axios({
          method: 'put',
          url: config.baseApi + "admin/update",
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
            this.getList()
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
        this.editForm.username=''
        this.editForm.password=''
          this.editForm.date=''
          this.editForm.id=''
        this.editForm.limits = '';
        this.fruitIds=[]
        this.editForm.face=''
      },
      handleCreateData() {
        let nowDate = new Date();
        let date = {
          year: nowDate.getFullYear(),
          month: nowDate.getMonth() + 1,
          date: nowDate.getDate(),
        }
        let systemDate = date.year + '-' +  date.month + '-' + 0 + date.date;
        this.editForm.date=systemDate

        var ids=this.fruitIds
        var limits=""
        for(var i=0;i<ids.length;i++){
          if(i!=(ids.length-1)){
            limits=limits+ids[i]+","
          }
          else{
            limits=limits+ids[i]
          }
        }
        this.editForm.limits = limits
        axios({
          method: 'post',
          url: config.baseApi + "admin/add",
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
            this.getList()
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
        this.editForm.username = row.username;
        this.editForm.id = row.id;
        this.editForm.password = row.password;
        this.fruitIds=row.limits.split(",")
        this.editForm.limits = row.limits;
        this.editForm.face = row.face;
        this.editForm.date = row.date;
        //this.$router.push({ path: '/goods/edit', query: { id: row.id }})
      },
      showDetail(detail) {
        this.goodsDetail = detail
        this.detailDialogVisible = true
      },
      handleDelete(row) {
        axios({
          method: 'get',
          url: config.baseApi + "admin/delete?id="+ row.id,
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

      }
    }
  }
</script>
