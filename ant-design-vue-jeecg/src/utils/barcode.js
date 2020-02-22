let url ="/pd/pdProduct/scanCode";
let stockUrl ="/pd/pdProduct/stockScanCode";
import { httpAction } from '@/api/manage'

/**
 * 入库扫码，扫码前校验该产品是否关联编码规则，如果关联编码规则用后台返回值，如果没有则继续使用
 * @param Barcode1
 * @param Barcode2
 * @param that
 * @returns {Promise<void>}
 */
export async function scanCode(Barcode1, Barcode2,that){
  //封装查询参数
  let formData = new URLSearchParams();
  formData.append("Barcode1",Barcode1);
  formData.append("Barcode2",Barcode2);
  let result = {};
  result.code = "500";
  let res = await httpAction(url,formData,"post");
  if(res.code==200){
    result = res.result;
  }else{
   //系统报错
    that.$message.error(res.message );
  }
  console.log(result);
  return result;
}

/**
 *
 * @param Barcode1 产品编号
 * @param Barcode2 二级条码
 * @param that
 * @returns {Promise<*>}
 */
/*export async function stockScanCode(Barcode1, Barcode2,that){
  let result = {};
  result.code = "500";
  let productBarCode;
  //产品条码修正
  if(Barcode1.indexOf(Barcode2) != -1){
    productBarCode = Barcode1;
  }else{
    productBarCode = Barcode1 + Barcode2;
  }
  //封装查询参数
  let formData = new URLSearchParams();
  formData.append("productBarCode",productBarCode);
  let res = await httpAction(stockUrl,formData,"post");
  if(res.success){
    let json = res.result;
    let stocks = json["stocks"];
    //判断如果查询到库存
    if(stocks){
      result.code = "200";
      result.stocks = stocks;
    }
    result.stocks = stocks;
  }else{
    //系统报错
    that.$message.error(res.message);
  }
  return result;
}*/

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




