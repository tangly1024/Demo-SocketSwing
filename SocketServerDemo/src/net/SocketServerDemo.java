package net;

import msg.Message;
import thread.ClientThread;
import view.ServerDemoFrame;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by tlyon on 2017/8/10 0010.
 */
public class SocketServerDemo implements Runnable {

    private ServerSocket ss;

    private ServerDemoFrame sdf;

    private volatile boolean start = false;

    public SocketServerDemo(ServerDemoFrame serverDemoFrame) {
        sdf = serverDemoFrame;
    }

    public boolean isStart() {
        return start;
    }

    public Message initServer(int port) {
        try {
            ss = new ServerSocket(port);
            start = true;
            return new Message(true, "服务器启动成功 地址 : " + InetAddress.getLocalHost().getHostAddress() + " : " + port);
        } catch (Exception e) {
            start = false;
            return new Message(false, "服务器启动失败,端口 "+ port +" 被占用 " + e.getMessage());
        }
    }

    public Message closeServer() {
        if (ss != null) {
            try {
                start = false; //将服务器状态标志为关闭
                ss.close();
            } catch (IOException e) {

            }
        }
        return new Message(true, "服务器已关闭");
    }


    public Message startAcceptThread() {
        try {
            Thread accepThread = new Thread(this);
            accepThread.start();
            return new Message(true, "接收线程启动成功");
        } catch (Exception e) {
            return new Message(false, e.getMessage());
        }

    }

    @Override
    public void run() {
        while (isStart()) {
            try {
                Socket s = ss.accept();
                new Thread(new ClientThread(s, sdf)).start();
            } catch (IOException e) {
                new Message(false, "接收客户端连接请求异常 " + e.getMessage()).done(sdf);
            }
        }
    }
}
