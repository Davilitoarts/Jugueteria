import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.text.DecimalFormat;

class Jugueteria {
    private List<Cliente> clientes;

    public Jugueteria() {
        clientes = new ArrayList<>();
    }

    public void addCliente(Cliente cliente) {
        clientes.add(cliente);
    }

    public List<Cliente> listarClientes() {
        List<Cliente> clientesOrdenados = new ArrayList<>(clientes);
        Collections.sort(clientesOrdenados, Comparator.comparing(Cliente::getNombre));
        return clientesOrdenados;
    }

    public List<Juguete> listarJuguetesPorCliente(String nombreCliente) {
        for (Cliente cliente : clientes) {
            if (cliente.getNombre().equals(nombreCliente)) {
                return cliente.getJuguetes();
            }
        }
        return new ArrayList<>();
    }

    public void guardarGastoClientes() throws IOException {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        FileWriter writer = null;
        try {
            writer = new FileWriter("gastos_clientes.txt");
            for (Cliente cliente : clientes) {
                String gastoFormateado = decimalFormat.format(cliente.calcularGastoTotal())+"€";
                writer.write(cliente.getNombre() + ": " + gastoFormateado + "\n");
            }
            System.out.println("\nSe ha guardado el gasto de los clientes en el archivo 'gastos_clientes.txt'.");
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}

class Cliente {
    private String nombre;
    private List<Juguete> juguetes;

    public Cliente(String nombre) {
        this.nombre = nombre;
        juguetes = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void addJuguete(Juguete juguete) {
        juguetes.add(juguete);
    }

    public List<Juguete> getJuguetes() {
        return juguetes;
    }

    public double calcularGastoTotal() {
        double total = 0;
        for (Juguete juguete : juguetes) {
            total += juguete.getPrecioFinal();
        }
        return total;
    }
}

abstract class Juguete {
    private String nombre;
    private double precioBase;

    public Juguete(String nombre, double precioBase) {
        this.nombre = nombre;
        this.precioBase = precioBase;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public abstract double getPrecioFinal();

    public abstract String getInfo();
}

class JugueteElectronico extends Juguete {
    private int edadMinima;
    private int numJugadores;

    public JugueteElectronico(String nombre, double precioBase, int edadMinima, int numJugadores) {
        super(nombre, precioBase);
        this.edadMinima = edadMinima;
        this.numJugadores = numJugadores;
    }

    public int getEdadMinima() {
        return edadMinima;
    }

    public int getNumJugadores() {
        return numJugadores;
    }

    @Override
    public double getPrecioFinal() {
        return getPrecioBase() * 1.1; // Aplicar recargo del 10%
    }

    @Override
    public String getInfo() {
        return getNombre() + " - Precio: " + String.format("%.2f", getPrecioFinal()) +
                "€ - Edad mínima: " + getEdadMinima() +
                " - Número de jugadores: " + getNumJugadores();
    }
}

class JugueteEducativo extends Juguete {
    private int edadMinima;
    private int numJugadores;

    public JugueteEducativo(String nombre, double precioBase, int edadMinima, int numJugadores) {
        super(nombre, precioBase);
        this.edadMinima = edadMinima;
        this.numJugadores = numJugadores;
    }

    public int getEdadMinima() {
        return edadMinima;
    }

    public int getNumJugadores() {
        return numJugadores;
    }

    @Override
    public double getPrecioFinal() {
        return getPrecioBase() * 0.8; // Aplicar descuento del 20%
    }

    @Override
    public String getInfo() {
        return getNombre() + " - Precio: " + String.format("%.2f", getPrecioFinal()) +
                "€ - Edad mínima: " + getEdadMinima() +
                " - Número de jugadores: " + getNumJugadores();
    }
}

public class Main {
    public static void main(String[] args) {
        try {
            Jugueteria jugueteria = new Jugueteria();

            Cliente cliente1 = new Cliente("David Serrano Diaz");
            cliente1.addJuguete(new JugueteElectronico("Húndeme si puedes electrónico", 34.99, 7, 2));
            cliente1.addJuguete(new JugueteEducativo("Tangram de madera", 11.99, 5, 4));
            cliente1.addJuguete(new JugueteElectronico("Tamagotchi Original", 23.99, 8, 1));
            jugueteria.addCliente(cliente1);

            Cliente cliente2 = new Cliente("Verónica Gómez Calvo");
            cliente2.addJuguete(new JugueteEducativo("Lince edición familia", 29.99, 6, -1));
            cliente2.addJuguete(new JugueteEducativo("Yo aprendo a leer", 25.99, 5, -1));
            cliente2.addJuguete(new JugueteElectronico("Scientific tools robot tibo", 39.99, 8, -1));
            jugueteria.addCliente(cliente2);

            Cliente cliente3 = new Cliente("Noemí Calvo Ballesteros");
            cliente3.addJuguete(new JugueteEducativo("Operación", 27.99, 6, -1));
            cliente3.addJuguete(new JugueteEducativo("Monopoly clásico", 29.99, 8, -1));
            jugueteria.addCliente(cliente3);
            System.out.println("");
            List<Juguete> juguetesCliente1 = jugueteria.listarJuguetesPorCliente("David Serrano Diaz");
            System.out.println("Juguetes de David Serrano Diaz:");
            for (Juguete juguete : juguetesCliente1) {
                System.out.println(juguete.getInfo());
            }
            System.out.println("");
            List<Juguete> juguetesCliente2 = jugueteria.listarJuguetesPorCliente("Verónica Gómez Calvo");
            System.out.println("Juguetes de Verónica Gómez Calvo:");
            for (Juguete juguete : juguetesCliente2) {
                System.out.println(juguete.getInfo());
            }
            System.out.println("");
            List<Juguete> juguetesCliente3 = jugueteria.listarJuguetesPorCliente("Noemí Calvo Ballesteros");
            System.out.println("Juguetes de Noemí Calvo Ballesteros:");
            for (Juguete juguete : juguetesCliente3) {
                System.out.println(juguete.getInfo());
            }

            jugueteria.guardarGastoClientes();
            System.out.println("\nSe ha guardado el gasto de los clientes en el archivo 'gastos_clientes.txt'.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}