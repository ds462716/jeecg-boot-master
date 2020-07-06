<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :maskClosable=disableSubmit
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">

    <a-spin :spinning="confirmLoading">
      <!-- 主表单区域 -->
      <a-form :form="form">
        <a-row>
          <a-col :span="12">
            <a-form-item label="发票登记号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'invoiceRegNo', validatorRules.invoiceRegNo]" placeholder="请输入发票登记号" disabled ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="发票号" :labelCol="labelCol2" :wrapperCol="wrapperCol2">
              <a-input :disabled="disableSubmit" v-decorator="[ 'invoiceNo', validatorRules.invoiceNo]" placeholder="请输入发票号"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="发票代码" :labelCol="labelCol2" :wrapperCol="wrapperCol2">
              <a-input :disabled="disableSubmit" v-decorator="[ 'invoiceCode', validatorRules.invoiceCode]" placeholder="请输入发票代码"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="发票日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date :disabled="disableSubmit" placeholder="请选择发票日期" v-decorator="[ 'invoiceData', validatorRules.invoiceData]" :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="发票金额" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number :disabled="disableSubmit" v-decorator="[ 'invoiceMoney', validatorRules.invoiceMoney]" placeholder="请输入发票金额" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="备注" :labelCol="labelCol2" :wrapperCol="wrapperCol2" style="text-align: left">
              <a-textarea :disabled="disableSubmit" v-decorator="[ 'remarks', validatorRules.remarks]" placeholder="请输入备注"></a-textarea>
            </a-form-item>
          </a-col>
          <a-col :span="24" v-show="false">
            <a-form-item label="发票明细id" :labelCol="labelCol2" :wrapperCol="wrapperCol2">
              <a-input :disabled="disableSubmit" v-decorator="[ 'id' ]" ></a-input>
            </a-form-item>
            <a-form-item label="发票id" :labelCol="labelCol2" :wrapperCol="wrapperCol2">
              <a-input :disabled="disableSubmit" v-decorator="[ 'invoiceId' ]" ></a-input>
            </a-form-item>
            <a-form-item label="单据id" :labelCol="labelCol2" :wrapperCol="wrapperCol2">
              <a-input :disabled="disableSubmit" v-decorator="[ 'billId' ]" ></a-input>
            </a-form-item>
            <a-form-item label="单据明细id" :labelCol="labelCol2" :wrapperCol="wrapperCol2">
              <a-input :disabled="disableSubmit" v-decorator="[ 'billDetailIdList' ]" ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-spin>

    <template slot="footer">
      <a-button @click="close" style="margin-right: 15px;">关  闭</a-button>
      <a-button @click="handleOk" v-show="!disableSubmit" type="primary" :loading="confirmLoading" style="margin-right: 15px;">确定</a-button>
    </template>

  </a-modal>
</template>

<script>

  import pick from 'lodash.pick'
  import {validateDuplicateValue} from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'
  import {httpAction, getAction} from '@/api/manage'
  import {duplicateCheckHasDelFlag} from '@/api/api'

  export default {
    name: 'PdInvoiceAddModal',
    components: {
      JDate,
    },
    data() {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:800,
        visible: false,
        disableSubmit:false,
        confirmLoading: false,
        model: {},
        initData:{},
        labelCol: { span: 6 },
        wrapperCol: { span: 16 },
        labelCol2: { span: 3 },
        wrapperCol2: { span: 20 },

        billDetailIdList:[],
        // 新增时子表默认添加几行空数据
        // addDefaultRowNum: 1,
        validatorRules: {
          invoiceRegNo: {rules: []},
          invoiceNo: {rules: [{required: true, message: '请输入发票号!'},]},
          invoiceCode: {rules: []},
          invoiceData: {rules: []},
          invoiceMoney: {rules: []},
          returnMoney: {rules: []},
          returnData: {rules: []},
          invoiceType: {rules: []},
          payStatus: {rules: []},
          invoiceStatus: {rules: []},
          billBy: {rules: []},
          remarks: {rules: []},
        },
        url: {
          init:"/pd/pdInvoice/initModal",
          add: "/pd/pdInvoice/add",
          edit: "/pd/pdInvoice/edit",
          pdInvoiceDetail: {
            list: '/pd/pdInvoice/queryPdInvoiceDetailByMainId'
          },
        }
      }
    },
    methods: {
      add(billDetailIdList) {
        // if(this.model.id){
        this.billDetailIdList = billDetailIdList;
          this.edit({});
      },
      edit(record) {
        this.form.resetFields();
        this.visible = true;
        this.model = Object.assign({}, record);
        if(this.model.id){
          this.$nextTick(() => {
            let fieldval = pick(this.model,'id','invoiceId','billId','billDetailIdList','invoiceRegNo','invoiceNo','invoiceNo','invoiceCode','invoiceData','invoiceMoney','remarks');
            this.form.setFieldsValue(fieldval);
          })
        }else{
          getAction(this.url.init, {id:""}).then((res) => {
            if (res.success) {
              this.initData = res.result;
              this.initData.billDetailIdList = this.billDetailIdList;
              let fieldval = pick(this.initData,'invoiceRegNo','billDetailIdList');
              this.$nextTick(() => {
                this.form.setFieldsValue(fieldval);
              })
            }
          })
        }
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
                that.close();
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
            })
          }

        })
      },
      handleCancel () {
        this.close()
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      popupCallback(row) {
        this.form.setFieldsValue(pick(row, 'id','invoiceId','billId','billDetailIdList','invoiceRegNo','invoiceNo','invoiceCode','invoiceData','invoiceMoney','remarks'))
      },

    }
  }
</script>

<style scoped>
  .drawer-bootom-button {
    width: 100%;
    text-align: right;
    background: #fff;
    margin-top:10px;
  }
</style>