import java.util.ArrayList;
import java.util.Collections;

public class vector_quantization {
    ArrayList<float[][]> vectors;
    int vectorSize;
    int bookSize;
    image img;
    ArrayList<float[][]> codex;
    ArrayList<Integer>[] IDs;
    static int i;
    ArrayList<Integer> parent;

    public vector_quantization(int vectorSize, int bookSize) {
        this.vectorSize = vectorSize;
        this.bookSize = bookSize;
    }

    public void compress(String source, String dest) {
        this.img = new image(source);
        vectors = new ArrayList<>();
        //split image to vectors
        for (int i = 0; i < img.w; i += vectorSize) {
            for (int j = 0; j < img.h; j += vectorSize) {
                float[][] tmp = new float[vectorSize][vectorSize];
                int Row = 0;
                for (int k = i; k < i + vectorSize; k++) {
                    int col = 0;
                    for (int l = j; l < j + vectorSize; l++) {
                        try {
                            tmp[Row][col++] = img.pixels[k][l];
                        } catch (Exception e) {
                        }
                    }
                    Row++;
                }
                vectors.add(tmp);
            }
        }
        splitting();
        encode(dest);
    }

    public void vectors_to_images(ArrayList<float[][]> vector, String str) {
        image tmp = new image();
        i = 0;
        for (float[][] vec : vector) {
            tmp.pixels = vec;
            tmp.toimage(".\\test\\" + str + "_%s.png".formatted(i++));
        }
    }

    public void vector_replace(float[][] vec, image img, int i) {
        vectors.set(i, vec);
    }

    public void encode(String dest) {
        String data = new String();
        String[] lables = new String[codex.size()];
        for (int i = 0; i < codex.size(); i++) {
            lables[i] = fileProcessor.toBin(i, fileProcessor.log2(codex.size()));
        }
        for (int i = 0; i < vectors.size(); i++) {
            data += lables[findcoodbookofID(i)];
        }
        String header = new String();
        header += fileProcessor.toBin(img.w * img.h * fileProcessor.log2(codex.size()) % 8, 16);
        header += fileProcessor.toBin(img.w, 16);
        header += fileProcessor.toBin(img.h, 16);
        header += fileProcessor.toBin(codex.size(), 16);
        header += fileProcessor.toBin(vectorSize, 16);
        String coodbook = new String();
        for (float[][] code : codex) {
            for (int i = 0; i < vectorSize; i++) {
                for (int j = 0; j < vectorSize; j++) {
                    coodbook += fileProcessor.toBin((int) code[i][j], 8);
                }
            }
        }
        comData += coodbook + data;
        int extraLen = 8 - (comData.length() % 8);
        if (extraLen == 8)
            extraLen = 0;

        comData = fileProcessor.binaryStringToBits(comData, extraLen);
        comData = fileProcessor.toBin(img.w, 16) + comData;
        comData = fileProcessor.toBin(img.h, 16) + comData;
        comData = fileProcessor.toBin(codex.size(), 16) + comData;
        comData = fileProcessor.toBin(vectorSize, 16) + comData;

        fileProcessor.writeToFile((byte) extraLen + fileProcessor.binaryStringToBits(comData, extraLen), dest);
    }

    private void decode(String source) {
        img = new image();
        String fileData = fileProcessor.fileToString(source);
//        int extralen = Integer.parseInt(fileProcessor.bitsToBinaryString(fileData.substring(0, 2), 0), 2);
//        fileData = fileData.substring(2);
//        System.out.println(extralen);
//        System.out.println(img.w = Integer.parseInt(fileProcessor.bitsToBinaryString(fileData.substring(0, 2), 0), 2));
//        fileData = fileData.substring(2);
//        System.out.println(img.h = Integer.parseInt(fileProcessor.bitsToBinaryString(fileData.substring(0, 2), 0), 2));
//        fileData = fileData.substring(2);
//        System.out.println(bookSize = Integer.parseInt(fileProcessor.bitsToBinaryString(fileData.substring(0, 2), 0), 2));
//        fileData = fileData.substring(2);
//        System.out.println(vectorSize = Integer.parseInt(fileProcessor.bitsToBinaryString(fileData.substring(0, 2), 0), 2));
//        fileData = fileData.substring(2);
        int extralen = Integer.parseInt(fileData.substring(0, 1), 2);
        System.out.println(extralen);
        fileData = fileData.substring(1);

        vectorSize = Integer.parseInt(fileData.substring(0, 2), 2);
        fileData = fileData.substring(2);

        int codexsize = Integer.parseInt(fileData.substring(0, 2),  2);
        fileData = fileData.substring(2);

        img.h = Integer.parseInt(fileData.substring(0, 2), 2);
        fileData = fileData.substring(2);

        img.w = Integer.parseInt(fileData.substring(0, 2), 2);
        fileData = fileData.substring(2);

        codex = new ArrayList<float[][]>();
        for (int q = 0; q < bookSize; q++) {
            float[][] tmp = new float[vectorSize][vectorSize];
            for (int i = 0; i < vectorSize; i++) {
                for (int j = 0; j < vectorSize; j++) {
                    tmp[i][j] = Integer.parseInt(fileProcessor.bitsToBinaryString(fileData.substring(0, 1), 0), 2);
                    fileData = fileData.substring(1);
                }
            }
            codex.add(tmp);
        }
        vectors_to_images(codex, ".\\encoded");
        int size = fileProcessor.log2(bookSize);
        System.out.println(size);
        parent = new ArrayList<>();
        fileData = fileProcessor.bitsToBinaryString(fileData, 0);
        System.out.println(fileData.length() / size);
        while (fileData != "") {
            System.out.println(fileData);
            parent.add(Integer.parseInt(fileData.substring(0, size), 2));
            fileData = fileData.substring(size);
        }
//        parent.add(Integer.parseInt(fileData, 2));
        System.out.println(parent.size());
    }

