<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <!--<a-col :md="6" :sm="8">
            <a-form-item label="值">
              <a-input placeholder="请输入值" autocomplete="off" v-model="queryParam.value"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="含义">
              <a-input placeholder="请输入含义" autocomplete="off" v-model="queryParam.meaning"></a-input>
            </a-form-item>
          </a-col>-->
          <a-col :md="6" :sm="8" >
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->
    <!-- table区域-begin -->
    <div>
      <a-table
        ref="table"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="false"
        :loading="loading"
        :customRow="onClickRow"
        :rowSelection="{fixed:false,selectedRowKeys: selectedRowKeys,type:'radio', onChange: onSelectChange}"
        :scroll="{ y: 280 }"
        @change="handleTableChange">

      </a-table>
    </div>
  </a-modal>
</template>

<script>
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import {getAction} from '@/api/manage'

  export default {
    name: "ExInspectionItemsAddModal",
    mixins:[JeecgListMixin],
    components: {
      JDictSelectTag,
    },
    data () {
      return {
        description: '病人信息关联页面',
        visible: false,
        title:"操作",
        width:800,
        model:{},
        // 查询参数
        queryParam: {},
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
            title:'患者年龄',
            align:"center",
            dataIndex: 'patientAge'
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
            title:'申请医生',
            align:"center",
            dataIndex: 'applyDoctor'
          },
          {
            title:'申请科室',
            align:"center",
            dataIndex: 'applyDepartment'
          },
          {
            title:'检验医生',
            align:"center",
            dataIndex: 'testDoctor'
          },
          {
            title:'检验科室',
            align:"center",
            dataIndex: 'testDepartment'
          },
          {
            title:'患者类型',
            align:"center",
            dataIndex: 'patientType'
          },
          {
            title:'工作组',
            align:"center",
            dataIndex: 'groupBy'
          },
          {
            title:'接收日期',
            align:"center",
            dataIndex: 'receiveDate'
          },
          {
            title:'检验日期',
            align:"center",
            dataIndex: 'testDate'
          },
          /*{
            title:'样本类型',
            align:"center",
            dataIndex: 'specimenType'
          },
          {
            title:'状态',
            align:"center",
            dataIndex: 'state'
          },
          {
            title:'项目组合名称',
            align:"center",
            dataIndex: 'combinationName'
          },
          {
            title:'项目组合代码',
            align:"center",
            dataIndex: 'combinationCode'
          },
          {
            title:'检查项目名称',
            align:"center",
            dataIndex: 'testItemName'
          },
          {
            title:'检查项目代码',
            align:"center",
            dataIndex: 'testItemCode'
          },
          {
            title:'检查项目费用',
            align:"center",
            dataIndex: 'testItemCost'
          },
          {
            title:'读取状态',
            align:"center",
            dataIndex: 'acceptStatus'
          },*/
        ],
        url: {
          list: "/external/exInspectionItems/list",
        },
        dictOptions:{
          type:[],
        },
      }
    },
    methods: {
      loadData(arg) {
        this.loading = true;
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        let params = this.getQueryParams();//查询条件
        getAction(this.url.list, params).then((res) => {
          if (res.success && res.result) {
            this.dataSource = res.result.records;
            this.ipagination.total = res.result.total;
            this.loading = false;
          }else{
            this.$message.error("系统异常!")
          }
        })

      },
      initDictConfig(){
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      show(){
        this.visible = true;
        this.loadData(1);
      },
      handleCancel () {
        this.close()
      },
      handleOk () {
        const that = this;
        let formData = that.selectionRows;
        that.$emit('ok', formData);
        that.close();
      },
      /**
       * 点击行选中checkbox
       * @param record
       * @returns {{on: {click: on.click}}}
       */
      onClickRow(record) {
        return {
          on: {
            click: () => {
              //操作那一行
              let recordId = record.id;
              let index = this.selectedRowKeys.indexOf(recordId);
              if(index>=0){
                this.selectedRowKeys = [];
                this.selectionRows = [];
                /*this.selectedRowKeys.splice(index, 1);
                this.selectionRows.splice(index, 1);*/
              }else{
                this.selectedRowKeys = [];
                this.selectionRows = [];
                this.selectedRowKeys.push(recordId);
                this.selectionRows.push(record);
              }
             }
            }
          }
        }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>