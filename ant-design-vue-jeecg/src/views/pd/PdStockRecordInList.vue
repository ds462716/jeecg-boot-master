<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="入库单号">
              <a-input placeholder="请输入入库单号" v-model="queryParam.recordNo"></a-input>
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
      <!--<a-button type="primary" icon="download" @click="handleExportXls('入库记录表')">导出</a-button>-->
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
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无此图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="图片不存在" style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无此文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="uploadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)" v-bind:disabled="record.submitStatus=='2'">修改</a>
          <a-divider type="vertical"/>
          <a @click="handleDetail(record)">详情</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item v-show="record.submitStatus=='1' || record.submitStatus=='3'"> <!--待提交、已撤回-->
                <a-popconfirm title="确定删除吗?"  @confirm="() => handleDelete(record.id)"  >
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <pd-stock-record-in-modal ref="modalForm" @ok="modalFormOk"></pd-stock-record-in-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import PdStockRecordInModal from './modules/PdStockRecordInModal'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'

  export default {
    name: "PdStockRecordInList",
    mixins:[JeecgListMixin],
    components: {
      PdStockRecordInModal
    },
    data () {
      return {
        description: '入库记录表管理页面',
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
            title:'入库单号',
            align:"center",
            dataIndex: 'recordNo'
          },
          {
            title:'入库库房',
            align:"center",
            dataIndex: 'inDepartName'
          },
          {
            title:'供应商',
            align:"center",
            dataIndex: 'supplierName'
          },
          {
            title:'提交时间',
            align:"center",
            dataIndex: 'submitDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'入库类型',
            align:"center",
            dataIndex: 'inType',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['inType'], text+"")
              }
            }
          },
          {
            title:'操作人',
            align:"center",
            dataIndex: 'submitByName'
          },
          {
            title:'提交状态',
            align:"center",
            dataIndex: 'submitStatus',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['submitStatus'], text+"")
              }
            }
          },
          {
            title:'审核状态',
            align:"center",
            dataIndex: 'auditStatus',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['auditStatus'], text+"")
              }
            }
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/pd/pdStockRecordIn/list",
          delete: "/pd/pdStockRecordIn/delete",
          // deleteBatch: "/pd/pdStockRecordIn/deleteBatch",
          exportXlsUrl: "/pd/pdStockRecordIn/exportXls",
          // importExcelUrl: "pd/pdStockRecordIn/importExcel",
        },
        dictOptions:{
        },
      }
    },
    computed: {
      // importExcelUrl: function(){
      //   return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      // }
    },
    methods: {
      initDictConfig(){ //静态字典值加载
        initDictOptions('audit_status').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'auditStatus', res.result)
          }
        })
        initDictOptions('submit_status').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'submitStatus', res.result)
          }
        })
        initDictOptions('in_type').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'inType', res.result)
          }
        })
      },
       
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>