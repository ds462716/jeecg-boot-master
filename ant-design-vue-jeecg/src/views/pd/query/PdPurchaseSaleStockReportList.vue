<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :span="6">
            <a-form-item label="科室"> <!-- 多选 请加 mode="multiple" -->
              <a-select
                showSearch
                placeholder="请选择入库库房"
                :supplierId="departValue"
                :defaultActiveFirstOption="false"
                :showArrow="true"
                :filterOption="false"
                @search="departHandleSearch"
                @focus="departHandleSearch"
                :notFoundContent="notFoundContent"
                v-model="queryParam.departId"
              >
                <a-select-option v-for="d in departList" :key="d.id">{{d.departName}}</a-select-option>
              </a-select>
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
            <a-form-item label="筛选日期">
              <a-range-picker @change="dateChange" v-model="queryParam.queryDate" v-decorator="[ 'queryDate', validatorRules.queryDate]"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
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
            <a-form-item label="规格">
              <a-input placeholder="请输入规格" v-model="queryParam.spec"></a-input>
            </a-form-item>
          </a-col>
          <!--<a-col :md="6" :sm="8">-->
          <!--<a-form-item label="生产厂家">-->
          <!--<a-select-->
          <!--showSearch-->
          <!--:venderId="venderValue"-->
          <!--placeholder="请选择生产厂家"-->
          <!--:defaultActiveFirstOption="false"-->
          <!--:allowClear="true"-->
          <!--:showArrow="true"-->
          <!--:filterOption="false"-->
          <!--@search="venderHandleSearch"-->
          <!--@change="venderHandleChange"-->
          <!--@focus="venderHandleSearch"-->
          <!--:notFoundContent="notFoundContent"-->
          <!--v-model="queryParam.venderId"-->
          <!--&gt;-->
          <!--<a-select-option v-for="d in venderData" :key="d.value">{{d.text}}</a-select-option>-->
          <!--</a-select>-->
          <!--</a-form-item>-->
          <!--</a-col>-->
          <template v-if="toggleSearchStatus">

          </template>

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
      <a-button type="primary" icon="download" @click="handleExportXls('入库明细')">导出</a-button>
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
        @change="handleTableChange">
      </a-table>
    </div>
  </a-card>
</template>
<script>

  import { httpAction,getAction,downFile } from '@/api/manage'
  import { filterObj } from '@/utils/util';
  import { JeecgListMixin} from '@/mixins/JeecgListMixin'
  import { validateDuplicateValue } from '@/utils/util'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'

  export default {
    name: "PdPurchaseSaleStockReportList",
    mixins:[JeecgListMixin],
    components: {

    },
    data () {
      return {
        description: '入库明细查询',

        notFoundContent:"未找到内容",
        supplierValue: undefined,
        supplierData: [],

        venderValue: undefined,
        venderData: [],

        departValue: undefined,
        departList:[],

        validatorRules: {
          queryDate: {rules: [ {required: true, message: '请输入筛选日期!'}, ]},
        },
        // 表头
        columns: [
          {
             title: '序号',
             dataIndex: '',
             key:'rowIndex',
             width:60,
             align:"center",
             customRender:function (t,r,index) {
               return parseInt(index)+1;
             }
          },
          {
            title:'供应商',
            align:"center",
            dataIndex: 'supplierName'
          },
          {
            title:'科室',
            align:"center",
            dataIndex: 'departName',
          },
          {
            title:'产品编号',
            align:"center",
            dataIndex: 'productNumber'
          },
          {
            title:'产品名称',
            align:"center",
            dataIndex: 'productName'
          },
          {
            title:'规格',
            align:"center",
            dataIndex: 'spec'
          },

          {
            title:'期初库存',
            align:"center",
            dataIndex: 'firstStockNum'
          },
          {
            title:'本期入库',
            align:"center",
            dataIndex: 'supplierInStockNum'
          },
          {
            title:'调入数量',
            align:"center",
            dataIndex: 'inStockNum'
          },
          {
            title:'调出数量',
            align:"center",
            dataIndex: 'outStockNum'
          },
          {
            title:'本期用量',
            align:"center",
            dataIndex: 'dosageNum'
          },
          {
            title:'单价',
            align:"center",
            dataIndex: 'sellingPrice'
          },
          {
            title:'用量金额',
            align:"center",
            dataIndex: 'dosageMoney'
          },
          {
            title:'本期退货',
            align:"center",
            dataIndex: 'rejectedNum'
          },
          {
            title:'当前库存',
            align:"center",
            dataIndex: 'stockNum'
          },
        ],
        url: {
          list: "/pd/pdPurchaseSaleStockReport/list",
          querySupplier:"/pd/pdSupplier/getSupplierList",
          queryVender:"/pd/pdVender/getVenderList",
          // departList:"/pd/pdDepart/getSysDepartList",
          departList: "/pd/pdDepart/queryListTree",
          exportXlsUrl: "/pd/pdPurchaseSaleStockReport/exportXls",
        },
        tableScroll:{x :2000},
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      initDictConfig(){ //静态字典值加载
      },
      //重写 loadData 为空方法体
      loadData(arg) {
      },
      queryData(arg) {

        if(!this.queryParam.queryDateStart || !this.queryParam.queryDateEnd){
          this.$message.warning("请输入筛选日期！");
          return;
        }

        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        var params = this.getQueryParams();//查询条件
        this.loading = true;
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records;
            this.ipagination.total = res.result.total;
          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
          this.loading = false;
        })
      },
      searchQuery() {
        this.queryData(1);
      },
      searchReset() {
        this.queryParam = {}
        this.queryData(1);
      },
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
      dateChange: function (value, dateString) {
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
        param.departId = this.queryParam.departId;
        delete param.queryDate;//范围参数不传递后台，传后台会报错
        return filterObj(param);
      },
      /**重写导出方法**/
      handleExportXls(fileName){
        if(!fileName || typeof fileName != "string"){
          fileName = "导出文件"
        }
        fileName = fileName + "_" + new Date().toLocaleString();
        let param = this.getQueryParams();//查询条件
        if(this.selectedRowKeys && this.selectedRowKeys.length>0){
          param['selections'] = this.selectedRowKeys.join(",")
        }
        console.log("导出参数",param)
        downFile(this.url.exportXlsUrl,param).then((data)=>{
          if (!data) {
            this.$message.warning("文件下载失败")
            return
          }
          if (typeof window.navigator.msSaveBlob !== 'undefined') {
            window.navigator.msSaveBlob(new Blob([data],{type: 'application/vnd.ms-excel'}), fileName+'.xls')
          }else{
            let url = window.URL.createObjectURL(new Blob([data],{type: 'application/vnd.ms-excel'}))
            let link = document.createElement('a')
            link.style.display = 'none'
            link.href = url
            link.setAttribute('download', fileName+'.xls')
            document.body.appendChild(link)
            link.click()
            document.body.removeChild(link); //下载完成移除元素
            window.URL.revokeObjectURL(url); //释放掉blob对象
          }
        })
      },
    }
  }
</script>
<style scoped>
</style>