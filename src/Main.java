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
        vector_quantization vecq = new vector_quantization(25, 32);
        vecq.compress("dd.jpg", ".\\tesst.txt");
        vecq.decompress(".\\tesst.txt", "something.png");
//        Image.toimage("out.png");
    }
}
