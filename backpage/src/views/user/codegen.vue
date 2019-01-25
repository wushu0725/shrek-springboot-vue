<template>
  <div class="app-container">
      <el-form  :inline="true" :model="tempObject" label-position="left" label-width="80px"
               style='width: 600px; margin-left:50px;'>
        <el-form-item label="表名" required >
          <el-select v-model="tempObject.tablename" style='width: 200px;' placeholder="请选择">
            <el-option
              v-for="item in allTables"
              :key="item.tableName"
              :label="item.tableName"
              :value="item.tableName">
            </el-option>
          </el-select>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary"  @click="codeGen">生成</el-button>
      </div>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        tempObject: {
          tablename: ''
        },
        allTables:[],
        dialogFormVisible:true,
        adminName: '管理员'
      }
    },
    created() {
      this.queryAllTable();
    },
    methods: {
      queryAllTable() {
        //查询所有权限
        this.api({
          url: "/generator/queryAllTable",
          method: "get"
        }).then(data => {
          this.allTables = data.list;
        })
      },
      codeGen() {
        //查询列表
        let _this=this
        this.api({
          url: "/generator/code",
          method: "get",
          params:{tablename:_this.tempObject.tablename},
          responseType: 'arraybuffer'
        }).then(data => {
          let blob = new Blob([data], { type: 'application/zip' })
          let filename = _this.tempObject.tablename + '.zip'
          let link = document.createElement('a')
          link.href = URL.createObjectURL(blob)
          link.download = filename
          document.body.appendChild(link)
          link.click()
          window.setTimeout(function () {
            URL.revokeObjectURL(blob)
            document.body.removeChild(link)
          }, 0)
        })
      },
    }
  }
</script>
<style scoped>
  .requiredPerm {
    color: #ff0e13;
  }
</style>
