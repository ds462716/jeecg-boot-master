<template>
  <a-modal
    :visible="visible"
    :width="popModal.width"
    :style="popModal.style"
    :maskClosable="false"
    :confirmLoading="confirmLoading"
    @cancel="handleCancel"
    :footer="null"
  >
    <!--@ok="handleOk"-->
    <!--@cancel="handleCancel"-->

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
              <a-col :span="6">
                <a-form-item label="入库单号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input disabled v-decorator="[ 'recordNo', validatorRules.recordNo]" placeholder="请输入出入库单号"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="入库日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <j-date disabled placeholder="请选择入库日期" v-decorator="[ 'recordDate', validatorRules.recordDate]" :trigger-change="true" style="width: 100%"/>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="入库类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <j-dict-select-tag :triggerChange="true" v-decorator="['inType', validatorRules.inType]" title="入库类型" dictCode="in_type"/>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="操作人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input disabled v-decorator="[ 'recordPeopleName', validatorRules.recordPeopleName]" placeholder="请输入操作人"></a-input>
                </a-form-item>
                <a-form-item v-show="false">
                  <a-input v-show="false" v-decorator="[ 'recordPeople', {}]"></a-input>
                </a-form-item>
                <a-form-item v-show="false" label="入库部门ID">
                  <a-input v-show="false" v-decorator="[ 'inDepartId', {}]" ></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="6">
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
              <!--</a-col>-->
            </a-row>
          </a-form>

          <div class="table-operator">
            <a-button @click="choosePurchaseOrder" type="primary" icon="import">从订单导入</a-button>
          </div>
          <!-- 订单明细表区域 ref="pdPurchaseOrderDetail"-->
          <a-table
            v-show="showOrderTable"
            ref="table"
            size="middle"
            bordered
            rowKey="productId"
            :pagination="false"
            :columns="pdPurchaseOrderDetailTable.columns"
            :dataSource="pdPurchaseOrderDetailTable.dataSource"
            :loading="pdPurchaseOrderDetailTable.loading" >
            <template slot="htmlSlot" slot-scope="text">
              <div v-html="text"></div>
            </template>
          </a-table>
        </a-card>

        <!-- 子表单区域 -->
        <a-card style="">
          <a-tabs v-model="activeKey" @change="handleChangeTabs">
            <a-tab-pane tab="产品明细" :key="refKeys[0]" :forceRender="true">
              <a-form >
                <a-row>
                  <a-col :md="6" :sm="8">
                    <a-form-item label="产品编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <a-input ref="productNumberInput" v-focus placeholder="请输入产品编号" v-model="queryParam.productNumber" @keyup.enter.native="searchQuery(0)"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="8">
                    <a-form-item label="二级条码" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <a-input ref="productBarCodeInput" placeholder="请输入二级条码" v-model="queryParam.productBarCode" @keyup.enter.native="searchQuery(1)"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="8">
                    <a-form-item label="" :labelCol="labelCol" :wrapperCol="wrapperCol" style="text-align: right">
                      提示：按<span style="color: red">Ctrl+Alt</span>键快速定位到扫码输入框
                    </a-form-item>
                  </a-col>
                </a-row>
                <!--<a-row>-->
                  <!--<a-col :md="6" :sm="8">-->
                    <!--<a-form-item label="产品名称" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
                      <!--<a-input placeholder="请输入产品编号" v-model="queryParam.productName" @keyup.enter.native="searchQuery(2)"></a-input>-->
                    <!--</a-form-item>-->
                  <!--</a-col>-->
                  <!--<a-col :md="6" :sm="8">-->
                    <!--<a-form-item label="规格" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
                      <!--<a-input placeholder="请输入规格" v-model="queryParam.spec" @keyup.enter.native="searchQuery(2)"></a-input>-->
                    <!--</a-form-item>-->
                  <!--</a-col>-->
                  <!--<a-col :md="6" :sm="8">-->
                    <!--<a-form-item label="型号" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
                      <!--<a-input placeholder="请输入型号" v-model="queryParam.version" @keyup.enter.native="searchQuery(2)"></a-input>-->
                    <!--</a-form-item>-->
                  <!--</a-col>-->
                <!--</a-row>-->
              </a-form>

              <div style="margin-bottom: 8px;">
                <a-button type="primary" icon="plus" @click="handleConfirmAdd">新增</a-button>
                <span style="padding-left: 8px;"></span>
                <a-popconfirm
                  :title="`确定要删除吗?`"
                  @confirm="handleConfirmDelete">
                  <a-button type="primary" icon="minus">删除</a-button>
                  <span class="gap"></span>
                </a-popconfirm>
              </div>

              <j-editable-table
                :ref="refKeys[0]"
                :loading="pdStockRecordDetailTable.loading"
                :columns="pdStockRecordDetailTable.columns"
                :dataSource="pdStockRecordDetailTable.dataSource"
                :maxHeight="600"
                :rowNumber="true"
                :rowSelection="true"
                :actionButton="false"
                @valueChange="valueChange" />
            </a-tab-pane>
          </a-tabs>
        </a-card>

      </div>

      <div class="drawer-bootom-button">
        <a-popconfirm title="确定放弃编辑？" @confirm="handleCancel" okText="确定" cancelText="取消">
          <a-button style="margin-right: 15px;">取  消</a-button>
        </a-popconfirm>
        <a-button @click="handleOk" type="primary" :loading="confirmLoading" style="margin-right: 15px;">保存草稿</a-button>
        <a-button @click="handleOk" type="primary" :loading="confirmLoading" style="margin-right: 15px;">提  交</a-button>
      </div>

    </a-spin>

    <pd-choose-purchase-order-list-model  ref="pdChoosePurchaseOrderListModel" @ok="returnPurchaseOrderData" ></pd-choose-purchase-order-list-model>
    <pd-choose-product-list-model  ref="pdChooseProductListModel" @ok="returnProductData" ></pd-choose-product-list-model>
  </a-modal>
