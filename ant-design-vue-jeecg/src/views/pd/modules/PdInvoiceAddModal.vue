<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :maskClosable="false"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">


    <!--<a-modal-->
      <!--:title="title"-->
      <!--:width="width"-->
      <!--:visible="visible"-->
      <!--:confirmLoading="confirmLoading"-->
      <!--@ok="handleOk"-->
      <!--@cancel="handleCancel"-->
      <!--:maskClosable=disableSubmit-->
      <!--cancelText="关闭">-->

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
              <a-input v-decorator="[ 'invoiceNo', validatorRules.invoiceNo]" placeholder="请输入发票号"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="发票代码" :labelCol="labelCol2" :wrapperCol="wrapperCol2">
              <a-input v-decorator="[ 'invoiceCode', validatorRules.invoiceCode]" placeholder="请输入发票代码"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="发票日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择发票日期" v-decorator="[ 'invoiceData', validatorRules.invoiceData]"
                      :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="发票金额" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="[ 'invoiceMoney', validatorRules.invoiceMoney]" placeholder="请输入发票金额"
                              style="width: 100%"/>
            </a-form-item>
          </a-col>
          <!--<a-col :span="12">-->
          <!--<a-form-item label="回款金额" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--<a-input-number v-decorator="[ 'returnMoney', validatorRules.returnMoney]" placeholder="请输入回款金额" style="width: 100%"/>-->
          <!--</a-form-item>-->
          <!--</a-col>-->
          <!--<a-col :span="12">-->
          <!--<a-form-item label="回款日期" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--<j-date placeholder="请选择回款日期" v-decorator="[ 'returnData', validatorRules.returnData]" :trigger-change="true" style="width: 100%"/>-->
          <!--</a-form-item>-->
          <!--</a-col>-->
          <!--<a-col :span="12">-->
          <!--<a-form-item label="发票类型；1-入库单发票；2-出库单发票" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--<a-input v-decorator="[ 'invoiceType', validatorRules.invoiceType]" placeholder="请输入发票类型；1-入库单发票；2-出库单发票"></a-input>-->
          <!--</a-form-item>-->
          <!--</a-col>-->
          <!--<a-col :span="12">-->
          <!--<a-form-item label="支付状态：1-已支付；2-未支付" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--<a-input v-decorator="[ 'payStatus', validatorRules.payStatus]" placeholder="请输入支付状态：1-已支付；2-未支付"></a-input>-->
          <!--</a-form-item>-->
          <!--</a-col>-->
          <!--<a-col :span="12">-->
          <!--<a-form-item label="发票状态：1-未完成；2-已完成" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--<a-input v-decorator="[ 'invoiceStatus', validatorRules.invoiceStatus]" placeholder="请输入发票状态：1-未完成；2-已完成"></a-input>-->
          <!--</a-form-item>-->
          <!--</a-col>-->
          <!--<a-col :span="12">-->
          <!--<a-form-item label="单据登记人（入库单创建人）" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--<a-input v-decorator="[ 'billBy', validatorRules.billBy]" placeholder="请输入单据登记人（入库单创建人）"></a-input>-->
          <!--</a-form-item>-->
          <!--</a-col>-->
          <a-col :span="24">
            <a-form-item label="备注" :labelCol="labelCol2" :wrapperCol="wrapperCol2">
              <a-input v-decorator="[ 'remarks', validatorRules.remarks]" placeholder="请输入备注"></a-input>
            </a-form-item>
          </a-col>
          <!--<a-col :span="12">-->
          <!--<a-form-item label="删除标识，0-正常；1-删除" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--<a-input v-decorator="[ 'delFlag', validatorRules.delFlag]" placeholder="请输入删除标识，0-正常；1-删除"></a-input>-->
          <!--</a-form-item>-->
          <!--</a-col>-->
          <!--<a-col :span="12">-->
          <!--<a-form-item label="创建人" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--<a-input v-decorator="[ 'createBy', validatorRules.createBy]" placeholder="请输入创建人"></a-input>-->
          <!--</a-form-item>-->
          <!--</a-col>-->
          <!--<a-col :span="12">-->
          <!--<a-form-item label="创建日期" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--<j-date placeholder="请选择创建日期" v-decorator="[ 'createTime', validatorRules.createTime]" :trigger-change="true" style="width: 100%"/>-->
          <!--</a-form-item>-->
          <!--</a-col>-->
          <!--<a-col :span="12">-->
          <!--<a-form-item label="更新人" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--<a-input v-decorator="[ 'updateBy', validatorRules.updateBy]" placeholder="请输入更新人"></a-input>-->
          <!--</a-form-item>-->
          <!--</a-col>-->
          <!--<a-col :span="12">-->
          <!--<a-form-item label="更新日期" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--<j-date placeholder="请选择更新日期" v-decorator="[ 'updateTime', validatorRules.updateTime]" :trigger-change="true" style="width: 100%"/>-->
          <!--</a-form-item>-->
          <!--</a-col>-->
          <!--<a-col :span="12">-->
          <!--<a-form-item label="所属部门" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--<a-input v-decorator="[ 'sysOrgCode', validatorRules.sysOrgCode]" placeholder="请输入所属部门"></a-input>-->
          <!--</a-form-item>-->
          <!--</a-col>-->
          <!--<a-col :span="12">-->
          <!--<a-form-item label="所属父部门" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--<a-input v-decorator="[ 'departParentId', validatorRules.departParentId]" placeholder="请输入所属父部门"></a-input>-->
          <!--</a-form-item>-->
          <!--</a-col>-->
          <!--<a-col :span="12">-->
          <!--<a-form-item label="所属部门" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--<a-input v-decorator="[ 'departId', validatorRules.departId]" placeholder="请输入所属部门"></a-input>-->
          <!--</a-form-item>-->
          <!--</a-col>-->

        </a-row>
      </a-form>

      <!-- 子表单区域 -->
      <!--<a-tabs v-model="activeKey" @change="handleChangeTabs">-->
      <!--<a-tab-pane tab="pd_invoice_detail" :key="refKeys[0]" :forceRender="true">-->
      <!--<j-editable-table-->
      <!--:ref="refKeys[0]"-->
      <!--:loading="pdInvoiceDetailTable.loading"-->
      <!--:columns="pdInvoiceDetailTable.columns"-->
      <!--:dataSource="pdInvoiceDetailTable.dataSource"-->
      <!--:maxHeight="300"-->
      <!--:rowNumber="true"-->
      <!--:rowSelection="true"-->
      <!--:actionButton="true"/>-->
      <!--</a-tab-pane>-->
      <!--</a-tabs>-->

    </a-spin>
  </a-modal>
