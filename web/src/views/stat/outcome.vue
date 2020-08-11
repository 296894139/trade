<template>
  <div class="app-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <span>酒庄选择</span>

      <el-select v-model="shopId" @change="changeShop">
        <el-option v-for="item in shopIds" :key="item.id" :label="item.name" :value="item.id"/>
      </el-select>

      <el-input v-model="listQuery.key" clearable class="filter-item" style="width: 200px;margin-left: 300px;" placeholder="请输入关键词"/>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">查找</el-button>
      <el-button :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="handleDownload">导出</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table v-loading="listLoading" :data="list" size="small" element-loading-text="正在查询中。。。" border fit highlight-current-row>

      <el-table-column align="center" label="id" prop="id"/>

      <el-table-column align="center" label="酒庄名称" prop="name"/>

      <el-table-column align="center" label="价格" prop="price"/>

      <el-table-column align="center" label="详细内容" prop="detail"/>

      <el-table-column align="center" label="时间" prop="time"/>

      <el-table-column align="center" label="所购买商品名称列表" prop="goodsList"/>

      <el-table-column align="center" label="操作" width="200" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="danger" size="mini" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <!-- 添加或修改对话框 -->
    <el-dialog customClass="customWidth" :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="dataForm" status-icon label-position="left" label-width="100px" style=" margin-left:50px;">
        <el-form-item label="用户" prop="userId">
          <el-select v-model="dataForm.userId">
            <el-option v-for="item in users" :key="item.id" :label="item.name" :value="item.id"/>
          </el-select>
        </el-form-item>

        <el-form-item label="送货地址" prop="address">
          <el-input v-model="dataForm.address"/>
        </el-form-item>

        <el-form-item label="联系手机" prop="mobilePone">
          <el-input v-model="dataForm.mobilePone"/>
        </el-form-item>

        <el-form-item label="收货人" prop="person">
          <el-input v-model="dataForm.person"/>
        </el-form-item>

        <el-form-item label="运费" prop="freight">
          <el-input v-model="dataForm.freight"/>
        </el-form-item>

        <el-form-item label="总价" prop="price">
          <el-input v-model="dataForm.price"/>
        </el-form-item>

        <el-form-item label="优惠" prop="discountPrice">
          <el-input v-model="dataForm.discountPrice"/>
        </el-form-item>

        <el-form-item label="购买时间" prop="BuyTime">
          <el-input v-model="dataForm.BuyTime"/>
        </el-form-item>

        <el-form-item label="状态">
          <el-select v-model="dataForm.status">
            <el-option v-for="item in statuses" :key="item" :label="item" :value="item"/>
          </el-select>
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
    name: 'Order',
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
        shopId:'',
        shopIds:[],
        dataForm: {
          id: '',
          userId: '',
          address: '',
          mobilePone: '',
          person: '',
          freight: 0,
          price: 0,
          discountPrice: 0,
          goodsList: [],
          BuyTime: '',
          status: ''
        },
        statuses:['待付款','待发货','待收货','待评价','已完成'],
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
    methods: {
      getList() {
        axios({
          method: 'get',
          url: config.baseApi + "shopBalance/find/type?type=支出",
          headers:{
            "X-Litemall-Admin-Token":sessionStorage.getItem('token')
          }
        }).then(response => {
          if(response.data.code==0){
            this.list = response.data.data.items
            this.listLoading = false
          }
        }).catch(error => {
          this.list = []
          this.total = 0
          this.listLoading = false
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
      handleDownload() {
        this.downloadLoading = true
        import('@/vendor/Export2Excel').then(excel => {
          const tHeader = ['id', '酒庄名称', '价格', '详细内容', '时间','所购买商品名称列表']
          const filterVal = ['id', 'name', 'price', 'detail', 'time', 'goodsList']
          excel.export_json_to_excel2(tHeader, this.list, filterVal, '酒庄支出信息')
          this.downloadLoading = false
        })
      },

      changeShop(){
        var shopId=this.shopId
        axios({
          method: 'get',
          url: config.baseApi + "shopBalance/find/type/shopId?type=支出&shopId="+this.shopId,
          headers:{
            "X-Litemall-Admin-Token":sessionStorage.getItem('token')
          }
        }).then(response => {
          this.list = response.data.data.items
          this.listLoading = false
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
          url: config.baseApi + "order/find/query?query="+this.listQuery.key+"&page="+ (this.listQuery.page-1)+"&size=20",
          headers:{
            "X-Litemall-Admin-Token":sessionStorage.getItem('token')
          }
        }).then(response => {
          if(response.data.code==0){
            this.list = response.data.data.items.content
            for(let i=0;i<this.list.length;i++){
              axios({
                method: 'get',
                url: config.baseApi + "goods/find/id?id="+ this.list[i].shopId,
                headers:{
                  "X-Litemall-Admin-Token":sessionStorage.getItem('token')
                }
              }).then(res => {
                this.list[i].goods = res.data.data.items

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
          userId: '',
          address: '',
          mobilePone: '',
          person: '',
          freight: 0,
          price: 0,
          discountPrice: 0,
          goodsList: [],
          BuyTime: '',
          status: ''
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
              url: config.baseApi + "order/add",
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
                  message: '添加商品成功'
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
              url: config.baseApi + "order/update",
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
                  message: '更新商品成功'
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
          url: config.baseApi + "shopBalance/delete?id="+row.id,
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


