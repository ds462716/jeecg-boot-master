<template>
  <j-modal
    :visible="visible"
    :width="popModal.width"
    :maskClosable="disableSubmit"
    :title="popModal.title"
    :lockScroll="popModal.lockScroll"
    :fullscreen="popModal.fullscreen"
    :switchFullscreen="popModal.switchFullscreen"
    @cancel="handleCancel"
  >

    <a-spin :spinning="confirmLoading">
      <!-- 主表单区域 -->
      <div style="background:#ECECEC; padding:20px">
        <a-card title="" style="margin-bottom: 10px;">
          <a-form :form="form">
            <a-row>
              <a-col :span="6">
                <a-form-item label="初始化单号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input disabled v-decorator="[ 'recordNo', validatorRules.recordNo]" placeholder="请输入单号"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="初始化日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <j-date disabled placeholder="请选择日期" v-decorator="[ 'submitDate', validatorRules.submitDate]" :trigger-change="true" style="width: 100%"/>
                </a-form-item>
              </a-col>
              <a-col :span="6" v-show="false">
                <a-form-item label="入库类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <j-dict-select-tag-expand disabled type="list" v-decorator="['inType', validatorRules.inType]" :trigger-change="true" dictCode="in_type" placeholder="请选择入库类型"/>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="操作人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input disabled v-decorator="[ 'submitByName', validatorRules.submitByName]" placeholder="请输入操作人"></a-input>
                </a-form-item>
                <a-form-item v-show="false" label="操作人ID">
                  <a-input v-show="false" v-decorator="[ 'submitBy', {}]"></a-input>
                </a-form-item>
                <a-form-item v-show="false" label="入库部门ID">
                  <a-input v-show="false" v-decorator="[ 'inDepartId', {}]" ></a-input>
                </a-form-item>
                <!--<a-form-item v-show="false" label="采购订单号">-->
                  <!--<a-input v-show="false" v-decorator="[ 'orderNo', {}]" ></a-input>-->
                <!--</a-form-item>-->
                <a-form-item v-show="false" label="合并采购订单号">
                  <a-input v-show="false" v-decorator="[ 'mergeOrderNo', {}]" ></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="供应商" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <!-- :disabled="disableSubmit" -->
                  <a-select
                    showSearch
                    disabled
                    placeholder="请选择供应商"
                    :supplierId="supplierValue"
                    :defaultActiveFirstOption="false"
                    :showArrow="true"
                    :filterOption="false"
                    @search="supplierHandleSearch"
                    @change="supplierHandleChange"
                    @focus="supplierHandleSearch"
                    :notFoundContent="notFoundContent"
                    v-decorator="[ 'supplierId', validatorRules.supplierId]"
                  >
                    <a-select-option v-for="d in supplierData" :key="d.value" :text="d.text" >{{d.text}}</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :span="6" v-show="false">
                <a-form-item label="业态" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <j-dict-select-tag-expand :disabled="disableSubmit" type="list" v-decorator="['format', validatorRules.format]" :trigger-change="true" dictCode="format" placeholder="请选择入库类型"/>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>

          <div class="table-operator" v-show="false">
            <!-- 订单明细表区域 -->
            <a-tabs>
              <a-tab-pane tab="订单明细" :forceRender="true">
                <a-button @click="choosePurchaseOrder" type="primary" icon="import" style="margin-bottom: 8px;" v-show="!disableSubmit">从订单导入</a-button>
                <a-table
                  v-show="showOrderTable"
                  ref="table"
                  size="middle"
                  bordered
                  rowKey="id"
                  :pagination="false"
                  :columns="pdPurchaseOrderDetailTable.columns"
                  :dataSource="pdPurchaseOrderDetailTable.dataSource"
                  :loading="pdPurchaseOrderDetailTable.loading" >
                  <template slot="htmlSlot" slot-scope="text">
                    <div v-html="text"></div>
                  </template>
                </a-table>
              </a-tab-pane>
            </a-tabs>
          </div>
        </a-card>

        <!-- 产品列表区域 -->
        <a-card style="margin-bottom: 10px;">
          <a-tabs v-model="activeKey" @change="handleChangeTabs"> <!-- @change="handleChangeTabs"  v-show="!showSBarcode" v-show="showSBarcode"-->
            <a-tab-pane tab="产品明细" :key="refKeys[0]" :forceRender="true">
              <a-form v-show="!disableSubmit">
                <a-row v-if="!showSBarcode">
                  <a-col :md="6" :sm="8">
                    <a-form-item label="产品编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <a-input ref="productNumberInput" v-focus placeholder="请输入产品编号" v-model="queryParam.productNumber" @keyup.enter.native="onlyNumbersearchQuery"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :md="12" :sm="8">
                    <a-form-item label="" :labelCol="labelCol" :wrapperCol="wrapperCol" style="text-align: left;padding-left: 15px;">
                      提示：按<span style="color: red">Ctrl+Alt</span>键快速定位到扫码输入框
                    </a-form-item>
                  </a-col>
                </a-row>
                <a-row v-if="showSBarcode">
                  <a-col :md="6" :sm="8">
                    <a-form-item label="产品编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <a-input ref="productNumberInput" v-focus placeholder="请输入产品编号" v-model="queryParam.productNumber" @keyup.enter.native="searchQuery(0)"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="8">
                    <a-form-item label="二级条码" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <a-input ref="productBarCodeInput" placeholder="请输入二级条码" v-model="queryParam.productBarCode" @keyup.enter.native="searchQuery(1)"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :md="12" :sm="8">
                    <a-form-item label="" :labelCol="labelCol" :wrapperCol="wrapperCol" style="text-align: left;padding-left: 15px;">
                      提示：按<span style="color: red">Ctrl+Alt</span>键快速定位到扫码输入框
                    </a-form-item>
                  </a-col>
                </a-row>
              </a-form>

              <div style="margin-bottom: 8px;" v-show="!disableSubmit">
                <a-button type="primary" icon="plus" @click="chooseProductList">选择产品</a-button>
                <span style="padding-left: 8px;"></span>
                <a-popconfirm
                  :title="`确定要删除吗?`"
                  @confirm="handleConfirmDelete">
                  <a-button type="primary" icon="minus">删除</a-button>
                  <span class="gap"></span>
                </a-popconfirm>
              </div>

              <j-editable-table
                bordered
                :ref="refKeys[0]"
                :loading="pdStockRecordDetailTable.loading"
                :columns="pdStockRecordDetailTable.columns"
                :dataSource="pdStockRecordDetailTable.dataSource"
                :maxHeight="650"
                :rowNumber="true"
                :rowSelection="true"
                :actionButton="false"
                :customRow="onClickRow"
                :disabled="disableSubmit"
                @valueChange="valueChange"
                @added="setPriceDisabled"
                style="text-overflow: ellipsis;"
              >
              </j-editable-table>
              <a-row style="margin-top:10px;text-align: right;padding-right: 5%">
                  <span style="font-weight: bold;font-size: large;padding-right: 5%">总数量：{{ totalSum }}</span>
                  <span style="font-weight: bold;font-size: large">总金额：{{ inTotalPrice }}</span>
              </a-row>
            </a-tab-pane>

            <a-tab-pane tab="唯一码明细" :forceRender="true" v-if="showUniqueTab">
              <a-table
                size="middle"
                bordered
                rowKey="id"
                :pagination="ipagination"
                :columns="pdStockRecordDetailUnique.columns"
                :dataSource="pdStockRecordDetailUnique.dataSource"
                :loading="pdStockRecordDetailUnique.loading"
                @change="handleTableChange">
                <template slot="htmlSlot" slot-scope="text">
                  <div v-html="text"></div>
                </template>
              </a-table>
            </a-tab-pane>
          </a-tabs>
        </a-card>

        <!-- 验收区域 -->
        <a-card style="">
          <a-form :form="form">
            <a-row v-show="false">
              <a-col :span="6">
                <a-form-item label="验收结果" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <j-dict-select-tag :disabled="disableSubmit" v-decorator="[ 'testResult', validatorRules.testResult]" placeholder="请选择验收结果" :type="'radio'" :triggerChange="true" dictCode="test_result"/>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="储运状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <j-dict-select-tag :disabled="disableSubmit" v-decorator="[ 'storageResult', validatorRules.storageResult]" placeholder="请选择储运状态" :type="'radio'" :triggerChange="true" dictCode="storage_result"/>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row v-show="false">
              <a-col :span="6">
                <a-form-item label="温度（℃）" :labelCol="labelCol3" :wrapperCol="wrapperCol3">
                 <a-input :disabled="disableSubmit" v-decorator="[ 'temperature', validatorRules.temperature]" placeholder=""></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="湿度（%）" :labelCol="labelCol3" :wrapperCol="wrapperCol3">
                  <a-input :disabled="disableSubmit" v-decorator="[ 'humidity', validatorRules.humidity]" placeholder=""></a-input>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item label="备注" :labelCol="labelCol2" :wrapperCol="wrapperCol2" style="text-align: left">
                  <a-textarea :disabled="disableSubmit" v-decorator="[ 'remarks', validatorRules.remarks]" placeholder="请输入备注"></a-textarea>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-card>

        <a-card style="margin-top: 10px;" v-show="showRefuseReason">
          <a-form :form="form">
            <a-col :span="12">
              <a-form-item label="审批意见" :labelCol="labelCol2" :wrapperCol="wrapperCol2" style="text-align: left">
                <a-textarea disabled v-decorator="[ 'refuseReason', validatorRules.refuseReason]" placeholder="请输入审批意见"></a-textarea>
              </a-form-item>
            </a-col>
          </a-form>
        </a-card>
      </div>
    </a-spin>


    <template slot="footer">
      <a-button @click="closeBtn" style="margin-right: 15px;" v-show="disableSubmit">关  闭</a-button>
      <a-popconfirm title="确定放弃编辑？" @confirm="handleCancel" v-show="!disableSubmit" okText="确定" cancelText="取消">
        <a-button style="margin-right: 15px;">取  消</a-button>
      </a-popconfirm>
      <a-popconfirm title="确定撤回？" @confirm="cancelBtn" v-show="showCancelBtn" okText="确定" cancelText="取消">
        <a-button style="margin-right: 15px;" :loading="confirmLoading" type="danger">撤  回</a-button>
      </a-popconfirm>
      <a-button @click="printBtn('1')" style="margin-right: 15px;" type="primary" v-show="showPrintBtn">打  印</a-button>
      <a-button @click="saveBtn" v-show="!disableSubmit" type="primary" :loading="confirmLoading" style="margin-right: 15px;">保存草稿</a-button>
      <a-button @click="submitBtn('1')" v-show="!disableSubmit" type="primary" :loading="confirmLoading" style="margin-right: 15px;">提  交</a-button>
      <!--<a-button @click="submitBtn('2')" v-show="showSubmitAndPrint" type="primary" :loading="confirmLoading" style="margin-right: 15px;">提交并打印</a-button>-->
      <a-button @click="submitBtn('2')" v-show="!disableSubmit" type="primary" :loading="confirmLoading" style="margin-right: 15px;">提交并打印</a-button>
    </template>

    <pd-choose-purchase-order-list-model  ref="pdChoosePurchaseOrderListModel" @ok="returnPurchaseOrderData" ></pd-choose-purchase-order-list-model>
    <pd-choose-product-list-model  ref="pdChooseProductListModel" @ok="returnProductData" ></pd-choose-product-list-model>
    <pd-stock-record-in-print-modal ref="pdStockRecordInPrintModal" ></pd-stock-record-in-print-modal>
    <ex-stock-record-in-print-modal ref="exStockRecordInPrintModal" ></ex-stock-record-in-print-modal>
  </j-modal>