</template>

<script>

  import pick from 'lodash.pick'
  // import { FormTypes,getRefPromise } from '@/utils/JEditableTableUtil'
  // import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import {validateDuplicateValue} from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'
  import {httpAction, getAction} from '@/api/manage'
  import {duplicateCheckHasDelFlag} from '@/api/api'

  export default {
    name: 'PdInvoiceAddModal',
    // mixins: [JEditableTableMixin],
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
        labelCol: { span: 6 },
        wrapperCol: { span: 16 },
        labelCol2: { span: 3 },
        wrapperCol2: { span: 20 },
        // 新增时子表默认添加几行空数据
        // addDefaultRowNum: 1,
        validatorRules: {
          invoiceRegNo: {rules: []},
          invoiceNo: {rules: []},
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
          delFlag: {rules: [{required: true, message: '请输入删除标识，0-正常；1-删除!'},]},
          createBy: {rules: []},
          createTime: {rules: []},
          updateBy: {rules: []},
          updateTime: {rules: []},
          sysOrgCode: {rules: []},
          departParentId: {rules: [{required: true, message: '请输入所属父部门!'},]},
          departId: {rules: [{required: true, message: '请输入所属部门!'},]},
        },
        // refKeys: ['pdInvoiceDetail',],
        // tableKeys: ['pdInvoiceDetail',],
        // activeKey: 'pdInvoiceDetail',
        // pd_invoice_detail
        // pdInvoiceDetailTable: {
        //   loading: false,
        //   dataSource: [],
        //   columns: [
        //     {
        //       title: '发票id',
        //       key: 'invoiceId',
        //       type: FormTypes.input,
        //       width:"200px",
        //       placeholder: '请输入${title}',
        //       defaultValue: '',
        //     },
        //     {
        //       title: '单据号',
        //       key: 'billNo',
        //       type: FormTypes.input,
        //       width:"200px",
        //       placeholder: '请输入${title}',
        //       defaultValue: '',
        //     },
        //     {
        //       title: '单据id',
        //       key: 'billId',
        //       type: FormTypes.input,
        //       width:"200px",
        //       placeholder: '请输入${title}',
        //       defaultValue: '',
        //     },
        //     {
        //       title: '单据明细id',
        //       key: 'billDetailId',
        //       type: FormTypes.input,
        //       width:"200px",
        //       placeholder: '请输入${title}',
        //       defaultValue: '',
        //     },
        //     {
        //       title: '产品id',
        //       key: 'productId',
        //       type: FormTypes.input,
        //       width:"200px",
        //       placeholder: '请输入${title}',
        //       defaultValue: '',
        //     },
        //     {
        //       title: '库存明细id',
        //       key: 'productStockId',
        //       type: FormTypes.input,
        //       width:"200px",
        //       placeholder: '请输入${title}',
        //       defaultValue: '',
        //     },
        //     {
        //       title: '数量',
        //       key: 'num',
        //       type: FormTypes.input,
        //       width:"200px",
        //       placeholder: '请输入${title}',
        //       defaultValue: '',
        //     },
        //     {
        //       title: '单价',
        //       key: 'price',
        //       type: FormTypes.input,
        //       width:"200px",
        //       placeholder: '请输入${title}',
        //       defaultValue: '',
        //     },
        //     {
        //       title: '金额',
        //       key: 'money',
        //       type: FormTypes.input,
        //       width:"200px",
        //       placeholder: '请输入${title}',
        //       defaultValue: '',
        //     },
        //     {
        //       title: '备注',
        //       key: 'remarks',
        //       type: FormTypes.input,
        //       width:"200px",
        //       placeholder: '请输入${title}',
        //       defaultValue: '',
        //     },
        //     {
        //       title: '删除标识，0-正常；1-删除',
        //       key: 'delFlag',
        //       type: FormTypes.input,
        //       width:"200px",
        //       placeholder: '请输入${title}',
        //       defaultValue: '',
        //       validateRules: [{ required: true, message: '${title}不能为空' }],
        //     },
        //     {
        //       title: '创建人',
        //       key: 'createBy',
        //       type: FormTypes.input,
        //       width:"200px",
        //       placeholder: '请输入${title}',
        //       defaultValue: '',
        //     },
        //     {
        //       title: '创建日期',
        //       key: 'createTime',
        //       type: FormTypes.date,
        //       width:"200px",
        //       placeholder: '请输入${title}',
        //       defaultValue: '',
        //     },
        //     {
        //       title: '更新人',
        //       key: 'updateBy',
        //       type: FormTypes.input,
        //       width:"200px",
        //       placeholder: '请输入${title}',
        //       defaultValue: '',
        //     },
        //     {
        //       title: '更新日期',
        //       key: 'updateTime',
        //       type: FormTypes.date,
        //       width:"200px",
        //       placeholder: '请输入${title}',
        //       defaultValue: '',
        //     },
        //     {
        //       title: '所属部门',
        //       key: 'sysOrgCode',
        //       type: FormTypes.input,
        //       width:"200px",
        //       placeholder: '请输入${title}',
        //       defaultValue: '',
        //     },
        //     {
        //       title: '所属父部门',
        //       key: 'departParentId',
        //       type: FormTypes.input,
        //       width:"200px",
        //       placeholder: '请输入${title}',
        //       defaultValue: '',
        //       validateRules: [{ required: true, message: '${title}不能为空' }],
        //     },
        //     {
        //       title: '所属部门',
        //       key: 'departId',
        //       type: FormTypes.input,
        //       width:"200px",
        //       placeholder: '请输入${title}',
        //       defaultValue: '',
        //       validateRules: [{ required: true, message: '${title}不能为空' }],
        //     },
        //   ]
        // },
        url: {
          add: "/pd/pdInvoice/add",
          edit: "/pd/pdInvoice/edit",
          pdInvoiceDetail: {
            list: '/pd/pdInvoice/queryPdInvoiceDetailByMainId'
          },
        }
      }
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
          this.form.setFieldsValue(pick(this.model, 'name', 'unitType', 'py', 'wb', 'zdy', 'remarks'))
          //获取光标
          // let input = this.$refs['inputFocus'];
          // input.focus()
        })
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
        this.form.setFieldsValue(pick(row, 'invoiceRegNo', 'invoiceNo', 'invoiceCode', 'invoiceData', 'invoiceMoney', 'returnMoney', 'returnData', 'invoiceType', 'payStatus', 'invoiceStatus', 'billBy', 'remarks', 'delFlag', 'createBy', 'createTime', 'updateBy', 'updateTime', 'sysOrgCode', 'departParentId', 'departId'))
      },

    }
  }
</script>

<style scoped>
</style>