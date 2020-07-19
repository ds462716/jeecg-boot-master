<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
       <!-- /* 供应商，生产厂家*/-->

        <a-row :gutter="24">
          <!--<a-col :md="6" :sm="8">
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
          </a-col>-->
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
              <a-form-item label="是否试剂">
                <j-dict-select-tag-expand type="list" v-model="queryParam.productFlag" dictCode="yn" placeholder="请选择"/>
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
              <a-form-item label="中标号">
                <a-input placeholder="中标号" v-model="queryParam.bidingNumber"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="入库日期">
                <a-range-picker @change="inDateChange" v-model="queryParam.queryInDate"/>
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
            <a-col :md="6" :sm="8">
              <a-form-item label="一级分类">
                <a-select
                  showSearch
                  :categoryOne="categoryOneValue"
                  placeholder="请选择一级分类"
                  :defaultActiveFirstOption="false"
                  :allowClear="true"
                  :showArrow="true"
                  :filterOption="false"
                  @search="categoryOneHandleSearch"
                  @change="categoryOneHandleChange"
                  @focus="categoryOneHandleSearch"
                  :notFoundContent="notFoundContent"
                  v-model="queryParam.categoryOne"
                >
                  <a-select-option v-for="d in categoryOneData" :key="d.value">{{d.text}}</a-select-option>
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
      <a-button type="primary" icon="download" @click="handleExportXls('供应商用量')">导出</a-button>
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
        :scroll="tableScroll"
        @change="handleTableChange"
        >
        <template slot="ellipsisText" slot-scope="text">
          <j-ellipsis :value="text" :length="textMaxLength"></j-ellipsis>
        </template>
      </a-table>
    </div>
  </a-card>
</template>
<script>

  import { JeecgListMixin ,handleEdit} from '@/mixins/JeecgListMixin'
  import { getAction ,downFile} from '@/api/manage'
  import { filterObj } from '@/utils/util'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import JDictSelectTagExpand from "@/components/dict/JDictSelectTagExpand"
  import JEllipsis from '@/components/jeecg/JEllipsis'
  export default {
    name: "PdSupplierCountQueryList",
    mixins:[JeecgListMixin],
    components: {
      JDictSelectTagExpand,
      JEllipsis
    },
    data () {
      return {
        description: '供应商用量查询',
        departData: [],
        departValue: undefined,
        notFoundContent:"未找到内容",
        supplierValue: undefined,
        supplierData: [],
        venderValue: undefined,
        venderData: [],
        categoryOneData: [],
        categoryOneValue: undefined,
        textMaxLength:20,
        // 表头
        columns: [
          {
            title:'供应商',
            align:"center",
            scopedSlots: {customRender: "ellipsisText"},
            dataIndex: 'supplierName'
          },
          {
            title:'产品名称',
            align:"center",
            scopedSlots: {customRender: "ellipsisText"},
            dataIndex: 'productName'
          },
          {
            title:'产品编号',
            align:"center",
            dataIndex: 'number'
          },
           /*{
            title:'一级分类',
            align:"center",
            dataIndex: 'categoryOne'
          },*/
          {
            title:'产品类型',
            align:"center",
            dataIndex: 'productFlagName'
          },

          {
            title:'规格',
            align:"center",
            dataIndex: 'spec'
          },
          {
            title:'单位',
            align:"center",
            dataIndex: 'unitName'
          },
          {
            title:'单价',
            align:"center",
            dataIndex: 'purchasePrice'
          },
          {
            title:'入库数量',
            align:"center",
            dataIndex: 'productNum'
          },
          {
            title:'入库金额',
            align:"center",
            dataIndex: 'inTotalPrice'
          },
          {
            title:'库存数量',
            align:"center",
            dataIndex: 'stockNum'
          },
          {
            title:'领用数量',
            align:"center",
            dataIndex: 'outProductNum'
          },
          {
            title:'领用金额',
            align:"center",
            dataIndex: 'outTotalPrice'
          },

          {
            title:'中标号',
            align:"center",
            dataIndex: 'bidingNumber'
          },

          {
            title:'注册证号',
            align:"center",
            scopedSlots: {customRender: "ellipsisText"},
            dataIndex: 'registration'
          },
          {
            title:'生产厂家',
            align:"center",
            scopedSlots: {customRender: "ellipsisText"},
            dataIndex: 'venderName'
          },

        ],
        url: {
          list: "/pd/pdStockRecordReturn/querySupplierCountList",
          exportXlsUrl: "/pd/pdProductStockTotal/stockExportXls",
          queryDepart: "/pd/pdDepart/queryListTree",
          querySupplier:"/pd/pdSupplier/getSupplierList",
          queryVender:"/pd/pdVender/getVenderList",
          queryCategoryOne:"/pd/pdCategory/getCategoryOneList?type=0",
        },
        dictOptions:{
            nestatStatus:[],
        },
        tableScroll:{x :2500},
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      inDateChange: function (value, dateString) {
        this.queryParam.queryDateStart=dateString[0];
        this.queryParam.queryDateEnd=dateString[1];
      },



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

      //一级分类查询start
      categoryOneHandleSearch(value) {
        this.getList(value,this.url.queryCategoryOne,"3");
      },
      categoryOneHandleChange(value) {
        this.categoryOneValue = value;
        this.getList(value,this.url.queryCategoryOne,"3");
      },
      //一级分类查询end
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
          }else if(flag == "3"){
            this.categoryOneData = data;
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
        delete param.queryInDate;
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

      initDictConfig(){ //静态字典值加载
         /*initDictOptions('nestat_status').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'nestatStatus', res.result)
          }
        })*/
      },
    }
  }
</script>
<style scoped>
</style>