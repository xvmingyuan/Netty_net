package protobuf;

import com.google.protobuf.InvalidProtocolBufferException;

public class ProtoBufTest {
    public static void main(String[] args) throws InvalidProtocolBufferException {
        // 构建对象
        DateInfo.Student student = DateInfo.Student.newBuilder().setName("张三").setAge(20).setAddress("北京").build();
        // 对象转化字节数组
        byte[] student2ByteArray = student.toByteArray();

        //字节数组转换为对象
        DateInfo.Student student1 = DateInfo.Student.parseFrom(student2ByteArray);
        System.out.println(student1.getName());
        System.out.println(student1.getAge());
        System.out.println(student1.getAddress());


    }
}
