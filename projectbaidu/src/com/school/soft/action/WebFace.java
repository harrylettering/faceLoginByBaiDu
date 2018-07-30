package com.school.soft.action;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSONObject;

public class WebFace {

	/**
	 * 获取权限token
	 * 
	 * @return 返回示例： { "access_token":
	 *         "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567",
	 *         "expires_in": 2592000 }
	 */
	public static String getAuth() {
		// 官网获取的 API Key 更新为你注册的
		String clientId = "2O47iLcXVgjAvhl0LctAO0M7";
		// 官网获取的 Secret Key 更新为你注册的
		String clientSecret = "rOVboXkm3g2Mdmp1ImXpxS2fVXlEsd6x";
		return getAuth(clientId, clientSecret);
	}

	/**
	 * 获取API访问token 该token有一定的有效期，需要自行管理，当失效时需重新获取.
	 * 
	 * @param ak
	 *            - 百度云官网获取的 API Key
	 * @param sk
	 *            - 百度云官网获取的 Securet Key
	 * @return assess_token 示例：
	 *         "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567"
	 */
	public static String getAuth(String ak, String sk) {// 获取access_token 有效期30天
		// 获取token地址
		String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
		String getAccessTokenUrl = authHost
				// 1. grant_type为固定参数
				+ "grant_type=client_credentials"
				// 2. 官网获取的 API Key
				+ "&client_id=" + ak
				// 3. 官网获取的 Secret Key
				+ "&client_secret=" + sk;
		try {
			URL realUrl = new URL(getAccessTokenUrl);
			// 打开和URL之间的连接
			HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet()) {
				System.err.println(key + "--->" + map.get(key));
			}
			// 定义 BufferedReader输入流来读取URL的响应
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String result = "";
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			/**
			 * 返回结果示例
			 */
			System.err.println("result:" + result);
			String access_token = JSONObject.parseObject(result).getString("access_token");
			// JSONObject jsonObject = new JSONObject(result);
			// String access_token = jsonObject.getString("access_token");
			return access_token;
		} catch (Exception e) {
			System.err.printf("获取token失败！");
			e.printStackTrace(System.err);
		}
		return null;
	}

