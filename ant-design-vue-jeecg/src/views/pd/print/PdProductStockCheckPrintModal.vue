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
              盘点单号：
            </span>
            <a-input style="width: 30%;text-align: left" disabled v-model="record.checkNo"/>
            <span style="margin-left: 12.5%">
              盘点日期：
            </span>
            <a-input style="width: 30%;text-align: left" disabled v-model="record.checkDate"/>
          </a-col>
          <a-col :span="24" style="margin-top: 10px">
            <span style="margin-left: 3%">
              盘点科室：
            </span>
            <a-input style="width: 30%;text-align: left" disabled v-model="record.deptName"/>
            <span style="margin-left: 12.5%">
              盘点人员：&nbsp;&nbsp;&nbsp;
            </span>
            <a-input style="width: 30%;text-align: left" disabled v-model="record.checkName"/>
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

          <!--<a-col :span="24" style="margin-top: 10px;">
            <span style="margin-left: 60%;font-size: medium;font-weight: bold">
              合计数量：{{ record.totalNum }}
            </span>
            <span style="margin-left: 5%;font-size: medium;font-weight: bold">
              合计金额：{{ record.totalPrice }}
            </span>
          </a-col>-->

          <a-col :span="24" style="margin-top: 10px">
            <span style="margin-left: 3%">
              仓库管理员签字：
            </span>
            <a-input style="width: 10%;text-align: left" />
            <span style="margin-left: 3%">
              盘点人员签字：
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
    name: 'PdProductStockCheckPrintModal',
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
          {
            title: '#',
            dataIndex: '',
            key:'id',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {title: '产品编号',  align:"center", dataIndex: 'number'},
          {title: '产品名称', align:"center", dataIndex: 'productName'},
          {title: '规格', align:"center",  dataIndex: 'spec'},
          {title: '型号', align:"center",  dataIndex: 'version'},
          {title: '单位', align:"center",  dataIndex: 'unitName'},
          {title: '供应商', align:"center",dataIndex: 'supplierName'},
          {title: '生产厂家', align:"center", dataIndex: 'venderName'},
          {title: '理论数量', align:"center", dataIndex: 'stockNum'},
          {title: '盘点数量', align:"center", dataIndex: 'checkNum'},
          {title: '盘盈盘亏', align:"center", dataIndex: 'profitLossCount'},
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
        this.dataSource = record.pdProductStockCheckChildList;
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