package io.github.biezhi.wechat.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class QRCodeFrame extends JFrame {

    private static final long serialVersionUID = 8550014433017811556L;
    private JPanel contentPane;

    /**
     * Create the frame.
     */
    @SuppressWarnings("serial")
    public QRCodeFrame(final String filePath) {
        setBackground(Color.WHITE);
        this.setResizable(false);
        this.setTitle("\u8BF7\u7528\u624B\u673A\u626B\u63CF\u5FAE\u4FE1\u4E8C\u7EF4\u7801");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(100, 100, 297, 362);
        this.contentPane = new JPanel();
        contentPane.setBackground(new Color(102, 153, 255));
        this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel qrcodePanel = new JPanel() {
            public void paintComponent(Graphics g) {
                ImageIcon icon = new ImageIcon(filePath);
                // 图片随窗体大小而变化
                g.drawImage(icon.getImage(), 0, 0, 301, 301, this);
            }
        };
        qrcodePanel.setBounds(0, 0, 295, 295);

        JLabel tipLable = new JLabel("扫描二维码登录微信");
        tipLable.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        tipLable.setHorizontalAlignment(SwingConstants.CENTER);
        tipLable.setBounds(0, 297, 291, 37);

        contentPane.add(qrcodePanel);
        contentPane.add(tipLable);

        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
