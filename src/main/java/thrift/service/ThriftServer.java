package thrift.service;

import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import thrift.generated.PersonService;

public class ThriftServer {
    public static void main(String[] args) throws Exception {
        // 设置基本的socket传输层模式,(阻塞)
        TNonblockingServerSocket socket = new TNonblockingServerSocket(8899);
        // 设置服务器类型
        THsHaServer.Args arg = new THsHaServer.Args(socket).minWorkerThreads(2).maxWorkerThreads(4);
        // 自己的处理器
        PersonService.Processor<PersonServiceImpl> processor = new PersonService.Processor<>(new PersonServiceImpl());

        // 设置传输格式,使用压缩格式:TCompacProtocol
        arg.protocolFactory(new TCompactProtocol.Factory());
        // 设置数据传输方式,使用非阻塞式frame单位传输:TFramedTransport
        arg.transportFactory(new TFramedTransport.Factory());
        // 设置处理器
        arg.processorFactory(new TProcessorFactory(processor));

        // 启用服务类型,THsHa的线程池服务模型
        TServer server = new THsHaServer(arg);
        System.out.println("Thrift Server Started!");
        server.serve();
    }
}
