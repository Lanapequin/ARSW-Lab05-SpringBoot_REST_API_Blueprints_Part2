# Blueprints REST API: Implementación con Spring Boot

## Integrantes
- Laura Natalia Perilla Quintero - [Lanapequin](https://github.com/Lanapequin)
- Santiago Botero Garcia - [LePeanutButter](https://github.com/LePeanutButter)

## Parte I. Configuración Inicial y Endpoints Básicos (GET)

Se desarrolló Blueprints REST API, una API REST para gestionar planos arquitectónicos de una empresa de diseño. La solución busca ser centralizada, multiplataforma y de bajo acoplamiento entre controlador, servicios y persistencia.

La implementación se realizó con Spring Boot, usando inyección de dependencias y SpringMVC para exponer los servicios REST.

Este componente ya había sido implementado en el laboratorio anterior, pero no contaba con todos los controladores necesarios. Por ello, se modificó el bean de persistencia `InMemoryBlueprintPersistence` para que inicialice tres planos adicionales, además del que ya se encontraba preconfigurado.

![](img/img1.png)

Se implementó la clase `BlueprintsController` con el objetivo de exponer varios métodos HTTP. El primero en ser desarrollado fue el método `GET`, encargado de retornar, en formato `JSON`, el conjunto completo de planos disponibles en el sistema. Además, en esta clase se inyectó el bean de tipo `BlueprintServices`, permitiendo así el acceso a la lógica de negocio y a la capa de persistencia.

![](img/img2.png)

La aplicación fue ejecutada desde el entorno de desarrollo `IntelliJ IDEA`, lo que permitió iniciar el proyecto directamente con soporte de `Spring Boot`. Para probar el funcionamiento del método `GET` del recurso `/blueprints`, se utilizó `Postman` como herramienta cliente. A través de esta, se envió una petición GET a http://localhost:8080/blueprints, obteniendo como respuesta un objeto JSON con los planos precargados y con el filtrado de puntos aplicado correctamente.

![](img/img3.png)

Se amplió el controlador para incluir un nuevo método GET que permite filtrar los planos según el nombre del autor, recibido como variable en la URL mediante la anotación `@PathVariable`. Este método devuelve una representación JSON con todos los planos correspondientes al autor especificado. En caso de que no exista ningún plano asociado a dicho autor, el controlador responde con un código de estado HTTP 404, indicando que el recurso solicitado no fue encontrado. Esta implementación garantiza un manejo robusto de las solicitudes y una adecuada respuesta ante casos de datos inexistentes.

![](img/img4.png)

![](img/img5.png)

![](img/img6.png)

Se implementó un nuevo método GET en el controlador para atender peticiones al recurso `/blueprints/{author}/{bpname}`, el cual permite obtener un único plano específico, identificado por el nombre del autor y el nombre del plano. La ruta utiliza la anotación `@PathVariable` para capturar los parámetros directamente desde la URL y retornar una representación JSON del plano correspondiente.

![](img/get-blueprint-controller.png)

Dado que el controlador no retorna directamente wildcards ni estructuras de respuesta genéricas, se implementó la clase `ControllerResponse`, la cual estandariza la salida del API en una estructura consistente que incluye:

- El contenido (`data`),
- Un mensaje descriptivo (`message`),
- Y el código de estado (`status`).

![](img/message-response-formater.png)

Esta clase mejora la legibilidad y trazabilidad de las respuestas, especialmente al realizar pruebas desde herramientas como Postman. Su constructor tiene la siguiente estructura:

```java
public ControllerResponse(T data, String message, int status) {
    this.data = data;
    this.message = message;
    this.status = status;
}
```

Adicionalmente, se actualizó la clase `InMemoryBlueprintPersistence`, encargada de simular la lógica de persistencia, para validar adecuadamente la existencia del autor y del plano solicitado. Se utilizaron excepciones personalizadas (`BlueprintPersistenceException`) para manejar los casos en los que alguno de los parámetros no se encuentra registrado, garantizando una respuesta con el código de estado HTTP 404 cuando corresponde.

![](img/get-blueprint-persistance.png)

Para validar el funcionamiento, se realizaron tres pruebas de aceptación:

1. **Caso exitoso:** Se obtiene un plano cuando existen tanto el autor como el nombre del plano.

    ![](img/get-blueprint-sucessfull.png)

2. **Plano inexistente:** Se retorna HTTP 404 cuando el autor existe, pero el nombre del plano no está registrado.

    ![](img/get-blueprint-nonexistent-blueprint.png)

3. **Autor inexistente:** Se retorna HTTP 404 cuando el autor no está registrado en el sistema.

    ![](img/get-blueprint-nonexistent-author.png)

### Parte II

1.  Agregue el manejo de peticiones POST (creación de nuevos planos), de manera que un cliente http pueda registrar una nueva orden haciendo una petición POST al recurso ‘planos’, y enviando como contenido de la petición todo el detalle de dicho recurso a través de un documento jSON. Para esto, tenga en cuenta el siguiente ejemplo, que considera -por consistencia con el protocolo HTTP- el manejo de códigos de estados HTTP (en caso de éxito o error):

    ```	java
    @RequestMapping(method = RequestMethod.POST)	
    public ResponseEntity<?> manejadorPostRecursoXX(@RequestBody TipoXX o){
        try {
            //registrar dato
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (XXException ex) {
            Logger.getLogger(XXController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("Error bla bla bla",HttpStatus.FORBIDDEN);            
        }        
     
    }
    ```	
Se agrego en el controlador el método POST con el fin de que se puedan crear nuevos planos, para esto se debe enviar en formato JSON toda la información del nuevo plano, la cual requiere del nombre del autor, el nombre del plano y las respectivas coordenadas.
![](img/img7.png)

2.  Para probar que el recurso ‘planos’ acepta e interpreta
    correctamente las peticiones POST, use el comando curl de Unix. Este
    comando tiene como parámetro el tipo de contenido manejado (en este
    caso jSON), y el ‘cuerpo del mensaje’ que irá con la petición, lo
    cual en este caso debe ser un documento jSON equivalente a la clase
    Cliente (donde en lugar de {ObjetoJSON}, se usará un objeto jSON correspondiente a una nueva orden:

    ```	
    $ curl -i -X POST -HContent-Type:application/json -HAccept:application/json http://URL_del_recurso_ordenes -d '{ObjetoJSON}'
    ```	

    Con lo anterior, registre un nuevo plano (para 'diseñar' un objeto jSON, puede usar [esta herramienta](http://www.jsoneditoronline.org/)):
    

    Nota: puede basarse en el formato jSON mostrado en el navegador al consultar una orden con el método GET.

Para poder probar que funciona el método POST, se utilizo nuevamente Postman para verificar la creación del nuevo plano.

![img8.png](img/img8.png)
![img9.png](img/img9.png)

3. Teniendo en cuenta el autor y numbre del plano registrado, verifique que el mismo se pueda obtener mediante una petición GET al recurso '/blueprints/{author}/{bpname}' correspondiente.

4. Agregue soporte al verbo PUT para los recursos de la forma '/blueprints/{author}/{bpname}', de manera que sea posible actualizar un plano determinado.


### Parte III

El componente BlueprintsRESTAPI funcionará en un entorno concurrente. Es decir, atederá múltiples peticiones simultáneamente (con el stack de aplicaciones usado, dichas peticiones se atenderán por defecto a través múltiples de hilos). Dado lo anterior, debe hacer una revisión de su API (una vez funcione), e identificar:

* Qué condiciones de carrera se podrían presentar?
* Cuales son las respectivas regiones críticas?

Ajuste el código para suprimir las condiciones de carrera. Tengan en cuenta que simplemente sincronizar el acceso a las operaciones de persistencia/consulta DEGRADARÁ SIGNIFICATIVAMENTE el desempeño de API, por lo cual se deben buscar estrategias alternativas.

Escriba su análisis y la solución aplicada en el archivo ANALISIS_CONCURRENCIA.txt

#### Criterios de evaluación

1. Diseño.
    * Al controlador REST implementado se le inyectan los servicios implementados en el laboratorio anterior.
    * Todos los recursos asociados a '/blueprint' están en un mismo Bean.
    * Los métodos que atienden las peticiones a recursos REST retornan un código HTTP 202 si se procesaron adecuadamente, y el respectivo código de error HTTP si el recurso solicitado NO existe, o si se generó una excepción en el proceso (dicha excepción NO DEBE SER de tipo 'Exception', sino una concreta)
2. Funcionalidad.
    * El API REST ofrece los recursos, y soporta sus respectivos verbos, de acuerdo con lo indicado en el enunciado.
3. Análisis de concurrencia.
    * En el código, y en las respuestas del archivo de texto, se tuvo en cuenta:
        * La colección usada en InMemoryBlueprintPersistence no es Thread-safe (se debió cambiar a una con esta condición).
        * El método que agrega un nuevo plano está sujeta a una condición de carrera, pues la consulta y posterior agregación (condicionada a la anterior) no se realizan de forma atómica. Si como solución usa un bloque sincronizado, se evalúa como R. Si como solución se usaron los métodos de agregación condicional atómicos (por ejemplo putIfAbsent()) de la colección 'Thread-Safe' usada, se evalúa como B.