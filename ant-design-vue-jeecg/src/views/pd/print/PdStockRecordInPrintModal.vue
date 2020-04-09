<template>
  <j-modal
    :visible="visible"
    :width="1200"
    :title="title"
    :lockScroll="lockScroll"
    :fullscreen="fullscreen"
    :switchFullscreen="switchFullscreen"
    @cancel="handleCancel"
    :footer="null"
  >
    <div class="no-print" style="text-align: right">
      <a-button v-print="'#printContent'" ghost type="primary">打印</a-button>
    </div>
    <section ref="print" id="printContent" class="printClass">
      <div style="text-align: center">
        <p style="font-size: 24px;font-weight: 800">{{title}}</p>
      </div>
      <!--签字-->
      <a-col :md="24" :sm="24">
        <div class="sign" style="text-align: left;height: inherit">

          <a-col :span="24">
            <span style="margin-left: 3%">
              入库单号：
            </span>
            <a-input style="width: 30%;text-align: left" disabled v-model="record.recordNo"/>
            <span style="margin-left: 12.5%">
              入库日期：
            </span>
            <a-input style="width: 30%;text-align: left" disabled v-model="record.auditDate"/>
          </a-col>
          <a-col :span="24" style="margin-top: 10px">
            <span style="margin-left: 3%">
              入库库房：
            </span>
            <a-input style="width: 30%;text-align: left" disabled v-model="record.inDepartName"/>
            <span style="margin-left: 12.5%">
              供应商：&nbsp;&nbsp;&nbsp;
            </span>
            <a-input style="width: 30%;text-align: left" disabled v-model="record.supplierName"/>
          </a-col>
          <a-col :span="24" style="margin-top: 10px">
            <span style="margin-left: 3%;text-align: right">
              备注：
            </span>
            <a-input style="width: 50%;text-align: left" disabled v-model="record.remarks"/>
          </a-col>
          <a-col :span="24" style="margin-top: 10px">
            <!--<span>入库明细：</span>-->
            <a-table
              ref="table"
              size="small"
              bordered
              rowKey="id"
              :pagination="false"
              :columns="columns"
              :dataSource="dataSource"
              >
            </a-table>
          </a-col>

          <a-col :span="24" style="margin-top: 10px;">
            <span style="margin-left: 70%;font-size: medium;font-weight: bold">
              合计数量：{{ record.totalSum }}
            </span>
            <span style="margin-left: 5%;font-size: medium;font-weight: bold">
              合计金额：{{ record.inTotalPrice }}
            </span>
          </a-col>

          <a-col :span="24" style="margin-top: 10px">
            <span style="margin-left: 3%">
              仓库管理员签字：
            </span>
            <a-input style="width: 10%;text-align: left" />
            <span style="margin-left: 3%">
              采购人员签字：
            </span>
            <a-input style="width: 10%;text-align: left" />
            <span style="margin-left: 3%">
              记账人员签字：
            </span>
            <a-input style="width: 10%;text-align: left" />
          </a-col>
        </div>
      </a-col>
    </section>
    </j-modal>
  <!--</page-layout>-->
</template>
<script>
  export default {
    components: {

    },
    name: 'PdStockRecordInPrintModal',
    props:{
      reBizCode:{
        type: String,
        default: ''
      }
    },
    data(){
      return {
        lockScroll: false,
        fullscreen: true,
        switchFullscreen: false,
        columns: [
          { title: '产品名称', dataIndex: 'productName', align:"center", width:"15%" },
          { title: '规格', dataIndex: 'spec',align:"center", width:"9%" },
          // { title: '型号', dataIndex: 'version',align:"center", width:"10%" },
          // { title: '单位', dataIndex: 'unitName',align:"center", width:"50px" },
          { title: '入库单价', dataIndex: 'purchasePrice',align:"center", width:"8%" },
          { title: '数量', dataIndex: 'productNum',align:"center", width:"8%" },
          { title: '金额', dataIndex: 'inTotalPrice',align:"center", width:"8%" },
          { title: '批号', dataIndex: 'batchNo',align:"center", width:"8%"},
          { title: '生产日期', dataIndex: 'produceDate',align:"center", width:"9%" },
          { title: '有效期', dataIndex: 'expDate',align:"center", width:"9%" },
          { title: '注册证号', dataIndex: 'registration',align:"center", width:"9%" },
          { title: '生产厂家', dataIndex: 'venderName',align:"center"},
        ],
        dataSource: [],
        labelCol: {
          xs: { span: 24 },
          sm: { span: 2 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 8 },
        },
        title:"操作",
        visible: false,
        record:{},
      }
    },
    created() {
    },
    methods: {
      // 重写close方法
      close() {
        this.visible = false;
        this.$emit('close')
      },
      /** 关闭按钮 **/
      handleCancel () {
        this.close()
      },
      show(record){
        this.visible = true;
        this.record = record;
        this.dataSource = record.pdStockRecordDetailList;
        console.log(this.dataSource)
      },
    }
  }
</script>
<style scoped>
  /*update_begin author:scott date:20191203 for:打印机打印的字体模糊问题 */
  * {
    color: #000000!important;
    -webkit-tap-highlight-color: #000000!important;
  }
  /*update_end author:scott date:20191203 for:打印机打印的字体模糊问题 */

  .printClass .ant-card-body{
    margin-left: 0%;
    margin-right: 0%;
    margin-bottom: 1%;
    border:0px solid black;
    min-width: 800px;
    color:#000000!important;
  }

  .explain{
    text-align: left;
    margin-left: 50px;
    color:#000000!important;
  }
  .explain .ant-input,.sign .ant-input{
    font-weight:bolder;
    text-align:center;
    border-left-width:0px!important;
    border-top-width:0px!important;
    border-right-width:0px!important;
  }
  .explain div{
    margin-bottom: 10px;
  }
</style>