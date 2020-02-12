<template xmlns:background-color="http://www.w3.org/1999/xhtml">
  <a-row :gutter="10">
    <a-col :md="9" :sm="24">
      <a-card :bordered="false">

        <!-- 按钮操作区域 -->
        <a-row style="margin-left: 14px">
          <a-button @click="handleAdd(1)" type="primary" v-show="showHuoqu">添加货区</a-button>
          <a-button @click="handleAdd(2)" type="primary" v-show="showHuowei">添加货位</a-button>
          <!--<a-button @click="handleAdd(1)" type="primary">添加一级部门</a-button>-->
          <!--<a-button type="primary" icon="download" @click="handleExportXls('部门信息')">导出</a-button>-->
          <!--<a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">-->
            <!--<a-button type="primary" icon="import">导入</a-button>-->
          <!--</a-upload>-->
          <a-button title="删除多条数据" @click="batchDel" type="default" v-show="showDelete">删除</a-button>
          <!--<a-button @click="refresh" type="default" icon="reload" :loading="loading">刷新</a-button>-->
        </a-row>
        <div style="background: #fff;padding-left:16px;height: 100%; margin-top: 5px">
          <a-alert type="info" :showIcon="true">
            <div slot="message">
              当前选择：
              <a v-if="this.currSelected.title">{{ getCurrSelectedTitle() }}</a>
              <a v-if="this.currSelected.title" style="margin-left: 10px" @click="onClearSelected">取消选择</a>
            </div>
          </a-alert>
          <a-input-search @search="onSearch" style="width:100%;margin-top: 10px" placeholder="请输入部门名称"/>
          <!-- 树-->
          <a-col :md="10" :sm="24">
            <template>
              <a-dropdown :trigger="[this.dropTrigger]" @visibleChange="dropStatus">
               <span style="user-select: none">
            <a-tree
              checkable
              multiple
              @select="onSelect"
              @check="onCheck"
              :selectedKeys="selectedKeys"
              :checkedKeys="checkedKeys"
              :treeData="departTree"
              :checkStrictly="checkStrictly"
              :expandedKeys="iExpandedKeys"
              :autoExpandParent="autoExpandParent"
              @expand="onExpand"/>
                </span>
                <!--新增右键点击事件,和增加添加和删除功能-->
                <!--<a-menu slot="overlay">-->
                  <!--<a-menu-item @click="handleAdd(3)" key="1">添加</a-menu-item>-->
                  <!--<a-menu-item @click="handleDelete" key="2">删除</a-menu-item>-->
                  <!--<a-menu-item @click="closeDrop" key="3">取消</a-menu-item>-->
                <!--</a-menu>-->
              </a-dropdown>
            </template>
          </a-col>
        </div>
      </a-card>
      <!---- author:os_chengtgen -- date:20190827 --  for:切换父子勾选模式 =======------>
      <div class="drawer-bootom-button">
        <a-dropdown :trigger="['click']" placement="topCenter">
          <a-menu slot="overlay">
            <!--<a-menu-item key="1" @click="switchCheckStrictly(1)">父子关联</a-menu-item>-->
            <!--<a-menu-item key="2" @click="switchCheckStrictly(2)">取消关联</a-menu-item>-->
            <a-menu-item key="3" @click="checkALL">全部勾选</a-menu-item>
            <a-menu-item key="4" @click="cancelCheckALL">取消全选</a-menu-item>
            <a-menu-item key="5" @click="expandAll">展开所有</a-menu-item>
            <a-menu-item key="6" @click="closeAll">合并所有</a-menu-item>
          </a-menu>
          <a-button>
            树操作 <a-icon type="up" />
          </a-button>
        </a-dropdown>
      </div>
      <!---- author:os_chengtgen -- date:20190827 --  for:切换父子勾选模式 =======------>
    </a-col>
    <a-col :md="15" :sm="24" v-show="showGoodsCard">
      <a-card :bordered="false">
        <a-alert type="info" :showIcon="false">
          <div slot="message">
            <span style="font-weight: bold;color: #2eabff">{{ title1 }} </span> {{ title2 }}
          </div>
        </a-alert>

        <a-form :form="form" style="margin-top: 10px" >
          <a-form-item label="部门名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input disabled v-decorator="['departName', validatorRules.departName ]" placeholder="请输入机构/部门名称"/>
            <a-input v-decorator="['departId', validatorRules.departId ]" v-show="false"/>
          </a-form-item>
          <!--<a-form-item label="上级机构" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
            <!--<a-tree-select style="width:100%" :dropdownStyle="{maxHeight:'200px',overflow:'auto'}"-->
              <!--:treeData="treeData" :disabled="disable" v-model="model.departParentId" placeholder="无">-->
            <!--</a-tree-select>-->
          <!--</a-form-item>-->
          <a-form-item :label="title2+'名称'" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="['name', validatorRules.name ]" @change="pinyinTran" placeholder="请输入名称"/>
          </a-form-item>
          <a-form-item label="下级存放单位编号标识" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input disabled v-decorator="['subCode', validatorRules.subCode ]" placeholder="请输入下级存放单位编号标识" />
          </a-form-item>
          <a-form-item label="编号后缀" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="['codeSuffix', validatorRules.codeSuffix ]" @change="getCode" placeholder="请输入编号后缀" />
          </a-form-item>
          <a-form-item :label="title2+'编号'" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input disabled v-decorator="[ 'code', validatorRules.code]" placeholder="请输入编号"></a-input>
          </a-form-item>
          <a-form-item label="类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <template>
              <a-radio-group disabled v-decorator="['areaType',validatorRules.areaType]" placeholder="类型">
                <a-radio value="1">
                  货区
                </a-radio>
                <a-radio value="2" >
                  货位
                </a-radio>
              </a-radio-group>
            </template>
          </a-form-item>

          <a-form-item label="地址" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="[ 'address', validatorRules.address]" placeholder="请输入地址"></a-input>
          </a-form-item>

          <a-form-item label="面积" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input-number v-decorator="[ 'area', validatorRules.area]" placeholder="请输入面积" style="width: 100%"/>
          </a-form-item>
          <a-form-item label="容积" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input-number v-decorator="[ 'volume', validatorRules.volume]" placeholder="请输入容积" style="width: 100%"/>
          </a-form-item>
          <a-form-item label="联系人" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="[ 'contacts', validatorRules.contacts]" placeholder="请输入联系人"></a-input>
          </a-form-item>
          <a-form-item label="联系方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="[ 'contactsPhone', validatorRules.contactsPhone]" placeholder="请输入联系方式"></a-input>
          </a-form-item>
          <a-form-item label="下级存放单位数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input-number v-decorator="[ 'subNum', validatorRules.subNum]" placeholder="请输入下级存放单位数量" style="width: 100%"/>
          </a-form-item>
          <a-form-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <template>
              <a-radio-group v-decorator="['state',validatorRules.state]" placeholder="类型">
                <a-radio value="0">
                  未启用
                </a-radio>
                <a-radio value="1">
                  启用
                </a-radio>
              </a-radio-group>
            </template>
          </a-form-item>
          <a-form-item label="拼音码" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="[ 'py', validatorRules.py]" placeholder="请输入拼音码"></a-input>
          </a-form-item>
          <a-form-item label="五笔码" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="[ 'wb', validatorRules.wb]" placeholder="请输入五笔码"></a-input>
          </a-form-item>
          <a-form-item label="自定义码" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="[ 'zdy', validatorRules.zdy]" placeholder="请输入自定义码"></a-input>
          </a-form-item>
          <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input v-decorator="[ 'remarks', validatorRules.remarks]" placeholder="请输入备注"></a-input>
          </a-form-item>
        </a-form>
        <div class="anty-form-btn">
          <a-button @click="cancelCurrForm" type="default" htmlType="button" icon="close">取消</a-button>
          <a-button @click="emptyCurrForm" type="default" htmlType="button" icon="sync">重置</a-button>
          <a-button @click="editCurrForm" type="primary" htmlType="button" icon="form" v-show="showEdit">修改并保存</a-button>
          <a-button @click="submitCurrForm" type="primary" htmlType="button" icon="form" v-show="showSubmit">保存</a-button>
        </div>
      </a-card>
    </a-col>
  </a-row>
