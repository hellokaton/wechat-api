package com.precious.controller.admin;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import com.blade.Blade;
import com.blade.web.http.Request;
import com.blade.web.http.Response;
import com.blade.web.multipart.FileItem;
import com.precious.Const;

import blade.kit.DateKit;
import blade.kit.FileKit;
import blade.kit.StringKit;
import blade.kit.json.JsonObject;

public class UploadController {
	
	public void upload_img(Request request, Response response){
		FileItem[] fileItems = request.files();
		JsonObject res = new JsonObject();
		if(null != fileItems && fileItems.length > 0){
			FileItem fileItem = fileItems[0];
			long length = fileItem.getContentLength();
			String name = fileItem.getFileName();
			String url = "";
			String save_path = "";
			File file = fileItem.getFile();
			
			// 新文件路径
			String dir = Const.UPLOAD_DIR + "/" + DateKit.dateFormat(new Date(), "yyyy/MM/dd");
			
			String fileName = StringKit.getRandomChar(8) + "_" + 
					StringKit.getRandomNumber(4) + "." + FileKit.getExtension(name);
			
			save_path = dir + "/" + fileName;
			
			String newPath = Blade.me().webRoot() + "/" + save_path;
			
			FileKit.createDir(Blade.me().webRoot() + "/" + dir);
			
			try {
				FileKit.copy(file.getPath(), newPath);
				String proto = request.isSecure() ? "https://" : "http://";
				url =  proto + request.host() + ":" + request.port() + "/" + request.contextPath() + save_path;
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			res.add("length", length);
			res.add("save_path", save_path);
			res.add("name", fileName);
			res.add("url", url);
		}
		response.json(res.toString());
	}
	
}
