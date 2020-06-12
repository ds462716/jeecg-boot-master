<template>
  <j-modal
    :visible="visible"
    :width="popModal.width"
    :title="popModal.title"
    :lockScroll="popModal.lockScroll"
    :fullscreen="popModal.fullscreen"
    :switchFullscreen="popModal.switchFullscreen"
    @cancel="handleCancel"
  >
    <a-spin :spinning="confirmLoading">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :md="5" :sm="8">
              <a-form-item label="检验项目编号">
                <a-input placeholder="请输入检验项目编号" v-model="queryParam.code"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="5" :sm="8">
              <a-form-item label="检验项目名称">
                <a-input placeholder="请输入检验项目名称" v-model="queryParam.name"></a-input>
              </a-form-item>
            </a-col>
            <a-col :md="5" :sm="8">
              <a-form-item label="检验科室">
                <a-input placeholder="请输入检验科室名称" v-model="queryParam.testDepartName"></a-input>
              </a-form-item>
            </a-col>
             <template v-if="toggleSearchStatus">
               <a-col :md="5" :sm="8">
                 <a-form-item label="扣减类型">
                   <j-dict-select-tag v-model="queryParam.deductuinType" dictCode="deductuin_type"/>
                  </a-form-item>
               </a-col>
               <a-col :md="5" :sm="8">
                <a-form-item label="产品编号">
                  <a-input placeholder="请输入产品编号" v-model="queryParam.number"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="5" :sm="8">
                <a-form-item label="产品名称">
                  <a-input placeholder="请输入产品名称" v-model="queryParam.productName"></a-input>
                </a-form-item>
              </a-col>
           </template>
            <a-col :md="6" :sm="8">
            <span style="float: right;overflow: hidden;" class="table-page-search-submitButtons">
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
      <a-table
        ref="table"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :expandedRowKeys= "expandedRowKeys"
        :customRow="onClickRow"
        :rowSelection="{fixed:false,type:'radio',selectedRowKeys: selectedRowKeys, onSelectAll:onSelectAll,onSelect:onSelect,onChange: onSelectChange}"
        @expand="handleExpand"
        @change="handleTableChange">

        <a-table
          slot="expandedRowRender"
          slot-scope="text"
          size="middle"
          bordered
          rowKey="id"
          :columns="innerColumns"
          :dataSource="innerData"
          :pagination="false"
          :loading="subloading"
        >
        </a-table>
      </a-table>
    </a-spin>
    <template slot="footer">
      <a-button @click="handleCancel" style="margin-right: 15px;">取  消</a-button>
      <a-button @click="handleOk" type="primary" style="margin-right: 15px;">确定</a-button>
    </template>
  </j-modal>
</template>

