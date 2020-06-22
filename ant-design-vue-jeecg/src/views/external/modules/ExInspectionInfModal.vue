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
    <a-spin :spinning="confirmLoading">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :md="6" :sm="8">
              <a-form-item label="产品名称">
                <a-input placeholder="请输入产品名称" v-model="queryParam.productName"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="产品编号">
                <a-input placeholder="请输入产品编号" v-model="queryParam.number"></a-input>
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
        :loading="loading"
        :scroll="tableScroll"
        @change="handleTableChange">
      </a-table>
    </a-spin>
  </a-modal>
</template>
<script>
  import { httpAction,getAction } from '@/api/manage'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  export default {
    name: "ExInspectionInfModal",
    mixins:[JeecgListMixin],
    components: {
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"扣减用量明细",
        venderData: [],
        venderValue: undefined,
        notFoundContent:"未找到内容",
        supplierSelecDisabled:false,
        width:1200,
        visible: false,
        model:{},
        validatorRules: {
          productTotNum:{},//入库总数量
          productOutTotNum:{},//出库总数量
          inPrice:{},//入库总金额
          outPrice:{},//出库总金额
        },
        confirmLoading: false,
        // 表头
        columns: [
          {
            title:'检验代号',
            align:"center",
            dataIndex: 'code'
          },
          {
            title:'检验名称',
            align:"center",
            dataIndex: 'testItemName'
          },
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
            title:'唯一码',
            align:"center",
            dataIndex: 'refBarCode'
          },
          {
            title:'规格',
            align:"center",
            dataIndex: 'spec'
          },
          {
            title:'单位',
            align:"center",
            dataIndex: 'unitName'
          },
          {
            title:'需扣减用量',
            align:"center",
            dataIndex: 'count'
          },
          {
            title:'扣减状态',
            align:"center",
            dataIndex: 'status',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['status'], text+"")
              }
            }
          },
          {
            title:'备注',
            align:"center",
            dataIndex: 'remarks'
          },
        ],
        url: {
          list: "/external/exInspectionInf/list",
        },
        dictOptions:{
          status:[],
        },
        tableScroll:{x :1300},
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
        params.code=this.model.testItemCode;
        if(this.model.testItemCode=="" || this.model.testItemCode==null){
          return;
        }
        params.jyId=this.model.jyId;
        this.loading = true;
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records;
            this.ipagination.total = res.result.total;
          }else{
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

      },
      handleCancel () {
        this.close();
      },
      popupCallback(row){

      },



      initDictConfig(){ //静态字典值加载
        initDictOptions('inspection_status').then((res) => { //
          if (res.success) {
            this.$set(this.dictOptions, 'status', res.result)
          }
        })

      }
    }
  }
</script>
<style scoped>
  .numberWARAP{width:100%;height:20px;line-height:20px;margin:1px 0;}
  .numberWARAP>div{float:left;width:25%;height:30px;line-height:30px;color:#666;font-size:16px;text-align:center;border-right:1px solid #ccc;}
  .numberWARAP>div:nth-child(4){border:none;}
  .changeColor .red td,.changeColor .red td a{color: red}
  @import '~@assets/less/common.less'
</style>