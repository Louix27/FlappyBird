import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JPanel implements ActionListener {
    private JButton easyButton;
    private JButton hardButton;
    private JFrame frame;

    public Menu(JFrame frame) {
        this.frame = frame;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel title = new JLabel("Flappy Bird");
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(Color.BLUE);

        easyButton = new JButton("Easy");
        hardButton = new JButton("Hard");

        easyButton.setFont(new Font("Arial", Font.PLAIN, 24));
        hardButton.setFont(new Font("Arial", Font.PLAIN, 24));

        easyButton.setBackground(Color.GREEN);
        hardButton.setBackground(Color.RED);

        easyButton.setForeground(Color.WHITE);
        hardButton.setForeground(Color.WHITE);

        easyButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        hardButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        easyButton.addActionListener(this);
        hardButton.addActionListener(this);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(title, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(easyButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(hardButton, gbc);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == easyButton) {
            startGame("easy");
        } else if (e.getSource() == hardButton) {
            startGame("hard");
        }
    }

    private void startGame(String difficulty) {
        frame.getContentPane().removeAll();
        FlappyBird flappyBird = new FlappyBird(difficulty);
        frame.add(flappyBird);
        frame.revalidate();
        frame.repaint();
        flappyBird.requestFocus();
    }
}