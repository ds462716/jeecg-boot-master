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
      <div v-if="batchPrint">
        <div v-for="(item, index1) in divNumber-1"  class="sign" style="page-break-after:always;text-align: left;height: inherit">
        <div style="text-align: center">
          <p class="right">赣州市肿瘤医院医用耗材<em>入库单</em></p>
        </div>
        <a-col :md="24" :sm="24">
          <a-col :span="24">
            <span style="margin-left: 3%;font-size: 14px;">
              入库单号：
            </span>
            <a-input style="width: 25%;text-align: left;font-size: 14px;" disabled v-model="record.recordNo"/>
            <span style="margin-left: 3%;font-size: 14px;">
              入库日期：
            </span>
            <a-input style="width: 12%;text-align: left;font-size: 14px;" disabled v-model="record.auditDate"/>
            <span style="margin-left: 33%">
                页码：
             {{index1+1}} /{{divNumber}}
              </span>
          </a-col>
          <a-col :span="24" style="margin-top: 0px">
            <span style="margin-left: 3%;font-size: 14px;">
              货源：&nbsp;&nbsp;&nbsp;
            </span>
            <a-input style="width: 27%;text-align: left;font-size: 14px;" disabled v-model="record.supplierName"/>
            <span style="margin-left: 3%;font-size: 14px;">
              入库库房：
            </span>
            <a-input style="width: 12%;text-align: left;font-size: 14px;" disabled v-model="record.inDepartName"/>
            <span style="margin-left: 3%;text-align: right;font-size: 14px;">
              备注：
            </span>
            <a-input style="width: 28%;text-align: left" disabled v-model="record.remarks"/>
          </a-col>
          <a-col :span="24" style="margin-top: 0px">
          </a-col>
          <a-col :span="24" style="margin-top: 5px">
            <a-form :form="form">
              <table width="98%"  class="tableStyle" style="border: 1px black">
                <tr>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:2%">

                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:20%">
                    产品名称
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:12%">
                    规格
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:10%">
                    生产厂家
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:5%">
                    单位
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:8%">
                    数量
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:7%">
                    单价
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:7%">
                    入库金额
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:12%">
                    批号
                  </th>
                  <!--<th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:10%">
                    生产日期
                  </th>-->
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:10%">
                    有效期
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:10%">
                    注册证号
                  </th>
                </tr>
                <tr v-for="(item, index2) in tableNumber[index1]">
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small;">
                    {{ index2+(index1*tableLength) +1}}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: medium;">
                    {{ item.productName }}

                    <a-form-item label="id" v-show="false">
                      <a-input v-decorator="[ 'pdStockRecordDetailList['+index2+'].id',{'initialValue':item.id} ]"></a-input>
                    </a-form-item>
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: medium;">
                    {{ item.spec }}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small;">
                    {{ item.venderName }}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: medium;">
                    {{ item.unitName }}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: medium;">
                    {{ item.productNum }}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: medium;">
                    {{ item.purchasePrice }}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: medium;">
                    {{ item.inTotalPrice }}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: medium;">
                    {{ item.batchNo }}
                  </td>
                  <!--<td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: medium;">
                    {{ item.produceDate }}
                  </td>-->
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: medium;">
                    {{ item.expDate }}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small;">
                    <a-form-item label="" style="width: 100%;height: 100%;padding: 0px;margin: 0px;line-height: 0px">
                      <a-select
                        size="small"
                        style="width: 100%;font-size: xx-small;"
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
                </tr>
              </table>
            </a-form>
          </a-col>
        </a-col>
      </div>
      </div>
      <!-- 超过十个 或者不等于十个 -->
      <div  class="sign" style="page-break-after:always;text-align: left;height: inherit">
        <div style="text-align: center">
          <p class="right">赣州市肿瘤医院医用耗材<em>入库单</em></p>
        </div>
      <a-col :md="24" :sm="24">
          <a-col :span="24">
            <span style="margin-left: 3%;font-size: 14px;">
              入库单号：
            </span>
            <a-input style="width: 25%;text-align: left;font-size: 14px;" disabled v-model="record.recordNo"/>
            <span style="margin-left: 3%;font-size: 14px;">
              入库日期：
            </span>
            <a-input style="width: 12%;text-align: left;font-size: 14px;" disabled v-model="record.auditDate"/>
            <span style="margin-left: 33%">
                页码：
              {{divNumber}} /{{divNumber}}
              </span>
          </a-col>
          <a-col :span="24" style="margin-top: 0px">
            <span style="margin-left: 3%;font-size: 14px;">
              货源：&nbsp;&nbsp;&nbsp;
            </span>
            <a-input style="width: 27%;text-align: left;font-size: 14px;" disabled v-model="record.supplierName"/>
            <span style="margin-left: 3%;font-size: 14px;">
              入库库房：
            </span>
            <a-input style="width: 12%;text-align: left;font-size: 14px;" disabled v-model="record.inDepartName"/>
            <span style="margin-left: 3%;text-align: right;font-size: 14px;">
              备注：
            </span>
            <a-input style="width: 28%;text-align: left" disabled v-model="record.remarks"/>
          </a-col>
          <a-col :span="24" style="margin-top: 0px">
          </a-col>
          <a-col :span="24" style="margin-top: 5px">
            <a-form :form="form">
              <table width="98%"  class="tableStyle" style="border: 1px black">
                <tr>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:2%">

                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:20%">
                    产品名称
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:12%">
                    规格
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:10%">
                    生产厂家
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:5%">
                    单位
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:8%">
                    数量
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:7%">
                    入库单价
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:7%">
                    入库金额
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:12%">
                    批号
                  </th>
                  <!--<th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:10%">
                    生产日期
                  </th>-->
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:10%">
                    有效期
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:10%">
                    注册证号
                  </th>
                </tr>
                <tr v-for="(item, index2) in tableNumber[divNumber-1]">
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small;">
                    {{ index2+((divNumber-1)*tableLength) +1}}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: medium;">
                    {{ item.productName }}

                    <a-form-item label="id" v-show="false">
                      <a-input v-decorator="[ 'pdStockRecordDetailList['+index2+'].id',{'initialValue':item.id} ]"></a-input>
                    </a-form-item>
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: medium;">
                    {{ item.spec }}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small;">
                    {{ item.venderName }}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: medium;">
                    {{ item.unitName }}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: medium;">
                    {{ item.productNum }}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: medium;">
                    {{ item.purchasePrice }}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: medium;">
                    {{ item.inTotalPrice }}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: medium;">
                    {{ item.batchNo }}
                  </td>
                  <!--<td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: medium;">
                    {{ item.produceDate }}
                  </td>-->
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: medium;">
                    {{ item.expDate }}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small;">
                    <a-form-item label="" style="width: 100%;height: 100%;padding: 0px;margin: 0px;line-height: 0px">
                      <a-select
                        size="small"
                        style="width: 100%;font-size: xx-small;"
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
                </tr>
                <tr>
                  <td  colspan="2" style="border-bottom: 1px solid #000000;text-align: center;padding: 3px 3px;font-size: small">
                  </td>
                  <td colspan="2"style="border-bottom: 1px solid #000000;text-align: center;padding: 3px 3px;font-size: medium;">
                    合计:
                  </td>
                  <td style="border-bottom: 1px solid #000000;text-align: center;padding: 3px 3px;font-size: small">
                  </td>
                  <td style="border-bottom: 1px solid #000000;text-align: center;padding: 3px 3px;font-size: medium;;">
                    {{ record.totalSum }}
                  </td>
                  <td style="border-bottom: 1px solid #000000;text-align: center;padding: 3px 3px;font-size: small">
                  </td>
                  <td style="border-bottom: 1px solid #000000;text-align: center;padding: 3px 3px;font-size: medium;;">
                    {{ record.inTotalPrice }}
                  </td>
                  <td style="border-bottom: 1px solid #000000;text-align: center;padding: 3px 3px;font-size: small">
                  </td>
                  <td style="border-bottom: 1px solid #000000;text-align: center;padding: 3px 3px;font-size: small">
                  </td>
                  <td style="border-bottom: 1px solid #000000;text-align: center;padding: 3px 3px;font-size: small">
                  </td>
                  <td style="border-bottom: 1px solid #000000;text-align: center;padding: 3px 3px;font-size: small">
                  </td>
                </tr>
              </table>
            </a-form>
          </a-col>
      </a-col>
        <a-col :span="24" style="margin-top: 5px">
              <span style="margin-left: 3%">
                科室负责人：
              </span>
          <a-input style="width: 13%;text-align: left" value="" />
          <span style="margin-left: 3%">
                验收人：
              </span>
          <a-input style="width: 13%;text-align: left" value=""/>
          <span style="margin-left: 3%">
                保管：
              </span>
          <a-input style="width: 13%;text-align: left" value="" />
          <span style="margin-left: 3%">
                材料会计：
              </span>
          <a-input style="width: 13%;text-align: left" value="" />
        </a-col>
      </div>


      <!-- 赣州肿瘤医院验收单start -->
      <div v-if="batchPrint">
        <div v-for="(item, index1) in divNumber-1"  class="sign" style="page-break-after:always;text-align: left;height: inherit">
          <div style="text-align: center">
            <p class="right">赣州市肿瘤医院医用耗材<em>验收单</em></p>
          </div>
          <a-col :md="24" :sm="24">
            <a-col :span="24">
            <span style="margin-left: 3%;font-size: 14px;">
              入库单号：
            </span>
              <a-input style="width: 25%;text-align: left;font-size: 14px;" disabled v-model="record.recordNo"/>
              <span style="margin-left: 3%;font-size: 14px;">
              入库日期：
            </span>
              <a-input style="width: 12%;text-align: left;font-size: 14px;" disabled v-model="record.auditDate"/>
              <span style="margin-left: 33%">
                页码：
             {{index1+1}} /{{divNumber}}
              </span>
            </a-col>
            <a-col :span="24" style="margin-top: 0px">
            <span style="margin-left: 3%;font-size: 14px;">
              货源：&nbsp;&nbsp;&nbsp;
            </span>
              <a-input style="width: 27%;text-align: left;font-size: 14px;" disabled v-model="record.supplierName"/>
              <span style="margin-left: 3%;font-size: 14px;">
              入库库房：
            </span>
              <a-input style="width: 12%;text-align: left;font-size: 14px;" disabled v-model="record.inDepartName"/>
              <span style="margin-left: 3%;text-align: right;font-size: 14px;">
              单据号：
            </span>
              <a-input style="width: 25%;text-align: left;font-size: 14px;" disabled v-model="record.recordNo"/>
            </a-col>
            <a-col :span="24" style="margin-top: 0px">
            </a-col>
            <a-col :span="24" style="margin-top: 5px">
              <a-form :form="form">
                <table width="98%"  class="tableStyle" style="border: 1px black">
                  <tr>
                    <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:2%">

                    </th>
                    <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:20%">
                      产品名称
                    </th>
                    <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:12%">
                      规格
                    </th>
                    <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:10%">
                      生产厂家
                    </th>
                    <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:5%">
                      单位
                    </th>
                    <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:8%">
                      数量
                    </th>
                    <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:12%">
                      批号
                    </th>
                    <!--<th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:10%">
                      生产日期
                    </th>-->
                    <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:10%">
                      有效期
                    </th>
                    <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:10%">
                      注册证号
                    </th>
                    <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:10%">
                      验收结论
                    </th>
                  </tr>
                  <tr v-for="(item, index2) in tableNumber[index1]">
                    <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small;">
                      {{ index2+(index1*tableLength) +1}}
                    </td>
                    <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: medium;">
                      {{ item.productName }}

                      <a-form-item label="id" v-show="false">
                        <a-input v-decorator="[ 'pdStockRecordDetailList['+index2+'].id',{'initialValue':item.id} ]"></a-input>
                      </a-form-item>
                    </td>
                    <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: medium;">
                      {{ item.spec }}
                    </td>
                    <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small;">
                      {{ item.venderName }}
                    </td>
                    <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: medium;">
                      {{ item.unitName }}
                    </td>
                    <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: medium;">
                      {{ item.productNum }}
                    </td>
                    <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: medium;">
                      {{ item.batchNo }}
                    </td>
                    <!--<td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: medium;">
                      {{ item.produceDate }}
                    </td>-->
                    <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: medium;">
                      {{ item.expDate }}
                    </td>
                    <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small;">
                      <a-form-item label="" style="width: 100%;height: 100%;padding: 0px;margin: 0px;line-height: 0px">
                        <a-select
                          size="small"
                          style="width: 100%;font-size: xx-small;"
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
                    <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: medium;">
                      验收合格
                    </td>
                  </tr>
                </table>
              </a-form>
            </a-col>
          </a-col>
        </div>
      </div>
      <!-- 超过十个 或者不等于十个 -->
      <div  class="sign" style="page-break-after:always;text-align: left;height: inherit">
        <div style="text-align: center">
          <p class="right">赣州市肿瘤医院医用耗材<em>验收单</em></p>
        </div>
        <a-col :md="24" :sm="24">
          <a-col :span="24">
            <span style="margin-left: 3%;font-size: 14px;">
              入库单号：
            </span>
            <a-input style="width: 25%;text-align: left;font-size: 14px;" disabled v-model="record.recordNo"/>
            <span style="margin-left: 3%;font-size: 14px;">
              入库日期：
            </span>
            <a-input style="width: 12%;text-align: left;font-size: 14px;" disabled v-model="record.auditDate"/>
            <span style="margin-left: 33%">
                页码：
              {{divNumber}} /{{divNumber}}
              </span>
          </a-col>
          <a-col :span="24" style="margin-top: 0px">
            <span style="margin-left: 3%;font-size: 14px;">
              货源：&nbsp;&nbsp;&nbsp;
            </span>
            <a-input style="width: 27%;text-align: left;font-size: 14px;" disabled v-model="record.supplierName"/>
            <span style="margin-left: 3%;font-size: 14px;">
              入库库房：
            </span>
            <a-input style="width: 12%;text-align: left;font-size: 14px;" disabled v-model="record.inDepartName"/>
            <span style="margin-left: 3%;text-align: right;font-size: 14px;">
             单据号：
            </span>
            <a-input style="width: 25%;text-align: left;font-size: 14px;font-weight: 800" disabled v-model="record.recordNo"/>
          </a-col>
          <a-col :span="24" style="margin-top: 0px">
          </a-col>
          <a-col :span="24" style="margin-top: 5px">
            <a-form :form="form">
              <table width="98%"  class="tableStyle" style="border: 1px black">
                <tr>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:2%">

                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:20%">
                    产品名称
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:12%">
                    规格
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:10%">
                    生产厂家
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:5%">
                    单位
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:8%">
                    数量
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:12%">
                    批号
                  </th>
                  <!--<th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:10%">
                    生产日期
                  </th>-->
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:10%">
                    有效期
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:10%">
                    注册证号
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:10%">
                    验收结论
                  </th>
                </tr>
                <tr v-for="(item, index2) in tableNumber[divNumber-1]">
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small;">
                    {{ index2+((divNumber-1)*tableLength) +1}}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: medium;">
                    {{ item.productName }}

                    <a-form-item label="id" v-show="false">
                      <a-input v-decorator="[ 'pdStockRecordDetailList['+index2+'].id',{'initialValue':item.id} ]"></a-input>
                    </a-form-item>
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: medium;">
                    {{ item.spec }}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small;">
                    {{ item.venderName }}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: medium;">
                    {{ item.unitName }}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: medium;">
                    {{ item.productNum }}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: medium;">
                    {{ item.batchNo }}
                  </td>
                  <!--<td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: medium;">
                    {{ item.produceDate }}
                  </td>-->
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: medium;">
                    {{ item.expDate }}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small;">
                    <a-form-item label="" style="width: 100%;height: 100%;padding: 0px;margin: 0px;line-height: 0px">
                      <a-select
                        size="small"
                        style="width: 100%;font-size: xx-small;"
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
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: medium;">
                    验收合格
                  </td>
                </tr>
              </table>
            </a-form>
          </a-col>
        </a-col>
        <a-col :span="24" style="margin-top: 5px">
              <span style="margin-left: 3%">
                制表：
              </span>
          <a-input style="width: 13%;text-align: left" value="曾波" />
          <span style="margin-left: 3%">
                主管：
              </span>
          <a-input style="width: 13%;text-align: left" value=""/>
          <span style="margin-left: 3%">
                验收：
              </span>
          <a-input style="width: 13%;text-align: left" value="" />
          <span style="margin-left: 3%">
                采购：
              </span>
          <a-input style="width: 13%;text-align: left" value="" />
          <span style="margin-left: 3%">
                会计：
              </span>
          <a-input style="width: 13%;text-align: left" value="" />
        </a-col>
      </div>
      <!-- 赣州肿瘤医院验收单end -->
    </section>
  </j-modal>
