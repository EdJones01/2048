import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame{
    public MainFrame() {
        setTitle("2048");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        NorthPanel scorePanel = new NorthPanel(600);
        GamePanel gamePanel = new GamePanel(scorePanel);
        scorePanel.setupActionListener(gamePanel);
        gamePanel.setPreferredSize(new Dimension(600, 600));

        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());

        container.add(gamePanel, BorderLayout.CENTER);
        container.add(scorePanel, BorderLayout.NORTH);
        setContentPane(container);

            pack();
        setLocationRelativeTo(null);
    }
}
