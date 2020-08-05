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
      <div v-if="batchPrint">
        <div v-for="(item, index1) in divNumber-1"  class="sign" style="page-break-after:always;text-align: left;height: inherit">
          <div style="text-align: center">
            <p style="font-size: 24px;font-weight: 800">{{title}}</p>
          </div>
          <a-col :md="24" :sm="24">
            <a-col :span="24">
            <span style="margin-left: 3%">
              申购单号：
            </span>
              <a-input style="width: 20%;text-align: left" disabled v-model="record.orderNo"/>
              <span style="margin-left: 3%">
              申购日期：
            </span>
              <a-input style="width: 12%;text-align: left" disabled v-model="record.orderDate"/>
              <span style="margin-left: 33%">
                页码：
             {{index1+1}} /{{divNumber}}
              </span>
            </a-col>
            <a-col :span="24" style="margin-top: 0px">
            <span style="margin-left: 3%">
              申购科室：
            </span>
              <a-input style="width: 20%;text-align: left" disabled v-model="record.deptName"/>
              <span style="margin-left: 3%">
              申购人员：
            </span>
              <a-input style="width: 12%;text-align: left" disabled v-model="record.purchaseName"/>
              <span style="margin-left: 3%;text-align: right">
              备注：
            </span>
              <a-input style="width: 30%;text-align: left" disabled v-model="record.remarks"/>
            </a-col>
            <a-col :span="24" style="margin-top: 10px">
              <a-form :form="form">
                <table width="100%"  class="tableStyle" style="border: 1px black">
                  <tr>
                    <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:2%">

                    </th>
                    <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;">
                      产品名称
                    </th>
                    <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:15%">
                      生产厂家
                    </th>
                    <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:15%">
                      供应商
                    </th>
                    <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:10%">
                      注册证号
                    </th>
                    <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:12%">
                      规格
                    </th>
                    <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:4%">
                      数量
                    </th>
                    <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:4%">
                      单价
                    </th>
                    <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:6%">
                      金额
                    </th>
                  </tr>
                  <tr v-for="(item, index2) in tableNumber[index1]">
                    <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                      {{ index2+(index1*10) +1}}
                    </td>
                    <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                      {{ item.productName }}

                      <a-form-item label="id" v-show="false">
                        <a-input v-decorator="[ 'pdStockRecordDetailList['+index2+'].id',{'initialValue':item.id} ]"></a-input>
                      </a-form-item>
                    </td>
                    <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                      {{ item.venderName }}
                    </td>
                    <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                      {{ item.supplierName }}
                    </td>
                    <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                      <a-form-item label="" style="width: 100%;height: 100%;padding: 0px;margin: 0px;line-height: 0px">
                        <a-select
                          size="small"
                          style="width: 100%;font-size: xx-small"
                          :showArrow="false"
                          :dropdownMatchSelectWidth="false"
                          v-decorator="[ 'pdStockRecordDetailList['+index2+'].registration',{'initialValue':item.registrationSelected}]"
                        >
                          <a-select-option v-for="(registration, index) in item.registrationList" :key="registration">
                            {{ registration }}
                          </a-select-option>
                        </a-select>
                      </a-form-item>
                    </td>
                    <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                      {{ item.spec }}
                    </td>
                    <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                      {{ item.orderNum }}
                    </td>
                    <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                      {{ item.purchasePrice }}
                    </td>
                    <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                      {{ item.orderMoney }}
                    </td>
                  </tr>
                </table>
              </a-form>
            </a-col>
          </a-col>
        </div>
      </div>
      <!-- 超过十个 或者不等于十个 -->
      <div  class="sign" style="text-align: left;height: inherit">
        <div style="text-align: center">
          <p style="font-size: 24px;font-weight: 800">{{title}}</p>
        </div>
        <a-col :md="24" :sm="24">
          <a-col :span="24">
            <span style="margin-left: 3%">
              申购单号：
            </span>
            <a-input style="width: 20%;text-align: left" disabled v-model="record.orderNo"/>
            <span style="margin-left: 3%">
              申购日期：
            </span>
            <a-input style="width: 12%;text-align: left" disabled v-model="record.orderDate"/>
            <span style="margin-left: 33%">
                页码：
             {{divNumber}} /{{divNumber}}
              </span>
          </a-col>
          <a-col :span="24" style="margin-top: 0px">
            <span style="margin-left: 3%">
              申购科室：
            </span>
            <a-input style="width: 20%;text-align: left" disabled v-model="record.deptName"/>
            <span style="margin-left: 3%">
              申购人员：
            </span>
            <a-input style="width: 12%;text-align: left" disabled v-model="record.purchaseName"/>
            <span style="margin-left: 3%;text-align: right">
              备注：
            </span>
            <a-input style="width: 30%;text-align: left" disabled v-model="record.remarks"/>
          </a-col>
          <a-col :span="24" style="margin-top: 10px">
            <a-form :form="form">
              <table width="100%"  class="tableStyle" style="border: 1px black">
                <tr>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:2%">

                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;">
                    产品名称
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:15%">
                    生产厂家
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:15%">
                    供应商
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:10%">
                    注册证号
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:12%">
                    规格
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:4%">
                    数量
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:4%">
                    单价
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:6%">
                    金额
                  </th>
                </tr>
                <tr v-for="(item, index2) in tableNumber[divNumber-1]">
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                    {{ index2+((divNumber-1)*10) +1}}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                    {{ item.productName }}

                    <a-form-item label="id" v-show="false">
                      <a-input v-decorator="[ 'pdStockRecordDetailList['+index2+'].id',{'initialValue':item.id} ]"></a-input>
                    </a-form-item>
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                    {{ item.venderName }}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                    {{ item.supplierName }}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                    <a-form-item label="" style="width: 100%;height: 100%;padding: 0px;margin: 0px;line-height: 0px">
                      <a-select
                        size="small"
                        style="width: 100%;font-size: xx-small"
                        :showArrow="false"
                        :dropdownMatchSelectWidth="false"
                        v-decorator="[ 'pdStockRecordDetailList['+index2+'].registration',{'initialValue':item.registrationSelected}]"
                      >
                        <a-select-option v-for="(registration, index) in item.registrationList" :key="registration">
                          {{ registration }}
                        </a-select-option>
                      </a-select>
                    </a-form-item>
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                    {{ item.spec }}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                    {{ item.orderNum }}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                    {{ item.purchasePrice }}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                    {{ item.orderMoney }}
                  </td>
                </tr>
                <tr>
                  <td  colspan="3" style="border: 1px solid #000000;text-align: center;padding: 3px 3px;font-size: small">
                  </td>
                  <td style="border: 1px solid #000000;text-align: center;padding: 3px 3px;font-size: small">
                  </td>
                  <td style="border: 1px solid #000000;text-align: center;padding: 3px 3px;font-size: larger;font-weight: bolder">
                    合计:
                  </td>
                  <td style="border: 1px solid #000000;text-align: center;padding: 3px 3px;font-size: small;">
                  </td>
                  <td style="border: 1px solid #000000;text-align: center;padding: 3px 3px;font-size: larger;font-weight: bolder">
                    {{record.totalNum}}
                  </td>
                  <td style="border: 1px solid #000000;text-align: center;padding: 3px 3px;font-size: small">
                  </td>
                  <td style="border: 1px solid #000000;text-align: center;padding: 3px 3px;font-size: larger;font-weight: bolder">
                    {{record.totalPrice}}
                  </td>
                </tr>
              </table>
            </a-form>
          </a-col>
        </a-col>
      </div>
      <div class="sign" style="text-align: left;height: inherit">
        <a-col :span="24" style="margin-top: 10px">
            <span style="margin-left: 3%">
              仓库管理员签字：
            </span>
          <a-input style="width: 10%;text-align: left" />
          <span style="margin-left: 17%">
              采购人员签字：
            </span>
          <a-input style="width: 10%;text-align: left" />
          <span style="margin-left: 17%">
              记账人员签字：
            </span>
          <a-input style="width: 10%;text-align: left" />
        </a-col>
      </div>
    </section>
    </j-modal>
  <!--</page-layout>-->
</template>
<script>
  import { httpAction } from '@/api/manage'
  export default {
    components: {

    },
    name: 'PdPurchaseOrderPrintModal',
    props:{
      reBizCode:{
        type: String,
        default: ''
      }
    },
    data(){
      return {
        form: this.$form.createForm(this),
        lockScroll: false,
        fullscreen: true,
        switchFullscreen: false,
        dataSource: [],
        divNumber:0,
        batchPrint:false,
        tableNumber:[],
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
        this.batchPrint = false;
        this.tableNumber = [];
        this.divNumber = 0;
        this.$emit('close')
      },
      /** 关闭按钮 **/
      handleCancel () {
        this.close()
      },
      show(record){
        console.log(record)
        this.visible = true;
        this.record = record;
        this.dataSource = record.pdPurchaseDetailList;
        this.printInit(this.dataSource);
        for (let item of this.dataSource){
          let registration = item.productRegistration.replace(/；/g, ";")
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