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
      <el-table-column align="center" width="100px" label="用户ID" prop="id" sortable/>

      <el-table-column align="center" label="用户" prop="userName"/>

      <el-table-column align="center" label="优惠券" prop="discountName"/>

      <el-table-column align="center" label="状态" prop="status"/>

      <el-table-column align="center" label="开始时间" prop="startTime"/>

      <el-table-column align="center" label="结束时间" prop="endTime"/>

      <el-table-column align="center" label="操作" width="200" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handleUpdate(scope.row)">编辑</el-button>
          <el-button type="danger" size="mini" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <!-- 添加或修改对话框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="dataForm" status-icon label-position="left" label-width="100px" style="width: 400px; margin-left:50px;">
        <el-form-item label="用户" prop="user">
          <el-select v-model="dataForm.user">
            <el-option v-for="item in users" :key="item.id" :label="item.name" :value="item.id"/>
          </el-select>
        </el-form-item>

        <el-form-item label="优惠券" prop="discount">
          <el-select v-model="dataForm.discount">
            <el-option v-for="item in discounts" :key="item.id" :label="item.name" :value="item.id"/>
          </el-select>
        </el-form-item>

        <el-form-item label="状态" prop="status">
          <el-select v-model="dataForm.status">
            <el-option v-for="item in statuses" :key="item" :label="item" :value="item"/>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-col :span="11">
            <el-date-picker v-model="dataForm.startTime" type="datetime"  format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss"  placeholder="开始时间" style="width: 100%;"/>
          </el-col>
          <el-col :span="2" class="line">至</el-col>
          <el-col :span="11">
            <el-date-picker v-model="dataForm.endTime" type="datetime"  format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss"  placeholder="结束时间" style="width: 100%;"/>
          </el-col>
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

<script>
import { fetchList, createUser, updateUser } from '@/api/user'
import { listshops,deleteshop,editshop } from '@/api/shop'
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

import Pagination from '@/components/Pagination' // Secondary package based on el-pagination

export default {
  name: 'Coupon',
  filters: {
    shareholderIdFilter(shareholderId) {
      axios({
        method: 'get',
        url: config.baseApi + "user/find/id?id="+shareholderId,
        headers:{
          "X-Litemall-Admin-Token":sessionStorage.getItem('token')
        }
      }).then(response => {
        if(response.data.code==0){
          return response.data.data.user.name
        }
      }).catch(error => {

      });

    }
  },
  components: { Pagination },
  data() {
    return {
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
      dataForm: {
        id: '',
        user: '',
        discount: '',
        status: '',
        startTime: '',
        endTime: ''
      },
      statuses: ['未使用','已使用','已过期'],
      users:[],
      discounts:[],
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
      url: config.baseApi + "coupon/find/all?page="+ (this.listQuery.page-1)+"&size=20",
      headers:{
        "X-Litemall-Admin-Token":sessionStorage.getItem('token')
      }
    }).then(response => {
      if(response.data.code==0){
        this.list = response.data.data.items.content
        for(let i=0;i<this.list.length;i++){
          axios({
            method: 'get',
            url: config.baseApi + "user/find/id?id="+ this.list[i].user,
            headers:{
              "X-Litemall-Admin-Token":sessionStorage.getItem('token')
            }
          }).then(res => {
            this.list[i].userName = res.data.data.items.name

          }).catch(error => {
          });
          axios({
            method: 'get',
            url: config.baseApi + "discount/find/id?id="+ this.list[i].discount,
            headers:{
              "X-Litemall-Admin-Token":sessionStorage.getItem('token')
            }
          }).then(res => {
            console.log(res.data.data.items.name)
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

    axios({
      method: 'get',
      url: config.baseApi + "user/find/identity?identity=member&page="+ (this.listQuery.page-1)+"&size=100",
      headers:{
        "X-Litemall-Admin-Token":sessionStorage.getItem('token')
      }
    }).then(response => {

      this.users = response.data.data.items.content

    }).catch(error => {
    });

    axios({
      method: 'get',
      url: config.baseApi + "discount/find/all?&page="+ (this.listQuery.page-1)+"&size=20",
      headers:{
        "X-Litemall-Admin-Token":sessionStorage.getItem('token')
      }
    }).then(response => {

      this.discounts = response.data.data.items.content

    }).catch(error => {
    });

    },
    handleFilter() {
      this.listQuery.page = 1
      this.list=[]
      this.listLoading = true
      axios({
        method: 'get',
        url: config.baseApi + "coupon/find/query?query="+this.listQuery.key+"&page="+ (this.listQuery.page-1)+"&size=20",
        headers:{
          "X-Litemall-Admin-Token":sessionStorage.getItem('token')
        }
      }).then(response => {
        if(response.data.code==0){
          this.list = response.data.data.items.content
          for(let i=0;i<this.list.length;i++){
            axios({
              method: 'get',
              url: config.baseApi + "user/find/id?id="+ this.list[i].user,
              headers:{
                "X-Litemall-Admin-Token":sessionStorage.getItem('token')
              }
            }).then(res => {
              this.list[i].userName = res.data.data.items.name

            }).catch(error => {
            });
            axios({
              method: 'get',
              url: config.baseApi + "discount/find/id?id="+ this.list[i].discount,
              headers:{
                "X-Litemall-Admin-Token":sessionStorage.getItem('token')
              }
            }).then(res => {
              console.log(res.data.data.items.name)
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
        user: '',
        discount: '',
        status: '',
        startTime: '',
        endTime: ''
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
            url: config.baseApi + "coupon/add",
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
            url: config.baseApi + "coupon/update",
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
                  axios({
                    method: 'get',
                    url: config.baseApi + "user/find/id?id="+ this.dataForm.user,
                    headers:{
                      "X-Litemall-Admin-Token":sessionStorage.getItem('token')
                    }
                  }).then(res => {

                    this.list[index].userName = res.data.data.items.name

                  }).catch(error => {
                  });
                  axios({
                    method: 'get',
                    url: config.baseApi + "discount/find/id?id="+ this.dataForm.discount,
                    headers:{
                      "X-Litemall-Admin-Token":sessionStorage.getItem('token')
                    }
                  }).then(res => {
                    this.list[index].discountName = res.data.data.items.name
                  }).catch(error => {
                  });
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
        url: config.baseApi + "coupon/delete?id="+row.id,
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
