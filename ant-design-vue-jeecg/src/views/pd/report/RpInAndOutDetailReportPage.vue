<template>
  <j-modal
    :visible="visible"
    :width="popModal.width"
    :maskClosable="false"
    :title="popModal.title"
    :lockScroll="popModal.lockScroll"
    :fullscreen="popModal.fullscreen"
    :switchFullscreen="popModal.switchFullscreen"
    @cancel="handleCancel"
  >


    <a-card :bordered="false">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">

            <a-col :md="6" :sm="8">
              <a-form-item label="出入库单号">
                <a-input placeholder="请输入单号" v-model="queryParam.recordNo"></a-input>
              </a-form-item>
            </a-col>
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

            <template v-if="toggleSearchStatus">
              <a-col :md="6" :sm="8">
                <a-form-item label="规格">
                  <a-input placeholder="请输入规格" v-model="queryParam.spec"></a-input>
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
                <a-form-item label="型号">
                  <a-input placeholder="请输入型号" v-model="queryParam.version"></a-input>
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
        <a-button type="primary" icon="download" @click="handleExportXls()">导出</a-button>
      </div>
      <!-- table区域-begin -->

      <div class="table-operator">
        <a-tabs>
          <a-tab-pane tab="入库明细" :forceRender="true">
            <a-table
              ref="out_table"
              size="small"
              bordered
              rowKey="id"
              :columns="inTable.columns"
              :dataSource="inTable.dataSource"
              :pagination="inTable.ipagination"
              :loading="inTable.loading"
              :scroll="inTable.tableScroll"
              @change="inHandleTableChange">
              <template slot="htmlSlot" slot-scope="text">
                <div v-html="text"></div>
              </template>
            </a-table>
          </a-tab-pane>
        </a-tabs>
      </div>

      <div class="table-operator">
        <a-tabs>
          <a-tab-pane tab="出库明细" :forceRender="true">
            <a-table
              ref="in_table"
              size="small"
              bordered
              rowKey="id"
              :columns="outTable.columns"
              :dataSource="outTable.dataSource"
              :pagination="outTable.ipagination"
              :loading="outTable.loading"
              :scroll="outTable.tableScroll"
              @change="outHandleTableChange">
              <template slot="htmlSlot" slot-scope="text">
                <div v-html="text"></div>
              </template>
            </a-table>
          </a-tab-pane>
        </a-tabs>
      </div>
    </a-card>
    <template slot="footer">
      <a-button @click="handleCancel" style="margin-right: 15px;">关  闭</a-button>
    </template>
  </j-modal>
</template>

