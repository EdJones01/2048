import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.*;

public class GamePanel extends JPanel implements KeyListener, ActionListener {
    private int score = 0;
    private int bestScore = 0;
    private int currentScore = 0;
    private Tile[][] tiles;
    private final int numOfCols = 4;
    private final int numOfRows = 4;
    private int pointer = 0;
    private final Random random = new Random();
    private Color[] tileColors;
    private Timer frameTick = new Timer(1000 / 60, this);
    private Tile[][] prevTiles;
    private final NorthPanel scorePanel;

    public GamePanel(NorthPanel scorePanel) {
        this.scorePanel = scorePanel;
        setFocusable(true);
        requestFocus();
        addKeyListener(this);
        setBackground(new Color(187, 173, 160));
        frameTick.setActionCommand("frame");
        frameTick.start();
        setup();
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2 = (Graphics2D) graphics;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int padding = getWidth() / 80;
        int tileWidth = getWidth() / numOfRows;
        int tileHeight = getHeight() / numOfCols;
        g2.setFont(Tools.getCustomFont((int) (tileWidth * 0.4)));

        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < numOfCols; j++) {
                int value = tiles[i][j].value;
                g2.setColor(findTileColor(value));
                g2.fillRoundRect(j * tileWidth + padding, i * tileHeight + padding,
                        tileWidth - 2 * padding, tileHeight - 2 * padding, padding, padding);
                if (value > 0) {
                    g2.setColor(findTextColor(value));
                    drawCenteredText(g2, value + "", (j * tileWidth) - (padding / 2), (i * tileHeight) - (padding / 2),
                            tileWidth, tileHeight);
                }
            }
        }

        if (isGameOver()) {
            g2.setColor(new Color(255, 255, 255, 180));
            g2.fillRect(0, 0, 800, 800);
            g2.setColor(new Color(119, 110, 101));
            drawCenteredText(g2, "Game Over!", 0, 0, getWidth(), getHeight() - 120);
            g2.fillRoundRect(getWidth() / 2 - 200, getHeight() / 2 + 40, 400, 70, 5, 5);
            g2.setColor(new Color(249, 245, 240));
            g2.setFont(Tools.getCustomFont(24));
            drawCenteredText(g2, "Press [ENTER] to try again.", getWidth() / 2 - 200, getHeight() / 2 + 35, 400, 70);
        }

    }

    private void drawCenteredText(Graphics2D g2, String text, int x, int y, int width, int height) {
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(text, x + width / 2 - fm.stringWidth(text) / 2,
                y + height / 2 + fm.getAscent() - fm.getHeight() / 2);
    }

    private boolean isGameOver() {
        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < numOfCols; j++) {
                if (tiles[i][j].value == 0) {
                    return false;
                }
                if (i > 0 && tiles[i][j].value == tiles[i - 1][j].value) {
                    return false;
                }
                if (j > 0 && tiles[i][j].value == tiles[i][j - 1].value) {
                    return false;
                }
            }
        }
        return true;
    }

    private Color findTileColor(int num) {
        if (num == 0)
            return tileColors[0];
        for (int i = 0; i < tileColors.length; i++) {
            if (Math.pow(2, i) == num) {
                return tileColors[i];
            }
        }
        return Color.BLACK;
    }

    private Color findTextColor(int num) {
        if (num > 7)
            return new Color(249, 245, 240);
        return new Color(119, 110, 101);
    }

    private void updatePrevTiles() {
        prevTiles = new Tile[numOfCols][numOfRows];
        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < numOfCols; j++) {
                prevTiles[i][j] = tiles[i][j].clone();
            }
        }
    }

    private boolean prevTilesAreSame() {
        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < numOfCols; j++) {
                if (prevTiles[i][j].value != tiles[i][j].value)
                    return false;
            }
        }
        return true;
    }

    private void setup() {
        tiles = new Tile[numOfCols][numOfRows];
        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < numOfCols; j++) {
                tiles[i][j] = new Tile(0);
            }
        }

        score = 0;
        currentScore = 0;
        addScore();

        updatePrevTiles();

        addStartingTiles();

        tileColors = new Color[]{new Color(215, 205, 197), new Color(238, 228, 218), new Color(237, 224, 200),
                new Color(242, 177, 121), new Color(245, 149, 99), new Color(246, 124, 96), new Color(246, 94, 59),
                new Color(237, 207, 115), new Color(237, 204, 98), new Color(237, 194, 45), new Color(237, 197, 63),
                new Color(237, 194, 45)};
    }

    private void addStartingTiles() {
        int numOfTiles = random.nextInt(2) + 1;
        for (int i = 0; i < numOfTiles; i++) {
            addNewTile();
        }
    }

    private void addNewTile() {
        LinkedList<Point> freeSpaces = new LinkedList<Point>();
        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < numOfCols; j++) {
                if (tiles[i][j].value == 0)
                    freeSpaces.add(new Point(i, j));
            }
        }
        if (freeSpaces.size() == 0)
            return;
        int index = random.nextInt(freeSpaces.size());
        Point freeSpace = freeSpaces.get(index);
        if (random.nextDouble() >= 0.1)
            tiles[freeSpace.x][freeSpace.y].value = 2;
        else
            tiles[freeSpace.x][freeSpace.y].value = 4;
    }

    private void addScore() {
        score += currentScore;
        scorePanel.setScore(score);
        if (bestScore < score) {
            bestScore = score;
            scorePanel.setBestScore(bestScore);
        }
        currentScore = 0;
    }

    private void moveNorth() {
        for (int col = 0; col < numOfCols; col++) {
            pointer = 0;
            for (int row = 0; row < numOfRows; row++) {
                if (tiles[row][col].value != 0) {
                    if (pointer <= row) {
                        shiftRowTiles(row, col, false);
                    }
                }
            }
        }
        addScore();
    }

    private void moveSouth() {
        for (int col = 0; col < numOfCols; col++) {
            pointer = numOfRows - 1;
            for (int row = numOfRows - 1; row >= 0; row--) {
                if (tiles[row][col].value != 0) {
                    if (pointer >= row) {
                        shiftRowTiles(row, col, true);
                    }
                }
            }
        }
        addScore();
    }

    private void moveWest() {
        for (int row = 0; row < numOfRows; row++) {
            pointer = 0;
            for (int col = 0; col < numOfCols; col++) {
                if (tiles[row][col].value != 0) {
                    if (pointer <= col) {
                        shiftColTiles(row, col, false);
                    }
                }
            }
        }
        addScore();
    }

    private void moveEast() {
        for (int row = 0; row < numOfRows; row++) {
            pointer = numOfCols - 1;
            for (int col = numOfCols - 1; col >= 0; col--) {
                if (tiles[row][col].value != 0) {
                    if (pointer >= col) {
                        shiftColTiles(row, col, true);
                    }
                }
            }
        }
        addScore();
    }

    private int combine(Tile tile, Tile into) {
        int scoreAddon = 0;
        if (into.value == tile.value)
            scoreAddon = tile.value * 2;
        into.value += tile.value;
        tile.value = 0;
        return scoreAddon;
    }

    private void shiftRowTiles(int currentRow, int currentCol, boolean forward) {
        if (tiles[pointer][currentCol].value == 0
                || tiles[pointer][currentCol].value == tiles[currentRow][currentCol].value) {
            if (currentRow > pointer || (forward && (pointer > currentRow))) {
                currentScore += combine(tiles[currentRow][currentCol], tiles[pointer][currentCol]);
            }
        } else {
            if (forward) {
                pointer--;
            } else {
                pointer++;
            }
            shiftRowTiles(currentRow, currentCol, forward);
        }
    }

    private void shiftColTiles(int currentRow, int currentCol, boolean reverse) {
        if (tiles[currentRow][pointer].value == 0
                || tiles[currentRow][pointer].value == tiles[currentRow][currentCol].value) {
            if (currentCol > pointer || (reverse && (pointer > currentCol))) {
                currentScore += combine(tiles[currentRow][currentCol], tiles[currentRow][pointer]);
            }
        } else {
            if (reverse) {
                pointer--;
            } else {
                pointer++;
            }
            shiftColTiles(currentRow, currentCol, reverse);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (!isGameOver()) {
            boolean moved = false;
            if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
                moveNorth();
                moved = true;
            }
            if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
                moveEast();
                moved = true;
            }
            if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
                moveSouth();
                moved = true;
            }
            if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
                moveWest();
                moved = true;
            }

            if (moved) {
                if (!prevTilesAreSame()) {
                    addNewTile();
                    updatePrevTiles();
                }
            }
        }
        if (key == KeyEvent.VK_ENTER)
            setup();

        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("new"))
            setup();
        repaint();
    }
}