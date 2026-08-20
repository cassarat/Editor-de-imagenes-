/*
* The Pixel class represents an RGB pixel.
* We use `int` as the data type to back up every
* color channel.
*/
// utilizamos int para modelar el color con tres numeros entreros 
// un arreglo de dos dimensiones seria una imagen 
/*
* The Pixel class represents an RGB pixel.
* We use `int` as the data type to back up every
* color channel.
*/
public class Pixel {
    // Atributos privados (Encapsulamiento)
    private int r;
    private int g;
    private int b;
    
    public Pixel (int r, int g, int b) {
        setR(r);
        setG(g);
        setB(b);
    }

    // Getters públicos
    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    // Setters públicos con validación (Clamping de 0 a 255)
    public void setR(int r) {
        if (r < 0) this.r = 0;
        else if (r > 255) this.r = 255;
        else this.r = r;
    }

    public void setG(int g) {
        if (g < 0) this.g = 0;
        else if (g > 255) this.g = 255;
        else this.g = g;
    }

    public void setB(int b) {
        if (b < 0) this.b = 0;
        else if (b > 255) this.b = 255;
        else this.b = b;
    }

    // Método de apoyo para los filtros que piden el promedio
    public int getPromedio() {
        return (this.r + this.g + this.b) / 3;
    }
}
