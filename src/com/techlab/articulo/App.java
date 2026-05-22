package com.techlab.articulo;
/*
// ============================================================
// CONSIGNA GENERAL DE LA PREENTREGA
// ============================================================
//
// Desarrollar una aplicación en Java por consola que permita gestionar
// artículos y categorías de una tienda.
//
// La aplicación deberá trabajar con:
// - Programación Orientada a Objetos
// - Clase abstracta
// - Herencia
// - Polimorfismo
// - Interfaces
// - Generics
// - Relación entre objetos
// - Menús separados
//
// ESTRUCTURA CONCEPTUAL ESPERADA
// ------------------------------------------------------------
// 1) Debe existir una clase abstracta Articulo con:
//    - codigo
//    - nombre
//    - precio
//    - categoria (objeto Categoria, NO String)
//
// 2) Deben existir dos clases hijas de Articulo:
//    - ArticuloElectronico
//    - ArticuloAlimenticio
//
// 3) Debe existir una clase Categoria con su propio CRUD.
//
// 4) Debe existir una interfaz Calculable con el método:
//    - double calcularPrecioFinal();
//
// 5) Debe existir una interfaz Identificable con el método:
//    - int getCodigo();
//
// 6) Debe existir un Repositorio<T extends Identificable>
//    para administrar artículos y categorías en memoria.
//
// 7) Debe existir una clase base Menu y dos menús hijos:
//    - MenuArticulos
//    - MenuCategorias
//
// REGLAS DE NEGOCIO PRINCIPALES
// ------------------------------------------------------------
// - No se puede crear un artículo si no existen categorías cargadas.
// - No se puede eliminar una categoría si tiene artículos asociados.
// - Los códigos de artículos y categorías deben ser automáticos.
// - Deben validarse todos los datos ingresados.
// - No debe resolverse toda la lógica dentro del main.
//
// IMPORTANTE
// ------------------------------------------------------------
// Este proyecto se entrega como PLANTILLA.
// Eso significa que:
// - la estructura ya está armada,
// - los nombres de las clases ya están definidos,
// - y en cada archivo encontrarás instrucciones específicas
//   sobre qué implementar.
//
// Tu tarea será completar cada clase respetando esta estructura.
//
 */

// Importamos Scanner para leer datos por consola.
import java.util.Scanner;

// Importamos los menús que luego deberán completarse.
import com.techlab.articulo.menu.MenuArticulos;
import com.techlab.articulo.menu.MenuCategorias;
import com.techlab.articulo.menu.MenuPrincipal;
import com.techlab.articulo.model.Articulo;
import com.techlab.articulo.model.ArticuloAlimenticio;
import com.techlab.articulo.model.ArticuloElectronico;
import com.techlab.articulo.model.Categoria;
import com.techlab.articulo.repository.Repositorio;
import com.techlab.articulo.service.ArticuloService;
import com.techlab.articulo.service.CategoriaService;

public class App {

    public static void main(String[] args) {

        /*
        // ============================================================
        // INSTRUCCIONES PARA ESTA CLASE
        // ============================================================
        //
        // Esta clase debe actuar como punto de entrada del programa.
        //
        // ¿Qué debe hacer el alumno acá?
        // 1) Crear un Scanner compartido.
        // 2) Crear los repositorios necesarios:
        //    - Repositorio<Articulo>
        //    - Repositorio<Categoria>
        // 3) Crear los dos menús:
        //    - MenuArticulos
        //    - MenuCategorias
        // 4) Crear un menú principal que permita elegir:
        //    - 1: ir al menú de artículos
        //    - 2: ir al menú de categorías
        //    - 0: salir
        // 5) Cerrar correctamente el Scanner al finalizar.
        //
        // Sugerencia:
        // El main NO debe tener toda la lógica del sistema.
        // El main solo debe coordinar el flujo principal.
        //
        // ============================================================
         */

        Scanner scanner = new Scanner(System.in);
        // Crear aquí los repositorios genéricos.
        Repositorio<Categoria> categoriaRepositorio = new Repositorio<Categoria>();
        Repositorio<Articulo> articuloRepositorio = new Repositorio<Articulo>();
        // Crear aquí los servicios
        CategoriaService categoriaService = new CategoriaService(categoriaRepositorio, articuloRepositorio);
        ArticuloService articuloService = new ArticuloService(articuloRepositorio, categoriaRepositorio);

        cargarDatosIniciales(categoriaService, articuloService);

        // Crear aquí los menús y pasarles lo que necesiten por constructor.
        MenuCategorias menuCategorias = new MenuCategorias(scanner, categoriaService);
        MenuArticulos menuArticulos = new MenuArticulos(scanner, articuloService, categoriaService);
        MenuPrincipal menu =  new MenuPrincipal(scanner,menuArticulos, menuCategorias);

        menu.ejecutar();
        scanner.close();
    }

    private static void cargarDatosIniciales(CategoriaService categoriaService, ArticuloService articuloService) {
        categoriaService.agregar(new Categoria("Tecnología", "Articulos relacionados con tecnología."));
        categoriaService.agregar(new Categoria("Alimentos", "Articulos relacionados con Alimentos no perecederos."));
        categoriaService.agregar(new Categoria("Bebidas", "Articulos relacionados con bebidas."));
        categoriaService.agregar(new Categoria("Frutas y Verduras", "Articulos relacionados con frutas y verduras."));

        articuloService.agregar(new ArticuloElectronico("Samsung AS02", 100000, categoriaService.obtenerPorCodigo(1), 6));
        articuloService.agregar(new ArticuloAlimenticio("Arroz Gallo", 2000, categoriaService.obtenerPorCodigo(2) , 60));
        articuloService.agregar(new ArticuloAlimenticio("Coca Cola Zero", 4000, categoriaService.obtenerPorCodigo(2) , 60));
    }
}
