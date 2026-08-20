public class Main {
    public static void main(String[] args) {
        //iniciar validacion de  cantidad de argumentos
        if (args.length < 3) {
            mostrarUso();
            return;
        }

        String rutaEntrada = args[0];
        String rutaSalida = args[1];
        String filtro = args[2].toLowerCase();

        // validar que no se sobrescriba el archivo original
        if (rutaEntrada.equalsIgnoreCase(rutaSalida)) {
            System.out.println("ERROR: El archivo de salida debe ser distinto al de entrada.");
            return;
        }

        // update la imagen y validar que exista
        Image imagenOriginal = ImageUtils.load(rutaEntrada);
        if (imagenOriginal == null) {
            System.out.println("ERROR: No se pudo abrir el archivo de entrada. Verifique la ruta.");
            return;
        }

        // enseñar información de la imagen de entrada
        int anchoOriginal = imagenOriginal.getWidth();
        int altoOriginal = imagenOriginal.getHeight();
        int totalPixelesOriginal = anchoOriginal * altoOriginal;
        System.out.println("--- Imagen Original ---");
        System.out.println("Ancho: " + anchoOriginal + " px");
        System.out.println("Alto: " + altoOriginal + " px");
        System.out.println("Total de pixeles: " + totalPixelesOriginal);
        
        double brilloOriginal = calcularPromedioBrillo(imagenOriginal);
        System.out.println("Brillo promedio original: " + brilloOriginal);

        // aplicar el filtro solicitado
        ImageEditor editor = new ImageEditor(imagenOriginal);
        Image imagenResultante = null;

        switch (filtro) {
            case "grises":
                imagenResultante = editor.grayscale();
                break;
            case "negativo":
                imagenResultante = editor.negative();
                break;
            case "rojo":
                imagenResultante = editor.keepOnlyChannel(0);
                break;
            case "verde":
                imagenResultante = editor.keepOnlyChannel(1);
                break;
            case "azul":
                imagenResultante = editor.keepOnlyChannel(2);
                break;
            case "brillo":
                int cantidadBrillo = 30; // Valor por defecto
                if (args.length > 3) {
                    cantidadBrillo = Integer.parseInt(args[3]);
                }
                imagenResultante = editor.brightness(cantidadBrillo);
                break;
            case "umbral":
                int limite = 128; // Valor por defecto
                if (args.length > 3) {
                    limite = Integer.parseInt(args[3]);
                }
                imagenResultante = editor.blackAndWhite(limite);
                break;
            case "espejo":
                imagenResultante = editor.mirrorHorizontal();
                break;
            case "rotar":
                imagenResultante = editor.rotate90();
                break;
            default:
                System.out.println("ERROR: Filtro no reconocido (" + filtro + ").");
                mostrarUso();
                return;
        }

        // estadísticas de la imagen resultante
        double brilloFinal = calcularPromedioBrillo(imagenResultante);
        System.out.println("\n--- Imagen Resultante ---");
        System.out.println("Brillo promedio final: " + brilloFinal);
        mostrarPixelesExtremos(imagenResultante);

        // guardar y confirmar
        ImageUtils.save(imagenResultante, rutaSalida);
        System.out.println("Dimensiones finales: " + imagenResultante.getWidth() + "x" + imagenResultante.getHeight());
    }

    // metodos auxiliares

    private static void mostrarUso() {
        System.out.println("\nUSO CORRECTO DE LA HERRAMIENTA:");
        System.out.println("java Main <entrada> <salida> <filtro> [parametros]");
        System.out.println("Filtros disponibles: grises, negativo, rojo, verde, azul, espejo, rotar");
        System.out.println("Filtros con parametros: brillo <cantidad>, umbral <limite>");
    }

    private static double calcularPromedioBrillo(Image img) {
        long sumaBrillo = 0;
        int totalPixeles = img.getWidth() * img.getHeight();

        for (int f = 0; f < img.getHeight(); f++) {
            for (int c = 0; c < img.getWidth(); c++) {
                Pixel p = img.getPixel(f, c);
                sumaBrillo += p.getPromedio();
            }
        }
        return (double) sumaBrillo / totalPixeles;
    }

    private static void mostrarPixelesExtremos(Image img) {
        int maxBrillo = -1;
        int minBrillo = 256;
        String posMasClaro = "";
        String posMasOscuro = "";

        for (int f = 0; f < img.getHeight(); f++) {
            for (int c = 0; c < img.getWidth(); c++) {
                Pixel p = img.getPixel(f, c);
                int brillo = p.getPromedio();

                if (brillo > maxBrillo) {
                    maxBrillo = brillo;
                    posMasClaro = "(Fila " + f + ", Columna " + c + ")";
                }
                if (brillo < minBrillo) {
                    minBrillo = brillo;
                    posMasOscuro = "(Fila " + f + ", Columna " + c + ")";
                }
            }
        }
        System.out.println("Pixel mas claro: " + posMasClaro + " con brillo de " + maxBrillo);
        System.out.println("Pixel mas oscuro: " + posMasOscuro + " con brillo de " + minBrillo);
    }
}