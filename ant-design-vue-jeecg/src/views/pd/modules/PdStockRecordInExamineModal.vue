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
                <a-form-item label="入库单号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input disabled v-decorator="[ 'recordNo', validatorRules.recordNo]" placeholder="请输入出入库单号"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="入库日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <j-date disabled placeholder="请选择入库日期" v-decorator="[ 'submitDate', validatorRules.submitDate]" :trigger-change="true" style="width: 100%"/>
                </a-form-item>
              </a-col>
              <a-col :span="6">
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
                <a-form-item v-show="false" label="采购订单号">
                  <a-input v-show="false" v-decorator="[ 'orderNo', {}]" ></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="供应商" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-select
                    showSearch
                    placeholder="请选择供应商"
                    disabled
                    :supplierId="supplierValue"
                    :defaultActiveFirstOption="false"
                    :showArrow="true"
                    :filterOption="false"
                    :notFoundContent="notFoundContent"
                    v-decorator="[ 'supplierId', validatorRules.supplierId]"
                  >
                    <a-select-option v-for="d in supplierData" :key="d.value">{{d.text}}</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="业态" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <j-dict-select-tag-expand disabled type="list" v-decorator="['format', validatorRules.format]" :trigger-change="true" dictCode="format" placeholder="请选择入库类型"/>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>

          <div class="table-operator">
            <!-- 订单明细表区域 -->
            <a-tabs>
              <a-tab-pane tab="订单明细" :forceRender="true">
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
          <a-tabs v-model="activeKey" @change="handleChangeTabs">
            <a-tab-pane tab="产品明细" :key="refKeys[0]" :forceRender="true">
              <div style="margin-bottom: 8px;"> <!-- v-show="disableSubmit" -->
                <a-button type="primary" icon="printer" @click="printNumber" v-show="isDisabledAuth('instock:printProductNumber')">打印编号</a-button>
              </div>
              <j-editable-table
                bordered
                :ref="refKeys[0]"
                :loading="pdStockRecordDetailTable.loading"
                :columns="pdStockRecordDetailTable.columns"
                :dataSource="pdStockRecordDetailTable.dataSource"
                :maxHeight="500"
                :rowNumber="true"
                :rowSelection="true"
                :actionButton="false"
                disabled
                @selectRowChange="handleSelectRowChange"
                style="text-overflow: ellipsis;"
              >
              <!--:maxHeight 大于 600 后就会有BUG 一次性选择9条以上产品，会少显示一条-->
              </j-editable-table>
              <a-row style="margin-top:10px;text-align: right;padding-right: 5%">
                  <span style="font-weight: bold;font-size: large;padding-right: 5%">总数量：{{ totalSum }}</span>
                  <span style="font-weight: bold;font-size: large">总金额：{{ inTotalPrice }}</span>
              </a-row>
            </a-tab-pane>
          </a-tabs>
        </a-card>

        <!-- 验收区域 -->
        <a-card style="margin-bottom: 10px;">
          <a-form :form="form">
            <a-row>
              <a-col :span="6">
                <a-form-item label="验收结果" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <j-dict-select-tag disabled v-decorator="[ 'testResult', validatorRules.testResult]" placeholder="请选择验收结果" :type="'radio'" :triggerChange="true" dictCode="test_result"/>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="储运状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <j-dict-select-tag disabled v-decorator="[ 'storageResult', validatorRules.storageResult]" placeholder="请选择储运状态" :type="'radio'" :triggerChange="true" dictCode="storage_result"/>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="6">
                <a-form-item label="温度（℃）" :labelCol="labelCol3" :wrapperCol="wrapperCol3">
                 <a-input disabled v-decorator="[ 'temperature', validatorRules.temperature]" placeholder=""></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="湿度（%）" :labelCol="labelCol3" :wrapperCol="wrapperCol3">
                  <a-input disabled v-decorator="[ 'humidity', validatorRules.humidity]" placeholder=""></a-input>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item label="备注" :labelCol="labelCol2" :wrapperCol="wrapperCol2" style="text-align: left">
                  <a-textarea disabled v-decorator="[ 'remarks', validatorRules.remarks]" placeholder="请输入备注"></a-textarea>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-card>

        <a-card style="">
          <a-form :form="form">
            <a-col :span="12">
              <a-form-item label="审批意见" :labelCol="labelCol2" :wrapperCol="wrapperCol2" style="text-align: left">
                <a-textarea :disabled="disableSubmit" v-decorator="[ 'refuseReason', validatorRules.refuseReason]" placeholder="请输入审批意见"></a-textarea>
              </a-form-item>
            </a-col>
          </a-form>
        </a-card>
      </div>
    </a-spin>

    <template slot="footer">
      <a-button @click="closeBtn" style="margin-right: 15px;" v-show="disableSubmit">关  闭</a-button>
      <a-button @click="printBtn('1')" style="margin-right: 15px;" type="primary" v-show="disableSubmit">打  印</a-button>
      <a-popconfirm title="确定放弃审核？" @confirm="handleCancel" v-show="!disableSubmit" okText="确定" cancelText="取消">
        <a-button style="margin-right: 15px;">取  消</a-button>
      </a-popconfirm>
      <a-popconfirm title="确定驳回？" @confirm="refuseBtn" v-show="!disableSubmit" okText="确定" cancelText="取消">
        <a-button type="danger" :loading="confirmLoading" style="margin-right: 15px;">驳回</a-button>
      </a-popconfirm>
      <a-button @click="submitBtn('1')" v-show="!disableSubmit" type="primary" :loading="confirmLoading" style="margin-right: 15px;">审核通过</a-button>
      <a-button @click="submitBtn('2')" v-show="!disableSubmit" type="primary" :loading="confirmLoading" style="margin-right: 15px;">审核通过并打印</a-button>
    </template>

    <pd-stock-record-in-print-modal ref="pdStockRecordInPrintModal"></pd-stock-record-in-print-modal>
    <ex-stock-record-in-print-modal ref="exStockRecordInPrintModal"></ex-stock-record-in-print-modal>
    <pd-product-number-print ref="printModalForm"></pd-product-number-print>
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
  import {scanCode} from '@/utils/barcode'
  import PdStockRecordInPrintModal from '../print/PdStockRecordInPrintModal'
  import ExStockRecordInPrintModal from "../../external/print/ExStockRecordInPrintModal";
  import PdProductNumberPrint from "../print/PdProductNumberPrint";
  import { disabledAuthFilter } from "@/utils/authFilter"

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
    name: 'PdStockRecordInExamineModal',
    mixins: [JEditableTableMixin],
    components: {
      PdProductNumberPrint,
      ExStockRecordInPrintModal,
      PdStockRecordInPrintModal,
      ATextarea,
      JDate,
      JDictSelectTagExpand,
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

        initData:{},
        queryParam:{},
        //供应商下拉列表 start
        supplierValue: undefined,
        notFoundContent:"未找到内容",
        supplierData: [],
        //供应商下拉列表 end
        showOrderTable:false,
        orderNo:"",
        totalSum:'0',
        inTotalPrice:'0.0000',
        submitDateStr:"",
        stockInText:"",

        //货区货位二级联动下拉框
        goodsAllocationList:[],
        huoquOptions:[],
        huoweiOptions:[],
        hospitalCode:"",

        sRowIds:[],//选中行id

        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 0,
        validatorRules: {
          inType:{rules: [{required: true, message: '请选择入库类型!'}]},
          refuseReason:{},
          remarks:{},
          format:{},
          isAllowProduct:{rules: [{required: true, message: '请选择!'}]},
          isAllowNum:{rules: [{required: true, message: '请选择!'}]},
          testResult:{rules: [{required: true, message: '请选择验收结果!'}]},
          storageResult:{rules: [{required: true, message: '请选择储运状态!'}]},
          temperature:{rules: [{required: true, message: '请输入温度!'},{pattern: '^-?\\d+\\.?\\d*$$',message: '只能输入数字' }]},
          humidity:{rules: [{required: true, message: '请输入湿度!'},{pattern: '^-?\\d+\\.?\\d*$',message: '只能输入数字' }]},
          supplierId:{rules: [{required: true, message: '请选择供应商!'}]},
        },
        refKeys: ['pdStockRecordDetail',],
        tableKeys:['pdStockRecordDetail', ],
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
            { title: '产品名称', key: 'productName', width:"220px" },
            { title: '产品编号', key: 'productNumber', width:"150px" },
            { title: '产品条码', key: 'productBarCode', width:"200px" },
            { title: '规格', key: 'spec', width:"150px" },
            // { title: '型号', key: 'version', width:"150px" },
            { title: '单位', key: 'unitName', width:"50px" },
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
            { title: '入库单价', key: 'purchasePrice', width:"80px" },
            {
              title: '数量', key: 'productNum', type: FormTypes.input, width:"80px",
              placeholder: '${title}', defaultValue: '1',
              validateRules: [{ required: true, message: '${title}不能为空' },{ pattern: '^-?\\d+\\.?\\d*$',message: '${title}的格式不正确' }]
            },
            { title: '金额', key: 'inTotalPrice', width:"90px" },
            // {
            //   title: '货区', key: 'huoquId', type: FormTypes.select, width:"150px", options: this.huoquOptions,allowSearch:true,
            //   placeholder: '${title}', validateRules: [{ required: true, message: '${title}不能为空' }]
            // },
            {
              title: '货位', key: 'inHuoweiCode', type: FormTypes.select, width:"150px", options: [],allowSearch:true,
              placeholder: '${title}', validateRules: [{ required: true, message: '${title}不能为空' }]
            },
            { title: '合并申购单号', key: 'mergeOrderNo', width:"180px" }
          ]
        },
        url: {
          init:"/pd/pdStockRecordIn/initModal",
          audit: "/pd/pdStockRecordIn/audit",
          querySupplier:"/pd/pdSupplier/getSupplierList",
          queryById: "/pd/pdStockRecordIn/queryById",
          getOnOff:"/pd/pdStockRecordIn/getOnOff",
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
        this.popModal.title="入库审核";
        //初始化供应商，用于回显供应商
        this.supplierHandleSearch();

        let fieldval = pick(this.model,'recordNo','inType','submitBy','submitByName','submitDate','inDepartId','supplierId',
          'testResult','storageResult','temperature','humidity','remarks','refuseReason','format')

        let params = { id: this.model.id }

        this.pdStockRecordDetailTable.loading = true;
        getAction(this.url.init, params).then((res) => {
          if (res.success) {
            this.$nextTick(() => {
              this.form.setFieldsValue(fieldval);

              if(this.model.id){
                this.pdPurchaseOrderDetailTable.dataSource = res.result.pdPurchaseOrderMergeDetail || [];
                this.pdStockRecordDetailTable.dataSource = res.result.pdStockRecordDetailList || [];
                this.totalSum = res.result.totalSum;
                this.inTotalPrice = res.result.inTotalPrice.toString();
                this.showOrderTable = false;
                if(this.pdPurchaseOrderDetailTable.dataSource.length > 0){
                  this.showOrderTable = true;
                }
              }

              this.stockInText = res.result.stockInText;
              this.hospitalCode = res.result.hospitalCode;
              this.goodsAllocationList = res.result.goodsAllocationList;
              this.pdStockRecordDetailTable.columns.forEach((item, idx) => {
                if(item.key === "inHuoweiCode"){
                  item.options = this.goodsAllocationList;
                }
              })
            })
          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
          // this.loading = false;
          this.pdStockRecordDetailTable.loading = false;
        })

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
            })
          }
          if(res.code==510){
            this.$message.warning(res.message)
          }
          // this.loading = false;
        })
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
            this.$refs.pdStockRecordInPrintModal.title = this.stockInText + "入库单";
          }else if(this.hospitalCode == "GZSLYY"){
            this.$refs.exStockRecordInPrintModal.show(res.result);
            this.$refs.exStockRecordInPrintModal.title = this.stockInText + "入库单";
          }else{
            this.$refs.pdStockRecordInPrintModal.show(res.result);
            this.$refs.pdStockRecordInPrintModal.title = this.stockInText + "入库单";
          }
        })
      },
      //产品编号打印，add by jiangxz 2020年7月15日 09:40:00
      printNumber(){
        if(this.sRowIds.length > 0){
          let dataSource = this.pdStockRecordDetailTable.dataSource;
          let printData = [];
          for(let item of dataSource){
            if(this.sRowIds.indexOf(item.id)>=0){
              let data = {};
              data.number = item.productNumber;
              data.name = item.productName;
              data.spec = item.spec;
              printData.push(data);
            }
          }
          if(printData.length > 0){
            this.$refs.printModalForm.init(printData);
          }else{
            this.$message.error("选择产品数据有误，请刷新页面后重新选择打印！")
          }
        }else{
          this.$message.error("请选择需要打印的产品！")
        }

      },
      handleSelectRowChange(selectedIds){
        this.sRowIds = selectedIds;
      },
      /** 关闭按钮 **/
      closeBtn(){
        this.close();
      },
      /** 关闭按钮点击事件 */
      handleCancel() {
        this.$emit('ok');
        this.close();
      },
      /** 拒绝 **/
      refuseBtn() {
        let refuseReason = this.form.getFieldValue("refuseReason");
        if(!refuseReason){
          this.$message.warning("请填写审批意见！");
          return;
        }
        let params =
          {
            id: this.model.id,
            refuseReason: refuseReason,
            auditStatus: "3"  // 拒绝
          }
        this.request(params);
      },
      /** 确定按钮点击事件 */
      submitBtn(flag) {
        let refuseReason = this.form.getFieldValue("refuseReason");
        if(!refuseReason){
          refuseReason = "同意";
        }
        let params =
          {
            id: this.model.id,
            refuseReason: refuseReason,
            auditStatus: "2" // 通过
          }
        this.request(params,flag);
      },
      // 保存 提交 修改 请求函数
      request(params,flag) {
        this.confirmLoading = true
        httpAction(this.url.audit, params, "post").then((res) => {
          if (res.success) {
            this.$message.success(res.message)
            this.$emit('ok');
            this.close();
            if(flag == "2"){
              this.printBtn("2"); //通过并打印
            }
          } else {
            this.$message.warning(res.message)
          }
        }).finally(() => {
          this.confirmLoading = false
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
        this.form.setFieldsValue(pick(row,'recordNo','inType','submitBy','submitByName','submitDate','inDepartId','supplierId',
          'testResult','storageResult','temperature','humidity','remarks','refuseReason','format'))
      },

      //-----------------供应商查询start
      supplierHandleSearch(value) {
        fetch(value, data => (this.supplierData = data),this.url.querySupplier);
      },
      //----------------供应商查询end
      /**
       * 校验权限
       * @param code
       * @returns {boolean|*}
       */
      isDisabledAuth(code){
        return !disabledAuthFilter(code);
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
        if (currentValue === value) {
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