<template>
  <!--<a-modal-->
    <!--:title="title"-->
    <!--:width="1600"-->
    <!--:visible="visible"-->
    <!--:maskClosable="false"-->
    <!--:confirmLoading="confirmLoading"-->
    <!--@ok="handleOk"-->
    <!--@cancel="handleCancel">-->
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
              <a-input v-decorator="[ 'code', validatorRules.code]" disabled="disabled" placeholder="请输入定数包编号"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="定数包名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'name', validatorRules.name]"  @change="pinyinTran" placeholder="请输入定数包名称"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="产品总数" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="[ 'sum', validatorRules.sum]" placeholder="0" disabled="disabled" style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="拼音简码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'py', validatorRules.py]" placeholder="请输入拼音简码"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="五笔简码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'wb', validatorRules.wb]" placeholder="请输入五笔简码"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="自定义码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'zdy', validatorRules.zdy]" placeholder="请输入自定义码"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="[ 'remarks', validatorRules.remarks]" placeholder="请输入备注"></a-input>
            </a-form-item>
          </a-col>
          <!--<a-col :span="12">-->
            <!--<a-form-item label="父机构" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
              <!--<a-input v-decorator="[ 'departParentId', validatorRules.departParentId]" placeholder="请输入父机构"></a-input>-->
            <!--</a-form-item>-->
          <!--</a-col>-->

        </a-row>
      </a-form>

      <!-- 子表单区域 -->
      <a-tabs v-model="activeKey" @change="handleChangeTabs">
        <a-tab-pane tab="定数包明细" :key="refKeys[0]" :forceRender="true">
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
            :loading="pdPackageDetailTable.loading"
            :columns="pdPackageDetailTable.columns"
            :dataSource="pdPackageDetailTable.dataSource"
            :maxHeight="380"
            :rowNumber="true"
            :rowSelection="true"
            :actionButton="false"
            @valueChange="valueChange"
          />
        </a-tab-pane>

        <!--  如果要用默认的新增、删除按钮，设置 :actionButton="true"
            @deleted="valueChange"   删除行事件
            @added="valueChange"     新增行之后处理的事件 但是只能取到之前行的值，取不到本次新增行的值
            @valueChange="valueChange"  更改值事件
            -->
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
  import { FormTypes,getRefPromise } from '@/utils/JEditableTableUtil'
  import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
  import { makeWb } from '@/utils/wubi'
  import {httpAction, deleteAction, getAction} from '@/api/manage'
  import PdChooseProductListModel from "./PdChooseProductListModel"

  export default {
    name: 'PdPackageModal',
    mixins: [JEditableTableMixin],
    components: {
      PdChooseProductListModel
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
        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 0,
        validatorRules: {
          code: { rules: [{ required: true, message: '请输入定数包编号!' }] },
          name: { rules: [{ required: true, message: '请输入定数包名称!' }] },
          sum:{},
          py:{},
          wb:{},
          zdy:{},
          remarks:{}
        },
        refKeys: ['pdPackageDetail', ],
        tableKeys:['pdPackageDetail', ],
        activeKey: 'pdPackageDetail',
        id:0,
        // sum:0,
        // 定数包明细
        pdPackageDetailTable: {
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
            // {
            //   title: '备注',
            //   key: 'remarks',
            //   type: FormTypes.input,
            //   width:"200px",
            //   placeholder: '请输入${title}',
            //   defaultValue: '',
            // },
          ]
        },
        url: {
          add: "/pd/pdPackage/add",
          init: "/pd/pdPackage/initModal",
          edit: "/pd/pdPackage/edit",
          pdPackageDetail: {
            list: '/pd/pdPackage/queryPdPackageDetailByMainId'
          },
        },
        popModal: {
          title: '定数包管理',
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
        let fieldval = pick(this.model,'code','name','sum','py','wb','zdy','remarks');
        this.form.setFieldsValue(fieldval);
        // 加载子表数据
        if (this.model.id) {
          let params = { id: this.model.id }
          this.requestSubTableData(this.url.pdPackageDetail.list, params, this.pdPackageDetailTable)
        }else{
          getAction(this.url.init, {id:""}).then((res) => {
            if (res.success) {
              this.$nextTick(() => {
                this.form.setFieldsValue({code:res.result.code});
              })
            }
          })
        }
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)

        return {
          ...main, // 展开
          pdPackageDetailList: allValues.tablesValue[0].values,
        }
      },
      validateError(msg){
        this.$message.error(msg)
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'code','name','sum','py','wb','zdy','remarks'))
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
        this.eachAllTable((item) => {
          item.initialize()
        })
        this.$emit('close')
        this.pdPackageDetailTable.dataSource = [];
      },
      //删除行
      handleConfirmDelete() {
        this.$refs.pdPackageDetail.removeSelectedRows();
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
        this.$refs.pdPackageDetail.getValues((error, values) => {
          // 错误数 = 0 则代表验证通过
          // if (error === 0) {
            // this.$message.success('验证通过')
            // 将通过后的数组提交到后台或自行进行其他处理
            let sum = 0;
            values.forEach((item, idx) => {
              sum = sum + Number(item.count);
            })
            this.form.setFieldsValue({sum:sum});
          // } else {
          //   this.$message.error('验证未通过')
          // }
        })
      },
      //弹出框返回调用
      returnData(formData) {
        this.$refs.pdPackageDetail.getValues((error, values) => {
          this.pdPackageDetailTable.dataSource = values;
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
          count: 1
        }
        this.pdPackageDetailTable.dataSource.push(data);
        this.$refs.pdPackageDetail.add();
      }
    }
  }
</script>

<style scoped>
</style>