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

        <a-form-item label="患者姓名" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'patientName', validatorRules.patientName]" placeholder="请输入患者姓名"></a-input>
        </a-form-item>
        <a-form-item label="患者性别" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'patientSex', validatorRules.patientSex]" placeholder="请输入患者性别"></a-input>
        </a-form-item>
        <a-form-item label="患者年龄" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'patientAge', validatorRules.patientAge]" placeholder="请输入患者年龄"></a-input>
        </a-form-item>
        <a-form-item label="就诊卡号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'cardId', validatorRules.cardId]" placeholder="请输入就诊卡号"></a-input>
        </a-form-item>
        <a-form-item label="条形码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'barCode', validatorRules.barCode]" placeholder="请输入条形码"></a-input>
        </a-form-item>
        <a-form-item label="申请医生" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'applyDoctor', validatorRules.applyDoctor]" placeholder="请输入申请医生"></a-input>
        </a-form-item>
        <a-form-item label="申请科室" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'applyDepartment', validatorRules.applyDepartment]" placeholder="请输入申请科室"></a-input>
        </a-form-item>
        <a-form-item label="检验医生" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'testDoctor', validatorRules.testDoctor]" placeholder="请输入检验医生"></a-input>
        </a-form-item>
        <a-form-item label="检验科室" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'testDepartment', validatorRules.testDepartment]" placeholder="请输入检验科室"></a-input>
        </a-form-item>
        <a-form-item label="患者类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'patientType', validatorRules.patientType]" placeholder="请输入患者类型"></a-input>
        </a-form-item>
        <a-form-item label="工作组" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'groupBy', validatorRules.groupBy]" placeholder="请输入工作组"></a-input>
        </a-form-item>
        <a-form-item label="接收日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'receiveDate', validatorRules.receiveDate]" placeholder="请输入接收日期"></a-input>
        </a-form-item>
        <a-form-item label="检验日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'testDate', validatorRules.testDate]" placeholder="请输入检验日期"></a-input>
        </a-form-item>
        <a-form-item label="样本类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'specimenType', validatorRules.specimenType]" placeholder="请输入样本类型"></a-input>
        </a-form-item>
        <a-form-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'state', validatorRules.state]" placeholder="请输入状态"></a-input>
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
    name: "ExInspectionItemsModal",
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
          patientName: {rules: [
          ]},
          patientSex: {rules: [
          ]},
          patientAge: {rules: [
          ]},
          cardId: {rules: [
          ]},
          barCode: {rules: [
          ]},
          applyDoctor: {rules: [
          ]},
          applyDepartment: {rules: [
          ]},
          testDoctor: {rules: [
          ]},
          testDepartment: {rules: [
          ]},
          patientType: {rules: [
          ]},
          groupBy: {rules: [
          ]},
          receiveDate: {rules: [
          ]},
          testDate: {rules: [
          ]},
          specimenType: {rules: [
          ]},
          state: {rules: [
          ]},
        },
        url: {
          add: "/external/exInspectionItems/add",
          edit: "/external/exInspectionItems/edit",
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
          this.form.setFieldsValue(pick(this.model,'patientName','patientSex','patientAge','cardId','barCode','applyDoctor','applyDepartment','testDoctor','testDepartment','patientType','groupBy','receiveDate','testDate','specimenType','state'))
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
        this.form.setFieldsValue(pick(row,'patientName','patientSex','patientAge','cardId','barCode','applyDoctor','applyDepartment','testDoctor','testDepartment','patientType','groupBy','receiveDate','testDate','specimenType','state'))
      },

      
    }
  }
</script>