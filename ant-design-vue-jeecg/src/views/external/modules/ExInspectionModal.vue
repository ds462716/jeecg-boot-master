<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :md="6" :sm="8">
              <a-form-item label="病人姓名">
                <a-input placeholder="请输入病人姓名" v-model="queryParam.patientName"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="条形码">
                <a-input placeholder="请输入条形码" v-model="queryParam.barCode"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="就诊卡号">
                <a-input placeholder="请输入就诊卡号" v-model="queryParam.cardId"></a-input>
              </a-form-item>
            </a-col>
            <template v-if="toggleSearchStatus">

            </template>
            <a-col :md="6" :sm="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
            </a-col>

          </a-row>
        </a-form>
      </div>
      <!-- 查询区域-END -->
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>
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
        :rowSelection="{fixed:false,selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">
      </a-table>
    </a-spin>
  </j-modal>
</template>

<script>

  import Vue from 'vue'
  import { httpAction,getAction } from '@/api/manage'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { FormTypes } from '@/utils/JEditableTableUtil'

  export default {
    name: "ExInspectionModel",
    mixins:[JeecgListMixin],
    components: {
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"用量明细",
        model:{},
        width:1200,
        visible: false,
        confirmLoading: false,
        // 表头
        columns: [
          {
            title:'患者姓名',
            align:"center",
            dataIndex: 'patientName'
          },
          {
            title:'患者性别',
            align:"center",
            dataIndex: 'patientSex'
          },
          {
            title:'就诊卡号',
            align:"center",
            dataIndex: 'cardId'
          },
          {
            title:'条形码',
            align:"center",
            dataIndex: 'barCode'
          },
          {
            title:'申请科室',
            align:"center",
            dataIndex: 'applyDepartment'
          },
          {
            title:'患者类型',
            align:"center",
            dataIndex: 'patientType'
          },
          {
            title:'检验日期',
            align:"center",
            dataIndex: 'testDate'
          },
          {
            title:'检验仪器',
            align:"center",
            dataIndex: 'instrName'
          },
          {
            title: '检验项目名称',
            align:"center",
            dataIndex: 'testItemName'
          },
          {
            title:'扣减时间',
            align:"center",
            dataIndex: 'inspectionTime'
          },
          {
            title: '扣减用量',
            align:"center",
            dataIndex: 'count'
          },
        ],
        url: {
          list: "/external/exInspectionItems/patientList",
        }
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
      edit (record) {
        this.model.refBarCode=record.refBarCode;
        this.queryParam = {};
        this.form.resetFields();
        this.loadData(1);
        this.visible = true;
      },
      handleOk () {
        this.close();
      },
      handleCancel () {
        this.close();
      },
      onClearSelected() {
        this.selectedRowKeys = [];
        this.selectionRows = [];
      },
      popupCallback(row){

      },
      loadData(arg) {
        if(!this.url.list){
          this.$message.error("请设置url.list属性!")
          return
        }
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        var params = this.getQueryParams();//查询条件
        params.refBarCode=this.model.refBarCode;
        this.loading = true;
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records;
            this.ipagination.total = res.result.total;
          }
          this.loading = false;
        })
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
                if(parseInt(lie)-parseInt(cellIndex)!=1){
                  //操作那一行
                  let recordId = record.id;
                  let index = this.selectedRowKeys.indexOf(recordId);
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
      }
    }
  }
</script>