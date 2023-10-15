import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Tareas {

    private JPanel panelTareas;
    private JTable table1;
    private JButton agregarTareaButton;
    private JButton eliminarTareaButton;
    private DefaultTableModel tableModel;

    public Tareas(){
        tableModel =new DefaultTableModel();
        actualizarTabla();




        eliminarTareaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int filaSeleccionada = table1.getSelectedRow();


                if (filaSeleccionada != -1) {  //Si fue seleccionada hace tal cosa
                    // System.out.println(table1.getValueAt(filaSeleccionada,0));
                    int idEvento = (int) table1.getValueAt(filaSeleccionada, 0);// aca agarra el id del evento


                    String jdbcUrl = "jdbc:mysql://localhost/gestion_eventos";
                    String usuario = "root";
                    String contraseña = "HoyTomeReliveran432";

                    try {
                        Connection connection = DriverManager.getConnection(jdbcUrl, usuario, contraseña);


                        String sql = "DELETE FROM todo_list WHERE id = ?";
                        PreparedStatement preparedStatement = connection.prepareStatement(sql);
                        preparedStatement.setInt(1, idEvento);
                        int filasEliminadas = preparedStatement.executeUpdate();

                        if (filasEliminadas > 0) {
                            JOptionPane.showMessageDialog(null, "El evento ha sido eliminado.");

                            // Aca actualizo la tabla después de la eliminación :D
                            DefaultTableModel model = (DefaultTableModel) table1.getModel();
                            model.removeRow(filaSeleccionada);
                        } else {
                            JOptionPane.showMessageDialog(null, "No se ha podido eliminar el evento.");
                        }


                        connection.close();  // Cierro la conexin a la bBD
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor, selecciona un evento para eliminar.");
                }
            }
        });
        Tareas ventana =this;
        agregarTareaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                AgregarTareas AT = new AgregarTareas(ventana);
                AT.setVisible(true);


            }
        });
    }





    public void actualizarTabla() {
        try {
            String jdbcUrl = "jdbc:mysql://localhost/gestion_eventos";
            String usuario = "root";
            String contraseña = "HoyTomeReliveran432";

            Connection connection = DriverManager.getConnection(jdbcUrl, usuario, contraseña);

            String sql = "SELECT * FROM todo_list";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            // tableModel.setRowCount(0);
            tableModel = new DefaultTableModel();
            tableModel.addColumn("ID");
            tableModel.addColumn("fecha_creacion");
            tableModel.addColumn("tarea");
            tableModel.addColumn("completada");



            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String fecha_creacion= resultSet.getString("fecha_creacion");
                String tarea = resultSet.getString("tarea");
                String completada= resultSet.getString("completada");



                tableModel.addRow(new Object[]{ id,fecha_creacion,tarea, completada});
            }


            connection.close();
            table1.setModel(tableModel);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al actualizar la tabla: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void setVisible(boolean b) {
        JFrame frame = new JFrame("Tareas Y To-Do List");
        frame.setContentPane(new Tareas().panelTareas);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }
}
