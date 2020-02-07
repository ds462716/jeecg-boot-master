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
              <a-input disabled="disabled" v-decorator="[ 'orderNo', validatorRules.orderNo]" placeholder="请输入申购编号"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="申购人编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input disabled="disabled" v-decorator="[ 'purchaseBy', validatorRules.purchaseBy]" placeholder="请输入申购人编号"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="申购日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择申购日期" disabled="disabled" v-decorator="[ 'orderDate', validatorRules.orderDate]" :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="申购库房名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'storeroomName', validatorRules.storeroomName]" placeholder="请输入库房名称"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="申购总数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number disabled="disabled" v-decorator="[ 'amountCount', validatorRules.amountCount]" placeholder="请输入申购总数量" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="申购总金额" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number disabled="disabled" v-decorator="[ 'amountMoney', validatorRules.amountMoney]" placeholder="请输入申购总金额" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <!-- <!-- 子表单区域 -->
          <div style="float: left;" v-show="!disableSubmit">
            <a-button @click="choice" style="margin-left: 0px;margin-bottom: 20px"  type="primary">选择产品</a-button>
          </div>
          <div style="float: left;width:100%;margin-bottom: 70px">
            <table id="contentTable" class="tableStyle">
              <tr>
                <th>操作</th>
                <th>产品编号</th>
                <th>产品批次号</th>
                <th>产品有效期</th>
                <th>产品单价</th>
                <th>申购数量</th>
                <th>申购总金额</th>
                <th>申购时总库存数量</th>
                <th>生产厂家</th>
              </tr>
              <tr v-for="(item, index) in pdPurchaseDetailTable.dataSource">
                <td><a @click="deleteDetail(item.productId)">删除</a></td>
                <td>{{item.productNo}}</td>
                <td>{{item.batchNo}}</td>
                <td>{{item.expireDate}}</td>
                <td>{{item.inPrice}}</td>
                <td>
                   <a-form-item>
                 <a-input  :readOnly="disableSubmit" :style="{width: 'calc(80% - 10px)'}" @blur="(e)=>{handleConfirmBlur(e.target,item)}"  v-decorator="['pdPurchaseDetailTable['+index+'].length', {'initialValue':item.applyCount,rules:validatorRules.applyCount}]"/>
                  </a-form-item>
                </td>
                <td>{{item.amountMoney}}</td>
                <td>{{item.stockNum}}</td>
                <td>{{item.meaning}}</td>
              </tr>
            </table>
          </div>
        </a-row>
        <pd-purchase-detail-add-modal  ref="PdPurchaseDetailAddModal" @ok="modalFormOk"></pd-purchase-detail-add-modal>
      </a-form>
    </a-spin>
    <div class="drawer-bootom-button" v-show="!disableSubmit">
      <a-button @click="handleOk" type="primary" :loading="confirmLoading">提交</a-button>
      <a-popconfirm title="确定放弃编辑？" @confirm="handleCancel" okText="确定" cancelText="取消">
        <a-button style="margin-right: .8rem">取消</a-button>
      </a-popconfirm>
    </div>
  </a-drawer>
</template>
<script>

  import pick from 'lodash.pick'
  import { httpAction,getAction } from '@/api/manage'
  import { FormTypes,getRefPromise } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import JDate from '@/components/jeecg/JDate'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import PdPurchaseDetailAddModal from './PdEncodingIdentifierAddModal'
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
          orderDate:{},
          storeroomName:{},
          amountCount:{},
          amountMoney:{},
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
      purchaseInfo() { //新增页面初始化
        getAction("/pd/pdPurchaseOrder/purchaseInfo",{}).then((res)=>{
          if (res.success) {
            let model={};
            this.model.orderNo=res.result.orderNo;//申购编号
            this.model.purchaseBy=res.result.auditBy;//申购人编号
            this.model.orderDate=res.result.orderDate;//申购日期
            this.model.storeroomName="待定";//申购库房名称
            this.model.amountCount=res.result.amountCount;//申购总数量
            this.model.amountMoney=res.result.amountMoney;//申购总金额
            this.$nextTick(() => {
              this.form.setFieldsValue(pick(this.model,'orderNo','purchaseBy','orderDate','storeroomName','amountCount','amountMoney'))
            })
          }
        })
      },
      //修改申购数量后重新计算总数量及总金额
      handleConfirmBlur(e,m){
        this.applyCount = e.value;//修改后的申购数量
        this.inPrice=m.inPrice;//产品单价；
        m.amountMoney= this.applyCount * this.inPrice;//计算修改后的总金额
        m.applyCount=e.value;
       let tableData=this.pdPurchaseDetailTable.dataSource;
       let count=0;
       let amountMoney=0;
        for(let i=0;i<tableData.length;i++){
           count=count+parseInt(tableData[i].applyCount);//计算总数量
           amountMoney=amountMoney+Number(tableData[i].amountMoney);//计算申购总金额
        }
        let model={};
        this.model.amountCount=count;//申购总数量
        this.model.amountMoney=amountMoney;//申购总金额
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'amountCount','amountMoney'))
        })
      },

      //选择产品
      choice() {
        this.$refs.PdPurchaseDetailAddModal.show();
        this.$refs.PdPurchaseDetailAddModal.title = "选择产品";
      },

      deleteDetail(productId){
        const newData = this.pdPurchaseDetailTable.dataSource.filter(item => item.productId !== productId);
        let count=0;
        let amountMoney=0;
        for(let i=0;i<newData.length;i++){
          count=count+parseInt(newData[i].applyCount);//计算总数量
          amountMoney=amountMoney+Number(newData[i].amountMoney);//计算申购总金额
        }
        let model={};
        this.model.amountCount=count;//申购总数量
        this.model.amountMoney=amountMoney;//申购总金额
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'amountCount','amountMoney'))
        })
        this.pdPurchaseDetailTable.dataSource = newData;
      },

      modalFormOk (formData) {//选择产品确定后返回所选择的数据
        let values = [];
        let count=0;
        let amountMoney=0;
        for(let i=0;i<formData.length;i++){
          values.push({
            orderNo: formData[i].value,
            productId: formData[i].value,
            productNo: formData[i].value,
            batchNo: formData[i].value,
            expireDate:new Date,
            inPrice: formData[i].value,
            applyCount: 1,//默认1
            amountMoney: formData[i].value,
            stockNum: formData[i].value
          })
          count=count+parseInt(formData[i].value);//计算总数量
          amountMoney=amountMoney+Number(formData[i].value);//计算申购总金额
        }
        let model={};
        this.model.amountCount=count;//申购总数量
        this.model.amountMoney=amountMoney;//申购总金额
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'amountCount','amountMoney'))
        })
        this.pdPurchaseDetailTable.dataSource = values;
      },

      handleOk () {//提交
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            //选择标识符的校验通过后
            let pdPurchaseDetailList=this.pdPurchaseDetailTable.dataSource;
            values.pdPurchaseDetailList=pdPurchaseDetailList;
            console.log(values)
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
        let fieldval = pick(this.model,'orderNo','purchaseBy','orderDate','storeroomName','orderStatus','amountCount','amountMoney','submitStart')
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
        this.form.setFieldsValue(pick(row,'orderNo','purchaseBy','orderDate','storeroomName','orderStatus','amountCount','amountMoney','submitStart'))
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
    padding: 16px 16px;
    font-weight: 500;
    box-sizing: border-box;
  }

  .tableStyle> tr > td >input{
    width:45px;
    text-align: center;
  }
</style>

