<template>
  <a-modal
    :visible="visible"
    :width="1200"
    :height="150"
    @cancel="handleCancel"
    :closable="false"
  >
    <button class="no-print" @click="print">打印</button>
    <section  ref="batchPrintRef" style="margin:0 auto;">
      <div v-for="(item, index) in printData.length">
        <div style="page-break-after:always">
          <div class="codeListBox" style="margin-top:2px;"><h5 style="display:inline-block;">厂商：</h5><span class="venderNameSpan">{{ printData[index].venderName }}</span></div>
          <div class="codeListBox"><h5 style="display:inline-block;">产品名称：</h5><span class="proNameSpan">{{ printData[index].productName }}</span></div>
          <div class="codeListBox"><h5 style="display:inline-block;">规格：</h5><span class="specSpan">{{ printData[index].spec }}</span></div>
          <div class="codeListBox" style="margin-bottom:15px;"><h5 style="display:inline-block;">型号：</h5><span class="versionSpan">{{ printData[index].version }}</span></div>
          <img :id="['barcode'+index]" style="width: 50%" />
        </div>
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
  //import Print from '@/utils/print'
  import Print from 'vue-print-nb'
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
        console.log(this.printData)
        this.visible = true;
        this.$nextTick(() => {
          for(let index = 0;index<record.length;index++){
            let id = "#barcode"+index;
            JsBarcode(id, record[index].productBarCode, {
              format: "CODE128",
              displayValue: 1,
              fontSize: 24,
              lineColor: "#000",
              height: 66,
              textAlign: "center"
            })
          }
        })
      },
      print(){
        setTimeout(() => {
          this.$print(this.$refs.batchPrintRef);
        }, 1500)

      }
    },

  }
</script>
<style scoped>
  .codeListBox>h5{display:inline-block;font-size:15px;color:#666;padding:5px 5px 0 5px;font-weight:400;width:110px;text-align:right;}
  .codeListBox>span{display:inline-block;font-size:13px;}
</style>