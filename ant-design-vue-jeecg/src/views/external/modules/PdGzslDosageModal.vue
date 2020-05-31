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
              <a-form v-show="!disableSubmit">
                <a-row>
                  <a-col :md="6" :sm="8">
                    <a-form-item label="唯一码编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <a-input ref="productNumberInput" v-focus placeholder="请输入唯一码编号" v-model="queryParam.productNumber" @keyup.enter.native="searchQuery(0)"></a-input>
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
               <!-- <a-button type="primary" icon="plus" @click="chooseProductList">选择产品</a-button>
                <a-button type="primary" icon="plus" @click="choosePackageList" style="margin-left: 8px">选择定数包</a-button>-->
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
                @selectRowChange="handleSelectRowChange"
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
                  <a-col :md="16" :sm="8">
                    <a-form-item label="执行收费" :labelCol="{span: 3}" :wrapperCol="{span: 20}">
                      <a-switch :disabled="disableSubmit" v-model="hyCharged"/>
                      <span style="color: red">  不选中的情况下，只在当前系统保存病人信息，医院系统中并不记账，此功能只作产品追溯用。</span>
                    </a-form-item>
                  </a-col>
                </a-row>
                <!-- <a-row>
                   <a-col :md="6" :sm="8">
                     <a-form-item label="病人类型"  :labelCol="labelCol" :wrapperCol="wrapperCol">
                       <a-select :disabled="disableSubmit"   v-decorator="[ 'patientType',validatorRules.patientType]"   placeholder="请选择病人类型">
                         <a-select-option value="1">住院病人</a-select-option>
                         <a-select-option value="2">门诊病人</a-select-option>
                       </a-select>
                     </a-form-item>
                   </a-col>-->
                <!-- <a-col :md="16" :sm="8">
                    <a-form-item  label="项目类别" :labelCol="{span: 3}" :wrapperCol="{span: 20}">
                     <template>
                       <a-radio-group   :disabled="disableSubmit" v-decorator="['prjType',validatorRules.prjType]" placeholder="项目类别">
                         <a-radio value="0">手术项目</a-radio>
                         <a-radio value="1">检查项目</a-radio>
                         <a-radio value="2">检验项目</a-radio>
                       </a-radio-group>
                     </template>
                     </a-form-item>
                 </a-col>
               </a-row>-->
                <a-row>
                  <a-col :md="6" :sm="8" v-if="hyCharged">
                    <a-form-item label="病历号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <a-input autocomplete="off" :disabled="disableSubmit" v-decorator="[ 'medicalRecordNo', validatorRules.medicalRecordNo]"  @keyup.enter.native="selectHis(0)"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="8" v-else="!hyCharged">
                    <a-form-item label="病历号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <a-input :disabled="disableSubmit" v-decorator="[ 'medicalRecordNo']" @keyup.enter.native="selectHis(0)"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="8">
                    <a-form-item label="门诊号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <a-input autocomplete="off" :disabled="disableSubmit" v-decorator="[ 'outpatientNumber', validatorRules.outpatientNumber]" @keyup.enter.native="selectHis(1)"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="8" v-if="hyCharged">
                    <a-form-item label="病人姓名" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <a-input :disabled="true" v-decorator="[ 'patientInfo', validatorRules.patientInfo]" ></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="8" v-else="!hyCharged">
                    <a-form-item label="病人姓名" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <a-input :disabled="true" v-decorator="[ 'patientInfo']" ></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="8">
                    <a-form-item label="项目编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <a-input :disabled="true" v-decorator="[ 'operativeNumber', validatorRules.operativeNumber]" ></a-input>
                    </a-form-item>
                  </a-col>
                </a-row>
                <a-row>
                  <a-col :md="6" :sm="8">
                    <a-form-item label="项目名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <a-input :disabled="true" v-decorator="[ 'operationName', validatorRules.operationName]" ></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="8">
                    <a-form-item label="执行科室" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <a-input :disabled="true" v-decorator="[ 'oprDeptName', validatorRules.oprDeptName]" ></a-input>
                    </a-form-item>
                    <!-- 执行科室id -->
                    <a-form-item >
                      <a-input type="hidden" v-decorator="[ 'oprDeptId']"></a-input>
                    </a-form-item>
                    <!-- 开单科室id -->
                    <a-form-item >
                      <a-input type="hidden" v-decorator="[ 'sqrtDoctorId']"></a-input>
                    </a-form-item>
                    <!-- 住院次数 -->
                    <a-form-item >
                      <a-input type="hidden" v-decorator="[ 'hospitalizationsNum']"></a-input>
                    </a-form-item>
                    <a-form-item >
                      <a-input type="hidden" v-decorator="[ 'extension1']"></a-input>
                    </a-form-item>
                    <a-form-item >
                      <a-input type="hidden" v-decorator="[ 'extension2']"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="8">
                    <a-form-item label="住院号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <a-input :disabled="true" v-decorator="[ 'inHospitalNo', validatorRules.inHospitalNo]"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="8">
                    <a-form-item label="诊治医生" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <a-input :disabled="true" v-decorator="[ 'surgeonName', validatorRules.surgeonName]" ></a-input>
                    </a-form-item>
                  </a-col>
                </a-row>
                <a-form-item label="病人详细信息" :labelCol="labelCol2" :wrapperCol="wrapperCol2">
                  <a-textarea :disabled="true" v-decorator="[ 'patientDetailInfo']"></a-textarea>
                </a-form-item>
                <a-form-item label="备注" :labelCol="labelCol2" :wrapperCol="wrapperCol2">
                  <a-textarea :disabled="disableSubmit" v-decorator="[ 'remarks', validatorRules.remarks]" ></a-textarea>
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
      <a-button @click="submitBtn" v-show="!disableSubmit" type="primary" :loading="confirmLoading" style="margin-right: 15px;">提  交</a-button>
    </template>

    <pd-choose-dosage-list-model   ref="PdChooseDosageListModel" @ok="modalFormOk"></pd-choose-dosage-list-model>
  </j-modal>
