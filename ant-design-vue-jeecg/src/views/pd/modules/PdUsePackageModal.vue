<template>
  <j-modal
    :visible="visible"
    :width="popModal.width"
    :maskClosable="false"
    :title="popModal.title"
    :lockScroll="popModal.lockScroll"
    :fullscreen="popModal.fullscreen"
    :switchFullscreen="popModal.switchFullscreen"
    @cancel="handleCancel"
  >
    <a-spin :spinning="confirmLoading">
      <!-- 主表单区域 -->
      <a-form :form="form">
        <a-row>

          <a-col :span="12">
            <a-form-item label="检验项目编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input :disabled="disableSubmit" v-decorator="[ 'code', validatorRules.code]" autocomplete="off"  placeholder="请输入检验项目编号"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="检验项目名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input :disabled="disableSubmit" v-decorator="[ 'name', validatorRules.name]"  autocomplete="off" @change="pinyinTran" placeholder="请输入检验项目名称"></a-input>
            </a-form-item>
          </a-col>
          <a-col  :span="12">
            <a-form-item label="科室" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-select
                mode="multiple"
                showSearch
                :departId="departValue"
                :defaultActiveFirstOption="false"
                :allowClear="true"
                :showArrow="true"
                :filterOption="false"
                @search="departHandleSearch"
                @focus="departHandleSearch"
                :notFoundContent="notFoundContent"
                v-decorator="[ 'departIdList', validatorRules.departIdList]"
                placeholder="请选择科室"
              >
                <a-select-option v-for="d in departData" :key="d.id">{{d.departName}}</a-select-option>
              </a-select>

            </a-form-item>
          </a-col>
          <a-col :lg="12">
            <a-form-item label="扣减类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
             <a-select     v-decorator="[ 'deductuinType', validatorRules.deductuinType]" placeholder="请选择扣减类型">
                <a-select-option value="0">自动扣减</a-select-option>
                <a-select-option value="1">人工扣减</a-select-option>
                <a-select-option value="2">无需扣减</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="拼音简码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input :disabled="disableSubmit" v-decorator="[ 'py', validatorRules.py]" autocomplete="off" placeholder="请输入拼音简码"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="五笔简码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input :disabled="disableSubmit" v-decorator="[ 'wb', validatorRules.wb]" autocomplete="off" placeholder="请输入五笔简码"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="自定义码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input :disabled="disableSubmit" v-decorator="[ 'zdy', validatorRules.zdy]" autocomplete="off" placeholder="请输入自定义码"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input :disabled="disableSubmit" v-decorator="[ 'remarks', validatorRules.remarks]" autocomplete="off" placeholder="请输入备注"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>

      <!-- 子表单区域 -->
      <a-tabs  v-model="activeKey" @change="handleChangeTabs">
        <a-tab-pane tab="检验用量明细" :key="refKeys[0]" :forceRender="true">
          <div style="margin-bottom: 8px;">
            <a-button v-show="!disableSubmit" type="primary" icon="plus" @click="handleConfirmAdd">新增</a-button>
            <span style="padding-left: 8px;"></span>
              <a-popconfirm
                :title="`确定要删除吗?`"
                @confirm="handleConfirmDelete">
                <a-button v-show="!disableSubmit" type="primary" icon="minus">删除</a-button>
                <span class="gap"></span>
              </a-popconfirm>
          </div>
          <j-editable-table
            bordered
            :ref="refKeys[0]"
            :loading="pdUsePackageDetailTable.loading"
            :columns="pdUsePackageDetailTable.columns"
            :dataSource="pdUsePackageDetailTable.dataSource"
            :maxHeight="500"
            :rowNumber="true"
            :rowSelection="true"
            :actionButton="false"
            :disabled="disableSubmit"
            @valueChange="valueChange"
            @change="usePackagedandleChange"
            style="text-overflow: ellipsis;"
          />
          <!--<a-row style="margin-top:10px;text-align: right;padding-right: 5%">
            <span style="font-weight: bold;font-size: large;padding-right: 5%">总数量：{{ totalSum }}</span>
          </a-row>-->
        </a-tab-pane>
      </a-tabs>

    </a-spin>

    <template slot="footer">
      <a-button @click="close" style="margin-right: 15px;" v-show="disableSubmit">关  闭</a-button>
      <a-popconfirm title="确定放弃编辑？" @confirm="handleCancel" v-show="!disableSubmit" okText="确定" cancelText="取消">
        <a-button style="margin-right: 15px;">取  消</a-button>
      </a-popconfirm>
      <a-button @click="handleOk" v-show="!disableSubmit" type="primary" :loading="confirmLoading" style="margin-right: 15px;">提  交</a-button>
    </template>

    <pd-choose-product-list-model  ref="pdChooseProductListModel" @ok="returnData" ></pd-choose-product-list-model>
  </j-modal>
