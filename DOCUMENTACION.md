# Análisis y Documentación de Arquitectura - Home Banking

## 1. Organización del Proyecto y Funcionalidad
El proyecto está estructurado siguiendo un patrón de arquitectura en capas (Layered Architecture) y una separación de responsabilidades clara que facilita su mantenibilidad y escalabilidad.

Las responsabilidades se dividen en los siguientes paquetes principales:

*   **`entidades` (Model/Domain Layer)**: Contiene los objetos de dominio (POJOs) como `Usuario`, `Cliente`, `Administrador`, `Cuenta`, `Tarjeta`, `MovimientoTarjeta`, y `Transferencia`. Representan los datos del negocio puros, sin lógica de persistencia.
*   **`dao` (Data Access Object Layer)**: Define las interfaces para acceder a los datos. Cada entidad tiene su respectivo contrato DAO (ej. `UsuarioDAO`, `CuentaDAO`).
    *   **`dao.impl`**: Contiene las implementaciones específicas de los DAOs para la base de datos relacional (H2), como `UsuarioDAOImplH2`. Aquí se ejecuta todo el lenguaje SQL, aislando al resto del sistema de los detalles de la base de datos.
*   **`service` (Business Logic Layer)**: Contiene las clases de servicio (ej. `UsuarioService`, `CuentaService`) que orquestan la lógica de negocio. Estas clases se apoyan en los DAOs para persistir información, validan reglas de negocio (ej. validaciones de DNI duplicado, saldos) y manejan el flujo transaccional.
*   **`UI` (Presentation Layer)**: Construida utilizando Java Swing. Está subdividida en paquetes lógicos (`login`, `cliente`, `formularioUsuario`, `tablaUsuarios`). Existe una clase `PanelManager` que actúa como un enrutador o controlador principal para cambiar de pantallas (paneles).
*   **`util`**: Clases de utilidad técnica que pueden ser consumidas por varias capas, como `DBManager` para inicializar y gestionar conexiones, y `TableManager` para abstraer la configuración de modelos en tablas visuales.
*   **`exceptions`**: Se definen excepciones de dominio y base de datos personalizadas, separadas en paquetes `DAOExceptions` y `serviceExceptions`.

---

## 2. Cumplimiento de Principios de Diseño

### Principio de Responsabilidad Única (SRP - Single Responsibility Principle)
El proyecto destaca positivamente por aplicar SRP en su estructura base:
*   **Entidades y DAOs**: Las entidades solo albergan datos. Los DAOs solo saben cómo ejecutar queries SQL (ej. `crearUsuario`, `borrarUsuario`); no intentan aplicar lógica como "crear una cuenta cuando se crea un usuario".
*   **Servicios (Services)**: Son los responsables del negocio y del "qué hacer". Por ejemplo, el `UsuarioService` llama a `usuarioDAO.crearUsuario()` y luego a `cuentaService.crearCuenta()` en un mismo flujo.
*   **Excepciones tipificadas**: Se crearon excepciones específicas como `DniDuplicadoException` o `UsuarioNoEncontradoException`. Cada excepción cumple el único propósito de reportar semánticamente una falla específica, lo que simplifica la captura de errores.

### Alta Cohesión y Bajo Acoplamiento
*   **Bajo Acoplamiento por Interfaces**: La capa de servicio (`service`) depende de interfaces del `dao` (ej. `UsuarioDAO`), no de sus implementaciones. Esto permite que el día de mañana se pueda migrar a PostgreSQL o MySQL reemplazando `UsuarioDAOImplH2` por `UsuarioDAOImplPostgres` sin tocar una sola línea de la capa de servicios.
*   **Alta Cohesión Funcional**: Todos los métodos y variables dentro de clases como `TarjetaDAO` están directa y exclusivamente relacionados con operaciones sobre tarjetas. Además, el empaquetado (como `UI.cliente` o `UI.login`) indica que los recursos que trabajan juntos cambian juntos.

---

## 3. Áreas de Mejora Identificadas

A pesar de tener una base arquitectónica robusta, existen oportunidades clave de refactorización y mejora orientadas a la modernización, seguridad y reutilización.

