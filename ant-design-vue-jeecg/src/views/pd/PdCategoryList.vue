<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button
        @click="batchDel"
        v-if="selectedRowKeys.length > 0"
        ghost
        type="primary"
        icon="delete">批量删除
      </a-button>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i>已选择&nbsp;<a style="font-weight: 600">{{
        selectedRowKeys.length }}</a>项&nbsp;&nbsp;
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        :columns="columns"
        size="middle"
        :pagination="false"
        :dataSource="dataSource"
        :loading="loading"
        :customRow="onClickRow"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}">
        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">
              更多 <a-icon type="down"/>
            </a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a href="javascript:;" @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a href="javascript:;" @click="handleAddSub(record)">添加二级分类</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>
        <!-- 字符串超长截取省略号显示 -->
        <span slot="url" slot-scope="text">
          <j-ellipsis :value="text" :length="25"/>
        </span>
        <!-- 字符串超长截取省略号显示-->
        <span slot="component" slot-scope="text">
          <j-ellipsis :value="text"/>
        </span>
      </a-table>
    </div>

    <pdCategory-modal ref="modalForm" @ok="modalFormOk"></pdCategory-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import PdCategoryModal from './modules/PdCategoryModal'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import { getCategoryList } from '@/api/api'

  export default {
    name: "PdCategoryList",
    mixins:[JeecgListMixin],
    components: {
      JDictSelectTag,
      PdCategoryModal
    },
    data () {
      return {
        description: '产品分类管理页面',
        // 表头
        columns: [
          {
            title:'名称',
            dataIndex: 'name'
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
            title:'自定义查询码',
            align:"center",
            dataIndex: 'zdy'
          },
          {
            title:'类型',
            dataIndex: 'type',
            customRender:(text)=>{
              text +="";
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['type'], text+"")
              }
            }
          },
          {
            title:'创建日期',
            dataIndex: 'createTime'
          },
          {
            title:'更新日期',
            dataIndex: 'updateTime'
          },
          {
            title:'备注',
            dataIndex: 'remarks'
          },
          {
            title: '操作',
            dataIndex: 'action',
            scopedSlots: { customRender: 'action' },
            width: 150
          }
        ],
        url: {
          list: "/pd/pdCategory/list",
          delete: "/pd/pdCategory/delete",
          deleteBatch: "/pd/pdCategory/deleteBatch",
          exportXlsUrl: "/pd/pdCategory/exportXls",
          importExcelUrl: "pd/pdCategory/importExcel",
        },
        dictOptions:{
         type:[],
        },
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      loadData() {
        this.dataSource = []
        getCategoryList().then((res) => {
          if (res.success) {
            this.dataSource = res.result
          }
        })
      },
      initDictConfig(){
        initDictOptions('category_type').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'type', res.result)
          }
        })
      },
      handleAddSub(record) {
        this.$refs.modalForm.title = "添加二级分类";
        this.$refs.modalForm.localCategoryType = 1;
        this.$refs.modalForm.disableSubmit = false;
        this.$refs.modalForm.edit({route:true,'parentId':record.id});
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
      }
       
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>