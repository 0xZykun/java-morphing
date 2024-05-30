package cytech.morphing;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;

public class ShapeImageGenerator {

    private static final int IMAGE_WIDTH = 500;
    private static final int IMAGE_HEIGHT = 500;

    public static void main(String[] args) {
        try {
            generateSquareImage();
            generateCrossImage();
            generateHeartImage();
            generatePacmanImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void generateSquareImage() throws Exception {
        BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.GRAY);
        g2d.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);

        // Solid color for square
        g2d.setColor(Color.GREEN);

        int size = 300;  // Adjusted size for larger square
        int x = (IMAGE_WIDTH - size) / 2;
        int y = (IMAGE_HEIGHT - size) / 2;

        g2d.fillRect(x, y, size, size);

        File outputfile = new File("square.png");
        ImageIO.write(image, "png", outputfile);

        g2d.dispose();

        List<Point> points = new ArrayList<>(Arrays.asList(
            new Point(x, y),
            new Point(x + size, y),
            new Point(x + size, y + size),
            new Point(x, y + size)
        ));
        System.out.println("Square coordinates: " + points);
    }

    private static void generateCrossImage() throws Exception {
        BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background color
        g2d.setColor(Color.BLUE);
        g2d.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);

        // Solid color for cross
        g2d.setColor(Color.RED);

        int size = 300;  // Adjusted size for larger cross
        int thickness = 50;  // Adjusted thickness for larger cross
        int x = (IMAGE_WIDTH - size) / 2;
        int y = (IMAGE_HEIGHT - size) / 2;

        g2d.fillRect(x, y + (size / 2) - (thickness / 2), size, thickness);
        g2d.fillRect(x + (size / 2) - (thickness / 2), y, thickness, size);

        File outputfile = new File("cross.png");
        ImageIO.write(image, "png", outputfile);

        g2d.dispose();

        List<Point> points = new ArrayList<>(Arrays.asList(
            new Point(x, y + (size / 2) - (thickness / 2)),  // Top-left of horizontal
            new Point(x + (size / 2) - (thickness / 2), y),  // Top-left of vertical
            new Point(x + (size / 2) + (thickness / 2), y),  // Top-right of vertical
            new Point(x + size, y + (size / 2) - (thickness / 2)),  // Top-right of horizontal
            new Point(x + size, y + (size / 2) + (thickness / 2)),  // Bottom-right of horizontal
            new Point(x + (size / 2) + (thickness / 2), y + size),  // Bottom-right of vertical
            new Point(x + (size / 2) - (thickness / 2), y + size),  // Bottom-left of vertical
            new Point(x, y + (size / 2) + (thickness / 2)),  // Bottom-left of horizontal
            new Point(x + (size / 2) - (thickness / 2), y + (size / 2) - (thickness / 2)),  // Inner top-left
            new Point(x + (size / 2) + (thickness / 2), y + (size / 2) - (thickness / 2)),  // Inner top-right
            new Point(x + (size / 2) + (thickness / 2), y + (size / 2) + (thickness / 2)),  // Inner bottom-right
            new Point(x + (size / 2) - (thickness / 2), y + (size / 2) + (thickness / 2))   // Inner bottom-left
        ));
        System.out.println("Cross coordinates: " + points);
    }

    private static void generateHeartImage() throws Exception {
        BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        g2d.setColor(Color.BLACK);

        int width = 300;  // Adjusted size for larger heart
        int height = 300;
        int x = (IMAGE_WIDTH - width) / 2;
        int y = (IMAGE_HEIGHT - height) / 2;

        Path2D.Double heart = new Path2D.Double();
        heart.moveTo(x + width / 2, y + height / 4);
        heart.curveTo(x + width / 2, y, x, y, x, y + height / 4);
        heart.curveTo(x, y + height / 2, x + width / 2, y + 3 * height / 4, x + width / 2, y + height);
        heart.curveTo(x + width / 2, y + 3 * height / 4, x + width, y + height / 2, x + width, y + height / 4);
        heart.curveTo(x + width, y, x + width / 2, y, x + width / 2, y + height / 4);
        heart.closePath();

        g2d.fill(heart);

        File outputfile = new File("heart.png");
        ImageIO.write(image, "png", outputfile);

        g2d.dispose();
    }

    private static void generatePacmanImage() throws Exception {
        BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        g2d.setColor(Color.BLACK);

        int size = 300;  // Adjusted size for larger Pacman
        int x = (IMAGE_WIDTH - size) / 2;
        int y = (IMAGE_HEIGHT - size) / 2;

        g2d.fillArc(x, y, size, size, 30, 300);

        File outputfile = new File("pacman.png");
        ImageIO.write(image, "png", outputfile);

        g2d.dispose();
    }
}