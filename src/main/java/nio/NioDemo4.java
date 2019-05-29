package nio;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
/**
 * buffer中清除 是覆盖式清除,如果没有覆盖,则原来的数据依然存在
 * 调用 clear()方法重置position.limit 视为清除buffer
 */
public class NioDemo4 {
    public static void main(String[] args) throws Exception {
        FileInputStream inputStream = new FileInputStream("input.txt");
        FileOutputStream outputStream = new FileOutputStream("output.txt");

        FileChannel inputStreamChannel = inputStream.getChannel();
        FileChannel outputStreamChannel = outputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while (true) {
            buffer.clear();
            int read = inputStreamChannel.read(buffer);
            if (-1 == read ) {
                break;
            }
            //切换状态
            buffer.flip();

            outputStreamChannel.write(buffer);
        }

        inputStreamChannel.close();
        outputStreamChannel.close();
        inputStream.close();
        outputStream.close();

    }

}
