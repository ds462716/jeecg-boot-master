<template>
  <j-modal
    :visible="visible"
    :width="popModal.width"
    :maskClosable="false"
    :lockScroll="popModal.lockScroll"
    :fullscreen="popModal.fullscreen"
    :switchFullscreen="popModal.switchFullscreen"
    @cancel="handleCancel"
  >
  <a-card :bordered="false">
    <a-tabs v-model="activeKey">
      <a-tab-pane tab="库存明细" key="1">
        <a-card :bordered="false">
          <!-- 查询区域 -->
          <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="stockSearchQuery">
              <a-row :gutter="24">
                <a-col :md="6" :sm="8">
                  <a-form-item label="产品名称">
                    <a-input placeholder="请选输入品名称" v-model="stockQueryParam.productName"></a-input>
                  </a-form-item>
                </a-col>
                <a-col :md="6" :sm="8">
                  <a-form-item label="产品编号">
                    <a-input placeholder="请输入产品编号" v-model="stockQueryParam.productNumber"></a-input>
                  </a-form-item>
                </a-col>

                <template v-if="stockToggleSearchStatus">
                  <a-col :md="6" :sm="8">
                    <a-form-item label="规格">
                      <a-input placeholder="请输入规格" v-model="stockQueryParam.spec"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="8">
                    <a-form-item label="有效期">
                      <a-range-picker @change="stockExpDateChange" v-model="stockQueryParam.queryExpDate"/>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="8">
                    <a-form-item label="注册证">
                      <a-input placeholder="请输入注册证" v-model="stockQueryParam.registration"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="8">
                    <a-form-item label="型号">
                      <a-input placeholder="请输入型号" v-model="stockQueryParam.version"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="8">
                    <a-form-item label="批号">
                      <a-input placeholder="请输入批号" v-model="stockQueryParam.batchNo"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="8">
                    <a-form-item label="生产厂家">
                      <a-select
                        showSearch
                        :venderId="stockVenderValue"
                        placeholder="请选择生产厂家"
                        :defaultActiveFirstOption="false"
                        :allowClear="true"
                        :showArrow="true"
                        :filterOption="false"
                        @search="stockVenderHandleSearch"
                        @change="stockVenderHandleChange"
                        @focus="stockVenderHandleSearch"
                        :notFoundContent="notFoundContent"
                        v-model="stockQueryParam.venderId"
                      >
                        <a-select-option v-for="d in stockVenderData" :key="d.value">{{d.text}}</a-select-option>
                      </a-select>
                    </a-form-item>
                  </a-col>
                </template>

                <a-col :md="6" :sm="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="stockSearchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="stockSearchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="stockHandleToggleSearch" style="margin-left: 8px">
                {{ stockToggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="stockToggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
                </a-col>
              </a-row>
            </a-form>
          </div>
          <!-- 查询区域-END -->
          <!-- 操作按钮区域 -->
          <div class="table-operator">
            <!--<a-button type="primary" icon="download" @click="handleExportXls()">导出</a-button>-->
          </div>
          <!-- table区域-begin -->
          <div>
            <a-table
              CLASS="changeColor"
              ref="stock_table"
              size="middle"
              bordered
              rowKey="id"
              :columns="stockTable.columns"
              :dataSource="stockTable.dataSource"
              :pagination="stockTable.ipagination"
              :loading="stockTable.loading"
              :scroll="stockTable.tableScroll"
              @change="stockHandleTableChange">
            </a-table>
          </div>
        </a-card>
      </a-tab-pane>
    </a-tabs>

  </a-card>
    <template slot="footer">
      <a-button @click="handleCancel" style="margin-right: 15px;">关  闭</a-button>
    </template>
  </j-modal>
</template>

<script>
  import { triggerWindowResizeEvent } from '@/utils/util'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import { httpAction,getAction,downFile } from '@/api/manage'
  import { filterObj } from '@/utils/util';
  import { VALIDATE_NO_PASSED, getRefPromise, validateFormAndTables } from '@/utils/JEditableTableUtil'

  export default {
    name: "RpDepartStockReportMain",
    components: {
    },
    data() {
      return {
        description: '订单管理页面',
        /* 查询折叠 */
        notFoundContent:"未找到内容",
        activeKey:"1",
        // 表头
        stockQueryParam: {},
        /* 查询折叠 */
        stockToggleSearchStatus:false,
        stockVenderValue: undefined,
        stockVenderData: [],
        // 表头
        stockTable: {
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
          tableScroll:{x :3000},
          columns: [
            { title: '序号', dataIndex: '', key:'rowIndex', width:60, align:"center",
              customRender:function (t,r,index) {
                return parseInt(index)+1;
              }
            },
            {
              title:'产品编号',
              align:"center",
              dataIndex: 'productNumber'
            },
            {
              title:'产品名称',
              align:"center",
              scopedSlots: {customRender: "ellipsisText"},
              dataIndex: 'productName'
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
            { title:'有效期', align:"center", dataIndex: 'expDate', width:'100px',
              customRender:function (text) {
                return !text?"":(text.length>10?text.substr(0,10):text)
              }
            },
            {
              title:'库存数量',
              align:"center",
              dataIndex: 'stockNum'
            },
            {
              title:'单位',
              align:"center",
              dataIndex: 'unitName'
            },
            {
              title:'入库单价',
              align:"center",
              dataIndex: 'purchasePrice'
            },
            {
              title:'入库金额',
              align:"center",
              dataIndex: 'purchaseAmount'
            },
            {
              title:'出库单价',
              align:"center",
              dataIndex: 'sellingPrice'
            },
            {
              title:'出库金额',
              align:"center",
              dataIndex: 'sellingAmount'
            },
            { title:'生产厂家', align:"center", width:'250px', dataIndex: 'venderName' },
            {
              title:'供应商',
              align:"center",
              scopedSlots: {customRender: "ellipsisText"},
              dataIndex: 'supplierName'
            },
            {
              title:'注册证号',
              align:"center",
              scopedSlots: {customRender: "ellipsisText"},
              dataIndex: 'productRegistration'
            },
            {
              title:'备注',
              align:"center",
              scopedSlots: {customRender: "ellipsisText"},
              dataIndex: 'remarks'
            },
            {
              title:'产品收费代码',
              align:"center",
              dataIndex: 'chargeCode'
            },

          ],
        },
        visible: false,
        initParams:{
          supplierId :"",
        },
        popModal: {
          visible: false,
          width: '100%',
          switchFullscreen: true,  //缩放按钮
          lockScroll: false,
          fullscreen: true,
        },
        url: {
          exportInReportXls: "/pd/pdStockRecordIn/exportInReportXls",
          queryVender:"/pd/pdVender/getVenderList",
          stockList: "/pd/pdStatisticalReport/rpDepartStockDetailReport",
        },
        dictOptions:{

        },
      }
    },
    methods: {
      //初始化字典
      initDictConfig(){
      },
      /** 关闭按钮 **/
      handleCancel(){
        this.$emit('ok');
        this.close();
      },
      //关闭方法
      close() {
        this.stockTable.ipagination.current = 1;
        this.stockTable.ipagination.pageSize = 10;
        this.stockTable.dataSource = [];
        this.visible = false;
        this.stockToggleSearchStatus = false;
        this.$emit('close');
      },
      //初始化
      show(initParams){
        this.visible = true;
        this.initDictConfig();
        this.initParams = initParams;
        this.activeKey = "1";
        this.stockLoadData(1);
      },
      //生产厂家下拉
      getList(value,url){
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
          this.stockVenderData = data;
        })
      },

      //供应商入库明细start
      stockLoadData(arg) {
        if (arg === 1) {
          this.stockTable.ipagination.current = 1;
        }
        let params = this.getStockQueryParams();//查询条件
        this.stockTable.loading = true;
        getAction(this.url.stockList, params).then((res) => {
          if (res.success) {
            this.stockTable.dataSource = res.result.records;
            this.stockTable.ipagination.total = res.result.total;
          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
          this.stockTable.loading = false;
        })
      },
      //获取入库查询条件
      getStockQueryParams() {
        let param = this.stockQueryParam;
        param.pageNo = this.stockTable.ipagination.current;
        param.pageSize = this.stockTable.ipagination.pageSize;
        param.departId = this.initParams.departId;
        param.flag = "";
        delete param.queryExpDate; //范围参数不传递后台，传后台会报错
        return filterObj(param);
      },
      //表格分页事件
      stockHandleTableChange(pagination, filters, sorter) {
        this.stockTable.ipagination = pagination;
        this.stockLoadData();
      },
      //有效期
      stockExpDateChange: function (value, dateString) {
        this.stockQueryParam.queryExpDateStart=dateString[0];
        this.stockQueryParam.queryExpDateEnd=dateString[1];
        // delete this.queryParam.queryExpDate; //范围参数不传递后台，传后台会报错
      },
      //入库明细搜索
      stockSearchQuery() {
        this.stockLoadData();
      },
      //入库明细重置
      stockSearchReset() {
        this.stockQueryParam = {}
        this.stockLoadData();
      },
      stockHandleToggleSearch(){
        this.stockToggleSearchStatus = !this.stockToggleSearchStatus;
      },
      //生产厂家查询start
      stockVenderHandleSearch(value) {
        this.getList(value,this.url.queryVender,"1");
      },
      stockVenderHandleChange(value) {
        this.stockVenderValue = value;
        this.getList(value,this.url.queryVender,"1");
      },
      //生产厂家查询end

      /**重写导出方法**/
      handleExportXls(){
        //入库明细导出
        let inFileName = "入库明细统计报表" + "_" + new Date().toLocaleString();
        let inParam = this.getInQueryParams();//查询条件
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
      },
      //供应商入库明细end
    }
  }
</script>
<style scoped>
</style>