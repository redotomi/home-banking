package entidades;

import java.sql.Timestamp;

public class Transferencia {
    private int id;
    private String cbuCuentaOrigen;
    private String cbuCuentaDestino;
    private String moneda;
    private int monto;
    private Timestamp fecha;

    public Transferencia() {}

    public Transferencia(int id, String cbuCuentaOrigen, String cbuCuentaDestino, String moneda, int monto, Timestamp fecha) {
        this.id = id;
        this.cbuCuentaOrigen = cbuCuentaOrigen;
        this.cbuCuentaDestino = cbuCuentaDestino;
        this.moneda = moneda;
        this.monto = monto;
        this.fecha = fecha;
    }

    public Transferencia(String cbuCuentaOrigen, String cbuCuentaDestino, String moneda, int monto, Timestamp fecha) {
        this.cbuCuentaOrigen = cbuCuentaOrigen;
        this.cbuCuentaDestino = cbuCuentaDestino;
        this.moneda = moneda;
        this.monto = monto;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCbuCuentaOrigen() {
        return cbuCuentaOrigen;
    }

    public void setCbuCuentaOrigen(String cbuCuentaOrigen) {
        this.cbuCuentaOrigen = cbuCuentaOrigen;
    }

    public String getCbuCuentaDestino() {
        return cbuCuentaDestino;
    }

    public void setCbuCuentaDestino(String cbuCuentaDestino) {
        this.cbuCuentaDestino = cbuCuentaDestino;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public int getMonto() {
        return monto;
    }

    public void setMonto(int monto) {
        this.monto = monto;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }
}
