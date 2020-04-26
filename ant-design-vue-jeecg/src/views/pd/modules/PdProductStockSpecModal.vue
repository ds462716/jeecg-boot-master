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
      <a-form-item    label="清零原因"    :labelCol="labelCol" :wrapperCol="wrapperCol">
        <a-textarea  style="width:100%;"   :rows="5" v-decorator="[ 'reason', validatorRules.reason]" placeholder="请输入原因"></a-textarea>
      </a-form-item>
    </a-form>
  </a-spin>
</a-modal>
</template>
<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import JDictSelectTagExpand from "@/components/dict/JDictSelectTagExpand"
  import ATextarea from "ant-design-vue/es/input/TextArea";

  export default {
    name: "PdProductStockSpecModal",
    components: {
      ATextarea,
      JDictSelectTagExpand,
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:800,
        visible: false,
        model: {},
        typeValue : false,
        confirmLoading: false,
        validatorRules:{
          reason:{rules: [{ required: true, message: '请填写原因!' }]}
        },
        url: {
          edit : "/pd/pdProductStockTotal/updateStockSpecNum",
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
      }
    }
  }
</script>