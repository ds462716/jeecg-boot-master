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
        <a-form-item label="开关名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="isEdit" v-decorator="[ 'name', validatorRules.name]" placeholder="请输入开关名称"></a-input>
        </a-form-item>
        <a-form-item label="开关编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="isEdit" v-decorator="[ 'code', validatorRules.code]" placeholder="请输入开关编码"></a-input>
        </a-form-item>
        <a-form-item label="值" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number  v-decorator="[ 'value', validatorRules.value]" placeholder="请输入删除状态" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'description', validatorRules.description]" placeholder="请输入描述"></a-input>
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'
  import { duplicateCheckHasDelFlag } from '@/api/api'

  export default {
    name: "PdOnOffModal",
    components: { 
      JDate,
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:800,
        visible: false,
        isEdit:false,
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
          name: {rules: [
              {required: true, message: '请输入开关名称!'},
          ]},
          code: {rules: [
              {required: true, message: '请输入开关编号!'},
              {validator: this.validateCode}
          ]},
          description: {rules: [
          ]},
          value: {rules: [
              {required: true, message: '请输入值!'},
          ]},
          remarks: {rules: [
          ]},
        },
        url: {
          add: "/pd/pdOnOff/add",
          edit: "/pd/pdOnOff/edit",
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
        this.isEdit = false;
        if(this.model.id){
          this.isEdit = true;
        }
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'name','code','description','value'))
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
        this.form.setFieldsValue(pick(row,'name','code','description','value'))
      },
      validateCode(rule, value, callback){
        let params = {
          tableName: 'pd_on_off',
          fieldName: 'code',
          fieldVal: value,
          dataId: this.model.id
        };
        duplicateCheckHasDelFlag(params).then((res) => {
          if (res.success) {
            callback();
          } else {
            callback("编号已存在!");
          }
        })
      },
      
    }
  }
</script>