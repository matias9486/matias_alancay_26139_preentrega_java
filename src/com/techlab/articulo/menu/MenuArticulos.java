package com.techlab.articulo.menu;

import com.techlab.articulo.excepciones.NoEncontrado;
import com.techlab.articulo.excepciones.PropiedadInvalida;
import com.techlab.articulo.model.Articulo;
import com.techlab.articulo.model.ArticuloAlimenticio;
import com.techlab.articulo.model.ArticuloElectronico;
import com.techlab.articulo.model.Categoria;
import com.techlab.articulo.service.ArticuloService;
import com.techlab.articulo.service.CategoriaService;
import com.techlab.articulo.utils.Validaciones;

import java.util.List;

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
    private ArticuloService articulos;
    private CategoriaService categorias;

    public MenuArticulos(java.util.Scanner scanner, ArticuloService articulos, CategoriaService categorias) {
        super(scanner);
        this.articulos = articulos;
        this.categorias = categorias;
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
                case 0:
                    System.out.println("Regresando a menu principal...");
                    break;
                case 1:
                    ingresarArticulo();
                    break;
                case 2:
                    listarArticulos();
                    break;
                case 3:
                    consultarArticulo();
                    break;
                case 4:
                    modificarArticulo();
                    break;
                case 5:
                    eliminarArticulo();
                    break;
                default:
                    System.out.println("Ingrese una opción válida.");
            }
        }while (opcion != 0);
    }

    // TODO:
    // Implementar todos los métodos del CRUD de artículos.
    private void ingresarArticulo() {
        try {
            System.out.println("----Ingresar Artículo----");
            Categoria categoria = pedirCategoriaExistente();

            String nombre = pedirNombre(20);
            double precio = pedirPrecio();

            Articulo nuevoArticulo = pedirTipoArticulo();
            //nuevoArticulo.setCodigo(Secuencias.generarCodigoArticulo()); lo agrega el service
            nuevoArticulo.setNombre(nombre);
            nuevoArticulo.setPrecio(precio);
            nuevoArticulo.setCategoria(categoria);

            articulos.agregar(nuevoArticulo);
            System.out.println("Artículo ingresado con éxito.");
        }catch (NoEncontrado ex) {
            System.out.println(ex.getMessage());
        }

    }

    private void listarArticulos(){
        List<Articulo> listaArticulos = articulos.listar();
        if(listaArticulos == null || listaArticulos.isEmpty())
            System.out.println("No hay artículos cargados.");
        else {
            System.out.println("----Articulos----");
            listaArticulos.forEach(System.out::println);
        }
    }

    private Articulo consultarArticulo() {
        List<Articulo> listaArticulos = articulos.listar();
        if(listaArticulos == null || listaArticulos.isEmpty())
            System.out.println("No hay artículos cargados.");
        else {
            System.out.println("----Articulos----");
            listaArticulos.forEach(System.out::println);
            int codigoArticulo = leerEntero("Ingrese código de artículo:");
            try {
                Articulo articulo = articulos.obtenerPorCodigo(codigoArticulo);
                System.out.println("Artículo buscado: " + articulo);
                return articulo;
            }catch (NoEncontrado ex) {
                System.out.println(ex.getMessage());
            }
        }
        return null;
    }

    private void modificarArticulo() {
        Articulo articulo = consultarArticulo();
        if(articulo == null)
            return;
        try {
            Articulo articuloEditar = null; //simula dto
            int codigoArticuloEditar = articulo.getCodigo();
            int garantia, vencimiento;
            String nombre = articulo.getNombre();
            double precio = articulo.getPrecio();
            Categoria categoria = articulo.getCategoria();

            if (leerSiNo("Desea modificar el nombre? (S/N)"))
                nombre = pedirNombre(20);
            if (leerSiNo("Desea modificar el precio? (S/N)"))
                precio = pedirPrecio();
            if (leerSiNo("Desea modificar la categoría? (S/N)"))
                categoria = pedirCategoriaExistente();

            //Pedir datos especificos. No permito modificar tipo de articulo
            if (articulo instanceof ArticuloElectronico electronico) {
                if (leerSiNo("Desea modificar la garantía? (S/N)"))
                    garantia = pedirGarantia();
                else
                    garantia = ((ArticuloElectronico) articulo).getGarantiaMeses();
                articuloEditar = new ArticuloElectronico(codigoArticuloEditar, nombre, precio, categoria, garantia);
            }
            if (articulo instanceof ArticuloAlimenticio alimenticio) {
                if (leerSiNo("Desea modificar los días para vencimiento? (S/N)"))
                    vencimiento = pedirDiasParaVencimiento();
                else
                    vencimiento = ((ArticuloAlimenticio) articulo).getDiasParaVencimiento();
                articuloEditar = new ArticuloAlimenticio(codigoArticuloEditar, nombre, precio, categoria, vencimiento);
            }

            articulos.editar(codigoArticuloEditar, articuloEditar);
            System.out.println("Artículo modificado.");
        } catch (NoEncontrado | PropiedadInvalida ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void eliminarArticulo() {
        Articulo articulo = consultarArticulo();
        if (articulo != null) {
            try {
                articulos.eliminar(articulo.getCodigo());
                System.out.println("Artículo eliminado con éxito.");
            }catch (NoEncontrado ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    //mét0dos auxiliares para pedir datos
    private Categoria pedirCategoriaExistente() throws NoEncontrado {
        List<Categoria> listaCategoria = categorias.listar();
        if (listaCategoria == null)
            throw new NoEncontrado("No hay categorías cargadas.");

        //mostrar categorias
        System.out.println("\n____Categorias disponibles:____");
        listaCategoria.forEach(System.out::println);

        int codigoCategoria = leerEntero("Ingrese código de la categoría del producto:");
        return categorias.obtenerPorCodigo(codigoCategoria);
    }

    //Metodo al crear articulo. Valida por disponibilidad
    private String pedirNombre(int cantidadCaracteresMaximo) {
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

            return nombre;
        }
    }

    private double pedirPrecio() {
        double precio;
        while (true) {
            precio = leerDouble("Ingrese precio del artículo:");
            if(!Validaciones.validarNoNegativo(precio)) {
                System.out.println("Precio no válida. No puedo ser negativo.");
                continue;
            }
            return precio;
        }
    }

    private int pedirGarantia() {
        int garantia;
        while (true) {
            garantia = leerEntero("Ingrese garantía en meses:");
            if(!Validaciones.validarNoNegativo(garantia)) {
                System.out.println("Garantia no válida. No puedo ser negativa.");
                continue;
            }
            return garantia;
        }
    }

    private int pedirDiasParaVencimiento() {
        int dias;
        while (true) {
            dias = leerEntero("Ingrese días para vencimiento:");
            if (!Validaciones.validarNoNegativo(dias)) {
                System.out.println("Días para vencimiento no válidos. No puedo ser negativo.");
                continue;
            }
            return dias;
        }
    }

    //Según el tipo de artículo elegido pide instancia un tipo de artículo y pide su dato específico.
    private Articulo pedirTipoArticulo() {
        int opcion;
        while (true) {
            opcion = leerEntero("Seleccione tipo de artículo a crear: 1- Electrónico. 2- Alimenticio.");

            switch (opcion) {
                case 1:
                    ArticuloElectronico nuevoArticuloElectronio = new ArticuloElectronico();
                    int mesesGarantia = pedirGarantia();
                    nuevoArticuloElectronio.setGarantiaMeses(mesesGarantia);
                    return nuevoArticuloElectronio;
                case 2:
                    ArticuloAlimenticio nuevoArticuloAlimenticio = new ArticuloAlimenticio();
                    int diasVencimiento = pedirDiasParaVencimiento();
                    nuevoArticuloAlimenticio.setDiasParaVencimiento(diasVencimiento);
                    return nuevoArticuloAlimenticio;
                default:
                    System.out.println("Opción no válida. Ingrese 1 o 2 para tipo de artículo a crear.");
                    continue;
            }
        }
    }
}
