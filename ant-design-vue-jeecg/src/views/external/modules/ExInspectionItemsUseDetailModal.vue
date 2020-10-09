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

        <a-form-item label="检验项目使用id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'refId', validatorRules.refId]" placeholder="请输入检验项目使用id"></a-input>
        </a-form-item>
        <a-form-item label="产品ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'productId', validatorRules.productId]" placeholder="请输入产品ID"></a-input>
        </a-form-item>
        <a-form-item label="检验包的id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'packageId', validatorRules.packageId]" placeholder="请输入检验包的id"></a-input>
        </a-form-item>
        <a-form-item label="产品条码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'productBarCode', validatorRules.productBarCode]" placeholder="请输入产品条码"></a-input>
        </a-form-item>
        <a-form-item label="产品批号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'batchNo', validatorRules.batchNo]" placeholder="请输入产品批号"></a-input>
        </a-form-item>
        <a-form-item label="有效期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择有效期" v-decorator="[ 'expDate', validatorRules.expDate]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="产品库存明细id-用于出库时记录出库库存id" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'productStockId', validatorRules.productStockId]" placeholder="请输入产品库存明细id-用于出库时记录出库库存id"></a-input>
        </a-form-item>
        <a-form-item label="出库货位编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'outHuoweiCode', validatorRules.outHuoweiCode]" placeholder="请输入出库货位编号"></a-input>
        </a-form-item>
        <a-form-item label="产品数量（出入库数量）" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'productNum', validatorRules.productNum]" placeholder="请输入产品数量（出入库数量）"></a-input>
        </a-form-item>
        <a-form-item label="出库单价" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'sellingPrice', validatorRules.sellingPrice]" placeholder="请输入出库单价"></a-input>
        </a-form-item>
        <a-form-item label="所属部门" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'departId', validatorRules.departId]" placeholder="请输入所属部门"></a-input>
        </a-form-item>
        <a-form-item label="所属父部门" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'departParentId', validatorRules.departParentId]" placeholder="请输入所属父部门"></a-input>
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

  export default {
    name: "ExInspectionItemsUseDetailModal",
    components: { 
      JDate,
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
          refId: {rules: [
          ]},
          productId: {rules: [
          ]},
          packageId: {rules: [
          ]},
          productBarCode: {rules: [
          ]},
          batchNo: {rules: [
          ]},
          expDate: {rules: [
          ]},
          productStockId: {rules: [
          ]},
          outHuoweiCode: {rules: [
          ]},
          productNum: {rules: [
          ]},
          sellingPrice: {rules: [
          ]},
          departId: {rules: [
          ]},
          departParentId: {rules: [
          ]},
        },
        url: {
          add: "/external/exInspectionItemsUseDetail/add",
          edit: "/external/exInspectionItemsUseDetail/edit",
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
          this.form.setFieldsValue(pick(this.model,'refId','productId','packageId','productBarCode','batchNo','expDate','productStockId','outHuoweiCode','productNum','sellingPrice','departId','departParentId'))
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
        this.form.setFieldsValue(pick(row,'refId','productId','packageId','productBarCode','batchNo','expDate','productStockId','outHuoweiCode','productNum','sellingPrice','departId','departParentId'))
      },

      
    }
  }
</script>