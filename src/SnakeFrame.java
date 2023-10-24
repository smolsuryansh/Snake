import javax.swing.JFrame;

public class SnakeFrame extends JFrame {

    SnakeFrame(){

        GamePanel panel = new GamePanel();

        this.add(panel);
        this.setTitle("Snek");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);


    }
}
