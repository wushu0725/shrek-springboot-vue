<template>
  <div class="app-container calendar-list-container">
    <div class="filter-container">
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" placeholder="姓名或账户" v-model="listQuery.name"> </el-input>
      <el-button class="filter-item" type="primary" icon="search" @click="handleFilter">搜索</el-button>
      <el-button class="filter-item"  style="margin-left: 10px;" @click="handleCreate" type="primary" icon="edit">添加</el-button>
    </div>
    <el-table :key='tableKey' :data="list" v-loading.body="listLoading" border fit highlight-current-row style="width: 100%">


        <el-table-column width="200px" align="center" label="名称">
      <template slot-scope="scope">
        <span>{{scope.row.name}}</span>
      </template>
    </el-table-column>
        <el-table-column width="200px" align="center" label="拼音">
      <template slot-scope="scope">
        <span>{{scope.row.pinyin}}</span>
      </template>
    </el-table-column>
        <el-table-column width="200px" align="center" label="区号">
      <template slot-scope="scope">
        <span>{{scope.row.areaCode}}</span>
      </template>
    </el-table-column>
        <el-table-column width="200px" align="center" label="首字母">
      <template slot-scope="scope">
        <span>{{scope.row.abbr}}</span>
      </template>
    </el-table-column>
        <el-table-column width="200px" align="center" label="排序">
      <template slot-scope="scope">
        <span>{{scope.row.sort}}</span>
      </template>
    </el-table-column>
        <el-table-column width="200px" align="center" label="">
      <template slot-scope="scope">
        <span>{{scope.row.isMap}}</span>
      </template>
    </el-table-column>

        <el-table-column width="200px" align="center" label="经度">
      <template slot-scope="scope">
        <span>{{scope.row.longitude}}</span>
      </template>
    </el-table-column>
        <el-table-column width="200px" align="center" label="纬度">
      <template slot-scope="scope">
        <span>{{scope.row.latitude}}</span>
      </template>
    </el-table-column>
        <el-table-column width="200px" align="center" label="是否热门">
      <template slot-scope="scope">
        <span>{{scope.row.hot}}</span>
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
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.pageNum" :page-sizes="[10,20,30, 50]" :page-size="listQuery.pageRow" layout="total, sizes, prev, pager, next, jumper" :total="totalCount"> </el-pagination>
    </div>
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form :model="form"  ref="form" label-width="100px">
        <el-form-item label="名称" prop="name">
      <el-input v-model="form.name" placeholder="请输入名称"></el-input>
    </el-form-item>
        <el-form-item label="拼音" prop="pinyin">
      <el-input v-model="form.pinyin" placeholder="请输入拼音"></el-input>
    </el-form-item>
        <el-form-item label="区号" prop="areaCode">
      <el-input v-model="form.areaCode" placeholder="请输入区号"></el-input>
    </el-form-item>
        <el-form-item label="首字母" prop="abbr">
      <el-input v-model="form.abbr" placeholder="请输入首字母"></el-input>
    </el-form-item>
        <el-form-item label="排序" prop="sort">
      <el-input v-model="form.sort" placeholder="请输入排序"></el-input>
    </el-form-item>
        <el-form-item label="是否地图" prop="isMap">
      <el-input v-model="form.isMap" placeholder="请输入"></el-input>
    </el-form-item>
          <el-form-item label="经度" prop="longitude">
      <el-input v-model="form.longitude" placeholder="请输入经度"></el-input>
    </el-form-item>
        <el-form-item label="纬度" prop="latitude">
      <el-input v-model="form.latitude" placeholder="请输入纬度"></el-input>
    </el-form-item>
        <el-form-item label="是否热门" prop="hot">
      <el-input v-model="form.hot" placeholder="请输入是否热门"></el-input>
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
    name: 'bsCity',
    data() {
      return {
        form: {
        name : undefined,        pinyin : undefined,        areaCode : undefined,        abbr : undefined,        sort : undefined,        isMap : undefined,        deleteStatus : undefined,        longitude : undefined,        latitude : undefined,        hot : undefined          },
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
              url: "/bsCity",
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
                    url: "/bsCity"+'?id='+row.id,
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
                      url: "bsCity",
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
                      url: "bsCity",
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
