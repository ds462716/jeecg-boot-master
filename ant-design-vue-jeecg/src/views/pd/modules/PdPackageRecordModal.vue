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
        <a-card  title="套包信息" style="margin-bottom: 10px;">
          <a-form :form="form">
            <a-row>
              <a-col :span="5">
                <a-form-item label="打包编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input disabled v-decorator="[ 'recordNo', validatorRules.recordNo]" placeholder="请输入打包编号"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="5">
                <a-form-item label="打包人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input disabled v-decorator="[ 'createBy', validatorRules.createBy]" placeholder="请输入打包人"/>
                </a-form-item>
              </a-col>
              <a-col :span="5">
                <a-form-item label="打包日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <j-date disabled placeholder="请选择打包日期" v-decorator="[ 'createTime', validatorRules.createTime]" :trigger-change="true" style="width: 100%"/>
                </a-form-item>
                <a-form-item label="套包ID" :labelCol="labelCol" :wrapperCol="wrapperCol" v-show="false">
                  <a-input disabled v-decorator="[ 'packageId', validatorRules.packageId]" :trigger-change="true" style="width: 100%"/>
                </a-form-item>
              </a-col>
              <a-col :span="9">
                <a-form-item label="备注" :labelCol="labelCol2" :wrapperCol="wrapperCol2" style="text-align: left">
                  <a-input :disabled="disableSubmit" v-decorator="[ 'remarks', validatorRules.remarks]" placeholder="请输入备注"></a-input>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>

          <div class="table-operator">
              <a-button @click="choosePackageList" type="primary" icon="plus" style="margin-bottom: 8px;">选择套包</a-button>
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
                  rowKey="productId"
                  :pagination="false"
                  :columns="pdPackageTable.innerColumns"
                  :dataSource="pdPackageTable.innerData"
                  :loading="pdPackageTable.subloading"
                >
                </a-table>
              </a-table>
          </div>
        </a-card>

        <a-card style="margin-bottom: 10px;" title="选择库存">
          <a-form v-show="!disableSubmit">
            <a-row v-if="!showSBarcode">
              <a-col :md="6" :sm="8">
                <a-form-item label="产品编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input ref="productNumberInput" v-focus placeholder="请输入产品编号" v-model="queryParam.productNumber" @keyup.enter.native="onlyNumbersearchQuery"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="12" :sm="8">
                <a-form-item label="" :labelCol="labelCol" :wrapperCol="wrapperCol" style="text-align: left;padding-left: 15px;">
                  提示：按<span style="color: red">Ctrl+Alt</span>键快速定位到扫码输入框
                </a-form-item>
              </a-col>
            </a-row>
            <a-row v-if="showSBarcode">
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
              <a-col :md="12" :sm="8">
                <a-form-item label="" :labelCol="labelCol" :wrapperCol="wrapperCol" style="text-align: left;padding-left: 15px;">
                  提示：按<span style="color: red">Ctrl+Alt</span>键快速定位到产品扫码输入框
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>

          <div style="margin-bottom: -5px;">
            <a-form :form="form" >
              <a-row>
                <a-col :md="6" :sm="8">
                  <a-button type="primary" icon="plus" @click="chooseProductList">选择库存产品</a-button>
                  <a-popconfirm style="margin-left: 8px" :title="`确定要删除吗?`" @confirm="handleConfirmDelete">
                    <a-button type="primary" icon="minus">删除</a-button>
                    <span class="gap"></span>
                  </a-popconfirm>
                </a-col>
                <a-col :md="18" :sm="8" >
                  <a-form-item label="打包数量" :labelCol="labelCol3" :wrapperCol="wrapperCol3" style="padding-left: 60%">
                    <a-input placeholder="请输入打包数量" v-decorator="[ 'packageCount', validatorRules.packageCount]" @change="packageCountChange" ></a-input>
                  </a-form-item>
                </a-col>
              </a-row>
            </a-form>
          </div>

          <j-editable-table
            bordered
            :ref="refKeys[0]"
            :loading="pdPackageRecordDetailTable.loading"
            :columns="pdPackageRecordDetailTable.columns"
            :dataSource="pdPackageRecordDetailTable.dataSource"
            :maxHeight="950"
            :rowNumber="true"
            :rowSelection="true"
            :actionButton="false"
            :disabled="disableSubmit"
            @valueChange="valueChange"
            style="text-overflow: ellipsis;"
          >
          </j-editable-table>
          <a-row style="margin-top:10px;text-align: right;padding-right: 5%">
            <span style="font-weight: bold;font-size: large;padding-right: 5%">产品数量：{{ recordTotalSum }} / 包</span>
            <span style="font-weight: bold;font-size: large">金额：{{ recordTotalPrice }} / 包</span>
          </a-row>
        </a-card>

        <a-card style="margin-bottom: 10px;" title="打包记录">
          <div class="table-operator">
            <a-table
              ref="table"
              bordered
              rowKey="id"
              :columns="pdPackageRecordTable.columns"
              :dataSource="pdPackageRecordTable.dataSource"
              :pagination="pdPackageRecordTable.ipagination"
              :loading="pdPackageRecordTable.loading"
              :expandedRowKeys= "pdPackageRecordTable.expandedRowKeys"
              @change="handleTableChange"
              @expand="recordHandleExpand" >

              <span slot="action" slot-scope="text, record">
                <a-popconfirm title="确定拆包吗?" @confirm="() => handleDelete(record.id)" v-bind:disabled="record.status=='0'">
                  <a>拆包</a>
                </a-popconfirm>
              </span>

              <a-table
                slot="expandedRowRender"
                slot-scope="text"
                size="middle"
                bordered
                rowKey="id"
                :pagination="false"
                :columns="pdPackageRecordTable.innerColumns"
                :dataSource="pdPackageRecordTable.innerData"
                :loading="pdPackageRecordTable.subloading"
              >
              </a-table>
            </a-table>
          </div>
        </a-card>

        <a-card style="">
        </a-card>
      </div>
    </a-spin>

    <template slot="footer">
      <a-popconfirm title="确定放弃编辑？" @confirm="handleCancel" v-show="!disableSubmit" okText="确定" cancelText="取消">
        <a-button style="margin-right: 15px;">关闭</a-button>
      </a-popconfirm>
      <!--<a-button @click="" style="margin-right: 15px;" type="primary">打  印</a-button>-->
      <a-button @click="submitBtn" v-show="!disableSubmit" type="primary" :loading="confirmLoading" style="margin-right: 15px;">确定</a-button>
    </template>


    <pd-choose-package-list-model  ref="pdChoosePackageListModel" @ok="returnPackageData" ></pd-choose-package-list-model>
    <pd-choose-product-stock-list-model ref="pdChooseProductStockListModel" @ok="returnProductStockData" ></pd-choose-product-stock-list-model>

  </j-modal>
