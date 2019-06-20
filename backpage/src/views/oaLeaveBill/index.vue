<template>
  <div class="app-container calendar-list-container">
    <div class="filter-container">
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" placeholder="姓名或账户" v-model="listQuery.name"> </el-input>
      <el-button class="filter-item" type="primary" icon="search" @click="handleFilter">搜索</el-button>
      <el-button class="filter-item"  style="margin-left: 10px;" @click="handleCreate" type="primary" icon="edit">添加</el-button>
    </div>
    <el-table :key='tableKey' :data="list" v-loading.body="listLoading" border fit highlight-current-row style="width: 100%">


        <el-table-column width="200px" align="center" label="用户名">
      <template slot-scope="scope">
        <span>{{scope.row.username}}</span>
      </template>
    </el-table-column>
        <el-table-column width="200px" align="center" label="请假天数">
      <template slot-scope="scope">
        <span>{{scope.row.days}}</span>
      </template>
    </el-table-column>
        <el-table-column width="200px" align="center" label="备注">
      <template slot-scope="scope">
        <span>{{scope.row.content}}</span>
      </template>
    </el-table-column>
        <el-table-column width="200px" align="center" label="状态">
      <template slot-scope="scope">
        <span>{{scope.row.state}}</span>
      </template>
    </el-table-column>
        <el-table-column width="200px" align="center" label="请假时间">
      <template slot-scope="scope">
        <span>{{scope.row.leaveTime}}</span>
      </template>
    </el-table-column>

        <el-table-column fixed="right" align="center" label="操作" width="150"> <template slot-scope="scope">
        <el-button  size="small" type="success" @click="handleUpdate(scope.row)">编辑
        </el-button>
        <el-button  size="small" type="danger" @click="handleDelete(scope.row)">删除
        </el-button>
          <el-button  size="small" type="danger" @click="handleSubmit(scope.row)">提交
          </el-button>
      </template> </el-table-column>
    </el-table>
    <div v-show="!listLoading" class="pagination-container">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.pageNum" :page-sizes="[10,20,30, 50]" :page-size="listQuery.pageRow" layout="total, sizes, prev, pager, next, jumper" :total="totalCount"> </el-pagination>
    </div>
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form :model="form"  ref="form" label-width="100px">
        <el-form-item label="请假天数" prop="days">
      <el-input v-model="form.days" placeholder="请输入请假天数"></el-input>
    </el-form-item>
        <el-form-item label="备注" prop="content">
      <el-input v-model="form.content" placeholder="请输入备注"></el-input>
    </el-form-item>
        <el-form-item label="请假时间" prop="leaveTime">
      <el-input v-model="form.leaveTime" placeholder="请输入请假时间"></el-input>
    </el-form-item>

        </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel('form')">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="create('form')">确 定</el-button>
        <el-button v-else type="primary" @click="update('form')">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: 'oaLeaveBill',
    data() {
      return {
        form: {
        username : undefined,        days : undefined,        content : undefined,        state : undefined,        leaveTime : undefined,        createTime : undefined,        updateTime : undefined,        deleteStatus : undefined,        tenantId : undefined          },
        list: null,
        totalCount: null,
        listLoading: true,
        listQuery: {
          pageNum: 1,
          pageRow: 20,
          name: undefined
        },
        dialogFormVisible: false,
        dialogStatus: '',
        textMap: {
          update: '编辑',
          create: '创建'
        },
        tableKey: 0
      }
    },
    created() {
      this.getList();
    },
    methods: {
      getList() {
          let _this=this;
          this.listLoading = true;
          this.api({
              url: "/oaLeaveBill",
              method: "get"
          }).then(data => {
              _this.listLoading = false;
              _this.list = data.list;
              _this.totalCount = data.totalCount;
          })
      },
      handleSubmit(row){
        let _this=this;
        this.listLoading = true;
        this.api({
          url: "/oaLeaveBill/submit"+'?id='+row.id,
          method: "get"
        }).then(data => {
          _this.getList();
        })
      },
      handleFilter() {
        this.getList();
      },
      handleSizeChange(val) {
        this.listQuery.limit = val;
        this.getList();
      },
      handleCurrentChange(val) {
        this.listQuery.page = val;
        this.getList();
      },
      handleCreate() {
        this.resetTemp();
        this.dialogStatus = 'create';
        this.dialogFormVisible = true;
      },
      handleUpdate(row) {
          this.form=row;
        this.dialogFormVisible = true;
        this.dialogStatus = 'update';
      },
      handleDelete(row) {
        let _this = this;
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            })
            .then(() => {
                _this.api({
                    url: "/oaLeaveBill"+'?id='+row.id,
                    method: "delete"
                }).then(() => {
                    _this.getList()
                }).catch(() => {
                    _this.$message.error("删除失败")
                })
      });
      },
      create(formName) {
          const set = this.$refs;
          this.form.deleteStatus='1';
          set[formName].validate(valid => {
              if (valid) {
                  this.api({
                      url: "oaLeaveBill",
                      method: "post",
                      data: this.form
                  }).then(() => {
                      this.getList();
                      this.dialogFormVisible = false
                  })
              } else {
                  return false;
              }
          });
      },
      cancel(formName) {
        this.dialogFormVisible = false;
        const set = this.$refs;
        set[formName].resetFields();
      },
      update(formName) {
          const set = this.$refs;
          set[formName].validate(valid => {
              if (valid) {
                  this.api({
                      url: "oaLeaveBill",
                      method: "put",
                      data: this.form
                  }).then(() => {
                      this.getList();
                      this.dialogFormVisible = false
                  })
              } else {
                  return false;
              }
          });
      },
      resetTemp() {
        this.form = {
        };
      }
    }
  }
</script>
