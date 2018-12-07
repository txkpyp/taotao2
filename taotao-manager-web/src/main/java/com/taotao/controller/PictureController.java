package com.taotao.controller;

import com.taotao.util.JsonUtils;
import com.taotao.util.FastDFSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Peng on 2018/11/8.
 */
@Controller
public class PictureController {

    @Value("${IMAGE_SERVER_URL}")
    private String IMAGE_SERVER_URL;


    /**
     * @ResponseBody
     *    默认的content-type:application/json;charset=utf-8  google浏览器是支持
     * 	//使用火狐浏览器 使用kindeditor的时候不支持 content-type:application/json;charset=utf-8
     *   //解决：设置content-type:text/plain;charset=utf-8  都支持
     * @param uploadFile
     * @return
     */

    //上传图片的方法
//	@RequestMapping("/pic/upload")
//	@ResponseBody
//	public Map<String, Object> uploadImage(MultipartFile uploadFile){
//		try {
//			// 1.获取元文件的扩展名
//			String originalFilename = uploadFile.getOriginalFilename();
//			String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
//			// 2.获取文件的字节数组
//			byte[] bytes = uploadFile.getBytes();
//			// 3.通过fastdfsclient的方法上传图片（参数要求有 字节数组 和扩展名 不包含"."）
//			FastDFSClient client = new FastDFSClient("classpath:resource/fastdfs.conf");
//			// 返回值：group1/M00/00/00/wKgZhVk4vDqAaJ9jAA1rIuRd3Es177.jpg
//			String string = client.uploadFile(bytes, extName);
//			//拼接成完整的URL
//			String path = "http://192.168.25.133/"+string;
//			// 4.成功时，设置map
//			Map<String, Object> map = new HashMap<>();
//			map.put("error", 0);
//			map.put("url", path);
//			// 6.返回map
//			return map;
//		} catch (Exception e) {
//			// 5.失败时，设置map
//			Map<String, Object> map = new HashMap<>();
//			map.put("error", 1);
//			map.put("message", "上传失败");
//			return map;
//		}
//	}

    //produces:就可以设置content-type

    @RequestMapping(value="/pic/upload",produces= MediaType.TEXT_PLAIN_VALUE+";charset=utf-8")
    @ResponseBody
    //就需要将字符串转成jONS 格式的字符串就可以了
    public String uploadImage(MultipartFile uploadFile){
        try {
            // 1.获取元文件的扩展名
            String originalFilename = uploadFile.getOriginalFilename();
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            // 2.获取文件的字节数组
            byte[] bytes = uploadFile.getBytes();
            // 3.通过fastdfsclient的方法上传图片（参数要求有 字节数组 和扩展名 不包含"."）
            FastDFSClient client = new FastDFSClient("classpath:resource/client.conf");
            // 返回值：group1/M00/00/00/wKgZhVk4vDqAaJ9jAA1rIuRd3Es177.jpg
            String string = client.uploadFile(bytes, extName);
            //拼接成完整的URL
            //"http://192.168.37.161/"
            String path = IMAGE_SERVER_URL+string;
            // 4.成功时，设置map
            Map<String, Object> map = new HashMap<>();
            map.put("error", 0);
            map.put("url", path);
            // 6.返回map
            return JsonUtils.objectToJson(map);
        } catch (Exception e) {
            // 5.失败时，设置map
            Map<String, Object> map = new HashMap<>();
            map.put("error", 1);
            map.put("message", "上传失败");
            return JsonUtils.objectToJson(map);
        }
    }
}
