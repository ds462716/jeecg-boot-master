<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    :maskClosable=disableSubmit
    cancelText="关闭"
  >
    <a-spin :spinning="confirmLoading">
      <div style="background:#ECECEC; padding:10px">
        <a-form :form="form">
          <a-card style="margin-bottom: 10px;">
            <a-row :gutter="24">
              <a-col :md="8" :sm="8" v-show="false">
                <a-form-item label="id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input autocomplete="off" v-decorator="[ 'id' ]"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="8">
                <a-form-item label="产品编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input autocomplete="off" v-decorator="[ 'number' ]"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="8">
                <a-form-item label="产品名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input autocomplete="off" v-decorator="[ 'name' ]"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="8">
                <a-form-item label="生产厂家" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input autocomplete="off" v-decorator="[ 'venderName' ]"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="8">
                <a-form-item label="单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input autocomplete="off" v-decorator="[ 'unitName' ]"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="8">
                <a-form-item label="规格" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input autocomplete="off" v-decorator="[ 'spec' ]"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="8">
                <a-form-item label="创建日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input autocomplete="off" v-decorator="[ 'createTime' ]"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="8">
                <a-form-item label="进阶" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input autocomplete="off" v-decorator="[ 'purchasePrice' ]"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="8">
                <a-form-item label="出价" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input autocomplete="off" v-decorator="[ 'sellingPrice' ]"></a-input>
                </a-form-item>
              </a-col>
            </a-row>
          </a-card>
          <a-card title="HIS当前价格">
            <a-row :gutter="24">
              <a-col :md="8" :sm="8" v-show="false">
                <a-form-item label="id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input autocomplete="off" v-decorator="[ 'id' ]"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="8">
                <a-form-item label="产品编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input autocomplete="off" v-decorator="[ 'number' ]"></a-input>
                </a-form-item>
              </a-col>
            </a-row>
          </a-card>
        </a-form>
      </div>
    </a-spin>
  </a-modal>
</template>

<script>

  import {httpAction, getAction} from '@/api/manage'
  import pick from 'lodash.pick'
  import {validateDuplicateValue} from '@/utils/util'
  import {makeWb} from '@/utils/wubi'
  import {duplicateCheckHasDelFlag} from '@/api/api'

  export default {
    name: "HisSynChargeCodeModelFC",
    components: {},
    data() {
      return {
        form: this.$form.createForm(this),
        title: "价格对照",
        width: 1200,
        visible: false,
        disableSubmit: false,
        model: {},
        unitId: "", // 单位ID
        labelCol: {
          xs: {span: 24},
          sm: {span: 6},
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 18},
        },
        confirmLoading: false,
        validatorRules: {
          name: {
            rules: [
              {required: true, message: '请输入单位名称!'},
              /*{validator: this.validateUnitname}*/
            ]
          },
          py: {
            rules: []
          },
          wb: {
            rules: []
          },
          zdy: {
            rules: []
          },
          remarks: {
            rules: []
          },
        },
        unitTypeRules: [
          {
            required: true, // 必填
            message: '请选择单位类型' // 显示的文本
          }
        ],
        url: {
          add: "/pd/pdUnit/add",
          edit: "/pd/pdUnit/edit",
        }
      }
    },
    created() {
    },
    methods: {
      add() {
        this.edit({});
      },
      edit(record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.unitId = record.id;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'id', 'number', 'name', 'venderName', 'unitName', 'spec', 'createTime', 'purchasePrice', 'sellingPrice'))
        })
      },
      close() {
        this.$emit('close');
        this.visible = false;
      },
      handleOk() {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if (!this.model.id) {
              httpurl += this.url.add;
              method = 'post';
            } else {
              httpurl += this.url.edit;
              method = 'put';
            }
            let formData = Object.assign(this.model, values);
            console.log("表单提交数据", formData)
            httpAction(httpurl, formData, method).then((res) => {
              if (res.success) {
                that.$message.success(res.message);
                that.$emit('ok');
                that.close();
              } else {
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
            })
          }

        })
      },
      handleCancel() {
        this.close()
      },
      popupCallback(row) {
        this.form.setFieldsValue(pick(row, 'name', 'unitType', 'py', 'wb', 'zdy', 'remarks'))
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