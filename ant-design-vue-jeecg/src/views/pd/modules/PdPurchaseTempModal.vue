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
            <a-form-item label="模板编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input     disabled="disabled"   v-decorator="[ 'tempNo', validatorRules.tempNo]"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="模板名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input     :disabled="disableSubmit"   v-decorator="[ 'tempName', validatorRules.tempName]" placeholder="请输入模板名称"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="创建人员" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input disabled="disabled" v-decorator="[ 'realname', validatorRules.realname]"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="创建日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date  disabled="disabled" v-decorator="[ 'createTime', validatorRules.createTime]" :trigger-change="true" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="创建科室" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input disabled="disabled" v-decorator="[ 'departName', validatorRules.departName]" placeholder="请输入库房名称"></a-input>
            </a-form-item>
          </a-col>
           <a-col :span="12">
               <a-form-item label="模板类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                 <j-dict-select-tag-expand  :disabled="disableSubmit"   v-decorator="['tempType',validatorRules.tempType]"   dictCode="temp_type" :trigger-change="true"  placeholder="请选择模板类型"/>
                </a-form-item>
          </a-col>
        </a-row>
        <pd-purchase-detail-add-modal  ref="PdPurchaseDetailAddModal" @ok="modalFormOk"></pd-purchase-detail-add-modal>
      </a-form>
        </a-card>
        <a-card style="margin-bottom: 10px;">

          <a-tabs v-model="activeKey" @change="handleChangeTabs">
            <a-tab-pane tab="产品明细表" :key="refKeys[0]" :forceRender="true">
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
                :loading="pdPurchaseTempDetailTable.loading"
                :columns="pdPurchaseTempDetailTable.columns"
                :dataSource="pdPurchaseTempDetailTable.dataSource"
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

              </a-row>
            </a-tab-pane>
          </a-tabs>
        </a-card>
      </div>
    </a-spin>
    <template slot="footer">
      <a-button @click="closeBtn" style="margin-right: 15px;" v-show="disableSubmit">关  闭</a-button>
      <a-popconfirm title="确定放弃编辑？" @confirm="handleCancel" v-show="!disableSubmit" okText="确定" cancelText="取消">
        <a-button style="margin-right: 15px;">取  消</a-button>
      </a-popconfirm>
      <a-button @click="handleOk()" v-show="!disableSubmit" type="primary" :loading="confirmLoading" style="margin-right: 15px;">保存</a-button>
     </template>
  </j-modal>
