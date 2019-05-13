<template>
  <div class="app-container calendar-list-container">
    <div class="filter-container">
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" placeholder="姓名或账户" v-model="listQuery.name"> </el-input>
      <el-button class="filter-item" type="primary" icon="search" @click="handleFilter">搜索</el-button>
      <el-button class="filter-item"  style="margin-left: 10px;" @click="handleCreate" type="primary" icon="edit">添加</el-button>
    </div>
    <el-table :key='tableKey' :data="list" v-loading.body="listLoading" border fit highlight-current-row style="width: 100%">


        <el-table-column width="200px" align="center" label="商品名称">
      <template slot-scope="scope">
        <span>{{scope.row.title}}</span>
      </template>
    </el-table-column>
        <el-table-column align="center" label="介绍">
      <template slot-scope="scope">
        <span>{{scope.row.intro}}</span>
      </template>
    </el-table-column>
        <el-table-column width="200px" align="center" label="价格">
      <template slot-scope="scope">
        <span>{{scope.row.price}}</span>
      </template>
    </el-table-column>

        <el-table-column fixed="right" align="center" label="操作" width="150"> <template slot-scope="scope">
        <el-button  size="small" type="success" @click="handleUpdate(scope.row)">编辑
        </el-button>
        <el-button  size="small" type="danger" @click="handleDelete(scope.row)">删除
        </el-button>
          <el-button  size="small" type="success" @click="getImgList(scope.row.id)">图片管理
          </el-button>
      </template> </el-table-column>
    </el-table>
    <div v-show="!listLoading" class="pagination-container">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.pageNum" :page-sizes="[10,20,30, 50]" :page-size="listQuery.pageRow" layout="total, sizes, prev, pager, next, jumper" :total="totalCount"> </el-pagination>
    </div>
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="商品名称" prop="title">
      <el-input v-model="form.title" placeholder="请输入商品名称"></el-input>
    </el-form-item>
        <el-form-item label="介绍" prop="intro">
      <el-input v-model="form.intro" placeholder="请输入介绍"></el-input>
    </el-form-item>
        <el-form-item label="价格" prop="price">
      <el-input v-model="form.price" placeholder="请输入"></el-input>
        </el-form-item>
        <el-form-item label="图片" prop="price">
          <el-upload
            class="avatar-uploader"
            :action="uploadUrl"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload">
            <img v-if="imageUrl" :src="imageUrl" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
    </el-form-item>
        <el-form-item label="分类" required >
          <el-select v-model="form.categoryId" style='width: 200px;' placeholder="请选择">
            <el-option
              v-for="item in listCategory"
              :key="item.id"
              :label="item.categoryName"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="栏位" required >
          <el-select v-model="form.sctionId" style='width: 200px;' placeholder="请选择">
            <el-option
              v-for="item in listSction"
              :key="item.id"
              :label="item.sectionName"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel('form')">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="create('form')">确 定</el-button>
        <el-button v-else type="primary" @click="update('form')">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog  :visible.sync="dialogFormVisible2">
      <el-row  v-if="searchzone">
        <div class="filter-container">
          <el-button class="filter-item"   @click="addImg" type="primary" icon="edit">添加</el-button>
        </div>
      <el-table :data="listImage"  element-loading-text="拼命加载中" border fit
                highlight-current-row>
        <el-table-column align="center" prop="type" label="类型1" style="width: 60px;"></el-table-column>
        <el-table-column align="center" prop="imgsrc" label="图片" style="width: 60px;"></el-table-column>
      </el-table>
      </el-row>
      <el-row  v-if="addzone">
        <el-form :model="form2" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="类别" prop="price">
          <el-select
            v-model="form2.type"
            placeholder="请选择"
            :disabled="forbidden"
          >
            <el-option
              v-for="item in typeOptions"
              :key="item.id"
              :label="item.lable"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="图片" prop="price">
          <el-upload
            class="avatar-uploader"
            :action="uploadUrl"
            :show-file-list="false"
            :on-success="handleAvatarSuccess2"
            :before-upload="beforeAvatarUpload">
            <img v-if="imageUrl" :src="imageUrl" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
          <el-form-item >
            <el-button @click="cancel('form')">取 消</el-button>
            <el-button  type="primary" @click="createImg('form2')">确 定</el-button>
          </el-form-item>
        </el-form>

      </el-row>
    </el-dialog>

  </div>
</template>

