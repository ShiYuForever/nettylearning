import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @Description
 * @Author ShiYuForever
 * @Date 2021-11-22 16:08
 */
public class Demo01 {

    public static void main(String[] args) throws IOException {
        Thread.currentThread().setName("listen");
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        Selector listenSelector = Selector.open();
        ssc.register(listenSelector,SelectionKey.OP_ACCEPT,null);
        ssc.bind(new InetSocketAddress(8081));
        while(true){
            listenSelector.select();
            Iterator<SelectionKey> iterator = listenSelector.selectedKeys().iterator();
            while(iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();
                if(key.isAcceptable()){
                    SocketChannel socketChannel = ssc.accept();
                    socketChannel.register(listenSelector,SelectionKey.OP_READ,null);
                    socketChannel.configureBlocking(false);
                } else if(key.isReadable()){
                    ByteBuffer buffer = ByteBuffer.allocate(16);
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    socketChannel.read(buffer);
                    buffer.flip();
                    System.out.println(buffer.toString());
                }
            }
        }
    }
}
