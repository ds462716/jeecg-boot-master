<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :span="6">
            <a-form-item label="科室">
              <a-select
                mode="multiple"
                showSearch
                placeholder="请选择库房"
                :departId="departValue"
                :defaultActiveFirstOption="false"
                :allowClear="true"
                :showArrow="true"
                :filterOption="false"
                @search="departHandleSearch"
                @focus="departHandleSearch"
                :notFoundContent="notFoundContent"
                v-model="queryParam.departIds"
              >
                <a-select-option v-for="d in departList" :key="d.id">{{d.departName}}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button type="primary" icon="download" @click="handleExportXls('供应商用量明细')">导出</a-button>
    </div>
    <!-- table区域-begin
        :scroll="tableScroll"-->
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
        @change="handleTableChange">
        <template slot="ellipsisText" slot-scope="text">
          <j-ellipsis :value="text" :length="textMaxLength"></j-ellipsis>
        </template>
        <span slot="action" slot-scope="text, record">
          <a @click="detailBtn(record)">明细</a>
        </span>
      </a-table>
    </div>

    <rp-depart-stock-report-main ref="RpDepartStockReportMain"></rp-depart-stock-report-main>

  </a-card>
</template>
<script>

  import { httpAction,getAction,downFile } from '@/api/manage'
  import { filterObj } from '@/utils/util';
  import { JeecgListMixin} from '@/mixins/JeecgListMixin'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import JDictSelectTagExpand from "@/components/dict/JDictSelectTagExpand"
  import JEllipsis from '@/components/jeecg/JEllipsis'
  import RpDepartStockReportMain from "./RpDepartStockReportMain";

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
    timeout = setTimeout(fake, 0);
  }

  export default {
    name: "RpDepartStockReport",
    mixins:[JeecgListMixin],
    components: {
      RpDepartStockReportMain,
      JDictSelectTagExpand,JEllipsis
    },
    data () {
      return {
        description: '库存统计报表',
        textMaxLength:15,
        notFoundContent:"未找到内容",
        departValue: undefined,
        departList:[],
        detailParam:{}, //查询明细条件
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
            title:'科室id',
            align:"center",
            width:'250px',
            dataIndex: 'departId',
            colSpan: 0,
            customRender: (value, row, index) => {
              const obj = {
                attrs: {colSpan:0},
              };
              return obj;
            },
          },
          {
            title:'科室',
            align:"center",
            width:'250px',
            dataIndex: 'departName'
          },
          {
            title:'当前库存数量',
            align:"center",
            dataIndex: 'stockNum',
            width:'250px',
          },
          {
            title:'入库金额',
            align:"center",
            dataIndex: 'purchaseAmount',
            width:'250px',
          },
          {
            title:'出库金额',
            align:"center",
            dataIndex: 'sellingAmount',
            width:'250px',
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          },
        ],
        url: {
          list: "/pd/pdStatisticalReport/departStockReport",
          departList: "/pd/pdDepart/queryListTree",
          exportXlsUrl: "/pd/pdStatisticalReport/exportDepartStockReportXls",
        },
        dictOptions:{
          inType:[],
        },
        tableScroll:{x :1200},
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
      // 部门下拉框搜索
      departHandleSearch(value){
        getAction(this.url.departList,{departName:value}).then((res)=>{
          if (!res.success) {
            this.cmsFailed(res.message);
          }
          this.departList = res.result;
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
        delete param.queryInDate; //范围参数不传递后台，传后台会报错
        this.detailParam = param;
        return filterObj(param);
      },
      searchReset() {
        this.queryParam = {}
        this.loadData(1);
      },
      detailBtn(record){
        let param = {};
        param.departId = record.departId;
        this.$refs.RpDepartStockReportMain.show(param);
      },
      /**重写导出方法**/
      handleExportXls(fileName){
        if(!fileName || typeof fileName != "string"){
          fileName = "供应商用量使用统计报表"
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