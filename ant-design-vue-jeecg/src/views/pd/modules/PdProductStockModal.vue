<template>
  <a-modal
    :title="title"
    :width="1500"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @cancel="handleCancel"
    cancelText="关闭">
    <template slot="footer">
      <a-button type="primary" @click="handleCancel">返回</a-button>
    </template>
    <div class="numberWARAP">
      <div class="total">总数量：<span id="totalNum">{{this.validatorRules.pCount}}</span></div>
      <div class="nearTime">近效期数量：<span id="nearNum">{{this.validatorRules.jCount}}</span></div>
      <div class="overTime">过期数量：<span id="overNum">{{this.validatorRules.gCount}}</span></div>
    </div>
    <a-spin :spinning="confirmLoading">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :md="6" :sm="8">
              <a-form-item label="产品编号">
                <a-input placeholder="请输入产品编号" v-model="queryParam.number"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="产品名称">
                <a-input placeholder="请输入产品名称" v-model="queryParam.productName"></a-input>
              </a-form-item>
            </a-col>
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
      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :rowClassName="setdataCss"
        :loading="loading"
        @change="handleTableChange">
      </a-table>
    </a-spin>
  </a-modal>
</template>

<script>

  import { FormTypes } from '@/utils/JEditableTableUtil'
  import { httpAction,getAction } from '@/api/manage'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'

  const VALIDATE_NO_PASSED = Symbol()
  export { FormTypes, VALIDATE_NO_PASSED }
  export default {
    name: "PdProductStockModel",
    mixins:[JeecgListMixin],
    components: {
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"库存产品",
        width:1200,
        visible: false,
        model:{},
        validatorRules: {
          pCount:{},//总数量
          gCount:{},//过期数量
          jCount:{},//近效期数量
        },
        confirmLoading: false,
        // 表头
        columns: [
          {
            title:'产品名称',
            align:"center",
            dataIndex: 'productName'
          },
          {
            title:'产品编号',
            align:"center",
            dataIndex: 'number'
          },
          {
            title:'产品条码',
            align:"center",
            dataIndex: 'productBarCode'
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
            title:'批号',
            align:"center",
            dataIndex: 'batchNo'
          },
          {
            title:'有效期',
            align:"center",
            dataIndex: 'expDate'
          },
          {
            title:'货位',
            align:"center",
            dataIndex: 'huoweiCode'
          },
          {
            title:'单位',
            align:"center",
            dataIndex: 'unitName'
          },
          {
            title: '生产厂家',
            align: "center",
            dataIndex: 'venderName'
          },
          {
            title:'注册证号',
            align:"center",
            dataIndex: 'registration'
          },
          {
            title:'数量',
            align:"center",
            dataIndex: 'stockNum'
          },
          {
            title:'是否过期',
            align:"center",
            dataIndex: 'expStatus',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['expStatus'], text+"")
              }
            }
          },
          {
            title:'是否久存',
            align:"center",
            dataIndex: 'isLong',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['isLong'], text+"")
              }
            }
          }
        ],
        url: {
          list: "/pd/pdProductStockTotal/chooseProductStockList",
        },
        dictOptions:{
          expStatus:[],
          isLong:[],
        },
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
        params.productId=this.model.productId;
        params.deptarId=this.model.deptarId;
        this.loading = true;
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.validatorRules.pCount=res.result.pCount;
            this.validatorRules.gCount=res.result.gCount;
            this.validatorRules.jCount=res.result.jCount;
            this.dataSource = res.result.records.records;
            this.ipagination.total = res.result.records.total;
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
      handleOk () {
        let rows = this.selectionRows;
        this.$emit('ok', rows);
        this.close();
      },
      handleCancel () {
        this.close();
      },
      popupCallback(row){

      },

      initDictConfig(){ //静态字典值加载
        initDictOptions('exp_status').then((res) => { //是否过期字典转换
          if (res.success) {
            this.$set(this.dictOptions, 'expStatus', res.result)
          }
        })
        initDictOptions('pd_isLong').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'isLong', res.result)
          }
        })
      },
      setdataCss(record,index) {
        let expire = record.expStatus;
        return "expire"+expire;
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