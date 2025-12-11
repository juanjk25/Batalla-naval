package org.example.batalla_naval_re;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

/**
 * Clase principal de la aplicación Batalla Naval.
 * <p>
 * Esta clase extiende de {@link Application} y sirve como punto de entrada
 * para la interfaz gráfica basada en JavaFX. Se encarga de configurar el escenario principal (Stage),
 * cargar la vista inicial (main.fxml) y aplicar los estilos globales (styles.css).
 * </p>
 */
public class App extends Application {

    /**
     * Método de inicio de la aplicación JavaFX.
     * <p>
     * Se ejecuta automáticamente al lanzar la aplicación. Realiza las siguientes tareas:
     * <ol>
     *     <li>Localiza y carga el archivo FXML de la vista principal (/main.fxml).</li>
     *     <li>Verifica la existencia del archivo para evitar errores en tiempo de ejecución.</li>
     *     <li>Carga la hoja de estilos CSS opcional (/styles.css).</li>
     *     <li>Configura y muestra la ventana principal.</li>
     * </ol>
     *
     *
     * @param stage El escenario principal (ventana) proporcionado por el runtime de JavaFX.
     * @throws Exception Si ocurre un error fatal durante la carga de recursos (FXML no encontrado o error de E/S).
     */
    @Override
    public void start(Stage stage) throws Exception {

        // 1. OBTENER URL del archivo FXML
        // IMPORTANTE: Usar "/" al inicio para buscar desde la raíz de resources
        URL fxmlUrl = App.class.getResource("/main.fxml");

        // Verificar que se encontró
        if (fxmlUrl == null) {
            System.err.println("ERROR: No se encontró /main.fxml en src/main/resources/");
            throw new RuntimeException("Archivo FXML no encontrado");
        }

        System.out.println("FXML encontrado en: " + fxmlUrl);

        // 2. CARGAR FXML usando la URL
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
        Scene scene = new Scene(fxmlLoader.load());

        // 3. OPCIONAL: Cargar CSS si existe
        URL cssUrl = App.class.getResource("/styles.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }

        // 4. CONFIGURAR VENTANA
        stage.setTitle("Batalla Naval - JavaFX");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Punto de entrada estándar para aplicaciones Java.
     * <p>
     * Invoca el método {@link #launch(String...)} para iniciar el ciclo de vida de JavaFX.
     * </p>
     *
     * @param args Argumentos de línea de comandos (no utilizados en esta aplicación).
     */
    public static void main(String[] args) {
        launch();
    }
}