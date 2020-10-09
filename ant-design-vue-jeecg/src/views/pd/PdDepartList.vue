<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <!-- 不放开批量删除  zxh-->
      <!--<a-button
        @click="batchDel"
        v-if="selectedRowKeys.length > 0"
        ghost
        type="primary"
        icon="delete">批量删除
      </a-button>-->
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i>已选择&nbsp;<a style="font-weight: 600">{{
        selectedRowKeys.length }}</a>项&nbsp;&nbsp;
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        :columns="columns"
        size="middle"
        :pagination="false"
        :dataSource="dataSource"
        :loading="loading"
        :customRow="onClickRow"
        :expandedRowKeys= "expandedRowKeys"
        @expand="handleExpand"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}">
        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">
              更多 <a-icon type="down"/>
            </a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a href="javascript:;" @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a href="javascript:;" @click="handleAddSub(record)">添加部门</a>
              </a-menu-item>
              <a-menu-item >
                <a href="javascript:;" @click="handleAddUserSub(record)">添加用户</a>
              </a-menu-item>
               <a-menu-item>
                <a href="javascript:;" @click="handleAddPermissionSub(record)">添加部门权限</a>
              </a-menu-item>
              <a-menu-item>
                <a href="javascript:;" @click="copyPermissionSub(record)">复制部门权限及部门角色权限</a>
              </a-menu-item>
              <a-menu-item>
                <a href="javascript:;" @click="pastePermissionSub(record)">粘贴部门权限及部门角色权限</a>
              </a-menu-item>
              <a-menu-item>
                <a href="javascript:;" @click="handleAddRoleSub(record)">添加部门角色</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>
        <!-- 字符串超长截取省略号显示 -->
        <span slot="url" slot-scope="text">
          <j-ellipsis :value="text" :length="25"/>
        </span>
        <!-- 字符串超长截取省略号显示-->
        <span slot="component" slot-scope="text">
          <j-ellipsis :value="text"/>
        </span>
      </a-table>
    </div>

    <pdDepart-modal ref="modalForm" @ok="modalFormOk"></pdDepart-modal>
    <depart-auth-modal ref="departAuth"></depart-auth-modal>
    <dept-role-info ref="deptRoleInfo"></dept-role-info>
    <dept-User-Info ref="deptUserInfo"></dept-User-Info>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import PdDepartModal from './modules/PdDepartModal'
  import DepartAuthModal from './modules/DepartAuthModal'
  import DeptRoleInfo from './modules/DeptRoleInfo'
  import DeptUserInfo from './modules/DeptUserInfo'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import { queryPdDepaTreeList } from '@/api/api'
  import { httpAction } from '@/api/manage'

  export default {
    name: "PdDepartList",
    mixins:[JeecgListMixin],
    components: {
      JDictSelectTag,
      PdDepartModal,
      DepartAuthModal,
      DeptRoleInfo,
      DeptUserInfo
    },
    data () {
      return {
        description: '部门管理页面',
        expandedRowKeys :[],
        copyPermission:"",//复制的部门权限
        // 表头
        columns: [
          {
            title:'机构名称',
            dataIndex: 'departName'
          },
          /*{
            title:'机构类型',
            align:"center",
            dataIndex: 'orgType',
            customRender:(text)=>{
              text +="";
              if(!text){
                return ''
              }else{
                return text +"级部门";
              }
            }
          },*/
          /*{
            title:'机构编码',
            align:"center",
            dataIndex: 'orgCode'
          },*/
          {
            title:'部门类型',
            align:"center",
            dataIndex: 'departType',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['departType'], text+"")
              }
            }
          },
          {
            title:'手机号',
            align:"center",
            dataIndex: 'mobile'
          },
          {
            title:'传真',
            align:"center",
            dataIndex: 'fax'
          },
          {
            title:'地址',
            align:"center",
            dataIndex: 'address'
          },
          {
            title: '操作',
            dataIndex: 'action',
            scopedSlots: { customRender: 'action' },
            width: 150
          }
        ],
        url: {
          list: "/pd/pdDepart/queryTreeList",
          copyPermission: "/pd/pdDepart/copyPermission",
          delete: '/pd/pdDepart/delete',
          edit: '/sys/sysDepart/edit',
          deleteBatch: '/sys/sysDepart/deleteBatch',
          exportXlsUrl: "sys/sysDepart/exportXls",
          importExcelUrl: "sys/sysDepart/importExcel",
        },
        dictOptions:{
          orgType:[],
          departType:[],
        },
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      loadData() {
        this.loading = true;
        this.dataSource = [];
        this.expandedRowKeys = [];
        this.expandedRowKeys = [];
        queryPdDepaTreeList().then((res) => {
          if (res.success) {
            this.dataSource = res.result;
            let resultData = res.result;
            if(resultData.length>0){
             this.recursion(resultData);
            }
          }
          this.loading = false;
        })
      },
      initDictConfig(){
        /*initDictOptions('org_type').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'orgType', res.result)
          }
        });*/
        initDictOptions('depart_type').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'departType', res.result)
          }
        });
      },
      handleAddSub(record) {
        this.$refs.modalForm.title = "添加部门";
        this.$refs.modalForm.localDepartType = 1;
        this.$refs.modalForm.disableSubmit = false;
        this.$refs.modalForm.edit({route:true,'parentId':record.id});
      },
      handleAdd: function () {
        this.$refs.modalForm.add();
        this.$refs.modalForm.localDepartType = 1;
        this.$refs.modalForm.title = "新增";
        this.$refs.modalForm.disableSubmit = false;
      },
      handleAddPermissionSub(record) {
        if(record.parentId){
          this.$refs.departAuth.title = "当前选择部门:"+record.departName;
          this.$refs.departAuth.disableSubmit = false;
          this.$refs.departAuth.show(record.id);
        }else{
          this.$message.error("当前机构不能添加权限!")
        }
      },
      handleAddRoleSub(record){
        if(record.parentId){
          this.$refs.deptRoleInfo.title = "当前选择部门:"+record.departName;
          this.$refs.deptRoleInfo.disableSubmit = false;
          this.$refs.deptRoleInfo.show(record.id);
        }else{
          this.$message.error("当前机构不能添加角色!")
        }
      },
      handleAddUserSub(record){
        if(record.parentId){
          this.$refs.deptUserInfo.title = "当前选择部门:"+record.departName;
          this.$refs.deptUserInfo.disableSubmit = false;
          this.$refs.deptUserInfo.show(record.id);
        }else{
          this.$message.error("当前机构不能添加用户!")
        }
      },
      /**
       * 点击行选中checkbox
       * @param record
       * @returns {{on: {click: on.click}}}
       */
      onClickRow(record) {
        return {
          on: {
            click: (e) => {
              //点击操作那一行不选中表格的checkbox
              let pathArray = e.path;
              //获取当前点击的是第几列
              let td = pathArray[0];
              let cellIndex = td.cellIndex;
              //获取tr
              let tr = pathArray[1];
              //获取一共多少列
              let lie = tr.childElementCount;
              if(lie && cellIndex){
                if(parseInt(lie)-parseInt(cellIndex)!=1){
                  //操作那一行
                  let recordId = record.id;
                  let index = this.selectedRowKeys.indexOf(recordId);
                  if(index>=0){
                    this.selectedRowKeys.splice(index, 1);
                  }else{
                    this.selectedRowKeys.push(recordId);
                  }
                }
              }
            }
          }
        }
      },

      /**
       * 递归展开所有节点
       * @param resultData
       */
      recursion (resultData) {
        for(let i = 0;i<resultData.length;i++){
          let treeData = resultData[i];
          if(treeData.children && treeData.children.length>0){
            this.expandedRowKeys.push(resultData[i].id);
            this.recursion(treeData.children);
          }
        }
      },
      /**
       * 收拢元素或展开元素
       * @param expanded
       * @param record
       */
      handleExpand (expanded, record){
        let index  = this.expandedRowKeys.indexOf(record.id);
        if(index > -1){
          this.expandedRowKeys.splice(index,1);
        }else{
          this.expandedRowKeys.push(record.id);
        }
      },
      /**
       * 复制部门权限
       * @param record
       */
      copyPermissionSub(record){
        if(record.parentId){
          this.copyPermission = record.id;
          this.$message.success("复制成功!")
        }else{
          this.$message.error("不能复制当前机构的权限!")
        }
      },
      /**
       * 粘贴部门权限
       * @param record
       */
      pastePermissionSub(record){
        if(record.parentId){
          if(this.copyPermission){
            if(this.copyPermission == record.id){
              this.$message.error("不能复制粘贴同一个!")
            }else{
              let formData = new URLSearchParams();
              formData.append("copyId",this.copyPermission);
              formData.append("pasteId",record.id);
              this.loading = true;
              httpAction(this.url.copyPermission,formData,"post").then((res)=>{
                if(res.success){
                  this.$message.success("粘贴成功!")
                  this.loading = false;
                }else{
                  this.$message.warning(res.message);
                  this.loading = false;
                }
              }).finally(() => {
                this.loading = false;
              })
            }
          }else{
            this.$message.error("请先复制产品权限!")
          }
        }else{
          this.$message.error("不能这样操作")
        }
      },

    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>