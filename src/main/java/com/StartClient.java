package com;

import com.alipay.remoting.ConnectionEventType;
import com.alipay.remoting.exception.RemotingException;
import com.alipay.remoting.rpc.RpcClient;
import org.junit.Assert;

/**
 * @Author:wjy
 * @Date: 2018/11/6.
 */
public class StartClient {
	static RpcClient client;

	static String             addr                      = "127.0.0.1:8999";

	SimpleClientUserProcessor clientUserProcessor       = new SimpleClientUserProcessor();
	CONNECTEventProcessor     clientConnectProcessor    = new CONNECTEventProcessor();
	DISCONNECTEventProcessor  clientDisConnectProcessor = new DISCONNECTEventProcessor();

	public StartClient(){
		// 1. create a rpc client
		client = new RpcClient();
		// 2. add processor for connect and close event if you need
		client.addConnectionEventProcessor(ConnectionEventType.CONNECT, clientConnectProcessor);
		client.addConnectionEventProcessor(ConnectionEventType.CLOSE, clientDisConnectProcessor);
		// 3. do init
		client.init();
	}


	public static void main(String[] args) {
		new StartClient();
		RequestBody req = new RequestBody(2, "hello world sync");
		try {
			String res = (String) client.invokeSync(addr, req, 3000);
			System.out.println("invoke sync result = [" + res + "]");
		} catch (RemotingException e) {
			String errMsg = "RemotingException caught in oneway!";
			//logger.error(errMsg, e);
			Assert.fail(errMsg);
		} catch (InterruptedException e) {
			//logger.error("interrupted!");
		}
		client.shutdown();
	}
}
