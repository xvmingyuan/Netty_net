package nio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NioClient {

    public static void main(String[] args) throws Exception {
        try {
            //创建 selector
            Selector selector = Selector.open();
            // 开启通信管道
            SocketChannel socketChannel = SocketChannel.open();
            // 非阻塞设置
            socketChannel.configureBlocking(false);

            //注册连接到selector
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
            // 绑定IP端口
            socketChannel.connect(new InetSocketAddress("127.0.0.1", 8899));
            //轮询
            while (true) {
                //开启selector
                selector.select();
                // 遍历selector 通信管道
                Set<SelectionKey> keySet = selector.selectedKeys();
                for (SelectionKey selectionKey : keySet) {
                    //通信状态:可连接
                    if (selectionKey.isConnectable()) {
                        // client(服务端在客户端被视为一个client)
                        SocketChannel client = (SocketChannel) selectionKey.channel();
                        //是否可连接
                        if (client.isConnectionPending()) {
                            //完成连接
                            client.finishConnect();
                            ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
                            writeBuffer.put((LocalDateTime.now() + " connect success").getBytes());
                            writeBuffer.flip();
                            // 客户端数据发送给服务端
                            client.write(writeBuffer);
                            //使用线程池开启一个单例线程
                            ExecutorService executorService = Executors.newSingleThreadExecutor(Executors.defaultThreadFactory());

                            // 线程功能:监听键盘输入,并将信息写入缓冲区,发送给client(服务端)
                            executorService.submit(() -> {
                                while (true) {
                                    try {
                                        writeBuffer.clear();
                                        InputStreamReader input = new InputStreamReader(System.in);
                                        BufferedReader br = new BufferedReader(input);
                                        String sendMsg = br.readLine();

                                        writeBuffer.put(sendMsg.getBytes());
                                        writeBuffer.flip();
                                        client.write(writeBuffer);

                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            });
                        }
                        //client向selector注册状态:OP_READ,并开启监听

                        client.register(selector, SelectionKey.OP_READ);
                    } else if (selectionKey.isReadable()) { //通信状态:可读

                        SocketChannel client = (SocketChannel) selectionKey.channel();
                        ByteBuffer readBuffer = ByteBuffer.allocate(1024);

                        int count = client.read(readBuffer);
                        if (count > 0) {
                            String receiveMessage = new String(readBuffer.array(), 0, count);
                            System.out.println(receiveMessage);
                        }

                    }
                }
                //清空当前轮的keySet信息监听状态
                keySet.clear();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
