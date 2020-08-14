<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="申购模板编号">
              <a-input placeholder="请输入申购模板编号" v-model="queryParam.tempNo"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="模板名称">
              <a-input placeholder="请输入申购模板名称" v-model="queryParam.tempName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="模板类型">
              <j-dict-select-tag-expand  placeholder="请选择模板类型" v-model="queryParam.tempType" dictCode="temp_type"/>
            </a-form-item>
          </a-col>
          <template :md="6" v-if="toggleSearchStatus">
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
        :customRow="onClickRow"
        :rowSelection="{fixed:false,selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">
        <span slot="action" slot-scope="text, record">
          <a  @click="handleEdit(record)">修改</a>
          <a-divider type="vertical" />
          <a @click="handleDetail(record)">详情</a>
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

    <pdPurchaseTemp-modal ref="modalForm" @ok="modalFormOk"></pdPurchaseTemp-modal>
   </a-card>
</template>

<script>

  import { JeecgListMixin,batchDel} from '@/mixins/JeecgListMixin'
  import { deleteAction } from '@/api/manage'
  import { filterObj } from '@/utils/util';
  import PdPurchaseTempModal from './modules/PdPurchaseTempModal'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import JDictSelectTagExpand from "@/components/dict/JDictSelectTagExpand"
   export default {
    name: "PdPurchaseTempList",
    mixins:[JeecgListMixin],
    components: {
      PdPurchaseTempModal,
      JDictSelectTagExpand,
     },
    data () {
      return {
        description: '申购模板管理页面',
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
            title:'模板编号',
            align:"center",
            dataIndex: 'tempNo'
          },
          {
            title:'模板名称',
            align:"center",
            dataIndex: 'tempName'
          },
          {
            title:'创建人',
            align:"center",
            dataIndex: 'realname'
          },
          {
            title:'模板类型',
            align:"center",
            dataIndex: 'tempType',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['tempType'], text+"")
              }
            }
          },
          {
            title:'创建日期',
            align:"center",
            dataIndex: 'createTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'所属科室',
            align:"center",
            dataIndex: 'departName'
          },

          {
            title:'产品数量',
            align:"center",
            dataIndex: 'totalNum'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/pd/pdPurchaseTemp/list",
          delete: "/pd/pdPurchaseTemp/delete"
        },
        dictOptions:{
          tempType:[],

        },

      }
    },
    computed: {

    },
    methods: {
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
        return filterObj(param);
      },




      handleEdit: function (record) { //编辑
          this.$refs.modalForm.edit(record);
          this.$refs.modalForm.title = "编辑";
          this.$refs.modalForm.disableSubmit = false;
      },

      handleDetail:function(record){  //详情
          this.$refs.modalForm.edit(record);
          this.$refs.modalForm.title="详情";
          this.$refs.modalForm.disableSubmit = true;
      },


      initDictConfig(){ //静态字典值加载
        initDictOptions('temp_type').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'tempType', res.result)
          }
        })

      },
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

       
    }
  }
</script>
<style scoped>
</style>