<template>
  <div class="app-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <span>分类选择</span>
      <el-select v-model="shopId" @change="changeShop">
        <el-option v-for="item in shopIds" :key="item.id" :label="item.name" :value="item.id"/>
      </el-select>

      <el-input v-model="listQuery.key" clearable class="filter-item" style="width: 200px;margin-left: 300px;" placeholder="请输入关键词"/>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">查找</el-button>
      <el-button class="filter-item" type="primary" icon="el-icon-edit" @click="handleCreate">添加</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table v-loading="listLoading" :data="list" size="small" element-loading-text="正在查询中。。。" border fit highlight-current-row>
      <el-table-column type="expand">
        <template slot-scope="props">
          <el-form label-position="left" class="table-expand">
            <el-form-item label="封面图">
              <img v-if="props.row.imageUrl" :src="props.row.imageUrl" width="40">
            </el-form-item>
            <!--<el-form-item label="商品轮播图">-->
            <!--<img v-for="pic in props.row.swiperImgs" :key="pic" :src="pic" width="40">-->
            <!--</el-form-item>-->
            <el-form-item label="活动详情">
              <span>{{ props.row.detail }}</span>
            </el-form-item>

            <!--<el-form-item label="颜色">-->
            <!--<ul v-for="(item,index) in props.row.color" :key="index">-->
            <!--<li>{{item}}</li>-->
            <!--</ul>-->
            <!--</el-form-item>-->

            <!--<el-form-item label="尺寸">-->
            <!--<ul v-for="(item,index) in props.row.size" :key="index">-->
            <!--<li>{{item}}</li>-->
            <!--</ul>-->
            <!--</el-form-item>-->

          </el-form>
        </template>
      </el-table-column>

      <el-table-column align="center" width="100px" label="商品ID" prop="id" sortable/>

      <el-table-column align="center" label="活动标题" prop="name"/>

      <el-table-column align="center" label="活动简介" prop="brief"/>

      <el-table-column align="center" label="行业" prop="standard"/>

      <el-table-column align="center" label="发布时间" prop="discountId"/>

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
        <el-form-item label="活动标题" prop="name">
          <el-input v-model="dataForm.name"/>
        </el-form-item>


        <el-form-item label="活动简介" prop="brief">
          <el-input v-model="dataForm.brief"/>
        </el-form-item>


        <el-form-item label="封面图">
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



        <el-form-item label="活动详情">
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
    name: 'List',
    components: { Pagination,Editor },
    data() {
      return {
        uploadPath,
        filePath,
        list: null,
        total: 0,
        listLoading: true,
        fileList: [],
        colorVisibile:false,
        color:"",
        sizeVisibile:false,
        size:"",
        colorUrl:"",
        sizeUrl:"",
        colorArray:[],
        sizeArray:[],
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
        shopId:'',
        shopIds:[],
        dataForm: {
          id: '',
          name: '',
          link: '',
          brief: '',
          price: 0,
          memberPrice: 0,
          stockPrice: 0,
          freight: 0,
          standard: '',
          number: 0,
          sales: 0,
          imageUrl: '',
          swiperImgs: [],
          detail: '',
          discountId:'',
          shopId:'activity',
          color:[],
          size: []
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
          url: config.baseApi + "goods/find/ShopId?ShopId=activity&page="+ (this.listQuery.page-1)+"&size=20",
          headers:{
            "X-Litemall-Admin-Token":sessionStorage.getItem('token')
          }
        }).then(response => {
          if(response.data.code==0){
            this.list = response.data.data.items.content
            // for(let i=0;i<this.list.length;i++){
            //   axios({
            //     method: 'get',
            //     url: config.baseApi + "shop/find/id?id="+ this.list[i].shopId,
            //     headers:{
            //       "X-Litemall-Admin-Token":sessionStorage.getItem('token')
            //     }
            //   }).then(res => {
            //     console.log(res.data.data.items.name)
            //     this.list[i].shopName = res.data.data.items.name
            //
            //   }).catch(error => {
            //   });
            //
            //
            // }
            this.total = response.data.data.items.totalElements//response.data.data.total
            this.listLoading = false
          }
        }).catch(error => {
          this.list = []
          this.total = 0
          this.listLoading = false
        });

        axios({
          method: 'get',
          url: config.baseApi + "shop/find/all?page="+ (this.listQuery.page-1)+"&size=100",
          headers:{
            "X-Litemall-Admin-Token":sessionStorage.getItem('token')
          }
        }).then(response => {

          this.shopIds = response.data.data.items.content

        }).catch(error => {
        });

      },

      uploadImageUrl: function(response) {
        if(response.code==0){
          this.dataForm.imageUrl =  this.filePath+"/"+ response.data.filePath
        }
      },
      uploadColor: function(response) {
        if(response.code==0){
          this.colorUrl =  this.filePath+"/"+ response.data.filePath
        }
      },
      uploadSize: function(response) {
        if(response.code==0){
          this.sizeUrl =  this.filePath+"/"+ response.data.filePath
        }
      },
      uploadOverrun: function() {
        this.$message({
          type: 'error',
          message: '上传文件个数超出限制!最多上传10张图片!'
        })
      },
      handleSwiperUrl(response, file, fileList) {
        if (response.code === 0) {
          this.dataForm.swiperImgs.push( this.filePath+"/"+ response.data.filePath)
        }
      },
      handleRemove: function(file, fileList) {
        for (var i = 0; i < this.dataForm.swiperImgs.length; i++) {
          // 这里存在两种情况
          // 1. 如果所删除图片是刚刚上传的图片，那么图片地址是file.response.data.url
          //    此时的file.url虽然存在，但是是本机地址，而不是远程地址。
          // 2. 如果所删除图片是后台返回的已有图片，那么图片地址是file.url
          var url
          console.log('！')

          console.log(file)
          if (file.response === undefined) {
            url = file.url
          } else {
            url = this.filePath+file.response.data.filePath
          }
          console.log(this.dataForm.swiperImgs)
          console.log(url)
          if (this.dataForm.swiperImgs[i] === url) {
            this.dataForm.swiperImgs.splice(i, 1)
          }
        }
      },
      changeShop(){
        var shopId=this.shopId
        axios({
          method: 'get',
          url: config.baseApi + "goods/find/ShopId?ShopId="+this.shopId+"&page="+ (this.listQuery.page-1)+"&size=20",
          headers:{
            "X-Litemall-Admin-Token":sessionStorage.getItem('token')
          }
        }).then(response => {
          if(response.data.code==0){
            this.list = response.data.data.items.content
            for(let i=0;i<this.list.length;i++){
              axios({
                method: 'get',
                url: config.baseApi + "shop/find/id?id="+ this.list[i].shopId,
                headers:{
                  "X-Litemall-Admin-Token":sessionStorage.getItem('token')
                }
              }).then(res => {
                console.log(res.data.data.items.name)
                this.list[i].shopName = res.data.data.items.name

              }).catch(error => {
              });
            }
            this.total = response.data.data.items.totalElements//response.data.data.total
            this.listLoading = false
          }
        }).catch(error => {
          this.list = []
          this.total = 0
          this.listLoading = false
        });
      },
      handleFilter() {
        this.listQuery.page = 1
        this.list=[]
        this.listLoading = true
        axios({
          method: 'get',
          url: config.baseApi + "goods/find/query?query="+this.listQuery.key+"&page="+ (this.listQuery.page-1)+"&size=20",
          headers:{
            "X-Litemall-Admin-Token":sessionStorage.getItem('token')
          }
        }).then(response => {
          if(response.data.code==0){
            this.list = response.data.data.items.content
            for(let i=0;i<this.list.length;i++){
              axios({
                method: 'get',
                url: config.baseApi + "shop/find/id?id="+ this.list[i].shopId,
                headers:{
                  "X-Litemall-Admin-Token":sessionStorage.getItem('token')
                }
              }).then(res => {
                console.log(res.data.data.items.name)
                this.list[i].shopName = res.data.data.items.name

              }).catch(error => {
              });

              axios({
                method: 'get',
                url: config.baseApi + "discount/find/id?id="+ this.list[i].level,
                headers:{
                  "X-Litemall-Admin-Token":sessionStorage.getItem('token')
                }
              }).then(res => {

                this.list[i].discountName = res.data.data.items.name

              }).catch(error => {
              });

            }
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
          link: '',
          price: 0,
          memberPrice: 0,
          stockPrice: 0,
          freight: 0,
          standard: '',
          number: 0,
          sales: 0,
          imageUrl: '',
          swiperImgs: [],
          detail: '',
          discountId:'',
          shopId:'activity',
          color:[],
          size:[]
        }
      },
      handleCreate() {
        this.resetForm()
        this.fileList=[]
        this.dialogStatus = 'create'
        this.dialogFormVisible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
        })
      },
      createData() {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            for(var i=0;i<this.colorArray.length;i++){
              this.dataForm.color.push(this.colorArray[i].id)
            }
            for(var i=0;i<this.sizeArray.length;i++){
              this.dataForm.size.push(this.sizeArray[i].id)
            }
            axios({
              method: 'post',
              url: config.baseApi + "goods/add",
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
        this.fileList=[]
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
              url: config.baseApi + "goods/update",
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
          url: config.baseApi + "goods/delete?id="+row.id,
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


