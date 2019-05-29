package netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ByteBufTest0 {
    public static void main(String[] args) {
        ByteBuf buffer = Unpooled.buffer(10);
        for (int i = 0; i < 10; i++) {
            buffer.writeByte(i);
        }
        /* 绝对方法 直接调用对应位置的值 读取出来*/
//        for (int i = 0; i < buffer.capacity(); ++i) {
//            System.out.println(buffer.getByte(i));
//        }
        /*相对方法 readByte 会在内部 读取一个字节 readindex 自动+1 下次调用readByte读取下一个字节*/
        for (int i = 0; i < buffer.capacity(); ++i){
            System.out.println(buffer.readByte());
        }
    }
}