<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
        <a-col :md="6" :sm="8">
          <a-form-item label="产品名称">
            <a-input placeholder="请选输入品名称" v-model="queryParam.productName"></a-input>
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
     </div>
    <!-- table区域-begin -->
    <div>
      <a-table
        CLASS="changeColor"
        ref="table"
        size="middle"
        bordered
        rowKey="productId"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
         @change="handleTableChange">
        <span slot="action" slot-scope="text, record">
          <a @click="queryDetail(record)">图表查看</a>
        </span>
      </a-table>
    </div>
    <inspection-chart-multid ref="modalForm" @ok="modalFormOk"></inspection-chart-multid>
  </a-card>
</template>
<script>

  import { httpAction,getAction,downFile } from '@/api/manage'
  import { filterObj } from '@/utils/util';
  import { JeecgListMixin} from '@/mixins/JeecgListMixin'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import JDictSelectTagExpand from "@/components/dict/JDictSelectTagExpand"
  import inspectionChartMultid from '../modules/inspectionChartMultid'


  export default {
    name: "PdInspectionReportQuery",
    mixins:[JeecgListMixin],
    components: {
      inspectionChartMultid,
      JDictSelectTagExpand
    },
    data () {
      return {
        description: '试剂消耗报表',
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
            title:'试剂编号',
            align:"center",
            width:'200px',
            dataIndex: 'number'
          },
          {
            title:'试剂名称',
            align:"center",
            width:'450px',
            dataIndex: 'productName'
          },
          {
            title:'规格数量',
            align:"center",
            width:'100px',
            dataIndex: 'specQuantity'
          },
          {
            title:'规格单位',
            align:"center",
            width:'100px',
            dataIndex: 'specUnitName'
          },
          {
            title:'消耗总数量',
            align:"center",
            width:'120px',
            dataIndex: 'num'
          },
          {
            title:'单位',
            align:"center",
            width:'100px',
            dataIndex: 'unitName'
          },
          {
            title:'消耗总金额',
            align:"center",
            width:'90px',
            dataIndex: 'totalPrice'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/pd/pdBottleInf/bottleInfReportQuery",
          exportXlsUrl: "/pd/pdStockRecordIn/exportXls",
        },
        dictOptions:{

        },

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
      //图表信息查看
      queryDetail(record){
          this.$refs.modalForm.edit(record);
          this.$refs.modalForm.disableSubmit = false;
      },
    }
  }
</script>
<style scoped>
</style>