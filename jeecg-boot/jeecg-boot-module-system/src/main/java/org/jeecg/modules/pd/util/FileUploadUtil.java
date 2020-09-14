package org.jeecg.modules.pd.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @Author: zxh
 * @Date: 2020年1月10日17:23:53
 */
@Slf4j
@Component
public class FileUploadUtil {
	private static String uploadPath;

	@Value("${jeecg.path.upload}")
	public void setUploadPath(String uploadPath) {
		FileUploadUtil.uploadPath = uploadPath;
	}
	/**
	 * 判断图片非空
	 * @param imgs
	 * @return
	 */
	public static  boolean isImgEmpty(MultipartFile[] imgs){
		if(imgs.length>0){
			for (int i=0; i<imgs.length; i++) {
				MultipartFile img = imgs[i];
				if(img.isEmpty()){
					return true;
				}
			}
			return false;
		}else{
			return true;
		}
	}

	/**
	 * form文件上传
	 * @return
	 */
	public static String  upload(MultipartFile[] imgs){
		String dbpath="";
		try {
			String ctxPath = uploadPath;
			String fileName = null;
			String bizPath = "files";
			String nowday = new SimpleDateFormat("yyyyMMdd").format(new Date());
			File file = new File(ctxPath + File.separator + bizPath + File.separator + nowday);
			if (!file.exists()) {
				file.mkdirs();// 创建文件根目录
			}
			if(imgs.length>0){
				for (int i=0; i<imgs.length; i++) {
					MultipartFile img = imgs[i];
					String orgName = img.getOriginalFilename();// 获取文件名
					fileName = orgName.substring(0, orgName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + orgName.substring(orgName.indexOf("."));
					String savePath = file.getPath() + File.separator + fileName;
					File savefile = new File(savePath);
					FileCopyUtils.copy(img.getBytes(), savefile);
					dbpath = bizPath + File.separator + nowday + File.separator + fileName;
					if (dbpath.contains("\\")) {
						dbpath = dbpath.replace("\\", "/");
					}
				}
			}else{
				return  "";
			}
		}catch (IllegalStateException e) {
			e.printStackTrace();
			return  "";
		}catch(IOException e){
			log.error(e.getMessage(), e);
			return "";
		}
		return dbpath;
	}

	public static boolean deleteFile(String fileName) {
		String ctxPath = uploadPath;
		fileName = ctxPath+"\\"+fileName;
		File file = new File(fileName);
		if (file.exists() && file.isFile()) {
			if (file.delete()) {
				log.debug("删除文件 " + fileName + " 成功!");
				return true;
			} else {
				log.debug("删除文件 " + fileName + " 失败!");
				return false;
			}
		} else {
			log.debug(fileName + " 文件不存在!");
			return true;
		}
	}
}
