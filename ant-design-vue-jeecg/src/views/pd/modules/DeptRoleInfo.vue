<template>
  <a-drawer :bordered="false"
            :visible="visible"
            :closable="true"
            :width="drawerWidth"
            @close="close"
            :title="title"
            :maskClosable=disableSubmit
            :wrapStyle="{height: 'calc(100% - 108px)',overflow: 'auto',paddingBottom: '108px'}"
  >
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <!-- 搜索区域 -->
      <a-form layout="inline">
        <a-row :gutter="10">
          <a-col :md="10" :sm="12">
            <a-form-item label="部门角色名称" style="margin-left:8px">
              <a-input placeholder="请输入部门角色" v-model="queryParam.roleName"></a-input>
            </a-form-item>
          </a-col>
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
    <div class="table-operator" :md="24" :sm="24">
      <a-button @click="handleAdd" type="primary" icon="plus">部门角色录入</a-button>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>
    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">
          {{selectedRowKeys.length }}</a>项
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
        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">
              更多 <a-icon type="down"/>
            </a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handlePerssion(record)">授权</a>
              </a-menu-item>
              <a-menu-item>
                <a @click="addDepartUser(record)">绑定用户</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
              <!--<a-menu-item>
                <a href="javascript:;" @click="handleDetail(record)">详情</a>
              </a-menu-item>-->
            </a-menu>
          </a-dropdown>
        </span>
      </a-table>
    </div>
    <!-- table区域-end -->
    <!-- 表单区域 -->
    <depart-role-modal ref="modalForm" @ok="modalFormOk"/>
    <dept-role-auth-modal ref="modalDeptRole" />
    <dept-role-user-info ref="deptRoleUserInfo" />
  </a-drawer>
</template>

<script>
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import {getAction} from '@/api/manage'
  import DepartRoleModal from './DepartRoleModal'
  import DeptRoleAuthModal from './DeptRoleAuthModal'
  import DeptRoleUserInfo from './DeptRoleUserInfo'

  export default {
    name: 'DeptRoleInfo',
    components: {
      DeptRoleAuthModal,
      DepartRoleModal,
      DeptRoleUserInfo
    },
    mixins: [JeecgListMixin],
    data() {
      return {
        description: '部门角色信息',
        currentDeptId: '',
        drawerWidth:800,
        visible: false,
        disableSubmit:false,
        title:"",
        // 表头
        columns: [{
          title: '部门角色名称',
          align: "center",
          dataIndex: 'roleName'
        },
        {
          title: '部门角色编码',
          align: "center",
          dataIndex: 'roleCode'
        },
        {
          title: '部门',
          align: "center",
          dataIndex: 'departId_dictText'
        },
        {
          title: '备注',
          align: "center",
          dataIndex: 'description'
        },
        {
          title: '操作',
          dataIndex: 'action',
          scopedSlots: {customRender: 'action'},
          align: "center",
          width: 170
        }],
        url: {
          list: "/pd/pdDepart/queryDepartRole",
          delete: "/sys/sysDepartRole/delete",
          deleteBatch: "/sys/sysDepartRole/deleteBatch",
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
      show(departId){
        this.resetScreenSize(); // 调用此方法,根据屏幕宽度自适应调整抽屉的宽度
        this.visible = true;
        this.currentDeptId=departId
        this.loadData(1);
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
      clearList() {
        this.currentDeptId = '';
        this.dataSource = [];
      },
      hasSelectDept() {
        if (this.currentDeptId == '') {
          this.$message.error("请选择一个部门!")
          return false;
        }
        return true;
      },
      handleEdit: function (record) {
        this.$refs.modalForm.title = "编辑";
        this.$refs.modalForm.departDisabled = true;
        this.$refs.modalForm.disableSubmit = false;
        this.$refs.modalForm.edit(record,record.departId);
      },
      handleAdd: function () {
        if (this.currentDeptId == '') {
          this.$message.error("请选择一个部门!")
        } else {
          this.$refs.modalForm.departDisabled = true;
          this.$refs.modalForm.add(this.currentDeptId);
          this.$refs.modalForm.title = "新增";
        }
      },
      handlePerssion: function(record){
        this.$refs.modalDeptRole.show(record.id,record.departId);
      },
      addDepartUser: function(record){
        this.$refs.deptRoleUserInfo.show(record.departId,record.id);
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
    }
  }
</script>

<style scoped>

</style>