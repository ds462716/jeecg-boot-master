<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="盘点科室">
              <a-select
                showSearch
                :targetDepartId="departValue"
                :defaultActiveFirstOption="false"
                :allowClear="true"
                :showArrow="true"
                :filterOption="false"
                @search="departHandleSearch"
                @change="departHandleChange"
                @focus="departHandleSearch"
                :notFoundContent="notFoundContent"
                v-model="queryParam.targetDepartId"
                placeholder="请选择科室"
              >
                <a-select-option v-for="d in departData" :key="d.value">{{d.text}}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="提交状态">
              <j-dict-select-tag-expand v-model="queryParam.checkStatus" dictCode="submit_status" placeholder="请选择提交状态"/>
            </a-form-item>
          </a-col>
         <!-- <template :md="6" v-if="toggleSearchStatus">
              &lt;!&ndash;待扩展查询条件&ndash;&gt;
          </template>-->
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
    
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增盘点单</a-button>
      <a-dropdown v-if="selectedRowKeys.length > 0">
      <a-menu slot="overlay">
        <!--<a-menu-item key="1" @click="batchLocking"><a-icon type="lock"/>锁定库房</a-menu-item>-->
        <!--<a-menu-item key="2" @click="batchUnlock"><a-icon type="unlock"/>解锁库房</a-menu-item>-->
        <a-menu-item key="3" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
      </a-menu>
      <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
    </a-dropdown>

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
        :customRow="onClickRow"
        :rowSelection="{fixed:false,selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">
          <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)" v-bind:disabled="record.checkStatus=='2'">修改</a>
          <a-divider type="vertical"/>
          <a @click="handleDetail(record)">详情</a>
          <a-divider type="vertical" />
          <a @click="locking(record)" v-bind:disabled="record.checkStatus=='2'">锁定库房</a>
          <a-divider type="vertical" />
          <a @click="unlock(record)" v-bind:disabled="record.checkStatus=='2'">解锁库房</a>
          <a-divider type="vertical" />
          <a @click="handleDelete(record)" v-bind:disabled="record.checkStatus=='2'">删除</a>


         <!-- <a-dropdown>
            <a class="ant-dropdown-link" v-bind:disabled="record.checkStatus=='2'" >更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item v-show="record.checkStatus=='1' || record.checkStatus=='3'"> &lt;!&ndash;待提交、已撤回&ndash;&gt;
                <a-popconfirm title="确定锁定吗?"  @confirm="() => locking(record)"  >
                  <a>锁定库房</a>
                </a-popconfirm>
              </a-menu-item>
              <a-menu-item v-show="record.checkStatus=='1' || record.checkStatus=='3'"> &lt;!&ndash;待提交、已撤回&ndash;&gt;
                <a-popconfirm title="确定解锁吗?解锁后需要按键盘F5进行刷新"  @confirm="() => unlock(record)"  >
                  <a>解锁库房</a>
                </a-popconfirm>
              </a-menu-item>
              <a-menu-item v-show="record.checkStatus=='1' || record.checkStatus=='3'"> &lt;!&ndash;待提交、已撤回&ndash;&gt;
                <a-popconfirm title="确定删除吗?"  @confirm="() => handleDelete(record.id)"  >
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>-->
        </span>
      </a-table>
    </div>

    <pdProductStockCheck-modal ref="modalForm" @ok="modalFormOk"></pdProductStockCheck-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin ,handleEdit,batchDel} from '@/mixins/JeecgListMixin'
  import { deleteAction,getAction } from '@/api/manage'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import PdProductStockCheckModal from './modules/PdProductStockCheckModal'
  import JDictSelectTagExpand from "@/components/dict/JDictSelectTagExpand"

  let timeout;
  let currentValue;

  function fetch(value, callback,url) {
    if (timeout) {
      clearTimeout(timeout);
      timeout = null;
    }
    currentValue = value;

    function fake() {
      getAction(url,{departName:value}).then((res)=>{
        if (!res.success) {
          this.cmsFailed(res.message);
        }
        if (currentValue === value) {
          const result = res.result;
          const data = [];
          result.forEach(r => {
            data.push({
              value: r.id,
              text: r.departName,
            });
          });
          callback(data);
        }
      })
    }
    timeout = setTimeout(fake, 0);
  }
  export default {
    name: "PdProductStockCheckList",
    mixins:[JeecgListMixin],
    components: {
      PdProductStockCheckModal,
      JDictSelectTagExpand
    },
    data () {
      return {
        description: '盘点记录表管理页面',
        departData: [],
        departValue: undefined,
        notFoundContent:"未找到内容",
        // 表头
        columns: [
          {
            title: '序号',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'盘点编号',
            align:"center",
            dataIndex: 'checkNo'
          },
          {
            title:'科室',
            align:"center",
            dataIndex: 'deptName'
          },
          {
            title:'盘点日期',
            align:"center",
            dataIndex: 'checkDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'盘点人',
            align:"center",
            dataIndex: 'checkName'
          },
          {
            title:'盘点总数量',
            align:"center",
            dataIndex: 'shouldCount'
          },
          {
            title:'已盘总数量',
            align:"center",
            dataIndex: 'checkCount'
          },
          {
            title:'盘盈盘亏数量',
            align:"center",
            dataIndex: 'profitLossCount'
          },
          {
            title:'提交状态',
            align:"center",
            dataIndex: 'checkStatus',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['checkStatus'], text+"")
              }
            }
          },
          {
            title:'审批状态',
            align:"center",
            dataIndex: 'auditStatus',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['auditStatus'], text+"")
              }
            }
          },
          {
            title:'库房状态',
            align:"center",
            dataIndex: 'lockingState',
            customRender:(text)=>{
              if(!text){
                return ''
              }else{
                return filterMultiDictText(this.dictOptions['lockingState'], text+"")
              }
            }
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/pd/pdProductStockCheck/list",
          delete: "/pd/pdProductStockCheck/delete",
          deleteBatch: "/pd/pdProductStockCheck/deleteBatch",
          queryDepart: "/pd/pdDepart/queryListTree",
          locking: "/pd/pdProductStockCheckPermission/locking",
          unlock: "/pd/pdProductStockCheckPermission/unlock",
        },
        dictOptions:{
          checkStatus:[],
          lockingState:[]
        },
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
        let params = this.getQueryParams();//查询条件
        this.loading = true;
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records;
            this.ipagination.total = res.result.total;
          }
          this.loading = false;
        })
      },
      //科室查询start
      departHandleSearch(value) {
        fetch(value, data => (this.departData = data),this.url.queryDepart);
      },
      departHandleChange(value) {
        this.departValue = value;
        fetch(value, data => (this.departData = data),this.url.queryDepart);
      },
      //科室查询end
      batchDel: function () { //批量删除
        if (this.selectionRows.length <= 0) {
          this.$message.warning('请选择一条记录！');
          return;
        } else {
          let ids = "";
          let checkNos1="",checkNos2="";
          for (let a = 0; a < this.selectionRows.length; a++) {
            let checkStatus= this.selectionRows[a].checkStatus;
            let lockingState= this.selectionRows[a].lockingState;
            if(checkStatus == '2'){
              checkNos1+=this.selectionRows[a].checkNo + ",";
            }

            if(lockingState == '1'){
              checkNos2+=this.selectionRows[a].checkNo + ",";
            }

            ids += this.selectionRows[a].id + ",";
          }
          if(checkNos1 != ""){
            this.$message.warning("盘点编号["+checkNos1.substring(0,checkNos1.length-1)+"]已提交，不允许删除！")
            return
          }
          if(checkNos2 != ""){
            this.$message.warning("盘点编号["+checkNos2.substring(0,checkNos2.length-1)+"]库房已锁定，不允许删除！")
            return
          }
          var that = this;
          this.$confirm({
            title: "确认删除",
            content: "是否删除选中数据?",
            onOk: function () {
              that.loading = true;
              deleteAction(that.url.deleteBatch, {ids: ids}).then((res) => {
                if (res.success) {
                  that.$message.success(res.message);
                  that.loadData();
                  that.onClearSelected();
                } else {
                  that.$message.warning(res.message);
                }
              }).finally(() => {
                that.loading = false;
              });
            }
          });
        }
      },
      initDictConfig(){
        initDictOptions('submit_status').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'checkStatus', res.result)
          }
        }),
        initDictOptions('audit_status').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'auditStatus', res.result)
          }
        }),
        initDictOptions('locking_state').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'lockingState', res.result)
          }
        })

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
      /**
       * 锁定
       */
      locking(record){
        let that = this;
        deleteAction(that.url.locking, {id: record.targetDepartId,recordId:record.id}).then((res) => {
          if (res.success) {
            that.$message.success(res.message);
            that.loadData();
          } else {
            that.$message.warning(res.message);
          }
        });
      },
      batchLocking(){ //批量删除
        if (this.selectionRows.length <= 0) {
          this.$message.warning('请选择一条记录！');
          return;
        } else {
          let ids = "";
          let checkNos1="",checkNos2="";
          for (let a = 0; a < this.selectionRows.length; a++) {
            let checkStatus= this.selectionRows[a].checkStatus;
            let lockingState= this.selectionRows[a].lockingState;
            if(checkStatus == '2'){
              checkNos1+=this.selectionRows[a].checkNo + ",";
            }

            if(lockingState == '1'){
              checkNos2+=this.selectionRows[a].checkNo + ",";
            }

            ids += this.selectionRows[a].id + ",";
          }
          if(checkNos1 != ""){
            this.$message.warning("盘点编号["+checkNos1.substring(0,checkNos1.length-1)+"]已提交，不允许锁定！")
            return
          }
          if(checkNos2 != ""){
            this.$message.warning("盘点编号["+checkNos2.substring(0,checkNos2.length-1)+"]库房已锁定，不能再次锁定！")
            return
          }

        }
      },

      /**
       * 解锁
       */
      unlock(record){
        let that = this;
        deleteAction(that.url.unlock, {id: record.targetDepartId,recordId:record.id}).then((res) => {
          if (res.success) {
            that.$message.success(res.message);
            that.loadData();
          } else {
            that.$message.warning(res.message);
          }
        });
      },
      batchUnlock(){

      },
       
    }
  }
</script>
<style scoped>

</style>