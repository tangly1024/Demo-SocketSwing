package thread;

import msg.Message;
import view.ServerDemoFrame;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * 为每个客户端分配一个线程
 * Created by tlyon on 2017/8/10 0010.
 */
public class ClientThread implements Runnable {

    private final Socket s;
    private final ServerDemoFrame sdf;

    public ClientThread(Socket s, ServerDemoFrame sdf) {
        this.s = s;
        this.sdf = sdf;
    }

    @Override
    public void run() {

//        new Message(true,s).done(sdf);

        DataInputStream dis;
        try {
            dis = new DataInputStream(s.getInputStream());
        } catch (IOException e) {
            new Message(false, e).done(sdf);
            return;
        }

        byte[] bytes = new byte[64];//字节缓存

        ByteArrayOutputStream baos;
        int len;

        try {
            baos = new ByteArrayOutputStream();

            while ((len = dis.read(bytes)) != -1) {
                baos.write(bytes, 0, len);
            }

            dis.close();
            baos.close();

            String receiveMsg = new String(baos.toByteArray(), "GB2312"); //转为字符串
            String converToresult = convert(receiveMsg);

            if (null == receiveMsg || "".equals(receiveMsg)) {
                receiveMsg = " 空";
            }

            new Message(true, " [ 收 到 信 息 ] " + receiveMsg).done(sdf);
            new Message(true, " [ 解 析 结 果 ] " + converToresult).done(sdf);
        } catch (IOException e) {
            new Message(false, " 读取输入信息异常:" + e).done(sdf);
        }
    }

    private static String convert(String receiveMsg) {
        try{
            int firstFlag; //第一个@符号的位置
            int secondFlag;//第二个@符号的位置
            int finalFlag; //最后一个#符号的位置

            String messageType = "-";//消息类型
            String windowNumber = "-"; //窗口号
            String content = "-"; //消息内容

            String finalResult = ""; // 最终拼接结果

            //SM@78@请0025号到78号窗口办理#
            if (null == receiveMsg || "".equals(receiveMsg)) {
                return "格式不正确，无法解析";
            }


            firstFlag = receiveMsg.indexOf("@");
            messageType = receiveMsg.substring(0, firstFlag);

            receiveMsg = receiveMsg.substring(firstFlag+1);
            secondFlag = receiveMsg.indexOf("@");
            windowNumber = receiveMsg.substring(0,secondFlag);

            receiveMsg = receiveMsg.substring(secondFlag+1);
            finalFlag = receiveMsg.indexOf("#");

            content = receiveMsg.substring(0, finalFlag);

            if("SM".equals(messageType)){
                return windowNumber + "号窗口显示\""+content+"\"";
            }else if("CQ".equals(messageType)){
                return "语音播报: 请"+content + "到"+windowNumber+"号窗口办理";
            }else{
                return "消息格式不符合要求: "+ messageType + " " + windowNumber + " " + content;
            }
        }catch (Exception e){
            return "消息格式不符合要求 " + e.getMessage();
        }

    }

    public static void main(String[] args) {
        String test = "SM@78@暂时离开#";
        convert(test);
    }
}
