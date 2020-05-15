<template>
  <j-modal
    :visible="visible"
    :width="popModal.width"
    :maskClosable="disableSubmit"
    :title="popModal.title"
    :lockScroll="popModal.lockScroll"
    :fullscreen="popModal.fullscreen"
    :switchFullscreen="popModal.switchFullscreen"
    @cancel="handleCancel"
  >

    <a-spin :spinning="confirmLoading">
      <!-- 主表单区域 -->
      <div style="background:#ECECEC; padding:20px">
        <a-card title="" style="margin-bottom: 10px;">
          <a-form :form="form">
            <a-row>
              <a-col :span="6">
                <a-form-item label="打包流水码" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input disabled v-decorator="[ 'packageBarCode', validatorRules.packageBarCode]" placeholder="请输入出库单号"></a-input>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="打包人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input disabled v-decorator="[ 'createBy', validatorRules.createBy]" placeholder="请输入打包人"/>
                </a-form-item>
              </a-col>
              <a-col :span="6">
                <a-form-item label="打包日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <j-date disabled placeholder="请选择打包日期" v-decorator="[ 'createTime', validatorRules.createTime]" :trigger-change="true" style="width: 100%"/>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>

          <div class="table-operator">
            <a-tabs>
              <a-tab-pane tab="定数包明细" :forceRender="true">
                <a-button @click="choosePackageList" type="primary" icon="plus" style="margin-bottom: 8px;">选择定数包</a-button>
                <a-table
                  v-show="pdPackageTable.show"
                  ref="table"
                  bordered
                  rowKey="productId"
                  :pagination="false"
                  :columns="pdPackageTable.columns"
                  :dataSource="pdPackageTable.dataSource"
                  :loading="pdPackageTable.loading"
                >
                </a-table>
              </a-tab-pane>
            </a-tabs>
          </div>
        </a-card>

        <a-card style="margin-bottom: 10px;">
          <a-tabs>
            <a-tab-pane tab="库存明细" :forceRender="true">
              <div style="margin-bottom: 8px;">
                <a-button type="primary" icon="plus" @click="chooseProductList">选择库存</a-button>
                <a-popconfirm style="margin-left: 8px" :title="`确定要删除吗?`" @confirm="">
                  <a-button type="primary" icon="minus">删除</a-button>
                  <span class="gap"></span>
                </a-popconfirm>
              </div>
              <!--<a-table-->
                <!--v-show="pdProductStockTable.show"-->
                <!--ref="table"-->
                <!--bordered-->
                <!--rowKey="id"-->
                <!--:pagination="false"-->
                <!--:columns="pdProductStockTable.columns"-->
                <!--:dataSource="pdProductStockTable.dataSource"-->
                <!--:loading="pdProductStockTable.loading"-->
                <!--:rowSelection="{fixed:false,selectedRowKeys: pdProductStockTable.selectedRowKeys, onChange: onSelectChange}"-->
                <!--:customRow="onClickRow"-->
              <!--&gt;-->
              <!--</a-table>-->



              <!--<j-editable-table-->
                <!--bordered-->
                <!--ref="editable"-->
                <!--:loading="pdProductStockTable.loading"-->
                <!--:columns="pdProductStockTable.columns"-->
                <!--:dataSource="pdProductStockTable.dataSource"-->
                <!--:maxHeight="650"-->
                <!--:rowNumber="true"-->
                <!--:rowSelection="true"-->
                <!--:actionButton="false"-->
                <!--:disabled="disableSubmit"-->
                <!--@valueChange="valueChange"-->
                <!--style="text-overflow: ellipsis;"-->
              <!--&gt;-->
              <!--</j-editable-table>-->
              <!--<a-row style="margin-top:10px;text-align: right;padding-right: 5%">-->
                <!--<span style="font-weight: bold;font-size: large;padding-right: 5%">总数量：{{ totalSum }}</span>-->
                <!--<span style="font-weight: bold;font-size: large">总金额：{{ outTotalPrice }}</span>-->
              <!--</a-row>-->


            </a-tab-pane>
          </a-tabs>
        </a-card>

        <a-card style="">
          <a-form :form="form">
            <a-row>
              <a-col :span="12">
                <a-form-item label="备注" :labelCol="labelCol2" :wrapperCol="wrapperCol2" style="text-align: left">
                  <a-textarea :disabled="disableSubmit" v-decorator="[ 'remarks', validatorRules.remarks]" placeholder="请输入备注"></a-textarea>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-card>
      </div>
    </a-spin>

    <template slot="footer">
      <a-popconfirm title="确定放弃编辑？" @confirm="handleCancel" v-show="!disableSubmit" okText="确定" cancelText="取消">
        <a-button style="margin-right: 15px;">取  消</a-button>
      </a-popconfirm>
      <a-button @click="" style="margin-right: 15px;" type="primary">打  印</a-button>
      <a-button @click="" v-show="!disableSubmit" type="primary" :loading="confirmLoading" style="margin-right: 15px;">保存</a-button>
    </template>


    <pd-choose-package-list-model  ref="pdChoosePackageListModel" @ok="returnPackageData" ></pd-choose-package-list-model>
    <pd-choose-product-stock-list-model ref="pdChooseProductStockListModel" @ok="returnProductStockData" ></pd-choose-product-stock-list-model>

  </j-modal>
</template>

