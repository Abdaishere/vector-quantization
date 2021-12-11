import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
//        Scanner input = new Scanner(System.in);
//        System.out.println("Vector size");
//        int vectorSize = input.nextInt();
//        System.out.println("code book size");
//        int bookSize = input.nextInt();
//        System.out.println("source");
//        String source = input.nextLine();
//        System.out.println("destination");
//        String dest = input.nextLine();
//        new vector_quantization().compress(vectorSize, bookSize, new image(source), dest);
//        System.out.println(new image("download.jpg").toString());
        vector_quantization vecq = new vector_quantization(2, (int) Math.pow(2,4));
        vecq.compress("dd.jpg", ".\\output.txt");
        vecq.img.toimage("gray.jpg");
        vecq.decompress(".\\output.txt", "result.png");
//        Image.toimage("out.png");
    }
}
