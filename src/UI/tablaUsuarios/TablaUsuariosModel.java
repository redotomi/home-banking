package UI.tablaUsuarios;

import entidades.Usuario;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class TablaUsuariosModel extends AbstractTableModel {
    private static final int COLUMNA_ID = 0;
    private static final int COLUMNA_NOMBRE = 1;
    private static final int COLUMNA_APELLIDO = 2;
    private static final int COLUMNA_DNI = 3;


    private String[] nombresColumnas = {"ID", "Nombre", "Apellido", "DNI"};
    private Class[] tiposColumnas = {Integer.class, String.class, String.class, Integer.class, String.class};

    private List<Usuario> filas;


    public TablaUsuariosModel() {
        filas = new ArrayList<Usuario>();
    }

    public TablaUsuariosModel(List<Usuario> contenidoInicial) {
        filas = contenidoInicial;
    }

    public int getColumnCount() {
        return nombresColumnas.length;
    }

    public int getRowCount() {
        return filas.size();
    }


    public void setValueAt(Object value, int row, int col) {
        Usuario usuario = filas.get(row);

        Object result = null;
        switch (col) {
            case COLUMNA_ID:
                usuario.setId((Integer)value);
                break;
            case COLUMNA_NOMBRE:
                usuario.setNombre((String)value);
                break;
            case COLUMNA_APELLIDO:
                usuario.setApellido((String)value);
                break;
            case COLUMNA_DNI:
                usuario.setDni((Integer)value);
                break;
        }
        fireTableCellUpdated(row, col);
    }

    public Object getValueAt(int rowIndex, int columnIndex) {

        Usuario usuario = filas.get(rowIndex);

        Object result = null;
        switch(columnIndex) {
            case COLUMNA_ID:
                result = usuario.getId();
                break;
            case COLUMNA_NOMBRE:
                result = usuario.getNombre();
                break;
            case COLUMNA_APELLIDO:
                result = usuario.getApellido();
                break;
            case COLUMNA_DNI:
                result = usuario.getDni();
                break;
            default:
                result = new String("");
        }

        return result;
    }

    public String getColumnName(int col) {
        return nombresColumnas[col];
    }

    public Class getColumnClass(int col) {
        return tiposColumnas[col];
    }


    public List<Usuario> getFilas() {
        return filas;
    }

    public void setFilas(List<Usuario> filas) {
        this.filas = filas;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex){
        return true;
    }
}
