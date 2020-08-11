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
      <el-table-column type="expand">
        <template slot-scope="props">
          <el-form label-position="left" class="table-expand">
            <el-form-item label="积分商品封面图">
                <img v-if="props.row.imageUrl" :src="props.row.imageUrl" width="40">
            </el-form-item>
            <el-form-item label="积分商品轮播图">
              <img v-for="pic in props.row.swiperImgs" :key="pic" :src="pic" width="40">
            </el-form-item>
            <el-form-item label="积分商品详情">
              <span>{{ props.row.detail }}</span>
            </el-form-item>

          </el-form>
        </template>
      </el-table-column>

      <el-table-column align="center" width="100px" label="商品ID" prop="id" sortable/>

      <el-table-column align="center" label="商品名称" prop="name"/>

      <el-table-column align="center" label="商品简介" prop="brief"/>

      <el-table-column align="center" label="所需积分" prop="integral"/>

      <el-table-column align="center" label="商品规格" prop="standard"/>

      <el-table-column align="center" label="剩余数量" prop="number"/>

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
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="dataForm.name"/>
        </el-form-item>

        <el-form-item label="商品简介" prop="brief">
          <el-input v-model="dataForm.brief"/>
        </el-form-item>

        <el-form-item label="所需积分" prop="integral">
          <el-input v-model="dataForm.integral"/>
        </el-form-item>


        <el-form-item label="商品规格" prop="standard">
          <el-input v-model="dataForm.standard"/>
        </el-form-item>

        <el-form-item label="剩余数量" prop="number">
          <el-input v-model="dataForm.number"/>
        </el-form-item>


        <el-form-item label="积分商品封面图">
          <span class="tip-pic">只能上传jpg/png文件，只有1张，且不超过500kb</span>
          <el-upload
            :action="uploadPath"
            :show-file-list="false"
            :on-success="uploadImageUrl"
            class="avatar-uploader"
            accept=".jpg,.jpeg,.png,.gif">
            <img v-if="dataForm.imageUrl" :src="dataForm.imageUrl" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"/>
          </el-upload>
        </el-form-item>

        <el-form-item label="积分商品轮播图">
          <span class="tip-pic">只能上传jpg/png文件，至多5张，且不超过500kb</span>
          <el-upload
            :action="uploadPath"
            :limit="5"
            :on-exceed="uploadOverrun"
            :on-success="handleSwiperUrl"
            :on-remove="handleRemove"
            multiple
            accept=".jpg,.jpeg,.png,.gif"
            list-type="picture-card">
            <i class="el-icon-plus"/>
          </el-upload>
        </el-form-item>

        <el-form-item label="积分商品详细介绍">
          <editor :init="editorInit" v-model="dataForm.detail"/>
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
import Editor from '@tinymce/tinymce-vue'
import axios from 'axios'
import * as config from '../../../config'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination


export default {
    name: 'IntegralGoods',
    components: { Pagination,Editor },
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
          name: '',
          brief: '',
          integral: 0,
          standard:'',
          number: 0,
          imageUrl: '',
          swiperImgs: [],
          detail: ''
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
        editorInit: {
          language: 'zh_CN',
          convert_urls: false,
          plugins: ['advlist anchor autolink autosave code codesample colorpicker colorpicker contextmenu directionality emoticons fullscreen hr image imagetools importcss insertdatetime link lists media nonbreaking noneditable pagebreak paste preview print save searchreplace spellchecker tabfocus table template textcolor textpattern visualblocks visualchars wordcount'],
          toolbar: ['searchreplace bold italic underline strikethrough alignleft aligncenter alignright outdent indent  blockquote undo redo removeformat subscript superscript code codesample', 'hr bullist numlist link image charmap preview anchor pagebreak insertdatetime media table emoticons forecolor backcolor fullscreen'],
          images_upload_handler: function(blobInfo, success, failure) {
            const formData = new FormData()
            formData.append('file', blobInfo.blob())

            axios.post('https://www.shaoshanlu.com:5010/upload', formData).then(res => {
              success('https://www.shaoshanlu.com/'+res.data.data.filePath)
            }).catch(res => {
              failure('error')
            })


          }
        },
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
          url: config.baseApi + "integragoods/find/all?page="+ (this.listQuery.page-1)+"&size=20",
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
          this.dataForm.imageUrl =  this.filePath+"/"+ response.data.filePath
        }
      },
      uploadOverrun: function() {
        this.$message({
          type: 'error',
          message: '上传文件个数超出限制!最多上传5张图片!'
        })
      },
      handleSwiperUrl(response, file, fileList) {

        if (response.code === 0) {
          this.dataForm.swiperImgs.push( this.filePath+"/"+ response.data.filePath)
        }
      },
      handleRemove: function(file, fileList) {
        for (var i = 0; i < this.goods.swiperImgs.length; i++) {
          // 这里存在两种情况
          // 1. 如果所删除图片是刚刚上传的图片，那么图片地址是file.response.data.url
          //    此时的file.url虽然存在，但是是本机地址，而不是远程地址。
          // 2. 如果所删除图片是后台返回的已有图片，那么图片地址是file.url
          var url
          if (file.response === undefined) {
            url = file.url
          } else {
            url = file.response.data.url
          }

          if (this.goods.swiperImgs[i] === url) {
            this.goods.swiperImgs.splice(i, 1)
          }
        }
      },
      handleFilter() {
        this.listQuery.page = 1
        this.list=[]
        this.listLoading = true
        axios({
          method: 'get',
          url: config.baseApi + "integragoods/find/query?query="+this.listQuery.key+"&page="+ (this.listQuery.page-1)+"&size=20",
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
          name: '',
          brief: '',
          integral: 0,
          standard: '',
          number: 0,
          imageUrl: '',
          swiperImgs: [],
          detail: ''
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
              url: config.baseApi + "integragoods/add",
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
                  message: '添加积分商品成功'
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
              url: config.baseApi + "integragoods/update",
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
                  message: '更新积分商品成功'
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
          url: config.baseApi + "integragoods/delete?id="+row.id,
          headers:{
            "X-Litemall-Admin-Token":sessionStorage.getItem('token')
          }
        }).then(response => {
          if(response.data.code==0){
            const index = this.list.indexOf(row)
            this.list.splice(index, 1)
            this.$notify.success({
              title: '成功',
              message: '删除积分商品成功'
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


