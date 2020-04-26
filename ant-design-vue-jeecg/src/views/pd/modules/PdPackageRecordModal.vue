<template>
  <a-modal
    :title="title"
    :width="1200"
    :visible="visible"
    :maskClosable="false"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel">
    <a-spin :spinning="confirmLoading">
      <!-- 主表单区域 -->
      <a-form :form="form">
        <a-row>

          <a-col :span="12">
            <a-form-item label="定数包ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'packageId', validatorRules.packageId]" placeholder="请输入定数包ID"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="科室ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'departId', validatorRules.departId]" placeholder="请输入科室ID"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="定数包流水码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'packageBarCode', validatorRules.packageBarCode]" placeholder="请输入定数包流水码"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="出库状态：0-已出库；1-未出库" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'status', validatorRules.status]" placeholder="请输入出库状态：0-已出库；1-未出库"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="删除标识，0-正常；1-删除" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'delFlag', validatorRules.delFlag]" placeholder="请输入删除标识，0-正常；1-删除"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'remarks', validatorRules.remarks]" placeholder="请输入备注"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="创建人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'createBy', validatorRules.createBy]" placeholder="请输入创建人"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="创建日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择创建日期" v-decorator="[ 'createTime', validatorRules.createTime]" :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="更新人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'updateBy', validatorRules.updateBy]" placeholder="请输入更新人"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="更新日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择更新日期" v-decorator="[ 'updateTime', validatorRules.updateTime]" :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>

        </a-row>
      </a-form>

      <!-- 子表单区域 -->
      <a-tabs v-model="activeKey" @change="handleChangeTabs">
        <a-tab-pane tab="pd_package_record_detail" :key="refKeys[0]" :forceRender="true">
          <j-editable-table
            :ref="refKeys[0]"
            :loading="pdPackageRecordDetailTable.loading"
            :columns="pdPackageRecordDetailTable.columns"
            :dataSource="pdPackageRecordDetailTable.dataSource"
            :maxHeight="300"
            :rowNumber="true"
            :rowSelection="true"
            :actionButton="true"/>
        </a-tab-pane>
        
      </a-tabs>

    </a-spin>
  </a-modal>
</template>

<script>

  import pick from 'lodash.pick'
  import { FormTypes,getRefPromise } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import { validateDuplicateValue } from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'  

  export default {
    name: 'PdPackageRecordModal',
    mixins: [JEditableTableMixin],
    components: {
      JDate,
    },
    data() {
      return {
        labelCol: {
          span: 6
        },
        wrapperCol: {
          span: 16
        },
        labelCol2: {
          span: 3
        },
        wrapperCol2: {
          span: 20
        },
        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 1,
        validatorRules: {
          packageId: {rules: [
          ]},
          departId: {rules: [
          ]},
          packageBarCode: {rules: [
          ]},
          status: {rules: [
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
        },
        refKeys: ['pdPackageRecordDetail', ],
        tableKeys:['pdPackageRecordDetail', ],
        activeKey: 'pdPackageRecordDetail',
        // pd_package_record_detail
        pdPackageRecordDetailTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '打包记录ID',
              key: 'recordId',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' }],
            },
            {
              title: '产品ID',
              key: 'productId',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '库存明细ID',
              key: 'productStockId',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '批号',
              key: 'batchNo',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '有效期',
              key: 'expDate',
              type: FormTypes.date,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '打包数量',
              key: 'packageNum',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
          ]
        },
        url: {
          add: "/pd/pdPackageRecord/add",
          edit: "/pd/pdPackageRecord/edit",
          pdPackageRecordDetail: {
            list: '/pd/pdPackageRecord/queryPdPackageRecordDetailByMainId'
          },
        }
      }
    },
    methods: {
      getAllTable() {
        let values = this.tableKeys.map(key => getRefPromise(this, key))
        return Promise.all(values)
      },
      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
        let fieldval = pick(this.model,'packageId','departId','packageBarCode','status','delFlag','remarks','createBy','createTime','updateBy','updateTime')
        this.$nextTick(() => {
          this.form.setFieldsValue(fieldval)
        })
        // 加载子表数据
        if (this.model.id) {
          let params = { id: this.model.id }
          this.requestSubTableData(this.url.pdPackageRecordDetail.list, params, this.pdPackageRecordDetailTable)
        }
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)

        return {
          ...main, // 展开
          pdPackageRecordDetailList: allValues.tablesValue[0].values,
        }
      },
      validateError(msg){
        this.$message.error(msg)
      },
     popupCallback(row){
       this.form.setFieldsValue(pick(row,'packageId','departId','packageBarCode','status','delFlag','remarks','createBy','createTime','updateBy','updateTime'))
     },

    }
  }
</script>

<style scoped>
</style>