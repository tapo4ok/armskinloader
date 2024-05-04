package mdk.debug;

import mdk.config.ModConfigs;
import mdk.mop.network.ModNetworkHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class GUI extends JFrame {
    public static PrintStream out;
    private JTextArea consoleTextArea;
    private JTextField TextFielda;
    private JButton button;
    public GUI() {

        if (ModConfigs.debug&&FMLCommonHandler.instance().getSide().isClient()) {
            OutputStream out2 = new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    updateTextArea(String.valueOf((char) b));
                    System.out.write(b);
                }

                @Override
                public void write(byte[] b, int off, int len) throws IOException {
                    updateTextArea(new String(b, off, len));
                    System.out.write(b, off, len);
                }

                @Override
                public void write(byte[] b) throws IOException {
                    write(b, 0, b.length);
                    System.out.write(b);
                }
            };

            out = new PrintStream(out2);
            setTitle("Debug Console Window");
            setSize(600, 400);

            consoleTextArea = new JTextArea();
            consoleTextArea.setEditable(false);
            JPanel panel = new JPanel();

            button = new JButton("send");
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //ModNetworkHandler.NETWORK.sendToServer();
                }
            });

            TextFielda = new JTextField();

            JScrollPane scrollPane = new JScrollPane(consoleTextArea);

            panel.add(TextFielda);
            panel.add(scrollPane, BorderLayout.CENTER);


            getContentPane().add(panel);
        }
        else {
            out = System.out;
        }
    }

    private void updateTextArea(final String text) {
        SwingUtilities.invokeLater(() -> consoleTextArea.append(text));
    }
}
