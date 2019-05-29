package zerocopy;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * 零拷贝
 * 关键词 : DMA copy,  kernel buffer(内核缓冲区) ,socket buffer(socket缓冲区),protocol engine(协议引擎)
 * DMA copy 到 kernel buffer CPU copy 一个描述信息给 socket buffer(包含 kernel buffer的地址 和 长度)
 * 操作完成后,由协议引擎根据描述信息直接对内核缓冲区操作,实现数据拷贝,减少2次拷贝过程
 */
public class NewIOClient {
    public static void main(String[] args) throws Exception {

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 8899));
        socketChannel.configureBlocking(true);

        String fileName = "/Users/xmy/Desktop/Tool/Hadoop/data/hadoop-2.9.1.tar.gz";
        FileChannel fileChannel = new FileInputStream(fileName).getChannel();

        long startTime = System.currentTimeMillis();
        long transferCount = fileChannel.transferTo(0, fileChannel.size(), socketChannel);

        System.out.println("发送总字节数：" + transferCount + "，耗时： " + (System.currentTimeMillis() - startTime));

        fileChannel.close();


    }
}
