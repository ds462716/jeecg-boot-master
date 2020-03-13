<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-form-item label="用量单号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'dosageNo', validatorRules.dosageNo]" placeholder="请输入用量单号"></a-input>
        </a-form-item>
        <a-form-item label="用量日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'dosageDate', validatorRules.dosageDate]" placeholder="请输入用量日期"></a-input>
        </a-form-item>
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
        <a-form-item label="操作人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'dosageBy', validatorRules.dosageBy]" placeholder="请输入操作人"></a-input>
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
        <a-form-item label="所属部门" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'departId', validatorRules.departId]" placeholder="请输入所属部门"></a-input>
        </a-form-item>
        <a-form-item label="所属医院" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'departParentId', validatorRules.departParentId]" placeholder="请输入所属医院"></a-input>
        </a-form-item>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: "PdDosageModal",
    components: { 
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:800,
        visible: false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
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
          add: "/pd/pdDosage/add",
          edit: "/pd/pdDosage/edit",
        }
      }
    },
    created () {
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'dosageNo','dosageDate','amountCount','amountMoney','patientInfo','patientDetailInfo','exeDeptId','exeDeptName','oprDeptId','oprDeptName','surgeonId','surgeonName','sqrtDoctorId','sqrtDoctorName','inHospitalNo','dosageBy','subordinateWardId','subordinateWardName','outpatientNumber','operativeNumber','displayFlag','remarks','departId','departParentId'))
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

      
    }
  }
</script>