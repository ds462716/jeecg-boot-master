var upn;//编号
var _Lot;//批次
var _ExpDate;//有效期
var _secondCode;//二级条码
let url ="/pd/pdProduct/scanCode"

import { httpAction } from '@/api/manage'


//扫码取产品编号
export function getPrdNumber(Barcode,that){
  if ((Barcode.length >= 24) && Barcode.substr(0, 2) == "01") {
    Barcode = Barcode.substr(0, 16);
  }
  if ((Barcode.length >= 24) && Barcode.substr(0, 2) == "00") {
    Barcode = Barcode.substr(0, 20);
  }
  if(Barcode.indexOf("+") == 0){
    upn = trimStr(getHIBCUPN(Barcode).toUpperCase());
  }else{
    upn = GetEANUPN(Barcode);
    upn = trimStr(upn.toUpperCase());
  }
  return upn;
}


//扫码
export async function scanCode(Barcode1, Barcode2,that){
  var barcodeObj = {};
  //扫码前校验该产品是否关联编码规则，如果关联编码规则用后台返回值，如果没有则继续使用
  //codeFlag true 有规则  false没有;
  let codeFlag = true;
  //截取字符串01后面14位，根据编码查询是否关联编码规则
  let resultUpn = true;
  //封装查询参数
  let formData = new URLSearchParams();
  formData.append("Barcode1",Barcode1);
  formData.append("Barcode2",Barcode2);
  let res = await httpAction(url,formData,"post");
    if(res.success){
      if(res.code==200){
        let json = res.result;
        let flag = false;
        if(json.hasOwnProperty("01")&&!json.hasOwnProperty("00")&&!json.hasOwnProperty("#")){
          flag = true;
          upn = json["01"];
        }else if(json.hasOwnProperty("00")&&!json.hasOwnProperty("01")&&!json.hasOwnProperty("#")){
          flag = true;
          upn = json["00"];
        }else if(json.hasOwnProperty("#")&&!json.hasOwnProperty("00")&&!json.hasOwnProperty("01")){
          flag = true;
          upn = json["#"];
        }else{
          upn = "";
          that.$message.error("没有解析出产品编号"  );
        }
        //赋值编号
        barcodeObj.upn = upn;
        if(json.hasOwnProperty("10")&&!json.hasOwnProperty("21")){
          _Lot = json["10"];
        }else if(!json.hasOwnProperty("10")&&json.hasOwnProperty("21")){
          _Lot = json["21"];
        }else if(json.hasOwnProperty("10")&&json.hasOwnProperty("21")){
          let key1 =  json["10"];
          let key2 =  json["21"];
          _Lot = key1+key2;
        }else{
          _Lot = "";
          //that.$message.error("没有解析出批号"  );
        }
        //赋值批号
        barcodeObj._Lot = _Lot;
        if(json.hasOwnProperty("17")){
          _ExpDate = json["17"];
          _ExpDate = "20" + _ExpDate.substr(0, 2) + "-" + _ExpDate.substr(2, 2) + "-" + _ExpDate.substr(4, 2);  //   20YY-MM-DD
        }else{
          _ExpDate = "";
          //that.$message.error("没有解析有效期" );
        }
        //赋值有效期
        barcodeObj._ExpDate = _ExpDate;
        _secondCode = json["secondCode"];
        //赋值条码
        barcodeObj._secondCode = _secondCode;
        if(!flag){
          resultUpn = false;
        }
      }else if(res.code==201){
        //没有规则
        codeFlag = false;
      }else{
        //后台错误
        resultUpn = false;
        that.$message.error(res.message );
      }
    }else{
      that.$message.error(res.message );
    }
  //如果有规则 在返回该处标识
  if(codeFlag){
    return barcodeObj;
  }else{
    //没有绑定扫码规则的 用现有的规则
    if(Barcode1.indexOf("+") == 0 && Barcode2.indexOf("+") == 0){
      //HIBC扫码
      let rt = HIBCBarcodeDec(Barcode1, Barcode2,that);
      return rt;
      //if(!rt){
      //	that.$message.error('条形码格式错误',{icon:0});
      //	return false;
      //}
    }else {
      //EAN扫码
      let rt = EANBarcodeDec(Barcode1, Barcode2, that);
      return rt;
      //if(!rt){
      //	that.$message.error('条形码格式错误',{icon:0});
      //	return false;
      //}
    }
  }

}

