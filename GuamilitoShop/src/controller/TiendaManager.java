package controller;

import java.util.ArrayList;
import java.util.HashMap;
import model.Producto;
import model.ProductoCarrito;
import model.Usuario;

public class TiendaManager {
    private final ArrayList<Producto> catalogo = new ArrayList<>();
    private final HashMap<String, Usuario> usuarios = new HashMap<>();
    private final ArrayList<ProductoCarrito> carrito = new ArrayList<>();

    // Métodos
    public void agregarProducto(Producto producto) {
        catalogo.add(producto);
    }

    public void registrarUsuario(Usuario usuario) {
        usuarios.put(usuario.getCorreo(), usuario);
    }

    public Usuario iniciarSesion(String correo) {
        return usuarios.get(correo);
    }

    public ArrayList<Producto> getCatalogo() {
        return catalogo;
    }

    public ArrayList<ProductoCarrito> getCarrito() {
        return carrito;
    }

    public boolean agregarAlCarrito(Producto producto, int cantidad) {
        if (producto.getStock() >= cantidad) {
            carrito.add(new ProductoCarrito(producto, cantidad));
            return true;
        } else {
            return false;
        }
    }

    public double calcularTotalCarrito() {
        double total = 0;
        for (ProductoCarrito pc : carrito) {
            total += pc.getProducto().getPrecio() * pc.getCantidad();
        }
        return total;
    }

    public void procesarPago() {
        for (ProductoCarrito pc : carrito) {
            Producto producto = pc.getProducto();
            int cantidad = pc.getCantidad();
            producto.setStock(producto.getStock() - cantidad);
        }
        carrito.clear();
    }

    public void vaciarCarrito() {
         carrito.clear(); 
    }
}

