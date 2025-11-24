# Introducción

El siguiente documento tiene por fin servir de guia para la creación de un proyecto Java desde 0. Gran parte, por no decir todo, de lo que describiremos a continuación esta mencionado y detallado a lo largo del material que se fue habilitando a lo largo del cursado. Cabe mencionar también que se verá una forma de realizar esta tarea, pero no es ni la única ni la mejor que existe, es meramente una ejemplificación que agrupa los conocimientos que deberían tener pero que se encuentran dispersos en distintos lugares. Por último, aclarar que en este __paso a paso__ no daremos muchas explicaciones de porqué se hacen de cierta forma algunas cosas, dicha explicación y tratamiento ya se encuentra más que detallada en cada apunte/ficha disponible en UV en su correspondiente sección/capítulo. Sin más introducción, empecemos.

# Consideraciones generales

Se asume en esta etapa que el alumno ya tiene descargado y configurado tanto Java como Maven. Se usará VS Code para el armado de todo el código, pero el IDE debería ser indistinto siempre que consideremos las particularidades y convenciones del lenguaje Java.

# Crear la estructura base del proyecto

Para iniciar, generaremos un nuevo proyecto de Java con Maven. El comando que hemos visto varias veces y que utilizaremos para ello es el siguiente:

``` ps
mvn archetype:generate "-DgroupId=ar.edu.utnfrc.backend" `
"-DartifactId=XXXXX" `
"-DarchetypeGroupId=org.apache.maven.archetypes" `
"-DarchetypeArtifactId=maven-archetype-quickstart" `
"-DinteractiveMode=false"
```

Tenga a bien reemplazar la cadena 'XXXXX' por el nombre deseado para el nuevo proyecto. Para el presente ejemplo dicha cadena será 'empleados'.

# Agregar al POM las dependencias y plugins faltantes

Para permitir la ejecución con el comando `mvn exec:java` es necesario agregar el siguiente extracto:

```xml
...
<plugin>
  <groupId>org.codehaus.mojo</groupId>
  <artifactId>exec-maven-plugin</artifactId>
  <version>3.1.0</version>
  <configuration>
    <mainClass>ar.edu.utnfrc.backend.App</mainClass>
  </configuration>
</plugin>
...
```

El mismo debe quedar comprendido entre los tags generados por el comando anterior:

```xml
...
  <build>
    <pluginManagement>
      <plugins>
        ...
        <plugin>
          <groupId>org.codehaus.mojo</groupId>
          <artifactId>exec-maven-plugin</artifactId>
          <version>3.1.0</version>
          <configuration>
            <mainClass>ar.edu.utnfrc.backend.App</mainClass>
          </configuration>
        </plugin>
      </plugins>
    </pluginManagement>
  </build>
...
```

> Considere que en caso de modificar la estructura inicial generada, es posible que la linea
`<mainClass>ar.edu.utnfrc.backend.App</mainClass>` debiera ser modificada para coincidir con la clase correspondiente.

Para las dependencias, el arquetipo elegido ya incluye JUnit por default, asique solo es necesario agregar Lombok (en caso que lo utilicen) y las dependencias para la DB. En este apartado del POM:

```xml
...
  <dependencies>
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-api</artifactId>
      <scope>test</scope>
    </dependency>
    <!-- Optionally: parameterized tests support -->
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-params</artifactId>
      <scope>test</scope>
    </dependency>
    <!-- Aquí incluiremos las dependencias faltantes -->
  </dependencies>
...
```

agregaremos lo siguiente (las versiones pueden variar):

```xml
    <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
      <version>1.18.38</version>
    </dependency>
    <dependency>
      <groupId>com.h2database</groupId>
      <artifactId>h2</artifactId>
      <version>2.3.232</version>
    </dependency>
    <dependency>
      <groupId>org.hibernate</groupId>
      <artifactId>hibernate-core</artifactId>
      <version>7.0.2.Final</version>
    </dependency>
    <dependency>
      <groupId>io.ebean</groupId>
      <artifactId>jakarta-persistence-api</artifactId>
      <version>3.1</version>
    </dependency>
```

