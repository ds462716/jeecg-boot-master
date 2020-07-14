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
                <a-form-item label="退货出库单号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input disabled v-decorator="[ 'recordNo', validatorRules.recordNo]" placeholder="请输入出库单号"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="退货出库日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <j-date disabled placeholder="请选择出库日期" v-decorator="[ 'submitDate', validatorRules.submitDate]" :trigger-change="true" style="width: 100%"/>
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
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="6">
                <a-form-item label="退货出库库房" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-select
                    showSearch
                    placeholder=""
                    :disabled="disableOutDepart"
                    :defaultActiveFirstOption="false"
                    :showArrow="true"
                    :filterOption="false"
                    :allowClear="true"
                    @search="departHandleSearch"
                    @change="departHandleChange"
                    @focus="departHandleSearch"
                    :notFoundContent="notFoundContent"
                    v-decorator="[ 'outDepartId', validatorRules.outDepartId]"
                  >
                    <a-select-option v-for="d in departList" :key="d.id" :text="d.departName" >{{d.departName}}</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="退货出库类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-select disabled placeholder="退货出库类型" :allowClear="false" v-decorator="['outType', validatorRules.outType]" >
                    <a-select-option value="4">退货出库</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="入库库房" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input disabled v-decorator="[ 'inDepartName', validatorRules.inDepartName]" placeholder=""></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="6" v-show="false">
                <a-form-item label="领用人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-select
                    showSearch
                    placeholder="请选择领用人"
                    :disabled="disableSubmit"
                    :defaultActiveFirstOption="false"
                    :showArrow="true"
                    :filterOption="false"
                    :allowClear="true"
                    @search="userHandleSearch"
                    @change="userHandleChange"
                    @focus="userHandleSearch"
                    :notFoundContent="notFoundContent"
                    v-decorator="[ 'applyBy', validatorRules.applyBy]"
                  >
                    <a-select-option v-for="d in userList" :key="d.id" :text="d.realname" >{{d.realname}}</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-card>

        <!-- 产品列表区域 -->
        <a-card style="margin-bottom: 10px;">
          <a-tabs v-model="activeKey">
            <a-tab-pane tab="产品明细" :key="refKeys[0]" :forceRender="true">
              <a-form v-show="!disableSubmit">
                <a-row>
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
                      提示：按<span style="color: red">Ctrl+Alt</span>键快速定位到产品扫码输入框
                    </a-form-item>
                  </a-col>
                </a-row>
              </a-form>

              <div style="margin-bottom: 8px;" v-show="!disableSubmit">
                <a-button type="primary" icon="plus" @click="chooseProductList">选择库存产品</a-button>
                <a-popconfirm style="margin-left: 8px"
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
                :maxHeight="850"
                :rowNumber="true"
                :rowSelection="true"
                :actionButton="false"
                :disabled="disableSubmit"
                @valueChange="valueChange"
                @added="setPriceDisabled"
                style="text-overflow: ellipsis;"
              >
              </j-editable-table>
              <a-row style="margin-top:10px;text-align: right;padding-right: 5%">
                  <span style="font-weight: bold;font-size: large;padding-right: 5%">总数量：{{ totalSum }}</span>
                  <span style="font-weight: bold;font-size: large">总金额：{{ outTotalPrice }}</span>
              </a-row>
            </a-tab-pane>
          </a-tabs>
        </a-card>

        <a-card style="">
          <a-form :form="form">
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
      <a-button @click="handleCancel" style="margin-right: 15px;" v-show="disableSubmit">关  闭</a-button>
      <a-popconfirm title="确定放弃编辑？" @confirm="handleCancel" v-show="!disableSubmit" okText="确定" cancelText="取消">
        <a-button style="margin-right: 15px;">取  消</a-button>
      </a-popconfirm>
      <a-popconfirm title="确定撤回？" @confirm="cancelBtn" v-show="showCancelBtn" okText="确定" cancelText="取消">
        <a-button style="margin-right: 15px;" :loading="confirmLoading" type="danger">撤  回</a-button>
      </a-popconfirm> <!-- margin-right: 50px; -->
      <a-button @click="printBtn('1')" style="margin-right: 15px;" type="primary" v-show="showPrintBtn">打  印</a-button>
      <a-button @click="saveBtn" v-show="!disableSubmit" type="primary" :loading="confirmLoading" style="margin-right: 15px;">保存草稿</a-button>
      <a-button @click="submitBtn('1')" v-show="!disableSubmit" type="primary" :loading="confirmLoading" style="margin-right: 15px;">提  交</a-button>
      <a-button @click="submitBtn('2')" v-show="!disableSubmit" type="primary" :loading="confirmLoading" style="margin-right: 15px;">提交并打印</a-button>
    </template>

    <pd-choose-product-stock-list-model ref="pdChooseProductStockListModel" @ok="returnProductStockData" ></pd-choose-product-stock-list-model>
    <pd-stock-record-out-print-modal ref="pdStockRecordOutPrintModal"></pd-stock-record-out-print-modal>
    <pd-stock-record-out-print-modal-f-c-z-y-y ref="pdStockRecordOutPrintModalFCZYY"></pd-stock-record-out-print-modal-f-c-z-y-y>
    <ex-stock-record-out-print-modal ref="exStockRecordOutPrintModal"></ex-stock-record-out-print-modal>
  </j-modal>
