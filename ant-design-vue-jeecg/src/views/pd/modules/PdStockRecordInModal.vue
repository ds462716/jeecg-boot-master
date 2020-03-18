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
                  <a-input disabled v-decorator="[ 'recordNo', validatorRules.recordNo]" placeholder="请输入入库单号"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="入库日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <j-date disabled placeholder="请选择入库日期" v-decorator="[ 'submitDate', validatorRules.submitDate]" :trigger-change="true" style="width: 100%"/>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="入库类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <!--<j-dict-select-tag :triggerChange="true" v-decorator="['inType', validatorRules.inType]" title="入库类型" dictCode="in_type"/>-->
                  <j-dict-select-tag-expand :disabled="disableSubmit" type="list" v-decorator="['inType', validatorRules.inType]" :trigger-change="true" dictCode="in_type" placeholder="请选择入库类型"/>
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
                    :disabled="disableSubmit"
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
                    <a-select-option v-for="d in supplierData" :key="d.value">{{d.text}}</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>

          <div class="table-operator">
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
          <a-tabs v-model="activeKey" @change="handleChangeTabs">
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
                :maxHeight="500"
                :rowNumber="true"
                :rowSelection="true"
                :actionButton="false"
                :disabled="disableSubmit"
                @valueChange="valueChange"
                style="text-overflow: ellipsis;"
              >
              <!--:maxHeight 大于 600 后就会有BUG 一次性选择9条以上产品，会少显示一条-->
              </j-editable-table>
              <a-row style="margin-top:10px;text-align: right;padding-right: 5%">
                  <span style="font-weight: bold;font-size: large;padding-right: 5%">总数量：{{ totalSum }}</span>
                  <span style="font-weight: bold;font-size: large">总金额：{{ totalPrice }}</span>
              </a-row>
            </a-tab-pane>
          </a-tabs>
        </a-card>

        <!-- 验收区域 -->
        <a-card style="">
          <a-form :form="form">
            <a-row>
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
            <a-row>
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
      </div>
    </a-spin>


    <template slot="footer">
      <a-button @click="closeBtn" style="margin-right: 15px;" v-show="disableSubmit">关  闭</a-button>
      <a-popconfirm title="确定放弃编辑？" @confirm="handleCancel" v-show="!disableSubmit" okText="确定" cancelText="取消">
        <a-button style="margin-right: 15px;">取  消</a-button>
      </a-popconfirm>
      <a-button @click="saveBtn" v-show="!disableSubmit" type="primary" :loading="confirmLoading" style="margin-right: 15px;">保存草稿</a-button>
      <a-button @click="submitBtn" v-show="!disableSubmit" type="primary" :loading="confirmLoading" style="margin-right: 15px;">提  交</a-button>
    </template>

    <pd-choose-purchase-order-list-model  ref="pdChoosePurchaseOrderListModel" @ok="returnPurchaseOrderData" ></pd-choose-purchase-order-list-model>
    <pd-choose-product-list-model  ref="pdChooseProductListModel" @ok="returnProductData" ></pd-choose-product-list-model>
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
    name: 'PdStockRecordInModal',
    mixins: [JEditableTableMixin],
    components: {
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

        initData:{},
        queryParam:{},
        allowInMoreOrder:"",		//开关-是否允许入库量大于订单量   1-允许入库量大于订单量；0-不允许入库量大于订单量
        allowNotOrderProduct:"",		//开关-是否允许入库非订单产品     1-允许非订单产品；0-不允许非订单产品
        //供应商下拉列表 start
        supplierValue: undefined,
        notFoundContent:"未找到内容",
        supplierData: [],
        //供应商下拉列表 end
        showOrderTable:false,
        orderNo:"",
        totalSum:'0',
        totalPrice:'0.0000',
        submitDateStr:"",

        //货区货位二级联动下拉框
        goodsAllocationList:[],
        huoquOptions:[],
        huoweiOptions:[],

        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 0,
        validatorRules: {
          recordNo:{},
          recordType:{},
          inType:{rules: [{required: true, message: '请选择入库类型!'}]},
          orderNo:{},
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
            { title: '申购单号', align:"center", dataIndex: 'orderNo' },
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
            { title: '产品名称', key: 'productName', type: FormTypes.normal,width:"220px" },
            { title: '产品编号', key: 'productNumber', width:"150px" },
            { title: '产品条码', key: 'productBarCode', type: FormTypes.input, disabled:true, width:"200px" },
            { title: '规格', key: 'spec', width:"150px" },
            // { title: '型号', key: 'version', width:"150px"  disabled },
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
            {
              title: '数量', key: 'productNum', type: FormTypes.input, width:"80px",
              placeholder: '${title}', defaultValue: '1',
              validateRules: [{ required: true, message: '${title}不能为空' },{ pattern: '^-?\\d+\\.?\\d*$',message: '${title}的格式不正确' }]
            },
            { title: '入库单价', key: 'purchasePrice', width:"80px" },
            { title: '金额', key: 'inTotalPrice', type: FormTypes.input, disabled:true, width:"100px" },
            { title: '货位', key: 'inHuoweiCode', type: FormTypes.select, width:"150px", options: [],allowSearch:true, placeholder: '${title}' },
            { title: '申购单号', key: 'orderNo', type: FormTypes.input, disabled:true, width:"180px" }
          ]
        },
        url: {
          init:"/pd/pdStockRecordIn/initModal",
          submit: "/pd/pdStockRecordIn/submit",
          edit: "/pd/pdStockRecordIn/edit",
          querySupplier:"/pd/pdSupplier/getSupplierList",
          pdStockRecordDetail: {
            list: "/pd/pdStockRecordIn/queryPdStockRecordDetailByMainId"
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
        this.orderNo = "";
        this.form.setFieldsValue({orderNo:""});
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
        this.loading = true;

        //初始化供应商，用于回显供应商
        this.supplierHandleSearch();

        let params = {};
          if(this.model.id){
            this.popModal.title="入库明细";
          let fieldval = pick(this.model,'recordNo','inType','submitBy','submitByName','submitDate','remarks','inDepartId','supplierId',
            'testResult','storageResult','temperature','humidity','remarks')
          this.$nextTick(() => {
            this.form.setFieldsValue(fieldval);
          })
          params = { id: this.model.id }
        }else{
          this.popModal.title="新增入库";
          params = { id: "" }
        }
        getAction(this.url.init, params).then((res) => {
          if (res.success) {
            this.$nextTick(() => {
              if(this.model.id){
                this.showOrderTable = true;
                this.pdPurchaseOrderDetailTable.dataSource = res.result.pdPurchaseDetailList || [];
                this.pdStockRecordDetailTable.dataSource = res.result.pdStockRecordDetailList || [];
                this.totalSum = res.result.totalSum;
                this.totalPrice = res.result.totalPrice.toString();
              }else{
                this.initData = res.result;
                this.initData.isAllowProduct = "0";
                this.initData.isAllowNum = "0";
                this.initData.testResult = "0";
                this.initData.storageResult = "0";
                this.initData.temperature = "25";
                this.initData.humidity = "50";
                this.allowInMoreOrder = res.result.allowInMoreOrder;
                this.allowNotOrderProduct = res.result.allowNotOrderProduct;
                this.submitDateStr = res.result.submitDateStr;
                let fieldval = pick(this.initData,'recordNo','inType','submitBy','submitByName','submitDate','remarks','inDepartId','supplierId',
                  'testResult','storageResult','temperature','humidity','remarks');
                this.form.setFieldsValue(fieldval);
                //获取光标
                this.$refs['productNumberInput'].focus();
              }

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
          this.loading = false;
        })
      },
      /** 关闭按钮 **/
      closeBtn(){
        this.visible = false;
        this.$emit('close');
      },
      /** 保存草稿 **/
      saveBtn() {

      },
      /** 确定按钮点击事件 */
      submitBtn() {
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

          let isexp = false;
          let iszero = false;
          let bool = false;
          let name = "";
          let name1 = "";

          let { values } = this.$refs.pdStockRecordDetail.getValuesSync({ validate: false });
          for(let row of values){
            let submitDate = this.form.getFieldValue("submitDate")
            if(submitDate >=  row.expDate){
              isexp = true;
              name = name + "  " + row.productName;
            }
          }
          for(let row of values){
            if(Number(row.productNum) <= 0){
              iszero = true;
              name1 = name1 + "  " + row.productName;
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

          if(this.allowInMoreOrder === "0"){
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
          let a =1;
          return this.request(formData);
        }).catch(e => {
          if (e.error === VALIDATE_NO_PASSED) {
            // 如果有未通过表单验证的子表，就自动跳转到它所在的tab
            this.activeKey = e.index == null ? this.activeKey : this.refKeys[e.index]
          } else {
            console.error(e)
          }
        })
      },
      // 保存 提交 修改 请求函数
      request(formData) {
        let url = this.url.submit, method = 'post'
        if (this.model.id) {
          url = this.url.edit
          method = 'put'
        }
        this.confirmLoading = true
        httpAction(url, formData, method).then((res) => {
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
        this.form.setFieldsValue(pick(row,'recordNo','inType','submitBy','submitByName','submitDate','remarks','inDepartId','supplierId',
          'testResult','storageResult','temperature','humidity','remarks'))
      },

      //-----------------供应商查询start
      supplierHandleSearch(value) {
        fetch(value, data => (this.supplierData = data),this.url.querySupplier);
      },
      supplierHandleChange(value) {
        this.totalSum = '0';
        this.totalPrice = '0.0000';
        this.eachAllTable((item) => {
          item.initialize()
        })
        this.supplierValue = value;
        fetch(value, data => (this.supplierData = data),this.url.querySupplier);
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
        this.orderNo = data[0].orderNo;
        this.form.setFieldsValue({orderNo:data[0].orderNo});
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
        let orderNo = "";
        if(!supplierId) {
          this.$message.error("请先选择供应商！");
          return;
        }
        //开关-是否允许入库非订单产品     1-允许非订单产品；0-不允许非订单产品
        if(this.allowNotOrderProduct === "0") {
          if(!this.orderNo){
            this.$message.error("请先导入订单！");
            return;
          }
          orderNo = this.orderNo;
        }
        this.$refs.pdChooseProductListModel.show({supplierId:supplierId,supplierName:"",orderNo:orderNo});
      },
      // 选择产品弹出框回调函数
      returnProductData(data) {
        let rows = [];
        let { values } = this.$refs.pdStockRecordDetail.getValuesSync({ validate: false });
        if(values.length > 0){
          // 如果列表中有相同产品则不加行
          data.forEach((item, idx) => {
            let bool = true;
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
          rows = data;
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
          productBarCode:"",
          expDate:"",
          batchNo:"",
          productNum: 1,
          orderNo:"",
          inHuoweiCode:""
        }
        let purchaseOrderDetail = this.pdPurchaseOrderDetailTable.dataSource;
        for (let detail of purchaseOrderDetail) {
          // 产品如果在订单列表中 则添加订单编号
          if(detail.number == data.productNumber){
            data.orderNo = detail.orderNo;
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
          productBarCode:row.productBarCode,
          expDate:row.expDate,
          batchNo:row.batchNo,
          productNum: 1,
          orderNo:"",
          inHuoweiCode:""
        }
        let purchaseOrderDetail = this.pdPurchaseOrderDetailTable.dataSource;
        purchaseOrderDetail.forEach((detail, idx) => {
          // 产品如果在订单列表中 则添加订单编号
          if(detail.number == data.productNumber){
            data.orderNo = detail.orderNo;
          }
        })
        this.pdStockRecordDetailTable.dataSource.push(data);
        this.$refs.pdStockRecordDetail.add();
      },
      // 计算总数量和总价格
      getTotalNumAndPrice(rows){
        this.$nextTick(() => {
          if (rows.length <= 0) {
            let {values} = this.$refs.pdStockRecordDetail.getValuesSync({validate: false});
            rows = values;
          }
          let totalSum = 0;
          let totalPrice = 0;
          rows.forEach((item, idx) => {
            totalSum = totalSum + Number(item.productNum);
            totalPrice = totalPrice + Number(item.inTotalPrice);
          })
          this.totalSum = totalSum;
          this.totalPrice = totalPrice.toFixed(4);
        })
      },
      //清空扫码框
      clearQueryParam(){
        this.queryParam.productNumber = "";
        this.queryParam.productBarCode = "";
        this.$refs.productNumberInput.focus();
      },
      // 表格数据变更
      valueChange(event) {
        if(event){
          const { type, row, column, value, target } = event;
          if (type === FormTypes.select) {

          }else if(type === FormTypes.input){
            if(column.key === "productNum"){
              // 产品数量变更 计算每条产品的价格
              let rows = target.getValuesSync({ validate: false });
              let result = this.checkAllowInMoreOrder(row,rows.values);
              if(!result.bool){
                // target.setValues([{rowKey: row.id, values: { productNum: result.num }}]);
                let inTotalPrice = (Number(row.purchasePrice) * Number(result.num)).toFixed(4);
                target.setValues([{rowKey: row.id, values: { inTotalPrice: inTotalPrice,productNum: result.num }}])
              }else{
                let inTotalPrice = (Number(row.purchasePrice) * Number(value)).toFixed(4);
                target.setValues([{rowKey: row.id, values: { inTotalPrice: inTotalPrice }}])
              }
            }
          }
        }
        // 计算总数量和总价格
        this.getTotalNumAndPrice([]);
      },
      // 扫码查询
      searchQuery(num) {
        let that = this;
        let productNumber = this.queryParam.productNumber.trim();
        if(!productNumber){
          this.$message.error("请输入产品编号！");
          this.$refs.productNumberInput.focus();
          return;
        }

        if(num == 0){       //产品编号扫码
          // 焦点条码输入框
          this.$refs.productBarCodeInput.focus();

        }else if(num == 1){ //条码扫码
          let productBarCode = this.queryParam.productBarCode.trim();
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
                          productNum: productNum,inTotalPrice: inTotalPrice,productBarCode:productBarCode }}]);

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
              }else if(result.code ==="201"){
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
            this.$refs.pdStockRecordDetail.setValues([{rowKey: row.id, values: { orderNo: "" }}]);
            for (let detail of purchaseOrderDetail) {
              if(detail.number == row.productNumber){
                  this.$refs.pdStockRecordDetail.setValues([{rowKey: row.id, values: {orderNo: detail.orderNo,productNum:row.productNum}}]);
              }
            }
          }
        }
      },
      //校验供应商 是否一致
      checkSupplier(product){
        let supplierId = this.form.getFieldValue("supplierId")
        if(supplierId){
          if(supplierId != product.supplierId){
            this.$message.error("产品("+product.name+")的供应商与选中的供应商不一致！");
            //清空扫码框
            this.clearQueryParam();
            return false;
          }
        }else{
          //默认选中扫码产品的供应商
          this.form.setFieldsValue({supplierId:product.supplierId});
        }
        return true;
      },
      //校验是否允许订单外产品入库
      checkAllowNotOrderProduct(product){
        if(this.allowNotOrderProduct === "0"){ //1-允许非订单产品；0-不允许非订单产品
          if(!this.orderNo){
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
      /* 修改产品数量时调用 校验是否允许入库量大于订单量 1-允许入库量大于订单量；0-不允许入库量大于订单量
         校验全局数据 入库数量是否大于订单量
       */
      checkAllowInMoreOrder(currentRow,rows){
        let result = {};
        if(this.allowInMoreOrder === "0"){
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
        if(this.allowInMoreOrder === "0"){
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
        if(this.allowInMoreOrder === "0"){
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