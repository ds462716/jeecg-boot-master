<template>
  <j-modal
    :visible="visible"
    :width="popModal.width"
    :title="popModal.title"
    :lockScroll="popModal.lockScroll"
    :fullscreen="popModal.fullscreen"
    :switchFullscreen="popModal.switchFullscreen"
    @cancel="handleCancel"
  >
    <a-spin :spinning="confirmLoading">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :md="6" :sm="8">
              <a-form-item label="产品名称">
                <a-input placeholder="请输入产品名称" v-model="queryParam.productName"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="产品编号">
                <a-input placeholder="请输入产品编号" v-model="queryParam.number"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="批号">
                <a-input placeholder="请输入批号" v-model="queryParam.batchNo"></a-input>
              </a-form-item>
            </a-col>
            <template v-if="toggleSearchStatus">
              <a-col :md="6" :sm="8">
                <a-form-item label="规格">
                  <a-input placeholder="请输入规格" v-model="queryParam.spec"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="8">
                <a-form-item label="有效期">
                  <!--<a-input placeholder="请输入有效期" v-model="queryParam.expDate"></a-input>-->
                  <a-range-picker @change="expDateChange" v-model="queryParam.queryDate"/>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="8">
                <a-form-item label="注册证">
                  <a-input placeholder="请输入注册证" v-model="queryParam.registration"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="8">
                <a-form-item label="收费代码">
                  <a-input placeholder="请输入收费代码" v-model="queryParam.chargeCode"></a-input>
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
                    :disabled="supplierSelecDisabled"
                    v-model="queryParam.supplierId"
                  >
                    <a-select-option v-for="d in supplierData" :key="d.value">{{d.text}}</a-select-option>
                  </a-select>
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
                <a-form-item label="产品类型">
                  <a-checkbox-group :disabled="productFlagDisabled" v-model="productFlagCheckValues" :options="productFlagOptions" @change="productFlagChange" />
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="8">
                <a-form-item label="使用状态">
                  <a-checkbox-group :disabled="nestatStatusDisabled" v-model="nestatStatusCheckValues" :options="nestatStatusOptions" @change="nestatStatusChange" />
                </a-form-item>
              </a-col>
            </template>
            <a-col :md="6" :sm="8">
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

      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>
      <!-- 查询区域-END -->
      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :customRow="onClickRow"
        :rowSelection="{fixed:false,selectedRowKeys: selectedRowKeys, onSelectAll:onSelectAll,onSelect:onSelect,onChange: onSelectChange}"
        @change="handleTableChange">
      </a-table>
    </a-spin>

    <template slot="footer">
      <a-button @click="handleCancel" style="margin-right: 15px;">取  消</a-button>
      <a-button @click="handleOk" type="primary" style="margin-right: 15px;">确定</a-button>
    </template>

  </j-modal>
</template>

