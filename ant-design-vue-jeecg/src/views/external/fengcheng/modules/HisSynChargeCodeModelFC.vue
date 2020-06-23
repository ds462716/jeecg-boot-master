<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    :maskClosable=disableSubmit
    cancelText="关闭"
  >
    <a-spin :spinning="confirmLoading">
      <div style="background:#ECECEC; padding:5px">
        <a-form :form="form">
          <a-card style="margin-bottom: 5px">
            <a-row :gutter="24">
              <a-col :md="8" :sm="8" v-show="false">
                <a-form-item label="id" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input autocomplete="off" disabled v-decorator="[ 'id' ]"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="8">
                <a-form-item label="产品编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input autocomplete="off" disabled v-decorator="[ 'number' ]"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="8">
                <a-form-item label="产品名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input autocomplete="off" disabled v-decorator="[ 'name' ]"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="8">
                <a-form-item label="生产厂家" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input autocomplete="off" disabled v-decorator="[ 'venderName' ]"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="8">
                <a-form-item label="单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input autocomplete="off" disabled v-decorator="[ 'unitName' ]"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="8">
                <a-form-item label="规格" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input autocomplete="off" disabled v-decorator="[ 'spec' ]"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="8">
                <a-form-item label="创建日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input autocomplete="off" disabled v-decorator="[ 'createTime' ]"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="8">
                <a-form-item label="进阶" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input autocomplete="off" disabled v-decorator="[ 'purchasePrice' ]"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="8">
                <a-form-item label="出价" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input autocomplete="off" disabled v-decorator="[ 'sellingPrice' ]"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="8">
                <a-form-item label="是否计费" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <!--<a-input autocomplete="off" disabled v-decorator="[ 'sellingPrice' ]"></a-input>-->
                  <a-select v-decorator="[ 'isCharge' ]" placeholder="请选择是否计费">
                    <a-select-option value="0">是</a-select-option>
                    <a-select-option value="1">否</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
            </a-row>
          </a-card>
          <a-card>
            <div class="table-page-search-wrapper">
              <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">
                  <a-col :md="6" :sm="8">
                    <a-form-item label="收费代码">
                      <a-input autocomplete="off" v-model="queryParam.fsfXmbh"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="8">
                    <a-form-item label="项目名称">
                      <a-input autocomplete="off" v-model="queryParam.fsfXmmc"></a-input>
                    </a-form-item>
                  </a-col>
                  <a-col :md="6" :sm="8" >
                    <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                      <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                      <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
                    </span>
                  </a-col>
                </a-row>
              </a-form>
            </div>
            <a-row :gutter="24"> <!-- :pagination="false" -->
              <a-table
                ref="table"
                size="middle"
                bordered
                rowKey="id"
                :columns="columns"
                :dataSource="dataSource"
                :pagination="ipagination"
                :loading="loading"
                :customRow="onClickRow"
                :rowSelection="{fixed:false,type:'radio',selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
                @change="handleTableChange">
              </a-table>
            </a-row>
          </a-card>
        </a-form>
      </div>
    </a-spin>
  </a-modal>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import {httpAction, getAction} from '@/api/manage'
  import pick from 'lodash.pick'
  import {validateDuplicateValue} from '@/utils/util'
  import {makeWb} from '@/utils/wubi'
  import {duplicateCheckHasDelFlag} from '@/api/api'
  import { filterObj } from '@/utils/util';

  export default {
    name: "HisSynChargeCodeModelFC",
    mixins:[JeecgListMixin],
    components: {},
    data() {
      return {
        form: this.$form.createForm(this),
        title: "价格对照",
        width: 1200,
        visible: false,
        disableSubmit: false,
        model: {},
        labelCol: {
          xs: {span: 24},
          sm: {span: 6},
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 18},
        },
        confirmLoading: false,
        validatorRules: {
        },
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'HIS收费代码',
            align:"center",
            width:120,
            dataIndex: 'fsfXmbh'
          },
          {
            title:'HIS产品名称',
            align:"center",
            dataIndex: 'fsfXmmc'
          },
          {
            title:'HIS规格',
            align:"center",
            dataIndex: 'fsfSpec'
          },
          {
            title:'HIS价格',
            align:"center",
            dataIndex: 'fsfJe'
          },
          {
            title:'输入码',
            align:"center",
            dataIndex: 'py'
          },
        ],
        url: {
          list:"/pd/hisChargeFCRMYY/hisProductList",
          edit: "/pd/pdProduct/edit",
        }
      }
    },
    created() {
    },
    methods: {
      add() {
        this.edit({});
      },
      edit(record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.unitId = record.id;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'id', 'number', 'name', 'venderName', 'unitName', 'spec', 'createTime', 'purchasePrice', 'sellingPrice', 'isCharge'))
        })
      },
      close() {
        this.selectedRowKeys = [];
        this.selectionRows = [];
        this.$emit('close');
        this.visible = false;
      },
      handleOk() {
        // this.confirmLoading = true;
        if(this.selectionRows.length != 1){
          this.$message.warning("请选择一条数据！");
          return;
        }
        let formData = {};
        formData.id = this.form.getFieldValue("id");
        formData.isCharge = this.form.getFieldValue("isCharge");
        formData.chargeCode = this.selectionRows[0].fsfXmbh;
        console.log("表单提交数据", formData)
        httpAction(this.url.edit, formData, "put").then((res) => {
          if (res.success) {
            this.$message.success(res.message);
            this.$emit('ok');
            this.close();
          } else {
            this.$message.warning(res.message);
          }
        }).finally(() => {
          this.confirmLoading = false;
        })
      },
      handleCancel() {
        this.close()
      },
      popupCallback(row) {
        this.form.setFieldsValue(pick(row, 'id', 'number', 'name', 'venderName', 'unitName', 'spec', 'createTime', 'purchasePrice', 'sellingPrice', 'isCharge'))
      },
      /**
       * 点击行选中checkbox
       * @param record
       * @returns {{on: {click: on.click}}}
       */
      onClickRow(record) {
        return {
          on: {
            click: (e) => {
              //点击操作那一行不选中表格的checkbox
              let pathArray = e.path;
              //获取当前点击的是第几列
              let td = pathArray[0];
              let cellIndex = td.cellIndex;
              //获取tr
              let tr = pathArray[1];
              //获取一共多少列
              let lie = tr.childElementCount;
              if(lie && cellIndex){
                if(parseInt(lie)-parseInt(cellIndex) > 0){
                  //操作那一行
                  let recordId = record.id;
                  let index = this.selectedRowKeys.indexOf(recordId);
                  this.selectedRowKeys = [];
                  this.selectionRows = [];
                  if(index>=0){
                    this.selectedRowKeys.splice(index, 1);
                    this.selectionRows.splice(index, 1);
                  }else{
                    this.selectedRowKeys.push(recordId);
                    this.selectionRows.push(record);
                  }
                }
              }
            }
          }
        }
      },
    }
  }
</script>
<style scoped>
</style>