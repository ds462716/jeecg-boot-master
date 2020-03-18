<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="值">
              <a-input placeholder="请输入值" v-model="queryParam.value"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="含义">
              <a-input placeholder="请输入含义" v-model="queryParam.meaning"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8" >
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->
    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="false"
        :loading="loading"
        :customRow="onClickRow"
        :rowSelection="{fixed:false,selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        :scroll="{ y: 280 }"
        @change="handleTableChange">

      </a-table>
    </div>
  </a-modal>
</template>

<script>
  import { getEncodingIdentifierList } from '@/api/api'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'

  export default {
    name: "PdEncodingIdentifierAddModal",
    mixins:[JeecgListMixin],
    components: {
      JDictSelectTag,
    },
    data () {
      return {
        description: '应用标识符表管理页面',
        visible: false,
        title:"操作",
        width:800,
        model:{},
        // 查询参数
        queryParam: {},
        // 表头
        columns: [
/*          {
            title: '序号',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },*/
          {
            title:'值',
            align:"center",
            width:60,
            dataIndex: 'value'
          },
          {
            title:'含义',
            align:"center",
            width:150,
            dataIndex: 'meaning'
          },
          {
            title:'类型',
            align:"center",
            width:150,
            dataIndex: 'type',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['type'], text+"")
              }
            }
          },
          {
            title:'长度',
            align:"center",
            width:60,
            dataIndex: 'length'
          },
          {
            title:'备注',
            align:"center",
            dataIndex: 'remarks'
          }
        ],
        url: {
          list: "/pd/pdEncodingIdentifier/NoPagingList",
        },
        dictOptions:{
          type:[],
        },
      }
    },
    methods: {
      loadData() {
        this.dataSource = []
        getEncodingIdentifierList({value:this.queryParam.value,meaning:this.queryParam.meaning}).then((res) => {
          if (res.success) {
            this.dataSource = res.result
          }
        })
      },
      initDictConfig(){
        initDictOptions('identifier_type').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'type', res.result)
          }
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      show(){
        this.visible = true;
      },
      handleCancel () {
        this.close()
      },
      handleOk () {
        const that = this;
        let formData = that.selectionRows;
        that.$emit('ok', formData);
        that.close();
      },
      /**
       * 点击行选中checkbox
       * @param record
       * @returns {{on: {click: on.click}}}
       */
      onClickRow(record) {
        return {
          on: {
            click: () => {
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
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>