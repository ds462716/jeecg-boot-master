export function photoCheck(oFile,that){
  var rFilter = /^(image\/bmp|image\/gif|image\/jpeg|image\/png|image\/tiff)$/i;
  if (oFile && !rFilter.test(oFile.type)) {
    that.$message.error('请上传文件非图片格式');
    return false;
  }
  if (oFile && oFile.size > 5242880) {
    that.$message.error('上传图片不能超过5M');
    return false;
  }
  return true;
}