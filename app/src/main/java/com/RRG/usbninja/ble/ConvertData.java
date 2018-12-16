package com.RRG.usbninja.ble;


import java.io.UnsupportedEncodingException;

public class ConvertData
{
    /**
     * byte[]->HEXString
     *
     * @param bytes byte[]
     * @param bSpace  add Space Char between two Chars
     *
     * @return String hexstr
     */
	public static String bytesToHexString(byte[] bytes, boolean bSpace)
	{
		if(bytes == null || bytes.length <= 0)
			return null;
		
		StringBuffer stringBuffer = new StringBuffer(bytes.length);
		String sTemp;
		
		for (int i = 0; i < bytes.length; i++) 
		{
			sTemp = Integer.toHexString(0xFF & bytes[i]);
			
			if (sTemp.length() < 2)
				stringBuffer.append(0);
			
			stringBuffer.append(sTemp);
			
			if(bSpace)
				stringBuffer.append(" ");
		}
		return stringBuffer.toString();
	}

	/**
	 * HEXString->byte[]
	 *
	 * @param String hexstr
	 *
	 * @return byte[]
	 */
	public static byte[] hexStringToBytes(String hexString)
	{
		if(hexString == null)
			return null;
		
		hexString = hexString.replace(" ", "");
		hexString = hexString.toUpperCase();
		
		int len = (hexString.length() / 2);
		if(len <= 0)
			return null;
		
		byte[] result = new byte[len];
		char[] achar = hexString.toCharArray();
		for (int i = 0; i < len; i++) 
		{
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		
		return result;
	}

	/**
	 * byte[]->utf-8
	 *
	 * @param String src
	 *
	 * @return return byte[]
	 */
	public static byte[] unicodeToBytes(String src)
	{
		if(src == null)
			return null;

        byte[] bytes = null;
        try {
            bytes = src.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return bytes;
	}

	public static String bytesToUnicode(byte[] bytes)
	{
		if(bytes == null)
			return null;

        String str = null;
        try {
            str = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return str;
	}
	
    /**
     * Copy Byte[]
     *
     * @param dst
     * @param dstOffset
     * @param src
     * @param srcOffset
     * @param length
     *
     * @return Success=true,else=false;
     */
	public static boolean cpyBytes(byte[] dst, int dstOffset, byte[] src, int srcOffset, int length)
	{
		if(dst == null || src == null || 
				dstOffset > dst.length || srcOffset > src.length ||
				length > (dst.length-dstOffset) || length > (src.length-srcOffset))
		{
			return false;
		}
		
		for (int i = 0; i < length; i++)
		{
			dst[i+dstOffset] = src[i+srcOffset];
		}
		
		return true;
	}
	
    /**
     * Compare two []
     *
     * @param data1 []1
     * @param data2 []2
     *
     * @return Equal=true,else=false;
     */
	public static boolean cmpBytes(byte[] data1, byte[] data2)
	{
		if (data1 == null && data2 == null)
		{
			return true;
		}
		if (data1 == null || data2 == null) 
		{
			return false;
		}
		if (data1 == data2)
		{
			return true;
		}
		if(data1.length != data2.length)
		{
			return false;
		}
		
		int len = data1.length;
		for (int i = 0; i < len; i++)
		{
			if(data1[i] != data2[i])
				return false;
		}
		
		return true;
	}
	
	private static int toByte(char c) 
	{
	    byte b = (byte) "0123456789ABCDEF".indexOf(c);
	    return b;
	 }
}
