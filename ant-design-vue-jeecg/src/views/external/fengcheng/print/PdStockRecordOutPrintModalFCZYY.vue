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
      <a-button v-print="'#printContent'" ghost type="primary" >打印</a-button>
    </div>
    <section ref="print" id="printContent" class="printClass">
      <div style="text-align: center">
        <p style="font-size: 22px;font-weight: 800">{{title}}</p>
      </div>
      <!--签字-->
      <a-col :md="24" :sm="24">
        <div class="sign" style="text-align: left;height: inherit">
          <a-col :span="24">
            <span style="margin-left: 3%">
              出库单号：
            </span>
            <a-input style="width: 20%;text-align: left" disabled v-model="record.recordNo"/>
            <span style="margin-left: 3%">
              出库日期：
            </span>
            <a-input style="width: 20%;text-align: left" disabled v-model="record.auditDate"/>
            <span style="margin-left: 3%">
              操作人员：
            </span>
            <a-input style="width: 20%;text-align: left" disabled v-model="record.submitByName"/>
            <!--<span style="margin-left: 3%">-->
            <!--出库类型：-->
            <!--</span>-->
            <!--<a-input style="width: 20%;text-align: left" disabled v-model="record.outType" dictCode="out_type"/>-->
          </a-col>
          <!--<a-col :span="24" style="margin-top: 10px">-->
          <!--<span style="margin-left: 3%">-->
          <!--操作人员：-->
          <!--</span>-->
          <!--<a-input style="width: 20%;text-align: left" disabled v-model="record.submitByName"/>-->
          <!--<span style="margin-left: 3%">-->
          <!--审核人员：-->
          <!--</span>-->
          <!--<a-input style="width: 20%;text-align: left" disabled v-model="record.auditByName"/>-->
          <!--<span style="margin-left: 3%">-->
          <!--审核时间：-->
          <!--</span>-->
          <!--<a-input style="width: 20%;text-align: left" disabled v-model="record.auditDate"/>-->
          <!--</a-col>-->
          <a-col :span="24" style="margin-top: 0px">
            <span style="margin-left: 3%">
              出库库房：
            </span>
            <a-input style="width: 20%;text-align: left" disabled v-model="record.outDepartName"/>
            <span style="margin-left: 3%">
              入库库房：
            </span>
            <a-input style="width: 20%;text-align: left" disabled v-model="record.inDepartName"/>
            <span style="margin-left: 3%">
              申请人员：
            </span>
            <a-input style="width: 20%;text-align: left" />
          </a-col>
          <a-col :span="24" style="margin-top: 0px">
            <span style="margin-left: 3%;text-align: right">
              备注：
            </span>
            <a-input style="width: 50%;text-align: left" disabled v-model="record.remarks"/>
          </a-col>
          <a-col :span="24" style="margin-top: 5px">
            <a-form :form="form">
              <table width="100%" id="contentTable" class="tableStyle">
                <tr>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:18%">
                    产品名称
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:12%">
                    规格
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:8%">
                    批号
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:4%">
                    数量
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:4%">
                    单位
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:7%">
                    入库单价
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:7%">
                    入库金额
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:7%">
                    出库单价
                  </th>
                  <th style="border: 1px solid #000000;text-align: center;padding: 3px 3px;width:7%">
                    出库金额
                  </th>
                </tr>
                <tr v-for="(item, index) in dataSource">
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                    {{ item.productName }}

                    <a-form-item label="id" v-show="false">
                      <a-input v-decorator="[ 'pdStockRecordDetailList['+index+'].id',{'initialValue':item.id} ]"></a-input>
                    </a-form-item>
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                    {{ item.spec }}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                    {{ item.batchNo }}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                    {{ item.productNum }}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                    {{ item.unitName }}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                    {{ item.purchasePrice }}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                    {{ item.inTotalPrice }}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                    {{ item.sellingPrice }}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: xx-small">
                    {{ item.outTotalPrice }}
                  </td>
                </tr>
                <tr>
                  <td  colspan="2" style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: small">
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: larger;font-weight: bolder">
                    合计
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: larger;font-weight: bolder">
                    {{ record.totalSum }}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: small">
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: small">
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: larger;font-weight: bolder">
                    {{ record.inTotalPrice }}
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: small">
                  </td>
                  <td style="text-align: center;border: 1px solid #000000;padding: 3px 3px;font-size: larger;font-weight: bolder">
                    {{ record.outTotalPrice }}
                  </td>
                </tr>
              </table>
            </a-form>
          </a-col>

          <!--<a-col :span="24" style="margin-top: 5px;">-->
          <!--<span style="margin-left: 60%;">-->
          <!--合计数量：{{ record.totalSum }}-->
          <!--</span>-->
          <!--<span style="margin-left: 2%;font-weight: bold">-->
          <!--合计入库金额：{{ record.inTotalPrice }} 元-->
          <!--</span>-->
          <!--<span style="margin-left: 2%;">-->
          <!--合计出库金额：{{ record.outTotalPrice }} 元-->
          <!--</span>-->
          <!--</a-col>-->

          <a-col :span="24" style="margin-top: 5px">
            <span style="margin-left: 3%">
              <!--仓库人员签字：-->
              领用部门主管：
            </span>
            <a-input style="width: 10%;text-align: left" />
            <span style="margin-left: 3%">
              <!--销售人员签字：-->
              会计员：
            </span>
            <a-input style="width: 10%;text-align: left" />
            <span style="margin-left: 3%">
              <!--客户收货人签字：-->
              库管员：
            </span>
            <a-input style="width: 10%;text-align: left" />
            <span style="margin-left: 3%" v-show="showApplyBy">
              <!--客户收货人签字：-->
              领用人：
            </span>
            <a-input style="width: 10%;text-align: left"  v-show="showApplyBy"/>
          </a-col>
        </div>
      </a-col>
    </section>
  </j-modal>
  <!--</page-layout>-->
