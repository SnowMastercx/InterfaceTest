package httpclientUtil;
import config.BaceConfig;

import java.io.IOException;
import java.net.InetAddress;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

public class TestMethod {
	public static String get(String url, int num, String[] name, String[] value) throws ClientProtocolException, IOException{
		String temp = "";
		//创建HttpClient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		//构造GET请求参数
		for (int i = 0; i < num; i++) {
			temp += name[i] + "=" + value[i] + "&";
		}
		String urlTemp = temp.substring(0, temp.length()-1);
		url += "?" + urlTemp;
		
		System.out.println("-----------------------URL-----------------------");
		System.out.println(url);
		
		//创建HttpGet对象
		HttpGet httpget = new HttpGet(url);
		
		//设置请求和传输超时时间
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
		httpget.setConfig(requestConfig);
		
		//执行GET请求
		CloseableHttpResponse response = httpClient.execute(httpget);
		
		//打印响应信息
		String rps = printResponse(response);
		
		//释放连接
		response.close();
		httpClient.close();
		
		return rps;
	}
	
	public void post(String url, int num, String[] name, String[] value) throws ClientProtocolException, IOException {
		//创建HttpClient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		//创建HttpPost对象
		HttpPost httppost = new HttpPost(url);
		
		//设置请求和传输超时时间
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
		httppost.setConfig(requestConfig);		
		
		//构造请求参数
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		for (int i = 0; i < num; i++) {
//			System.out.println(name[i] + " " + value[i]);
			formparams.add(new BasicNameValuePair(name[i], value[i]));
		}
		
		//处理编码格式
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
		
		//设置POST请求参数
		httppost.setEntity(entity);
		
		//执行POST请求
		CloseableHttpResponse response =  httpClient.execute(httppost);
		
		//打印响应信息
		printResponse(response);
		
		//释放连接
		response.close();
		httpClient.close();
		
	} 
	
	//打印服务器响应信息
	public static String printResponse(HttpResponse response) throws ParseException, IOException{
		//获取文件头信息
		Header headers[] = response.getAllHeaders();
		System.out.println("-----------------------Header-----------------------");
		for (Header header : headers) {
			System.out.println(header.getName() + ": " + header.getValue());
		}
		
		//获取服务器返回状态码
		System.out.println("-----------------------Status Code-----------------------");
		if (200 == response.getStatusLine().getStatusCode()) {
			System.out.println("Status Code: " + response.getStatusLine().getStatusCode());
		}
		else {
			System.out.println("Status Code Error!");
		}
		
		//获取服务器返回实体
		System.out.println("-----------------------HttpEntity-----------------------");
		String josnStr = EntityUtils.toString(response.getEntity());
		System.out.println(josnStr + "\n");
		
		return josnStr;
	}
	
	public static void main(String[] args) throws ClientProtocolException, IOException {
		String[] name = {"phone", "password", "type"};
		String[] value = {"13580475555", "123456", "1"};
		String url = BaceConfig.baceUrl + "/account/login";
		
		System.out.println(get(url, name.length, name, value));
//		post(url, name.length, name, value);
		
	}
}
