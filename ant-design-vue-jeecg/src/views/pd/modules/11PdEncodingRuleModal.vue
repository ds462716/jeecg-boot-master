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

        <a-form-item label="编码名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'name', validatorRules.name]" placeholder="请输入编码名称"></a-input>
        </a-form-item>
        <a-form-item label="编码名称拼音简码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'py', validatorRules.py]" placeholder="请输入编码名称拼音简码"></a-input>
        </a-form-item>
        <a-form-item label="编码名称五笔简码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'wb', validatorRules.wb]" placeholder="请输入编码名称五笔简码"></a-input>
        </a-form-item>
        <a-form-item label="自定义名称查询码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'zdy', validatorRules.zdy]" placeholder="请输入自定义名称查询码"></a-input>
        </a-form-item>
        <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'remarks', validatorRules.remarks]" placeholder="请输入备注"></a-input>
        </a-form-item>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'

  export default {
    name: "PdEncodingRuleModal",
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
        validatorRules:{
        name:{rules: [{ required: true, message: '请输入编码名称!' }]},
        py:{},
        wb:{},
        zdy:{},
        remarks:{},
        },
        url: {
          add: "/pd/pdEncodingRule/add",
          edit: "/pd/pdEncodingRule/edit",
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
          this.form.setFieldsValue(pick(this.model,'name','py','wb','zdy','codeDetail','codeDesc','codeQuery','totalDigit','remarks'))
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
        this.form.setFieldsValue(pick(row,'name','py','wb','zdy','codeDetail','codeDesc','codeQuery','totalDigit','remarks'))
      },

      
    }
  }
</script>