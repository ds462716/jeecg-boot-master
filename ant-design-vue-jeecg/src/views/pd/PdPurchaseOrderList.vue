<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="申购编号">
              <a-input placeholder="请输入申购编号" v-model="queryParam.orderNo"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="申购库房名称">
              <a-input placeholder="请输入申购库房名称" v-model="queryParam.storeroomName"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="6" :sm="8">
              <a-form-item label="审核状态">
                <a-select v-model="queryParam.orderStatus" placeholder="请选择审核状态">
                  <a-select-option value="0">待审核</a-select-option>
                  <a-select-option value="1">审核中</a-select-option>
                  <a-select-option value="2">审核通过</a-select-option>
                  <a-select-option value="3">已拒绝</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
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
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
     <!--<a-button type="primary" icon="download" @click="handleExportXls('申购订单主表')">导出</a-button>
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

        <!--<template slot="htmlSlot" slot-scope="text">
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
        </template>-->

        <span slot="action" slot-scope="text, record">
          <a  @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a href="javascript:;" @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <pdPurchaseOrder-modal ref="modalForm" @ok="modalFormOk"></pdPurchaseOrder-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin,handleEdit} from '@/mixins/JeecgListMixin'
  import { deleteAction } from '@/api/manage'
  import PdPurchaseOrderModal from './modules/PdPurchaseOrderModal'
  //import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'

  export default {
    name: "PdPurchaseOrderList",
    mixins:[JeecgListMixin],
    components: {
     //JDictSelectTag,
      PdPurchaseOrderModal
    },
    data () {
      return {
        description: '申购订单主表管理页面',
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
            title:'申购编号',
            align:"center",
            dataIndex: 'orderNo'
          },
          {
            title:'申购人编号',
            align:"center",
            dataIndex: 'purchaseBy'
          },
          {
            title:'申购日期',
            align:"center",
            dataIndex: 'orderDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'申购库房名称',
            align:"center",
            dataIndex: 'storeroomName'

          },
          {
            title:'审核状态',
            align:"center",
            dataIndex: 'orderStatus',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['orderStatus'], text+"")
              }
            }
          },
          {
            title:'申购总数量',
            align:"center",
            dataIndex: 'amountCount'
          },
          {
            title:'申购总金额',
            align:"center",
            dataIndex: 'amountMoney'
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
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/pd/pdPurchaseOrder/list",
          delete: "/pd/pdPurchaseOrder/delete",
          deleteBatch: "/pd/pdPurchaseOrder/deleteBatch"
        },
        dictOptions:{
         storeroomName:[],
         orderStatus:[],
         submitStart:[],
        },

      }
    },
    computed: {
      /*importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }*/
    },
    methods: {
      handleEdit: function (record) {//编译
        if(record.submitStart=='2'){
          this.$message.warning("此订单已提交审核，不允许编译！")
          return
        }
        this.$refs.modalForm.edit(record);
        this.$refs.modalForm.title = "编辑";
        this.$refs.modalForm.disableSubmit = false;
      },
      handleDelete: function (record) { //删除
        if(record.submitStart=='2'){
          this.$message.warning("此订单已提交审核，不允许删除！")
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
            this.$set(this.dictOptions, 'orderStatus', res.result)
          }
        })
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