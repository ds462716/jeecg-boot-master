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
                <a-form-item label="打包流水码" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input disabled v-decorator="[ 'packageBarCode', validatorRules.packageBarCode]" placeholder="请输入出库单号"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="打包人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input disabled v-decorator="[ 'createBy', validatorRules.createBy]" placeholder="请输入打包人"/>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="打包日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <j-date disabled placeholder="请选择打包日期" v-decorator="[ 'createTime', validatorRules.createTime]" :trigger-change="true" style="width: 100%"/>
                </a-form-item>
                <a-form-item label="定数包ID" :labelCol="labelCol" :wrapperCol="wrapperCol" v-show="false">
                  <a-input disabled v-decorator="[ 'packageId', validatorRules.packageId]" :trigger-change="true" style="width: 100%"/>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>

          <div class="table-operator">
            <a-tabs>
              <a-tab-pane tab="定数包明细" :forceRender="true">
                <a-button @click="choosePackageList" type="primary" icon="plus" style="margin-bottom: 8px;">选择定数包</a-button>
                <a-table
                  ref="table"
                  bordered
                  rowKey="productId"
                  :pagination="false"
                  :columns="pdPackageTable.columns"
                  :dataSource="pdPackageTable.dataSource"
                  :loading="pdPackageTable.loading"
                >
                </a-table>
                <a-row style="margin-top:10px;text-align: right;padding-right: 5%">
                  <span style="font-weight: bold;font-size: large;padding-right: 5%">定数包总数量：{{ packageTotalSum }}</span>
                  <!--<span style="font-weight: bold;font-size: large">总金额：{{ packageTotalPrice }}</span>-->
                </a-row>
              </a-tab-pane>
            </a-tabs>
          </div>
        </a-card>

        <a-card style="margin-bottom: 10px;">
          <a-tabs v-model="activeKey">
            <a-tab-pane tab="库存明细" :key="refKeys[0]" :forceRender="true">
              <a-form v-show="!disableSubmit">
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
                  <a-col :md="12" :sm="8">
                    <a-form-item label="" :labelCol="labelCol" :wrapperCol="wrapperCol" style="text-align: left;padding-left: 15px;">
                      提示：按<span style="color: red">Ctrl+Alt</span>键快速定位到产品扫码输入框
                    </a-form-item>
                  </a-col>
                </a-row>
              </a-form>

              <div style="margin-bottom: 8px;" v-show="!disableSubmit" >
                <a-button type="primary" icon="plus" @click="chooseProductList">选择库存产品</a-button>
                <a-popconfirm style="margin-left: 8px" :title="`确定要删除吗?`" @confirm="handleConfirmDelete">
                  <a-button type="primary" icon="minus">删除</a-button>
                  <span class="gap"></span>
                </a-popconfirm>
              </div>
              <j-editable-table
                bordered
                :ref="refKeys[0]"
                :loading="pdPackageRecordDetailTable.loading"
                :columns="pdPackageRecordDetailTable.columns"
                :dataSource="pdPackageRecordDetailTable.dataSource"
                :maxHeight="650"
                :rowNumber="true"
                :rowSelection="true"
                :actionButton="false"
                :disabled="disableSubmit"
                @valueChange="valueChange"
                style="text-overflow: ellipsis;"
              >
              </j-editable-table>
              <a-row style="margin-top:10px;text-align: right;padding-right: 5%">
                <span style="font-weight: bold;font-size: large;padding-right: 5%">打包总数量：{{ recordTotalSum }}</span>
                <span style="font-weight: bold;font-size: large">总金额：{{ recordTotalPrice }}</span>
              </a-row>


            </a-tab-pane>
          </a-tabs>
        </a-card>

        <a-card style="">
          <a-form :form="form">
            <a-row>
              <a-col :span="12">
                <a-form-item label="备注" :labelCol="labelCol2" :wrapperCol="wrapperCol2" style="text-align: left">
                  <a-textarea :disabled="disableSubmit" v-decorator="[ 'remarks', validatorRules.remarks]" placeholder="请输入备注"></a-textarea>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-card>
      </div>
    </a-spin>

    <template slot="footer">
      <a-popconfirm title="确定放弃编辑？" @confirm="handleCancel" v-show="!disableSubmit" okText="确定" cancelText="取消">
        <a-button style="margin-right: 15px;">取  消</a-button>
      </a-popconfirm>
      <!--<a-button @click="" style="margin-right: 15px;" type="primary">打  印</a-button>-->
      <a-button @click="submitBtn" v-show="!disableSubmit" type="primary" :loading="confirmLoading" style="margin-right: 15px;">提交</a-button>
    </template>


    <pd-choose-package-list-model  ref="pdChoosePackageListModel" @ok="returnPackageData" ></pd-choose-package-list-model>
    <pd-choose-product-stock-list-model ref="pdChooseProductStockListModel" @ok="returnProductStockData" ></pd-choose-product-stock-list-model>

  </j-modal>
