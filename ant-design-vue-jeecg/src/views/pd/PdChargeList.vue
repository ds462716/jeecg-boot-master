<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="收费项目代码">
              <a-input placeholder="请输入收费项目代码" v-model="queryParam.fsfXmbh"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="收费项目名称">
              <a-input placeholder="收费项目名称" v-model="queryParam.fsfXmmc"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
          <a-form-item label="所属科室">
            <a-input placeholder="请输入科室名称" v-model="queryParam.departName"></a-input>
          </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="项目类别">
              <a-input placeholder="请输入费用类别" v-model="queryParam.fsfXmlb"></a-input>
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
    
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="sysUpdate" type="primary"   :loading="confirmLoading" icon="plus">同步更新</a-button>
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
        :rowSelection="{fixed:false,selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>



        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { getAction,httpAction } from '@/api/manage'
  export default {
    name: "PdChargeList",
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
            title:'收费项目编号',
            align:"center",
            dataIndex: 'fsfXmbh'
          },
          {
            title:'收费项目名称',
            align:"center",
            dataIndex: 'fsfXmmc'
          },
          {
            title:'收费项目规格',
            align:"center",
            dataIndex: 'fsfXmgg'
          },
          {
            title:'收费类别',
            align:"center",
            dataIndex: 'fsfXmlb'
          },
          {
            title:'收费金额',
            align:"center",
            dataIndex: 'fsfJe'
          },
          {
            title:'所属科室',
            align:"center",
            dataIndex: 'departName'
          },
          {
            title:'同步时间',
            align:"center",
            dataIndex: 'createTime'
          },
          /*{
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }*/
        ],
        url: {
          list: "/pd/pdHisCharge/list",
          synUpdate: "/pd/pdHisCharge/synUpdate",
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
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        let params = this.getQueryParams();//查询条件
        //查询
        this.loading = true;
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records;
            this.ipagination.total = res.result.total;
          }
          this.loading = false;
        })
      },


      sysUpdate: function () {
        const that = this;
        that.confirmLoading = true;
        httpAction(this.url.synUpdate,{},"post").then((res)=>{
          if(res.success){
             that.$message.success(res.message);
             that.loadData(1);
          }else{
            that.$message.warning(res.message);
          }
        }).finally(() => {
          that.confirmLoading = false;
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