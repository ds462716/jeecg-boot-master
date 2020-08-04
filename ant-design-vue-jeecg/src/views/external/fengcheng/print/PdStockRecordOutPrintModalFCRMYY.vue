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
      <!-- 超过十个的循环区域 -->
      <div v-if="batchPrint">
        <div v-for="(item, index1) in divNumber-1" class="sign" style="page-break-after:always;text-align: left;height: inherit">
          <div style="text-align: center">
            <p style="font-size: 22px;font-weight: 800">{{title}}</p>
          </div>
          <!--签字-->
          <a-col :md="24" :sm="24">
            <div class="sign" style="text-align: left;height: inherit">
              <a-col :span="24">
                <span style="margin-left: 3%">
                  制单人：
                </span>
                <a-input style="width: 10%;text-align: left" disabled v-model="record.submitByName"/>
                <span style="margin-left: 3%">
                  出库单号：
                </span>
                <a-input style="width: 18%;text-align: left" disabled v-model="record.recordNo"/>
                <span style="margin-left: 3%">
                  出库日期：
                </span>
                <a-input style="width: 12%;text-align: left" disabled v-model="record.auditDate"/>
                <span style="margin-left: 15%">
                  页码：
                {{index1+1}} /{{divNumber}}
                </span>
              </a-col>
              <a-col :span="24" style="margin-top: 0px">
                <span style="margin-left: 3%">
                  入库库房：
                </span>
                <a-input style="width: 12%;text-align: left" disabled v-model="record.inDepartName"/>
                <span style="margin-left: 3%;text-align: right">
                  备注：
                </span>
                <a-input style="width: 50%;text-align: left" disabled v-model="record.remarks"/>
              </a-col>
              <a-col :span="24" style="margin-top: 5px">
                <a-form :form="form">
                  <table width="100%" class="tableStyle">
                    <tr>
                      <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:2%">

                      </th>
                      <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:20%">
                        产品名称
                      </th>
                      <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:15%">
                        规格
                      </th>
                      <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:5%">
                        单位
                      </th>
                      <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:5%">
                        数量
                      </th>
                      <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:8%">
                        入库单价
                      </th>
                      <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:8%">
                        入库金额
                      </th>
                      <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:10%">
                        批号
                      </th>
                      <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;">
                        生产厂家
                      </th>
                      <!--<th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:7%">-->
                      <!--出库单价-->
                      <!--</th>-->
                      <!--<th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:7%">-->
                      <!--出库金额-->
                      <!--</th>-->
                    </tr>
                    <tr v-for="(item, index2) in tableNumber[index1]">
                      <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                        {{ index2+(index1*10) +1}}
                      </td>
                      <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                        {{ item.productName }}

                        <a-form-item label="id" v-show="false">
                          <a-input
                            v-decorator="[ 'pdStockRecordDetailList['+index2+'].id',{'initialValue':item.id} ]"></a-input>
                        </a-form-item>
                      </td>
                      <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                        {{ item.spec }}
                      </td>
                      <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                        {{ item.unitName }}
                      </td>
                      <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                        {{ item.productNum }}
                      </td>
                      <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                        {{ item.purchasePrice }}
                      </td>
                      <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                        {{ item.inTotalPrice }}
                      </td>
                      <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                        {{ item.batchNo }}
                      </td>
                      <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                        {{ item.venderName }}
                      </td>
                      <!--<td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">-->
                      <!--{{ item.sellingPrice }}-->
                      <!--</td>-->
                      <!--<td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">-->
                      <!--{{ item.outTotalPrice }}-->
                      <!--</td>-->
                    </tr>
                    <!--<tr>-->
                      <!--<td colspan="3" style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: small">-->
                      <!--</td>-->
                      <!--<td-->
                        <!--style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: larger;font-weight: bolder">-->
                        <!--合计-->
                      <!--</td>-->
                      <!--<td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: larger;font-weight: bolder">-->
                        <!--{{ record.totalSum }}-->
                      <!--</td>-->
                      <!--<td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: small">-->
                      <!--</td>-->
                      <!--<td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: larger;font-weight: bolder">-->
                        <!--{{ record.inTotalPrice }}-->
                      <!--</td>-->
                      <!--<td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: small">-->
                      <!--</td>-->
                      <!--<td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: small">-->
                      <!--</td>-->
                    <!--</tr>-->
                  </table>
                </a-form>
              </a-col>
            </div>
          </a-col>
        </div>
      </div>

      <div class="sign" style="text-align: left;height: inherit">
        <div style="text-align: center">
          <p style="font-size: 22px;font-weight: 800">{{title}}</p>
        </div>
        <!--签字-->
        <a-col :md="24" :sm="24">
          <div class="sign" style="text-align: left;height: inherit">
            <a-col :span="24">
              <span style="margin-left: 3%">
                制单人：
              </span>
              <a-input style="width: 10%;text-align: left" disabled v-model="record.submitByName"/>
              <span style="margin-left: 3%">
                出库单号：
              </span>
              <a-input style="width: 18%;text-align: left" disabled v-model="record.recordNo"/>
              <span style="margin-left: 3%">
                出库日期：
              </span>
              <a-input style="width: 12%;text-align: left" disabled v-model="record.auditDate"/>
              <span style="margin-left: 15%">
                  页码：
                {{divNumber}} /{{divNumber}}
              </span>
            </a-col>
            <a-col :span="24" style="margin-top: 0px">
              <span style="margin-left: 3%">
                入库库房：
              </span>
              <a-input style="width: 12%;text-align: left" disabled v-model="record.inDepartName"/>
              <span style="margin-left: 3%;text-align: right">
                备注：
              </span>
              <a-input style="width: 50%;text-align: left" disabled v-model="record.remarks"/>
            </a-col>
            <a-col :span="24" style="margin-top: 5px">
              <a-form :form="form">
                <table width="100%" class="tableStyle">
                  <tr>
                    <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:2%">

                    </th>
                    <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:20%">
                      产品名称
                    </th>
                    <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:15%">
                      规格
                    </th>
                    <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:5%">
                      单位
                    </th>
                    <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:5%">
                      数量
                    </th>
                    <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:8%">
                      入库单价
                    </th>
                    <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:8%">
                      入库金额
                    </th>
                    <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:10%">
                      批号
                    </th>
                    <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;">
                      生产厂家
                    </th>
                    <!--<th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:7%">-->
                    <!--出库单价-->
                    <!--</th>-->
                    <!--<th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:7%">-->
                    <!--出库金额-->
                    <!--</th>-->
                  </tr>
                  <tr v-for="(item, index2) in tableNumber[divNumber-1]">
                    <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                      {{ index2+((divNumber-1)*10) +1}}
                    </td>
                    <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                      {{ item.productName }}

                      <a-form-item label="id" v-show="false">
                        <a-input
                          v-decorator="[ 'pdStockRecordDetailList['+index2+'].id',{'initialValue':item.id} ]"></a-input>
                      </a-form-item>
                    </td>
                    <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                      {{ item.spec }}
                    </td>
                    <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                      {{ item.unitName }}
                    </td>
                    <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                      {{ item.productNum }}
                    </td>
                    <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                      {{ item.purchasePrice }}
                    </td>
                    <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                      {{ item.inTotalPrice }}
                    </td>
                    <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                      {{ item.batchNo }}
                    </td>
                    <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                      {{ item.venderName }}
                    </td>
                    <!--<td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">-->
                    <!--{{ item.sellingPrice }}-->
                    <!--</td>-->
                    <!--<td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">-->
                    <!--{{ item.outTotalPrice }}-->
                    <!--</td>-->
                  </tr>
                  <tr>
                    <td colspan="3" style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: small">
                    </td>
                    <td
                      style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: larger;font-weight: bolder">
                      合计
                    </td>
                    <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: larger;font-weight: bolder">
                      {{ record.totalSum }}
                    </td>
                    <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: small">
                    </td>
                    <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: larger;font-weight: bolder">
                      {{ record.inTotalPrice }}
                    </td>
                    <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: small">
                    </td>
                    <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: small">
                    </td>
                  </tr>
                </table>
              </a-form>
            </a-col>

          </div>
        </a-col>
      </div>
      <!-- 页脚  -->
      <div class="sign" style="text-align: left;height: inherit">
        <a-col :span="24" style="margin-top: 5px">
          <span style="margin-left: 3%">
            出库管理：
          </span>
          <a-input style="width: 10%;text-align: left"/>
          <span style="margin-left: 3%">
            领用人：
          </span>
          <a-input style="width: 10%;text-align: left"/>
        </a-col>
      </div>
    </section>
  </j-modal>
  <!--</page-layout>-->
