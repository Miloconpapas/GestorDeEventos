package modelo;

import java.sql.*;
import java.util.Observable;

public class EventoModelo extends Observable {

    private static final String URL = "jdbc:mysql://localhost:3306/EventosNuevo";
    private static final String USUARIO = "root";
    private static final String CONTRASEÑA = "root4894756";

    public void agregarEvento(String nombre, String fechaHora, String ubicacion,
                              String descripcion, double presupuesto, int cantidadInvitados) {
        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA)) {
            String query = "INSERT INTO EventosTable " +
                    "(nombre, fecha_hora, ubicacion, descripcion, presupuesto, cantidad_invitados) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, nombre);
                preparedStatement.setString(2, fechaHora);
                preparedStatement.setString(3, ubicacion);
                preparedStatement.setString(4, descripcion);
                preparedStatement.setDouble(5, presupuesto);
                preparedStatement.setInt(6, cantidadInvitados);

                preparedStatement.executeUpdate();
            }


            setChanged();
            notifyObservers(new Object[]{obtenerUltimoID(), nombre, fechaHora, ubicacion,
                    descripcion, presupuesto, cantidadInvitados});

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }










    public ResultSet obtenerEventos() throws SQLException {
        Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
        String query = "SELECT * FROM EventosTable";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        return preparedStatement.executeQuery();
    }

    private int obtenerUltimoID() throws SQLException {
        Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
        String query = "SELECT MAX(id) AS max_id FROM EventosTable";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next() ? resultSet.getInt("max_id") : -1;
    }








    public void modificarEvento(int idEvento, String nombre, String fechaHora, String ubicacion,
                                String descripcion, double presupuesto, int cantidadInvitados) {
        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA)) {
            String query = "UPDATE EventosTable SET nombre=?, fecha_hora=?, ubicacion=?, descripcion=?, presupuesto=?, cantidad_invitados=? WHERE id=?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, nombre);
                preparedStatement.setString(2, fechaHora);
                preparedStatement.setString(3, ubicacion);
                preparedStatement.setString(4, descripcion);
                preparedStatement.setDouble(5, presupuesto);
                preparedStatement.setInt(6, cantidadInvitados);
                preparedStatement.setInt(7, idEvento);

                preparedStatement.executeUpdate();
            }

            setChanged();
            notifyObservers(new Object[]{idEvento, nombre, fechaHora, ubicacion, descripcion, presupuesto, cantidadInvitados});

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminarEvento(int idEvento) {
        try (Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA)) {
            String query = "DELETE FROM EventosTable WHERE id=?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, idEvento);
                preparedStatement.executeUpdate();
            }

            setChanged();
            notifyObservers(new Object[]{idEvento});

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
