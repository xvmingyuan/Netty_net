package nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class NioServer {
    private static Map<String, SocketChannel> clientMap = new HashMap();

    public static void main(String[] args) throws Exception {
        // 创建 selector
        Selector selector = Selector.open();

        // 创建 一个通信监听(端口:8899)管道(ServerSocketChannel)
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //配置管道为非阻塞试
        serverSocketChannel.configureBlocking(false);
        // 添加端口
        serverSocketChannel.socket().bind(new InetSocketAddress(8899));

        // 将通信监听 交给selector 统一管理,接受
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            try {
                //selector
                selector.select();

                //获取一组键
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                // 遍历每个键
                selectionKeys.forEach(selectionKey -> {
                    //信息交流通道
                    final SocketChannel client;
                    try {
                        //selectionKey,状态:接受
                        if (selectionKey.isAcceptable()) {
                            //获取 具体某个通信管道
                            ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
                            //拿出通信管道中的 信息管道
                            client = server.accept();
                            //信息管道 设置为非阻塞
                            client.configureBlocking(false);
                            //向注册selector 该client的信息管道状态为READ的监听事件
                            client.register(selector, SelectionKey.OP_READ);

                            String key = "[" + UUID.randomUUID().toString() + "<" + client.getRemoteAddress() + ">]";
                            clientMap.put(key, client);

                        } else if (selectionKey.isReadable()) {//selectionKey,状态:可读
                            //拿出通信管道中的 信息管道
                            client = (SocketChannel) selectionKey.channel();
                            //建立缓冲流
                            ByteBuffer readBuffer = ByteBuffer.allocate(512);

                            //将client中数据输出到缓冲流当中
                            int count = client.read(readBuffer);
                            // 当count 大于0 说明有数据被读到 readBuffer中
                            if (count > 0) {
                                //翻转readBuffer状态 (limit=position,position=0,...
                                readBuffer.flip();
                                //设置字符编码格式
                                Charset charset = Charset.forName("utf-8");
                                //使用解码器 解readBuffer 转给String 输出
                                String msg = String.valueOf(charset.decode(readBuffer).array());

                                System.out.println("(" + client.getRemoteAddress() + ")" + msg);

                                String senderKey = null;
                                //在clientMap寻找当前client
                                for (Map.Entry<String, SocketChannel> entry : clientMap.entrySet()) {
                                    if (client == entry.getValue()) {
                                        //找到后获取Key
                                        senderKey = entry.getKey();
                                        break;
                                    }
                                }
                                //通知所有在线的客户端信息管道 ,某客户端的上线信息

                                for (Map.Entry<String, SocketChannel> entry : clientMap.entrySet()) {
                                    //获取客户端 信息管道
                                    SocketChannel value = entry.getValue();
                                    ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
                                    writeBuffer.put((senderKey + ":" + msg).getBytes());
                                    writeBuffer.flip();
                                    //将信息 写入到客户端 信息管道中
                                    value.write(writeBuffer);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                });
                //清空这一组键
                selectionKeys.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}
