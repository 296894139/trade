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

      <el-table-column align="center" width="100px" label="ID" prop="id" sortable/>

      <el-table-column align="center" label="内容" prop="content"/>

      <el-table-column align="center" label="发布时间" prop="time"/>

      <el-table-column align="center" label="投诉者用户名" prop="username"/>


      <el-table-column align="center" label="操作" width="200" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handleUpdate(scope.row)">编辑</el-button>
          <el-button type="danger" size="mini" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <!-- 添加或修改对话框 -->
    <el-dialog customClass="customWidth" :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="dataForm" status-icon label-position="left" label-width="100px" style=" margin-left:50px;">
        <el-form-item label="投诉者用户名" prop="username">
          <el-input v-model="dataForm.username"/>
        </el-form-item>


        <el-form-item label="内容">
          <el-input type="textarea" v-model="dataForm.content"/>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createData">确定</el-button>
        <el-button v-else type="primary" @click="updateData">确定</el-button>
      </div>
    </el-dialog>

  </div>
</template>


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
  .customWidth{
    width:80%;
  }
</style>

<script>
import { createStorage, uploadPath,filePath } from '@/api/storage'
import { uploadDetailPic } from '@/api/upload'
import axios from 'axios'
import * as config from '../../../config'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination


export default {
    name: 'List',
    components: { Pagination },
    data() {
      return {
        uploadPath,
        filePath,
        list: null,
        total: 0,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 20,
          key: '',
          sort: 'add_time',
          order: 'desc'
        },
        passwordForm:{
          password:'',
        },
        shopIds:[],
        dataForm: {
          id: '',
          content: '',
          time: '',
          username: ''
        },
        discount:[],
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
        shopId:'',
        shopIds:[],
        downloadLoading: false
      }
    },

    created() {
      this.getList()
    },
    methods: {
      getList() {
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

        axios({
          method: 'get',
          url: config.baseApi + "complain/find/all?page="+ (this.listQuery.page-1)+"&size=20",
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
      changeShop() {
        var shopId = this.shopId

        axios({
          method: 'get',
          url: config.baseApi + "complain/find/shopId?shopId="+shopId+"&page="+ (this.listQuery.page-1)+"&size=20",
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
      uploadImageUrl: function(response) {
        if(response.code==0){
          this.dataForm.image =  this.filePath+"/"+ response.data.filePath
        }
      },
      handleFilter() {
        this.listQuery.page = 1
        this.list=[]
        this.listLoading = true
        axios({
          method: 'get',
          url: config.baseApi + "complain/find/query?query="+this.listQuery.key+"&page="+ (this.listQuery.page-1)+"&size=20",
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
          id: '',
          content: '',
          time: '',
          username: ''
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
      getTime:function(){
        let yy = new Date().getFullYear();
        let mm = new Date().getMonth()+1;
        let dd = new Date().getDate();
        let hh = new Date().getHours();
        let mf = new Date().getMinutes()<10 ? '0'+new Date().getMinutes() : new Date().getMinutes();
        let ss = new Date().getSeconds()<10 ? '0'+new Date().getSeconds() : new Date().getSeconds();
        return yy+'-'+mm+'-'+dd+' '+hh+':'+mf+':'+ss;

      },
      createData() {
        this.dataForm.time=this.getTime()
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {

            axios({
              method: 'post',
              url: config.baseApi + "complain/add",
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
                this.changeShop()
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
              url: config.baseApi + "complain/update",
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
                  message: '更新成功'
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
      handleDelete(row) {
        /*this.$notify.error({
          title: '警告',
          message: '用户删除操作不支持！'
        })*/
        var data = {id:row.id};
        axios({
          method: 'get',
          url: config.baseApi + "complain/delete?id="+row.id,
          headers:{
            "X-Litemall-Admin-Token":sessionStorage.getItem('token')
          }
        }).then(response => {
          if(response.data.code==0){
            const index = this.list.indexOf(row)
            this.list.splice(index, 1)
            this.$notify.success({
              title: '成功',
              message: '删除成功'
            })

          }
        }).catch(error => {
          this.$notify.error({
            title: '失败',
            message: response.data.errmsg
          })
        });


      }

    }
  }
</script>


