<template>
  <a-modal
    :visible="visible"
    :width="popModal.width"
    :style="popModal.style"
    :maskClosable="false"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel">

    <template slot="title">
      <div style="width: 100%;height:20px;padding-right:32px;">
        <div style="float: left;">{{ title }}</div>
        <div style="float: right;">
          <a-button icon="fullscreen" style="width:56px;height:100%;border:0" @click="handleClickToggleFullScreen"/>
        </div>
      </div>
    </template>
    <!--<template>-->
    <!--<p>这是主体内容，高度是自适应的</p>-->
    <!--</template>-->

    <a-spin :spinning="confirmLoading">
      <!-- 主表单区域 -->
      <div style="background:#ECECEC; padding:20px">
        <a-card title="" style="margin-bottom: 10px;">
          <a-form :form="form">
            <a-row>
              <a-col :span="8">
                <a-form-item label="入库单号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input disabled v-decorator="[ 'recordNo', validatorRules.recordNo]" placeholder="请输入出入库单号"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item label="入库日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <j-date disabled placeholder="请选择入库日期" v-decorator="[ 'recordDate', validatorRules.recordDate]" :trigger-change="true" style="width: 100%"/>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item label="入库类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <j-dict-select-tag :triggerChange="true" v-decorator="['inType', validatorRules.inType]" title="入库类型" dictCode="in_type"/>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item label="操作人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input disabled v-decorator="[ 'recordPeopleName', validatorRules.recordPeopleName]" placeholder="请输入操作人"></a-input>
                </a-form-item>
                <a-form-item v-show="false">
                  <a-input v-show="false" v-decorator="[ 'recordPeople', {}]"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item label="供应商" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <!--<a-input v-decorator="[ 'supplierId', validatorRules.supplierId]" placeholder="请输入供应商ID"></a-input>-->
                  <a-select
                    showSearch
                    :supplierId="supplierValue"
                    placeholder="请选择供应商"
                    :defaultActiveFirstOption="false"
                    :showArrow="true"
                    :filterOption="false"
                    @search="supplierHandleSearch"
                    @change="supplierHandleChange"
                    @focus="supplierHandleSearch"
                    :notFoundContent="notFoundContent"
                    v-decorator="[ 'supplierId', validatorRules.supplierId]"
                  >
                    <a-select-option v-for="d in supplierData" :key="d.value">{{d.text}}</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <!--<a-col :span="12">-->
                <!--<a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
                  <!--<a-input v-decorator="[ 'remarks', validatorRules.remarks]" placeholder="请输入备注"></a-input>-->
                <!--</a-form-item>-->
              <!--</a-col>-->
              <!--<a-col :span="12">-->
                <!--<a-form-item label="验收结果 : 0-合格；1-不合格" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
                  <!--<a-input v-decorator="[ 'testResult', validatorRules.testResult]" placeholder="请输入验收结果 : 0-合格；1-不合格"></a-input>-->
                <!--</a-form-item>-->
              <!--</a-col>-->
              <!--<a-col :span="12">-->
                <!--<a-form-item label="储运状态 : 0-合格；1-不合格" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
                  <!--<a-input v-decorator="[ 'storageResult', validatorRules.storageResult]" placeholder="请输入储运状态 : 0-合格；1-不合格"></a-input>-->
                <!--</a-form-item>-->
              <!--</a-col>-->
              <!--<a-col :span="12">-->
                <!--<a-form-item label="温度" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
                  <!--<a-input v-decorator="[ 'temperature', validatorRules.temperature]" placeholder="请输入温度"></a-input>-->
                <!--</a-form-item>-->
              <!--</a-col>-->
              <!--<a-col :span="12">-->
                <!--<a-form-item label="湿度" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
                  <!--<a-input v-decorator="[ 'humidity', validatorRules.humidity]" placeholder="请输入湿度"></a-input>-->
                <!--</a-form-item>-->
              <!--</a-col>-->
              <!--<a-col :span="12">-->
                <!--<a-form-item label="入库部门ID" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
                  <!--<a-input v-decorator="[ 'inDepaetId', validatorRules.inDepaetId]" placeholder="请输入入库部门ID"></a-input>-->
                <!--</a-form-item>-->
              <!--</a-col>-->
            </a-row>
          </a-form>

          <div class="table-operator">
            <a-button @click="choosePurchaseOrder" type="primary" icon="import">从订单导入</a-button>
          </div>

        </a-card>

        <a-card title="" style="margin-bottom: 10px;">

        </a-card>

        <!-- 子表单区域 -->
        <a-card style="">
          <a-tabs v-model="activeKey" @change="handleChangeTabs">
            <a-tab-pane tab="出入库明细表" :key="refKeys[0]" :forceRender="true">
              <j-editable-table
                :ref="refKeys[0]"
                :loading="pdStockRecordDetailTable.loading"
                :columns="pdStockRecordDetailTable.columns"
                :dataSource="pdStockRecordDetailTable.dataSource"
                :maxHeight="300"
                :rowNumber="true"
                :rowSelection="true"
                :actionButton="true"/>
            </a-tab-pane>
          </a-tabs>
        </a-card>
      </div>
    </a-spin>
    <pd-choose-purchase-order-list-model  :ref="refKeys[1]" @ok="returnPurchaseOrderData" ></pd-choose-purchase-order-list-model>
  </a-modal>
</template>

