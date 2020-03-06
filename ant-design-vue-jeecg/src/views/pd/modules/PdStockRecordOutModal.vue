<template>
  <a-modal
    :visible="visible"
    :width="popModal.width"
    :style="popModal.style"
    :maskClosable="disableSubmit"
    :confirmLoading="confirmLoading"
    @cancel="handleCancel"
    :footer="null"
  >

    <template slot="title">
      <div style="width: 100%;height:20px;padding-right:32px;">
        <div style="float: left;">{{ title }}</div>
        <div style="float: right;">
          <a-button icon="fullscreen" style="width:56px;height:100%;border:0" @click="handleClickToggleFullScreen"/>
        </div>
      </div>
    </template>

    <a-spin :spinning="confirmLoading">
      <!-- 主表单区域 -->
      <div style="background:#ECECEC; padding:20px">
        <a-card title="" style="margin-bottom: 10px;">
          <a-form :form="form">
            <a-row>
              <a-col :span="6">
                <a-form-item label="出库单号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input disabled v-decorator="[ 'recordNo', validatorRules.recordNo]" placeholder="请输入出库单号"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="出库日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
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
                <a-form-item v-show="false" label="出库部门ID">
                  <a-input v-show="false" v-decorator="[ 'outDepartId', {}]" ></a-input>
                </a-form-item>
                <a-form-item v-show="false" label="采购订单号"> <!-- TODO 申领单号 调拨单号 -->
                  <a-input v-show="false" v-decorator="[ 'orderNo', {}]" ></a-input>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="6">
                <a-form-item label="出库库房" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input disabled v-decorator="[ 'outDepartName', validatorRules.outDepartName]" placeholder=""></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="入库库房" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-select
                    showSearch
                    placeholder="请选择入库库房"
                    :disabled="disableSubmit"
                    :supplierId="departValue"
                    :defaultActiveFirstOption="false"
                    :showArrow="true"
                    :filterOption="false"
                    @search="departHandleSearch"
                    @change="departHandleChange"
                    @focus="departHandleSearch"
                    :notFoundContent="notFoundContent"
                    v-decorator="[ 'inDepartId', validatorRules.inDepartId]"
                  >
                    <a-select-option v-for="d in departList" :key="d.id">{{d.departName}}</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="出库类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <j-dict-select-tag-expand :disabled="disableSubmit" @change="outTypeChange" type="list" v-decorator="['outType', validatorRules.outType]" :trigger-change="true" dictCode="out_type" placeholder="请选择出库类型"/>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>

          <div class="table-operator" v-show="showOrderTable">
            <!-- 申领单 调拨单表区域 -->
            <a-tabs>
              <a-tab-pane :tab="orderTableTitle" :forceRender="true">
                <a-button @click="chooseOrder(1)" type="primary" icon="import" style="margin-bottom: 8px;" v-show="showApplyBtn">从申领单导入</a-button>
                <a-button @click="chooseOrder(2)" type="primary" icon="import" style="margin-bottom: 8px;" v-show="showAllocationBtn">从调拨单导入</a-button>
                <a-table
                  v-show="showOrderTable"
                  ref="table"
                  size="middle"
                  bordered
                  rowKey="id"
                  :pagination="false"
                  :columns="pdOrderDetailTable.columns"
                  :dataSource="pdOrderDetailTable.dataSource"
                  :loading="pdOrderDetailTable.loading" >
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
                <a-button type="primary" icon="plus" @click="choosePackageList" style="margin-left: 8px">选择定数包</a-button>
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
      </div>

      <div class="drawer-bootom-button">
        <a-button @click="closeBtn" style="margin-right: 15px;" v-show="disableSubmit">关  闭</a-button>
        <a-popconfirm title="确定放弃编辑？" @confirm="handleCancel" v-show="!disableSubmit" okText="确定" cancelText="取消">
          <a-button style="margin-right: 15px;">取  消</a-button>
        </a-popconfirm>
        <a-button @click="saveBtn" v-show="!disableSubmit" type="primary" :loading="confirmLoading" style="margin-right: 15px;">保存草稿</a-button>
        <a-button @click="submitBtn" v-show="!disableSubmit" type="primary" :loading="confirmLoading" style="margin-right: 15px;">提  交</a-button>
      </div>

    </a-spin>
    <pd-choose-product-stock-list-model ref="pdChooseProductStockListModel" @ok="returnProductStockData" ></pd-choose-product-stock-list-model>
  </a-modal>
