<template>
  <a-modal
    centered
    :title="title"
    :width="1200"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    :footer="null">

    <div :style="{ padding: '0 0 32px 32px' }">
      <h4 :style="{ marginBottom: '20px' }">{{this.model.productName+title1 }}</h4>
      <v-chart :force-fit="true" :height="height" :data="data1" :scale="scale" :onClick="handleClick">
        <v-tooltip/>
        <v-axis/>
        <v-legend/>
        <v-line position="type*金额" color="x"/>
        <v-point position="type*金额" color="x" :size="4" :v-style="style" :shape="'circle'"/>
      </v-chart>
    </div>


    <div :style="{ padding: '0 0 32px 32px' }">
      <h4 :style="{ marginBottom: '20px' }">{{this.model.productName+title2 }}</h4>
      <v-chart :force-fit="true" :height="height" :data="data2" :scale="scale" :onClick="handleClick">
        <v-tooltip/>
        <v-axis/>
        <v-legend/>
        <v-line position="type*数量" color="x"/>
        <v-point position="type*数量" color="x" :size="4" :v-style="style" :shape="'circle'"/>
      </v-chart>
    </div>
  </a-modal>


</template>

<script>
  import {httpAction,getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'
  import JDictSelectTag from "@/components/dict/JDictSelectTag"
  import {queryPdDepartTreeList} from '@/api/api'
  import { DataSet } from '@antv/data-set'
  export default {
    name: "inRecordChartMultid",
    components: {
      JDictSelectTag,
    },
    props: {
      title1: {
        type: String,
        default: '——金额统计(元)'
      },
      title2: {
        type: String,
        default: '——数量统计'
      },
      dataSource1: {
        type: Array,
        default: () => []
      },
      dataSource2: {
        type: Array,
        default: () => []
      },
      fields1: {
        type: Array,
        default: () => ['金额']
      },
      fields2: {
        type: Array,
        default: () => ['数量']
      },
      // 别名，需要的格式：[{field:'name',alias:'姓名'}, {field:'sex',alias:'性别'}]
      aliases:{
        type: Array,
        default: () => []
      },
      height: {
        type: Number,
        default: 254
      }
    },
    data () {
      return {
        scale: [{
          dataKey: 'x',
          min: 0,
          max: 1
        }],
        style: { stroke: '#fff', lineWidth: 1 },
        form: this.$form.createForm(this),
        title:"操作",
        drawerWidth:800,
        visible: false,
        model: {},
        localCategoryType:0,
        show:true,//根据菜单类型，动态显示隐藏表单元素
        treeData:[],
        validateStatus:"",
        disableSubmit:false,
        menuLabel:'名称',
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        validatorRules: {
        },
        url: {
          queryView: "/pd/pdBottleInf/queryRecordView",
        }
      }
    },
    created () {
    },
    computed: {
      data1() {

        const dv = new DataSet.View().source(this.dataSource1)
        dv.transform({
          type: 'fold',
          fields: this.fields1,
          key: 'x',
          value: '金额'
        })
        let rows =  dv.rows
        // 替换别名
        rows.forEach(row => {
          for (let item of this.aliases) {
            if (item.field === row.x) {
              row.x = item.alias
              break
            }
          }
        })
        return rows
      },
      data2() {
        const dv = new DataSet.View().source(this.dataSource2)
        dv.transform({
          type: 'fold',
          fields: this.fields2,
          key: 'x',
          value: '数量'
        })
        let rows =  dv.rows
        // 替换别名
        rows.forEach(row => {
          for (let item of this.aliases) {
            if (item.field === row.x) {
              row.x = item.alias
              break
            }
          }
        })
        return rows
      }
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.resetScreenSize(); // 调用此方法,根据屏幕宽度自适应调整抽屉的宽度
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.loadData();
      },


      loadData() {
        var params={};
        params.productId =this.model.productId;
        this.loading = true;
        getAction(this.url.queryView, params).then((res) => {
          if (res.success) {
            this.dataSource1 = res.result.orderMoney;
            this.dataSource2 = res.result.orderCount;
          }else{
            this.$message.warning(res.message)
          }
          this.loading = false;
        })
      },

      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {

      },
      handleCancel () {
        this.close()
      },
      handleClick(event, chart) {
        this.handleEvent('click', event, chart)
      },
      handleEvent(eventName, event, chart) {
        this.$emit(eventName, event, chart)
      },
      // 根据屏幕变化,设置抽屉尺寸
      resetScreenSize(){
        let screenWidth = document.body.clientWidth;
        if(screenWidth < 500){
          this.drawerWidth = screenWidth;
        }else{
          this.drawerWidth = 700;
        }
      },
    }
  }
</script>

<style lang="less" scoped>
  /** Button按钮间距 */
  .ant-btn {
    margin-left: 30px;
    margin-bottom: 30px;
    float: right;
  }
  .drawer-bootom-button {
    position: absolute;
    /*top:95%;*/
    bottom: -30px;
    width: 100%;
    border-top: 1px solid #e8e8e8;
    padding: 10px 16px;
    text-align: right;
    left: 0;
    background: #fff;
    border-radius: 0 0 2px 2px;
    z-index:199;
  }
</style>
