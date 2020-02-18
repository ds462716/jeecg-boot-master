<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="科室">
              <a-input placeholder="请选择科室" v-model="queryParam.storeroomId"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="产品名称">
              <a-input placeholder="请选择产品名称" v-model="queryParam.productName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="产品编号">
              <a-input placeholder="请输入产品编号" v-model="queryParam.productName"></a-input>
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
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button type="primary" icon="download" @click="handleExportXls('库存明细')">导出</a-button>
    </div>
    <!-- table区域-begin -->
    <div>
      <a-table
        CLASS="changeColor"
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        @change="handleTableChange">
      </a-table>
    </div>
  </a-card>
</template>
<script>

  import { JeecgListMixin ,handleEdit} from '@/mixins/JeecgListMixin'

  import { getAction } from '@/api/manage'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'

  export default {
    name: "PdProductStockQueryList",
    mixins:[JeecgListMixin],
    components: {
    },
    data () {
      return {
        description: '库存明细查询',
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
            title:'所属科室',
            align:"center",
            dataIndex: 'storeroomName'
          },
          {
            title:'产品名称',
            align:"center",
            dataIndex: 'productName'
          },
          {
            title:'产品编号',
            align:"center",
            dataIndex: 'productNo'
          },
          {
            title:'产品条码',
            align:"center",
            dataIndex: 'productNo'
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
            dataIndex: 'unitName'
          },
          {
            title:'有效期',
            align:"center",
            dataIndex: 'limitUp'
          },
          {
            title:'数量',
            align:"center",
            dataIndex: 'limitDown'
          },
          {
            title:'生产厂家',
            align:"center",
            dataIndex: 'stockNum'
          },
          {
            title:'供应商',
            align:"center",
            dataIndex: 'stockNum'
          }
        ],
        url: {
          list: "/pd/pdProductStockTotal/list",
          exportXlsUrl: "/pd/pdProductStockTotal/exportXls",
        },
        dictOptions:{
          expire:[],
          isLong:[]
        },

      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      loadData(arg) {
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        var params = this.getQueryParams();//查询条件
        this.loading = true;
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.validatorRules.pCount=res.result.pCount;
            this.validatorRules.gCount=res.result.gCount;
            this.validatorRules.jCount=res.result.jCount;
            this.validatorRules.isLcount=res.result.isLcount;
            this.validatorRules.limtCount=res.result.limtCount;
            this.dataSource = res.result.records.records;
            this.ipagination.total = res.result.records.total;
          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
          this.loading = false;
        })
      },
      handleEdit: function (record) { //编译
        this.$refs.stockForm.edit(record);
        this.$refs.stockForm.title = "库存明细";
        this.$refs.stockForm.disableSubmit = false;
      },
      handleRecordEdit: function (record) { //编译
        this.$refs.stockForm2.edit(record);
        this.$refs.stockForm2.title = "出入库明细";
        this.$refs.stockForm2.disableSubmit = false;
      },
      handleUpdate(type) { //设置库存上限
        if (this.selectedRowKeys.length <= 0) {
          this.$message.warning('请勾选需要设置的数据！');
          return;
        }
        let  ids="";
        for (var a = 0; a < this.selectedRowKeys.length; a++) {
          ids += this.selectedRowKeys[a] + ",";
        }
        var name="下限";
        if(type=='Up'){
            name="上限"
        }
        this.$refs.modalForm1.edit({"ids":ids,"type":type});
        this.$refs.modalForm1.title = "设置"+name;
        this.$refs.modalForm1.disableSubmit = false;
      },

      initDictConfig(){ //静态字典值加载
        initDictOptions('pd_state').then((res) => { //是否过期字典转换
          if (res.success) {
            this.$set(this.dictOptions, 'expire', res.result)
          }
        })
        initDictOptions('pd_isLong').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'isLong', res.result)
          }
        })
      }
    }
  }
</script>
<style scoped>
  .numberWARAP{width:100%;height:30px;line-height:30px;margin:20px 0;}
  .numberWARAP>div{float:left;width:33%;height:30px;line-height:30px;color:#666;font-size:16px;text-align:center;border-right:1px solid #ccc;}
  .numberWARAP>div:nth-child(3){border:none;}
  .changeColor .red td,.changeColor .red td a{color: red}
  /*.alert_close_btn:hover{color:#fff;border-bottom:1px solid #dedede}*/
  @import '~@assets/less/common.less'
</style>