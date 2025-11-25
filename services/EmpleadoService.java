package enunciado.parcial.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import enunciado.parcial.services.interfaces.IService;
import enunciado.parcial.entities.Empleado;
import enunciado.parcial.repositories.EmpleadoRepository;


public class EmpleadoService implements IService<Empleado, Integer> {

    private final EmpleadoRepository repository;
    private final DepartamentoService departamentoService;
    private final PuestoService puestoService;

    public EmpleadoService() {
        this.repository = new EmpleadoRepository();
        departamentoService = new DepartamentoService();
        puestoService = new PuestoService();
    }

    /**
     * Recupera un autor por ID.
     * - Devuelve null si no existe.
     */
    @Override
    public Empleado getById(Integer id) {
        return repository.getById(id);
    }

    /**
     * Recupera un autor por nombre, y si no existe lo crea.
     */
    @Override
    public Empleado getOrCreateByName(String nombre) {
        Empleado d = repository.getByName(nombre);
        if (d == null) {
            d = new Empleado();
            d.setNombre(nombre);
            repository.create(d);
        }
        return d;
    }

    /**
     * Devuelve todos los autores como lista.
     */
    @Override
    public List<Empleado> getAll() {
        return repository.getAllList();  // usa el genérico
    }

    public void bulkInsert(File fileToImport) throws IOException {
        Files.lines(Paths.get(fileToImport.toURI()))
                .skip(1) // saltear cabecera
                .forEach(linea -> {
                    Empleado emp = this.procesarLinea(linea);
                    this.repository.create(emp);
                });
    }

    private Empleado procesarLinea(String linea) {
        // nombre,edad,fecha_ingreso,salario,empleado_fijo,departamento,puesto
        //  0       1         2        3        4            5            6
        String[] tokens = linea.split(",");

        Empleado empleado = new Empleado();
        empleado.setNombre(tokens[0]);

        // para convetir valores enteros
        empleado.setEdad(Integer.parseInt(tokens[1]));

        // para convertir fechas
        LocalDate fecha = LocalDate.parse(tokens[2]);
        empleado.setFechaIngreso(fecha);

        // para convertir a double
        empleado.setSalario(Double.parseDouble(tokens[3]));

        // para convertir a booleano es
        empleado.setEmpleadoFijo(tokens[4].equalsIgnoreCase("1"));

        String nombre = tokens[5];
        var depa = departamentoService.getOrCreateByName(nombre);
        empleado.setDepartamento(depa);

        nombre = tokens[6];
        var puesto = puestoService.getOrCreateByName(nombre);
        empleado.setPuesto(puesto);

        return empleado;
    }

    /* =================================================================================
     * Opcion 2
     ================================================================================= */
    public Map<Boolean, Long> contarFijosYNoFijos(List<Empleado> empleados) {
        // 3️⃣ Crear un Stream de empleados y dividirlos en dos grupos:
        //      - Clave 'true': empleados fijos
        //      - Clave 'false': empleados no fijos
        //    Luego, contar cuántos hay en cada grupo
        var resultado = empleados.stream()
            .collect(
                Collectors.partitioningBy(     // Agrupa los elementos en dos listas: true y false
                    e -> e.isEmpleadoFijo(),  // Función que devuelve true si el empleado es fijo    Empleado::isEmpleadoFijo
                    Collectors.counting()      // En lugar de guardar listas, cuenta los elementos
                )
            );

        return resultado;
    }

    /*
    public long contarEmpleadosFijos(List<Empleado> empleados, boolean flag) {

        // Contar empleados fijos
        // empleados = [ E1,         E2,             E3] 
        // fijo          True         False        True 
        long contFijos = empleados.stream()
                                .filter(e -> e.isEmpleadoFijo() == flag)        // Empleado::isEmpleadoFijo
                                .count();
        // empleados2 = [E1, E3] --> el tamaño de la lista 2

        /*
        // Contar empleados no fijos
        // empleados2 = [E2]
        long contNoFijos = empleados.stream()
                                    .filter(e -> !e.isEmpleadoFijo())
                                    .count();
 
        return contFijos;
    } */

    /* =================================================================================
     * Opcion 4
     ================================================================================= */
    public Map<String, Long> mapaEmpleadosPorDepartamento(List<Empleado> empleados) {
        // Agrupar y contar por nombre de departamento
        return empleados.stream()
            .collect(Collectors.groupingBy(
                e -> e.getDepartamento().getNombre(),  // Clasifica por nombre del departamento
                Collectors.counting()                  // Cuenta cuántos empleados hay en cada grupo
            ));

        /*/ Contar empleados por departamento
        mapa_departmentos = {
            "It": 0, 
            "Ventas": 0
        }*/
    }

    /* =================================================================================
     * Opcion 5
     ================================================================================= */
    public Map<String, Double> promedioSalariosPorPuesto(List<Empleado> empleados) {
        // 3️⃣ Agrupar por puesto y calcular el salario promedio en una sola pasada
        return empleados.stream()
            .collect(Collectors.groupingBy(
                e -> e.getPuesto().getNombre(),     // Clave: nombre del puesto
                Collectors.averagingDouble(         // Valor: promedio de salario final
                    e -> e.calcularSalarioFinal()  // Función para obtener el salario final Empleado::calcularSalarioFinal
                )
            ));
    }

    /* =================================================================================
     * Opcion 8
     ================================================================================= */ 
    // forma fitrando en la base de datos   
    public List<Empleado> getEmpleadosByAge(int edad){
        return this.repository.getEmpleadosFilterByAge(edad);
    }

    // forma filtrando en java
    public List<Empleado> getEmpleadosByAge(List<Empleado> empleados, int edad){
        return empleados.stream().filter(e -> e.getEdad() == edad).toList();
    }


}