</template>

<script>

  import Vue from 'vue'
  import pick from 'lodash.pick'
  import { FormTypes,getRefPromise,validateFormAndTables } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import JDate from '@/components/jeecg/JDate'
  import {httpAction, deleteAction, getAction} from '@/api/manage'
  // import PdChoosePurchaseOrderListModel from "./PdChoosePurchaseOrderListModel";
  // import PdChooseProductListModel from "./PdChooseProductListModel";
  import JDictSelectTagExpand from "@/components/dict/JDictSelectTagExpand"
  import ATextarea from "ant-design-vue/es/input/TextArea";
  import {scanCode} from '@/utils/barcode'
  import PdChooseProductStockListModel from "./PdChooseProductStockListModel";

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
    name: 'PdStockRecordOutModal',
    mixins: [JEditableTableMixin],
    components: {
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

        initData:{},
        queryParam:{},
        allowInMoreOrder:"",		//开关-是否允许入库量大于订单量   1-允许入库量大于订单量；0-不允许入库量大于订单量

        //部门下拉列表 start
        departValue: undefined,
        notFoundContent:"未找到内容",
        departList:[],
        //部门下拉列表 end

        orderTableTitle:"",
        showApplyBtn:false,
        showAllocationBtn:false,
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
          outDepartName:{},
          recordType:{},
          outType:{rules: [{required: true, message: '请选择出库类型!'}]},
          orderNo:{},
          remarks:{},
          outDepartId:{},
          inDepartId:{rules: [{required: true, message: '请选择入库库房!'}]},

        },
        refKeys: ['pdStockRecordDetail',],
        tableKeys:['pdStockRecordDetail', ],
        activeKey: 'pdStockRecordDetail',
        // 申购订单明细表
        pdOrderDetailTable: {
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
            { title: '单号', align:"center", dataIndex: 'orderNo' },
            { title: '定数包名称', align:"center", dataIndex: 'packageName' },
            { title: '定数包编号', align:"center", dataIndex: 'packageCode' },
            { title: '产品编码', align:"center", dataIndex: 'number' },
            { title: '产品名称', align:"center", dataIndex: 'productName' },
            { title: '规格', align:"center", dataIndex: 'spec' },
            { title: '型号', align:"center", dataIndex: 'version' },
            { title: '申请数量', align:"center", dataIndex: 'productNum' },
            { title: '单位', align:"center", dataIndex: 'unitName' },
          ],
        },
        // 出入库明细表(产品明细)
        pdStockRecordDetailTable: {
          loading: false,
          dataSource: [],
          columns: [
            { title: '产品ID', key: 'productId', type: FormTypes.hidden },
            { title: '产品名称', key: 'productName', type: FormTypes.normal,width:"220px" },
            { title: '产品编号', key: 'productNumber', width:"160px" },
            { title: '产品条码', key: 'productBarCode', type: FormTypes.input, disabled:true, width:"200px" },
            { title: '规格', key: 'spec', width:"150px" },
            { title: '批号', key: 'batchNo', width:"100px" },
            { title: '单位', key: 'unitName', width:"50px" },
            { title: '有效期', key: 'expDate', width:"100px" },
            { title: '入库单价', key: 'purchasePrice', width:"80px" },
            { title: '出库单价', key: 'sellingPrice', width:"80px" },
            {
              title: '出库数量', key: 'productNum', type: FormTypes.input, width:"80px",
              placeholder: '${title}', defaultValue: '1',
              validateRules: [{ required: true, message: '${title}不能为空' },{ pattern: '^-?\\d+\\.?\\d*$',message: '${title}的格式不正确' }]
            },
            { title: '出库金额', key: 'outTotalPrice', type: FormTypes.input, disabled:true, width:"100px" },
            { title: '库存数量', key: 'stockNum', width:"80px" },
            { title: '出库货位', key: 'outhuoweiName', width:"100px" },
            {
              title: '入库货位', key: 'inHuoweiCode', type: FormTypes.select, width:"150px", options: [],allowSearch:true,
              placeholder: '${title}', validateRules: [{ required: true, message: '${title}不能为空' }]
            },
          ]
        },
        url: {
          init:"/pd/pdStockRecordOut/initModal",
          submit: "/pd/pdStockRecordOut/submit",
          edit: "/pd/pdStockRecordOut/edit",
          querySupplier:"/pd/pdSupplier/getSupplierList",
          pdStockRecordDetail: {
            list: "/pd/pdStockRecordOut/queryPdStockRecordDetailByMainId"
          },
          pdPurchaseDetail: {
            list: "/pd/pdPurchaseOrder/queryPdPurchaseDetail"
          },
        },
        popModal: {
          title: '这里是标题',
          visible: false,
          width: '100%',
          style: { top: '20px' },
          fullScreen: true
        },
      }
    },
    methods: {
      // 重写close方法
      close() {
        this.visible = false;
        this.showOrderTable = false;
        // this.pdOrderDetailTable.dataSource = [];
        this.queryParam = {};
        // this.orderNo = "";
        // this.form.setFieldsValue({orderNo:""});
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

        let params = {};
        if(this.model.id){
          let fieldval = pick(this.model,'recordNo','outType','submitBy','submitByName','submitDate','remarks',
            'inDepartId','outDepartId','outDepartName','testResult','storageResult','temperature','humidity','remarks')
          this.$nextTick(() => {
            this.form.setFieldsValue(fieldval);
          })
          params = { id: this.model.id }
        }else{
          params = { id: "" }
        }
        getAction(this.url.init, params).then((res) => {
          if (res.success) {
            this.$nextTick(() => {
              if(this.model.id){
                this.showApplyBtn = false;
                this.showAllocationBtn = false;
                this.showOrderTable = true;
                // this.pdOrderDetailTable.dataSource = res.result.pdPurchaseDetailList || [];
                this.pdStockRecordDetailTable.dataSource = res.result.pdStockRecordDetailList || [];
                this.totalSum = res.result.totalSum;
                this.totalPrice = res.result.totalPrice.toString();
              }else{
                this.initData = res.result;
                this.submitDateStr = res.result.submitDateStr;
                let fieldval = pick(this.initData,'recordNo','outType','submitBy','submitByName','submitDate','remarks',
                  'inDepartId','outDepartId','outDepartName','testResult','storageResult','temperature','humidity','remarks');
                this.form.setFieldsValue(fieldval);
                //获取光标
                this.$refs['productNumberInput'].focus();
              }

              this.goodsAllocationList = res.result.goodsAllocationList;
              this.departList = res.result.sysDepartList;
              this.pdStockRecordDetailTable.columns.forEach((item, idx) => {
                if(item.key === "inHuoweiCode"){ // TODO 查询入库货位
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
      outTypeChange(val){
        if(val == "1"){
          this.orderTableTitle = "申领单明细";
          this.showApplyBtn = true;
          this.showAllocationBtn = false;
          this.showOrderTable = true;
        }else if(val == "2"){
          this.orderTableTitle = "";
          this.showApplyBtn = false;
          this.showAllocationBtn = false;
          this.showOrderTable = false;
        }else if(val == "3"){
          this.orderTableTitle = "调拨单明细";
          this.showApplyBtn = false;
          this.showAllocationBtn = true;
          this.showOrderTable = true;
        }
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
            this.$message.warning("出库产品数据为空，请扫码出库或选择产品");
            return;
          }

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
        this.form.setFieldsValue(pick(row,'recordNo','outType','submitBy','submitByName','submitDate','remarks',
          'inDepartId','outDepartId','outDepartName','testResult','storageResult','temperature','humidity','remarks'))
      },
      /** 切换全屏显示 */
      handleClickToggleFullScreen() {
        let mode = !this.popModal.fullScreen
        if (mode) {
          this.popModal.width = '100%'
          this.popModal.style.top = '20px'
        } else {
          this.popModal.width = '1200px'
          this.popModal.style.top = '50px'
        }
        this.popModal.fullScreen = mode
      },
      departHandleSearch(){

      },
      departHandleChange(){

      },
      //删除行
      handleConfirmDelete() {
        if(this.$refs.pdStockRecordDetail.selectedRowIds.length > 0){
          this.$refs.pdStockRecordDetail.removeSelectedRows();
          this.$nextTick(() => {
            this.getTotalNumAndPrice();
          })
        }else{
          this.$message.error("请选择需要删除的数据！")
        }
      },
      chooseOrder(flag){
        if(flag == "1"){  //申领单

        }else if(flag == "2"){  //调拨单

        }
      },
      // 选择产品 新增行
      chooseProductList() {
        let outType = this.form.getFieldValue("outType");
        let inDepartId = this.form.getFieldValue("inDepartId");
        // if(!inDepartId){
        //   this.$message.error("请选择入库科室！");
        //   return;
        // }
        //
        // if(outType == "1"){        //申领单
        //   this.$refs.pdChooseProductStockListModel.show({supplierId:supplierId,supplierName:"",orderNo:orderNo});
        // }else if(outType == "2"){
          this.$refs.pdChooseProductStockListModel.show({});
        // }else if(outType == "3"){  //调拨单
        //   this.$refs.pdChooseProductStockListModel.show({supplierId:supplierId,supplierName:"",orderNo:orderNo});
        // }else{
        //   this.$message.error("请选择出库类型！");
        //   return;
        // }
      },
      // 选择产品后返回
      returnProductStockData(data) {
        let rows = data;
        let { values } = this.$refs.pdStockRecordDetail.getValuesSync({ validate: false });
        rows.forEach((item, idx) => {
          // j-editable-table表格（可能是BUG）：values变更 不会同步变更到dataSource，新增行时需要手动赋值到dataSource
          this.pdStockRecordDetailTable.dataSource = values;
          this.addrows(item);
        })

        // 计算总数量和总价格
        this.getTotalNumAndPrice(values);
      },
      // 选择定数包
      choosePackageList() {

      },
      // 点“选择产品”按钮后 调用 新增一行
      addrows(row){
        let data = {
          productId: row.productId,
          productName: row.productName,
          productNumber:row.number,
          productBarCode:row.productBarCode,
          spec: row.spec,
          batchNo:row.batchNo,
          unitName:row.unitName,
          expDate:row.expDate,
          sellingPrice:row.sellingPrice,
          productNum: 1,
          purchasePrice:row.purchasePrice,
          outTotalPrice:Number(!row.sellingPrice ? 0 : row.sellingPrice).toFixed(4),
          stockNum:row.stockNum,
          outhuoweiName:row.huoweiName,
          inHuoweiCode:""
        }
        this.pdStockRecordDetailTable.dataSource.push(data);
        this.$refs.pdStockRecordDetail.add();
      },
      // 扫码 调用 新增一行
      addrowsByScanCode(row){

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
            totalPrice = totalPrice + Number(item.outTotalPrice);
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
              let outTotalPrice = (Number(row.sellingPrice) * Number(value)).toFixed(4);
              target.setValues([{rowKey: row.id, values: { outTotalPrice: outTotalPrice }}])
            }
          }
        }
        // 计算总数量和总价格
        this.getTotalNumAndPrice([]);
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
            console.log(res)
            if(res.code == "200" ){
              let result = res.result;
              if(result.code == "200" || result.code == "203"){
                let product = result.pdProduct;

                let isAddRow = true;// 是否增加一行
                // 循环表格数据
                this.$refs.pdStockRecordDetail.getValues((error, values) => {

                  if(isAddRow){

                  }

                  if(result.code == "203"){ // 近效期提醒
                    this.$message.error(result.msg);
                  }
                })
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