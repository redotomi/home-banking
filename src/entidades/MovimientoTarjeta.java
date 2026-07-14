package entidades;

import java.time.LocalDateTime;

public class MovimientoTarjeta {
    private long id;
    private String numeroTarjeta;
    private int monto;
    private LocalDateTime fecha;
    private String referencia;

    public MovimientoTarjeta() {}

    public MovimientoTarjeta(String numeroTarjeta, int monto, LocalDateTime fecha, String referencia) {
        this.numeroTarjeta = numeroTarjeta;
        this.monto = monto;
        this.fecha = fecha;
        this.referencia = referencia;
    }

    public MovimientoTarjeta(long id, String numeroTarjeta, int monto, LocalDateTime fecha, String referencia) {
        this.id = id;
        this.numeroTarjeta = numeroTarjeta;
        this.monto = monto;
        this.fecha = fecha;
        this.referencia = referencia;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public int getMonto() {
        return monto;
    }

    public void setMonto(int monto) {
        this.monto = monto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
}