</template>
<script>

  import {initDictOptions, filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import { httpAction } from '@/api/manage'

  export default {
    componens: {

    },
    name: 'PdStockRecordOutPrintModalFCZYY',
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
        showApplyBy:true,
        columns: [
          { title: '产品名称', dataIndex: 'productName', align:"center", width:"16%" },
          { title: '生产厂家', dataIndex: 'venderName', align:"center",},
          { title: '注册证号', dataIndex: 'registration',align:"center",  },
          { title: '规格', dataIndex: 'spec',align:"center", },
          // { title: '型号', dataIndex: 'version',align:"center", width:"12%" },
          { title: '批号', dataIndex: 'batchNo',align:"center" },
          { title: '有效期', dataIndex: 'expDate',align:"center", width:"10%" },
          { title: '数量', dataIndex: 'productNum',align:"center", width:"6%" },
          // { title: '入库单价', dataIndex: 'purchasePrice',align:"center", width:"8%" },
          { title: '出库单价', dataIndex: 'sellingPrice',align:"center", width:"9%" },
          { title: '出库金额', dataIndex: 'outTotalPrice',align:"center", width:"9%"  },
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
        title:"出库单",
        visible: false,
        record:{},
        outTypeList:{},
        validatorRules:{},
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
        this.dataSource = [];
        this.$emit('close')
      },
      /** 关闭按钮 **/
      handleCancel () {
        this.close()
      },
      show(record){
        // for(let item of this.outTypeList){
        //   if(item.value == record.outType){
        //     record.outType = item.text;
        //     break;
        //   }
        // }

        this.visible = true;
        this.dataSource = record.pdStockRecordDetailList;
        this.record = record;
        this.showApplyBy = true;
        if(this.record.outType == "4"){
          this.showApplyBy = false;
        }

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
      initDictConfig(){ //静态字典值加载
        // initDictOptions('out_type').then((res) => {
        //   if (res.success) {
        //     this.outTypeList=res.result;
        //     this.$set(this.dictOptions, 'outType', res.result)
        //   }
        // })
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
  /*.printClass .ant-card-body{*/
  /*margin-left: 0%;*/
  /*margin-right: 0%;*/
  /*margin-bottom: 0%;*/
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