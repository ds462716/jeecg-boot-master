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

        <a-form-item label="定数包ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'packageId', validatorRules.packageId]" placeholder="请输入定数包ID"></a-input>
        </a-form-item>
        <a-form-item label="定数包名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'productId', validatorRules.productId]" placeholder="请输入定数包名称"></a-input>
        </a-form-item>
        <a-form-item label="库存ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'stockId', validatorRules.stockId]" placeholder="请输入库存ID"></a-input>
        </a-form-item>
        <a-form-item label="定数包流水码" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'packageBarCode', validatorRules.packageBarCode]" placeholder="请输入定数包流水码"></a-input>
        </a-form-item>
        <a-form-item label="批号" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'batchNo', validatorRules.batchNo]" placeholder="请输入批号"></a-input>
        </a-form-item>
        <a-form-item label="有效期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择有效期" v-decorator="[ 'expDate', validatorRules.expDate]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="打包数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input-number v-decorator="[ 'packageNum', validatorRules.packageNum]" placeholder="请输入打包数量" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="打包时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择打包时间" v-decorator="[ 'packageTime', validatorRules.packageTime]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="打包人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'packageBy', validatorRules.packageBy]" placeholder="请输入打包人"></a-input>
        </a-form-item>
        <a-form-item label="删除标识，0-正常；1-删除" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'delFlag', validatorRules.delFlag]" placeholder="请输入删除标识，0-正常；1-删除"></a-input>
        </a-form-item>
        <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'remarks', validatorRules.remarks]" placeholder="请输入备注"></a-input>
        </a-form-item>
        <a-form-item label="创建人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'createBy', validatorRules.createBy]" placeholder="请输入创建人"></a-input>
        </a-form-item>
        <a-form-item label="创建日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择创建日期" v-decorator="[ 'createTime', validatorRules.createTime]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="更新人" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'updateBy', validatorRules.updateBy]" placeholder="请输入更新人"></a-input>
        </a-form-item>
        <a-form-item label="更新日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <j-date placeholder="请选择更新日期" v-decorator="[ 'updateTime', validatorRules.updateTime]" :trigger-change="true" style="width: 100%"/>
        </a-form-item>
        <a-form-item label="所属部门" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'sysOrgCode', validatorRules.sysOrgCode]" placeholder="请输入所属部门"></a-input>
        </a-form-item>
        <a-form-item label="所属父部门" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'departParentId', validatorRules.departParentId]" placeholder="请输入所属父部门"></a-input>
        </a-form-item>
        <a-form-item label="所属部门" :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input v-decorator="[ 'departId', validatorRules.departId]" placeholder="请输入所属部门"></a-input>
        </a-form-item>
        
      </a-form>
    </a-spin>
    <a-button type="primary" @click="handleOk">确定</a-button>
    <a-button type="primary" @click="handleCancel">取消</a-button>
  </a-drawer>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'  
  
  export default {
    name: "PdPackageRecordModal",
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
          packageId: {rules: [
          ]},
          productId: {rules: [
          ]},
          stockId: {rules: [
          ]},
          packageBarCode: {rules: [
          ]},
          batchNo: {rules: [
          ]},
          expDate: {rules: [
          ]},
          packageNum: {rules: [
          ]},
          packageTime: {rules: [
          ]},
          packageBy: {rules: [
          ]},
          delFlag: {rules: [
            {required: true, message: '请输入删除标识，0-正常；1-删除!'},
          ]},
          remarks: {rules: [
          ]},
          createBy: {rules: [
          ]},
          createTime: {rules: [
          ]},
          updateBy: {rules: [
          ]},
          updateTime: {rules: [
          ]},
          sysOrgCode: {rules: [
          ]},
          departParentId: {rules: [
          ]},
          departId: {rules: [
          ]},
        },
        url: {
          add: "/pd/pdPackageRecord/add",
          edit: "/pd/pdPackageRecord/edit",
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
          this.form.setFieldsValue(pick(this.model,'packageId','productId','stockId','packageBarCode','batchNo','expDate','packageNum','packageTime','packageBy','delFlag','remarks','createBy','createTime','updateBy','updateTime','sysOrgCode','departParentId','departId'))
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
        this.form.setFieldsValue(pick(row,'packageId','productId','stockId','packageBarCode','batchNo','expDate','packageNum','packageTime','packageBy','delFlag','remarks','createBy','createTime','updateBy','updateTime','sysOrgCode','departParentId','departId'))
      }
      
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