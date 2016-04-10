package inputFileManage;
import httpclientUtil.*;
import junit.framework.Assert;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.awt.List;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.annotation.JsonFormat.Value;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import config.BaceConfig;

public class Login {
	//全局配置
	private static String filePath = "D:/开发项目/eclipse/workspace/InterfaceTest/inputFile/login.json";
	private static String url = BaceConfig.baceUrl + "/account/login";
	private static int num = 3;
	private static String[] name = {"phone","password","type"};
	
	//json的参数
	private String phone;
	private String password;
	private int type;

	private AssertMethod assertMethod;
	private MysqlMethod mysqlMethod;
		
	//Assert
	public static class AssertMethod {
		private int ret;

		public int getRet() {
			return ret;
		}

		public void setRet(int ret) {
			this.ret = ret;
		}
	}
	
	//Mysql
	public static class MysqlMethod {
		private String uid;

		public String getUid() {
			return uid;
		}

		public void setUid(String uid) {
			this.uid = uid;
		}
	}
	
	//json参数的getters/setters方法
	public String getPhone() {
		return phone;
	}

	public String getPassword() {
		return password;
	}

	public int getType() {
		return type;
	}

	public AssertMethod getAssertMethod() {
		return assertMethod;
	}

	public MysqlMethod getMysqlMethod() {
		return mysqlMethod;
	}
	
	//全局配置的getters/setters方法
	public String getFilePath() {
		return filePath;
	}

	public String getUrl() {
		return url;
	}

	public int getNum() {
		return num;
	}

	public String[] getName() {
		return name;
	}
	
	/**
	 * 使用json-lib解析json
	 * @param args
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
/*	
 * json-lib的效率低下，改用jackson进行json的解析
 * 
	public static void main(String[] args) throws ClientProtocolException, IOException{
		JSONArray readJson = new JsonUtil().readJsonlib(name, num, filePath);
		for (int i = 0; i < readJson.size(); i++) {
			ArrayList<String> temp = new ArrayList<String>();
			JSONObject jsonObject = readJson.getJSONObject(i);
			for (int j = 0; j < num; j++) {
				temp.add(jsonObject.getString(name[j]));
			}
			String[] value = temp.toArray(new String[0]);
			new TestMethod().get(url, num, name, value);
		}
	}
*/
	
	/**
	 * 使用jackson解析json文件
	 * @param args
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException, SQLException{
		ObjectMapper mapper = new ObjectMapper();
		
		Login[] logins = mapper.readValue(new File(filePath), Login[].class);
		
		for (Login login : logins) {
//			System.out.println("phone: " + login.getPhone());
//			System.out.println("password: " + login.getPassword());
//			System.out.println("type: " + login.getType());
//			System.out.println("Assert: ret = " + login.getAssertMethod().getRet());
//			System.out.println("Mysql:" + login.getMysqlMethod().getUid());
//			System.out.println();
			
			String[] value = {login.getPhone(),login.getPassword(),Integer.toString(login.getType())};
			String test = new TestMethod().get(url, name.length, name, value);
			
			Responses rpse = new Responses().responses(test);
			
			if (rpse.getRet() == login.getAssertMethod().getRet()) {
				System.out.println("Assert Success!");
			}
			else {
				System.out.println("Assert Error!");
			}
			if (new config.MysqlMethod().MysqlMethod(login.getMysqlMethod().getUid(), rpse.getDatas().getUid())) {
				System.out.println("Mysql Success!");
			}
			else {
				System.out.println("Mysql Error!");
			}
		}
	}

	//解析响应信息
	public static class Responses {
		private int ret;
		private String msg;
		public Datas getDatas() {
			return datas;
		}

		private Datas datas;
		
		public int getRet() {
			return ret;
		}

		public String getMsg() {
			return msg;
		}
		
		public static class Datas{
			@JsonProperty(value = "PHPSESSID")
			private String phpsessid;
			
			private String uid;
			private String type;
			private String password;
			public String getUid() {
				return uid;
			}
			public void setUid(String uid) {
				this.uid = uid;
			}
			public String getType() {
				return type;
			}
			public void setType(String type) {
				this.type = type;
			}
			public String getPassword() {
				return password;
			}
			public void setPassword(String password) {
				this.password = password;
			}
			
			@JsonProperty(value = "PHPSESSID")
			public String getPhpsessid() {
				return phpsessid;
			}
		}
		
		public Responses responses(String rps) throws JsonParseException, JsonMappingException, IOException{
			ObjectMapper mapper_rp = new ObjectMapper();
			
			Responses tem = mapper_rp.readValue(rps, Responses.class);
			
			return tem;
		}
	}
}
