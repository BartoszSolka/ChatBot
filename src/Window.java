import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * @author Bartosz Solka
 */
public class Window {
    private int degree;
    private JButton sendButton;
    private JButton statsButton;
    private JButton loadButton;
    private JTextField inputField;
    private JTextPane textPane;
    private JPanel mainPanel;

    private Parser parser = new Parser();
    private Generator generator = new Generator();

    private static AttributeSet USER_SET;
    private static AttributeSet BOT_SET;

    public Window() {

        SimpleAttributeSet temp;

        temp = new SimpleAttributeSet();
        StyleConstants.setForeground(temp, Color.blue);
        StyleConstants.setFontSize(temp, 14);
        USER_SET = temp;

        temp = new SimpleAttributeSet();
        StyleConstants.setForeground(temp, Color.green);
        StyleConstants.setFontSize(temp, 14);
        BOT_SET = temp;

        ActionListener sendButtonActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = inputField.getText();
                inputField.setText(null);
                if (text.isEmpty()) {
                    return;
                }

                if (parser.isEmpty()) {
                    JOptionPane.showMessageDialog(mainPanel, "Load base");
                    return;
                }

                StyledDocument doc = textPane.getStyledDocument();
                try {
                    doc.insertString(
                            doc.getLength(),
                            "You: " + text + "\n\n",
                            USER_SET
                    );
                    doc.insertString(
                            doc.getLength(),
                            "Bot: " + generator.generateMessage(parser, degree) + "\n\n",
                            BOT_SET
                    );
                } catch (BadLocationException e1) {
                    e1.printStackTrace();
                }

            }
        };
        sendButton.addActionListener(sendButtonActionListener);
        inputField.addActionListener(sendButtonActionListener);

        statsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setMultiSelectionEnabled(true);

                int option = fileChooser.showDialog(mainPanel, "Load");
                if (option != JFileChooser.APPROVE_OPTION) {
                    return;
                }

                List<File> files = Arrays.asList(fileChooser.getSelectedFiles());

                for (File file : files) {
                    try {
                        InputStream is = new FileInputStream(file);
                        parser.run(is, degree);
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        while (true) {
            String input = JOptionPane.showInputDialog(mainPanel, "Enter ngram degree", 2);

            if (input == null) {
                System.exit(-1);
            }

            try {
                degree = Integer.parseInt(input);
                if (degree < 2 || degree > 10) {
                    throw new IllegalArgumentException();

                }
                break;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(mainPanel, "Enter a number");
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(mainPanel, "Degree must be between 2 and 10");
            }
        }

        inputField.requestFocus();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(new WindowsLookAndFeel());
                } catch (UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                }

                JFrame frame = new JFrame("Window");
                frame.setContentPane(new Window().mainPanel);
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setTitle("Rozmowca czatu");
                frame.setPreferredSize(new Dimension(400, 600));
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}





