<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :md="6" :sm="8">
              <a-form-item label="产品编号">
                <a-input placeholder="请输入产品编号" v-model="queryParam.number"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="产品名称">
                <a-input placeholder="请输入产品名称" v-model="queryParam.productName"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="批号">
                <a-input placeholder="请输入批号" v-model="queryParam.batchNo"></a-input>
              </a-form-item>
            </a-col>
            <template v-if="toggleSearchStatus">
              <a-col :md="6" :sm="8">
                <a-form-item label="有效期">
                  <!--<a-input placeholder="请输入有效期" v-model="queryParam.expDate"></a-input>-->
                  <a-range-picker @change="expDateChange" v-model="queryParam.queryDate"/>
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
        :rowSelection="{fixed:true,selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">
      </a-table>
    </a-spin>
  </a-modal>
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
        supplierId:"", //供应商ID 用于入库时查询产品
        // model: {},
        confirmLoading: false,

        // supplierSelecDisabled:false,
        supplierValue: undefined,
        notFoundContent:"未找到内容",
        supplierData: [],

        // 表头
        columns: [
          {
            title: '#',
            dataIndex: 'productId',
            key:'rowIndex',
            width:50,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          { title:'产品编号', align:"center", width:180, dataIndex: 'number' },
          { title:'产品名称', align:"center", width:200,dataIndex: 'productName' },
          { title:'规格', align:"center", width:150,dataIndex: 'spec' },
          // { title:'型号', align:"center", width:150,dataIndex: 'version' },
          { title:'批号', align:"center", width:100,dataIndex: 'batchNo' },
          { title:'单位', align:"center", width:50,dataIndex: 'unitName' },
          { title:'有效期', align:"center", width:100,dataIndex: 'expDate' },
          { title:'供应商', align:"center", width:150,dataIndex: 'supplierName' },
          { title: '生产厂家', align:"center", width:150,dataIndex: 'venderName' },
          { title: '库存数量', align:"center", width:80,dataIndex: 'stockNum' },
          { title: '进价', align:"center",width:80, dataIndex: 'purchasePrice' },
          { title: '出价', align:"center",width:80, dataIndex: 'sellingPrice' },
          { title: '货位', align:"center", width:80,dataIndex: 'huoweiName' },
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
        ],
        url: {
          list: "/pd/pdProductStockTotal/chooseProductStockList",
          querySupplier:"/pd/pdSupplier/getSupplierList",
        }
      }
    },
    methods: {
      close () {
        this.selectedRowKeys = [];
        this.selectionRows = [];
        // this.queryParam.supplierId="";
        this.queryParam = {};
        this.loadData(1);
        // this.supplierData = [];
        this.$emit('close');
        this.visible = false;
      },
      show(params) {
        // if(params && params.supplierId){
          // this.supplierId = params.supplierId;
          // this.orderNo = params.orderNo;
          // this.$nextTick(() => {
            // this.$refs.supplierSelect.disabled="disabled";
            // this.supplierHandleSearch(); //初始化供应商
            // this.queryParam.supplierId = this.supplierId; //默认选择父页面传来的供应商
            // this.supplierSelecDisabled = true;
          // })
        // }
        this.loadData(1);
        this.visible = true;
      },
      handleOk () {
        let rows = this.selectionRows;
        this.$emit('ok', rows);
        this.close();
      },
      handleCancel () {
        this.close();
      },
      popupCallback(row){

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
        // if(this.supplierId){
        //   params.supplierId = this.supplierId;
        // }
        // if(this.orderNo){
        //   params.orderNo = this.orderNo;
        // }
        this.loading = true;
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records.records;
            this.ipagination.total = res.result.records.total;
          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
          this.loading = false;
        })
      },

      //供应商查询start
      supplierHandleSearch(value) {
        fetch(value, data => (this.supplierData = data),this.url.querySupplier);
      },
      supplierHandleChange(value) {
        this.supplierValue = value;
        fetch(value, data => (this.supplierData = data),this.url.querySupplier);
      },
      //供应商查询end
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

  let timeout;
  let currentValue;

  function fetch(value, callback,url) {
    if (timeout) {
      clearTimeout(timeout);
      timeout = null;
    }
    currentValue = value;

    function fake() {
      getAction(url, {name: value}).then((res) => {
        if (!res.success) {
          this.cmsFailed(res.message);
        }
        if (currentValue === value) {
          const result = res.result;
          const data = [];
          result.forEach(r => {
            data.push({
              value: r.id,
              text: r.name,
            });
          });
          callback(data);
        }
      })
    }
    timeout = setTimeout(fake, 0); //这边不延迟
  }
</script>