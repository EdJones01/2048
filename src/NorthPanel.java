import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class NorthPanel extends JPanel {
    private final JButton scoreLabel;
    private final JButton bestScoreLabel;
    private final JButton newButton;
    private Color foregroundColor = new Color(249, 245, 240);
    private Color backgroundColor = new Color(250, 248, 239);
    private Color buttonBackgroundColor = new Color(187, 173, 160);

    public NorthPanel(int panelWidth) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(panelWidth, 50));
        setBackground(backgroundColor);

        JPanel newPanel = new JPanel();
        newPanel.setBackground(backgroundColor);

        newButton = new JButton("New Game");
        newButton.setFont(Tools.getCustomFont(18));
        newButton.setForeground(foregroundColor);
        newButton.setRolloverEnabled(false);
        newButton.setFocusable(false);
        newButton.setBackground(buttonBackgroundColor.darker());
        newPanel.add(newButton);

        add(newPanel, BorderLayout.WEST);

        JPanel scorePanel = new JPanel();
        scorePanel.setBackground(backgroundColor);

        scoreLabel = new JButton("SCORE = 0");
        scoreLabel.setFont(Tools.getCustomFont(18));
        scoreLabel.setForeground(foregroundColor);
        scoreLabel.setBackground(buttonBackgroundColor);
        scoreLabel.setRolloverEnabled(false);
        scoreLabel.setFocusable(false);
        scorePanel.add(scoreLabel);

        bestScoreLabel = new JButton("BEST SCORE = 0");
        bestScoreLabel.setFont(Tools.getCustomFont(18));
        bestScoreLabel.setForeground(foregroundColor);
        bestScoreLabel.setBackground(buttonBackgroundColor);
        bestScoreLabel.setRolloverEnabled(false);
        bestScoreLabel.setFocusable(false);
        scorePanel.add(bestScoreLabel);

        add(scorePanel, BorderLayout.EAST);
    }

    public void setupActionListener(ActionListener e) {
        newButton.setActionCommand("new");
        newButton.addActionListener(e);
    }

    public void setScore(int score) {
        scoreLabel.setText("SCORE = " + score);
    }

    public void setBestScore(int score) {
        bestScoreLabel.setText("BEST SCORE = " + score);
    }

}
