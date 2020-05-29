<template>
  <j-modal
    :visible="visible"
    :width="popModal.width"
    :maskClosable="disableSubmit"
    :title="popModal.title"
    :lockScroll="popModal.lockScroll"
    :fullscreen="popModal.fullscreen"
    :switchFullscreen="popModal.switchFullscreen"
    @cancel="handleCancel"
  >

    <a-spin :spinning="confirmLoading">
      <!-- 主表单区域 -->
      <div style="background:#ECECEC; padding:20px">
        <a-card title="" style="margin-bottom: 10px;">
          <a-form :form="form">
            <a-row>
              <a-col :md="6" :sm="8">
                <a-form-item label="退货编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input disabled v-decorator="[ 'rejectedNo', {}]" placeholder="请输入退货编号"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="供应商" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-select
                    showSearch
                    placeholder="请选择供应商"
                    :disabled="disableSubmit"
                    :supplierId="supplierValue"
                    :defaultActiveFirstOption="false"
                    :showArrow="true"
                    :allowClear="true"
                    :filterOption="false"
                    @search="supplierHandleSearch"
                    @change="supplierHandleChange"
                    @focus="supplierHandleSearch"
                    :notFoundContent="notFoundContent"
                    v-decorator="[ 'supplierId', validatorRules.supplierId]"
                  >
                    <a-select-option v-for="d in supplierData" :key="d.value">{{d.text}}</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-card>

        <!-- 产品列表区域 -->
        <a-card style="margin-bottom: 10px;">
          <a-tabs v-model="activeKey" @change="handleChangeTabs">
            <a-tab-pane tab="产品明细" :key="refKeys[0]" :forceRender="true">
              <a-form v-show="!disableSubmit">
                <a-row>
                  <a-col :md="6" :sm="8">
                    <a-form-item label="唯一码编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                      <a-input ref="productNumberInput" v-focus placeholder="请输入唯一码编号" v-model="queryParam.productNumber" @keyup.enter.native="searchQuery(0)"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :md="12" :sm="8">
                    <a-form-item label="" :labelCol="labelCol" :wrapperCol="wrapperCol" style="text-align: left;padding-left: 15px;">
                      提示：按<span style="color: red">Ctrl+Alt</span>键快速定位到扫码输入框
                    </a-form-item>
                  </a-col>
                </a-row>
              </a-form>

              <div style="margin-bottom: 8px;" v-show="!disableSubmit">
                <a-popconfirm style="margin-left: 8px"
                              :title="`确定要删除吗?`"
                              @confirm="handleConfirmDelete">
                  <a-button type="primary" icon="minus">删除</a-button>
                  <span class="gap"></span>
                </a-popconfirm>
              </div>

              <j-editable-table
                bordered
                :ref="refKeys[0]"
                :loading="loading"
                :columns="pdRejectedDetailTable.columns"
                :dataSource="pdRejectedDetailTable.dataSource"
                :maxHeight="500"
                :rowNumber="true"
                :rowSelection="true"
                :actionButton="false"
                :disabled="disableSubmit"
                style="text-overflow: ellipsis;"
              >
                <!--:maxHeight 大于 600 后就会有BUG 一次性选择9条以上产品，会少显示一条-->
              </j-editable-table>
              <a-row style="margin-top:10px;text-align: right;padding-right: 5%">
                <span style="font-weight: bold;font-size: large;padding-right: 5%">总数量：{{ totalSum }}</span>
              </a-row>
            </a-tab-pane>
          </a-tabs>
        </a-card>

        <a-card style="">
          <a-form :form="form">
            <a-row>
              <a-col :span="12">
                <a-form-item label="备注" :labelCol="labelCol2" :wrapperCol="wrapperCol2" style="text-align: left">
                  <a-textarea :disabled="disableSubmit" v-decorator="[ 'remarks', validatorRules.remarks]" placeholder="请输入备注"></a-textarea>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-card>
      </div>
    </a-spin>

    <template slot="footer">
      <a-button @click="closeBtn" style="margin-right: 15px;" v-show="disableSubmit">关  闭</a-button>
      <a-popconfirm title="确定放弃编辑？" @confirm="handleCancel" v-show="!disableSubmit" okText="确定" cancelText="取消">
        <a-button style="margin-right: 15px;">取  消</a-button>
      </a-popconfirm>
      <!--<a-button @click="saveBtn" v-show="!disableSubmit" type="primary" :loading="confirmLoading" style="margin-right: 15px;">保存草稿</a-button>-->
      <a-button @click="submitBtn" v-show="!disableSubmit" type="primary" :loading="confirmLoading" style="margin-right: 15px;">提  交</a-button>
    </template>

  </j-modal>
