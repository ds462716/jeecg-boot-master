<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @cancel="handleCancel"
    :maskClosable=disableSubmit
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <!--<a-col :md="6" :sm="8">
            <a-form-item label="类型">
              <j-dict-select-tag-expand v-model="queryParam.printType" dictCode="bar_code_type" placeholder="请选择类型"/>
            </a-form-item>
          </a-col>-->
          <a-col :md="6" :sm="8">
            <a-form-item label="状态">
              <j-dict-select-tag-expand v-model="queryParam.codeState" dictCode="bar_code_state" placeholder="请选择状态"/>
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

    <!-- table区域-begin -->
    <div>
      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        @change="handleTableChange"
        >
      </a-table>
    </div>

    </a-spin>

    <template slot="footer">
      <a-button @click="close" style="margin-right: 15px;">关  闭</a-button>
    </template>
  </a-modal>
</template>

<script>
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import {getAction} from '@/api/manage'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import JDictSelectTagExpand from "@/components/dict/JDictSelectTagExpand"

  export default {
    name: "PdProductStockUniqueCodeModal",
    components: {
      JDictSelectTagExpand
    },
    mixins: [JeecgListMixin],
    data () {
      return {
        description: '库存关联条码表管理页面',
        visible: false,
        title:"操作",
        width:800,
        disableSubmit:false,
        confirmLoading: false,
        productStockId: "",
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
            title:'条码',
            align:"center",
            dataIndex: 'id'
          },
          {
            title:'条码序号',
            align:"center",
            dataIndex: 'uniqueCodeOrder'
          },
          {
            title:'类型',//1唯一码打印，0批量打印
            align:"center",
            dataIndex: 'printType',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['barCodeType'], text+"")
              }
            }
          },
          {
            title:'状态',//0正常，1已退货，2已用完
            align:"center",
            dataIndex: 'codeState',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['barCodeState'], text+"")
              }
            }
          },
          /*{
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' }
          }*/
        ],
        url: {
          list: "/pd/pdProductStockUniqueCode/list",
          delete: "/pd/pdProductStockUniqueCode/delete",
          deleteBatch: "/pd/pdProductStockUniqueCode/deleteBatch",
          exportXlsUrl: "/pd/pdProductStockUniqueCode/exportXls",
          importExcelUrl: "pd/pdProductStockUniqueCode/importExcel",
        },
        dictOptions:{
          barCodeState:[],
          barCodeType:[],
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
        initDictOptions('bar_code_state').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'barCodeState', res.result)
          }
        }),
          initDictOptions('bar_code_type').then((res) => {
            if (res.success) {
              this.$set(this.dictOptions, 'barCodeType', res.result)
            }
          })
      },
      handleCancel () {
        this.close()
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      show(record){
        if(record.barCodeType=="0"){
          this.loading = true;
          if(record.refBarCode){
            //解决转移到二级科室条码显示问题
            this.visible = true;
            let data = {
              id:record.refBarCode,
              uniqueCodeOrder:1,
              printType:"0",
              codeState:"0",
            };
            let array = new Array();
            array.push(data);
            this.dataSource = array;
            this.ipagination.total = 1;
            this.loading = false;
          }else{
            this.$message.error("请先打印条码!")
          }
        }else{
          this.productStockId = record.id;
          this.visible = true;
          this.loadData(1);
        }
      },
      loadData(arg) {
        if(this.productStockId){
          this.loading = true;
          //加载数据 若传入参数1则加载第一页的内容
          if (arg === 1) {
            this.ipagination.current = 1;
          }
          let params = this.getQueryParams();//查询条件
          params.productStockId = this.productStockId;
          getAction(this.url.list, params).then((res) => {
            if (res.success && res.result) {
              this.dataSource = res.result.records;
              this.ipagination.total = res.result.total;
              this.loading = false;
            }else{
              this.$message.error("系统异常!")
            }
          })
        }
      },
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>