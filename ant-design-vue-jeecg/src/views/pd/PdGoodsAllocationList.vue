<template>
  <a-row :gutter="10">
    <a-col :md="12" :sm="24">
      <a-card :bordered="false">
        <!-- 操作按钮区域 -->
        <!--<div class="table-operator">-->
          <!--<a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>-->
          <!--<a-button @click="batchDel" v-if="selectedRowKeys.length > 0" ghost type="primary" icon="delete" style="padding-left: 8px;">批量删除 </a-button>-->
        <!--</div>-->

        <!-- table区域-begin -->
        <div>
          <!--<div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">-->
            <!--<i class="anticon anticon-info-circle ant-alert-icon"></i>已选择&nbsp;<a style="font-weight: 600">{{-->
            <!--selectedRowKeys.length }}</a>项&nbsp;&nbsp;-->
            <!--<a style="margin-left: 24px" @click="onClearSelected">清空</a>-->
          <!--</div>-->

          <a-table
            :columns="departColumns"
            size="middle"
            rowKey="id"
            :pagination="false"
            :dataSource="departDataSource"
            :loading="loading"
            :customRow="departOnClickRow"
            :expandedRowKeys= "departExpandedRowKeys"
            @expand="departHandleExpand"
            :rowSelection="{selectedRowKeys: departSelectedRowKeys, onChange: departOnSelectChange}">
            <span slot="action" slot-scope="text, record">
              <a @click="showHuoweiList(record)" v-if="record.orgType!='1'">查看</a>
              <a-divider type="vertical" v-if="record.orgType!='1'" />
              <a @click="addHuoqu(record)" v-if="record.orgType!='1'">添加货区</a>
              <!--<a-divider type="vertical"/>-->
              <!--<a @click="addHuowei(record)" v-bind:disabled="record.orgType=='1'">添加货位</a>-->
            </span>
          </a-table>
        </div>
      </a-card>
    </a-col>

    <a-col :md="12" :sm="24" v-show="showGoodsCard">
      <a-card :bordered="false">
        <!--<div class="table-operator">-->
          <!--<a-button-->
            <!--@click="batchDel"-->
            <!--v-if="huoweiSelectedRowKeys.length > 0"-->
            <!--ghost-->
            <!--type="primary"-->
            <!--icon="delete">批量删除-->
          <!--</a-button>-->
        <!--</div>-->

        <div>
          <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
            <i class="anticon anticon-info-circle ant-alert-icon"></i>科室：&nbsp;<a style="font-weight: 600;margin-right: 40px">{{ departName }}</a>
            <i class="anticon anticon-info-circle ant-alert-icon"></i>已选择：&nbsp;<a style="font-weight: 600">{{ huoweiSelectedRowKeys.length }}</a>项&nbsp;&nbsp;
            <a style="margin-left: 15px" @click="onClearSelected">清空</a>
            <a-button ghost style="margin-left: 20px" @click="batchDelHuowei" v-if="huoweiSelectedRowKeys.length > 0" type="primary" icon="delete">批量删除 </a-button>
          </div>

          <a-table
            :columns="huoweiColumns"
            size="middle"
            rowKey="id"
            :pagination="false"
            :dataSource="huoweiDataSource"
            :loading="huoweiLoading"
            :customRow="huoweiOnClickRow"
            :expandedRowKeys= "huoweiExpandedRowKeys"
            @expand="huoweiHandleExpand"
            :rowSelection="{selectedRowKeys: huoweiSelectedRowKeys, onChange: huoweiOnSelectChange}">
            <span slot="action" slot-scope="text, record">
              <a @click="showHuoweiDetail(record)" >查看</a>
              <a-divider type="vertical"  />
              <a @click="editHuoweiDetail(record)" >修改</a>
              <a-divider type="vertical"  />
              <a @click="addHuowei(record)" v-bind:disabled="record.areaType=='2'">添加货位</a>
            </span>
          </a-table>
        </div>
      </a-card>
    </a-col>

    <pd-goods-allocation-modal ref="modalForm" @ok="modalFormOk"></pd-goods-allocation-modal>
  </a-row>
</template>

