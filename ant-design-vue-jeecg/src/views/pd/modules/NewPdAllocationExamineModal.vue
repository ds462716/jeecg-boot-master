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
      <div style="background:#ECECEC; padding:20px">
        <a-card title="" style="margin-bottom: 10px;">
      <!-- 主表单区域 -->
      <a-form :form="form">
        <a-row>
          <a-col :span="12">
            <a-form-item label="调拨单编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input disabled="disabled" v-decorator="[ 'allocationNo', validatorRules.allocationNo]"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="调拨日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date disabled="disabled"  v-decorator="[ 'allocationDate', validatorRules.allocationDate]" :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="操作人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input disabled="disabled" v-decorator="[ 'realName', validatorRules.realName]"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="出库科室" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input disabled="disabled" v-decorator="[ 'outDeptName', validatorRules.outDeptName]"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="入库科室" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input disabled="disabled" v-decorator="[ 'inDeptName', validatorRules.inDeptName]"></a-input>
            </a-form-item>
          </a-col>
         <!-- <a-col :span="12">
            <a-form-item label="调拨总数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number disabled="disabled" v-decorator="[ 'totalNum', validatorRules.totalNum]" style="width: 100%"/>
            </a-form-item>
          </a-col>-->
          <a-col :span="12">
            <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input disabled="disabled" v-decorator="[ 'remarks', validatorRules.remarks]"  style="width: 100%;height: 60px"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
        </a-card>
        <a-card style="margin-bottom: 10px;">
          <a-tabs v-model="activeKey" @change="handleChangeTabs">
            <a-tab-pane tab="调拨明细表" :key="refKeys[0]" :forceRender="true">
              <div style="margin-bottom: 8px;">
              </div>
              <j-editable-table
                bordered
                :ref="refKeys[0]"
                :loading="pdAllocationDetailTable.loading"
                :columns="pdAllocationDetailTable.columns"
                :dataSource="pdAllocationDetailTable.dataSource"
                :maxHeight="500"
                :rowNumber="true"
                :rowSelection="false"
                :actionButton="false"
                style="text-overflow: ellipsis;"
              />


              <span style="padding-left: 8px;"></span>
              <a-table
                ref="table"
                bordered
                rowKey="packageRecordId"
                :columns="table2.columns"
                :dataSource="table2.dataSource"
                :loading="pdAllocationDetailTable.loading"
                :expandedRowKeys= "expandedRowKeys"
                 @expand="handleExpand">

                <a-table
                  slot="expandedRowRender"
                  slot-scope="text"
                  :columns="innerColumns"
                  :dataSource="innerData"
                  size="middle"
                  bordered
                  rowKey="purchaseDetailId"
                  :pagination="false"
                  :loading="subloading">
                </a-table>
              </a-table>
              <a-row style="margin-top:10px;text-align: right;padding-right: 5%">
                <span style="font-weight: bold;font-size: large;padding-right: 5%">总数量：{{this.model.totalNum }}</span>
              </a-row>
            </a-tab-pane>
          </a-tabs>
        </a-card>
        <a-card style="margin-bottom: 10px;">
        <a-form :form="form">
          <a-row>
            <a-col :span="12">
              <a-form-item label="审核意见" :labelCol="labelCol" :wrapperCol="wrapperCol" style="text-align: left">
                <a-textarea :disabled="disableSubmit" v-decorator="[ 'refuseReason', validatorRules.refuseReason]" placeholder="请输入审核意见"></a-textarea>
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
      </a-card>
      </div>
    </a-spin>
    <template slot="footer">
      <a-button @click="closeBtn" style="margin-right: 15px;" v-show="disableSubmit">关  闭</a-button>
      <a-button @click="submitPrintBtn" v-show="disableSubmit" type="primary" :loading="confirmLoading" style="margin-right: 15px;">打  印</a-button>
      <a-popconfirm title="确定放弃审核？" @confirm="handleCancel" v-show="!disableSubmit" okText="确定" cancelText="取消">
        <a-button style="margin-right: 15px;">取  消</a-button>
      </a-popconfirm>
      <a-button @click="handleOk('no')" v-show="!disableSubmit" type="danger" :loading="confirmLoading" style="margin-right: 15px;">驳 回</a-button>
      <a-button @click="handleOk('yes')" v-show="!disableSubmit" type="primary" :loading="confirmLoading" style="margin-right: 15px;">审核通过</a-button>
     </template>
    <pd-stock-record-out-modal ref="stockForm"></pd-stock-record-out-modal>
    <pd-allocation-record-print-modal  ref="PdAllocationRecordPrintModal"></pd-allocation-record-print-modal>
  </j-modal>
