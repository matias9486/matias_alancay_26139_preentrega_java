package com.techlab.articulo.menu;

public class MenuPrincipal extends Menu{
    private MenuArticulos menuArticulos;
    private MenuCategorias menuCategorias;

    public MenuPrincipal(java.util.Scanner scanner, MenuArticulos menuArticulos, MenuCategorias menuCategorias) {
        super(scanner);
        this.menuArticulos = menuArticulos;
        this.menuCategorias = menuCategorias;
    }

    @Override
    public void mostrarMenu() {
        System.out.println("\n--- MENÚ PRINCIPAL ---");
        System.out.println("1 - Menú de Categorías");
        System.out.println("2 - Menú de Artículos");
        System.out.println("0 - Salir");
    }

    @Override
    public void ejecutar() {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una opción:");
            switch (opcion) {
                case 1:
                    menuCategorias.ejecutar();
                    break;
                case 2:
                    menuArticulos.ejecutar();
                    break;
                case 0:
                    System.out.println("Gracias por usar TalentoTech System :v");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

}