<script>

  import pick from 'lodash.pick'
  import JDate from '@/components/jeecg/JDate'
  import JDictSelectTagExpand from "@/components/dict/JDictSelectTagExpand"
  import ATextarea from "ant-design-vue/es/input/TextArea"
  // import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { httpAction, deleteAction, getAction } from '@/api/manage'
  import PdChoosePackageListModel from "./PdChoosePackageListModel"
  import PdChooseProductStockListModel from "./PdChooseProductStockListModel"

  export default {
    name: 'PdPackageRecordModal',
    // mixins: [JeecgListMixin],
    components: {
      PdChooseProductStockListModel,
      PdChoosePackageListModel,
      ATextarea,
      JDate,
      JDictSelectTagExpand
    },
    data() {
      return {
        form: this.$form.createForm(this),
        visible: false,
        confirmLoading: false,
        labelCol: {span: 6},
        wrapperCol: {span: 16},

        labelCol2: {span: 3},
        wrapperCol2: {span: 20},

        disableSubmit:false,

        queryParam:{},

        pdPackageTable:{
          show: false,
          dataSource:[],
          loading:false,
          // 表头
          columns: [
            {
              title: '#',
              dataIndex: 'id',
              key:'rowIndex',
              width:60,
              align:"center",
              customRender:function (t,r,index) {
                return parseInt(index) + 1;
              }
            },
            {
              title:'定数包编号',
              align:"center",
              dataIndex: 'packageCode'
            },
            {
              title:'定数包名称',
              align:"center",
              dataIndex: 'packageName',
              customRender:function (text) {
                return !text?"":(text.length>10?text.substr(0,10):text)
              }
            },
            {
              title:'产品编号',
              align:"center",
              width: 100,
              dataIndex: 'number'
            },
            {
              title:'产品名称',
              align:"center",
              dataIndex: 'productName'
            },
            {
              title:'规格',
              align:"center",
              dataIndex: 'spec'
            },
            {
              title:'型号',
              align:"center",
              dataIndex: 'version'
            },
            {
              title:'单位',
              align:"center",
              dataIndex: 'unitName'
            },
            {
              title:'定数包产品数量',
              align:"center",
              width: 200,
              dataIndex: 'count'
            },
            {
              title:'库存数量',
              align:"center",
              dataIndex: 'stockNum'
            },
            {
              title:'产品ID',
              align:"center",
              dataIndex: 'productId',
              colSpan: 0,
              customRender: (value, row, index) => {
                const obj = {
                  attrs: {colSpan:0},
                };
                return obj;
              },
            },
          ],
        },
        pdProductStockTable:{
          show: false,
          dataSource:[],
          selectedRowKeys: [],
          selectionRows: [],
          loading:false,
          subloading:false,
          // 表头
          columns: [

          ],
        },
        validatorRules: {
          packageBarCode:{},
          createBy:{},
          createTime:{},
          remarks:{},
        },
        url: {
          init:"/pd/pdPackagRecord/initModal",
          add: "/pd/pdPackagRecord/add",
        },
        popModal: {
          title: '这里是标题',
          visible: false,
          width: '100%',
          // width: '1200',
          style: { top: '20px' },
          switchFullscreen: true,  //缩放按钮
          lockScroll: false,
          fullscreen: true,
        },
      }
    },
    directives: {
    },
    methods: {
      // 重写close方法
      close() {
        this.visible = false;
        this.pdPackageTable.show = false;
        this.pdPackageTable.dataSource = [];
        this.pdProductStockTable.dataSource = [];
        // this.pdProductStockTable.selectedRowKeys = [];
        // this.pdProductStockTable.selectionRows = [];

        this.queryParam = {};
        this.$emit('close');
      },
      /** 关闭按钮点击事件 */
      handleCancel() {
        this.$emit('ok');
        this.close();
      },
      add() {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          // this.form.setFieldsValue(pick(this.model,'name','unitType','py','wb','zdy','remarks'))
        })
      },
      loadData(arg) {

      },
      /** 保存草稿 **/
      saveBtn() {
        this.request(this.url.add,"post","");
      },
      submitBtn(flag) {
        this.request(this.url.add,"post",flag);
      },
      /** 确定按钮点击事件 */
      request(url, method,flag) {

      },
      //删除行
      handleConfirmDelete() {

      },
      // 选择产品 新增行
      chooseProductList() {
        let packageData = this.pdPackageTable.dataSource;

        if(packageData.length <= 0){
          this.$message.error("请选择定数包！");
          return;
        }
        // let packageDetailList = packageData[0].pdPackageDetailList;
        let productIdList = [];
        packageData.forEach((item, idx) => {
          productIdList.push(item.productId)
        })
        this.$refs.pdChooseProductStockListModel.show({productIdList:productIdList});
      },
      // 选择产品后返回
      returnProductStockData(data) {

      },
      // 选择定数包
      choosePackageList() {
        this.$refs.pdChoosePackageListModel.show();
      },
      // 选择定数包后返回
      returnPackageData(data){
        // this.pdPackageTable.dataSource = data;
        let pdPackageDetailList = data[0].pdPackageDetailList;

        pdPackageDetailList.forEach((item, idx) => {
          item.packageCode = data[0].packageCode;
          item.packageName = data[0].packageName;
        })

        this.pdPackageTable.dataSource = pdPackageDetailList;
        this.pdPackageTable.show = true;
      },
      // 清空选中
      onClearSelected() {

      },
      //定数包删除行
      handleConfirmDeletePackage(){

      },
      onClickRow(record) {
      },
      // 点“选择产品”按钮后 调用 新增一行
      addrows(row){

      },
      // 计算总数量和总价格
      getTotalNumAndPrice(rows){

      },

    },
  }

</script>

<style scoped>
  .drawer-bootom-button {
    width: 100%;
    text-align: right;
    background: #fff;
    margin-top:10px;
  }
</style>