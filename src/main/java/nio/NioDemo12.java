package nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Selector
 */
public class NioDemo12 {
    public static void main(String[] args) throws Exception {
        int[] ports = new int[5];
        ports[0] = 5000;
        ports[1] = 5001;
        ports[2] = 5002;
        ports[3] = 5003;
        ports[4] = 5004;

        Selector selector = Selector.open();

        for (int i = 0; i < ports.length; i++) {

            // 创建 一个通信监听(端口:ports[i])管道(ServerSocketChannel)
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            // 非阻塞式
            serverSocketChannel.configureBlocking(false);
            //绑定端口
            serverSocketChannel.socket().bind(new InetSocketAddress(ports[i]));

            // 将通信监听 交给selector 统一管理
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("listen port: " + ports[i]);
        }
        // 服务器 始终启动
        while (true) {
            int numbers = selector.select();
            System.out.println("numbers: " + numbers);
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            System.out.println("selectionKeys" + selectionKeys);

            Iterator<SelectionKey> iter = selectionKeys.iterator();

            //轮寻 selector 中 获取 每个SelectionKey状态信息
            while (iter.hasNext()) {
                SelectionKey selectionKey = iter.next();

                // 接受服务器连接阶段
                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel serverSocketChannel1 = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel socketChannel = serverSocketChannel1.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);

                    // 清空当前轮训的状态数据,为下一轮做准备
                    iter.remove();
                    System.out.println("获得客户端连接" + socketChannel);
                } else if (selectionKey.isReadable()) { //接受可读阶段
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    int byteRead = 0;
                    while (true) {

                        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
                        byteBuffer.clear();
                        //从bytebuffer 中读取缓冲数据
                        int read = socketChannel.read(byteBuffer);
                        if (read <= 0) {// 已经读完,不必再写出
                            break;
                        }
                        //翻转
                        byteBuffer.flip();
                        // 写出数据到socketChannel(socket管道)
                        socketChannel.write(byteBuffer);
                        byteRead += read;
                    }
                    System.out.println("读取： " + byteRead + "，来自于： " + socketChannel);

                    // 清空当前轮训的状态数据,为下一轮做准备
                    iter.remove();
                }
            }
        }
    }

}
