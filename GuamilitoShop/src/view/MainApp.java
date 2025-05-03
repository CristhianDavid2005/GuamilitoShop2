package view;

import controller.TiendaManager;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import model.Usuario;

public class MainApp extends Application {

    private final TiendaManager tiendaManager;
    private Stage primaryStage;

    public MainApp() {
        this.tiendaManager = new TiendaManager();
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Guamilito Shop");

        inicializarDatos();

        mostrarPantallaLogin();
    }

    private void inicializarDatos() {
        tiendaManager.registrarUsuario(new Usuario(1, "David Perez", "perezfigueroacristhiandavid@gmail.com"));
        tiendaManager.registrarUsuario(new Usuario(2, "Claudia Castro", "cc422181@gmail.com"));
        tiendaManager.registrarUsuario(new Usuario(3, "Besy Medina", "besym242@gmail.com"));
        tiendaManager.registrarUsuario(new Usuario(4, "Andrea Padilla", "andreapadilla123@gmail.com"));
        
        tiendaManager.agregarProducto(new model.Producto(1, "Camisa", 200.0, "Ropa", 10));
        tiendaManager.agregarProducto(new model.Producto(2, "Laptop", 15000.0, "Electrónica", 5));
        tiendaManager.agregarProducto(new model.Producto(3, "Silla", 250.0, "Hogar", 8));
        tiendaManager.agregarProducto(new model.Producto(4, "Mesa", 2000.0, "Hogar", 2));
        tiendaManager.agregarProducto(new model.Producto(5, "Mueble de sala", 20000.0, "Hogar", 2));
        tiendaManager.agregarProducto(new model.Producto(6, "Mause", 250.0, "Hogar", 5));
        tiendaManager.agregarProducto(new model.Producto(7, "Jeans", 500.0, "Hogar", 20));
        tiendaManager.agregarProducto(new model.Producto(8, "Falda", 250.0, "Hogar", 15));
        tiendaManager.agregarProducto(new model.Producto(9, "Impresora", 7500.0, "Hogar", 2));
        tiendaManager.agregarProducto(new model.Producto(10, "Televisor Plasma", 4500.0, "Hogar", 1));
    }

    private void mostrarPantallaLogin() {
        Label labelCorreo = new Label("Correo electrónico:");
        TextField campoCorreo = new TextField();
        Button botonLogin = new Button("Iniciar Sesión");
        Label labelMensaje = new Label();

        botonLogin.setOnAction(e -> {
            String correo = campoCorreo.getText();
            Usuario usuario = tiendaManager.iniciarSesion(correo);
            if (usuario != null) {
                mostrarPantallaCatalogo(); // de aqui en adelante es el catálogo
            } else {
                labelMensaje.setText("Correo no encontrado.");
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(labelCorreo, campoCorreo, botonLogin, labelMensaje);

        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

        //  catálogo de productos
        private void mostrarPantallaCatalogo() {
    TableView<model.Producto> tableView = new TableView<>();

    TableColumn<model.Producto, Integer> colId = new TableColumn<>("ID");
    colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());

    TableColumn<model.Producto, String> colNombre = new TableColumn<>("Nombre");
    colNombre.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));

    TableColumn<model.Producto, Double> colPrecio = new TableColumn<>("Precio");
    colPrecio.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getPrecio()).asObject());

    TableColumn<model.Producto, String> colCategoria = new TableColumn<>("Categoría");
    colCategoria.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCategoria()));

    TableColumn<model.Producto, Integer> colStock = new TableColumn<>("Stock");
    colStock.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getStock()).asObject());

    tableView.getColumns().addAll(colId, colNombre, colPrecio, colCategoria, colStock);

    tableView.getItems().addAll(tiendaManager.getCatalogo());

    // Campo para cantidad
    TextField campoCantidad = new TextField();
    campoCantidad.setPromptText("Cantidad");

    // Botón agregar al carrito
    Button botonAgregar = new Button("Agregar al Carrito");
    Label labelMensaje = new Label();

    botonAgregar.setOnAction(e -> {
        model.Producto productoSeleccionado = tableView.getSelectionModel().getSelectedItem();
        if (productoSeleccionado != null) {
            try {
                int cantidad = Integer.parseInt(campoCantidad.getText());
                if (cantidad > 0) {
                    boolean exito = tiendaManager.agregarAlCarrito(productoSeleccionado, cantidad);
                    if (exito) {
                        labelMensaje.setText("Producto agregado al carrito.");
                    } else {
                        labelMensaje.setText("Stock insuficiente.");
                    }
                } else {
                    labelMensaje.setText("Ingrese una cantidad válida.");
                }
            } catch (NumberFormatException ex) {
                labelMensaje.setText("Cantidad no válida.");
            }
        } else {
            labelMensaje.setText("Seleccione un producto.");
        }
    });

    // Botón para ir al carrito
    Button botonVerCarrito = new Button("Ver Carrito");

    botonVerCarrito.setOnAction(e -> {
        mostrarPantallaCarrito();
    });

    VBox layout = new VBox(10);
    layout.getChildren().addAll(tableView, campoCantidad, botonAgregar, botonVerCarrito, labelMensaje);

    Scene scene = new Scene(layout, 600, 400);
    primaryStage.setScene(scene);
    primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

        
        private void mostrarPantallaCarrito() {
    TableView<model.ProductoCarrito> tableViewCarrito = new TableView<>();

    TableColumn<model.ProductoCarrito, String> colNombre = new TableColumn<>("Producto");
    colNombre.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getProducto().getNombre()));

    TableColumn<model.ProductoCarrito, Integer> colCantidad = new TableColumn<>("Cantidad");
    colCantidad.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getCantidad()).asObject());

    TableColumn<model.ProductoCarrito, Double> colSubtotal = new TableColumn<>("Total");
    colSubtotal.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(
        cellData.getValue().getProducto().getPrecio() * cellData.getValue().getCantidad()).asObject());

    tableViewCarrito.getColumns().addAll(colNombre, colCantidad, colSubtotal);

    tableViewCarrito.getItems().addAll(tiendaManager.getCarrito());

    Label labelTotal = new Label("Total: $" + tiendaManager.calcularTotalCarrito());

    Button botonPagar = new Button("Pagar");
    Label labelMensaje = new Label();

    botonPagar.setOnAction(e -> {
        tiendaManager.procesarPago();
        labelMensaje.setText("Pago realizado exitosamente.");
        mostrarPantallaCatalogo(); // Regresamos al catálogo después de pagar
    });

    Button botonCancelar = new Button("Cancelar Compra");
        botonCancelar.setOnAction((ActionEvent e) -> {
            tiendaManager.vaciarCarrito(); // Este método debe estar en TiendaManager
            labelMensaje.setText("Compra cancelada. Carrito vacío.");
            mostrarPantallaCatalogo(); // Vuelve al catálogo
    });
    
    VBox layout = new VBox(10);
    layout.getChildren().addAll(tableViewCarrito, labelTotal, botonPagar, labelMensaje);

    Scene scene = new Scene(layout, 500, 400);
    primaryStage.setScene(scene);
    primaryStage.show();
}

    }


