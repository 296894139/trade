<template>
  <div class="table">
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item><i class="el-icon-lx-cascades"/>商家列表</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container">
      <div class="handle-box">
        <el-input v-model="select_word" placeholder="筛选关键词" class="handle-input mr10"/>
        <el-button type="primary" icon="search" @click="search">搜索</el-button>
        <el-button type="primary" icon="add" @click="insert">增加</el-button>
        <tr v-for="item in form">
          <td>{{ item.name }}</td>
          <td>{{ item.data }}</td>
          <td>{{ item.address }}</td>
        </tr>
      </div>
      <el-table ref="multipleTable" :data="data" border class="table" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column prop="date" label="日期" sortable width="150"/>
        <el-table-column prop="name" label="商品名称" width="120"/>
        <el-table-column :formatter="formatter" prop="address" label="商品信息"/>
        <el-table-column label="操作" width="180" align="center">
          <template slot-scope="scope">
            <el-button type="text" icon="el-icon-edit" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
            <el-button type="text" icon="el-icon-delete" class="red" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination :total="1000" background layout="prev, pager, next" @current-change="handleCurrentChange"/>
      </div>
    </div>
    <!-- 编辑弹出框 -->
    <el-dialog :visible.sync="editVisible" title="编辑" width="30%">
      <el-form ref="form" :model="form" label-width="50px">
        <el-form-item label="日期">
          <el-date-picker v-model="form.date" type="date" placeholder="选择日期" value-format="yyyy-MM-dd" style="width: 100%;"/>
        </el-form-item>
        <el-form-item label="商品名称">
          <el-input v-model="form.name"/>
        </el-form-item>
        <el-form-item label="商品信息">
          <el-input v-model="form.address"/>
        </el-form-item>

      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveEdit">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 删除提示框 -->
    <el-dialog :visible.sync="delVisible" title="提示" width="300px" center>
      <div class="del-dialog-cnt">删除不可恢复，是否确定删除？</div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="delVisible = false">取 消</el-button>
        <el-button type="primary" @click="deleteRow">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'Manage',
  data() {
    return {
      url: '',
      tableData: [],
      cur_page: 1,
      multipleSelection: [],
      select_cate: '',
      select_word: '',
      del_list: [],
      is_search: false,
      editVisible: false,
      delVisible: false,
      form: [{
        name: '',
        date: '',
        address: ''
      }],
      idx: -1
    }
  },
  computed: {
    data() {
      return this.tableData.filter((d) => {
        let is_del = false
        for (let i = 0; i < this.del_list.length; i++) {
          if (d.name === this.del_list[i].name) {
            is_del = true
            break
          }
        }
        if (!is_del) {
          if (d.address.indexOf(this.select_cate) > -1 &&
                            (d.name.indexOf(this.select_word) > -1 ||
                                d.address.indexOf(this.select_word) > -1)
          ) {
            return d
          }
        }
      })
    }
  },
  // created: function() {
  //   this.getlist()// 这里定义这个方法，vue实例之后运行到这里就调用这个函数
  // },
  created() {
    this.getData()
  },
  methods: {
    // 分页导航
    handleCurrentChange(val) {
      this.cur_page = val
      this.getData()
    },
    // 获取 easy-mock 的模拟数据
    getData() {
      // 开发环境使用 easy-mock 数据，正式环境使用 json 文件
      if (process.env.NODE_ENV === 'development') {
        this.url = '/ms/table/list'
      }
      this.$axios.post(this.url, {
        page: this.cur_page
      }).then((res) => {
        this.tableData = res.data.list
      })
    },
    search() {
      this.is_search = true
    },
    formatter(row, column) {
      return row.address
    },
    filterTag(value, row) {
      return row.tag === value
    },
    handleEdit(index, row) {
      this.idx = index
      const item = this.tableData[index]
      this.form = {
        name: item.name,
        date: item.date,
        address: item.address
      }
      this.editVisible = true
    },
    handleDelete(index, row) {
      this.idx = index
      this.delVisible = true
    },
    // 增加数据
    insert() {
      this.form.id = this.books.length + 1
      this.form.push(this.form)
      // this.form.created(this.tableData(), this.idx, this.form);
      // this.message.success(`添加成功`);
    },
    // 保存编辑
    saveEdit() {
      this.$set(this.tableData, this.idx, this.form)
      this.editVisible = false
      this.$message.success(`修改第 ${this.idx + 1} 行成功`)
    },
    // 确定删除
    deleteRow() {
      this.tableData.splice(this.idx, 1)
      this.$message.success('删除成功')
      this.delVisible = false
    }
  }
}

</script>

<style scoped>
    .handle-box {
        margin-bottom: 20px;
    }

    .handle-select {
        width: 120px;
    }

    .handle-input {
        width: 300px;
        display: inline-block;
    }
    .del-dialog-cnt{
        font-size: 16px;
        text-align: center
    }
    .table{
        width: 100%;
        font-size: 14px;
    }
    .red{
        color: #ff0000;
    }
    .mr10{
        margin-right: 10px;
    }
</style>