<script>

  import { httpAction,getAction } from '@/api/manage'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import { filterObj } from '@/utils/util';

  export default {
    name: "ExChoosePackageExInspectionItemsListModel",
    mixins:[JeecgListMixin],
    components: {
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"选择检验项目包",
        width:1200,
        visible: false,
        innerData:[],
        expandedRowKeys:[],
        subloading:false,
        confirmLoading: false,

        dataSource2: [],
        selectedRowKeys: [],
        selectedRows: [],
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: 'id',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          { title:'检验项目编号', align:"center", dataIndex: 'code' },
          { title:'检验项目名称', align:"center", dataIndex: 'name' },
          { title:'检验科室', align:"center", dataIndex: 'testDepartNames' },
          { title:'扣减类型', align:"center", dataIndex: 'deductuinType',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['deductuinType'], text+"")
              }
            }
          },
          { title:'创建时间', align:"center", dataIndex: 'createTime',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          { title:'备注', align:"center", dataIndex: 'remarks' }
        ],
        innerColumns:[
          {
            title: '#',
            dataIndex: 'id',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          { title: '产品id', align:"center", dataIndex: 'productId',
            colSpan: 0,
            customRender: (value, row, index) => {
              const obj = {
                attrs: {colSpan:0},
              };
              return obj;
            },
          },
          { title:'产品名称', align:"center", dataIndex: 'productName' },
          { title:'产品编号', align:"center", dataIndex: 'productNumber' },
          { title:'规格', align:"center", dataIndex: 'spec' },
          { title:'单位', align:"center", dataIndex: 'unitName' },
          { title:'规格单位', align:"center", dataIndex: 'specUnitName' },
          { title:'规格数量', align:"center", dataIndex: 'specQuantity' },
          { title:'生产厂家', align:"center", dataIndex: 'venderName' },
          { title:'供应商', align:"center", dataIndex: 'supplierName' },
          { title:'产品类型', align:"center", dataIndex: 'productFlagName' },

          { title:'用量', align:"center", dataIndex: 'count' },
        ],
        url: {
          list: "/pd/pdUsePackage/list",
          queryPdUsePackageListByIds: "/pd/pdUsePackage/queryPdUsePackageListByIds",
          chooseDetailList:"/pd/pdUsePackage/queryPdUsePackageDetailByMainId",
        },
        dictOptions:{
          deductuinType:[],
        },
        popModal: {
          title: '选择检验项目',
          visible: false,
          width: '100%',
          // width: '1200',
          style: { top: '20px' },
          switchFullscreen: false,  //缩放按钮
          lockScroll: false,
          fullscreen: true,
        },
      }
    },
    methods: {
      handleExpand(expanded, record){
        this.expandedRowKeys=[];
        this.innerData=[];
        if(expanded===true){
          this.subloading = true;
          this.expandedRowKeys.push(record.id);
          getAction(this.url.chooseDetailList, {id: record.id}).then((res) => {
            if (res.success) {
              this.subloading = false;
              this.innerData = res.result;
            }
          });
        }
      },
      close () {
        this.selectedRowKeys = [];
        this.selectionRows = [];
        this.dataSource2 = [];
        this.$emit('close');
        this.visible = false;
      },
      show() {
        this.visible = true;
      },
      handleOk () {
        if(this.selectionRows.length > 0){
          let ids = ''
          for (let item of this.selectionRows){
            ids += item.id + ','
          }
          getAction(this.url.queryPdUsePackageListByIds, {ids:ids}).then((res) => {
            if (res.success) {
              let data = {};
              data.pdPdUsePackageList = res.result;
              this.$emit('ok', data);
              this.close();
            }
          });
        }else{
          this.$message.error("请选择一行数据!")
        }
      },
      handleCancel () {
        this.close();
      },
      popupCallback(row){

      },
      initDictConfig(){ //静态字典值加载
        initDictOptions('deductuin_type').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'deductuinType', res.result)
          }
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
        delete param.queryDate; //范围参数不传递后台，传后台会报错
        return filterObj(param);
      },

      onSelectAll(selected, selectedRows, changeRows) {
        if (selected === true) {
          for (var a = 0; a < changeRows.length; a++) {
            this.dataSource2.push(changeRows[a]);
          }
        } else {
          for (var b = 0; b < changeRows.length; b++) {
            this.dataSource2.splice(this.dataSource2.indexOf(changeRows[b]), 1);
          }
        }
      },
      onSelect(record, selected) {
        if (selected === true) {
          this.dataSource2.push(record);
        } else {
          var index = this.dataSource2.indexOf(record);
          if (index >= 0) {
            this.dataSource2.splice(this.dataSource2.indexOf(record), 1);
          }

        }
      },
      onSelectChange(selectedRowKeys, selectedRows) {
        this.selectedRowKeys = selectedRowKeys;
        this.selectionRows = selectedRows;
      },
      onClearSelected() {
        this.selectedRowKeys = [];
        this.selectionRows = [];
      },
      handleDelete: function (record) {
        this.dataSource2.splice(this.dataSource2.indexOf(record), 1);
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
                if(parseInt(lie)-parseInt(cellIndex) > 0){
                  //操作那一行
                  let recordId = record.id;
                  let index = this.selectedRowKeys.indexOf(recordId);
                  this.selectedRowKeys = [];
                  this.selectionRows = [];
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
      },
    }
  }
</script>