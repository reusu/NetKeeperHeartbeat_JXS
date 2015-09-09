package name.yumao.netkeeper;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import name.yumao.netkeeper.mina.KeepAliveClient;
import name.yumao.netkeeper.utils.AESUtils;
import name.yumao.netkeeper.utils.HexUtils;

import com.hisunsray.commons.res.Config;

public class NetKeeperHearbeatMain {
	private static String configFilePath = "conf" + File.separator + "global.properties";
	public static void main(String[] args) throws Exception {
		int i = 0;
		//读取配置文件
		Config.setConfigResource(configFilePath);
		String _ip = "1.2.4.8";
		if(Config.getProperty("SX_HB_LOCAL_IP")==null || Config.getProperty("SX_HB_LOCAL_IP").length()==0){
			//获取公网IP 适用于Linux系统
			String[] _command = {"/bin/sh","-c"," ifconfig  | grep 'inet addr:'| grep -v '127.0.0.1' | grep -v '127.0.0.2' | grep -v '172.20.20.' | grep -v '172.25.25.' | grep -v '192.168.100.1' | cut -d: -f2 | awk '{ print $1}'"};
	        Runtime _runtime =  Runtime.getRuntime();
	        Process _process = _runtime.exec(_command);
	        _process.waitFor();
			InputStream is = _process.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line;
			while((line = reader.readLine())!= null){
				_ip = line.trim();
			}
		}else{
			 _ip = Config.getProperty("SX_HB_LOCAL_IP");
		}
		//准备心跳包内容
		String _hbStr = 
				"TYPE=HEARTBEAT&" +
				"USER_NAME=" + Config.getProperty("SX_HB_USERNAME") + "&" +
				"PASSWORD=" + Config.getProperty("SX_HB_PASSWORD") + "&" +
				"IP=" + _ip + "&" +
				"MAC=00%2D00%2D00%2D00%2D00%2D00&" +
				"DRIVER=1&" +
				"VERSION_NUMBER=2.5.0016v32&" +
				"HOOK=&" +
				"IP2=" + _ip;
		
		System.out.println(_hbStr);
		//进行AES|ECB加密
		byte[] _bodyBytes = AESUtils.encrypt(_hbStr.getBytes("UTF-8"), Config.getProperty("SX_HB_AESKEY"));
		//变成HexString加上头
		String _hexString = "4852333005" + HexUtils.setStringHeader(HexUtils.Bytes2HexString(_bodyBytes));
		//江西暂时没有动态key 所以2分钟发一次这个包就ok
		while(true){
			KeepAliveClient.getInstance().sendHeartbeat(_hexString);
			System.out.println("丢你楼某\t" + (++i));
			Thread.sleep(120000);
		}
	}
}