export function HIBCBarcodeDec(Barcode1, Barcode2,that){
  upn = "";
  _Lot = "";
  _ExpDate = "";
  _secondCode = "";
  if (Barcode1 != "" && Barcode2 != ""){
    if ((Barcode1 == Barcode2) && (Barcode1.length >= 24) && Barcode1.substr(0, 2) == "01") {
      Barcode2 = Barcode1.substr(16, Barcode2.length - 16);
      Barcode1 = Barcode1.substr(0, 16);
    }
    if (Barcode1.indexOf("+$$") >= 0 && Barcode2.indexOf("+$$") >= 0){
      Barcode1 = "";
      Barcode2 = "";
      that.$message.error("扫了2次二级条形码!" );
      return false;
    }
    if (Barcode1.indexOf("+") >= 0 && (Barcode1 + Barcode2).indexOf("+") < 0){
      Barcode1 = "";
      Barcode2 = "";
      that.$message.error("扫描错误,请重扫!" );
      return false;
    }
    if (Barcode1 == Barcode2){
      Barcode1 = "";
      Barcode2 = "";
      that.$message.error("同一条形码被扫了2次!" );
      return false;
    }else{
      if (Barcode1.indexOf("$$") >= 0 || Barcode2.substr(0, 2) == "01") {
        Barcode1 = "";
        Barcode2 = "";
        that.$message.error("Scan Err. Retry!\nMust Scan UPN Barcode First!" );
        return false;
      }
    }

    _secondCode = Barcode2;
    //获取upn
    upn = trimStr(getHIBCUPN(Barcode1).toUpperCase());
    //alert(upn);

    GetHIBCLot(Barcode2, false,that);

    let _E = _ExpDate;
    if (_E.length == 7) {  //2009-11
      try {
        let theDate = _E + "-01";
        let difDays = dateDiff(theDate);
        if (difDays < 32) {
          if (difDays < 0) {
            that.$message.error("扫描成功.\n但请注意该产品的有效期!" );
          }else {
            //SunSound s = new SunSound("\\windows\\type.wav");
            //s.Play();
          }
        }
      }catch(err){
        //SunSound s = new SunSound("\\windows\\type.wav");
        //s.Play();
      }
    }
    if (_E.length == 10){ //2009-11-11
      try{
        let difDays = dateDiff(_E);
        if (difDays < 32) {
          if (difDays < 0) {
            that.$message.error("扫描成功.\n但请注意该产品的有效期!" );
          } else {
            //SunSound s = new SunSound("\\windows\\type.wav");
            //s.Play();
          }
        }
      }catch(err) {

      }
    }
    _Lot = trimStr(_Lot.toUpperCase());
  } else {
    return false;
  }
  //alert("lot:" + _Lot + "  exp:" + _ExpDate);
  return true;
}