</template>

<script>

  import Vue from 'vue'
  import pick from 'lodash.pick'
  import { FormTypes,getRefPromise,validateFormAndTables } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import JDate from '@/components/jeecg/JDate'
  import {httpAction, deleteAction, getAction} from '@/api/manage'
  import PdChoosePurchaseOrderListModel from "./PdChoosePurchaseOrderListModel";
  import PdChooseProductListModel from "./PdChooseProductListModel";
  import JDictSelectTagExpand from "@/components/dict/JDictSelectTagExpand"
  import ATextarea from "ant-design-vue/es/input/TextArea";
  import {scanCode} from '@/utils/barcode'
  import PdStockRecordInPrintModal from "../print/PdStockRecordInPrintModal";
  import ExStockRecordInPrintModal from "../../external/print/ExStockRecordInPrintModal";


  const VALIDATE_NO_PASSED = Symbol()
  export { FormTypes, VALIDATE_NO_PASSED }

  // 自定义焦点事件
  Vue.directive('focus', {
    // 当被绑定的元素插入到 DOM 中时……
    inserted: function (el) {
      //全局监听键盘事件
      document.onkeydown = function(event) {
        if(event.ctrlKey && event.altKey) {
          // 按ctrl+alt  聚焦元素
          el.focus()
        }
      }
    }
  })

  export default {
    name: 'PdProductStockInitModal',
    mixins: [JEditableTableMixin],
    components: {
      ExStockRecordInPrintModal,
      PdStockRecordInPrintModal,
      ATextarea,
      PdChooseProductListModel,
      PdChoosePurchaseOrderListModel,
      JDate,
      JDictSelectTagExpand
    },
    data() {
      return {
        labelCol: {span: 6},
        wrapperCol: {span: 16},

        labelCol2: {span: 3},
        wrapperCol2: {span: 20},

        labelCol3: {span: 6},
        wrapperCol3: {span: 3},

        labelCol4: {span: 13},
        wrapperCol4: {span: 5},
        disableSubmit:false,
        showCancelBtn:false,
        showPrintBtn:false,
        showRefuseReason:false,
        showSubmitAndPrint:false,

        initData:{},
        queryParam:{},
        allowInMoreOrder:"",		   //开关-是否允许入库量大于订单量    1-允许；0-不允许
        allowNotOrderProduct:"",	 //开关-是否允许入库非订单产品     1-允许；0-不允
        allowSupplier:"",          //开关-是否允许入库非本供应商产品   1-允许；0不允许
        allowEditPrice:"",         //开关-是否允许出入库时可修改进价和出价   1-允许；0不允许
        allowStockInExpProduct:"", //开关-是否允许入库证照过期的产品   1-允许；0不允许
        allowStockInExpSupplier:"",//开关-是否允许入库证照过期的供应商   1-允许；0不允许
        showSBarcode:false,           //开关-是否显示二级条码框（入库、出库、退货） 1-显示；0-不显示
        inDepartName:"",
        supplierName:"",
        //供应商下拉列表 start
        supplierValue: undefined,
        notFoundContent:"未找到内容",
        supplierData: [],
        //供应商下拉列表 end
        showOrderTable:false,
        showUniqueTab:false,
        // orderNo:"",
        mergeOrderNo:"",
        totalSum:'0',
        inTotalPrice:'0.0000',
        submitDateStr:"",
        stockInText:"",
        //货区货位二级联动下拉框
        goodsAllocationList:[],
        huoquOptions:[],
        huoweiOptions:[],
        hospitalCode:"",

        //分页参数
        ipagination:{
          current: 1,
          pageSize: 10,
          pageSizeOptions: ['10', '20', '30'],
          showTotal: (total, range) => {
            return range[0] + "-" + range[1] + " 共" + total + "条"
          },
          showQuickJumper: true,
          showSizeChanger: true,
          total: 0
        },

        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 0,
        validatorRules: {
          recordNo:{},
          recordType:{},
          inType:{},
          format:{},
          // orderNo:{},
          mergeOrderNo:{},
          remarks:{},
          isAllowProduct:{rules: [{required: true, message: '请选择!'}]},
          isAllowNum:{rules: [{required: true, message: '请选择!'}]},
          testResult:{rules: [{required: true, message: '请选择验收结果!'}]},
          storageResult:{rules: [{required: true, message: '请选择储运状态!'}]},
          temperature:{rules: [{required: true, message: '请输入温度!'},{pattern: '^-?\\d+\\.?\\d*$$',message: '只能输入数字' }]},
          humidity:{rules: [{required: true, message: '请输入湿度!'},{pattern: '^-?\\d+\\.?\\d*$',message: '只能输入数字' }]},
          outDepartId:{},
          inDepartId:{},
          supplierId:{rules: [{required: true, message: '请选择供应商!'}]},
        },
        refKeys: ['pdStockRecordDetail',],
        tableKeys:['pdStockRecordDetail',],
        activeKey: 'pdStockRecordDetail',
        // 申购订单明细表
        pdPurchaseOrderDetailTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '产品ID', align:"center", dataIndex: 'productId',
              colSpan: 0,
              customRender: (value, row, index) => {
                const obj = {
                  attrs: {colSpan:0},
                };
                return obj;
              },
            },
            // { title: '申购单号', align:"center", dataIndex: 'orderNo' },
            { title: '合并申购单号', align:"center", dataIndex: 'mergeOrderNo' },
            { title: '产品编码', align:"center", dataIndex: 'number' },
            { title: '产品名称', align:"center", dataIndex: 'productName' },
            { title: '规格', align:"center", dataIndex: 'spec' },
            { title: '型号', align:"center", dataIndex: 'version' },
            { title: '数量', align:"center", dataIndex: 'orderNum' },
            { title:'到货数量', align:"center", dataIndex: 'arrivalNum' },
            { title: '单位', align:"center", dataIndex: 'unitName' },
            { title: '生产厂家', align:"center", dataIndex: 'venderName' },
            { title: '供应商', align:"center", dataIndex: 'supplierName' },
          ],
        },
        // 出入库明细表(产品明细)
        pdStockRecordDetailTable: {
          loading: false,
          dataSource: [],
          columns: [
            { title: '产品ID', key: 'productId', type: FormTypes.hidden },
            { title: '产品名称', key: 'productName', type: FormTypes.normal,width:"250px" },
            { title: '产品编号', key: 'productNumber', width:"180px" },
            { title: '产品条码', key: 'productBarCode', type: FormTypes.input, disabled:true, width:"200px" },
            { title: '规格', key: 'spec', width:"150px" },
            // { title: '型号', key: 'version', width:"150px"  disabled },
            { title: '单位', key: 'unitName', width:"50px" },
            {
              title: '生产日期', key: 'produceDate', type: FormTypes.date, width:"130px",
              placeholder: '${title}', defaultValue: '',
            },
            {
              title: '有效期', key: 'expDate', type: FormTypes.date, width:"130px",
              placeholder: '${title}', defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' }]
            },
            {
              title: '批号', key: 'batchNo', width:"120px", type: FormTypes.input,
              placeholder: '${title}', defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' }]
            },
            { title: '入库单价', key: 'purchasePrice', type: FormTypes.input, disabled:true, width:"80px" },
            {
              title: '数量', key: 'productNum', type: FormTypes.input, width:"80px",
              placeholder: '${title}', defaultValue: '1',
              validateRules: [{ required: true, message: '${title}不能为空' },{ pattern: '^-?\\d+\\.?\\d*$',message: '${title}的格式不正确' }]
            },
            { title: '金额', key: 'inTotalPrice', type: FormTypes.input, disabled:true, width:"100px" },
            { title: '货位', key: 'inHuoweiCode', type: FormTypes.select, width:"150px", options: [],allowSearch:true, placeholder: '${title}' },
            // { title: '申购单号', key: 'orderNo', },
            { title: '合并申购单号', key: 'mergeOrderNo', type: FormTypes.hidden, disabled:true, width:"180px" },
            { title: '生产厂家', key: 'venderName', type: FormTypes.hidden },
            { title: '紧急产品-0是1不是', key: 'isUrgent', type: FormTypes.hidden },
            { title: '紧急产品需要采购数量', key: 'upQuantity', type: FormTypes.hidden },
            { title: '紧急产品已采购数量', key: 'purchasedQuantity', type: FormTypes.hidden },
            { title: '规格单位ID', key: 'specUnitId', type: FormTypes.hidden },
            { title: '规格数量', key: 'specQuantity', type: FormTypes.hidden },
            { title: '注册证号', key: 'registration', type: FormTypes.hidden },
          ]
        },
        pdStockRecordDetailUnique: {
          loading: false,
          dataSource: [],
          columns: [
            { title: '唯一码', align:"center", dataIndex: 'refBarCode' },
            { title: '产品名称', align:"center", dataIndex: 'productName' },
            { title: '产品编码', align:"center", dataIndex: 'productNumber' },
            { title: '产品条码', align:"center", dataIndex: 'productBarCode' },
            { title: '规格', align:"center", dataIndex: 'spec' },
            { title: '单位', align:"center", dataIndex: 'unitName' },
            { title: '生产日期', align:"center", dataIndex: 'produceDate' },
            { title:'有效期', align:"center", dataIndex: 'expDate' },
            { title: '批号', align:"center", dataIndex: 'batchNo' },
            { title: '入库单价', align:"center", dataIndex: 'purchasePrice' },
            { title: '数量', align:"center", dataIndex: 'productNum' },
            // { title: '金额', align:"center", dataIndex: 'inTotalPrice' },
            // { title: '货位', align:"center", dataIndex: 'inHuoweiCode' },
            // { title: '合并申购单号', align:"center", dataIndex: 'mergeOrderNo' },
          ],
        },
        url: {
          init:"/pd/pdProductStockInit/initModal",
          getOnOff:"/pd/pdProductStockInit/getOnOff",
          submit: "/pd/pdProductStockInit/submit",
          add: "/pd/pdProductStockInit/add",
          queryById: "/pd/pdProductStockInit/queryById",
          cancel: "/pd/pdProductStockInit/cancel",
          querySupplier:"/pd/pdSupplier/getSupplierList",
          querySupplierById:"/pd/pdSupplier/queryById",
          pdStockRecordDetail: {
            list: "/pd/pdProductStockInit/queryPdProductStockInitDetailByMainId",
            uniqueList: "/pd/pdProductStockInit/queryUniqueDetailPageList"
          },
          pdPurchaseDetail: {
            list: "/pd/pdPurchaseOrder/queryPdPurchaseDetail"
          },
        },
        popModal: {
          title: '这里是标题',
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
    methods: {
      // 重写close方法
      close() {
        this.visible = false;
        this.showOrderTable = false;
        this.pdPurchaseOrderDetailTable.dataSource = [];
        this.queryParam = {};
        // this.orderNo = "";
        // this.form.setFieldsValue({orderNo:""});
        this.mergeOrderNo = "";
        this.totalSum = "";
        this.inTotalPrice = "";
        this.form.setFieldsValue({mergeOrderNo:""});
        this.eachAllTable((item) => {
          item.initialize()
        })
        this.$emit('close')
      },
      getAllTable() {
        let values = this.tableKeys.map(key => getRefPromise(this, key))
        return Promise.all(values)
      },
      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
        this.loadData();
      },
      loadData() {
        // this.loading = true;
        this.showCancelBtn = false;
        this.showPrintBtn = false;
        this.showRefuseReason = false;
        this.showSubmitAndPrint = false;
        //初始化供应商，用于回显供应商
        this.supplierHandleSearch();

        let params = {};
        if(this.model.id){
          if(this.model.auditStatus == "1" && this.model.submitStatus == "2"){
            this.showCancelBtn = true;
          }
          // if(this.model.auditStatus == "2"){ // 审核完可打印
          if(this.model.submitStatus == "2"){ // 提交完可打印
            this.showPrintBtn = true;
          }
          if(this.model.auditStatus == "2" || this.model.auditStatus == "3"){
            this.showRefuseReason = true;
          }

          this.popModal.title="入库明细";
          let fieldval = pick(this.model,'recordNo','mergeOrderNo','inType','submitBy','submitByName','submitDate','remarks','inDepartId','supplierId',
            'testResult','storageResult','temperature','humidity','remarks','refuseReason','format')
          this.$nextTick(() => {
            this.form.setFieldsValue(fieldval);
          })
          params = { id: this.model.id };
          this.showUniqueTab = true;
        }else{
          this.popModal.title="新增入库";
          params = { id: "" };
          this.showUniqueTab = false;
        }
        this.pdStockRecordDetailTable.loading = true;
        getAction(this.url.init, params).then((res) => {
          if (res.success) {
            this.$nextTick(() => {
              if(this.model.id){
                this.pdPurchaseOrderDetailTable.dataSource = res.result.pdPurchaseOrderMergeDetail || [];
                this.pdStockRecordDetailTable.dataSource = res.result.pdStockRecordDetailList || [];
                this.pdStockRecordDetailUnique.dataSource = res.result.pdStockRecordDetailUniqueList || [];
                this.totalSum = res.result.totalSum;
                this.inTotalPrice = res.result.inTotalPrice.toString();
                this.showOrderTable = false;
                if(this.pdPurchaseOrderDetailTable.dataSource.length > 0){
                  this.showOrderTable = true;
                }
              }else{
                this.initData = res.result;
                this.initData.isAllowProduct = "0";
                this.initData.isAllowNum = "0";
                this.initData.testResult = "0";
                this.initData.storageResult = "0";
                this.initData.temperature = "25";
                this.initData.humidity = "50";
                this.submitDateStr = res.result.submitDateStr;
                this.inDepartName = res.result.inDepartName;
                let fieldval = pick(this.initData,'recordNo','mergeOrderNo','inType','submitBy','submitByName','submitDate','remarks','inDepartId','supplierId',
                  'testResult','storageResult','temperature','humidity','remarks','refuseReason','format');
                this.form.setFieldsValue(fieldval);
                // //获取光标
                this.$refs['productNumberInput'].focus();
              }

              // this.stockInText = res.result.stockInText;
              // this.allowInMoreOrder = res.result.allowInMoreOrder;
              // this.allowNotOrderProduct = res.result.allowNotOrderProduct;
              // this.allowSupplier = res.result.allowSupplier;
              // this.allowEditPrice = res.result.allowEditPrice;
              // this.allowStockInExpProduct = res.result.allowStockInExpProduct;
              // this.allowStockInExpSupplier = res.result.allowStockInExpSupplier;
              // if(this.disableSubmit){
              //   this.allowEditPrice = "0";
              // }
              this.goodsAllocationList = res.result.goodsAllocationList;
              this.hospitalCode = res.result.hospitalCode;
              //开关-是否需要入库审批  1-是；0-否
              // if(res.result.allowStockInAudit == "0" && this.disableSubmit == false){
              //   this.showSubmitAndPrint = true;
              // }

              this.pdStockRecordDetailTable.columns.forEach((item, idx) => {
                if(item.key == "inHuoweiCode"){
                  item.options = this.goodsAllocationList;
                }
              })
            })
          }
          if(res.code==510){
            this.$message.warning(res.message)
          }
          // this.loading = false;
          this.pdStockRecordDetailTable.loading = false;
        })
        //开关
        getAction(this.url.getOnOff, params).then((res) => {
          if (res.success) {
            this.$nextTick(() => {

              this.stockInText = res.result.stockInText;
              this.allowInMoreOrder = res.result.allowInMoreOrder;
              this.allowNotOrderProduct = res.result.allowNotOrderProduct;
              this.allowSupplier = res.result.allowSupplier;
              this.allowEditPrice = res.result.allowEditPrice;
              this.allowStockInExpProduct = res.result.allowStockInExpProduct;
              this.allowStockInExpSupplier = res.result.allowStockInExpSupplier;
              if(this.disableSubmit){
                this.allowEditPrice = "0";
              }
              //开关-是否需要入库审批  1-是；0-否
              if(res.result.allowStockInAudit == "0" && this.disableSubmit == false){
                this.showSubmitAndPrint = true;
              }
              //开关-是否显示二级条码框（入库、出库、退货） 1-显示；0-不显示
              if(res.result.showSBarcode && res.result.showSBarcode == "0"){
                this.showSBarcode = false;
              }else{
                this.showSBarcode = true;
              }
            })
          }
          if(res.code==510){
            this.$message.warning(res.message)
          }
        })
      },
      loadUniqueData(pageNo){
        var params = {};//查询条件
        params.id = this.model.id;
        if(pageNo == 1){
          this.ipagination.current = 1;
        }
        params.pageNo = this.ipagination.current;
        params.pageSize = this.ipagination.pageSize;

        this.pdStockRecordDetailUnique.loading = true;
        getAction(this.url.pdStockRecordDetail.uniqueList, params).then((res) => {
          if (res.success) {
            this.pdStockRecordDetailUnique.dataSource = res.result.records;
            this.ipagination.total = res.result.total;
          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
          this.pdStockRecordDetailUnique.loading = false;
        })

      },
      handleChangeTabs(){
        if(this.pdStockRecordDetailUnique.dataSource.length <= 0){
          this.loadUniqueData(1);
        }
      },
      handleTableChange(pagination, filters, sorter) {
        //分页、排序、筛选变化时触发
        //TODO 筛选
        if (Object.keys(sorter).length > 0) {
          this.isorter.column = sorter.field;
          this.isorter.order = "ascend" == sorter.order ? "asc" : "desc"
        }
        this.ipagination = pagination;
        this.loadUniqueData();
      },
      /** 关闭按钮 **/
      closeBtn(){
        this.$emit('ok');
        this.close();
      },
      /**打印按钮**/
      printBtn(flag){
        let recordId = this.model.id;
        if(!recordId){
          this.$message.warning("参数不正确，请重新打印！");
          return;
        }
        getAction(this.url.queryById, {id:this.model.id}).then((res) => {

          if(!res.result.auditDate){
            res.result.auditDate = res.result.submitDate;
          }
          if(this.hospitalCode == "FCZYY" || this.hospitalCode == "FCRMYY"){
            this.$refs.pdStockRecordInPrintModal.show(res.result);
            this.$refs.pdStockRecordInPrintModal.title = this.stockInText + "初始化库存";
          }else if(this.hospitalCode == "GZSLYY"){
            this.$refs.exStockRecordInPrintModal.show(res.result);
            this.$refs.exStockRecordInPrintModal.title = this.stockInText + "初始化库存";
          }else{
            this.$refs.pdStockRecordInPrintModal.show(res.result);
            this.$refs.pdStockRecordInPrintModal.title = this.stockInText + "初始化库存";
          }
        })
      },
      /** 关闭按钮点击事件 */
      handleCancel() {
        this.$emit('ok');
        this.close();
      },
      /**撤回**/
      cancelBtn(){
        if(this.model.auditStatus == "1" && this.model.submitStatus == "2"){
          this.confirmLoading = true
          httpAction(this.url.cancel, {id:this.model.id}, 'put').then((res) => {
            if (res.success) {
              this.$message.success(res.message)
              this.$emit('ok')
              this.close()
            } else {
              this.$message.warning(res.message)
            }
          }).finally(() => {
            this.confirmLoading = false
          })
        }else{
          this.$message.warning("当前入库单状态非已提交、待审核状态，不能撤回！")
        }
      },
      /** 保存草稿 **/
      saveBtn() {
        this.request(this.url.add,"post","");
      },
      /** 提交 **/
      submitBtn(flag) {
        this.request(this.url.submit,"post",flag);
      },
      /** 请求 */
      request(url, method,flag) {
        /** 触发表单验证 */
        this.getAllTable().then(tables => {
          /** 一次性验证主表和所有的次表 */
          return validateFormAndTables(this.form, tables)
        }).then(allValues => {
          if (typeof this.classifyIntoFormData !== 'function') {
            throw this.throwNotFunction('classifyIntoFormData')
          }
          let formData = this.classifyIntoFormData(allValues);
          if(formData.pdStockRecordDetailList.length <= 0){
            this.$message.warning("入库产品数据为空，请扫码入库或选择产品");
            return;
          }

          let isexp = false, iszero = false, isproduce = false, bool = false;
          let name = "", name1 = "", name2 = "";

          let { values } = this.$refs.pdStockRecordDetail.getValuesSync({ validate: false });
          for(let row of values){
            let submitDate = this.form.getFieldValue("submitDate")
            if(submitDate >=  row.expDate){
              isexp = true;
              if(name == ""){
                name = name + row.productName;
              }else{
                name = name + "、" + row.productName;
              }
            }

            if(Number(row.productNum) <= 0){
              iszero = true;
              if(name1 == ""){
                name1 = name1 + row.productName;
              }else{
                name1 = name1 + "、" + row.productName;
              }
            }

            if(row.produceDate && row.produceDate >= row.expDate){
              isproduce = true;
              if(name2 == ""){
                name2 = name2 + row.productName;
              }else{
                name2 = name2 + "、" + row.productName;
              }
            }
          }

          if(isexp){
            this.$message.error("入库产品["+name+"]已到期，不能入库！");
            return;
          }
          if(iszero){
            this.$message.error("入库产品["+name1+"]数量必须大于0！");
            return;
          }
          if(isproduce){
            this.$message.error("入库产品["+name2+"]有效期必须大于生产日期！");
            return;
          }

          if(this.allowInMoreOrder == "0"){
            let purchaseOrderDetail = this.pdPurchaseOrderDetailTable.dataSource;
            if(purchaseOrderDetail.length > 0){
              for (let detail of purchaseOrderDetail) {
                let totalNum = 0; //当前产品总数量
                for(let row of values){
                  if(row.productId == detail.productId){
                    totalNum = totalNum + Number(row.productNum);
                  }
                }
                if(Number(detail.arrivalNum) + Number(totalNum) > Number(detail.orderNum)){
                  name = name + "  " + detail.productName;
                  bool = true;
                }
              }
              if(bool){
                this.$message.error("入库产品["+name+"]数量不能大于订单产品数量！");
                return;
              }
            }
          }

          // 发起请求
          this.confirmLoading = true
          httpAction(url, formData, method).then((res) => {
            if (res.success) {
              this.model.id = res.result.recordId;
              this.$message.success(res.result.message);
              this.$emit('ok');
              this.close();
              if(flag == "2"){
                this.printBtn("2"); //通过并打印
              }
            } else {
              this.$message.warning(res.message);
            }
          }).finally(() => {
            this.confirmLoading = false
          })

        }).catch(e => {
          if (e.error == VALIDATE_NO_PASSED) {
            // 如果有未通过表单验证的子表，就自动跳转到它所在的tab
            this.activeKey = e.index == null ? this.activeKey : this.refKeys[e.index]
          } else {
            console.error(e)
          }
        })
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)

        return {
          ...main, // 展开
          pdStockRecordDetailList: allValues.tablesValue[0].values,
        }
      },
      validateError(msg){
        this.$message.error(msg)
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'recordNo','mergeOrderNo','inType','submitBy','submitByName','submitDate','remarks','inDepartId','supplierId',
          'testResult','storageResult','temperature','humidity','remarks','refuseReason','format'))
      },

      //-----------------供应商查询start
      supplierHandleSearch(value) {
        fetch(value, data => (this.supplierData = data),this.url.querySupplier);
      },
      supplierHandleChange(value,option) {
        if(this.allowSupplier == "0"){ // 是否允许入库非本供应商产品 1-允许；0-不允许
          this.totalSum = '0';
          this.inTotalPrice = '0.0000';
          this.eachAllTable((item) => {
            item.initialize()
          })
        }
        this.supplierValue = value;
        // fetch(value, data => (this.supplierData = data),this.url.querySupplier);
        if(this.allowStockInExpSupplier == "0"){ //开关-是否允许入库证照过期的供应商   1-允许；0不允许
          this.checkSupplierIsExp(value);
        }

        if(option){
          this.supplierName = option.data.attrs.text;
          console.log(this.supplierName);
        }
      },
      //----------------供应商查询end

      //选择采购单
      choosePurchaseOrder() {
        this.$refs.pdChoosePurchaseOrderListModel.show();
      },
      //选择订单后回调函数
      returnPurchaseOrderData(data) {
        this.showOrderTable = true;
        this.pdPurchaseOrderDetailTable.dataSource = data;
        this.mergeOrderNo = data[0].mergeOrderNo;
        this.form.setFieldsValue({mergeOrderNo:data[0].mergeOrderNo});
        //校验产品列表是否有订单中的产品
        this.checkProductInOrder();
      },
      //删除行
      handleConfirmDelete() {
        if(this.$refs.pdStockRecordDetail.selectedRowIds.length > 0){
          this.$refs.pdStockRecordDetail.removeSelectedRows();
          // 计算总数量和总价格
          this.getTotalNumAndPrice([]);
        }else{
          this.$message.error("请选择需要删除的数据！")
        }
      },
      // 选择产品 新增行
      chooseProductList() {
        let supplierId = this.form.getFieldValue("supplierId");
        let isAllowProduct = this.form.getFieldValue("isAllowProduct");
        let mergeOrderNo = "";
        if(this.allowSupplier == "0"){ // 是否允许入库非本供应商产品 1-允许；0-不允许
          if(!supplierId) {
            this.$message.error("请先选择供应商！");
            return;
          }
        }else{
          supplierId = "";
        }
        //开关-是否允许入库非订单产品     1-允许非订单产品；0-不允许非订单产品
        if(this.allowNotOrderProduct == "0") {
          if(!this.mergeOrderNo){
            this.$message.error("请先导入订单！");
            return;
          }
          mergeOrderNo = this.mergeOrderNo;
        }
        this.$refs.pdChooseProductListModel.show({supplierId:supplierId,supplierName:"",mergeOrderNo:mergeOrderNo});
      },
      // 选择产品弹出框回调函数
      returnProductData(data) {
        let rows = [];
        let products = [];

        if(this.allowStockInExpProduct == "0"){
          let name = "";
          data.forEach((item, idx) => {
            let bool = true;
            if(item.validityFlag == "1"){ // 证照过期标志
              // bool = false; // TODO
              if(name == ""){
                name = name + item.productName;
              }else{
                name = name + "、" + item.productName;
              }
            }
            if(bool){
              products.push(item)
            }
          })
          if(name){
            this.$message.error("产品[" + name + "]证照已过期，请更新证照信息！");
          }
        }else{
          products = data;
        }

        let { values } = this.$refs.pdStockRecordDetail.getValuesSync({ validate: false });
        if(values.length > 0){
          // 如果列表中有相同产品则不加行
          products.forEach((item, idx) => {
            let bool = true;
            let name = "";
            values.forEach((value, idx) => {
              if (value.productId == item.productId
                && value.batchNo == "" && value.expDate == ""){
                bool = false;
              }
            })
            if(bool){
              rows.push(item)
            }
          })
        }else{
          rows = products;
        }

        //校验是否允许入库量大于订单量
        if(!this.checkAllowInMoreOrderForAddProductBtn(rows,values)){
          return;
        }

        rows.forEach((item, idx) => {
          // j-editable-table表格（可能是BUG）：values变更 不会同步变更到dataSource，新增行时需要手动赋值到dataSource
          this.pdStockRecordDetailTable.dataSource = values;
          this.addrows(item);
        })

        // 计算总数量和总价格
        this.getTotalNumAndPrice(values);
      },
      setPriceDisabled(){
        if(this.allowEditPrice == "1"){
          this.$nextTick(() => {
            let caseId = this.$refs.pdStockRecordDetail.caseId;
            let rows = this.$refs.pdStockRecordDetail.rows;
            for(let item of rows){
              document.getElementById("purchasePrice"+item.id).disabled = false;
            }
          })
        }
      },
      // 点“选择产品”按钮后 调用 新增一行
      addrows(row){
        let data = {
          productId: row.productId,
          productName: row.productName,
          productNumber:row.number,
          spec: row.spec,
          version: row.version,
          purchasePrice:row.purchasePrice,
          inTotalPrice:Number(!row.purchasePrice ? 0 : row.purchasePrice).toFixed(4),
          unitName: row.unitName,
          venderName: row.venderName,
          supplierName: row.supplierName,
          specUnitId: row.specUnitId,
          specQuantity: row.specQuantity,
          // registration: row.registration,
          productBarCode:"",
          produceDate:"",
          expDate:"",
          batchNo:"",
          productNum: 1,
          mergeOrderNo:"",
          inHuoweiCode:"",
          isUrgent:row.isUrgent,
          upQuantity:row.upQuantity,
          purchasedQuantity:row.purchasedQuantity
        }
        let purchaseOrderDetail = this.pdPurchaseOrderDetailTable.dataSource;
        for (let detail of purchaseOrderDetail) {
          // 产品如果在订单列表中 则添加订单编号
          if(detail.productId == data.productId){
            data.mergeOrderNo = detail.mergeOrderNo;
          }
        }
        this.pdStockRecordDetailTable.dataSource.push(data);
        // this.$refs.pdStockRecordDetail.add();
      },
      // 扫码 调用 新增一行
      addrowsByScanCode(row){
        let data = {
          productId: row.pdProduct.id,
          productName: row.pdProduct.name,
          productNumber:row.number,
          spec: row.pdProduct.spec,
          version: row.pdProduct.version,
          purchasePrice: row.pdProduct.purchasePrice,
          inTotalPrice:Number(!row.pdProduct.purchasePrice ? 0 : row.pdProduct.purchasePrice).toFixed(4),
          unitName: row.pdProduct.unitName,
          venderName: row.pdProduct.venderName,
          supplierName: row.pdProduct.supplierName,
          specUnitId: row.pdProduct.specUnitId,
          specQuantity: row.pdProduct.specQuantity,
          // registration: row.pdProduct.registration,
          productBarCode:row.productBarCode,
          produceDate:row.produceDate,
          expDate:row.expDate,
          batchNo:row.batchNo,
          productNum: 1,
          mergeOrderNo:"",
          inHuoweiCode:"",
          isUrgent:row.pdProduct.isUrgent,
          upQuantity:row.pdProduct.upQuantity,
          purchasedQuantity:row.pdProduct.purchasedQuantity
        }
        let purchaseOrderDetail = this.pdPurchaseOrderDetailTable.dataSource;
        purchaseOrderDetail.forEach((detail, idx) => {
          // 产品如果在订单列表中 则添加订单编号
          if(detail.productId == data.productId){
            data.mergeOrderNo = detail.mergeOrderNo;
          }
        })
        this.pdStockRecordDetailTable.dataSource.push(data);
        // this.$refs.pdStockRecordDetail.add();
      },
      // 计算总数量和总价格
      getTotalNumAndPrice(rows){
        this.$nextTick(() => {
          if (rows.length <= 0) {
            let {values} = this.$refs.pdStockRecordDetail.getValuesSync({validate: false});
            rows = values;
          }
          let totalSum = 0;
          let inTotalPrice = 0;
          rows.forEach((item, idx) => {
            totalSum = totalSum + Number(item.productNum);
            inTotalPrice = inTotalPrice + Number(item.inTotalPrice);
          })
          this.totalSum = totalSum;
          this.inTotalPrice = inTotalPrice.toFixed(4);
        })
      },
      //清空扫码框
      clearQueryParam(){
        this.queryParam = {};
        this.$refs.productNumberInput.focus();
      },
      onClickRow(record) {
        return {
          on: {
            click: (e) => {
              this.selectedRowIds.push(record.id)
            }
          }
        }
      },
      // 表格数据变更
      valueChange(event) {
        if(event){
          const { type, row, column, value, target } = event;
          if (type == FormTypes.select) {

          }else if(type == FormTypes.input){
            if(column.key == "productNum"){
              // 产品数量变更 计算每条产品的价格
              let rows = target.getValuesSync({ validate: false });
              // 校验是否允许入库量大于订单量
              let result1 = this.checkAllowInMoreOrder(row,rows.values);
              if(!result1.bool){
                // target.setValues([{rowKey: row.id, values: { productNum: result.num }}]);
                let inTotalPrice = (Number(row.purchasePrice) * Number(result1.num)).toFixed(4);
                target.setValues([{rowKey: row.id, values: { inTotalPrice: inTotalPrice,productNum: result1.num }}])
              }else{
                let inTotalPrice = (Number(row.purchasePrice) * Number(value)).toFixed(4);
                target.setValues([{rowKey: row.id, values: { inTotalPrice: inTotalPrice }}])
              }

              // 校验是否是紧急产品 TODO1
              // let result2 = this.checkUrgentProduct(row,rows.values);
              // if(!result2.bool){
              //   let inTotalPrice = (Number(row.purchasePrice) * Number(result2.num)).toFixed(4);
              //   target.setValues([{rowKey: row.id, values: { inTotalPrice: inTotalPrice,productNum: result2.num }}])
              // }else{
              //   let inTotalPrice = (Number(row.purchasePrice) * Number(value)).toFixed(4);
              //   target.setValues([{rowKey: row.id, values: { inTotalPrice: inTotalPrice }}])
              // }

            }else if(column.key == "purchasePrice"){
              let inTotalPrice = (Number(row.productNum) * Number(value)).toFixed(4);
              target.setValues([{rowKey: row.id, values: { inTotalPrice: inTotalPrice }}])
            }
          }
        }
        // 计算总数量和总价格
        this.getTotalNumAndPrice([]);
      },
      // 只扫产品编号查询
      onlyNumbersearchQuery(){
        let productNumber = this.queryParam.productNumber;
        if(!productNumber){
          this.$message.error("请输入产品编号！");
          this.$refs.productNumberInput.focus();
          return;
        }
        this.queryParam.productBarCode = productNumber;
        this.searchQuery(1);
      },
      // 扫码查询
      searchQuery(num) {
        let that = this;
        let productNumber = this.queryParam.productNumber;
        if(!productNumber){
          this.$message.error("请输入产品编号！");
          this.$refs.productNumberInput.focus();
          return;
        }

        if(num == 0){       //产品编号扫码
          // 焦点条码输入框
          this.$refs.productBarCodeInput.focus();

        }else if(num == 1){ //条码扫码
          let productBarCode = this.queryParam.productBarCode;
          if(!productBarCode){
            this.$message.error("请输入二级条码！");
            return;
          }
          //解析条码
          scanCode(productNumber,productBarCode).then((res) => {
            if(res.code == "200" ){
              let result = res.result;
              if(result.code == "200" || result.code == "203"){
                let product = result.pdProduct;
                //开关-是否允许入库证照过期的产品   1-允许；0不允许
                if(that.allowStockInExpProduct == "0" && product.validityFlag == "1"){
                  this.clearQueryParam();
                  this.$message.error("产品[" + product.name + "]证照已过期，请更新证照信息！");
                  // return;  // TODO
                }
                //校验开关-是否允许入库非订单产品
                if(!this.checkAllowNotOrderProduct(product)){
                  return;
                }
                //校验供应商
                if(!this.checkSupplier(product)){
                  return;
                }

                let isAddRow = true;// 是否增加一行
                // 循环表格数据
                let { values } = this.$refs.pdStockRecordDetail.getValuesSync({ validate: false });
                if(values.length > 0){ //表格有数据
                  for(let item of values){
                    // 1.比较被扫码产品与列表产品条码是否一致：一致则数量相加，不一致则加一行
                    // 2.如果列表中的产品条码为空，则比较产品ID、批号、有效期，如果一致则数量相加，不一致则加一行
                    if((item.productBarCode && item.productBarCode == productBarCode)
                      || (!item.productBarCode && item.productId == product.id && item.batchNo == result.batchNo && item.expDate == result.expDate)){
                      isAddRow = false;
                      //校验是否允许入库量大于订单量
                      if(!this.checkAllowInMoreOrderForScanCode(product.id,values)){
                        break;
                      }
                      //条码一致 则数量相加
                      let productNum = Number(item.productNum) + 1;
                      let inTotalPrice = (Number(item.purchasePrice) * Number(productNum)).toFixed(4);
                      this.$refs.pdStockRecordDetail.setValues([{rowKey: item.id, values: {
                          productNum: productNum,inTotalPrice: inTotalPrice,productBarCode:result.productBarCode }}]);

                      this.getTotalNumAndPrice([]);
                      break;
                    }else{
                      //校验是否允许入库量大于订单量
                      if(!this.checkAllowInMoreOrderForScanCode(product.id,values)){
                        isAddRow = false;
                        break;
                      }
                    }
                  }
                }else{
                  //校验是否允许入库量大于订单量
                  if(!this.checkAllowInMoreOrderForScanCode(product.id,[])){
                    isAddRow = false;
                  }
                }

                if(isAddRow){
                  // j-editable-table表格（可能是BUG）：values变更 不会同步变更到dataSource，新增行时需要手动赋值到dataSource
                  this.pdStockRecordDetailTable.dataSource = values;
                  //条码新增一行
                  this.addrowsByScanCode(result);
                    // 计算总数量和总价格
                  this.getTotalNumAndPrice(values);
                }

                if(result.code == "203"){ // 近效期提醒
                  this.$message.error(result.msg);
                }
              }else if(result.code =="201"){
                this.$message.error(result.msg);
              }else{
                this.$message.error(result.msg);
              }
            }else{
              this.$message.error(res.message);
            }
            //清空扫码框
            this.clearQueryParam();
          })
        }
      },
      // 校验产品是否在订单列表中
      checkProductInOrder(){
        let purchaseOrderDetail = this.pdPurchaseOrderDetailTable.dataSource;
        let { values } = this.$refs.pdStockRecordDetail.getValuesSync({ validate: false });
        if(values.length > 0 && purchaseOrderDetail.length > 0){
          for(let row of values){
            this.$refs.pdStockRecordDetail.setValues([{rowKey: row.id, values: { mergeOrderNo: "" }}]);
            for (let detail of purchaseOrderDetail) {
              if(detail.productId == row.productId){
                  this.$refs.pdStockRecordDetail.setValues([{rowKey: row.id, values: {mergeOrderNo: detail.mergeOrderNo,productNum:row.productNum}}]);
              }
            }
          }
        }
      },
      //校验供应商 是否一致
      checkSupplier(product){
        let supplierId = this.form.getFieldValue("supplierId")

        if(!product.supplierId && this.allowSupplier == "0"){ // 是否允许入库非本供应商产品 1-允许；0-不允许
          this.$message.error("产品("+product.name+")没有维护供应商，请先维护供应商！");
          //清空扫码框
          this.clearQueryParam();
          return false;
        }

        if(supplierId){
          if(supplierId != product.supplierId && this.allowSupplier == "0"){  // 是否允许入库非本供应商产品 1-允许；0-不允许
            this.$message.error("产品("+product.name+")的供应商与选中的供应商不一致！");
            //清空扫码框
            this.clearQueryParam();
            return false;
          }
        }else{
          //默认选中扫码产品的供应商
          this.form.setFieldsValue({supplierId:product.supplierId});
          this.supplierName = product.supplierName;
          if(this.allowStockInExpSupplier == "0"){ //开关-是否允许入库证照过期的供应商   1-允许；0不允许
            let bool = this.checkSupplierIsExp(product.supplierId);
            if(this.allowSupplier == "0" && !bool){
              this.clearQueryParam();
              return false;
            }
          }
        }
        return true;
      },
      //校验供应商证照是否过期
      checkSupplierIsExp(supplierId){
        getAction(this.url.querySupplierById,{id:supplierId}).then((res)=>{
          if (!res.success) {
            this.cmsFailed(res.message);
          }
          const result = res.result;
          if(result.validityFlag == "1"){
            // this.form.setFieldsValue({supplierId:""});  // TODO
            this.$message.error("供应商["+result.name+"]证照已过期，请更新供应商证照信息！");
            return false;
          }else{
            return true;
          }
        })
      },
      //校验是否允许订单外产品入库
      checkAllowNotOrderProduct(product){
        if(this.allowNotOrderProduct == "0"){ //1-允许非订单产品；0-不允许非订单产品
          if(!this.mergeOrderNo){
            this.$message.error("请先导入订单！");
            //清空扫码框
            this.clearQueryParam();
            return false;
          }
          let bool = true;
          let purchaseOrderDetail = this.pdPurchaseOrderDetailTable.dataSource;
          for(let item of purchaseOrderDetail){
            if(item.number == product.number){
              bool = false;
              break;
            }
          }
          if(bool){
            this.$message.error("扫码产品("+product.name+")不是订单中的产品！");
            //清空扫码框
            this.clearQueryParam();
            return false;
          }
        }
        return true;
      },
      // 校验是否是紧急产品 TODO1
      // checkUrgentProduct(currentRow,rows){
      //   let result = {};
      //   result.bool = true;
      //   if(currentRow.isUrgent == "0"){// 紧急产品
      //     let upQuantity = Number(currentRow.upQuantity ? currentRow.upQuantity : ""); // 需要采购数量
      //     let purchasedQuantity = Number(currentRow.purchasedQuantity ? currentRow.purchasedQuantity : ""); // 已采购数量
      //     let lastNum = upQuantity-purchasedQuantity; // 可采购产品数量
      //     let totalNum = 0; //当前产品总数量
      //     let exceptNum = 0;//产品数量(除了当前编辑行)
      //
      //     for(let row of rows){
      //       if(row.productId == currentRow.productId){
      //         totalNum = totalNum + Number(row.productNum);
      //         // if(currentRow.id != row.id && currentRow.productId == row.productId){
      //         //   exceptNum = exceptNum + Number(row.productNum);
      //         // }
      //       }
      //     }
      //     exceptNum = totalNum - Number(currentRow.productNum);
      //     if(totalNum > lastNum){
      //       result.bool = false;
      //       result.num = lastNum - exceptNum;
      //       this.$message.error("入库产品["+currentRow.productName+"]是紧急产品，入库数量不能大于紧急产品需采购数量"+lastNum+"！");
      //     }else{
      //       result.bool = true;
      //     }
      //   }
      //   return result;
      // },
      // /* 扫码调用 校验是否是紧急产品
      //  */
      // checkUrgentProductForScanCode(currentProductId,rows){
      //
      // },
      /* 修改产品数量时调用 校验是否允许入库量大于订单量 1-允许入库量大于订单量；0-不允许入库量大于订单量
         校验全局数据 入库数量是否大于订单量
       */
      checkAllowInMoreOrder(currentRow,rows){
        let result = {};
        if(this.allowInMoreOrder == "0"){
          let bool = true;
          let name = "";
          let purchaseOrderDetail = this.pdPurchaseOrderDetailTable.dataSource;
          if(purchaseOrderDetail.length <= 0){
              result.bool = true;
              return result;
          }
          for (let detail of purchaseOrderDetail) {
            let totalNum = 0; //当前产品总数量
            let exceptNum = 0;//产品数量(除了当前编辑行)
            for(let row of rows){
              if(row.productId == detail.productId && currentRow.productId == detail.productId){
                totalNum = totalNum + Number(row.productNum);
                if(currentRow.id != row.id && currentRow.productId == detail.productId){
                  exceptNum = exceptNum + Number(row.productNum);
                }
              }
            }
            if(Number(detail.arrivalNum) + Number(totalNum) > Number(detail.orderNum)){
              name = detail.productName;
              bool = false;
              // 计算产品数量最大值
              result.num = Number(detail.orderNum) - Number(detail.arrivalNum) - Number(exceptNum);
              break;
            }
          }

          if(!bool){
            this.$message.error("入库产品["+name+"]数量不能大于订单产品数量！");
            //清空扫码框
            // this.clearQueryParam();
            result.bool = false;
            return result;
          }
        }
        result.bool = true;
        return result;
      },
      /* 扫码调用 校验是否允许入库量大于订单量 1-允许入库量大于订单量；0-不允许入库量大于订单量
         校验当前扫码产品 入库数量是否大于订单量
       */
      checkAllowInMoreOrderForScanCode(currentProductId,rows){
        if(this.allowInMoreOrder == "0"){
          let bool = true;
          let name = "";
          let purchaseOrderDetail = this.pdPurchaseOrderDetailTable.dataSource;
          if(purchaseOrderDetail.length <= 0){
            return true;
          }
          for (let detail of purchaseOrderDetail) {
            let totalNum = 0; //当前产品总数量
            if(currentProductId == detail.productId){
              for(let row of rows){
                if(row.productId == detail.productId){
                  totalNum = totalNum + Number(row.productNum);
                }
              }
              if((Number(detail.arrivalNum) + Number(totalNum) + 1) > Number(detail.orderNum)){
                name = detail.productName;
                bool = false;
                break;
              }
            }
          }

          if(!bool){
            this.$message.error("入库产品["+name+"]数量不能大于订单产品数量！");
            //清空扫码框
            this.clearQueryParam();
            return false;
          }
        }
        return true;
      },
      /* 选择产品调用 校验是否允许入库量大于订单量 1-允许入库量大于订单量；0-不允许入库量大于订单量
         校验当前选择的产品 入库数量是否大于订单量
       */
      checkAllowInMoreOrderForAddProductBtn(currentRows,rows){
        if(this.allowInMoreOrder == "0"){
          let bool = true;
          let name = "";
          let purchaseOrderDetail = this.pdPurchaseOrderDetailTable.dataSource;
          if(purchaseOrderDetail.length <= 0){
            return true;
          }
          for (let detail of purchaseOrderDetail) {
            let totalNum = 0; //当前产品总数量
            for(let curr of currentRows){
              if(detail.productId == curr.productId){
                for(let row of rows){
                  if(detail.productId == row.productId){
                    totalNum = totalNum + Number(row.productNum);
                  }
                }
                if((Number(detail.arrivalNum) + Number(totalNum) + 1) > Number(detail.orderNum)){
                  name = detail.productName;
                  bool = false;
                  break;
                }
              }
            }
          }

          if(!bool){
            this.$message.error("入库产品["+name+"]数量不能大于订单产品数量！");
            //清空扫码框
            // this.clearQueryParam();
            return false;
          }
        }
        return true;
      },
    },
  }

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
        if (currentValue == value) {
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
    timeout = setTimeout(fake, 0); //这边不延迟
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