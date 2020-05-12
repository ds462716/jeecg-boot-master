<template>
  <a-modal
  :title="title"
  :width="500"
  :visible="visible"
  :confirmLoading="confirmLoading"
  @ok="handleOk"
  @cancel="handleCancel"
  cancelText="关闭">
  <a-spin :spinning="confirmLoading">
    <a-form :form="form">
      <a-form-item v-if="model.type=='Up'"  label="库存上限"    :labelCol="labelCol" :wrapperCol="wrapperCol">
        <a-input  style="width:100%;"  ref="inputFocus" v-decorator="[ 'limitUp', validatorRules.limitUp]" placeholder="请输入值"></a-input>
      </a-form-item>
        <a-form-item  v-if="model.type=='Down'"  label="库存下限"    :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input  style="width:100%;" ref="inputFocus" v-decorator="[ 'limitDown', validatorRules.limitDown]" placeholder="请输入值"></a-input>
        </a-form-item>
      <a-form-item  v-if="model.type=='Down'"  label="自动补货量"    :labelCol="labelCol" :wrapperCol="wrapperCol">
        <a-input  style="width:100%;" ref="inputFocus" v-decorator="[ 'autoNum', validatorRules.autoNum]" placeholder="请输入值"></a-input>
        <span style="color: red">  补货数量为空或为0时,不会自动补货。</span>
      </a-form-item>
    </a-form>
  </a-spin>
</a-modal>
</template>
<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import JDictSelectTagExpand from "@/components/dict/JDictSelectTagExpand"

  export default {
    name: "PdProductStockTotalModal",
    components: {
      JDictSelectTagExpand,
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
          limitUp:{rules: [{ required: true, message: '请输入值!' }
          , {pattern: '^(?:[1-9][0-9]*(?:\\.[0-9]+)?|0\\.(?!0+$)[0-9]+)$',
                message: '格式不正确'
              }]},
          limitDown:{rules: [{ required: true, message: '请输入值!'},
              {pattern: '^(?:[1-9][0-9]*(?:\\.[0-9]+)?|0\\.(?!0+$)[0-9]+)$',
                message: '格式不正确'
              }]},
          /*autoNum:{rules: [{ required: true, message: '请输入值!'},
              {pattern: '^(?:[1-9][0-9]*(?:\\.[0-9]+)?|0\\.(?!0+$)[0-9]+)$',
                message: '格式不正确'
              }]},*/
          autoNum:{},
        },
        url: {
          edit : "/pd/pdProductStockTotal/updateProductStock",
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
            let formData =Object.assign(this.model, values);
            console.log("表单提交数据",formData)
            httpAction(this.url.edit,formData,"put").then((res)=>{
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
        this.form.setFieldsValue(pick(row,'stockNum'))
      },
    }
  }
</script>