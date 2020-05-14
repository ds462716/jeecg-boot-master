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
            <a-input style="width: 25%;text-align: left" disabled v-model="record.recordNo"/>
            <span style="margin-left: 3%">
              入库日期：
            </span>
            <a-input style="width: 20%;text-align: left" disabled v-model="record.auditDate"/>
            <span style="margin-left: 3%">
              入库库房：
            </span>
            <a-input style="width: 20%;text-align: left" disabled v-model="record.inDepartName"/>
          </a-col>
          <a-col :span="24" style="margin-top: 0px">
            <span style="margin-left: 3%">
              供应商：&nbsp;&nbsp;&nbsp;
            </span>
            <a-input style="width: 25%;text-align: left" disabled v-model="record.supplierName"/>
            <span style="margin-left: 3%;text-align: right">
              备注：
            </span>
            <a-input style="width: 50%;text-align: left" disabled v-model="record.remarks"/>
          </a-col>
          <a-col :span="24" style="margin-top: 0px">
          </a-col>
          <a-col :span="24" style="margin-top: 5px">
            <!--<a-table-->
              <!--ref="table"-->
              <!--size="small"-->
              <!--bordered-->
              <!--rowKey="id"-->
              <!--:pagination="false"-->
              <!--:columns="columns"-->
              <!--:customRow="customRow"-->
              <!--:dataSource="dataSource"-->
              <!--&gt;-->
            <!--</a-table>-->
            <table width="100%" id="contentTable" class="tableStyle">
              <tr>
                <th style="border: 1px solid #e8e8e8;text-align: center;padding: 3px 3px;">
                  产品名称
                </th>
                <th style="border: 1px solid #e8e8e8;text-align: center;padding: 3px 3px;width:15%">
                  生产厂家
                </th>
                <th style="border: 1px solid #e8e8e8;text-align: center;padding: 3px 3px;width:10%">
                  注册证号
                </th>
                <th style="border: 1px solid #e8e8e8;text-align: center;padding: 3px 3px;width:10%">
                  规格
                </th>
                <th style="border: 1px solid #e8e8e8;text-align: center;padding: 3px 3px;width:8%">
                  批号
                </th>
                <th style="border: 1px solid #e8e8e8;text-align: center;padding: 3px 3px;width:8%">
                  生产日期
                </th>
                <th style="border: 1px solid #e8e8e8;text-align: center;padding: 3px 3px;width:8%">
                  有效期
                </th>
                <th style="border: 1px solid #e8e8e8;text-align: center;padding: 3px 3px;width:4%">
                  数量
                </th>
                <th style="border: 1px solid #e8e8e8;text-align: center;padding: 3px 3px;width:4%">
                  单价
                </th>
                <th style="border: 1px solid #e8e8e8;text-align: center;padding: 3px 3px;width:5%">
                  金额
                </th>
              </tr>
              <tr v-for="(item, index) in dataSource">
                <td style="text-align: center;border: 1px solid #e8e8e8;padding: 3px 3px;font-size: xx-small">
                  {{ item.productName }}
                </td>
                <td style="text-align: center;border: 1px solid #e8e8e8;padding: 3px 3px;font-size: xx-small">
                  {{ item.venderName }}
                </td>
                <td style="text-align: center;border: 1px solid #e8e8e8;padding: 3px 3px;font-size: xx-small">
                  <!--{{ item.registration }}-->
                  <a-select
                    size="small"
                    style="width: 100%;font-size: xx-small"
                    :defaultValue="item.registrationSelected"
                    :showArrow="false"
                    :dropdownMatchSelectWidth="false"
                  >
                    <a-select-option v-for="(registration, index) in item.registrationList" :key="registration">
                      {{ registration }}
                    </a-select-option>
                  </a-select>

                </td>
                <td style="text-align: center;border: 1px solid #e8e8e8;padding: 3px 3px;font-size: xx-small">
                  {{ item.spec }}
                </td>
                <td style="text-align: center;border: 1px solid #e8e8e8;padding: 3px 3px;font-size: xx-small">
                  {{ item.batchNo }}
                </td>
                <td style="text-align: center;border: 1px solid #e8e8e8;padding: 3px 3px;font-size: xx-small">
                  {{ item.produceDate }}
                </td>
                <td style="text-align: center;border: 1px solid #e8e8e8;padding: 3px 3px;font-size: xx-small">
                  {{ item.expDate }}
                </td>
                <td style="text-align: center;border: 1px solid #e8e8e8;padding: 3px 3px;font-size: xx-small">
                  {{ item.productNum }}
                </td>
                <td style="text-align: center;border: 1px solid #e8e8e8;padding: 3px 3px;font-size: xx-small">
                  {{ item.purchasePrice }}
                </td>
                <td style="text-align: center;border: 1px solid #e8e8e8;padding: 3px 3px;font-size: xx-small">
                  {{ item.inTotalPrice }}
                </td>
              </tr>
              <tr>
                <td  colspan="6" style="text-align: center;border: 1px solid #e8e8e8;padding: 3px 3px;font-size: small">
                </td>
                <td style="text-align: center;border: 1px solid #e8e8e8;padding: 3px 3px;font-size: small">
                  合计
                </td>
                <td style="text-align: center;border: 1px solid #e8e8e8;padding: 3px 3px;font-size: small">
                  {{ record.totalSum }}
                </td>
                <td style="text-align: center;border: 1px solid #e8e8e8;padding: 3px 3px;font-size: small">
                </td>
                <td style="text-align: center;border: 1px solid #e8e8e8;padding: 3px 3px;font-size: small">
                  {{ record.inTotalPrice }}
                </td>
              </tr>
            </table>
          </a-col>

          <!--<a-col :span="24" style="margin-top: 5px;">-->
            <!--<span style="margin-left: 60%;font-weight: bold">-->
              <!--合计数量：{{ record.totalSum }}-->
            <!--</span>-->
            <!--<span style="margin-left: 2%;font-weight: bold">-->
              <!--合计金额：{{ record.inTotalPrice }}-->
            <!--</span>-->
          <!--</a-col>-->

          <a-col :span="24" style="margin-top: 5px">
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
          { title: '生产厂家', dataIndex: 'venderName',align:"center"},
          { title: '注册证号', dataIndex: 'registration',align:"center"  },
          { title: '规格', dataIndex: 'spec',align:"center" },
          // { title: '型号', dataIndex: 'version',align:"center", width:"10%" },
          // { title: '单位', dataIndex: 'unitName',align:"center", width:"50px" },
          { title: '批号', dataIndex: 'batchNo',align:"center" },
          { title: '生产日期', dataIndex: 'produceDate',align:"center", width:"10%" },
          { title: '有效期', dataIndex: 'expDate',align:"center", width:"10%" },
          { title: '数量', dataIndex: 'productNum',align:"center", width:"7%" },
          { title: '单价', dataIndex: 'purchasePrice',align:"center", width:"7%" },
          { title: '金额', dataIndex: 'inTotalPrice',align:"center", width:"7%" },
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
        this.dataSource = [];
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

        for (let item of this.dataSource){
          let registration = item.registration.replace(/；/g, ";")
          let registrationList = [];
          let list = registration.split(";");
          for (let li of list){
            li = li.replace(/(^\s*)|(\s*$)/g, "");
            if(li){
              registrationList.push(li)
            }
          }
          item.registrationSelected = registrationList[registrationList.length-1];
          item.registrationList = registrationList;
        }
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

<style media="print">
  @page {
    size: auto;
    margin-top: 5mm;
    margin-left: 0mm;
    margin-right: 0mm;
    margin-bottom: 0mm;
  }
</style>