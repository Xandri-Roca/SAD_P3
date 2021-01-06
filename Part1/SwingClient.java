// Bernat Xandri Zaragoza & Ramon Roca Oliver

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

public class SwingClient implements ActionListener {

    private Document doc;
    private SimpleAttributeSet attributeSet;
    private JListSW list;
    private JTextField message;
    private JButton button;
    private MySocket socket;
    private String name;

    public SwingClient(String name, MySocket socket) {
        this.socket = socket;
        this.name = name;
    }

// With this method, we write the messages of the user - client, and we pass them to the message box.
    public void actionPerformed(ActionEvent event) {
        String text = message.getText();
        message.setText("");
        if (text.length() > 0) {
            socket.write(text);
            try {
                doc.insertString(doc.getLength(), text + "\n", attributeSet);
            } catch (BadLocationException e) {
                System.out.println("Error. Message not valid.");
            }
        }
    }

// With the following method, we take the messages from the server, we add the user who sent it to the 
// JList list if it is a new user, and we print the message in the message box.
    public void addMessage(String text) {
        String rcv[] = text.split("> ", 2);
        if (rcv[1].equals("EXIT")) {
            try {
                doc.insertString(doc.getLength(), rcv[0] + " -> " + "left the chat." + "\n", attributeSet);
            } catch (BadLocationException e) {
                System.out.println("Error. Message not valid.");
            }
            list.removeUser(rcv[0]);

        } else {
            list.addUser(rcv[0]);
            try {
                doc.insertString(doc.getLength(), rcv[0] + " -> " + rcv[1] + "\n", attributeSet);
            } catch (BadLocationException e) {
                System.out.println("Error. Message not valid.");
            }
        }
    }

// We instantiate the graphical display.
    public void createAndShowGUI(String name) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame(name);

        JPanel out = new JPanel();
        out.setLayout(new BoxLayout(out, BoxLayout.LINE_AXIS));
        JTextPane pane = new JTextPane();
        pane.setEditable(false);
        attributeSet = new SimpleAttributeSet();
        StyleConstants.setBold(attributeSet, true);
        pane.setCharacterAttributes(attributeSet, true);
        attributeSet = new SimpleAttributeSet();
        doc = pane.getStyledDocument();
        JScrollPane scrollPane = new JScrollPane(pane);
        scrollPane.setPreferredSize(new Dimension(350, 150));
        out.add(scrollPane, BorderLayout.CENTER);

        list = new JListSW();
        JScrollPane jscroll = new JScrollPane(list.getJList());
        out.add(jscroll);
        JPanel inp = new JPanel();
        inp.setLayout(new BoxLayout(inp, BoxLayout.LINE_AXIS));
        message = new JTextField(30);
        button = new JButton("Send");
        message.addActionListener(this);
        button.addActionListener(this);
        inp.add(message);
        inp.add(button);
        frame.add(out, BorderLayout.CENTER);
        frame.add(inp, BorderLayout.PAGE_END);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setMinimumSize(new Dimension(450, 250));
        frame.setVisible(true);
    }

}
