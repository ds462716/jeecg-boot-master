<template>
  <a-drawer :bordered="false"
            :visible="visible"
            :closable="true"
            :width="drawerWidth"
            :title="title"
            :maskClosable=disableSubmit
            @close="close"

            :wrapStyle="{height: 'calc(100% - 108px)',overflow: 'auto',paddingBottom: '108px'}">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <!-- 搜索区域 -->
      <a-form layout="inline">
        <a-row :gutter="10">
          <a-col :md="10" :sm="12">
            <a-form-item label="用户账号" style="margin-left:8px">
              <a-input placeholder="请输入账号" v-model="queryParam.username"></a-input>
            </a-form-item>
          </a-col>
          <!--<a-col :md="8" :sm="8">-->
          <!--<a-form-item label="用户名称" :labelCol="{span: 5}" :wrapperCol="{span: 18, offset: 1}">-->
          <!--<a-input placeholder="请输入名称查询" v-model="queryParam.realname"></a-input>-->
          <!--</a-form-item>-->
          <!--</a-col>-->
          <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
            <a-col :md="6" :sm="24">
             <a-button type="primary" @click="searchQuery" icon="search" style="margin-left: 18px">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </a-col>
          </span>
        </a-row>
      </a-form>
    </div>
    <!-- 操作按钮区域 -->

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{
        selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">

      </a-table>

      <div class="drawer-bootom-button">
        <a-button @click="handleSubmit()" type="primary">保存</a-button>
      </div>
    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
  </a-drawer>
</template>

<script>
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import {getAction, postAction, deleteAction} from '@/api/manage'
  import UserModal from './UserModal'

  export default {
    name: "DeptRoleUserInfo",
    mixins: [JeecgListMixin],
    components: {
      UserModal
    },
    data() {
      return {
        description: '用户信息',
        currentDeptId: '',
        departRoleId: '',
        visible: false,
        disableSubmit:false,
        drawerWidth:800,
        title:'',
        // 表头
        columns: [{
          title: '用户账号',
          align: "center",
          dataIndex: 'username'
        },
          {
            title: '用户名称',
            align: "center",
            dataIndex: 'realname'
          },
          {
            title: '性别',
            align: "center",
            dataIndex: 'sex_dictText'
          },
          {
            title: '电话',
            align: "center",
            dataIndex: 'phone'
          },
          /*{
            title: '部门',
            align: "center",
            dataIndex: 'orgCode'
          },*/
          ],
        url: {
          list: "/pd/pdDepart/departUserList",
          edit: "/sys/user/editSysDepartWithUser",
          delete: "/sys/user/deleteUserInDepart",
          saveDepartUserBatch: "/pd/pdDepart/saveDepartUserBatch",
        }
      }
    },
    created() {
    },

    methods: {
      searchReset() {
        this.queryParam = {}
        this.loadData(1);
        this.$emit('clearSelectedDepartKeys')
      },
      loadData(arg) {
        if(this.currentDeptId){
          this.loading = true;
          //加载数据 若传入参数1则加载第一页的内容
          if (arg === 1) {
            this.ipagination.current = 1;
          }
          //if (this.currentDeptId === '') return;
          let params = this.getQueryParams();//查询条件
          params.deptId = this.currentDeptId;
          getAction(this.url.list, params).then((res) => {
            if (res.success && res.result) {
              this.dataSource = res.result.records;
              this.ipagination.total = res.result.total;
              this.loading = false;
            }else{
              this.$message.error("系统异常!")
            }
          })
        }

      },
      batchDel: function () {

        if (!this.url.deleteBatch) {
          this.$message.error("请设置url.deleteBatch属性!")
          return
        }
        if (!this.currentDeptId) {
          this.$message.error("未选中任何部门，无法取消部门与用户的关联!")
          return
        }

        if (this.selectedRowKeys.length <= 0) {
          this.$message.warning('请选择一条记录！');
          return;
        } else {
          var ids = "";
          for (var a = 0; a < this.selectedRowKeys.length; a++) {
            ids += this.selectedRowKeys[a] + ",";
          }
          var that = this;
          console.log(this.currentDeptId);
          this.$confirm({
            title: "确认取消",
            content: "是否取消用户与选中部门的关联?",
            onOk: function () {
              deleteAction(that.url.deleteBatch, {depId: that.currentDeptId, userIds: ids}).then((res) => {
                if (res.success) {
                  that.$message.success("保存成功！");
                  that.loadData();
                  that.onClearSelected();
                } else {
                  that.$message.warning(res.message);
                }
              });
            }
          });
        }
      },
      handleDelete: function (id) {
        if (!this.url.delete) {
          this.$message.error("请设置url.delete属性!")
          return
        }
        if (!this.currentDeptId) {
          this.$message.error("未选中任何部门，无法取消部门与用户的关联!")
          return
        }

        var that = this;
        deleteAction(that.url.delete, {depId: this.currentDeptId, userId: id}).then((res) => {
          if (res.success) {
            that.$message.success("保存成功！");
            if (this.selectedRowKeys.length>0){
               for(let i =0; i<this.selectedRowKeys.length;i++){
                   if (this.selectedRowKeys[i] == id){
                     this.selectedRowKeys.splice(i,1);
                     break;
                   }
               }
            }
            that.loadData();
          } else {
            that.$message.warning(res.message);
          }
        });
      },
      show(departId,departRoleId){
        this.resetScreenSize(); // 调用此方法,根据屏幕宽度自适应调整抽屉的宽度
        this.visible = true;
        this.currentDeptId = departId;
        this.departRoleId = departRoleId;
        this.loadData(1);
      },
      clearList() {
        this.currentDeptId = '';
        this.dataSource = [];
      },
      close () {
        this.reset()
        this.$emit('close');
        this.visible = false;
      },
      reset () {
        this.checkedKeys = []
        this.loading = false
      },
      // 根据屏幕变化,设置抽屉尺寸
      resetScreenSize(){
        let screenWidth = document.body.clientWidth;
        if(screenWidth < 500){
          this.drawerWidth = screenWidth;
        }else{
          this.drawerWidth = 700;
        }
      },
      handleSubmit(){
        if (this.selectedRowKeys.length <= 0) {
          this.$message.warning('请选择一条记录！');
          return;
        }else {
          var ids = "";
          for (var a = 0; a < this.selectedRowKeys.length; a++) {
            ids += this.selectedRowKeys[a] + ",";
          }
          var that = this;
          deleteAction(that.url.saveDepartUserBatch, {departRoleId: that.departRoleId, userIds: ids}).then((res) => {
            if (res.success) {
              that.$message.success("保存成功！");
              that.loadData();
              that.onClearSelected();
            } else {
              that.$message.warning(res.message);
            }
          });
        }
      }
    }
  }
</script>
<style scoped>
  /** Button按钮间距 */
  .ant-btn {
    margin-left: 3px
  }

  .ant-card {
    margin-left: -30px;
    margin-right: -30px;
  }

  .table-page-search-wrapper {
    margin-top: -16px;
    margin-bottom: 16px;
  }
</style>