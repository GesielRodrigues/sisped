package br.com.sisped.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.sisped.model.Categoria;

public class Categorias implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager entity;

	public Categoria porId(Long id) {
		return entity.find(Categoria.class, id);
	}

	public List<Categoria> raizes() {
		return entity.createQuery("from Categoria where categoriaPai is null", Categoria.class).getResultList();
	}

	public List<Categoria> subcategoriasDe(Categoria categoriaPai) {
		return entity.createQuery("from Categoria where categoriaPai = :raiz", Categoria.class)
				.setParameter("raiz", categoriaPai).getResultList();
	}

}
