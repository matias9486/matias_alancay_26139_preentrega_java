package com.techlab.articulo.service;

import com.techlab.articulo.excepciones.NoEncontrado;
import com.techlab.articulo.excepciones.PropiedadInvalida;
import com.techlab.articulo.model.Articulo;
import com.techlab.articulo.model.ArticuloAlimenticio;
import com.techlab.articulo.model.ArticuloElectronico;
import com.techlab.articulo.model.Categoria;
import com.techlab.articulo.repository.Repositorio;
import com.techlab.articulo.utils.Secuencias;

import java.util.List;

public class ArticuloService {
    private Repositorio<Articulo> articulos;
    private Repositorio<Categoria> categorias;

    public ArticuloService(Repositorio<Articulo> articulos, Repositorio<Categoria> categorias) {
        this.articulos = articulos;
        this.categorias = categorias;
    }

    public List<Articulo> listar() {
        return articulos.listar();
    }

    public Articulo obtenerPorCodigo(int articuloId) throws NoEncontrado {
        Articulo articulo = articulos.buscarPorCodigo(articuloId);
        if (articulo == null)
            throw new NoEncontrado("No se encontró un artículo con ID: " + articuloId);
        return articulo;
    }

    public void agregar(Articulo articulo) throws PropiedadInvalida, NoEncontrado {
        //Validar categoria vinculada
        if (articulo.getCategoria() == null) {
            throw new PropiedadInvalida("El artículo debe tener una categoría asociada.");
        }
        //validar disponibilidad nombre
        if (articulos.buscarPorNombre(articulo.getNombre()) != null) {
            throw new PropiedadInvalida("Ya existe un artículo con ese nombre.");
        }

        //validar categoria existente
        Categoria categoriaExistente = categorias.buscarPorCodigo(articulo.getCategoria().getCodigo());
        if (categoriaExistente == null)
            throw new NoEncontrado("No se encontró una categoría con ID: " + articulo.getCategoria().getCodigo());

        articulo.setCategoria(categoriaExistente);
        articulo.setCodigo(Secuencias.generarCodigoArticulo());
        articulos.agregar(articulo);
    }

    public void editar(int articuloId, Articulo articuloEditar) throws NoEncontrado, PropiedadInvalida{
        //Validar categoria vinculada
        if (articuloEditar.getCategoria() == null) {
            throw new PropiedadInvalida("El artículo debe tener una categoría asociada.");
        }

        Articulo articuloExistente = articulos.buscarPorCodigo(articuloId);
        //validar articulo existente en repositorio
        if (articuloExistente == null)
            throw new NoEncontrado("No se encontró un artículo con ID: "+ articuloId);

        //Validar que no se intente cambiar el tipo de artículo
        if (!articuloExistente.getClass().equals(articuloEditar.getClass())) {
            throw new PropiedadInvalida("No se puede cambiar el tipo de artículo");
        }

        //validar disponibilidad del nombre
        Articulo articuloConNombreExistente = articulos.buscarPorNombre(articuloEditar.getNombre());
        if (articuloConNombreExistente != null && articuloConNombreExistente.getCodigo() != articuloId)
            throw new PropiedadInvalida("Ya existe un artículo con ese nombre.");

        //validar categoria existente
        Categoria categoriaExistente = categorias.buscarPorCodigo(articuloEditar.getCategoria().getCodigo());
        if (categoriaExistente == null)
            throw new NoEncontrado("No se encontró una categoría con ID: "+ articuloEditar.getCategoria().getCodigo());

        //Manejar los atributos específicos usando Pattern Matching
        if (articuloExistente instanceof ArticuloElectronico electExistente &&
                articuloEditar instanceof ArticuloElectronico electEditar) {
            electExistente.setGarantiaMeses(electEditar.getGarantiaMeses());
        } else if (articuloExistente instanceof ArticuloAlimenticio alimExistente &&
                articuloEditar instanceof ArticuloAlimenticio alimEditar) {
            alimExistente.setDiasParaVencimiento(alimEditar.getDiasParaVencimiento());
        }

        //editar datos
        articuloExistente.setNombre(articuloEditar.getNombre());
        articuloExistente.setPrecio(articuloEditar.getPrecio());
        articuloExistente.setCategoria(categoriaExistente);
    }

    public void eliminar(int articuloId) throws NoEncontrado{
        Articulo articulo = articulos.buscarPorCodigo(articuloId);
        if (articulo == null )
            throw new NoEncontrado("No se encontró un artículo con ID: " + articuloId);
        articulos.eliminar(articulo);
    }
}
