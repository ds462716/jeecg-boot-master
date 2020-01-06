<template>
  <a-drawer
    :title="title"
    :width="width"
    placement="right"
    :closable="false"
    @close="close"
    :visible="visible">
  
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">

        <a-form-item label="编码名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input @change="pinyinTran"  v-decorator="[ 'name', validatorRules.name]" placeholder="请输入编码名称"></a-input>
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
        <default-table ref="defaultTable"/>
      </a-form>
    </a-spin>
    <a-button type="primary" @click="handleOk">确定</a-button>
    <a-button type="primary" @click="handleCancel">取消</a-button>
  </a-drawer>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { makeWb } from '@/utils/wubi'
  import DefaultTable from './PdIdentifierModal'

  export default {
    name: "PdEncodingRuleModal",
    components: { DefaultTable
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
          add: "/pd/pdEncodingRule/savePdEncodingRule",
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
          this.form.setFieldsValue(pick(this.model,'name','py','wb','zdy','remarks'))
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
            //选择标识符的校验通过后
            this.$refs.defaultTable.$refs.editableTable.getValues((error, pdEncodingRuleDetails) => {
            //this.$refs.defaultTable.$refs.editableTable.getValues((error, pdEncodingRuleDetails) => {
              if(pdEncodingRuleDetails.length>0){
                if(!error){
                  //that.confirmLoading = true;
                  let httpurl = '';
                  let method = '';
                  if(!this.model.id){
                    httpurl+=this.url.add;
                    method = 'post';
                  }else{
                    httpurl+=this.url.edit;
                    method = 'put';
                  }
                  values.pdEncodingRuleDetails = pdEncodingRuleDetails;
                  let formData = Object.assign(this.model, values);
                  console.log(formData)
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
              }else{
                this.$message.info('请添加编码规则')
              }
            });
          }
        })
      },
      handleCancel () {
        this.close()
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'name','py','wb','zdy','codeDetail','codeDesc','codeQuery','totalDigit','remarks'))
      },
      pinyinTran(e){
        var val = e.target.value;
        let pinyin = require('js-pinyin');
        pinyin.setOptions({checkPolyphone: false, charCase: 0});
        //var py = pinyin.getFullChars(val);//获取全拼
        var py = pinyin.getCamelChars(val);//获取简码
        this.form.setFieldsValue({py:py});
        var wb = makeWb(val);
        this.form.setFieldsValue({wb:wb});//获取五笔简码
      },
    }
  }
</script>

<style lang="less" scoped>
/** Button按钮间距 */
  .ant-btn {
    margin-left: 30px;
    margin-bottom: 30px;
    float: right;
  }
</style>