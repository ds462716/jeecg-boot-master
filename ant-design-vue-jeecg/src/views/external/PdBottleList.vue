<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="开瓶操作人">
              <a-input placeholder="请输入操作人名称" v-model="queryParam.productName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="产品名称">
              <a-input placeholder="请输入产品名称" v-model="queryParam.productName"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">

          </template>
          <a-col :md="6" :sm="8" >
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
      <a-button @click="handleBottle" type="primary" icon="plus">开瓶</a-button>
      <a-button @click="handleClose" type="primary" icon="download"  >闭瓶</a-button>
    </div>
    <!-- table区域-begin -->
    <div>
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
        :rowSelection="{fixed:true,selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">
      </a-table>
    </div>
    <pd-bottle-modal ref="pdBottleModal" @ok="modalFormOk"></pd-bottle-modal>
  </a-card>
</template>
<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import pdBottleModal from './modules/PdBottleModal'
  import { deleteAction, getAction,downFile } from '@/api/manage'

  export default {
    name: "PdBottleList",
    mixins:[JeecgListMixin],
    components: {
      pdBottleModal
    },
    data () {
      return {
        description: '开闭瓶管理页面',
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
            title:'开瓶操作人',
            align:"center",
            dataIndex: 'boottleBy'
          },
          {
            title:'开瓶时间',
            align:"center",
            dataIndex: 'boottleDate'
          },
          {
            title:'产品名称',
            align:"center",
            dataIndex: 'productName'
          },
          {
            title:'唯一码编号',
            align:"center",
            dataIndex: 'refBarCode'
          },
          {
            title:'库存明细ID',
            align:"center",
            dataIndex: 'stockId'
          },
          {
            title:'使用数量',
            align:"center",
            dataIndex: 'specNum'
          },
          {
            title:'闭瓶时间',
            align:"center",
            dataIndex: 'closeDate'
          },
          {
            title:'闭瓶操作人',
            align:"center",
            dataIndex: 'closeBy'
          },

          {
            title:'备注',
            align:"center",
            dataIndex: 'remarks'
          },
          {
            title:'所属部门',
            align:"center",
            dataIndex: 'departName'
          },
        ],
        url: {
          list: "/pd/pdBottleInf/list"
        },
        dictOptions:{},
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {

      // 重写loadData方法
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
        this.loading = true;
        getAction(this.url.list, params).then((res) => {
          if (res.success){
            this.dataSource = res.result.records;
            this.ipagination.total = res.result.total;
          }
          this.loading = false;
        })
      },




      handleBottle() { //开瓶
        this.$refs.pdBottleModal.edit(1);
        this.$refs.pdBottleModal.title = "开瓶";
        this.$refs.pdBottleModal.disableSubmit = false;
      },

      handleClose(){ //闭瓶
        this.$refs.pdBottleModal.edit(2);
        this.$refs.pdBottleModal.title = "闭瓶";
        this.$refs.pdBottleModal.disableSubmit = false;
      },


      modalFormOk(){
        this.loadData();
      },
      initDictConfig(){
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
 </style>