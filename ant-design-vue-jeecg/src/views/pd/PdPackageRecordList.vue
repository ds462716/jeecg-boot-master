<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <!--<a-col :md="6" :sm="8">-->
            <!--<a-form-item label="流水码">-->
              <!--<a-input placeholder="请输入流水码" v-model="queryParam.packageBarCode"></a-input>-->
            <!--</a-form-item>-->
          <!--</a-col>-->
          <a-col :md="6" :sm="8">
            <a-form-item label="套包编号">
              <a-input placeholder="请输入套包编号" v-model="queryParam.packageCode"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="套包名称">
              <a-input placeholder="请输入套包名称" v-model="queryParam.packageName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="状态">
              <j-dict-select-tag-expand v-model="queryParam.status" dictCode="package_record_status"/>
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
          <a-col :md="4" :sm="8">
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
    
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">打包</a-button>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>拆包</a-menu-item>
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
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :expandedRowKeys= "expandedRowKeys"
        :rowSelection="{fixed:false,selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        :customRow="onClickRow"
        @change="handleTableChange"
        @expand="handleExpand">

        <span slot="action" slot-scope="text, record">
          <!--<a @click="handleDelete(record.id)">查看</a>-->
          <a-popconfirm title="确定拆包吗?" @confirm="() => handleDelete(record.id)" v-bind:disabled="record.status=='0'">
            <a>拆包</a>
          </a-popconfirm>
        </span>


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
    </div>

    <pd-package-record-modal ref="modalForm" @ok="modalFormOk"></pd-package-record-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import {httpAction, deleteAction, getAction} from '@/api/manage'
  import { filterObj } from '@/utils/util';
  import PdPackageRecordModal from "./modules/PdPackageRecordModal";
  import JDictSelectTagExpand from "@/components/dict/JDictSelectTagExpand"

  export default {
    name: "PdPackageRecordList",
    mixins:[JeecgListMixin],
    components: {
      PdPackageRecordModal,
      JDictSelectTagExpand
    },
    data () {
      return {
        description: 'pd_package_record管理页面',
        innerData:[],
        expandedRowKeys:[],
        subloading:false,
        confirmLoading: false,
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
          { title:'打包流水码', align:"center", dataIndex: 'packageBarCode' },
          { title:'套包编号', align:"center", dataIndex: 'packageCode' },
          { title:'套包名称', align:"center", dataIndex: 'packageName' },
          { title:'产品总数', align:"center", dataIndex: 'packageSum' },
          { title:'所属科室', align:"center", dataIndex: 'departName' },
          { title:'状态', align:"center", dataIndex: 'status',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['packageRecordStatus'], text+"")
              }
            }
          },
          { title:'打包人', align:"center", dataIndex: 'createBy' },
          { title:'打包时间', align:"center", dataIndex: 'createTime' },
          { title:'备注', align:"center", dataIndex: 'remarks' },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
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
          { title:'产品编号', align:"center", dataIndex: 'productNumber' },
          { title:'产品名称', align:"center", dataIndex: 'productName' },
          { title:'产品条码', align:"center",dataIndex: 'productBarCode' },
          { title:'规格', align:"center", dataIndex: 'spec' },
          { title:'批号', align:"center", dataIndex: 'batchNo' },
          // { title:'型号', align:"center", dataIndex: 'version' },
          { title:'单位', align:"center", dataIndex: 'unitName' },
          { title: '生产日期', align:"center", dataIndex: 'produceDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          { title:'有效期', align:"center", dataIndex: 'expDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          { title:'入库单价', align:"center", dataIndex: 'purchasePrice' },
          { title:'出库单价', align:"center", dataIndex: 'sellingPrice' },
          { title:'打包数量', align:"center", dataIndex: 'packageNum' },
          // { title:'出库金额', align:"center", dataIndex: 'outTotalPrice' },
          // { title:'库存数量', align:"center", dataIndex: 'stockNum' },
          { title: '出库货位', align:"center", dataIndex: 'outHuoweiName' },
          { title: '打包记录ID', align:"center", dataIndex: 'packageRecordId',
            colSpan: 0,
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
        ],
        url: {
          list: "/pd/pdPackageRecord/list",
          delete: "/pd/pdPackageRecord/delete",
          deleteBatch: "/pd/pdPackageRecord/deleteBatch",
          exportXlsUrl: "/pd/pdPackageRecord/exportXls",
          importExcelUrl: "pd/pdPackageRecord/importExcel",
          queryPackageRecordListByIds: "/pd/pdPackageRecord/queryPackageRecordListByIds",
          chooseDetailList:"/pd/pdPackageRecord/queryPdPackageRecordDetailByMainId",
        },
        dictOptions:{
          packageRecordStatus:[],
        },
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      initDictConfig(){
        initDictOptions('package_record_status').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'packageRecordStatus', res.result)
          }
        })
      },
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
      handleDelete(id){
        if(!this.url.delete){
          this.$message.error("请设置url.delete属性!")
          return
        }
        // var that = this;
        deleteAction(this.url.delete, {id: id}).then((res) => {
          if (res.success) {
            this.$message.success(res.message);
            this.loadData();
          } else {
            this.$message.warning(res.message);
          }
        });
      },
      batchDel() {
        if (this.selectedRowKeys.length <= 0) {
          this.$message.warning('请选择一条记录！');
          return;
        } else {
          var ids = "";
          for (var a = 0; a < this.selectedRowKeys.length; a++) {
            ids += this.selectedRowKeys[a] + ",";
          }
          var that = this;
          this.$confirm({
            title: "确认拆包",
            content: "是否拆包选中数据?",
            onOk: function () {
              that.loading = true;
              deleteAction(that.url.deleteBatch, {ids: ids}).then((res) => {
                if (res.success) {
                  that.$message.success(res.message);
                  that.loadData();
                  that.onClearSelected();
                } else {
                  that.$message.warning(res.message);
                }
              }).finally(() => {
                that.loading = false;
              });
            }
          });
        }
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
                if(parseInt(lie)-parseInt(cellIndex) > 1){
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
<style scoped>
  @import '~@assets/less/common.less';
</style>