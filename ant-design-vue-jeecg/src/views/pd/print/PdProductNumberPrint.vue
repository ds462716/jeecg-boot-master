<template>
  <a-modal
    :visible="visible"
    :width="400"
    :height="150"
    @cancel="handleCancel"
    :closable="false"
  >
    <div class="no-print" style="text-align: right">
      <a-button class="no-print" @click="close" style="margin-right: 15px;" >关  闭</a-button>
      <a-button v-print="'#printContent'" ghost type="primary">打印</a-button>
    </div>
    <section  id="printContent">
      <div v-for="(item, index) in printData.length" style="page-break-after:always;width: 400px;">
          <svg :id="['barcode'+index]" style="margin-top:5px;" />
          <!--<div class="codeListBox"><p>编号：</p><div>{{ printData[index].number }}</div></div>-->
          <div v-if="printData[index].name.length < 12" class="codeListBox"><p>名称：</p><div>{{ printData[index].name }}</div></div>
          <div v-else class="codeListBoxM"><p>名称：</p><div>{{ printData[index].name }}</div></div>
          <div class="codeListBox"><p>规格：</p><div>{{ printData[index].spec }}</div></div>
          <div class="codeListBox"><p>厂家：</p><div>{{ printData[index].venderName }}</div></div>
          <!--<div class="codeListBox"><p>型号：</p><div>{{ printData[index].version }}</div></div>-->
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
    name: "PdProductNumberPrint",
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
        this.printData = record;
        this.visible = true;
        this.$nextTick(() => {
          for(let index = 0;index < record.length;index++){
            let id = "#barcode"+index;
            JsBarcode(id, record[index].number, {
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
      },

      print(){
        setTimeout(() => {
          this.$print(this.$refs.batchPrintRef);
        }, 1500)
      },
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
    font-size:12px;
    color:#666;
    padding:-5px -5px 0 -5px;
    margin:1px;
    text-align:left;}
  .codeListBox>div{
    display:inline-block;
    margin:1px;
    font-weight:400;
    font-size:12px;
    }
  .codeListBoxM>p{
    display:inline-block;
    font-size:12px;
    margin:1px;
    text-align:left;
    overflow: hidden;
  }
  .codeListBoxM>div{
    width: 230px;
    display:inline-block;
    margin:1px;
    font-weight:400;
    font-size:12px;
    white-space:normal;
    word-break: break-all;
  }
</style>