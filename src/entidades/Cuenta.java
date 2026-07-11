package entidades;

public class Cuenta {
    private String nombreCuenta;
    private String CBU;
    private String alias;
    private String moneda;
    private int monto;

    Cuenta() {}

    public Cuenta(String nombreCuenta, String CBU, String alias, String moneda, int monto) {
        this.nombreCuenta = nombreCuenta;
        this.CBU = CBU;
        this.alias = alias;
        this.moneda = moneda;
        this.monto = monto;
    }

    public String getNombreCuenta() {
        return nombreCuenta;
    }

    public void setNombreCuenta(String nombreCuenta) {
        this.nombreCuenta = nombreCuenta;
    }

    public String getCBU() {
        return CBU;
    }

    public void setCBU(String CBU) {
        this.CBU = CBU;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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
}
