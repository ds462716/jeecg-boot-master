<template>
  <j-modal
    :visible="visible"
    :width="1200"
    :title="title"
    :lockScroll="lockScroll"
    :fullscreen="fullscreen"
    :switchFullscreen="switchFullscreen"
    @cancel="handleCancel"

  >

    <a-spin :spinning="confirmLoading">
      <div style="background:#ECECEC; padding:20px">
        <a-card title="" style="margin-bottom: 10px;">
          <a-form :form="form">
            <a-row>
              <a-col :span="6">
                <a-form-item label="用量单号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input disabled v-decorator="[ 'dosageNo']" ></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="用量日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input disabled v-decorator="[ 'dosageDate']" ></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="操作人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input disabled v-decorator="[ 'dosageByName']" ></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="所属部门" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input disabled v-decorator="[ 'departName']" ></a-input>
                </a-form-item>
              </a-col>
            </a-row>
            <!-- <a-row>

             </a-row>-->
          </a-form>
        </a-card>

        <!-- 产品列表区域 -->
        <a-card style="margin-bottom: 10px;">
          <a-tabs v-model="activeKey" @change="handleChangeTabs">
            <a-tab-pane tab="产品明细" :key="refKeys[0]"  :forceRender="true">
              <a-form v-show="false">
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

              <div style="margin-bottom: 8px;" v-show="false">
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
                :loading="pdDosageDetailTable.loading"
                :columns="pdDosageDetailTable.columns"
                :dataSource="pdDosageDetailTable.dataSource"
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

        <a-card style="margin-bottom: 10px;">
          <a-tabs v-model="activeKey" @change="handleChangeTabs">
            <a-tab-pane tab="收费信息" :key="refKeys[0]"  :forceRender="true">
              <a-form :form="form">
                <a-row>
                  <!--<a-col :md="16" :sm="8">
                    <a-form-item label="执行收费" :labelCol="{span: 3}" :wrapperCol="{span: 20}">
                      <a-switch v-model="hyCharged"/>
                      <span style="color: red">  不选中的情况下，只在当前系统保存病人信息，医院系统中并不记账，此功能只作产品追溯用。</span>
                    </a-form-item>
                  </a-col>-->
                </a-row>
                <a-row>
                  <a-col :md="6" :sm="8" v-if="hyCharged">
                    <a-form-item label="住院号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <a-input :disabled="true" v-decorator="[ 'inHospitalNo', validatorRules.inHospitalNo]"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="8" v-else="!hyCharged">
                    <a-form-item label="住院号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <a-input :disabled="true" v-decorator="[ 'inHospitalNo']" ></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="8" v-if="hyCharged">
                    <a-form-item label="病人信息" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <a-input :disabled="true" v-decorator="[ 'patientInfo', validatorRules.patientInfo]" ></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="8" v-else="!hyCharged">
                    <a-form-item label="病人信息" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <a-input :disabled="true" v-decorator="[ 'patientInfo']" ></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="8">
                    <a-form-item label="门诊号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <a-input :disabled="true" v-decorator="[ 'outpatientNumber', validatorRules.outpatientNumber]" ></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="8">
                    <a-form-item label="手术编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <a-input :disabled="true" v-decorator="[ 'operativeNumber', validatorRules.operativeNumber]" ></a-input>
                    </a-form-item>
                  </a-col>
                </a-row>

                <a-row>
                  <a-col :md="6" :sm="8">
                    <a-form-item label="执行科室" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <a-input :disabled="true" v-decorator="[ 'exeDeptName', validatorRules.exeDeptName]" ></a-input>
                    </a-form-item>
                    <!-- 执行科室id -->
                    <a-form-item >
                      <a-input type="hidden" v-decorator="[ 'exeDeptId']"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="8">
                    <a-form-item label="手术科室" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <a-input :disabled="true" v-decorator="[ 'oprDeptName', validatorRules.oprDeptName]"></a-input>
                    </a-form-item>
                    <!-- 手术科室id -->
                    <a-form-item>
                      <a-input type="hidden" v-decorator="[ 'oprDeptId']"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="8">
                    <a-form-item label="手术医生" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <a-input :disabled="true" v-decorator="[ 'surgeonName', validatorRules.surgeonName]" ></a-input>
                    </a-form-item>
                    <!-- 手术医生id -->
                    <a-form-item>
                      <a-input type="hidden" v-decorator="[ 'surgeonId']"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="8">
                    <a-form-item label="开方医生" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <a-input :disabled="true" v-decorator="[ 'sqrtDoctorName', validatorRules.sqrtDoctorName]" ></a-input>
                    </a-form-item>
                    <!-- 开方医生id -->
                    <a-form-item>
                      <a-input type="hidden" v-decorator="[ 'sqrtDoctorId']"></a-input>
                    </a-form-item>
                  </a-col>
                </a-row>
                <a-row>
                  <a-col :md="6" :sm="8">
                    <a-form-item label="所属病区" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <a-input :disabled="true" v-decorator="[ 'subordinateWardName', validatorRules.subordinateWardName]"></a-input>
                    </a-form-item>
                    <!-- 所属病区id -->
                    <a-form-item>
                      <a-input type="hidden" v-decorator="[ 'subordinateWardId']"></a-input>
                    </a-form-item>
                  </a-col>
                </a-row>

                <a-form-item label="病人详细信息" :labelCol="labelCol2" :wrapperCol="wrapperCol2">
                  <a-textarea :disabled="true" v-decorator="[ 'patientDetailInfo', validatorRules.patientDetailInfo]"></a-textarea>
                </a-form-item>
                <a-form-item label="备注" :labelCol="labelCol2" :wrapperCol="wrapperCol2">
                  <a-textarea :disabled="true" v-decorator="[ 'remarks', validatorRules.remarks]" ></a-textarea>
                </a-form-item>

              </a-form>
            </a-tab-pane>
          </a-tabs>
        </a-card>
      </div>
    </a-spin>

    <template slot="footer">
      <a-button @click="closeBtn" style="margin-right: 15px;" v-show="disableSubmit">关  闭</a-button>
      <a-popconfirm title="确定放弃编辑？" @confirm="handleCancel" v-show="!disableSubmit" okText="确定" cancelText="取消">
        <a-button style="margin-right: 15px;">取  消</a-button>
      </a-popconfirm>
      <a-button @click="submitBtn" v-show="!disableSubmit" type="primary" :loading="confirmLoading" style="margin-right: 15px;">库存还回</a-button>
    </template>

    <pd-choose-product-stock-list-model ref="pdChooseProductStockListModel" @ok="returnProductStockData" ></pd-choose-product-stock-list-model>
  </j-modal>
