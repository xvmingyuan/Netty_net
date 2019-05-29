package netty.sixthDemo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class TestServerHandler extends SimpleChannelInboundHandler<Message.MyMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message.MyMessage msg) throws Exception {
        Message.MyMessage.DataType dataType = msg.getDataType();

        if (dataType == Message.MyMessage.DataType.PersonType) {
            Message.Person person = msg.getPerson();
            System.out.println(person.getName());
            System.out.println(person.getAge());
            System.out.println(person.getAddress());

        } else if (dataType == Message.MyMessage.DataType.DogType) {
            Message.Dog dog = msg.getDog();
            System.out.println(dog.getName());
            System.out.println(dog.getSex());

        } else {
            Message.Cat cat = msg.getCat();
            System.out.println(cat.getName());
            System.out.println(cat.getAge());

        }
    }
}
