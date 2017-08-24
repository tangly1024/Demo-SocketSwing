package view;

import msg.Message;
import thread.SendThread;
import thread.TextAreaThread;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
/*
 * Created by JFormDesigner on Wed Aug 09 17:43:47 CST 2017
 */


/**
 * @author tang
 */
public class ClientDemoFrame extends JFrame {

    SendThread sendThread;
    TextAreaThread viewThread;

    /**
     * 构造函数
     */
    public ClientDemoFrame() {
        initComponents();
        mubanBox.addItem("SM@78@请0025号到78号窗口办理#");
        mubanBox.addItem("SM@78@欢迎光临#");
        mubanBox.addItem("CQ@90@12@张三@1#");
    }

    /**
     * 点击清除按钮
     *
     * @param e
     */
    synchronized private void btn_clearActionPerformed(ActionEvent e) {
        tf_send.setText("");
    }

    /**
     * 点击发送按钮
     *
     * @param e
     */
    synchronized private void btn_sendActionPerformed(ActionEvent e) {

        setProgressPercent(0);

        String address = tf_ip.getText();
        if (null == address || "".equals(address)) {
            showLog(new Message(false, "IP地址未填写"));
            return;
        }

        Integer port;
        try {
            port = Integer.valueOf(tf_port.getText());
        } catch (Exception e1) {
            showLog(new Message(false, "请检查端口号格式"));
            return;
        }


        try {

            if (viewThread == null) {
                viewThread = new TextAreaThread(this);
                new Thread(viewThread).start();
            }

            if (sendThread == null) {
                sendThread = new SendThread(viewThread, this);
                new Thread(sendThread).start();
            }

            sendThread.addSendTask(tf_send.getText(), address, port);

        } catch (Exception e2) {
            showLog(new Message(false,e2.getMessage()));
        }

    }

