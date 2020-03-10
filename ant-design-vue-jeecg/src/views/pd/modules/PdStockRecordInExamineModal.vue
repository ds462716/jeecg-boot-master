<template>
  <a-modal
    :visible="visible"
    :width="popModal.width"
    :style="popModal.style"
    :maskClosable="disableSubmit"
    :confirmLoading="confirmLoading"
    @cancel="handleCancel"
    :footer="null"
  >

    <template slot="title">
      <div style="width: 100%;height:20px;padding-right:32px;">
        <div style="float: left;">{{ title }}</div>
        <div style="float: right;">
          <a-button icon="fullscreen" style="width:56px;height:100%;border:0" @click="handleClickToggleFullScreen"/>
        </div>
      </div>
    </template>

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
                  <j-date disabled placeholder="请选择入库日期" v-decorator="[ 'submitDate', validatorRules.submitDate]" :trigger-change="true" style="width: 100%"/>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="入库类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <j-dict-select-tag-expand disabled type="list" v-decorator="['inType', validatorRules.inType]" :trigger-change="true" dictCode="in_type" placeholder="请选择入库类型"/>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="操作人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input disabled v-decorator="[ 'submitByName', validatorRules.submitByName]" placeholder="请输入操作人"></a-input>
                </a-form-item>
                <a-form-item v-show="false" label="操作人ID">
                  <a-input v-show="false" v-decorator="[ 'submitBy', {}]"></a-input>
                </a-form-item>
                <a-form-item v-show="false" label="入库部门ID">
                  <a-input v-show="false" v-decorator="[ 'inDepartId', {}]" ></a-input>
                </a-form-item>
                <a-form-item v-show="false" label="采购订单号">
                  <a-input v-show="false" v-decorator="[ 'orderNo', {}]" ></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="供应商" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-select
                    showSearch
                    placeholder="请选择供应商"
                    disabled
                    :supplierId="supplierValue"
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
            </a-row>
          </a-form>

          <div class="table-operator">
            <!-- 订单明细表区域 -->
            <a-tabs>
              <a-tab-pane tab="订单明细" :forceRender="true">
                <a-table
                  v-show="showOrderTable"
                  ref="table"
                  size="middle"
                  bordered
                  rowKey="id"
                  :pagination="false"
                  :columns="pdPurchaseOrderDetailTable.columns"
                  :dataSource="pdPurchaseOrderDetailTable.dataSource"
                  :loading="pdPurchaseOrderDetailTable.loading" >
                  <template slot="htmlSlot" slot-scope="text">
                    <div v-html="text"></div>
                  </template>
                </a-table>
              </a-tab-pane>
            </a-tabs>
          </div>
        </a-card>

        <!-- 产品列表区域 -->
        <a-card style="margin-bottom: 10px;">
          <a-tabs v-model="activeKey" @change="handleChangeTabs">
            <a-tab-pane tab="产品明细" :key="refKeys[0]" :forceRender="true">

              <j-editable-table
                bordered
                :ref="refKeys[0]"
                :loading="pdStockRecordDetailTable.loading"
                :columns="pdStockRecordDetailTable.columns"
                :dataSource="pdStockRecordDetailTable.dataSource"
                :maxHeight="500"
                :rowNumber="true"
                :rowSelection="true"
                :actionButton="false"
                disabled
                style="text-overflow: ellipsis;"
              >
              <!--:maxHeight 大于 600 后就会有BUG 一次性选择9条以上产品，会少显示一条-->
              </j-editable-table>
              <a-row style="margin-top:10px;text-align: right;padding-right: 5%">
                  <span style="font-weight: bold;font-size: large;padding-right: 5%">总数量：{{ totalSum }}</span>
                  <span style="font-weight: bold;font-size: large">总金额：{{ totalPrice }}</span>
              </a-row>
            </a-tab-pane>
          </a-tabs>
        </a-card>

        <!-- 验收区域 -->
        <a-card style="margin-bottom: 10px;">
          <a-form :form="form">
            <a-row>
              <a-col :span="6">
                <a-form-item label="验收结果" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <j-dict-select-tag disabled v-decorator="[ 'testResult', validatorRules.testResult]" placeholder="请选择验收结果" :type="'radio'" :triggerChange="true" dictCode="test_result"/>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="储运状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <j-dict-select-tag disabled v-decorator="[ 'storageResult', validatorRules.storageResult]" placeholder="请选择储运状态" :type="'radio'" :triggerChange="true" dictCode="storage_result"/>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="6">
                <a-form-item label="温度（℃）" :labelCol="labelCol3" :wrapperCol="wrapperCol3">
                 <a-input disabled v-decorator="[ 'temperature', validatorRules.temperature]" placeholder=""></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="湿度（%）" :labelCol="labelCol3" :wrapperCol="wrapperCol3">
                  <a-input disabled v-decorator="[ 'humidity', validatorRules.humidity]" placeholder=""></a-input>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item label="备注" :labelCol="labelCol2" :wrapperCol="wrapperCol2" style="text-align: left">
                  <a-textarea disabled v-decorator="[ 'remarks', validatorRules.remarks]" placeholder="请输入备注"></a-textarea>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-card>


        <a-card style="">
          <a-form :form="form">
            <a-col :span="12">
              <a-form-item label="审批意见" :labelCol="labelCol2" :wrapperCol="wrapperCol2" style="text-align: left">
                <a-textarea :disabled="disableSubmit" v-decorator="[ 'refuseReason', validatorRules.refuseReason]" placeholder="请输入审批意见"></a-textarea>
              </a-form-item>
            </a-col>
          </a-form>
        </a-card>
      </div>

      <div class="drawer-bootom-button">
        <a-button @click="closeBtn" style="margin-right: 15px;" v-show="disableSubmit">关  闭</a-button>
        <a-popconfirm title="确定放弃编辑？" @confirm="handleCancel" v-show="!disableSubmit" okText="确定" cancelText="取消">
          <a-button style="margin-right: 15px;">取  消</a-button>
        </a-popconfirm>
        <a-button @click="refuseBtn" v-show="!disableSubmit" type="danger" :loading="confirmLoading" style="margin-right: 15px;">驳回</a-button>
        <a-button @click="submitBtn" v-show="!disableSubmit" type="primary" :loading="confirmLoading" style="margin-right: 15px;">审核通过</a-button>
      </div>

    </a-spin>

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
  import JDictSelectTagExpand from "@/components/dict/JDictSelectTagExpand"
  import ATextarea from "ant-design-vue/es/input/TextArea";
  import {scanCode} from '@/utils/barcode'

  const VALIDATE_NO_PASSED = Symbol()
  export { FormTypes, VALIDATE_NO_PASSED }

  // 自定义焦点事件
  Vue.directive('focus', {
    // 当被绑定的元素插入到 DOM 中时……
    inserted: function (el) {
      //全局监听键盘事件
      document.onkeydown = function(event) {
        if(event.ctrlKey && event.altKey) {
          // 按ctrl+alt  聚焦元素
          el.focus()
        }
      }
    }
  })

  export default {
    name: 'PdStockRecordInExamineModal',
    mixins: [JEditableTableMixin],
    components: {
      ATextarea,
      JDate,
      JDictSelectTagExpand
    },
    data() {
      return {
        labelCol: {span: 6},
        wrapperCol: {span: 16},

        labelCol2: {span: 3},
        wrapperCol2: {span: 20},

        labelCol3: {span: 6},
        wrapperCol3: {span: 3},

        labelCol4: {span: 13},
        wrapperCol4: {span: 5},
        disableSubmit:false,

        initData:{},
        queryParam:{},
        allowInMoreOrder:"",		//开关-是否允许入库量大于订单量   1-允许入库量大于订单量；0-不允许入库量大于订单量
        allowNotOrderProduct:"",		//开关-是否允许入库非订单产品     1-允许非订单产品；0-不允许非订单产品
        //供应商下拉列表 start
        supplierValue: undefined,
        notFoundContent:"未找到内容",
        supplierData: [],
        //供应商下拉列表 end
        showOrderTable:false,
        orderNo:"",
        totalSum:'0',
        totalPrice:'0.0000',
        submitDateStr:"",

        //货区货位二级联动下拉框
        goodsAllocationList:[],
        huoquOptions:[],
        huoweiOptions:[],

        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 0,
        validatorRules: {
          inType:{rules: [{required: true, message: '请选择入库类型!'}]},
          refuseReason:{},
          remarks:{},
          isAllowProduct:{rules: [{required: true, message: '请选择!'}]},
          isAllowNum:{rules: [{required: true, message: '请选择!'}]},
          testResult:{rules: [{required: true, message: '请选择验收结果!'}]},
          storageResult:{rules: [{required: true, message: '请选择储运状态!'}]},
          temperature:{rules: [{required: true, message: '请输入温度!'},{pattern: '^-?\\d+\\.?\\d*$$',message: '只能输入数字' }]},
          humidity:{rules: [{required: true, message: '请输入湿度!'},{pattern: '^-?\\d+\\.?\\d*$',message: '只能输入数字' }]},
          supplierId:{rules: [{required: true, message: '请选择供应商!'}]},
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
              title: '产品ID', align:"center", dataIndex: 'productId',
              colSpan: 0,
              customRender: (value, row, index) => {
                const obj = {
                  attrs: {colSpan:0},
                };
                return obj;
              },
            },
            { title: '申购单号', align:"center", dataIndex: 'orderNo' },
            { title: '产品编码', align:"center", dataIndex: 'number' },
            { title: '产品名称', align:"center", dataIndex: 'productName' },
            { title: '规格', align:"center", dataIndex: 'spec' },
            { title: '型号', align:"center", dataIndex: 'version' },
            { title: '数量', align:"center", dataIndex: 'orderNum' },
            { title:'到货数量', align:"center", dataIndex: 'arrivalNum' },
            { title: '单位', align:"center", dataIndex: 'unitName' },
            { title: '生产厂家', align:"center", dataIndex: 'venderName' },
            { title: '供应商', align:"center", dataIndex: 'supplierName' },
          ],
        },
        // 出入库明细表(产品明细)
        pdStockRecordDetailTable: {
          loading: false,
          dataSource: [],
          columns: [
            { title: '产品ID', key: 'productId', type: FormTypes.hidden },
            { title: '产品名称', key: 'productName', width:"220px" },
            { title: '产品编号', key: 'productNumber', width:"150px" },
            { title: '产品条码', key: 'productBarCode', width:"200px" },
            { title: '规格', key: 'spec', width:"150px" },
            // { title: '型号', key: 'version', width:"150px" },
            { title: '单位', key: 'unitName', width:"50px" },
            {
              title: '有效期', key: 'expDate', type: FormTypes.date, width:"130px",
              placeholder: '${title}', defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' }]
            },
            {
              title: '批号', key: 'batchNo', width:"120px", type: FormTypes.input,
              placeholder: '${title}', defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' }]
            },
            {
              title: '数量', key: 'productNum', type: FormTypes.input, width:"80px",
              placeholder: '${title}', defaultValue: '1',
              validateRules: [{ required: true, message: '${title}不能为空' },{ pattern: '^-?\\d+\\.?\\d*$',message: '${title}的格式不正确' }]
            },
            { title: '入库单价', key: 'purchasePrice', width:"80px" },
            { title: '金额', key: 'inTotalPrice', width:"90px" },
            // {
            //   title: '货区', key: 'huoquId', type: FormTypes.select, width:"150px", options: this.huoquOptions,allowSearch:true,
            //   placeholder: '${title}', validateRules: [{ required: true, message: '${title}不能为空' }]
            // },
            {
              title: '货位', key: 'inHuoweiCode', type: FormTypes.select, width:"150px", options: [],allowSearch:true,
              placeholder: '${title}', validateRules: [{ required: true, message: '${title}不能为空' }]
            },
            { title: '申购单号', key: 'orderNo', width:"180px" }
          ]
        },
        url: {
          init:"/pd/pdStockRecordIn/initModal",
          // submit: "/pd/pdStockRecordIn/submit",
          audit: "/pd/pdStockRecordIn/audit",
          // edit: "/pd/pdStockRecordIn/edit",
          querySupplier:"/pd/pdSupplier/getSupplierList",
          pdStockRecordDetail: {
            list: "/pd/pdStockRecordIn/queryPdStockRecordDetailByMainId"
          },
          pdPurchaseDetail: {
            list: "/pd/pdPurchaseOrder/queryPdPurchaseDetail"
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
        this.$emit('close')
      },
      getAllTable() {
        let values = this.tableKeys.map(key => getRefPromise(this, key))
        return Promise.all(values)
      },
      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
        this.loadData();
      },
      loadData() {
        this.loading = true;

        //初始化供应商，用于回显供应商
        this.supplierHandleSearch();

        let fieldval = pick(this.model,'recordNo','inType','submitBy','submitByName','submitDate','remarks','inDepartId','supplierId',
          'testResult','storageResult','temperature','humidity','remarks','refuseReason')
        this.$nextTick(() => {
          this.form.setFieldsValue(fieldval);
        })
        let params = { id: this.model.id }

        getAction(this.url.init, params).then((res) => {
          if (res.success) {
            this.$nextTick(() => {
              if(this.model.id){
                this.showOrderTable = true;
                this.pdPurchaseOrderDetailTable.dataSource = res.result.pdPurchaseDetailList || [];
                this.pdStockRecordDetailTable.dataSource = res.result.pdStockRecordDetailList || [];
                this.totalSum = res.result.totalSum;
                this.totalPrice = res.result.totalPrice.toString();
              }

              this.goodsAllocationList = res.result.goodsAllocationList;
              this.pdStockRecordDetailTable.columns.forEach((item, idx) => {
                if(item.key === "inHuoweiCode"){
                  item.options = this.goodsAllocationList;
                }
              })
            })
          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
          this.loading = false;
        })
      },
      /** 关闭按钮 **/
      closeBtn(){
        this.close();
      },
      /** 拒绝 **/
      refuseBtn() {
        let params =
          {
            id: this.model.id,
            refuseReason: this.form.getFieldValue("refuseReason"),
            auditStatus: "3"  // 拒绝
          }
        this.request(params);
      },
      /** 确定按钮点击事件 */
      submitBtn() {
        let params =
          {
            id: this.model.id,
            refuseReason: this.form.getFieldValue("refuseReason"),
            auditStatus: "2" // 通过
          }
        this.request(params);
      },
      // 保存 提交 修改 请求函数
      request(params) {
        this.confirmLoading = true
        httpAction(this.url.audit, params, "post").then((res) => {
          if (res.success) {
            this.$message.success(res.message)
            this.$emit('ok')
            this.close();
          } else {
            this.$message.warning(res.message)
          }
        }).finally(() => {
          this.confirmLoading = false
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
        this.form.setFieldsValue(pick(row,'recordNo','inType','submitBy','submitByName','submitDate','remarks','inDepartId','supplierId',
          'testResult','storageResult','temperature','humidity','remarks','refuseReason'))
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
        this.totalSum = '0';
        this.totalPrice = '0.0000';
        this.eachAllTable((item) => {
          item.initialize()
        })
        this.supplierValue = value;
        fetch(value, data => (this.supplierData = data),this.url.querySupplier);
      },
      //----------------供应商查询end
    },
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