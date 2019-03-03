import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

/**
 * 2 * @Author: 睿
 * 3 * @Date: 2018/11/11 0011 18:24
 * 4    非阻塞NIO UDP
 */
public class TestNonBlockingNIO2 {
	@Test
	public void send() throws IOException {
		DatagramChannel dc = DatagramChannel.open();

		dc.configureBlocking(false);

		ByteBuffer buf = ByteBuffer.allocate(1024);

		buf.put(("哈哈哈啊啊啊").getBytes());

		buf.flip();

		dc.send(buf, new InetSocketAddress("127.0.0.1", 9898));

		dc.close();
	}

	@Test
	public void receive() throws IOException {
		DatagramChannel dc = DatagramChannel.open();

		dc.configureBlocking(false);

		dc.bind(new InetSocketAddress(9898));

		Selector selector = Selector.open();

		dc.register(selector, SelectionKey.OP_READ);

		while (selector.select() > 0) {
			Iterator<SelectionKey> it = selector.selectedKeys().iterator();
			while (it.hasNext()) {
				SelectionKey sk=it.next();

				if(sk.isReadable()){
					ByteBuffer buf=ByteBuffer.allocate(1024);
					dc.receive(buf);
					buf.flip();
					System.out.println(new String (buf.array(),0,buf.limit()));
					buf.clear();
				}
			}
			it.remove();
		}
	}
}
