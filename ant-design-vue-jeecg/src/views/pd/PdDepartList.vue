<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button
        @click="batchDel"
        v-if="selectedRowKeys.length > 0"
        ghost
        type="primary"
        icon="delete">批量删除
      </a-button>
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
               <a-menu-item>
                <a href="javascript:;" @click="handleAddPermissionSub(record)">添加部门权限</a>
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
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import PdDepartModal from './modules/PdDepartModal'
  import DepartAuthModal from './modules/DepartAuthModal'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import { queryPdDepaTreeList } from '@/api/api'

  export default {
    name: "PdDepartList",
    mixins:[JeecgListMixin],
    components: {
      JDictSelectTag,
      PdDepartModal,
      DepartAuthModal,
    },
    data () {
      return {
        description: '部门管理页面',
        expandedRowKeys :[],
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
          {
            title:'机构编码',
            align:"center",
            dataIndex: 'orgCode'
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
          delete: '/sys/sysDepart/delete',
          edit: '/sys/sysDepart/edit',
          deleteBatch: '/sys/sysDepart/deleteBatch',
          exportXlsUrl: "sys/sysDepart/exportXls",
          importExcelUrl: "sys/sysDepart/importExcel",
        },
        dictOptions:{
          orgType:[],
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
        })
      },
      initDictConfig(){
        initDictOptions('org_type').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'orgType', res.result)
          }
        })
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
        this.$refs.departAuth.title = "添加部门权限";
        this.$refs.departAuth.disableSubmit = false;
        this.$refs.departAuth.show(record.id);
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
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>