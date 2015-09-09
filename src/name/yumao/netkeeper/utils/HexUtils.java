package name.yumao.netkeeper.utils;



import org.apache.mina.core.buffer.IoBuffer;

public class HexUtils {
    public static IoBuffer hexString2IoBuffer(String hexString){
//    	System.out.println(hexString);
    	IoBuffer ioBuffer = IoBuffer.allocate(8);
    	ioBuffer.setAutoExpand(true);
    	ioBuffer.put(HexString2Bytes(hexString));
    	ioBuffer.flip(); 
    	return ioBuffer;
    }
    public static String ioBufferToString(Object message) throws Exception   
    {   
          if (!(message instanceof IoBuffer))   
          {   
            return "";   
          }   
          IoBuffer ioBuffer = (IoBuffer) message;   
          byte[] b = new byte [ioBuffer.limit()];   
          ioBuffer.get(b);   
          String bb = new String(b,"utf-8");
//          bb = bb.substring(bb.indexOf("type"),bb.indexOf("@Srg"));
//          StringBuffer stringBuffer = new StringBuffer();   
//          for (int i = 12; i < b.length; i++)   
//          {   
//           stringBuffer.append((char) b [i]);   
//          }   
           return bb;
//          return new String(stringBuffer.toString().getBytes());
    }  
    
    public static byte[] ioBufferToBytes(Object message) throws Exception   
    {   
          if (!(message instanceof IoBuffer))   
          {   
            return "".getBytes();   
          }   
          IoBuffer ioBuffer = (IoBuffer) message;   
          byte[] b = new byte [ioBuffer.limit()];   
          ioBuffer.get(b);    
          return b;
    }  
    
    public static String ioBufferToHexString(Object message) throws Exception   
    {   
          if (!(message instanceof IoBuffer))   
          {   
            return "";   
          }   
          IoBuffer ioBuffer = (IoBuffer) message;   
          byte[] b = new byte [ioBuffer.limit()];   
          ioBuffer.get(b);   
          String bb = Bytes2HexString(b);
          return bb;
    }  
    
    private final static byte[] hex = "0123456789ABCDEF".getBytes();  
    private static int parse(char c) {  
        if (c >= 'a')  
            return (c - 'a' + 10) & 0x0f;  
        if (c >= 'A')  
            return (c - 'A' + 10) & 0x0f;  
        return (c - '0') & 0x0f;  
    }  
    // 从字节数组到十六进制字符串转换  
    public static String Bytes2HexString(byte[] b) {  
        byte[] buff = new byte[3 * b.length];  
        for (int i = 0; i < b.length; i++) {  
            buff[3 * i] = hex[(b[i] >> 4) & 0x0f];  
            buff[3 * i + 1] = hex[b[i] & 0x0f];  
            buff[3 * i + 2] = 45;  
        }  
        String re = new String(buff);  
        return re.replace("-", " ");  
    }  
    public static String Bytes2HexStringWithOutSpace(byte[] b) {  
        byte[] buff = new byte[3 * b.length];  
        for (int i = 0; i < b.length; i++) {  
            buff[3 * i] = hex[(b[i] >> 4) & 0x0f];  
            buff[3 * i + 1] = hex[b[i] & 0x0f];  
            buff[3 * i + 2] = 45;  
        }  
        String re = new String(buff);  
        return re.replace("-", "");  
    } 
    // 从字节数组到十六进制字符串转换  
    public static String Bytes2HexStringLower(byte[] b) {  
        byte[] buff = new byte[3 * b.length];  
        for (int i = 0; i < b.length; i++) {  
            buff[3 * i] = hex[(b[i] >> 4) & 0x0f];  
            buff[3 * i + 1] = hex[b[i] & 0x0f];  
            buff[3 * i + 2] = 45;  
        }  
        String re = new String(buff);  
        return re.replace("-", "").toLowerCase();  
    } 
    // 从十六进制字符串到字节数组转换  
    public static byte[] HexString2Bytes(String hexstr) {  
        hexstr = hexstr.replace(" ", "");  
        byte[] b = new byte[hexstr.length() / 2];  
        int j = 0;  
        for (int i = 0; i < b.length; i++) {  
            char c0 = hexstr.charAt(j++);  
            char c1 = hexstr.charAt(j++);  
            b[i] = (byte) ((parse(c0) << 4) | parse(c1));  
        }  
        return b;  
    }  
    public static byte[] hexString2Bytes(String hexString) {
    	hexString = hexString.replace(" ", "");  
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
            
        }
        return d;
    }
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
    
    public static String setStringHeader(String hexStr){
    	hexStr = hexStr.replace(" ", "");
    	String length = Integer.toHexString(hexStr.length()/2);
    	String lh = "";
    	for(int i=0;i<8-length.length() ; i++){
    		lh+="0";
    	}
    	return (lh + length + hexStr).replace(" ", "");
    }
    public static int getHexStringLength(String hexStr){
    	hexStr = hexStr.replace(" ", "");
    	if(hexStr.length() < 8){
    		return hexStr.length() + 1;
    	}
    	String headerStr = hexStr.substring(0,8);
    	return bytes2Length(HexUtils.HexString2Bytes(headerStr), "left");
    }
    //4位byte[]转int长度
    public static int bytes2Length(byte[] bytes,String level){
    	if(level.equals("left")){
    	    int leng = (int) ((bytes[0] & 0xFF)   
    	            | ((bytes[1] & 0xFF)<<8)) 
    	            | ((bytes[2] & 0xFF)<<16)   
    	            | ((bytes[3] & 0xFF)<<24); 
    	    return leng;
    	}else if(level.equals("right")){
    	    int leng = (int) ((bytes[1] & 0xFF)   
    	            | ((bytes[0] & 0xFF)<<8))
    	            | ((bytes[1] & 0xFF)<<16)   
    	            | ((bytes[0] & 0xFF)<<24); 
    	    return leng;
    	}else{
    		return 0;
    	}
    }
}
