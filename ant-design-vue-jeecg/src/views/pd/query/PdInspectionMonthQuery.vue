<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
        <a-col :md="6" :sm="8">
          <a-form-item label="年月">
            <a-month-picker placeholder="选择年月" @change="monthChange" v-model="queryParam.ym"/>
          </a-form-item>
        </a-col>
          <a-col :md="6" :sm="8">
          <a-form-item label="科室">
            <!--<a-input placeholder="请选择科室" v-model="queryParam.deptName"></a-input>-->
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

          <template v-if="toggleSearchStatus">
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
      <a-button type="primary" icon="download" @click="handleExportXls('耗材消耗报表')">导出</a-button>
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
        <span slot="action" slot-scope="text, record">
          <a @click="queryBottleDetail(record)">试剂消耗明细</a>
           <a-divider type="vertical" />
            <a @click="queryItemDetail(record)">检验项目明细</a>
        </span>

      </a-table>
    </div>
    <pd-bottle-inf-modal ref="modalForm" @ok="modalFormOk"></pd-bottle-inf-modal>
    <pd-inspection-item-modal  ref="modalForm1" @ok="modalFormOk"></pd-inspection-item-modal>
   </a-card>
</template>
<script>

  import { httpAction,getAction,downFile } from '@/api/manage'
  import { filterObj } from '@/utils/util';
  import { JeecgListMixin} from '@/mixins/JeecgListMixin'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import JDictSelectTagExpand from "@/components/dict/JDictSelectTagExpand"
  import pdBottleInfModal from '../modules/PdBottleInfModal'
  import pdInspectionItemModal from '../modules/PdInspectionItemModal'


  export default {
    name: "PdInspectionMonthQuery",
    mixins:[JeecgListMixin],
    components: {
      pdBottleInfModal,
      pdInspectionItemModal,
      JDictSelectTagExpand
    },
    data () {
      return {
        description: '耗材月统计报表',
        notFoundContent:"未找到内容",
        venderValue: undefined,
        venderData: [],
        supplierValue: undefined,
        supplierData: [],
        departData: [],
        departValue: undefined,
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
            title:'统计月份',
            align:"center",
            width:'200px',
            dataIndex: 'month'
          },
          {
            title:'统计科室',
            align:"center",
            width:'200px',
            dataIndex: 'departName'
          },
         /* {
            title:'入库数量',
            align:"center",
            dataIndex: 'inRecordNum'
          },*/
          {
            title:'入库金额',
            align:"center",
            dataIndex: 'inRecordPrice'
          },
          /*{
            title:'出库数量',
            align:"center",
            dataIndex: 'callOutNum'
          },*/
          {
            title:'出库金额',
            align:"center",
            dataIndex: 'callOutPrice'
          },
          /*{
            title:'使用数量',
            align:"center",
            dataIndex: 'purchaseNum'
          },*/
          {
            title:'使用金额',
            align:"center",
            dataIndex: 'purchasePrice'
          },
          /*{
            title:'调入数量',
            align:"center",
            dataIndex: 'callInNum'
          },*/
          {
            title:'调入金额',
            align:"center",
            dataIndex: 'callInPrice'
          },
         /* {
            title:'退货数量',
            align:"center",
            dataIndex: 'rejectedNum'
          },*/
          {
            title:'退货金额',
            align:"center",
            dataIndex: 'rejectedPrice'
          },
          /*{
            title:'库存数量',
            align:"center",
            dataIndex: 'stockNum'
          },*/
          {
            title:'库存金额',
            align:"center",
            dataIndex: 'stockPrice'
          },
          /*{
            title:'差异数量',
            align:"center",
            dataIndex: 'disNum'
          },*/
          {
            title:'差异金额 ',
            align:"center",
            dataIndex: 'disPrice'
          },

        ],
        url: {
          list: "/pd/pdStatisticalReport/queryNumericalInfList",
          queryDepart: "/pd/pdDepart/queryListTree",
          exportXlsUrl: "/pd/pdStatisticalReport/numericalInfExportXls",
        },
        dictOptions:{

        },
        tableScroll:{ x: 1300 },
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
      monthChange(date, dateString){
        this.queryParam.month=dateString;
      },
      //科室查询start
      departHandleSearch(value) {
        getAction(this.url.queryDepart,{departName:value,departType:"1,2"}).then((res)=>{
          if (!res.success) {
            this.cmsFailed(res.message);
          }
          this.departData = res.result;
        })
      },
      //科室查询end

      loadData(arg) {
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        var params = this.getQueryParams();//查询条件
        params.tjType='0';
        this.loading = true;
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records;
            this.ipagination.total = res.result.total;
          }
          this.loading = false;
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
        param.tjType="0";
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
      //试剂消耗明细
      queryBottleDetail(record){
          this.$refs.modalForm.edit(record);
          this.$refs.modalForm.disableSubmit = false;
      },
      //检验项目明细
      queryItemDetail(record){
        this.$refs.modalForm1.edit(record);
        this.$refs.modalForm1.disableSubmit = false;
      },
    }
  }
</script>
<style scoped>
</style>