</template>
<script>

  import pick from 'lodash.pick'
  import { httpAction,getAction,downFile,inArray } from '@/api/manage'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import JDate from '@/components/jeecg/JDate'
  import { FormTypes,getRefPromise,validateFormAndTables } from '@/utils/JEditableTableUtil'
  import PdPurchaseDetailAddModal from './PdChooseProductListModel'
  import JDictSelectTagExpand from "@/components/dict/JDictSelectTagExpand"
  export default {
    name: 'PdPurchaseTempModal',
    mixins: [JEditableTableMixin],
    components: {JDate,PdPurchaseDetailAddModal,JDictSelectTagExpand},
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
          tempNo:{},
           totalNum:{},
           refuseReason:{},
           tempType: {rules: [
              {required: true, message: '请选择模板类型'},
            ]},
          tempName: {rules: [
              {required: true, message: '请输入模板名称'},
            ]},
        },
        refKeys: ['pdPurchaseTempDetail', ],
        tableKeys:['pdPurchaseTempDetail', ],
        activeKey: 'pdPurchaseTempDetail',
        // 申购单详细表
        pdPurchaseTempDetailTable: {
          loading: false,
          dataSource: [],
          columns: [
            { title: '产品ID', key: 'productId', type: FormTypes.hidden },
            { title: '产品编号',width:"200px",   key: 'number' },
            { title: '产品名称', width:"250px",  key: 'productName' },
            { title: '规格',width:"240px",   key: 'spec' },
            { title: '单位',width:"50px",  key: 'unitName' },
            { title: '中标号', width:"100px", key: 'bidingNumber' },
            {title: '产品数量', key: 'orderNum', type: FormTypes.input, width:"100px",
              placeholder: '${title}', defaultValue: '1',
              validateRules: [{ required: true, message: '${title}不能为空' },
                { pattern: '^(?:[1-9][0-9]*(?:\\.[0-9]+)?|0\\.(?!0+$)[0-9]+)$',message: '${title}的格式不正确' }]
            },
            { title: '供应商', width:"250px",  key: 'supplierName' },
            { title: '供应商Id',   key: 'supplierId',type: FormTypes.hidden  },
            { title: '生产厂家', width:"250px",  key: 'venderName' },
          ]
        },
        url: {
          add: "/pd/pdPurchaseTemp/add",
          edit: "/pd/pdPurchaseTemp/edit",
          exportXlsUrl: "/pd/pdPurchaseTemp/exportXls",
          pdPurchaseTempDetail: {
            list: '/pd/pdPurchaseTemp/queryPdPurchaseTempDetail'
          },
        },
      }
    },
    methods: {
      add () {//初始化新增
        this.pdPurchaseTempDetailTable.dataSource = [];
        this.edit({});
        this.purchaseInfo();
      },
      /* 导出 */
       exportXls(fileName){
        if(!fileName || typeof fileName != "string"){
          fileName = "导出文件"
        }
        if(this.pdPurchaseTempDetailTable.dataSource.length>0){
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
        getAction("/pd/pdPurchaseTemp/purchaseInfo",{}).then((res)=>{
          if (res.success) {
              this.model = res.result;
            this.$nextTick(() => {
              this.form.setFieldsValue(pick(this.model,'tempNo','tempName','departName','createTime','realname','tempType'))
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
              target.setValues([{rowKey: row.id, values: {orderNum :row.orderNum}}]);
              // 计算总数量
              this.getTotalNum([]);
            }
          }
        }
      },
      // 计算总数量
      getTotalNum(rows){
        this.$nextTick(() => {
          if (rows.length <= 0) {
            let {values} = this.$refs.pdPurchaseTempDetail.getValuesSync({validate: false});
            rows = values;
          }
            let totalNum = 0;
             rows.forEach((item, idx) => {
              totalNum += parseFloat(item.orderNum);
            })
            this.model.totalNum = totalNum;

        });
      },

      //选择产品
      choice() {
        this.$refs.PdPurchaseDetailAddModal
          .show({stockDepartId:this.model.departId});
        this.$refs.PdPurchaseDetailAddModal.title = "选择产品";
      },

      handleConfirmDelete() {
        if(this.$refs.pdPurchaseTempDetail.selectedRowIds.length > 0){
          this.$refs.pdPurchaseTempDetail.removeSelectedRows();
          this.$nextTick(() => {
            // 计算总数量
            this.getTotalNum([]);
          })
        }else{
          this.$message.error("请选择需要删除的数据！")
        }
      },
      modalFormOk (formData) {//选择产品确定后返回所选择的数据
        let data = [];
        this.$refs.pdPurchaseTempDetail.getValues((error, values) => {
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
            this.pdPurchaseTempDetailTable.dataSource = values;
            this.addrows(item);
          })
          this.$nextTick(() => {
            // 计算总数量
            this.getTotalNum(values);
          })
        })
      },

      addrows(row) {
        let data = {
          productId: row.productId,
          number: row.number,
          productName: row.productName,
          spec:row.spec,
          bidingNumber:row.bidingNumber,
          purchasePrice: row.purchasePrice,
          orderNum: 1.00,//默认1
          version: row.version,
          unitName:row.unitName,
          venderName:row.venderName,
          supplierId:row.supplierId,
          supplierName:row.supplierName
        }
        this.pdPurchaseTempDetailTable.dataSource.push(data)
      },
      /** 关闭按钮 **/
      closeBtn(){
        this.visible = false;
        this.$emit('close');
        this.disableSubmit = false;
      },

      handleOk () { //保存
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
          let pdPurchaseTempDetailList = formData.pdPurchaseTempDetailList;
          if (pdPurchaseTempDetailList.length > 0) {
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
        let fieldval = pick(this.model,'tempNo','tempName','departName','createTime','realname','tempType' )
        this.$nextTick(() => {
          this.form.setFieldsValue(fieldval)
        })
        // 加载子表数据
        if (this.model.id) {
          let params = { tempNo: this.model.tempNo }
          this.requestSubTableData(this.url.pdPurchaseTempDetail.list, params, this.pdPurchaseTempDetailTable)
        }
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)

        return {
          ...main, // 展开
          pdPurchaseTempDetailList: allValues.tablesValue[0].values,
        }
      },
      validateError(msg){
        this.$message.error(msg)
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'tempNo','tempName','departName','createTime','realname','tempType'))
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

