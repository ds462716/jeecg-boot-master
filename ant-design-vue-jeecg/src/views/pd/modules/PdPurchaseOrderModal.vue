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
              <a-input disabled="disabled" v-decorator="[ 'orderNo', validatorRules.orderNo]"></a-input>
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
            <a-form-item label="申购总数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number disabled="disabled" v-decorator="[ 'totalNum', validatorRules.totalNum]"  style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="申购总金额" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number disabled="disabled" v-decorator="[ 'totalPrice', validatorRules.totalPrice]" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <!-- <!-- 子表单区域 -->
          <div style="float: left;">
            <a-button type="primary" icon="download" @click="exportXls('申购产品列表')">导出</a-button>
            <a-button v-show="!disableSubmit" @click="choice" style="margin-left: 0px;margin-bottom: 20px"  type="primary">选择产品</a-button>
          </div>
          <div style="float: left;width:100%;margin-bottom: 70px;white-space:nowrap;overflow-x:auto;overflow-y:hidden;">
            <table id="contentTable" class="tableStyle">
              <tr>
                <th v-show="!disableSubmit">操作</th>
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
              <tr  v-for="(item, index) in pdPurchaseDetailTable.dataSource">
                <td v-show="!disableSubmit"><a @click="deleteDetail(item.productId)">删除</a></td>
                <td>{{item.number}}</td>
                <td>{{item.productName}}</td>
                <td>{{item.spec}}</td>
                <td>{{item.version}}</td>
                <td>{{item.unitName}}</td>
                <td>{{item.stockNum}}</td>
                <td>
                   <a-form-item>
                 <a-input-number  :disabled="disableSubmit"  @blur="(e)=>{handleConfirmBlur(e.target,item)}"  v-decorator="['pdPurchaseDetailTable['+index+'].orderNum', {'initialValue':item.orderNum,rules:validatorRules.orderNum}]"/>
                   </a-form-item>
                </td>
                <td>{{item.purchasePrice}}</td>
                <td>{{item.orderMoney}}</td>
                <td>{{item.supplierName}}</td>
                <td>{{item.venderName}}</td>
              </tr>
            </table>
          </div>
          <a-col :span="12" v-show="disableSubmit">
            <a-form-item   label="审核意见" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input  :disabled="disableSubmit"  v-decorator="[ 'refuseReason', validatorRules.refuseReason]"  style="width: 100%;height: 80px"/>
            </a-form-item>
          </a-col>
        </a-row>
        <pd-purchase-detail-add-modal  ref="PdPurchaseDetailAddModal" @ok="modalFormOk"></pd-purchase-detail-add-modal>
      </a-form>
    </a-spin>
    <div class="drawer-bootom-button" v-show="!disableSubmit">
      <a-button @click="handleOk('submit')" type="primary" :loading="confirmLoading">提交</a-button>
      <a-button @click="handleOk('save')" type="primary" :loading="confirmLoading">保存草稿</a-button>
      <a-popconfirm title="确定放弃编辑？" @confirm="handleCancel" okText="确定" cancelText="取消">
        <a-button style="margin-right: .8rem">取消</a-button>
      </a-popconfirm>
    </div>
  </a-drawer>
