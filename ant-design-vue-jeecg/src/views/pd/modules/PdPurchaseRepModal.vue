<template>
  <j-modal
    :visible="visible"
    :width="1200"
    :title="title"
    :lockScroll="lockScroll"
    :fullscreen="fullscreen"
    :switchFullscreen="switchFullscreen"
    @cancel="handleCancel"
  >
    <a-spin :spinning="confirmLoading">
      <!-- 主表单区域 -->
      <div style="background:#ECECEC; padding:20px">
        <a-card title="" style="margin-bottom: 10px;">
      <a-form :form="form">
        <a-row>
          <a-col :span="12">
            <a-form-item label="申购编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input     disabled="disabled"   v-decorator="[ 'orderNo', validatorRules.orderNo]"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="申购人员" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input disabled="disabled" v-decorator="[ 'purchaseName', validatorRules.purchaseName]"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="申购日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date  disabled="disabled" v-decorator="[ 'orderDate', validatorRules.orderDate]" :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="申购科室" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input disabled="disabled" v-decorator="[ 'deptName', validatorRules.deptName]" placeholder="请输入库房名称"></a-input>
            </a-form-item>
          </a-col>
           <a-col :span="12">
               <a-form-item label="申购类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                 <j-dict-select-tag-expand  :disabled="disableSubmit"   v-decorator="['purchaseType',validatorRules.purchaseType]"   dictCode="purchase_type" :trigger-change="true"  placeholder="请选择类型"/>
                </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="补货类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag-expand  :disabled="disableSubmit"   v-decorator="['repType',validatorRules.repType]"   dictCode="rep_type" :trigger-change="true"  placeholder="请选择类型"/>
            </a-form-item>
          </a-col>
        </a-row>
        <pd-choose-stock-list-model ref="pdChooseStockListModel" @ok="modalFormOk"></pd-choose-stock-list-model>
        <pd-choose-apply-list-model ref="pdChooseApplyListModel" @ok="modalFormOk"></pd-choose-apply-list-model>
      </a-form>
        </a-card>
        <a-card style="margin-bottom: 10px;">

          <a-tabs v-model="activeKey" @change="handleChangeTabs">
            <a-tab-pane tab="申购明细表" :key="refKeys[0]" :forceRender="true">
              <div style="margin-bottom: 8px;" >
                <a-button  v-show="!disableSubmit" type="primary" icon="plus" @click="choice">选择产品</a-button>
                <span style="padding-left: 8px;"></span>
                <a-popconfirm
                  :title="`确定要删除吗?`"
                  @confirm="handleConfirmDelete">
                  <a-button  v-show="!disableSubmit" type="primary" icon="minus">删除</a-button>
                  <span class="gap"></span>
                </a-popconfirm>
                <span style="padding-left: 8px;"></span>
                  <!--<a-button type="primary" icon="download" @click="exportXls('申购产品列表')">导出</a-button>-->
              </div>
              <j-editable-table
                bordered
                :ref="refKeys[0]"
                :loading="pdPurchaseDetailTable.loading"
                :columns="pdPurchaseDetailTable.columns"
                :dataSource="pdPurchaseDetailTable.dataSource"
                :maxHeight="500"
                :rowNumber="true"
                :rowSelection="true"
                :disabled="disableSubmit"
                :actionButton="false"
                @valueChange="valueChange"
                style="text-overflow: ellipsis;"
              />
              <a-row style="margin-top:10px;text-align: right;padding-right: 5%">
                <span style="font-weight: bold;font-size: large;padding-right: 5%">总数量：{{this.model.totalNum }}</span>
                <span style="font-weight: bold;font-size: large">总金额：{{ this.model.totalPrice }}</span>
              </a-row>
            </a-tab-pane>
          </a-tabs>
        </a-card>
        <a-card style="margin-bottom: 10px;" v-show="disableSubmit">
          <a-form :form="form">
            <a-row>
              <a-col :span="12">
                <a-form-item label="审核意见" :labelCol="labelCol" :wrapperCol="wrapperCol" style="text-align: left">
                  <a-textarea :disabled="true" v-decorator="[ 'refuseReason', validatorRules.refuseReason]" placeholder="请输入审核意见"></a-textarea>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-card>
      </div>
    </a-spin>
    <template slot="footer">
      <a-button @click="closeBtn" style="margin-right: 15px;" v-show="disableSubmit">关  闭</a-button>
      <a-popconfirm title="确定放弃编辑？" @confirm="handleCancel" v-show="!disableSubmit" okText="确定" cancelText="取消">
        <a-button style="margin-right: 15px;">取  消</a-button>
      </a-popconfirm>
      <a-button @click="handleOk('save')" v-show="!disableSubmit" type="primary" :loading="confirmLoading" style="margin-right: 15px;">保存草稿</a-button>
      <a-button @click="handleOk('submit')" v-show="!disableSubmit" type="primary" :loading="confirmLoading" style="margin-right: 15px;">提  交</a-button>
    </template>
  </j-modal>
  </template>
