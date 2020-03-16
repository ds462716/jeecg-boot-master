<template>
  <j-modal
    :visible="visible"
    :width="1200"
    :title="title"
    :lockScroll="lockScroll"
    :fullscreen="fullscreen"
    :switchFullscreen="switchFullscreen"
    @cancel="handleCancel"
    footer="null"
  >

    <a-spin :spinning="confirmLoading">
      <div style="background:#ECECEC; padding:20px">
        <a-card title="" style="margin-bottom: 10px;">
          <a-form :form="form">
            <a-row>
              <a-col :span="6">
                <a-form-item label="用量单号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="[ 'dosageNo']" placeholder="请输入用量单号"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="用量日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="[ 'dosageDate']" placeholder="请输入用量日期"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="操作人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="[ 'dosageBy']" placeholder="请输入操作人"></a-input>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="6">
                <a-form-item label="所属部门" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-decorator="[ 'departId']" placeholder="请输入所属部门"></a-input>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-card>

        <!-- 产品列表区域 -->
        <a-card style="margin-bottom: 10px;">
          <a-tabs v-model="activeKey" @change="handleChangeTabs">
            <a-tab-pane tab="产品明细"  :forceRender="true">
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


       <!-- <a-card style="margin-bottom: 10px;">
          <a-form :form="form">
            <a-form-item label="用量总数" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="[ 'amountCount', validatorRules.amountCount]" placeholder="请输入用量总数" style="width: 100%"/>
            </a-form-item>
            <a-form-item label="用量总金额" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="[ 'amountMoney', validatorRules.amountMoney]" placeholder="请输入用量总金额" style="width: 100%"/>
            </a-form-item>
            <a-form-item label="病人信息" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'patientInfo', validatorRules.patientInfo]" placeholder="请输入病人信息"></a-input>
            </a-form-item>
            <a-form-item label="病人详细信息" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'patientDetailInfo', validatorRules.patientDetailInfo]" placeholder="请输入病人详细信息"></a-input>
            </a-form-item>
            <a-form-item label="执行科室id" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'exeDeptId', validatorRules.exeDeptId]" placeholder="请输入执行科室id"></a-input>
            </a-form-item>
            <a-form-item label="执行科室名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'exeDeptName', validatorRules.exeDeptName]" placeholder="请输入执行科室名称"></a-input>
            </a-form-item>
            <a-form-item label="手术科室id" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'oprDeptId', validatorRules.oprDeptId]" placeholder="请输入手术科室id"></a-input>
            </a-form-item>
            <a-form-item label="手术科室名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'oprDeptName', validatorRules.oprDeptName]" placeholder="请输入手术科室名称"></a-input>
            </a-form-item>
            <a-form-item label="手术医生id" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'surgeonId', validatorRules.surgeonId]" placeholder="请输入手术医生id"></a-input>
            </a-form-item>
            <a-form-item label="手术医生名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'surgeonName', validatorRules.surgeonName]" placeholder="请输入手术医生名称"></a-input>
            </a-form-item>
            <a-form-item label="开方医生id" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'sqrtDoctorId', validatorRules.sqrtDoctorId]" placeholder="请输入开方医生id"></a-input>
            </a-form-item>
            <a-form-item label="开方医生名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'sqrtDoctorName', validatorRules.sqrtDoctorName]" placeholder="请输入开方医生名称"></a-input>
            </a-form-item>
            <a-form-item label="住院号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'inHospitalNo', validatorRules.inHospitalNo]" placeholder="请输入住院号"></a-input>
            </a-form-item>

            <a-form-item label="所属病区id" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'subordinateWardId', validatorRules.subordinateWardId]" placeholder="请输入所属病区id"></a-input>
            </a-form-item>
            <a-form-item label="所属病区名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'subordinateWardName', validatorRules.subordinateWardName]" placeholder="请输入所属病区名称"></a-input>
            </a-form-item>
            <a-form-item label="门诊号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'outpatientNumber', validatorRules.outpatientNumber]" placeholder="请输入门诊号"></a-input>
            </a-form-item>
            <a-form-item label="手术编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'operativeNumber', validatorRules.operativeNumber]" placeholder="请输入手术编号"></a-input>
            </a-form-item>
            <a-form-item label="是否有his接口标识符0没有，1有" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'displayFlag', validatorRules.displayFlag]" placeholder="请输入是否有his接口标识符0没有，1有"></a-input>
            </a-form-item>
            <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'remarks', validatorRules.remarks]" placeholder="请输入备注"></a-input>
            </a-form-item>

          </a-form>
        </a-card>-->
      </div>
    </a-spin>
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
    name: "PdDosageModal",
    mixins: [JEditableTableMixin],
    components: { 
    },
    data () {
      return {
        width:800,
        visible: false,
        model: {},
        title: '这里是标题',
        lockScroll: true,
        fullscreen: true,
        switchFullscreen: true,
        totalSum:'0',
        totalPrice:'0.0000',
        activeKey: 'pdDosageDetail',
        refKeys: ['pdDosageDetail',],
        queryParam:{},
        pdDosageDetailTable: {
          loading: false,
          dataSource: [],
          columns: [/*
            { title: '库存明细ID', key: 'productStockId' },
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
            { title: '出库货位', key: 'outHuoweiName', width:"100px" },
            { title: '出库货位编号', key: 'outHuoweiCode', type: FormTypes.hidden },
            { title: '入库货位', key: 'inHuoweiCode', type: FormTypes.select, width:"150px", options: [],allowSearch:true, placeholder: '${title}' },
          */]
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
          init:"/pd/pdStockRecordOut/initModal",
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
          let fieldval = pick(this.model,'recordNo','outType','submitBy','submitByName','submitDate','applyNo','allocationNo',
            'inDepartId','outDepartId','outDepartName','remarks')
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
              if(this.model.id){ //详情页
                this.disableSubmit2 = true;
                this.showApplyBtn = false;
                this.showAllocationBtn = false;
                this.showOrderTable = true;
                this.pdStockRecordDetailTable.dataSource = res.result.pdStockRecordDetailList || [];

                if(res.result.outType == "1"){
                  this.orderTableTitle = "申领单明细";
                  let pdApplyDetailList = res.result.pdApplyDetailList || [];
                  pdApplyDetailList.forEach((item, idx) => {
                    item.orderNo = item.applyNo;
                    item.productNum = item.applyNum;
                  })
                  this.pdOrderDetailTable.dataSource = pdApplyDetailList;
                }else if(res.result.outType == "2"){
                  this.orderTableTitle = "";
                }else if(res.result.outType == "3"){
                  this.orderTableTitle = "调拨单明细";
                  let pdApplyDetailList = res.result.pdAllocationDetailList || [];
                  pdApplyDetailList.forEach((item, idx) => {
                    item.orderNo = item.allocationNo;
                    item.productNum = item.allocationNum;
                  })
                  this.pdOrderDetailTable.dataSource = pdApplyDetailList;
                }

                this.goodsAllocationList = res.result.goodsAllocationList;
                this.pdStockRecordDetailTable.columns.forEach((item, idx) => {
                  if(item.key === "inHuoweiCode"){
                    item.options = this.goodsAllocationList;
                  }
                })

                this.totalSum = res.result.totalSum;
                this.totalPrice = res.result.totalPrice.toString();
              }else{  // 新增页
                this.disableSubmit2 = false;
                this.initData = res.result;
                this.submitDateStr = res.result.submitDateStr;
                let fieldval = pick(this.initData,'recordNo','outType','submitBy','submitByName','submitDate','applyNo','allocationNo',
                  'inDepartId','outDepartId','outDepartName','remarks');
                this.form.setFieldsValue(fieldval);
                //获取光标
                this.$refs['productNumberInput'].focus();

                // 从申领单或调拨单直接打开
                if(this.args && this.args.outType){
                  let outType = this.args.outType;//	1-申领出库; 2-科室出库; 3-调拨出库
                  let data = this.args.data;
                  let inDepartId = this.args.inDepartId;
                  this.form.setFieldsValue({outType:outType});
                  this.form.setFieldsValue({inDepartId:inDepartId});
                  this.showApplyBtn = false;
                  this.showAllocationBtn = false;
                  this.showOrderTable = true;
                  this.disableSubmit2 = true;
                  if(outType == "1"){
                    this.orderTableTitle = "申领单明细";
                    this.returnApplyOrderData(data);
                  }else if(outType == "3"){
                    this.orderTableTitle = "调拨单明细";
                    this.returnAllocationData(data);
                  }
                }
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
        this.visible = false;
      },
      handleOk () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            let formData = Object.assign(this.model, values);
            console.log("表单提交数据",formData)
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })
          }
         
        })
      },
      handleCancel () {
        this.close()
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'dosageNo','dosageDate','amountCount','amountMoney','patientInfo','patientDetailInfo','exeDeptId','exeDeptName','oprDeptId','oprDeptName','surgeonId','surgeonName','sqrtDoctorId','sqrtDoctorName','inHospitalNo','dosageBy','subordinateWardId','subordinateWardName','outpatientNumber','operativeNumber','displayFlag','remarks','departId','departParentId'))
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
              let pdProductStock = res.result[0];
              if(!pdProductStock){
                //清空扫码框
                this.clearQueryParam();
                this.$message.error("条码解析失败，请校验条码是否正确！");
                return;
              }
              let isAddRow = true;// 是否增加一行
              // 循环表格数据
              let { values } = this.$refs.pdDosageDetail.getValuesSync({ validate: false });
              if(values.length > 0) { //表格有数据
                for(let item of values){
                  if(pdProductStock.id == item.productStockId){// 库存明细ID一致，就+1
                    isAddRow = false;
                    if(Number(item.productNum) + 1 > Number(item.stockNum)){
                      //清空扫码框
                      this.clearQueryParam();
                      this.$message.error("["+item.productName+"]出库数量不能大于库存数量！");
                      return;
                    }

                    let productNum = Number(item.productNum) + 1;
                    let outTotalPrice = (Number(item.sellingPrice) * Number(productNum)).toFixed(4);

                    this.$refs.pdDosageDetail.setValues([{rowKey: item.id, values: {
                        productNum: productNum,outTotalPrice: outTotalPrice }}]);
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
        let outType = this.form.getFieldValue("outType");
        // 校验是否选择入库科室
        if(!this.checkInDepart()){
          this.$message.error("请选择入库科室！");
          return;
        }

        if(outType == "1"){        //申领单
          if(!this.applyNo){
            this.$message.error("请先导入申领单！");
            return;
          }
          this.$refs.pdChooseProductStockListModel.show({applyNo:this.applyNo});
        }else if(outType == "2"){
          this.$refs.pdChooseProductStockListModel.show({});
        }else if(outType == "3"){  //调拨单
          if(!this.allocationNo){
            this.$message.error("请先导入调拨单！");
            return;
          }
          this.$refs.pdChooseProductStockListModel.show({allocationNo:this.allocationNo});
        }else{
          this.$message.error("请选择出库类型！");
          return;
        }
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
            totalSum = totalSum + Number(item.productNum);
            totalPrice = totalPrice + Number(item.outTotalPrice);
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
            if(column.key === "productNum"){
              let { values } = target.getValuesSync({ validate: false });
              for(let item of values){
                if(item.id == row.id && Number(value) > Number(item.stockNum)){
                  this.$message.error("["+row.productName+"]出库数量不能大于库存数量！");
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
            }
          }
        }
      },
    }
  }
</script>