</template>
<script>

  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import {httpAction} from '@/api/manage'

  export default {
    componens: {},
    name: 'PdStockRecordOutPrintModalFCRMYY',
    props: {
      reBizCode: {
        type: String,
        default: ''
      }
    },
    data() {
      return {
        form: this.$form.createForm(this),
        model: {},
        lockScroll: false,
        fullscreen: true,
        switchFullscreen: false,
        showApplyBy: true,
        dataSource: [],
        divNumber:0,
        batchPrint:false,
        tableNumber:[],
        labelCol: {
          xs: {span: 24},
          sm: {span: 2},
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 8},
        },
        title: "出库单",
        visible: false,
        record: {},
        outTypeList: {},
        validatorRules: {},
        url: {
          edit: "/pd/pdStockRecordOut/edit",
        },
      }
    },
    created() {
      // this.initDictConfig();
    },
    methods: {
      // 重写close方法
      close() {
        this.visible = false;
        this.batchPrint = false;
        this.dataSource = [];
        this.tableNumber = [];
        this.divNumber = 0;
        this.$emit('close')
      },
      /** 关闭按钮 **/
      handleCancel() {
        this.close()
      },
      show(record) {
        this.visible = true;
        this.dataSource = record.pdStockRecordDetailList;
        this.printInit(this.dataSource);
        this.record = record;
        this.showApplyBy = true;
        if (this.record.outType == "4") {
          this.showApplyBy = false;
        }

        for (let item of this.dataSource) {
          let registration = item.productRegistration.replace(/；/g, ";")
          let registrationList = [];
          let list = registration.split(";");
          for (let li of list) {
            li = li.replace(/(^\s*)|(\s*$)/g, "");
            if (li) {
              registrationList.push(li)
            }
          }
          item.registrationSelected = registrationList[registrationList.length - 1];
          item.registrationList = registrationList;
        }
      },
      printBtn() {
        this.form.validateFields((err, values) => {
          // if (!err) {
          //选择标识符的校验通过后
          if (values.hasOwnProperty("pdStockRecordDetailList")) {
            let formData = Object.assign(this.model, values);
            httpAction(this.url.edit, formData, 'put').then((res) => {
              if (res.success) {
                // this.$message.success(res.message);
              } else {
                // this.$message.warning(res.message);
              }
            }).finally(() => {
            })
          } else {
            this.$message.error("请添加标识符");
          }
          // }
        })
      },
      initDictConfig() { //静态字典值加载
      },
      //打印字体设置
      customRow(record) {
        return {
          style: {
            // 字体颜色
            // color:  'rgba(0, 0, 0, 0.65)',
            // 行背景色
            // 'background-color':  '#ffffff',
            'font-weight': 600,
            'font-size': 'xx-small',
            'height': '10px',
          },
        }
      },
      printInit(data){
        let number = data.length;
        if(number>10){
          this.batchPrint = true;
        }
        let num;
        if(number%10==0){
          num = parseInt(number/10);
          for(let index =0;index<num;index++){
            let lsData = [];
            for(let i =0;i<10;i++){
              lsData.push(data[index*10+i])
            }
            this.tableNumber.push(lsData);
          }
        }else{
          num = parseInt(number/10);
          if(num>0){
            for(let index =0;index<num;index++){
              let lsData = [];
              for(let i =0;i<10;i++){
                lsData.push(data[index*10+i]);
              }
              this.tableNumber.push(lsData);
            }
            let wsData = [];
            let ws = parseInt(number%10);
            for(let x =0;x<ws;x++){
              wsData.push(data[num*10+x]);
            }
            this.tableNumber.push(wsData);
          }else{
            this.tableNumber.push(data);
          }
          num = num+1;
        }
        this.divNumber = num;
      },
    }
  }
</script>
<style scoped>
  /*update_begin author:scott date:20191203 for:打印机打印的字体模糊问题 */
  * {
    color: #000000 !important;
    -webkit-tap-highlight-color: #000000 !important;
  }

  /*update_end author:scott date:20191203 for:打印机打印的字体模糊问题 */
  /*.printClass .ant-card-body{*/
  /*margin-left: 0%;*/
  /*margin-right: 0%;*/
  /*margin-bottom: 0%;*/
  /*border:0px solid black;*/
  /*min-width: 800px;*/
  /*color:#000000!important;*/
  /*}*/
  .explain {
    text-align: left;
    margin-left: 50px;
    color: #000000 !important;
  }

  .explain .ant-input, .sign .ant-input {
    text-align: center;
    border-left-width: 0px !important;
    border-top-width: 0px !important;
    border-right-width: 0px !important;
  }

  .explain div {
    margin-bottom: 10px;
  }
</style>
<!-- 打印去页眉页脚 去边距 -->
<style media="print">
  @page {
    size: auto;
    margin-top: 5mm;
    margin-left: 5mm;
    margin-right: 5mm;
    margin-bottom: 0mm;
  }

  .ant-form-item-control {
    line-height: 0px;
  }
</style>