<script>

  import pick from 'lodash.pick'
  import { httpAction,getAction,downFile,inArray } from '@/api/manage'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import JDate from '@/components/jeecg/JDate'
  import { FormTypes,getRefPromise,validateFormAndTables } from '@/utils/JEditableTableUtil'
  import pdChooseStockListModel from './PdChooseStockListModel'
  import pdChooseApplyListModel from './PdChooseApplyListModel'
  import JDictSelectTagExpand from "@/components/dict/JDictSelectTagExpand"
  export default {
    name: 'PdPurchaseRepModal',
    mixins: [JEditableTableMixin],
    components: {
      JDate,
      pdChooseStockListModel,
      pdChooseApplyListModel,
      JDictSelectTagExpand},
    data() {
      return {
        model:{},
        title: '这里是标题',
        lockScroll: false,
        fullscreen: true,
        switchFullscreen: false,
        disableSubmit:false,
        confirmLoading: false,
        labelCol: {span: 6},
        wrapperCol: {span: 16},
        labelCol2: {span: 3},
        wrapperCol2: {span: 20},
        validatorRules: {
          orderNo:{},
          purchaseBy:{},
          purchaseName:{},
          orderDate:{},
          deptName:{},
          totalNum:{},
          totalPrice:{},
          refuseReason:{},
          purchaseType: {rules: [
              {required: true, message: '请选择申购类型'},
            ]},
          repType: {rules: [
              {required: true, message: '请选择补货类型'},
            ]},
        },
        refKeys: ['pdPurchaseDetail', ],
        tableKeys:['pdPurchaseDetail', ],
        activeKey: 'pdPurchaseDetail',
        // 申购单详细表
        pdPurchaseDetailTable: {
          loading: false,
          dataSource: [],
          columns: [
            { title: '产品ID', key: 'productId', type: FormTypes.hidden },
            { title: '产品编号',width:"200px",   key: 'number' },
            { title: '产品名称', width:"250px",  key: 'productName' },
            { title: '规格',width:"240px",   key: 'spec' },
            { title: '单位',width:"50px",  key: 'unitName' },
            { title: '中标号', width:"100px", key: 'bidingNumber' },
            { title: '库存数量', width:"100px",  key: 'stockNum' },
            {title: '申购数量', key: 'orderNum', type: FormTypes.input, width:"100px",
              placeholder: '${title}', defaultValue: '1',
              validateRules: [{ required: true, message: '${title}不能为空' },
                { pattern: '^(?:[1-9][0-9]*(?:\\.[0-9]+)?|0\\.(?!0+$)[0-9]+)$',message: '${title}的格式不正确' }]
            },
            { title: '产品单价',   key: 'purchasePrice' },
            { title: '申购金额',  key: 'orderMoney' },
            { title: '供应商', width:"250px",  key: 'supplierName' },
            { title: '供应商Id',   key: 'supplierId',type: FormTypes.hidden  },
            { title: '生产厂家', width:"250px",  key: 'venderName' },
          ]
        },
        url: {
          add: "/pd/pdPurchaseOrder/add",
          edit: "/pd/pdPurchaseOrder/edit",
          exportXlsUrl: "/pd/pdPurchaseOrder/exportXls",
          pdPurchaseDetail: {
            list: '/pd/pdPurchaseOrder/queryPdPurchaseDetail'
          },
        },
      }
    },
    methods: {
      add () {//初始化新增
        this.pdPurchaseDetailTable.dataSource = [];
        this.edit({});
        this.purchaseInfo();
      },
      /* 导出 */
       exportXls(fileName){
        if(!fileName || typeof fileName != "string"){
          fileName = "导出文件"
        }
        if(this.pdPurchaseDetailTable.dataSource.length>0){
        let param = {"orderNo":this.model.orderNo};
        console.log("导出参数",param)
        downFile(this.url.exportXlsUrl,param).then((data)=>{
          if (!data) {
            this.$message.warning("文件下载失败")
            return
          }
          if (typeof window.navigator.msSaveBlob !== 'undefined') {
            window.navigator.msSaveBlob(new Blob([data]), fileName+'.xls')
          }else{
            let url = window.URL.createObjectURL(new Blob([data]))
            let link = document.createElement('a')
            link.style.display = 'none'
            link.href = url
            link.setAttribute('download', fileName+'.xls')
            document.body.appendChild(link)
            link.click()
            document.body.removeChild(link); //下载完成移除元素
            window.URL.revokeObjectURL(url); //释放掉blob对象
          }
        })
      }else{
          this.$message.warning("产品为空")
          return
        }
      },
      purchaseInfo() { //新增页面初始化
        getAction("/pd/pdPurchaseOrder/purchaseInfo",{}).then((res)=>{
          if (res.success) {
              this.model = res.result;
            this.$nextTick(() => {
              this.form.setFieldsValue(pick(this.model,'orderNo','purchaseName','orderDate','purchaseType','deptName','refuseReason','repType'))
            })
          }
        })
      },

      // 表格数据变更
      valueChange(event) {
        if(event){
          const { type, row, column, value, target } = event;
          if(type === FormTypes.input){
            if(column.key === "orderNum"){
              // 申领数量变更
              let rows = target.getValuesSync({ validate: false });
              let orderMoney = (Number(row.orderNum) * Number(row.purchasePrice)).toFixed(2);
              target.setValues([{rowKey: row.id, values: {orderNum :row.orderNum,orderMoney :orderMoney }}]);
              // 计算总数量
              this.getTotalNumAndPrice([]);
            }
          }
        }
      },
      // 计算总数量及总金额
      getTotalNumAndPrice(rows){
        this.$nextTick(() => {
          if (rows.length <= 0) {
            let {values} = this.$refs.pdPurchaseDetail.getValuesSync({validate: false});
            rows = values;
          }
            let totalNum = 0;
            let totalPrice = 0;
             rows.forEach((item, idx) => {
              totalNum += parseFloat(item.orderNum);
              totalPrice += Number(item.orderMoney);
            })
            this.model.totalNum = totalNum;
            this.model.totalPrice = totalPrice.toFixed(2);//申购总金额
            //this.form.setFieldsValue(pick(this.model, 'totalNum', 'totalPrice'))

        });
      },

      //选择产品
      choice() {
        let repType = this.form.getFieldValue("repType");
        if(repType==null){
          this.$message.warning("请先选择补货类型")
          return
        }
        if(repType=="1"){ //根据库存上下限补货
          this.$refs.pdChooseStockListModel.show({});
          this.$refs.pdChooseStockListModel.title = "选择产品";
        }else{ //根据上月领用量补货
           this.$refs.pdChooseApplyListModel.show({});
           this.$refs.pdChooseApplyListModel.title = "选择产品";
        }
      },

      handleConfirmDelete() {
        if(this.$refs.pdPurchaseDetail.selectedRowIds.length > 0){
          this.$refs.pdPurchaseDetail.removeSelectedRows();
          this.$nextTick(() => {
            // 计算总数量和总金额
            this.getTotalNumAndPrice([]);
          })
        }else{
          this.$message.error("请选择需要删除的数据！")
        }
      },
      modalFormOk (formData) {//选择产品确定后返回所选择的数据
        let data = [];
        this.$refs.pdPurchaseDetail.getValues((error, values) => {
          formData.forEach((item, idx) => {
            let bool = true;
            values.forEach((value, idx) => {
                if(item.productId==value.productId){
                  bool=false;
                }
            })
            if(bool){
              data.push(item);
            }
          })
          data.forEach((item, idx) => {
            this.pdPurchaseDetailTable.dataSource = values;
            this.addrows(item);
          })
          this.$nextTick(() => {
            // 计算总数量及总金额
            this.getTotalNumAndPrice(values);
          })
        })
      },

      addrows(row) {
        let data = {
          productId: row.productId,
          number: row.number,
          productName: row.productName,
          spec:row.spec,
          purchasePrice: row.purchasePrice,
          bidingNumber:row.bidingNumber,
          orderNum: row.autoNum,
          version: row.version,
          stockNum: row.stockNum,
          unitName:row.unitName,
          orderMoney:row.purchasePrice * row.autoNum,
          venderName:row.venderName,
          supplierId:row.supplierId,
          supplierName:row.supplierName
        }
        this.pdPurchaseDetailTable.dataSource.push(data)
      },
      /** 关闭按钮 **/
      closeBtn(){
        this.visible = false;
        this.$emit('close');
        this.disableSubmit = false;
      },

      handleOk (submitType) { //提交
        this.model.submitStatus = '1';
        if (submitType == "submit") {
          this.model.submitStatus = '2';
          this.model.auditStatus = '1';
        }
        const that = this;
        // 触发表单验证
        // 触发表单验证
        this.getAllTable().then(tables => {
          /** 一次性验证主表和所有的次表 */
          return validateFormAndTables(this.form, tables)
        }).then(allValues => {
          if (typeof this.classifyIntoFormData !== 'function') {
            throw this.throwNotFunction('classifyIntoFormData')
          }
          let formData = this.classifyIntoFormData(allValues)
          // 发起请求
          let pdPurchaseDetailList = formData.pdPurchaseDetailList;
          if (pdPurchaseDetailList.length > 0) {
            let httpurl = '';
            let method = '';
            if (!this.model.id) {
              httpurl += this.url.add;
              method = 'post';
            } else {
              httpurl += this.url.edit;
              method = 'put';
            }
            that.confirmLoading = true;
            httpAction(httpurl, formData, method).then((res) => {
              if (res.success) {
                that.$message.success(res.message);
                that.$emit('ok');
              } else {
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.closeBtn();
            })
          } else {
            that.$message.error("请选择产品");
          }
        }).catch(e => {
          if (e.error === VALIDATE_NO_PASSED) {
            // 如果有未通过表单验证的子表，就自动跳转到它所在的tab
            this.activeKey = e.index == null ? this.activeKey : this.refKeys[e.index]
          } else {
            console.error(e)
          }
        })
      },
      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
        let fieldval = pick(this.model,'orderNo','purchaseName','orderDate','deptName','purchaseType','refuseReason','repType')
        this.$nextTick(() => {
          this.form.setFieldsValue(fieldval)
        })
        // 加载子表数据
        if (this.model.id) {
          let params = { orderNo: this.model.orderNo }
          this.requestSubTableData(this.url.pdPurchaseDetail.list, params, this.pdPurchaseDetailTable)
        }
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)

        return {
          ...main, // 展开
          pdPurchaseDetailList: allValues.tablesValue[0].values,
        }
      },
      validateError(msg){
        this.$message.error(msg)
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'orderNo','purchaseName','orderDate','deptName','purchaseType','auditStatus','submitStatus','refuseReason','repType'))
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
  .tableStyle> tr > th{
    border: 1px solid #e8e8e8;
    text-align: center;
    padding: 16px 16px;
    font-weight: 500;
    color: rgba(0, 0, 0, 0.85);
    background: #fafafa;
    transition: background 0.3s ease;
    display: table-cell;
    vertical-align: inherit;
    box-sizing: border-box;
  }
  .tableStyle> tr > td{
    border:1px solid #e8e8e8;
    text-align: center;
    padding: 1px 16px;
    font-weight: 500;
    box-sizing: border-box;
  }

  .tableStyle> tr > td >input{
    width:45px;
    text-align: center;
  }
</style>

