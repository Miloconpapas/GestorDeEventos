import javax.swing.*;

public class Listas {
    private JTable table1;
    private JButton agregarButton;
    private JButton modificarButton;
    private JButton eliminarButton;
    private JTable table2;
    private JComboBox<String> comboBox1;

    private JPanel panelListas;
    private JComboBox<String> comboBox;
    private JButton cargarButton;
    public Listas() {



    }



    public void setVisible(boolean b) {
        JFrame frame = new JFrame("Listas de invitados");
        frame.setContentPane(new Listas().panelListas);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }
}