export function GetHIBCLot(LotBarcode, IsUserProduceDate,that){
  _ExpDate = "";
  _Lot = "";
  if (LotBarcode.substr(0, 1) != "+") {
    if (LotBarcode.substr(0, 2) == "17") {
      _ExpDate = "20" + LotBarcode.substr(2, 2) + "-" + LotBarcode.substr(4, 2) + "-" + LotBarcode.substr(6, 2);
    } else {
      _ExpDate = "";
    }
    if (LotBarcode.length > 10) {
      let shortlot = "";
      if (LotBarcode.substr(8, 2) == "10") {
        //01 234567 8901234
        //17 100427 10 106066
        shortlot = LotBarcode.substr(8, LotBarcode.length - 8);
        //_Lot = App.GetMidStr(shortlot + ((char)29).ToString(), "10", ((char)29).ToString());
        _Lot = shortlot.substr(2);
      }
      if (LotBarcode.substr(8, 2) == "21") {
        //01 234567 8901234
        //17 100427 10 106066
        shortlot = LotBarcode.substr(8, LotBarcode.length - 8);
        //_Lot = App.GetMidStr(shortlot + ((char)29).ToString(), "21", ((char)29).ToString());
        _Lot = shortlot.substr(2);
      }
    } else {
      _Lot = "";
    }
    return true;
  } else {
    try {
      //#region 从二级条码中获取 _Lot，_ExpDate
      //读取标志位: Qty Format Char
      let _QtyFormatChar = "";
      if (LotBarcode.substr(0, 3) == "+$$") {
        _QtyFormatChar = LotBarcode.substr(3, 1);
        switch (_QtyFormatChar) {
          case "0":
            _ExpDate = "20" + LotBarcode.substr(5, 2) + "-" + LotBarcode.substr(3, 2);  //   20YY-MM
            _Lot = LotBarcode.substr(7, LotBarcode.length - 9);
            break;
          case "1":
            _ExpDate = "20" + LotBarcode.substr(3, 2) + "-" + LotBarcode.substr(5, 2);  //   20YY-MM
            _Lot = LotBarcode.substr(7, LotBarcode.length - 9);
            break;
          case "2":
            _ExpDate = "20" + LotBarcode.substr(8, 2) + "-" + LotBarcode.substr(4, 2) + "-" + LotBarcode.substr(6, 2);  //   20YY-MM-DD
            _Lot = LotBarcode.substr(10, LotBarcode.length - 12);
            break;
          case "3":
            _ExpDate = "20" + LotBarcode.substr(4, 2) + "-" + LotBarcode.substr(6, 2) + "-" + LotBarcode.substr(8, 2);  //   20YY-MM-DD
            _Lot = LotBarcode.substr(10, LotBarcode.length - 12);
            break;
          case "4":
            _ExpDate = "20" + LotBarcode.substr(4, 2) + "-" + LotBarcode.substr(6, 2) + "-" + LotBarcode.substr(8, 2) + " " + LotBarcode.substr(10, 2) + ":00";  //   20YY-MM-DD HH:00
            _Lot = LotBarcode.substr(12, LotBarcode.length - 14);
            break;
          case "5":
            _ExpDate = "20" + LotBarcode.substr(4, 2) + " Julian day:" + LotBarcode.substr(6, 2);  //   20YY Julian day:JJJ
            _Lot = LotBarcode.substr(9, LotBarcode.length - 11);
            break;
          case "6":
            _ExpDate = "20" + LotBarcode.substr(4, 2) + " Julian day:" + LotBarcode.substr(6, 3) + " " + LotBarcode.substr(9, 2) + ":00";  //   20YY Julian day:JJJ HH:00
            _Lot = LotBarcode.substr(11, LotBarcode.length - 13);
            break;
          case "7":
            _ExpDate = "";
            // _ExpDate = App.InputBox("请依据外包装的内容，输入有效期（格式为：YYYY-MM-DD HH:MM:SS）");
            _Lot = LotBarcode.substr(4, LotBarcode.length - 6);
            that.$message.error("不符合预定的HIBC格式" );
            return false;
          // break;
          case "8":
            //#region  依据第6位确定Exp Date 的格式
            let _ExpDateFlag = LotBarcode.substr(6, 1);
            switch (_ExpDateFlag){
              case "0":
                _ExpDate = "20" + LotBarcode.substr(8, 2) + "-" + LotBarcode.substr(6, 2);  //   20YY-MM
                _Lot = LotBarcode.substr(10, LotBarcode.length - 12);
                break;
              case "1":
                _ExpDate = "20" + LotBarcode.substr(8, 2) + "-" + LotBarcode.substr(6, 2);  //   20YY-MM
                _Lot = LotBarcode.substr(10, LotBarcode.length - 12);
                break;
              case "2":
                _ExpDate = "20" + LotBarcode.substr(11, 2) + "-" + LotBarcode.substr(7, 2) + "-" + LotBarcode.substr(9, 2);  //   20YY-MM-DD
                _Lot = LotBarcode.substr(13, LotBarcode.length - 15);
                break;
              case "3":
                _ExpDate = "20" + LotBarcode.substr(7, 2) + "-" + LotBarcode.substr(9, 2) + "-" + LotBarcode.substr(11, 2);  //   20YY-MM-DD
                _Lot = LotBarcode.substr(13, LotBarcode.length - 15);
                break;
              case "4":
                _ExpDate = "20" + LotBarcode.substr(7, 2) + "-" + LotBarcode.substr(9, 2) + "-" + LotBarcode.substr(11, 2) + " " + LotBarcode.substr(13, 2) + ":00";  //   20YY-MM-DD HH:00
                _Lot = LotBarcode.substr(15, LotBarcode.length - 17);
                break;
              case "5":
                _ExpDate = "20" + LotBarcode.substr(7, 2) + " Julian day:" + LotBarcode.substr(9, 3);  //   20YY Julian day:JJJ
                _Lot = LotBarcode.substr(12, LotBarcode.length - 14);
                break;
              case "6":
                _ExpDate = "20" + LotBarcode.substr(7, 2) + " Julian day:" + LotBarcode.substr(9, 3) + " " + LotBarcode.substr(12, 2) + ":00";  //   20YY Julian day:JJJ HH:00
                _Lot = LotBarcode.substr(14, LotBarcode.length - 16);
                break;
              case "7":
                if (IsUserProduceDate) {
                  _ExpDate = "";// App.InputBox("请依据外包装的内容，输入产品的生产日期（格式与英文标签一致!）");
                } else {
                  _ExpDate = "";// App.InputBox("请依据外包装的内容，输入产品的有效期（格式与英文标签一致!）");
                }
                _Lot = LotBarcode.substr(7, LotBarcode.length - 9);
                break;
              default:
                that.$message.error("该二级条形码不符合HIBC规范！\n +$$ 8 QQ (0-7) Must 格式错误。" );
                return false;
              //break;
            }
            //#endregion
            break;
          case "9":
            //#region  依据第9位确定Exp Date 的格式
            _ExpDateFlag = LotBarcode.substr(9, 1);
            switch (_ExpDateFlag) {
              case "0":
                _ExpDate = "20" + LotBarcode.substr(11, 2) + "-" + LotBarcode.substr(9, 2);  //   20YY-MM
                _Lot = LotBarcode.substr(13, LotBarcode.length - 15);
                break;
              case "1":
                _ExpDate = "20" + LotBarcode.substr(11, 2) + "-" + LotBarcode.substr(9, 2);  //   20YY-MM
                _Lot = LotBarcode.substr(13, LotBarcode.length - 15);
                break;
              case "2":
                _ExpDate = "20" + LotBarcode.substr(14, 2) + "-" + LotBarcode.substr(10, 2) + "-" + LotBarcode.substr(12, 2);  //   20YY-MM-DD
                _Lot = LotBarcode.substr(16, LotBarcode.length - 18);
                break;
              case "3":
                _ExpDate = "20" + LotBarcode.substr(10, 2) + "-" + LotBarcode.substr(12, 2) + "-" + LotBarcode.substr(14, 2);  //   20YY-MM-DD
                _Lot = LotBarcode.substr(16, LotBarcode.length - 18);
                break;
              case "4":
                _ExpDate = "20" + LotBarcode.substr(10, 2) + "-" + LotBarcode.substr(12, 2) + "-" + LotBarcode.substr(14, 2) + " " + LotBarcode.substr(16, 2) + ":00";  //   20YY-MM-DD HH:00
                _Lot = LotBarcode.substr(18, LotBarcode.length - 20);
                break;
              case "5":
                _ExpDate = "20" + LotBarcode.substr(10, 2) + " Julian day:" + LotBarcode.substr(12, 3);  //   20YY Julian day:JJJ
                _Lot = LotBarcode.substr(15, LotBarcode.length - 17);
                break;
              case "6":
                _ExpDate = "20" + LotBarcode.substr(10, 2) + " Julian day:" + LotBarcode.substr(12, 3) + " " + LotBarcode.substr(15, 2) + ":00";  //   20YY Julian day:JJJ HH:00
                _Lot = LotBarcode.substr(17, LotBarcode.length - 19);
                break;
              case "7":
                _ExpDate = "";
                //_ExpDate = App.InputBox("请依据外包装的内容，输入有效期（格式为：YYYY-MM-DD HH:MM:SS）");
                _Lot = LotBarcode.substr(10, LotBarcode.length - 12);
                break;
              default:
                that.$message.error("该二级条形码不符合HIBC规范！\n +$$ 9 QQQQQ (0-7) Must 格式错误。" );
                return false;
              //break;
            }
            //#endregion
            break;
        }
      } else if (LotBarcode.substr(0, 2) == "+$") {
        _Lot = LotBarcode.substr(2, LotBarcode.length - 4);
        _ExpDate = "";
      } else if (LotBarcode.substr(0, 1) == "+") {
        _Lot = LotBarcode.substr(5, LotBarcode.length - 8);
        _ExpDate = "20" + LotBarcode.substr(1, 2) + "-01-01";
        //_ExpDate = DateTime.Parse(_ExpDate).AddDays(int.Parse(LotBarcode.substr(3, 3))).ToString("yyyy-MM-dd");
        //Msg("二级条形码日期形式为Julian Day :" + _ExpDate + "");
        return true;
      } else {
        if (tiped < 2) {
          that.$message.error("二级条形码不可识别!" );
        }
        tiped++;
        return false;
      }
      //2008-01"
      if (_ExpDate.length == 7) _ExpDate = _ExpDate + "-01";
      return true;
      //#endregion
    }
    catch (err) {
      that.$message.error("二级条形码不可识别!"+ err.message );
      return false;
    }
  }
}

