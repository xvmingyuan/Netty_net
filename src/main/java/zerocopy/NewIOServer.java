package zerocopy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * 零拷贝
 * 关键词  DMA copy,  kernel buffer(内核缓冲区) ,socket buffer(socket缓冲区),protocol engine(协议引擎)
 * DMA copy 到 kernel buffer CPU copy 一个描述信息给 socket buffer(包含 kernel buffer的地址 和 长度)
 * 操作完成后,由协议引擎根据描述信息直接对内核缓冲区操作,实现数据拷贝,减少2次拷贝过程
 */
public class NewIOServer {
    public static void main(String[] args) throws Exception {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(new InetSocketAddress(8899));
        serverSocket.setReuseAddress(true);

        ByteBuffer buffer = ByteBuffer.allocate(4096);

        while (true) {
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(true);

            int readCount = 0;
            while (-1 != readCount) {
                try {
                    readCount = socketChannel.read(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                buffer.rewind();
            }
        }
    }
}
