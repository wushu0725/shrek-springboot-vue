<template>
  <div class="app-container calendar-list-container">
    <el-table :key='tableKey' :data="list" v-loading.body="listLoading" border fit highlight-current-row style="width: 100%">
        <el-table-column width="200px" align="center" label="ID">
      <template slot-scope="scope">
        <span>{{scope.row.taskId}}</span>
      </template>
    </el-table-column>
        <el-table-column width="200px" align="center" label="任务名称">
      <template slot-scope="scope">
        <span>{{scope.row.taskName}}</span>
      </template>
    </el-table-column>
        <el-table-column width="200px" align="center" label="提交时间">
      <template slot-scope="scope">
        <span>{{scope.row.time}}</span>
      </template>
    </el-table-column>
        <el-table-column fixed="right" align="center" label="操作" width="150"> <template slot-scope="scope">
          <el-button  size="small" type="success" @click="handleSubmit(scope.row)">办理
          </el-button>
        <el-button  size="small" type="danger" @click="handleDelete(scope.row)">删除
        </el-button>
      </template> </el-table-column>
    </el-table>
    <div v-show="!listLoading" class="pagination-container">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.pageNum" :page-sizes="[10,20,30, 50]" :page-size="listQuery.pageRow" layout="total, sizes, prev, pager, next, jumper" :total="totalCount"> </el-pagination>
    </div>
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form :model="form" ref="form" label-width="100px">
        <el-form-item label="请假天数" prop="days">
      <el-input v-model="form.days" readonly="true" placeholder="请输入请假天数"></el-input>
    </el-form-item>
        <el-form-item label="备注" prop="content">
      <el-input v-model="form.content" readonly="true" placeholder="请输入备注"></el-input>
    </el-form-item>
        <el-form-item label="请假时间" prop="leaveTime">
      <el-input v-model="form.leaveTime" readonly="true" placeholder="请输入请假时间"></el-input>
    </el-form-item>

        <el-form-item label="审批意见" prop="leaveTime">
          <el-input v-model="form.comment"  placeholder="请输入审批意见"></el-input>
        </el-form-item>
        </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel('form')">取 消</el-button>
        <el-button icon="el-icon-check"
                   v-for="flag in flagList" :key="flag"
                   @click="handleTask(flag)"
                   plain>{{flag}}
        </el-button>
      </div>
      <el-table :key='tableKey' :data="commentList" v-loading.body="listLoading" border fit highlight-current-row style="width: 100%">
        <el-table-column width="200px" align="center" label="操作人">
          <template slot-scope="scope">
            <span>{{scope.row.userId}}</span>
          </template>
        </el-table-column>
        <el-table-column width="200px" align="center" label="操作时间">
          <template slot-scope="scope">
            <span>{{scope.row.time}}</span>
          </template>
        </el-table-column>
        <el-table-column width="200px" align="center" label="操作意见">
          <template slot-scope="scope">
            <span>{{scope.row.fullMessage}}</span>
          </template>
        </el-table-column>
      </el-table>
      <img :src="actPicUrl"  width="100%">
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
        tableKey: 0,
        flagList:{},
        obj:{},
        taskId:'',
        commentList:[],
        actPicUrl: ''
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
              url: "/task/todo",
              method: "get"
          }).then(data => {
              _this.listLoading = false;
              _this.list = data.list;
              _this.totalCount = data.totalCount;
          })
      },
      handleFilter() {
        this.getList();
      },
      handleSizeChange(val) {
        this.listQuery.limit = val;
        this.getList();
      },
      handleTask: function ( result) {
        this.obj.taskId=this.taskId;
        this.obj.taskFlag = result;
        this.obj.comment  = this.form.comment;
        this.obj.leaveId  = this.form.id;
        this.obj.days = this.form.days;
        this.api({
          url: "/task",
          method: "post",
          data:this.obj
        }).then(response => {
          this.$message({
            showClose: true,
            message: '提交成功',
            type: 'success'
          });
          this.dialogFormVisible = false;
          this.getList()
        })
      },
      handleCurrentChange(val) {
        this.listQuery.page = val;
        this.getList();
      },
      viewImg() {
        this.actPicUrl = '/api/task/view/' + this.taskId
      },
      handleCreate() {
        this.resetTemp();
        this.dialogStatus = 'create';
        this.dialogFormVisible = true;
      },
      handleSubmit(row){
        let _this=this;
        this.obj.taskId=row.taskId;
        this.taskId=row.taskId;
        this.api({
          url: "task/"+row.taskId,
          method: "get"
        }).then(data => {
          this.form=data;
          this.obj=data;
          this.flagList=data.flagList;
          this.dialogFormVisible = true;
          _this.getCommont(_this.taskId);
          _this.viewImg()
        })
      },

      getCommont(taks_id){
        let _this=this;
        this.api({
          url: "task/comment/"+taks_id,
          method: "get"
        }).then(data => {
          _this.commentList=data;
          console.log(data);
        })
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
