<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
       <!-- /* 供应商，生产厂家*/-->

        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="科室">
               <a-select
                 mode="multiple"
                showSearch
                :departId="departValue"
                :defaultActiveFirstOption="false"
                :allowClear="true"
                :showArrow="true"
                :filterOption="false"
                @search="departHandleSearch"
                @focus="departHandleSearch"
                :notFoundContent="notFoundContent"
                v-model="queryParam.departIds"
                placeholder="请选择科室"
              >
                <a-select-option v-for="d in departData" :key="d.id">{{d.departName}}</a-select-option>
              </a-select>

            </a-form-item>
          </a-col>
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
          <template v-if="toggleSearchStatus">
            <a-col :md="6" :sm="8">
              <a-form-item label="使用状态">
                <j-dict-select-tag v-model="queryParam.nestatStatus" dictCode="nestat_status"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="规格">
                <a-input placeholder="请输入规格" v-model="queryParam.spec"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="注册证">
                <a-input placeholder="请输入注册证" v-model="queryParam.registration"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="批号">
                <a-input placeholder="请输入批号" v-model="queryParam.batchNo"></a-input>
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
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button type="primary" icon="download" @click="handleExportXls('库存明细')">导出</a-button>
    </div>
    <!-- table区域-begin -->
    <div>
      <a-table
        CLASS="changeColor"
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :scroll="tableScroll"
        @change="handleTableChange"
        >
      </a-table>
    </div>
  </a-card>
</template>
<script>

  import { JeecgListMixin ,handleEdit} from '@/mixins/JeecgListMixin'
  import { getAction } from '@/api/manage'
  import { filterObj } from '@/utils/util'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'


  export default {
    name: "PdProductStockQueryList",
    mixins:[JeecgListMixin],
    components: {
    },
    data () {
      return {
        description: '库存明细查询',
        departData: [],
        departValue: undefined,
        notFoundContent:"未找到内容",
        supplierValue: undefined,
        supplierData: [],

        venderValue: undefined,
        venderData: [],
        // 表头
        columns: [
          /*{
            title: '序号',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },*/
          {
            title:'所属科室',
            align:"center",
            dataIndex: 'deptName'
          },
          {
            title:'货位',
            align:"center",
            dataIndex: 'huoweiCode'
          },
          {
            title:'产品名称',
            align:"center",
            dataIndex: 'productName'
          },
          {
            title:'产品编号',
            align:"center",
            dataIndex: 'number'
          },
          {
            title:'产品类型',
            align:"center",
            dataIndex: 'productFlagName'
          },
          {
            title:'产品条码',
            align:"center",
            dataIndex: 'productBarCode'
          },
          {
            title:'规格',
            align:"center",
            dataIndex: 'spec'
          },
          {
            title:'批号',
            align:"center",
            dataIndex: 'batchNo'
          },
          {
            title:'有效期',
            align:"center",
            dataIndex: 'expDate'
          },
          {
            title:'生产日期',
            align:"center",
            dataIndex: 'produceDate'
          },
          {
            title:'库存数量',
            align:"center",
            dataIndex: 'stockNum'
          },
          {
            title:'规格单位',
            align:"center",
            dataIndex: 'specUnitName'
          },
          {
            title:'库存规格数量',
            align:"center",
            dataIndex: 'specNum'
          },
          {
            title:'使用状态',
            align:"center",
            dataIndex: 'nestatStatus',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['nestatStatus'], text+"")
              }
            }
          },
          {
            title:'进价',
            align:"center",
            dataIndex: 'inPurchasePrice'
          },
          {
            title:'出价',
            align:"center",
            dataIndex: 'sellingPrice'
          },
          {
            title:'单位',
            align:"center",
            dataIndex: 'unitName'
          },
          {
            title:'注册证号',
            align:"center",
            dataIndex: 'registration'
          },
          {
            title:'JDE编号',
            align:"center",
            dataIndex: 'jdeCode'
          },
          {
            title:'生产厂家',
            align:"center",
            dataIndex: 'venderName'
          },
          {
            title:'供应商',
            align:"center",
            dataIndex: 'supplierName'
          }
        ],
        url: {
          list: "/pd/pdProductStockTotal/queryList",
          exportXlsUrl: "/pd/pdProductStockTotal/exportXls",
          queryDepart: "/pd/pdDepart/queryListTree",
          querySupplier:"/pd/pdSupplier/getSupplierList",
          queryVender:"/pd/pdVender/getVenderList",
        },
        dictOptions:{
            nestatStatus:[],
        },
        tableScroll:{x :13*147+50},
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
     //科室查询start
      departHandleSearch(value) {
        getAction(this.url.queryDepart,{departName:value}).then((res)=>{
          if (!res.success) {
            this.cmsFailed(res.message);
          }
          this.departData = res.result;
        })
      },
      //科室查询end

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
        param.departIds = this.queryParam.departIds+"";
        return filterObj(param);
      },

      initDictConfig(){ //静态字典值加载
        initDictOptions('nestat_status').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'nestatStatus', res.result)
          }
        })
      },
    }
  }
</script>
<style scoped>
</style>