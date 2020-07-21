<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="名称">
              <a-input placeholder="请输入名称" v-model="queryParam.name"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="状态">
              <j-dict-select-tag-expand v-model="queryParam.status" dictCode="disable_enable_status" placeholder="请选择状态"/>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="过期状态">
              <a-select placeholder="过期状态" :allowClear="true" v-model="queryParam.validityFlag" >
                <a-select-option value="0">正常</a-select-option>
                <a-select-option value="1">已过期</a-select-option>
                <a-select-option value="2">近效期</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
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
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('供应商')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
          <a-menu-item key="2" @click="batchDisable('1')"><a-icon type="lock" />停用</a-menu-item>
          <a-menu-item key="3" @click="batchDisable('0')"><a-icon type="unlock" />启用</a-menu-item>
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
        :rowClassName="setdataCss"
        :customRow="onClickRow"
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

    <pdSupplier-modal ref="modalForm" @ok="modalFormOk"></pdSupplier-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import PdSupplierModal from './modules/PdSupplierrModal'
  import {postAction} from '@/api/manage'
  import JDictSelectTagExpand from "@/components/dict/JDictSelectTagExpand"
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'

  export default {
    name: "PdSupplrList",
    mixins:[JeecgListMixin],
    components: {
      PdSupplierModal,
      JDictSelectTagExpand
    },
    data () {
      return {
        description: '供应商管理页面',
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
            title:'名称',
            align:"center",
            dataIndex: 'name'
          },
          {
            title:'分类',
            align:"center",
            dataIndex: 'supplierType',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['supplierType'], text+"")
              }
            }
          },
          {
            title:'拼音简码',
            align:"center",
            dataIndex: 'py'
          },
          {
            title:'五笔简码',
            align:"center",
            dataIndex: 'wb'
          },
          {
            title:'自定义码',
            align:"center",
            dataIndex: 'zdy'
          },
          {
            title:'JDE编码',
            align:"center",
            dataIndex: 'jdeCode'
          },
          {
            title:'状态',
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
          /*{
            title:'创建日期',
            align:"center",
            dataIndex: 'createTime'
          },
          {
            title:'更新日期',
            align:"center",
            dataIndex: 'updateTime'
          },*/
          {
            title:'备注',
            align:"center",
            dataIndex: 'remarks'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/pd/pdSupplier/list",
          delete: "/pd/pdSupplier/delete",
          deleteBatch: "/pd/pdSupplier/deleteBatch",
          exportXlsUrl: "/pd/pdSupplier/exportXls",
          importExcelUrl: "pd/pdSupplier/importExcel",
          batchDisable: "pd/pdSupplier/batchDisable",
        },
        dictOptions:{
          status:[],
          supplierType:[]
        },
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      initDictConfig(){
        initDictOptions('disable_enable_status').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'status', res.result)
          }
        }),
          initDictOptions('supplier_type').then((res) => {
            if (res.success) {
              this.$set(this.dictOptions, 'supplierType', res.result)
            }
          })
      },
      setdataCss(record,index) {
        let validityFlag = record.validityFlag;
        return "validityFlag"+validityFlag;
      },
      /**
       * 点击行选中checkbox
       * @param record
       * @returns {{on: {click: on.click}}}
       */
      onClickRow(record) {
        return {
          on: {
            click: (e) => {
              //点击操作那一行不选中表格的checkbox
              let pathArray = e.path;
              //获取当前点击的是第几列
              let td = pathArray[0];
              let cellIndex = td.cellIndex;
              //获取tr
              let tr = pathArray[1];
              //获取一共多少列
              let lie = tr.childElementCount;
              if(lie && cellIndex){
                if(parseInt(lie)-parseInt(cellIndex)!=1){
                  //操作那一行
                  let recordId = record.id;
                  let index = this.selectedRowKeys.indexOf(recordId);
                  if(index>=0){
                    this.selectedRowKeys.splice(index, 1);
                  }else{
                    this.selectedRowKeys.push(recordId);
                  }
                }
              }
            }
          }
        }
      },
      /**
       * 批量启用或停用
       * @param status
       * @returns {boolean}
       */
      batchDisable(status) {
        if (this.selectedRowKeys.length <= 0) {
          this.$message.warning('请选择一条记录！');
          return false;
        } else {
          let ids = "";
          let that = this;
          that.selectedRowKeys.forEach(function (val) {
            ids += val + ",";
          });
          that.$confirm({
            title: "确认操作",
            content: "是否" + (status == 0 ? "启用" : "停用") + "选中供应商?",
            onOk: function () {
              postAction(that.url.batchDisable, {ids: ids, status: status}).then((res) => {
                if (res.success) {
                  that.$message.success(res.message);
                  that.loadData();
                  that.onClearSelected();
                } else {
                  that.$message.warning(res.message);
                }
              });
            }
          });
        }
      },
    }
  }
</script>

<style scoped>
  @import '~@assets/less/common.less'
</style>
<style>
  .validityFlag0{
  }
  .validityFlag1{
    background-color:#FF3333;
  }
  .validityFlag2{
    background-color:#FFFFCC;
  }
</style>