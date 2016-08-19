package com.biz.rest;




import play.Logger;


	import java.io.BufferedReader;
	import java.io.InputStreamReader;
	import java.io.OutputStream;
	import java.net.URL;
	import java.security.SecureRandom;
	import java.security.cert.CertificateException;
	import java.security.cert.X509Certificate;

	import javax.net.ssl.HostnameVerifier;
	import javax.net.ssl.HttpsURLConnection;
	import javax.net.ssl.KeyManager;
	import javax.net.ssl.SSLContext;
	import javax.net.ssl.SSLSession;
	import javax.net.ssl.TrustManager;
	import javax.net.ssl.X509TrustManager;

	import org.codehaus.jettison.json.JSONException;
	import org.codehaus.jettison.json.JSONObject;

	

	public class RestCall extends Thread{
		private static org.slf4j.Logger logger = Logger.underlying();
		
		String documentList;
		
		public RestCall(String documentList) {
			
			this.documentList = documentList;
		}

		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			restcallTostagMail(documentList);
		}
		
		public  void restcallTostagMail(String documentList) {
			// TODO Auto-generated method stub
			logger.info("lead {} : "+documentList);

			try{
				
				//	URL url = new URL("https://todoist.com/API/v6/login?email=assistant@visdom.ca&password=Visdom1234");
					//URL url = new URL("http:localhost:9000/doclist");
				//SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();

				 // configure the SSLContext with a TrustManager
				  SSLContext ctx;
					
					ctx = SSLContext.getInstance("TLS");
				
		        ctx.init(new KeyManager[0], new TrustManager[] {(TrustManager) new DefaultTrustManager()}, new SecureRandom());
		        SSLContext.setDefault(ctx);

		        URL url = new URL("https://dev-stage.visdom.ca/lead");
		        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		        conn.setHostnameVerifier(new HostnameVerifier() {
		            @Override
		            public boolean verify(String arg0, SSLSession arg1) {
		                return true;
		            }

					
		        });
		        
		        conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/json");
				
				
			

				String input=documentList.toString();
			OutputStream os = conn.getOutputStream();
					os.write(input.getBytes());
					os.flush();
					if (conn.getResponseCode() != 200) {
						throw new RuntimeException("Failed : HTTP error code : "
							+ conn.getResponseCode());
					}else{
			 
					BufferedReader br = new BufferedReader(new InputStreamReader(
							(conn.getInputStream())));
			 
					
					System.out.println("Output from Server .... \n");
		                           String output;
					while ((output = br.readLine()) != null) {
						System.out.println(output);
				
						
						
						
					}
		                            
					}
		        System.out.println(conn.getResponseCode());
		        conn.disconnect();
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}


		

		   private static class DefaultTrustManager implements X509TrustManager {

			       

					@Override
					public void checkClientTrusted(X509Certificate[] chain, String authType)
							throws CertificateException {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void checkServerTrusted(X509Certificate[] chain, String authType)
							throws CertificateException {
						// TODO Auto-generated method stub
						
					}

					@Override
					public X509Certificate[] getAcceptedIssuers() {
						// TODO Auto-generated method stub
						return null;
					}
			    }
		
			   public static void main(String[] args) throws JSONException {
				   JSONObject jsonTableData=new JSONObject();
					jsonTableData.put("id", "Applicant_4255");
					//restCallDocumentListCreation(jsonTableData.toString());

			}
	}




