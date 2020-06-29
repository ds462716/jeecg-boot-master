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
    <div class="table-operator">
      <!--<a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>-->
      <a-button @click="handleAddInvoice" type="primary" icon="plus">发票维护</a-button>
      <a-button @click="" type="primary" icon="check">完成</a-button>
      <a-button @click="" type="primary" icon="printer">打印</a-button>

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
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="billDetailId"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :scroll="tableScroll"
        :customRow="onClickRow"
        :rowSelection="{fixed:false,selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">
        <!--<span slot="action" slot-scope="text, record">-->
          <!--<a @click="handleEdit(record)">发票维护</a>-->
        <!--</span>-->
      </a-table>
    </div>

    <pd-invoice-add-modal ref="modalForm" @ok="modalFormOk"></pd-invoice-add-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import PdInvoiceAddModal from './modules/PdInvoiceAddModal'

  export default {
    name: "PdInvoiceAddList",
    mixins:[JeecgListMixin],
    components: {
      PdInvoiceAddModal
    },
    data () {
      return {
        description: 'pd_invoice管理页面',
        tableScroll:{x :2000},
        dataSource2:[],
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'登记号',
            align:"center",
            dataIndex: 'invoiceRegNo'
          },
          {
            title:'发票号',
            align:"center",
            dataIndex: 'invoiceNo'
          },
          {
            title:'发票代码',
            align:"center",
            dataIndex: 'invoiceCode'
          },
          {
            title:'发票日期',
            align:"center",
            dataIndex: 'invoiceData',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'状态',
            align:"center",
            dataIndex: 'status',
            customRender:function (text) {
              return !text?"":(text=='2'?'已完成':'未完成')
            }
          },
          {
            title:'供应商',
            align:"center",
            dataIndex: 'supplierName'
          },
          {
            title:'入库单号',
            align:"center",
            dataIndex: 'billNo'
          },
          {
            title:'入库日期',
            align:"center",
            dataIndex: 'billDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'产品编号',
            align:"center",
            dataIndex: 'productNumber'
          },
          {
            title:'产品名称',
            align:"center",
            dataIndex: 'productName'
          },
          {
            title:'规格',
            align:"center",
            dataIndex: 'spec'
          },
          {
            title:'采购价',
            align:"center",
            dataIndex: 'price'
          },
          {
            title:'入库数量',
            align:"center",
            dataIndex: 'num'
          },
          {
            title:'应付金额',
            align:"center",
            dataIndex: 'money'
          },
          {
            title:'库存数量',
            align:"center",
            dataIndex: 'stockNum'
          },
          {
            title:'批号',
            align:"center",
            dataIndex: 'batchNo'
          },
          {
            title:'有效期',
            align:"center",
            dataIndex: 'expDate'
          },
          {
            title:'备注',
            align:"center",
            dataIndex: 'remarks'
          },
          // {
          //   title:'发票金额',
          //   align:"center",
          //   dataIndex: 'invoiceMoney'
          // },
          // {
          //   title:'回款金额',
          //   align:"center",
          //   dataIndex: 'returnMoney'
          // },
          // {
          //   title:'回款日期',
          //   align:"center",
          //   dataIndex: 'returnData',
          //   customRender:function (text) {
          //     return !text?"":(text.length>10?text.substr(0,10):text)
          //   }
          // },
          // {
          //   title:'发票类型；1-入库单发票；2-出库单发票',
          //   align:"center",
          //   dataIndex: 'invoiceType'
          // },
          // {
          //   title:'支付状态：1-已支付；2-未支付',
          //   align:"center",
          //   dataIndex: 'payStatus'
          // },
          // {
          //   title:'发票状态：1-未完成；2-已完成',
          //   align:"center",
          //   dataIndex: 'invoiceStatus'
          // },
          // {
          //   title:'单据登记人（入库单创建人）',
          //   align:"center",
          //   dataIndex: 'billBy'
          // },
          // {
          //   title:'创建人',
          //   align:"center",
          //   dataIndex: 'createBy'
          // },
          // {
          //   title:'创建日期',
          //   align:"center",
          //   dataIndex: 'createTime',
          //   customRender:function (text) {
          //     return !text?"":(text.length>10?text.substr(0,10):text)
          //   }
          // },
          // {
          //   title: '操作',
          //   dataIndex: 'action',
          //   align:"center",
          //   fixed: 'right',
          //   width: 100,
          //   scopedSlots: { customRender: 'action' },
          // }
        ],
        url: {
          list: "/pd/pdInvoice/listByStockRecord",
          delete: "/pd/pdInvoice/delete",
          deleteBatch: "/pd/pdInvoice/deleteBatch",
          exportXlsUrl: "/pd/pdInvoice/exportXls",
          importExcelUrl: "pd/pdInvoice/importExcel",
        },
        dictOptions:{},
      }
    },
    computed: {
    },
    methods: {
      initDictConfig(){
      },
      handleAddInvoice(){
        if(this.selectedRowKeys.length < 1){
          this.$message.warning("请选择一条记录！");
          return;
        }
        if(this.selectedRowKeys.length > 1){
          this.$message.warning("维护发票不能选择多条记录，只能选择一条记录！");
          return;
        }
        this.$refs.modalForm.add();
        this.$refs.modalForm.title = "维护发票";
        this.$refs.modalForm.disableSubmit = false;

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
                if(parseInt(lie)-parseInt(cellIndex) > 0){
                  //操作那一行
                  let recordId = record.billDetailId;
                  let index = this.selectedRowKeys.indexOf(recordId);
                  if(index>=0){
                    this.selectedRowKeys.splice(index, 1);
                    this.dataSource2.splice(index, 1);
                  }else{
                    this.selectedRowKeys.push(recordId);
                    this.dataSource2.push(record);
                  }
                }
              }
            }
          }
        }
      },
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>