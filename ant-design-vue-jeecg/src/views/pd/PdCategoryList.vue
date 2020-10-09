<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
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
                <a href="javascript:;" v-if="record.type=='0'" @click="handleAddSub(record)">添加二级分类</a>
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
  import {postAction} from '@/api/manage'

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
            title:'自定义码',
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
          batchDisable: "pd/pdCategory/batchDisable",
        },
        dictOptions:{
          type:[],
          status:[],
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
        this.dataSource = [];
        this.loading = true;
        getCategoryList().then((res) => {
          if (res.success) {
            this.dataSource = res.result;
          }
          this.loading = false;
        })
      },
      initDictConfig(){
        initDictOptions('category_type').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'type', res.result)
          }
        }),
        initDictOptions('disable_enable_status').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'status', res.result)
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
            content: "是否" + (status == 0 ? "启用" : "停用") + "选中分类及其子类?",
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