package httpclientUtil;
import inputFileManage.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonUtil {
	/**
	 * 读取文件
	 * @param Path json文件的路径
	 * @return
	 */
	public static String ReadFile(String Path){
		BufferedReader reader = null;
		String laststr = "";
		try{
			FileInputStream fileInputStream = new FileInputStream(Path);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
			reader = new BufferedReader(inputStreamReader);
			String tempString = null;
			while((tempString = reader.readLine()) != null){
			laststr += tempString;
		}
		reader.close();
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if(reader != null){
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			}
		}
		return laststr;
	}	
	
	/**
	 * 使用json-lib解析json
	 * @param name json的参数
	 * @param num 参数个数
	 * @param filePath json文件路径
	 * @return
	 */
	public JSONArray readJsonlib(String[] name, int num, String filePath) {
		ArrayList<String> temp = new ArrayList<String>();
//		String filePath = new Main().getFilePath();
		String JsonContext = ReadFile(filePath);
		JSONArray jsonArray = JSONArray.fromObject(JsonContext);
		int jsonSize = jsonArray.size();
		System.out.println("jsonSize: " + jsonSize);
		
		return jsonArray;
	}
	

}
