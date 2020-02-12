<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="申领单号">
              <a-input placeholder="请输入申领单号" v-model="queryParam.applyNo"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="申领科室名称">
              <a-input placeholder="请输入申领科室名称" v-model="queryParam.deptName"></a-input>
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
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
     <!-- <a-button type="primary" icon="download" @click="handleExportXls('申领单主表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>-->
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
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
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a href="javascript:;" @click="handleDetail(record)">详情</a>
              </a-menu-item>
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

    <pdApplyOrder-modal ref="modalForm" @ok="modalFormOk"></pdApplyOrder-modal>
  </a-card>
</template>

<script>

  import PdApplyOrderModal from './modules/PdApplyOrderModal'
  import { JeecgListMixin,handleEdit} from '@/mixins/JeecgListMixin'
  import { deleteAction } from '@/api/manage'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  export default {
    name: "PdApplyOrderList",
    mixins:[JeecgListMixin],
    components: {
      PdApplyOrderModal
    },
    data () {
      return {
        description: '申领管理页面',
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
            title:'申领单号',
            align:"center",
            dataIndex: 'applyNo'
          },
          {
            title:'申领日期',
            align:"center",
            dataIndex: 'applyDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'申领科室',
            align:"center",
            dataIndex: 'deptName'
          },
          {
            title:'申领数量',
            align:"center",
            dataIndex: 'applyNum'
          },
          {
            title:'实际领用数量',
            align:"center",
            dataIndex: 'factCount'
          },
          {
            title:'审核状态',
            align:"center",
            dataIndex: 'applyStatus',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['applyStatus'], text+"")
              }
            }
          },
          {
            title:'提交状态',
            align:"center",
            dataIndex: 'submitStart',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['submitStart'], text+"")
              }
            }
          },
          {
            title:'操作人',
            align:"center",
            dataIndex: 'applyBy'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/pd/pdApplyOrder/list",
          delete: "/pd/pdApplyOrder/delete",
          deleteBatch: "/pd/pdApplyOrder/deleteBatch",
         /* exportXlsUrl: "/pd/pdApplyOrder/exportXls",
          importExcelUrl: "pd/pdApplyOrder/importExcel",*/
        },
        dictOptions:{
          applyStatus:[],
          submitStart:[],
        },

      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      handleEdit: function (record) { //编译
        if(record.submitStart=='2' && record.applyStatus !='3'){
          this.$message.warning("此单已提交审核，不允许编译！")
          return
        }
        this.$refs.modalForm.edit(record);
        this.$refs.modalForm.title = "编辑";
        this.$refs.modalForm.disableSubmit = false;
      },
      handleDelete: function (record) { //删除
        if(record.submitStart=='2'){
          this.$message.warning("此单已提交审核，不允许删除！")
          return
        }
        var that = this;
        var id=record.id;
        deleteAction(that.url.delete, {id: id}).then((res) => {
          if (res.success) {
            that.$message.success(res.message);
            that.loadData();
          } else {
            that.$message.warning(res.message);
          }
        });
      },
      initDictConfig(){ //静态字典值加载
        initDictOptions('order_status').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'applyStatus', res.result)
          }
        }),
        initDictOptions('submit_status').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'submitStart', res.result)
          }
        })
      }
       
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>