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

        <a-form-item label="值" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'value', validatorRules.value]" placeholder="请输入值"></a-input>
        </a-form-item>
        <a-form-item label="含义" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'meaning', validatorRules.meaning]" placeholder="请输入含义"></a-input>
        </a-form-item>
        <a-form-item label="类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-dict-select-tag type="list" @change="handleChange" v-decorator="['type',validatorRules.type]" :trigger-change="true" dictCode="identifier_type" placeholder="请选择类型"/>
        </a-form-item>
        <a-form-item label="长度"  v-if="typeValue" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number  v-decorator="[ 'length', validatorRules.length]" placeholder="请输入长度" style="width: 100%"/>
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
  import JDictSelectTag from "@/components/dict/JDictSelectTag"

  export default {
    name: "PdEncodingIdentifierModal",
    components: { 
      JDictSelectTag,
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
        typeValue : false,
        confirmLoading: false,
        validatorRules:{
        value:{rules: [{ required: true, message: '请输入值!' }]},
        meaning:{},
        type:{rules: [{ required: true, message: '请输入类型!' }]},
        length:{rules: [{ required: true, message: '请输入长度!' }]},
        remarks:{},
        },
        url: {
          add: "/pd/pdEncodingIdentifier/add",
          edit: "/pd/pdEncodingIdentifier/edit",
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
          this.form.setFieldsValue(pick(this.model,'value','meaning','type','length','remarks'))
        })
        //控制长度的显示隐藏
        if(this.model.type==1){
          this.typeValue = true;
        }else{
          this.typeValue = false;
        }
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
        this.form.setFieldsValue(pick(row,'value','meaning','type','length','remarks'))
      },
      //根据选择的类型控制长度的显示隐藏
      handleChange(value){
        if(value=="1"){
          this.typeValue = true;
          this.model.length=null;
        }else{
          this.typeValue = false;
          this.model.length=null;
        }
      }
      
    }
  }
</script>