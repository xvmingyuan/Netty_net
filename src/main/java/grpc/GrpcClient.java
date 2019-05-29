package grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import proto.*;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class GrpcClient {
    private ManagedChannel channel;
    private StudentServiceGrpc.StudentServiceBlockingStub blockingStub;
    private StudentServiceGrpc.StudentServiceStub stub;

    // 同步形式
    public GrpcClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext(), null);
    }

    // 可选异步
    public GrpcClient(String host, int port, Integer type) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext(), type);
    }

    public GrpcClient(ManagedChannelBuilder<?> channelBuilder, Integer type) {
        channel = channelBuilder.build();
        // 线程类型
        if (type == null || type == 1) {
            blockingStub = StudentServiceGrpc.newBlockingStub(channel); //阻塞
        } else if (type == 2) {
            stub = StudentServiceGrpc.newStub(channel); //非阻塞 (异步)
        }
    }

    private void newStub() {
        stub = StudentServiceGrpc.newStub(channel); //非阻塞 (异步)
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(10, TimeUnit.SECONDS);
    }

    public void setName(String name) {
        // 1.请求提交对象数据,返回对象数据
        MyResponse myResponse = blockingStub
                .getRealNameByUsername(MyRequest.newBuilder().setUsername(name).build());
        System.out.println(myResponse.getRealname());
        System.out.println("----------------");

    }

    public void setAge(int age) {
        // 2.请求提交对象数据,返回流式数据
        Iterator<StudentResponse> iterator = blockingStub.getStudentsByAge(StudentRequest.newBuilder().setAge(age).build());
        while (iterator.hasNext()) {
            StudentResponse studentResponse = iterator.next();
            System.out.println(studentResponse.getName() + "," + studentResponse.getAge() + "," + studentResponse.getCity());
        }
        System.out.println("----------------");

    }

    public void setAgeByStreamWrapper(int age) throws InterruptedException {
        // 3.请求提交流数据,返回对象数据
        StreamObserver<StudentResponseList> streamObserver = new StreamObserver<StudentResponseList>() {
            @Override
            public void onNext(StudentResponseList value) {
                value.getStudentResponseList().forEach(studentResponse -> {
                    System.out.println(studentResponse.getName() + "," + studentResponse.getAge() + "," + studentResponse.getCity());
                    System.out.println("*********");
                });

            }

            @Override
            public void onError(Throwable t) {
                System.out.println(t.getMessage());

            }

            @Override
            public void onCompleted() {
                System.out.println("completed !");

            }
        };
        StreamObserver<StudentRequest> studentWrapperByAges = stub.getStudentWrapperByAges(streamObserver);
        studentWrapperByAges.onNext(StudentRequest.newBuilder().setAge(20).build());
        studentWrapperByAges.onNext(StudentRequest.newBuilder().setAge(30).build());
        studentWrapperByAges.onNext(StudentRequest.newBuilder().setAge(40).build());
        studentWrapperByAges.onNext(StudentRequest.newBuilder().setAge(50).build());
        studentWrapperByAges.onCompleted();
        Thread.sleep(10000);
    }

    public void BiTalkByStream() throws InterruptedException {
        // 4.请求提交流数据,返回流数据
        StreamObserver<StreamRequest> requestStreamObserver = stub.biTalk(new StreamObserver<StreamResponse>() {
            @Override
            public void onNext(StreamResponse value) {
                System.out.println(value.getResponseInfo());

            }

            @Override
            public void onError(Throwable t) {
                System.out.println(t.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("onCompleted");
            }
        });

        for (int i = 0; i < 10; i++) {
            requestStreamObserver.onNext(StreamRequest.newBuilder().build().newBuilder().setRequestInfo(LocalDateTime.now().toString()).build());
            Thread.sleep(1000);
        }
        Thread.sleep(10000);

    }

    public static void main(String[] args) throws Exception {
        // 同步
//        GrpcClient client = new GrpcClient("127.0.0.1", 8899);
//        client.setName("客户端张三");
//        client.setAge(22);
//        client.shutdown();
        // 异步
        GrpcClient client1 = new GrpcClient("127.0.0.1", 8899, 2);
//        client1.setAgeByStreamWrapper(23);
        client1.BiTalkByStream();
        client1.shutdown();
    }

}
