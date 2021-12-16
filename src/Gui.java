import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;


public class Gui extends JFrame implements ActionListener {

    JButton source_file = new JButton("Source");
    JButton destination = new JButton("Destination");
    JPanel leftImage = new JPanel();
    JLabel the_selected_image = new JLabel("The selected image");
    JButton compress = new JButton("Compress");
    JButton decompress = new JButton("Decompress");
    JPanel rightImage = new JPanel();
    JLabel the_compressed_image = new JLabel("The compressed image");

    JPanel dimensions = new JPanel();

    JLabel size = new JLabel("CodeBook size");
    JLabel vectorDimensions = new JLabel("Vector Dimensions ");
    JLabel block_height = new JLabel("block height");
    JTextField text1 = new JTextField("32");
    JTextField text2 = new JTextField("2");
    JTextField sourcetext = new JTextField(null);
    JTextField desttext = new JTextField(null);
    BufferedImage image = null;
    int numberOfBlocks = 32;
    int blockWidth = 2;
    Border lineBorder = BorderFactory.createLineBorder(Color.gray);

    Gui() {
        this.setVisible(true);
        this.setSize(1280, 720);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setBackground(new Color(0xe6e6e6));
        this.setTitle("Image Compression Program");

        JPanel sourceblock = new JPanel();
        JPanel destinationblock = new JPanel();
        source_file.addActionListener(this);
        destination.addActionListener(this);

        this.setLayout(null);
        this.setResizable(false);
        this.add(sourceblock);
        this.add(destinationblock);

        sourceblock.setBounds(150, 60, 300, 35);
        sourceblock.setBackground(new Color(0xe6e6e6));
        destinationblock.setBounds(800, 60, 300, 35);
        destinationblock.setBackground(new Color(0xe6e6e6));
        sourceblock.add(sourcetext);
        sourcetext.addActionListener(this);
        sourceblock.add(source_file);
        sourcetext.setPreferredSize(new Dimension(150, 20));
        destinationblock.add(desttext);

        destinationblock.add(destination);
        desttext.setPreferredSize(new Dimension(150, 20));

        this.add(leftImage);
        leftImage.setBounds(50, 100, 500, 400);
        leftImage.setBackground(Color.white);
        leftImage.add(the_selected_image);
        leftImage.setBorder(lineBorder);

        this.add(compress);
        compress.setBounds(300, 520, 100, 25);
        compress.addActionListener(this);

        this.add(decompress);
        decompress.setBounds(170, 520, 110, 25);
        decompress.addActionListener(this);

        this.add(rightImage);
        rightImage.setBounds(710, 100, 500, 400);
        rightImage.setBackground(Color.white);
        rightImage.add(the_compressed_image);
        rightImage.setBorder(lineBorder);

        the_selected_image.setHorizontalTextPosition(JLabel.CENTER);
        the_selected_image.setVerticalTextPosition(JLabel.TOP);

        the_compressed_image.setHorizontalTextPosition(JLabel.CENTER); //this
        the_compressed_image.setVerticalTextPosition(JLabel.TOP);
        this.add(dimensions);
        dimensions.setBounds(800, 520, 300, 70);
        dimensions.setBackground(new Color(0xe6e6e6));
        dimensions.add(size);

        dimensions.add(text1);
        text1.setPreferredSize(new Dimension(100, 20));
        dimensions.add(vectorDimensions);
        dimensions.setBorder(lineBorder);
        text2.setPreferredSize(new Dimension(100, 20));

        dimensions.add(text2);


//		text3.setBounds(480,50,100,20) ; 
//		this.add(text3) ;  
    } //this


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == decompress) {
            String source = sourcetext.getText();
            String dest = desttext.getText();
            if (source.equals("") || dest.equals("")) {
                JOptionPane.showMessageDialog(this,
                        "Please chose source and Destination.",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                new vector_quantization().decompress(source, dest);
                ImageIcon icon = new ImageIcon(dest);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                image = ImageIO.read(new File(dest));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            Image image2 = image.getScaledInstance(rightImage.getWidth() - 1, rightImage.getHeight() - 1, Image.SCALE_SMOOTH);
            ImageIcon format2 = new ImageIcon(image2);
            the_compressed_image.setIcon(format2);

        } else if (e.getSource() == sourcetext) {
            try {
                image = ImageIO.read(new File(sourcetext.getText()));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                Image image1 = image.getScaledInstance(leftImage.getWidth() - 1, leftImage.getHeight() - 1, Image.SCALE_SMOOTH);
                ImageIcon format = new ImageIcon(image1);
                the_selected_image.setIcon(format);
            } catch (Exception r) {
            }
        } else if (e.getSource() == destination) {
            String s;
            JFileChooser chooser = new JFileChooser();
            int response = chooser.showOpenDialog(null);
            if (response == 0) {
                File file = new File(chooser.getSelectedFile().getAbsolutePath());
                s = file.getAbsolutePath();
                desttext.setText(s);
            }
        } else if (e.getSource() == source_file) {
            String s = null;
            JFileChooser chooser = new JFileChooser();
            int response = chooser.showOpenDialog(null);
            if (response == 0) {
                File file = new File(chooser.getSelectedFile().getAbsolutePath());
                s = file.getAbsolutePath();
                sourcetext.setText(s);
            }
            try {
                image = ImageIO.read(new File(s));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                Image image1 = image.getScaledInstance(leftImage.getWidth() - 1 - 1, leftImage.getHeight() - 1 - 1, Image.SCALE_SMOOTH);
                ImageIcon format = new ImageIcon(image1);
                the_selected_image.setIcon(format);
            } catch (Exception r) {
            }
        } else if (e.getSource() == compress) {
            String source = sourcetext.getText();
            String dest = desttext.getText();
            if (source.equals("") || dest.equals("")) {
                JOptionPane.showMessageDialog(this,
                        "Please chose source and Destination.",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                String str;
                str = text1.getText();
                numberOfBlocks = Integer.valueOf(str);
                str = text2.getText();
                blockWidth = Integer.valueOf(str);
            } catch (Exception r) {
                JOptionPane.showMessageDialog(this,
                        "Vector size and codebook size was set to default.",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            vector_quantization vecq;
            try {
                vecq = new vector_quantization(blockWidth, numberOfBlocks);
                vecq.compress(source, dest);
                vecq.img.toimage("tmp.png");
            } catch (Exception r) {
                JOptionPane.showMessageDialog(this,
                        "Compression failed.",
                        "Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            String s = "tmp.png";
            try {
                image = ImageIO.read(new File(s));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            Image image1 = image.getScaledInstance(leftImage.getWidth() - 1, leftImage.getHeight() - 1, Image.SCALE_SMOOTH);
            ImageIcon format = new ImageIcon(image1);
            the_selected_image.setIcon(format);

            vecq.decompress(dest, "result.png");
            ImageIcon icon = new ImageIcon("result.png");

            try {
                image = ImageIO.read(new File("result.png"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            Image image2 = image.getScaledInstance(rightImage.getWidth() - 1, rightImage.getHeight() - 1, Image.SCALE_SMOOTH);
            ImageIcon format2 = new ImageIcon(image2);
            the_compressed_image.setIcon(format2);

        }
    }
}
	
