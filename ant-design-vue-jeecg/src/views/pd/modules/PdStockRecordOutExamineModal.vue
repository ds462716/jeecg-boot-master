<template>
  <j-modal
    :visible="visible"
    :width="popModal.width"
    :maskClosable="disableSubmit"
    :title="popModal.title"
    :lockScroll="popModal.lockScroll"
    :fullscreen="popModal.fullscreen"
    :switchFullscreen="popModal.switchFullscreen"
    @cancel="handleCancel"
  >

    <a-spin :spinning="confirmLoading">
      <!-- 主表单区域 -->
      <div style="background:#ECECEC; padding:20px">
        <a-card title="" style="margin-bottom: 10px;">
          <a-form :form="form">
            <a-row>
              <a-col :span="6">
                <a-form-item label="出库单号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input disabled v-decorator="[ 'recordNo', validatorRules.recordNo]" placeholder="请输入出库单号"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="出库日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <j-date disabled placeholder="请选择出库日期" v-decorator="[ 'submitDate', validatorRules.submitDate]" :trigger-change="true" style="width: 100%"/>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="操作人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input disabled v-decorator="[ 'submitByName', validatorRules.submitByName]" placeholder="请输入操作人"></a-input>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="6">
                <a-form-item label="出库库房" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input disabled v-decorator="[ 'outDepartName', validatorRules.outDepartName]" placeholder=""></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="入库库房" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-select
                    showSearch
                    placeholder="请选择入库库房"
                    disabled
                    :defaultActiveFirstOption="false"
                    :showArrow="true"
                    :filterOption="false"
                    :notFoundContent="notFoundContent"
                    v-decorator="[ 'inDepartId', validatorRules.inDepartId]"
                  >
                    <a-select-option v-for="d in departList" :key="d.id">{{d.departName}}</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="出库类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <j-dict-select-tag-expand disabled type="list" v-decorator="['outType', validatorRules.outType]" :trigger-change="true" dictCode="out_type" placeholder="请选择出库类型"/>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="领用人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-select
                    showSearch
                    placeholder="请选择领用人"
                    disabled
                    :defaultActiveFirstOption="false"
                    :showArrow="true"
                    :filterOption="false"
                    :notFoundContent="notFoundContent"
                    v-decorator="[ 'applyBy', validatorRules.applyBy]"
                  >
                    <a-select-option v-for="d in userList" :key="d.id" :text="d.realname" >{{d.realname}}</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>

          <div class="table-operator" v-show="showOrderTable">
            <!-- 申领单 调拨单表区域 -->
            <a-tabs>
              <a-tab-pane :tab="orderTableTitle" :forceRender="true">
                <a-table
                  v-show="showOrderTable"
                  ref="table"
                  size="middle"
                  bordered
                  rowKey="id"
                  :pagination="false"
                  :columns="pdOrderDetailTable.columns"
                  :dataSource="pdOrderDetailTable.dataSource"
                  :loading="pdOrderDetailTable.loading" >
                  <template slot="htmlSlot" slot-scope="text">
                    <div v-html="text"></div>
                  </template>
                </a-table>
              </a-tab-pane>
            </a-tabs>
          </div>
        </a-card>

        <!-- 定数包区域 -->
        <a-card style="margin-bottom: 10px;" v-show="showPackageCard">
          <a-tabs v-model="activeKey">
            <a-tab-pane tab="定数包明细" :key="refKeys[0]" :forceRender="true">
              <a-table
                ref="table"
                bordered
                rowKey="id"
                :pagination="false"
                :expandedRowKeys= "pdPackageTable.expandedRowKeys"
                :columns="pdPackageTable.columns"
                :dataSource="pdPackageTable.dataSource"
                :loading="pdPackageTable.loading"
                @expand="handleExpand"
              >

                <a-table
                  slot="expandedRowRender"
                  slot-scope="text"
                  size="middle"
                  bordered
                  rowKey="id"
                  :pagination="false"
                  :columns="pdPackageTable.innerColumns"
                  :dataSource="pdPackageTable.innerData"
                  :loading="pdPackageTable.subloading"
                >

                </a-table>
              </a-table>
            </a-tab-pane>
          </a-tabs>
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
                :disabled="disableSubmit"
                disabled
                style="text-overflow: ellipsis;"
              >
                <!--:maxHeight 大于 600 后就会有BUG 一次性选择9条以上产品，会少显示一条-->
              </j-editable-table>
              <a-row style="margin-top:10px;text-align: right;padding-right: 5%">
                <span style="font-weight: bold;font-size: large;padding-right: 5%">总数量：{{ totalSum }}</span>
                <span style="font-weight: bold;font-size: large">总金额：{{ outTotalPrice }}</span>
              </a-row>
            </a-tab-pane>
          </a-tabs>
        </a-card>

        <a-card style="margin-bottom: 10px;">
          <a-form :form="form">
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
    </a-spin>

    <template slot="footer">
      <a-button @click="closeBtn" style="margin-right: 15px;" v-show="disableSubmit">关  闭</a-button>
      <a-button @click="printBtn('1')" style="margin-right: 15px;" type="primary" v-show="disableSubmit">打  印</a-button>
      <a-popconfirm title="确定放弃审核？" @confirm="handleCancel" v-show="!disableSubmit" okText="确定" cancelText="取消">
        <a-button style="margin-right: 15px;">取  消</a-button>
      </a-popconfirm>
      <a-popconfirm title="确定驳回？" @confirm="refuseBtn" v-show="!disableSubmit" okText="确定" cancelText="取消">
        <a-button type="danger" :loading="confirmLoading" style="margin-right: 15px;">驳回</a-button>
      </a-popconfirm>
      <!--<a-button @click="refuseBtn" v-show="!disableSubmit" type="danger" :loading="confirmLoading" style="margin-right: 15px;">驳回</a-button>-->
      <a-button @click="submitBtn('1')" v-show="!disableSubmit" type="primary" :loading="confirmLoading" style="margin-right: 15px;">审核通过</a-button>
      <a-button @click="submitBtn('2')" v-show="!disableSubmit" type="primary" :loading="confirmLoading" style="margin-right: 15px;">审核通过并打印</a-button>
    </template>

    <pd-stock-record-out-print-modal ref="pdStockRecordOutPrintModal"></pd-stock-record-out-print-modal>
    <ex-stock-record-out-print-modal ref="exStockRecordOutPrintModal"></ex-stock-record-out-print-modal>
  </j-modal>
