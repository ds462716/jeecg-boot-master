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
            <a-form-item label="定数包编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'code', validatorRules.code]" autocomplete="off" disabled="disabled" placeholder="请输入定数包编号"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="使用包名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'name', validatorRules.name]"  autocomplete="off" @change="pinyinTran" placeholder="请输入使用包名称"></a-input>
            </a-form-item>
          </a-col>
          <!--<a-col :span="12">-->
            <!--<a-form-item label="产品总数" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
              <!--<a-input-number v-decorator="[ 'sum', validatorRules.sum]" placeholder="0" disabled="disabled" style="width: 100%"/>-->
            <!--</a-form-item>-->
          <!--</a-col>-->
          <a-col :span="12">
            <a-form-item label="拼音简码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'py', validatorRules.py]" autocomplete="off" placeholder="请输入拼音简码"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="五笔简码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'wb', validatorRules.wb]" autocomplete="off" placeholder="请输入五笔简码"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="自定义码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'zdy', validatorRules.zdy]" autocomplete="off" placeholder="请输入自定义码"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'remarks', validatorRules.remarks]" autocomplete="off" placeholder="请输入备注"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>

      <!-- 子表单区域 -->
      <a-tabs v-model="activeKey" @change="handleChangeTabs">
        <a-tab-pane tab="使用包明细" :key="refKeys[0]" :forceRender="true">
          <div style="margin-bottom: 8px;">
            <a-button type="primary" icon="plus" @click="handleConfirmAdd">新增</a-button>
            <span style="padding-left: 8px;"></span>
              <a-popconfirm
                :title="`确定要删除吗?`"
                @confirm="handleConfirmDelete">
                <a-button type="primary" icon="minus">删除</a-button>
                <span class="gap"></span>
              </a-popconfirm>
          </div>

          <j-editable-table
            :ref="refKeys[0]"
            :loading="pdUsePackageDetailTable.loading"
            :columns="pdUsePackageDetailTable.columns"
            :dataSource="pdUsePackageDetailTable.dataSource"
            :maxHeight="380"
            :rowNumber="true"
            :rowSelection="true"
            :actionButton="false"
            @valueChange="valueChange"
          />
          <a-row style="margin-top:10px;text-align: right;padding-right: 5%">
            <span style="font-weight: bold;font-size: large;padding-right: 5%">总数量：{{ totalSum }}</span>
          </a-row>
        </a-tab-pane>
      </a-tabs>

    </a-spin>

    <template slot="footer">
      <a-popconfirm title="确定放弃编辑？" @confirm="handleCancel" okText="确定" cancelText="取消">
        <a-button style="margin-right: 15px;">取  消</a-button>
      </a-popconfirm>
      <a-button @click="handleOk" type="primary" :loading="confirmLoading" style="margin-right: 15px;">保  存</a-button>
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

  export default {
    name: 'PdUsePackageModal',
    mixins: [JEditableTableMixin],
    components: {
      PdChooseProductListModel
    },
    data() {
      return {
        totalSum:'0',
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
          code: { rules: [{ required: true, message: '请输入使用包编号!' }] },
          name: { rules: [{ required: true, message: '请输入使用包名称!' }] },
          sum:{},
          py:{},
          wb:{},
          zdy:{},
          remarks:{}
        },
        refKeys: ['pdUsePackageDetail', ],
        tableKeys:['pdUsePackageDetail', ],
        activeKey: 'pdUsePackageDetail',
        id:0,
        // sum:0,
        // 使用包明细
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
              title: '产品名称',
              align:"center",
              key: 'productName'
            },
            {
              title: '规格',
              align:"center",
              key: 'spec'

            },
            {
              title: '单位',
              align:"center",
              key: 'unitName'
            },
            {
              title: '使用单位',
              align:"center",
              key: 'specUnitName'
            },
            {
              title: '规格数量',
              align:"center",
              key: 'specQuantity'
            },
            {
              title: '生产厂家',
              align:"center",
              key: 'venderName'
            },
            {
              title: '供应商',
              align:"center",
              key: 'supplierName'
            },
            {
              title: '产品数量',
              key: 'count',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue: '',
              validateRules: [{ required: true, message: '${title}不能为空' },{pattern: '^-?\\d+$',message: '${title}的格式不正确' }]
            },
          ]
        },
        url: {
          add: "/pd/pdUsePackage/add",
          init: "/pd/pdUsePackage/initModal",
          edit: "/pd/pdUsePackage/edit",
          pdUsePackageDetail: {
            list: '/pd/pdUsePackage/queryPdUsePackageDetailByMainId'
          },
        },
        popModal: {
          title: '使用包管理',
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
        let fieldval = pick(this.model,'code','name','py','wb','zdy','remarks');
        this.$nextTick(() => {
          this.form.setFieldsValue(fieldval);
          this.totalSum = this.model.sum;
          // 加载子表数据
          if (this.model.id) {
            let params = { id: this.model.id }
            this.requestSubTableData(this.url.pdUsePackageDetail.list, params, this.pdUsePackageDetailTable)
          }else{
            getAction(this.url.init, {id:""}).then((res) => {
              if (res.success) {
                this.$nextTick(() => {
                  this.form.setFieldsValue({code:res.result.code});
                })
              }
            })
          }

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
        this.form.setFieldsValue(pick(row,'code','name','py','wb','zdy','remarks'))
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
          if(formData.pdUsePackageDetailList.length <= 0){
            this.$message.warning("使用包产品数据为空，请选择产品！");
            return;
          }

          let { values } = this.$refs.pdUsePackageDetail.getValuesSync({ validate: false });
          for(let row of values){
            if(row.count <= 0){
              this.$message.error("产品["+row.productName+"]数量必须大于0！");
              return;
            }
          }

          // 发起请求
          formData.sum = this.totalSum;
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
        this.$refs.pdChooseProductListModel.show();
      },
      // 产品数量变更
      valueChange(e) {
        this.$refs.pdUsePackageDetail.getValues((error, values) => {
          let sum = 0;
          values.forEach((item, idx) => {
            sum = sum + Number(item.count);
          })
          this.totalSum = sum;
        })
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
          productName: row.productName,
          spec: row.spec,
          unitName: row.unitName,
          venderName: row.venderName,
          supplierName: row.supplierName,
          count: 0
        }
        this.pdUsePackageDetailTable.dataSource.push(data);
        this.$refs.pdUsePackageDetail.add();
      }
    }
  }
</script>

<style scoped>
</style>