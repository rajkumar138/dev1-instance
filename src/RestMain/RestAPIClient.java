
package RestMain;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.ClientProtocolException;
import java.io.IOException;
import java.util.Scanner;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONTokener;

import casePackage.CaseHandling;

import org.json.JSONException;

public class RestAPIClient 
{
	//--------------------------Keep This Standard------------------------------------------------//
	public static final String LOGINURL = "https://login.salesforce.com";
	public static final String GRANTTYPE = "/services/oauth2/token?grant_type=password";
	public static final String ACCESSTOKEN = "access_token";
	public static final String INSTANCEURL = "instance_url";
	
	//--------------------------Dynamic changes needed----------------------------------------------//
	/*
	public static final String CLIENTID = "3MVG9ZL0ppGP5UrA0AN2mfcFAmh0BaxaTp9c7ztNZS6pfnRJZBckoZvQel5r6OXjjGM.I4oI1_eeEuCp3ETYu";
	public static final String CLIENTSECRET = "3462536453017440505";
	public static final String USERID = "rajmca138@dev.com";
	public static final String PASSWORD = "rajkumar138";
	*/
	public static final String CLIENTID = "3MVG9Y6d_Btp4xp7JjBJVV.rtFgjPzs9pAb1dAljgYmn6HCdiNId.klcXfuhRcbiyLDH8WXEqIBHSu7E3u2np";
	public static final String CLIENTSECRET = "3716471580220914117";
	public static final String USERID = "rajmca138@salesforce.com";
	public static final String PASSWORD = "rajkumar12345";
	//----------------------------------------------------------------------------------------------//

	private static String instanceUrl;
	private static Header oAuthHeader;
	private static Header printHeader = new BasicHeader("X-PrettyPrint", "1");
	private static String caseId;

	private static String caseNumber;
	private static String caseSubject;
	private static String caseStatus;
	private static String caseOrigin;
	private static String casePriority;

	public static void main(String[] args) 
	{

		Scanner reader = new Scanner(System.in);
		int Opt;
		
		System.out.println("***********************************************************");
		System.out.println("1.Login");
		System.out.println("2.Get Cases");
		System.out.println("3.Bulk API");
		System.out.println("***********************************************************");		
		System.out.println();
		
		do
		{
			System.out.println("Enter You Option :");
			Opt = reader.nextInt();
					
			
			switch(Opt)
			{
			case 1: System.out.println("Loading...."); Login(); break;
			case 2: getCases(); break;
			case 3: InsertBulkSobject(); break;
			case 4: break;
			}
		}
		while(Opt<4);
		
		/*
		HttpClient httpclient = HttpClientBuilder.create().build();

		String loginURL = LOGINURL + GRANTTYPE + 
				"&client_id=" + CLIENTID + 
				"&client_secret=" + CLIENTSECRET + 
				"&username=" + USERID + 
				"&password=" + PASSWORD;
					
		System.out.println(loginURL);
		HttpPost httpPost = new HttpPost(loginURL);
		HttpResponse httpResponse = null;

		try 
		{
			httpResponse = httpclient.execute(httpPost);
		} 
		catch (ClientProtocolException clientProtocolException) 
		{
			clientProtocolException.printStackTrace();
		} 
		catch (IOException ioException) 
		{			
			ioException.printStackTrace();
		} 
		catch (Exception exception) 
		{
			exception.printStackTrace();
		}


		final int statusCode = httpResponse.getStatusLine().getStatusCode();

		if (statusCode != HttpStatus.SC_OK) 
		{
			System.out.println("Error authenticating to Salesforce.com platform: " + statusCode);
			return;
		}

		String httpMessage = null;
		try
		{
			httpMessage = EntityUtils.toString(httpResponse.getEntity());
		} 
		catch (IOException ioException) 
		{
			ioException.printStackTrace();
		}

		JSONObject jsonObject = null;
		String accessToken = null;

		try 
		{
			jsonObject = (JSONObject) new JSONTokener(httpMessage).nextValue();
			accessToken = jsonObject.getString(ACCESSTOKEN);
			instanceUrl = jsonObject.getString(INSTANCEURL);
		}
		catch (JSONException jsonException) 
		{
			jsonException.printStackTrace();
		}

		oAuthHeader = new BasicHeader("Authorization", "OAuth " + accessToken) ;

		CaseHandling.getCases(oAuthHeader);
		
		getCases();
		//createCase();

		httpPost.releaseConnection();
		*/

	}