</template>

<script>

  import pick from 'lodash.pick'
  import { FormTypes,getRefPromise,validateFormAndTables } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import JDate from '@/components/jeecg/JDate'
  import {httpAction, deleteAction, getAction} from '@/api/manage'
  import JDictSelectTagExpand from "@/components/dict/JDictSelectTagExpand"
  import ATextarea from "ant-design-vue/es/input/TextArea";
  import {stockScanCode} from '@/utils/barcode'
  import PdStockRecordOutPrintModal from "../print/PdStockRecordOutPrintModal";
  import ExStockRecordOutPrintModal from "../../external/print/ExStockRecordOutPrintModal";


  const VALIDATE_NO_PASSED = Symbol()
  export { FormTypes, VALIDATE_NO_PASSED }

  export default {
    name: 'PdStockRecordOutModal',
    mixins: [JEditableTableMixin],
    components: {
      ExStockRecordOutPrintModal,
      PdStockRecordOutPrintModal,
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

        //部门下拉列表 start
        departValue: undefined,
        notFoundContent:"未找到内容",
        departList:[],
        userList:[],
        //部门下拉列表 end
        hospitalCode:"",

        orderTableTitle:"",
        showOrderTable:false,
        showPackageCard:true,
        applyNo:"",
        allocationNo:"",
        totalSum:'0',
        outTotalPrice:'0.0000',
        inTotalPrice:'0.0000',
        submitDateStr:"",
        auditByName:"",
        stockOutText:"",

        args:{},
        //货区货位二级联动下拉框
        goodsAllocationList:[],
        huoquOptions:[],
        huoweiOptions:[],

        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 0,
        validatorRules: {
          recordNo:{},
          outDepartName:{},
          recordType:{},
          outType:{rules: [{required: true, message: '请选择出库类型!'}]},
          applyNo:{},
          allocationNo:{},
          remarks:{},
          outDepartId:{},
          inDepartId:{rules: [{required: true, message: '请选择入库库房!'}]},

        },
        refKeys: ['pdStockRecordDetail',],
        tableKeys:['pdStockRecordDetail', ],
        activeKey: 'pdStockRecordDetail',
        // 申购订单明细表
        pdOrderDetailTable: {
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
            { title: '单号', align:"center", dataIndex: 'orderNo' },
            { title: '产品编码', align:"center", dataIndex: 'number' },
            { title: '产品名称', align:"center", dataIndex: 'productName' },
            { title: '规格', align:"center", dataIndex: 'spec' },
            { title: '型号', align:"center", dataIndex: 'version' },
            { title: '申请数量', align:"center", dataIndex: 'productNum' },
            { title: '已发货数量', align:"center", dataIndex: 'arrivalNum' },
            { title: '单位', align:"center", dataIndex: 'unitName' },
            { title: '定数包名称', align:"center", dataIndex: 'packageName' },
            { title: '定数包编号', align:"center", dataIndex: 'packageCode' },
          ],
        },
        // 定数包列表
        pdPackageTable: {
          loading: false,
          dataSource: [],
          subloading:false,
          innerData:[],
          expandedRowKeys:[],
          /* table选中keys*/
          selectedRowKeys: [],
          /* table选中records*/
          selectionRows: [],
          columns: [
            {
              title: '#',
              dataIndex: 'id',
              key:'rowIndex',
              width:60,
              align:"center",
              customRender:function (t,r,index) {
                return parseInt(index)+1;
              }
            },
            { title:'定数包编号', align:"center", dataIndex: 'packageCode' },
            { title:'定数包条码', align:"center", dataIndex: 'packageBarCode' },
            { title:'定数包名称', align:"center", dataIndex: 'packageName' },
            { title:'产品总数', align:"center", dataIndex: 'packageSum' },
            { title:'打包人', align:"center", dataIndex: 'createBy' },
            { title:'打包时间', align:"center", dataIndex: 'createTime',
              customRender:function (text) {
                return !text?"":(text.length>10?text.substr(0,10):text)
              }
            },
            { title:'备注', align:"center", dataIndex: 'remarks' }
          ],
          innerColumns:[
            {
              title: '#',
              dataIndex: 'id',
              key:'rowIndex',
              width:60,
              align:"center",
              customRender:function (t,r,index) {
                return parseInt(index)+1;
              }
            },
            { title:'产品名称', align:"center", dataIndex: 'productName' },
            { title:'产品编号', align:"center", dataIndex: 'productNumber' },
            { title:'产品条码', align:"center",dataIndex: 'productBarCode' },
            { title:'规格', align:"center", dataIndex: 'spec' },
            { title:'批号', align:"center", dataIndex: 'batchNo' },
            // { title:'型号', align:"center", dataIndex: 'version' },
            { title:'单位', align:"center", dataIndex: 'unitName' },
            { title:'有效期', align:"center", dataIndex: 'expDate',
              customRender:function (text) {
                return !text?"":(text.length>10?text.substr(0,10):text)
              }
            },
            { title:'入库单价', align:"center", dataIndex: 'purchasePrice' },
            { title:'出库单价', align:"center", dataIndex: 'sellingPrice' },
            { title:'定数包产品数量', align:"center", dataIndex: 'productNum' },
            { title:'出库金额', align:"center", dataIndex: 'outTotalPrice' },
            { title:'库存数量', align:"center", dataIndex: 'stockNum' },
            { title: '出库货位', align:"center", dataIndex: 'outHuoweiName' },
            { title: '生产日期', align:"center", dataIndex: 'produceDate',
              customRender:function (text) {
                return !text?"":(text.length>10?text.substr(0,10):text)
              }
            },
            { title: '打包记录ID', align:"center", dataIndex: 'packageRecordId',
              colSpan: 0,
              customRender: (value, row, index) => {
                const obj = {
                  attrs: {colSpan:0},
                };
                return obj;
              },
            },
            { title: '库存明细ID', align:"center", dataIndex: 'productStockId', colSpan: 0,
              customRender: (value, row, index) => {
                const obj = {
                  attrs: {colSpan:0},
                };
                return obj;
              },
            },
            { title: '产品ID', align:"center", dataIndex: 'productId', colSpan: 0,
              customRender: (value, row, index) => {
                const obj = {
                  attrs: {colSpan:0},
                };
                return obj;
              },
            },
            { title: '出库货位编号', align:"center", dataIndex: 'outHuoweiCode', colSpan: 0,
              customRender: (value, row, index) => {
                const obj = {
                  attrs: {colSpan:0},
                };
                return obj;
              },
            },
            { title: '供应商id', align:"center", dataIndex: 'supplierId', colSpan: 0,
              customRender: (value, row, index) => {
                const obj = {
                  attrs: {colSpan:0},
                };
                return obj;
              },
            },
            { title: '规格单位ID', align:"center", dataIndex: 'specUnitId', colSpan: 0,
              customRender: (value, row, index) => {
                const obj = {
                  attrs: {colSpan:0},
                };
                return obj;
              },
            },
            { title: '规格数量', align:"center", dataIndex: 'specQuantity', colSpan: 0,
              customRender: (value, row, index) => {
                const obj = {
                  attrs: {colSpan:0},
                };
                return obj;
              },
            },
          ],
        },
        // 出入库明细表(产品明细)
        pdStockRecordDetailTable: {
          loading: false,
          dataSource: [],
          columns: [
            { title: '库存明细ID', key: 'productStockId', type: FormTypes.hidden },
            { title: '产品ID', key: 'productId', type: FormTypes.hidden },
            { title: '产品名称', key: 'productName', type: FormTypes.normal,width:"220px" },
            { title: '产品编号', key: 'productNumber', width:"160px" },
            { title: '产品条码', key: 'productBarCode', type: FormTypes.input, disabled:true, width:"200px" },
            { title: '规格', key: 'spec', width:"150px" },
            { title: '批号', key: 'batchNo', width:"100px" },
            { title: '单位', key: 'unitName', width:"50px" },
            { title: '有效期', key: 'expDate', width:"100px" },
            { title: '入库单价', key: 'purchasePrice', width:"80px" },
            { title: '出库单价', key: 'sellingPrice', width:"80px" },
            {
              title: '出库数量', key: 'productNum', type: FormTypes.input, width:"80px",
              placeholder: '${title}', defaultValue: '1',
              validateRules: [{ required: true, message: '${title}不能为空' },{ pattern: '^-?\\d+\\.?\\d*$',message: '${title}的格式不正确' }]
            },
            { title: '出库金额', key: 'outTotalPrice', type: FormTypes.input, disabled:true, width:"100px" },
            { title: '库存数量', key: 'stockNum', width:"80px" },
            { title: '出库货位', key: 'outHuoweiName', width:"100px" },
            { title: '出库货位编号', key: 'outHuoweiCode', type: FormTypes.hidden },
            { title: '入库货位', key: 'inHuoweiCode', type: FormTypes.select, width:"150px", options: [],allowSearch:true, placeholder: '${title}' },
            { title: '批量码', key: 'refBarCode' },
          ]
        },
        url: {
          init:"/pd/pdStockRecordOut/initModal",
          audit: "/pd/pdStockRecordOut/audit",
          userList:"/pd/pdDepart/queryUserByDepartParentId",
          departList:"/pd/pdDepart/getSysDepartList",
          queryById: "/pd/pdStockRecordOut/queryById",
        },
        popModal: {
          title: '这里是标题',
          visible: false,
          width: '100%',
          // width: '1200',
          style: { top: '20px' },
          switchFullscreen: true,  //缩放按钮
          lockScroll: false,
          fullscreen: true,
        },
      }
    },
    methods: {
      // 重写close方法
      close() {
        this.visible = false;
        this.showOrderTable = false;
        this.showPackageCard = false;
        this.pdOrderDetailTable.dataSource = [];
        this.pdPackageTable.pdPackageTable = [];
        this.pdPackageTable.selectedRowKeys = [];
        this.pdPackageTable.selectionRows = [];
        this.pdPackageTable.expandedRowKeys = [];
        this.pdPackageTable.dataSource = [];
        this.pdPackageTable.innerData = [];
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
        this.loadData();
      },
      loadData() {
        // this.loading = true;
        this.popModal.title="出库审核";
        this.departHandleSearch();  // 初始化部门列表 用于数据回显
        this.userHandleSearch();

        let fieldval = pick(this.model,'recordNo','outType','submitByName','submitDate','inDepartId','outDepartName','remarks','refuseReason','applyBy');

        let params = { id: this.model.id };

        this.pdStockRecordDetailTable.loading = true;
        getAction(this.url.init, params).then((res) => {
          if (res.success) {
            this.$nextTick(() => {
              if(this.model.id){ //详情页
                this.form.setFieldsValue(fieldval);
                this.showOrderTable = true;
                this.pdStockRecordDetailTable.dataSource = res.result.pdStockRecordDetailList || [];
                this.totalSum = res.result.totalSum;
                this.outTotalPrice = res.result.outTotalPrice.toString();
                this.inTotalPrice = res.result.inTotalPrice.toString();
                if(!this.model.auditByName){
                  this.model.auditByName = res.result.auditByName;
                }
                if(res.result.outType == "1"){
                  this.orderTableTitle = "申领单明细";
                  let pdApplyDetailList = res.result.pdApplyDetailList || [];
                  pdApplyDetailList.forEach((item, idx) => {
                    item.orderNo = item.applyNo;
                    item.productNum = item.applyNum;
                  })
                  this.pdOrderDetailTable.dataSource = pdApplyDetailList;
                }else if(res.result.outType == "2"){
                  this.showOrderTable = false;
                  this.orderTableTitle = "";
                }else if(res.result.outType == "3"){
                  this.orderTableTitle = "调拨单明细";
                  let pdApplyDetailList = res.result.pdAllocationDetailList || [];
                  pdApplyDetailList.forEach((item, idx) => {
                    item.orderNo = item.allocationNo;
                    item.productNum = item.allocationNum;
                  })
                  this.pdOrderDetailTable.dataSource = pdApplyDetailList;
                }
                this.stockOutText = res.result.stockOutText;
                this.hospitalCode = res.result.hospitalCode;
                //货区货位 下拉框
                this.goodsAllocationList = res.result.goodsAllocationList;
                this.pdStockRecordDetailTable.columns.forEach((item, idx) => {
                  if(item.key === "inHuoweiCode"){
                    item.options = this.goodsAllocationList;
                  }
                })

                // 定数包打包记录明细
                this.showPackageCard = false;
                let pdPackageRecordList = res.result.pdPackageRecordList || [];
                if(pdPackageRecordList.length > 0){
                  this.showPackageCard = true;
                  this.pdPackageTable.dataSource = pdPackageRecordList;
                }
              }
            })
          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
          // this.loading = false;
          this.pdStockRecordDetailTable.loading = false;
        })
      },
      /** 关闭按钮 **/
      closeBtn(){
        this.close();
      },
      /**打印按钮**/
      printBtn(flag){
        let recordId = this.model.id;
        if(!recordId){
          this.$message.warning("参数不正确，请重新打印！");
          return;
        }
        getAction(this.url.queryById, {id:this.model.id}).then((res) => {
          if(!res.result.auditDate){
            res.result.auditDate = res.result.submitDate;
          }
          if(this.hospitalCode == "FCZYY"){
            this.$refs.pdStockRecordOutPrintModal.show(res.result);
            this.$refs.pdStockRecordOutPrintModal.title = this.stockOutText + "出库单";
          }else if(this.hospitalCode == "GZSLYY"){
            this.$refs.exStockRecordOutPrintModal.show(res.result);
            this.$refs.exStockRecordOutPrintModal.title = this.stockOutText + "出库单";
          }else{
            this.$refs.pdStockRecordOutPrintModal.show(res.result);
            this.$refs.pdStockRecordOutPrintModal.title = this.stockOutText + "出库单";
          }
        })
      },
      /** 关闭按钮点击事件 */
      handleCancel() {
        this.$emit('ok');
        this.close();
      },
      /** 拒绝 **/
      refuseBtn() {
        let refuseReason = this.form.getFieldValue("refuseReason");
        if(!refuseReason){
          this.$message.warning("请填写审批意见！");
          return;
        }
        let params =
          {
            id: this.model.id,
            refuseReason: refuseReason,
            auditStatus: "3"  // 拒绝
          }
        this.request(params);
      },
      /** 确定按钮点击事件 */
      submitBtn(flag) {
        let refuseReason = this.form.getFieldValue("refuseReason");
        if(!refuseReason){
          refuseReason = "同意";
        }
        let params =
          {
            id: this.model.id,
            refuseReason: refuseReason,
            auditStatus: "2" // 通过
          }
        this.request(params,flag);
      },
      // 保存 提交 修改 请求函数
      request(params,flag) {
        this.confirmLoading = true
        httpAction(this.url.audit, params, "post").then((res) => {
          if (res.success) {
            this.$message.success(res.message);
            this.$emit('ok');
            if(flag == "2"){
              this.printBtn("2"); //通过并打印
            }
            this.close();
          } else {
            this.$message.warning(res.message)
          }
        }).finally(() => {
          this.confirmLoading = false
        })
      },
      //定数包展开按钮
      handleExpand(expanded, record){
        this.pdPackageTable.expandedRowKeys=[];
        this.pdPackageTable.innerData=[];
        if(expanded===true){
          this.pdPackageTable.expandedRowKeys.push(record.id);
          this.pdPackageTable.innerData = record.pdPackageRecordDetailList;
        }
      },
      onClickRow(record) {
        return {
          on: {
            click: (e) => {
              //点击操作那一行不选中表格的checkbox
              let pathArray = e.path;
              let td = pathArray[0];//获取当前点击的是第几列
              let cellIndex = td.cellIndex;
              let tr = pathArray[1];//获取tr
              let lie = tr.childElementCount;//获取一共多少列
              if(lie && cellIndex){
                if(parseInt(lie)-parseInt(cellIndex) > 0){
                  //操作那一行
                  let recordId = record.id;
                  let index = this.pdPackageTable.selectedRowKeys.indexOf(recordId);
                  if(index>=0){
                    this.pdPackageTable.selectedRowKeys.splice(index, 1);
                    this.pdPackageTable.selectionRows.splice(index, 1);
                  }else{
                    this.pdPackageTable.selectedRowKeys.push(recordId);
                    this.pdPackageTable.selectionRows.push(record);
                  }
                }
              }
            }
          }
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
        this.form.setFieldsValue(pick(row,'recordNo','outType','submitByName','submitDate','inDepartId','outDepartName','remarks','refuseReason','applyBy'))
      },
      // 部门下拉框搜索
      departHandleSearch(value){
        getAction(this.url.departList,{departName:value}).then((res)=>{
          if (!res.success) {
            this.cmsFailed(res.message);
          }
          this.departList = res.result;
        })
      },
      userHandleSearch(value){
        getAction(this.url.userList,{realname:value}).then((res)=>{
          if (!res.success) {
            this.cmsFailed(res.message);
          }
          this.userList = res.result;
        })
      },
      // 计算总数量和总价格
      getTotalNumAndPrice(rows){
        this.$nextTick(() => {
          if (rows.length <= 0) {
            let {values} = this.$refs.pdStockRecordDetail.getValuesSync({validate: false});
            rows = values;
          }
          let totalSum = 0;
          let outTotalPrice = 0;
          rows.forEach((item, idx) => {
            totalSum = totalSum + Number(item.productNum);
            outTotalPrice = outTotalPrice + Number(item.outTotalPrice);
          })

          //定数包
          if(this.pdPackageTable.dataSource.length > 0){
            for (let data of this.pdPackageTable.dataSource){
              for(let item of data.pdPackageRecordDetailList){
                // TODO
                totalSum = totalSum + Number(item.productNum);
                outTotalPrice = outTotalPrice + Number(item.outTotalPrice);
              }
            }
          }
          this.totalSum = totalSum;
          this.outTotalPrice = outTotalPrice.toFixed(4);
        })
      },
    },
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