</template>

<script>

  import Vue from 'vue'
  import pick from 'lodash.pick'
  import { FormTypes,getRefPromise,validateFormAndTables } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import JDate from '@/components/jeecg/JDate'
  import {httpAction, deleteAction, getAction} from '@/api/manage'
  import JDictSelectTagExpand from "@/components/dict/JDictSelectTagExpand"
  import ATextarea from "ant-design-vue/es/input/TextArea";
  import {stockScanCode, packageRecordScanCode} from '@/utils/barcode'
  import PdChooseProductStockListModel from "./PdChooseProductStockListModel";
  import PdStockRecordOutPrintModal from "../print/PdStockRecordOutPrintModal";
  import ExStockRecordOutPrintModal from "../../external/print/ExStockRecordOutPrintModal";
  import PdStockRecordOutPrintModalFCZYY from "../../external/fengcheng/print/PdStockRecordOutPrintModalFCZYY";

  const VALIDATE_NO_PASSED = Symbol()
  export { FormTypes, VALIDATE_NO_PASSED }

  export default {
    name: 'PdStockRecordReturnModal',
    mixins: [JEditableTableMixin],
    components: {
      PdStockRecordOutPrintModalFCZYY,
      ExStockRecordOutPrintModal,
      PdStockRecordOutPrintModal,
      PdChooseProductStockListModel,
      ATextarea,
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
        disableSubmit2:false,
        showCancelBtn:false,
        showPrintBtn:false,
        showRefuseReason:false,
        showSubmitAndPrint:false,
        disableOutDepart:true,
        stockOutText:"",

        initData:{},
        queryParam:{},

        allowInMoreOrder:"",		//开关-是否允许入库量大于订单量   1-允许入库量大于订单量；0-不允许入库量大于订单量
        allowEditPrice:"",//关-是否允许出入库时可修改进价和出价   1-允许；0不允许

        //部门下拉列表 start
        departValue: undefined,
        notFoundContent:"未找到内容",
        departList:[],
        inDepartName:"",
        outDepartName:"",
        //部门下拉列表 end
        //用户拉列表 start
        // userValue: undefined,
        // notFoundContent:"未找到内容",
        userList:[],
        //用户下拉列表 end

        hospitalCode:"",

        totalSum:'0',
        outTotalPrice:'0.0000',
        inTotalPrice:'0.0000',
        submitDateStr:"",
        args:{},
        //货区货位二级联动下拉框
        goodsAllocationList:[],
        huoquOptions:[],
        huoweiOptions:[],

        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 0,
        validatorRules: {
          recordNo:{},
          inDepartName:{},
          recordType:{},
          outType:{rules: [{required: true, message: '请选择出库类型!'}]},
          remarks:{},
          outDepartId:{rules: [{required: true, message: '请选择退货出库库房!'}]},
          inDepartId:{rules: [{required: true, message: '请选择入库库房!'}]},
          applyBy:{},

        },
        refKeys: ['pdStockRecordDetail',],
        tableKeys:['pdStockRecordDetail', ],
        activeKey: 'pdStockRecordDetail',
        // 出入库明细表(产品明细)  , type: FormTypes.hidden
        pdStockRecordDetailTable: {
          loading: false,
          dataSource: [],
          columns: [
            { title: '产品名称', key: 'productName', type: FormTypes.normal,width:"220px" },
            { title: '产品编号', key: 'productNumber', width:"200px" },
            { title: '产品条码', key: 'productBarCode', type: FormTypes.input, disabled:true, width:"200px" },
            { title: '规格', key: 'spec', width:"150px" },
            { title: '批号', key: 'batchNo', width:"100px" },
            { title: '单位', key: 'unitName', width:"60px" },
            { title: '有效期', key: 'expDate', width:"100px" },
            { title: '入库单价', key: 'purchasePrice', width:"90px" },
            { title: '出库单价', key: 'sellingPrice', type: FormTypes.input, disabled:true, width:"90px" },
            {
              title: '出库数量', key: 'productNum', type: FormTypes.input, width:"90px",
              placeholder: '${title}', defaultValue: '1',
              validateRules: [{ required: true, message: '${title}不能为空' },{ pattern: '^-?\\d+\\.?\\d*$',message: '${title}的格式不正确' }]
            },
            { title: '出库金额', key: 'outTotalPrice', type: FormTypes.input, disabled:true, width:"100px" },
            { title: '库存数量', key: 'stockNum', width:"90px" },
            { title: '出库货位', key: 'outHuoweiName', width:"100px" },
            { title: '生产日期', key: 'produceDate',  },
            { title: '入库货位', key: 'inHuoweiCode', type: FormTypes.select, width:"150px", options: [],allowSearch:true, placeholder: '${title}' },
            { title: '批次码', key: 'refBarCode', width:"160px" },
            { title: '库存明细ID', key: 'productStockId', type: FormTypes.hidden },
            { title: '产品ID', key: 'productId', type: FormTypes.hidden },
            { title: '出库货位编号', key: 'outHuoweiCode', type: FormTypes.hidden },
            { title: '供应商id', key: 'supplierId', type: FormTypes.hidden },
            { title: '规格单位ID', key: 'specUnitId', type: FormTypes.hidden },
            { title: '规格数量', key: 'specQuantity', type: FormTypes.hidden },
            { title: '注册证号', key: 'registration', type: FormTypes.hidden },
            { title: '生产厂家', key: 'venderName', type: FormTypes.hidden },
          ]
        },
        url: {
          init:"/pd/pdStockRecordReturn/initModal",
          submit: "/pd/pdStockRecordReturn/submit",
          add: "/pd/pdStockRecordReturn/add",
          cancel: "/pd/pdStockRecordReturn/cancel",
          queryById: "/pd/pdStockRecordReturn/queryById",
          // edit: "/pd/pdStockRecordReturn/edit",
          // querySupplier:"/pd/pdSupplier/getSupplierList",
          departList:"/pd/pdDepart/getSysTwoDepartList",
          userList:"/pd/pdDepart/queryUserByDepartParentId",
          huoweiList:"/pd/pdGoodsAllocation/getOptions",
          getApplyOrder:"/pd/pdApplyOrder/getOne",
          getAllocationOrder:"/pd/pdAllocationRecord/getOne",
          pdStockRecordDetail: {
            list: "/pd/pdStockRecordReturn/queryPdStockRecordDetailByMainId"
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
    directives: {
      focus: {
        // 当被绑定的元素插入到 DOM 中时……
        inserted: function (el) {
          //全局监听键盘事件
          document.onkeydown = function (event) {
            if (event.ctrlKey && event.altKey) {
              // 按ctrl+alt  聚焦元素
              el.focus();
              return;
            }
          }
        }
      },
    },
    methods: {
      // 重写close方法
      close() {
        this.visible = false;
        this.queryParam = {};
        this.totalSum = "";
        this.outTotalPrice = "";
        this.eachAllTable((item) => {
          item.initialize()
        })
        this.$emit('close')
      },
      getAllTable() {
        let values = this.tableKeys.map(key => getRefPromise(this, key))
        return Promise.all(values)
      },
      add(args) {
        if(args){
          this.args = args;
        }
        this.edit({})
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
        this.$nextTick(() => {
          this.departHandleSearch();  // 初始化部门列表 用于详情页(有id）、申领出库(无id)、调拨出库(无id)数据回显
        })

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
          this.popModal.title="退货出库明细";
          let fieldval = pick(this.model,'recordNo','outType','submitBy','submitByName','submitDate',
            'inDepartId','outDepartId','inDepartName','remarks','refuseReason','applyBy')
          this.$nextTick(() => {
            this.form.setFieldsValue(fieldval);
            // this.userHandleSearch(); //初始化申领人，用于详情页回显
          })
          params = { id: this.model.id }
        }else{
          this.popModal.title="新增退货出库";
          params = { id: "" }
        }

        this.pdStockRecordDetailTable.loading = true;
        getAction(this.url.init, params).then((res) => {
          if (res.success) {
            this.$nextTick(() => {
              // this.departList = res.result.sysDepartList; // 初始化部门列表 用于数据回显
              if(this.model.id){ //详情页
                this.pdStockRecordDetailTable.dataSource = res.result.pdStockRecordDetailList || [];
                //货区货位 下拉框
                this.goodsAllocationList = res.result.goodsAllocationList;
                this.pdStockRecordDetailTable.columns.forEach((item, idx) => {
                  if(item.key === "inHuoweiCode"){
                    item.options = this.goodsAllocationList;
                  }
                })

                this.totalSum = res.result.totalSum;
                this.outTotalPrice = res.result.outTotalPrice.toString();
                this.inTotalPrice = res.result.inTotalPrice.toString();
                this.disableOutDepart = true;
              }else{  // 新增页
                this.disableSubmit2 = false;
                this.initData = res.result;
                this.submitDateStr = res.result.submitDateStr;
                let fieldval = pick(this.initData,'recordNo','outType','submitBy','submitByName','submitDate',
                  'inDepartId','outDepartId','inDepartName','remarks','refuseReason','applyBy');
                this.form.setFieldsValue(fieldval);
                this.form.setFieldsValue({outType:"4"}); //	1-申领出库; 2-科室出库; 3-调拨出库; 4-退货出库
                //获取光标
                this.$refs['productNumberInput'].focus();
                if(res.result.departType == "1"){
                  this.disableOutDepart = false;
                }
              }
              this.hospitalCode = res.result.hospitalCode;
              this.allowEditPrice = res.result.allowEditPrice;
              if(this.disableSubmit){
                this.allowEditPrice = "0";
              }
              this.stockOutText = res.result.stockOutText;
              //开关-是否需要出库审批  1-是；0-否
              if(res.result.allowStockOutAudit == "0" && this.disableSubmit == false){
                this.showSubmitAndPrint = true;
              }

            })
          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
          // this.loading = false;
          this.pdStockRecordDetailTable.loading = false;
        })
      },
      /** 关闭按钮 **/
      closeBtn(){
        this.$emit('ok');
        this.close();
      },
      /** 关闭按钮点击事件 */
      handleCancel() {
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
          if(this.hospitalCode == "FCZYY"){
            this.$refs.pdStockRecordOutPrintModalFCZYY.show(res.result);
            this.$refs.pdStockRecordOutPrintModalFCZYY.title = this.stockOutText + "退货出库单";
          }else if(this.hospitalCode == "GZSLYY"){
            this.$refs.exStockRecordOutPrintModal.show(res.result);
            this.$refs.exStockRecordOutPrintModal.title = this.stockOutText + "退货出库单";
          }else{
            this.$refs.pdStockRecordOutPrintModal.show(res.result);
            this.$refs.pdStockRecordOutPrintModal.title = this.stockOutText + "退货出库单";
          }
        })
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
          this.$message.warning("当前退货出库单状态已被审批或已撤回，不能撤回！"); //当前出库单状态非已提交、待审核状态，不能撤回！
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
      /** 确定按钮点击事件 */
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
            this.$message.warning("退货出库产品数据为空，请扫码退货出库或选择产品");
            return;
          }

          let list = formData.pdStockRecordDetailList;

          for (let item of list){
            if(Number(item.productNum) > Number(item.stockNum)){
              this.$message.error("["+item.productName+"]退货出库数量不能大于库存数量！");
              return;
            }
            if(Number(item.productNum) <= 0){
              this.$message.error("["+item.productName+"]退货出库数量必须大于0！");
              return;
            }
          }

          // 发起请求
          this.confirmLoading = true
          httpAction(url, formData, method).then((res) => {
            if (res.success) {
              this.model.id = res.result.recordId;
              this.$message.success(res.message);
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
          if (e.error === VALIDATE_NO_PASSED) {
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
        this.form.setFieldsValue(pick(row,'recordNo','outType','submitBy','submitByName','submitDate','applyNo','allocationNo',
          'inDepartId','outDepartId','inDepartName','remarks','refuseReason','applyBy'))
      },
      // 部门下拉框搜索
      departHandleSearch(value){
        getAction(this.url.departList,{departName:value,parentFlag:"0"}).then((res)=>{
          if (!res.success) {
            this.cmsFailed(res.message);
          }
          this.departList = res.result;
        })
      },
      // 部门下拉框变更
      departHandleChange(value,option){
        this.userList = [];//清空领用人list
        this.form.setFieldsValue({applyBy:""});//清空领用人
        if(option){
          this.outDepartName = option.data.attrs.text;
        }
        this.eachAllTable((item) => {
          item.initialize()
        })
      },
      userHandleSearch(value){
        // if(!this.checkInDepart()) {
        //   this.$message.error("请选择入库科室！");
        //   return;
        // }
        //
        // let inDepartId = this.form.getFieldValue("inDepartId");
        // getAction(this.url.userList,{realname:value,currentDepartId:inDepartId}).then((res)=>{
        //   if (!res.success) {
        //     this.cmsFailed(res.message);
        //   }
        //   this.userList = res.result;
        // })
      },
       userHandleChange(value,option){
      },
      //删除行
      handleConfirmDelete() {
        if(this.$refs.pdStockRecordDetail.selectedRowIds.length > 0){
          this.$refs.pdStockRecordDetail.removeSelectedRows();
          this.getTotalNumAndPrice([]);
        }else{
          this.$message.error("请选择需要删除的数据！")
        }
      },
      // 选择产品 新增行
      chooseProductList() {
        let outType = this.form.getFieldValue("outType");
        // 校验是否选择入库科室
        if(!this.checkOutDepart()){
          this.$message.error("请选择退货出库科室！");
          return;
        }
        if(outType == "4"){

          let outDepartId = this.form.getFieldValue("outDepartId");

          if(this.hospitalCode == "FCZYY" || this.hospitalCode == "FCRMYY"){
            this.$refs.pdChooseProductStockListModel.show({nestatStatus:"1",barCodeType:"0",departId:outDepartId});
          }else if(this.hospitalCode == "GZSLYY"){
            this.$refs.pdChooseProductStockListModel.show({nestatStatus:"1",barCodeType:"0",productFlag:"0",departId:outDepartId}); //赣州市立医院，普通出库 禁止不能查询试剂
          }else{
            this.$refs.pdChooseProductStockListModel.show({nestatStatus:"1",barCodeType:"0",departId:outDepartId});
          }
        }else{
          this.$message.error("请选择出库类型！");
          return;
        }
      },
      // 选择产品后返回
      returnProductStockData(data) {
        let rows = [];
        let { values } = this.$refs.pdStockRecordDetail.getValuesSync({ validate: false });
        if(values.length > 0){
          // 如果列表中有相同产品则不加行
          data.forEach((item, idx) => {
            let bool = true;
            values.forEach((value, idx) => {
              if(item.id == value.productStockId){
                bool = false;
              }
            })
            if(bool){
              rows.push(item)
            }
          })
        }else{
          rows = data;
        }
        rows.forEach((item, idx) => {
          // j-editable-table表格（可能是BUG）：values变更 不会同步变更到dataSource，新增行时需要手动赋值到dataSource
          this.pdStockRecordDetailTable.dataSource = values;
          this.addrows(item);
        })
        // 计算总数量和总价格
        this.getTotalNumAndPrice(values);
      },
      // 点“选择产品”按钮后 调用 新增一行
      addrows(row){
        let data = {
          productStockId:row.id,
          productId: row.productId,
          productName: row.productName,
          productNumber:row.number,
          productBarCode:row.productBarCode,
          spec: row.spec,
          batchNo:row.batchNo,
          unitName:row.unitName,
          expDate:row.expDate,
          sellingPrice:row.sellingPrice,
          specUnitId: row.specUnitId,
          specQuantity: row.specQuantity,
          productNum: 1,
          purchasePrice:row.purchasePrice,
          outTotalPrice:Number(!row.sellingPrice ? 0 : row.sellingPrice).toFixed(4),
          stockNum:row.stockNum,
          outHuoweiName:row.huoweiName,
          outHuoweiCode:row.huoweiCode,
          registration:row.registration,
          refBarCode:row.refBarCode,
          venderName:row.venderName,
          inHuoweiCode:"",
          supplierId:row.supplierId,
          produceDate:row.produceDate
        }
        this.pdStockRecordDetailTable.dataSource.push(data);
        // this.$refs.pdStockRecordDetail.add();
      },
      // 计算总数量和总价格
      getTotalNumAndPrice(rows){
        this.$nextTick(() => {
          //产品
          if (rows.length <= 0) {
            let {values} = this.$refs.pdStockRecordDetail.getValuesSync({validate: false});
            rows = values;
          }
          let totalSum = 0;
          let outTotalPrice = 0;
          rows.forEach((item, idx) => {
            totalSum = totalSum + Number(item.productNum);
            outTotalPrice = outTotalPrice + Number(item.outTotalPrice);
          })
          this.totalSum = totalSum;
          this.outTotalPrice = outTotalPrice.toFixed(4);
        })
      },
      //清空扫码框
      clearQueryParam(){
        this.queryParam = {};
        this.$refs.productNumberInput.focus();
      },
      setPriceDisabled(){
        if(this.allowEditPrice == "1"){
          this.$nextTick(() => {
            let caseId = this.$refs.pdStockRecordDetail.caseId;
            let rows = this.$refs.pdStockRecordDetail.rows;
            for(let item of rows){
              document.getElementById("sellingPrice"+item.id).disabled = false;
            }
          })
        }
      },
      // 表格数据变更
      valueChange(event) {
        if(event){
          const { type, row, column, value, target } = event;
          if(type === FormTypes.input){
            if(column.key === "productNum"){
              let { values } = target.getValuesSync({ validate: false });
              for(let item of values){
                if(item.id == row.id && Number(value) > Number(item.stockNum)){
                  this.$message.error("["+row.productName+"]退货出库数量不能大于库存数量！");
                  // 产品数量变更 计算每条产品的价格
                  let outTotalPrice = (Number(row.sellingPrice) * Number(item.stockNum)).toFixed(4);
                  target.setValues([{rowKey: row.id, values: { outTotalPrice: outTotalPrice, productNum: item.stockNum }}])
                  // 计算总数量和总价格
                  this.getTotalNumAndPrice([]);
                  return;
                }
              }
              // 产品数量变更 计算每条产品的价格
              let outTotalPrice = (Number(row.sellingPrice) * Number(value)).toFixed(4);
              target.setValues([{rowKey: row.id, values: { outTotalPrice: outTotalPrice }}])
              // 计算总数量和总价格
              this.getTotalNumAndPrice([]);
            }else if(column.key == "sellingPrice"){
              let outTotalPrice = (Number(row.productNum) * Number(value)).toFixed(4);
              target.setValues([{rowKey: row.id, values: { outTotalPrice: outTotalPrice }}])
              // 计算总数量和总价格
              this.getTotalNumAndPrice([]);
            }
          }
        }
      },
      // 扫码查询
      searchQuery(num) {
        if(!this.checkOutDepart()) {
          this.clearQueryParam();
          this.$message.error("请选择退货出库科室！");
          return;
        }
        let productNumber = this.queryParam.productNumber;
        if(!productNumber){
          //清空扫码框
          this.clearQueryParam();
          this.$message.error("请输入产品编号！");
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
          stockScanCode(productNumber,productBarCode,"0","1","0").then((res) => {
            if(res.code == "200" || res.code == "203"){
              let pdProductStockList = res.result;
              if(!pdProductStockList){
                //清空扫码框
                this.clearQueryParam();
                this.$message.error("条码解析失败，请校验条码是否正确！");
                return;
              }
              if(pdProductStockList.length <= 0){
                //清空扫码框
                this.clearQueryParam();
                this.$message.error("库存中没有该产品！");
                return;
              }

              let { values } = this.$refs.pdStockRecordDetail.getValuesSync({ validate: false });
              for(let pdProductStock of pdProductStockList){
                let isAddRow = true;// 是否增加一行
                // 循环表格数据
                if(values.length > 0) { //表格有数据
                  for(let item of values){
                    if(pdProductStock.id == item.productStockId){// 库存明细ID一致，就+1
                      isAddRow = false;
                      if(Number(item.productNum) + 1 > Number(item.stockNum)){
                        //清空扫码框
                        this.clearQueryParam();
                        this.$message.error("["+item.productName+"]退货出库数量不能大于库存数量！");
                        return;
                      }

                      let productNum = Number(item.productNum) + 1;
                      let outTotalPrice = (Number(item.sellingPrice) * Number(productNum)).toFixed(4);

                      this.$refs.pdStockRecordDetail.setValues([{rowKey: item.id, values: {
                          productNum: productNum,outTotalPrice: outTotalPrice }}]);
                      // 计算总数量和总价格
                      this.getTotalNumAndPrice([]);
                      break;
                    }
                  }
                }

                if(isAddRow){
                  this.pdStockRecordDetailTable.dataSource = values;
                  //条码新增一行
                  this.addrows(pdProductStock);
                  // 计算总数量和总价格
                  this.getTotalNumAndPrice(values);
                }
              }
              if(res.code == "203"){ // 近效期提醒
                this.$message.error(result.message);
              }
            }else if(res.code ==="201"){
              this.$message.error(res.message);
            }else{
              this.$message.error(res.message);
            }
            //清空扫码框
            this.clearQueryParam();
          })
        }
      },
      // 校验是否选择入库科室
      checkOutDepart(){
        let outDepartId = this.form.getFieldValue("outDepartId");
        if(!outDepartId){
          return false;
        }
        return true;
      },
    },
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