</template>

<script>

  import Vue from 'vue'
  import pick from 'lodash.pick'
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

        disableSubmit:false,

        queryParam:{},

        packageTotalSum:0,
        packageTotalPrice:0,

        recordTotalSum:0,
        recordTotalPrice:0,

        refKeys: ['pdPackageRecordDetail',],
        tableKeys:['pdPackageRecordDetail', ],
        activeKey: 'pdPackageRecordDetail',
        pdPackageTable:{
          show: false,
          dataSource:[],
          loading:false,
          // 表头
          columns: [
            {
              title: '#',
              dataIndex: 'id',
              key:'rowIndex',
              width:60,
              align:"center",
              customRender:function (t,r,index) {
                return parseInt(index) + 1;
              }
            },
            {
              title:'定数包编号',
              align:"center",
              dataIndex: 'packageCode'
            },
            {
              title:'定数包名称',
              align:"center",
              dataIndex: 'packageName',
              customRender:function (text) {
                return !text?"":(text.length>10?text.substr(0,10):text)
              }
            },
            {
              title:'产品编号',
              align:"center",
              width: 100,
              dataIndex: 'number'
            },
            {
              title:'产品名称',
              align:"center",
              dataIndex: 'productName'
            },
            {
              title:'规格',
              align:"center",
              dataIndex: 'spec'
            },
            {
              title:'型号',
              align:"center",
              dataIndex: 'version'
            },
            {
              title:'单位',
              align:"center",
              dataIndex: 'unitName'
            },
            {
              title:'定数包产品数量',
              align:"center",
              width: 200,
              dataIndex: 'count'
            },
            {
              title:'库存数量',
              align:"center",
              dataIndex: 'stockNum'
            },
            {
              title:'产品ID',
              align:"center",
              dataIndex: 'productId',
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
            {
              title: '打包数量', key: 'packageNum', type: FormTypes.input, width:"80px",
              placeholder: '${title}', defaultValue: '1',
              validateRules: [{ required: true, message: '${title}不能为空' },{ pattern: '^-?\\d+\\.?\\d*$',message: '${title}的格式不正确' }]
            },
            { title: '出库金额', key: 'outTotalPrice', type: FormTypes.input, disabled:true, width:"100px" },
            { title: '库存数量', key: 'stockNum', width:"80px" },
            { title: '出库货位', key: 'outHuoweiName', width:"100px" },
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
        validatorRules: {
          packageBarCode:{},
          createBy:{},
          createTime:{},
          remarks:{},
          packageId: {},
        },
        url: {
          init:"/pd/pdPackageRecord/initModal",
          add: "/pd/pdPackageRecord/add",
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
      },
      loadData() {
        this.loading = true;
        let params = {};
        if(this.model.id){

          params = { id: this.model.id }
        }else{

          params = { id: "" }
        }
        getAction(this.url.init, params).then((res) => {
          if (res.success) {
            this.$nextTick(() => {
              // this.departList = res.result.sysDepartList; // 初始化部门列表 用于数据回显
              if(this.model.id){ //详情页

              }else{  // 新增页
                this.initData = res.result;
                let fieldval = pick(this.initData,'packageBarCode','createBy','createTime','remarks');
                this.$nextTick(() => {
                  this.form.setFieldsValue(fieldval);
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
      /** 保存草稿 **/
      // saveBtn() {
      //   this.request(this.url.add,"post","");
      // },
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
            this.$message.warning("定数包读取失败，请重新选择定数包！");
            return;
          }

          if(formData.pdPackageRecordDetailList.length <= 0){
            this.$message.warning("打包产品数据为空，请扫码打包或选择库存产品！");
            return;
          }
          
          let recordList = formData.pdPackageRecordDetailList;
          let packageList = this.pdPackageTable.dataSource;

          for(let pa of packageList){
            let isInPackage = false; // 打包的产品是否在定数包中
            let count = Number(pa.count);//定数包产品数量
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
              this.$message.error("产品["+pa.productName+"]打包数量与定数包产品数量不一致，请重新打包！");
              return;
            }
          }

          for (let item of recordList){
            if(Number(item.packageNum) > Number(item.stockNum)){
              this.$message.error("["+item.productName+"]打包数量不能大于库存数量！");
              return;
            }
            if(Number(item.packageNum) <= 0){
              this.$message.error("["+item.productName+"]打包数量必须大于0！");
              return;
            }
          }

          // 发起请求
          this.confirmLoading = true
          httpAction(url, formData, method).then((res) => {
            if (res.success) {
              this.$message.success(res.message)
              this.$emit('ok');
              this.close();
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
        let packageData = this.pdPackageTable.dataSource;

        let packageId = this.form.getFieldValue("packageId");
        if(!packageId){
          this.$message.warning("请选择定数包！");
          return;
        }

        // if(packageData.length <= 0){
        //   this.$message.error("请选择定数包！");
        //   return;
        // }

        // let packageDetailList = packageData[0].pdPackageDetailList;
        let productIdList = [];
        packageData.forEach((item, idx) => {
          productIdList.push(item.productId)
        })
        this.$refs.pdChooseProductStockListModel.show({productIdList:productIdList,nestatStatus:"1"});
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
      // 选择定数包
      choosePackageList() {
        this.$refs.pdChoosePackageListModel.show();
      },
      // 选择定数包后返回
      returnPackageData(data){
        // this.pdPackageTable.dataSource = data;
        this.pdPackageRecordDetailTable.dataSource = [];
        this.recordTotalSum = 0;
        this.recordTotalPrice = 0;

        this.form.setFieldsValue({"packageId":data[0].id});

        let pdPackageDetailList = data[0].pdPackageDetailList;

        pdPackageDetailList.forEach((item, idx) => {
          item.packageCode = data[0].packageCode;
          item.packageName = data[0].packageName;
        })

        this.pdPackageTable.dataSource = pdPackageDetailList;
        this.pdPackageTable.show = true;
        this.packageTotalSum = data[0].packageSum;
      },
      // 表格数据变更
      valueChange(event) {
        if (event) {
          const {type, row, column, value, target} = event;
          if (type === FormTypes.input) {
            if (column.key === "packageNum") {
              let {values} = target.getValuesSync({validate: false});
              for (let item of values) {
                if (item.id == row.id && Number(value) > Number(item.stockNum)) {
                  this.$message.error("[" + row.productName + "]出库数量不能大于库存数量！");
                  // 产品数量变更 计算每条产品的价格
                  let outTotalPrice = (Number(row.sellingPrice) * Number(item.stockNum)).toFixed(4);
                  target.setValues([{
                    rowKey: row.id,
                    values: {outTotalPrice: outTotalPrice, packageNum: item.stockNum}
                  }])
                  // 计算总数量和总价格
                  this.getTotalNumAndPrice([]);
                  return;
                }
              }
              // 产品数量变更 计算每条产品的价格
              let outTotalPrice = (Number(row.sellingPrice) * Number(value)).toFixed(4);
              target.setValues([{rowKey: row.id, values: {outTotalPrice: outTotalPrice}}])
              // 计算总数量和总价格
              this.getTotalNumAndPrice([]);
            }
          }
        }
      },
      //清空扫码框
      clearQueryParam(){
        this.queryParam = {};
        this.$refs.productNumberInput.focus();
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
          stockScanCode(productNumber,productBarCode,"0","1").then((res) => {
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
              for(let pdProductStock of pdProductStockList){
                let isAddRow = true;// 是否增加一行
                // 循环表格数据
                if(values.length > 0) { //表格有数据
                  for(let item of values){
                    if(pdProductStock.id == item.productStockId){// 库存明细ID一致，就+1
                      isAddRow = false;
                      if(Number(item.packageNum) + 1 > Number(item.stockNum)){
                        //清空扫码框
                        this.clearQueryParam();
                        this.$message.error("["+item.productName+"]出库数量不能大于库存数量！");
                        return;
                      }

                      let packageNum = Number(item.packageNum) + 1;
                      let outTotalPrice = (Number(item.sellingPrice) * Number(packageNum)).toFixed(4);

                      this.$refs.pdPackageRecordDetail.setValues([{rowKey: item.id, values: {
                          packageNum: packageNum,outTotalPrice: outTotalPrice }}]);
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