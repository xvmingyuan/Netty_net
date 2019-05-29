package nio;

import java.nio.ByteBuffer;

/**
 * 按顺序提取数据
 */
public class NioDemo5 {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(64);


        buffer.putInt(11);
        buffer.putLong(12345768911123L);
        buffer.putShort((short) 12);
        buffer.putChar('你');
        buffer.putDouble(11.23);
        buffer.putFloat((float) 111.333);
        buffer.putChar('a');

        buffer.flip();

        System.out.println(buffer.getInt());
        System.out.println(buffer.getLong());
        System.out.println(buffer.getShort());
        System.out.println(buffer.getChar());
        System.out.println(buffer.getDouble());
        System.out.println(buffer.getFloat());
        System.out.println(buffer.getChar());

    }
}
