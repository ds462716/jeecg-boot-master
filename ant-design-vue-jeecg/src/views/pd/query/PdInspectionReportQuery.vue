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
          <a-col :md="6" :sm="8">
            <a-form-item label="产品编号">
              <a-input placeholder="请输入编号" v-model="queryParam.number"></a-input>
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
          <template v-if="toggleSearchStatus">
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
              <a-form-item label="使用日期">
                <a-range-picker @change="dateChange" v-model="queryParam.queryDate"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
                  <a-form-item label="使用状态">
                    <a-select placeholder="使用状态" :allowClear="true" v-model="queryParam.status" >
                      <a-select-option value="0">正在使用</a-select-option>
                      <a-select-option value="1">已使用完</a-select-option>
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
    <div class="numberWARAP">
      <div class="total">总数量：<span >{{this.validatorRules.totalCount}}</span></div>
      <div class="overTime">总金额：<span>{{this.validatorRules.totalPrice}}</span></div>
    </div>
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button type="primary" icon="download" @click="handleExportXls('试剂消耗报表')">导出</a-button>
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
        notFoundContent:"未找到内容",
        venderValue: undefined,
        venderData: [],
        supplierValue: undefined,
        supplierData: [],
        validatorRules: {
          totalCount:{},//总数量
          totalPrice:{},//总金额
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
            title:'中标号',
            align:"center",
            width:'80px',
            dataIndex: 'bidingNumber'
          },
          {
            title:'规格数量',
            align:"center",
            width:'80px',
            dataIndex: 'specQuantity'
          },
          {
            title:'规格单位',
            align:"center",
            width:'80px',
            dataIndex: 'specUnitName'
          },
          {
            title:'消耗总数量',
            align:"center",
            width:'100px',
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
            title:'生产厂家',
            align:"center",
            dataIndex: 'venderName'
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
          exportXlsUrl: "/pd/pdBottleInf/ReportQueryExportXls",
          queryVender:"/pd/pdVender/getVenderList",
          querySupplier:"/pd/pdSupplier/getSupplierList",
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
      loadData(arg) {
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        var params = this.getQueryParams();//查询条件
        this.loading = true;
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.validatorRules.totalCount=res.result.totalCount;
            this.validatorRules.totalPrice=res.result.totalPrice;
            this.dataSource = res.result.records.records;
            this.ipagination.total = res.result.records.total;
          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
          this.loading = false;
        })
      },
      dateChange: function (value, dateString) {
        this.queryParam.queryDateStart=dateString[0];
        this.queryParam.queryDateEnd=dateString[1];
      },
      //生产厂家查询start
      venderHandleSearch(value) {
        this.getList(value,this.url.queryVender,"2");
      },
      venderHandleChange(value) {
        this.venderValue = value;
        this.getList(value,this.url.queryVender,"2");
      },
      //生产厂家查询end

      //供应商查询start
      supplierHandleSearch(value) {
        this.getList(value,this.url.querySupplier,"1");
      },
      supplierHandleChange(value) {
        this.supplierValue = value;
        this.getList(value,this.url.querySupplier,"1");
      },
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
      //图表信息查看
      queryDetail(record){
        record.status=this.queryParam.status;
          this.$refs.modalForm.edit(record);
          this.$refs.modalForm.disableSubmit = false;
      },
    }
  }
</script>

<style scoped>
.numberWARAP{width:100%;height:30px;line-height:30px;margin:20px 0;}
.numberWARAP>div{float:left;width:50%;height:30px;line-height:30px;color:#666;font-size:20px;text-align:center;border-right:1px solid #ccc;}
.numberWARAP>div:nth-child(2){border:none;}
.changeColor .red td,.changeColor .red td a{color: red}
</style>