> En este punto, si es la primera vez que estamos usando estas dependencias (lo cual sería raro ya que es con lo que venimos trabajando hace casi 1 mes), o si a posterior les salta algún error derivado sobre todo de Lombok o Jakarta, es buen momento para ejecutar en la terminal, parados en la raiz del proyecto:

```
mvn clean install -U
```

> Este comando obliga a Maven a que baje dependencias nuevas aunque crea que ya tiene la versión en caché.
Eso asegura que todas las librerías del pom.xml estén en el repositorio local __~/.m2/repository__. De esta forma no debería ocurrir de que el proyecto no reconozca los imports o las referencias a las clases asociadas a dichas dependencias.

# Empezar a armar la estructura del dominio del problema

En este momento tenemos un esqueleto generado, pero que en si no hace nada (por default si ejecutamos veremos la salida de la implementación del `"Hello World!"`)

Vamos a generar un package 'entities' donde guardaremos las clases del modelado del problema. A partir de acá el paso a paso sigue con la implementación del proyecto del simulacro del 3K1, pero lo expuesto aplica para otros problemas o dominios. Lo importante es entender los conceptos en cada paso, y como dijimos anteriormente, tener algo organizado, pero que tranquilamente podría ser diferente si el estudiante lo desea (entendiendo que debemos generar, el cómo lo hagamos pierde relevancia, siempre que no descuidemos buenas prácticas o convenciones -ya sea del lenguaje, o en este contexto particular, de la catedra o de cada docente a cargo de cada curso).

Siguiendo con lo anterior, dentro del package vamos a crear las clases Empleado, Departamento y Puesto las cuales si vienen siguiendo el paso a paso y han armado todo bien, deberian tener en la parte superior la siguiente linea:
```
package ar.edu.utnfrc.backend.entities;
```

Completamos las mismas con los datos correspondientes según el enunciado disponible:
![picture 1](./images/Clases%20del%20modelo%20v1.png)

O bien ya podriamos armar la estructura de dichas clases con la notación de JPA. Las consideraciones a tener en cuenta aquí es si la DB ya existe, se genera con un script dado o se genera en base a lo que definamos nosotros via código con JPA.

Para el enunciado específico, nos dan un script, por lo que hay algunas consideraciones para nuestras clases:
  - Espero haya notado, y sino se lo informo, que hay una discrepancia entre el nombre por convencion de nuestro atributos de la clase Empleado en Java, y los nombres de los mismos en la DB. No es lo mismo la columna `empleado_fijo` que el atributo `empleadoFijo`, por lo que es necesario aclarar la situación para los atributos empleadoFijo y fechaIngreso, de la forma `@Column(name = "fecha_ingreso"...` por ejemplo.
  - Similar a lo anterior ocurre con los nombres de las tablas en la DB y nuestras clases de entidad, donde el nombre es case-sensitive. Esto se resuelve de forma similar `@Table(name = "empleado")` para nuestra clase Empleado (notar la diferencia de mayusculas y minusculas).
  - Se agregan los listados de empleado en Departamento y Puesto para permitir la navegabilidad en ambos sentidos.

Con dichas consideraciones, el resultado final es:
![picture 2](./images/Clases%20del%20modelo%20v2a.png)
![picture 3](./images/Clases%20del%20modelo%20v2b.png)

# DB, configuraciones básicas y conexión necesaria (MODIFICADO)

Vamos a continuar generando los archivos de configuración. Generamos una subcarpeta resources, a la altura /src/main/

