package com.techlab.articulo.service;

import com.techlab.articulo.excepciones.NoEncontrado;
import com.techlab.articulo.excepciones.PropiedadInvalida;
import com.techlab.articulo.excepciones.RelacionExistente;
import com.techlab.articulo.model.Articulo;
import com.techlab.articulo.model.Categoria;
import com.techlab.articulo.repository.Repositorio;
import com.techlab.articulo.utils.Secuencias;

import java.util.List;

public class CategoriaService {
    private Repositorio<Categoria> categorias;
    private Repositorio<Articulo> articulos;

    public CategoriaService(Repositorio<Categoria> categorias, Repositorio<Articulo> articulos) {
        this.categorias = categorias;
        this.articulos = articulos;
    }

    public List<Categoria> listar() {
        return categorias.listar();
    }

    public Categoria obtenerPorCodigo(int codigo) throws NoEncontrado{
        Categoria categoria = categorias.buscarPorCodigo(codigo);
        if (categoria == null)
            throw new NoEncontrado("No se encontró una categoría con Código: " + codigo);
        return categoria;
    }

    public void agregar(Categoria categoria) throws PropiedadInvalida{
        if (categorias.buscarPorNombre(categoria.getNombre()) != null) {
            throw new PropiedadInvalida("Ya existe una categoría con ese nombre.");
        }
        categorias.agregar(new Categoria(Secuencias.generarCodigoCategoria(), categoria.getNombre(), categoria.getDescripcion()));
    }

    public void editar(int categoriaId, Categoria categoriaEditar) throws NoEncontrado, PropiedadInvalida{
        Categoria categoria = categorias.buscarPorCodigo(categoriaId);
        if (categoria == null)
            throw new NoEncontrado("No se encontró una categoría con ID: "+ categoriaId);

        Categoria categoriaConNombreExistente = categorias.buscarPorNombre(categoriaEditar.getNombre());
        if (categoriaConNombreExistente != null && categoriaConNombreExistente.getCodigo() != categoriaId)
            throw new PropiedadInvalida("Ya existe una categoría con ese nombre.");

        categoria.setNombre(categoriaEditar.getNombre());
        categoria.setDescripcion(categoriaEditar.getDescripcion());
    }

    public void eliminar(int categoriaId) throws NoEncontrado, RelacionExistente{
        Categoria categoria = categorias.buscarPorCodigo(categoriaId);
        if (categoria == null)
            throw new NoEncontrado("No se encontró una categoría con ese ID: "+ categoriaId);

        if (articulos.listar().stream().anyMatch(articulo -> articulo.getCategoria().getCodigo() == categoriaId))
            throw new RelacionExistente("No se puede eliminar la categoría. Tiene artículos asociados.");

        categorias.eliminar(categoria);
    }
}
