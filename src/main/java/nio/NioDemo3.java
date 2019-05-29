package nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioDemo3 {
    public static void main(String[] args) throws Exception {
        FileOutputStream fileOutputStream = new FileOutputStream("NioTest3.txt");
        FileChannel fileChannel = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        byte[] msg = "hello world welcome to nio".getBytes();

        for (int i = 0; i < msg.length; i++) {
            byteBuffer.put(msg[i]);
        }

        byteBuffer.flip();
        fileChannel.write(byteBuffer);
        fileOutputStream.close();


    }
}
