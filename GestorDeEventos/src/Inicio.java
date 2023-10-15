import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;


public class Inicio {

    private JPanel panelinicio;
    private JTable table1;
    private JButton agregarEventoButton ;
    private JButton tareasYToDoButton;
    private JButton modificarEventoButton;
    private JButton eliminarEventoButton;
    private JButton listasDeInvitadosButton;
    private JButton actualizarEventosButton;
    private DefaultTableModel tableModel;


    public Inicio() {
        tableModel =new DefaultTableModel();
       // tableModel = (DefaultTableModel) table1.getModel();
        actualizarTabla();
        // Configurar la conexión a la base de datos
     /*   String jdbcUrl = "jdbc:mysql://localhost/gestion_eventos";
        String usuario = "root";
        String contraseña = "HoyTomeReliveran432";

        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, usuario, contraseña);

            // Crear una consulta SQL para recuperar los datos de la tabla "eventos"
            String sql = "SELECT * FROM eventos";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            // Crear un modelo de tabla para almacenar los datos
            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("ID");
            tableModel.addColumn("Nombre");
            tableModel.addColumn("Fecha/Hora");
            tableModel.addColumn("Ubicación");
            tableModel.addColumn("Descripción");
            tableModel.addColumn("Presupuesto");
            tableModel.addColumn("cantidad_invitados");

            // Llenar la tabla con los datos de la consulta
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nombre = resultSet.getString("nombre");
                String fechaHora = resultSet.getString("fecha_hora");
                String ubicacion = resultSet.getString("ubicacion");
                String descripcion = resultSet.getString("descripcion");
                double presupuesto = resultSet.getDouble("presupuesto");
                int cantidad_invitados = resultSet.getInt("cantidad_invitados");

                tableModel.addRow(new Object[]{id, nombre, fechaHora, ubicacion, descripcion, presupuesto,cantidad_invitados});
            }

            // Establecer el modelo de tabla en el JTable
            table1.setModel(tableModel);

            // Cerrar la conexión a la base de datos
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        Inicio ventana =this;
        agregarEventoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AgregarEventos AG = new AgregarEventos(ventana);
                AG.setVisible(true);
            }
        });
        tareasYToDoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Tareas TA = new Tareas();
                TA.setVisible(true);
            }
        });
        listasDeInvitadosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Listas LI = new Listas ();
                LI.setVisible(true);
            }
        });
        eliminarEventoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Esta es la fila que selecciono en la tabla
                int filaSeleccionada = table1.getSelectedRow();


                if (filaSeleccionada != -1) {  //Si fue seleccionada hace tal cosa
                // System.out.println(table1.getValueAt(filaSeleccionada,0));
                    int idEvento = (int) table1.getValueAt(filaSeleccionada, 0);// aca agarra el id del evento


                    String jdbcUrl = "jdbc:mysql://localhost/gestion_eventos";
                    String usuario = "root";
                    String contraseña = "HoyTomeReliveran432";

                    try {
                        Connection connection = DriverManager.getConnection(jdbcUrl, usuario, contraseña);


                        String sql = "DELETE FROM eventos WHERE id = ?";// Aca lo elimino
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
        modificarEventoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    int filaSeleccionada = table1.getSelectedRow();

                    if (filaSeleccionada != -1) {
                        // Obtén los valores actuales de la fila seleccionada
                        int idEvento = (int) table1.getValueAt(filaSeleccionada, 0);
                        String nombreActual = (String) table1.getValueAt(filaSeleccionada, 1);
                        String fechaHoraActual = (String) table1.getValueAt(filaSeleccionada, 2);
                        String ubicacionActual = (String) table1.getValueAt(filaSeleccionada, 3);
                        String descripcionActual = (String) table1.getValueAt(filaSeleccionada, 4);
                        double presupuestoActual = (double) table1.getValueAt(filaSeleccionada, 5);
                        int cantidadInvitadosActual = (int) table1.getValueAt(filaSeleccionada, 6);

                        // Aquí debes abrir un cuadro de diálogo o un formulario donde el usuario pueda ingresar nuevos valores
                        // y actualizar los valores actuales

                        // Después de que el usuario haya ingresado los nuevos valores, puedes actualizar la base de datos.
                        String nuevoNombre = JOptionPane.showInputDialog("Nuevo nombre", nombreActual);
                        String nuevaFechaHora = JOptionPane.showInputDialog("Nueva fecha/hora", fechaHoraActual);
                        String nuevaUbicacion = JOptionPane.showInputDialog("Nueva ubicación", ubicacionActual);
                        String nuevaDescripcion = JOptionPane.showInputDialog("Nueva descripción", descripcionActual);
                        String nuevoPresupuestoStr = JOptionPane.showInputDialog("Nuevo presupuesto", presupuestoActual);
                        double nuevoPresupuesto = Double.parseDouble(nuevoPresupuestoStr);
                        String nuevaCantidadInvitadosStr = JOptionPane.showInputDialog("Nueva cantidad de invitados", cantidadInvitadosActual);
                        int nuevaCantidadInvitados = Integer.parseInt(nuevaCantidadInvitadosStr);

                        // Actualiza los datos en la base de datos
                        String jdbcUrl = "jdbc:mysql://localhost/gestion_eventos";
                        String usuario = "root";
                        String contraseña = "HoyTomeReliveran432";

                        try {
                            Connection connection = DriverManager.getConnection(jdbcUrl, usuario, contraseña);

                            String sql = "UPDATE eventos SET nombre = ?, fecha_hora = ?, ubicacion = ?, descripcion = ?, presupuesto = ?, cantidad_invitados = ? WHERE id = ?";
                            PreparedStatement preparedStatement = connection.prepareStatement(sql);
                            preparedStatement.setString(1, nuevoNombre);
                            preparedStatement.setString(2, nuevaFechaHora);
                            preparedStatement.setString(3, nuevaUbicacion);
                            preparedStatement.setString(4, nuevaDescripcion);
                            preparedStatement.setDouble(5, nuevoPresupuesto);
                            preparedStatement.setInt(6, nuevaCantidadInvitados);
                            preparedStatement.setInt(7, idEvento);

                            int filasActualizadas = preparedStatement.executeUpdate();

                            if (filasActualizadas > 0) {
                                JOptionPane.showMessageDialog(null, "El evento ha sido actualizado en la base de datos.");

                                // Actualiza las celdas en la tabla con los nuevos valores
                                table1.setValueAt(nuevoNombre, filaSeleccionada, 1);
                                table1.setValueAt(nuevaFechaHora, filaSeleccionada, 2);
                                table1.setValueAt(nuevaUbicacion, filaSeleccionada, 3);
                                table1.setValueAt(nuevaDescripcion, filaSeleccionada, 4);
                                table1.setValueAt(nuevoPresupuesto, filaSeleccionada, 5);
                                table1.setValueAt(nuevaCantidadInvitados, filaSeleccionada, 6);
                            } else {
                                JOptionPane.showMessageDialog(null, "No se han realizado cambios en el evento.");
                            }

                            connection.close();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Por favor, selecciona un evento para modificar.");
                    }
                }

        });
        actualizarEventosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarTabla();
            }
        });
    }

    public void actualizarTabla() {
        try {
            String jdbcUrl = "jdbc:mysql://localhost/gestion_eventos";
            String usuario = "root";
            String contraseña = "HoyTomeReliveran432";

            Connection connection = DriverManager.getConnection(jdbcUrl, usuario, contraseña);

            String sql = "SELECT * FROM eventos";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

           // tableModel.setRowCount(0);
            tableModel = new DefaultTableModel();
            tableModel.addColumn("ID");
            tableModel.addColumn("Nombre");
            tableModel.addColumn("Fecha/Hora");
            tableModel.addColumn("Ubicación");
            tableModel.addColumn("Descripción");
            tableModel.addColumn("Presupuesto");
            tableModel.addColumn("cantidad_invitados");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nombre = resultSet.getString("nombre");
                String fechaHora = resultSet.getString("fecha_hora");
                String ubicacion = resultSet.getString("ubicacion");
                String descripcion = resultSet.getString("descripcion");
                double presupuesto = resultSet.getDouble("presupuesto");
                int cantidad_invitados = resultSet.getInt("cantidad_invitados");

                tableModel.addRow(new Object[]{ id,nombre, fechaHora, ubicacion, descripcion, presupuesto, cantidad_invitados});
            }


            connection.close();
            table1.setModel(tableModel);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al actualizar la tabla: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Inicio");
        frame.setContentPane(new Inicio().panelinicio);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


}
