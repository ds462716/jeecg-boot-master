<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">


      </a-form>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="productId"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{fixed:true,selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"

        @change="handleTableChange">


      </a-table>
    </a-spin>
  </a-modal>
</template>

<script>

  import { httpAction,getAction } from '@/api/manage'
  // import pick from 'lodash.pick'
  import { makeWb } from '@/utils/wubi'
  import { duplicateCheckHasDelFlag } from '@/api/api'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'

  export default {
    name: "PdChooseProductListModel",
    mixins:[JeecgListMixin],
    components: {
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"选择产品",
        width:1200,
        visible: false,
        // model: {},
        confirmLoading: false,
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: 'productId',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'产品编号',
            align:"center",
            dataIndex: 'number'
          },
          {
            title:'产品名称',
            align:"center",
            dataIndex: 'productName'
          },
          {
            title:'规格',
            align:"center",
            dataIndex: 'spec'
          },
          {
            title:'型号',
            align:"center",
            dataIndex: 'version'
          },
          {
            title:'单位',
            align:"center",
            dataIndex: 'unitName'
          },
          {
            title:'供应商',
            align:"center",
            dataIndex: 'supplierName'
          },
          {
            title: '生产厂家',
            align:"center",
            dataIndex: 'venderName'
          }
        ],
        url: {
          list: "/pd/pdProduct/chooseProductList",
        }
      }
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      show() {
        this.visible = true;
      },
      handleOk () {
        const that = this;
        let formData = that.selectionRows;
        that.$emit('ok', formData);
        that.close();
      },
      handleCancel () {
        this.close()
      },
      popupCallback(row){

      },
    }
  }
</script>