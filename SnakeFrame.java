import javax.swing.JFrame;

public class SnakeFrame extends JFrame {

    SnakeFrame() { // constructor

        SnakePanel panel = new SnakePanel();
        this.add(panel); // ????
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // to exit window
        this.setResizable(false); // fixed window shape
        this.pack(); // appropriate sizing
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

}
