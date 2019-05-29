package netty.sixthDemo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Random;

public class TestClientHandler extends SimpleChannelInboundHandler<Message.MyMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message.MyMessage msg) throws Exception {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        int randomi = new Random().nextInt(3);
        Message.MyMessage msg = null;
        if (0 == randomi) {
            msg = Message.MyMessage.newBuilder()
                    .setDataType(Message.MyMessage.DataType.PersonType)
                    .setPerson(
                            Message.Person.newBuilder()
                                    .setName("张珊").setAge(22).setAddress("北京").build()
                    ).build();
        } else if (1 == randomi) {
            msg = Message.MyMessage.newBuilder()
                    .setDataType(Message.MyMessage.DataType.DogType)
                    .setDog(
                            Message.Dog.newBuilder()
                                    .setName("狗").setSex("女").build()
                    ).build();
        } else {
            msg = Message.MyMessage.newBuilder()
                    .setDataType(Message.MyMessage.DataType.CatType)
                    .setCat(
                            Message.Cat.newBuilder()
                                    .setName("猫").setAge("男").build()
                    ).build();

        }

        ctx.channel().writeAndFlush(msg);

    }
}
