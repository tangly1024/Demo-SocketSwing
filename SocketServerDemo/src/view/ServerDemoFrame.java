/*
 * Created by JFormDesigner on Thu Aug 10 20:44:54 CST 2017
 */

package view;

import msg.Message;
import net.SocketServerDemo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.InetAddress;

/**
 * @author tlyon
 */
public class ServerDemoFrame extends JFrame {

    SocketServerDemo ss;

    public ServerDemoFrame() {
        initComponents();
        //填写本机IP地址
        try {
            tf_ip.setText(InetAddress.getLocalHost().getHostAddress());
        } catch (Exception e) {
        }

    }

    /**
     * 显示信息
     *
     * @param msg
     */
    public void showLog(Message msg) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (!msg.isSuccess()) {
                    ta_result.setText(ta_result.getText() + "\n" + " [ 错 误 ] " + msg.getResult());
                } else {
                    ta_result.setText(ta_result.getText() + "\n" + " [ 正 常 ] " + msg.getResult());
                }
            }
        });
    }

    /**
     * 开启服务器
     *
     * @param e
     */
    private void btn_startActionPerformed(ActionEvent e) {

        Integer port;
        try {
            port = Integer.valueOf(tf_port.getText());
        } catch (Exception e1) {
            new Message(false, "检查端口号格式").done(this);
            return;
        }

        ss = new SocketServerDemo(this);

        Message initServerMessage = ss.initServer(port);
        initServerMessage.done(this);

        if (!initServerMessage.isSuccess()) {
            //启动失败就不执行后续步骤
            return;
        }

        tf_port.setEnabled(false);
        btn_start.setEnabled(false);
        btn_shutdown.setEnabled(true);

        ss.startAcceptThread();//启动服务器的接收线程，接受客户端消息

    }

    /**
     * 关闭服务器
     *
     * @param e
     */
    private void btn_shutdownActionPerformed(ActionEvent e) {
        if (ss.isStart()) {
            ss.closeServer().done(this);
            tf_port.setEnabled(true);
            btn_start.setEnabled(true);
            btn_shutdown.setEnabled(false);
        }
    }


    /**
     * 初始化组件
     */
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        dialogPane = new JPanel();
        label2 = new JLabel();
        tf_ip = new JTextField();
        scrollPane1 = new JScrollPane();
        ta_result = new JTextArea();
        btn_start = new JButton();
        btn_shutdown = new JButton();
        label4 = new JLabel();
        tf_port = new JTextField();

        //======== this ========
        setTitle("\u670d\u52a1\u5668\u7aef");
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setPreferredSize(new Dimension(535, 355));
            dialogPane.setMinimumSize(new Dimension(535, 355));
            dialogPane.setLayout(null);

            //---- label2 ----
            label2.setText("\u672c\u673a\u670d\u52a1\u5668\u5730\u5740:");
            dialogPane.add(label2);
            label2.setBounds(new Rectangle(new Point(20, 20), label2.getPreferredSize()));

            //---- tf_ip ----
            tf_ip.setText("127.0.0.1");
            tf_ip.setEditable(false);
            dialogPane.add(tf_ip);
            tf_ip.setBounds(120, 15, 85, tf_ip.getPreferredSize().height);

            //======== scrollPane1 ========
            {

                //---- ta_result ----
                ta_result.setBorder(new TitledBorder("\u7ed3\u679c:"));
                ta_result.setText("\u670d\u52a1\u5668\u5c1a\u672a\u542f\u52a8...");
                scrollPane1.setViewportView(ta_result);
            }
            dialogPane.add(scrollPane1);
            scrollPane1.setBounds(15, 55, 505, 235);

            //---- btn_start ----
            btn_start.setText("\u542f\u52a8\u670d\u52a1\u5668");
            btn_start.addActionListener(e -> btn_startActionPerformed(e));
            dialogPane.add(btn_start);
            btn_start.setBounds(350, 10, 115, btn_start.getPreferredSize().height);

            //---- btn_shutdown ----
            btn_shutdown.setText("\u505c\u6b62");
            btn_shutdown.setEnabled(false);
            btn_shutdown.addActionListener(e -> btn_shutdownActionPerformed(e));
            dialogPane.add(btn_shutdown);
            btn_shutdown.setBounds(new Rectangle(new Point(470, 10), btn_shutdown.getPreferredSize()));

            //---- label4 ----
            label4.setText("\u7aef\u53e3\u53f7:");
            dialogPane.add(label4);
            label4.setBounds(new Rectangle(new Point(225, 20), label4.getPreferredSize()));

            //---- tf_port ----
            tf_port.setText("8080");
            dialogPane.add(tf_port);
            tf_port.setBounds(275, 15, 60, tf_port.getPreferredSize().height);

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
    private JLabel label2;
    private JTextField tf_ip;
    private JScrollPane scrollPane1;
    private JTextArea ta_result;
    private JButton btn_start;
    private JButton btn_shutdown;
    private JLabel label4;
    private JTextField tf_port;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
