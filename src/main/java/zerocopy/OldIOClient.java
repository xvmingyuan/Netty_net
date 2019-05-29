package zerocopy;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.Socket;

/**
 * 4次拷贝过程 文件 -(复制)-> 内核缓冲区 -(复制)->用户缓冲区  ->一系列操作 --> 用户缓冲区 -(复制)-> 内核缓冲区 -(复制)-> 文件
 */
public class OldIOClient {

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 8899);

        String fileName = "/Users/xmy/Desktop/Tool/Hadoop/data/hadoop-2.9.1.tar.gz";
        FileInputStream inputStream = new FileInputStream(fileName);

        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

        byte[] buffer = new byte[4096];
        long readCount;
        long total = 0;
        long startTime = System.currentTimeMillis();
        while ((readCount = inputStream.read(buffer)) >= 0) {
            total += readCount;
            dataOutputStream.write(buffer);
        }
        System.out.println(" send byte count: " + total + ", time: " + (System.currentTimeMillis() - startTime));

        dataOutputStream.close();
        socket.close();
        inputStream.close();
    }
}