Dentro de la misma vamos a crear otra carpeta con el nombre __META-INF__, y dentro el archivo __persistence.xml__:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence xmlns="https://jakarta.ee/xml/ns/persistence"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="https://jakarta.ee/xml/ns/persistence https://jakarta.ee/xml/ns/persistence/persistence_3_0.xsd"
    version="3.0">

    <persistence-unit name="empleados" transaction-type="RESOURCE_LOCAL">
        <!-- Your entity classes -->
        <class>ar.edu.utnfrc.backend.entities.Empleado</class>
        <class>ar.edu.utnfrc.backend.entities.Departamento</class>
        <class>ar.edu.utnfrc.backend.entities.Puesto</class>

        <properties>
            <!-- JDBC connection -->
            <property name="jakarta.persistence.jdbc.driver" value="org.h2.Driver" />
            <property name="jakarta.persistence.jdbc.url" value="jdbc:h2:mem:empleados;DB_CLOSE_DELAY=-1" />
            <property name="jakarta.persistence.jdbc.user" value="sa" />
            <property name="jakarta.persistence.jdbc.password" value="" />

            <!-- Hibernate as JPA provider -->
            <property name="hibernate.dialect" value="org.hibernate.dialect.H2Dialect" />
            <property name="hibernate.show_sql" value="true" />
            <property name="hibernate.format_sql" value="true" />

            <property name="jakarta.persistence.schema-generation.database.action" value="create"/>
            <property name="jakarta.persistence.schema-generation.create-source" value="script"/>
            <property name="jakarta.persistence.schema-generation.create-script-source" value="META-INF/db-schema.sql"/>
        </properties>
    </persistence-unit>
