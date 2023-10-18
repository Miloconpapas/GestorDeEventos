import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class Listas {
    private Connection connection;
    private JPanel panelListas;
    private JTable table1;
    private JTable table2;
    private DefaultTableModel modeloTabla1;
    private DefaultTableModel modeloTabla2;
    private JComboBox<String> comboBox1;
    private JButton agregarButton;
    private JButton modificarButton;
    private JButton eliminarButton;

    public Listas() {
        String jdbcUrl = "jdbc:mysql://localhost/gestion_eventos";
        String usuario = "root";
        String contraseña = "HoyTomeReliveran432";

        try {
            connection = DriverManager.getConnection(jdbcUrl, usuario, contraseña);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT nombre FROM eventos");

            while (resultSet.next()) {
                String nombreEvento = resultSet.getString("nombre");
                comboBox1.addItem(nombreEvento);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        agregarColumnasTabla1();
        agregarColumnasTabla2();


        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String eventoSeleccionado = (String) comboBox1.getSelectedItem();

                    // Obtener el ID del evento seleccionado
                    int idSeleccionado = obtenerIdDelEvento(eventoSeleccionado);

                    String sql = "SELECT * FROM eventos WHERE id = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setInt(1, idSeleccionado);

                    ResultSet resultSet = preparedStatement.executeQuery();

                    limpiarTablas();


                    String sqlTabla1 = "SELECT * FROM invitados WHERE evento_id = ?";
                    PreparedStatement preparedStatementTabla1 = connection.prepareStatement(sqlTabla1);
                    preparedStatementTabla1.setInt(1, idSeleccionado);

                    ResultSet resultSetTabla1 = preparedStatementTabla1.executeQuery();
                    llenarTabla1(resultSetTabla1);


                    String sqlTabla2 = "SELECT * FROM eventos WHERE id = ?";
                    PreparedStatement preparedStatementTabla2 = connection.prepareStatement(sqlTabla2);
                    preparedStatementTabla2.setInt(1, idSeleccionado);

                    ResultSet resultSetTabla2 = preparedStatementTabla2.executeQuery();
                    llenarTabla2(resultSetTabla2);

                    preparedStatement.close();

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String eventoSeleccionado = (String) comboBox1.getSelectedItem();
                if (eventoSeleccionado != null) {
                    try {
                        int idActivo = obtenerIdDelEvento(eventoSeleccionado);

                        // Obtener los datos del nuevo invitado (nombre y cantidad) desde el usuario
                        String nombreInvitado = JOptionPane.showInputDialog("Nombre del invitado:");
                        String cantidadInvitadosStr = JOptionPane.showInputDialog("Cantidad de invitados:");

                        if (nombreInvitado != null && !nombreInvitado.isEmpty() && cantidadInvitadosStr != null) {
                            try {
                                int cantidadInvitados = Integer.parseInt(cantidadInvitadosStr);


                                String sqlInsert = "INSERT INTO invitados (evento_id, nombre, cantidad) VALUES (?, ?, ?)";
                                PreparedStatement preparedStatementInsert = connection.prepareStatement(sqlInsert);
                                preparedStatementInsert.setInt(1, idActivo);
                                preparedStatementInsert.setString(2, nombreInvitado);
                                preparedStatementInsert.setInt(3, cantidadInvitados);

                                preparedStatementInsert.executeUpdate();
                                preparedStatementInsert.close();

                                // Actualizar la tabla
                                Object[] fila = {idActivo, nombreInvitado, cantidadInvitados};
                                modeloTabla1.addRow(fila);
                            } catch (NumberFormatException ex) {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(panelListas, "La cantidad de invitados debe ser un número válido.");
                            }
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(panelListas, "Error al agregar el invitado." + ex.getMessage());
                    }
                }
            }
        });

        modificarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table1.getSelectedRow();
                if (selectedRow != -1) {

                    int id = (int) modeloTabla1.getValueAt(selectedRow, 0);
                    String nombre = (String) modeloTabla1.getValueAt(selectedRow, 1);
                    Object value = modeloTabla1.getValueAt(selectedRow, 2); // Obtiene el valor como un objeto

                    int cantidad;

                    if (value instanceof Integer) {
                        cantidad = (int) value;
                    } else if (value instanceof String) {
                        try {
                            cantidad = Integer.parseInt((String) value);
                        } catch (NumberFormatException ex) {
                            // Maneja el caso en el que el valor no se puede convertir a un entero
                            cantidad = 0; // O utiliza un valor predeterminado
                        }
                    } else {
                        // Maneja otros tipos de datos si es necesario
                        cantidad = 0; // O utiliza un valor predeterminado
                    }

                    // Permite al usuario realizar modificaciones
                    String nuevoNombre = JOptionPane.showInputDialog("Nuevo nombre del invitado:", nombre != null ? nombre : "");
                    String nuevaCantidadStr = JOptionPane.showInputDialog("Nueva cantidad de invitados:");

                    // Verifica que el valor ingresado sea válido para la cantidad.
                    try {
                        int nuevaCantidad = Integer.parseInt(nuevaCantidadStr);

                        // Actualiza la fila en la tabla con los datos modificados
                        modeloTabla1.setValueAt(nuevoNombre, selectedRow, 1);
                        modeloTabla1.setValueAt(nuevaCantidad, selectedRow, 2);

                        try {
                            // Ejecuta una consulta SQL para actualizar la fila en la base de datos
                            String sql = "UPDATE invitados SET nombre = ?, cantidad = ? WHERE id = ?";
                            PreparedStatement preparedStatement = connection.prepareStatement(sql);
                            preparedStatement.setString(1, nuevoNombre);
                            preparedStatement.setInt(2, nuevaCantidad);
                            preparedStatement.setInt(3, id);
                            preparedStatement.executeUpdate();
                            preparedStatement.close();

                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(panelListas, "Error al modificar el invitado en la base de datos: " + ex.getMessage());
                        }
                    } catch (NumberFormatException ex) {
                        // Manejar el caso en el que la conversión de cantidad falle, por ejemplo, mostrar un mensaje de error.
                        JOptionPane.showMessageDialog(panelListas, "Error: Ingresa un número válido para la cantidad de invitados.");
                    }
                }
            }
        });

        eliminarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table1.getSelectedRow();
                if (selectedRow != -1) {

                    int idInvitado = (int) modeloTabla1.getValueAt(selectedRow, 0);


                    modeloTabla1.removeRow(selectedRow);

                    try {

                        String sql = "DELETE FROM invitados WHERE id = ?";
                        PreparedStatement preparedStatement = connection.prepareStatement(sql);
                        preparedStatement.setInt(1, idInvitado);
                        preparedStatement.executeUpdate();
                        preparedStatement.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(panelListas, "Error al eliminar el invitado de la base de datos: " + ex.getMessage());
                    }
                }
            }
        });

    }


    private int obtenerIdDelEvento(String nombreEvento) throws SQLException {
        String sql = "SELECT id FROM eventos WHERE nombre = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, nombreEvento);

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getInt("id");
        } else {

            throw new SQLException("El evento no se encuentra en la base de datos.");
        }
    }

    private void agregarColumnasTabla1() {
        modeloTabla1 = (DefaultTableModel) table1.getModel();
        modeloTabla1.addColumn("id");
        modeloTabla1.addColumn("nombre");
        modeloTabla1.addColumn("cantidad");
        modeloTabla1.addColumn("evento_id");

        table1.setRowSelectionAllowed(true);
        table1.setColumnSelectionAllowed(false);
        table1.setDefaultEditor(Object.class, null);
    }


    private void agregarColumnasTabla2() {
        modeloTabla2 = (DefaultTableModel) table2.getModel();
        modeloTabla2.addColumn("id");
        modeloTabla2.addColumn("nombre");
        modeloTabla2.addColumn("fecha/Hora");
        modeloTabla2.addColumn("ubicación");
        modeloTabla2.addColumn("descripción");
        modeloTabla2.addColumn("presupuesto");
        modeloTabla2.addColumn("cantidad_invitados");

        table2.setDefaultEditor(Object.class, null);
    }


    private void llenarTabla2(ResultSet resultSetTabla2) throws SQLException {

        DefaultTableModel modeloTabla2 = (DefaultTableModel) table2.getModel();
        modeloTabla2.setRowCount(0);

        while (resultSetTabla2.next()) {
            int id = resultSetTabla2.getInt("id");
            String nombre = resultSetTabla2.getString("nombre");
            String fechaHora = resultSetTabla2.getString("fecha_hora");
            String ubicacion = resultSetTabla2.getString("ubicacion");
            String descripcion = resultSetTabla2.getString("descripcion");
            double presupuesto = resultSetTabla2.getDouble("presupuesto");
            int cantidad_invitados = resultSetTabla2.getInt("cantidad_invitados");

            modeloTabla2.addRow(new Object[]{ id,nombre, fechaHora, ubicacion, descripcion, presupuesto, cantidad_invitados});
        }

        resultSetTabla2.close();
        table2.setModel(modeloTabla2);
    }

    private void llenarTabla1(ResultSet resultSetTabla1) throws SQLException {
        DefaultTableModel modeloTabla1 = (DefaultTableModel) table1.getModel();
        modeloTabla1.setRowCount(0);

        while (resultSetTabla1.next()) {
            int id = resultSetTabla1.getInt("id");
            String nombre = resultSetTabla1.getString("nombre");
            String cantidad = resultSetTabla1.getString("cantidad");
            String evento_id = resultSetTabla1.getString("evento_id");

            modeloTabla1.addRow(new Object[]{ id,nombre, cantidad, evento_id});
        }

        table1.setModel(modeloTabla1);
    }

    private void limpiarTablas() {
        DefaultTableModel modeloTabla1 = (DefaultTableModel) table1.getModel();
        modeloTabla1.setRowCount(0);

        DefaultTableModel modeloTabla2 = (DefaultTableModel) table2.getModel();
        modeloTabla2.setRowCount(0);
    }


    public void setVisible(boolean b) {
        JFrame frame = new JFrame("Listas de invitados");
        frame.setContentPane(new Listas().panelListas);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }
}
