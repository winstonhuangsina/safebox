package com.safebox.msg;

import intent.pack.R;

import java.security.Key; 

import javax.crypto.Cipher; 
import javax.crypto.spec.SecretKeySpec; 
 





import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class EncryptService {
	
	private String keyStr;
	 String sKey;
	
	public EncryptService(){
		keyStr = "safebox_encrypt_code";
		sKey = "1234567890abcDEF";
	}
	
	
	
	private String AESTYPE ="AES/ECB/PKCS5Padding"; 
    /*public  String encrypt(String plainText) { 
        byte[] encrypt = null; 
        try{ 
            Key key = generateKey(keyStr); 
            Cipher cipher = Cipher.getInstance(AESTYPE); 
            cipher.init(Cipher.ENCRYPT_MODE, key); 
            encrypt = cipher.doFinal(plainText.getBytes());     
        }catch(Exception e){ 
            e.printStackTrace(); 
        }
        BASE64Encoder enc=new BASE64Encoder();
  	  //String ת�����string=enc.encode(byte����);
        return new String(enc.encode(encrypt)); 
    } 
 
    public  String decrypt(String encryptData) {
        byte[] decrypt = null; 
        try{ 
            Key key = generateKey(keyStr); 
            Cipher cipher = Cipher.getInstance(AESTYPE); 
            cipher.init(Cipher.DECRYPT_MODE, key); 
            BASE64Decoder dec=new BASE64Decoder(); 
            decrypt = cipher.doFinal(dec.decodeBuffer(encryptData)); 
        }catch(Exception e){ 
            e.printStackTrace(); 
        } 
        return new String(decrypt).trim(); 
    } 
 
    private  Key generateKey(String key)throws Exception{ 
        try{            
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES"); 
            return keySpec; 
        }catch(Exception e){ 
            e.printStackTrace(); 
            throw e; 
        } 
 
    } 
	*/
	
    public  String Decrypt(String sSrc) throws Exception {
		try {
			// �ж�Key�Ƿ���ȷ
			if (sKey == null) {
				System.out.print("KeyΪ��null");
				return null;
			}
			// �ж�Key�Ƿ�Ϊ16λ
			if (sKey.length() != 16) {
				System.out.print("Key���Ȳ���16λ");
				return null;
			}
			byte[] raw = sKey.getBytes("ASCII");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			byte[] encrypted1 = hex2byte(sSrc);
			try {
				byte[] original = cipher.doFinal(encrypted1);
				String originalString = new String(original);
				return originalString;
			} catch (Exception e) {
				System.out.println(e.toString());
				return null;
			}
		} catch (Exception ex) {
			System.out.println(ex.toString());
			return null;
		}
	}

	// �ж�Key�Ƿ���ȷ
	public  String Encrypt(String sSrc) throws Exception {
		System.out.println("111########## EncryptService sSrc = " + sSrc);
		if (sKey == null) {
			System.out.print("KeyΪ��null");
			return null;
		}
		// �ж�Key�Ƿ�Ϊ16λ
		if (sKey.length() != 16) {
			System.out.print("Key���Ȳ���16λ");
			return null;
		}
		System.out.println("222##########in EncryptService  sSrc = " + sSrc);
		byte[] raw = sKey.getBytes("ASCII");
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(sSrc.getBytes());
		return byte2hex(encrypted).toLowerCase();
	}

	public byte[] hex2byte(String strhex) {
		if (strhex == null) {
			return null;
		}
		int l = strhex.length();
		if (l % 2 == 1) {
			return null;
		}
		byte[] b = new byte[l / 2];
		for (int i = 0; i != l / 2; i++) {
			b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2),
					16);
		}
		return b;
	}

	public String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
		}
		return hs.toUpperCase();
	}
	
	
	/*
	
	*//** 
	 * ���� 
	 *  
	 * @param content ��Ҫ���ܵ����� 
	 * @param password  �������� 
	 * @return 
	 *//*  
	public byte[] encrypt(String content, String password) {  
	        try {             
	                KeyGenerator kgen = KeyGenerator.getInstance("AES");  
	                kgen.init(128, new SecureRandom(password.getBytes()));  
	                SecretKey secretKey = kgen.generateKey();  
	                byte[] enCodeFormat = secretKey.getEncoded();  
	                SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");  
	                Cipher cipher = Cipher.getInstance("AES");// ����������   
	                byte[] byteContent = content.getBytes("utf-8");  
	                cipher.init(Cipher.ENCRYPT_MODE, key);// ��ʼ��   
	                byte[] result = cipher.doFinal(byteContent);  
	                return result; // ����   
	        } catch (NoSuchAlgorithmException e) {  
	                e.printStackTrace();  
	        } catch (NoSuchPaddingException e) {  
	                e.printStackTrace();  
	        } catch (InvalidKeyException e) {  
	                e.printStackTrace();  
	        } catch (UnsupportedEncodingException e) {  
	                e.printStackTrace();  
	        } catch (IllegalBlockSizeException e) {  
	                e.printStackTrace();  
	        } catch (BadPaddingException e) {  
	                e.printStackTrace();  
	        }  
	        return null;  
	}  
	
	*//**���� 
	 * @param content  ���������� 
	 * @param password ������Կ 
	 * @return 
	 *//*  
	public byte[] decrypt(byte[] content, String password) {  
	        try {  
	                 KeyGenerator kgen = KeyGenerator.getInstance("AES");  
	                 kgen.init(128, new SecureRandom(password.getBytes()));  
	                 SecretKey secretKey = kgen.generateKey();  
	                 byte[] enCodeFormat = secretKey.getEncoded();  
	                 SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");              
	                 Cipher cipher = Cipher.getInstance("AES");// ����������   
	                cipher.init(Cipher.DECRYPT_MODE, key);// ��ʼ��   
	                byte[] result = cipher.doFinal(content);  
	                return result; // ����   
	        } catch (NoSuchAlgorithmException e) {  
	                e.printStackTrace();  
	        } catch (NoSuchPaddingException e) {  
	                e.printStackTrace();  
	        } catch (InvalidKeyException e) {  
	                e.printStackTrace();  
	        } catch (IllegalBlockSizeException e) {  
	                e.printStackTrace();  
	        } catch (BadPaddingException e) {  
	                e.printStackTrace();  
	        }  
	        return null;  
	}  
	
	
	
	*//**��������ת����16���� 
	 * @param buf 
	 * @return 
	 *//*  
	public static String parseByte2HexStr(byte buf[]) {  
	        StringBuffer sb = new StringBuffer();  
	        for (int i = 0; i < buf.length; i++) {  
	                String hex = Integer.toHexString(buf[i] & 0xFF);  
	                if (hex.length() == 1) {  
	                        hex = '0' + hex;  
	                }  
	                sb.append(hex.toUpperCase());  
	        }  
	        return sb.toString();  
	}  
	
	
	*//**��16����ת��Ϊ������ 
	 * @param hexStr 
	 * @return 
	 *//*  
	public static byte[] parseHexStr2Byte(String hexStr) {  
	        if (hexStr.length() < 1)  
	                return null;  
	        byte[] result = new byte[hexStr.length()/2];  
	        for (int i = 0;i< hexStr.length()/2; i++) {  
	                int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);  
	                int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);  
	                result[i] = (byte) (high * 16 + low);  
	        }  
	        return result;  
	}  
	
	public String ByteToString(byte[] arg0){
	BASE64Encoder enc=new BASE64Encoder();
	  //String ת�����string=enc.encode(byte����);
	 return enc.encode(arg0);
	}
	//��stringת��������Ϊbyte���飺  

	public byte[] StringToByte(String arg){
	  BASE64Decoder dec=new BASE64Decoder(); 
	  byte[] byte_from_string = null;
	  try {
	   //byte���� = dec.decodeBuffer(ת�����string);
		   byte_from_string  =  dec.decodeBuffer(arg);
	  } catch (IOException e1) {
	   e1.printStackTrace();
	  }
	  return byte_from_string;
	}
	*/
}