export function getHIBCUPN(HIBCUPNBarcode){
  let UPN = "";
  if (HIBCUPNBarcode.substr(0, 2) == "01"){
    UPN = HIBCUPNBarcode.substr(2, HIBCUPNBarcode.length - 2);
    //if (UPN.length > 2) {
    //    if (UPN.Substring(0, 1) == "0") UPN = UPN.Substring(1, UPN.length - 1);
    //}
    //if (UPN.length > 2) {
    //    if (UPN.Substring(0, 1) == "0") UPN = UPN.Substring(1, UPN.length - 1);
    //}
  }else{
    UPN = HIBCUPNBarcode.substr(1, HIBCUPNBarcode.length - 2);
  }
  return UPN;
}

export function EANBarcodeDec(Barcode1, Barcode2,that) {
  upn = "";
  _Lot = "";
  _ExpDate = "";
  _secondCode = "";
  if (Barcode1 != "" && Barcode2 != ""){
    if ((Barcode1 == Barcode2) && (Barcode1.length >= 24) && Barcode1.substr(0, 2) == "01") {
      Barcode2 = Barcode1.substr(16, Barcode2.length - 16);
      Barcode1 = Barcode1.substr(0, 16);
    }
    if (Barcode1 == Barcode2) {
      that.$message.error("不是规范的条形码!" );
      // return false;
    } else {
      if (Barcode1.indexOf("$$") >= 0 || Barcode2.substr(0, 2) == "01") {
        Barcode1 = "";
        Barcode2 = "";
        that.$message.error("必须首先扫描UPN条码!" );
        return false;
      }
    }

    _secondCode = Barcode2;
    if (Barcode1 == Barcode2){
      upn = Barcode1;
    } else {
      upn = GetEANUPN(Barcode1);
      upn = trimStr(upn.toUpperCase());
      GetEANLot(Barcode2);
    }
    let _E = _ExpDate;
    if (_E.length == 7) { //2009-11
      try {
        let theDate = _E + "-01";
        let difDays = dateDiff(theDate);
        if (difDays < 32){
          if (difDays < 0) {
            that.$message.error("扫描成功.\n但请注意该产品的有效期!" );
          } else {
            //SunSound s = new SunSound("\\windows\\type.wav");
            //s.Play();
          }
        }
      } catch(err) {
        //SunSound s = new SunSound("\\windows\\type.wav");
        //s.Play();
      }
    }
    if (_E.length == 10){  //2009-11-11
      try {
        let theDate = _E;
        let difDays = dateDiff(theDate);
        if (difDays < 32){
          if (difDays < 0){
            that.$message.error("扫描成功.\n但请注意该产品的有效期!" );
          } else {
            //SunSound s = new SunSound("\\windows\\type.wav");
            //s.Play();
          }
        }
      } catch(err) {

      }
    }
    //_L = _L.ToUpper().Trim();
    _Lot = trimStr(_Lot.toUpperCase());
  } else {
    return false;
  }
  return true;
}

