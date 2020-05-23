<template>
  <a-modal
    :title="title"
    :width="500"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item   label="移入库位"    :labelCol="labelCol" :wrapperCol="wrapperCol">
           <a-select
                showSearch
                :huoweiCode="huoweiValue"
                placeholder="请选择库位"
                :defaultActiveFirstOption="false"
                :allowClear="true"
                :showArrow="true"
                :filterOption="false"
                :disabled="disableSubmit"
                @search="unitHandleSearch"
                @focus="unitHandleSearch"
                :notFoundContent="notFoundContent"
                v-decorator="[ 'huoweiCode', validatorRules.huoweiCode]"
              >
             <a-select-option v-for="d in formData" :key="d.value">{{d.text}}</a-select-option>
           </a-select>
        </a-form-item>
        <a-form-item   label="移库数量"    :labelCol="labelCol" :wrapperCol="wrapperCol">
          <a-input style="width:100%;"  ref="inputFocus" v-decorator="[ 'ykStockNum', validatorRules.ykStockNum]" placeholder="请输入值"></a-input>
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>
<script>

  import { httpAction,getAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import JDictSelectTagExpand from "@/components/dict/JDictSelectTagExpand"


  let timeout;
  let currentValue;

  function fetch(value, callback,url) {
    if (timeout) {
      clearTimeout(timeout);
      timeout = null;
    }
    currentValue = value;

    function fake() {
      getAction(url,{name:value}).then((res)=>{
        if (!res.success) {
          this.cmsFailed(res.message);
        }
        if (currentValue === value) {
          const result = res.result;
          const data = [];
          result.forEach(r => {
            data.push({
              value: r.value,
              text: r.label,
            });
          });
          callback(data);
        }
      })
    }
    timeout = setTimeout(fake, 300);
  }
  export default {
    name: "PdStockHuoWeiModal",
    components: {
      JDictSelectTagExpand,
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:800,
        visible: false,
         huoweiValue: undefined,
        notFoundContent:"未找到内容",
        model: {},
        formData:[],
        disableSubmit:false,
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        typeValue : false,
        confirmLoading: false,
        validatorRules:{
          huoweiCode:{rules: [{ required: true, message: '请输入值!' }]},
          ykStockNum:{rules: [{ required: true, message: '请输入值!' }
              , {pattern: '^(?:[1-9][0-9]*(?:\\.[0-9]+)?|0\\.(?!0+$)[0-9]+)$',
                message: '格式不正确'
              }]},
                },
        url: {
          list:"/pd/pdProductStockTotal/queryHuoWei",
          edit : "/pd/pdProductStockTotal/updateStockHuowei",
        }

      }
    },
    created () {

    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
      },

      //单位查询start
      unitHandleSearch(value) {
        fetch(value, data => (this.formData = data),this.url.list);
      },

      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        const that = this;
        var stockNum=this.model.stockNum;
        var ykStockNum=this.form.getFieldValue("ykStockNum");
        if(ykStockNum > stockNum){
          that.$message.warning("移库数量不能大于当前库存数量");
          return;
        }
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let formData =Object.assign(this.model, values);
            console.log("表单提交数据",formData)
            httpAction(this.url.edit,formData,"put").then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })
          }

        })
      },
      handleCancel () {
        this.close()
      },

    }
  }
</script>