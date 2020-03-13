<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="申购编号">
              <a-input placeholder="请输入申购编号" v-model="queryParam.orderNo"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="申购科室名称">
              <a-input placeholder="请输入申购科室名称" v-model="queryParam.deptName"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :md="6" :sm="8">
              <a-form-item label="审核状态">
                <a-select v-model="queryParam.auditStatus" placeholder="请选择审核状态">
                  <a-select-option value="1">待审核</a-select-option>
                  <a-select-option value="2">审核通过</a-select-option>
                  <a-select-option value="3">审核不通过</a-select-option>
                </a-select>
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
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="batchAduit('2')" type="primary" icon="plus">批量审核</a-button>
      <a-button @click="onClearSelected" type="primary" icon="plus">合并并提交</a-button>
      <a-button @click="batchAduit('3')" type="primary" icon="plus">批量拒绝</a-button>

      <!--<a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="onClearSelected"><a-icon type="delete"/>删除</a-menu-item>
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
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">
        <span slot="action" slot-scope="text, record">
          <a v-if="record.auditStatus=='1'" @click="handleEdit(record)">审核</a>&nbsp;&nbsp;&nbsp;
          <a href="javascript:;" @click="handleDetail(record)">详情</a>
        </span>
      </a-table>
    </div>
    <pd-purchase-examine-modal ref="modalForm" @ok="modalFormOk"></pd-purchase-examine-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin,handleEdit} from '@/mixins/JeecgListMixin'
  import { deleteAction } from '@/api/manage'
  import PdPurchaseExamineModal from './modules/PdPurchaseExamineModal'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'

  export default {
    name: "PdPurchaseExamineList",
    mixins:[JeecgListMixin],
    components: {
      JDictSelectTag,
      PdPurchaseExamineModal
    },
    data () {
      return {
        description: '申购订单主表管理页面',
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
            title:'申购编号',
            align:"center",
            dataIndex: 'orderNo'
          },
          {
            title:'申购人名称',
            align:"center",
            dataIndex: 'purchaseName'
          },
          {
            title:'申购日期',
            align:"center",
            dataIndex: 'orderDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'申购科室',
            align:"center",
            dataIndex: 'deptName'

          },
          {
            title:'审核状态',
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
            title:'申购总数量',
            align:"center",
            dataIndex: 'totalNum'
          },
          {
            title:'申购总金额',
            align:"center",
            dataIndex: 'totalPrice'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/pd/pdPurchaseOrder/auditList",
          delete: "/pd/pdPurchaseOrder/delete",
          deleteBatch: "/pd/pdPurchaseOrder/deleteBatch"
        },
        dictOptions:{
          auditStatus:[],
         },
      }
    },
    computed: {

    },
    methods: {

         //批量审核
      batchAduit() {
        if (this.selectionRows.length <= 0) {
          this.$message.warning('请先选择申购单！');
          return;
        }else{
          var ids = "";
          var orderNos="";
          for (let a = 0; a < this.selectionRows.length; a++) {
            let auditStatus= this.selectionRows[a].auditStatus;
            alert("sss:"+auditStatus);
            if(auditStatus!='1'){
              orderNos+=this.selectionRows[a].orderNo + ",";
            }else{
              ids += this.selectionRows[a].id + ",";
            }
          }
          if(orderNos != ""){
            this.$message.warning("采购编号["+orderNos.substring(0,orderNos.length-1)+"]已提交过审核！")
            return
          }
          var that = this;
          this.$confirm({
            title: "审批提醒",
            content: "确认是否审批通过选择的订单吗?",
            onOk: function () {
              that.loading = true;
            /*  deleteAction(that.url.deleteBatch, {ids: ids}).then((res) => {
                if (res.success) {
                  that.$message.success(res.message);
                  that.loadData();
                  that.onClearSelected();
                } else {
                  that.$message.warning(res.message);
                }
              }).finally(() => {
                that.loading = false;
              });*/
            }
          });
        }



        /*if (len > 0) {
          $("#submitNum").text(len);
          var orderNos = [];
          $.each(rowsObj, function (i, v) {
            orderNos.push($(this).data('orderno'));
          })
          layer.open({
            type: 1,
            title: "提示",
            content: $(".submitBox"),
            area: ["300px", "200px"],
            shade: [0.8, '#393D49'],
            btn: ["确定", "取消"],
            yes: function (index, layero) {
              //批量通过
              loading('正在提交，请稍等...');
              batchDeal(orderNos.join(','), '1', null, '1');//最后一个一代表批量保存
              //layer.closeAll();
            },
            btn2: function () {
              layer.closeAll();
            }
          })
        }*/
      },




    /* batchDeal(orderNos,orderStatus,refuseReason,oprtSource){
    $.post('${ctx}/pd/pdPurchaseOrder/audit',{"orderNos":orderNos,"orderStatus":orderStatus,"refuseReason":refuseReason,"oprtSource":oprtSource},function(data){
      if("200" == data.code){
        layer.alert("操作成功",{icon:1},function(index){
          layer.closeAll();
          location.href = '${ctx}'+data.uri;
        });
      }else{
        layer.alert("操作失败",{icon:2},function(index){
          layer.close(index);
        });
      }
    });
  },*/





      initDictConfig(){//静态字典值加载
        initDictOptions('audit_status').then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'auditStatus', res.result)
          }
        })
      }
       
    }
  }
</script>
<style scoped>
</style>