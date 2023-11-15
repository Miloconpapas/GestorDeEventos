package controlador;

import modelo.EventoModelo;
import vista.EventoVista;

import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EventoControlador {

    private EventoVista vista;
    private EventoModelo modelo;

    public EventoControlador(EventoVista vista) {
        this.vista = vista;
        this.modelo = new EventoModelo();

        // Inicializar la tabla con datos de la base de datos
        inicializarTabla();

        // Agregar el controlador como observador de eventos en el modelo
        modelo.addObserver((obj, arg) -> {
            // Actualizar la tabla cuando se agrega un evento
            Object[] fila = (Object[]) arg;
            vista.agregarFila(fila);
        });
    }
    public void inicializarTabla() {
        // agarro eventos de la base de datos y lleno la tabla
        try {
            ResultSet resultSet = modelo.obtenerEventos();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nombre = resultSet.getString("nombre");
                String fechaHora = resultSet.getString("fecha_hora");
                String ubicacion = resultSet.getString("ubicacion");
                String descripcion = resultSet.getString("descripcion");
                double presupuesto = resultSet.getDouble("presupuesto");
                int cantidadInvitados = resultSet.getInt("cantidad_invitados");

                Object[] fila = {id, nombre, fechaHora, ubicacion, descripcion, presupuesto, cantidadInvitados};

                vista.agregarFila(fila);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }






    public void mostrarFormularioModificarEvento(int filaSeleccionada) {
        if (filaSeleccionada != -1) {
            // Obtener los datos del evento seleccionado desde la vista
            String nombre = (String) vista.getModeloTabla().getValueAt(filaSeleccionada, 1);
            String fechaHora = (String) vista.getModeloTabla().getValueAt(filaSeleccionada, 2);
            String ubicacion = (String) vista.getModeloTabla().getValueAt(filaSeleccionada, 3);
            String descripcion = (String) vista.getModeloTabla().getValueAt(filaSeleccionada, 4);
            double presupuesto = (double) vista.getModeloTabla().getValueAt(filaSeleccionada, 5);
            int cantidadInvitados = (int) vista.getModeloTabla().getValueAt(filaSeleccionada, 6);

            // Llamar el método en el modelo para obtener el ID del evento seleccionado
            int idEvento = obtenerIDEvento(filaSeleccionada);

            // aca se abre el formulario de modificación con los datos actuales
            vista.mostrarFormularioModificarEvento(idEvento, nombre, fechaHora, ubicacion, descripcion, presupuesto, cantidadInvitados);
        } else {
            vista.mostrarMensaje("Seleccione un evento para modificar.");
        }
    }

    // Método para obtener el ID del evento seleccionado desde la vista
    private int obtenerIDEvento(int filaSeleccionada) {
        return (int) vista.getModeloTabla().getValueAt(filaSeleccionada, 0);
    }

    public void mostrarFormularioAgregarEvento() {
        // Mostrar elformulario para agregar eventos
        String nombre = vista.obtenerNombreEvento();
        if (nombre != null && !nombre.isEmpty()) {
            String fechaHora = vista.obtenerFechaHora();
            String ubicacion = vista.obtenerUbicacion();
            String descripcion = vista.obtenerDescripcion();
            double presupuesto = vista.obtenerPresupuesto();
            int cantidadInvitados = vista.obtenerCantidadInvitados();

            // Agregar el evento al modelo
            modelo.agregarEvento(nombre, fechaHora, ubicacion, descripcion, presupuesto, cantidadInvitados);
            vista.mostrarMensaje("Evento agregado correctamente");
        }
    }

    public void modificarEvento(int idEvento, String nuevoNombre, String nuevaFechaHora, String nuevaUbicacion, String nuevaDescripcion, double nuevoPresupuesto, int nuevaCantidadInvitados) {
        //método para modificar el evento
        modelo.modificarEvento(idEvento, nuevoNombre, nuevaFechaHora, nuevaUbicacion, nuevaDescripcion, nuevoPresupuesto, nuevaCantidadInvitados);
    }


    public void eliminarEvento(int filaSeleccionada) {
        if (filaSeleccionada != -1) {
            int idEvento = obtenerIDEvento(filaSeleccionada);
            // Llamar al método en el modelo para eliminar el evento
            modelo.eliminarEvento(idEvento);
            // Eliminar la fila de la tabla en la vista
            ((DefaultTableModel) vista.getModeloTabla()).removeRow(filaSeleccionada);
            vista.mostrarMensaje("Evento eliminado correctamente");
        } else {
            vista.mostrarMensaje("Seleccione un evento para eliminar.");
        }
    }
}
