<template>
  <j-modal
    :visible="visible"
    :width="popModal.width"
    :title="popModal.title"
    :lockScroll="popModal.lockScroll"
    :fullscreen="popModal.fullscreen"
    :switchFullscreen="popModal.switchFullscreen"
    @cancel="handleCancel"
  >
    <a-spin :spinning="confirmLoading">
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>
      <!-- 查询区域-END -->
      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="productId"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{type:'radio',selectedRowKeys: selectedRowKeys,  onChange: onSelectChange}"
         >
      </a-table>
    </a-spin>
    <template slot="footer">
      <a-button @click="handleCancel" style="margin-right: 15px;">取  消</a-button>
      <a-button @click="handleOk" type="primary" style="margin-right: 15px;">确定</a-button>
    </template>
  </j-modal>
</template>

<script>

  import { httpAction,getAction } from '@/api/manage'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { FormTypes } from '@/utils/JEditableTableUtil'
  import JDictSelectTagExpand from "@/components/dict/JDictSelectTagExpand"

  export default {
    name: "PdChooseDosageListModel",
    mixins:[JeecgListMixin],
    components: {
      JDictSelectTagExpand
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"选择病人信息",
        width:1200,
        visible: false,
        confirmLoading: false,
        selectedRowKeys: [],
        selectedRows: [],
        dataSource:[],
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: 'id',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'病人姓名',
            align:"center",
            dataIndex: 'patientInfo'
          },
          {
            title:'性别',
            align:"center",
            dataIndex: 'fsfXb'
          },
          {
            title:'出生日期',
            align:"center",
            dataIndex: 'fsfCsrq'
          },
          {
            title:'病历号',
            align:"center",
            dataIndex: 'medicalRecordNo'
          },
          {
            title:'手术编号',
            align:"center",
            dataIndex: 'operativeNumber'
          },
          {
            title:'手术名称',
            align:"center",
            dataIndex: 'operationName'
          },
          {
            title:'手术时间',
            align:"center",
            dataIndex: 'operationTime'
          },
          {
            title:'执行科室',
            align:"center",
            dataIndex: 'oprDeptName'
          },
          {
            title:'诊治医生',
            align:"center",
            dataIndex: 'surgeonName'
          },
          {
            title:'入院日期',
            align:"center",
            dataIndex: 'admissionDate'
          },
        ],
        url: {
          list: "/pd/pdProduct/chooseProductList",
        },
        popModal: {
          title: '选择病人信息',
          visible: false,
          width: '100%',
          style: { top: '20px' },
          switchFullscreen: true,  //缩放按钮
          lockScroll: false,
          fullscreen: true,
        },
      }
    },
    methods: {
      close () {
        this.selectedRowKeys = [];
        this.selectionRows = [];
        this.queryParam = {};
        this.$emit('close');
        this.visible = false;
      },
      show(rows) {
         this.dataSource=rows;
         this.visible = true;
      },
      handleOk () {
         let rows = this.selectionRows;
         this.$emit('ok', rows[0]);
        this.close();
      },
      handleCancel () {
        this.close();
      },
      popupCallback(row){

      },
      loadData(arg) {

      },


      onSelectChange(selectedRowKeys, selectedRows) {
        this.selectedRowKeys = selectedRowKeys;
        this.selectionRows = selectedRows;
      },
      onClearSelected() {
        this.selectedRowKeys = [];
        this.selectionRows = [];
      },
      handleDelete: function (record) {
        this.dataSource.splice(this.dataSource.indexOf(record), 1);
      },

    }
  }
</script>