<script>

  import Vue from 'vue'
  import { httpAction,getAction } from '@/api/manage'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { FormTypes } from '@/utils/JEditableTableUtil'
  import { filterObj } from '@/utils/util';


  export default {
    name: "PdChooseProductStockListModel",
    mixins:[JeecgListMixin],
    components: {
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"选择产品",
        width:1600,
        visible: false,
        // model: {},
        confirmLoading: false,

        supplierSelecDisabled:false,
        // supplierSelecDisabled:false,
        supplierValue: undefined,
        notFoundContent:"未找到内容",
        supplierData: [],

        venderData: [],
        venderValue: undefined,
        applyNo:"",
        allocationNo:"",
        supplierId:"", //供应商ID

        productFlagDisabled:false,
        productFlag:"", //0-器械；1-试剂
        productFlagOptions:[ { label: '器械', value: '0' },{ label: '试剂', value: '1' }],
        productFlagCheckValues:[],

        nestatStatusDisabled:false,
        nestatStatus:"", //0:使用中   1:未使用 2:已用完
        nestatStatusOptions:[ { label: '未使用', value: '1' },{ label: '使用中', value: '0' }],
        nestatStatusCheckValues:[],

        productIdList: [],

        dataSource2: [],
        selectedRowKeys: [],
        selectedRows: [],

        // 表头
        columns: [
          {
            title: '#',
            dataIndex: 'productId',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          { title:'产品编号', align:"center", dataIndex: 'number' },
          { title:'产品名称', align:"center", dataIndex: 'productName' },
          { title:'规格', align:"center", dataIndex: 'spec' },
          { title:'型号', align:"center", dataIndex: 'version' },
          { title:'批号', align:"center", dataIndex: 'batchNo' },
          { title:'单位', align:"center", dataIndex: 'unitName' },
          { title:'有效期', align:"center", dataIndex: 'expDate' },
          { title:'供应商', align:"center", dataIndex: 'supplierName' },
          { title: '生产厂家', align:"center", dataIndex: 'venderName' },
          { title: '库存数量', align:"center", dataIndex: 'stockNum' },
          { title: '规格数量', align:"center", dataIndex: 'specNum' },
          { title: '进价', align:"center",dataIndex: 'purchasePrice' },
          { title: '出价', align:"center",dataIndex: 'sellingPrice' },
          { title: '货位', align:"center", dataIndex: 'huoweiName' },
          { title: '收费代码', align:"center", dataIndex: 'chargeCode' },
          { title: '注册证', align:"center", dataIndex: 'registration' },
          {
            title: '生产日期',
            align:"center",
            dataIndex: 'produceDate',
            colSpan: 0,
            customRender: (value, row, index) => {
              const obj = {
                attrs: {colSpan:0},
              };
              return obj;
            },
          },
          {
            title: '供应商ID',
            align:"center",
            dataIndex: 'supplierId',
            colSpan: 0,
            customRender: (value, row, index) => {
              const obj = {
                attrs: {colSpan:0},
              };
              return obj;
            },
          },
          {
            title: '货位编号',
            align:"center",
            dataIndex: 'huoweiCode',
            colSpan: 0,
            customRender: (value, row, index) => {
              const obj = {
                attrs: {colSpan:0},
              };
              return obj;
            },
          },
          {
            title: '产品条码',
            align:"center",
            dataIndex: 'productBarCode',
            colSpan: 0,
            customRender: (value, row, index) => {
              const obj = {
                attrs: {colSpan:0},
              };
              return obj;
            },
          },
          {
            title: '库存明细id',
            align:"center",
            dataIndex: 'id',
            colSpan: 0,
            customRender: (value, row, index) => {
              const obj = {
                attrs: {colSpan:0},
              };
              return obj;
            },
          },
          {
            title:'规格数量',
            align:"center",
            dataIndex: 'specQuantity',
            colSpan: 0,
            customRender: (value, row, index) => {
              const obj = {
                attrs: {colSpan:0},
              };
              return obj;
            },
          },
          {
            title: '规格单位ID',
            align:"center",
            dataIndex: 'specUnitId',
            colSpan: 0,
            customRender: (value, row, index) => {
              const obj = {
                attrs: {colSpan:0},
              };
              return obj;
            },
          },
        ],
        url: {
          list: "/pd/pdProductStockTotal/selectProductStockList",
          querySupplier:"/pd/pdSupplier/getSupplierList",
          queryVender:"/pd/pdVender/getVenderList",
        },
        popModal: {
          title: '选择库存产品',
          visible: false,
          width: '100%',
          // width: '1200',
          style: { top: '20px' },
          switchFullscreen: true,  //缩放按钮
          lockScroll: false,
          fullscreen: true,
        },
      }
    },
    methods: {
      close () {
        this.selectedRowKeys = [];
        this.selectionRows = [];
        this.dataSource2 = [];
        this.dataSource = [];
        this.queryParam = {};
        this.productFlag = "";
        this.productFlagCheckValues = [];
        this.productIdList = [];
        this.nestatStatus = "";
        this.nestatStatusCheckValues = [];
        this.applyNo = "";
        this.allocationNo = "";
        this.supplierId = "";
        // this.loadData(1);
        this.$emit('close');
        this.visible = false;
      },
      show(params) {
        if(params && params.applyNo){
          this.applyNo = params.applyNo;
          this.allocationNo = "";
        }
        if(params && params.allocationNo){
          this.applyNo = "";
          this.allocationNo = params.allocationNo;
        }
        if(params && params.supplierId){
          this.supplierId = params.supplierId;
          this.$nextTick(() => {
            // 初始化供应商
            this.supplierHandleSearch();
            this.queryParam.supplierId = this.supplierId; //默认选择父页面传来的供应商
            this.supplierSelecDisabled = true;
          })
        }
        if(params && params.productFlag){
          this.productFlag = params.productFlag;
          this.productFlagCheckValues.push(params.productFlag);
          this.productFlagDisabled = true;
        }
        if(params && params.nestatStatus){
          this.nestatStatus = params.nestatStatus;
          this.nestatStatusCheckValues.push(params.nestatStatus);
          this.nestatStatusDisabled = true;
        }
        if(params && params.productIdList){
          this.productIdList = params.productIdList;
        }

        this.loadData(1);
        this.visible = true;
      },
      handleOk () {
        let rows = this.dataSource2;
        this.$emit('ok', rows);
        this.close();
      },
      handleCancel () {
        this.close();
      },
      popupCallback(row){

      },
      searchReset() {
        this.queryParam = {}
        if(!this.productFlagDisabled){
          this.productFlag = "";
          this.productFlagCheckValues = [];
        }
        if(!this.nestatStatusDisabled){
          this.nestatStatus = "";
          this.nestatStatusCheckValues = [];
        }
        this.loadData(1);
      },
      loadData(arg) {
        if(!this.url.list){
          this.$message.error("请设置url.list属性!")
          return
        }
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        var params = this.getQueryParams();//查询条件
        if(this.applyNo){
          params.applyNo = this.applyNo;
        }
        if(this.allocationNo){
          params.allocationNo = this.allocationNo;
        }
        if(this.supplierId){
          params.supplierId = this.supplierId;
        }
        if(this.productFlag){
          params.productFlag = this.productFlag;
        }
        if(this.nestatStatus){
          params.nestatStatus = this.nestatStatus;
        }
        if(this.productIdList){
          params.productIds = this.productIdList.join(",");
        }
        this.loading = true;
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records;
          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
          this.loading = false;
        })
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
                  let recordId = record.id;
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
      productFlagChange(checkList){
        this.productFlag = "";
        if(this.productFlagCheckValues.length == 1){
          this.productFlag = this.productFlagCheckValues[0];
        }
      },
      nestatStatusChange(checkList){
        this.nestatStatus = "";
        if(this.nestatStatusCheckValues.length == 1){
          this.nestatStatus = this.nestatStatusCheckValues[0];
        }
      },
      expDateChange: function (value, dateString) {
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
        delete param.queryDate; //范围参数不传递后台，传后台会报错
        return filterObj(param);
      },
    }
  }
</script>