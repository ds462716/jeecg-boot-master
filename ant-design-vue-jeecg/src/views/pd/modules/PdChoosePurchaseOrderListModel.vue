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
              <a-form-item label="申购单号">
                <a-input placeholder="请输入采购单号" v-model="queryParam.mergeOrderNo"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="申购日期">
                <a-range-picker @change="orderDateChange" v-model="queryParam.queryDate"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="供应商">
                <a-select
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
            <template v-if="toggleSearchStatus">
              <a-col :md="6" :sm="8">
                <a-form-item label="产品编号">
                  <a-input placeholder="请输入产品编号" v-model="queryParam.productNumber"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="8">
                <a-form-item label="产品名称">
                  <a-input placeholder="请输入产品名称" v-model="queryParam.productName"></a-input>
                </a-form-item>
              </a-col>
            </template>
            <a-col :md="6" :sm="8">
            <span style="float: right;overflow: hidden;" class="table-page-search-submitButtons">
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
        bordered
        rowKey="purchaseId"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :expandedRowKeys= "expandedRowKeys"
        :customRow="onClickRow"
        :rowSelection="{fixed:false,type:'radio',selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @expand="handleExpand"
        @change="handleTableChange">

        <a-table
          slot="expandedRowRender"
          slot-scope="text"
          :columns="innerColumns"
          :dataSource="innerData"
          size="middle"
          bordered
          rowKey="id"
          :pagination="false"
          :loading="subloading"
        >
        </a-table>
      </a-table>
    </a-spin>

    <template slot="footer">
      <a-button @click="handleCancel" style="margin-right: 15px;">取  消</a-button>
      <a-button @click="handleOk" type="primary" style="margin-right: 15px;">确定</a-button>
    </template>

  </j-modal>
</template>

<script>

  import { httpAction,getAction } from '@/api/manage'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import { filterObj } from '@/utils/util';

  export default {
    name: "PdChoosePurchaseOrderListModel",
    mixins:[JeecgListMixin],
    components: {
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"选择订单",
        width:1200,
        visible: false,
        innerData:[],
        expandedRowKeys:[],
        subloading:false,

        queryDate:[],

        supplierValue: undefined,
        notFoundContent:"未找到内容",
        supplierData: [],

        // model: {},
        confirmLoading: false,
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: 'purchaseId',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'合并申购单号',
            align:"center",
            dataIndex: 'orderNo'
          },
          {
            title:'审核日期',
            align:"center",
            dataIndex: 'orderDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'审核科室',
            align:"center",
            dataIndex: 'deptName'
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
            title:'审核人',
            align:"center",
            dataIndex: 'auditByName'
          }
        ],
        innerColumns:[
          {
            title:'合并申购编号',
            align:"center",
            width: 100,
            dataIndex: 'mergeOrderNo'
          },
          {
            title:'产品编号',
            align:"center",
            width: 100,
            dataIndex: 'number'
          },
          {
            title:'产品名称',
            align:"center",
            width: 180,
            dataIndex: 'productName'
          },
          {
            title:'规格',
            align:"center",
            width: 180,
            dataIndex: 'spec'
          },
          {
            title:'型号',
            align:"center",
            width: 180,
            dataIndex: 'version'
          },
          {
            title:'申购数量',
            align:"center",
            width: 75,
            dataIndex: 'orderNum'
          },
          {
            title:'到货数量',
            align:"center",
            width: 75,
            dataIndex: 'arrivalNum'
          },
          {
            title:'申购单价',
            align:"center",
            width: 75,
            dataIndex: 'purchasePrice'
          },
          {
            title:'价格',
            align:"center",
            width: 70,
            dataIndex: 'price'
          },
          {
            title:'单位',
            align:"center",
            width: 70,
            dataIndex: 'unitName'
          },
          {
            title:'供应商',
            align:"center",
            width: 150,
            dataIndex: 'supplierName'
          },
          {
            title:'厂家',
            align:"center",
            width: 150,
            dataIndex: 'venderName'
          },
          {
            title:'申购编号',
            align:"center",
            width: 100,
            dataIndex: 'orderNo'
          },
        ],
        url: {
          list: "/pd/pdPurchaseOrderMerge/choosePurchaseOrderList",
          chooseDetailList:"/pd/pdPurchaseOrderMerge/queryPdPurchaseMergeDetail",
          querySupplier:"/pd/pdSupplier/getSupplierList",
          detailList:"/pd/pdPurchaseOrderMerge/queryPdPurchaseMergeDetail",
        },
        tableScroll1:{x :13*47+50},
        tableScroll:{x :13*147+50},
        dictOptions:{
          // deptName:[],
          orderStatus:[],
          // submitStart:[],
        },
        popModal: {
          title: '选择采购订单',
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
      handleExpand(expanded, record){
        this.expandedRowKeys=[];
        this.innerData=[];
        if(expanded===true){
          this.subloading = true;
          this.expandedRowKeys.push(record.purchaseId);
          getAction(this.url.chooseDetailList, {mergeOrderNo: record.orderNo}).then((res) => {
            if (res.success) {
              this.subloading = false;
              this.innerData = res.result;
            }
          });
        }
      },
      close () {
        this.selectedRowKeys = [];
        this.selectionRows = [];
        this.$emit('close');
        this.visible = false;
      },
      show() {
        this.visible = true;
      },
      handleOk () {
        if(this.selectionRows.length > 0){
          let params = { mergeOrderNo: this.selectionRows[0].orderNo }
          getAction(this.url.detailList, params).then((res) => {
            if (res.success) {
              let data = res.result;
              this.$emit('ok', data);
              this.close();
            }
          });

        }else{
          this.$message.error("请选择一行数据!")
        }
      },
      handleCancel () {
        this.close();
      },
      popupCallback(row){

      },
      initDictConfig(){ //静态字典值加载
        initDictOptions('audit_status').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'auditStatus', res.result)
          }
        })
        initDictOptions('submit_status').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'submitStatus', res.result)
          }
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

      orderDateChange: function (value, dateString) {
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
                  let recordId = record.purchaseId;
                  let index = this.selectedRowKeys.indexOf(recordId);
                  this.selectedRowKeys = [];
                  this.selectionRows = [];
                  if(index>=0){
                    this.selectedRowKeys.splice(index, 1);
                    this.selectionRows.splice(index, 1);
                  }else{
                    this.selectedRowKeys.push(recordId);
                    this.selectionRows.push(record);
                  }
                }
              }
            }
          }
        }
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
      getAction(url,{name:value}).then((res)=>{
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