package netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

/**
 * heapBuffer 堆缓冲
 * 可读字节数 = writerIndex - readerIndex
 *
 * readerIndex <= writerIndex <= capacity
 */
public class ByteBufTest1 {
    public static void main(String[] args) {
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello world", Charset.forName("utf-8"));

        if(byteBuf.hasArray())
        {
            byte[] content = byteBuf.array();
            System.out.println(new String(content,Charset.forName("utf-8")));
            System.out.println(byteBuf.arrayOffset()); // 数组的偏移量 第一个字节的索引0
            System.out.println(byteBuf.readerIndex()); // 读索引起始位置
            System.out.println(byteBuf.writerIndex()); // 写索引起始位置
            System.out.println(byteBuf.capacity()); // 容量

        }
    }
}
