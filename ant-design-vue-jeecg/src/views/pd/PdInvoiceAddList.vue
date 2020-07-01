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
      <a-button @click="handleAddInvoice" type="primary" icon="plus">维护发票</a-button>
      <a-button @click="handleEditInvoice" type="primary" icon="plus">修改</a-button>
      <a-button @click="handleCompleteInvoice" type="primary" icon="check">完成</a-button>
      <a-button @click="handlePrintInvoice" type="primary" icon="printer">打印</a-button>

      <!--<a-dropdown v-if="selectedRowKeys.length > 0">-->
        <!--<a-menu slot="overlay">-->
          <!--<a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>-->
        <!--</a-menu>-->
        <!--<a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>-->
      <!--</a-dropdown>-->
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="small"
        bordered
        rowKey="billDetailId"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :scroll="tableScroll"
        :customRow="onClickRow"
        :rowSelection="{fixed:false,selectedRowKeys: selectedRowKeys, onSelectAll:onSelectAll,onSelect:onSelect,onChange: onSelectChange}"
        @change="handleTableChange">
        <!--<span slot="action" slot-scope="text, record">-->
          <!--<a @click="handleEdit(record)">发票维护</a>-->
        <!--</span>-->

        <template slot="ellipsisText" slot-scope="text">
          <j-ellipsis :value="text" :length="textMaxLength"></j-ellipsis>
        </template>

      </a-table>
    </div>

    <pd-invoice-add-modal ref="modalForm" @ok="modalFormOk"></pd-invoice-add-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import PdInvoiceAddModal from './modules/PdInvoiceAddModal'
  import JEllipsis from '@/components/jeecg/JEllipsis'

  export default {
    name: "PdInvoiceAddList",
    mixins:[JeecgListMixin],
    components: {
      PdInvoiceAddModal,JEllipsis
    },
    data () {
      return {
        description: 'pd_invoice管理页面',
        tableScroll:{x :2500},
        textMaxLength:20,
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
            width:100,
            dataIndex: 'invoiceRegNo'
          },
          {
            title:'发票号',
            align:"center",
            width:100,
            scopedSlots: {customRender: "ellipsisText"},
            dataIndex: 'invoiceNo'
          },
          {
            title:'发票代码',
            align:"center",
            width:100,
            scopedSlots: {customRender: "ellipsisText"},
            dataIndex: 'invoiceCode'
          },
          {
            title:'发票日期',
            align:"center",
            width:90,
            dataIndex: 'invoiceData',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'状态',
            align:"center",
            width:80,
            dataIndex: 'status',
            customRender:function (text) {
              return !text?"":(text=='2'?'已完成':'未完成')
            }
          },
          {
            title:'供应商',
            align:"center",
            width:200,
            dataIndex: 'supplierName'
          },
          {
            title:'入库单号',
            align:"center",
            width:100,
            dataIndex: 'billNo'
          },
          {
            title:'入库日期',
            align:"center",
            width:90,
            dataIndex: 'billDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'产品编号',
            align:"center",
            width:100,
            dataIndex: 'productNumber'
          },
          {
            title:'产品名称',
            align:"center",
            width:150,
            dataIndex: 'productName'
          },
          {
            title:'规格',
            align:"center",
            width:150,
            dataIndex: 'spec'
          },
          {
            title:'采购价',
            align:"center",
            width:80,
            dataIndex: 'price'
          },
          {
            title:'入库数量',
            align:"center",
            width:80,
            dataIndex: 'num'
          },
          {
            title:'库存数量',
            align:"center",
            width:80,
            dataIndex: 'stockNum'
          },
          {
            title:'应付金额',
            align:"center",
            width:80,
            dataIndex: 'money'
          },
          {
            title:'批号',
            align:"center",
            width:100,
            dataIndex: 'batchNo'
          },
          {
            title:'有效期',
            align:"center",
            width:90,
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
        this.dataSource2 = [];
      },
      //维护发票
      handleAddInvoice(){
        if(this.selectedRowKeys.length < 1){
          this.$message.warning("至少选择一条入库明细！");
          return;
        }
        for(let item of this.dataSource2){
          if(item.id){
            this.$message.warning("请选择未维护发票的入库明细！");
            return;
          }
        }
        this.$refs.modalForm.add(this.selectedRowKeys);
        this.$refs.modalForm.title = "维护发票";
        this.$refs.modalForm.disableSubmit = false;
      },
      //修改发票
      handleEditInvoice(){
        if(this.selectedRowKeys.length < 1){
          this.$message.warning("至少选择一条发票！");
          return;
        }
        for(let item of this.dataSource2){
          if(!item.id){
            this.$message.warning("请选择已维护发票的入库明细！");
            return;
          }
          for(let item2 of this.dataSource2){
            if(item.invoiceRegNo != item2.invoiceRegNo){
              this.$message.warning("只能选择登记号相同的发票修改！");
              return;
            }
          }
        }
        this.$refs.modalForm.edit(this.dataSource2);
        this.$refs.modalForm.title = "修改发票";
        this.$refs.modalForm.disableSubmit = false;
      },
      // 完成
      handleCompleteInvoice(){
        if(this.selectedRowKeys.length < 1){
          this.$message.warning("至少选择一条记录！");
          return;
        }

      },
      // 打印
      handlePrintInvoice(){
        if(this.selectedRowKeys.length < 1){
          this.$message.warning("至少选择一条记录！");
          return;
        }

      },
      onSelectAll(selected, selectedRows, changeRows) {
        if (selected === true) {
          for (var a = 0; a < changeRows.length; a++) {
            this.dataSource2.push(changeRows[a]);
          }
        } else {
          for (var b = 0; b < changeRows.length; b++) {
            this.dataSource2.splice(this.dataSource2.indexOf(changeRows[b]), 1);
          }
        }
      },
      onSelect(record, selected) {
        if (selected === true) {
          this.dataSource2.push(record);
        } else {
          var index = this.dataSource2.indexOf(record);
          if (index >= 0) {
            this.dataSource2.splice(this.dataSource2.indexOf(record), 1);
          }

        }
      },
      onSelectChange(selectedRowKeys, selectedRows) {
        this.selectedRowKeys = selectedRowKeys;
        this.selectionRows = selectedRows;
      },
      onClearSelected() {
        this.selectedRowKeys = [];
        this.selectionRows = [];
      },
      handleDelete: function (record) {
        this.dataSource2.splice(this.dataSource2.indexOf(record), 1);
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