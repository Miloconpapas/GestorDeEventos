package vista;

import controlador.EventoControlador;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EventoVista extends JFrame {

    private EventoControlador controlador;
    private JTable tablaEventos;
    private  DefaultTableModel modeloTabla;

    public EventoVista() {
        controlador = new EventoControlador(this);

        // ventana principal
        setTitle("Lista de Eventos");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Creo el modelo de la tabla
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Nombre del Evento");
        modeloTabla.addColumn("Fecha y Hora");
        modeloTabla.addColumn("Ubicación");
        modeloTabla.addColumn("Descripción");
        modeloTabla.addColumn("Presupuesto");
        modeloTabla.addColumn("Cantidad de Invitados");

        // Asignao el modelo a la tabla
        tablaEventos = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaEventos);



        JButton btnAgregarEvento = new JButton("Agregar Evento");
        JButton btnModificarEvento = new JButton("Modificar Evento");
        JButton btnEliminarEvento = new JButton("Eliminar Evento");

        btnAgregarEvento.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controlador.mostrarFormularioAgregarEvento();
            }
        });

        btnModificarEvento.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int filaSeleccionada = tablaEventos.getSelectedRow();
                controlador.mostrarFormularioModificarEvento(filaSeleccionada);
            }
        });

        btnEliminarEvento.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controlador.eliminarEvento(tablaEventos.getSelectedRow());
            }
        });

        //  ventana principal
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnAgregarEvento);
        panelBotones.add(btnModificarEvento);
        panelBotones.add(btnEliminarEvento);

        add(panelBotones, BorderLayout.SOUTH);
        // Llamar al método para inicializar la tabla en el controlador
        controlador.inicializarTabla();
    }

    // Métodos para obtener los datos ingresados por el usuario
    public String obtenerNombreEvento() {
        return JOptionPane.showInputDialog(this, "Ingrese el nombre del evento:");
    }

    public String obtenerFechaHora() {
        return JOptionPane.showInputDialog(this, "Ingrese la fecha y hora del evento (YYYY-MM-DD HH:mm:ss):");
    }

    public String obtenerUbicacion() {
        return JOptionPane.showInputDialog(this, "Ingrese la ubicación del evento:");
    }

    public String obtenerDescripcion() {
        return JOptionPane.showInputDialog(this, "Ingrese la descripción del evento:");
    }

    public double obtenerPresupuesto() {
        String presupuestoStr = JOptionPane.showInputDialog(this, "Ingrese el presupuesto del evento:");
        return Double.parseDouble(presupuestoStr);
    }

    public int obtenerCantidadInvitados() {
        String cantidadInvitadosStr = JOptionPane.showInputDialog(this, "Ingrese la cantidad de invitados:");
        return Integer.parseInt(cantidadInvitadosStr);
    }


    // Obtener el modelo de la tabla
    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }
    // Método para agregar una fila a la tabla
    public void agregarFila(Object[] fila) {
        if (modeloTabla != null) {
            modeloTabla.addRow(fila);
        } else {
            // Si el modeloTabla es nulo.. entonces
            System.out.println("Error: modeloTabla no inicializado");
        }
    }

    // Método para mostrar mensajes al usuario
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    // Método para modificar una fila en la tabla
    public void modificarFila(int fila, Object[] nuevosDatos) {
        for (int i = 0; i < nuevosDatos.length; i++) {
            modeloTabla.setValueAt(nuevosDatos[i], fila, i);
        }
    }

    // Método para mostrar el formulario de modificación con los datos actuales
    public void mostrarFormularioModificarEvento(int idEvento, String nombre, String fechaHora, String ubicacion, String descripcion, double presupuesto, int cantidadInvitados) {
        // Crear un nuevo formulario para la modificación
        JDialog formularioModificacion = new JDialog(this, "Modificar Evento", true);
        formularioModificacion.setSize(400, 300);
        formularioModificacion.setLayout(new GridLayout(7, 2));

        // Crear etiquetas y campos de texto para mostrar los datos actuales
        JLabel lblId = new JLabel("ID:");
        JTextField txtId = new JTextField(String.valueOf(idEvento));
        txtId.setEditable(false);

        JLabel lblNombre = new JLabel("Nombre:");
        JTextField txtNombre = new JTextField(nombre);

        JLabel lblFechaHora = new JLabel("Fecha y Hora:");
        JTextField txtFechaHora = new JTextField(fechaHora);

        JLabel lblUbicacion = new JLabel("Ubicación:");
        JTextField txtUbicacion = new JTextField(ubicacion);

        JLabel lblDescripcion = new JLabel("Descripción:");
        JTextField txtDescripcion = new JTextField(descripcion);

        JLabel lblPresupuesto = new JLabel("Presupuesto:");
        JTextField txtPresupuesto = new JTextField(String.valueOf(presupuesto));

        JLabel lblCantidadInvitados = new JLabel("Cantidad de Invitados:");
        JTextField txtCantidadInvitados = new JTextField(String.valueOf(cantidadInvitados));

        // Botón de confirmación para aplicar la modificación
        JButton btnModificar = new JButton("Modificar");
        btnModificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtener los nuevos datos ingresados por el usuario
                String nuevoNombre = txtNombre.getText();
                String nuevaFechaHora = txtFechaHora.getText();
                String nuevaUbicacion = txtUbicacion.getText();
                String nuevaDescripcion = txtDescripcion.getText();
                double nuevoPresupuesto = Double.parseDouble(txtPresupuesto.getText());
                int nuevaCantidadInvitados = Integer.parseInt(txtCantidadInvitados.getText());

                // Llamar al controlador para aplicar la modificación
                controlador.modificarEvento(idEvento, nuevoNombre, nuevaFechaHora, nuevaUbicacion, nuevaDescripcion, nuevoPresupuesto, nuevaCantidadInvitados);

                // Modificar la fila en la tabla de la vista
                Object[] nuevosDatos = {idEvento, nuevoNombre, nuevaFechaHora, nuevaUbicacion, nuevaDescripcion, nuevoPresupuesto, nuevaCantidadInvitados};
                modificarFila(tablaEventos.getSelectedRow(), nuevosDatos);

                // Cerrar el formulario de modificación
                formularioModificacion.dispose();
            }
        });

        // Añadir componentes al formulario
        formularioModificacion.add(lblId);
        formularioModificacion.add(txtId);
        formularioModificacion.add(lblNombre);
        formularioModificacion.add(txtNombre);
        formularioModificacion.add(lblFechaHora);
        formularioModificacion.add(txtFechaHora);
        formularioModificacion.add(lblUbicacion);
        formularioModificacion.add(txtUbicacion);
        formularioModificacion.add(lblDescripcion);
        formularioModificacion.add(txtDescripcion);
        formularioModificacion.add(lblPresupuesto);
        formularioModificacion.add(txtPresupuesto);
        formularioModificacion.add(lblCantidadInvitados);
        formularioModificacion.add(txtCantidadInvitados);
        formularioModificacion.add(btnModificar);

        formularioModificacion.setVisible(true);
    }

    public static void main(String[] args) {
        EventoVista vista = new EventoVista();
        vista.setVisible(true);
    }
}