<script>

  import pick from 'lodash.pick'
  import { FormTypes,getRefPromise } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import JDate from '@/components/jeecg/JDate'
  import {httpAction, deleteAction, getAction} from '@/api/manage'
  import PdChoosePurchaseOrderListModel from "./PdChoosePurchaseOrderListModel";


  export default {
    name: 'PdStockRecordModal',
    mixins: [JEditableTableMixin],
    components: {
      PdChoosePurchaseOrderListModel,
      JDate,
    },
    data() {
      return {
        labelCol: {span: 6},
        wrapperCol: {span: 16},
        labelCol2: {span: 3},
        wrapperCol2: {span: 20},

        initData:{},
        supplierValue: undefined,
        notFoundContent:"未找到内容",
        supplierData: [],

        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 1,
        validatorRules: {
          recordNo:{},
          recodeType:{},
          outType:{},
          inType:{rules: [{required: true, message: '请输选择入库类型!'}]},
          orderNo:{},
          allocationNo:{},
          applyNo:{},
          dosagertNo:{},
          recordPeople:{},
          recordPeopleName:{},
          recordDate:{},
          recordState:{},
          rejectReason:{},
          remarks:{},
          testResult:{},
          storageResult:{},
          temperature:{},
          humidity:{},
          outDepartId:{},
          inDepaetId:{},
          supplierId:{},
          checkPeople:{},
          checkTime:{},
          returnState:{},
          extend1:{},
          extend2:{},
          extend3:{},
          delFlag:{},
          sysOrgParentCode:{},
        },
        refKeys: ['pdStockRecordDetail', 'pdChoosePurchaseOrderListModel'],
        tableKeys:['pdStockRecordDetail', ],
        activeKey: 'pdStockRecordDetail',
        // 出入库明细表
        pdStockRecordDetailTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '出入库记录ID',
              key: 'recordId',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
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
              title: '产品条码',
              key: 'productBarCode',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '货位ID',
              key: 'goodsAllocationId',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '产品批号',
              key: 'batchNo',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '产品数量（出入库数量）',
              key: 'productNum',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '出库单价',
              key: 'outPrice',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '入库单价',
              key: 'inPrice',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },
            {
              title: '有效期',
              key: 'limitDate',
              type: FormTypes.date,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
            },

          ]
        },
        url: {
          init:"/pd/pdStockRecord/initModal",
          add: "/pd/pdStockRecord/add",
          edit: "/pd/pdStockRecord/edit",
          querySupplier:"/pd/pdSupplier/getSupplierList",
          pdStockRecordDetail: {
            list: '/pd/pdStockRecord/queryPdStockRecordDetailByMainId'
          },
        },
        popModal: {
          title: '这里是标题',
          visible: false,
          width: '100%',
          style: { top: '20px' },
          fullScreen: true
        },
      }
    },
    methods: {
      loadData() {
        this.loading = true;
        getAction(this.url.init, {}).then((res) => {
          if (res.success) {
            this.initData = res.result;
            // this.initData.resultData = res.result.resultDataStr;
            let fieldval = pick(this.initData,'recordNo','inType','recordPeople','recordPeopleName','recordDate','remarks','inDepaetId','supplierId')
            this.$nextTick(() => {
              this.form.setFieldsValue(fieldval)
            })
          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
          this.loading = false;
        })
      },
      getAllTable() {
        let values = this.tableKeys.map(key => getRefPromise(this, key))
        return Promise.all(values)
      },
      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
        // 加载子表数据
        if (this.model.id) {
          let fieldval = pick(this.model,'recordNo','inType','recordPeople','recordPeopleName','recordDate','remarks','inDepaetId','supplierId')
          this.$nextTick(() => {
            this.form.setFieldsValue(fieldval)
          })
          let params = { id: this.model.id }
          this.requestSubTableData(this.url.pdStockRecordDetail.list, params, this.pdStockRecordDetailTable)
        }else{
          this.loadData();
        }
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)

        return {
          ...main, // 展开
          pdStockRecordDetailList: allValues.tablesValue[0].values,
        }
      },
      validateError(msg){
        this.$message.error(msg)
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'recordNo','recodeType','outType','inType','orderNo','allocationNo','applyNo','dosagertNo','recordPeople','recordDate','recordState','rejectReason','remarks','testResult','storageResult','temperature','humidity','outDepartId','inDepaetId','supplierId','checkPeople','checkTime','returnState','extend1','extend2','extend3','delFlag','sysOrgParentCode'))
      },
      /** 切换全屏显示 */
      handleClickToggleFullScreen() {
        let mode = !this.popModal.fullScreen
        if (mode) {
          this.popModal.width = '100%'
          this.popModal.style.top = '20px'
        } else {
          this.popModal.width = '1200px'
          this.popModal.style.top = '50px'
        }
        this.popModal.fullScreen = mode
      },
      //供应商查询start
      supplierHandleSearch(value) {
        fetch(value, data => (this.supplierData = data),this.url.querySupplier);
      },
      supplierHandleChange(value) {
        this.supplierValue = value;
        fetch(value, data => (this.supplierData = data),this.url.querySupplier);
      },
      //供应商查询end
      choosePurchaseOrder() {
        this.$refs.pdChoosePurchaseOrderListModel.show();
      },
      returnPurchaseOrderData() {

      },
    }
  }

  let timeout;
  let currentValue;

  function fetch(value, callback,url) {
    if (timeout) {
      clearTimeout(timeout);
      timeout = null;
    }
    currentValue = value;

    function fake() {
      getAction(url,{name:value}).then((res)=>{
        if (!res.success) {
          this.cmsFailed(res.message);
        }
        if (currentValue === value) {
          const result = res.result;
          const data = [];
          result.forEach(r => {
            data.push({
              value: r.id,
              text: r.name,
            });
          });
          callback(data);
        }
      })
    }
    timeout = setTimeout(fake, 0); //这边不延迟
  }
</script>

<style scoped>
</style>