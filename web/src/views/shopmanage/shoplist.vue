<template>
  <div class="app-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input v-model="listQuery.key" clearable class="filter-item" style="width: 200px;" placeholder="请输入关键词"/>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">查找</el-button>
      <el-button class="filter-item" type="primary" icon="el-icon-edit" @click="handleCreate">添加</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table v-loading="listLoading" :data="list" size="small" element-loading-text="正在查询中。。。" border fit highlight-current-row>
      <el-table-column align="center" width="100px" label="编号" prop="id" sortable/>

      <el-table-column align="center" label="分类名称" prop="name"/>

      <!--<el-table-column align="center" property="faceUrl" label="分类图片">-->
        <!--<template slot-scope="scope">-->
          <!--<img v-if="scope.row.faceUrl" :src="scope.row.faceUrl" width="40">-->
        <!--</template>-->
      <!--</el-table-column>-->

      <el-table-column align="center" label="操作" width="200" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handleUpdate(scope.row)">编辑</el-button>
          <el-button type="danger" size="mini" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <!-- 添加或修改对话框 -->
    <el-dialog :title="'请输入管理员密码'" :visible.sync="passwordView">
      <el-form ref="loginForm" :rules="rules" :model="loginForm" status-icon label-position="left" label-width="100px" style="width: 400px; margin-left:50px;">
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="loginForm.password"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="passwordView = false">取消</el-button>
        <el-button type="primary" @click="deleteShop()">确定</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="dataForm" status-icon label-position="left" label-width="100px" style="width: 400px; margin-left:50px;">
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="dataForm.name"/>
        </el-form-item>

        <!--<el-form-item label="分类图片" prop="dataForm.faceUrl">-->
          <!--<el-upload-->
            <!--:action="uploadPath"-->
            <!--:show-file-list="false"-->
            <!--:on-success="uploadfaceUrl"-->
            <!--class="avatar-uploader"-->
            <!--accept=".jpg,.jpeg,.png,.gif">-->
            <!--<img v-if="dataForm.faceUrl" :src="dataForm.faceUrl" class="avatar">-->
            <!--<i v-else class="el-icon-plus avatar-uploader-icon"/>-->
          <!--</el-upload>-->
        <!--</el-form-item>-->


      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createData">确定</el-button>
        <el-button v-else type="primary" @click="updateData">确定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
  import { fetchList, createUser, updateUser } from '@/api/user'
  import { listshops,deleteshop,editshop } from '@/api/shop'
  import { createStorage, uploadPath,filePath } from '@/api/storage'
  import { mapGetters } from 'vuex'
  import axios from 'axios'
  import * as config from '../../../config'
  import {
    getAll,
    postData,
    getDataByID,
    putData,
    deleteData,
    getSearch
  } from '@/api/dbhelper'
  import VDistpicker from 'v-distpicker'
  import Pagination from '@/components/Pagination' // Secondary package based on el-pagination

  export default {
    name: 'Shoplist',
    components: { Pagination },
    data() {
      return {
        list: null,
        total: 0,
        uploadPath,
        filePath,
        passwordView: false,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 20,
          key: '',
          sort: 'add_time',
          order: 'desc'
        },
        index:0,
        selectData:{},//选中的数据
        passwordForm:{
          password:'',
        },
        loginForm:{
          username:'',
          password:''
        },
        dataForm: {
          id:'',
          name:'',
          mobilePhone:'',
          workTime:'',
          province:'',
          city:'',
          district:'',
          detail:'',
          balance:0,
          faceUrl:'',
          showUrl:''
        },
        id:'',
        levels:[],
        dialogFormVisible: false,
        dialogStatus: '',
        textMap: {
          update: '编辑',
          create: '创建'
        },
        rules: {
          username: [{ required: true, message: '用户名不能为空', trigger: 'blur' }],
          mobile: [{ required: true, message: '手机号码不能为空', trigger: 'blur' }],
          /* password: [{ required: true, message: '密码不能为空', trigger: 'blur' }*/
        },
        downloadLoading: false
      }
    },
    created() {
      this.getList()
    },
    computed: {
      ...mapGetters([
        'name'
      ])
    },
    methods: {
      getList() {

        axios({
          method: 'get',
          url: config.baseApi + "shop/find/all?page="+ (this.listQuery.page-1)+"&size=20",
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
      uploadfaceUrl: function(response) {
        if(response.code==0){
          this.dataForm.faceUrl =  this.filePath+"/"+ response.data.filePath
        }

      },
      uploadshowUrl: function(response) {
        if(response.code==0){
          this.dataForm.showUrl =  this.filePath+"/"+ response.data.filePath
        }

      },
      onSelected(data) {
        //alert(data.province + ' | ' + data.city + ' | ' + data.area)
        this.selectData = data
        console.log(data,844)
      },
      onChangeProvince(a){
        this.dataForm.province = a.value;
        console.log(a.value,911)
      },
      onChangeCity(a){
        this.dataForm.city = a.value
        console.log(a,922)
      },
      onChangeArea(a){
        this.dataForm.district = a.value
        console.log(a,933)
      },
      handleFilter() {
        this.listQuery.page = 1
        this.list=[]
        this.listLoading = true
        axios({
          method: 'get',
          url: config.baseApi + "shop/find/query?query="+this.listQuery.key+"&page="+ (this.listQuery.page-1)+"&size=20",
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
      resetForm() {
        this.dataForm = {
          id:'',
          name:'',
          mobilePhone:'',
          workTime:'',
          province:'',
          city:'',
          district:'',
          detail:'',
          balance:0,
          faceUrl:'',
          showUrl:''
        }
      },
      handleCreate() {
        this.resetForm()
        this.dialogStatus = 'create'
        this.dialogFormVisible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
        })
      },
      createData() {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {

            axios({
              method: 'post',
              url: config.baseApi + "shop/add",
              headers:{
                "X-Litemall-Admin-Token":sessionStorage.getItem('token')
              },
              data:this.dataForm
            }).then(response => {
              if(response.data.code==0){
                this.list.unshift(response.data.data)
                this.dialogFormVisible = false
                this.$notify.success({
                  title: '成功',
                  message: '添加成功'
                })
                this.listQuery.page = 1
                this.getList()
              }
            }).catch(error => {
              this.$notify.error({
                title: '失败',
                message: response.data.errmsg
              })
            });


          }
        })
      },
      handleUpdate(row) {

        this.dataForm = Object.assign({}, row)
        this.dialogStatus = 'update'
        this.dialogFormVisible = true

        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
        })
      },
      updateData() {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {

            axios({
              method: 'put',
              url: config.baseApi + "shop/update",
              headers:{
                "X-Litemall-Admin-Token":sessionStorage.getItem('token')
              },
              data:this.dataForm
            }).then(response => {
              if(response.data.code==0){
                for (const v of this.list) {
                  if (v.id === this.dataForm.id) {
                    const index = this.list.indexOf(v)
                    this.list.splice(index, 1, this.dataForm)
                    break
                  }
                }
                this.dialogFormVisible = false
                this.$notify.success({
                  title: '成功',
                  message: '更新酒庄成功'
                })

              }
            }).catch(error => {
              this.$notify.error({
                title: '失败',
                message: response.data.errmsg
              })
            });


          }
        })
      },
      deleteShop(){
        axios({
          method: 'get',
          url: config.baseApi + "admin/login?username="+this.loginForm.username+"&password="+this.loginForm.password,
        }).then(response => {
          console.log(response)
          if(response.data.code==0){
            var admin=response.data.data.items
            if(admin!=null&&admin.username===this.loginForm.username){
              console.log(this.id)
              var data = {id:this.id};
              axios({
                method: 'get',
                url: config.baseApi + "shop/delete?id="+this.id,
                headers:{
                  "X-Litemall-Admin-Token":sessionStorage.getItem('token')
                }
              }).then(response => {
                if(response.data.code==0){
                  this.passwordView=false
                  this.list.splice(this.index, 1)
                  this.$notify.success({
                    title: '成功',
                    message: '删除成功'
                  })
                }
              }).catch(error => {
                this.$notify.error({
                  title: '失败',
                  message: response.data.message
                })
              });

            }
            else{
              console.log(2)
              this.$notify.error({
                title: '失败',
                message: '密码错误'
              })
            }
          }
        }).catch(error => {
          console.log(3);
          this.$notify.error({
            title: '失败',
            message: error
          })
        });
      },
      handleDelete(row) {
        /*this.$notify.error({
          title: '警告',
          message: '用户删除操作不支持！'
        })*/
        this.id=row.id
        this.index = this.list.indexOf(row)
        this.passwordView=true
        var storage=window.localStorage;
        this.loginForm.username=storage["username"];
        this.$nextTick(() => {
          this.$refs['loginForm'].clearValidate()
        })

      }

    }
  }
</script>

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
</style>


