<template>
  <div class="app-container calendar-list-container">
    <div class="filter-container">
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" placeholder="姓名或账户" v-model="listQuery.name"> </el-input>
      <el-button class="filter-item" type="primary" icon="search" @click="handleFilter">搜索</el-button>
      <el-button class="filter-item"  style="margin-left: 10px;" @click="handleCreate" type="primary" icon="edit">添加</el-button>
    </div>
    <el-table :key='tableKey' :data="list" v-loading.body="listLoading" border fit highlight-current-row style="width: 100%">


      <el-table-column width="200px" align="center" label="模型ID">
        <template slot-scope="scope">
          <span>{{scope.row.id}}</span>
        </template>
      </el-table-column>
      <el-table-column width="200px" align="center" label="模型标识">
        <template slot-scope="scope">
          <span>{{scope.row.key}}</span>
        </template>
      </el-table-column>
      <el-table-column width="200px" align="center" label="模型分类">
        <template slot-scope="scope">
          <span>{{scope.row.category}}</span>
        </template>
      </el-table-column>
      <el-table-column width="200px" align="center" label="模型名称">
        <template slot-scope="scope">
          <span>{{scope.row.name}}</span>
        </template>
      </el-table-column>

      <el-table-column fixed="right" align="center" label="操作" width="150"> <template slot-scope="scope">
        <el-button  size="small" type="success" @click="handleUpdate(scope.row)">编辑
        </el-button>
        <el-button  size="small" type="success" @click="editModel(scope.row)">模板
        </el-button>
        <el-button  size="small" type="success" @click="deployModel(scope.row)">发布
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
        <el-form-item label="模型标识" >
          <el-input v-model="form.key" placeholder="模型标识"></el-input>
        </el-form-item>
        <el-form-item label="模型名称" prop="ordernum">
          <el-input v-model="form.name" placeholder="请输入模型名称"></el-input>
        </el-form-item>
        <el-form-item label="模型分类" prop="ordernum">
          <el-input v-model="form.category" placeholder="请输入模型分类"></el-input>
        </el-form-item>

        <el-form-item label="模型描述" prop="ordernum">
          <el-input v-model="form.description" placeholder="请输入模型描述"></el-input>
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
    name: 'shrekCategory',
    data() {
      return {
        form: {
          name :'',key : '',category:'',description:''},

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
        listCategory:[],
        addModel:false,
        modelSrc:''
      }
    },
    created() {
      this.getList();
      window['closeMode'] = () => {
        this.closeMode();
      };
    },
    methods: {
      getList() {
        let _this=this;
        this.listLoading = true;
        this.api({
          url: "/process",
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
      editModel(row){
        this.modelSrc='api/modeler.html?modelId='+row.id;
        this.addModel=true;
      },
      deployModel(row){
        let _this=this;
        this.listLoading = true;
        this.api({
          url: "/model/deploy/"+row.id,
          method: "post"
        }).then(data => {
          _this.listLoading = false;
          _this.list = data.list;
          _this.totalCount = data.totalCount;
        })
      },
      closeMode(){
        console.log('关闭自己');
        this.addModel=false;
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
              url: "/shrekCategory"+'?id='+row.id,
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
              url: "model/addModel",
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
              url: "shrekCategory",
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
