<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="出库单号">
              <a-input placeholder="请输入出库单号" v-model="queryParam.recordNo"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="入库库房">
              <a-select
                showSearch
                placeholder="请选择入库库房"
                :supplierId="departValue"
                :defaultActiveFirstOption="false"
                :allowClear="true"
                :showArrow="true"
                :filterOption="false"
                @search="departHandleSearch"
                @focus="departHandleSearch"
                :notFoundContent="notFoundContent"
                v-model="queryParam.inDepartId"
              >
                <a-select-option v-for="d in departList" :key="d.id">{{d.departName}}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="出库类型">
              <j-dict-select-tag-expand v-model="queryParam.outType" dictCode="out_type"/>
            </a-form-item>
          </a-col>
          <!--<a-col :md="6" :sm="8">-->
            <!--<a-form-item label="提交状态">-->
              <!--<j-dict-select-tag v-model="queryParam.submitStatus" dictCode="submit_status"/>-->
            <!--</a-form-item>-->
          <!--</a-col>-->
          <a-col :md="6" :sm="8">
            <a-form-item label="审核状态">
              <j-dict-select-tag-expand v-model="queryParam.auditStatus" dictCode="audit_status"/>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="提交日期">
              <a-range-picker @change="dateChange" v-model="queryParam.queryDate"/>
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
    <!--<div class="table-operator">-->
      <!--<a-button type="primary" icon="download" @click="handleExportXls('出库记录表')">导出</a-button>-->
    <!--</div>-->

    <!-- table区域-begin -->
    <div>
      <!--<div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">-->
        <!--<i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项-->
        <!--<a style="margin-left: 24px" @click="onClearSelected">清空</a>-->
      <!--</div>-->

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
          <a v-if="record.auditStatus=='1'" @click="handleExamine(record)">审核</a> &nbsp;&nbsp;&nbsp;
          <a @click="handleDetail(record)">详情</a>


          <!--<a-divider type="vertical" />-->
          <!--<a-dropdown>-->
            <!--<a class="ant-dropdown-link">更多 <a-icon type="down" /></a>-->
            <!--<a-menu slot="overlay">-->
              <!--<a-menu-item>-->
                <!--<a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">-->
                  <!--<a>删除</a>-->
                <!--</a-popconfirm>-->
              <!--</a-menu-item>-->
            <!--</a-menu>-->
          <!--</a-dropdown>-->
        </span>

      </a-table>
    </div>

    <pd-stock-record-return-examine-modal ref="modalForm" @ok="modalFormOk"></pd-stock-record-return-examine-modal>
  </a-card>
</template>

<script>

  import { filterObj } from '@/utils/util';
  import {getAction} from '@/api/manage'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import JDictSelectTagExpand from "@/components/dict/JDictSelectTagExpand"
  import PdStockRecordReturnExamineModal from "./modules/PdStockRecordReturnExamineModal";

  export default {
    name: "PdStockRecordReturnExamineList",
    mixins:[JeecgListMixin],
    components: {
      PdStockRecordReturnExamineModal,
      JDictSelectTagExpand
    },
    data () {
      return {
        description: '出库记录表管理页面',

        notFoundContent:"未找到内容",
        departValue: undefined,
        departList:[],
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
            title:'出库单号',
            align:"center",
            dataIndex: 'recordNo'
          },
          {
            title:'出库库房',
            align:"center",
            dataIndex: 'outDepartName'
          },
          {
            title:'入库库房',
            align:"center",
            dataIndex: 'inDepartName'
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
            title:'出库类型',
            align:"center",
            dataIndex: 'outType',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['outType'], text+"")
              }
            }
          },
          {
            title:'操作人',
            align:"center",
            dataIndex: 'submitByName'
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
          list: "/pd/pdStockRecordReturn/examineList",
          // delete: "/pd/pdStockRecordIn/delete",
          // deleteBatch: "/pd/pdStockRecordIn/deleteBatch",
          exportXlsUrl: "/pd/pdStockRecordReturn/exportXls",
          departList:"/pd/pdDepart/getSysDepartList",
          // importExcelUrl: "pd/pdStockRecordIn/importExcel",
        },
        dictOptions:{
        },
      }
    },
    computed: {
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
        initDictOptions('out_type').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'outType', res.result)
          }
        })
      },
      handleExamine: function (record) {
        this.$refs.modalForm.edit(record);
        this.$refs.modalForm.title = "审核";
        this.$refs.modalForm.disableSubmit = false;
      },
      dateChange: function (value, dateString) {
        this.queryParam.queryDateStart=dateString[0];
        this.queryParam.queryDateEnd=dateString[1];
      },
      // 部门下拉框搜索
      departHandleSearch(value){
        getAction(this.url.departList,{departName:value}).then((res)=>{
          if (!res.success) {
            this.cmsFailed(res.message);
          }
          this.departList = res.result;
        })
      },
      getQueryParams() {
        //获取查询条件
        let sqp = {}
        if(this.superQueryParams){
          sqp['superQueryParams']=encodeURI(this.superQueryParams)
        }
        var param = Object.assign(sqp, this.queryParam, this.isorter ,this.filters);
        param.field = this.getQueryField();
        param.pageNo = this.ipagination.current;
        param.pageSize = this.ipagination.pageSize;
        delete param.queryDate; //范围参数不传递后台，传后台会报错
        return filterObj(param);
      },
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>