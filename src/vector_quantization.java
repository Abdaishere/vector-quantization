import java.util.ArrayList;

public class vector_quantization {
    ArrayList<int[][]> vectors;

    public void compress(int vectorSize, int bookSize, image img, String dest) {
        vectors = new ArrayList<int[][]>();
        for (int i = 0; i < img.w; i += vectorSize) {
            for (int j = 0; j < img.h; j += vectorSize) {
                int[][] tmp = new int[vectorSize][vectorSize];
                int Row = 0;
                for (int k = i; k < i + vectorSize; k++) {
                    int col = 0;
                    for (int l = j; l < j + vectorSize; l++) {
                        if (k >= img.h || l >= img.w){
                            tmp[Row][col++] = 0;
                            continue;
                        }
                        tmp[Row][col++] = img.pixels[k][l];
                    }
                    Row++;
                }
                vectors.add(tmp);
            }
        }

        int[][] tmp = vectors.get(0);
        for (int i = 0; i < vectorSize; i++) {
            for (int j = 0; j < vectorSize; j++) {
                System.out.print(tmp[i][j] + " ");
            }
            System.out.println(" ");
        }
    }
    public void splitting(){

    }
}
