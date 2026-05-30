package entidades;

public class Cuenta {
    private String nombreCuenta;
    private String CBU;
    private String alias;
    private String moneda;
    private int monto;

    Cuenta() {}

    Cuenta(String nombreCuenta, String CBU, String alias, String moneda, int monto) {
        this.nombreCuenta = nombreCuenta;
        this.CBU = CBU;
        this.alias = alias;
        this.moneda = moneda;
        this.monto = monto;
    }
}
