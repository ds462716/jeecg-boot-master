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
                <a-form-item label="申购编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input disabled="disabled" v-decorator="[ 'orderNo', validatorRules.orderNo]" ></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="申购人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input disabled="disabled" v-decorator="[ 'purchaseName', validatorRules.purchaseName]" ></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="申购日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <j-date disabled="disabled" v-decorator="[ 'orderDate', validatorRules.orderDate]" :trigger-change="true" style="width: 100%"/>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="申购科室" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input disabled="disabled" v-decorator="[ 'deptName', validatorRules.deptName]" ></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="申购总数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input-number disabled="disabled" v-decorator="[ 'totalNum', validatorRules.totalNum]"  style="width: 100%"/>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item label="申购总金额" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input-number disabled="disabled" v-decorator="[ 'totalPrice', validatorRules.totalPrice]"  style="width: 100%"/>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-card>
        <a-card style="margin-bottom: 10px;">
          <a-tabs v-model="activeKey" @change="handleChangeTabs">
            <a-tab-pane tab="申购明细表" :key="refKeys[0]" :forceRender="true">
              <div style="margin-bottom: 8px;">
              </div>
              <j-editable-table
                bordered
                :ref="refKeys[0]"
                :loading="pdPurchaseDetailTable.loading"
                :columns="pdPurchaseDetailTable.columns"
                :dataSource="pdPurchaseDetailTable.dataSource"
                :maxHeight="900"
                :rowNumber="true"
                :rowSelection="true"
                :actionButton="false"
                style="text-overflow: ellipsis;"
              />
            </a-tab-pane>
          </a-tabs>
        </a-card>
        <a-card style="margin-bottom: 10px;">
          <a-form :form="form">
            <a-row>
              <a-col :span="12">
                <a-form-item label="审核意见" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input :disabled="disableSubmit" v-decorator="[ 'refuseReason', validatorRules.refuseReason]" placeholder="请输入审核意见" style="width: 100%;height: 80px"/>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-card>
      </div>
      <pd-apply-stock-record-out-modal ref="stockForm"></pd-apply-stock-record-out-modal>
    </a-spin>
    <template slot="footer">
      <a-button @click="closeBtn" style="margin-right: 15px;" v-show="disableSubmit">关  闭</a-button>
      <a-popconfirm title="确定放弃审核？" @confirm="handleCancel" v-show="!disableSubmit" okText="确定" cancelText="取消">
        <a-button style="margin-right: 15px;">取  消</a-button>
      </a-popconfirm>
      <a-button @click="handleOk('no')" v-show="!disableSubmit" type="danger" :loading="confirmLoading" style="margin-right: 15px;">驳 回</a-button>
      <a-button @click="handleOk('yes')" v-show="!disableSubmit" type="primary" :loading="confirmLoading" style="margin-right: 15px;">审核通过</a-button>
    </template>
  </j-modal>
</template>
<script>

  import pick from 'lodash.pick'
  import { httpAction,getAction,downFile } from '@/api/manage'
  import { FormTypes,getRefPromise } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import JDate from '@/components/jeecg/JDate'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import PdApplyStockRecordOutModal from './PdStockRecordOutModal'


  export default {
    name: 'PdApplyOrderModal',
    mixins: [JEditableTableMixin],
    components: {JDate, JDictSelectTag,PdApplyStockRecordOutModal},
    data() {
      return {
        title: '这里是标题',
        lockScroll: false,
        fullscreen: true,
        switchFullscreen: false,
        disableSubmit: false,
        confirmLoading: false,
        labelCol: {span: 6},
        wrapperCol: {span: 16},
        labelCol2: {span: 3},
        wrapperCol2: {span: 20},
        validatorRules: {
          applyNo: {},
          deptName: {},
          applyDate: {},
          totalNum: {},
          realName: {},
          remarks: {},
          refuseReason: {}
        },
        refKeys: ['pdPurchaseDetail',],
        tableKeys: ['pdPurchaseDetail',],
        activeKey: 'pdPurchaseDetail',
        // 申购单详细表
        pdPurchaseDetailTable: {
          loading: false,
          dataSource: [],
          columns: [
             {title: '产品编号', width: "200px", align: "center", key: 'number'},
             {title: '产品名称', width: "240px", key: 'productName'},
             {title: '规格', width: "200px", align: "center", key: 'spec'},
             {title: '型号', width: "200px", align: "center", key: 'version'},
             {title: '单位', width: "50px", align: "center", key: 'unitName'},
             {title: '库存数量', align: "center", key: 'stockNum'},
             {title: '申购数量',  align: "center", key: 'orderNum'},
             {title: '产品单价',  align: "center", key: 'purchasePrice'},
             {title: '申购金额',  align: "center", key: 'orderMoney'},
             {title: '供应商', width: "300px", align: "center", key: 'supplierName'},
             {title: '生产厂家', width: "300px", align: "center", key: 'venderName'}
                  ]
        },
        url: {
          edit: "/pd/pdPurchaseOrderMerge/edit",
          exportXlsUrl: "/pd/pdPurchaseOrder/exportXls",
          pdPurchaseDetail: {
            list: '/pd/pdPurchaseOrder/queryPdPurchaseDetail'
          },
        }
      }
    },
    methods: {
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
      handleOk (type) { //审核提交
        this.model.auditStatus='2';//审核通过
        this.model.submitStatus='2';//已提交
        if(type=="no"){
          this.model.auditStatus='3';//拒绝
          this.model.submitStatus='3';//已撤回
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
            this.model.orderNos=this.model.orderNo;
            this.model.oprtSource="1";
            let formData = Object.assign(this.model, values);
            httpAction(this.url.edit, formData, 'put').then((res) => {
              if (res.success) {
                that.$message.success("操作成功");
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
      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
        let fieldval = pick(this.model,'orderNo','purchaseName','orderDate','deptName','auditStatus','totalNum','totalPrice','submitStatus','refuseReason')
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
        this.form.setFieldsValue(pick(row,'orderNo','purchaseName','orderDate','deptName','auditStatus','totalNum','totalPrice','submitStatus','refuseReason'))
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

