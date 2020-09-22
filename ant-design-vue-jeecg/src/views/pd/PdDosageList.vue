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
            <a-form-item label="患者姓名">
              <a-input placeholder="请输入患者姓名" v-model="queryParam.patientInfo"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="住院号/门诊号">
              <a-input placeholder="请输入住院号" v-model="queryParam.inHospitalNo"></a-input>
            </a-form-item>
          </a-col>

          <a-col :md="6" :sm="8" >
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
          <!--<a @click="handleEdit(record)">编辑</a>-->
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
    <pdDosageReturned-modal ref="pdDosageReturnedForm" @ok="modalFormOk"></pdDosageReturned-modal>
    <pd-dosage-fee-modal ref="pdDosageFeeForm" @ok="modalFormOk"></pd-dosage-fee-modal>
    <pd-dosage-cncl-fee-modal  ref="pdDosageCnclFeeForm" @ok="modalFormOk"></pd-dosage-cncl-fee-modal>

    <!-- 丰城中医院收费模组 -->
    <pd-dosage-modal-f-c-z-y-y ref="pdDosageModalFCZYY" @ok="modalFormOk"></pd-dosage-modal-f-c-z-y-y>
    <pd-dosage-returned-modal-f-c-z-y-y ref="pdDosageReturnedModalFCZYY" @ok="modalFormOk"></pd-dosage-returned-modal-f-c-z-y-y>
    <pd-dosage-fee-modal-f-c-z-y-y ref="pdDosageFeeModalFCZYY" @ok="modalFormOk"></pd-dosage-fee-modal-f-c-z-y-y>
    <pd-dosage-cncl-fee-modal-f-c-z-z-y ref="pdDosageCnclFeeModalFCZZY" @ok="modalFormOk"></pd-dosage-cncl-fee-modal-f-c-z-z-y>

    <!-- 丰城人民医院收费模组 -->
    <pd-dosage-modal-f-c-r-m-y-y ref="pdDosageModalFCRMYY" @ok="modalFormOk"></pd-dosage-modal-f-c-r-m-y-y>
    <pd-dosage-returned-modal-f-c-r-m-y-y ref="pdDosageReturnedModalFCRMYY" @ok="modalFormOk"></pd-dosage-returned-modal-f-c-r-m-y-y>
    <pd-dosage-fee-modal-f-c-r-m-y-y ref="pdDosageFeeModalFCRMYY" @ok="modalFormOk"></pd-dosage-fee-modal-f-c-r-m-y-y>
    <pd-dosage-cncl-fee-modal-f-c-r-m-y-y ref="pdDosageCnclFeeModalFCRMYY" @ok="modalFormOk"></pd-dosage-cncl-fee-modal-f-c-r-m-y-y>

    <!-- 赣州五院收费模组 -->
    <pd-dosage-modal-g-z-w-y ref="pdDosageModalGZWY" @ok="modalFormOk"></pd-dosage-modal-g-z-w-y>
    <pd-dosage-returned-modal-g-z-w-y ref="pdDosageReturnedModalGZWY" @ok="modalFormOk"></pd-dosage-returned-modal-g-z-w-y>
    <pd-dosage-fee-modal-g-z-w-y ref="pdDosageFeeModalGZWY" @ok="modalFormOk"></pd-dosage-fee-modal-g-z-w-y>
    <pd-dosage-cncl-fee-modal-g-z-w-y  ref="pdDosageCnclFeeModalGZWY" @ok="modalFormOk"></pd-dosage-cncl-fee-modal-g-z-w-y>

    <pd-gzwy-dosage-modal ref="modalUniqueForm" @ok="modalFormOk"></pd-gzwy-dosage-modal>
    <pd-gzwy-dosage-unique-cncl-fee-modal ref="uniqueCnclFeeModal" @ok="modalFormOk"></pd-gzwy-dosage-unique-cncl-fee-modal>
    <pd-gzwy-returned-dosage-modal ref="uniqueReturnedModal" @ok="modalFormOk"></pd-gzwy-returned-dosage-modal>




    <!-- 赣州肿瘤医院收费模组 -->
    <pd-dosage-modal-g-z-z-l ref="pdDosageModalGZZL" @ok="modalFormOk"></pd-dosage-modal-g-z-z-l>
    <pd-dosage-returned-modal-g-z-z-l ref="pdDosageReturnedModalGZZL" @ok="modalFormOk"></pd-dosage-returned-modal-g-z-z-l>
    <pd-dosage-fee-modal-g-z-z-l ref="pdDosageFeeModalGZZL" @ok="modalFormOk"></pd-dosage-fee-modal-g-z-z-l>
    <pd-dosage-cncl-fee-modal-g-z-z-l  ref="pdDosageCnclFeeModalGZZL" @ok="modalFormOk"></pd-dosage-cncl-fee-modal-g-z-z-l>


  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import PdDosageModal from './modules/PdDosageModal'
  import PdDosageReturnedModal from './modules/PdDosageReturnedModal'
  import PdDosageFeeModal from '../pd/modules/PdDosageFeeModal'
  import PdDosageCnclFeeModal from '../pd/modules/PdDosageCnclFeeModal'
  import { deleteAction, getAction,downFile } from '@/api/manage'
  import PdDosageModalFCZYY from "../external/fengcheng/modules/PdDosageModalFCZYY";
  import PdDosageReturnedModalFCZYY from "../external/fengcheng/modules/PdDosageReturnedModalFCZYY";
  import PdDosageFeeModalFCZYY from "../external/fengcheng/modules/PdDosageFeeModalFCZYY";
  import PdDosageCnclFeeModalFCZZY from "../external/fengcheng/modules/PdDosageCnclFeeModalFCZZY";
  import PdDosageModalFCRMYY from "../external/fengcheng/modules/PdDosageModalFCRMYY";
  import PdDosageReturnedModalFCRMYY from "../external/fengcheng/modules/PdDosageReturnedModalFCRMYY";
  import PdDosageFeeModalFCRMYY from "../external/fengcheng/modules/PdDosageFeeModalFCRMYY";
  import PdDosageCnclFeeModalFCRMYY from "../external/fengcheng/modules/PdDosageCnclFeeModalFCRMYY";
  import PdDosageModalGZWY from '../external/ganzhouwuyuan/modules/PdDosageModalGZWY'
  import PdDosageReturnedModalGZWY from '../external/ganzhouwuyuan/modules/PdDosageReturnedModalGZWY'
  import PdDosageFeeModalGZWY from '../external/ganzhouwuyuan/modules/PdDosageFeeModalGZWY'
  import PdDosageCnclFeeModalGZWY from '../external/ganzhouwuyuan/modules/PdDosageCnclFeeModalGZWY'
  import PdDosageModalGZZL from '../external/ganzhouzhongliu/modules/PdDosageModalGZZL'
  import PdDosageReturnedModalGZZL from '../external/ganzhouzhongliu/modules/PdDosageReturnedModalGZZL'
  import PdDosageFeeModalGZZL from '../external/ganzhouzhongliu/modules/PdDosageFeeModalGZZL'
  import PdDosageCnclFeeModalGZZL from '../external/ganzhouzhongliu/modules/PdDosageCnclFeeModalGZZL'

  import PdGzwyDosageModal from '../external/ganzhouwuyuan/modules/PdDosageModalRefGZWY'
  import PdGzwyDosageUniqueCnclFeeModal from '../external/ganzhouwuyuan/modules/PdDosageUniqueCnclFeeModalGZWY'
  import PdGzwyReturnedDosageModal from '../external/ganzhouwuyuan/modules/PdDosageUniqueReturnedModalGZWY'

  export default {
    name: "PdDosageList",
    mixins:[JeecgListMixin],
    components: {
      PdDosageCnclFeeModalFCRMYY,
      PdDosageFeeModalFCRMYY,
      PdDosageReturnedModalFCRMYY,
      PdDosageModalFCRMYY,
      PdDosageCnclFeeModalFCZZY,
      PdDosageFeeModalFCZYY,
      PdDosageReturnedModalFCZYY,
      PdDosageModalFCZYY,
      PdDosageModal,
      PdDosageReturnedModal,
      PdDosageFeeModal,
      PdDosageCnclFeeModal,
      PdDosageModalGZWY,
      PdDosageReturnedModalGZWY,
      PdDosageFeeModalGZWY,
      PdDosageCnclFeeModalGZWY,
      PdDosageModalGZZL,
      PdDosageReturnedModalGZZL,
      PdDosageFeeModalGZZL,
      PdDosageCnclFeeModalGZZL,
      PdGzwyDosageModal,
      PdGzwyDosageUniqueCnclFeeModal,
      PdGzwyReturnedDosageModal
    },
    data () {
      return {
        hospitalCode:"",
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
            title:'患者姓名',
            align:"center",
            dataIndex: 'patientInfo'
          },
          {
            title:'门诊号',
            align:"center",
            dataIndex: 'outpatientNumber'
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
        },
        dictOptions:{},
      }
    },
    computed: {

    },
    methods: {
      initDictConfig(){
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
      handleAdd: function () {
        console.log(this.hospitalCode)
        if(this.hospitalCode=="FCZYY"){ // 丰城市中医院
          this.$refs.pdDosageModalFCZYY.add();
          this.$refs.pdDosageModalFCZYY.title = "新增";
          this.$refs.pdDosageModalFCZYY.disableSubmit = false;
        }else if(this.hospitalCode=="FCRMYY"){ // 丰城市人民医院
          this.$refs.pdDosageModalFCRMYY.add();
          this.$refs.pdDosageModalFCRMYY.title = "新增";
          this.$refs.pdDosageModalFCRMYY.disableSubmit = false;
        }else if(this.hospitalCode=="GZWY"){//赣州五院
            this.$refs.pdDosageModalGZWY.add();
            this.$refs.pdDosageModalGZWY.title = "新增";
            this.$refs.pdDosageModalGZWY.disableSubmit = false;
        }else if(this.hospitalCode=="GZZLYY"){//赣州肿瘤医院
          this.$refs.pdDosageModalGZZL.add();
          this.$refs.pdDosageModalGZZL.title = "新增";
          this.$refs.pdDosageModalGZZL.disableSubmit = false;
        }else{
          this.$refs.modalForm.add();
          this.$refs.modalForm.title = "新增";
          this.$refs.modalForm.disableSubmit = false;
        }
      },
      //收费
      inventoryFee(record){
        if(this.hospitalCode=="FCZYY"){ // 丰城市中医院
          this.$refs.pdDosageFeeModalFCZYY.edit(record);
          this.$refs.pdDosageFeeModalFCZYY.title="收费";
          this.$refs.pdDosageFeeModalFCZYY.disableSubmit = false;
        }else if(this.hospitalCode=="FCRMYY"){ // 丰城市人民医院
          this.$refs.pdDosageFeeModalFCRMYY.edit(record);
          this.$refs.pdDosageFeeModalFCRMYY.title="收费";
          this.$refs.pdDosageFeeModalFCRMYY.disableSubmit = false;
        }else if(this.hospitalCode=="GZWY"){ //赣州五院
          this.$refs.pdDosageFeeModalGZWY.edit(record);
          this.$refs.pdDosageFeeModalGZWY.title="收费";
          this.$refs.pdDosageFeeModalGZWY.disableSubmit = false;
        }else{
          this.$refs.pdDosageFeeForm.edit(record);
          this.$refs.pdDosageFeeForm.title="收费";
          this.$refs.pdDosageFeeForm.disableSubmit = false;
        }
      },
      //取消收费
      inventoryCnclFee(record){
        if(this.hospitalCode=="FCZYY") { // 丰城市中医院
          this.$refs.pdDosageCnclFeeModalFCZZY.edit(record);
          this.$refs.pdDosageCnclFeeModalFCZZY.title="取消收费";
          this.$refs.pdDosageCnclFeeModalFCZZY.disableSubmit = false;
        }else if(this.hospitalCode=="FCRMYY"){ // 丰城市人民医院
          this.$refs.pdDosageCnclFeeModalFCRMYY.edit(record);
          this.$refs.pdDosageCnclFeeModalFCRMYY.title="取消收费";
          this.$refs.pdDosageCnclFeeModalFCRMYY.disableSubmit = false;
        }else if(this.hospitalCode=="GZWY"){ // 赣州五院
          if(record.dosageType=="0"){
            //唯一码取消收费
            this.$refs.uniqueCnclFeeModal.edit(record);
            this.$refs.uniqueCnclFeeModal.title="取消收费";
            this.$refs.uniqueCnclFeeModal.disableSubmit = false;
          }else{
            this.$refs.pdDosageCnclFeeModalGZWY.edit(record);
            this.$refs.pdDosageCnclFeeModalGZWY.title = "取消收费";
            this.$refs.pdDosageCnclFeeModalGZWY.disableSubmit = false;
          }
        }else{
          this.$refs.pdDosageCnclFeeForm.edit(record);
          this.$refs.pdDosageCnclFeeForm.title="取消收费";
          this.$refs.pdDosageCnclFeeForm.disableSubmit = false;
        }
      },
      //库存还回
      inventoryReturned(record){
        if(this.hospitalCode=="FCZYY"){ // 丰城市中医院
          record.dhyCharged = "1";
          this.$refs.pdDosageReturnedModalFCZYY.edit(record);
          this.$refs.pdDosageReturnedModalFCZYY.title = "库存还回";
          this.$refs.pdDosageReturnedModalFCZYY.disableSubmit = false;
        }else if(this.hospitalCode=="FCRMYY"){ // 丰城市人民医院
          record.dhyCharged = "1";
          this.$refs.pdDosageReturnedModalFCRMYY.edit(record);
          this.$refs.pdDosageReturnedModalFCRMYY.title = "库存还回";
          this.$refs.pdDosageReturnedModalFCRMYY.disableSubmit = false;
        }else if(this.hospitalCode=="GZWY"){ // 赣州五院
          if(record.dosageType=="0"){
            //唯一码还回
            this.$refs.uniqueReturnedModal.edit(record);
            this.$refs.uniqueReturnedModal.title="库存还回";
            this.$refs.uniqueReturnedModal.disableSubmit = false;
          }else {
            this.$refs.pdDosageReturnedModalGZWY.edit(record);
            this.$refs.pdDosageReturnedModalGZWY.title = "库存还回";
            this.$refs.pdDosageReturnedModalGZWY.disableSubmit = false;
          }
        }else{
          this.$refs.pdDosageReturnedForm.edit(record);
          this.$refs.pdDosageReturnedForm.title="库存还回";
          this.$refs.pdDosageReturnedForm.disableSubmit = false;
        }
      },
      // 详情
      handleDetail(record){
        if(this.hospitalCode=="FCZYY"){ // 丰城市中医院
          record.dhyCharged = "";
          this.$refs.pdDosageReturnedModalFCZYY.edit(record);
          this.$refs.pdDosageReturnedModalFCZYY.title = "详情";
          this.$refs.pdDosageReturnedModalFCZYY.disableSubmit = true;
        }else if(this.hospitalCode=="FCRMYY"){ // 丰城市人民医院
          record.dhyCharged = "";
          this.$refs.pdDosageReturnedModalFCRMYY.edit(record);
          this.$refs.pdDosageReturnedModalFCRMYY.title = "详情";
          this.$refs.pdDosageReturnedModalFCRMYY.disableSubmit = true;
        }else if(this.hospitalCode=="GZWY"){
          if(record.dosageType=="0"){
            //唯一码详情
            this.$refs.modalUniqueForm.edit(record);
            this.$refs.modalUniqueForm.title="详情";
            this.$refs.modalUniqueForm.disableSubmit = true;
          }else {
            this.$refs.pdDosageReturnedModalGZWY.edit(record);
            this.$refs.pdDosageReturnedModalGZWY.title = "详情";
            this.$refs.pdDosageReturnedModalGZWY.disableSubmit = true;
          }
        }else{
          this.$refs.pdDosageReturnedForm.edit(record);
          this.$refs.pdDosageReturnedForm.title="详情";
          this.$refs.pdDosageReturnedForm.disableSubmit = true;
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