package msg;

import view.ServerDemoFrame;

/**
 * 页面与控制器内部通讯的消息格式
 * USER：tangly
 * DATE：2017/8/9
 * TIME：18:13
 */
public class Message implements CallBack{

    private boolean success; //方法执行结果
    private Object result; //方法执行描述


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Message(boolean success, Object result) {
        this.success = success;
        this.result = result;
    }

    @Override
    public void done(ServerDemoFrame cd) {
        synchronized (cd){
            cd.showLog(this);
        }
    }
}