</persistence>
```

Notar algunas cuestiones:
  - Se define el `persistence-unit name="empleados"`
  - Se incluyen las 3 clases de entidad generadas
  - Como el enunciado menciona una DB en memoria, se utiliza como url `<property name="jakarta.persistence.jdbc.url" value="jdbc:h2:mem:empleados;DB_CLOSE_DELAY=-1" />`. Si se quisiera persistir en un archivo, dicha linea deberia ser algo similar a `<property name="jakarta.persistence.jdbc.url" value="jdbc:h2:file:./data/empleados;AUTO_SERVER=TRUE" />`. El nombre empleados puede modificarse. Para más detalles ver el bloque 6 donde se explica con mayor detalle los tipos de conexiones y persistencia de la DB.
  - Como el enunciado tambien sugería que se daba un script para generar la db, se dejo separadas las 3 lineas que venian en el enunciado. Si la DB se debiera generar simplemente a partir del código con JPA generado por el alumno, se debería dejar simplemente algo similar a `<property name="hibernate.hbm2ddl.auto" value="XXXXX" />` con el valor value 'XXXXX' correspondiente (create | create-drop | update | validate). Nuevamente, revisar el bloque 6 para más información.

Por último, la linea final dentro de `<properties>` declara que la ubicación del script a ejecutar es 
> META-INF/db-schema.sql

por lo que debemos ubicar justamente ahí el archivo facilitado. La estructura en este momento deberia ser algo similar a lo siguiente:

![picture 4](./images/Estructura%20actual%20del%20proyecto.png)

__MODIFICACIÓN__
# Opcion A
> Nota: Si van a usar esta configuración quizas sea necesario modificar el archivo sql que reciban, esto debido a que si hay sentencias a ejecutar en diferentes lineas, al leer el archivo puede arrojar errores por espacios o saltos de linea que se leen y no se estan interpretando como corresponde. Para evitar esto, modificamos el archivo sql que nos dieron, para que todas las sentencias queden expresadas en 1 sola linea, sin saltos de linea al final. El cambio a realizar es similar a lo siguiente:

![picture 5](./images/Cambios%20script.png)

> Si hacen eso no deberian tener problemas al levantar el proyecto y generara la estructura de base de datos correctamente.

Pero existe otra forma de abordar el inconveniente de ejecutar el archivo sql tal como nos lo proveen.

Tener en cuenta que esta forma, reemplaza lo anterior de modificar el sql y dejar toda sentencia en 1 linea. 

# Opcion B
Esta segunda opción consiste en levantar el proyecto sin generar la estructura de la db, pero corriendo un metodo de una clase nueva al inicio de App que ejecute el sql provisto. _(Si necesitan, consulten nuevamente los apuntes del bloque 6 donde se muestran diferentes ejemplos de lo explicado a continuación)._

Para ello, vamos a agregar al proyecto la clase (puede tener cualquier nombre acorde) __DbInit__, dentro del package 'infrastructure' para tener organizado el proyecto. El código es el siguiente:

```
package ar.edu.utnfrc.backend.infrastructure;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class DbInit {
  private static final String URL = "jdbc:h2:mem:empleados;DB_CLOSE_DELAY=-1";
  private static final String USER = "sa";
  private static final String PASS = "";

  private DbInit() {}

  public static void run() throws SQLException, IOException {
    try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
      exec(conn, "src/main/resources/sql/db-schema.sql");
    }
  }

  private static void exec(Connection conn, String file) throws IOException, SQLException {
    String sql = Files.readString(Path.of(file), StandardCharsets.UTF_8);
    try (Statement st = conn.createStatement()) {
      st.execute(sql);
    }
  }
}
```

Sin entrar en muchos detalles, estamos definiendo la conexión hacia la db (con los datos que deberian coincidir con los ingresados en el archivo persistence), y ejecutando contra esa cadena de conexión, el código sql correspondiente (cambiar la ruta si es necesario).

> Atención: si son observadores, veran que definimos todo el metodo para realizar la ejecución, pero faltan 2 cosas. Llamar al método al ejecutar la app, y modificar ligeramente el persistence.


1: Importar la clase, declarar que el metodo ahora puede arrojar Exception, y llamar al metodo run().
```
public class App {
    public static void main(String[] args) throws Exception {
        DbInit.run();

        ...
```

2: Reemplazarlas las 3 lineas extras que se sugerían antes:
```
<property name="jakarta.persistence.schema-generation.database.action" value="create"/>
<property name="jakarta.persistence.schema-generation.create-source" value="script"/>
<property name="jakarta.persistence.schema-generation.create-script-source" value="META-INF/db-schema.sql"/>
```

por:
```
<property name="jakarta.persistence.schema-generation.database.action" value="none"/>
```

Esto permite ejecutar el proyecto sin generar ninguna base de datos ni ejecutrar archivo alguno. El mismo se ejecuta al inicio del proyecto, cuando se invoca a "DbInit.run();".

__FIN MODIFICACIÓN__


Como último apartado en lo referido a los archivos y estructura base para conectarnos a la DB, vamos a generar el `DbContext` como una clase que implemente el patrón Singleton. Para ello, creamos otro package en donde ya teniamos el de entities, denominado 'repositories'. Estructuralmente aquí tendremos todas nuestras clases asociadas al manejo y conexión con la db.

Dentro creamos otro package denominado 'context', donde finalmente generaremos nuestra clase DbContext antes mencionada. La estructura es la misma que ya hemos visto en clase.

  - La única consideración es que el path pasado a `Persistence.createEntityManagerFactory();` debe coincidir con el `persistence-unit name` del __persistence.xml__.

```
package ar.edu.utnfrc.backend.repositories.context;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class DbContext {
    private final EntityManager manager;

    public static DbContext instance = null;

    private DbContext() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("empleados");
        manager = emf.createEntityManager();
    }

    public static DbContext getInstance() {
        if (instance == null) {
            instance = new DbContext();
        }
        return instance;
    }

    public EntityManager getManager() {
        return this.manager;
    }
}
```

# Capa de Repositorios

Si bien creamos nuestro `DbContext`, nadie se relaciona con dicha clase ni obtiene su instancia. Como vimos en clase (aunque cabe remarcar en este punto que no es algo obligatorio lo siguiente, si nos facilitará a futuro, razón por la que se decide seguir adelante con esta __propuesta__ en el paso a paso, pero existen otras soluciones igualmente validas, y de hecho también se encuentran disponibles algunas versiones diferentes en los distintos proyectos de GitLab de otros docentes, queda a cargo de cada alumno romper esta burbuja y extrapolar los conceptos expuestos, no solo quedarse con esta propuesta), vamos a generar una clase abstracta generica 'Repository', que utilizaran de molde las implementaciones puntuales de nuestros repositorios asociados a su clase de entidad correspondiente.

De más esta decir a esta altura, que si bien lo siguiente (y parte de lo anterior hasta aquí) es un poco más complejo en estructura y organización, si se escalaran los alcances de los problemas a situaciones reales o laborales, rapidamente se puede observar porque es tan importante mantener una estructura de proyecto organizada, y la ventaja de crear estructuras comunes que luego se utilicen y consuman en varios lados de nuestro código. 

```
package ar.edu.utnfrc.backend.repositories;

import java.util.Set;
import java.util.stream.Stream;
import ar.edu.utnfrc.backend.repositories.context.DbContext;
import jakarta.persistence.EntityManager;

public abstract class Repository<T, K> {

