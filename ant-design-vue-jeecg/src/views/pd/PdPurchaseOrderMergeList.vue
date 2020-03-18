<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col  :md="6" :sm="8">
            <a-form-item label="合并申购单号">
              <a-input placeholder="请输入合并申购单号"   v-model="queryParam.mergeOrderNo"></a-input>
            </a-form-item>
          </a-col>
          <a-col  :md="6" :sm="8">
            <a-form-item label="合并申购日期">
              <j-date placeholder="请选择合并申购日期"  v-model="queryParam.purchaseDate"></j-date>
             </a-form-item>
          </a-col>
          <a-col  :md="6" :sm="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->
    
    <!-- 操作按钮区域 -->
    <div class="table-operator">
       <a-button type="primary" icon="download" @click="handleExportXls('合并申购单表')">导出</a-button>
    </div>

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
        :rowSelection="{fixed:false,selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">查看</a>
        </span>
      </a-table>
    </div>
    <pd-purchase-order-merge-modal ref="modalForm" @ok="modalFormOk"></pd-purchase-order-merge-modal>
  </a-card>
</template>

<script>
  import { JeecgListMixin ,handleEdit} from '@/mixins/JeecgListMixin'
  import PdPurchaseOrderMergeModal from './modules/PdPurchaseOrderMergeModal'
  import JDate from '@/components/jeecg/JDate.vue'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'

  export default {
    name: "PdPurchaseOrderMergeList",
    mixins:[JeecgListMixin],
    components: {
      JDate,
      PdPurchaseOrderMergeModal
    },
    data () {
      return {
        description: '合并申购单表管理页面',
        // 表头
        columns: [
          /*{
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },*/
          {
            title:'合并申购单号',
            align:"center",
            dataIndex: 'mergeOrderNo'
          },
          {
            title:'合并申购日期',
            align:"center",
            dataIndex: 'purchaseDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'审核科室',
            align:"center",
            dataIndex: 'departName'
          },
          {
            title:'审核状态',
            align:"center",
            dataIndex: 'auditStatus',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['auditStatus'], text+"")
              }
            }
          },
          {
            title:'操作人',
            align:"center",
            dataIndex: 'mergeName'
          },

          {
            title:'备注',
            align:"center",
            dataIndex: 'remarks'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/pd/pdPurchaseOrderMerge/list",
          exportXlsUrl: "/pd/pdPurchaseOrderMerge/exportXls",
          importExcelUrl: "pd/pdPurchaseOrderMerge/importExcel",
        },
        dictOptions:{
          auditStatus:[],
        },
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      handleEdit: function (record) { //查看
        this.$refs.modalForm.edit(record);
       // this.$refs.modalForm.title = "合并订单列表";
        this.$refs.modalForm.disableSubmit = false;
      },
      initDictConfig(){
        initDictOptions('audit_status').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'auditStatus', res.result)
          }
        })
      }
    }
  }
</script>
<style scoped>
</style>