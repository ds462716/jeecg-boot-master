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
              调拨单号：
            </span>
            <a-input style="width: 30%;text-align: left" disabled v-model="record.allocationNo"/>
            <span style="margin-left: 12.5%">
              调拨日期：
            </span>
            <a-input style="width: 30%;text-align: left" disabled v-model="record.allocationDate"/>
          </a-col>
          <a-col :span="24" style="margin-top: 10px">
            <span style="margin-left: 3%">
              调拨科室：
            </span>
            <a-input style="width: 30%;text-align: left" disabled v-model="record.inDeptName"/>
            <span style="margin-left: 12.5%">
              出库科室：
            </span>
            <a-input style="width: 30%;text-align: left" disabled v-model="record.outDeptName"/>
          </a-col>
          <a-col :span="24" style="margin-top: 10px">
           <span style="margin-left: 3%">
              调拨人员：
            </span>
            <a-input style="width: 30%;text-align: left" disabled v-model="record.realName"/>
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
            <span style="margin-left: 80%;font-size: medium;font-weight: bold">
              合计调拨数量：{{ record.totalNum }}
            </span>
          </a-col>

          <a-col :span="24" style="margin-top: 10px">
            <span style="margin-left: 3%">
              仓库管理员签字：
            </span>
            <a-input style="width: 10%;text-align: left" />
            <span style="margin-left: 3%">
              调拨人员签字：
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
</template>
<script>
  export default {
    components: {

    },
    name: 'PdAllocationRecordPrintModal',
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
          { title: '定数包名称', align:"center",   dataIndex: 'packageName' },
          { title: '定数包编号', align:"center",  dataIndex: 'packageId' },
          { title: '产品名称', align:"center",  dataIndex: 'productName' },
          { title: '产品编号',align:"center",  dataIndex: 'number' },
          { title: '规格',align:"center", dataIndex: 'spec' },
          { title: '型号',align:"center", dataIndex: 'version' },
          { title: '单位',align:"center",  dataIndex: 'unitName' },
          { title: '发货数量',align:"center",  dataIndex: 'arrivalNum' },
          {title: '调拨数量', align:"center", dataIndex: 'allocationNum'},
          { title: '库存数量',align:"center",  key: 'stockNum' },
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
        this.dataSource = record.pdAllocationDetailList;
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