</template>

<script>

  import Vue from 'vue'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import { FormTypes,getRefPromise,validateFormAndTables } from '@/utils/JEditableTableUtil'
  import {stockScanCode} from '@/utils/barcode'
  import {httpAction, deleteAction, getAction} from '@/api/manage'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import PdChooseDosageListModel from "../../pd/modules/PdChooseDosageListModel"
  import {uniqueScanCode} from '@/utils/barcode'

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
    name: "PdGzslDosageModal",
    mixins: [JEditableTableMixin],
    components: {
      PdChooseDosageListModel,
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
        sRowIds:[],//选中行id
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
            { title: '唯一码', key: 'refBarCode', type: FormTypes.normal, width:"250px" },
            { title: '产品名称', key: 'productName', type: FormTypes.normal,width:"220px" },
            { title: '产品编号', key: 'productNumber', width:"200px" },
            { title: '产品条码', key: 'productBarCode', type: FormTypes.input, disabled:true, width:"200px" },
            { title: '规格', key: 'spec', width:"200px" },
            { title: '批号', key: 'batchNo', width:"100px" },
            { title: '单位', key: 'unitName', width:"50px" },
            { title: '有效期', key: 'expDate', width:"100px" },
            /*{ title: '入库单价', key: 'purchasePrice', width:"80px" },*/
            { title: '出库单价', key: 'sellingPrice', width:"80px" },
            {
              title: '用量数量', key: 'dosageCount', type: FormTypes.input, disabled:true, width:"80px",
              placeholder: '${title}', defaultValue: '1',
              validateRules: [{ required: true, message: '${title}不能为空' },{ pattern: '^-?\\d+\\.?\\d*$',message: '${title}的格式不正确' }]
            },
            { title: '用量金额', key: 'amountMoney', type: FormTypes.input, disabled:true, width:"100px" },
            { title: '库存数量', key: 'stockNum', width:"80px" },
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
          dosageNo: {rules: [{required: true, message: '请输入用量单号!'},]},
          patientType: {rules: []},
          dosageDate: {rules: []},
          amountCount: {rules: []},
          amountMoney: {rules: []},
          patientInfo: {rules: [{required: true, message: '请输入病人信息!'},]},
          patientDetailInfo: {rules: []},
          exeDeptId: {rules: []},
          exeDeptName: {rules: []},
          oprDeptId: {rules: []},
          oprDeptName: {rules: []},
          surgeonId: {rules: []},
          surgeonName: {rules: []},
          prjType:{rules:[]},
          hospitalizationsNum:{rules:[]},
          sqrtDoctorId: {rules: []},
          sqrtDoctorName: {rules: []},
          inHospitalNo: {rules: []},
          dosageBy: {rules: []},
          subordinateWardId: {rules: []},
          subordinateWardName: {rules: []},
          outpatientNumber: {rules: []},
          operativeNumber: {rules: []},
          displayFlag: {rules: []},
          remarks: {rules: []},
          departId: {rules: [{required: true, message: '请输入所属部门!'},]},
          departParentId: {rules: [{required: true, message: '请输入所属医院!'},]},
        },
        url: {
          init:"/pd/pdDosage/initModal",
          submit: "/pd/newPdDosage/uniqueSubmit",
          add: "/pd/pdDosage/add",
          edit: "/pd/pdDosage/edit",
          departList:"/pd/pdDepart/getSysDepartList",
          queryPatientInfoList:"/pd/newPdDosage/queryPatientInfoList",
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
                // 详情页
                this.initData = res.result;
                if(res.result.hyCharged==0){
                  this.hyCharged = true;
                }else{
                  this.hyCharged = false;
                }
                this.totalSum = res.result.totalSum;
                this.totalPrice = res.result.totalPrice;
                this.pdDosageDetailTable.dataSource = res.result.pdDosageDetails || [];
                let fieldval = pick(this.initData,'dosageNo','dosageDate','departName','outHuoweiCode','dosageByName','inHospitalNo','patientInfo','operativeNumber','operationName','outpatientNumber','medicalRecordNo','sqrtDoctorId','oprDeptId','oprDeptName','exeDeptId','exeDeptName','surgeonName','surgeonId','patientDetailInfo','hospitalizationsNum','remarks','extension1','extension2');
                this.form.setFieldsValue(fieldval);
                this.goodsAllocationList = res.result.goodsAllocationList;
                //获取光标
                this.$refs['productNumberInput'].focus();
              }else{  // 新增页
                this.hyCharged = true;
                this.prjType = "1";
                this.initData = res.result;
                let fieldval = pick(this.initData,'dosageNo','dosageDate','departName','outHuoweiCode','dosageByName','inHospitalNo','patientInfo','operativeNumber','operationName','outpatientNumber','medicalRecordNo','sqrtDoctorId','oprDeptId','oprDeptName','exeDeptId','exeDeptName','surgeonName','surgeonId','patientDetailInfo','hospitalizationsNum','remarks','extension1','extension2');
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
        this.sRowIds = [];
        this.visible = false;
        this.pdDosageDetailTable.dataSource = [];
        this.eachAllTable((item) => {
          item.initialize()
        })
      },
      handleCancel () {
        this.close()
      },


      selectHis(num){//查詢病人信息   num:0：住院病人查詢   1：門診病人查詢
        let  medicalRecordNo='';
        let  outpatientNumber='';
        if(num=='0'){
          medicalRecordNo=this.form.getFieldValue('medicalRecordNo');
          if(medicalRecordNo=="" || medicalRecordNo==null){
            this.$message.error("请输入病历号！");
            return;
          }
        }else{
          outpatientNumber=this.form.getFieldValue('outpatientNumber');
          if(outpatientNumber=="" || outpatientNumber==null){
            this.$message.error("请输入门诊号！");
            return;
          }
        }
        let  formData={medicalRecordNo:medicalRecordNo,
          outpatientNumber:outpatientNumber,prjType:num};
        getAction(this.url.queryPatientInfoList,formData).then((res)=>{
          if (res.success) {
            if(res.result.length==1){
              res.result[0].patientDetailInfo="姓名:"+res.result[0].patientInfo+",性别:"+res.result[0].fsfXb+",出生日期:"+res.result[0].fsfCsrq;
              let fieldval = pick(res.result[0],'inHospitalNo','patientInfo','operativeNumber','operationName','outpatientNumber','medicalRecordNo','sqrtDoctorId','oprDeptId','oprDeptName','exeDeptId','exeDeptName','surgeonName','surgeonId','patientDetailInfo','hospitalizationsNum','remarks','extension1','extension2');
              this.form.setFieldsValue(fieldval);
            }else{
              this.$refs.PdChooseDosageListModel.width = 1550;
              this.$refs.PdChooseDosageListModel.show(res.result);
            }
          } else {
            this.$message.error(res.message);
          }
        })

      },


      modalFormOk (formData) { //选择病人信息确定后返回所选择的数据
        formData.patientDetailInfo="姓名:"+formData.patientInfo+",性别:"+formData.fsfXb+",出生日期:"+formData.fsfCsrq;
        let fieldval = pick(formData,'inHospitalNo','patientInfo','operativeNumber','operationName','outpatientNumber','medicalRecordNo','sqrtDoctorId','oprDeptId','oprDeptName','exeDeptId','exeDeptName','surgeonName','surgeonId','patientDetailInfo','hospitalizationsNum','remarks','extension1','extension2');
        this.form.setFieldsValue(fieldval);
      },

      // 扫码查询
      searchQuery(num) {
        let productNumber = this.queryParam.productNumber;
        if(!productNumber){
          //清空扫码框
          this.clearQueryParam();
          this.$message.error("请输入唯一码编号！");
          return;
        }
        if(num == 0){       //产品编号扫码
          //解析条码
          uniqueScanCode(productNumber).then((res) => {
            if(res.code == "200" || res.code == "203"){
              let pdProductStock = res.result;
              if(!pdProductStock){
                //清空扫码框
                this.clearQueryParam();
                this.$message.error("条码解析失败，请校验条码是否正确！");
                return;
              }
              // 循环表格数据
              let { values } = this.$refs.pdDosageDetail.getValuesSync({ validate: false });
              this.pdDosageDetailTable.dataSource = values;
              //条码新增一行
              this.addrowsByScanCode(pdProductStock);
              // 计算总数量和总价格
              this.getTotalNumAndPrice(values);
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
          });
        }
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
        if(this.sRowIds.length <= 0){
          this.totalSum = "0";
          this.totalPrice = "0.0000";
          return;
        }

        this.$nextTick(() => {
          let {values} = this.$refs.pdDosageDetail.getValuesSync({validate: false});
          let totalSum = 0;
          let totalPrice = 0;
          values.forEach((item, idx) => {
            if(this.sRowIds.indexOf(item.id)>=0){
              totalSum = totalSum + Number(item.dosageCount);
              totalPrice = totalPrice + Number(item.amountMoney);
            }
          })
          this.totalSum = totalSum;
          this.totalPrice = totalPrice.toFixed(4);
        })
      },

      handleSelectRowChange(selectedIds){
        this.sRowIds = selectedIds;
        this.getTotalNumAndPrice([]);
      },
      //清空扫码框
      clearQueryParam(){
        this.queryParam.productNumber = "";
        this.queryParam.productBarCode = "";
        this.$refs.productNumberInput.focus();
      },
      // 扫码 调用 新增一行
      addrowsByScanCode(row){
        let { values } = this.$refs.pdDosageDetail.getValuesSync({ validate: false });
        //判断是否已经存在唯一码值 存在不新加
        let flag = true;
        if(values.length > 0){
          values.forEach((value, idx) => {
            if(row.refBarCode == value.refBarCode){
              this.$message.error("列表中已存在该唯一码！");
              flag = false;
            }
          });
        }
        if(!flag){
          return ;
        }
        let data = {
          productStockId:row.id,
          refBarCode:row.refBarCode,
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
          /*purchasePrice:row.purchasePrice,*/
          amountMoney:Number(!row.sellingPrice ? 0 : row.sellingPrice).toFixed(4),
          stockNum:row.stockNum,
          chargeCode:row.chargeCode,
          isChargeText:row.isCharge=="0"?"是":"否",
          isCharge:row.isCharge,
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
            list[i].id=null;
            if(Number(list[i].dosageCount) > Number(list[i].stockNum)){
              this.$message.error("["+list[i].productName+"]用量数量不能大于库存数量！");
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
        if(formData.inHospitalNo && formData.outpatientNumber){
          this.$message.error("请先根据住院号或门诊号查询病人信息！");
          return;
        }
        let url = this.url.submit, method = 'post'
        if (this.model.id) {
          url = this.url.edit
          method = 'put'
        }
        this.confirmLoading = true
        //是否收费标识
        formData.hyCharged=this.hyCharged==true?"0":"1";
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

    }
  }
</script>
<style scoped>
</style>