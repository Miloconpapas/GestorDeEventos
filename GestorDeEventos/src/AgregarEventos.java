import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AgregarEventos {
    private JPanel panel1;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;
    private JButton cargarButton;
    //private DefaultTableModel tableModel;  // Agrega una referencia al modelo de tabla
private Inicio ventana;
    public AgregarEventos(Inicio origen) {
    //    this.tableModel = tableModel;
     this.ventana = origen;
        cargarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtén los datos ingresados en los campos de texto
                String nombre = textField1.getText();
                String fechaHora = textField2.getText();
                String ubicacion = textField3.getText();
                String descripcion = textField4.getText();
                double presupuesto = Double.parseDouble(textField5.getText());
                int cantidadInvitados = Integer.parseInt(textField6.getText());


                String jdbcUrl = "jdbc:mysql://localhost/gestion_eventos";
                String usuario = "root";
                String contraseña = "HoyTomeReliveran432";

                try {
                    Connection connection = DriverManager.getConnection(jdbcUrl, usuario, contraseña);

                    String sql = "INSERT INTO eventos (nombre, fecha_hora, ubicacion, descripcion, presupuesto, cantidad_invitados) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, nombre);
                    preparedStatement.setString(2, fechaHora);
                    preparedStatement.setString(3, ubicacion);
                    preparedStatement.setString(4, descripcion);
                    preparedStatement.setDouble(5, presupuesto);
                    preparedStatement.setInt(6, cantidadInvitados);

                    int filasInsertadas = preparedStatement.executeUpdate();

                    if (filasInsertadas > 0) {
                        JOptionPane.showMessageDialog(null, "El evento ha sido agregado a la base de datos.");


                        Object[] rowData = {null, nombre, fechaHora, ubicacion, descripcion, presupuesto, cantidadInvitados};
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
        JFrame frame = new JFrame("Agregar Evento");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
