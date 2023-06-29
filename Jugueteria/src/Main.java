import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import static java.util.Comparator.*;

class EinvalidPropertyException extends Exception {
    public EinvalidPropertyException(String message) {
        super(message);
    }
}
class Juguete implements Comparable<Juguete> {
    private final String nombre;
    private final Integer precioBase;

    public Juguete(String nombre, Integer edadMinima, Integer precioBase) throws EinvalidPropertyException {
        if (nombre == null || nombre.isEmpty()) {
            throw new EinvalidPropertyException("El nombre del juguete es obligatorio.");
        }
        if (edadMinima == null || edadMinima <= 0) {
            throw new EinvalidPropertyException("La edad mínima del juguete debe ser mayor que 0.");
        }
        if (precioBase == null || precioBase <= 0) {
            throw new EinvalidPropertyException("El precio base del juguete debe ser mayor que 0.");
        }
        this.nombre = nombre;
        this.precioBase = precioBase;
    }
    public String getNombre() {
        return nombre;
    }
    public Integer getPrecioBase() {
        return precioBase;
    }
    public Integer getPrecio() {
        return precioBase;
    }
    @Override
    public int compareTo(Juguete juguete) {
        return this.precioBase.compareTo(juguete.getPrecioBase());
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Juguete other = (Juguete) obj;
        return Objects.equals(nombre, other.nombre);
    }
    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }
}
class JugueteEducativo extends Juguete {
    private final Integer bonificacionPrecio;

    public JugueteEducativo(String nombre, Integer edadMinima, Integer precioBase, Integer edadMaxima, Integer bonificacionPrecio) throws EinvalidPropertyException {
        super(nombre, edadMinima, precioBase);
        if (edadMaxima == null || edadMaxima <= edadMinima) {
            throw new EinvalidPropertyException("La edad máxima del juguete educativo debe ser mayor que la edad mínima.");
        }
        if (bonificacionPrecio == null || bonificacionPrecio <= 0 || bonificacionPrecio >= precioBase) {
            throw new EinvalidPropertyException("La bonificación en el precio del juguete educativo debe ser mayor que 0 y menor que el precio base.");
        }
        this.bonificacionPrecio = bonificacionPrecio;
    }
    @Override
    public Integer getPrecio() {
        return getPrecioBase() - bonificacionPrecio;
    }
}
class JugueteElectronico extends Juguete {
    private final Integer recargoPrecio;

    public JugueteElectronico(String nombre, Integer edadMinima, Integer precioBase, Integer recargoPrecio) throws EinvalidPropertyException {
        super(nombre, edadMinima, precioBase);
        if (recargoPrecio == null || recargoPrecio <= 0 || recargoPrecio >= precioBase) {
            throw new EinvalidPropertyException("El recargo en el precio del juguete electrónico debe ser mayor que 0 y menor que el precio base.");
        }
        this.recargoPrecio = recargoPrecio;
    }
    @Override
    public Integer getPrecio() {
        return getPrecioBase() + recargoPrecio;
    }
}
class Cliente {
    private final String nombre;
    private final List<Juguete> juguetes;

    public Cliente(String nombre) {
        this.nombre = nombre;
        this.juguetes = new ArrayList<>();
    }
    public String getNombre() {
        return nombre;
    }
    public void addJuguete(Juguete juguete) throws EinvalidPropertyException {
        if (juguetes.contains(juguete)) {
            throw new EinvalidPropertyException("No se pueden añadir dos juguetes iguales.");
        }
        juguetes.add(juguete);
    }
    public int calcularGasto() {
        int totalGasto = 0;
        for (Juguete juguete : juguetes) {
            totalGasto += juguete.getPrecio();
        }
        return totalGasto;
    }
    public List<Juguete> listarJuguetes() {
        List<Juguete> juguetesOrdenados = new ArrayList<>(juguetes);
        Collections.sort(juguetesOrdenados);
        return juguetesOrdenados;
    }

}
class Jugueteria {
    private final List<Cliente> clientes;

    public Jugueteria() {
        this.clientes = new ArrayList<>();
    }
    public void addCliente(Cliente cliente) {
        Cliente existingCliente = buscarCliente(cliente.getNombre());
        if (existingCliente != null) {
            clientes.remove(existingCliente);
        }
        clientes.add(cliente);
    }
    public List<Juguete> listarJuguetesPorCliente(String nombreCliente) throws EinvalidPropertyException {
        Cliente cliente = buscarCliente(nombreCliente);
        if (cliente == null) {
            throw new EinvalidPropertyException("El cliente no existe.");
        }
        return cliente.listarJuguetes();
    }
    public List<Cliente> listarClientes() {
        List<Cliente> clientesOrdenados;
        clientesOrdenados = new ArrayList<>(clientes);
        clientesOrdenados.sort(comparing(Cliente::getNombre));
        return clientesOrdenados;
    }
    private Cliente buscarCliente(String nombreCliente) {
        for (Cliente cliente : clientes) {
            if (cliente.getNombre().equals(nombreCliente)) {
                return cliente;
            }
        }
        return null;
    }
    public void guardarGastoClientes() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("info.txt"))) {
            for (Cliente cliente : clientes) {
                writer.write(cliente.getNombre() + ": " + cliente.calcularGasto());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }
}
public class Main {
    public static void main(String[] args) {
        try {
            Jugueteria jugueteria = new Jugueteria();

            Cliente cliente1 = new Cliente("Juan");
            cliente1.addJuguete(new Juguete("Pelota", 3, 10));
            cliente1.addJuguete(new JugueteEducativo("Rompecabezas", 5, 20, 8, 5));
            cliente1.addJuguete(new JugueteElectronico("Robot", 8, 30, 7));
            jugueteria.addCliente(cliente1);

            Cliente cliente2 = new Cliente("Ana");
            cliente2.addJuguete(new Juguete("Muñeca", 4, 15));
            cliente2.addJuguete(new JugueteEducativo("Puzzle", 6, 25, 10, 8));
            cliente2.addJuguete(new JugueteElectronico("Videojuego", 10, 40, 10));
            jugueteria.addCliente(cliente2);

            List<Juguete> juguetesCliente1 = jugueteria.listarJuguetesPorCliente("Juan");
            System.out.println("Juguetes de Juan ordenados por precio base:");
            for (Juguete juguete : juguetesCliente1) {
                System.out.println(juguete.getNombre() + ": " + juguete.getPrecioBase());
            }
            List<Cliente> clientesOrdenados = jugueteria.listarClientes();
            System.out.println("\nClientes ordenados por nombre:");
            for (Cliente cliente : clientesOrdenados) {
                System.out.println(cliente.getNombre());
            }
            jugueteria.guardarGastoClientes();
        } catch (EinvalidPropertyException e) {
            e.printStackTrace();
        }
    }
}