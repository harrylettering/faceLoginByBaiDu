package com.school.soft.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Controller
public class LoginAction {

	@RequestMapping(value = "/insertFace", method = RequestMethod.POST)
	@ResponseBody
	public String insert(@RequestBody Map<String, Object> map, HttpServletRequest request) throws Exception {//。／／／／／／／／／／／先要人脸检测，我没有检测
		String result = null;
		String userPhone=null;
		//System.out.println("map.size: "+ map.size());
		HttpSession httpSession=request.getSession();
		Logger logger=LoggerFactory.getLogger(LoginAction.class);
		String access_token="24.38cf113a8e743e56c976cab83d58207f.2592000.1535554542.282335-11219291";////////////////////你的token
		String img=null;
		if(map.containsKey("img"))
		{
			img=(String) map.get("img");
		}
		userPhone=(String) httpSession.getAttribute("userPhone");
		logger.info("img: {}",img.getBytes().length);
		logger.info("userPhone:{}",userPhone);
	     if(img!=null&&userPhone!=null)
	     {
	    	 String group_id=userPhone.substring(0,4);
			String user_id=userPhone;
	    	 result=WebFace.getList(user_id, group_id,access_token);
	    	 JSONObject fresult=JSONObject.parseObject(result).getJSONObject("result");
	    	 if(fresult==null)
	    	 {
	    		 result = WebFace.insert(img, group_id, user_id, access_token);
	 			System.out.println(result);
	 			JSONObject rresult=JSONObject.parseObject(result).getJSONObject("result");
	 			if(rresult!=null)
	 			{
	 				String face_token = rresult.getString("face_token");
	 				System.out.println(face_token);
	 				if (face_token != null) {
	 					//httpSession.setAttribute("face_token", face_token);
	 					return "OK";
	 				}
	 			}
	 			else
	 			{
	 				return JSONObject.parseObject(result).getString("error_msg");
	 			}
	    	 }
	    	 else
	    	 {
	    		 result=WebFace.update(img, userPhone.substring(0, 4), userPhone, access_token);
	    		 System.out.println(result);
		 			JSONObject rresult=JSONObject.parseObject(result).getJSONObject("result");
		 			if(rresult!=null)
		 			{
		 				String face_token = rresult.getString("face_token");
		 				System.out.println(face_token);
		 				if (face_token != null) {
		 					//httpSession.setAttribute("face_token", face_token);
		 					return "OK";
		 				}
		 			}
		 			else
		 			{
		 				return JSONObject.parseObject(result).getString("error_msg");
		 			}
	    		
	    	 }
	     }
		else {
			request.setCharacterEncoding("utf8");
			return "出现未知错误请重写添加照片";
		}
		return "error";
	}

	@ResponseBody
	@RequestMapping(value = "/faceLogin", method = RequestMethod.POST)
	public String faceLogin(@RequestParam("img") String img, HttpSession httpSession) throws Exception {
		String groupsResult = null;
		String result = null;
		String face_token = null;
		//String face_token_source = (String) httpSession.getAttribute("face_token");
		String access_token="24.38cf113a8e743e56c976cab83d58207f.2592000.1535554542.282335-11219291";/////////////待定
		Logger logger=LoggerFactory.getLogger(LoginAction.class);
		if(img!=null)
		{
			groupsResult = WebFace.GroupGetlist(access_token, 0, 100);
			String groups=JSONObject.parseObject(groupsResult).getJSONObject("result").getString("group_id_list");
			//String [] groupss=groups.substring(1, groups.length()-1).split(",");
			String groupsss=groups.substring(1, groups.length()-1);
			groupsss=groupsss.replace("\"", "");
			int index = 0;
			logger.info("groupsss:{}",groupsss);
			result=WebFace.searchFace(img, groupsss, access_token);
			JSONObject rresult=JSONObject.parseObject(result).getJSONObject("result");
			if(rresult!=null)
			{
				face_token = rresult.getString("face_token");
				JSONArray jsonArray2 = JSONObject.parseObject(result).getJSONObject("result").getJSONArray("user_list");
				System.out.println("jsonArray2:  "+jsonArray2);
				double xiangsidu = jsonArray2.getJSONObject(0).getDouble("score");
				String userPhone=jsonArray2.getJSONObject(0).getString("user_id");
				if (xiangsidu >= 90.0) {
					httpSession.setAttribute("userPhone",userPhone);
					return "OK";
				}
			}
			else
			{
				return JSONObject.parseObject(result).getString("error_msg");
			}
			
		}
		return "遇见未知错误请重写拍照";
	}
	
	@ResponseBody
	@RequestMapping(value = "/getAuth", method = RequestMethod.POST)
	public String getAuth(@RequestParam(value="phone",required=false) String phone,HttpSession httpSession)
	{
		String access_token=null;
		// 官网获取的 API Key 更新为你注册的
		String clientId = "2O47iLcXVgjAvhl0LctAO0M7";
		// 官网获取的 Secret Key 更新为你注册的
		String clientSecret = "rOVboXkm3g2Mdmp1ImXpxS2fVXlEsd6x";
		access_token=WebFace.getAuth(clientId,clientSecret);
		System.out.println(access_token);
		if(access_token!=null)
		{
			httpSession.setAttribute("access_token", access_token);
			return "OK";
		}
		return "error";
		
	}
	
	@ResponseBody
	@RequestMapping()
	public String delete(@RequestParam(value="",required=false) String userPhone,String img)
	{
		return null;
	}
	

}