</template>
<script>

  import pick from 'lodash.pick'
  import { httpAction,getAction,downFile } from '@/api/manage'
  import { FormTypes,getRefPromise } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import JDate from '@/components/jeecg/JDate'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import PdStockRecordOutModal from './PdStockRecordOutModal'
  import PdAllocationRecordPrintModal from '../print/PdAllocationRecordPrintModal'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'


  export default {
    name: 'NewPdAllocationRecordModal',
    mixins: [JEditableTableMixin,JeecgListMixin],
    components: {
      JDate,
      JDictSelectTag,
      PdStockRecordOutModal,
      PdAllocationRecordPrintModal},
    data() {
      return {
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
        innerData:[],
        expandedRowKeys:[],
        subloading:false,
        validatorRules: {
          allocationNo:{},
          allocationDate:{},
          allocationBy:{},
          realName:{},
          outDeptId:{},
          outDeptName:{},
          inDeptId:{},
          inDeptName:{},
          totalNum: { rules: [{ required: true, message: '请输入调拨总数量!' }] },
          remarks:{},
          rejectReason:{}
        },
       refKeys: ['pdAllocationDetail', ],
        tableKeys:['pdAllocationDetail', ],
        activeKey: 'pdAllocationDetail',
        // 申购单详细表
        pdAllocationDetailTable: {
          loading: false,
          dataSource: [],
          columns: [
               { title: '产品ID', key: 'productId', type: FormTypes.hidden },
               { title: '产品名称', width:"250px",  key: 'productName' },
               { title: '产品编号',width:"200px",  key: 'number' },
               { title: '规格',width:"240px", key: 'spec' },
               { title: '型号', width:"240px", key: 'version' },
               { title: '单位',width:"50px",  key: 'unitName' },
               { title: '发货数量', width:"100px", key: 'arrivalNum' },
               { title: '调拨数量',  width:"100px",key: 'allocationNum'},
               { title: '库存数量', key: 'stockNum' },
          ]
        },
        // 表头
        table2:{
          dataSource:[],
          columns: [
            {title: '#', dataIndex: 'packageRecordId', key:'rowIndex', width:60, align:"center",
              customRender:function (t,r,index) {
                return parseInt(index)+1;
              }
            },
            {title:'套包编号', align:"center",key:"packageCode", dataIndex: 'packageCode'},
            {title:'套包名称', align:"center", key:"packageName",dataIndex: 'packageName'},
            { title:'套包条码', align:"center", dataIndex: 'packageBarCode' },
            {title:'产品总数', align:"center", key:"packageSum",dataIndex: 'packageSum'},
            {title:'调拨数量', align:"center",key:"allocationNum", dataIndex: 'allocationNum'},
            { title:'打包人', align:"center", dataIndex: 'createBy' },
            {title:'备注', align:"center", key:"remarks",dataIndex: 'remarks'}
          ]
        },
        innerColumns:[
          { title:'产品名称', align:"center", dataIndex: 'productName' },
          { title:'产品编号', align:"center", dataIndex: 'productNumber' },
          { title:'产品条码', align:"center",dataIndex: 'productBarCode' },
          { title:'规格', align:"center", dataIndex: 'spec' },
          { title:'批号', align:"center", dataIndex: 'batchNo' },
          { title:'单位', align:"center", dataIndex: 'unitName' },
          { title:'有效期', align:"center", dataIndex: 'expDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          { title:'入库单价', align:"center", dataIndex: 'purchasePrice' },
          { title:'出库单价', align:"center", dataIndex: 'sellingPrice' },
          { title:'套包产品数量', align:"center", dataIndex: 'productNum' },
          { title:'出库金额', align:"center", dataIndex: 'outTotalPrice' },
          { title:'库存数量', align:"center", dataIndex: 'stockNum' },
          { title: '出库货位', align:"center", dataIndex: 'outHuoweiName' },
          { title: '生产日期', align:"center", dataIndex: 'produceDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }}
        ],
        url: {
          edit: "/pd/pdAllocationRecord/editAllocationInf",
          exportXlsUrl: "/pd/pdAllocationRecord/exportXls",
          chooseDetailList:"/pd/pdPackageRecord/queryPdPackageRecordDetailByMainId",
          queryPackageRecordListByIds: "/pd/pdPackageRecord/queryPackageRecordListByIds",
          pdAllocationDetail: {
            list: '/pd/pdAllocationRecord/queryPdAllocationDetailList',
            packList: '/pd/pdAllocationRecord/queryAllocationDetailPack'
          },
        },

      }
    },
    methods: {
      loadData(arg) {
      },

      handleExpand(expanded, record){
        this.expandedRowKeys=[];
        this.innerData=[];
        if(expanded===true){
          this.subloading = true;
          this.expandedRowKeys.push(record.packageRecordId);
          getAction(this.url.chooseDetailList, {id: record.packageRecordId}).then((res) => {
            if (res.success) {
              this.subloading = false;
              this.innerData = res.result;
            }
          });
        }
      },
      /* 导出 */
      exportXls(fileName){
        if(!fileName || typeof fileName != "string"){
          fileName = "导出文件"
        }
        if(this.pdAllocationDetailTable.dataSource.length>0){
          let param = {"allocationNo":this.model.allocationNo};
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

      submitPrintBtn() {  //通过并打印
        this.model.packageIds=[];
        this.model.pdAllocationDetailList = this.pdAllocationDetailTable.dataSource;
        var source = this.table2.dataSource;
        let packageIds="";
        source.forEach((value, idx) => {
          packageIds+=value.packageId+",";
        })
        this.model.packageIds = packageIds;
        this.$refs.PdAllocationRecordPrintModal.show(this.model);
        this.$refs.PdAllocationRecordPrintModal.title = "调拨单";
      },
      handleOk (type) { //审核提交
        this.model.auditStatus='2';//审核通过
        this.model.submitStatus='2';//已提交
        if(type=="no"){
          this.model.auditStatus='3';//拒绝
          this.model.submitStatus='1';//待提交
        }
        this.form.validateFields((err, values) => {
          if(type=="no"){
            if(values.refuseReason==null || values.refuseReason==''){
              this.$message.warning("请填写审核意见")
              return
            }
          }
          this.model.refuseReason= values.refuseReason;
          if (!err) {
            const that = this;
            let pdPurchaseDetailList = this.pdAllocationDetailTable.dataSource;
            let values = [];
            values.pdAllocationDetailList = pdPurchaseDetailList;
            let formData = Object.assign(this.model, values);
            that.confirmLoading = true;
            httpAction(this.url.edit, formData, 'put').then((res) => {
              if (res.success) {
                 if(type=="yes"){
                   this.recordOut();
                }
                that.$emit('ok');
              } else {
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })
          }
          })

      },

      recordOut(){
        let args = {};
        args.outType = "3";  //  1-申领出库; 2-科室出库; 3-调拨出库
        args.data = this.pdAllocationDetailTable.dataSource;  // 申领单或调拨单产品明细 按选择器传值就行
        var source = this.table2.dataSource;
        let packageRecordIds="";
        source.forEach((value, idx) => {
          packageRecordIds+=value.packageRecordId+",";
        })
        getAction(this.url.queryPackageRecordListByIds, {ids:packageRecordIds}).then((res) => {
          if (res.success) {
            let data = {};
            args.pdPackageRecordList = res.result;
            args.inDepartId = this.model.inDeptId; //入库部门ID
            this.$refs.stockForm.add(args);
            this.$refs.stockForm.title = "新增出库";
            this.$refs.stockForm.disableSubmit = false;
          }
        });
      },

 //--------------
      editLound(){
          let params = {allocationNo: this.model.allocationNo}
           this.requestSubTableData(this.url.pdAllocationDetail.list, params, this.pdAllocationDetailTable)//加载产品及套包明细
      },
 //---------------




      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
        let fieldval = pick(this.model,'allocationNo','allocationDate','outDeptName','inDeptName','realName','remarks','rejectReason')
         this.$nextTick(() => {
          this.form.setFieldsValue(fieldval)
        })
        // 加载子表数据
        if (this.model.id) {
          let params = {allocationNo: this.model.allocationNo,productAttr:"1"}
          this.requestSubTableData(this.url.pdAllocationDetail.list, params, this.pdAllocationDetailTable)//加载产品
          params =  {allocationNo: this.model.allocationNo,productAttr:"2" }
          this.requestSubTableData(this.url.pdAllocationDetail.packList, params, this.table2)//加载套包


        }
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)

        return {
          ...main, // 展开
          pdApplyDetailList: allValues.tablesValue[0].values,
        }
      },

      validateError(msg){
        this.$message.error(msg)
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'allocationNo','allocationDate','outDeptName','inDeptName','realName','remarks','rejectReason'))
       },
      /** 关闭按钮 **/
      closeBtn(){
        this.visible = false;
        this.$emit('close');
      },
    }
  }
</script>

<style scoped>
  .drawer-bootom-button {
    position: absolute;
    /*top:95%;*/
    bottom: -30px;
    width: 100%;
    border-top: 1px solid #e8e8e8;
    padding: 10px 16px;
    text-align: right;
    left: 0;
    background: #fff;
    border-radius: 0 0 2px 2px;
    z-index:199;
  }
  /** Button按钮间距 */
  /*.ant-btn {
    margin-left: 30px;
    margin-bottom: 30px;
    float: right;
  }*/
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

