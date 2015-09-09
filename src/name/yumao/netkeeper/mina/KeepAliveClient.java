package name.yumao.netkeeper.mina;

import java.net.InetSocketAddress;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;

import com.hisunsray.commons.res.Config;

public class KeepAliveClient {
	private static KeepAliveClient keepAliveClient;
	private static NioDatagramConnector connector;
	public static KeepAliveClient getInstance(){
		if(keepAliveClient==null){
			keepAliveClient = new KeepAliveClient();
			connector = new NioDatagramConnector();
			connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new HexCodecFactory()));
		}
		return keepAliveClient;
	}
	public void sendHeartbeat(String hexString){
	    DatagramSessionConfig cfg = connector.getSessionConfig();
	    cfg.setUseReadOperation(true);
	    IoSession session = connector.connect(new InetSocketAddress(Config.getProperty("SX_HB_SERVER_IP"), Integer.parseInt(Config.getProperty("SX_HB_SERVER_PORT")))).awaitUninterruptibly().getSession();
	    session.write(hexString);
	    session.close(true);
	}
}
