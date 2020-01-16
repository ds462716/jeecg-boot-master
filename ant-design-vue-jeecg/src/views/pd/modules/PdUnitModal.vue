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

        <a-form-item label="单位名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input @change="pinyinTran" v-decorator="[ 'name', validatorRules.name]" placeholder="请输入单位名称"></a-input>
        </a-form-item>
        <a-form-item label="拼音简码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'py', validatorRules.py]" placeholder="请输入拼音简码"></a-input>
        </a-form-item>
        <a-form-item label="五笔码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'wb', validatorRules.wb]" placeholder="请输入五笔码"></a-input>
        </a-form-item>
        <a-form-item label="自定义码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'zdy', validatorRules.zdy]" placeholder="请输入自定义码"></a-input>
        </a-form-item>
        <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'remarks', validatorRules.remarks]" placeholder="请输入备注"></a-input>
        </a-form-item>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction,getAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import { makeWb } from '@/utils/wubi'
  import { duplicateCheckHasDelFlag } from '@/api/api'

  export default {
    name: "PdUnitModal",
    components: {
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:800,
        visible: false,
        model: {},
        unitId:"", // 单位ID
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
            {required: true, message: '请输入单位名称!'},
            {validator: this.validateUnitname}
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
          add: "/pd/pdUnit/add",
          edit: "/pd/pdUnit/edit",
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
        this.unitId = record.id;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'name','py','wb','zdy','remarks'))
        })
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
      validateUnitname(rule, value, callback){
        let params = {
          tableName: 'pd_unit',
          fieldName: 'name',
          fieldVal: value,
          dataId: this.unitId
        };
        duplicateCheckHasDelFlag(params).then((res) => {
          if (res.success) {
            callback();
          } else {
            callback("单位名称已存在!");
          }
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
        this.form.setFieldsValue(pick(row,'name','py','wb','zdy','remarks'))
      },

      
    }
  }
</script>