    protected EntityManager manager;

    public Repository() {
        manager = DbContext.getInstance().getManager();
    }

    public void add(T entity) {
        var transaction = manager.getTransaction();
        transaction.begin();
        manager.persist(entity);
        transaction.commit();
    }

    public void update(T entity) {
        var transaction = manager.getTransaction();
        transaction.begin();
        manager.merge(entity);
        transaction.commit();
    }

    public T delete(K id) {
        var transaction = manager.getTransaction();
        transaction.begin();
        var entity = this.getById(id);
        manager.remove(entity);
        transaction.commit();
        return entity;
    }

    public abstract T getById(K id);

    public abstract Set<T> getAll();

    public abstract Stream<T> getAllStrem();
}
```

Con esto podemos generar la implementacion puntual o especifíca para Empleado, que se llamará __EmpleadoRepository__:

```
package ar.edu.utnfrc.backend.repositories;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ar.edu.utnfrc.backend.entities.Empleado;

public class EmpleadoRepository extends Repository<Empleado, Integer> {
    public EmpleadoRepository() {
        super();
    }

    @Override
    public Empleado getById(Integer id) {
        return this.manager.find(Empleado.class, id);
    }

    @Override
    public Set<Empleado> getAll() {
        return this.manager.createQuery("SELECT e FROM Empleado e", Empleado.class)
                .getResultList()
                .stream().collect(Collectors.toSet());
    }

    @Override
    public Stream<Empleado> getAllStrem() {
        return this.manager.createQuery("SELECT e FROM Empleado e", Empleado.class).getResultStream();
    }
}
```

# Capa de Servicio

Si bien previo al enunciado final todavía desconocemos que filtrado de datos se solicitaran, si que podemos plantear tanto la estructura básica de nuestros Servicios (asociados a sus respectivas clases de Entidad), como plantear el metodo de lectura y procesamiento del CSV (si bien nos falta la implementación especifica, se puede armar el esqueleto del metodo).

A modo de ejemplo, se muestra la estructura basica de un Servicio recien generado, y se muestra la implementación por encima del metodo __bulkInsert__ para procesar las lineas del CSV:

1. Creamos el package 'Service' a la misma altura que 'repositories' y 'entities'.
2. Creamos la clase EmpleadoService, con la siguiente estructura base:

```
public class EmpleadoService {
    private final EmpleadoRepository empleadoRepository;

