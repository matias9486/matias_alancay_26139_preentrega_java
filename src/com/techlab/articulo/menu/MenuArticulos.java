package com.techlab.articulo.menu;

import com.techlab.articulo.model.Articulo;
import com.techlab.articulo.model.Categoria;
import com.techlab.articulo.repository.Repositorio;
import com.techlab.articulo.utils.Validaciones;

/**
 * CONSIGNA DE ESTA CLASE
 * ------------------------------------------------------------
 * Esta clase debe heredar de Menu y encargarse del CRUD de artículos.
 *
 * Debe trabajar con:
 * - Repositorio<Articulo>
 * - Repositorio<Categoria>
 *
 * ¿Por qué necesita también categorías?
 * Porque un artículo debe asociarse a una categoría ya existente.
 *
 * FUNCIONALIDADES ESPERADAS
 * ------------------------------------------------------------
 * 1) Ingresar artículo
 * 2) Listar artículos
 * 3) Consultar un artículo por código
 * 4) Modificar un artículo
 * 5) Eliminar un artículo
 * 0) Volver
 *
 * REQUISITOS IMPORTANTES
 * ------------------------------------------------------------
 * - Antes de crear un artículo, debe verificarse que existan categorías.
 * - Debe preguntarse qué tipo de artículo se quiere crear:
 *   - electrónico
 *   - alimenticio
 * - Debe pedirse:
 *   - nombre
 *   - precio
 *   - categoría por código
 * - Si es electrónico:
 *   - garantía en meses
 * - Si es alimenticio:
 *   - días para vencimiento
 *
 * VALIDACIONES
 * ------------------------------------------------------------
 * - nombre no vacío
 * - precio no negativo
 * - categoría existente
 * - garantía no negativa
 * - días para vencimiento no negativos
 *
 * SUGERENCIA DE MÉTODOS
 * ------------------------------------------------------------
 * - ingresarArticulo()
 * - listarArticulos()
 * - consultarArticulo()
 * - modificarArticulo()
 * - eliminarArticulo()
 * - pedirCategoriaExistente()
 * - pedirNombreArticulo()
 * - pedirPrecioArticulo()
 * - pedirGarantia()
 * - pedirDiasParaVencimiento()
 */
public class MenuArticulos extends Menu {

    private Repositorio<Articulo> articuloRepositorio = new Repositorio<>();
    private Repositorio<Categoria> categoriaRepositorio = new Repositorio<>();

    public MenuArticulos(java.util.Scanner scanner, Repositorio<Articulo> articuloRepositorio, Repositorio<Categoria> categoriaRepositorio) {
        super(scanner);
        this.articuloRepositorio = articuloRepositorio;
        this.categoriaRepositorio = categoriaRepositorio;
    }

    @Override
    public void mostrarMenu() {
        System.out.println("\n--- MENÚ ARTÍCULOS ---");
        System.out.println("1 - Ingresar artículo");
        System.out.println("2 - Listar artículos");
        System.out.println("3 - Consultar artículo");
        System.out.println("4 - Modificar artículo");
        System.out.println("5 - Eliminar artículo");
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
                    ingresarArticulo();
                    break;
                default:
                    System.out.println();
            }
        }while (opcion != 0);
    }

    // TODO:
    // Implementar todos los métodos del CRUD de artículos.
    private void ingresarArticulo() {}

    //mét0dos auxiliares para pedir datos
    /*
     * - pedirCategoriaExistente()
     * - pedirNombreArticulo()
     * - pedirPrecioArticulo()
     * - pedirGarantia()
     * - pedirDiasParaVencimiento()
     */
    private Categoria pedirCategoriaExistente() {
        if (categoriaRepositorio.estaVacio()) {
            System.out.println("No hay categorías cargadas.");
            return null;
        }
        else {
            //mostrar categorias
            System.out.println("\n____Categorias:____");
            categoriaRepositorio.listar().forEach(System.out::println);

            int codigoCategoria = leerEntero("Ingrese código de categoría:");
            Categoria categoria = categoriaRepositorio.buscarPorCodigo(codigoCategoria);
            if (categoria != null) {
                System.out.println(categoria);
                return categoria;
            } else {
                System.out.println("No se encontró categoria con código: " + codigoCategoria);
                return null;
            }
        }
    }

    //Metodo al crear articulo. Valida por disponibilidad
    private String pedirNombreArticulo(int cantidadCaracteresMaximo) {
        String nombre;
        while (true) {
            nombre = leerTexto("Ingrese el nombre del artículo:");
            if(!Validaciones.validarTextoNoVacio(nombre)) {
                System.out.println("Nombre no válido. No puedo estar vacío.");
                continue;
            }
            if(!Validaciones.validarLongitudMaxima(nombre,cantidadCaracteresMaximo)) {
                System.out.println("Nombre no puede exceder los " + cantidadCaracteresMaximo + " caracteres.");
                continue;
            }

            if (articuloRepositorio.buscarPorNombre(nombre) != null) {
                System.out.println("Nombre no disponible. Ingrese otro nombre.");
                continue;
            }
            return nombre;
        }
    }

    //Metodo al modificar articulo. Valida por disponibilidad o si es del mismo articulo
    private String pedirNombreArticuloModificar(int codigoArticulo, int cantidadCaracteres) {
        String nombre;
        while (true) {
            nombre = leerTexto("Ingrese el nombre del artículo:");
            if(!Validaciones.validarTextoNoVacio(nombre)) {
                System.out.println("Nombre no válido. No puedo estar vacío.");
                continue;
            }

            if(!Validaciones.validarLongitudMaxima(nombre,cantidadCaracteres)) {
                System.out.println("Nombre no puede exceder los " + cantidadCaracteres + "caracteres.");
                continue;
            }

            Articulo articuloBuscado= articuloRepositorio.buscarPorNombre(nombre);
            //nombre disponible o ingreso el mismo nombre que tenia
            if (articuloBuscado == null || articuloBuscado.getCodigo() == codigoArticulo)
                return nombre;
            //el nuevo nombre ya está en uso
            if (articuloBuscado.getCodigo() != codigoArticulo) {
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
