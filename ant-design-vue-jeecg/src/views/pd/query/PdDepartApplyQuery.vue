<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
        <a-col :md="6" :sm="8">
          <a-form-item label="年月">
<!--
            <a-month-picker placeholder="选择年月" @change="monthChange" v-model="queryParam.ym"/>
-->

<!--
            <a-year-panel  placeholder="选择年月" @change="monthChange" v-model="queryParam.ym"/>
-->

             <year-picker @input="monthChange"     v-model="queryParam.ym"/>


          </a-form-item>
        </a-col>
          <a-col :md="6" :sm="8">
          <a-form-item label="统计产品类型">
            <a-select placeholder="产品类型" :allowClear="true" v-model="queryParam.productFlag" >
              <a-select-option value="">试剂+耗材</a-select-option>
              <a-select-option value="0">耗材</a-select-option>
              <a-select-option value="1">试剂</a-select-option>
            </a-select>
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
      <a-button type="primary" icon="download" @click="handleExportXls('科室领用报表')">导出</a-button>
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
  import YearPicker from '../modules/YearPicker'

  export default {
    name: "PdDepartApplyQuery",
    mixins:[JeecgListMixin],
    components: {
      YearPicker,
      pdBottleInfModal,
      pdInspectionItemModal,
      JDictSelectTagExpand
    },
    data () {
      return {
        description: '科室耗材领用情况统计表',
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
            title:'科室',
            align:"center",
            width:'200px',
            dataIndex: 'departName'
          },
          {
            title:'01月金额',
            align:"center",
            dataIndex: 'janPrice'
          },
          {
            title:'02月金额',
            align:"center",
            dataIndex: 'febPrice'
          },
          {
            title:'03月金额',
            align:"center",
            dataIndex: 'marPrice'
          },
          {
            title:'04月金额',
            align:"center",
            dataIndex: 'aprPrice'
          },
          {
            title:'05月金额',
            align:"center",
            dataIndex: 'mayPrice'
          },
          {
            title:'06月金额',
            align:"center",
            dataIndex: 'junPrice'
          },
          {
            title:'07月金额',
            align:"center",
            dataIndex: 'julPrice'
          },
          {
            title:'08月金额',
            align:"center",
            dataIndex: 'augPrice'
          },
          {
            title:'09月金额',
            align:"center",
            dataIndex: 'septPrice'
          },
          {
            title:'10月金额',
            align:"center",
            dataIndex: 'octPrice'
          },
          {
            title:'11月金额',
            align:"center",
            dataIndex: 'novPrice'
          },
          {
            title:'12月金额',
            align:"center",
            dataIndex: 'decPrice'
          },
          {
            title:'合计金额',
            align:"center",
            dataIndex: 'countPrice'
          },
        ],
        url: {
          list: "/pd/pdStatisticalReport/departApplyUseReport",
          queryDepart: "/pd/pdDepart/queryListTree",
          exportXlsUrl: "/pd/pdStatisticalReport/departApplyExportXls",
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
      monthChange(dateString){
        this.queryParam.year=dateString;
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
        //params.tjType='0';
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


    }
  }
</script>
<style scoped>
</style>