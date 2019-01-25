<template>
  <div class="app-container calendar-list-container">
    <div class="filter-container">
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" placeholder="姓名或账户" v-model="listQuery.name"> </el-input>
      <el-button class="filter-item" type="primary" icon="search" @click="handleFilter">搜索</el-button>
      <el-button class="filter-item"  style="margin-left: 10px;" @click="handleCreate" type="primary" icon="edit">添加</el-button>
    </div>
    <el-table :key='tableKey' :data="list" v-loading.body="listLoading" border fit highlight-current-row style="width: 100%">


        <el-table-column width="200px" align="center" label="应用">
      <template slot-scope="scope">
        <span>{{scope.row.name}}</span>
      </template>
    </el-table-column>
        <el-table-column width="200px" align="center" label="机构ID">
      <template slot-scope="scope">
        <span>{{scope.row.pkOrg}}</span>
      </template>
    </el-table-column>
        <el-table-column width="200px" align="center" label="cortid">
      <template slot-scope="scope">
        <span>{{scope.row.cortid}}</span>
      </template>
    </el-table-column>
        <el-table-column width="200px" align="center" label="secret">
      <template slot-scope="scope">
        <span>{{scope.row.secret}}</span>
      </template>
    </el-table-column>
        <el-table-column width="200px" align="center" label="txlsecret">
      <template slot-scope="scope">
        <span>{{scope.row.txlsecret}}</span>
      </template>
    </el-table-column>

        <el-table-column fixed="right" align="center" label="操作" width="150"> <template slot-scope="scope">
        <el-button  size="small" type="success" @click="handleUpdate(scope.row)">编辑
        </el-button>
        <el-button  size="small" type="danger" @click="handleDelete(scope.row)">删除
        </el-button>
      </template> </el-table-column>
    </el-table>
    <div v-show="!listLoading" class="pagination-container">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.page" :page-sizes="[10,20,30, 50]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="totalCount"> </el-pagination>
    </div>
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="应用" prop="name">
      <el-input v-model="form.name" placeholder="请输入应用"></el-input>
    </el-form-item>
        <el-form-item label="机构ID" prop="pkOrg">
      <el-input v-model="form.pkOrg" placeholder="请输入机构ID"></el-input>
    </el-form-item>
        <el-form-item label="cortid" prop="cortid">
      <el-input v-model="form.cortid" placeholder="请输入cortid"></el-input>
    </el-form-item>
        <el-form-item label="secret" prop="secret">
      <el-input v-model="form.secret" placeholder="请输入secret"></el-input>
    </el-form-item>
        <el-form-item label="txlsecret" prop="txlsecret">
      <el-input v-model="form.txlsecret" placeholder="请输入txlsecret"></el-input>
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
    name: 'webchatConfig',
    data() {
      return {
        form: {
        name : undefined,        pkOrg : undefined,        cortid : undefined,        secret : undefined,        txlsecret : undefined,        deleteStatus : undefined          },
        rules: {
    name: [
  {
    required: true,
    message: '请输入应用',
    trigger: 'blur'
  },
  {
    min: 3,
    max: 20,
    message: '长度在 3 到 20 个字符',
    trigger: 'blur'
  }
],   pkOrg: [
  {
    required: true,
    message: '请输入机构ID',
    trigger: 'blur'
  },
  {
    min: 3,
    max: 20,
    message: '长度在 3 到 20 个字符',
    trigger: 'blur'
  }
],   cortid: [
  {
    required: true,
    message: '请输入cortid',
    trigger: 'blur'
  }
],   secret: [
  {
    required: true,
    message: '请输入secret',
    trigger: 'blur'
  }
],   txlsecret: [
  {
    required: true,
    message: '请输入txlsecret',
    trigger: 'blur'
  }
]
        },
        list: null,
        totalCount: null,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 20,
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
              url: "/webchatConfig",
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
        let _this = this
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            })
            .then(() => {
                _this.api({
                    url: "/webchatConfig"+'?id='+row.id,
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
                      url: "webchatConfig",
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
                      url: "webchatConfig",
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
