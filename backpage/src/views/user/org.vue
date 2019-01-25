<template>

  <imp-panel>
    <h3 class="box-title" slot="header" style="width: 100%;">
      <el-button type="primary" icon="plus" @click="newAdd"  v-if="hasPerm('org:add')">新增</el-button>

    </h3>
    <el-row slot="body" :gutter="24" style="margin-bottom: 20px;">
      <el-col :span="6" :xs="24" :sm="24" :md="6" :lg="6" style="margin-bottom: 20px;">
        <el-tree v-if="orgTree"
                 :data="orgTree"
                 ref="orgTree"
                 highlight-current
                 @node-click="handleNodeClick" clearable node-key="id" :props="defaultProps"></el-tree>
      </el-col>
      <el-col :span="18" :xs="24" :sm="24" :md="18" :lg="18">
        <el-card class="box-card">
          <div class="text item">
            <el-form :model="form" ref="form">
              <el-form-item label="父级" :label-width="formLabelWidth">
                <!--<el-input v-model="form.parentId" auto-complete="off"></el-input>-->
                <el-select-tree v-model="form.parentId" :treeData="orgTree" :propNames="defaultProps" clearable
                                placeholder="请选择父级">
                </el-select-tree>
              </el-form-item>
              <el-form-item label="名称" :label-width="formLabelWidth">
                <el-input v-model="form.name" auto-complete="off"></el-input>
              </el-form-item>
              <el-form-item label="是否生效" :label-width="formLabelWidth">
                <el-radio class="radio" v-model="form.usable" label="1">是</el-radio>
                <el-radio class="radio" v-model="form.usable" label="0">否</el-radio>
              </el-form-item>
              <el-form-item label="排序" :label-width="formLabelWidth">
                <el-slider v-model="form.sort"></el-slider>
              </el-form-item>
              <el-form-item label="" :label-width="formLabelWidth">
              	
              	<el-button type="primary"  @click="onSubmit"  v-show="!form.id" v-if="hasPerm('org:add')">新增</el-button>
              	<el-button type="primary"  @click="onSubmit"  v-show="form.id" v-if="hasPerm('org:update')">修改</el-button>
              	
                <!-- <el-button type="primary" @click="onSubmit" v-text="form.id?'修改':'新增'"></el-button>-->
                <el-button type="danger" @click="deleteSelected" v-if="hasPerm('org:delete')" icon="delete" v-show="form.id && form.id!=null">删除
                </el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-card>
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
        dialogLoading:false,
        dialogVisible:false,
        formLabelWidth: '100px',
        defaultProps: {
          children: 'children',
          label: 'name',
          id: "id",
        },
        orgTree: [],
        resourceTree:[],
        maxId:700000,
        form: {
          id: null,
          parentId: null,
          name: '',
          enName: '',
          sort: 0,
          usable: "1"
        }
      }
    },
    methods: {
      configRoleResources(){
        let checkedKeys = this.$refs.resourceTree.getCheckedKeys();
//        this.$http.get(api.SYS_SET_ROLE_RESOURCE + "?roleId=" + this.form.id + "&resourceIds="+checkedKeys.join(','))
//          .then(res => {
//            this.$message('修改成功');
//            this.dialogVisible = false;
//          })
      },
      handleNodeClick(data){
        this.form = data;
        if(this.form.usable==1){
          this.form.usable="1";
        }else{
          this.form.usable="0";
        }
      },
      newAdd(){
        this.form = {
          id: null,
          parentId: null,
          name: '',
          enName: '',
          sort: 0,
          usable: "1",
          remarks: '',
          deleteStatus:1
        };
      },
      onSubmit(){
        let this_=this
          if(this.form.id){   //修改
            this.api({
              url: "/org/updateOrg",
              method: "post",
              data: this_.form
            }).then(data => {
              this.$message('操作成功');
              this.appendTreeNode(this.orgTree, this_.form);
            }).catch(e => {
              this.$message('操作失败');
            })
          }else{               //新增
            this.api({
              url: "/org/insertOrg",
              method: "post",
              data: this_.form
            }).then(data => {
              this.$message('操作成功');
              this.appendTreeNode(this.orgTree, this_.form);
            }).catch(e => {
              this.$message('操作失败');
            })
          }
//        this.$http.post(api.SYS_ROLE_ADD, this.form)
//          .then(res => {
//            this.form.id = res.data.id;
//            this.appendTreeNode(this.roleTree, res.data);
//          }).catch(e => {
//          this.maxId += 1;
//          this.$message('操作成功');
//          this.form.id = this.maxId;
//          var  ddd = {
//            id: this.form.id,
//            name: this.form.name,
//            sort: this.form.sort,
//            enName:this.form.enName,
//            parentId: this.form.parentId,
//            usable: this.form.usable,
//            children:[]
//          }
//          this.appendTreeNode(this.roleTree, ddd);
//          this.roleTree.push({});
//          this.roleTree.pop();
//        })
      },
      deleteSelected(id){
        let this_=this
        this.$confirm('确定删除此机构?', '提示', {
          confirmButtonText: '确定',
          showCancelButton: false,
          type: 'warning'
        }).then(() => {
          this_.form.deleteStatus=2;
          this.api({
            url: "/org/updateOrg",
            method: "post",
            data: this_.form
          }).then(data => {

            this.$message('操作成功');
            this.deleteFromTree(this.orgTree, this_.form.id);
            this.newAdd();
          }).catch(e => {
            this.$message('失败成功');
          })
        })
      },
      load(){
        if (!this.hasPerm('org:list')) {
          return
        }
        this.listLoading = true;

        this.api({
          url: "/org/listOrg",
          method: "get",
          params: this.listQuery
        }).then(data => {
          this.listLoading = false;
          this.orgTree = [];
          this.orgTree.push(...data)
          console.log(data)
        })
      }
    },
    created(){
      this.load();
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