</template>
<script>
  import pick from 'lodash.pick'
  import {queryGoodsAllocationTreeList, searchByKeywords, deleteByDepartId,duplicateCheckHasDelFlag} from '@/api/api'
  import {httpAction, deleteAction, getAction} from '@/api/manage'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import { makeWb } from '@/utils/wubi'

  export default {
    name: 'PdGoodsAllocationList',
    mixins: [JeecgListMixin],
    components: {
    },
    data() {
      return {
        title1:'',
        title2:'',
        showGoodsCard:false,
        showEdit:false,
        showSubmit:false,
        showDelete:false,
        showHuoqu:false,
        showHuowei:false,
        iExpandedKeys: [],
        loading: false,
        autoExpandParent: true,
        currFlowId: '',
        currFlowName: '',
        disable: true,
        treeData: [],
        visible: false,
        departTree: [],
        rightClickSelectedKey: '',
        hiding: true,
        model: {},
        dropTrigger: '',
        depart: {},
        disableSubmit: false,
        checkedKeys: [],
        selectedKeys: [],
        autoIncr: 1,
        currSelected: {},
        allTreeKeys:[],
        checkStrictly: true, // 父子关联

        goodsParentId:"", //货位父ID
        goodsCode:"",
        subCode:'',
        parentCode:'',

        form: this.$form.createForm(this),
        labelCol: {
          xs: {span: 24},
          sm: {span: 5}
        },
        wrapperCol: {
          xs: {span: 24},
          sm: {span: 16}
        },
        graphDatasource: {
          nodes: [],
          edges: []
        },
        validatorRules: {
          departName: {rules: [{required: true, message: '请输入机构/部门名称!'}]},
          departId: {rules: []},
          subCode: {rules: [{required: true, message: '下级存放单位编号标识!'}]},
          // orgCategory: {rules: [{required: true, message: '请输入机构类型!'}]},
          mobile: {rules: [{validator: this.validateMobile}]},
          name: {rules: [{required: true, message: '请输入名称!'}]},
          code: {rules: [{required: true, message: '请输入编号!'}]},
          codeSuffix: {rules: [{required: true, message: '请输入编号后缀!'},{validator: this.validateCode}]},
          areaType: {rules: []},
          address: {rules: []},
          area: {rules: [{pattern:/^-?\d+\.?\d*$/, message: '请输入数字!'},]},
          volume: {rules: [{pattern:/^-?\d+\.?\d*$/, message: '请输入数字!'},]},
          contacts: {rules: []},
          contactsPhone: {rules: [{pattern:/^1[3456789]\d{9}$/, message: '请输入正确的手机号码!'},]},
          subNum: {rules: [{pattern:/^-?\d+$/, message: '请输入整数!'},]},
          state: {rules: []},
          py: {rules: []},
          wb: {rules: []},
          zdy: {rules: []},
          remarks: {rules: []},
        },
        url: {
          add: "/pd/pdGoodsAllocation/add",
          edit: "/pd/pdGoodsAllocation/edit",
          delete: "/pd/pdGoodsAllocation/delete",
          deleteBatch: "/pd/pdGoodsAllocation/deleteBatch",
          queryById: "/pd/pdGoodsAllocation/queryById",
        },
        orgCategoryDisabled:false,
      }
    },
    computed: {
      // importExcelUrl: function () {
      //   return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      // }
    },
    methods: {
      loadData() {
        this.refresh();
      },
      loadTree() {
        var that = this
        that.treeData = []
        that.departTree = []
        queryGoodsAllocationTreeList().then((res) => {
          if (res.success) {
            for (let i = 0; i < res.result.length; i++) {
              let temp = res.result[i]
              that.treeData.push(temp)
              that.departTree.push(temp)
              that.setThisExpandedKeys(temp)
              that.getAllKeys(temp);
              // console.log(temp.id)
            }
            this.loading = false
          }
        })
      },
      pinyinTran(e){
        let val = e.target.value;
        let pinyin = require('js-pinyin');
        pinyin.setOptions({checkPolyphone: false, charCase: 0});
        let py = pinyin.getCamelChars(val);//获取简码
        this.form.setFieldsValue({py:py});
        let wb = makeWb(val);
        this.form.setFieldsValue({wb:wb});//获取五笔简码
      },
      getCode(e){
        let codeSuffix = e.target.value;
        this.goodsCode = this.form.getFieldValue("subCode")+codeSuffix;
        this.form.setFieldsValue({code:this.goodsCode});
      },
      setThisExpandedKeys(node) {
        if (node.children && node.children.length > 0) {
          this.iExpandedKeys.push(node.key)
          for (let a = 0; a < node.children.length; a++) {
            this.setThisExpandedKeys(node.children[a])
          }
        }
      },
      refresh() {
        this.loading = true
        this.loadTree()
      },
      // 右键操作方法
      // rightHandle(node) {
      //   this.dropTrigger = 'contextmenu'
      //   console.log(node.node.eventKey)
      //   this.rightClickSelectedKey = node.node.eventKey
      // },
      onExpand(expandedKeys) {
        console.log('onExpand', expandedKeys)
        // if not set autoExpandParent to false, if children expanded, parent can not collapse.
        // or, you can remove all expanded children keys.
        this.iExpandedKeys = expandedKeys
        this.autoExpandParent = false
      },
      backFlowList() {
        this.$router.back(-1)
      },
      // 右键点击下拉框改变事件
      dropStatus(visible) {
        if (visible == false) {
          this.dropTrigger = ''
        }
      },
      // 右键店家下拉关闭下拉框
      closeDrop() {
        this.dropTrigger = ''
      },
      addRootNode() {
        this.$refs.nodeModal.add(this.currFlowId, '')
      },
      batchDel: function () {
        console.log(this.checkedKeys)
        if (this.checkedKeys.length <= 0) {
          this.$message.warning('请勾选一条记录！')
        } else {
          var ids = ''
          for (var a = 0; a < this.checkedKeys.length; a++) {
            ids += this.checkedKeys[a] + ','
          }
          var that = this
          this.$confirm({
            title: '确认删除',
            content: '确定要删除所选中的 ' + this.checkedKeys.length + ' 条数据，以及子节点数据吗?',
            onOk: function () {
              deleteAction(that.url.deleteBatch, {ids: ids}).then((res) => {
                if (res.success) {
                  that.$message.success(res.message)
                  that.loadTree()
                  that.onClearSelected()
                } else {
                  that.$message.warning(res.message)
                }
              })
            }
          })
        }
      },
      onSearch(value) {
        let that = this
        if (value) {
          searchByKeywords({keyWord: value}).then((res) => {
            if (res.success) {
              that.departTree = []
              for (let i = 0; i < res.result.length; i++) {
                let temp = res.result[i]
                that.departTree.push(temp)
              }
            } else {
              that.$message.warning(res.message)
            }
          })
        } else {
          that.loadTree()
        }

      },
      nodeModalOk() {
        this.loadTree()
      },
      nodeModalClose() {
      },
      hide() {
        console.log(111)
        this.visible = false
      },
      onCheck(checkedKeys, info) {
        console.log('onCheck', checkedKeys, info)
        this.hiding = false;
        //this.checkedKeys = checkedKeys.checked
        // <!---- author:os_chengtgen -- date:20190827 --  for:切换父子勾选模式 =======------>
        if(this.checkStrictly){
          this.checkedKeys = checkedKeys.checked;
        }else{
          this.checkedKeys = checkedKeys;
        }
        // <!---- author:os_chengtgen -- date:20190827 --  for:切换父子勾选模式 =======------>

        if(info.checkedNodes.length > 0){
          let bool = true;
          info.checkedNodes.forEach((item) => {
            let orgType = item.data.props.orgType;
            if(orgType != "huoqu" && orgType != "huowei"){
              bool = false;
            }
          })
          this.showDelete = bool;
        }else{
          this.showDelete = false;
        }
      },
      onSelect(selectedKeys, e) {
        console.log('selected', selectedKeys, e);
        this.hiding = false;
        let record = e.node.dataRef; //获取选中部门信息
        this.subCode = record.orgCode;
        record.subCode = this.subCode;
        record.departId = record.id;
        console.log('onSelect-record', record);
        this.currSelected = Object.assign({}, record);
        // this.model = this.currSelected;
        this.selectedKeys = [record.key];
        // this.model.departParentId = record.parentId;
        if(record.hasOwnProperty("orgType") && (this.currSelected.orgType == "huoqu" || this.currSelected.orgType == "huowei")){
          this.showGoodsCard = true;
          this.showEdit = true; //显示修改按钮
          this.showSubmit = false;//不显示保存按钮
          this.title1 = "修改";

          let params = { id: record.id }
          // this.loading = true;
          getAction(this.url.queryById, params).then(res => {
            let goodsData = res.result || [];
            goodsData.departName = record.departName;
            this.model = Object.assign({}, goodsData);
            this.subCode = this.model.subCode;
            this.currSelected.departId = this.model.departId;
            typeof success === 'function' ? success(res) : ''
            this.$nextTick(() => {
              this.setValuesToForm(this.model)
            })

            if(this.currSelected.orgType == "huoqu"){
              this.$nextTick(() => {
                this.goodsParentId = this.model.id;
                this.parentCode = this.model.code;
              })
            }else{
              this.goodsParentId = "";
              // this.subCode = this.model.subCode;
            }
          }).finally(() => {
            // this.loading = false
          })

          if(this.currSelected.orgType == "huoqu"){
            this.showHuoqu = false;
            this.showHuowei = true;
            this.title2 = "货区";
          }else{
            this.showHuoqu = false;
            this.showHuowei = false;
            this.title2 = "货位";
          }
        }else if(record.hasOwnProperty("orgType") && this.currSelected.orgType == "2"){
          this.showHuoqu = true;
          this.showHuowei = false;

          this.showGoodsCard = false;
          this.showEdit = false; //不显示修改按钮
          this.showSubmit = false;//不显示保存按钮
        }else {
          this.showHuoqu = false;
          this.showHuowei = false;

          this.showGoodsCard = false;
          this.showEdit = false; //不显示修改按钮
          this.showSubmit = false;//不显示保存按钮
        }
      },
      // 触发onSelect事件时,为部门树右侧的form表单赋值
      setValuesToForm(record) {
        this.form.setFieldsValue(pick(record,'departId', 'departName','name','code','areaType','address','area','volume','contacts','contactsPhone','subCode','codeSuffix','subNum','state','py','wb','zdy','remarks'))
      },
      getCurrSelectedTitle() {
        return !this.currSelected.title ? '' : this.currSelected.title
      },
      onClearSelected() {
        this.hiding = true
        this.showGoodsCard = false;
        this.checkedKeys = []
        this.currSelected = {}
        this.form.resetFields()
        this.selectedKeys = []
      },
      handleNodeTypeChange(val) {
        this.currSelected.nodeType = val
      },
      notifyTriggerTypeChange(value) {
        this.currSelected.notifyTriggerType = value
      },
      receiptTriggerTypeChange(value) {
        this.currSelected.receiptTriggerType = value
      },
      editCurrForm() {
        this.form.validateFields((err, values) => {
          if (!err) {
            if (!this.currSelected.id) {
              this.$message.warning('请点击选择要修改部门!')
              return
            }

            let formData = Object.assign(this.currSelected, values)
            console.log('Received values of form: ', formData)
            httpAction(this.url.edit, formData, 'put').then((res) => {
              if (res.success) {
                this.$message.success('保存成功!')
                this.loadTree()
              } else {
                this.$message.error(res.message)
              }
            })
          }
        })
      },
      submitCurrForm() {
        this.form.validateFields((err, values) => {
          if (!err) {
            let formData = Object.assign(this.model, values)
            formData.id = "";
            formData.parentId = this.goodsParentId;
            console.log('Received values of form: ', formData)
            httpAction(this.url.add, formData, 'post').then((res) => {
              if (res.success) {
                this.$message.success('保存成功!');
                this.showGoodsCard = false;
                this.loadTree();
              } else {
                this.$message.error(res.message)
              }
            })
          }
        })
      },
      emptyCurrForm() {
        this.form.resetFields()
      },
      cancelCurrForm() {
        this.form.resetFields()
        this.showGoodsCard = false;
      },
      nodeSettingFormSubmit() {
        this.form.validateFields((err, values) => {
          if (!err) {
            console.log('Received values of form: ', values)
          }
        })
      },
      // openSelect() {
      //   this.$refs.sysDirectiveModal.show()
      // },
      //新增按钮
      handleAdd(num) {
        this.title1 = "新增";

        if(this.currSelected.hasOwnProperty("orgType") &&  this.currSelected.orgType == "2"){//选中科室 新增货区
          this.emptyCurrForm();
          this.setValuesToForm(this.currSelected);
          this.showGoodsCard = true;//显示货区货位新增页面
          this.showEdit = false; //不显示修改按钮
          this.showSubmit = true;//显示保存按钮
          this.form.setFieldsValue({areaType:"1"});
          this.form.setFieldsValue({state:"1"}); //默认启用
          this.title2 = "货区";
        }else if(this.currSelected.hasOwnProperty("orgType") &&  this.currSelected.orgType == "huoqu"){ //选中货区 新增货位
          this.emptyCurrForm();
          this.setValuesToForm(this.currSelected);
          this.showGoodsCard = true;//显示货区货位新增页面
          this.showEdit = false; //不显示修改按钮
          this.showSubmit = true;//显示保存按钮
          this.form.setFieldsValue({areaType:"2"});
          this.form.setFieldsValue({state:"1"}); //默认启用
          this.form.setFieldsValue({subCode:this.parentCode});
          this.title2 = "货位";
        }else if(this.currSelected.hasOwnProperty("orgType") && (this.currSelected.orgType == "1" || this.currSelected.orgType == "huowei")){
          this.$message.warning('请先选中科室或货区!')
          this.showGoodsCard = false;
          this.showEdit = false;
          this.showSubmit = false;
        }
      },
      handleDelete() {
        // deleteByDepartId({id: this.rightClickSelectedKey}).then((resp) => {
        //   if (resp.success) {
        //     this.$message.success('删除成功!')
        //     this.loadTree()
        //   } else {
        //     this.$message.warning('删除失败!')
        //   }
        // })
      },
      selectDirectiveOk(record) {
        console.log('选中指令数据', record)
        this.nodeSettingForm.setFieldsValue({directiveCode: record.directiveCode})
        this.currSelected.sysCode = record.sysCode
      },
      getFlowGraphData(node) {
        this.graphDatasource.nodes.push({
          id: node.id,
          text: node.flowNodeName
        })
        if (node.children.length > 0) {
          for (let a = 0; a < node.children.length; a++) {
            let temp = node.children[a]
            this.graphDatasource.edges.push({
              source: node.id,
              target: temp.id
            })
            this.getFlowGraphData(temp)
          }
        }
      },

      validateCode(rule, value, callback){
        let params = {
          tableName: 'pd_goods_allocation',
          fieldName: 'code',
          fieldVal: this.goodsCode,
          dataId: ""
        };
        duplicateCheckHasDelFlag(params).then((res) => {
          if (res.success) {
            callback();
          } else {
            callback("货位编号已存在，请重新输入编号后缀!");
          }
        })
      },
      // <!---- author:os_chengtgen -- date:20190827 --  for:切换父子勾选模式 =======------>
      expandAll () {
        this.iExpandedKeys = this.allTreeKeys
      },
      closeAll () {
        this.iExpandedKeys = []
      },
      checkALL () {
        this.checkStriccheckStrictlytly = false
        this.checkedKeys = this.allTreeKeys
      },
      cancelCheckALL () {
        //this.checkedKeys = this.defaultCheckedKeys
        this.checkedKeys = []
      },
      // switchCheckStrictly (v) {
      //   if(v==1){
      //     this.checkStrictly = false
      //   }else if(v==2){
      //     this.checkStrictly = true
      //   }
      // },
      getAllKeys(node) {
        // console.log('node',node);
        this.allTreeKeys.push(node.key)
        if (node.children && node.children.length > 0) {
          for (let a = 0; a < node.children.length; a++) {
            this.getAllKeys(node.children[a])
          }
        }
      }
      // <!---- author:os_chengtgen -- date:20190827 --  for:切换父子勾选模式 =======------>

    },
    created() {
      this.currFlowId = this.$route.params.id
      this.currFlowName = this.$route.params.name
      // this.loadTree()
    },

  }
</script>
<style scoped>
  .ant-card-body .table-operator {
    margin: 15px;
  }

  .anty-form-btn {
    width: 100%;
    text-align: center;
  }

  .anty-form-btn button {
    margin: 0 5px;
  }

  .anty-node-layout .ant-layout-header {
    padding-right: 0
  }

  .header {
    padding: 0 8px;
  }

  .header button {
    margin: 0 3px
  }

  .ant-modal-cust-warp {
    height: 100%
  }

  .ant-modal-cust-warp .ant-modal-body {
    height: calc(100% - 110px) !important;
    overflow-y: auto
  }

  .ant-modal-cust-warp .ant-modal-content {
    height: 90% !important;
    overflow-y: hidden
  }

  #app .desktop {
    height: auto !important;
  }

  /** Button按钮间距 */
  .ant-btn {
    margin-left: 3px
  }

  .drawer-bootom-button {
    /*position: absolute;*/
    bottom: 0;
    width: 100%;
    border-top: 1px solid #e8e8e8;
    padding: 10px 16px;
    text-align: left;
    left: 0;
    background: #fff;
    border-radius: 0 0 2px 2px;
  }
</style>