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
            <a-form-item label="仓库id" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'storeroomId', validatorRules.storeroomId]" placeholder="请输入仓库id"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="产品id" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'productId', validatorRules.productId]" placeholder="请输入产品id"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="库存数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="[ 'stockNum', validatorRules.stockNum]" placeholder="请输入库存数量" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="生产日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择生产日期" v-decorator="[ 'produceDate', validatorRules.produceDate]" :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="产品有效期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择产品有效期" v-decorator="[ 'validDate', validatorRules.validDate]" :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="库存上限" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="[ 'limitUp', validatorRules.limitUp]" placeholder="请输入库存上限" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="库存下限" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="[ 'limitDown', validatorRules.limitDown]" placeholder="请输入库存下限" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="过期标识" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'expire', validatorRules.expire]" placeholder="请输入过期标识"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="供应商ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'supplierId', validatorRules.supplierId]" placeholder="请输入供应商ID"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'remarks', validatorRules.remarks]" placeholder="请输入备注"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="删除标识 " :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'delFlag', validatorRules.delFlag]" placeholder="请输入删除标识 "></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="是否永存" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'isLong', validatorRules.isLong]" placeholder="请输入是否永存"></a-input>
            </a-form-item>
          </a-col>

        </a-row>
      </a-form>

      <!-- 子表单区域 -->
      <a-tabs v-model="activeKey" @change="handleChangeTabs">
        <a-tab-pane tab="库存明细表" :key="refKeys[0]" :forceRender="true">
          <j-editable-table
            :ref="refKeys[0]"
            :loading="pdProductStockTable.loading"
            :columns="pdProductStockTable.columns"
            :dataSource="pdProductStockTable.dataSource"
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
  import JDate from '@/components/jeecg/JDate'  

  export default {
    name: 'PdProductStockTotalModal',
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
          storeroomId:{},
          productId:{},
          stockNum:{},
          produceDate:{},
          validDate:{},
          limitUp:{},
          limitDown:{},
          expire:{},
          supplierId:{},
          remarks:{},
          delFlag:{},
          isLong:{},
        },
        refKeys: ['pdProductStock', ],
        tableKeys:['pdProductStock', ],
        activeKey: 'pdProductStock',
        // 库存明细表
        pdProductStockTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '库房ID',
              key: 'storeroomId',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '产品id',
              key: 'productId',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '产品名称',
              key: 'productName',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '产品编号',
              key: 'productNo',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '产品条码',
              key: 'productBarCode',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '批次号',
              key: 'batchNo',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '数量',
              key: 'stockNum',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '生产日期',
              key: 'produceDate',
              type: FormTypes.date,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '有效期',
              key: 'validDate',
              type: FormTypes.date,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '过期状态',
              key: 'pdState',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '供应商ID',
              key: 'supplierId',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '备注',
              key: 'remarks',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '删除标识',
              key: 'delFlag',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '是否永存',
              key: 'isLong',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
          ]
        },
        url: {
          add: "/stock/pdProductStockTotal/add",
          edit: "/stock/pdProductStockTotal/edit",
          pdProductStock: {
            list: '/stock/pdProductStockTotal/queryPdProductStockByMainId'
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
        let fieldval = pick(this.model,'storeroomId','productId','stockNum','produceDate','validDate','limitUp','limitDown','expire','supplierId','remarks','delFlag','isLong')
        this.$nextTick(() => {
          this.form.setFieldsValue(fieldval)
        })
        // 加载子表数据
        if (this.model.id) {
          let params = { id: this.model.id }
          this.requestSubTableData(this.url.pdProductStock.list, params, this.pdProductStockTable)
        }
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)

        return {
          ...main, // 展开
          pdProductStockList: allValues.tablesValue[0].values,
        }
      },
      validateError(msg){
        this.$message.error(msg)
      },
     popupCallback(row){
       this.form.setFieldsValue(pick(row,'storeroomId','productId','stockNum','produceDate','validDate','limitUp','limitDown','expire','supplierId','remarks','delFlag','isLong'))
     },

    }
  }
</script>

<style scoped>
</style>