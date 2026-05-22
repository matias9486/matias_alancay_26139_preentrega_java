package com.techlab.articulo.menu;

import com.techlab.articulo.excepciones.NoEncontrado;
import com.techlab.articulo.excepciones.PropiedadInvalida;
import com.techlab.articulo.excepciones.RelacionExistente;
import com.techlab.articulo.model.Categoria;
import com.techlab.articulo.service.CategoriaService;
import com.techlab.articulo.utils.Secuencias;
import com.techlab.articulo.utils.Validaciones;

import java.util.List;

/**
 * CONSIGNA DE ESTA CLASE
 * ------------------------------------------------------------
 * Esta clase debe heredar de Menu y encargarse del CRUD de categorías.
 *
 * Debe trabajar con:
 * - Repositorio<Categoria>
 * - Repositorio<Articulo>
 *
 * ¿Por qué necesita también artículos?
 * Porque antes de eliminar una categoría debe verificarse si está
 * siendo utilizada por algún artículo.
 *
 * FUNCIONALIDADES ESPERADAS
 * ------------------------------------------------------------
 * 1) Ingresar categoría
 * 2) Listar categorías
 * 3) Consultar una categoría por código
 * 4) Modificar una categoría
 * 5) Eliminar una categoría
 * 0) Volver
 *
 * VALIDACIONES
 * ------------------------------------------------------------
 * - nombre no vacío
 * - descripción no vacía
 * - no permitir categorías repetidas por nombre
 *
 * REGLA DE NEGOCIO IMPORTANTE
 * ------------------------------------------------------------
 * No se puede eliminar una categoría si existe al menos un artículo
 * asociado a ella.
 *
 * SUGERENCIA DE MÉTODOS
 * ------------------------------------------------------------
 * - ingresarCategoria()
 * - listarCategorias()
 * - consultarCategoria()
 * - modificarCategoria()
 * - eliminarCategoria()
 * - categoriaTieneArticulosAsociados(...)
 */
public class MenuCategorias extends Menu {
    private CategoriaService categoriaService;

    public MenuCategorias(java.util.Scanner scanner, CategoriaService categoriaService) {
        super(scanner);
        this.categoriaService = categoriaService;
    }

    @Override
    public void mostrarMenu() {
        System.out.println("\n--- MENÚ CATEGORÍAS ---");
        System.out.println("1 - Ingresar categoría");
        System.out.println("2 - Listar categorías");
        System.out.println("3 - Consultar categoría");
        System.out.println("4 - Modificar categoría");
        System.out.println("5 - Eliminar categoría");
        System.out.println("0 - Volver");
    }

    @Override
    public void ejecutar() {
        // TODO:
        // Implementar el loop del menú y llamar a los métodos correspondientes.
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Ingrese una opción:");
            switch (opcion) {
                case 0:
                    System.out.println("Regresando a menu principal...");
                    break;
                case 1:
                    ingresarCategoriaService();
                    break;
                case 2:
                    listarCategoriasService();
                    break;
                case 3:
                    consultarCategoriaService();
                    break;
                case 4:
                    modificarCategoriaService();
                    break;
                case 5:
                    eliminarCategoriaService();
                    break;
                default:
                    System.out.println("Ingrese una opción válida.");
            }
        } while (opcion != 0);
    }

    // TODO:
    // Implementar todos los métodos del CRUD de categorías.
    private void ingresarCategoriaService() {
        System.out.println("\n____Ingresar Categoria:____");
        //int codigo = Secuencias.generarCodigoCategoria(); lo genera el service
        String nombre = pedirNombre(20);
        String descripcion = pedirDescripcion(30);

        Categoria categoria = new Categoria(nombre, descripcion);
        try {
            categoriaService.agregar(categoria);
            System.out.println("Categoría ingresada con éxito.");
        }catch (PropiedadInvalida ex) {
            System.out.println(ex.getMessage());
        }

    }

    private void listarCategoriasService() {
        List<Categoria> listaCategorias = categoriaService.listar();
        if(listaCategorias == null || listaCategorias.isEmpty()) {
            System.out.println("No hay categorías cargadas.");
            return;
        }
        System.out.println("\n____Categorias:____");
        listaCategorias.forEach(System.out::println);
    }

    private Categoria consultarCategoriaService() {
        List<Categoria> categorias = categoriaService.listar();
        if(categorias == null || categorias.isEmpty()) {
            System.out.println("No hay categorías cargadas.");
            return null;
        }

        System.out.println("\n____Categorias:____");
        categorias.forEach(System.out::println);

        int codigoCategoria = leerEntero("Ingrese código de categoría:");
        try {
            Categoria categoria = categoriaService.obtenerPorCodigo(codigoCategoria);
            System.out.println("Categoria buscada: " + categoria);
            return categoria;
        } catch (NoEncontrado ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    private void modificarCategoriaService() {
        try {
            System.out.println("\n____Modificar Categoria:____");
            Categoria categoriaBuscada = consultarCategoriaService();
            if(categoriaBuscada == null)
                return;

            int codigoCategoriaModificar = categoriaBuscada.getCodigo();

            String nombre = categoriaBuscada.getNombre(), descripcion = categoriaBuscada.getDescripcion();
            if(codigoCategoriaModificar > 0) {
                if(leerSiNo("Desea modificar el nombre? (S/N)"))
                    nombre = pedirNombre( 20);

                if (leerSiNo("Desea modificar la descripcion? (S/N)"))
                    descripcion = pedirDescripcion(30);
                Categoria categoriaEditar = new Categoria(codigoCategoriaModificar, nombre, descripcion);
                categoriaService.editar(codigoCategoriaModificar, categoriaEditar);
                System.out.println("Categoría modificada.");
            }
        }catch ( NoEncontrado | PropiedadInvalida ex){
            System.out.println(ex.getMessage());
        }
    }

    private void eliminarCategoriaService() {
        try {
            System.out.println("\n____Eliminar Categoria:____");
            Categoria categoria = consultarCategoriaService();

            if(categoria == null)
                return;

            int codigoCategoria = categoria.getCodigo();
            categoriaService.eliminar(codigoCategoria);
            System.out.println("Categoría eliminada.");
        } catch (NoEncontrado | RelacionExistente ex) {
            System.out.println(ex.getMessage());
        }
    }

    //------Mét0dos Auxiliares para pedir los datos necesarios en el CRUD de Categoria.------
    private String pedirNombre(int cantidadCaracteres) {
        String nombre;
        while (true) {
            nombre = leerTexto("Ingrese el nombre de la categoria:");
            if(!Validaciones.validarTextoNoVacio(nombre)) {
                System.out.println("Nombre no válido. No puedo estar vacío.");
                continue;
            }

            if(!Validaciones.validarLongitudMaxima(nombre,cantidadCaracteres)) {
                System.out.println("Nombre no puede exceder los " + cantidadCaracteres + " caracteres.");
                continue;
            }
            return nombre;
        }
    }

    private String pedirDescripcion(int cantidadCaracteres) {
        String descripcion;
        while (true) {
            descripcion = leerTexto("Ingrese descripcion de la categoria:");
            if(!Validaciones.validarTextoNoVacio(descripcion)) {
                System.out.println("Descripción no válida. No puedo estar vacía.");
                continue;
            }
            if(!Validaciones.validarLongitudMaxima(descripcion,cantidadCaracteres)) {
                System.out.println("Descripción no puede exceder los " + cantidadCaracteres + "caracteres.");
                continue;
            }
            return descripcion;
        }
    }
}