
package com.smsapi.util;

// java imports
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.cloudhopper.commons.util.HexUtil;
import com.cloudhopper.smpp.pdu.SubmitSm;
import com.cloudhopper.smpp.pdu.SubmitSmResp;
import com.cloudhopper.smpp.tlv.TlvConvertException;

public class SubmitObject implements Serializable	{

	public static final String SMPP_DATE_FORMAT = "yyMMddHHmm";
	
	public static final String DATE_FORMAT = "yyyy/MM/dd/HH/mm";


	private transient SubmitSm sm = null;
	private transient SubmitSmResp _sr=null;
	private String message = null;

	private String fromAddress = null;
	private String deliveryTime = null;
	private String udh = null;
	private String msgType = null;
	private String expiry = null;
	private String esmClass = null;
	private String dataCoding = null;

	private String entityid=null;

	private String templateid=null;
	
	private static final long serialVersionUID = -5559816609285962474L;
	
		
	
	public SubmitObject( SubmitSm _sm, SubmitSmResp _sr)	{

		sm = _sm;
		this._sr=_sr;

		fromAddress = _sm.getSourceAddress().getAddress();

		dataCoding = Byte.toString(_sm.getDataCoding());
		esmClass = Byte.toString(_sm.getEsmClass());

				

		setMessage();				
		setScheduleTime();
		
		setExpiry();		
		setEntityId();
        setTemplateId();
		
		
	}
	
	private void setTemplateId() {

		
		if(sm.hasOptionalParameter((short)0x1401)){
			
			if(sm.getOptionalParameter((short)0x1401)!=null){
				
				try {
					templateid=sm.getOptionalParameter((short)0x1401).getValueAsString();
				} catch (TlvConvertException e) {
					
				}
			}
			
		}
	
	}

	private void setEntityId() {
		
		if(sm.hasOptionalParameter((short)0x1400)){
			
			if(sm.getOptionalParameter((short)0x1400)!=null){
				
				try {
					entityid=sm.getOptionalParameter((short)0x1400).getValueAsString();
				} catch (TlvConvertException e) {
					
				}
			}
			
		}
	}

	private void setMessage() {
		
		if(esmClass != null && (esmClass.equals("64") || esmClass.equals("67")))
		{
			//msgWithHeader = _sm.getShortMsg().getHexDump();
			String msgWithHeader =HexUtil.toHexString(sm.getShortMessage());
			String headerLen = msgWithHeader.substring(0, 2);
			int totLen = Integer.parseInt(headerLen,16);
					
			udh = msgWithHeader.substring(0, ((totLen*2)+2));
			
			
			message = msgWithHeader.substring(udh.length(), msgWithHeader.length());
			
			if(dataCoding.trim().equals("0")){
			message=new String(HexUtil.toByteArray(message));
			}
		}
		else
		{
			if(dataCoding.trim().equals("8")||dataCoding.trim().equals("18")||dataCoding.trim().equals("24"))
			{
				message=HexUtil.toHexString(sm.getShortMessage());
			}
			else
			{
				message = new String(sm.getShortMessage());
				
				
			}

		}			
	


		
	}


	private void setScheduleTime() {
		
		String origDelTS = sm.getScheduleDeliveryTime();
		
		if(origDelTS != null && !origDelTS.isEmpty() && origDelTS.length() > 6) {
					String deliveryTS = origDelTS.substring(0, origDelTS.length()-6);
		
					SimpleDateFormat sdf_1 = new SimpleDateFormat(SMPP_DATE_FORMAT);
					SimpleDateFormat sdf_2 = new SimpleDateFormat(DATE_FORMAT);				
					try {
						deliveryTime = sdf_2.format(sdf_1.parse(deliveryTS));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					deliveryTime = null;
				}
			

		
	}


	private void setExpiry() {
		

		
		String origValPeriod = sm.getValidityPeriod();
		if(origValPeriod != null && !origValPeriod.isEmpty() && origValPeriod.length() > 4) {
			String validityPeriod = origValPeriod.substring(0, origValPeriod.length()-4);			
			
				SimpleDateFormat format = new SimpleDateFormat( "yyMMddHHmmss" );	

				Date current_date = new Date();
				Date valid_date=null;
				try {
					valid_date = format.parse(validityPeriod);
					long validMins = (valid_date.getTime()/60000) - (current_date.getTime()/60000);

					
					if(validMins <= 0){ //IGNORE IF USER HAS GIVEN PAST DATE 
						expiry = null;
					}else {
						expiry = String.valueOf(validMins);
					}

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

						}


		
	}

	
	
	
	
	public String decodeHexString(String hexText) throws Exception 
	{
		String decodedText=null;
		String chunk=null;
	
		try
		{
			
			if(hexText!=null && hexText.length()>0) {
				int numBytes = hexText.length()/2;

				byte[] rawToByte = new byte[numBytes];
				int offset=0;
				int bCounter=0;
				for(int i =0; i <numBytes; i++) {
					chunk = hexText.substring(offset,offset+2);
					offset+=2;
					rawToByte[i] = (byte) (Integer.parseInt(chunk,16) & 0x000000FF);
				}
				decodedText= new String(rawToByte);
			}
		}
		catch(Exception e)
		{
			decodedText = hexText;
			

		}
	
		return decodedText;
	}


	public void setValues(Map<String, Object> msgmap) {
		
		msgmap.put(MapKeys.SENDERID_ORG, fromAddress);
		msgmap.put(MapKeys.SENDERID, fromAddress);
		msgmap.put(MapKeys.FULLMSG, message);
		msgmap.put(MapKeys.MESSAGE, message);
		msgmap.put(MapKeys.MSGTYPE, msgType);
		msgmap.put(MapKeys.UDH, udh);
		msgmap.put(MapKeys.SCHEDULE_TIME_STRING, deliveryTime);
		msgmap.put(MapKeys.EXPIRY, expiry);
		msgmap.put(MapKeys.TEMPLATEID, templateid);
		msgmap.put(MapKeys.ENTITYID, entityid);
		msgmap.put("esm", ""+esmClass);
		msgmap.put("datacoding", ""+dataCoding);
		msgmap.put("module","smppgateway");

	}

	
	public static void main(String args[]){
		
		System.out.println(new String(HexUtil.toByteArray("616765206D6F7265207468616E2031363020636861726163746572")));
	}
}