<script>

  import Vue from 'vue'
  import { ACCESS_TOKEN } from "@/store/mutation-types"
  import { httpAction,getAction,downFile } from '@/api/manage'
  import { filterObj } from '@/utils/util';
  // import { JeecgListMixin} from '@/mixins/JeecgListMixin'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import JDictSelectTagExpand from "@/components/dict/JDictSelectTagExpand"
  import JEllipsis from '@/components/jeecg/JEllipsis'

  export default {
    name: 'RpInAndOutDetailReportPage',
    // mixins:[JeecgListMixin],
    components: {
      JDictSelectTagExpand,JEllipsis
    },
    data() {
      return {
        //token header
        tokenHeader: {'X-Access-Token': Vue.ls.get(ACCESS_TOKEN)},
        /* 查询条件-请不要在queryParam中声明非字符串值的属性 */
        queryParam: {},
        /* 查询折叠 */
        toggleSearchStatus:false,

        visible: false,
        record:{},
        notFoundContent:"未找到内容",
        supplierValue: undefined,
        supplierData: [],

        venderValue: undefined,
        venderData: [],

        departValue: undefined,
        departList:[],

        allDepartValue: undefined,
        allDepartList:[],
        inTable: {
          loading:false,
          dataSource: [],
          ipagination:{
            current: 1,
            pageSize: 10,
            pageSizeOptions: ['10', '20', '30', '50', '100'],
            showTotal: (total, range) => {
              return range[0] + "-" + range[1] + " 共" + total + "条"
            },
            showQuickJumper: true,
            showSizeChanger: true,
            total: 0
          },
          tableScroll:{x :4700},
          columns: [
            { title: '序号', dataIndex: '', key:'rowIndex', width:60, align:"center",
              customRender:function (t,r,index) {
                return parseInt(index)+1;
              }
            },
            { title:'入库单号', align:"center", width:'100px', dataIndex: 'recordNo'},
            { title:'入库日期', align:"center", width:'100px', dataIndex: 'auditDate',
              customRender:function (text) {
                return !text?"":(text.length>10?text.substr(0,10):text)
              }
            },
            { title:'入库类型', align:"center", width:'100px',dataIndex: 'inType',
              customRender:(text)=>{
                if(!text){
                  return ''
                }else{
                  return filterMultiDictText(this.dictOptions['inType'], text+"")
                }
              }
            },
            { title:'出库科室', align:"center", width:'100px', dataIndex: 'outDepartName' },
            { title:'入库科室', align:"center", width:'100px', dataIndex: 'inDepartName' },
            { title:'产品编号', align:"center", width:'150px', dataIndex: 'productNumber' },
            { title:'产品名称', align:"center", width:'250px', dataIndex: 'productName' },
            // { title:'产品条码', align:"center", width:'150px', dataIndex: 'productBarCode' },
            { title:'规格', align:"center", width:'150px', dataIndex: 'spec' },
            { title:'型号', align:"center", width:'150px', dataIndex: 'version' },
            { title:'批号', align:"center", width:'150px', dataIndex: 'batchNo' },
            { title:'生产日期', align:"center", dataIndex: 'produceDate', width:'100px',
              customRender:function (text) {
                return !text?"":(text.length>10?text.substr(0,10):text)
              }
            },
            { title:'有效期', align:"center", dataIndex: 'expDate', width:'100px',
              customRender:function (text) {
                return !text?"":(text.length>10?text.substr(0,10):text)
              }
            },
            { title:'入库数量', align:"center", width:'100px', dataIndex: 'productNum' },
            { title:'库存数量', align:"center", width:'100px', dataIndex: 'stockNum' },
            { title:'单位', align:"center", width:'60px', dataIndex: 'unitName' },
            { title:'入库单价', align:"center", width:'100px', dataIndex: 'purchasePrice' },
            { title:'入库金额', align:"center", width:'100px', dataIndex: 'inTotalPrice' },
            { title:'规格数量', align:"center", width:'100px', dataIndex: 'specQuantity' },
            { title:'规格单位', align:"center", width:'100px', dataIndex: 'specUnitName' },

            { title:'生产厂家', align:"center", width:'250px', dataIndex: 'venderName' },
            { title:'供应商', align:"center", width:'250px', dataIndex: 'supplierName' },
            { title:'配送商', align:"center", width:'250px', dataIndex: 'distributorName' },
            { title:'注册证号', align:"center", width:'250px', dataIndex: 'productRegistration' },
            { title:'发票号', align:"center", width:'150px', scopedSlots: {customRender: "ellipsisText"}, dataIndex: 'invoiceNo' },
            { title:'发票代码', align:"center", width:'150px', scopedSlots: {customRender: "ellipsisText"}, dataIndex: 'invoiceCode' },
            { title:'发票日期', align:"center", width:'100px', dataIndex: 'invoiceData',
              customRender:function (text) {
                return !text?"":(text.length>10?text.substr(0,10):text)
              }
            },
            { title:'备注', align:"center", dataIndex: 'remarks' },
            { title:'收费代码', align:"center", dataIndex: 'chargeCode' },
            { title:'中标号', align:"center", width:'150px', dataIndex: 'bidingNumber' },
            { title:'省标码', align:"center", width:'150px', dataIndex: 'dartCode' },
            { title:'产品JDE编号', align:"center", width:'150px', dataIndex: 'jdeCode' },
            { title:'供应商JDE编号', align:"center", width:'150px', dataIndex: 'supplierJdeCode' },
            { title:'生产厂家JDE编号', align:"center", width:'150px', dataIndex: 'venderJdeCode' },
            { title:'操作人', align:"center", width:'100px', dataIndex: 'realname' }
          ],
        },
        outTable: {
          loading:false,
          dataSource: [],
          ipagination:{
            current: 1,
            pageSize: 10,
            pageSizeOptions: ['10', '20', '30', '50', '100'],
            showTotal: (total, range) => {
              return range[0] + "-" + range[1] + " 共" + total + "条"
            },
            showQuickJumper: true,
            showSizeChanger: true,
            total: 0
          },
          tableScroll:{x :4500},
          columns: [
            { title: '序号', dataIndex: '', key:'rowIndex', width:60, align:"center",
              customRender:function (t,r,index) {
                return parseInt(index)+1;
              }
            },
            { title:'出库单号', align:"center", width:'100px', dataIndex: 'recordNo' },
            { title:'出库日期', align:"center", width:'100px', dataIndex: 'auditDate',
              customRender:function (text) {
                return !text?"":(text.length>10?text.substr(0,10):text)
              }
            },
            { title:'出库类型', align:"center", width:'100px', dataIndex: 'outType',
              customRender:(text)=>{
                if(!text){
                  return ''
                }else{
                  return filterMultiDictText(this.dictOptions['outType'], text+"")
                }
              }
            },
            { title:'出库科室', align:"center", width:'100px', dataIndex: 'outDepartName' },
            { title:'入库科室', align:"center", width:'100px', dataIndex: 'inDepartName' },
            { title:'产品编号', align:"center", width:'150px', dataIndex: 'productNumber' },
            { title:'产品名称', align:"center", width:'250px', dataIndex: 'productName' },
            // { title:'产品条码', align:"center", width:'150px', dataIndex: 'productBarCode' },
            { title:'规格', align:"center", width:'150px', dataIndex: 'spec' },
            { title:'型号', align:"center", width:'150px', dataIndex: 'version' },
            { title:'批号', align:"center", width:'150px', dataIndex: 'batchNo' },
            { title:'生产日期', align:"center", width:'100px', dataIndex: 'produceDate',
              customRender:function (text) {
                return !text?"":(text.length>10?text.substr(0,10):text)
              }
            },
            { title:'有效期', align:"center", width:'100px', dataIndex: 'expDate',
              customRender:function (text) {
                return !text?"":(text.length>10?text.substr(0,10):text)
              }
            },
            { title:'出库数量', align:"center", width:'100px', dataIndex: 'productNum' },
            { title:'单位', align:"center", width:'60px', dataIndex: 'unitName' },
            { title:'入库单价', align:"center", width:'100px', dataIndex: 'purchasePrice' },
            { title:'入库金额', align:"center", width:'100px', dataIndex: 'inTotalPrice' },
            { title:'出库单价', align:"center", width:'100px', dataIndex: 'sellingPrice' },
            { title:'出库金额', align:"center", width:'100px', dataIndex: 'outTotalPrice' },
            { title:'规格数量', align:"center", width:'100px', dataIndex: 'specQuantity' },
            { title:'规格单位', align:"center", width:'100px', dataIndex: 'specUnitName' },

            { title:'生产厂家', align:"center", width:'250px', dataIndex: 'venderName' },
            { title:'供应商', align:"center", width:'250px', dataIndex: 'supplierName' },
            { title:'配送商', align:"center", width:'250px', dataIndex: 'distributorName' },
            { title:'注册证号', align:"center", width:'250px', dataIndex: 'productRegistration' },

            { title:'备注', align:"center", dataIndex: 'remarks' },
            { title:'收费代码', align:"center", dataIndex: 'chargeCode' },
            { title:'中标号', align:"center", width:'150px', dataIndex: 'bidingNumber' },
            { title:'省标码', align:"center", width:'150px', dataIndex: 'dartCode' },
            { title:'产品JDE编号', align:"center", width:'150px', dataIndex: 'jdeCode' },
            { title:'供应商JDE编号', align:"center", width:'150px', dataIndex: 'supplierJdeCode' },
            { title:'生产厂家JDE编号', align:"center", width:'150px', dataIndex: 'venderJdeCode' },
            { title:'操作人', align:"center", width:'100px', dataIndex: 'realname' }
          ],
        },
        url: {
          inList: "/pd/pdStockRecordIn/rpInDetailReport",
          outList: "/pd/pdStockRecordIn/rpOutDetailReport",
          querySupplier:"/pd/pdSupplier/getSupplierList",
          queryVender:"/pd/pdVender/getVenderList",
          allDepartList:"/pd/pdDepart/getSysDepartList",
          departList: "/pd/pdDepart/queryListTree",
          exportInReportXls: "/pd/pdStockRecordIn/exportInReportXls",
          exportOutReportXls: "/pd/pdStockRecordIn/exportOutReportXls",
        },
        dictOptions:{
          outType:[],
        },
        popModal: {
          title: '出入库明细统计报表',
          visible: false,
          width: '100%',
          // width: '1200',
          style: { top: '20px' },
          switchFullscreen: true,  //缩放按钮
          lockScroll: false,
          fullscreen: true,
        },
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      show(record){
        this.visible = true;
        this.record = record;
        this.initDictConfig();
        this.loadData(record);
      },
      initDictConfig(){
        //静态字典值加载
        initDictOptions('out_type').then((res) => { //出库类型
          if (res.success) {
            this.$set(this.dictOptions, 'outType', res.result)
          }
        });
        initDictOptions('in_type').then((res) => { //入库类型
          if (res.success) {
            this.$set(this.dictOptions, 'inType', res.result)
          }
        })
      },
      loadData(record) {
        this.inTable.ipagination.current = 1;
        this.outTable.ipagination.current = 1;
        this.loadInData(record);
        this.loadOutData(record);
      },
      //查入库明细
      loadInData(record){
        var params = this.getInQueryParams(record);//查询条件
        this.inTable.loading = true;
        getAction(this.url.inList, params).then((res) => {
          if (res.success) {
            this.inTable.dataSource = res.result.records;
            this.inTable.ipagination.total = res.result.total;
          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
          this.inTable.loading = false;
        })
      },
      //查出库明细
      loadOutData(record){
        var params = this.getOutQueryParams(record);//查询条件
        this.outTable.loading = true;
        getAction(this.url.outList, params).then((res) => {
          if (res.success) {
            this.outTable.dataSource = res.result.records;
            this.outTable.ipagination.total = res.result.total;
          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
          this.outTable.loading = false;
        })
      },
      getInQueryParams(record) {
        //获取查询条件
        var param = this.queryParam;
        param.pageNo = this.inTable.ipagination.current;
        param.pageSize = this.inTable.ipagination.pageSize;
        param.departId = record.departId;
        param.yearMonth = record.yearMonth;
        param.queryDateStart = record.queryDateStart;
        param.queryDateEnd = record.queryDateEnd;
        delete param.queryExpDate; //范围参数不传递后台，传后台会报错
        return filterObj(param);
      },
      getOutQueryParams(record) {
        //获取查询条件
        var param = this.queryParam;
        param.pageNo = this.outTable.ipagination.current;
        param.pageSize = this.outTable.ipagination.pageSize;
        param.departId = record.departId;
        param.yearMonth = record.yearMonth;
        param.queryDateStart = record.queryDateStart;
        param.queryDateEnd = record.queryDateEnd;
        // delete param.queryExpDate; //范围参数不传递后台，传后台会报错
        return filterObj(param);
      },
      expDateChange: function (value, dateString) {
        this.queryParam.queryExpDateStart=dateString[0];
        this.queryParam.queryExpDateEnd=dateString[1];
        // delete this.queryParam.queryExpDate; //范围参数不传递后台，传后台会报错
      },
      searchQuery() {
        this.loadData(this.record);
      },
      superQuery() {
        this.$refs.superQueryModal.show();
      },
      searchReset() {
        this.queryParam = {}
        this.loadData(this.record);
      },
      inHandleTableChange(pagination, filters, sorter) {
        this.inTable.ipagination = pagination;
        this.loadInData(this.record);
      },
      outHandleTableChange(pagination, filters, sorter) {
        this.outTable.ipagination = pagination;
        this.loadOutData(this.record);
      },
      handleToggleSearch(){
        this.toggleSearchStatus = !this.toggleSearchStatus;
      },
      /** 关闭按钮 **/
      handleCancel(){
        this.$emit('ok');
        this.close();
      },
      close() {
        this.queryParam = {};
        this.inTable.ipagination.current = 1;
        this.inTable.ipagination.pageSize = 10;
        this.outTable.ipagination.current = 1;
        this.outTable.ipagination.pageSize = 10;
        this.visible = false;
        this.$emit('close');
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
      // 部门下拉框搜索
      allDepartHandleSearch(value){
        getAction(this.url.allDepartList,{departName:value,parentFlag:"0"}).then((res)=>{
          if (!res.success) {
            this.cmsFailed(res.message);
          }
          this.allDepartList = res.result;
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
      /**重写导出方法**/
      handleExportXls(){
        //入库明细导出
        let inFileName = "入库明细统计报表" + "_" + new Date().toLocaleString();
        let inParam = this.getInQueryParams(this.record);//查询条件
        downFile(this.url.exportInReportXls,inParam).then((data)=>{
          if (!data) {
            this.$message.warning("文件下载失败")
            return
          }
          if (typeof window.navigator.msSaveBlob !== 'undefined') {
            window.navigator.msSaveBlob(new Blob([data],{type: 'application/vnd.ms-excel'}), inFileName+'.xls')
          }else{
            let url = window.URL.createObjectURL(new Blob([data],{type: 'application/vnd.ms-excel'}))
            let link = document.createElement('a')
            link.style.display = 'none'
            link.href = url
            link.setAttribute('download', inFileName+'.xls')
            document.body.appendChild(link)
            link.click()
            document.body.removeChild(link); //下载完成移除元素
            window.URL.revokeObjectURL(url); //释放掉blob对象
          }
        });

        //出库明细导出
        let outFileName = "出库明细统计报表" + "_" + new Date().toLocaleString();
        let outParam = this.getOutQueryParams(this.record);//查询条件
        downFile(this.url.exportOutReportXls,outParam).then((data)=>{
          if (!data) {
            this.$message.warning("文件下载失败")
            return
          }
          if (typeof window.navigator.msSaveBlob !== 'undefined') {
            window.navigator.msSaveBlob(new Blob([data],{type: 'application/vnd.ms-excel'}), outFileName+'.xls')
          }else{
            let url = window.URL.createObjectURL(new Blob([data],{type: 'application/vnd.ms-excel'}))
            let link = document.createElement('a')
            link.style.display = 'none'
            link.href = url
            link.setAttribute('download', outFileName+'.xls')
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
  .drawer-bootom-button {
    width: 100%;
    text-align: right;
    background: #fff;
    margin-top:10px;
  }
</style>