</template>

<script>

  import pick from 'lodash.pick'
  import { FormTypes,getRefPromise,validateFormAndTables,VALIDATE_NO_PASSED } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import { makeWb } from '@/utils/wubi'
  import {httpAction, deleteAction, getAction} from '@/api/manage'
  import PdChooseProductListModel from "./PdChooseProductListModel"
  import {queryPdDepartTreeList} from '@/api/api'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'

  export default {
    name: 'PdUsePackageModal',
    mixins: [JEditableTableMixin],
    components: {
      PdChooseProductListModel
    },
    data() {
      return {
        totalSum:'0',
        departData: [],
        departValue: undefined,
        notFoundContent:"未找到内容",
        treeData:[],
        validateStatus:"",
        disableSubmit:false,
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
        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 0,
        validatorRules: {
          code: { rules: [{ required: true, message: '请输入检验项目编号!' }] },
          name: { rules: [{ required: true, message: '请输入检验项目名称!' }] },
          departIdList: { rules: [{ required: true, message: '请选择科室!' }] },
          sum:{},
          py:{},
          wb:{},
          zdy:{},
          remarks:{},
          deductuinType:{rules: []}
        },
        refKeys: ['pdUsePackageDetail', ],
        tableKeys:['pdUsePackageDetail', ],
        activeKey: 'pdUsePackageDetail',
        id:0,
        // sum:0,
        // 检验项目明细
        pdUsePackageDetailTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '产品ID',
              align:"center",
              key: 'productId',
              type: FormTypes.hidden
            },
            {
              title: '产品编号',
              align:"center",
              width:"200px",
              key: 'productNumber'
            },
            {
              title: '产品名称',
              align:"center",
              width:"300px",
              key: 'productName'
            },
            {
              title: '规格',
              align:"center",
              width:"200px",
              key: 'spec'
            },
            {
              title: '单位',
              align:"center",
              width:"50px",
              key: 'unitName'
            },
            {
              title: '规格单位',
              align:"center",
              width:"50px",
              key: 'specUnitName'
            },
            {
              title: '规格数量',
              align:"center",
              width:"50px",
              key: 'specQuantity'
            },
            {
              title: '生产厂家',
              align:"center",
              width:"350px",
              key: 'venderName'
            },
            {
              title: '供应商',
              align:"center",
              key: 'supplierName'
            },
            {
              title: '产品类型',
              align:"center",
              width:"50px",
              key: 'productFlagName'
            },
            {
              title: '产品类型',
              align:"center",
              key: 'productFlag',
              type: FormTypes.hidden
            },
            {
              title: '用量',
              key: 'count',
              type: FormTypes.input,
              width:"80px",
              placeholder: '请输入${title}',
              defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' },{pattern: '^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){1,4})?$',message: '${title}的格式不正确' }]
            },

            { title: '使用类型',
              key: 'useType',
              type: FormTypes.select,
              width:"150px",
              options: [],
              allowSearch:true,
              placeholder: '${title}'
            },

          ]
        },
        url: {
          queryDepart: "/pd/pdDepart/queryListTree",
          add: "/pd/pdUsePackage/add",
          init: "/pd/pdUsePackage/initModal",
          edit: "/pd/pdUsePackage/edit",
          pdUsePackageDetail: {
            list: '/pd/pdUsePackage/queryPdUsePackageDetailByMainId'
          },
        },
        popModal: {
          title: '检验项目管理',
          visible: false,
          width: '100%',
          // width: '1200',
          style: { top: '20px' },
          switchFullscreen: true,  //缩放按钮
          lockScroll: false,
          fullscreen: true,
        },
      }
    },
    mounted() {
      this.$nextTick(function () {

      })
    },
    methods: {
      getAllTable() {
        let values = this.tableKeys.map(key => getRefPromise(this, key))
        return Promise.all(values)
      },
      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
        this.confirmLoading = true;
        this.usePackagedandleChange();
        this.departHandleSearch();
        //this.loadTree();
        let fieldval = pick(this.model,'code','name','py','wb','zdy','remarks','deductuinType');
        this.$nextTick(() => {
          this.form.setFieldsValue(fieldval);
          this.totalSum = this.model.sum;
          // 加载子表数据
          if (this.model.id) {
            let params = { id: this.model.id }
            this.requestSubTableData(this.url.pdUsePackageDetail.list, params, this.pdUsePackageDetailTable)
            let testDepartId=this.model.testDepartId;
            if(testDepartId != null && testDepartId != ""){
              let departIds = this.model.testDepartId.split(",");
              this.form.setFieldsValue({departIdList:departIds});
              }
          }else{
            /*getAction(this.url.init, {id:""}).then((res) => {
              if (res.success) {
                this.$nextTick(() => {
                  this.form.setFieldsValue({code:res.result.code});
                })
              }
            })*/
          }
          setTimeout(() => {
            this.confirmLoading = false
          }, 500)
        })
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)

        return {
          ...main, // 展开
          pdUsePackageDetailList: allValues.tablesValue[0].values,
        }
      },
      validateError(msg){
        this.$message.error(msg)
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'code','name','py','wb','zdy','remarks','deductuinType'))
      },
      pinyinTran(e){
        let val = e.target.value;
        let pinyin = require('js-pinyin');
        pinyin.setOptions({checkPolyphone: false, charCase: 0});
        let py = pinyin.getCamelChars(val);//获取简码
        this.form.setFieldsValue({py:py});
        let wb = makeWb(val);
        this.form.setFieldsValue({wb:wb});//获取五笔简码
      },
      /** 关闭按钮点击事件 */
      handleCancel() {
        this.visible = false
        this.totalSum = '0';
        this.eachAllTable((item) => {
          item.initialize()
        })
        this.$emit('close')
        this.pdUsePackageDetailTable.dataSource = [];
      },
      /** 确定按钮点击事件 */
      handleOk() {
        /** 触发表单验证 */
        this.getAllTable().then(tables => {
          /** 一次性验证主表和所有的次表 */
          return validateFormAndTables(this.form, tables)
        }).then(allValues => {
          if (typeof this.classifyIntoFormData !== 'function') {
            throw this.throwNotFunction('classifyIntoFormData')
          }
          let formData = this.classifyIntoFormData(allValues)
          /*if(formData.pdUsePackageDetailList.length <= 0){
            this.$message.warning("检验项目产品数据为空，请选择产品！");
            return;
          }*/

          let { values } = this.$refs.pdUsePackageDetail.getValuesSync({ validate: false });
          for(let row of values) {
            if (row.specQuantity && Number(row.count) > Number(row.specQuantity)) {
              this.$message.error("[" + row.productName + "]试剂用量不能大于规格数量！");
              return;
          }
            if(row.count <= 0){
              this.$message.error("产品["+row.productName+"]数量必须大于0！");
              return;
            }
            if(row.useType=='' || row.useType==null){
              this.$message.error("产品["+row.productName+"]请选择使用类型！");
              return;
            }
          }

          // 发起请求
          formData.sum = this.totalSum;
          formData.testDepartId=this.model.departIdList.join(",");
          return this.request(formData)
        }).catch(e => {
          if (e.error === VALIDATE_NO_PASSED) {
            // 如果有未通过表单验证的子表，就自动跳转到它所在的tab
            this.activeKey = e.index == null ? this.activeKey : this.refKeys[e.index]
          } else {
            console.error(e)
          }
        })
      },
      //删除行
      handleConfirmDelete() {
        this.$refs.pdUsePackageDetail.removeSelectedRows();
        this.$nextTick(() => {
          this.valueChange();
        })
      },
      // 新增行
      handleConfirmAdd() {
        this.$refs.pdChooseProductListModel.show({productFlag:"1"});//只查詢试剂耗材
      },
      // 产品数量变更
      valueChange(event) {
        if(event){
          const { type, row, column, value, target } = event;
          if (type === FormTypes.select) {

          }else if(type === FormTypes.input){
            if(column.key === "count"){
              let { values } = target.getValuesSync({ validate: false });
              let sum = 0;
              for(let item of values){
                sum = sum + Number(item.count);
                if(item.id == row.id&&item.specQuantity && Number(value) > Number(item.specQuantity)) {
                  this.$message.error("[" + row.productName + "]试剂用量不能大于规格数量！");
                  target.setValues([{rowKey: row.id, values: {count: item.specQuantity }}])
                  return;
                }
              }
              this.totalSum = sum;
            }
          }
        }
        // this.$refs.pdUsePackageDetail.getValues((error, values) => {
        //   let sum = 0;
        //   for(let item of values){
        //     if(item.id == row.id && Number(value) > Number(item.stockNum)){
        //       this.$message.error("["+row.productName+"]使用数量不能大于库存数量！");
        //     }
        //     sum = sum + Number(item.count);
        //   }
        //   this.totalSum = sum;
        // })
      },
      //弹出框返回调用
      returnData(formData) {
        this.$refs.pdUsePackageDetail.getValues((error, values) => {
          this.pdUsePackageDetailTable.dataSource = values;
          if(values.length > 0){
            formData.forEach((item, idx) => {
              let bool = true;
              values.forEach((value, idx) => {
                if (value.productId == item.productId){
                  bool = false;
                }
              })
              if(bool){
                this.addrows(item);
              }
            })
          }else{
            formData.forEach((item, idx) => {
              this.addrows(item);
            })
          }
          this.$nextTick(() => {
            this.valueChange();
          })
        })
      },
      addrows(row){
        let data = {
          productId: row.productId,
          productNumber: row.productNumber,
          productName: row.productName,
          spec: row.spec,
          specUnitName: row.specUnitName,
          specQuantity: row.specQuantity,
          unitName: row.unitName,
          venderName: row.venderName,
          supplierName: row.supplierName,
          productFlag:row.productFlag,
          productFlagName:row.productFlag==0?"耗材":"试剂",
          count: 1,
          options:this.options
        }
        this.pdUsePackageDetailTable.dataSource.push(data);
        this.$refs.pdUsePackageDetail.add();
      },


      loadTree(){
        let that = this;
        queryPdDepartTreeList().then((res)=>{
          if(res.success){
            that.treeData = [];
            let treeList = res.result.treeList
            for(let a=0;a<treeList.length;a++){
              let temp = treeList[a];
              temp.isLeaf = temp.leaf;
              that.treeData.push(temp);
            }
          }
        });
      },
      usePackagedandleChange(){ //试剂使用类型
        initDictOptions('use_type').then((res) => {
          if (res.success) {
            this.pdUsePackageDetailTable.columns.forEach((item, idx) => {
              if(item.key === "useType"){
                item.options = res.result;
              }
            })
          }
        })
      },
      //科室查询start
      departHandleSearch(value) {
        getAction(this.url.queryDepart,{departName:value}).then((res)=>{
          if (!res.success) {
            this.cmsFailed(res.message);
          }
          this.departData = res.result;
        })
      },
      //科室查询end
    }
  }
</script>

<style scoped>
</style>