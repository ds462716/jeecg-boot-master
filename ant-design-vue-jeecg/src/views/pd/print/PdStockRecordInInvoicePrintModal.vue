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
      <a-button v-print="'#printContent'" ghost type="primary" @click="printBtn" >打印</a-button>
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
              打印人：
            </span>
            <a-input style="width: 20%;text-align: left" disabled v-model="userName"/>
            <span style="margin-left: 3%">
              科室：
            </span>
            <a-input style="width: 20%;text-align: left" disabled v-model="departName"/>
            <span style="margin-left: 3%">
              打印日期：
            </span>
            <a-input style="width: 20%;text-align: left" disabled v-model="nowDate"/>
          </a-col>
          <a-col :span="24" style="margin-top: 5px">
            <a-form :form="form">
              <table width="100%" id="contentTable" class="tableStyle" style="border: 1px black">
                <tr>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:6%">
                    入库日期
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:8%">
                    入库单号
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:15%">
                    供应商
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:12%">
                    产品名称
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:10%">
                    规格
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:3%">
                    单位
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:5%">
                    入库数量
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:5%">
                    单价
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:5%">
                    金额
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:15%">
                    发票号
                  </th>
                  <!--<th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:8%">-->
                  <!--发票代码-->
                  <!--</th>-->
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:6%">
                    发票日期
                  </th>
                  <!--<th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:5%">-->
                    <!--批号-->
                  <!--</th>-->
                </tr>
                <tr v-for="(item, index) in dataSource">
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                    {{ item.billDate }}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                    {{ item.billNo }}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                    {{ item.supplierName }}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                    {{ item.productName }}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                    {{ item.spec }}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                    {{ item.unitName }}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                    {{ item.num }}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                    {{ item.price }}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                    {{ item.money }}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                    {{ item.invoiceNo }}
                  </td>
                  <!--<td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">-->
                  <!--{{ item.invoiceCode }}-->
                  <!--</td>-->
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                    {{ item.invoiceData }}
                  </td>
                  <!--<td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">-->
                    <!--{{ item.batchNo }}-->
                  <!--</td>-->
                </tr>
                <tr>
                  <td  colspan="5" style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: small">
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: small">
                    合计
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: small">
                    {{ totalSum }}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: small">
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: small">
                    {{ inTotalPrice }}
                  </td>
                  <td colspan="2" style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: small">
                  </td>
                </tr>
              </table>
            </a-form>
          </a-col>

          <!--<a-col :span="24" style="margin-top: 5px;">-->
            <!--<span style="margin-left: 60%;font-weight: bold">-->
              <!--合计数量：{{ record.totalSum }}-->
            <!--</span>-->
            <!--<span style="margin-left: 2%;font-weight: bold">-->
              <!--合计金额：{{ record.inTotalPrice }}-->
            <!--</span>-->
          <!--</a-col>-->

          <!--<a-col :span="24" style="margin-top: 5px">-->
            <!--<span style="margin-left: 3%">-->
              <!--打印人签字：-->
            <!--</span>-->
            <!--<a-input style="width: 10%;text-align: left"  v-model="userName"/>-->
          <!--</a-col>-->
        </div>
      </a-col>
    </section>
    </j-modal>
  <!--</page-layout>-->