</template>
<script>

  import { httpAction } from '@/api/manage'

  export default {
    components: {

    },
    name: 'PdStockRecordInPrintModalGZZLYY',
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
        dataSource: [],
        divNumber:0,
        batchPrint:false,
        tableNumber:[],
        tableLength:10,
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
        this.visible = true;
        this.record = record;
        this.dataSource = record.pdStockRecordDetailList;
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
      printBtn(){
        this.form.validateFields((err, values) => {
          // if (!err) {
          //选择标识符的校验通过后
          if(values.hasOwnProperty("pdStockRecordDetailList")){
            let formData = Object.assign(this.model, values);
            httpAction(this.url.edit,formData,'put').then((res)=>{
              if(res.success){
                // this.$message.success(res.message);
              }else{
                // this.$message.warning(res.message);
              }
            }).finally(() => {
            })
          }else{
            this.$message.error("请添加标识符");
          }
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
      printInit(data){
        let length = this.tableLength;
        let number = data.length;
        if(number>length){
          this.batchPrint = true;
        }
        let num;
        if(number%length==0){
          num = parseInt(number/length);
          for(let index =0;index<num;index++){
            let lsData = [];
            for(let i =0;i<length;i++){
              lsData.push(data[index*length+i])
            }
            this.tableNumber.push(lsData);
          }
        }else{
          num = parseInt(number/length);
          if(num>0){
            for(let index =0;index<num;index++){
              let lsData = [];
              for(let i =0;i<length;i++){
                lsData.push(data[index*length+i]);
              }
              this.tableNumber.push(lsData);
            }
            let wsData = [];
            let ws = parseInt(number%length);
            for(let x =0;x<ws;x++){
              wsData.push(data[num*length+x]);
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
<style lang="scss" scoped >
  /*update_begin author:scott date:20191203 for:打印机打印的字体模糊问题 */
  * {
    color: #000000!important;
    -webkit-tap-highlight-color: #000000!important;
    font-family:'SimHei';
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

  .right{
    font-size: x-large;
    font-weight: 800;
    text-align: -moz-center !important;
    text-align: center;
    vertical-align: middle;
  }
  .right em{
    font-size:  xx-large;
    font-family:'SimHei';
  }
</style>