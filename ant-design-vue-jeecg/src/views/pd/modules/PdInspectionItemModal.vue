<template>
  <j-modal
    :visible="visible"
    :width="1200"
    :title="title"
    :lockScroll="lockScroll"
    :fullscreen="fullscreen"
    :switchFullscreen="switchFullscreen"
    :maskClosable=disableSubmit
    @cancel="handleCancel"

  >
    <template slot="footer">
      <a-button type="primary" @click="handleCancel">返回</a-button>
    </template>
    <a-spin :spinning="confirmLoading">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :md="6" :sm="8">
              <a-form-item label="项目名称">
                <a-input placeholder="请输入项目名称" v-model="queryParam.testItemName"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="项目代号">
                <a-input placeholder="请输入项目代号" v-model="queryParam.testItemCode"></a-input>
              </a-form-item>
            </a-col>
            <template :md="6" v-if="toggleSearchStatus">
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
      <!-- 操作按钮区域 -->
      <hr/>
      <div>
          <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
            <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
            <a style="margin-left: 24px" @click="onClearSelected">清空</a>
          </div>
      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="testItemCode"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :scroll="tableScroll"
        @change="handleTableChange">
      </a-table>
      </div>
    </a-spin>
  </j-modal>
</template>

<script>

  import { FormTypes } from '@/utils/JEditableTableUtil'
  import { httpAction,getAction } from '@/api/manage'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
   import JDate from "../../../components/jeecg/JDate";
  import { disabledAuthFilter } from "@/utils/authFilter"
   import JDictSelectTagExpand from "@/components/dict/JDictSelectTagExpand"

  const VALIDATE_NO_PASSED = Symbol()
  export { FormTypes, VALIDATE_NO_PASSED }
  export default {
    name: "PdBottleInfModal",
    mixins:[JeecgListMixin],
    components: {
       JDate,
       JDictSelectTagExpand
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"检验项目明细",
        width:1200,
        visible: false,
        model:{},
        confirmLoading: false,
        lockScroll: false,
        fullscreen: true,
        switchFullscreen: false,
        disableSubmit:false,
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        // 表头
        columns: [
          {
            title: '序号',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'检验项目名称',
            align:"center",
            dataIndex: 'testItemName'
          },
          {
            title:'检验项目代号',
            align:"center",
            dataIndex: 'testItemCode'
          },
          {
            title:'检验数量',
            align:"center",
            dataIndex: 'itemNum'
          },
          {
            title:'检验金额',
            align:"center",
            dataIndex: 'itemPrice'
          },
        ],
        url: {
          list: "/external/exInspectionItems/selectExInsepectionMonth",
        },
        dictOptions:{
        },
        tableScroll:{ x: 1300 },
      }
    },
    created () {

    },
    methods: {
      add () {
        this.edit({});
      },
      loadData(arg){
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        var params = this.getQueryParams();//查询条件
        if(this.model.month){
          params.month=this.model.month;
        }
        this.loading = true;
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records;
            this.ipagination.total = res.result.total;
          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
          this.loading = false;
        })
      },


      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.loadData(1);
       this.initDictConfig();
      },
      close () {
        this.selectedRowKeys = [];
        this.selectionRows = [];
        this.$emit('close');
        this.visible = false;
      },
      show() {
        this.visible = true;
      },

      handleCancel () {
        this.close();
      },

      initDictConfig(){ //静态字典值加载

      },

    }
  }
</script>
<style scoped>
  .numberWARAP{width:100%;height:20px;line-height:20px;margin:1px 0;}
  .numberWARAP>div{float:left;width:33%;height:30px;line-height:30px;color:#666;font-size:16px;text-align:center;border-right:1px solid #ccc;}
  .numberWARAP>div:nth-child(3){border:none;}
  .changeColor .red td,.changeColor .red td a{color: red}
</style>
<style>
  .expire0{
  }
  .expire1{
    background-color:#FFFFCC;
  }
  .expire2{
    background-color:#FF3333;
  }
</style>