</template>

<script>

  import Vue from 'vue'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import { FormTypes,getRefPromise,validateFormAndTables } from '@/utils/JEditableTableUtil'
  import ATextarea from "ant-design-vue/es/input/TextArea";
  import {stockScanCode} from '@/utils/barcode'
  import {httpAction, deleteAction, getAction} from '@/api/manage'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
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
    name: "PdDosageReturnedModal",
    mixins: [JEditableTableMixin],
    components: {
      PdChooseProductStockListModel,
    },
    data () {
      return {
        width:800,
        visible: false,
        model: {},
        title: '这里是标题',
        lockScroll: false,
        fullscreen: true,
        switchFullscreen: false,
        hyCharged: true,
        totalSum:'0',
        totalPrice:'0.0000',
        activeKey: 'pdDosageDetail',
        refKeys: ['pdDosageDetail',],
        //货区货位二级联动下拉框
        goodsAllocationList:[],
        queryParam:{},
        initData:{},
        pdDosageDetailTable: {
          loading: false,
          dataSource: [],
          columns: [
            { title: '库存明细ID', key: 'productStockId', type: FormTypes.hidden },
            { title: '产品ID', key: 'productId', type: FormTypes.hidden },
            { title: '产品名称', key: 'productName', type: FormTypes.normal,width:"220px" },
            { title: '产品编号', key: 'productNumber', width:"200px" },
            { title: '产品条码', key: 'productBarCode', type: FormTypes.input, disabled:true, width:"200px" },
            { title: '规格', key: 'spec', width:"200px" },
            { title: '批号', key: 'batchNo', width:"100px" },
            { title: '单位', key: 'unitName', width:"50px" },
            { title: '有效期', key: 'expDate', width:"100px" },
            { title: '入库单价', key: 'purchasePrice', width:"80px" },
            { title: '出库单价', key: 'sellingPrice', width:"80px" },
            {
              title: '用量数量', key: 'dosageCount', type: FormTypes.input,disabled:true, width:"80px",
              placeholder: '${title}', defaultValue: '1',
              validateRules: [{ required: true, message: '${title}不能为空' },{ pattern: '^-?\\d+\\.?\\d*$',message: '${title}的格式不正确' }]
            },
            { title: '用量金额', key: 'amountMoney', type: FormTypes.input, disabled:true, width:"100px" },
            { title: '剩余可退数量', key: 'leftRefundNum', width:"80px" },
            { title: '收费项目代码', key: 'chargeCode', width:"80px" },
            { title: '是否计费', key: 'isCharge',type: FormTypes.hidden},
            { title: '是否计费', key: 'isChargeText', width:"80px"},
            { title: '出库货位', key: 'outHuoweiName', width:"100px" },
            { title: '出库货位编号', key: 'outHuoweiCode', type: FormTypes.hidden },
          ]
        },
        disableSubmit:false,
        labelCol: {
          xs: { span: 16 },
          sm: { span: 8 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        labelCol2: {
          xs: { span: 16 },
          sm: { span: 4 },
        },
        wrapperCol2: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        validatorRules: {
          dosageNo: {rules: [
              {required: true, message: '请输入用量单号!'},
            ]},
          dosageDate: {rules: [
            ]},
          amountCount: {rules: [
            ]},
          amountMoney: {rules: [
            ]},
          patientInfo: {rules: [
              {required: true, message: '请输入病人信息!'},
            ]},
          patientDetailInfo: {rules: [
            ]},
          exeDeptId: {rules: [
            ]},
          exeDeptName: {rules: [
            ]},
          oprDeptId: {rules: [
            ]},
          oprDeptName: {rules: [
            ]},
          surgeonId: {rules: [
            ]},
          surgeonName: {rules: [
            ]},
          sqrtDoctorId: {rules: [
            ]},
          sqrtDoctorName: {rules: [
            ]},
          inHospitalNo: {rules: [
              {required: true, message: '请输入住院号!'},
            ]},
          dosageBy: {rules: [
            ]},
          subordinateWardId: {rules: [
            ]},
          subordinateWardName: {rules: [
            ]},
          outpatientNumber: {rules: [
            ]},
          operativeNumber: {rules: [
            ]},
          displayFlag: {rules: [
            ]},
          remarks: {rules: [
            ]},
          departId: {rules: [
              {required: true, message: '请输入所属部门!'},
            ]},
          departParentId: {rules: [
              {required: true, message: '请输入所属医院!'},
            ]},
        },
        url: {
          init:"/pd/pdDosage/initModal",
          submit: "/pd/pdDosageDetail/dosageReturned",
          add: "/pd/pdDosage/add",
          edit: "/pd/pdDosage/edit",
          departList:"/pd/pdDepart/getSysDepartList",
        }
      }
    },
    created () {
    },
    methods: {
      add () {
        this.edit({});
      },
      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
        this.loadData();
      },
      loadData() {
        this.loading = true;
        this.departHandleSearch();  // 初始化部门列表 用于数据回显
        let params = {};
        if(this.model.id){
          let fieldval = pick(this.model,'dosageNo','dosageDate','departName','dosageByName')
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
              // this.departList = res.result.sysDepartList; // 初始化部门列表 用于数据回显
              if(this.model.id){
                // 新增页
                this.initData = res.result;
                if(res.result.hyCharged==0){
                  this.hyCharged = true;
                }else{
                  this.hyCharged = false;
                }
                this.pdDosageDetailTable.dataSource = res.result.pdDosageDetails || [];
                let fieldval = pick(this.initData,'dosageNo','dosageDate','departName','dosageByName','inHospitalNo','patientInfo','patientDetailInfo','outpatientNumber','operativeNumber','exeDeptName','exeDeptId','oprDeptName','oprDeptId','surgeonName','surgeonId','sqrtDoctorName','sqrtDoctorId','subordinateWardName','subordinateWardId','remarks');
                this.form.setFieldsValue(fieldval);
                this.goodsAllocationList = res.result.goodsAllocationList;
                //获取光标
                this.$refs['productNumberInput'].focus();
              }else{  // 新增页
                this.initData = res.result;
                let fieldval = pick(this.initData,'dosageNo','dosageDate','departName','dosageByName','inHospitalNo','patientInfo','patientDetailInfo','outpatientNumber','operativeNumber','exeDeptName','exeDeptId','oprDeptName','oprDeptId','surgeonName','surgeonId','sqrtDoctorName','sqrtDoctorId','subordinateWardName','subordinateWardId','remarks');
                this.form.setFieldsValue(fieldval);
                this.goodsAllocationList = res.result.goodsAllocationList;
                //获取光标
                this.$refs['productNumberInput'].focus();
              }
            })
          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
          this.loading = false;
        })
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
      close () {
        this.$emit('close');
        this.totalSum = 0;
        this.totalPrice = 0.0000;
        this.visible = false;
        this.pdDosageDetailTable.dataSource = [];
        this.eachAllTable((item) => {
          item.initialize()
        })
      },
      handleCancel () {
        this.close()
      },
      // 扫码查询
      searchQuery(num) {
        let that = this;
        let productNumber = this.queryParam.productNumber;
        if(!productNumber){
          //清空扫码框
          this.clearQueryParam();
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
          stockScanCode(productNumber,productBarCode).then((res) => {
            if(res.code == "200" || res.code == "203"){
              let pdProductStockList = res.result;
              if(!pdProductStockList){
                //清空扫码框
                this.clearQueryParam();
                this.$message.error("条码解析失败，请校验条码是否正确！");
                return;
              }
              // 循环表格数据
              let { values } = this.$refs.pdDosageDetail.getValuesSync({ validate: false });
              for(let pdProductStock of pdProductStockList){
                let isAddRow = true;// 是否增加一行
                if(values.length > 0) { //表格有数据
                  for(let item of values){
                    if(pdProductStock.id == item.productStockId){// 库存明细ID一致，就+1
                      isAddRow = false;
                      if(Number(item.dosageCount) + 1 > Number(item.leftRefundNum)){
                        //清空扫码框
                        this.clearQueryParam();
                        this.$message.error("["+item.productName+"]出库数量不能大于剩余可退数量！");
                        return;
                      }

                      let dosageCount = Number(item.dosageCount) + 1;
                      let amountMoney = (Number(item.sellingPrice) * Number(dosageCount)).toFixed(4);

                      this.$refs.pdDosageDetail.setValues([{rowKey: item.id, values: {
                          dosageCount: dosageCount,amountMoney: amountMoney }}]);
                      // 计算总数量和总价格
                      this.getTotalNumAndPrice([]);
                      break;
                    }
                  }
                }
                if(isAddRow){
                  this.pdDosageDetailTable.dataSource = values;
                  //条码新增一行
                  this.addrowsByScanCode(pdProductStock);
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
      // 选择产品 新增行
      chooseProductList() {

        this.$refs.pdChooseProductStockListModel.width = 1550;
        this.$refs.pdChooseProductStockListModel.show({});
      },
      // 选择定数包
      choosePackageList() {

      },
      //删除行
      handleConfirmDelete() {
        if(this.$refs.pdDosageDetail.selectedRowIds.length > 0){
          this.$refs.pdDosageDetail.removeSelectedRows();
          this.getTotalNumAndPrice([]);
        }else{
          this.$message.error("请选择需要删除的数据！")
        }
      },
      // 计算总数量和总价格
      getTotalNumAndPrice(rows){
        this.$nextTick(() => {
          if (rows.length <= 0) {
            let {values} = this.$refs.pdDosageDetail.getValuesSync({validate: false});
            rows = values;
          }
          let totalSum = 0;
          let totalPrice = 0;
          rows.forEach((item, idx) => {
            totalSum = totalSum + Number(item.dosageCount);
            totalPrice = totalPrice + Number(item.amountMoney);
          })
          this.totalSum = totalSum;
          this.totalPrice = totalPrice.toFixed(4);
        })
      },
      // 表格数据变更
      valueChange(event) {
        if(event){
          const { type, row, column, value, target } = event;
          if (type === FormTypes.select) {

          }else if(type === FormTypes.input){
            if(column.key === "dosageCount"){
              let { values } = target.getValuesSync({ validate: false });
              for(let item of values){
                if(item.id == row.id && Number(value) > Number(item.leftRefundNum)){
                  this.$message.error("["+row.productName+"]使用数量不能大于剩余可退数量！");
                  // 产品数量变更 计算每条产品的价格
                  let amountMoney = (Number(row.sellingPrice) * Number(item.leftRefundNum)).toFixed(4);
                  target.setValues([{rowKey: row.id, values: { amountMoney: amountMoney, dosageCount: item.leftRefundNum }}])
                  // 计算总数量和总价格
                  this.getTotalNumAndPrice([]);
                  return;
                }
              }
              // 产品数量变更 计算每条产品的价格
              let amountMoney = (Number(row.sellingPrice) * Number(value)).toFixed(4);
              target.setValues([{rowKey: row.id, values: { amountMoney: amountMoney }}])
              // 计算总数量和总价格
              this.getTotalNumAndPrice([]);
            }
          }
        }
      },
      //清空扫码框
      clearQueryParam(){
        this.queryParam.productNumber = "";
        this.queryParam.productBarCode = "";
        this.$refs.productNumberInput.focus();
      },
      // 扫码 调用 新增一行
      addrowsByScanCode(row){
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
          dosageCount: 1,
          purchasePrice:row.purchasePrice,
          amountMoney:Number(!row.sellingPrice ? 0 : row.sellingPrice).toFixed(4),
          leftRefundNum:row.leftRefundNum,
          chargeCode:row.chargeCode,
          isChargeText:row.isCharge=="0"?"是":"否",
          isCharge:row.isCharg,
          outHuoweiName:row.huoweiName,
          outHuoweiCode:row.huoweiCode,
        }
        this.pdDosageDetailTable.dataSource.push(data);
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
          let selectedArrays = this.$refs.pdDosageDetail.selectedRowIds;
          if(selectedArrays <= 0){
            this.$message.warning("请勾选需要退费的产品");
            return;
          }
          //查找出勾选的产品信息
          let selectedIds = new Array();
          for(let i =0;i<selectedArrays.length;i++){
            let selectId = selectedArrays[i].substring(selectedArrays[i].lastIndexOf("-")+1);
            selectedIds.push(selectId);
          }
          let list = formData.pdDosageDetails;
          for (let i =0; i <list.length;i++){
            //如果包含
            if(selectedIds.indexOf(list[i].id)<0){
              list.splice(i--, 1);
              continue;
            }
            if(Number(list[i].dosageCount) > Number(list[i].leftRefundNum)){
              this.$message.error("["+list[i].productName+"]用量数量不能大于剩余可退数量！");
              return;
            }
            if(Number(list[i].dosageCount) <= 0){
              this.$message.error("["+list[i].productName+"]用量数量必须大于0！");
              return;
            }
          }
          formData.pdDosageDetails = list;
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
        //是否收费标识
        formData.hyCharged=this.hyCharged=="true"?"0":"1";
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
          pdDosageDetails: allValues.tablesValue[0].values,
        }
      },
      /** 关闭按钮 **/
      closeBtn(){
        this.visible = false;
        this.$emit('close');
      },
      // 选择产品后返回
      returnProductStockData(data) {
        let rows = [];
        let { values } = this.$refs.pdDosageDetail.getValuesSync({ validate: false });
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
          this.pdDosageDetailTable.dataSource = values;
          this.addrowsByScanCode(item);
        })
        // 计算总数量和总价格
        this.getTotalNumAndPrice(values);
      },
    }
  }
</script>
<style scoped>
</style>