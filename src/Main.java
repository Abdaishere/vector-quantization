import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

public class Main extends JFrame implements ActionListener {
    static JFrame frame = new JFrame("main frame");
    static JPanel panel = new JPanel();
    static JTextField vectorSize = new JTextField("25");
    static JTextField codebooksize = new JTextField("32");
    static JTextField source = new JTextField("downlaod.png");
    static JTextField dest = new JTextField("result.png");
    static JButton button = new JButton("compress");

    public static void main(String[] args) {
        Main te = new Main();

        button.addActionListener(te);
        frame.add(panel);
        panel.add(vectorSize);
        frame.add(panel);
        panel.add(codebooksize);
        frame.add(panel);
        panel.add(source);
        frame.add(panel);
        panel.add(dest);
        panel.add(button);


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
//        vector_quantization vecq = new vector_quantization(Integer.parseInt(vectorSize.getText(),10), Integer.parseInt(codebooksize.getText(),10));
//        vecq.compress(source.getText(), ".\\output2.txt");
//        vecq.img.toimage("gray2.jpg");
//        vecq.decompress(".\\output2.txt", dest.getText());
//        Image.toimage("out.png");
        frame.setSize(300,100);
        frame.show();
    }
    public void BAction(ActionEvent e)
    {
        vector_quantization vecq = new vector_quantization(Integer.parseInt(vectorSize.getText(),10), Integer.parseInt(codebooksize.getText(),10));
        vecq.compress(source.getText(), ".\\output2.txt");
        vecq.img.toimage("gray2.jpg");
        vecq.decompress(".\\output2.txt", dest.getText());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        BAction(e);
    }
}
