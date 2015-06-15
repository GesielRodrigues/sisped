package br.com.sisped.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.sisped.model.Produto;
import br.com.sisped.repository.filter.ProdutoFilter;
import br.com.sisped.service.NegocioException;
import br.com.sisped.util.jpa.Transactional;

public class Produtos implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	EntityManager entity;

	public Produto guardar(Produto produto) {
		return entity.merge(produto);
	}

	@Transactional
	public void remover(Produto produto) {
		try {
			produto = porId(produto.getId());
			entity.remove(produto);
			entity.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("O produto selecionado não pode ser excluído.");
		}
	}

	public Produto porSku(String sku) {
		try {
			return entity.createQuery("from Produto where upper(sku) = :sku", Produto.class)
					.setParameter("sku", sku.toUpperCase()).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Produto> filtrados(ProdutoFilter filtro) {
		Session session = entity.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Produto.class);

		if (StringUtils.isNotBlank(filtro.getSku())) {
			criteria.add(Restrictions.eq("sku", filtro.getSku()));
		}

		if (StringUtils.isNotBlank(filtro.getNome())) {
			criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
		}

		return criteria.addOrder(Order.asc("nome")).list();
	}

	public List<Produto> todos() {
		return entity.createQuery("from Produto", Produto.class).getResultList();
	}

	public Produto porId(Long id) {
		return entity.find(Produto.class, id);
	}

	public List<Produto> porNome(String nome) {
		return this.entity.createQuery("from Produto where upper(nome) like :nome", Produto.class)
				.setParameter("nome", nome.toUpperCase() + "%").getResultList();
	}

}
