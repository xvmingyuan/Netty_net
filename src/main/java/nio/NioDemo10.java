package nio;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * 文件操作的共享锁和排他锁.
 * true 共享锁
 * false 排他锁
 * lock();
 * If <tt>shared</tt> is <tt>true</tt> this channel was not
 * opened for reading
 * If <tt>shared</tt> is <tt>false</tt> but this channel was not
 * opened for writing
 */
public class NioDemo10 {
    public static void main(String[] args) throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile("NioDemo10.txt", "rw");
        FileChannel fileChannel = randomAccessFile.getChannel();
        FileLock fileLock = fileChannel.lock(3, 6, true);

        System.out.println("valid:" + fileLock.isValid());
        System.out.println("lock type" + fileLock.isShared());

        fileLock.release();

        randomAccessFile.close();

    }
}
