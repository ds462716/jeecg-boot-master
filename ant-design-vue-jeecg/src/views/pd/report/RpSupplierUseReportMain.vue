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
    <a-tabs v-model="activeKey" @change="handleChangeTabs">
      <a-tab-pane tab="入库信息" key="1">
        <a-card :bordered="false">
          <!-- 查询区域 -->
          <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="inSearchQuery">
              <a-row :gutter="24">

                <a-col :md="6" :sm="8">
                  <a-form-item label="出入库单号">
                    <a-input placeholder="请输入单号" v-model="inQueryParam.recordNo"></a-input>
                  </a-form-item>
                </a-col>
                <a-col :md="6" :sm="8">
                  <a-form-item label="产品名称">
                    <a-input placeholder="请选输入品名称" v-model="inQueryParam.productName"></a-input>
                  </a-form-item>
                </a-col>
                <a-col :md="6" :sm="8">
                  <a-form-item label="产品编号">
                    <a-input placeholder="请输入产品编号" v-model="inQueryParam.productNumber"></a-input>
                  </a-form-item>
                </a-col>

                <template v-if="inToggleSearchStatus">
                  <a-col :md="6" :sm="8">
                    <a-form-item label="规格">
                      <a-input placeholder="请输入规格" v-model="inQueryParam.spec"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="8">
                    <a-form-item label="有效期">
                      <a-range-picker @change="inExpDateChange" v-model="inQueryParam.queryExpDate"/>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="8">
                    <a-form-item label="注册证">
                      <a-input placeholder="请输入注册证" v-model="inQueryParam.registration"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="8">
                    <a-form-item label="型号">
                      <a-input placeholder="请输入型号" v-model="inQueryParam.version"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="8">
                    <a-form-item label="批号">
                      <a-input placeholder="请输入批号" v-model="inQueryParam.batchNo"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="8">
                    <a-form-item label="生产厂家">
                      <a-select
                        showSearch
                        :venderId="inVenderValue"
                        placeholder="请选择生产厂家"
                        :defaultActiveFirstOption="false"
                        :allowClear="true"
                        :showArrow="true"
                        :filterOption="false"
                        @search="inVenderHandleSearch"
                        @change="inVenderHandleChange"
                        @focus="inVenderHandleSearch"
                        :notFoundContent="notFoundContent"
                        v-model="inQueryParam.venderId"
                      >
                        <a-select-option v-for="d in inVenderData" :key="d.value">{{d.text}}</a-select-option>
                      </a-select>
                    </a-form-item>
                  </a-col>
                </template>

                <a-col :md="6" :sm="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="inSearchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="inSearchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="inHandleToggleSearch" style="margin-left: 8px">
                {{ inToggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="inToggleSearchStatus ? 'up' : 'down'"/>
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
          <div>
            <a-table
              ref="in_table"
              size="small"
              bordered
              rowKey="id"
              :columns="inTable.columns"
              :dataSource="inTable.dataSource"
              :pagination="inTable.ipagination"
              :loading="inTable.loading"
              :scroll="inTable.tableScroll"
              @change="inHandleTableChange"
            >
              <template slot="htmlSlot" slot-scope="text">
                <div v-html="text"></div>
              </template>
            </a-table>
          </div>
        </a-card>

      </a-tab-pane>
      <a-tab-pane tab="供应商用量明细" key="2">
        <a-card :bordered="false">
          <!-- 查询区域 -->
          <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="useSearchQuery">
              <a-row :gutter="24">
                <a-col :md="6" :sm="8">
                  <a-form-item label="用量单号">
                    <a-input placeholder="请输入用量单号" v-model="useQueryParam.dosageNo"></a-input>
                  </a-form-item>
                </a-col>
                <a-col :md="6" :sm="8">
                  <a-form-item label="产品名称">
                    <a-input placeholder="请选输入品名称" v-model="useQueryParam.productName"></a-input>
                  </a-form-item>
                </a-col>
                <a-col :md="6" :sm="8">
                  <a-form-item label="产品编号">
                    <a-input placeholder="请输入产品编号" v-model="useQueryParam.productNumber"></a-input>
                  </a-form-item>
                </a-col>

                <template v-if="useToggleSearchStatus">
                  <a-col :md="6" :sm="8">
                    <a-form-item label="规格">
                      <a-input placeholder="请输入规格" v-model="useQueryParam.spec"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="8">
                    <a-form-item label="有效期">
                      <a-range-picker @change="useExpDateChange" v-model="useQueryParam.queryExpDate"/>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="8">
                    <a-form-item label="注册证">
                      <a-input placeholder="请输入注册证" v-model="useQueryParam.registration"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="8">
                    <a-form-item label="型号">
                      <a-input placeholder="请输入型号" v-model="useQueryParam.version"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="8">
                    <a-form-item label="批号">
                      <a-input placeholder="请输入批号" v-model="useQueryParam.batchNo"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="8">
                    <a-form-item label="生产厂家">
                      <a-select
                        showSearch
                        :venderId="useVenderValue"
                        placeholder="请选择生产厂家"
                        :defaultActiveFirstOption="false"
                        :allowClear="true"
                        :showArrow="true"
                        :filterOption="false"
                        @search="useVenderHandleSearch"
                        @change="useVenderHandleChange"
                        @focus="useVenderHandleSearch"
                        :notFoundContent="notFoundContent"
                        v-model="useQueryParam.venderId"
                      >
                        <a-select-option v-for="d in useVenderData" :key="d.value">{{d.text}}</a-select-option>
                      </a-select>
                    </a-form-item>
                  </a-col>
                </template>

                <a-col :md="6" :sm="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="useSearchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="useSearchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="useHandleToggleSearch" style="margin-left: 8px">
                {{ useToggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="useToggleSearchStatus ? 'up' : 'down'"/>
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
              ref="use_table"
              size="middle"
              bordered
              rowKey="id"
              :columns="useTable.columns"
              :dataSource="useTable.dataSource"
              :pagination="useTable.ipagination"
              :loading="useTable.loading"
              :scroll="useTable.tableScroll"
              @change="useHandleTableChange">
            </a-table>
          </div>
        </a-card>
      </a-tab-pane>
      <a-tab-pane tab="供应商退货明细" key="3">
        <a-card :bordered="false">
          <!-- 查询区域 -->
          <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="reSearchQuery">
              <a-row :gutter="24">
                <a-col :md="6" :sm="8">
                  <a-form-item label="退货单号">
                    <a-input placeholder="请输入退货单号" v-model="reQueryParam.rejectedNo"></a-input>
                  </a-form-item>
                </a-col>
                <a-col :md="6" :sm="8">
                  <a-form-item label="产品名称">
                    <a-input placeholder="请选输入品名称" v-model="reQueryParam.productName"></a-input>
                  </a-form-item>
                </a-col>
                <a-col :md="6" :sm="8">
                  <a-form-item label="产品编号">
                    <a-input placeholder="请输入产品编号" v-model="reQueryParam.productNumber"></a-input>
                  </a-form-item>
                </a-col>

                <template v-if="reToggleSearchStatus">
                  <a-col :md="6" :sm="8">
                    <a-form-item label="规格">
                      <a-input placeholder="请输入规格" v-model="reQueryParam.spec"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="8">
                    <a-form-item label="有效期">
                      <a-range-picker @change="reExpDateChange" v-model="reQueryParam.queryExpDate"/>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="8">
                    <a-form-item label="注册证">
                      <a-input placeholder="请输入注册证" v-model="reQueryParam.registration"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="8">
                    <a-form-item label="型号">
                      <a-input placeholder="请输入型号" v-model="reQueryParam.version"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="8">
                    <a-form-item label="批号">
                      <a-input placeholder="请输入批号" v-model="reQueryParam.batchNo"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="8">
                    <a-form-item label="生产厂家">
                      <a-select
                        showSearch
                        :venderId="reVenderValue"
                        placeholder="请选择生产厂家"
                        :defaultActiveFirstOption="false"
                        :allowClear="true"
                        :showArrow="true"
                        :filterOption="false"
                        @search="reVenderHandleSearch"
                        @change="reVenderHandleChange"
                        @focus="reVenderHandleSearch"
                        :notFoundContent="notFoundContent"
                        v-model="reQueryParam.venderId"
                      >
                        <a-select-option v-for="d in reVenderData" :key="d.value">{{d.text}}</a-select-option>
                      </a-select>
                    </a-form-item>
                  </a-col>
                </template>

                <a-col :md="6" :sm="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="reSearchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="reSearchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="reHandleToggleSearch" style="margin-left: 8px">
                {{ reToggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="reToggleSearchStatus ? 'up' : 'down'"/>
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
              ref="re_table"
              size="middle"
              bordered
              rowKey="id"
              :columns="reTable.columns"
              :dataSource="reTable.dataSource"
              :pagination="reTable.ipagination"
              :loading="reTable.loading"
              :scroll="reTable.tableScroll"
              @change="reHandleTableChange">
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
    name: "RpSupplierUseReportMain",
    components: {
    },
    data() {
      return {
        description: '订单管理页面',
        inQueryParam: {},
        /* 查询折叠 */
        inToggleSearchStatus:false,
        notFoundContent:"未找到内容",
        inVenderValue: undefined,
        inVenderData: [],
        activeKey:"1",
        // 表头
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
            { title:'入库日期', align:"center", width:'100px', dataIndex: 'auditDate',
              customRender:function (text) {
                return !text?"":(text.length>10?text.substr(0,10):text)
              }
            },
            { title:'供应商', align:"center", width:'250px', dataIndex: 'supplierName' },
            { title:'入库单号', align:"center", width:'100px', dataIndex: 'recordNo'},
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
            { title:'单位', align:"center", width:'60px', dataIndex: 'unitName' },
            { title:'入库单价', align:"center", width:'100px', dataIndex: 'purchasePrice' },
            { title:'入库金额', align:"center", width:'100px', dataIndex: 'inTotalPrice' },
            { title:'生产厂家', align:"center", width:'250px', dataIndex: 'venderName' },
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
          ],
        },
        useQueryParam: {},
        /* 查询折叠 */
        useToggleSearchStatus:false,
        useVenderValue: undefined,
        useVenderData: [],
        // 表头
        useTable: {
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
            { title:'用量日期', align:"center", width:'100px', dataIndex: 'dosageDate',
              customRender:function (text) {
                return !text?"":(text.length>10?text.substr(0,10):text)
              }
            },
            {
              title:'供应商',
              align:"center",
              scopedSlots: {customRender: "ellipsisText"},
              dataIndex: 'supplierName'
            },
            {
              title:'用量单号',
              align:"center",
              dataIndex: 'dosageNo'
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
              title:'用量数量',
              align:"center",
              dataIndex: 'leftRefundNum'
            },
            {
              title:'单位',
              align:"center",
              dataIndex: 'unitName'
            },
            {
              title:'单价',
              align:"center",
              dataIndex: 'sellingPrice'
            },
            {
              title:'金额',
              align:"center",
              dataIndex: 'dosagePrice'
            },
            { title:'生产厂家', align:"center", width:'250px', dataIndex: 'venderName' },
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

        reQueryParam: {},
        /* 查询折叠 */
        reToggleSearchStatus:false,
        reVenderValue: undefined,
        reVenderData: [],
        // 表头
        reTable: {
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
            { title:'退货日期', align:"center", width:'100px', dataIndex: 'rejectedDate',
              customRender:function (text) {
                return !text?"":(text.length>10?text.substr(0,10):text)
              }
            },
            {
              title:'供应商',
              align:"center",
              scopedSlots: {customRender: "ellipsisText"},
              dataIndex: 'supplierName'
            },
            {
              title:'退货单号',
              align:"center",
              dataIndex: 'rejectedNo'
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
              title:'退货数量',
              align:"center",
              dataIndex: 'rejectedCount'
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
              title:'金额',
              align:"center",
              dataIndex: 'rejectedPrice'
            },
            { title:'生产厂家', align:"center", width:'250px', dataIndex: 'venderName' },
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
          yearMonth :""
        },
        popModal: {
          visible: false,
          width: '100%',
          switchFullscreen: true,  //缩放按钮
          lockScroll: false,
          fullscreen: true,
        },
        url: {
          inList: "/pd/pdStatisticalReport/supplierInDetailReport",
          exportInReportXls: "/pd/pdStockRecordIn/exportInReportXls",
          queryVender:"/pd/pdVender/getVenderList",
          useList: "/pd/pdStatisticalReport/rpUseDetailReport",
          reList: "/pd/pdStatisticalReport/rpReDetailReport",

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
        this.inQueryParam = {};
        this.inTable.ipagination.current = 1;
        this.inTable.ipagination.pageSize = 10;
        this.inTable.dataSource = [];
        this.useTable.ipagination.current = 1;
        this.useTable.ipagination.pageSize = 10;
        this.useTable.dataSource = [];
        this.reTable.ipagination.current = 1;
        this.reTable.ipagination.pageSize = 10;
        this.reTable.dataSource = [];
        this.visible = false;
        this.inToggleSearchStatus = false;
        this.$emit('close');
      },
      //初始化
      show(initParams){
        this.visible = true;
        this.initDictConfig();
        this.initParams = initParams;
        this.activeKey = "1";
        this.handleChangeTabs(1)
      },
      //tabs 切换事件
      handleChangeTabs(key){
        // 自动重置scrollTop状态，防止出现白屏
        getRefPromise(this, key).then(editableTable => {
          editableTable.resetScrollTop()
        })
        if(key==1){
          this.inLoadData(1);
        }else if(key==2){
          this.useLoadData(1);
        }else if(key==3){
          this.reLoadData(1);
        }
      },
      //生产厂家下拉
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
            this.inVenderData = data;
          }else if(flag == "2"){
            this.useVenderData = data;
          }else if(flag == "3"){
            this.reVenderData = data;
          }
        })
      },

      //供应商入库明细start
      inLoadData(arg) {
        if (arg === 1) {
          this.inTable.ipagination.current = 1;
        }
        let params = this.getInQueryParams();//查询条件
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
      //获取入库查询条件
      getInQueryParams() {
        let param = this.inQueryParam;
        param.pageNo = this.inTable.ipagination.current;
        param.pageSize = this.inTable.ipagination.pageSize;
        param.supplierId = this.initParams.supplierId;
        param.yearMonth = this.initParams.yearMonth;
        param.productFlag = 0;//只查产品
        delete param.queryExpDate; //范围参数不传递后台，传后台会报错
        return filterObj(param);
      },
      //表格分页事件
      inHandleTableChange(pagination, filters, sorter) {
        this.inTable.ipagination = pagination;
        this.inLoadData();
      },
      //有效期
      inExpDateChange: function (value, dateString) {
        this.inQueryParam.queryExpDateStart=dateString[0];
        this.inQueryParam.queryExpDateEnd=dateString[1];
        // delete this.queryParam.queryExpDate; //范围参数不传递后台，传后台会报错
      },
      //入库明细搜索
      inSearchQuery() {
        this.inLoadData();
      },
      //入库明细重置
      inSearchReset() {
        this.inQueryParam = {}
        this.inLoadData();
      },
      inHandleToggleSearch(){
        this.inToggleSearchStatus = !this.inToggleSearchStatus;
      },
      //生产厂家查询start
      inVenderHandleSearch(value) {
        this.getList(value,this.url.queryVender,"1");
      },
      inVenderHandleChange(value) {
        this.inVenderValue = value;
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

      //供应商使用明细start
      useLoadData(arg) {
        if (arg === 1) {
          this.useTable.ipagination.current = 1;
        }
        let params = this.getUseQueryParams();//查询条件
        this.useTable.loading = true;
        getAction(this.url.useList, params).then((res) => {
          if (res.success) {
            this.useTable.dataSource = res.result.records;
            this.useTable.ipagination.total = res.result.total;
          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
          this.useTable.loading = false;
        })
      },
      //获取使用条件
      getUseQueryParams() {
        //获取查询条件
        let param = this.useQueryParam;
        param.pageNo = this.useTable.ipagination.current;
        param.pageSize = this.useTable.ipagination.pageSize;
        param.supplierId = this.initParams.supplierId;
        param.yearMonth = this.initParams.yearMonth;
        delete param.queryExpDate; //范围参数不传递后台，传后台会报错
        return filterObj(param);
      },
      //表格分页事件
      useHandleTableChange(pagination, filters, sorter) {
        this.useTable.ipagination = pagination;
        this.useLoadData();
      },
      //有效期
      useExpDateChange: function (value, dateString) {
        this.useQueryParam.queryExpDateStart=dateString[0];
        this.useQueryParam.queryExpDateEnd=dateString[1];
        // delete this.queryParam.queryExpDate; //范围参数不传递后台，传后台会报错
      },
      //入库明细搜索
      useSearchQuery() {
        this.useLoadData();
      },
      //入库明细重置
      useSearchReset() {
        this.useQueryParam = {}
        this.useLoadData();
      },
      useHandleToggleSearch(){
        this.useToggleSearchStatus = !this.useToggleSearchStatus;
      },
      //生产厂家查询start
      useVenderHandleSearch(value) {
        this.getList(value,this.url.queryVender,"2");
      },
      useVenderHandleChange(value) {
        this.useVenderValue = value;
        this.getList(value,this.url.queryVender,"2");
      },
      //生产厂家查询end
      //供应商使用明细end

      //供应商退货明细start
      reLoadData(arg) {
        if (arg === 1) {
          this.reTable.ipagination.current = 1;
        }
        let params = this.getReQueryParams();//查询条件
        this.reTable.loading = true;
        getAction(this.url.reList, params).then((res) => {
          if (res.success) {
            this.reTable.dataSource = res.result.records;
            this.reTable.ipagination.total = res.result.total;
          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
          this.reTable.loading = false;
        })
      },
      //获取使用条件
      getReQueryParams() {
        //获取查询条件
        let param = this.reQueryParam;
        param.pageNo = this.reTable.ipagination.current;
        param.pageSize = this.reTable.ipagination.pageSize;
        param.supplierId = this.initParams.supplierId;
        param.yearMonth = this.initParams.yearMonth;
        param.productFlag = 0;//只查产品
        delete param.queryExpDate; //范围参数不传递后台，传后台会报错
        return filterObj(param);
      },
      //表格分页事件
      reHandleTableChange(pagination, filters, sorter) {
        this.reTable.ipagination = pagination;
        this.reLoadData();
      },
      //有效期
      reExpDateChange: function (value, dateString) {
        this.reQueryParam.queryExpDateStart=dateString[0];
        this.reQueryParam.queryExpDateEnd=dateString[1];
        // delete this.queryParam.queryExpDate; //范围参数不传递后台，传后台会报错
      },
      //入库明细搜索
      reSearchQuery() {
        this.reLoadData();
      },
      //入库明细重置
      reSearchReset() {
        this.reQueryParam = {}
        this.reLoadData();
      },
      reHandleToggleSearch(){
        this.reToggleSearchStatus = !this.reToggleSearchStatus;
      },
      //生产厂家查询start
      reVenderHandleSearch(value) {
        this.getList(value,this.url.queryVender,"3");
      },
      reVenderHandleChange(value) {
        this.reVenderValue = value;
        this.getList(value,this.url.queryVender,"3");
      },
      //生产厂家查询end
      //供应商使用明细end
    }
  }
</script>
<style scoped>
</style>