<script>
  import { filterObj } from '@/utils/util';
  import Vue from 'vue'
  import { ACCESS_TOKEN } from "@/store/mutation-types"
  import pick from 'lodash.pick'
  // import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import {httpAction, deleteAction, getAction} from '@/api/manage'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import { queryPdDepaTreeList,queryGoodsAllocationTreeList, searchByKeywords, deleteByDepartId,duplicateCheckHasDelFlag } from '@/api/api'
  import { makeWb } from '@/utils/wubi'
  import PdGoodsAllocationModal from "./modules/PdGoodsAllocationModal";

  export default {
    name: "PdGoodsAllocationList",
    // mixins:[JeecgListMixin],
    components: {
      PdGoodsAllocationModal
      // JDictSelectTag
    },
    data () {
      return {
        tokenHeader: {'X-Access-Token': Vue.ls.get(ACCESS_TOKEN)},
        form: this.$form.createForm(this),
        /* table加载状态 */
        loading:false,
        huoweiLoading:false,
        // title1:'',
        // title2:'',
        showGoodsCard:false,
        // showEdit:false,
        // showSubmit:false,
        // showSubNum:false,
        // goodsCode:"",
        qParam:{},
        departName:"",
        description: '货位管理页面',
        departExpandedRowKeys :[],
        departDataSource:[],
        departSelectedRowKeys:[],
        departSelectionRows: [],

        huoweiExpandedRowKeys :[],
        huoweiDataSource:[],
        huoweiSelectedRowKeys:[],
        huoweiSelectionRows: [],

        labelCol: {
          xs: {span: 24},
          sm: {span: 5}
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 16}
        },
        // 表头
        departColumns: [
          {
            title:'机构名称',
            dataIndex: 'departName'
          },
          {
            title:'编码',
            align:"center",
            dataIndex: 'orgCode',
            width: 200
          },
          {
            title: '操作',
            align:"center",
            dataIndex: 'action',
            scopedSlots: { customRender: 'action' },
            width: 200
          }
        ],
        huoweiColumns: [
          {
            title:'名称',
            dataIndex: 'name'
          },
          {
            title:'编码',
            align:"center",
            dataIndex: 'code',
            width: 200
          },
          {
            title: '操作',
            align:"center",
            dataIndex: 'action',
            scopedSlots: { customRender: 'action' },
            width: 200
          }

        ],
        url: {
          list: "/pd/pdDepart/queryTreeList",
          add: "/pd/pdGoodsAllocation/add",
          edit: "/pd/pdGoodsAllocation/edit",
          delete: "/pd/pdGoodsAllocation/delete",
          deleteBatch: "/pd/pdGoodsAllocation/deleteBatch",
          queryById: "/pd/pdGoodsAllocation/queryById",
          huoweiList: "/pd/pdGoodsAllocation/queryTreeList",
        },
        dictOptions:{
          orgType:[],
        },
      }
    },
    computed: {
      // importExcelUrl: function(){
      //   return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      // }
    },
    created() {
      if(!this.disableMixinCreated){
        console.log(' -- mixin created -- ')
        this.loadData();
        //初始化字典配置 在自己页面定义
        this.initDictConfig();
      }
    },
    methods: {
      loadData() {
        this.departDataSource = [];
        this.departExpandedRowKeys = [];
        this.departSelectedRowKeys = [];
        queryPdDepaTreeList().then((res) => {
          if (res.success) {
            this.departDataSource = res.result;
            let resultData = res.result;
            if(resultData.length>0){
              this.departRecursion(resultData);
            }
          }
        })
      },
      modalFormOk(departId) {
        // 新增/修改 成功时，重载列表
        this.showHuoweiList({id:departId,departName:this.departName});
      },
      initDictConfig(){
        initDictOptions('org_type').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'orgType', res.result)
          }
        })
      },
      departOnSelectChange(selectedRowKeys, selectionRows){
        this.departSelectedRowKeys = selectedRowKeys;
        this.departSelectionRows = selectionRows;
      },
      huoweiOnSelectChange(selectedRowKeys, selectionRows){
        this.huoweiSelectedRowKeys = selectedRowKeys;
        this.huoweiSelectionRows = selectionRows;
      },
      showHuoweiList(record){
        this.qParam = record;
        this.huoweiLoading = true;
        this.huoweiDataSource = [];
        this.huoweiExpandedRowKeys = [];
        this.huoweiSelectedRowKeys = [];
        this.departName = record.departName;
        getAction(this.url.huoweiList, {departId:record.id}).then(res => {
          if (res.success) {
            let resultData = res.result || [];
            this.huoweiDataSource = resultData;
            if(resultData.length>0){
              this.huoweiRecursion(resultData);
            }
          }
          this.huoweiLoading = false;
          this.showGoodsCard = true;
        }).finally(() => {
          this.huoweiLoading = false;
        })
      },
      showHuoweiDetail(record){
        if(record.areaType == "1"){
          this.$refs.modalForm.title = "查看货区";
          this.$refs.modalForm.title2 = "货区";
        }else{
          this.$refs.modalForm.title = "查看货位";
          this.$refs.modalForm.title2 = "货位";
        }
        this.$refs.modalForm.add(record);
        this.$refs.modalForm.disableSubmit = true;
      },
      editHuoweiDetail(record){
        if(record.areaType == "1"){
          this.$refs.modalForm.title = "修改货区";
          this.$refs.modalForm.title2 = "货区";
        }else{
          this.$refs.modalForm.title = "修改货位";
          this.$refs.modalForm.title2 = "货位";
        }
        this.$refs.modalForm.add(record);
        this.$refs.modalForm.disableSubmit = false;
      },
      addHuoqu(record) {
        this.showGoodsCard = true;
        this.departName = record.departName;
        let param = {};
        param.areaType = "1";
        param.departName = record.departName;
        param.departId = record.id;
        param.subCode = record.orgCode;
        this.$refs.modalForm.add(param);
        this.$refs.modalForm.title = "新增货区";
        this.$refs.modalForm.title2 = "货区";
        this.$refs.modalForm.disableSubmit = false;
        this.showHuoweiList(record);
      },
      addHuowei(record) {
        this.showGoodsCard = true;
        let param = {};
        param.areaType = "2";
        param.departName = record.departName;
        param.departId = record.departId;
        param.parentId = record.id;
        param.subCode = record.code;
        this.$refs.modalForm.add(param);
        this.$refs.modalForm.title = "新增货位";
        this.$refs.modalForm.title2 = "货位";
        this.$refs.modalForm.disableSubmit = false;
      },
      batchDelHuowei(){
        if(!this.url.deleteBatch){
          this.$message.error("请设置url.deleteBatch属性!")
          return
        }
        if (this.huoweiSelectedRowKeys.length <= 0) {
          this.$message.warning('请选择一条记录！');
          return;
        } else {
          var ids = "";
          for (var a = 0; a < this.huoweiSelectedRowKeys.length; a++) {
            ids += this.huoweiSelectedRowKeys[a] + ",";
          }
          var that = this;
          this.$confirm({
            title: "确认删除",
            content: "删除货区会同时删除该货区下的货位，货位有库存则不能删除。是否删除选中数据？",
            onOk: function () {
              that.loading = true;
              deleteAction(that.url.deleteBatch, {ids: ids}).then((res) => {
                if (res.success) {
                  that.$message.success(res.message);
                  that.showHuoweiList(that.qParam);
                  that.onClearSelected();
                } else {
                  that.$message.warning(res.message);
                  that.showHuoweiList(that.qParam);
                }
              }).finally(() => {
                that.loading = false;
              });
            }
          });
        }
      },
      /**
       * 点击行选中checkbox
       * @param record
       * @returns {{on: {click: on.click}}}
       */
      departOnClickRow(record) {
        // this.showHuowei(record);
      },
      huoweiOnClickRow(record) {
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
                  let index = this.huoweiSelectedRowKeys.indexOf(recordId);
                  if(index>=0){
                    this.huoweiSelectedRowKeys.splice(index, 1);
                  }else{
                    this.huoweiSelectedRowKeys.push(recordId);
                  }
                }
              }
            }
          }
        }
      },
      /**
       * 递归展开所有节点
       * @param resultData
       */
      departRecursion (resultData) {
        for(let i = 0;i<resultData.length;i++){
          let treeData = resultData[i];
          if(treeData.children && treeData.children.length>0){
            this.departExpandedRowKeys.push(resultData[i].id);
            this.departRecursion(treeData.children);
          }
        }
      },
      huoweiRecursion (resultData) {
        for(let i = 0;i<resultData.length;i++){
          let treeData = resultData[i];
          if(treeData.children && treeData.children.length>0){
            this.huoweiExpandedRowKeys.push(resultData[i].id);
            this.huoweiRecursion(treeData.children);
          }
        }
      },
      /**
       * 收拢元素或展开元素
       * @param expanded
       * @param record
       */
      departHandleExpand (expanded, record){
        let index  = this.departExpandedRowKeys.indexOf(record.id);
        if(index > -1){
          this.departExpandedRowKeys.splice(index,1);
        }else{
          this.departExpandedRowKeys.push(record.id);
        }
      },
      huoweiHandleExpand (expanded, record){
        let index  = this.huoweiExpandedRowKeys.indexOf(record.id);
        if(index > -1){
          this.huoweiExpandedRowKeys.splice(index,1);
        }else{
          this.huoweiExpandedRowKeys.push(record.id);
        }
      },
      onClearSelected() {
        this.huoweiSelectedRowKeys = [];
        this.huoweiSelectionRows = [];
      },
    }
  }
</script>
<style scoped>
  .ant-card-body .table-operator {
    margin: 15px;
  }

  .anty-form-btn {
    width: 100%;
    text-align: center;
  }

  .anty-form-btn button {
    margin: 0 5px;
  }

  .anty-node-layout .ant-layout-header {
    padding-right: 0
  }

  .header {
    padding: 0 8px;
  }

  .header button {
    margin: 0 3px
  }

  .ant-modal-cust-warp {
    height: 100%
  }

  .ant-modal-cust-warp .ant-modal-body {
    height: calc(100% - 110px) !important;
    overflow-y: auto
  }

  .ant-modal-cust-warp .ant-modal-content {
    height: 90% !important;
    overflow-y: hidden
  }

  #app .desktop {
    height: auto !important;
  }

  /** Button按钮间距 */
  .ant-btn {
    margin-left: 3px
  }

  .drawer-bootom-button {
    /*position: absolute;*/
    bottom: 0;
    width: 100%;
    border-top: 1px solid #e8e8e8;
    padding: 10px 16px;
    text-align: left;
    left: 0;
    background: #fff;
    border-radius: 0 0 2px 2px;
  }
</style>