    public EmpleadoService() {
        empleadoRepository = new EmpleadoRepository();
    }
}
```

3. Especificamente para este Service, podemos expandir un poco la funcionalidad del servicio sabiendo que de alguna forma recibiremos y deberemos popular la DB con lineas de un CSV, por lo que podemos agregar este código:

```
    public void bulkInsert(File fileToImport) throws IOException {
        Files.lines(Paths.get(fileToImport.toURI()))
                .skip(1)
                .forEach(linea -> {
                    Empleado empleado = procesarLinea(linea);
                    this.empleadoRepository.add(empleado);
                });
    }

    private Empleado procesarLinea(String linea) {
        String[] valores = linea.split(","); //Podria ser otro caracter de division
        Empleado empleado = new Empleado();

        //No conozco en que posicion vendran los valores
        //No conozco si habra algun tipo de chequeo extra o logica asociada
        empleado.setNombre(valores[0]);
        empleado.setEdad(Integer.parseInt(valores[1]));
        empleado.setFechaIngreso(LocalDate.parse(valores[2]));
        empleado.setSalario(Double.parseDouble(valores[3]));
        empleado.setEmpleadoFijo(Boolean.parseBoolean(valores[4]));

        //No conozco como llegaran los datos de Departamento/Puesto
        //empleado.setDepartamento();
        //empleado.setPuesto();

        return empleado;
    }
```

> La estructura de Repository y Service para cada entidad del dominio se podria plantear de forma basica, luego se agregarán los comportamientos que sean necesarios el dia del parcial

# Menu de opciones (opcional)

Hasta ahora hemos armado y generado muchas estructuras y clases, pero falta el core del proyecto. Debemos tener accesible la arquitectura que nos permita mostrar un menu de opciones al que agregarle los metodos y consultas requeridos el dia del parcial.

Para dicha estructura, utilizaremos algo con lo que ya hemos trabajado muchas veces en el dictado de la asignatura. Por un lado la estructura de Menu (integrada por Menu, ItemMenu, OpcionMenu y ApplicationContext), y por otro la clase Acciones que nos permita armar la funcionalidad y derivacion de cada opcion.

1. Creamos/importamos la estructura de menu mencionada, en un package menu.
2. Creamos la clase Acciones a la misma altura que tenemos la clase App (podrian agruparse en otro package, para el ejemplo quedan desagrupadas):

```
package ar.edu.utnfrc.backend;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import ar.edu.utnfrc.backend.menu.ApplicationContext;
import ar.edu.utnfrc.backend.services.EmpleadoService;

public class Acciones {
    public void importarCsv(ApplicationContext context) {
        var pathToImport = (URL) context.get("path");

        try (var paths = Files.walk(Paths.get(pathToImport.toURI()))) {
            var csvFiles = paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".csv"))
                    .map(path -> path.toFile())
                    .toList();

