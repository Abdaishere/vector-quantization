import java.util.ArrayList;

public class vector_quantization {
    ArrayList<int[][]> vectors;
    int vectorSize;
    int bookSize;
    image img;

    public vector_quantization(int vectorSize, int bookSize, image img) {
        this.vectorSize = vectorSize;
        this.bookSize = bookSize;
        this.img = img;
        vectors = new ArrayList<int[][]>();
        //split image to vectors
        for (int i = 0; i < img.w; i += vectorSize) {
            for (int j = 0; j < img.h; j += vectorSize) {
                int[][] tmp = new int[vectorSize][vectorSize];
                int Row = 0;
                for (int k = i; k < i + vectorSize; k++) {
                    int col = 0;
                    for (int l = j; l < j + vectorSize; l++) {
                        if (k >= img.h || l >= img.w) {
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

    }

    public void compress(String dest) {
        splitting();


    }

    public void splitting() {
        //Get average vector
        float[][] averageVec = new float[vectorSize][vectorSize];           //Average
        averageVec = getAverageOfVectors(vectors);

        ArrayList<float[][]> nodes = new ArrayList<>();
        ArrayList<Integer>[] IDs = new ArrayList[bookSize];
        nodes.add(averageVec);

        while (nodes.size() < bookSize) {
            ArrayList<float[][]> tmpnodes = new ArrayList<>();
            for (float[][] vec : nodes) {       // split
                tmpnodes.add(addToVector(vec, 1));
                tmpnodes.add(addToVector(vec, -1));
            }
            nodes = tmpnodes;
            ArrayList<Integer>[] tmpIds = new ArrayList[bookSize];

            for (int i = 0; i < vectors.size(); i++) {                  // Association
                ArrayList<Integer> meanErrors = new ArrayList<Integer>();
                for (float[][] vec : nodes) {
                    meanErrors.add(MSE(vectors.get(i), vec));
                }
                int[] tmp = new int[2];
                for (int f = 0; f < meanErrors.size(); f++) {
                    if (meanErrors.get(f) < tmp[0]) {
                        tmp[0] = meanErrors.get(f);
                        tmp[1] = f;
                    }
                }
                if (tmpIds[tmp[1]] == null) {
                    tmpIds[tmp[1]] = new ArrayList<Integer>();
                }
                tmpIds[tmp[1]].add(i);
            }
            IDs = tmpIds;

            // Avrage nodes
            for (int i = 0; i < nodes.size(); i++) {
                if (IDs[i] == null)
                    break;
                nodes.set(i, AvgbyID(IDs[i]));
            }
        }

        for (float[][] vec : nodes) {
            printAVector(vec);
            System.out.println(" ");
        }
    }


    public float[][] AvgbyID(ArrayList<Integer> a) {
        ArrayList<int[][]> vector = new ArrayList<>();
        for (Integer ID : a) {
            vector.add(vectors.get(ID));
        }
        return getAverageOfVectors(vector);
    }

    public float[][] getAverageOfVectors(ArrayList<int[][]> vecto) {
        float[][] averageVec = new float[vectorSize][vectorSize];
        for (int[][] vec : vecto) {
            for (int i = 0; i < vectorSize; i++) {
                for (int j = 0; j < vectorSize; j++) {
                    averageVec[i][j] += vec[i][j];
                }
            }
        }
        for (int i = 0; i < vectorSize; i++) {
            for (int j = 0; j < vectorSize; j++) {
                averageVec[i][j] /= vectors.size();
            }
        }
        return averageVec;
    }

    public void printAVector(float[][] vec) {
        for (int i = 0; i < vectorSize; i++) {
            for (int j = 0; j < vectorSize; j++) {
                System.out.print(vec[i][j] + "   ");
            }
            System.out.print("\n");
        }
    }

    public static int log2(int x) {
        return (int) Math.ceil(Math.log(x) / Math.log(2));
    }

    public float[][] addToVector(float[][] vec, int val) {
        float[][] temp = new float[vectorSize][vectorSize];
        for (int i = 0; i < vectorSize; i++) {
            for (int j = 0; j < vectorSize; j++) {
                temp[i][j] = (int) Math.floor(vec[i][j] + val);
            }
            System.out.print("\n");
        }
        return temp;
    }

    public int MSE(int[][] vec1, float[][] vec2) {
        int result = 0;

        for (int i = 0; i < vectorSize; i++) {
            for (int j = 0; j < vectorSize; j++) {
                result += (vec1[i][j] - vec2[i][j]) * (vec1[i][j] - vec2[i][j]);
            }
        }

        return result;
    }
}
