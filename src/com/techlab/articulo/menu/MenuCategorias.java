package com.techlab.articulo.menu;

import com.techlab.articulo.model.Articulo;
import com.techlab.articulo.model.Categoria;
import com.techlab.articulo.repository.Repositorio;
import com.techlab.articulo.utils.Secuencias;
import com.techlab.articulo.utils.Validaciones;

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
    private Repositorio<Categoria> categorias;
    private Repositorio<Articulo> articulos;

    public MenuCategorias(java.util.Scanner scanner, Repositorio<Categoria> categoriaRepositorio, Repositorio<Articulo> articuloRepositorio) {
        super(scanner);
        this.categorias = categoriaRepositorio;
        this.articulos = articuloRepositorio;
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
                case 1:
                    ingresarCategoria();
                    break;
                case 2:
                    listarCategorias();
                    break;
                case 3:
                    System.out.println("\n____Consultar Categoria:____");
                    consultarCategoria();
                    break;
                case 4:
                    modificarCategoria();
                    break;
                case 5:
                    eliminarCategoria();
                    break;
                default:
                    System.out.println("Ingrese una opción válida.");
            }
        } while (opcion != 0);
        System.out.println("Gracias por usar TalentoTech System :v");
    }

    // TODO:
    // Implementar todos los métodos del CRUD de categorías.
    private void ingresarCategoria() {
        System.out.println("\n____Ingresar Categoria:____");
        //codigo, nombre, descripcion
        int codigo = Secuencias.generarCodigoCategoria();
        String nombre = pedirNombre(20);
        String descripcion = pedirDescripcion(30);

        Categoria categoria = new Categoria(codigo, nombre, descripcion);
        categorias.agregar(categoria);
        System.out.println("Categoría ingresada con éxito.");
    }

    private void listarCategorias() {
        if(!categorias.estaVacio()) {
            System.out.println("\n____Categorias:____");
            categorias.listar().forEach(System.out::println);
        }
        else
            System.out.println("No hay categorías cargadas.");
    }

    private Categoria consultarCategoria() {
        if (categorias.estaVacio()) {
            System.out.println("No hay categorías cargadas.");
            return null;
        }
        else {
            //mostrar categorias
            System.out.println("\n____Categorias:____");
            categorias.listar().forEach(System.out::println);

            int codigoCategoria = leerEntero("Ingrese código de categoría:");
            Categoria categoria = categorias.buscarPorCodigo(codigoCategoria);
            if (categoria != null) {
                System.out.println(categoria);
                return categoria;
            } else {
                System.out.println("No se encontró categoria con código: " + codigoCategoria);
                return null;
            }
        }
    }

    private void modificarCategoria() {
        System.out.println("\n____Modificar Categoria:____");
        Categoria categoriaEditar = consultarCategoria();
        if(categoriaEditar != null) {
            if(leerSiNo("Desea modificar el nombre? (S/N)"))
                categoriaEditar.setNombre(pedirNombreModificar(categoriaEditar.getCodigo(), 20));

            if (leerSiNo("Desea modificar la descripcion? (S/N)"))
                categoriaEditar.setDescripcion(pedirDescripcion(30));

            System.out.println("Categoría modificada con éxito.");
        }
    }

    private void eliminarCategoria() {
        System.out.println("\n____Eliminar Categoria:____");
        Categoria categoria = consultarCategoria();
        if(categoria != null) {
            if(categoriaTieneArticulosAsociados(categoria.getCodigo())) {
                System.out.println("No se puede eliminar categoría. Tiene artículos asociados.");
            } else {
                categorias.eliminar(categoria);
                System.out.println("Categoría eliminada.");
            }
        }
    }

    private boolean categoriaTieneArticulosAsociados(int codigoCategoria) {
        return articulos.listar().stream().anyMatch(articulo -> articulo.getCategoria().getCodigo() == codigoCategoria);
    }

            //------Mét0dos Auxiliares para pedir los datos necesarios en el CRUD de Categoria.------
    //Mét0do utilizado para el alta de categoria. Valida que no exista el nombre
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

            if (categorias.buscarPorNombre(nombre) != null) {
                System.out.println("Nombre no disponible. Ingrese otro nombre.");
                continue;
            }
            return nombre;
        }
    }

    //Utilizado para editar categoria. Cambia la forma de validar la disponibilidad del nombre
    private String pedirNombreModificar(int codigoCategoria, int cantidadCaracteres) {
        String nombre;
        while (true) {
            nombre = leerTexto("Ingrese el nombre de la categoria:");
            if(!Validaciones.validarTextoNoVacio(nombre)) {
                System.out.println("Nombre no válido. No puedo estar vacío.");
                continue;
            }

            if(!Validaciones.validarLongitudMaxima(nombre,cantidadCaracteres)) {
                System.out.println("Nombre no puede exceder los " + cantidadCaracteres + "caracteres.");
                continue;
            }

            Categoria categoriaBuscada= categorias.buscarPorNombre(nombre);
            //nombre disponible o ingreso el mismo nombre que tenia
            if (categoriaBuscada == null || categoriaBuscada.getCodigo() == codigoCategoria)
                return nombre;
            //el nuevo nombre ya está en uso
            if (categoriaBuscada.getCodigo() != codigoCategoria) {
                System.out.println("Nombre no disponible. Ingrese otro nombre.");
                continue;
            }
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