	public static void InsertBulkSobject()
	{
		 
		Header printHeader1 = new BasicHeader("operation", "insert");
		Header printHeader2 = new BasicHeader("object", "Account");
		Header printHeader3 = new BasicHeader("contentType", "CSV");
		Header printHeader4 = new BasicHeader("lineEnding", "CRLF");
		try 
		{			
			HttpClient httpClient = HttpClientBuilder.create().build();

			String finalURI = instanceUrl + "/services/data/v41.0/jobs/ingest";
			System.out.println("Query URL: " + finalURI);
			HttpGet httpGet = new HttpGet(finalURI);
			httpGet.addHeader(oAuthHeader);
			httpGet.addHeader(printHeader);
			
			httpGet.addHeader(printHeader1);
			httpGet.addHeader(printHeader2);
			httpGet.addHeader(printHeader3);
			httpGet.addHeader(printHeader4);
			
			HttpResponse httpResponse = httpClient.execute(httpGet);
			
			int statusCode = httpResponse.getStatusLine().getStatusCode();

			if (statusCode == 200) 
			{
				System.out.println(statusCode);
				
				String httpMessage = EntityUtils.toString(httpResponse.getEntity());

				JSONObject jsonObject = null;

				jsonObject = (JSONObject) new JSONTokener(httpMessage).nextValue();				

				JSONArray jobObjectsArray = jsonObject.getJSONArray("records");
				
				JSONObject jobObjects = (JSONObject) jobObjectsArray.get(0);
				
				System.out.println(jobObjects);
				System.out.println(jobObjects.get("id"));
								
				//----------------------------------------------------------------------------------------------------------//
				//String URLtoBulkUpsert = instanceUrl + "/services/data/v41.0/jobs/ingest"+jobObjects.get("id")+"/batches";
				String URLtoBulkUpsert = instanceUrl + "/services/data/v41.0/jobs/ingest"+jobObjects.get("id")+"/batches";
				HttpClient httpClient2 = HttpClientBuilder.create().build();
				
				HttpPut  httpPost = new HttpPut(URLtoBulkUpsert);
				httpPost.addHeader(oAuthHeader);
								
				httpPost.addHeader(new BasicHeader("Content-Type", "text/x-json"));
				httpPost.addHeader(new BasicHeader("Accept", "application/json"));
				
				
				JSONObject AccountNew = new JSONObject();
				AccountNew.put("Name", "Bulk API Rest");
				
				StringEntity entityBody = new StringEntity(AccountNew.toString(1));
				//entityBody.setContentType("application/json");
				httpPost.setEntity(entityBody);
				
				HttpResponse httpResponse2 = httpClient2.execute(httpPost);				
				
				System.out.println(httpResponse2.getStatusLine().getStatusCode());
				
				//----------------------------------------------------------------------------------------------------------//
			}
			else
			{
				
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	public static void Logout()
	{
		
	}
	
	public static void Login()
	{
		HttpClient httpclient = HttpClientBuilder.create().build();
		
		String loginURL = LOGINURL + GRANTTYPE + "&client_id=" + CLIENTID + "&client_secret=" + CLIENTSECRET + "&username=" + USERID + "&password=" + PASSWORD;
		
		HttpPost httpPost = new HttpPost(loginURL);
		HttpResponse httpResponse = null;
		
		try 
		{
			httpResponse = httpclient.execute(httpPost);
		} 
		catch (Exception e){}
		
		final int statusCode = httpResponse.getStatusLine().getStatusCode();

		if (statusCode != HttpStatus.SC_OK) 
		{
			System.out.println("Error authenticating to Salesforce.com platform: " + statusCode);
			return;
		}

		String httpMessage = null;
		try
		{
			httpMessage = EntityUtils.toString(httpResponse.getEntity());
		} 
		catch (IOException ioException) 
		{
			ioException.printStackTrace();
		}

		JSONObject jsonObject = null;
		String accessToken = null;

		try 
		{
			jsonObject = (JSONObject) new JSONTokener(httpMessage).nextValue();
			accessToken = jsonObject.getString(ACCESSTOKEN);
			instanceUrl = jsonObject.getString(INSTANCEURL);
		}
		catch (JSONException jsonException) 
		{
			jsonException.printStackTrace();
		}

		oAuthHeader = new BasicHeader("Authorization", "OAuth " + accessToken) ;
		
		System.out.println("*****"+oAuthHeader);
					
	}
	
	public static void getCases() 
	{

		System.out.println("****************Case QUERY**************");

		try 
		{			
			HttpClient httpClient = HttpClientBuilder.create().build();

			String finalURI = instanceUrl + "/services/data/v38.0/query?q=Select+Id+,+CaseNumber+,+Subject+,+Status+,+Origin+,+Priority+From+Case+Limit+10";
			System.out.println("Query URL: " + finalURI);
			HttpGet httpGet = new HttpGet(finalURI);
			httpGet.addHeader(oAuthHeader);
			httpGet.addHeader(printHeader);

			HttpResponse httpResponse = httpClient.execute(httpGet);
			
			int statusCode = httpResponse.getStatusLine().getStatusCode();

			if (statusCode == 200) 
			{				
				String responseString = EntityUtils.toString(httpResponse.getEntity());
				try
				{
					JSONObject jsonObject = new JSONObject(responseString);
					System.out.println("JSON result of Query:\n" + jsonObject.toString(1));
					JSONArray jsonArray = jsonObject.getJSONArray("records");
					
					for (int i = 0; i < jsonArray.length(); i++)
					{
						caseId = jsonObject.getJSONArray("records").getJSONObject(i).getString("Id");
						caseNumber = jsonObject.getJSONArray("records").getJSONObject(i).getString("CaseNumber");
						caseSubject = jsonObject.getJSONArray("records").getJSONObject(i).getString("Subject");
						caseStatus = jsonObject.getJSONArray("records").getJSONObject(i).getString("Status");
						caseOrigin = jsonObject.getJSONArray("records").getJSONObject(i).getString("Origin");
						casePriority = jsonObject.getJSONArray("records").getJSONObject(i).getString("Priority"); 
						//Since the values are available, can be used later to create objects.
					}
				} 
				catch (JSONException jsonException) 
				{
					jsonException.printStackTrace();
				}			
			} 
			else 
			{
				System.out.print("Query was unsuccessful. Status code returned is " + statusCode);
				System.out.println(httpResponse.getEntity().getContent());
				System.exit(-1);
			}
		}
		catch (IOException ioException) 
		{
			ioException.printStackTrace();
		}
		catch (Exception exception) 
		{
			exception.printStackTrace();		
		}
	}

	public static void createCase() 
	{
		System.out.println("****************Case Creation**************");

		String finalURI = instanceUrl + "/services/apexrest/Cases/";
		try 
		{
			JSONObject newCase = new JSONObject();
			newCase.put("subject", "Smallfoot Sighting!");
			newCase.put("status", "New"); 
			newCase.put("origin", "Phone");
			newCase.put("priority", "Low");

			System.out.println("JSON for case record to be inserted:\n" + newCase.toString(1));

			HttpClient httpClient = HttpClientBuilder.create().build();

			HttpPost httpPost = new HttpPost(finalURI);
			httpPost.addHeader(oAuthHeader);
			httpPost.addHeader(printHeader);
			StringEntity entityBody = new StringEntity(newCase.toString(1));
			entityBody.setContentType("application/json");
			httpPost.setEntity(entityBody);

			HttpResponse httpResponse = httpClient.execute(httpPost);

			int statusCode = httpResponse.getStatusLine().getStatusCode();

			if (statusCode == 200) 
			{
				String responseString = EntityUtils.toString(httpResponse.getEntity());
				caseId = responseString;
				System.out.println("New Case Id from response: " + caseId);
			} 
			else 
			{
				System.out.println("Insertion unsuccessful. Status code returned is " + statusCode);
			}

		}
		catch (JSONException jsonException) 
		{
			System.out.println("Issue creating JSON or processing results");
			jsonException.printStackTrace();
		}
		catch (IOException ioException) 
		{
			ioException.printStackTrace();
		}
		catch (Exception exception) 
		{
			exception.printStackTrace();
		}
}
}