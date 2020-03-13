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

        <a-form-item label="合并申购单号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'mergeOrderNo', validatorRules.mergeOrderNo]" placeholder="请输入合并申购单号"></a-input>
        </a-form-item>
        <a-form-item label="合并申购日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择合并申购日期" v-decorator="[ 'purchaseDate', validatorRules.purchaseDate]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="合并科室ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'departId', validatorRules.departId]" placeholder="请输入合并科室ID"></a-input>
        </a-form-item>
        <a-form-item label="操作人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'mergeBy', validatorRules.mergeBy]" placeholder="请输入操作人"></a-input>
        </a-form-item>
        <a-form-item label="合并状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'auditStatus', validatorRules.auditStatus]" placeholder="请输入合并状态"></a-input>
        </a-form-item>
        <a-form-item label="供应商商受理状态.0待上传,1待接收,2已接收,3已拒绝,4已收货" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'supplierStatus', validatorRules.supplierStatus]" placeholder="请输入供应商商受理状态.0待上传,1待接收,2已接收,3已拒绝,4已收货"></a-input>
        </a-form-item>
        <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'remarks', validatorRules.remarks]" placeholder="请输入备注"></a-input>
        </a-form-item>
        <a-form-item label="创建人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'createBy', validatorRules.createBy]" placeholder="请输入创建人"></a-input>
        </a-form-item>
        <a-form-item label="创建时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择创建时间" v-decorator="[ 'createTime', validatorRules.createTime]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="更新人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'updateBy', validatorRules.updateBy]" placeholder="请输入更新人"></a-input>
        </a-form-item>
        <a-form-item label="更新时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择更新时间" v-decorator="[ 'updateTime', validatorRules.updateTime]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="删除标识" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'delFlag', validatorRules.delFlag]" placeholder="请输入删除标识"></a-input>
        </a-form-item>
        <a-form-item label="所属父部门ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'departParentId', validatorRules.departParentId]" placeholder="请输入所属父部门ID"></a-input>
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
    name: "PdPurchaseOrderMergeModal",
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
          mergeOrderNo: {rules: [
          ]},
          purchaseDate: {rules: [
          ]},
          departId: {rules: [
          ]},
          mergeBy: {rules: [
          ]},
          auditStatus: {rules: [
          ]},
          supplierStatus: {rules: [
          ]},
          remarks: {rules: [
          ]},
          createBy: {rules: [
          ]},
          createTime: {rules: [
            {required: true, message: '请输入创建时间!'},
          ]},
          updateBy: {rules: [
          ]},
          updateTime: {rules: [
            {required: true, message: '请输入更新时间!'},
          ]},
          delFlag: {rules: [
            {required: true, message: '请输入删除标识!'},
          ]},
          departParentId: {rules: [
            {required: true, message: '请输入所属父部门ID!'},
          ]},
        },
        url: {
          add: "/pd/pdPurchaseOrderMerge/add",
          edit: "/pd/pdPurchaseOrderMerge/edit",
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
          this.form.setFieldsValue(pick(this.model,'mergeOrderNo','purchaseDate','departId','mergeBy','auditStatus','supplierStatus','remarks','createBy','createTime','updateBy','updateTime','delFlag','departParentId'))
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
        this.form.setFieldsValue(pick(row,'mergeOrderNo','purchaseDate','departId','mergeBy','auditStatus','supplierStatus','remarks','createBy','createTime','updateBy','updateTime','delFlag','departParentId'))
      },

      
    }
  }
</script>