<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="入库单号">
              <a-input placeholder="请输入单号" v-model="queryParam.recordNo"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="供应商">
              <a-select
                ref="supplierSelect"
                showSearch
                :supplierId="supplierValue"
                placeholder="请选择供应商"
                :defaultActiveFirstOption="false"
                :showArrow="true"
                :allowClear="true"
                :filterOption="false"
                @search="supplierHandleSearch"
                @change="supplierHandleChange"
                @focus="supplierHandleSearch"
                :notFoundContent="notFoundContent"
                v-model="queryParam.supplierId"
              >
                <a-select-option v-for="d in supplierData" :key="d.value">{{d.text}}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="产品名称">
              <a-input placeholder="请选输入品名称" v-model="queryParam.productName"></a-input>
            </a-form-item>
          </a-col>

          <a-col :md="6" :sm="8">
            <a-form-item label="产品编号">
              <a-input placeholder="请输入产品编号" v-model="queryParam.productNumber"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="入库日期">
              <a-range-picker @change="inDateChange" v-model="queryParam.queryInDate"/>
            </a-form-item>
          </a-col>
          <!--<a-col :md="6" :sm="8">-->
            <!--<a-form-item label="有效期">-->
              <!--<a-range-picker @change="expDateChange" v-model="queryParam.queryExpDate"/>-->
            <!--</a-form-item>-->
          <!--</a-col>-->
          <!--<a-col :md="6" :sm="8">-->
            <!--<a-form-item label="注册证">-->
              <!--<a-input placeholder="请输入注册证" v-model="queryParam.registration"></a-input>-->
            <!--</a-form-item>-->
          <!--</a-col>-->
          <a-col :md="6" :sm="8">
            <a-form-item label="规格">
              <a-input placeholder="请输入规格" v-model="queryParam.spec"></a-input>
            </a-form-item>
          </a-col>
          <!--<a-col :md="6" :sm="8">-->
            <!--<a-form-item label="型号">-->
              <!--<a-input placeholder="请输入型号" v-model="queryParam.version"></a-input>-->
            <!--</a-form-item>-->
          <!--</a-col>-->
          <a-col :md="6" :sm="8">
            <a-form-item label="批号">
              <a-input placeholder="请输入批号" v-model="queryParam.batchNo"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="生产厂家">
              <a-select
                showSearch
                :venderId="venderValue"
                placeholder="请选择生产厂家"
                :defaultActiveFirstOption="false"
                :allowClear="true"
                :showArrow="true"
                :filterOption="false"
                @search="venderHandleSearch"
                @change="venderHandleChange"
                @focus="venderHandleSearch"
                :notFoundContent="notFoundContent"
                v-model="queryParam.venderId"
              >
                <a-select-option v-for="d in venderData" :key="d.value">{{d.text}}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="是否完成">
              <a-select  default-value="1" v-model="queryParam.status" >
                <a-select-option value="1">未完成</a-select-option>
                <a-select-option value="2">已完成</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>

          <!--<template v-if="toggleSearchStatus"> -->
          <!--</template>-->

          <a-col :md="6" :sm="8">
            <span style="float: right;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <!--<a @click="handleToggleSearch" style="margin-left: 8px">-->
                <!--{{ toggleSearchStatus ? '收起' : '展开' }}-->
                <!--<a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>-->
              <!--</a>-->
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->
    
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAddInvoice" type="primary" icon="plus">维护发票</a-button>
      <!--<a-button @click="handleEditInvoice" type="primary" icon="edit">修改</a-button>-->
      <!--<a-button @click="handleDeleteInvoice" type="primary" icon="delete">删除</a-button>-->
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
        <span slot="action" slot-scope="text, record">
          <a @click="handleEditInvoice(record)" v-bind:disabled="!record.invoiceNo">修改</a>
          <a-divider type="vertical" />
          <a href="javascript:;" @click="handleDetail(record)" v-bind:disabled="!record.invoiceNo">详情</a>
           <a-divider type="vertical" />
           <a-dropdown>
            <a class="ant-dropdown-link"  v-bind:disabled="!record.invoiceNo" >更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDeleteInvoice(record)" v-bind:disabled="!record.invoiceNo">删除</a>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

        <template slot="ellipsisText" slot-scope="text">
          <j-ellipsis :value="text" :length="textMaxLength"></j-ellipsis>
        </template>

      </a-table>
    </div>

    <pd-invoice-add-modal ref="modalForm" @ok="modalFormOk"></pd-invoice-add-modal>
    <pd-stock-record-in-invoice-print-modal ref="pdStockRecordInInvoicePrintModal"></pd-stock-record-in-invoice-print-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import PdInvoiceAddModal from './modules/PdInvoiceAddModal'
  import JEllipsis from '@/components/jeecg/JEllipsis'
  import {httpAction, deleteAction, getAction} from '@/api/manage'
  import { filterObj } from '@/utils/util';
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import JDictSelectTagExpand from "@/components/dict/JDictSelectTagExpand"
  import PdStockRecordInInvoicePrintModal from "./print/PdStockRecordInInvoicePrintModal";


  export default {
    name: "PdInvoiceAddList",
    mixins:[JeecgListMixin],
    components: {
      PdStockRecordInInvoicePrintModal,
      PdInvoiceAddModal,JEllipsis,JDictSelectTagExpand
    },
    data () {
      return {
        description: 'pd_invoice管理页面',
        tableScroll:{x :2700},
        textMaxLength:20,
        dataSource2:[],
        hospitalCode:"",
        userName:"",
        departName:"",

        notFoundContent:"未找到内容",
        supplierValue: undefined,
        supplierData: [],

        venderValue: undefined,
        venderData: [],

        departValue: undefined,
        departList:[],
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
          // {
          //   title:'登记号',
          //   align:"center",
          //   width:100,
          //   dataIndex: 'invoiceRegNo'
          // },
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
            title:'发票金额',
            align:"center",
            width:90,
            dataIndex: 'invoiceMoney'
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
            title:'单位',
            align:"center",
            width:50,
            dataIndex: 'unitName'
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
            title:'生产厂家',
            align:"center",
            width:200,
            dataIndex: 'venderName'
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
            fixed: 'right',
            width: 150,
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/pd/pdInvoice/listByStockRecord",
          delete: "/pd/pdInvoice/delete",
          deleteBatch: "/pd/pdInvoice/deleteBatch",
          completeInvoice:"/pd/pdInvoice/completeInvoice",
          exportXlsUrl: "/pd/pdInvoice/exportXls",
          importExcelUrl: "pd/pdInvoice/importExcel",
          querySupplier:"/pd/pdSupplier/getSupplierList",
          queryVender:"/pd/pdVender/getVenderList",
          departList: "/pd/pdDepart/queryListTree",
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
      loadData(arg) {
        if(!this.url.list){
          this.$message.error("请设置url.list属性!")
          return
        }

        this.onClearSelected();
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        var params = this.getQueryParams();//查询条件
        this.loading = true;
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.dataSource = res.result.pageList.records;
            this.ipagination.total = res.result.pageList.total;
            this.hospitalCode = res.result.hospitalCode;
            this.userName = res.result.userName;
            this.departName = res.result.departName;
          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
          this.loading = false;
        })
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
      // 修改发票
      handleEditInvoice(record){
        var that = this;
        // this.$confirm({
        //   title: "确认修改",
        //   content: "相同登记号的发票会一起修改，是否确定修改?",
        //   onOk: function () {
            that.$refs.modalForm.edit(record);
            that.$refs.modalForm.title = "修改发票";
            that.$refs.modalForm.disableSubmit = false;
        //   }
        // });
      },
      //删除
      handleDeleteInvoice(record){
        var that = this;
        this.$confirm({
          title: "确认删除",
          content: "是否确定删除?",
          onOk: function () {
            deleteAction(that.url.delete, {id: record.invoiceId}).then((res) => {
              that.loadData();
              if (res.success) {
                that.$message.success(res.message);
              } else {
                that.$message.warning(res.message);
              }
            });
          }
        });
      },
      // 完成
      handleCompleteInvoice(){
        if(this.selectedRowKeys.length < 1){
          this.$message.warning("至少选择一条记录！");
          return;
        }
        let idList = [];
        for(let item of this.dataSource2){
          if(!item.id){
            this.$message.warning("请选择已维护发票的入库明细！");
            return;
          }
          idList.push(item.id);
        }

        var that = this;
        this.$confirm({
          title: "确认完成",
          content: "是否确定完成选中数据?",
          onOk: function () {
            that.loading = true;
            httpAction(that.url.completeInvoice,{idList: idList},"put").then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.loadData();
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.loading = false;
            })
          }
        });

      },
      // 打印
      handlePrintInvoice(){
        if(this.selectedRowKeys.length < 1){
          this.$message.warning("至少选择一条记录！");
          return;
        }
        for(let item of this.dataSource2){
          if(!item.id){
            this.$message.warning("请选择已维护发票的入库明细！");
            return;
          }
        }
        let title = ""
        // if(this.hospitalCode == "FCZYY"){
        //   title = "丰城市中医院";
        // }
        switch(this.hospitalCode) {
          case "FCZYY":
            title = "丰城市中医院";
            break;
          case "FCRMYY":
            title = "丰城市人民医院";
            break;
          default:
            title = "";
        }
        this.$refs.pdStockRecordInInvoicePrintModal.show(this.dataSource2,this.userName,this.departName);
        this.$refs.pdStockRecordInInvoicePrintModal.title = title+"供货商验收明细";
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
        this.dataSource2 = [];
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

      //↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓查询条件相关↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓//
      // 部门下拉框搜索
      departHandleSearch(value){
        getAction(this.url.departList,{departName:value}).then((res)=>{
          if (!res.success) {
            this.cmsFailed(res.message);
          }
          this.departList = res.result;
        })
      },
      //供应商查询start
      supplierHandleSearch(value) {
        this.getList(value,this.url.querySupplier,"1");
      },
      supplierHandleChange(value) {
        this.supplierValue = value;
        this.getList(value,this.url.querySupplier,"1");
      },
      //供应商查询end
      //生产厂家查询start
      venderHandleSearch(value) {
        this.getList(value,this.url.queryVender,"2");
      },
      venderHandleChange(value) {
        this.venderValue = value;
        this.getList(value,this.url.queryVender,"2");
      },
      //生产厂家查询end
      getList(value,url,flag){
        getAction(url,{name:value}).then((res)=>{
          if (!res.success) {
            this.cmsFailed(res.message);
          }
          const result = res.result;
          const data = [];
          result.forEach(r => {
            data.push({
              value: r.id,
              text: r.name,
            });
          });
          if(flag == "1"){
            this.supplierData = data;
          }else if(flag == "2"){
            this.venderData = data;
          }
        })
      },
      expDateChange: function (value, dateString) {
        this.queryParam.queryExpDateStart=dateString[0];
        this.queryParam.queryExpDateEnd=dateString[1];
      },
      inDateChange: function (value, dateString) {
        this.queryParam.queryDateStart=dateString[0];
        this.queryParam.queryDateEnd=dateString[1];
      },
      getQueryParams() {
        //获取查询条件
        let sqp = {}
        if(this.superQueryParams){
          sqp['superQueryParams']=encodeURI(this.superQueryParams)
        }
        var param = Object.assign(sqp, this.queryParam, this.isorter ,this.filters);
        param.field = this.getQueryField();
        param.pageNo = this.ipagination.current;
        param.pageSize = this.ipagination.pageSize;
        param.inDepartIds = this.queryParam.inDepartIds+"";
        delete param.queryExpDate; //范围参数不传递后台，传后台会报错
        delete param.queryInDate;
        return filterObj(param);
      },
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>