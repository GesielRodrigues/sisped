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

import br.com.sisped.model.Grupo;
import br.com.sisped.model.Usuario;
import br.com.sisped.repository.filter.UsuarioFilter;
import br.com.sisped.service.NegocioException;
import br.com.sisped.util.jpa.Transactional;

public class Usuarios implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager entity;

	public Usuario guardar(Usuario usuario) {
		return entity.merge(usuario);
	}

	public List<Grupo> todosOsGrupos() {
		return entity.createQuery("from Grupo", Grupo.class).getResultList();
	}

	public Grupo grupoPorId(Long id) {
		return entity.find(Grupo.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<Usuario> filtrados(UsuarioFilter filtro) {
		Session session = entity.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Usuario.class);

		if (StringUtils.isNotBlank(filtro.getNome())) {
			criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
		}

		return criteria.addOrder(Order.asc("nome")).list();
	}

	@Transactional
	public void remover(Usuario usuarioSelecionado) {
		try {
			usuarioSelecionado = porId(usuarioSelecionado.getId());
			entity.remove(usuarioSelecionado);
			entity.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("O usuario selecionado não pode ser excluído.");
		}

	}

	public Usuario porId(Long id) {
		return entity.find(Usuario.class, id);
	}

	public List<Usuario> vendedores() {
		return this.entity.createQuery("from Usuario", Usuario.class).getResultList();
	}

	public Usuario porEmail(String email) {
		Usuario usuario = null;

		try {
			usuario = this.entity.createQuery("from Usuario where lower(email) = :email", Usuario.class)
					.setParameter("email", email.toLowerCase()).getSingleResult();
		} catch (NoResultException e) {
			// nenhum usuário encontrado com o e-mail informado
		}

		return usuario;
	}

}
