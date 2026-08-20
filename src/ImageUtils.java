import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageUtils {

    public static Image load(String filename) {
        try {
            BufferedImage img = ImageIO.read(new File(filename));

            // Couldn't read image
            if (img == null) {
                return null;
            }

            int height = img.getHeight();
            int width = img.getWidth();

            // Create pixel matrix
            Pixel[][] pixels = new Pixel[height][width];

            // Loop over BufferedImage
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {

                    int packed = img.getRGB(col, row);

                    int r = (packed >> 16) & 0xFF;
                    int g = (packed >> 8) & 0xFF;
                    int b = packed & 0xFF;

                    pixels[row][col] = new Pixel(r, g, b);
                }
            }

            return new Image(pixels);

        } catch (IOException e) {
            System.out.println("Couldn't open image at: " + filename);
            return null;
        }
    }

    public static void save(Image image, String filename) {

        int height = image.getHeight();
        int width = image.getWidth();

        BufferedImage img =
            new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {

                Pixel p = image.getPixel(row, col);

                int r = p.getR();
                int g = p.getG();
                int b = p.getB();

                int packed = (r << 16) | (g << 8) | b;

                img.setRGB(col, row, packed);
            }
        }

        try {
            File file = new File(filename);

            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }

            String format = "png";
            int dot = filename.lastIndexOf('.');

            if (dot != -1 && dot < filename.length() - 1) {
                format = filename.substring(dot + 1).toLowerCase();
            }

            ImageIO.write(img, format, file);

            System.out.println(
                "Saved: " + filename +
                " (" + width + "x" + height + ")"
            );

        } catch (IOException e) {
            System.out.println(
                "ERROR: could not save " + filename + ": " + e.getMessage()
            );
        }
    }
}