<script>
  import ElSelectTree from "../../components/Tree/selectTree.vue";

  export default {
    components: {ElSelectTree},
    name: 'shrekGoods',
    data() {
      return {
        form: {
        title : undefined,        intro : undefined,        price : undefined,        deleteStatues : undefined,categoryId:'' ,sctionId:'',imgPath:''         },
        form2:{
          type:'',
          imgsrc:'',
          goodsid:'',
          deleteStatus:'1',
        },
        typeOptions:[{
          id:1,
          lable:'轮播'
        },
          {
            id:2,
            lable:'详情'
          }
        ],
        rules: {
    title: [
  {
    required: true,
    message: '请输入商品名称',
    trigger: 'blur'
  },
  {
    min: 3,
    max: 20,
    message: '长度在 3 到 20 个字符',
    trigger: 'blur'
  }
],   intro: [
  {
    required: true,
    message: '请输入介绍',
    trigger: 'blur'
  },
  {
    min: 3,
    max: 20,
    message: '长度在 3 到 20 个字符',
    trigger: 'blur'
  }
],   price: [
  {
    required: true,
    message: '请输入',
    trigger: 'blur'
  }
],   deleteStatues: [
  {
    required: true,
    message: '请输入',
    trigger: 'blur'
  },
  {
    min: 3,
    max: 20,
    message: '长度在 3 到 20 个字符',
    trigger: 'blur'
  }
]        },
        list: null,
        totalCount: null,
        listLoading: true,
        listQuery: {
          pageNum: 1,
          pageRow: 20,
          name: undefined
        },
        dialogFormVisible: false,
        dialogFormVisible2: false,
        searchzone:true,
        addzone: false,
        dialogStatus: '',
        textMap: {
          update: '编辑',
          create: '创建'
        },
        tableKey: 0,
        listCategory:[],
        listSction:[],
        forbidden: false,
        uploadUrl:'api/shrekGoods/singlefile',
        imageUrl:'',
        listImage:[],
        goodsId:''
      }
    },
    created() {
      this.getList();
      this.getlistCategory();
      this.getlistSction();
    },
    methods: {
      getList() {
        let _this=this;
        this.listLoading = true;
        this.api({
          url: "/shrekGoods",
          method: "get"
        }).then(data => {
          _this.listLoading = false;
          _this.list = data.list;
          _this.totalCount = data.totalCount;
        })
      },
      getlistCategory() {
        let _this=this;
        this.listLoading = true;
        this.api({
          url: "/shrekCategory/selectList",
          method: "get"
        }).then(data => {
          _this.listCategory = data;
        })
      },
      getlistSction() {
        let _this=this;
        this.listLoading = true;
        this.api({
          url: "/shrekSction/selectList",
          method: "get"
        }).then(data => {
          _this.listSction = data;
        })
      },
      handleFilter() {
        this.getList();
      },
      handleSizeChange(val) {
        this.listQuery.limit = val;
        this.getList();
      },
      addImg(){
        this.searchzone=false;
          this.addzone=true;
      },
      getImgList(val){
        let _this=this;
        this.dialogFormVisible2=true;
        this.searchzone=true;
        this.addzone=false;
        this.goodsId=val;
        this.form2.goodsid=val;
        this.getImgList1();
      },
      getImgList1(){
        let _this=this;
        this.api({
          url: "/shrekGoodsImg",
          method: "get",
          params:{goodsId:this.goodsId}
        }).then(data => {
          _this.listImage = data.list;
        })
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
          this.imageUrl=row.imgPath;
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
                    url: "/shrekGoods"+'?id='+row.id,
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
                      url: "shrekGoods",
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
      createImg(){
        let _this = this;
        this.api({
          url: "shrekGoodsImg",
          method: "post",
          data: this.form2
        }).then(() => {
          _this.getImgList(this.goodsId);
        })
      },
      cancel(formName) {
        this.dialogFormVisible = false;
      },
      // 图片上传函数
      handleAvatarPreview() {
        this.dialogImageUrl = file.url;
        this.dialogVisible = true;
      },
      handleAvatarSuccess(res, file) {
        this.imageUrl = 'api/images/'+res.returnData;
        this.form.imgPath='api/images/'+res.returnData;
      },
      handleAvatarSuccess2(res, file) {
        this.imageUrl = 'api/images/'+res.returnData;
        this.form2.imgsrc='api/images/'+res.returnData;
      },
      handleAvatarRemove(file) {},
      beforeAvatarRemove(file) {
        if (file.status == "success") {
          return this.$confirm("是否确定删除该图片？", "确认删除", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
            cancelButtonClass: "el-button--danger",
            confirmButtonClass: "el-button--success"
          });
        }
      },
    beforeAvatarUpload(file) {
      let isJpg = true;
      // for(var i=0;i<this.fileList.length;i++){
      //   var obj=this.fileList[i];
      //   if(file.name==obj.name){
      //     isJpg=false;
      //     this.$message.error('已上传了此图片,不能重复上传!');
      //   };
      // }
      let extension = file.name.substring(file.name.lastIndexOf(".") + 1);
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (file.size == 0) {
        isJpg = false;
        this.$message.error("上传头像图片大小不能 0 KB!");
      }
      if (
        extension !== "jpg" &&
        extension !== "jpeg" &&
        extension !== "png" &&
        extension !== "gif" &&
        extension !== "bmp" &&
        extension !== "JPG" &&
        extension !== "JPEG" &&
        extension !== "PNG" &&
        extension !== "GIF" &&
        extension !== "BMP"
      ) {
        isJpg = false;
        this.$message.error("上传图片格式为 jpg、jpeg、png、gif、bmp格式!");
      }
      if (!isLt2M) {
        this.$message.error("上传图片大小不能超过 2MB!");
      }
      return isJpg && isLt2M;
    },
      update(formName) {
          const set = this.$refs;
          set[formName].validate(valid => {
              if (valid) {
                  this.api({
                      url: "shrekGoods",
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

<style>
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
</style>