    synchronized public void setProgressPercent(final int cent) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                progressBar1.setValue(cent);
                progressPercent.setText(cent + " %");
            }
        });
    }

    /**
     * 重置结果按键
     *
     * @param e
     */
    synchronized private void button1ActionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ta_result.setText("");
                setProgressPercent(0);
            }
        });
    }

    /**
     * 复制模板按键
     *
     * @param e
     */
    private void button2ActionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                tf_send.setText(mubanBox.getSelectedItem().toString());
            }
        });
    }


    synchronized public void showLog(Message msg) {
        SwingUtilities.invokeLater(new Runnable() { //视图的更新要使用Swing的工具类 否则卡死
            @Override
            public void run() {
                if (!msg.isSuccess()) {
                    ta_result.setText(ta_result.getText() + " [ 错 误 ] " + msg.getResult() + "\n");
                } else {
                    ta_result.setText(ta_result.getText() + " [ 正 常 ] " + msg.getResult() + "\n");
                }
            }
        });
    }


    private void comboBox1ActionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ta_result.setText(mubanBox.getSelectedItem().toString());
            }
        });
    }


    /**
     * 初始化样式组件
     */
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        dialogPane = new JPanel();
        label1 = new JLabel();
        tf_send = new JTextField();
        label2 = new JLabel();
        tf_ip = new JTextField();
        btn_send = new JButton();
        label3 = new JLabel();
        scrollPane1 = new JScrollPane();
        ta_result = new JTextArea();
        btn_clear = new JButton();
        button1 = new JButton();
        button2 = new JButton();
        label4 = new JLabel();
        tf_port = new JTextField();
        progressBar1 = new JProgressBar();
        mubanBox = new JComboBox();
        progressPercent = new JLabel();

        //======== this ========
        setTitle("\u5ba2\u6237\u7aefDemo");
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setPreferredSize(new Dimension(600, 355));
            dialogPane.setMinimumSize(new Dimension(600, 355));
            dialogPane.setLayout(null);

            //---- label1 ----
            label1.setText("\u5185\u5bb9:");
            dialogPane.add(label1);
            label1.setBounds(new Rectangle(new Point(170, 20), label1.getPreferredSize()));
            dialogPane.add(tf_send);
            tf_send.setBounds(200, 15, 270, 30);

            //---- label2 ----
            label2.setText("IP\u5730\u5740:");
            dialogPane.add(label2);
            label2.setBounds(new Rectangle(new Point(15, 20), label2.getPreferredSize()));

            //---- tf_ip ----
            tf_ip.setText("127.0.0.1");
            dialogPane.add(tf_ip);
            tf_ip.setBounds(55, 15, 105, 30);

            //---- btn_send ----
            btn_send.setText("\u53d1\u9001");
            btn_send.addActionListener(e -> btn_sendActionPerformed(e));
            dialogPane.add(btn_send);
            btn_send.setBounds(480, 10, 60, btn_send.getPreferredSize().height);

            //---- label3 ----
            label3.setText("\u6a21\u677f: ");
            dialogPane.add(label3);
            label3.setBounds(new Rectangle(new Point(170, 60), label3.getPreferredSize()));

            //======== scrollPane1 ========
            {

                //---- ta_result ----
                ta_result.setBorder(new TitledBorder("\u7ed3\u679c:"));
                scrollPane1.setViewportView(ta_result);
            }
            dialogPane.add(scrollPane1);
            scrollPane1.setBounds(15, 100, 480, 180);

            //---- btn_clear ----
            btn_clear.setText("\u6e05\u7a7a");
            btn_clear.addActionListener(e -> btn_clearActionPerformed(e));
            dialogPane.add(btn_clear);
            btn_clear.setBounds(new Rectangle(new Point(540, 10), btn_clear.getPreferredSize()));

            //---- button1 ----
            button1.setText("\u6e05\u9664\u7ed3\u679c");
            button1.addActionListener(e -> button1ActionPerformed(e));
            dialogPane.add(button1);
            button1.setBounds(new Rectangle(new Point(505, 250), button1.getPreferredSize()));

            //---- button2 ----
            button2.setText("\u4f7f\u7528\u6a21\u677f");
            button2.addActionListener(e -> button2ActionPerformed(e));
            dialogPane.add(button2);
            button2.setBounds(new Rectangle(new Point(485, 55), button2.getPreferredSize()));

            //---- label4 ----
            label4.setText("\u7aef\u53e3\u53f7:");
            dialogPane.add(label4);
            label4.setBounds(new Rectangle(new Point(15, 60), label4.getPreferredSize()));

            //---- tf_port ----
            tf_port.setText("8080");
            tf_port.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
            dialogPane.add(tf_port);
            tf_port.setBounds(55, 55, 105, 30);

            //---- progressBar1 ----
            progressBar1.setEnabled(false);
            dialogPane.add(progressBar1);
            progressBar1.setBounds(15, 285, 510, 25);
            dialogPane.add(mubanBox);
            mubanBox.setBounds(200, 55, 270, 27);

            //---- progressPercent ----
            progressPercent.setText("0 %");
            dialogPane.add(progressPercent);
            progressPercent.setBounds(540, 290, 45, progressPercent.getPreferredSize().height);

            { // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < dialogPane.getComponentCount(); i++) {
                    Rectangle bounds = dialogPane.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = dialogPane.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                dialogPane.setMinimumSize(preferredSize);
                dialogPane.setPreferredSize(preferredSize);
            }
        }
        contentPane.add(dialogPane);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel dialogPane;
    private JLabel label1;
    private JTextField tf_send;
    private JLabel label2;
    private JTextField tf_ip;
    private JButton btn_send;
    private JLabel label3;
    private JScrollPane scrollPane1;
    private JTextArea ta_result;
    private JButton btn_clear;
    private JButton button1;
    private JButton button2;
    private JLabel label4;
    private JTextField tf_port;
    private JProgressBar progressBar1;
    private JComboBox mubanBox;
    private JLabel progressPercent;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
