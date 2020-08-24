<template>
  <a-modal
    :visible="visible"
    :width="400"
    :height="150"
    @cancel="handleCancel"
    :closable="false"
  >
    <div class="no-print" style="text-align: right">
      <a-button v-print="'#printContent'" @click="savePintRecord()" ghost type="primary">打印</a-button>
    </div>
    <section  id="printContent">
      <div v-for="(item, index) in printData.length" style="page-break-after:always;width: 400px;">
          <svg :id="['barcode'+index]" style="margin-top:5px;" />
          <div class="codeListBox"><p>批号:</p><div>{{ printData[index].batchNo }}</div></div>
        <div class="codeListBox"><p>效期:</p><div>{{ printData[index].expDate }}</div>&nbsp;&nbsp;&nbsp;<p>类型:</p><div>{{ printData[index].productFlagName }}</div></div>
          <div v-if="printData[index].productName.length < 12" class="codeListBox"><p>名称：</p><div>{{ printData[index].productName }}</div></div>
          <div v-else class="codeListBoxM"><p>名称:</p><div>{{ printData[index].productName }}</div></div>
          <!--<div class="codeListBox"><p>产品类型:</p><div>{{ printData[index].productFlagName }}</div></div>-->
      </div>
    </section>

    <template  slot="footer">
      <a-button class="no-print" @click="close" style="margin-right: 15px;" >关  闭</a-button>
    </template>
  </a-modal>
</template>


<script>
  import Vue from 'vue'
  import JsBarcode from 'jsbarcode'
  import Print from '@/utils/print'
  import { httpAction ,getAction } from '@/api/manage'
  Vue.use(Print); //注册

  export default {
    name: "PdProductStockQueryPrint",
    components: {
    },
    data () {
      return {
        description: '条码打印',
        previewVisible:false,
        confirmLoading: false,
        visible: false,
        title:"",
        printData:[],
        lockScroll: false,
        fullscreen: true,
        switchFullscreen: false,
        dictOptions:{
        },
        tableScroll:{x :13*147+50},
        url: {
          uniqueCodeGeneration: "/pd/pdProductStockUniqueCode/uniqueCodeGeneration",
          batchCodeGeneration: "/pd/pdProductStockUniqueCode/batchCodeGeneration",
          updatePrintNum: "/pd/pdProductStockUniqueCode/updatePrintNum",
        },
      }
    },

    methods: {
      handleCancel () {
        this.close()
      },
      close () {
        //解决滚动条缓存bug
        this.$emit('close');
        this.visible = false;
      },
      init(record){
        let ids = "";
        for (let a = 0; a < record.length; a++) {
          ids += record[a].id + ",";
        }
        let formData = new URLSearchParams();
        formData.append("ids",ids);
        httpAction(this.url.batchCodeGeneration,formData,"post").then((res)=>{
          if(res.success){
            if(res.code ==200 || res.code ==202){
              if(res.code ==202){
                this.$message.error(res.message)
              }
              let uniqueCodes= res.result;
              if(uniqueCodes.length>0){
                this.printData = uniqueCodes;
                this.visible = true;
                this.$nextTick(() => {
                  for(let index = 0;index<uniqueCodes.length;index++){
                    let id = "#barcode"+index;
                    JsBarcode(id, uniqueCodes[index].id, {
                      format: "CODE128",
                      lineColor: "#000000",
                      font:"cursive",
                      fontOptions: "bold",
                      fontSize: 20,
                      textAlign:"center",
                      /*displayValue: false,*/
                      height: 66,
                      width:2,
                    })
                  }
                })
              }
            }else{
              this.$message.error(res.message)
            }
          }else{
            this.$message.error("系统异常!")
          }
          this.confirmLoading = false;
        }).finally(() => {
          this.confirmLoading = false;
        });
      },

      onlyInit(record,startOrder,endOrder){
        //查询库存数量根据数量生产唯一码
        let formData = new URLSearchParams();
        formData.append("productStockId",record.id);
        formData.append("startOrder",startOrder);
        formData.append("endOrder",endOrder);
        httpAction(this.url.uniqueCodeGeneration,formData,"post").then((res)=>{
          if(res.success){
            if(res.code ==200 || res.code ==202){
              if(res.code ==202){
                this.$message.error(res.message)
              }
              let uniqueCodes= res.result;
              if(uniqueCodes.length>0){
                this.printData = uniqueCodes;
                this.visible = true;
                this.$nextTick(() => {
                  for(let index = 0;index<uniqueCodes.length;index++){
                    let id = "#barcode"+index;
                    JsBarcode(id, uniqueCodes[index].id, {
                      format: "CODE128",
                      lineColor: "#000000",
                      font:"cursive",
                      fontOptions: "bold",
                      fontSize: 20,
                      textAlign:"center",
                      /*displayValue: false,*/
                      height: 66,
                      width:2,
                    })
                  }
                })
              }
            }else{
              this.$message.error(res.message)
            }
          }else{
            this.$message.error("系统异常!")
          }
          this.confirmLoading = false;
        }).finally(() => {
          this.confirmLoading = false;
        });
      },
      print(){
        setTimeout(() => {
          this.$print(this.$refs.batchPrintRef);
        }, 1500)

      },
      //报存打印日志
      savePintRecord(){
        if(this.printData.length>0){
          let that = this;
          httpAction(this.url.updatePrintNum,this.printData,"post").then((res)=>{

          })
        }
      }

    },

  }
</script>
<style scoped>
  * {
    color: #000000!important;
    -webkit-tap-highlight-color: #000000!important;
  }
  .codeListBox>p{
    display:inline-block;
    font-size:18px;
    color:#666;
    padding:-5px -5px 0 -5px;
    margin:1px;
    text-align:left;}
  .codeListBox>div{
    display:inline-block;
    margin:1px;
    font-weight:400;
    font-size:18px;
    }
  .codeListBoxM>p{
    display:inline-block;
    font-size:18px;
    margin:1px;
    text-align:left;
    overflow: hidden;
  }
  .codeListBoxM>div{
    width: 230px;
    display:inline-block;
    margin:1px;
    font-weight:400;
    font-size:14px;
    white-space:normal;
    word-break: break-all;
  }
</style>