//	{
//		  "face_token": "2fa64a88a9d5118916f9a303782a97d3",
//		  "location": {
//		      "left": 117,
//		      "top": 131,
//		      "width": 172,
//		      "height": 170,
//		      "rotation": 4
//		  }
//		}
	public static String insert(String img, String group_id, String user_id, String access_token) {// 注册
		// 请求url
		String url = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/user/add";
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("image", img);
			map.put("group_id", group_id);
			map.put("user_id", user_id);
			map.put("liveness_control", "NORMAL");
			map.put("image_type", "BASE64");
			map.put("quality_control", "NORMAL");
			// String param = GsonUtils.toJson(map);
			String param = JSONObject.toJSONString(map);
			// String
			// param=JSONObject.parseObject(map.toString()).toJSONString();
			// 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间，
			// 客户端可自行缓存，过期后重新获取。
			// String access_token = "[调用鉴权接口获取的token]";

			String result = HttpUtil.post(url, access_token, "application/json", param);
			System.out.println(result);
			// String
			// face_token=JSONObject.parseObject(result).getString("face_token");

			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// {
	// "face_token": "fid",
	// "user_list": [
	// {
	// "group_id" : "test1",
	// "user_id": "u333333",
	// "user_info": "Test User",
	// "score": 99.3
	// }
	// ]
	// }
	public static String searchFace(String img, String groups, String access_token) {// 人脸搜索groups总的元素用逗号分开的。
		// 请求url
		String url = "https://aip.baidubce.com/rest/2.0/face/v3/search";
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("image", img);
			map.put("liveness_control", "NORMAL");
			map.put("group_id_list", groups);// 从指定的group中进行查找 用逗号分隔，上限20个
			map.put("image_type", "BASE64");
			map.put("quality_control", "NORMAL");
			// String param = GsonUtils.toJson(map);
			String param = JSONObject.toJSONString(map);
			// 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间，
			// 客户端可自行缓存，过期后重新获取。
			// String access_token = "[调用鉴权接口获取的token]";
			// access_token = (String) httpSession.getAttribute("access_token");
			String result = HttpUtil.post(url, access_token, "application/json", param);
			System.out.println(result);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
//	{
//		  "face_token": "2fa64a88a9d5118916f9a303782a97d3",
//		  "location": {
//		      "left": 117,
//		      "top": 131,
//		      "width": 172,
//		      "height": 170,
//		      "rotation": 4
//		  }
//		}
	public static String update(String img, String group_id, String user_id, String access_token) {// 更新用户
		// 请求url
		String url = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/user/update";
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("image", img);
			map.put("group_id", group_id);
			map.put("user_id", user_id);
			map.put("liveness_control", "NORMAL");
			map.put("image_type", "BASE64");
			map.put("quality_control", "LOW");
			// String param = GsonUtils.toJson(map);
			String param = JSONObject.toJSONString(map);
			// 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间，
			// 客户端可自行缓存，过期后重新获取。
			// String access_token = "[调用鉴权接口获取的token]";
			// access_token=(String)httpSession.getAttribute("access_token");
			String result = HttpUtil.post(url, access_token, "application/json", param);
			System.out.println(result);

			// JSONObject jsonObject = new JSONObject(result);
			// String face_token=jsonObject.getString("face_token");
			// System.out.println(face_token);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 删除用户的某一张人脸，如果该用户只有一张人脸图片，则同时删除用户。
	// 删除成功
	// {
	// "error_code": 0,
	// "log_id": 73473737,
	// }
	// // 删除发生错误
	// {
	// "error_code": 223106,
	// "log_id": 1382953199,
	// "error_msg": "face is not exist"
	// }
	public static String deleteUserFace(String group_id, String user_id, String access_token,String face_token) {// 删除
		// 请求url
		String url = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/face/delete";
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("group_id", group_id);
			map.put("user_id", user_id);
			map.put("face_token", face_token);
			// String param = GsonUtils.toJson(map);
			String param = JSONObject.toJSONString(map);

			// 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间，
			// 客户端可自行缓存，过期后重新获取。
			// String access_token = "[调用鉴权接口获取的token]";
			// access_token=(String)httpSession.getAttribute("access_token");
			String result = HttpUtil.post(url, access_token, "application/json", param);
			System.out.println(result);

			// JSONObject jsonObject = new JSONObject(result);
			// String face_token=jsonObject.getString("face_token");
			// System.out.println(face_token);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 获取人脸库中某个用户的信息(user_info信息和用户所属的组)。
	// {
	// "user_list": [
	// {
	// "user_info": "user info ...",
	// "group_id": "gid1"
	// },
	// {
	// "user_info": "user info2 ...",
	// "group_id": "gid2"
	// }
	// ]
	// }
	public static String get(String user_id, String group_id, String access_token) {// 用户信息查询
		// 请求url
		String url = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/user/get";
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("user_id", user_id);
			map.put("group_id", group_id);

			// String param = GsonUtils.toJson(map);
			String param = JSONObject.toJSONString(map);

			String result = HttpUtil.post(url, access_token, "application/json", param);
			System.out.println(result);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 用于获取一个用户的全部人脸列表。
	// {
	// "face_list": [
	// {
	// "face_token": "fid1",
	// "ctime": "2018-01-01 00:00:00"
	// },
	// {
	// "face_token": "fid2",
	// "ctime": "2018-01-01 10:00:00"
	// }
	// ]
	// }
	public static String getList(String user_id, String group_id, String access_token) {// 用于获取一个用户的全部人脸列表。
		// 请求url
		String url = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/face/getlist";
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("user_id", user_id);
			map.put("group_id", group_id);

			// String param = GsonUtils.toJson(map);
			String param = JSONObject.toJSONString(map);

			// 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间，
			// 客户端可自行缓存，过期后重新获取。
			// String accessToken = "[调用鉴权接口获取的token]";

			String result = HttpUtil.post(url, access_token, "application/json", param);
			System.out.println(result);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 用于查询指定用户组中的用户列表。GET请求
	// {
	// "user_id_list": [
	// "uid1",
	// "uid2"
	// ]
	// }
	public static String getUsers(String group_id, String access_token) {
		// 请求url
		String url = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/group/getusers";
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("group_id", group_id);

			// String param = GsonUtils.toJson(map);
			String param = JSONObject.toJSONString(map);

			// 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间，
			// 客户端可自行缓存，过期后重新获取。
			// String accessToken = "[调用鉴权接口获取的token]";

			String result = HttpUtil.get(url, access_token, "application/json", param);
			System.out.println(result);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 用于将已经存在于人脸库中的用户复制到一个新的组。
	// 正确返回值
	// {
	// "error_code": 0,
	// "log_id": 3314921889,
	// }
	// // 发生错误时返回值
	// {
	// "error_code": 223111,
	// "log_id": 3111284097,
	// "error_msg": "dst group is not exist"
	// }
	public static String copyUserToOtherGroup(String user_id, String sc_group_id, String dst_group_id,
			String access_token) {
		// 请求url
		String url = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/user/copy";
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("user_id", user_id);
			map.put("sc_group_id", sc_group_id);
			map.put("dst_group_id", dst_group_id);
			// String param = GsonUtils.toJson(map);
			// String param=JSONObject.toJSONString(map);
			String param = JSONObject.toJSONString(map);
			// 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间，
			// 客户端可自行缓存，过期后重新获取。
			// String accessToken = "[调用鉴权接口获取的token]";

			String result = HttpUtil.get(url, access_token, "application/json", param);
			System.out.println(result);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 用于将用户从某个组中删除。
	// // 正确返回值
	// {
	//
	// "error_code": 0,
	// "log_id": 3314921889,
	// }
	// // 发生错误时返回值
	// {
	// "error_code": 223103,
	// "log_id": 815967402,
	// "error_msg": "user is not exist"
	// }
	public static String deleteuser(String group_id, String user_id, String access_token) {
		// 请求url
		String url = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/user/delete";
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("group_id", group_id);
			map.put("user_id", user_id);
			// String param = GsonUtils.toJson(map);
			String param = JSONObject.toJSONString(map);

			// 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间，
			// 客户端可自行缓存，过期后重新获取。
			// String access_token = "[调用鉴权接口获取的token]";
			// access_token = (String) httpSession.getAttribute("access_token");
			String result = HttpUtil.post(url, access_token, "application/json", param);
			System.out.println(result);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 用于创建一个空的用户组，如果用户组已存在 则返回错误。
	// // 正确返回值
	// {
	//
	// "error_code": 0,
	// "log_id": 3314921889,
	// }
	// // 发生错误时返回值
	// {
	// "error_code": 223101,
	// "log_id": 815967402,
	// "error_msg": " group is already exist"
	// }
	public static String groupAdd(String group_id, String access_token) {// 增加用户组
		// 请求url
		String url = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/group/add";
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("group_id", group_id);

			// String param = GsonUtils.toJson(map);
			String param = JSONObject.toJSONString(map);

			// 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间，
			// 客户端可自行缓存，过期后重新获取。
			// String access_token = "[调用鉴权接口获取的token]";
			// access_token = (String) httpSession.getAttribute("access_token");
			String result = HttpUtil.post(url, access_token, "application/json", param);
			System.out.println(result);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 删除用户组下所有的用户及人脸，如果组不存在 则返回错误。
	// // 正确返回值
	// {
	//
	// "error_code":0,
	// "log_id": 3314921889,
	// }
	// // 发生错误时返回值
	// {
	// "error_code": 223100,
	// "log_id": 815967402,
	// "error_msg": " group is not exist"
	// }
	public static String groupDelete(String group_id, String access_token) {
		// 请求url
		String url = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/group/delete";
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("group_id", group_id);
			// String param = GsonUtils.toJson(map);
			String param = JSONObject.toJSONString(map);
			// 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间，
			// 客户端可自行缓存，过期后重新获取。
			// String accessToken = "[调用鉴权接口获取的token]";

			String result = HttpUtil.post(url, access_token, "application/json", param);
			System.out.println(result);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 用于查询用户组的列表。
	// {
	// "group_id_list": [
	// "gid1",
	// "gid2"
	// ]
	// }
	public static String GroupGetlist(String access_token, Integer start, Integer length) {// 组列表查询
		// 请求url
		String url = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/group/getlist";
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("start", start);
			map.put("length", length);

			// String param = GsonUtils.toJson(map);
			String param = JSONObject.toJSONString(map);

			// 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间，
			// 客户端可自行缓存，过期后重新获取。
			// String access_token = "[调用鉴权接口获取的token]";
			// access_token = (String) httpSession.getAttribute("access_token");
			// System.out.println(access_token);
			String result = HttpUtil.post(url, access_token, "application/json", param);
			System.out.println(result);

			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
