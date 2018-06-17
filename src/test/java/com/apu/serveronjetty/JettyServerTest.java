package com.apu.serveronjetty;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.junit.Before;
import org.junit.Test;

public class JettyServerTest {
	
	private HttpClient client;
	private JettyServer server;
	private String SERVER_LINK = "http://localhost:8082/status";
	 
	@Before
	public void beforeTest() {
		server = new JettyServer();
		client = new HttpClient();
	}

	@Test
	public void testStart() {			
		GetMethod request = new GetMethod(SERVER_LINK);
		try {
			Thread thread = new Thread( new Runnable() {
				@Override
				public void run() {
					try {
						server.start();
					} catch (Exception e) {
						e.printStackTrace();
					}					
				}				
			});
			thread.start();
			while(!server.isStarted()) {};
			client.executeMethod(request);
			BufferedReader breader = new BufferedReader(
                    new InputStreamReader(
                    request.getResponseBodyAsStream(), "utf-8"), 4096);
			StringBuilder sb = new StringBuilder();
			String line;
			while((line = breader.readLine()) != null) {
				sb.append(line);
			}
			String responseBody = sb.toString();
			String waitingBody = "{ \"status\": \"ok\"}";
			server.stop();
			assertTrue(responseBody.equals(waitingBody));
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
