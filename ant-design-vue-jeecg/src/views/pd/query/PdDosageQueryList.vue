<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :span="6">
            <a-form-item label="入库科室">
              <a-select
                mode="multiple"
                showSearch
                placeholder="请选择入库科室"
                :supplierId="departValue"
                :defaultActiveFirstOption="false"
                :showArrow="true"
                :filterOption="false"
                :allowClear="true"
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
            <a-form-item label="产品名称">
              <a-input placeholder="请选择产品名称" v-model="queryParam.productName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="产品编号">
              <a-input placeholder="请输入产品编号" v-model="queryParam.productNumber"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="病人姓名">
              <a-input placeholder="请输入病人姓名" v-model="queryParam.patientInfo"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="住院号">
              <a-input placeholder="请输入住院号" v-model="queryParam.inHospitalNo"></a-input>
            </a-form-item>
          </a-col>
          <a-col  :md="6" :sm="8">
            <a-form-item label="使用日期">
              <a-range-picker @change="dosageDateChange" v-model="queryParam.queryDate"/>
            </a-form-item>
          </a-col>
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
      <a-button type="primary" icon="download" @click="handleExportXls('用量明细')">导出</a-button>
    </div>
    <!-- table区域-begin -->
    <div>
      <a-table
        CLASS="changeColor"
        ref="table"
        size="middle"
        bordered
        rowKey="pdDosageDetailId"
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

  import { JeecgListMixin} from '@/mixins/JeecgListMixin'
  import { httpAction,getAction,downFile } from '@/api/manage'
  import { filterObj } from '@/utils/util';

  export default {
    name: "PdDosageQueryList",
    mixins:[JeecgListMixin],
    components: {
    },
    data () {
      return {
        description: '用量明细查询',
        tableScroll:{x :3000},
        notFoundContent:"未找到内容",
        departValue: undefined,
        departList:[],
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
            title:'用量单号',
            align:"center",
            dataIndex: 'dosageNo'
          },
          {
            title:'用量科室',
            align:"center",
            dataIndex: 'departName'
          },
          /*{
            title:'货位',
            align:"center",
            dataIndex: 'outHuoweiName'
          },*/
          {
            title:'用量日期',
            align:"center",
            dataIndex: 'dosageDate'
          },
          {
            title:'产品名称',
            align:"center",
            dataIndex: 'productName'
          },
          /*{
            title:'产品条码',
            align:"center",
            dataIndex: 'productBarCode'
          },*/
          {
            title:'唯一码',
            align:"center",
            dataIndex: 'refBarCode'
          },
          {
            title:'规格',
            align:"center",
            dataIndex: 'spec'
          },
          {
            title:'型号',
            align:"center",
            dataIndex: 'version'
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
            title:'数量',
            align:"center",
            dataIndex: 'dosageCount'
          },
          {
            title:'单位',
            align:"center",
            dataIndex: 'unitName'
          },
          /*{
            title:'单价',
            align:"center",
            dataIndex: 'limitDown'
          },
          {
            title:'金额',
            align:"center",
            dataIndex: 'limitDown'
          },*/
          {
            title:'生产厂家',
            align:"center",
            dataIndex: 'venderName'
          },
          /*{
            title:'操作人',
            align:"center",
            dataIndex: 'stockNum'
          },*/
          {
            title:'执行科室',
            align:"center",
            dataIndex: 'exeDeptName'
          },
          {
            title:'手术科室',
            align:"center",
            dataIndex: 'oprDeptName'
          },
          {
            title:'住院号',
            align:"center",
            dataIndex: 'inHospitalNo'
          },
          {
            title:'病人姓名',
            align:"center",
            dataIndex: 'patientInfo'
          },
          {
            title:'手术医生',
            align:"center",
            dataIndex: 'surgeonName'
          },
          {
            title:'HIS收费代码',
            align:"center",
            dataIndex: 'chargeCode'
          },
        ],
        url: {
          list: "/pd/pdDosage/queryPdDosageList",
          querySupplier:"/pd/pdSupplier/getSupplierList",
          queryVender:"/pd/pdVender/getVenderList",
          // departList:"/pd/pdDepart/getSysDepartList",
          departList: "/pd/pdDepart/queryListTree",
          exportXlsUrl: "/pd/pdDosage/exportXls",
        },
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      dosageDateChange (value, dateString) {
        this.queryParam.queryDateStart=dateString[0];
        this.queryParam.queryDateEnd=dateString[1];
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
        delete param.queryDate; //范围参数不传递后台，传后台会报错
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