</template>

<script>

  import Vue from 'vue'
  import pick from 'lodash.pick'
  import { FormTypes,getRefPromise,validateFormAndTables } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import JDate from '@/components/jeecg/JDate'
  import {httpAction, deleteAction, getAction} from '@/api/manage'
  import PdChoosePurchaseOrderListModel from "./PdChoosePurchaseOrderListModel";
  import PdChooseProductListModel from "./PdChooseProductListModel";

  const VALIDATE_NO_PASSED = Symbol()
  export { FormTypes, VALIDATE_NO_PASSED }

  // 自定义焦点事件
  Vue.directive('focus', {
    // 当被绑定的元素插入到 DOM 中时……
    inserted: function (el) {
      //全局监听键盘事件
      document.onkeydown = function(event) {
        if(event.ctrlKey && event.altKey) {
          // 聚焦元素
          el.focus()
        }
      }
    }
  })

  export default {
    name: 'PdStockRecordInModal',
    mixins: [JEditableTableMixin],
    components: {
      PdChooseProductListModel,
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
        queryParam:{},
        //供应商下拉列表 start
        supplierValue: undefined,
        notFoundContent:"未找到内容",
        supplierData: [],
        //供应商下拉列表 end
        showOrderTable:false,

        //货区货位二级联动下拉框
        goodsAllocationList:[],

        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 0,
        validatorRules: {
          recordNo:{},
          recodeType:{},
          outType:{},
          inType:{rules: [{required: true, message: '请选择入库类型!'}]},
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
          inDepartId:{},
          supplierId:{rules: [{required: true, message: '请选择供应商!'}]},
          checkPeople:{},
          checkTime:{},
          returnState:{},
          extend1:{},
          extend2:{},
          extend3:{},
          delFlag:{},
          sysOrgParentCode:{},
        },
        refKeys: ['pdStockRecordDetail',],
        tableKeys:['pdStockRecordDetail', ],
        activeKey: 'pdStockRecordDetail',
        // 申购订单明细表
        pdPurchaseOrderDetailTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '产品ID',
              align:"center",
              dataIndex: 'productId',
              type: FormTypes.hidden
            },
            {
              title: '申购单号',
              align:"center",
              dataIndex: 'orderNo',
            },
            {
              title: '产品编码',
              align:"center",
              dataIndex: 'productNo'
            },
            {
              title: '产品名称',
              align:"center",
              dataIndex: 'productName'
            },
            {
              title: '规格',
              align:"center",
              dataIndex: 'spec'

            },
            {
              title: '型号',
              align:"center",
              dataIndex: 'version'
            },
            {
              title: '数量',
              align:"center",
              dataIndex: 'applyCount'
            },
            {
              title: '单位',
              align:"center",
              dataIndex: 'unitName'
            },
            {
              title: '生产厂家',
              align:"center",
              dataIndex: 'venderName'
            },
            {
              title: '供应商',
              align:"center",
              dataIndex: 'supplierName'
            },
          ],
        },
        // 出入库明细表
        pdStockRecordDetailTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '产品ID',
              key: 'productId',
              type: FormTypes.hidden,
              align:"center",
            },
            {
              title: '产品名称',
              key: 'productName',
              align:"center",
              width:"150px",
            },
            {
              title: '产品编号',
              key: 'productNumber',
              align:"center",
              width:"150px",
            },
            {
              title: '产品条码',
              key: 'productBarCode',
              align:"center",
              width:"150px",
            },
            {
              title: '规格',
              key: 'spec',
              align:"center",
              width:"80px",
            },
            {
              title: '型号',
              key: 'version',
              align:"center",
              width:"80px",
            },
            {
              title: '单位',
              key: 'unitName',
              align:"center",
              width:"50px",
            },
            {
              title: '批号',
              key: 'batchNo',
              width:"120px",
              align:"center",
              type: FormTypes.input,
              placeholder: '${title}',
              defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' }]
            },
            {
              title: '有效期',
              key: 'limitDate',
              type: FormTypes.date,
              width:"130px",
              placeholder: '${title}',
              defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' }]
            },
            {
              title: '数量',
              key: 'productNum',
              type: FormTypes.input,
              width:"80px",
              placeholder: '${title}',
              defaultValue: '1',
              validateRules: [{ required: true, message: '${title}不能为空' },{pattern: '^-?\\d+$',message: '${title}的格式不正确' }]
            },
            {
              title: '入库单价',
              key: 'inPrice',
              align:"center",
              width:"80px",
            },
            {
              title: '金额',
              key: 'price',
              align:"center",
              width:"80px",
            },
            {
              title: '货区',
              key: 'huoqu',
              type: FormTypes.select,
              width:"150px",
              options: [],
              placeholder: '${title}',
              validateRules: [{ required: true, message: '${title}不能为空' }]
            },
            {
              title: '货位',
              key: 'goodsAllocationId',
              type: FormTypes.select,
              width:"150px",
              options: [],
              placeholder: '${title}',
              validateRules: [{ required: true, message: '${title}不能为空' }]
            }
          ]
        },
        url: {
          init:"/pd/pdStockRecordIn/initModal",
          add: "/pd/pdStockRecordIn/add",
          edit: "/pd/pdStockRecordIn/edit",
          querySupplier:"/pd/pdSupplier/getSupplierList",
          pdStockRecordDetail: {
            list: '/pd/pdStockRecordIn/queryPdStockRecordDetailByMainId'
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
      // 重写close方法
      close() {
        this.visible = false;
        this.showOrderTable = false;
        this.pdPurchaseOrderDetailTable.dataSource = [];
        this.eachAllTable((item) => {
          item.initialize()
        })
        this.$emit('close')
      },
      getAllTable() {
        let values = this.tableKeys.map(key => getRefPromise(this, key))
        return Promise.all(values)
      },
      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
        // 加载子表数据
        if (this.model.id) {
          let fieldval = pick(this.model,'recordNo','inType','recordPeople','recordPeopleName','recordDate','remarks','inDepartId','supplierId')
          this.$nextTick(() => {
            this.form.setFieldsValue(fieldval)
          })
          let params = { id: this.model.id }
          this.requestSubTableData(this.url.pdStockRecordDetail.list, params, this.pdStockRecordDetailTable)
        }else{
          this.loadData();
        }
        // this.$nextTick(() => { //这种写法有问题 用自定义钩子函数v-focus
        //   document.onkeydown = function(event) {
        //     if(event.ctrlKey && event.altKey) {
        //       // 聚焦元素
        //       this.$refs.productNumberInput.focus();
        //     }
        //   }
        // })
      },
      loadData() {
        this.loading = true;
        getAction(this.url.init, {}).then((res) => {
          if (res.success) {
            this.initData = res.result;
            // this.initData.resultData = res.result.resultDataStr;
            let fieldval = pick(this.initData,'recordNo','inType','recordPeople','recordPeopleName','recordDate','remarks','inDepartId','supplierId');
            this.goodsAllocationList = this.initData.goodsAllocationList;
            this.$nextTick(() => {
              this.form.setFieldsValue(fieldval)
              this.pdStockRecordDetailTable.columns[12].options = this.goodsAllocationList.filter(i => i.parent === null)
            })
          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
          this.loading = false;
        })
      },
      /** 确定按钮点击事件 */
      handleOk() {
        /** 触发表单验证 */
        this.getAllTable().then(tables => {
          /** 一次性验证主表和所有的次表 */
          return validateFormAndTables(this.form, tables)
        }).then(allValues => {
          if (typeof this.classifyIntoFormData !== 'function') {
            throw this.throwNotFunction('classifyIntoFormData')
          }
          let formData = this.classifyIntoFormData(allValues)
          // 发起请求
          // return this.request(formData)
        }).catch(e => {
          if (e.error === VALIDATE_NO_PASSED) {
            // 如果有未通过表单验证的子表，就自动跳转到它所在的tab
            this.activeKey = e.index == null ? this.activeKey : this.refKeys[e.index]
          } else {
            console.error(e)
          }
        })
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
        this.form.setFieldsValue(pick(row,'recordNo','recodeType','outType','inType','orderNo','allocationNo','applyNo','dosagertNo','recordPeople','recordDate','recordState','rejectReason','remarks','testResult','storageResult','temperature','humidity','outDepartId','inDepartId','supplierId','checkPeople','checkTime','returnState','extend1','extend2','extend3','delFlag','sysOrgParentCode'))
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
      //-----------------供应商查询start
      supplierHandleSearch(value) {
        fetch(value, data => (this.supplierData = data),this.url.querySupplier);
      },
      supplierHandleChange(value) {
        this.supplierValue = value;
        fetch(value, data => (this.supplierData = data),this.url.querySupplier);
      },
      //----------------供应商查询end
      choosePurchaseOrder() {
        this.$refs.pdChoosePurchaseOrderListModel.show();
      },
      //选择订单后回调函数
      returnPurchaseOrderData(data) {
        // this.$refs.pdPurchaseOrderDetail.getValues((error, values) => {
          this.showOrderTable = true;
          this.pdPurchaseOrderDetailTable.dataSource = data;
        // })
      },
      returnProductData(data) {

      },
      //删除行
      handleConfirmDelete() {
        if(this.$refs.pdStockRecordDetail.selectedRowIds.length > 0){
          this.$refs.pdStockRecordDetail.removeSelectedRows();
        }else{
          this.$message.error("请选择需要删除的数据！")
        }
        // this.$nextTick(() => {
        //   this.valueChange();
        // })
      },
      // 新增行
      handleConfirmAdd() {
        this.$refs.pdChooseProductListModel.show();
      },
      returnProductData(data) {
        this.$refs.pdStockRecordDetail.getValues((error, values) => {
          this.pdStockRecordDetailTable.dataSource = values;
          if(values.length > 0){
            data.forEach((item, idx) => {
              let bool = true;
              values.forEach((value, idx) => {
                if (value.productId == item.productId){
                  bool = false;
                }
              })
              if(bool){
                this.addrows(item);
              }
            })
          }else{
            data.forEach((item, idx) => {
              this.addrows(item);
            })
          }
          this.$nextTick(() => {
            this.valueChange();
          })
        })
      },
      addrows(row){
        let data = {
          productId: row.productId,
          productName: row.productName,
          productNumber:row.number,
          spec: row.spec,
          version: row.version,
          inPrice:row.purchasePrice,
          unitName: row.unitName,
          venderName: row.venderName,
          supplierName: row.supplierName,
          productNum: 1
        }
        this.pdStockRecordDetailTable.dataSource.push(data);
        this.$refs.pdStockRecordDetail.add();
      },
      // 产品数量变更
      valueChange(event) {
        const { type, row, column, value, target } = event;

        if (type === FormTypes.select) {
          if (column.key === 'huoqu') {

            let options = this.goodsAllocationList.filter(i => i.parent === value)
            this.pdStockRecordDetailTable.columns[13].options = options
            // 清空库区下拉框数据
            target.setValues([{
              rowKey: row.id,
              values: { goodsAllocationId: '' }
            }])
          }
        }

        // this.$refs.pdStockRecordDetail.getValues((error, values) => {
          // let sum = 0;
          // values.forEach((item, idx) => {
          //   sum = sum + Number(item.count);
          // })
          // this.form.setFieldsValue({sum:sum});
        // })
      },
      searchQuery(num) {
        if(num == 0){
          //产品编号扫码
          if(!this.queryParam.productNumber){
            this.$message.error("请输入产品编号！")
          }else{
            this.$refs.productBarCodeInput.focus();
          }
        }else if(num == 1){
          //条码扫码查询 TODO
        }else if(num == 2){
          //名称 规格 型号 查询 TODO
        }
      },
      searchProductList(num){

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
  .drawer-bootom-button {
    width: 100%;
    text-align: right;
    background: #fff;
    margin-top:10px;
  }
</style>