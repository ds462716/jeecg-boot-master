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
        <a-card style="margin-bottom: 10px;">
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
          <a-col :span="12">
            <a-form-item label="调拨总数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number disabled="disabled" v-decorator="[ 'totalNum', validatorRules.totalNum]" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input disabled="disabled" v-decorator="[ 'remarks', validatorRules.remarks]"  style="width: 100%;height: 60px"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
          </a-card>
      <!-- 子表单区域 -->
        <a-card style="margin-bottom: 10px;">
      <a-tabs v-model="activeKey" @change="handleChangeTabs">
        <a-tab-pane tab="调拨明细表" :key="refKeys[0]" :forceRender="true">
          <div style="margin-bottom: 8px;">
            <!--<a-button type="primary" icon="download" @click="exportXls('调拨产品列表')">导出</a-button>-->
          </div>
          <j-editable-table
            bordered
            :ref="refKeys[0]"
            :loading="pdAllocationDetailTable.loading"
            :columns="pdAllocationDetailTable.columns"
            :dataSource="pdAllocationDetailTable.dataSource"
            :maxHeight="500"
            :rowNumber="true"
            :rowSelection="true"
            :disabled="disableSubmit"
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
                <a-form-item label="审核意见" :labelCol="labelCol" :wrapperCol="wrapperCol" style="text-align: left">
                  <a-textarea :disabled="disableSubmit" v-decorator="[ 'rejectReason', validatorRules.rejectReason]" placeholder="请输入审核意见"></a-textarea>
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
  import { FormTypes,getRefPromise,validateFormAndTables } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import JDate from '@/components/jeecg/JDate'
  import { httpAction,getAction,downFile,inArray} from '@/api/manage'
  import PdApplyStockRecordOutModal from './PdStockRecordOutModal'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"


  const VALIDATE_NO_PASSED = Symbol()
  export { FormTypes, VALIDATE_NO_PASSED }
  export default {
    name: 'PdAllocationRecordModal',
    mixins: [JEditableTableMixin],
    components: {
      JDate,PdApplyStockRecordOutModal,JDictSelectTag
    },
    data() {
      return {
        labelCol: {
          span: 6
        },
        wrapperCol: {
          span: 16
        },
        labelCol2: {
          span: 3
        },
        wrapperCol2: {
          span: 20
        },
        model:{},
        title: '这里是标题',
        lockScroll: false,
        fullscreen: true,
        switchFullscreen: false,
        disableSubmit:false,
        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 1,
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
        // 调拨明细表
        pdAllocationDetailTable: {
          loading: false,
          dataSource: [],
          columns: [
            { title: '定数包名称', width:"130px",   key: 'packageName' },
            { title: '定数包编号', width:"130px",   key: 'packageId' },
            { title: '产品ID', key: 'productId', type: FormTypes.hidden },
            { title: '产品名称', width:"250px",  key: 'productName' },
            { title: '产品编号',width:"200px",  key: 'number' },
            { title: '规格',width:"240px", key: 'spec' },
            { title: '型号', width:"240px", key: 'version' },
            { title: '单位',width:"50px",  key: 'unitName' },
            { title: '发货数量', width:"100px", key: 'arrivalNum' },
            {title: '调拨数量',  width:"100px",key: 'allocationNum'},
            { title: '库存数量', key: 'stockNum' },
          ]
        },
        url: {
          edit: "/pd/pdAllocationRecord/edit",
          exportXlsUrl: "/pd/pdAllocationRecord/exportXls",
          pdAllocationDetail: {
            list: '/pd/pdAllocationRecord/queryPdAllocationDetailList'
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
      handleOk (type) { //审核提交
        this.model.auditStatus='2';//审核通过
        if(type=="no"){
          this.model.auditStatus='3';//拒绝
          this.model.submitStatus='1';//待提交
        }
        this.form.validateFields((err, values) => {
          if(type=="no"){
            if(values.rejectReason==null || values.rejectReason==''){
              this.$message.warning("请填写审核意见")
              return
            }
          }
          this.model.rejectReason= values.rejectReason;
          if (!err) {
            const that = this;
            let pdAllocationDetailList = this.pdAllocationDetailTable.dataSource;
            let values = [];
            values.pdAllocationDetailList = pdAllocationDetailList;
            let formData = Object.assign(this.model, values);
            httpAction(this.url.edit, formData, 'put').then((res) => {
              if (res.success) {
                if(type=="yes"){
                  let args = {};
                  args.outType = "3";  //  1-申领出库; 2-科室出库; 3-调拨出库
                  args.data = pdAllocationDetailList;  // 申领单或调拨单明细 按选择器传值就行
                  args.inDepartId = this.model.inDeptId; // 入库部门ID
                  this.$refs.stockForm.add(args);
                  this.$refs.stockForm.title = "新增出库";
                  this.$refs.stockForm.disableSubmit = false;
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

      getAllTable() {
        let values = this.tableKeys.map(key => getRefPromise(this, key))
        return Promise.all(values)
      },
      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
        let fieldval = pick(this.model,'allocationNo','allocationDate','totalNum','outDeptName','inDeptName','realName','remarks','rejectReason')
        this.$nextTick(() => {
          this.form.setFieldsValue(fieldval)
        })
        // 加载子表数据
        if (this.model.id) {
          let params = { allocationNo: this.model.allocationNo }
          this.requestSubTableData(this.url.pdAllocationDetail.list, params, this.pdAllocationDetailTable)
        }
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)

        return {
          ...main, // 展开
          pdAllocationDetailList: allValues.tablesValue[0].values,
        }
      },
      validateError(msg){
        this.$message.error(msg)
      },
     popupCallback(row){
       this.form.setFieldsValue(pick(row,'allocationNo','allocationDate','totalNum','outDeptName','inDeptName','realName','remarks','rejectReason'))
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