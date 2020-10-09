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
              <a-form-item label="套包编号">
                <a-input placeholder="请输入套包编号" v-model="queryParam.packageCode"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="5" :sm="8">
              <a-form-item label="套包名称">
                <a-input placeholder="请输入套包名称" v-model="queryParam.packageName"></a-input>
              </a-form-item>
            </a-col>
            <!--<template v-if="toggleSearchStatus">-->
              <a-col :md="5" :sm="8">
                <a-form-item label="产品编号">
                  <a-input placeholder="请输入产品编号" v-model="queryParam.productNumber"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="5" :sm="8">
                <a-form-item label="产品名称">
                  <a-input placeholder="请输入产品名称" v-model="queryParam.productName"></a-input>
                </a-form-item>
              </a-col>
            <!--</template>-->
            <a-col :md="4" :sm="8">
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
      <a-table
        ref="table"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :expandedRowKeys= "expandedRowKeys"
        :customRow="onClickRow"
        :rowSelection="{fixed:false,selectedRowKeys: selectedRowKeys, onSelectAll:onSelectAll,onSelect:onSelect,onChange: onSelectChange}"
        @expand="handleExpand"
        @change="handleTableChange">

        <a-table
          slot="expandedRowRender"
          slot-scope="text"
          size="middle"
          bordered
          rowKey="id"
          :columns="innerColumns"
          :dataSource="innerData"
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
    name: "PdChoosePackageRecordListModel",
    mixins:[JeecgListMixin],
    components: {
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"选择套包",
        width:1200,
        visible: false,
        innerData:[],
        expandedRowKeys:[],
        subloading:false,
        confirmLoading: false,

        dataSource2: [],
        selectedRowKeys: [],
        selectedRows: [],
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: 'id',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          { title:'套包编号', align:"center", dataIndex: 'packageCode' },
          { title:'套包条码', align:"center", dataIndex: 'packageBarCode' },
          { title:'套包名称', align:"center", dataIndex: 'packageName' },
          { title:'产品总数', align:"center", dataIndex: 'packageSum' },
          { title:'打包人', align:"center", dataIndex: 'createBy' },
          { title:'打包时间', align:"center", dataIndex: 'createTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          { title:'备注', align:"center", dataIndex: 'remarks' }
        ],
        innerColumns:[
          {
            title: '#',
            dataIndex: 'id',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          { title:'产品名称1', align:"center", dataIndex: 'productName' },
          { title:'产品编号', align:"center", dataIndex: 'productNumber' },
          { title:'产品条码', align:"center",dataIndex: 'productBarCode' },
          { title:'规格', align:"center", dataIndex: 'spec' },
          { title:'批号', align:"center", dataIndex: 'batchNo' },
          // { title:'型号', align:"center", dataIndex: 'version' },
          { title:'单位', align:"center", dataIndex: 'unitName' },
          { title:'有效期', align:"center", dataIndex: 'expDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          { title:'入库单价', align:"center", dataIndex: 'purchasePrice' },
          { title:'出库单价', align:"center", dataIndex: 'sellingPrice' },
          { title:'产品数量', align:"center", dataIndex: 'productNum' },
          { title:'出库金额', align:"center", dataIndex: 'outTotalPrice' },
          { title:'库存数量', align:"center", dataIndex: 'stockNum' },
          { title: '出库货位', align:"center", dataIndex: 'outHuoweiName' },
          { title: '生产日期', align:"center", dataIndex: 'produceDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          { title: '打包记录ID', align:"center", dataIndex: 'packageRecordId',
            colSpan: 0,
            customRender: (value, row, index) => {
              const obj = {
                attrs: {colSpan:0},
              };
              return obj;
            },
          },
          { title: '打包记录明细ID', align:"center", dataIndex: 'packageRecordDetailId', colSpan: 0,
            customRender: (value, row, index) => {
              const obj = {
                attrs: {colSpan:0},
              };
              return obj;
            },
          },
          { title: '库存明细ID', align:"center", dataIndex: 'productStockId', colSpan: 0,
            customRender: (value, row, index) => {
              const obj = {
                attrs: {colSpan:0},
              };
              return obj;
            },
          },
          { title: '产品ID', align:"center", dataIndex: 'productId', colSpan: 0,
            customRender: (value, row, index) => {
              const obj = {
                attrs: {colSpan:0},
              };
              return obj;
            },
          },
          { title: '出库货位编号', align:"center", dataIndex: 'outHuoweiCode', colSpan: 0,
            customRender: (value, row, index) => {
              const obj = {
                attrs: {colSpan:0},
              };
              return obj;
            },
          },
          { title: '供应商id', align:"center", dataIndex: 'supplierId', colSpan: 0,
            customRender: (value, row, index) => {
              const obj = {
                attrs: {colSpan:0},
              };
              return obj;
            },
          },
          { title: '规格单位ID', align:"center", dataIndex: 'specUnitId', colSpan: 0,
            customRender: (value, row, index) => {
              const obj = {
                attrs: {colSpan:0},
              };
              return obj;
            },
          },
          { title: '规格数量', align:"center", dataIndex: 'specQuantity', colSpan: 0,
            customRender: (value, row, index) => {
              const obj = {
                attrs: {colSpan:0},
              };
              return obj;
            },
          },
          { title: '注册证号', align:"center", dataIndex: 'registration', colSpan: 0,
            customRender: (value, row, index) => {
              const obj = {
                attrs: {colSpan:0},
              };
              return obj;
            },
          },
          { title: '配送商id', align:"center", dataIndex: 'distributorId', colSpan: 0,
            customRender: (value, row, index) => {
              const obj = {
                attrs: {colSpan:0},
              };
              return obj;
            },
          },
        ],
        url: {
          list: "/pd/pdPackageRecord/pdChoosePackageRecordList",
          queryPackageRecordListByIds: "/pd/pdPackageRecord/queryPackageRecordListByIds",
          chooseDetailList:"/pd/pdPackageRecord/queryPdPackageRecordDetailByMainId",
        },
        dictOptions:{
        },
        popModal: {
          title: '选择产品',
          visible: false,
          width: '100%',
          // width: '1200',
          style: { top: '20px' },
          switchFullscreen: false,  //缩放按钮
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
          this.expandedRowKeys.push(record.id);
          getAction(this.url.chooseDetailList, {id: record.id}).then((res) => {
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
        this.dataSource2 = [];
        this.$emit('close');
        this.visible = false;
      },
      show() {
        this.visible = true;
      },
      handleOk () {
        if(this.selectionRows.length > 0){
          let ids = ''
          for (let item of this.selectionRows){
            ids += item.id + ','
          }
          getAction(this.url.queryPackageRecordListByIds, {ids:ids}).then((res) => {
            if (res.success) {
              let data = {};
              data.pdPackageRecordList = res.result;
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
                  let recordId = record.id;
                  let index = this.selectedRowKeys.indexOf(recordId);
                  // this.selectedRowKeys = [];
                  // this.selectionRows = [];
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
</script>