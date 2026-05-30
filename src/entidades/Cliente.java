package entidades;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Cliente extends Usuario {
    private List<Cuenta> cuentas;

    Cliente() {

    }

    Cliente(int id, String nombre, String apellido, int dni) {
        super(id, nombre, apellido, dni);
        this.cuentas = new ArrayList<Cuenta>();
    }
}
