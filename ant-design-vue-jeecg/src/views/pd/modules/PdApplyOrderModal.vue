<template>
  <a-drawer
    :title="title"
    :width="800"
    placement="right"
    :closable="false"
    @close="close"
    :maskClosable="disableSubmit"
    :confirmLoading="confirmLoading"
    @cancel="handleCancel"
    :visible="visible">
    <a-spin :spinning="confirmLoading">
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
              <a-input  :disabled="disableSubmit"  v-decorator="[ 'remarks', validatorRules.remarks]"  style="width: 100%"/>
            </a-form-item>
          </a-col>
            <!-- 子表单区域 -->
          <div style="float: left;">
            <a-button type="primary" icon="download" @click="exportXls('申领产品列表')">导出</a-button>
            <a-button v-show="!disableSubmit" @click="choice" style="margin-left: 0px;margin-bottom: 20px"  type="primary">选择产品</a-button>
          </div>
          <div style="float: left;width:100%;margin-bottom: 70px;white-space:nowrap;overflow-x:auto;overflow-y:hidden;">
            <table id="contentTable" class="tableStyle">
              <tr>
                <th v-show="!disableSubmit">操作</th>
                <th>定数包名称</th>
                <th>定数包编号</th>
                <th>产品名称</th>
                <th>产品编号</th>
                <th>规格</th>
                <th>型号</th>
                <th>单位</th>
                <th>产品数量</th>
                <th>申领数量</th>
                <th>库存数量</th>
              </tr>
              <tr v-for="(item, index) in pdApplyDetailTable.dataSource">
                <td v-show="!disableSubmit"><a @click="deleteDetail(item.productId)">删除</a></td>
                <td>{{item.packageId}}</td>
                <td>{{item.packageName}}</td>
                <td>{{item.productName}}</td>
                <td>{{item.number}}</td>
                <td>{{item.spec}}</td>
                <td>{{item.version}}</td>
                <td>{{item.unitName}}</td>
                <td>{{item.productNum}}</td>
                <td>
                  <a-form-item>
                    <a-input-number  :disabled="disableSubmit" :style="{width: 'calc(120% - 5px)'}" @blur="(e)=>{handleConfirmBlur(e.target,item)}"  v-decorator="['pdApplyDetailTable['+index+'].applyNum', {'initialValue':item.applyNum,rules:validatorRules.applyNum}]"/>
                  </a-form-item>
                </td>
                <td>{{item.stockNum}}</td>
              </tr>
            </table>
          </div>
          <a-col :span="12" v-show="disableSubmit">
            <a-form-item   label="审核意见" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input  :disabled="disableSubmit"  v-decorator="[ 'refuseReason', validatorRules.refuseReason]"  style="width: 100%;height: 80px"/>
            </a-form-item>
          </a-col>
        </a-row>
        <pd-apply-detail-add-modal  ref="PdApplyDetailAddModal" @ok="modalFormOk"></pd-apply-detail-add-modal>
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
  import { httpAction,getAction,downFile,inArray} from '@/api/manage'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import JDate from '@/components/jeecg/JDate'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import PdApplyDetailAddModal from './PdChooseProductListModel'
  export default {
    name: 'PdApplyOrderModal',
    mixins: [JEditableTableMixin],
    components: {JDate, JDictSelectTag,PdApplyDetailAddModal},
    data() {
      return {
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
          refuseReason:{},
          applyNum:[
            {required: true,message: '请输入值'}]
        },
        refKeys: ['pdApplyDetail', ],
        tableKeys:['pdApplyDetail', ],
        // 申领单详细表
        pdApplyDetailTable: {
          dataSource: []
        },
        url: {
          add: "/pd/pdApplyOrder/add",
          edit: "/pd/pdApplyOrder/edit",
          exportXlsUrl: "/pd/pdApplyOrder/exportXls",
          pdPurchaseDetail: {
            list: '/pd/pdApplyOrder/queryApplyDetail'
          },
        }
      }
    },
    methods: {
      add () { //初始化新增
        this.pdApplyDetailTable.dataSource = [];
        this.edit({});
        this.applyInfo();
      },
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
      applyInfo() { //新增页面初始化
        getAction("/pd/pdApplyOrder/applyInfo",{}).then((res)=>{
          if (res.success) {
            let model={};
            this.model.applyNo=res.result.applyNo;//申领编号
            this.model.deptName=res.result.deptName;//申领科室名称
            this.model.deptId=res.result.deptId;//申领科室id
            this.model.applyDate=res.result.applyDate;//申领日期
            this.model.totalNum=res.result.totalNum;//申领总数量
            this.model.applyBy=res.result.applyBy;//申领人编号
            this.model.realName=res.result.realName;//申领人姓名
            this.model.submitStatus=res.result.submitStatus;//提交状态
            this.model.auditStatus=res.result.auditStatus;//审核状态
            this.$nextTick(() => {
              this.form.setFieldsValue(pick(this.model,'applyNo','deptName','totalNum','applyDate','realName','remarks'))
            })
          }
        })
      },
      //修改申购数量后重新计算总数量及总金额
      handleConfirmBlur(e,m){
        const that = this;
        let applyNum=e.value;//修改后的申领数量
        let stockNum=m.stockNum;//目前库存数量
        if(parseFloat(stockNum)<parseFloat(applyNum)){
          that.$message.error("库存数量小于申领数量");
          return;
        }
        m.applyNum=e.value;
        let tableData=this.pdApplyDetailTable.dataSource;
        let totalNum=0;
        for(let i=0;i<tableData.length;i++){
          totalNum+=parseFloat(tableData[i].applyNum);//计算总数量
        }
        let model={};
        this.model.totalNum=totalNum;//申购总数量
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'totalNum'))
        })
      },

      //选择产品
      choice() {
        this.$refs.PdApplyDetailAddModal.show();
        this.$refs.PdApplyDetailAddModal.title = "选择产品";
      },

      deleteDetail(productId){
        const newData = this.pdApplyDetailTable.dataSource.filter(item => item.productId !== productId);
        let totalNum=0;
        for(let i=0;i<newData.length;i++){
          totalNum+=parseFloat(newData[i].applyNum);//计算总数量
        }
        let model={};
        this.model.totalNum=totalNum;//申购总数量
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'totalNum'))
        })
        this.pdApplyDetailTable.dataSource = newData;
      },

      modalFormOk (formData) {//选择产品确定后返回所选择的数据
        let idList = [];
        let values = [];
        let totalNum=0;
        let data= this.pdApplyDetailTable.dataSource;
        for(let j=0;j<data.length;j++) {
          totalNum+=data[j].applyNum;
          let prodId=data[j].productId;
          idList.push(prodId);
        }
        values=data;
        for(let i=0;i<formData.length;i++){
          let prodId=formData[i].productId;
          if(inArray(prodId, idList) ==-1) {
            values.push({
              productId: formData[i].productId,
              number: formData[i].number,
              productName: formData[i].productName,
              spec: formData[i].spec,
              version: formData[i].version,
              unitName: formData[i].unitName,
              applyNum: 1,//默认1
              stockNum: formData[i].stockNum
            })
            totalNum+=1;//计算总数量
          }
        }
        let model={};
        this.model.totalNum=totalNum;//申领总数量
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'totalNum'))
        })
        this.pdApplyDetailTable.dataSource = values;
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
            let pdApplyDetailList=this.pdApplyDetailTable.dataSource;
            values.pdApplyDetailList=pdApplyDetailList;
            if(pdApplyDetailList.length>0){
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
        let fieldval = pick(this.model,'applyNo','deptName','totalNum','applyDate','realName','remarks','refuseReason')
        this.$nextTick(() => {
          this.form.setFieldsValue(fieldval)
        })
        // 加载子表数据
        if (this.model.id) {
          let params = { applyNo: this.model.applyNo }
          this.requestSubTableData(this.url.pdPurchaseDetail.list, params, this.pdApplyDetailTable)
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
        this.form.setFieldsValue(pick(row,'applyNo','deptName','totalNum','applyDate','realname','remarks','refuseReason'))
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
    padding: 1px 16px;
    font-weight: 500;
    box-sizing: border-box;
  }

  .tableStyle> tr > td >input{
    width:45px;
    text-align: center;
  }
</style>

