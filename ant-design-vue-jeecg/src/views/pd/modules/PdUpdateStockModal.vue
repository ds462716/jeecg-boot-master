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
      <div style="background:#ECECEC; padding:20px">
        <a-card title="" style="margin-bottom: 10px;">
      <a-form :form="form">
        <a-row>
        <a-col :span="12">
          <a-form-item label="产品名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input ref="inputFocus" :disabled="true"   v-decorator="[ 'productName', validatorRules.productName]"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="产品编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input ref="inputFocus" :disabled="true"  v-decorator="[ 'number', validatorRules.number]"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="产品条码" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input ref="inputFocus" :disabled="true"  v-decorator="[ 'productBarCode', validatorRules.productBarCode]"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="规格" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input ref="inputFocus" :disabled="true"  v-decorator="[ 'spec', validatorRules.spec]"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="型号" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input ref="inputFocus" :disabled="true"v-decorator="[ 'version', validatorRules.version]"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="批次号" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input ref="inputFocus" :disabled="true"  v-decorator="[ 'batchNo', validatorRules.batchNo]" ></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="生产日期"
            class="endTime">
            <j-date style="width: 100%"  v-decorator="[ 'produceDate', validatorRules.produceDate]" placeholder="请选择生产日期" showTime dateFormat="YYYY-MM-DD"></j-date>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="有效期"
            class="endTime">
            <j-date style="width: 100%" :disabled="true"  v-decorator="[ 'expDate', validatorRules.expDate]" showTime dateFormat="YYYY-MM-DD"></j-date>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="货位" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input ref="inputFocus" :disabled="true"  v-decorator="[ 'huoweiCode', validatorRules.huoweiCode]"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input ref="inputFocus" :disabled="true"  v-decorator="[ 'unitName', validatorRules.unitName]"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="产品数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input ref="inputFocus" :disabled="true"  v-decorator="[ 'stockNum', validatorRules.stockNum]"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="生产厂家" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input ref="inputFocus" :disabled="true" v-decorator="[ 'venderName', validatorRules.venderName]"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="注册证号" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input ref="inputFocus" :disabled="true" v-decorator="[ 'registration', validatorRules.registration]"></a-input>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="是否过期" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <j-dict-select-tag
              :disabled="true"
              ref="inputFocus"
              v-decorator="[ 'expStatus', validatorRules.expStatus]"
              dictCode="exp_status"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label="是否久存" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <j-dict-select-tag
              :disabled="true"
              ref="inputFocus"
              v-decorator="[ 'isLong', validatorRules.isLong]"
               dictCode="pd_isLong"/>
           </a-form-item>
        </a-col>
        </a-row>
      </a-form>
        </a-card>
      </div>
    </a-spin>
  </a-modal>
</template>

<script>

  import pick from 'lodash.pick'
  import { httpAction } from '@/api/manage'
  import JDate from '@/components/jeecg/JDate'

  export default {
    name: "PdUpdateStockModal",
    components: {
      JDate
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:1300,
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
          productName: {rules: []},
          number: {rules: []},
          productBarCode: {rules: []},
          spec: {rules: []},
          version: {rules: []},
          batchNo: {rules: []},
          produceDate: {rules: []},
          expDate: {rules: []},
          huoweiCode: {rules: []},
          unitName: {rules: []},
          venderName: {rules: []},
          registration: {rules: []},
          stockNum: {rules: []},
          expStatus: {rules: []},
          isLong: {rules: []},
        },
        url: {
          edit: "/pd/pdProductStockTotal/updateStock",
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
          this.form.setFieldsValue(pick(this.model,'productName','number','productBarCode',
            'spec','version','batchNo','produceDate',
            'expDate','huoweiCode','unitName','venderName',
             'stockNum','expStatus','isLong','registration'
          ))
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
            let formData = Object.assign(this.model, values);
            console.log("表单提交数据",formData)
            httpAction(this.url.edit,formData,"put").then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok',this.model);
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
    }
  }
</script>