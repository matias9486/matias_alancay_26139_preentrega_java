package com.techlab.articulo.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * CONSIGNA DE ESTA CLASE
 * ------------------------------------------------------------
 * Esta debe ser la clase base de todos los menús.
 *
 * Objetivo:
 * centralizar la lógica común para no repetir código.
 *
 * Esta clase debe:
 * - guardar un Scanner compartido
 * - declarar el método mostrarMenu()
 * - declarar el método ejecutar()
 *
 * Además, podés agregar métodos protegidos reutilizables, por ejemplo:
 * - leerEntero(String mensaje)
 * - leerDouble(String mensaje)
 * - leerTexto(String mensaje)
 * - leerSiNo(String mensaje)
 *
 * IMPORTANTE:
 * Esta clase debe ser abstracta, porque no tiene sentido crear un
 * "menú genérico" instanciable. Solo debe servir como base para:
 * - MenuArticulos
 * - MenuCategorias
 */
public abstract class Menu {

    protected Scanner scanner;

    public Menu(Scanner scanner) {
        this.scanner = scanner;
    }

    // TODO:
    // Declarar método abstracto para mostrar el menú.
    public abstract void mostrarMenu();

    // TODO:
    // Declarar método abstracto para ejecutar el menú.
    public abstract void ejecutar();

    // TODO:
    /* Agregar métodos auxiliares de lectura segura si querés reutilizar lógica.
     * - leerEntero(String mensaje)
     * - leerDouble(String mensaje)
     * - leerTexto(String mensaje)
     * - leerSiNo(String mensaje)
     */
    protected int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.println(mensaje);
                return Integer.parseInt(scanner.nextLine());
            }
            catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número entero válido.");
            }
        }
    }

    protected double leerDouble(String mensaje) {
        while (true) {
            try {
                System.out.println(mensaje);
                return Double.parseDouble(scanner.nextLine());
            }
            catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número decimal válido.");
            }
        }
    }

    protected String leerTexto(String mensaje) {
        System.out.println(mensaje);
        return scanner.nextLine().trim();
    }

    protected boolean leerSiNo(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String respuesta = scanner.nextLine().trim().toUpperCase();

            if (respuesta.equals("S")) {
                return true;
            }

            if (respuesta.equals("N")) {
                return false;
            }
            System.out.println("Error: debe ingresar S o N.");
        }
    }
}
