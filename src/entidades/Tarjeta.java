package entidades;

import java.time.LocalDate;

public class Tarjeta {
    private String numero;       // Formato 'XXXX XXXX XXXX XXXX'
    private int limite;
    private int consumo;
    private String proveedor;    // VISA, MC, AMEX
    private LocalDate vencimiento;
    private String cvv;
    private String nombreTitular;
    private String dniUsuario;   // FK al dueño de la tarjeta

    public Tarjeta() {}

    public Tarjeta(String numero, int limite, int consumo, String proveedor,
                   LocalDate vencimiento, String cvv, String nombreTitular, String dniUsuario) {
        this.numero = numero;
        this.limite = limite;
        this.consumo = consumo;
        this.proveedor = proveedor;
        this.vencimiento = vencimiento;
        this.cvv = cvv;
        this.nombreTitular = nombreTitular;
        this.dniUsuario = dniUsuario;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public int getLimite() {
        return limite;
    }

    public void setLimite(int limite) {
        this.limite = limite;
    }

    public int getConsumo() {
        return consumo;
    }

    public void setConsumo(int consumo) {
        this.consumo = consumo;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public LocalDate getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(LocalDate vencimiento) {
        this.vencimiento = vencimiento;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getNombreTitular() {
        return nombreTitular;
    }

    public void setNombreTitular(String nombreTitular) {
        this.nombreTitular = nombreTitular;
    }

    public String getDniUsuario() {
        return dniUsuario;
    }

    public void setDniUsuario(String dniUsuario) {
        this.dniUsuario = dniUsuario;
    }
}
