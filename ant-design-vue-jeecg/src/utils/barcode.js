let url ="/pd/pdProduct/scanCode";
let stockUrl ="/pd/pdProduct/stockScanCode";
let openingUrl ="/pd/pdProduct/openingQuotation";
let packageRecordUrl ="/pd/pdPackageRecord/packageRecordScanCode";
import { httpAction } from '@/api/manage'

/**
 * 入库扫码，扫码前校验该产品是否关联编码规则，如果关联编码规则用后台返回值，如果没有则继续使用
 * @param Barcode1
 * @param Barcode2
 * @param that
 * @returns {Promise<void>}
 */
export async function scanCode(Barcode1, Barcode2){
  //封装查询参数
  let formData = new URLSearchParams();
  formData.append("Barcode1",Barcode1);
  formData.append("Barcode2",Barcode2);
  let res = await httpAction(url,formData,"post");
  return res;
}

/**
 * 开瓶扫码
 * @param Barcode
 * @returns {Promise<*>}
 */
export async function openingQuotation(Barcode){
  //封装查询参数
  let formData = new URLSearchParams();
  formData.append("Barcode",Barcode);
  let res = await httpAction(openingUrl,formData,"post");
  return res;
}


/**
 *
 * @param Barcode1 产品编号
 * @param Barcode2 二级条码
 * @param productFlag 产品类型0耗材1试剂
 * @param nestatStatus 1未使用""查未使用和以使用
 * @param that
 * @returns {Promise<*>}
 */
export async function stockScanCode(Barcode1, Barcode2,productFlag,nestatStatus){
  //封装查询参数
  let formData = new URLSearchParams();
  formData.append("Barcode1",Barcode1);
  formData.append("Barcode2",Barcode2);
  formData.append("productFlag",productFlag);
  formData.append("nestatStatus",nestatStatus);
  let res = await httpAction(stockUrl,formData,"post");
  return res;
}

/**
 * 定数包记录扫码
 * @param Barcode1  定数包打包记录条码
 * @returns {Promise<*>}
 */
export async function packageRecordScanCode(Barcode1){
  //封装查询参数
  let formData = new URLSearchParams();
  formData.append("Barcode1",Barcode1);
  let res = await httpAction(packageRecordUrl,formData,"post");
  return res;
}


/**
 * 扫码取产品编号  如果修改  需要同步修改BarCodeUtil的getPrdNumber方法 和HIBCBarcodeDec方法  2020年2月22日11:19:57
 * @param Barcode
 * @returns {*}
 */
export function getPrdNumber(Barcode){
  Barcode = trimStr(Barcode.toUpperCase());
  let number;
  if(Barcode.startsWith("+")){
    let x = getSubCount(Barcode,"+");
    let y = getSubCount(Barcode,"/");
    if(x==1 && y==1 && Barcode.startsWith("+")){
      //如果是 +J123123451/122713C001L5"  一个+一个/
      let i = Barcode.indexOf("/");
      Barcode = Barcode.substring(0, i);
    }else if(x>1 && y==0 && Barcode.startsWith("+")){
      //如果是 +J123123451+122713C001L5"  两个+类型
      Barcode = Barcode.substring(0, Barcode.indexOf("+",Barcode.indexOf("+")+1));
    }else if(x>1 && y==1 && Barcode.endsWith("+")){
      //如果是 +J123123451/122713C001L5+"  两个+一个/
      let i = Barcode.indexOf("/");
      Barcode = Barcode.substring(0, i);
    }else if(x>1 && y==1 && Barcode.endsWith("/")){
      //如果是 +J123123451+122713C001L5"  两个+类型 一个/类型
      Barcode = Barcode.substring(0, Barcode.indexOf("+",Barcode.indexOf("+")+1));
    }else if (x==1 && y>1 && Barcode.endsWith("/")){
      //如果是 +J123123451/122713C001L5/"  一个+两个//
      let i = Barcode.indexOf("/");
      Barcode = Barcode.substring(0, i);
    }
    number = Barcode.substring(1, Barcode.length - 2);
  }else{
    number = GetEANUPN(Barcode);
  }
  return number;
}

/**
 * gs1码解析产品编号
 * @param EANUPNBarcode
 * @return
 */
function  GetEANUPN(EANUPNBarcode){
  let UPN = "";
  if ("01" ==(EANUPNBarcode.substring(0, 2)) || "02"==(EANUPNBarcode.substring(0, 2))) {
    UPN = EANUPNBarcode.substring(2, 16);
  }else if("00"==(EANUPNBarcode.substring(0, 2))){
    UPN = EANUPNBarcode.substring(2, 20);
  } else if("93"==(EANUPNBarcode.substring(0, 2))){
    UPN = EANUPNBarcode.substring(0, 19);
    console.log(UPN)
  }else{
    UPN = EANUPNBarcode.substring(0, 13);
  }
  return UPN;
}

/**
 * 去空格
 * @param s
 * @returns {*}
 */
function trimStr(s){
  return s.replace(/(^\s*)|(\s*$)/g, "");
}

/**
 * 是否包含多个字符串
 * @param str
 * @param key
 * @return
 */
function getSubCount(str,key){
  if(str!=null && ""!=(str) && key !=null && ""!=key){
    var count = 0;
    var index = 0;
    while ((index = str.indexOf(key, index)) != -1) {
      index = index + key.length;
      count++;
    }
    return count;
  }else{
    return 0;
  }
}

/**
 * 生成产品编号
 * @param code
 * @returns {*}
 */
export function generateNumber(code){
  let date = new Date();
  let year = date.getFullYear(); //当前年份
  let month = (date.getMonth() + 1) < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1);//当前月份
  let data = date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate();//天
  let hours = date.getHours(); //小时
  let minute = date.getMinutes(); //分
  let second = date.getSeconds(); //秒
  let milliseconds = date.getMilliseconds(); //毫秒
  code = code + year + month+data+hours+minute+second+milliseconds;
  return code;
}




