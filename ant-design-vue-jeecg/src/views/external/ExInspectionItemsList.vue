<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">

        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->
    
    <!-- 操作按钮区域 -->
    <!--<div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>-->

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
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
        :scroll="tableScroll"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        
        @change="handleTableChange">

        <span slot="action" slot-scope="text, record">
          <a @click="editUsePackageDetail(record)" v-bind:disabled="record.acceptStatus=='0'">重新扣减</a>&nbsp;&nbsp;&nbsp;
         <a  @click="queryUsePackageDetail(record)">查看检验包详情</a>
        </span>

      </a-table>
    </div>

    <exInspectionItems-modal ref="modalForm" @ok="modalFormOk"></exInspectionItems-modal>
    <pdUsePackage-modal ref="pdUsePackageModal" @ok="modalFormOk"></pdUsePackage-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import ExInspectionItemsModal from './modules/ExInspectionItemsModal'
  import PdUsePackageModal from '../pd/modules/PdUsePackageModal'
  import {getAction} from '@/api/manage'

  export default {
    name: "ExInspectionItemsList",
    mixins:[JeecgListMixin],
    components: {
      ExInspectionItemsModal,
      PdUsePackageModal
    },
    data () {
      return {
        description: '检查项目表管理页面',
        // 表头
        columns: [
          {
            title:'患者姓名',
            align:"center",
            dataIndex: 'patientName'
          },
          {
            title:'患者性别',
            align:"center",
            dataIndex: 'patientSex'
          },
          {
            title:'患者年龄',
            align:"center",
            dataIndex: 'patientAge'
          },
          {
            title:'就诊卡号',
            align:"center",
            dataIndex: 'cardId'
          },
          {
            title:'条形码',
            align:"center",
            dataIndex: 'barCode'
          },
          {
            title:'申请医生',
            align:"center",
            dataIndex: 'applyDoctor'
          },
          {
            title:'申请科室',
            align:"center",
            dataIndex: 'applyDepartment'
          },
          {
            title:'检验医生',
            align:"center",
            dataIndex: 'testDoctor'
          },
          {
            title:'检验科室',
            align:"center",
            dataIndex: 'testDepartment'
          },
          {
            title:'患者类型',
            align:"center",
            dataIndex: 'patientType'
          },
          {
            title:'工作组',
            align:"center",
            dataIndex: 'groupBy'
          },
          {
            title:'接收日期',
            align:"center",
            dataIndex: 'receiveDate'
          },
          {
            title:'检验日期',
            align:"center",
            dataIndex: 'testDate'
          },
          {
            title:'样本类型',
            align:"center",
            dataIndex: 'specimenType'
          },
          {
            title:'状态',
            align:"center",
            dataIndex: 'state'
          },
          {
            title:'项目组合名称',
            align:"center",
            dataIndex: 'combinationName'
          },
          {
            title:'项目组合代码',
            align:"center",
            dataIndex: 'combinationCode'
          },
          {
            title:'检查项目名称',
            align:"center",
            dataIndex: 'testItemName'
          },
          {
            title:'检查项目代码',
            align:"center",
            dataIndex: 'testItemCode'
          },
          {
            title:'检查项目费用',
            align:"center",
            dataIndex: 'testItemCost'
          },
          {
            title:'读取状态',
            align:"center",
            dataIndex: 'acceptStatus'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:140,
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/external/exInspectionItems/list",
          queryUsePackageDetail: "/pd/pdUsePackage/queryUsePackageDetail",
          delete: "/external/exInspectionItems/delete",
          deleteBatch: "/external/exInspectionItems/deleteBatch",
          exportXlsUrl: "/external/exInspectionItems/exportXls",
          importExcelUrl: "external/exInspectionItems/importExcel",
        },
        dictOptions:{},
        tableScroll:{x :10*140+30},
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      initDictConfig(){
      },
      //重新扣减
      editUsePackageDetail: function (record) {
        //TODO 试剂扣减算法
      },
      //查询检验包详情
      queryUsePackageDetail: function (record) {
        getAction(this.url.queryUsePackageDetail, {testItemCode:record.testItemCode}).then((res) => {
          if (res.success) {
            this.$refs.pdUsePackageModal.edit(res.result);
            this.$refs.pdUsePackageModal.title = "详情";
            this.$refs.pdUsePackageModal.disableSubmit = true;
          }else{
            this.$message.warning(res.message);
          }
        })

      },
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>