            csvFiles.stream()
                    .filter(f -> f.getName()
                            .contains("empleados"))
                    .findFirst()
                    .ifPresentOrElse(f -> {
                        var service = context.getService(EmpleadoService.class);
                        try {
                            service.bulkInsert(f);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    },
                            () -> new IllegalArgumentException("Archivo inexistente"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
```
_Para tratar con el CSV, se pueden usar otras soluciones, tal como se describió en uno de los apuntes del bloque 5._ 

> Observe como podemos armar la estructura del metodo que procesará el CSV, mas allá de que no tenemos la certeza de cual será el nombre especifico, tal como describia el pre enunciado. Por default filtramos "empleados", pero habria que cambiarlo al tener el csv en cuestion, o seguir la logica que plantee el parcial.

> Observe tambien como hacemos una llamada al metodo bulkInsert que generamos en nuestro EmpleadoService. Esto solo es el esqueleto, seran necesarias las modificaciones pertinentes al tener todos los datos.

# Clase App

Llegó el momento de modificar finalmente nuestra clase App, la cual aun se encontraba sin cambio alguno. Sirviendonos del uso de ApplicationContext (si no conoce esta clase y sus usos, considere revisar el bloque 5 y la guia de ejercicios de Streams, en la misma se encuentra ampliamente documentada y detallada su implementación), vamos a escribir todo lo que sabemos hasta ahora para que al menos se pueda ejecutar y mostrar por pantalla nuestro menu de opciones (carente de opciones y funcionalidad real por el momento, pero no por ello menos útil):

```
package ar.edu.utnfrc.backend;

import java.net.URL;

import ar.edu.utnfrc.backend.menu.ApplicationContext;
import ar.edu.utnfrc.backend.menu.ItemMenu;
import ar.edu.utnfrc.backend.menu.Menu;
import ar.edu.utnfrc.backend.services.EmpleadoService;

public class App {
    public static void main(String[] args) {
        var ctx = ApplicationContext.getInstance();
        Menu menu = new Menu();
        menu.setTitulo("Menu de Opciones de Empleados");

        Acciones acciones = new Acciones();

        URL folderPath = App.class.getResource("/files"); //Puede definir la ruta que desee
        ctx.put("path", folderPath);
        ctx.registerService(EmpleadoService.class, new EmpleadoService());

        menu.addOpcion(new ItemMenu(1, "Cargar Empleados", acciones::importarCsv));

        menu.ejecutar(ctx);
    }
}
```

> Note como:
> 1. seteamos en ctx la instancia de ApplicationContext.
> 2. Creamos nuestro menu y nuestra clase acciones.
> 3. Definimos el label del menu (opcional).
> 4. Definimos y asignamos en ctx nuestro folderPath (podria ser diferente la ruta).
> 5. Registramos el servicio de EmpleadoService en el ctx (y deberiamos hacer lo mismo con cada servicio que de forma posterior quisieramos acceder desde Acciones).
> 6. Agregamos la unica opción "conocida" al menu y disparamos su ejecucuón.

# Conclusión

Después de un largo camino (en realidad no debería llevar mas de 1 o 2 horas, pero si fue la primera vez que se detuvieron a hacer esto es posible que se exceda este tiempo), llegamos a... ningún lado, la verdad es que nuestro proyecto actual carece de funcionalidad real y hay muchas cosas "temporales" a cambiar cuando tengamos la versión final del enunciado, pero un poco esa era la idea. Tener una estructura sólida, que conocemos (importante este punto para poder trabajar con buenos resultados en la hora que tendremos disponible el enunciado real/final), y que nos permite rapidamente dar soporte a cualquier requerimiento que se pida. 

Con todo lo que hemos armado (o que se ha mencionado pero quizá no mostrado a fondo, ya que esto es una guía, pero se supone que el estudiante ha asistido a clase, ha leido los apuntes y fichas que serán evaluadas, y sobre todo, se ha tomado el tiempo de ejercitar y practicar), contamos con:
1. Una estructura de menu de opciones lista para recibir nuevas opciones con solo 1 linea.
2. Una clase de contexto a la que es posible agregar referencias nuevas de servicios que puedan ser facil y rapidamente accesibles desde la clase Acciones.
3. Nuestra conocida clase Acciones que permite implementar y ejecutar las interfaces funcionales definidas en cada opcion de menu.
4. Toda la estructura de entidades del dominio del problema, implementada con JPA (y Lombok posiblemente)
5. Una arquitectura de repositorios y servicios facilmente escalable en funcionalidad segun requiera el problema
6. Toda la conexión y configuración con la DB

Si llegó hasta aca y entendió todo, debería estar en condiciones de lidiar con cualquier apartado que se pueda llegar a solicitar. Si por el contrario no logró entender al 100% las pinceladas que hemos ido viendo en este paso a paso, lo invito a arremangarse y ponerse a practicar para el parcial.

Espero este apunte sea útil y les de mas herramientas que dudas. Si luego de leer detenidamente todo, consultar con el material de apoyo y renegar un poco por su cuenta, aún sigue sin comprender algo, tenga a bien levantar la mano y pedir ayuda, idealmente antes del parcial.


Saludos,
Ing. Rodrigo Diaz.