<template>

  <imp-panel>
    <el-row slot="header" style="width:100%;padding-bottom: 0px !important;">
    	 <el-form :inline="true">
	        <el-form-item label="姓名">
	          <el-input v-model="listQuery.name" placeholder="姓名"></el-input>
	        </el-form-item>
	        <el-form-item label="用户名">
	          <el-input v-model="listQuery.userid" placeholder="工号"></el-input>
	        </el-form-item>
	         <el-form-item>
	          <el-button type="primary" icon="plus" @click="searchList">查询</el-button>
	        </el-form-item>
        
        	<el-form-item>
	    		<el-button type="primary" icon="plus" @click="sysnTxl">同步通讯录</el-button>
	    	</el-form-item>
	      </el-form>
    </el-row>
    
    <el-row slot="body" :gutter="24" style="margin-bottom: 20px;">
      <el-col :span="4" :xs="24" :sm="24" :md="4" :lg="4" style="margin-bottom: 20px;">
        <el-tree v-if="orgTree"
                 :data="orgTree"
                 ref="orgTree"
                 highlight-current
                 @node-click="handleNodeClick" clearable node-key="id" :props="defaultProps"></el-tree>
      </el-col>
      <el-col :span="20" :xs="24" :sm="24" :md="20" :lg="20">
        <el-card class="box-card">
          <div class="text item">
            <el-table :data="list" v-loading.body="listLoading" element-loading-text="拼命加载中" border fit
                      highlight-current-row>
              <el-table-column align="center" label="序号" width="80">
                <template slot-scope="scope">
                  <span v-text="getIndex(scope.$index)"> </span>
                </template>
              </el-table-column>
              <el-table-column align="center" label="姓名" prop="name" style="width: 60px;"></el-table-column>
              <el-table-column align="center" label="用户名" prop="userId" style="width: 60px;"></el-table-column>

              <el-table-column align="center" label="手机号" prop="mobile" width="170"></el-table-column>
              <el-table-column align="center" label="管理" width="220">
                <template slot-scope="scope">
                  <el-button type="primary" icon="edit" @click="showUpdate(scope.$index)">复 制</el-button>
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
          </div>
        </el-card>

        <el-dialog title="复制用户" :visible.sync="dialogFormVisible">
          <el-form class="small-space" :model="tempUser" label-position="left" label-width="80px"
                   style='width: 300px; margin-left:50px;'>
            <el-form-item label="所属机构">
              <el-select-tree v-model="tempUser.orgId" :treeData="orgTree2" :propNames="defaultProps"
                              placeholder="请选择机构" style="width: 310px">
              </el-select-tree>
            </el-form-item>
            <el-form-item label="用户名">
              <el-input type="text" v-model="tempUser.username">
              </el-input>
            </el-form-item>
            <el-form-item label="手机号" >
              <el-input type="text" v-model="tempUser.phone">
              </el-input>
            </el-form-item>
            <el-form-item label="密码"  required>
              <el-input type="password"  placeholder="请输入密码" v-model="tempUser.password">
              </el-input>
            </el-form-item>
            <el-form-item label="角色" required>
              <el-select v-model="tempUser.roleId" placeholder="请选择">
                <el-option
                  v-for="item in roles"
                  :key="item.roleId"
                  :label="item.roleName"
                  :value="item.roleId">
                </el-option>
              </el-select>
            </el-form-item>

            <el-form-item label="姓名" required>
              <el-input type="text" v-model="tempUser.nickname">
              </el-input>
            </el-form-item>
            
            

          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="createUser">复 制</el-button>
          </div>
        </el-dialog>
      </el-col>
    </el-row>
  </imp-panel>


