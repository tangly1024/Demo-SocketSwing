package thread;

import msg.Message;
import view.ClientDemoFrame;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by tlyon on 2017/8/10 0010.
 */
public class SendThread implements Runnable {

    private TextAreaThread viewThread;
    private ClientDemoFrame cdf;

    class Task{
        String address;
        int port;
        String sendText;

        public Task(String text, String adress, int port) {
            this.sendText = text;
            this.address =adress;
            this.port = port;
        }
    }

    BlockingQueue<Task> taskPool = new ArrayBlockingQueue<Task>(1000);

    public SendThread( TextAreaThread viewThread, ClientDemoFrame clientDemoFrame) {
        this.viewThread = viewThread;
        this.cdf = clientDemoFrame;
    }

    public void addSendTask(String text,String adress,int port) {
        synchronized (taskPool){
            taskPool.add(new Task(text,adress,port));
        }
    }

    @Override
    public void run() {
        /**
         * 死循环 执行任务
         */
        while (true) {

            if(taskPool.isEmpty()){
                continue;
            }

            Task sendTask;
            try {
                sendTask = taskPool.take();
            } catch (InterruptedException e) {
                viewThread.addTextList(new Message(false,e.getMessage()));
                continue;
            }

            cdf.setProgressPercent(20);

            Socket s;
            try {
                s = new Socket(sendTask.address,sendTask.port);
            } catch (IOException e1) {
                viewThread.addTextList(new Message(false,"无法连接目标服务器与端口号 "+e1.getMessage()));
                continue;
            }
            viewThread.addTextList(new Message(true,"连接成功..." + sendTask.address + ":"+sendTask.port));
            cdf.setProgressPercent(40);


            DataOutputStream dos;
            try {
                dos = new DataOutputStream(s.getOutputStream());
            } catch (IOException e) {
                viewThread.addTextList(new Message(false,e.getMessage()));
                continue;
            }
            cdf.setProgressPercent(60);



            try {
                //写出字节码
//                String testSend = "SM@78@请0025号到78号窗口办理#";
                byte[] bytes = sendTask.sendText.getBytes("GB2312");
                cdf.setProgressPercent(80);
                dos.write(bytes);
                dos.flush();
                dos.close();
            } catch (IOException e) {
                viewThread.addTextList( new Message(false,e.getMessage()));
                continue;
            }
            cdf.setProgressPercent(100);
            viewThread.addTextList( new Message(true,"数据发送成功..."));

        }
    }
}