export function GetEANUPN(EANUPNBarcode){
  let UPN = "";
  if (EANUPNBarcode.substr(0, 2) == "01" || EANUPNBarcode.substr(0, 2) == "02") {
    UPN = EANUPNBarcode.substr(2, 14);
    //if (UPN.length > 2)
    //{
    //    if (UPN.substr(0, 1) == "0") UPN = UPN.substr(1, UPN.length - 1);
    //}
    //if (UPN.length > 2)
    //{
    //    if (UPN.substr(0, 1) == "0") UPN = UPN.substr(1, UPN.length - 1);
    //}
  }else if(EANUPNBarcode.substr(0, 2) == "00"){
    UPN = EANUPNBarcode.substr(2, 20);
  } else {
    UPN = EANUPNBarcode;
  }
  return UPN;
}

export function GetEANLot(LotBarcode){
  _ExpDate = ""; _Lot = "";
  if (LotBarcode.substr(0, 1) == "+") {
    return;
  }
  //#region 逐步缩减法
  //#region L1
  //产品号
  if ((LotBarcode.substr(0, 2) == "01") || (LotBarcode.substr(0, 2) == "02")) {
    LotBarcode = LotBarcode.substr(16, LotBarcode.length - 16);
  }
  if ((LotBarcode.length>2) && ((LotBarcode.substr(0, 2) == "00"))) {
    LotBarcode = LotBarcode.substr(20, LotBarcode.length - 20);
  }
  //#endregion
  //#region L2
  //有效期
  if ((LotBarcode.length > 2) && (LotBarcode.substr(0, 2) == "17")) {
    let ri = LotBarcode.substr(6, 2);
    if (ri == "00") ri = "01";
    _ExpDate = "20" + LotBarcode.substr(2, 2) + "-" + LotBarcode.substr(4, 2) + "-" + ri;
    //17 121100 30 01 10 091222
    LotBarcode = LotBarcode.substr(8, LotBarcode.length - 8);
  }

  //生产日期
  if ((LotBarcode.length > 2) && LotBarcode.substr(0, 2) == "11" ) {
    LotBarcode = LotBarcode.substr(8, LotBarcode.length - 8);
  }
  //其他日期
  if ((LotBarcode.length>2) && ((LotBarcode.substr(0, 2) == "13") || (LotBarcode.substr(0, 2) == "15"))) {
    LotBarcode = LotBarcode.substr(8, LotBarcode.length - 8);
  }
  //#endregion
  //#region L3

  ////序列号
  //if ((LotBarcode.length > 2) && ((LotBarcode.substr(0, 2) == "21"))) {
  //    _Lot = LotBarcode.substr(2, LotBarcode.length - 2);
  //    LotBarcode = LotBarcode.substr(2, LotBarcode.length - 2);
  //}
  ////批号
  //if ((LotBarcode.length > 2) && ((LotBarcode.substr(0, 2) == "10"))) {
  //    _Lot = LotBarcode.substr(2, LotBarcode.length - 2);
  //    LotBarcode = LotBarcode.substr(2, LotBarcode.length - 2);
  //}
  //#endregion
  //#endregion
  if ((LotBarcode.length > 3) && (LotBarcode.substr(0, 2) == "23")) {
    _Lot = "";
  }
  if (LotBarcode.indexOf("10") < 0) {
    _Lot = "";
  } else {
    //300110091222
    let x = LotBarcode.indexOf("10");
    _Lot = LotBarcode.substr(x + 2, LotBarcode.length - (x + 2));
    if(_Lot.length < 4) {
      _Lot = "";
    }
  }
  if (_Lot == "" && LotBarcode.indexOf("21") >= 0) {
    let y = LotBarcode.indexOf("21");
    _Lot = LotBarcode.substr(y + 2, LotBarcode.length - (y + 2));
  }
  if (_Lot.length >= 10) {
    let mx = _Lot.lastIndexOf("91");
    if (mx > 0) {
      if ((_Lot.length - mx) == 6) {
        _Lot = _Lot.substr(0, mx);
      }
    }
  }
  //if (LotBarcode.length > 10)
  //{
  //    string shortlot = "";
  //    if (LotBarcode.substr(8, 2) == "10")
  //    {
  //        //01 234567 8901234
  //        //17 100427 10 106066
  //        shortlot = LotBarcode.substr(8, LotBarcode.length - 8);
  //        _Lot = App.GetMidStr(shortlot + ((char)29).ToString(), "10", ((char)29).ToString());
  //    }

  //    if (LotBarcode.substr(8, 2) == "21")
  //    {
  //        //01 234567 8901234
  //        //17 100427 10 106066
  //        shortlot = LotBarcode.substr(8, LotBarcode.length - 8);
  //        _Lot = App.GetMidStr(shortlot + ((char)29).ToString(), "21", ((char)29).ToString());
  //    }
  //}
  //else
  //{
  //    _Lot = "";
  //}
}

//计算传入时间与现在时间天数差
export function dateDiff(sDate){
  let d1 = new Date(sDate.replace(/-/g, "/"));
  let now = new Date();
  let days = d1.getTime() - now.getTime();
  let time = parseInt(days / (1000 * 60 * 60 * 24));
  return time;
}

//字符串去空格
export function trimStr(s){
  return s.replace(/(^\s*)|(\s*$)/g, "");
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