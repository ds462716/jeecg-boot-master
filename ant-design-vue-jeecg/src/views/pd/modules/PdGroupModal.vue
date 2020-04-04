<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    :maskClosable=disableSubmit
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-form-item label="名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="disableSubmit" autocomplete="off" ref="inputFocus" @change="pinyinTran" v-decorator="[ 'name', validatorRules.name]" placeholder="请输入名称"></a-input>
        </a-form-item>
        <a-form-item label="拼音简码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="disableSubmit" autocomplete="off" v-decorator="[ 'py', validatorRules.py]" ></a-input>
        </a-form-item>
        <a-form-item label="五笔简码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="disableSubmit" autocomplete="off" v-decorator="[ 'wb', validatorRules.wb]" ></a-input>
        </a-form-item>
        <a-form-item label="自定义码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="disableSubmit" autocomplete="off" v-decorator="[ 'zdy', validatorRules.zdy]" ></a-input>
        </a-form-item>
        <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input :disabled="disableSubmit" autocomplete="off" v-decorator="[ 'remarks', validatorRules.remarks]" ></a-input>
        </a-form-item>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import { makeWb } from '@/utils/wubi'
  import {duplicateCheckHasDelFlag } from '@/api/api'

  export default {
    name: "PdGroupModal",
    components: { 
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:800,
        visible: false,
        disableSubmit:false,
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
        validateGroupId:"",
        validatorRules: {
          name: {rules: [
            {required: true, message: '请输入名称!'},
              {
                validator: this.validateName,
              }
          ]},
          py: {rules: [
          ]},
          wb: {rules: [
          ]},
          zdy: {rules: [
          ]},
          remarks: {rules: [
          ]},
        },
        url: {
          add: "/pd/pdGroup/add",
          edit: "/pd/pdGroup/edit",
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
        //编辑时显示图片
        if(record.hasOwnProperty("id")){
          this.validateGroupId = record.id;
        }
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'name','py','wb','zdy','remarks'))
          //获取光标
          let input = this.$refs['inputFocus'];
          input.focus()
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
        this.form.setFieldsValue(pick(row,'name','py','wb','zdy','createTime','updateTime','remarks'))
      },
      pinyinTran(e){
        let val = e.target.value;
        let pinyin = require('js-pinyin');
        pinyin.setOptions({checkPolyphone: false, charCase: 0});
        //let py = pinyin.getFullChars(val);//获取全拼
        let py = pinyin.getCamelChars(val);//获取简码
        this.form.setFieldsValue({py:py});
        let wb = makeWb(val);
        this.form.setFieldsValue({wb:wb});//获取五笔简码
      },
      validateName(rule, value, callback){
        var params = {
          tableName: 'pd_group',
          fieldName: 'name',
          fieldVal: value,
          dataId: this.validateGroupId
        };
        duplicateCheckHasDelFlag(params).then((res) => {
          if (res.success) {
            callback()
          } else {
            callback("组别已存在!")
          }
        })
      },
    }
  }
</script>