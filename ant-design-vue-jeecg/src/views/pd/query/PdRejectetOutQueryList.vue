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
            <a-form-item label="产品名称">
              <a-input placeholder="请选输入品名称" v-model="queryParam.productName"></a-input>
            </a-form-item>
          </a-col>

          <template v-if="toggleSearchStatus">
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
          <a-col :md="6" :sm="8">
            <a-form-item label="型号">
              <a-input placeholder="请输入型号" v-model="queryParam.version"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="有效期">
              <a-range-picker @change="expDateChange" v-model="queryParam.queryExpDate"/>
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
      <a-button type="primary" icon="download" @click="handleExportXls('院外退货明细')">导出</a-button>
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
        :scroll="tableScroll"
        :rowSelection="{fixed:false,selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        
        @change="handleTableChange">
      </a-table>

    </div>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { filterObj } from '@/utils/util';
  import { httpAction,getAction,downFile } from '@/api/manage'
  import { disabledAuthFilter } from "@/utils/authFilter"

  export default {
    name: "PdRejectedOutQueryList",
    mixins:[JeecgListMixin],
    components: {
    },
    data () {
      return {
        description: '退货明细查询',

        notFoundContent:"未找到内容",

        supplierValue: undefined,
        supplierData: [],

        venderValue: undefined,
        venderData: [],

        tableScroll:{x :3500},

        // 表头
        columns: [
          // {
          //   title: '#',
          //   dataIndex: '',
          //   key:'id',
          //   width:60,
          //   align:"center",
          //   customRender:function (t,r,index) {
          //     return parseInt(index)+1;
          //   }
          // },
          {
            title:'退货单号',
            align:"center",
            width:'100px',
            dataIndex: 'rejectedNo'
          },
          {
            title:'退货日期',
            align:"center",
            width:'90px',
            dataIndex: 'rejectedDate'
          },
          {
            title:'科室',
            align:"center",
            width:'80px',
            dataIndex: 'departName'
          },
          {
            title:'产品编号',
            align:"center",
            width:'150px',
            dataIndex: 'productNumber'
          },
          {
            title:'产品名称',
            align:"center",
            width:'250px',
            dataIndex: 'productName'
          },
          {
            title:'产品条码',
            align:"center",
            width:'150px',
            dataIndex: 'productBarCode'
          },
          {
            title:'退货数量',
            align:"center",
            width:'90px',
            dataIndex: 'rejectedCount'
          },
          {
            title:'单位',
            align:"center",
            width:'60px',
            dataIndex: 'unitName'
          },
          {
            title:'规格',
            align:"center",
            width:'180px',
            dataIndex: 'spec'
          },
          {
            title:'型号',
            align:"center",
            width:'180px',
            dataIndex: 'version'
          },
          {
            title:'批号',
            align:"center",
            width:'150px',
            dataIndex: 'batchNo'
          },
          {
            title:'生产日期',
            align:"center",
            dataIndex: 'produceDate',
            width:'90px',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'有效期',
            align:"center",
            dataIndex: 'expDate',
            width:'90px',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'生产厂家',
            align:"center",
            width:'250px',
            dataIndex: 'venderName'
          },
          {
            title:'供应商',
            align:"center",
            width:'250px',
            dataIndex: 'supplierName'
          },
          {
            title:'注册证号',
            align:"center",
            width:'250px',
            dataIndex: 'registration'
          },
          {
            title:'操作人',
            align:"center",
            width:'100px',
            dataIndex: 'createBy'
          },
        ],
        url: {
          list: "/pd/pdRejected/queryPdRejectedOutList",
          exportXlsUrl: "/pd/pdRejected/exportXls",
          querySupplier:"/pd/pdSupplier/getSupplierList",
          queryVender:"/pd/pdVender/getVenderList",
        },
        dictOptions:{},
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      initDictConfig(){
      },
      rejectedDateChange (value, dateString) {
        this.queryParam.queryDateStart=dateString[0];
        this.queryParam.queryDateEnd=dateString[1];
      },
      expDateChange: function (value, dateString) {
        this.queryParam.queryExpDateStart=dateString[0];
        this.queryParam.queryExpDateEnd=dateString[1];
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
        delete param.queryExpDate; //范围参数不传递后台，传后台会报错
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
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>