### A. Reutilización de Código (DRY - Don't Repeat Yourself)
1.  **Patrón Abstract DAO / Base DAO**: Actualmente, métodos como `obtenerConexion()` y `cerrarConexion()` están copiados y pegados en cada clase dentro de `dao.impl` (ej. en `UsuarioDAOImplH2`, `CuentaDAOImplH2`, etc.).
    *   *Solución*: Crear una clase `AbstractDAO` que concentre estos métodos comunes, y hacer que todos los DAOs hereden de ella. También se podría incluir métodos CRUD genéricos mediante el uso de *Generics* en Java (`public abstract class AbstractDAO<T, ID>`).
2.  **Componentes Genéricos UI**: Muchas vistas tipo tabla en Swing repetirán lógicas de llenado y refresco de datos. Se podrían abstraer clases como `GenericTablePanel` para evitar duplicar configuraciones de columnas en Swing.

### B. Aprovechamiento de Características de Java y Seguridad
1.  **Vulnerabilidad a SQL Injection y uso de PreparedStatement**: Actualmente se utilizan objetos `Statement` concatenando cadenas de texto (ej. `"INSERT INTO ... " + nombre`). Esto es ineficiente y es una vulnerabilidad grave.
    *   *Solución*: Reemplazar todos los `Statement` por `PreparedStatement`, utilizando parámetros binding `(?)` que sanearán automáticamente los inputs.
2.  **Try-with-resources**: El cierre explícito de la conexión `c.close()` en el bloque `finally` con anidamiento de try/catch ensucia el código.
    *   *Solución*: Utilizar el bloque `try-with-resources` introducido en Java 7, lo que cerraría automáticamente la conexión y el `ResultSet`: `try (Connection c = DBManager.connect(); PreparedStatement ps = ...) { ... }`.
3.  **Uso de `java.util.Optional`**: Varios DAOs lanzan `ObjetoNoEncontradoException` si un "SELECT" no trae resultados.
    *   *Solución*: En el Java moderno se recomienda retornar un `Optional<Usuario>`. Esto obliga a la capa superior (Service) a decidir qué hacer si está vacío, de forma funcional en vez de manejar la falta de un registro como una anomalía/excepción costosa de arrojar.
4.  **Uso de Records (Java 14+)**: Entidades que actúan únicamente como DTOs inmutables podrían convertirse en `records` en el futuro para evitar getters/setters manuales si se migra a versiones de Java más recientes.

### C. Reforzando la Cohesión y Bajo Acoplamiento
1.  **PanelManager como God Object (Anti-patrón)**: La clase `PanelManager` inyecta casi todos los servicios existentes de la aplicación. Es una clase sumamente acoplada que crecerá infinitamente.
    *   *Solución*: Implementar el patrón *Facade* para tener una interfaz unificada que abstraiga subsistemas a la interfaz gráfica, o mejor aún, utilizar Inyección de Dependencias (ej. Spring Core o un inyector simple propio).
2.  **Acoplamiento Lógico (Patrón Observer)**: Cuando se da de alta un usuario en `UsuarioService`, este llama directamente a `CuentaService` para generarle una caja de ahorro. Si mañana hay que darle una Tarjeta por defecto, habrá que modificar el servicio otra vez.
    *   *Solución*: Implementar un patrón *Observer* o publicación de eventos. `UsuarioService` puede emitir un evento `UsuarioRegistradoEvent`. Los interesados (como `CuentaService` o `TarjetaService`) lo escucharían y actuarían independientemente.

### D. Otras Observaciones de UI / Estructura
1.  **Controladores Acoplados a las Vistas**: En `Swing`, se recomienda que las acciones de los botones (los `ActionListener`) estén extraídos en clases "Controller" diferentes para no mezclar la manipulación visual (colores, labels, constraints) con las llamadas a los `Services`.
2.  **Manejo Global de Errores**: Para evitar repetir `JOptionPane.showMessageDialog` en todos los bloques `catch` de la vista, se podría implementar una captura genérica utilizando un hilo `Thread.setDefaultUncaughtExceptionHandler()` para el manejo de fallas no interceptadas.
