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
            <a-form-item label="产品名称">
              <a-input placeholder="请输入产品名称" v-model="queryParam.productName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="5" :sm="8">
            <a-form-item label="是否试剂">
              <j-dict-select-tag type="list" v-model="queryParam.productFlag" dictCode="yn" placeholder="请选择"/>
            </a-form-item>
          </a-col>
          <template :md="6" v-if="toggleSearchStatus">
            <a-col :md="6" :sm="8">
              <a-form-item label="产品编号">
                <a-input placeholder="请输入产品编号" v-model="queryParam.number"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="规格">
                <a-input placeholder="请输入规格" v-model="queryParam.spec"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="型号">
                <a-input placeholder="请输入型号" v-model="queryParam.version"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="是否久存">
                <j-dict-select-tag v-model="queryParam.isLong" dictCode="pd_isLong"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="8">
              <a-form-item label="是否过期">
                <j-dict-select-tag v-model="queryParam.expStatus" dictCode="exp_status"/>
              </a-form-item>
            </a-col>
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
    <div class="numberWARAP">
      <div class="total">总数量：<span id="pCount">{{this.validatorRules.pCount}}</span></div>
      <div class="nearTime">近效期数量：<span id="jCount">{{this.validatorRules.jCount}}</span></div>
      <div class="overTime">过期数量：<span id="gCount">{{this.validatorRules.gCount}}</span></div>
    <!--  <div class="overTime">久存产品数量：<span id="isLcount">{{this.validatorRules.isLcount}}</span></div>
      <div class="overTime">超出库房上下限产品数量：<span id="limtCount">{{this.validatorRules.limtCount}}</span></div>-->
    </div>
    <!-- 操作按钮区域 -->
     <div class="table-operator">
      <a-button @click="handleUpdate('Up')" type="primary" icon="plus">设置库存上限</a-button>
      <a-button @click="handleUpdate('Down')" type="primary" icon="plus">设置库存下限</a-button>
      <!--<a-button type="primary" icon="download" @click="handleExportXls('库存明细')">导出</a-button>-->
    </div>
    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        CLASS="changeColor"
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
        :scroll="tableScroll"
        :rowSelection="{fixed:false,selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">库存明细</a>&nbsp;&nbsp;&nbsp;
         <a  @click="handleRecordEdit(record)">出入库明细</a>
        </span>

      </a-table>
    </div>
<!--设置库存上下限弹出框-->
    <pdProductStockTotal-modal ref="modalForm1" @ok="modalFormOk"></pdProductStockTotal-modal>
    <!--库存明细查看页面-->
    <pdProductStock-modal ref="stockForm" @ok="modalFormOk"></pdProductStock-modal>
    <!--出入库明细查看页面-->
    <pd-stock-record-detail-info-modal ref="stockForm2" @ok="modalFormOk"></pd-stock-record-detail-info-modal>
  </a-card>