</template>
<script>

  import pick from 'lodash.pick'
  import { httpAction,getAction,downFile } from '@/api/manage'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import JDate from '@/components/jeecg/JDate'
  import {JDictSelectTag,typeText}from "@/components/dict/JDictSelectTag"
  import PdPurchaseDetailAddModal from './PdChooseProductListModel'
  export default {
    name: 'PdPurchaseOrderModal',
    mixins: [JEditableTableMixin],
    components: {JDate, JDictSelectTag,PdPurchaseDetailAddModal},
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
          applyCount:[
            {required: true,message: '请输入值'}],
        },
        refKeys: ['pdPurchaseDetail', ],
        tableKeys:['pdPurchaseDetail', ],
        // 申购单详细表
        pdPurchaseDetailTable: {
          dataSource: []
        },
        url: {
          add: "/pd/pdPurchaseOrder/add",
          edit: "/pd/pdPurchaseOrder/edit",
          exportXlsUrl: "/pd/pdPurchaseOrder/exportXls",
          pdPurchaseDetail: {
            list: '/pd/pdPurchaseOrder/queryPdPurchaseDetail'
          },
        }
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
            let model={};
            this.model.orderNo=res.result.orderNo;//申购编号
            this.model.purchaseBy=res.result.purchaseBy;//申购人编号
            this.model.purchaseName=res.result.purchaseName;//申购人名称
            this.model.orderDate=res.result.orderDate;//申购日期
            this.model.deptId=res.result.deptId;//申购科室Id
            this.model.deptName=res.result.deptName;//申购科室名称
            this.model.totalNum=res.result.totalNum;//申购总数量
            this.model.totalPrice=res.result.totalPrice;//申购总金额
            this.model.submitStatus=res.result.submitStatus;//提交状态
            this.model.auditStatus=res.result.auditStatus;//审核状态
            this.$nextTick(() => {
              this.form.setFieldsValue(pick(this.model,'orderNo','purchaseName','orderDate','deptName','totalNum','totalPrice','refuseReason'))
            })
          }
        })
      },
      //修改申购数量后重新计算总数量及总金额
      handleConfirmBlur(e,m){
        this.orderNum = e.value;//修改后的申购数量
        this.purchasePrice=m.purchasePrice;//产品单价；
        m.orderMoney= (this.orderNum * this.purchasePrice).toFixed(2);//计算修改后的总金额
        m.orderNum=e.value;
       let tableData=this.pdPurchaseDetailTable.dataSource;
       let totalNum=0;
       let totalPrice=0;
        for(let i=0;i<tableData.length;i++){
          totalNum=totalNum+parseFloat(tableData[i].orderNum);//计算总数量
          totalPrice=totalPrice+Number(tableData[i].orderMoney);//计算申购总金额
        }
        let model={};
        this.model.totalNum=totalNum;//申购总数量
        this.model.totalPrice=totalPrice.toFixed(2);//申购总金额
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'totalNum','totalPrice'))
        })
      },

      //选择产品
      choice() {
        this.$refs.PdPurchaseDetailAddModal.show();
        this.$refs.PdPurchaseDetailAddModal.title = "选择产品";
      },

      deleteDetail(productId){
        const newData = this.pdPurchaseDetailTable.dataSource.filter(item => item.productId !== productId);
        let totalNum=0;
        let totalPrice=0;
        for(let i=0;i<newData.length;i++){
          totalNum=totalNum+parseFloat(newData[i].applyCount);//计算总数量
          totalPrice=totalPrice+Number(newData[i].amountMoney);//计算申购总金额
        }
        let model={};
        this.model.totalNum=totalNum;//申购总数量
        this.model.totalPrice=totalPrice.toFixed(2);//申购总金额
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'totalNum','totalPrice'))
        })
        this.pdPurchaseDetailTable.dataSource = newData;
      },

      modalFormOk (formData) {//选择产品确定后返回所选择的数据
        let values = [];
        let totalNum=0;
        let totalPrice=0;
        for(let i=0;i<formData.length;i++){
          values.push({
            productId: formData[i].productId,
            number: formData[i].number,
            productName: formData[i].productName,
            spec:formData[i].spec,
            purchasePrice: formData[i].purchasePrice,
            orderNum: 1.00,//默认1
            version: formData[i].version,
            stockNum: formData[i].stockNum,
            unitName:formData[i].unitName,
            orderMoney:formData[i].purchasePrice * 1,
            venderName:formData[i].venderName,
            supplierId:formData[i].supplierId,
            supplierName:formData[i].supplierName
          })
          totalNum+=1;//计算总数量
          totalPrice=totalPrice+Number(formData[i].purchasePrice);//计算申购总金额
        }
        let model={};
        this.model.totalNum=totalNum;//申购总数量
        this.model.totalPrice=totalPrice.toFixed(2);//申购总金额
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'totalNum','totalPrice'))
        })
        this.pdPurchaseDetailTable.dataSource = values;
      },
      handleOk (submitType) { //提交
        if(submitType=="submit"){
          this.model.submitStatus='2';
          this.model.auditStatus='1';
        }
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            //选择标识符的校验通过后
            let pdPurchaseDetailList=this.pdPurchaseDetailTable.dataSource;
            values.pdPurchaseDetailList=pdPurchaseDetailList;
            if(pdPurchaseDetailList.length>0){
              let httpurl = '';
              let method = '';
              if(!this.model.id){
                httpurl+=this.url.add;
                method = 'post';
              }else{
                httpurl+=this.url.edit;
                method = 'put';
              }
              let formData = Object.assign(this.model, values);
              httpAction(httpurl,formData,method).then((res)=>{
                 if(res.success){
                  that.$message.success(res.message);
                  that.$emit('ok');
                }else{
                  that.$message.warning(res.message);
                }
              }).finally(() => {
                that.confirmLoading = false;
                that.close();
              })
            }else{
              that.$message.error("请选择产品");
            }
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

