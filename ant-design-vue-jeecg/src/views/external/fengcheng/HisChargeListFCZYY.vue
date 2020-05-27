<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="收费代码">
              <a-input placeholder="请输入收费代码" v-model="queryParam.SFCODE"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="项目名称">
              <a-input placeholder="项目名称" v-model="queryParam.SFNAME"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
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
        size="middle"
        bordered
        rowKey="sfcode"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="false"
        :loading="loading"
        :rowSelection="{fixed:false,selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">
      </a-table>
    </div>

  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { getAction,httpAction } from '@/api/manage'
  export default {
    name: "HisChargeListFCZYY",
    mixins:[JeecgListMixin],
    components: {},
    data () {
      return {
        description: '收费项目管理页面',
        confirmLoading: false,
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
            title:'项目编号',
            align:"center",
            // dataIndex: 'SFNO'
            dataIndex: 'sfno'
          },
          {
            title:'项目名称',
            align:"center",
            // dataIndex: 'SFNAME'
            dataIndex: 'sfname'
          },
          {
            title:'收费代码',
            align:"center",
            // dataIndex: 'SFCODE'
            dataIndex: 'sfcode'
          },
        ],
        url: {
          list: "/pd/hisChargeFCZYY/list",
         },
        dictOptions:{
        },

      }
    },
    computed: {

    },
    methods: {

      loadData(arg) {
        //加载数据 若传入参数1则加载第一页的内容
        let params = this.getQueryParams();//查询条件
        //查询
        this.loading = true;
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.dataSource = res.result;
            // this.ipagination.total = res.result.total;
          }else{
            this.$message.warn(res.message);
          }
          this.loading = false;
        })
      },

      initDictConfig(){
      }
       
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>