</template>
<script>

  import { JeecgListMixin ,handleEdit} from '@/mixins/JeecgListMixin'
  import PdProductStockTotalModal from './modules/PdProductStockTotalModal'
  import PdProductStockModal from './modules/PdProductStockModal'
  import PdStockRecordDetailInfoModal from './modules/PdStockRecordDetailInfoModal'
  import { getAction } from '@/api/manage'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import { filterObj } from '@/utils/util';


  export default {
    name: "PdProductStockTotalList",
    mixins:[JeecgListMixin],
    components: {
      PdProductStockTotalModal,
      PdProductStockModal,
      PdStockRecordDetailInfoModal
    },
    data () {
      return {
        description: '库存管理页面',
        validatorRules: {
          pCount:{},//总数量
          gCount:{},//过期数量
          jCount:{},//近效期数量
          isLcount:{},//久存产品数量
          limtCount:{},////超出库房上下限产品数量
        },
        departData: [],
        departValue: undefined,
        notFoundContent:"未找到内容",
        // 表头
        columns: [
          /*{
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
            title:'所属科室',
            align:"center",
            dataIndex: 'deptName'
          },
          {
            title:'产品名称',
            align:"center",
            dataIndex: 'productName'
          },
          {
            title:'产品类型',
            align:"center",
            dataIndex: 'productFlagName'
          },
          {
            title:'产品编号',
            align:"center",
            dataIndex: 'number'
          },
          {
            title:'规格',
            align:"center",
            dataIndex: 'spec'
          },
          {
            title:'型号',
            align:"center",
            dataIndex: 'version'
          },
          {
            title:'单位',
            align:"center",
            dataIndex: 'unitName'
          },
          {
            title:'库存数量',
            align:"center",
            dataIndex: 'stockNum'
          },
          {
            title:'库存上限',
            align:"center",
            dataIndex: 'limitUp'
          },
          {
            title:'库存下限',
            align:"center",
            dataIndex: 'limitDown'
          },
          {
            title:'自动补货量',
            align:"center",
            dataIndex: 'autoNum'
          },
          {
            title:'是否过期',
            align:"center",
            dataIndex: 'expStatus',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['expStatus'], text+"")
              }
            }
          },
          {
            title:'是否久存',
            align:"center",
            dataIndex: 'isLong',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['isLong'], text+"")
              }
            }
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:140,
            scopedSlots: { customRender: 'action' }
          }

        ],
        url: {
          list: "/pd/pdProductStockTotal/list",
          exportXlsUrl: "/pd/pdProductStockTotal/exportXls",
          queryDepart: "/pd/pdDepart/queryListTree",
        },
        dictOptions:{
          expStatus:[],
          isLong:[]
        },
        tableScroll:{x :10*140+30},
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      loadData(arg) {
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        var params = this.getQueryParams();//查询条件
        this.loading = true;
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.validatorRules.pCount=res.result.pCount;
            this.validatorRules.gCount=res.result.gCount;
            this.validatorRules.jCount=res.result.jCount;
            this.validatorRules.isLcount=res.result.isLcount;
            this.validatorRules.limtCount=res.result.limtCount;
            this.dataSource = res.result.records.records;
            this.ipagination.total = res.result.records.total;
          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
          this.loading = false;
        })
      },
      handleEdit: function (record) { //编译
        this.$refs.stockForm.edit(record);
        this.$refs.stockForm.title = "库存明细";
        this.$refs.stockForm.disableSubmit = false;
      },
      handleRecordEdit: function (record) { //编译
        this.$refs.stockForm2.edit(record);
        this.$refs.stockForm2.title = "出入库明细";
        this.$refs.stockForm2.disableSubmit = false;
      },
      handleUpdate(type) { //设置库存上限
        if (this.selectedRowKeys.length <= 0) {
          this.$message.warning('请勾选需要设置的数据！');
          return;
        }
        let  ids="";
        for (var a = 0; a < this.selectedRowKeys.length; a++) {
          ids += this.selectedRowKeys[a] + ",";
        }
        var name="下限";
        if(type=='Up'){
            name="上限"
        }
        this.$refs.modalForm1.edit({"ids":ids,"type":type});
        this.$refs.modalForm1.title = "设置"+name;
        this.$refs.modalForm1.disableSubmit = false;
      },

      initDictConfig(){ //静态字典值加载
        initDictOptions('exp_status').then((res) => { //是否过期字典转换
          if (res.success) {
            this.$set(this.dictOptions, 'expStatus', res.result)
          }
        })
        initDictOptions('pd_isLong').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'isLong', res.result)
          }
        })
      },
      setdataCss(record,index) {
        let expire = record.expStatus;
        return "expire"+expire;
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
    }
  }
</script>
<style scoped>
  .numberWARAP{width:100%;height:30px;line-height:30px;margin:20px 0;}
  .numberWARAP>div{float:left;width:33%;height:30px;line-height:30px;color:#666;font-size:16px;text-align:center;border-right:1px solid #ccc;}
  .numberWARAP>div:nth-child(3){border:none;}
  .changeColor .red td,.changeColor .red td a{color: red}
</style>

<style>
  .expire0{
  }
  .expire1{
    background-color:#FFFFCC;
  }
  .expire2{
    background-color:#FF3333;
  }
</style>