</template>

<script>

  import Vue from 'vue'
  import pick from 'lodash.pick'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import { FormTypes,getRefPromise,validateFormAndTables } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import JDate from '@/components/jeecg/JDate'
  import {httpAction, deleteAction, getAction} from '@/api/manage'
  import JDictSelectTagExpand from "@/components/dict/JDictSelectTagExpand"
  import ATextarea from "ant-design-vue/es/input/TextArea";
  import {stockScanCode, packageRecordScanCode} from '@/utils/barcode'
  import PdChoosePackageListModel from "./PdChoosePackageListModel"
  import PdChooseProductStockListModel from "./PdChooseProductStockListModel"

  const VALIDATE_NO_PASSED = Symbol()
  export { FormTypes, VALIDATE_NO_PASSED }

  export default {
    name: 'PdPackageRecordModal',
    mixins: [JEditableTableMixin],
    components: {
      PdChooseProductStockListModel,
      PdChoosePackageListModel,
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
        labelCol3: {span: 8},
        wrapperCol3: {span: 6},

        disableSubmit:false,
        showSBarcode:false, //开关-是否显示二级条码框 1-显示；0-不显示

        queryParam:{},

        packageTotalSum:0,
        packageTotalPrice:0,

        recordTotalSum:0,
        recordTotalPrice:0,

        refKeys: ['pdPackageRecordDetail',],
        tableKeys:['pdPackageRecordDetail', ],
        activeKey: 'pdPackageRecordDetail',
        // 套包模板表
        pdPackageTable:{
          show: false,
          dataSource:[],
          loading:false,
          subloading:false,
          innerData:[],
          expandedRowKeys:[],
          /* table选中keys*/
          selectedRowKeys: [],
          /* table选中records*/
          selectionRows: [],
          // 表头
          columns: [
            { title:'套包编号', align:"center", width: 350, dataIndex: 'packageCode' },
            { title:'套包名称', align:"center", width: 450, dataIndex: 'packageName' },
            { title:'产品总数', align:"center", width: 250, dataIndex: 'packageSum' },
            { title:'备注', align:"center", dataIndex: 'remarks' },
          ],
          innerColumns: [
            { title: '#', dataIndex: 'id', key:'rowIndex', width:60, align:"center",
              customRender:function (t,r,index) {
                return parseInt(index) + 1;
              }
            },
            { title:'产品编号', align:"center", width: 100, dataIndex: 'number' },
            { title:'产品名称', align:"center", dataIndex: 'productName' },
            { title:'规格', align:"center", dataIndex: 'spec' },
            { title:'型号', align:"center", dataIndex: 'version' },
            { title:'单位', align:"center", dataIndex: 'unitName' },
            { title:'产品数量', align:"center", dataIndex: 'count' },
            { title:'库存数量', align:"center", dataIndex: 'stockNum' },
            { title:'产品ID', align:"center", dataIndex: 'productId',
              colSpan: 0,
              customRender: (value, row, index) => {
                const obj = {
                  attrs: {colSpan:0},
                };
                return obj;
              },
            },
          ],
        },
        // 打包明细表
        pdPackageRecordDetailTable:{
          show: false,
          dataSource:[],
          selectedRowKeys: [],
          selectionRows: [],
          loading:false,
          subloading:false,
          // 表头
          columns: [
            { title: '产品名称', key: 'productName', type: FormTypes.normal,width:"220px" },
            { title: '产品编号', key: 'productNumber', width:"160px" },
            { title: '产品条码', key: 'productBarCode', type: FormTypes.input, disabled:true, width:"200px" },
            { title: '规格', key: 'spec', width:"150px" },
            { title: '批号', key: 'batchNo', width:"100px" },
            { title: '单位', key: 'unitName', width:"50px" },
            { title: '生产日期', key: 'produceDate',  },
            { title: '有效期', key: 'expDate', width:"100px" },
            { title: '入库单价', key: 'purchasePrice', width:"80px" },
            { title: '出库单价', key: 'sellingPrice', width:"80px" },
            { title: '产品数量', key: 'packageNum', type: FormTypes.input, width:"80px",
              placeholder: '${title}', defaultValue: '1',
              validateRules: [{ required: true, message: '${title}不能为空' },{ pattern: '^-?\\d+\\.?\\d*$',message: '${title}的格式不正确' }]
            },
            { title: '出库金额', key: 'outTotalPrice', type: FormTypes.input, disabled:true, width:"100px" },
            { title: '打包总数', key: 'sumPackageNum', type: FormTypes.input, width:"80px",disabled:true,
              placeholder: '${title}', defaultValue: '0',
            },
            { title: '库存数量', key: 'stockNum', width:"80px" },
            { title: '出库货位', key: 'outHuoweiName', width:"100px", type: FormTypes.hidden },
            // { title: '入库货位', key: 'inHuoweiCode', type: FormTypes.select, width:"150px", options: [],allowSearch:true, placeholder: '${title}' },

            { title: '库存明细ID', key: 'productStockId', type: FormTypes.hidden },
            { title: '产品ID', key: 'productId', type: FormTypes.hidden },
            { title: '出库货位编号', key: 'outHuoweiCode', type: FormTypes.hidden },
            { title: '供应商id', key: 'supplierId', type: FormTypes.hidden },
            { title: '规格单位ID', key: 'specUnitId', type: FormTypes.hidden },
            { title: '规格数量', key: 'specQuantity', type: FormTypes.hidden },
            { title: '注册证号', key: 'registration', type: FormTypes.hidden },
            { title: '生产厂家', key: 'venderName', type: FormTypes.hidden },
          ],
        },
        // 打包记录表
        pdPackageRecordTable:{
          show: false,
          dataSource:[],
          loading:false,
          subloading:false,
          innerData:[],
          expandedRowKeys:[],
          /* table选中keys*/
          selectedRowKeys: [],
          /* table选中records*/
          selectionRows: [],
          ipagination:{
            current: 1,
            pageSize: 10,
            pageSizeOptions: ['10', '20', '30', '50', '100'],
            showTotal: (total, range) => {
              return range[0] + "-" + range[1] + " 共" + total + "条"
            },
            showQuickJumper: true,
            showSizeChanger: true,
            total: 0
          },
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
            { title:'打包流水码', align:"center", width: 200, dataIndex: 'packageBarCode' },
            { title:'套包编号', align:"center", width: 200, dataIndex: 'packageCode' },
            { title:'套包名称', align:"center", width: 300, dataIndex: 'packageName' },
            { title:'产品总数', align:"center", width: 200, dataIndex: 'packageSum' },
            { title:'状态', align:"center", width: 200, dataIndex: 'status',
              customRender:(text)=>{
                if(!text){
                  return ''
                }else{
                  return filterMultiDictText(this.dictOptions['packageRecordStatus'], text+"")
                }
              }
            },
            { title:'打包时间', align:"center", width: 200, dataIndex: 'createTime' },
            { title:'备注', align:"center", dataIndex: 'remarks' },
            { title: '操作', dataIndex: 'action', align:"center",
              scopedSlots: { customRender: 'action' },
            }
          ],
          innerColumns: [
            { title: '#', dataIndex: 'id', key:'rowIndex', width:60, align:"center",
              customRender:function (t,r,index) {
                return parseInt(index) + 1;
              }
            },
            { title:'产品编号', align:"center", width: 100, dataIndex: 'productNumber' },
            { title:'产品名称', align:"center", dataIndex: 'productName' },
            { title:'规格', align:"center", dataIndex: 'spec' },
            { title:'批号', align:"center", dataIndex: 'batchNo' },
            // { title:'型号', align:"center", dataIndex: 'version' },
            { title:'单位', align:"center", dataIndex: 'unitName' },
            { title: '生产日期', align:"center", dataIndex: 'produceDate',
              customRender:function (text) {
                return !text?"":(text.length>10?text.substr(0,10):text)
              }
            },
            { title:'有效期', align:"center", dataIndex: 'expDate',
              customRender:function (text) {
                return !text?"":(text.length>10?text.substr(0,10):text)
              }
            },
            { title:'产品数量', align:"center", dataIndex: 'packageNum' },
            // { title:'库存数量', align:"center", dataIndex: 'stockNum' },
            { title:'产品ID', align:"center", dataIndex: 'productId',
              colSpan: 0,
              customRender: (value, row, index) => {
                const obj = {
                  attrs: {colSpan:0},
                };
                return obj;
              },
            },
          ],
        },
        validatorRules: {
          recordNo:{},
          packageBarCode:{},
          createBy:{},
          createTime:{},
          remarks:{},
          packageId: {},
          packageCount:{rules: [{required: true, message: '请输入打包数量!'},{ pattern: /^[1-9]\d*$/, message: '请输入零以上的正整数' }]},
        },
        url: {
          init:"/pd/pdPackageRecord/initModal",
          add: "/pd/pdPackageRecord/add",
          stockList: "/pd/pdProductStockTotal/chooseProductStockList",
          recordList: "/pd/pdPackageRecord/list",
          recordDetailList:"/pd/pdPackageRecord/queryPdPackageRecordDetailByMainId",
          recordDelete: "/pd/pdPackageRecord/delete",
        },
        dictOptions:{
          packageRecordStatus:[],
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
    directives: {
      focus: {
        // 当被绑定的元素插入到 DOM 中时……
        inserted: function (el) {
          //全局监听键盘事件
          document.onkeydown = function (event) {
            if (event.ctrlKey && event.altKey) {
              // 按ctrl+alt  聚焦元素
              el.focus();
              return;
            }
          }
        }
      },
    },
    methods: {
      initDictConfig(){
        initDictOptions('package_record_status').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'packageRecordStatus', res.result)
          }
        })
      },
      // 重写close方法
      close() {
        this.visible = false;
        this.pdPackageTable.show = false;
        this.pdPackageTable.dataSource = [];
        this.pdPackageRecordDetailTable.dataSource = [];
        this.packageTotalSum = 0;
        this.packageTotalPrice = 0;
        this.recordTotalSum = 0;
        this.recordTotalPrice = 0;
        this.eachAllTable((item) => {
          item.initialize()
        })
        this.queryParam = {};
        this.$emit('close');
      },
      /** 关闭按钮点击事件 */
      handleCancel() {
        this.$emit('ok');
        this.close();
      },
      getAllTable() {
        let values = this.tableKeys.map(key => getRefPromise(this, key))
        return Promise.all(values)
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
        // this.form.setFieldsValue(pick(row,'recordNo','outType','submitBy','submitByName','submitDate','applyNo','allocationNo',
        //   'inDepartId','outDepartId','outDepartName','remarks','refuseReason'))
      },
      add() {
        this.edit({});
      },
      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter (record) {
        this.loadData();
        this.initDictConfig();//初始化数据字典
      },
      loadData() {
        this.loading = true;
        let params = {};
        if(this.model.id){
          params = { id: this.model.id }
        }else{
          params = { id: "" }
          // 初始化打包数量
          this.$nextTick(() => {
            this.form.setFieldsValue({packageCount:"1"});
          })
        }
        getAction(this.url.init, params).then((res) => {
          if (res.success) {
            this.$nextTick(() => {
              // this.departList = res.result.sysDepartList; // 初始化部门列表 用于数据回显
              if(this.model.id){ //详情页

              }else{  // 新增页
                this.initData = res.result;
                let fieldval = pick(this.initData,'recordNo','createBy','createTime','remarks');
                // let fieldval = pick(this.initData,'createBy','createTime','remarks');
                this.$nextTick(() => {
                  this.form.setFieldsValue(fieldval);
                  //开关-是否显示二级条码框  1-显示；0-不显示
                  if(res.result.showSBarcode && res.result.showSBarcode == "0"){
                    this.showSBarcode = false;
                  }else{
                    this.showSBarcode = true;
                  }
                })
              }
            })
          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
          this.loading = false;
        })

      },
      /** 确定按钮点击事件 */
      submitBtn(flag) {
        this.request(this.url.add,"post",flag);
      },
      request(url, method,flag) {
        /** 触发表单验证 */
        this.getAllTable().then(tables => {
          /** 一次性验证主表和所有的次表 */
          return validateFormAndTables(this.form, tables)
        }).then(allValues => {
          if (typeof this.classifyIntoFormData !== 'function') {
            throw this.throwNotFunction('classifyIntoFormData')
          }

          let formData = this.classifyIntoFormData(allValues);

          if(!formData.packageId){
            this.$message.warning("套包读取失败，请重新选择套包！");
            return;
          }

          if(formData.pdPackageRecordDetailList.length <= 0){
            this.$message.warning("打包产品数据为空，请扫码打包或选择库存产品！");
            return;
          }
          let packageCount = formData.packageCount;
          let recordList = formData.pdPackageRecordDetailList;
          let packageList = this.pdPackageTable.dataSource[0].pdPackageDetailList;

          for(let pa of packageList){
            let isInPackage = false; // 打包的产品是否在套包中
            let count = Number(pa.count);//套包产品数量
            let sumPackageNum = 0;//打包产品总数
            for(let record of recordList){
              if(pa.productId == record.productId){
                isInPackage = true;
                sumPackageNum = sumPackageNum + Number(record.packageNum);
              }
            }
            if(!isInPackage){
              this.$message.error("产品["+pa.productName+"]没有被打包，请继续打包！");
              return;
            }
            if(sumPackageNum != count){
              this.$message.error("产品["+pa.productName+"]打包产品数量与套包产品数量不一致，请重新打包！");
              return;
            }
          }

          for (let item of recordList){
            if(Number(item.packageNum)*Number(packageCount) > Number(item.stockNum)){
              this.$message.error("["+item.productName+"]打包产品数量不能大于库存数量！");
              return;
            }
            if(Number(item.packageNum) <= 0){
              this.$message.error("["+item.productName+"]打包产品数量必须大于0！");
              return;
            }
          }

          // 发起请求
          this.confirmLoading = true
          httpAction(url, formData, method).then((res) => {
            if (res.success) {
              this.$message.success(res.message)
              // 1.获取打包记录
              this.pdPackageRecordTable.ipagination.current = 1;
              this.getRecordList(formData.recordNo);
              // 2.刷新库存选择表
              this.getStockList();
              // 3.清空打包数量
              this.form.setFieldsValue({packageCount:"1"});
            } else {
              this.$message.warning(res.message)
            }
          }).finally(() => {
            this.confirmLoading = false
          })
        }).catch(e => {
          if (e.error === VALIDATE_NO_PASSED) {
            // 如果有未通过表单验证的子表，就自动跳转到它所在的tab
            this.activeKey = e.index == null ? this.activeKey : this.refKeys[e.index]
          } else {
            console.error(e)
          }
        })
      },
      getRecordList(recordNo){
        if(recordNo){
          var param = {};
          param.pageNo = this.pdPackageRecordTable.ipagination.current;
          param.pageSize = this.pdPackageRecordTable.ipagination.pageSize;
          param.recordNo = recordNo;
          getAction(this.url.recordList, param).then((res) => {
            if (res.success) {
              this.$nextTick(() => {
                this.pdPackageRecordTable.dataSource = res.result.records;
                this.pdPackageRecordTable.ipagination.total = res.result.total;
              })
            }
            if(res.code===510){
              this.$message.warning(res.message)
            }
          })
        }
      },
      handleTableChange(pagination, filters, sorter) {
        let recordNo = this.form.getFieldValue("recordNo");
        this.pdPackageRecordTable.ipagination = pagination;
        this.getRecordList(recordNo);
      },
      //拆包
      handleDelete(id){
        deleteAction(this.url.recordDelete, {id: id}).then((res) => {
          if (res.success) {
            this.$message.success(res.message);
            let recordNo = this.form.getFieldValue("recordNo");
            this.pdPackageRecordTable.ipagination.current = 1;
            // 1.获取打包记录
            this.getRecordList(recordNo);
            // 2.刷新库存选择表
            this.getStockList();
          } else {
            this.$message.warning(res.message);
          }
        });
      },
      //打包记录展开按钮
      recordHandleExpand(expanded, record){
        this.pdPackageRecordTable.expandedRowKeys=[];
        this.pdPackageRecordTable.innerData=[];
        if(expanded===true){
          this.pdPackageRecordTable.subloading = true;
          this.pdPackageRecordTable.expandedRowKeys.push(record.id);
          getAction(this.url.recordDetailList, {id: record.id}).then((res) => {
            if (res.success) {
              this.pdPackageRecordTable.subloading = false;
              this.pdPackageRecordTable.innerData = res.result;
            }
          });
        }
      },
      //删除行
      handleConfirmDelete() {
        if(this.$refs.pdPackageRecordDetail.selectedRowIds.length > 0){
          this.$refs.pdPackageRecordDetail.removeSelectedRows();
          this.getTotalNumAndPrice([]);
        }else{
          this.$message.error("请选择需要删除的数据！")
        }
      },
      // 点“选择产品”按钮后 调用 新增一行
      addrows(row){
        let packageCount = this.form.getFieldValue("packageCount");
        let data = {
          productStockId:row.id,
          productId: row.productId,
          productName: row.productName,
          productNumber:row.number,
          productBarCode:row.productBarCode,
          spec: row.spec,
          batchNo:row.batchNo,
          unitName:row.unitName,
          expDate:row.expDate,
          sellingPrice:row.sellingPrice,
          specUnitId: row.specUnitId,
          specQuantity: row.specQuantity,
          packageNum: 1,
          sumPackageNum:1 * Number(packageCount),
          purchasePrice:row.purchasePrice,
          outTotalPrice:Number(!row.sellingPrice ? 0 : row.sellingPrice).toFixed(4),
          stockNum:row.stockNum,
          outHuoweiName:row.huoweiName,
          outHuoweiCode:row.huoweiCode,
          registration:row.registration,
          venderName:row.venderName,
          // inHuoweiCode:"",
          supplierId:row.supplierId,
          produceDate:row.produceDate
        }
        this.pdPackageRecordDetailTable.dataSource.push(data);
        this.$refs.pdPackageRecordDetail.add();
      },
      // 计算总数量和总价格
      getTotalNumAndPrice(rows){
        this.$nextTick(() => {
          //产品
          if (rows.length <= 0) {
            let {values} = this.$refs.pdPackageRecordDetail.getValuesSync({validate: false});
            rows = values;
          }
          let totalSum = 0;
          let outTotalPrice = 0;
          rows.forEach((item, idx) => {
            totalSum = totalSum + Number(item.packageNum);
            outTotalPrice = outTotalPrice + Number(item.outTotalPrice);
          })

          this.recordTotalSum = totalSum;
          this.recordTotalPrice = outTotalPrice.toFixed(4);
        })
      },
      // 选择产品 新增行
      chooseProductList() {
        let packageId = this.form.getFieldValue("packageId");
        if(!packageId){
          this.$message.warning("请选择套包！");
          return;
        }
        let packageData = this.pdPackageTable.dataSource[0].pdPackageDetailList;
        let productIdList = [];
        packageData.forEach((item, idx) => {
          productIdList.push(item.productId)
        })
        this.$refs.pdChooseProductStockListModel.show({productIdList:productIdList,nestatStatus:"1",barCodeType:"0"});
      },
      // 选择产品后返回
      returnProductStockData(data) {
        let rows = [];
        let { values } = this.$refs.pdPackageRecordDetail.getValuesSync({ validate: false });
        if(values.length > 0){
          // 如果列表中有相同产品则不加行
          data.forEach((item, idx) => {
            let bool = true;
            values.forEach((value, idx) => {
              if(item.id == value.productStockId){
                bool = false;
              }
            })
            if(bool){
              rows.push(item)
            }
          })
        }else{
          rows = data;
        }
        rows.forEach((item, idx) => {
          // j-editable-table表格（可能是BUG）：values变更 不会同步变更到dataSource，新增行时需要手动赋值到dataSource
          this.pdPackageRecordDetailTable.dataSource = values;
          this.addrows(item);
        })
        // 计算总数量和总价格
        this.getTotalNumAndPrice(values);
      },
      // 选择套包
      choosePackageList() {
        this.$refs.pdChoosePackageListModel.show();
      },
      // 选择套包后返回
      returnPackageData(data){
        this.form.setFieldsValue({"packageId":data[0].id});
        this.pdPackageTable.dataSource = data;
        this.pdPackageTable.show = true;
        // 自动带出库存
        this.getStockList();
      },
      // 查询库存列表
      getStockList(){
        this.pdPackageRecordDetailTable.dataSource = [];
        let packageData = this.pdPackageTable.dataSource[0].pdPackageDetailList;
        let productIdList = [];
        packageData.forEach((item, idx) => {
          productIdList.push(item.productId)
        })
        let params = {};
        params.productIds = productIdList.join(",");//产品id
        params.nestatStatus = "1"; //未使用
        params.barCodeType = "0"; // 批次码（非唯一码）
        this.pdPackageRecordDetailTable.loading = true;
        getAction(this.url.stockList, params).then((res) => {
          if (res.success && res.result.length > 0) {
            this.returnProductStockData(res.result);
          }
          this.pdPackageRecordDetailTable.loading = false;
        })
      },
      //套包展开按钮
      handleExpand(expanded, record){
        this.pdPackageTable.expandedRowKeys=[];
        this.pdPackageTable.innerData=[];
        if(expanded===true){
          this.pdPackageTable.expandedRowKeys.push(record.id);
          this.pdPackageTable.innerData = record.pdPackageDetailList;
        }
      },
      // 表格数据变更
      valueChange(event) {
        if (event) {
          const {type, row, column, value, target} = event;
          if (type === FormTypes.input) {
            if (column.key === "packageNum") {
              let {values} = target.getValuesSync({validate: false});
              let packageCount = this.form.getFieldValue("packageCount");
              let sumPackageNum = Number(value) * Number(packageCount);
              for (let item of values) {
                if (item.id == row.id && Number(sumPackageNum) > Number(item.stockNum)) {
                // if (item.id == row.id && Number(value) > Number(item.stockNum)) {
                  this.$message.error("[" + row.productName + "]打包数量不能大于库存数量！");
                  // 产品数量变更 计算每条产品的价格
                  // let outTotalPrice = (Number(row.sellingPrice) * Number(item.stockNum)).toFixed(4);
                  // target.setValues([{
                  //   rowKey: row.id,
                  //   values: {outTotalPrice: outTotalPrice, packageNum: item.stockNum}
                  // }])
                  // // 计算总数量和总价格
                  // this.getTotalNumAndPrice([]);
                  // return;
                }
              }
              // 产品数量变更 计算每条产品的价格
              let outTotalPrice = (Number(row.sellingPrice) * Number(value)).toFixed(4);
              target.setValues([{rowKey: row.id, values: {outTotalPrice: outTotalPrice,sumPackageNum: sumPackageNum}}]);
              // 计算总数量和总价格
              this.getTotalNumAndPrice([]);
            }
          }
        }
      },
      // 计算每行打包总数
      packageCountChange(e){
        let packageCount = e.target.value;
        let { values } = this.$refs.pdPackageRecordDetail.getValuesSync({ validate: false });

        for (let item of values) {
          let sumPackageNum = Number(item.packageNum) * Number(packageCount);
          if (Number(sumPackageNum) > Number(item.stockNum)) {
            this.$message.error("[" + item.productName + "]打包数量不能大于库存数量！");
          }
          this.$refs.pdPackageRecordDetail.setValues([{rowKey: item.id, values: {sumPackageNum: sumPackageNum}}]);
        }
      },
      //清空扫码框
      clearQueryParam(){
        this.queryParam = {};
        this.$refs.productNumberInput.focus();
      },
      // 只扫产品编号查询
      onlyNumbersearchQuery(){
        let productNumber = this.queryParam.productNumber;
        if(!productNumber){
          this.$message.error("请输入产品编号！");
          this.$refs.productNumberInput.focus();
          return;
        }
        this.queryParam.productBarCode = productNumber;
        this.searchQuery(1);
      },
      // 扫码查询
      searchQuery(num) {
        let productNumber = this.queryParam.productNumber;
        if(!productNumber){
          //清空扫码框
          this.clearQueryParam();
          this.$message.error("请输入产品编号！");
          return;
        }

        if(num == 0){       //产品编号扫码
          // 焦点条码输入框
          this.$refs.productBarCodeInput.focus();

        }else if(num == 1){ //条码扫码
          let productBarCode = this.queryParam.productBarCode;
          if(!productBarCode){
            this.$message.error("请输入二级条码！");
            return;
          }
          //解析条码
          stockScanCode(productNumber,productBarCode,"0","1","0").then((res) => {
            if(res.code == "200" || res.code == "203"){
              let pdProductStockList = res.result;
              if(!pdProductStockList){
                //清空扫码框
                this.clearQueryParam();
                this.$message.error("条码解析失败，请校验条码是否正确！");
                return;
              }
              if(pdProductStockList.length <= 0){
                //清空扫码框
                this.clearQueryParam();
                this.$message.error("库存中没有该产品！");
                return;
              }

              let { values } = this.$refs.pdPackageRecordDetail.getValuesSync({ validate: false });
              let packageCount = this.form.getFieldValue("packageCount");
              for(let pdProductStock of pdProductStockList){
                let isAddRow = true;// 是否增加一行
                // 循环表格数据
                if(values.length > 0) { //表格有数据
                  for(let item of values){
                    if(pdProductStock.id == item.productStockId){// 库存明细ID一致，就+1
                      isAddRow = false;
                      let packageNum = Number(item.packageNum) + 1;
                      let sumPackageNum = packageNum * Number(packageCount);
                      // if(Number(item.packageNum) + 1 > Number(item.stockNum)){
                      if(Number(sumPackageNum) > Number(item.stockNum)){
                        //清空扫码框
                        this.$message.error("["+item.productName+"]打包数量不能大于库存数量！");
                        // this.clearQueryParam();
                        // return;
                      }

                      let outTotalPrice = (Number(item.sellingPrice) * Number(packageNum)).toFixed(4);

                      this.$refs.pdPackageRecordDetail.setValues([{rowKey: item.id, values: {
                          packageNum: packageNum,outTotalPrice: outTotalPrice,sumPackageNum: sumPackageNum }}]);
                      // 计算总数量和总价格
                      this.getTotalNumAndPrice([]);
                      break;
                    }
                  }
                }

                if(isAddRow){
                  this.pdPackageRecordDetailTable.dataSource = values;
                  //条码新增一行
                  this.addrows(pdProductStock);
                  // 计算总数量和总价格
                  this.getTotalNumAndPrice(values);
                }
              }
              if(res.code == "203"){ // 近效期提醒
                this.$message.error(result.message);
              }
            }else if(res.code ==="201"){
              this.$message.error(res.message);
            }else{
              this.$message.error(res.message);
            }
            //清空扫码框
            this.clearQueryParam();
          })
        }
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