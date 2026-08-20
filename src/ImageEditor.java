public class ImageEditor {
    private Image og;

    public ImageEditor(Image og) {
        this.og = og;
    }

    /**
    * Negative: every color value is replaced by its opposite.
    * A very dark pixel (0) becomes very bright (255), and the other way around.
    *
    * The pattern is always the same:
    *   1. loop over every row
    *   2. loop over every column
    *   3. do something with the three values of that pixel
    */
    public Image negative() {
        Image transformed = new Image(og.getHeight(), og.getWidth());

        for (int row = 0; row < og.getHeight(); row++) {
            for (int col = 0; col < og.getWidth(); col++) {
                // apply pixel transform here
                Pixel p = og.getPixel(row, col);
              
                int r = 255 - p.getR();
                int g = 255 - p.getG();
                int b = 255 - p.getB();
                // assign pixel to `transformed` image
                transformed.setPixel(row, col, new Pixel(r,g,b));
            }
        }

        return transformed;
    }

    /**
    * TASK 1 - Grayscale.
    *
    * For each pixel, calculate the average of its three values and then
    * store that same average in all three channels.
    *
    * average = (red + green + blue) / 3
    *
    * Use the same double loop as negative().
    */
   public Image grayscale() {
        Image transformed = new Image(og.getHeight(), og.getWidth());
        for (int row = 0; row < og.getHeight(); row++) {
            for (int col = 0; col < og.getWidth(); col++) {
                Pixel p = og.getPixel(row, col);
                int avg = p.getPromedio(); // Usamos nuestro nuevo método
                transformed.setPixel(row, col, new Pixel(avg, avg, avg));
            }
        }
        return transformed;
    }

    /**
    * TASK 2 - Keep only one channel.
    *
    * If channel is 0, keep red and set green and blue to 0.
    * If channel is 1, keep green and set red and blue to 0.
    * If channel is 2, keep blue and set red and green to 0.
    *
    * Hint: you can do this with a loop over the three channels and an if,
    * or with three separate lines. Both are fine.
    *
    * @param channel 0 = red, 1 = green, 2 = blue
    */
    public Image keepOnlyChannel(int channel) {
        Image transformed = new Image(og.getHeight(), og.getWidth());
        for (int row = 0; row < og.getHeight(); row++) {
            for (int col = 0; col < og.getWidth(); col++) {
                Pixel p = og.getPixel(row, col);
                int r = (channel == 0) ? p.getR() : 0;
                int g = (channel == 1) ? p.getG() : 0;
                int b = (channel == 2) ? p.getB() : 0;
                transformed.setPixel(row, col, new Pixel(r, g, b));
            }
        }
        return transformed;
    }

    /**
    * TASK 3 - Brightness.
    *
    * Add 'amount' to every color value. A positive amount makes the image
    * brighter, a negative amount makes it darker.
    *
    * CAREFUL: color values must stay between 0 and 255.
    * If a result is above 255, store 255. If it is below 0, store 0.
    * (This is called "clamping".)
    *
    * Run it once WITHOUT clamping and look at the output file. Then add the
    * clamping and compare. You should be able to explain the difference.
    */
  public Image brightness(int amount) {
        Image transformed = new Image(og.getHeight(), og.getWidth());
        for (int row = 0; row < og.getHeight(); row++) {
            for (int col = 0; col < og.getWidth(); col++) {
                Pixel p = og.getPixel(row, col);
                // Los setters en la clase Pixel se encargarán del "clamping"
                transformed.setPixel(row, col, new Pixel(
                    p.getR() + amount, 
                    p.getG() + amount, 
                    p.getB() + amount
                ));
            }
        }
        return transformed;
    }

    /**
    * TASK 4 - Black and white (threshold).
    *
    * For each pixel, calculate the average of its three values.
    * If the average is greater than 'limit', turn the pixel completely
    * white (255, 255, 255). Otherwise turn it completely black (0, 0, 0).
    *
    * Try it with limit = 128, then with 60, then with 200.
    *
    * @param limit a value between 0 and 255
    */
   public Image blackAndWhite(int limit) {
        Image transformed = new Image(og.getHeight(), og.getWidth());
        for (int row = 0; row < og.getHeight(); row++) {
            for (int col = 0; col < og.getWidth(); col++) {
                Pixel p = og.getPixel(row, col);
                int avg = p.getPromedio();
                int val = (avg > limit) ? 255 : 0;
                transformed.setPixel(row, col, new Pixel(val, val, val));
            }
        }
        return transformed;
    }

    // ---------------------------------------------------------------
    // PART 2 - MOVING PIXELS AROUND
    // Here you do not change colors. You change POSITIONS.
    // ---------------------------------------------------------------

    /**
    * TASK 5 - Mirror horizontally (like a mirror on the wall).
    *
    * The pixel in column x must end up in column (width - 1 - x),
    * staying in the same row.
    *
    * Suggested approach:
    *   1. create a new array of the SAME size:
    *          int[][][] result = new int[height][width][3];
    *   2. copy each pixel to its mirrored position
    *   3. at the end, replace the field:  this.pixels = result;
    *
    * Question to answer in your report: why is it a bad idea to swap the
    * values directly inside the original array using the full width?
    */
    public Image mirrorHorizontal() {
        Image transformed = new Image(og.getHeight(), og.getWidth());
        for (int row = 0; row < og.getHeight(); row++) {
            for (int col = 0; col < og.getWidth(); col++) {
                Pixel p = og.getPixel(row, col);
                int nuevaColumna = og.getWidth() - 1 - col;
                transformed.setPixel(row, nuevaColumna, new Pixel(p.getR(), p.getG(), p.getB()));
            }
        }
        return transformed;
    }

    /**
    * TASK 6 - Rotate 90 degrees to the right.
    *
    * This is the important one. The new image does NOT have the same shape
    * as the old one: rows become columns and columns become rows.
    *
    *   new array size  ->  int[width][height][3]
    *
    * The pixel at (row y, column x) of the original ends up at
    * (row x, column height - 1 - y) of the result.
    *
    * Do not forget to update this.width and this.height at the end,
    * otherwise every method you call afterwards will break.
    */
    public Image rotate90() {
        
        Image transformed = new Image(og.getWidth(), og.getHeight());
        for (int row = 0; row < og.getHeight(); row++) {
            for (int col = 0; col < og.getWidth(); col++) {
                Pixel p = og.getPixel(row, col);
                int nuevaFila = col;
                int nuevaColumna = og.getHeight() - 1 - row;
                transformed.setPixel(nuevaFila, nuevaColumna, new Pixel(p.getR(), p.getG(), p.getB()));
            }
        }
        return transformed;
    }

    // ---------------------------------------------------------------
    // PART 3 - OPTIONAL CHALLENGE
    // ---------------------------------------------------------------

    /**
    * BONUS - Blur.
    *
    * Each pixel becomes the average of itself and its 8 neighbours.
    *
    * You must read from a COPY of the original and write into a new array,
    * because if you overwrite pixels while reading them, the blur will
    * "smear" in one direction and look wrong.
    *
    * Ignore the pixels on the border of the image (start your loops at 1
    * and stop at height - 1 and width - 1). Copy the border unchanged.
    */
    public void blur() {
        // TODO: optional
    }
}
