package BrickBreaker;

import java.awt.*;

public class MapGenerator {
    final int[][] bricks; // Grid of bricks (1 = visible, 0 = destroyed)
    final int brickWidth;
    final int brickHeight;

    // Padding and margins
    private static final int HORIZONTAL_PADDING = 80;
    private static final int VERTICAL_PADDING = 50;
    private static final Color BRICK_COLOR = Color.DARK_GRAY;
    private static final Color BORDER_COLOR = Color.BLACK;

    /**
     * Constructor to initialize the brick map.
     *
     * @param rows Number of rows of bricks
     * @param cols Number of columns of bricks
     */
    public MapGenerator(int rows, int cols) {
        bricks = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                bricks[i][j] = 1; // 1 indicates a visible brick
            }
        }

        // Calculate brick dimensions
        brickWidth = 540 / cols;
        brickHeight = 150 / rows;
    }

    /**
     * Draws the bricks on the screen.
     *
     * @param g Graphics2D object to draw the bricks
     */
    public void draw(Graphics2D g) {
        for (int row = 0; row < bricks.length; row++) {
            for (int col = 0; col < bricks[0].length; col++) {
                if (bricks[row][col] > 0) { // If the brick is visible
                    // Draw the brick
                    g.setColor(BRICK_COLOR);
                    int x = col * brickWidth + HORIZONTAL_PADDING;
                    int y = row * brickHeight + VERTICAL_PADDING;
                    g.fillRect(x, y, brickWidth, brickHeight);

                    // Draw a border for better visibility
                    g.setColor(BORDER_COLOR);
                    g.setStroke(new BasicStroke(3));
                    g.drawRect(x, y, brickWidth, brickHeight);
                }
            }
        }
    }

    public void setBrickValue(int value, int row,int col){
        bricks[row][col] = value;
    }
}
