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
            <a-col :md="5" :sm="8">
              <a-form-item label="产品名称">
                <a-input placeholder="请输入产品名称" v-model="queryParam.productName"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="5" :sm="8">
              <a-form-item label="产品编号">
                <a-input placeholder="请输入产品编号" v-model="queryParam.number"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="5" :sm="8">
              <a-form-item label="注册证">
                <a-input placeholder="请输入注册证" v-model="queryParam.registration"></a-input>
              </a-form-item>
            </a-col>
             <a-col :md="5" :sm="8">
              <a-form-item label="是否试剂">
                <j-dict-select-tag-expand type="list" v-model="queryParam.productFlag" dictCode="yn" placeholder="请选择"/>
              </a-form-item>
            </a-col>
            <template v-if="toggleSearchStatus">
              <a-col :md="5" :sm="8">
                <a-form-item label="规格">
                  <a-input placeholder="请输入规格" v-model="queryParam.spec"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="5" :sm="8">
                <a-form-item label="收费代码">
                  <a-input placeholder="请输入收费代码" v-model="queryParam.chargeCode"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="5" :sm="8">
                <a-form-item label="中标号">
                  <a-input placeholder="请输入中标号" v-model="queryParam.bidingNumber"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="5" :sm="8">
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
                    :disabled="supplierSelecDisabled"
                  >
                    <a-select-option v-for="d in supplierData" :key="d.value">{{d.text}}</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :md="5" :sm="8">
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
            </template>
            <a-col :md="4" :sm="8">
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
        rowKey="productId"
        :columns="columns"
        :dataSource="dataSource"
        :loading="loading"
        :customRow="onClickRow"
        :pagination="false"
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
  import JDictSelectTagExpand from "@/components/dict/JDictSelectTagExpand"

  export default {
    name: "PdChooseStockListModel",
    mixins:[JeecgListMixin],
    components: {
      JDictSelectTagExpand
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"选择产品",
        width:1200,
        visible: false,
        supplierId:"", //供应商ID 用于入库时查询产品
        // model: {},
        confirmLoading: false,
        venderData: [],
        dataSource2: [],
        selectedRowKeys: [],
        selectedRows: [],
        venderValue: undefined,
        supplierSelecDisabled:false,
        supplierValue: undefined,
        notFoundContent:"未找到内容",
        supplierData: [],

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
          {
            title:'产品编号',
            align:"center",
            dataIndex: 'number'
          },
          {
            title:'产品名称',
            align:"center",
            dataIndex: 'productName'
          },
          {
            title:'产品类型',
            align:"center",
            dataIndex: 'productFlagName'
          },
          {
            title:'规格',
            align:"center",
            dataIndex: 'spec'
          },
          {
            title:'单位',
            align:"center",
            dataIndex: 'unitName'
          },
          {
            title:'库存数量',
            align:"center",
            dataIndex: 'stockNum'
          },
          {
            title:'库存上限',
            align:"center",
            dataIndex: 'limitUp'
          },
          {
            title:'库存下限',
            align:"center",
            dataIndex: 'limitDown'
          },
          {
            title:'再订货数',
            align:"center",
            dataIndex: 'autoNum'
          },
          {
            title:'供应商',
            align:"center",
            dataIndex: 'supplierName'
          },
          {
            title: '生产厂家',
            align:"center",
            dataIndex: 'venderName'
          },
          {
            title: '收费代码',
            align:"center",
            dataIndex: 'chargeCode'
          },
          {
            title: '中标号',
            align:"center",
            dataIndex: 'bidingNumber'
          },
          {
            title: '采购价',
            align:"center",
            dataIndex: 'purchasePrice',
          },
          {
            title: '注册证',
            align:"center",
            dataIndex: 'registration'
          }
        ],
        url: {
          list: "/pd/pdProductStockTotal/chooseStockTotalList",
          querySupplier:"/pd/pdSupplier/getSupplierList",
          queryVender:"/pd/pdVender/getVenderList",
        },
        popModal: {
          title: '选择产品',
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
        // this.queryParam.supplierId="";
        this.queryParam = {};
        this.loadData(1);
        // this.supplierData = [];
        this.$emit('close');
        this.visible = false;
      },
      show(params) {
        this.loadData(1);
        this.visible = true;
      },
      handleOk () {
        let data=this.dataSource2;
       let  retData = [];
        data.forEach((item,index)=>{
          if(item.autoNum>0){
            retData.push(item)
          }
        })
        this.$emit('ok',retData);
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
        this.loading = true;
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.dataSource = res.result;
           // this.ipagination.total = res.result.total;
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
        this.dataSource2 = [];
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
                  let recordId = record.productId;
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
      //生产厂家查询start
      venderHandleSearch(value) {
        fetch(value, data => (this.venderData = data),this.url.queryVender);
      },
      venderHandleChange(value) {
        this.venderValue = value;
        fetch(value, data => (this.venderData = data),this.url.queryVender);
      },
      //生产厂家查询end
      //供应商查询start
      supplierHandleSearch(value) {
        fetch(value, data => (this.supplierData = data),this.url.querySupplier);
      },
      supplierHandleChange(value) {
        this.supplierValue = value;
        fetch(value, data => (this.supplierData = data),this.url.querySupplier);
      },
      //供应商查询end
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