<template>
  <j-modal
    :visible="visible"
    :width="1200"
    :title="title"
    :lockScroll="lockScroll"
    :fullscreen="fullscreen"
    :switchFullscreen="switchFullscreen"
    @cancel="handleCancel"

  >
    <button @click="print">打印</button>
    <div ref="print" v-for="(item, index) in printData.length">
      <img :id="['barcode'+index]" style="width: 100%" />
    </div>


  </j-modal>
</template>


<script>
  import Vue from 'vue'
  import JsBarcode from 'jsbarcode'
  import Print from '@/utils/print'
  Vue.use(Print)
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
        title:"操作",
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
        console.log(record);
        this.printData = record;
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
        this.$print(this.$refs.print);
      }
    },

  }
</script>
<style scoped>
</style>