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
            <a-form-item label="申领单号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input disabled="disabled" v-decorator="[ 'applyNo', validatorRules.applyNo]"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="申领科室" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input disabled="disabled" v-decorator="[ 'deptName', validatorRules.deptName]"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="申领人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input disabled="disabled" v-decorator="[ 'realName', validatorRules.realName]"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="申领日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date  disabled="disabled" v-decorator="[ 'applyDate', validatorRules.applyDate]" :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="申领总数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number disabled="disabled" v-decorator="[ 'totalNum', validatorRules.totalNum]"  style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item   label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input  disabled="disabled"  v-decorator="[ 'remarks', validatorRules.remarks]"  style="width: 100%;height: 60px"/>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
        </a-card>
        <a-card style="margin-bottom: 10px;">
          <a-tabs v-model="activeKey" @change="handleChangeTabs">
            <a-tab-pane tab="申领明细表" :key="refKeys[0]" :forceRender="true">
              <div style="margin-bottom: 8px;">
                <!--  <a-button type="primary" icon="download" @click="exportXls('申领产品列表')">导出</a-button>-->
              </div>
              <j-editable-table
                bordered
                :ref="refKeys[0]"
                :loading="pdApplyDetailTable.loading"
                :columns="pdApplyDetailTable.columns"
                :dataSource="pdApplyDetailTable.dataSource"
                :maxHeight="500"
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
        lockScroll: true,
        fullscreen: true,
        switchFullscreen: true,
        disableSubmit:false,
        confirmLoading: false,
        labelCol: {span: 6},
        wrapperCol: {span: 16},
        labelCol2: {span: 3},
        wrapperCol2: {span: 20},
        validatorRules: {
          applyNo:{},
          deptName:{},
          applyDate:{},
          totalNum:{},
          realName:{},
          remarks:{},
          refuseReason:{}
        },
       refKeys: ['pdApplyDetail', ],
        tableKeys:['pdApplyDetail', ],
        activeKey: 'pdApplyDetail',
        // 申购单详细表
        pdApplyDetailTable: {
          loading: false,
          dataSource: [],
          columns: [
            { title: '定数包编号', width:"130px",   key: 'packageId' },
            { title: '定数包名称',  width:"130px", key: 'packageName' },
            { title: '产品ID', key: 'productId', type: FormTypes.hidden },
            { title: '产品名称', width:"250px",  key: 'productName' },
            { title: '申领数量',  width:"100px",align:"center",key: 'applyNum'},
            { title: '产品编号',width:"200px", align:"center", key: 'number' },
            { title: '规格',width:"240px", align:"center", key: 'spec' },
            { title: '型号', width:"240px",align:"center", key: 'version' },
            { title: '单位',width:"50px", align:"center", key: 'unitName' },
            { title: '发货数量', width:"100px",align:"center", key: 'arrivalNum' },
            { title: '库存数量', align:"center", key: 'stockNum' },
          ]
        },
        url: {
          edit: "/pd/pdApplyOrder/edit",
          exportXlsUrl: "/pd/pdApplyOrder/exportXls",
          pdApplyDetail: {
            list: '/pd/pdApplyOrder/queryApplyDetail'
          },
        },

      }
    },
    methods: {
      /* 导出 */
      exportXls(fileName){
        if(!fileName || typeof fileName != "string"){
          fileName = "导出文件"
        }
        if(this.pdApplyDetailTable.dataSource.length>0){
          let param = {"applyNo":this.model.applyNo};
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
            let pdPurchaseDetailList = this.pdApplyDetailTable.dataSource;
            let values = [];
            values.pdApplyDetailList = pdPurchaseDetailList;
            let formData = Object.assign(this.model, values);
            httpAction(this.url.edit, formData, 'put').then((res) => {
              if (res.success) {
                 if(type=="yes"){
                   let args = {};
                   args.outType = "1";  //  1-申领出库; 2-科室出库; 3-调拨出库
                   args.data = pdPurchaseDetailList;  // 申领单或调拨单明细 按选择器传值就行
                   args.inDepartId = this.model.departId; // 入库部门ID
                   this.$refs.stockForm.add(args);
                   this.$refs.stockForm.title = "新增出库";
                   this.$refs.stockForm.disableSubmit = false;
                }
               // that.$message.success("操作成功");
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
        let fieldval = pick(this.model,'applyNo','deptName','totalNum','applyDate','realName','remarks','refuseReason')
        this.$nextTick(() => {
          this.form.setFieldsValue(fieldval)
        })
        // 加载子表数据
        if (this.model.id) {
          let params = { applyNo: this.model.applyNo }
          this.requestSubTableData(this.url.pdApplyDetail.list, params, this.pdApplyDetailTable)
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
        this.form.setFieldsValue(pick(row,'applyNo','deptName','totalNum','applyDate','realName','remarks','refuseReason'))
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

