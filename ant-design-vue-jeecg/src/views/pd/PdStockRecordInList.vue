<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="入库单号">
              <a-input placeholder="请输入入库单号" v-model="queryParam.recordNo"></a-input>
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
            <a-form-item label="入库类型">
              <j-dict-select-tag v-model="queryParam.inType" dictCode="in_type"/>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="提交状态">
              <j-dict-select-tag v-model="queryParam.submitStatus" dictCode="submit_status"/>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="审核状态">
              <j-dict-select-tag v-model="queryParam.auditStatus" dictCode="audit_status"/>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="提交日期">
              <a-range-picker @change="dateChange" v-model="queryParam.queryDate"/>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
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
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <!--<a-button type="primary" icon="download" @click="handleExportXls('入库记录表')">导出</a-button>-->
    </div>

    <!-- table区域-begin -->
    <div>
      <!--<div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">-->
        <!--<i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项-->
        <!--<a style="margin-left: 24px" @click="onClearSelected">清空</a>-->
      <!--</div>-->

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
          <a @click="handleEdit(record)" v-bind:disabled="record.submitStatus=='2'">修改</a>
          <a-divider type="vertical"/>
          <a @click="handleDetail(record)">详情</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link" v-bind:disabled="record.submitStatus=='2'" >更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item v-show="record.submitStatus=='1' || record.submitStatus=='3'"> <!--待提交、已撤回-->
                <a-popconfirm title="确定删除吗?"  @confirm="() => handleDelete(record.id)"  >
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <pd-stock-record-in-modal ref="modalForm" @ok="modalFormOk"></pd-stock-record-in-modal>
  </a-card>
</template>

<script>

  // import JDictSelectTagExpand from "@/components/dict/JDictSelectTagExpand"
  import { filterObj } from '@/utils/util';
  import {getAction} from '@/api/manage'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import PdStockRecordInModal from './modules/PdStockRecordInModal'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'

  export default {
    name: "PdStockRecordInList",
    mixins:[JeecgListMixin],
    components: {
      PdStockRecordInModal,
      // JDictSelectTagExpand
    },
    data () {
      return {
        description: '入库记录表管理页面',
        supplierValue: undefined,
        notFoundContent:"未找到内容",
        supplierData: [],

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
            title:'入库单号',
            align:"center",
            dataIndex: 'recordNo'
          },
          {
            title:'入库库房',
            align:"center",
            dataIndex: 'inDepartName'
          },
          {
            title:'供应商',
            align:"center",
            dataIndex: 'supplierName'
          },
          {
            title:'提交时间',
            align:"center",
            dataIndex: 'submitDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'入库类型',
            align:"center",
            dataIndex: 'inType',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['inType'], text+"")
              }
            }
          },
          {
            title:'操作人',
            align:"center",
            dataIndex: 'submitByName'
          },
          {
            title:'提交状态',
            align:"center",
            dataIndex: 'submitStatus',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['submitStatus'], text+"")
              }
            }
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
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/pd/pdStockRecordIn/list",
          delete: "/pd/pdStockRecordIn/delete",
          // deleteBatch: "/pd/pdStockRecordIn/deleteBatch",
          exportXlsUrl: "/pd/pdStockRecordIn/exportXls",
          querySupplier:"/pd/pdSupplier/getSupplierList",
          // importExcelUrl: "pd/pdStockRecordIn/importExcel",
        },
        dictOptions:{
        },
      }
    },
    computed: {
      // importExcelUrl: function(){
      //   return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      // }
    },
    methods: {
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
        initDictOptions('in_type').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'inType', res.result)
          }
        })
      },
      dateChange: function (value, dateString) {
        this.queryParam.queryDateStart=dateString[0];
        this.queryParam.queryDateEnd=dateString[1];
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
            // this.venderData = data;
          }
        })
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
<style scoped>
  @import '~@assets/less/common.less'
</style>