    public void decompress(String source, String dest) {
        decode(source);
        img.pixels = new float[img.w][img.h];
        int w = 0;
        for (int i = 0; i < img.w; i += vectorSize) {
            for (int j = 0; j < img.h; j += vectorSize) {
                float[][] tmp = codex.get(parent.get(w));
                int Row = 0;
                for (int k = i; k < i + vectorSize; k++) {
                    int col = 0;
                    for (int l = j; l < j + vectorSize; l++) {
                        try {
                            img.pixels[k][l] = tmp[Row][col++];
                        } catch (Exception e) {
                        }
                    }
                    Row++;
                }
                w++;
            }
        }
        img.toimage(dest);
    }

    public int findcoodbookofID(int ID) {
        for (int node = 0; node < codex.size(); node++) {
            if (IDs[node] != null && IDs[node].contains(ID)) {
                return node;
            }
        }
        return -1;
    }

    public void splitting() {
        //Get average vector
        float[][] averageVec = new float[vectorSize][vectorSize];           //Average
        averageVec = getAverageOfVectors(vectors);

        codex = new ArrayList<>();
        IDs = new ArrayList[bookSize];
        codex.add(averageVec);
        while (codex.size() < bookSize) {
            int size = codex.size();
            for (int i = 0; i < size; i++) {
                float[][] tmp = addToVector(codex.remove(0), 0);
                codex.add(tmp);
                codex.add(addToVector(tmp, 1));
            }
            size = vectors.size();
            ArrayList<Integer>[] tmpIDs = new ArrayList[codex.size()];
            for (int i = 0; i < size; i++) {
                ArrayList<Integer> MESs = new ArrayList<>();
                for (float[][] code : codex) {
                    MESs.add(MSE(code, vectors.get(i)));
                }
                int possion = getindexofmin(MESs);
                if (tmpIDs[possion] == null)
                    tmpIDs[possion] = new ArrayList<Integer>();
                tmpIDs[possion].add(i);
            }
            size = codex.size();
            for (int i = 0; i < size; i++) {
                if (tmpIDs[i] == null) {
                    continue;
                } else
                    codex.set(i, AvgbyID(tmpIDs[i]));
            }
            IDs = tmpIDs;
        }
        int size = vectors.size();
        ArrayList<Integer>[] tmpIDs = new ArrayList[codex.size()];
        for (int i = 0; i < size; i++) {
            ArrayList<Integer> MESs = new ArrayList<>();
            for (float[][] code : codex) {
                MESs.add(MSE(code, vectors.get(i)));
            }
            int possion = getindexofmin(MESs);
            if (tmpIDs[possion] == null)
                tmpIDs[possion] = new ArrayList<Integer>();
            tmpIDs[possion].add(i);
        }
        IDs = tmpIDs;
        vectors_to_images(codex, "codebook number ");
    }

    public int getindexofmin(ArrayList<Integer> a) {
        return a.indexOf(Collections.min(a));
    }

    public float[][] AvgbyID(ArrayList<Integer> a) {
        ArrayList<float[][]> vector = new ArrayList<>();
        for (Integer ID : a) {
            vector.add(vectors.get(ID));
        }
//        vectors_to_images(vector, str);
        return getAverageOfVectors(vector);
    }

    public float[][] getAverageOfVectors(ArrayList<float[][]> vecto) {
        float[][] averageVec = new float[vectorSize][vectorSize];
        for (float[][] vec : vecto) {
            for (int i = 0; i < vectorSize; i++) {
                for (int j = 0; j < vectorSize; j++) {
                    averageVec[i][j] += vec[i][j];
                }
            }
        }
        for (int i = 0; i < vectorSize; i++) {
            for (int j = 0; j < vectorSize; j++) {
                averageVec[i][j] /= vecto.size();
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


    public float[][] addToVector(float[][] vec, int val) {
        float[][] temp = new float[vectorSize][vectorSize];
        for (int i = 0; i < vectorSize; i++) {
            for (int j = 0; j < vectorSize; j++) {
                int tmp = (int) Math.floor(vec[i][j] + val);
                if (tmp < 0)
                    temp[i][j] = 0;
                else if (tmp > 255)
                    temp[i][j] = 255;
                else
                    temp[i][j] = tmp;
            }
        }
        return temp;
    }

    public int MSE(float[][] vec1, float[][] vec2) {
        int result = 0;

        for (int i = 0; i < vectorSize; i++) {
            for (int j = 0; j < vectorSize; j++) {
                result += Math.abs(vec1[i][j] - vec2[i][j]);
            }
        }
        return result;
    }
}
