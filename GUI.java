import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GUI {
    public static void main(String[] args) {
        JFrame frame = new JFrame("GUI");
        frame.setSize(400, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        //frame.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));//

        //clock//
        JLabel timeLabel = new JLabel();
        timeLabel.setFont(new Font("Dialog", Font.PLAIN, 40));
        Timer timer = new Timer(1000, e -> {
            String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
            timeLabel.setText(time);
        });

        //button//
        JButton button = new JButton("Click me to show clock");
        JButton closeButton = new JButton("Close Program");

        //calculator//
        JTextField inputOne = new JTextField();
        JTextField inputTwo = new JTextField();
        JButton inputButton = new JButton("");

        button.setBounds(105,20, 200, 35);
        timeLabel.setBounds(110, 100, 200, 50);
        inputOne.setBounds(105, 180, 90, 30);
        inputTwo.setBounds(205, 180, 90, 30);
        inputButton.setBounds(195, 185, 10, 20);
        closeButton.setBounds(140, 420, 120, 30);

        button.addActionListener(e -> {
            button.setText("Show clock!");
            timer.start();
        });

        closeButton.addActionListener(e -> {
            System.exit(0);
        });

        frame.add(button);
        frame.add(timeLabel);
        frame.add(inputOne);
        frame.add(inputTwo);
        frame.add(inputButton);
        frame.add(closeButton);

        frame.setVisible(true);
    }
}
