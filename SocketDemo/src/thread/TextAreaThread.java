package thread;

import msg.Message;
import view.ClientDemoFrame;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by tlyon on 2017/8/10 0010.
 */
public class TextAreaThread implements Runnable {

    ClientDemoFrame cdf;

    BlockingQueue<Message> taskPool = new ArrayBlockingQueue<>(1000);

    public TextAreaThread(ClientDemoFrame clientDemoFrame) {
        this.cdf = clientDemoFrame;
    }

    @Override
    public void run() {
        while (true){
            if(taskPool.isEmpty()){
                continue;
            }
            Message msg;
            try {
                msg = taskPool.take();
            } catch (InterruptedException e) {
                cdf.showLog(new Message(false,"信息显示进程异常 : " + e.getMessage()));
                continue;
            }

            cdf.showLog(msg);

        }

    }

    public void addTextList(Message message) {
        taskPool.add(message);
    }
}