</template>
<script>


  import panel from "../../components/Tree/panel.vue"
  import selectTree from "../../components/Tree/selectTree.vue"
  import treeter from "../../components/Tree/treeter"
  

  export default {
    mixins: [treeter],
    components: {
      'imp-panel': panel,
      'el-select-tree': selectTree,
    },
    data(){
      return {
      	totalCount: 0, //分页组件--数据总条数
      	list: [],//表格的数据
      	listLoading: false,//数据加载等待动画
        dialogFormVisible: false,
        formLabelWidth: '100px',
        listQuery: {
          pageNum: 1,//页码
          pageRow: 10,//每页条数
          depId: null,
          name:'',
          userid:''
        },
        defaultProps: {
          children: 'children',
          label: 'name',
          id: "id",
        },
        orgTree: [],
        orgTree2: [],
        resourceTree:[],
        roles: [],//角色列表
       
        tempUser: {
          username: '',
          password: '',
          nickname: '',
          roleId: '',
          userId: '',
          phone:'',
          orgId:'',
          webUsername:'',
          deleteStatus:1
        }
      }
    },
    methods: {
      configRoleResources(){
        let checkedKeys = this.$refs.resourceTree.getCheckedKeys();
      },
      handleNodeClick(data){
        this.listQuery.name='';
        this.listQuery.userid='';
      	this.listQuery.pageNum=1;
        this.listQuery.depId = data.id;
        this.getWebChatUser();
      },
      getIndex($index) {
        //表格序号
        //return $index + 1
        return (this.listQuery.pageNum - 1) * this.listQuery.pageRow + $index + 1
      },
      createUser() {
        //添加新用户
        this.api({
          url: "/webchattxl/addUser",
          method: "post",
          data: this.tempUser
        }).then(() => {
          this.dialogFormVisible = false
        })
      },

      getWebChatUser(){
        this.api({
          url: "/webchattxl/listUser",
          method: "get",
          params: this.listQuery
        }).then(data => {
          this.listLoading = false;
          this.list = data.list;
          this.totalCount = data.totalCount;
        })
      },
      searchList(){
      	this.listQuery.depId='';
      	 this.api({
          url: "/webchattxl/listUser",
          method: "get",
          params: this.listQuery
        }).then(data => {
          this.listLoading = false;
          this.list = data.list;
          this.totalCount = data.totalCount;
        })
      },
      initOrg() {
        this.api({
          url: "/org/listOrg",
          method: "get"
        }).then(data => {
          this.orgTree2 = [];
          this.orgTree2.push(...data)
        })
      },
      getAllRoles() {
        this.api({
          url: "/user/getAllRoles",
          method: "get"
        }).then(data => {
          this.roles = data.list;
        })
      },
      showUpdate($index){

        this.initOrg();
        let user = this.list[$index];
        this.tempUser.username=user.userId;
        this.tempUser.phone=user.mobile;
        this.tempUser.nickname = user.name;

        this.dialogFormVisible = true;

      },
      sysnTxl(){
        this.api({
          url: "/webchattxl/sysnTxl",
          method: "get"
        }).then(data => {
          this.load();
          this.listLoading = false;
        })
      },
      load(){
        this.listLoading = true;
        this.api({
          url: "/webchattxl/listOrg",
          method: "get"
        }).then(data => {
          this.orgTree = [];
          this.orgTree.push(...data)
          console.log(data)
        })
        this.getWebChatUser();
      },
      handleFilter() {
        //查询事件
        this.listQuery.pageNum = 1;
        this.getWebChatUser();
    },
    handleCurrentChange(val) {
    	console.info("currentpage="+val);
        //改变页码
        this.listQuery.pageNum = val;
        this.getWebChatUser();
    },
    handleSizeChange(val) {
        //改变每页数量
        this.listQuery.pageRow = val;
        this.handleFilter();
    }
    },
    created(){
      this.load();

      this.getAllRoles();
    }
    
  }
</script>

<style>
  .render-content {
    float: right;
    margin-right: 20px
  }

  .render-content i.fa {
    margin-left: 10px;
  }

  .render-content i.fa:hover{
    color: #e6000f;
  }

  .select-tree .el-scrollbar.is-empty .el-select-dropdown__list {
    padding: 0;
  }

  .select-tree .el-scrollbar {
    border: 1px solid #d1dbe5;
  }

  .select-tree .el-tree{
    border:0;
  }

</style>
