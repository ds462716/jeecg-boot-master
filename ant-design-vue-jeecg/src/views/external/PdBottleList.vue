<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="科室">
              <!--<a-input placeholder="请选择科室" v-model="queryParam.deptName"></a-input>-->
              <a-select
                mode="multiple"
                showSearch
                :departId="departValue"
                :defaultActiveFirstOption="false"
                :allowClear="true"
                :showArrow="true"
                :filterOption="false"
                @search="departHandleSearch"
                @focus="departHandleSearch"
                :notFoundContent="notFoundContent"
                v-model="queryParam.departIds"
                placeholder="请选择科室"
              >
                <a-select-option v-for="d in departData" :key="d.id">{{d.departName}}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="开瓶操作人">
              <a-input placeholder="请输入操作人名称" v-model="queryParam.boottleBy"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="产品名称">
              <a-input placeholder="请输入产品名称" v-model="queryParam.productName"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="6" :sm="8">
            <a-form-item label="唯一码编号">
              <a-input placeholder="请输入唯一码编号" v-model="queryParam.refBarCode"></a-input>
            </a-form-item>
          </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="闭瓶原因">
                <j-dict-select-tag placeholder="请选择闭瓶原因"  v-model="queryParam.closeRemarks" dictCode="close_remarks"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="所属仪器">
                <a-select
                  showSearch
                  :departId="instrValue"
                  :defaultActiveFirstOption="false"
                  :allowClear="true"
                  :showArrow="true"
                  :filterOption="false"
                  @search="instrHandleSearch"
                  @focus="instrHandleSearch"
                  :notFoundContent="notFoundContent"
                  v-model="queryParam.instrCode"
                  placeholder="请选择仪器"
                >
                  <a-select-option v-for="d in instrData" :key="d.instrCode">{{d.instrName}}</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </template>
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
      <a-button @click="handleBottle" type="primary" icon="plus">开瓶</a-button>
      <a-button @click="handleClose" type="primary" icon="delete"  >闭瓶</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('试剂用量明细')">导出</a-button>
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
        :scroll="tableScroll"
        :rowSelection="{fixed:true,selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">
      </a-table>
    </div>
    <pd-bottle-modal ref="pdBottleModal" @ok="modalFormOk"></pd-bottle-modal>
  </a-card>
</template>
<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import pdBottleModal from './modules/PdBottleModal'
  import { deleteAction, getAction,downFile } from '@/api/manage'
  import { filterObj } from '@/utils/util';
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'

  export default {
    name: "PdBottleList",
    mixins:[JeecgListMixin],
    components: {
      pdBottleModal,
    },
    data () {
      return {
        description: '开闭瓶管理页面',
        departData: [],
        departValue: undefined,
        instrData: [],
        instrValue: undefined,
        notFoundContent:"未找到内容",
        // 表头
        columns: [
          /*{
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },*/
          {
            title:'开瓶操作人',
            align:"center",
            dataIndex: 'boottleBy'
          },
          {
            title:'开瓶时间',
            align:"center",
            dataIndex: 'boottleDate'
          },
          {
            title:'所属仪器',
            align:"center",
            dataIndex: 'instrName'
          },
          {
            title:'产品名称',
            align:"center",
            dataIndex: 'productName'
          },
          {
            title:'唯一码编号',
            align:"center",
            dataIndex: 'refBarCode'
          },
          /*{
            title:'库存明细ID',
            align:"center",
            dataIndex: 'stockId'
          },*/
          {
            title:'批次号',
            align:"center",
            dataIndex: 'batchNo'
          },
          {
            title:'有效期',
            align:"center",
            dataIndex: 'expDate'
          },
          {
            title:'使用数量',
            align:"center",
            dataIndex: 'specNum'
          },
          {
            title:'规格数量',
            align:"center",
            dataIndex: 'specQuantity'
          },
          {
            title:'规格单位',
            align:"center",
            dataIndex: 'unitName'
          },
          {
            title:'闭瓶时间',
            align:"center",
            dataIndex: 'closeDate'
          },
          {
            title:'闭瓶操作人',
            align:"center",
            dataIndex: 'closeBy'
          },

          {
            title:'闭瓶原因',
            align:"center",
            dataIndex: 'closeRemarks',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['closeRemarks'], text+"")
              }
            }
          },
          {
            title:'所属部门',
            align:"center",
            dataIndex: 'departName'
          },
        ],
        url: {
          list: "/pd/pdBottleInf/list",
          queryDepart: "/pd/pdDepart/queryListTree",
          exportXlsUrl: "/pd/pdBottleInf/exportXls",
          queryExLabInstrInf:"/ex/exLabInstrInf/getExLabInstrInf",
        },
        dictOptions:{
          closeRemarks:[],
        },
        tableScroll:{x :1500},
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {

      // 重写loadData方法
      loadData(arg) {
        if(!this.url.list){
          this.$message.error("请设置url.list属性!")
          return
        }
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        var params = this.getQueryParams();//查询条件
        this.loading = true;
        getAction(this.url.list, params).then((res) => {
          if (res.success){
            this.dataSource = res.result.records;
            this.ipagination.total = res.result.total;
          }
          this.loading = false;
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
        param.departIds = this.queryParam.departIds+"";
        return filterObj(param);
      },


      handleBottle() { //开瓶
        this.$refs.pdBottleModal.edit(1);
        this.$refs.pdBottleModal.title = "开瓶";
        this.$refs.pdBottleModal.disableSubmit = false;
      },

      handleClose(){ //闭瓶
        this.$refs.pdBottleModal.edit(2);
        this.$refs.pdBottleModal.title = "闭瓶";
        this.$refs.pdBottleModal.disableSubmit = false;
      },
      //科室查询start
      departHandleSearch(value) {
        getAction(this.url.queryDepart,{departName:value}).then((res)=>{
          if (!res.success) {
            this.cmsFailed(res.message);
          }
          this.departData = res.result;
        })
      },
      //科室查询end

      //仪器查询start
      instrHandleSearch(value) {
        getAction(this.url.queryExLabInstrInf,{queryType:"0",instrName:value}).then((res)=>{
          if (!res.success) {
            this.cmsFailed(res.message);
          }
          this.instrData = res.result;
        })
      },
      //仪器查询end

      modalFormOk(){
        this.loadData();
      },

      /**重写导出方法**/
      handleExportXls(fileName){
        if(!fileName || typeof fileName != "string"){
          fileName = "导出文件"
        }
        fileName = fileName + "_" + new Date().toLocaleString();
        let param = this.getQueryParams();//查询条件
        if(this.selectedRowKeys && this.selectedRowKeys.length>0){
          param['selections'] = this.selectedRowKeys.join(",")
        }
        console.log("导出参数",param)
        downFile(this.url.exportXlsUrl,param).then((data)=>{
          if (!data) {
            this.$message.warning("文件下载失败")
            return
          }
          if (typeof window.navigator.msSaveBlob !== 'undefined') {
            window.navigator.msSaveBlob(new Blob([data],{type: 'application/vnd.ms-excel'}), fileName+'.xls')
          }else{
            let url = window.URL.createObjectURL(new Blob([data],{type: 'application/vnd.ms-excel'}))
            let link = document.createElement('a')
            link.style.display = 'none'
            link.href = url
            link.setAttribute('download', fileName+'.xls')
            document.body.appendChild(link)
            link.click()
            document.body.removeChild(link); //下载完成移除元素
            window.URL.revokeObjectURL(url); //释放掉blob对象
          }
        })
      },



      initDictConfig(){
        initDictOptions('close_remarks').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'closeRemarks', res.result)
          }
        })
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
 </style>