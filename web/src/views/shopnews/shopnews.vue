
<template>
  <div class="app-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-button class="filter-item" type="primary" icon="el-icon-edit" @click="handleCreate">添加</el-button>
      <!--<el-button :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="handleDownload">导出</el-button>-->
    </div>

    <!-- 查询结果 -->
    <el-table  :data="list" size="small" border fit highlight-current-row>

      <el-table-column align="center" label="新闻标题" prop="title"/>

      <el-table-column align="center" min-width="100" label="新闻内容" prop="content"/>
  
      <el-table-column align="center" min-width="100" label="发表时间" prop="addTime"/>
  
    
   
      
 


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
  <el-form ref="editForm">
    	<el-form-item label="新闻标题" >
         <el-input v-model="editForm.title" style="width: 200px;"/>
        		</el-form-item>
            <el-form-item label="新闻内容" >
      <el-input
  type="textarea"
  :rows="4"
  placeholder="请输入内容"
  v-model="editForm.content">
</el-input>
        </el-form-item>

        	<el-form-item label="上传图片">
					<span class="tip-pic">只能上传jpg/png文件，至多5张，且不超过500kb</span>
				  <el-upload
				    :action="uploadPath"
				    :limit="5"
				    :headers="headers"
                     :file-list="galleryFileList"
				    :on-exceed="uploadOverrun"
				    :on-success="handleGalleryUrl"
				    :on-remove="handleRemove"
				    multiple
				    accept=".jpg,.jpeg,.png,.gif"
				    list-type="picture-card">
				    <i class="el-icon-plus"/>
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
import { createStorage, uploadPath,filePath } from '@/api/storage'
import BackToTop from '@/components/BackToTop'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import { mapGetters } from "vuex";
import { getToken } from '@/utils/auth'
import {
  getAll,
  postData,
  getDataByID,
  putData,
  deleteData,
  deleteByID,
	getSearch
} from '@/api/dbhelper'
const statusMap = {
  0: '商品',
  1: '直播间',
 
}

export default {
  name: 'shopnews',
  components: { BackToTop, Pagination },
  filters: {
    moduleStatusFilter(status) {
      return statusMap[status]
    }
  },
  data() {
    return {
        uploadPath,
        filePath,
        total:0,
      dialogStatus:'',
         textMap: {
        update: '编辑',
        create: '创建'
      },
      galleryFileList:[],
      dialogVisible:false,
      editForm:{
        id:'',
        name:'',
        type:'',
        shopId:''
      },
      isYesNo:'',//显示直播间或者商品
      list: [
        {
        "name":"今日特价",
        "data":"商品"
      },
         {
        "name":"潮流穿搭",
        "data":"直播间"
      },
         {
        "name":"居家生活",
        "data":"商品"
      }
      ],
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
      statusMap
    }
  },
  	  computed: {
				  ...mapGetters(["shopId","roles"]),
    headers() {
      return {
        'X-Litemall-Admin-Token': getToken()
      }
    }
  },
  
  created() {  
    this.listQuery.shopId = this.shopId;
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true  //listShopGoods
    
      getAll("news/list",this.listQuery).then(response=>{
        if(response.data.errno==0){
             this.list = response.data.data.items
        this.total = response.data.data.total
      //  this.listLoading = false
        }
          console.log(response,'74447')
      })
    },
        handleGalleryUrl(response, file, fileList) {
			console.log(response)
      if (response.errno === 0) {
          var url = response.data.url.replace("http://localhost:8080/wx/storage/fetch",this.filePath);
        this.galleryFileList.push(url)
      }
			console.log( this.galleryFileList,8788)
    },
        uploadOverrun: function() {
      this.$message({
        type: 'error',
        message: '上传文件个数超出限制!最多上传5张图片!'
      })
    },
      handleRemove: function(file, fileList) {
      for (var i = 0; i < this.editForm.editForm.length; i++) {
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

        if (this.goods.gallery[i] === url) {
          this.goods.gallery.splice(i, 1)
        }
      }
    },
    handleClose(){
    this.dialogVisible = false
    },
    //提交编辑
    handleSubmit(){
      putData("news/edit",this.editForm).then(response=>{
        if(response.data.errno==0){
          this.$notify.success({
          title: '成功',
          message: '修改成功'
        })
          this.getList();
        }
        console.log("dddd",response)
      })
       this.dialogVisible = false
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
      this.editForm.title=''
      this.editForm.content='',
   // this.editForm.
        this.galleryFileList = []
    },
    handleCreateData() {
        this.editForm.picUrl  = this.galleryFileList
        this.editForm.shopId = this.shopId
     postData("news/add",this.editForm).then(response=>{
       if(response.data.errno===0){
       console.log(response,'addd')
       this.getList();
       }
          this.dialogVisible = false
     })
    },
    handleUpdate(row) {
      this.dialogStatus  = 'update'
      this.dialogVisible = true
      this.editForm = row
         this.galleryFileList = []
        for (var i = 0; i < row.picUrl.length; i++) {
          this.galleryFileList.push({
            url: row.picUrl[i],
          })
        }
   //   this.editForm.id = row.id;
    // this.editForm.type = row.type
      //this.$router.push({ path: '/goods/edit', query: { id: row.id }})
    },
    showDetail(detail) {
      this.goodsDetail = detail
      this.detailDialogVisible = true
    },
    handleDelete(row) {
      deleteByID("Module/delete",row).then(response=>{
        console.log(response,'444')
        if(response.data.errno==0){
    this.$notify.success({
          title: '成功',
          message: '删除成功'
        })
     
        const index = this.list.indexOf(row)
        this.list.splice(index, 1)
        this.total = this.total-1
           }
      })
      
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
