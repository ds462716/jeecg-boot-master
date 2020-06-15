<template>
  <a-modal
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
        <!-- 产品列表区域 -->
        <a-card style="margin-bottom: 10px;">
             <a-tab-pane tab="" :key="refKeys[0]"  :forceRender="true">
              <a-form v-show="!disableSubmit">
                <a-row>
                   <a-col :md="6" :sm="8"  v-show="this.model.bottleType==2">
                    <a-form-item label="闭瓶原因" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <j-dict-select-tag-expand v-model="queryParam.closeRemarks" dictCode="close_remarks"/>
                     </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="8"  v-show="this.model.bottleType==1 || queryParam.closeRemarks==2">
                    <a-form-item label="选择仪器" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <a-select
                        showSearch
                        :instrCode="instrCodeValue"
                        :defaultActiveFirstOption="false"
                        :allowClear="true"
                        :showArrow="true"
                        :filterOption="false"
                        v-model="queryParam.instrCode"
                        @search="instrHandleSearch"
                        @change="instrHandleChange"
                        @focus="instrHandleSearch"
                        :notFoundContent="notFoundContent"
                        v-decorator="[ 'instrCode', validatorRules.instrCode]"
                      >
                        <a-select-option v-for="d in instrData" :key="d.value">{{d.text}}</a-select-option>
                      </a-select>
                    </a-form-item>
                  </a-col>
                  <a-col :md="12" :sm="8">
                    <a-form-item label="唯一码编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <a-input ref="productBarCodeInput" v-focus placeholder="请输入唯一码编号" v-model="queryParam.productBarCode" @keyup.enter.native="searchQuery()"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :md="12" :sm="8">
                    <a-form-item label="" :labelCol="labelCol" :wrapperCol="wrapperCol" style="text-align: left;padding-left: 15px;">
                      提示：按<span style="color: red">Ctrl+Alt</span>键快速定位到扫码输入框
                    </a-form-item>
                  </a-col>
                </a-row>
              </a-form>
              <a-row style="margin-top:10px;text-align: right;padding-right: 5%">
                </a-row>
            </a-tab-pane>
         </a-card>
      </div>
    </a-spin>

    <template slot="footer">
      <a-button @click="closeBtn" style="margin-right: 15px;" v-show="disableSubmit">关  闭</a-button>
     </template>
   </a-modal>
</template>

