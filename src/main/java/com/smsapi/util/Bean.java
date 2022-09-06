package com.smsapi.util;

import java.util.Map;
import java.util.UUID;

public class Bean {

	public static void setDefaultValues(Map<String,Object> msgmap){
		WinDate date= new WinDate();
		String ackid=UUID.randomUUID().toString();
		msgmap.put(MapKeys.ACKID, ackid);
		msgmap.put(MapKeys.TTYPE, "request");

		msgmap.put(MapKeys.SPLIT_MSGID, ackid);

		if(msgmap.get(MapKeys.MSGID)==null){
		msgmap.put(MapKeys.MSGID, msgmap.get(MapKeys.ACKID));
		}long sysdate=System.currentTimeMillis();
		msgmap.put(MapKeys.RTIME, ""+sysdate);

	}
}
