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

import br.com.sisped.model.Cliente;
import br.com.sisped.repository.filter.ClienteFilter;
import br.com.sisped.service.NegocioException;
import br.com.sisped.util.jpa.Transactional;

public class Clientes implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	EntityManager entity;

	public Cliente guardar(Cliente cliente) {
		return entity.merge(cliente);
	}

	public Cliente porDocumento(String documentoReceitaFederal) {
		try {
			return entity.createQuery("from Cliente where documentoReceitaFederal = :documento", Cliente.class)
					.setParameter("documento", documentoReceitaFederal).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Cliente> filtrados(ClienteFilter filtro) {
		Session session = entity.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Cliente.class);

		if (StringUtils.isNotBlank(filtro.getDocumentoReceitaFederal())) {
			criteria.add(Restrictions.eq("documentoReceitaFederal", filtro.getDocumentoReceitaFederal()));
		}

		if (StringUtils.isNotBlank(filtro.getNome())) {
			criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
		}

		return criteria.addOrder(Order.asc("nome")).list();
	}

	public Cliente porId(Long id) {
		return entity.find(Cliente.class, id);
	}

	@Transactional
	public void remover(Cliente clienteSelecionado) {
		try {
			clienteSelecionado = porId(clienteSelecionado.getId());
			entity.remove(clienteSelecionado);
			entity.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("O Cliente selecionado não pode ser excluído.");
		}

	}

	public List<Cliente> porNome(String nome) {
		return this.entity.createQuery("from Cliente " + "where upper(nome) like :nome", Cliente.class)
				.setParameter("nome", nome.toUpperCase() + "%").getResultList();
	}
}