</template>
<script>

  import { httpAction } from '@/api/manage'
  import moment from 'dayjs'

  export default {
    components: {

    },
    name: 'PdStockRecordInInvoicePrintModal',
    props:{
      reBizCode:{
        type: String,
        default: ''
      }
    },
    data(){
      return {
        form: this.$form.createForm(this),
        model: {},
        lockScroll: false,
        fullscreen: true,
        switchFullscreen: false,
        selectRegistrations: [],
        totalSum:0,
        inTotalPrice:0,
        userName:"",
        departName:"",
        nowDate:"",
        columns: [
          { title: '发票号', dataIndex: 'invoiceNo', align:"center", width:"15%" },
          { title: '发票代码', dataIndex: 'invoiceCode',align:"center", width:"10%"},
          { title: '发票日期', dataIndex: 'invoiceData',align:"center", width:"7%" },
          { title: '入库日期', dataIndex: 'billDate',align:"center", width:"7%" },
          { title: '入库单号', dataIndex: 'billNo',align:"center", width:"10%" },
          { title: '供应商', dataIndex: 'supplierName',align:"center", width:"15%" },
          { title: '产品名称', dataIndex: 'productName',align:"center", width:"15%" },
          { title: '规格', dataIndex: 'spec',align:"center", width:"15%" },
          { title: '单位', dataIndex: 'unitName',align:"center", width:"5%" },
          { title: '采购单价', dataIndex: 'price',align:"center", width:"7%" },
          { title: '入库数量', dataIndex: 'num',align:"center", width:"7%" },
          { title: '金额', dataIndex: 'money',align:"center", width:"7%" },
          { title: '批号', dataIndex: 'batchNo',align:"center", width:"7%" },
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
        validatorRules:{},
        url: {
          edit: "/pd/pdStockRecordIn/edit",
        },
      }
    },
    created() {
    },
    methods: {
      // 重写close方法
      close() {
        this.visible = false;
        this.dataSource = [];
        this.$emit('close')
      },
      /** 关闭按钮 **/
      handleCancel () {
        this.close()
      },
      show(record,userName,departName){
        this.visible = true;
        this.dataSource = record;
        this.userName = userName;
        this.departName = departName;
        for(let item of record){
          this.totalSum += Number(item.num);
          this.inTotalPrice += Number(item.money);
          // totalSum:0,
          // inTotalPrice:0,
        }

        this.nowDate = moment(new Date()).format('YYYY-MM-DD');
      },
      printBtn(){
        this.form.validateFields((err, values) => {
          // if (!err) {
            //选择标识符的校验通过后
            // if(values.hasOwnProperty("pdStockRecordDetailList")){
            //   let formData = Object.assign(this.model, values);
            //   httpAction(this.url.edit,formData,'put').then((res)=>{
            //     if(res.success){
            //       // this.$message.success(res.message);
            //     }else{
            //       // this.$message.warning(res.message);
            //     }
            //   }).finally(() => {
            //   })
            // }else{
            //   this.$message.error("请添加标识符");
            // }
          // }
        })
      },
      customRow(record) {
        return {
          style: {
            // 字体颜色
            // color:  'rgba(0, 0, 0, 0.65)',
            // 行背景色
            // 'background-color':  '#ffffff',
            'font-weight': 600,
            'font-size': 'xx-small',
            'height':'10px',
          },
        }
      },
    }
  }
</script>
<style lang="scss" scoped >
  /*update_begin author:scott date:20191203 for:打印机打印的字体模糊问题 */
  * {
    color: #000000!important;
    -webkit-tap-highlight-color: #000000!important;
  }
  /*update_end author:scott date:20191203 for:打印机打印的字体模糊问题 */

  /*.printClass .ant-card-body{*/
    /*margin-left: 0%;*/
    /*margin-right: 0%;*/
    /*margin-bottom: 1%;*/
    /*border:0px solid black;*/
    /*min-width: 800px;*/
    /*color:#000000!important;*/
  /*}*/

  .explain{
    text-align: left;
    margin-left: 50px;
    color:#000000!important;
  }
  .explain .ant-input,.sign .ant-input{
    text-align:center;
    border-left-width:0px!important;
    border-top-width:0px!important;
    border-right-width:0px!important;
  }
  .explain div{
    margin-bottom: 10px;
  }
  .ant-form-item-control {
    position: relative;
    line-height: 100px;
    zoom: 1;
  }
</style>

<style media="print">
  @page {
    size: auto;
    margin-top: 5mm;
    margin-left: 13mm;
    margin-right: 13mm;
    margin-bottom: 0mm;
  }
  .ant-form-item-control {
    line-height: 0px;
  }
</style>