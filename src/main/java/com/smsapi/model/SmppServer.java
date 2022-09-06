package com.smsapi.model;

import com.cloudhopper.smpp.SmppServerConfiguration;
import com.cloudhopper.smpp.impl.DefaultSmppServer;
import com.cloudhopper.smpp.type.SmppChannelException;


public class SmppServer {
	
	public SmppServer(SmppServerConfiguration smppserverconfiguration,DefaultSmppServerHandler defaultSmppServerHandler)  {

		DefaultSmppServer defaultSmppServer = new DefaultSmppServer(smppserverconfiguration, defaultSmppServerHandler);

		try {
			defaultSmppServer.start();
		} catch (SmppChannelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Server Started");
	}





	
}
