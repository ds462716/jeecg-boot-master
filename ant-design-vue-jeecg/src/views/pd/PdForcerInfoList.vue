<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="智能柜编号">
              <a-input placeholder="请输入智能柜编号" v-model="queryParam.forcerNo"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="智能柜名称">
              <a-input placeholder="请输入智能柜名称" v-model="queryParam.forcerName"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
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
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
         @change="handleTableChange">
        <span slot="action" slot-scope="text, record">
           <a v-if="record.isDisable=='1'" @click="disableEdit(record,'0')">启用</a>
           <a v-if="record.isDisable=='0'"  @click="disableEdit(record,'1')">停用</a>
        </span>
      </a-table>
    </div>

  </a-card>
</template>

<script>
  import { JeecgListMixin,handleEdit} from '@/mixins/JeecgListMixin'
  import { deleteAction,httpAction,getAction } from '@/api/manage'
  import { filterObj } from '@/utils/util';
   import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import JDictSelectTagExpand from "@/components/dict/JDictSelectTagExpand"


  export default {
    name: "PdForcerInfoList",
    mixins:[JeecgListMixin],
    components: {
      JDictSelectTag,
      JDictSelectTagExpand
    },
    data () {
      return {
        dataSource:[],
        description: '智能柜管理页面',
        departData: [],
        departValue: undefined,
        notFoundContent:"未找到内容",
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
            title:'智能柜编号',
            align:"center",
            dataIndex: 'forcerNo'
          },
          {
            title:'智能柜名称',
            align:"center",
            dataIndex: 'forcerName'
          },
          {
            title:'启用状态',
            align:"center",
            dataIndex: 'isDisable',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['isDisable'], text+"")
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
            title:'MAC地址',
            align:"center",
            dataIndex: 'macAddress'
          },
          {
            title:'管理库房名称',
            align:"center",
            dataIndex: 'departName'
          },
          {
            title:'上级库房名称',
            align:"center",
            dataIndex: 'supDepartName'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/pd/pdForcerInfo/list",
          edit: "/pd/pdForcerInfo/edit"
        },
        dictOptions:{
          isDisable:[],
         },
      }
    },
    computed: {

    },
    methods: {
      disableEdit(record,isDisable){ //启用或停用操作
        const that = this;
         var param=record;
         param.isDisable=isDisable;
        httpAction(this.url.edit,param,"put").then((res)=>{
          if(res.success){
            that.$message.success(res.message);
            that.$emit('ok');
          }else{
            that.$message.warning(res.message);
          }
        })
      },



      initDictConfig(){ //静态字典值加载
        initDictOptions('disable_enable_status').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'isDisable', res.result)
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
                this.selectionRows.splice(index, 1);
              }else{
                this.selectedRowKeys.push(recordId);
                this.selectionRows.push(record);
              }
            }
          }
        }
      }
    }
  }
       
    }
  }
</script>
<style scoped>
</style>