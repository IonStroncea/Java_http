package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class Main {
	public static void main(String args[])
	{
		try
		{			
			String command="";
			Scanner in=new Scanner(System.in);
			while(!command.equals("stop"))
			{
				System.out.println("Ce doriti sa faceti?");
				System.out.println("1.Afisati lista de categorii");
				System.out.println("2.Detalii despre o categorie");
				System.out.println("3.Creati o categorie noua");
				System.out.println("4.Stergeti o categorie");
				System.out.println("5.Modifica titlul categoriei");
				System.out.println("6.Creati produse noi in categorie");
				System.out.println("7.Afisati lista de produse dintro categorie");
				
				command=in.nextLine();
				switch(command)
				{
					case"1":
					{
						JSONArray response=getMethod("https://localhost:5001/api/Category/categories");
						
						for(int i=0; i<response.length(); i++)
						{
							JSONObject object=response.getJSONObject(i);
							System.out.println("Category id:"+object.getInt("id")+" name:"+object.getString("name")
													+" items count:"+object.getInt("itemsCount"));
						}
					}	
						break;
					case"2":
					{
						System.out.println("Entre category id");
						command=in.nextLine();
						
						JSONArray response=getMethod("https://localhost:5001/api/Category/categories/"+command);
						
						for(int i=0; i<response.length(); i++)
						{
							JSONObject object=response.getJSONObject(i);
							System.out.println("Category id:"+object.getInt("id")+" name:"+object.getString("name")
													+" items count:"+object.getInt("itemsCount"));
						}
					}	
						break;
					case"3":
					{
						System.out.println("Entre category title");
						command=in.nextLine();
						
						JSONObject object=new JSONObject();
						object.put("title", command);
						
						JSONArray response=postMethod("https://localhost:5001/api/Category/categories",object);
						for(int i=0; i<response.length(); i++)
						{
							object=response.getJSONObject(i);
							System.out.println("Category id:"+object.getInt("id")+" name:"+object.getString("name")
													+" items count:"+object.getInt("itemsCount"));
						}
						
						
					}	
						break;
					case"4":
					{
						System.out.println("Entre category id");
						command=in.nextLine();
						deleteMethod("https://localhost:5001/api/Category/categories/"+command);
					}						
						break;
					case"5":
					{
						System.out.println("Entre category id");
						command=in.nextLine();
						
						System.out.println("Entre category new title");
						String title=in.nextLine();
						
						JSONObject object=new JSONObject();
						object.put("title", title);
						
						JSONArray response=putMethod("https://localhost:5001/api/Category/"+command,object);
						for(int i=0; i<response.length(); i++)
						{
							object=response.getJSONObject(i);
							System.out.println("Category id:"+object.getInt("id")+" name:"+object.getString("name")
													+" items count:"+object.getInt("itemsCount"));
						}
						
					}						
						break;
					case"6":
					{
						System.out.println("Entre category id");
						command=in.nextLine();
						
						System.out.println("Entre item title");
						String title=in.nextLine();
						
						System.out.println("Entre item price");
						String price=in.nextLine();
						
						JSONObject object=new JSONObject();
						object.put("id", 0);
						object.put("title", title);
						object.put("price", Integer.parseInt(price));
						object.put("categoryId", command);
						
						JSONArray response=postMethod("https://localhost:5001/api/Category/categories/"
														+command+"/products",object);
						for(int i=0; i<response.length(); i++)
						{
							object=response.getJSONObject(i);
							System.out.println("Item id:"+object.getInt("id")+" item title:"+object.getString("title")
													+" item price:"+object.getInt("price")
														+" categoryId:"+object.getInt("categoryId"));
						}
						
						
					}						
						break;
					case"7":
					{
						System.out.println("Entre category id");
						command=in.nextLine();
						JSONArray response=getMethod("https://localhost:5001/api/Category/categories/"+command+"/products");
						
						for(int i=0; i<response.length(); i++)
						{
							JSONObject object=response.getJSONObject(i);
							System.out.println("Item id:"+object.getInt("id")+" item title:"+object.getString("title")
													+" item price:"+object.getInt("price")
														+" categoryId:"+object.getInt("categoryId"));
						}
						
					}						
						break;
					
				}
			}
			
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	public static JSONArray getMethod(String URL)throws Exception
	{
		URL url = new URL(URL);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setConnectTimeout(5000);
		con.setReadTimeout(5000);
		int response=con.getResponseCode();
		
		JSONArray  albums=null;
		
		if(response == 200)
		{
			BufferedReader reader=new BufferedReader(new InputStreamReader(con.getInputStream()));
			
			StringBuffer all = new StringBuffer();
			String line="";
			
			while((line = reader.readLine()) != null)
			{
				all.append(line);
			}
			
			reader.close();
			
			albums = new JSONArray (all.toString());
			
			//System.out.println(all.toString());
						
		}
		else
		{
			System.out.println("Sometingh went wrong");
			throw new Exception("Error at getMethod. Response code: "+response+" at "+URL);
		}
		return albums;
	}
	
	public static JSONArray postMethod(String URL, JSONObject object)throws Exception
	{
		URL url = new URL(URL);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setConnectTimeout(5000);
		con.setReadTimeout(5000);
		con.setDoOutput(true);
		con.setRequestProperty("Content-Type", "application/json; utf-8");
		con.setRequestProperty("Accept", "application/json");
		
		String jsonString=object.toString();
		//System.out.println(jsonString);
		
		OutputStream os = con.getOutputStream();
		os.write(jsonString.getBytes("UTF-8"));
		os.close();
		
		int response=con.getResponseCode();
		JSONArray  albums=null;
		
		if(response == 200)
		{
			BufferedReader reader=new BufferedReader(new InputStreamReader(con.getInputStream()));
			
			StringBuffer all = new StringBuffer();
			String line="";
			
			while((line = reader.readLine()) != null)
			{
				all.append(line);
			}
			
			reader.close();
			
			//System.out.println(all.toString());
			
			if(all.charAt(0) == '[')
			{
				albums = new JSONArray (all.toString());
			}
			else
			{
				albums = new JSONArray ("["+all.toString()+"]");
			}
			
						
		}
		else
		{
			System.out.println("Sometingh went wrong");
			throw new Exception("Error at postMethod. Response code: "+response+" at "+URL);
		}
		return albums;
		
	}

	public static void deleteMethod(String URL)throws Exception
	{
		URL url = new URL(URL);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("DELETE");
		con.setConnectTimeout(5000);
		con.setReadTimeout(5000);
		
		int response=con.getResponseCode();
		if(response == 200)
		{
			System.out.println("Category succesfuly deleted");
		}
		else
		{
			System.out.println("Sometingh went wrong");
			throw new Exception("Error at deleteMethod. Response code: "+response+" at "+URL);
		}
		
		
	}

	public static JSONArray putMethod(String URL, JSONObject object)throws Exception
	{
		URL url = new URL(URL);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("PUT");
		con.setConnectTimeout(5000);
		con.setReadTimeout(5000);
		con.setDoOutput(true);
		con.setRequestProperty("Content-Type", "application/json; utf-8");
		con.setRequestProperty("Accept", "application/json");
		
		String jsonString=object.toString();
		//System.out.println(jsonString);
		
		OutputStream os = con.getOutputStream();
		os.write(jsonString.getBytes("UTF-8"));
		os.close();
		
		int response=con.getResponseCode();
		JSONArray  albums=null;
		
		if(response == 200)
		{
			BufferedReader reader=new BufferedReader(new InputStreamReader(con.getInputStream()));
			
			StringBuffer all = new StringBuffer();
			String line="";
			
			while((line = reader.readLine()) != null)
			{
				all.append(line);
			}
			
			reader.close();
			
			//System.out.println(all.toString());
			if(all.charAt(0) == '[')
			{
				albums = new JSONArray (all.toString());
			}
			else
			{
				albums = new JSONArray ("["+all.toString()+"]");
			}
			
						
		}
		else
		{
			System.out.println("Sometingh went wrong");
			throw new Exception("Error at putMethod. Response code: "+response+" at "+URL);
		}
		return albums;
	}
}