import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AgregarTareas {
    private JTextField textField1;
    private JButton agregarTareaButton;
    private JPanel PanelTareas;
    private Tareas ventana;


    public AgregarTareas(Tareas origen) {
        this.ventana = origen;

        agregarTareaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // Obtén los datos ingresados en los campos de texto
                String tarea = textField1.getText();


                String jdbcUrl = "jdbc:mysql://localhost/gestion_eventos";
                String usuario = "root";
                String contraseña = "HoyTomeReliveran432";

                try {
                    Connection connection = DriverManager.getConnection(jdbcUrl, usuario, contraseña);

                    String sql = "INSERT INTO  todo_list (tarea) VALUES (?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, tarea);


                    int filasInsertadas = preparedStatement.executeUpdate();

                    if (filasInsertadas > 0) {
                        JOptionPane.showMessageDialog(null, "El evento ha sido agregado a la base de datos.");


                        Object[] rowData = {null, tarea, };
                        //  tableModel.addRow(rowData);
                        ventana.actualizarTabla();
                    } else {
                        JOptionPane.showMessageDialog(null, "No se ha podido agregar el evento.");
                    }


                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

            }


        });
    }

    public void setVisible(boolean b) {
        JFrame frame = new JFrame("Agregar Tarea");
        frame.setContentPane(PanelTareas);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
