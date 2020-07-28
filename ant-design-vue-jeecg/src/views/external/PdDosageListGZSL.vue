<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="用量单号">
              <a-input placeholder="请输入用量单号" v-model="queryParam.dosageNo"></a-input>
            </a-form-item>
          </a-col>
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
            <a-form-item label="病人姓名">
              <a-input placeholder="请输入病人姓名" v-model="queryParam.patientInfo"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="住院号">
              <a-input placeholder="请输入住院号" v-model="queryParam.inHospitalNo"></a-input>
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

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button @click="handleUniqueAdd" type="primary" icon="plus">唯一码使用</a-button>
      <!--<a-button type="primary" icon="download" @click="handleExportXls('用量表')">导出</a-button>-->
      <!--<a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>-->
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
          <a @click="handleDetail(record)">查看</a>
          <a-divider type="vertical" />
           <a @click="inventoryFee(record)">收费</a>
          <a-divider type="vertical" />
           <a @click="inventoryCnclFee(record)">取消收费</a>
          <a-divider type="vertical" />
           <a @click="inventoryReturned(record)">库存还回</a>
        </span>
      </a-table>
    </div>

    <pdDosage-modal ref="modalForm" @ok="modalFormOk"></pdDosage-modal>
    <pd-gzsl-dosage-modal ref="modalUniqueForm" @ok="modalFormOk"></pd-gzsl-dosage-modal>
    <pdDosageReturned-modal ref="pdDosageReturnedForm" @ok="modalFormOk"></pdDosageReturned-modal>
    <pd-gzsl-returned-dosage-modal ref="uniqueReturnedModal" @ok="modalFormOk"></pd-gzsl-returned-dosage-modal>
    <pd-dosage-unique-cncl-fee-modal ref="uniqueCnclFeeModal" @ok="modalFormOk"></pd-dosage-unique-cncl-fee-modal>
    <pd-dosage-fee-modal ref="pdDosageFeeForm" @ok="modalFormOk"></pd-dosage-fee-modal>
    <pd-dosage-cncl-fee-modal  ref="pdDosageCnclFeeForm" @ok="modalFormOk"></pd-dosage-cncl-fee-modal>
    <pd-dosage-list-modal ref="pdDosageListModal" @ok="modalFormOk"></pd-dosage-list-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import PdDosageModal from '../pd/modules/NewPdDosageModal'
  import PdGzslDosageModal from './modules/PdDosageModalRefGZSL'
  import PdGzslReturnedDosageModal from './modules/PdDosageUniqueReturnedModalGZSL'
  import PdDosageUniqueCnclFeeModal from './modules/PdDosageUniqueCnclFeeModal'
  import PdDosageListModal from '../pd/modules/NewPdDosageListModal'
  import PdDosageReturnedModal from '../pd/modules/PdDosageReturnedModal'
  import PdDosageFeeModal from '../pd/modules/PdDosageFeeModal'
  import PdDosageCnclFeeModal from '../pd/modules/PdDosageCnclFeeModal'
  import { deleteAction, getAction,downFile } from '@/api/manage'
  import { filterObj } from '@/utils/util'

  export default {
    name: "PdDosageList",
    mixins:[JeecgListMixin],
    components: {
      PdDosageModal,
      PdDosageReturnedModal,
      PdDosageFeeModal,
      PdDosageCnclFeeModal,
      PdDosageListModal,
      PdGzslDosageModal,
      PdGzslReturnedDosageModal,
      PdDosageUniqueCnclFeeModal
    },
    data () {
      return {
        departData: [],
        departValue: undefined,
        notFoundContent:"未找到内容",
        description: '用量表管理页面',
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
            title:'用量科室',
            align:"center",
            dataIndex: 'departName'
          },
          {
            title:'用量单号',
            align:"center",
            dataIndex: 'dosageNo'
          },
          {
            title:'用量日期',
            align:"center",
            dataIndex: 'dosageDate'
          },
          {
            title:'用量总数',
            align:"center",
            dataIndex: 'totalSum'
          },
          {
            title:'用量总金额',
            align:"center",
            dataIndex: 'totalPrice'
          },
          {
            title:'病人姓名',
            align:"center",
            dataIndex: 'patientInfo'
          },
          {
            title:'住院号',
            align:"center",
            dataIndex: 'inHospitalNo'
          },
          {
            title:'操作人',
            align:"center",
            dataIndex: 'dosageByName'
          },
          {
            title:'操作时间',
            align:"center",
            dataIndex: 'updateTime'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/pd/pdDosage/list",
          delete: "/pd/pdDosage/delete",
          deleteBatch: "/pd/pdDosage/deleteBatch",
          exportXlsUrl: "/pd/pdDosage/exportXls",
          queryDepart: "/pd/pdDepart/queryListTree",
        },
        dictOptions:{},
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
        param.departIds = this.queryParam.departIds+"";
        return filterObj(param);
      },
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
          if (res.success) {
            this.dataSource = res.result.pageList.records;
            this.ipagination.total = res.result.pageList.total;
            this.hospitalCode = res.result.hospitalCode;
          }
          if(res.code===510){
            this.$message.warning(res.message)
          }
          this.loading = false;
        })
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


      initDictConfig(){
      },
      inventoryFee(record){//收费
        this.$refs.pdDosageFeeForm.edit(record);
        this.$refs.pdDosageFeeForm.title="收费";
        this.$refs.pdDosageFeeForm.disableSubmit = false;
      },

      inventoryCnclFee(record){
        if(record.dosageType=="0"){
          //唯一码取消收费
          this.$refs.uniqueCnclFeeModal.edit(record);
          this.$refs.uniqueCnclFeeModal.title="取消收费";
          this.$refs.uniqueCnclFeeModal.disableSubmit = false;

        }else{
          //取消收费
          this.$refs.pdDosageCnclFeeForm.edit(record);
          this.$refs.pdDosageCnclFeeForm.title="取消收费";
          this.$refs.pdDosageCnclFeeForm.disableSubmit = false;
        }
      },

      inventoryReturned(record){//库存还回
        if(record.dosageType=="0"){
          //唯一码还回
          this.$refs.uniqueReturnedModal.edit(record);
          this.$refs.uniqueReturnedModal.title="库存还回";
          this.$refs.uniqueReturnedModal.disableSubmit = false;
        }else{
          this.$refs.pdDosageReturnedForm.edit(record);
          this.$refs.pdDosageReturnedForm.title="库存还回";
          this.$refs.pdDosageReturnedForm.disableSubmit = false;
        }

      },
      handleDetail(record){//新增
        if(record.dosageType=="0"){
          //唯一码使用
          this.$refs.modalUniqueForm.edit(record);
          this.$refs.modalUniqueForm.title="详情";
          this.$refs.modalUniqueForm.disableSubmit = true;
        }else{
          this.$refs.pdDosageListModal.edit(record);
          this.$refs.pdDosageListModal.title="详情";
          this.$refs.pdDosageListModal.disableSubmit = true;
        }
      },
      //唯一码使用
      handleUniqueAdd(){
        this.$refs.modalUniqueForm.add();
        this.$refs.modalUniqueForm.title = "唯一码使用";
        this.$refs.modalUniqueForm.disableSubmit = false;
      },
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>