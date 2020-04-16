<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="巡查单号">
              <a-input placeholder="请输入巡查单号" v-model="queryParam.patrolNo"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="巡查科室">
              <a-select
                showSearch
                placeholder="请选择巡查科室"
                :supplierId="departValue"
                :defaultActiveFirstOption="false"
                :showArrow="true"
                :filterOption="false"
                @search="departHandleSearch"
                @focus="departHandleSearch"
                :notFoundContent="notFoundContent"
                v-model="queryParam.patrolDepartId"
              >
                <a-select-option v-for="d in departList" :key="d.id">{{d.departName}}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="巡查日期">
              <a-range-picker @change="dateChange" v-model="queryParam.queryDate"/>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <!--<a @click="handleToggleSearch" style="margin-left: 8px">-->
              <!--{{ toggleSearchStatus ? '收起' : '展开' }}-->
              <!--<a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>-->
              <!--</a>-->
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->
    
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
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
          <!--<a @click="handleEdit(record)">编辑</a>-->
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

    <pdStoreroomPatrol-modal ref="modalForm" @ok="modalFormOk"></pdStoreroomPatrol-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import PdStoreroomPatrolModal from './modules/PdStoreroomPatrolModal'
  import {httpAction, deleteAction, getAction} from '@/api/manage'
  import { filterObj } from '@/utils/util';

  export default {
    name: "PdStoreroomPatrolList",
    mixins:[JeecgListMixin],
    components: {
      PdStoreroomPatrolModal
    },
    data () {
      return {
        description: '巡查列表',

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
            title:'巡查单号',
            align:"center",
            dataIndex: 'patrolNo'
          },
          {
            title:'科室',
            align:"center",
            dataIndex: 'patrolDepartName'
          },
          {
            title:'巡查产品量',
            align:"center",
            dataIndex: 'patrolCount'
          },
          {
            title:'合格产品量',
            align:"center",
            dataIndex: 'passCount'
          },
          {
            title:'不合格产品量',
            align:"center",
            dataIndex: 'failCount'
          },
          {
            title:'温度',
            align:"center",
            dataIndex: 'temperature'
          },
          {
            title:'湿度',
            align:"center",
            dataIndex: 'wetness'
          },
          {
            title:'巡查员',
            align:"center",
            dataIndex: 'patrolMan'
          },
          {
            title:'巡查日期',
            align:"center",
            dataIndex: 'patrolDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
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
          list: "/pd/pdStoreroomPatrol/list",
          departList:"/pd/pdDepart/getSysDepartList",
          // delete: "/pd/pdStoreroomPatrol/delete",
          // deleteBatch: "/pd/pdStoreroomPatrol/deleteBatch",
          // exportXlsUrl: "/pd/pdStoreroomPatrol/exportXls",
          // importExcelUrl: "pd/pdStoreroomPatrol/importExcel",
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
      initDictConfig(){
      },
      dateChange: function (value, dateString) {
        this.queryParam.queryDateStart=dateString[0];
        this.queryParam.queryDateEnd=dateString[1];
      },
      // 部门下拉框搜索
      departHandleSearch(value){
        getAction(this.url.departList,{departName:value,parentFlag:"0"}).then((res)=>{
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
  @import '~@assets/less/common.less';
</style>