<script>

  import Vue from 'vue'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import { FormTypes,getRefPromise,validateFormAndTables } from '@/utils/JEditableTableUtil'
   import {openingQuotation,closeQuotation} from '@/utils/barcode'
   import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import JDictSelectTagExpand from "@/components/dict/JDictSelectTagExpand"
  import { getAction } from  '@/api/manage'

   const VALIDATE_NO_PASSED = Symbol()
  export { FormTypes, VALIDATE_NO_PASSED }

  // 自定义焦点事件
  Vue.directive('focus', {
    // 当被绑定的元素插入到 DOM 中时……
    inserted: function (el) {
      //全局监听键盘事件
      document.onkeydown = function(event) {
        if(event.ctrlKey && event.altKey) {
          // 按ctrl+alt  聚焦元素
          el.focus()
        }
      }
    }
  })

  let timeout;
  let currentValue;

  function fetch(value, callback,url) {
    if (timeout) {
      clearTimeout(timeout);
      timeout = null;
    }
    currentValue = value;

    function fake() {
      getAction(url,{instrName:value}).then((res)=>{
        if (!res.success) {
          this.cmsFailed(res.message);
        }
        if (currentValue === value) {
          const result = res.result;
          const data = [];
          result.forEach(r => {
            data.push({
              value: r.instrCode,
              text: r.instrName,
            });
          });
          callback(data);
        }
      })
    }
    timeout = setTimeout(fake, 0);
  }

  export default {
    name: "PdBottleModal",
    mixins: [JEditableTableMixin],
    components: {
      JDictSelectTagExpand
     },
    data () {
      return {
        width:1200,
        visible: false,
        model: {},
        title: '这里是标题',
        notFoundContent:"未找到内容",
        lockScroll: false,
        fullscreen: true,
        switchFullscreen: false,
        instrData: [],
        instrCodeValue: undefined,
        hyCharged: true,
        totalSum:'0',
        totalPrice:'0.0000',
        activeKey: 'PdBottleModal',
        refKeys: ['PdBottleModal',],
        //货区货位二级联动下拉框
        goodsAllocationList:[],
        queryParam:{},
        initData:{},
        pdBottleTable: {
          loading: false,
          dataSource: [],
          columns: []
        },
        disableSubmit:false,
        labelCol: {
          xs: { span: 16 },
          sm: { span: 8 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        labelCol2: {
          xs: { span: 16 },
          sm: { span: 4 },
        },
        wrapperCol2: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        validatorRules: {
          remarks: {rules: []},
          departId: {rules: []},
          departParentId: {rules: []},
          closeRemarks:{rules: []},
          instrCode: {rules: [
              {required: true, message: '请选择仪器设备!'},
            ]},
        },
        url: {
          submit: "/pd/pdBottleInf/submitPdBottleInf",
          queryExLabInstrInf:"/ex/exLabInstrInf/getExLabInstrInf",
        }
      }
    },
    created () {
    },
    methods: {
      setdataCss(record,index) {
        return "validity1";
      },
      add () {
        this.edit({});
      },

      edit (bottleType) {
        this.model.bottleType = bottleType;
        this.queryParam = {};
        this.form.resetFields();
        this.visible = true;
      },


      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {

      },


      close () {
        this.$emit('close');
        this.visible = false;
        this.eachAllTable((item) => {
          item.initialize()
        })
      },
      handleCancel () {
        this.$emit('ok', "");
        this.close()
      },


      handleOk () {
        //this.$emit('ok', "");
        //this.close();

      },

      // 扫码查询
      searchQuery() {
        let that = this;
         //条码扫码
          let productBarCode = this.queryParam.productBarCode;
          if(!productBarCode){
            this.$message.error("请输入二级条码！");
            return;
          }

          if(this.model.bottleType=='1'){ //扫码开瓶
            let instrCode = this.queryParam.instrCode;
            if(!instrCode){
              this.$message.error("请选择仪器设备！");
              return;
            }
            this.openingQuot(productBarCode,instrCode);
          }else{  //闭瓶扫码
            let closeRemarks = this.queryParam.closeRemarks;
            let instrCode ="";
            if(!closeRemarks){
              this.$message.error("请选择闭瓶原因！");
              //清空扫码框
              this.clearQueryParam();
              return;
            }else if(closeRemarks=='2'){
                instrCode = this.queryParam.instrCode;
              if(!instrCode){
                this.$message.error("请选择试剂需要迁移的仪器！");
                return;
              }
            }
            this.openingClose(productBarCode,closeRemarks,instrCode);
          }
      },

     //解析条码(开瓶)
      openingQuot(productBarCode,instrCode){
        let that = this;
      openingQuotation(productBarCode,instrCode).then((res) => {
      if(res.code ==200){
        that.$message.success("开瓶完成");
      }else{
        that.$message.error(res.message);
      }
      //清空扫码框
      this.clearQueryParam();
    })
      },
      //解析条码(闭瓶)
      openingClose(productBarCode,closeRemarks,instrCode){
        let that = this;
        closeQuotation(productBarCode,closeRemarks,instrCode).then((res) => {
          if(res.code ==200){
            that.$message.success("闭瓶完成");
          }else{
            that.$message.error(res.message);
          }
          //清空扫码框
          this.clearQueryParam();
        })
      },

      //清空扫码框
      clearQueryParam(){
        this.queryParam.productNumber = "";
        this.queryParam.productBarCode = "";
        this.$refs.productNumberInput.focus();
      },



      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)

        return {
          ...main, // 展开
          pdProductStockList: allValues.tablesValue[0].values,
        }
      },
      /** 关闭按钮 **/
      closeBtn(){
        this.visible = false;
        this.$emit('close');
      },
      //组别查询start
      instrHandleSearch(value) {
        fetch(value, data => (this.instrData = data),this.url.queryExLabInstrInf);
      },
      instrHandleChange(value) {
        this.instrCodeValue = value;
        fetch(value, data => (this.instrData = data),this.url.queryExLabInstrInf);
      },
      //组别查询end
    }
  }
</script>
<style scoped>
  .validity0{
    border:1px solid #ccc;
  }
  .validity1{
    border:2px solid #FF3333;
  }

  .validity2{
    border:2px solid #FFFFCC;
  }
</style>