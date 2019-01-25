<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form :inline="true">
        <el-form-item label="姓名">
          <el-input v-model="listQuery.psnname" placeholder="姓名"></el-input>
        </el-form-item>
        <el-form-item label="工号">
          <el-input v-model="listQuery.psncode" placeholder="工号"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" icon="plus" @click="getList">查询
          </el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="plus" @click="sysnData">同步数据
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
      <el-table-column align="center" label="姓名" prop="psnname" width="100px;"></el-table-column>
      <el-table-column align="center" label="性别" prop="sex" width="100px;"></el-table-column>
      <el-table-column align="center" label="资格证号" prop="certificateno"></el-table-column>
      <el-table-column align="center" label="工号" prop="psncode" width="150"></el-table-column>
      <el-table-column align="center" label="旧工号" prop="oldpsncode" width="150"></el-table-column>
      <el-table-column align="center" label="IC卡号" prop="icnumber" width="150"></el-table-column>
      <el-table-column align="center" label="管理" v-if="hasPerm('driver:bindic')" width="200" >
        <template slot-scope="scope">
          <el-button type="primary" icon="edit" @click="showUpdate(scope.$index)">绑卡</el-button>
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
      <el-form class="small-space" :model="tempDriver" label-position="left" label-width="80px"
               style='width: 300px; margin-left:50px;'>
               
        <el-form-item label="姓名" >{{tempDriver.username}}</el-form-item>
        <el-form-item label="工号" >{{tempDriver.psncode}}</el-form-item>
        
         <el-form-item label="IC卡号" >
          <el-input type="text" v-model="tempDriver.icnumber"></el-input>
        </el-form-item>
       
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button  @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="bindic">绑 定</el-button>
      </div>
    </el-dialog>
    
    
    
  </div>
</template>
<script>



  export default {

    data() {
      return {
        totalCount: 0, //分页组件--数据总条数
        list: [],//表格的数据
        listLoading: false,//数据加载等待动画
        listQuery: {
          pageNum: 1,//页码
          pageRow: 10,//每页条数
          psnname:'',
          psncode:''
        },
        dialogStatus: 'create',
        dialogFormVisible: false,
        textMap: {
          update: '绑定IC卡号',
          create: '绑定用户卡号'
        },
        tempDriver: {
          username: '',
          psncode:'',
          icnumber:'',
          dindex:''
        }

      }
    },
    created() {
      this.getList();
    },
    methods: {

      sysnData() {
        let _this = this;

        this.api({
          url: "/paibanuser/sysnPaiBanUser",
          method: "get"
        }).then(data => {
            alert('同步数据成功');
          _this.getList();
        })
      },
      getList() {
        //查询列表
        this.listLoading = true;
        this.api({
          url: "/paibanuser/listPaiBanUser",
          method: "get",
          params: this.listQuery
        }).then(data => {
          this.listLoading = false;
          this.list = data.list;
          this.totalCount = data.totalCount;
        })
      },
      showUpdate($index){
      	let driver=this.list[$index];
      	 //显示新增对话框
        this.tempDriver.username = driver.psnname;
        this.tempDriver.psncode=driver.psncode;
        this.tempDriver.icnumber=driver.icnumber;
        this.tempDriver.dindex=$index;
        this.dialogStatus = "update";
        this.dialogFormVisible = true;
      },
      bindic(){
      	let _vue = this;
      	let i=this.tempDriver.dindex;
      	let flag=true;
      	let msg="";
      	console.info(this.tempDriver.icnumber);
      	console.info(_vue.list[i].icnumber);
      	//获取param
      	//提交绑定
      	//返回查询列表
      	if(this.tempDriver.icnumber==''||this.tempDriver.icnumber==_vue.list[i].icnumber){
      		flag=false;
      		msg="卡号未改变或者为空,不能提交!";
      		
      	}else if(this.tempDriver.icnumber.length!=10){
      		flag=false;
      		msg="卡号长度不规范!";
      	}
      	if(flag==false){
      		_vue.$message({
	            message: msg,
	            type: 'error',
	            duration: 1 * 1000
	         })
	         return false;
      	}
      	
      	this.api({
      	  url: "/driveric/bindic",
          method: "post",
          params: this.tempDriver
      	}).then((re)=>{
      		
      		let type="success";
      		if(re.cknum==1){
      			msg="已存在该卡号!";
      			type="error";
      		}else{
				if(re.result==1){
					 msg="绑定成功!";
					_vue.dialogFormVisible = false;
				}else{
					_vue.dialogFormVisible = false;
					msg="绑定失败!";
				}
      		
      		}
			_vue.$message({
	            message: msg,
	            type: type,
	            duration: 1 * 1000,
	            onClose: () => {
	              _vue.getList();
	            }
	         })
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
      }
    }
  }
</script>
