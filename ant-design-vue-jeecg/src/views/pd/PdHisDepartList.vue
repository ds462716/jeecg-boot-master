<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="所属科室">
              <a-input placeholder="请输入科室名称" v-model="queryParam.departName"></a-input>
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
      <a-button @click="sysUpdate" type="primary" icon="plus">同步更新</a-button>
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
          <a @click="departEdit(record)">科室维护</a>
        </span>

      </a-table>
    </div>
    <his-depart-modal ref="modalForm" @ok="modalFormOk"></his-depart-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import HisDepartModal from "./modules/HisDepartModal"
  import { httpAction } from '@/api/manage'
  export default {
    name: "PdHisDepartList",
    mixins:[JeecgListMixin],
    components: {HisDepartModal},
    data () {
      return {
        description: 'HIS科室管理页面',
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
            title:'his科室编号',
            align:"center",
            dataIndex: 'fsfKsbh'
          },
          {
            title:'his科室名称',
            align:"center",
            dataIndex: 'fsfKsmc'
          },
          {
            title:'关联科室名称',
            align:"center",
            dataIndex: 'departName'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/pd/pdHisDepart/list",
          synUpdate: "/pd/pdHisDepart/synUpdateDept",
         },
        dictOptions:{
        },

      }
    },
    computed: {

    },
    methods: {
      sysUpdate: function () {
        httpAction(this.url.synUpdate,{},"post").then((res)=>{
          if(res.success){
            that.$message.success(res.message);
            that.$emit('ok');
          }else{
            that.$message.warning(res.message);
          }
        })

      },

      departEdit: function (record) {
        this.$refs.modalForm.title = "编辑";
        this.$refs.modalForm.disableSubmit = false;
        this.$refs.modalForm.edit(record);
      },

      initDictConfig(){
      }
       
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>