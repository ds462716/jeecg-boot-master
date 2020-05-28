<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="退货单号">
              <a-input placeholder="请输入退货单号" v-model="queryParam.rejectedNo"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="退货日期">
              <a-range-picker @change="rejectedDateChange" v-model="queryParam.queryDate"/>
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
          <template v-if="toggleSearchStatus">
            <a-col :md="6" :sm="8">
              <a-form-item label="退货类型">
                <j-dict-select-tag-expand v-model="queryParam.rejectedType" dictCode="rejected_type" placeholder="请选择退货类型"/>
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
    
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" v-show="isDisabledAuth('stock:form:addRejected')" icon="plus">新增</a-button>
      <a-button @click="handleUniqueAdd" type="primary" v-show="isDisabledAuth('stock:form:addRejected')" icon="plus">唯一码退货</a-button>
      <!--<a-button type="primary" icon="download" @click="handleExportXls('pd_rejected')">导出</a-button>-->
    </div>

    <!-- table区域-begin -->
    <div>

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
          <a @click="handleDetail(record)">详情</a>
        </span>

      </a-table>
    </div>
    <pdRejected-modal ref="modalForm" @ok="modalFormOk"></pdRejected-modal>
    <pd-Unique-Rejected-modal ref="modalUniqueForm" @ok="modalFormOk"></pd-Unique-Rejected-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import PdRejectedModal from './modules/PdRejectedModal'
  import PdUniqueRejectedModal from './modules/PdUniqueRejectedModal'
  import { filterObj } from '@/utils/util';
  import { httpAction,getAction } from '@/api/manage'
  import { disabledAuthFilter } from "@/utils/authFilter"
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import JDictSelectTagExpand from "@/components/dict/JDictSelectTagExpand"

  export default {
    name: "PdRejectedList",
    mixins:[JeecgListMixin],
    components: {
      PdRejectedModal,
      PdUniqueRejectedModal,
      JDictSelectTagExpand
    },
    data () {
      return {
        description: 'pd_rejected管理页面',

        //供应商下拉列表 start
        supplierValue: undefined,
        notFoundContent:"未找到内容",
        supplierData: [],
        //供应商下拉列表 end

        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'id',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'退货单号',
            align:"center",
            dataIndex: 'rejectedNo'
          },
          {
            title:'退货日期',
            align:"center",
            dataIndex: 'rejectedDate'
          },
          {
            title:'退货总数',
            align:"center",
            dataIndex: 'totalSum'
          },
          {
            title:'退货类型',
            align:"center",
            dataIndex: 'rejectedType',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['rejectedType'], text+"")
              }
            }
          },
          {
            title:'科室',
            align:"center",
            dataIndex: 'departName'
          },
          {
            title:'供应商',
            align:"center",
            dataIndex: 'supplierName'
          },
          {
            title:'创建人',
            align:"center",
            dataIndex: 'createBy'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/pd/pdRejected/list",
          // delete: "/pd/pdRejected/delete",
          // deleteBatch: "/pd/pdRejected/deleteBatch",
          exportXlsUrl: "/pd/pdRejected/exportXls",
          // importExcelUrl: "pd/pdRejected/importExcel",
          querySupplier:"/pd/pdSupplier/getSupplierList",
        },
        dictOptions:{
          rejectedType:[],
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
        initDictOptions('rejected_type').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'rejectedType', res.result)
          }
        });
      },
      /**
       * 校验权限
       * @param code
       * @returns {boolean|*}
       */
      isDisabledAuth(code){
        return !disabledAuthFilter(code);
      },
      rejectedDateChange (value, dateString) {
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
      //-----------------供应商查询start
      supplierHandleSearch(value) {
        this.getSupplierList(value);
      },
      supplierHandleChange(value) {
        this.totalSum = '0';
        this.eachAllTable((item) => {
          item.initialize()
        })
        this.supplierValue = value;
        this.getSupplierList(value);
      },
      getSupplierList(value){
        getAction(this.url.querySupplier,{name:value}).then((res)=>{
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
          this.supplierData = data;
        })
      },
      //----------------供应商查询end
      handleUniqueAdd(){
        this.$refs.modalUniqueForm.add();
        this.$refs.modalUniqueForm.title = "唯一码退货";
        this.$refs.modalUniqueForm.disableSubmit = false;
      },

      handleDetail:function(record){
        if(record.rejectedType=="0"){
          this.$refs.modalUniqueForm.edit(record);
          this.$refs.modalUniqueForm.title="详情";
          this.$refs.modalUniqueForm.disableSubmit = true;
          this.$refs.modalUniqueForm.totalSum = record.totalSum;
        }else{
          console.log(record)
          this.$refs.modalForm.edit(record);
          this.$refs.modalForm.title="详情";
          this.$refs.modalForm.disableSubmit = true;
          this.$refs.modalForm.totalSum = record.totalSum;
        }
      },
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>