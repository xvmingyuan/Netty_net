package grpc;


import io.grpc.stub.StreamObserver;
import proto.*;

import java.util.ArrayList;
import java.util.UUID;

public class StudentServiceImpl extends StudentServiceGrpc.StudentServiceImplBase {
    @Override
    public void getRealNameByUsername(MyRequest request, StreamObserver<MyResponse> responseObserver) {
        System.out.println("Accept client information " + request.getUsername());
        MyResponse myResponse = MyResponse.newBuilder().setRealname("Java服务端张三").build();
        // 设置数据
        responseObserver.onNext(myResponse);
        // 标记完成
        responseObserver.onCompleted();
    }

    @Override
    public void getStudentsByAge(StudentRequest request, StreamObserver<StudentResponse> responseObserver) {
        System.out.println("接受到客户端信息 " + request.getAge());

        responseObserver.onNext(StudentResponse.newBuilder().setName("zhang").setAge(20).setCity("北京").build());
        responseObserver.onNext(StudentResponse.newBuilder().setName("李四").setAge(30).setCity("天津").build());
        responseObserver.onNext(StudentResponse.newBuilder().setName("王五").setAge(40).setCity("上海").build());
        responseObserver.onNext(StudentResponse.newBuilder().setName("赵柳").setAge(50).setCity("深圳").build());
        responseObserver.onNext(StudentResponse.newBuilder().setName("哈哈").setAge(60).setCity("杭州").build());
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<StudentRequest> getStudentWrapperByAges(StreamObserver<StudentResponseList> responseObserver) {
        return new StreamObserver<StudentRequest>() {
            @Override
            public void onNext(StudentRequest value) {
                System.out.println("onNext " + value.getAge());

            }

            @Override
            public void onError(Throwable t) {
                System.out.println(t.getMessage());

            }

            @Override
            public void onCompleted() {
                StudentResponse s1 = StudentResponse.newBuilder().setName("三").setAge(20).setCity("西安").build();
                StudentResponse s2 = StudentResponse.newBuilder().setName("四").setAge(30).setCity("成都").build();
                ArrayList<StudentResponse> st = new ArrayList<>();
                st.add(s1);
                st.add(s2);
                StudentResponseList studentResponseList = StudentResponseList.newBuilder().addAllStudentResponse(st).build();
                responseObserver.onNext(studentResponseList);
                responseObserver.onCompleted();

            }
        };
    }

    @Override
    public StreamObserver<StreamRequest> biTalk(StreamObserver<StreamResponse> responseObserver) {
        return new StreamObserver<StreamRequest>() {
            @Override
            public void onNext(StreamRequest value) {
                System.out.println(value.getRequestInfo());
                responseObserver.onNext(StreamResponse.newBuilder().setResponseInfo(UUID.randomUUID().toString()).build());
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(t.getMessage());
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}
