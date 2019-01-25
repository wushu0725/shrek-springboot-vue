<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <el-button type="primary" icon="plus" v-if="hasPerm('user:add')" @click="showCreate">添加
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table :data="list" v-loading.body="listLoading" element-loading-text="拼命加载中" border fit
              highlight-current-row>
      <el-table-column align="center" label="序号" width="80">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"> </span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="姓名" prop="nickname" width="100px;"></el-table-column>
      <el-table-column align="center" label="用户名" prop="username" width="100px;"></el-table-column>
      <el-table-column align="center" label="角色" width="100">
        <template slot-scope="scope">
          <el-tag type="success" v-text="scope.row.roleName" v-if="scope.row.roleId===1"></el-tag>
          <el-tag type="primary" v-text="scope.row.roleName" v-else></el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="手机号" prop="phone" width="150"></el-table-column>
      <el-table-column align="center" label="分公司" prop="pkName" width="150"></el-table-column>
      <el-table-column align="center" label="组织" prop="orgName" width="200"></el-table-column>
      <el-table-column align="center" label="管理" width="200"  v-if="hasPerm('user:delete') || hasPerm('user:update')">
        <template slot-scope="scope">
          <el-button type="primary" icon="edit" @click="showUpdate(scope.$index)" v-if="hasPerm('user:update')">修改</el-button>
          <el-button type="danger" icon="delete"
                     @click="removeUser(scope.$index)">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="listQuery.pageNum"
      :page-size="listQuery.pageRow"
      :total="totalCount"
      :page-sizes="[10, 20, 50, 100]"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form class="small-space" :inline="true" :model="tempUser" label-position="left" label-width="80px"
               style='width: 600px; margin-left:50px;'>
        <el-form-item label="用户名" required v-if="dialogStatus=='create'">
          <el-input type="text" v-model="tempUser.username">
          </el-input>
        </el-form-item>
        <el-form-item label="手机号" >
          <el-input type="text" v-model="tempUser.phone">
          </el-input>
        </el-form-item>
        <el-form-item label="IC卡号" >
          <el-input type="text" v-model="tempUser.icNumber">
          </el-input>
        </el-form-item>
        <el-form-item label="密码" v-if="dialogStatus=='create'" required>
          <el-input type="password" v-model="tempUser.password">
          </el-input>
        </el-form-item>
        <el-form-item label="新密码" v-else>
          <el-input type="password" v-model="tempUser.password" placeholder="不填则表示不修改">
          </el-input>
        </el-form-item>
        <el-form-item label="角色" required >
          <el-select v-model="tempUser.roleId" style='width: 200px;' placeholder="请选择">
            <el-option
              v-for="item in roles"
              :key="item.roleId"
              :label="item.roleName"
              :value="item.roleId">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="昵称" required>
          <el-input type="text" v-model="tempUser.nickname">
          </el-input>
        </el-form-item>

        <el-form-item label="分公司" required>
          <el-select v-model="tempUser.orgPk" style='width: 200px;' placeholder="请选择">
            <el-option
              v-for="item in orgPk"
              :key="item.pkOrg"
              :label="item.name"
              :value="item.pkOrg">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="所属机构">
          <el-select-tree v-model="tempUser.orgId" :treeData="orgTree" :propNames="defaultProps" clearable
                          placeholder="请选择机构" style="width: 310px">
          </el-select-tree>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="success" @click="createUser">创 建</el-button>
        <el-button type="primary" v-else @click="updateUser">修 改</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import {mapGetters} from 'vuex'
  import selectTree from "../../components/Tree/selectTree.vue"

  export default {
    components: {
      'el-select-tree': selectTree
    },
    data() {
      return {
        totalCount: 0, //分页组件--数据总条数
        list: [],//表格的数据
        listLoading: false,//数据加载等待动画
        listQuery: {
          pageNum: 1,//页码
          pageRow: 10,//每页条数
        },
        orgTree: [],
        orgPk: [],
        defaultProps: {
          children: 'children',
          label: 'name',
          id: "id",
        },
        roles: [],//角色列表
        dialogStatus: 'create',
        dialogFormVisible: false,
        textMap: {
          update: '编辑',
          create: '新建用户'
        },
        tempUser: {
          username: '',
          password: '',
          nickname: '',
          roleId: '',
          userId: '',
          phone:'',
          orgId:'',
          webUsername:'',
          icNumber:'',
          orgPk:''
        }
      }
    },
    created() {
      this.getList();
      if (this.hasPerm('user:add') || this.hasPerm('user:update')) {
        this.getAllRoles();
      }
      this.initOrg();
      this.initOrgPk();
    },
    methods: {
      getAllRoles() {
        this.api({
          url: "/user/getAllRoles",
          method: "get"
        }).then(data => {
          this.roles = data.list;
        })
      },
      initOrg() {
        this.api({
          url: "/org/listOrg",
          method: "get"
        }).then(data => {
          this.orgTree = [];
          this.orgTree.push(...data)
        })
      },
      initOrgPk() {
        this.api({
          url: "/webchatConfig/listAll",
          method: "get"
        }).then(data => {
          this.orgPk = data.list;
        })
      },
      getList() {
        //查询列表
        this.listLoading = true;
        this.api({
          url: "/user/list",
          method: "get",
          params: this.listQuery
        }).then(data => {
          this.listLoading = false;
          this.list = data.list;
          this.totalCount = data.totalCount;
        })
      },
      handleSizeChange(val) {
        //改变每页数量
        this.listQuery.pageRow = val
        this.handleFilter();
      },
      handleCurrentChange(val) {
        //改变页码
        this.listQuery.pageNum = val
        this.getList();
      },
      handleFilter() {
        //查询事件
        this.listQuery.pageNum = 1
        this.getList()
      },
      getIndex($index) {
        //表格序号
        return (this.listQuery.pageNum - 1) * this.listQuery.pageRow + $index + 1
      },
      showCreate() {
        //显示新增对话框
        this.tempUser.username = "";
        this.tempUser.password = "";
        this.tempUser.nickname = "";
        this.tempUser.roleId = "";
        this.tempUser.userId = "";
        this.tempUser.phone = "";
        this.tempUser.orgId = "";
        this.tempUser.orgPk="";
        this.dialogStatus = "create"
        this.dialogFormVisible = true
      },
      showUpdate($index) {
        let user = this.list[$index];
        this.tempUser.username = user.username;
        this.tempUser.nickname = user.nickname;
        this.tempUser.roleId = user.roleId;
        this.tempUser.userId = user.userId;
        this.tempUser.deleteStatus = '1';
        this.tempUser.phone = user.phone;
        this.tempUser.orgId = user.orgId;
        this.tempUser.password = '';
        this.tempUser.orgPk=user.pkOrg;
        this.dialogStatus = "update"
        this.dialogFormVisible = true

      },
      createUser() {
        //添加新用户
        this.api({
          url: "/user/addUser",
          method: "post",
          data: this.tempUser
        }).then(() => {
          this.getList();
          this.dialogFormVisible = false
        })
      },
      updateUser() {
        //修改用户信息
        let _vue = this;
        this.api({
          url: "/user/updateUser",
          method: "post",
          data: this.tempUser
        }).then(() => {
          let msg = "修改成功";
          this.dialogFormVisible = false
          this.$message({
            message: msg,
            type: 'success',
            duration: 1 * 1000,
            onClose: () => {
              _vue.getList();
            }
          })
        })
      },
      removeUser($index) {
        let _vue = this;
        this.$confirm('确定删除此用户?', '提示', {
          confirmButtonText: '确定',
          showCancelButton: false,
          type: 'warning'
        }).then(() => {
          let user = _vue.list[$index];
          user.deleteStatus = '2';
          _vue.api({
            url: "/user/updateUser",
            method: "post",
            data: user
          }).then(() => {
            _vue.getList()
          }).catch(() => {
            _vue.$message.error("删除失败")
          })
        })
      },
    }
  }
</script>