</template>

<script>

  import Vue from 'vue'
  import pick from 'lodash.pick'
  import { FormTypes,getRefPromise,validateFormAndTables } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import JDate from '@/components/jeecg/JDate'
  import {httpAction, deleteAction, getAction} from '@/api/manage'
  import JDictSelectTagExpand from "@/components/dict/JDictSelectTagExpand"
  import ATextarea from "ant-design-vue/es/input/TextArea";
  import {uniqueScanCode} from '@/utils/barcode'

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

  export default {
    name: 'PdRejectedModal',
    mixins: [JEditableTableMixin],
    components: {
      ATextarea,
      JDate,
      JDictSelectTagExpand
    },
    data() {
      return {
        labelCol: {span: 6},
        wrapperCol: {span: 16},

        labelCol2: {span: 3},
        wrapperCol2: {span: 20},

        disableSubmit:false,
        title: '这里是标题',
        loading:false,
        initData:{},
        queryParam:{},

        //供应商下拉列表 start
        supplierValue: undefined,
        notFoundContent:"未找到内容",
        supplierData: [],
        //供应商下拉列表 end

        totalSum:'0',
        args:{},
        //货区货位二级联动下拉框
        goodsAllocationList:[],
        huoquOptions:[],
        huoweiOptions:[],

        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 0,
        validatorRules: {
          supplierId:{rules: [{required: true, message: '请选择供应商!'}]},
        },
        refKeys: ['pdRejectedDetail',],
        tableKeys:['pdRejectedDetail', ],
        activeKey: 'pdRejectedDetail',

        pdRejectedDetailTable: {
          loading: false,
          dataSource: [],
          columns: [
            { title: '库存明细ID', key: 'productStockId', type: FormTypes.hidden },
            { title: '产品ID', key: 'productId', type: FormTypes.hidden },
            { title: '唯一码', key: 'refBarCode', type: FormTypes.normal, width:"250px" },
            { title: '产品名称', key: 'productName', type: FormTypes.normal, width:"250px" },
            { title: '产品编号', key: 'productNumber', width:"200px" },
            { title: '产品条码', key: 'productBarCode', type: FormTypes.hidden},
            { title: '规格', key: 'spec', width:"180px" },
            { title: '批号', key: 'batchNo' },
            { title: '单位', key: 'unitName', width:"80px" },
            { title: '有效期', key: 'expDate' },
            {
              title: '退货数量', key: 'rejectedCount', type: FormTypes.normal, width:"100px",
              placeholder: '${title}', defaultValue: '1',
              validateRules: [{ required: true, message: '${title}不能为空' },
                              { pattern: '^-?\\d+\\.?\\d*$',message: '${title}的格式不正确' }]
            },
            { title: '库存数量', key: 'stockNum' ,type: FormTypes.hidden},
            { title: '货位', key: 'huoweiName'},
            { title: '货位编号', key: 'huoweiCode', type: FormTypes.hidden },
          ]
        },
        url: {
          init:"/pd/pdRejected/initModal",
          submit: "/pd/pdRejected/uniqueSubmit",
          add: "/pd/pdRejected/add",
          edit: "/pd/pdRejected/edit",
          querySupplier:"/pd/pdSupplier/getSupplierList",
          huoweiList:"/pd/pdGoodsAllocation/getOptions",

        },
        popModal: {
          title: '这里是标题',
          visible: false,
          // width: '100%',
          width: '1200',
          style: { top: '20px' },
          switchFullscreen: false,  //缩放按钮
          lockScroll: false,
          fullscreen: true,
        },
      }
    },
    methods: {
      // 重写close方法
      close() {
        this.visible = false;
        this.totalSum = '0';
        this.pdRejectedDetailTable.dataSource = [];
        this.queryParam = {};
        this.eachAllTable((item) => {
          item.initialize()
        })
        this.$emit('close')
      },
      getAllTable() {
        let values = this.tableKeys.map(key => getRefPromise(this, key));
        return Promise.all(values)
      },
      add() {
        this.edit({})
      },
      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
        this.loadData();
      },
      loadData() {
        this.loading = true;
        let params = {};
        if(this.model.id){
          this.popModal.title="退货明细";
          //初始化供应商，用于回显供应商
          this.supplierHandleSearch();

          let fieldval = pick(this.model,'rejectedNo','supplierId','remarks');
          this.$nextTick(() => {
            this.form.setFieldsValue(fieldval);
          })
          params = { id: this.model.id }
        }else{
          this.popModal.title="新增退货";
          params = { id: "" }
        }
        getAction(this.url.init, params).then((res) => {
          if (res.success) {
            this.$nextTick(() => {
              if(this.model.id){ //详情页
                this.pdRejectedDetailTable.dataSource = res.result.pdRejectedDetailList || [];
              }else{  // 新增页
                this.form.setFieldsValue({rejectedNo:res.result.rejectedNo}); // 退货单号
              }
            })
          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
          this.loading = false;
        })
      },
      /** 关闭按钮 **/
      closeBtn(){
        this.visible = false;
        this.$emit('close');
      },
      /** 保存草稿 **/
      saveBtn() {

      },
      /** 确定按钮点击事件 */
      submitBtn() {
        /** 触发表单验证 */
        this.getAllTable().then(tables => {
          /** 一次性验证主表和所有的次表 */
          return validateFormAndTables(this.form, tables)
        }).then(allValues => {
          if (typeof this.classifyIntoFormData !== 'function') {
            throw this.throwNotFunction('classifyIntoFormData')
          }

          let formData = this.classifyIntoFormData(allValues);

          if(formData.pdRejectedDetailList.length <= 0){
            this.$message.warning("退货产品数据为空，请扫码退货或选择产品");
            return;
          }

          let list = formData.pdRejectedDetailList;
          for (let item of list){
            if(Number(item.rejectedCount) > Number(item.stockNum)){
              this.$message.error("["+item.productName+"]退货数量不能大于库存数量！");
              return;
            }
            if(Number(item.rejectedCount) <= 0){
              this.$message.error("["+item.productName+"]退货数量必须大于0！");
              return;
            }
          }
          return this.request(formData);
        }).catch(e => {
          if (e.error === VALIDATE_NO_PASSED) {
            // 如果有未通过表单验证的子表，就自动跳转到它所在的tab
            this.activeKey = e.index == null ? this.activeKey : this.refKeys[e.index]
          } else {
            console.error(e)
          }
        })
      },
      // 保存 提交 修改 请求函数
      request(formData) {
        let url = this.url.submit, method = 'post';
        if (this.model.id) {
          url = this.url.edit;
          method = 'put'
        }
        this.confirmLoading = true;
        httpAction(url, formData, method).then((res) => {
          if (res.success) {
            this.$message.success(res.message);
            this.$emit('ok');
            this.close()
          } else {
            this.$message.warning(res.message)
          }
        }).finally(() => {
          this.confirmLoading = false
        })
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue);

        return {
          ...main, // 展开
          pdRejectedDetailList: allValues.tablesValue[0].values,
        }
      },
      validateError(msg){
        this.$message.error(msg)
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'rejectedNo','supplierId','remarks'))
      },
      //删除行
      handleConfirmDelete() {
        if(this.$refs.pdRejectedDetail.selectedRowIds.length > 0){
          this.$refs.pdRejectedDetail.removeSelectedRows();
          this.getTotalNumAndPrice([]);
        }else{
          this.$message.error("请选择需要删除的数据！")
        }
      },

      // 点“选择产品”按钮后 调用 新增一行
      addrows(row){
        let { values } = this.$refs.pdRejectedDetail.getValuesSync({ validate: false });
        //判断是否已经存在唯一码值 存在不新加
        let flag = true;
        if(values.length > 0){
          values.forEach((value, idx) => {
            if(row.refBarCode == value.refBarCode){
              this.$message.error("该唯一码已存在！");
              flag = false;
            }
          });
        }
        if(!flag){
          return ;
        }
        let data = {
          productStockId:row.id,
          refBarCode:row.refBarCode,
          productId: row.productId,
          productName: row.productName,
          productNumber:row.number,
          productBarCode:row.productBarCode,
          spec: row.spec,
          batchNo:row.batchNo,
          unitName:row.unitName,
          expDate:row.expDate,
          rejectedCount: 1,
          stockNum:row.stockNum,
          huoweiName:row.huoweiName,
          huoweiCode:row.huoweiCode
        };
        this.pdRejectedDetailTable.dataSource.push(data);
      },
      // 计算总数量和总价格
      getTotalNumAndPrice(rows){
        this.$nextTick(() => {
          if (rows.length <= 0) {
            let {values} = this.$refs.pdRejectedDetail.getValuesSync({validate: false});
            rows = values;
          }
          let totalSum = 0;
          rows.forEach((item, idx) => {
            totalSum = totalSum + Number(item.rejectedCount);
          })
          this.totalSum = totalSum;
        })
      },
      //清空扫码框
      clearQueryParam(){
        this.queryParam = {};
        this.$refs.productNumberInput.focus();
      },

      // 扫码查询
      searchQuery(num) {
        let productNumber = this.queryParam.productNumber;
        if(!productNumber){
          //清空扫码框
          this.clearQueryParam();
          this.$message.error("请输入唯一码编号！");
          return;
        }
        if(num == 0){       //产品编号扫码
          //解析条码
          uniqueScanCode(productNumber).then((res) => {
            if(res.code == "200" || res.code == "203"){
              let result = res.result;
              if(!result){
                //清空扫码框
                this.clearQueryParam();
                this.$message.error("条码解析失败，请校验条码是否正确！");
                return;
              }
              let supplierId = this.form.getFieldValue("supplierId")
              if(!supplierId){
                if(this.pdRejectedDetailTable.dataSource.length>0){
                  this.$message.error("请选择供应商！");
                }else{
                  //供应商
                  const supplierData = [];
                  supplierData.push({
                    value: result.supplierId,
                    text: result.supplierName,
                  });
                  this.supplierData = supplierData;
                  this.form.setFieldsValue({supplierId:result.supplierId});
                }
              }else{
                if(result.supplierId != supplierId){
                  //清空扫码框
                  this.clearQueryParam();
                  this.$message.error("当前科室中没有该产品！");
                  return;
                }
              }
              let { values } = this.$refs.pdRejectedDetail.getValuesSync({ validate: false });
              //解决删除后存在重复数据bug
              this.pdRejectedDetailTable.dataSource = values;
              this.addrows(result);
              this.getTotalNumAndPrice(values);
            }else if(res.code ==="201"){
              this.$message.error(res.message);
            }else{
              this.$message.error(res.message);
            }
            //清空扫码框
            this.clearQueryParam();
          })
        }
      },

      //-----------------供应商查询start
      supplierHandleSearch(value) {
        this.getSupplierList(value);
      },
      supplierHandleChange(value) {
        this.totalSum = '0';
        this.eachAllTable((item) => {
          item.initialize()
        });
        this.pdRejectedDetailTable.dataSource = [];
        this.supplierValue = value;
        this.getSupplierList(value);
      },
      getSupplierList(value){
        getAction(this.url.querySupplier,{name:value}).then((res)=>{
          if (!res.success) {
            this.cmsFailed(res.message);
          }
          const result = res.result;
          const data = [];
          result.forEach(r => {
            data.push({
              value: r.id,
              text: r.name,
            });
          });
          this.supplierData = data;
        })
      },
      //----------------供应商查询end

    },
  }

</script>

<style scoped>
  .drawer-bootom-button {
    width: 100%;
    text-align: right;
    background: #fff;
    margin-top:10px;
  }
</style>