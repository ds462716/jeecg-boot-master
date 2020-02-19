<template>
  <a-drawer
    :title="title"
    :width="800"
    placement="right"
    :closable="false"
    @close="close"
    :maskClosable="true"
    :confirmLoading="confirmLoading"
    @cancel="handleCancel"
    :visible="visible">
    <a-spin :spinning="confirmLoading">
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
          <!-- <!-- 子表单区域 -->
          <a-button style="float: left;" type="primary" icon="download" @click="exportXls('申购产品列表')">导出</a-button>
          <div style="float: left;width:100%;margin-bottom: 70px;white-space:nowrap;overflow-x:auto;overflow-y:hidden;">
            <table id="contentTable" class="tableStyle">
              <tr>
                <th>产品编号</th>
                <th>产品名称</th>
                <th>规格</th>
                <th>型号</th>
                <th>单位</th>
                <th>库存数量</th>
                <th>申购数量</th>
                <th>产品单价</th>
                <th>申购金额</th>
                <th>供应商</th>
                <th>生产厂家</th>
              </tr>
              <tr v-for="(item, index) in pdPurchaseDetailTable.dataSource">
                <td>{{item.number}}</td>
                <td>{{item.productName}}</td>
                <td>{{item.spec}}</td>
                <td>{{item.version}}</td>
                <td>{{item.unitName}}</td>
                <td>{{item.stockNum}}</td>
                <td>
                   <a-form-item>
                 <a-input  disabled="disabled"   @blur="(e)=>{handleConfirmBlur(e.target,item)}"  v-decorator="['pdPurchaseDetailTable['+index+'].orderNum', {'initialValue':item.orderNum,rules:validatorRules.orderNum}]"/>
                  </a-form-item>
                </td>
                <td>{{item.purchasePrice}}</td>
                <td>{{item.orderMoney}}</td>
                <td>{{item.supplierName}}</td>
                <td>{{item.venderName}}</td>
              </tr>
            </table>
          </div>
         <a-col :span="12">
          <a-form-item label="审核意见" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input :disabled="disableSubmit" v-decorator="[ 'refuseReason', validatorRules.refuseReason]" placeholder="请输入审核意见" style="width: 100%;height: 80px"/>
          </a-form-item>
        </a-col>
        </a-row>
      </a-form>
    </a-spin>
    <div class="drawer-bootom-button" v-show="!disableSubmit">
      <a-button @click="handleOk('yes')" type="primary" :loading="confirmLoading">审核通过</a-button>
      <a-button @click="handleOk('no')" type="primary" :loading="confirmLoading">拒绝</a-button>
      <a-popconfirm title="确定放弃审核？" @confirm="handleCancel" okText="确定" cancelText="取消">
        <a-button style="margin-right: .8rem">取消</a-button>
      </a-popconfirm>
    </div>
  </a-drawer>
</template>
<script>

  import pick from 'lodash.pick'
  import { httpAction,getAction,downFile } from '@/api/manage'
  import { FormTypes,getRefPromise } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import JDate from '@/components/jeecg/JDate'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  export default {
    name: 'PdPurchaseOrderModal',
    mixins: [JEditableTableMixin],
    components: {JDate, JDictSelectTag},
    data() {
      return {
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
        },
       refKeys: ['pdPurchaseDetail', ],
        tableKeys:['pdPurchaseDetail', ],
        // 申购单详细表
        pdPurchaseDetailTable: {
          dataSource: []
        },
        url: {
          edit: "/pd/pdPurchaseOrder/edit",
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
            let pdPurchaseDetailList = this.pdPurchaseDetailTable.dataSource;
            let values = [];
            values.pdPurchaseDetailList = pdPurchaseDetailList;
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
  .ant-btn {
    margin-left: 30px;
    margin-bottom: 30px;
    float: right;
  }
  .tableStyle> tr > th{
    border: 1px solid #e8e8e8;
    text-align: center;
    padding: 10px 16px;
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

