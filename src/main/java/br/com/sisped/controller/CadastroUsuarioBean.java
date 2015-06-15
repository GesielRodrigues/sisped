package br.com.sisped.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sisped.model.Grupo;
import br.com.sisped.model.Usuario;
import br.com.sisped.repository.Usuarios;
import br.com.sisped.service.CadastroUsuarioService;
import br.com.sisped.service.NegocioException;
import br.com.sisped.util.jpa.Transactional;
import br.com.sisped.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroUsuarioBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private CadastroUsuarioService service;

	@Inject
	private Usuarios usuarios;

	private List<Grupo> grupos;

	private Usuario usuario;
	private Grupo grupoSelecionado;

	public CadastroUsuarioBean() {
		limpar();
	}

	public void carregaGrupos() {
		if (this.usuario == null) {
			limpar();
		}
		this.grupos = usuarios.todosOsGrupos();
	}

	public List<Grupo> getGrupos() {
		return this.grupos;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Grupo getGrupoSelecionado() {
		return grupoSelecionado;
	}

	public void setGrupoSelecionado(Grupo grupo) {
		this.grupoSelecionado = grupo;
	}

	private void limpar() {
		this.usuario = new Usuario();
	}

	public void adicionaGrupo() {

		if (this.grupoSelecionado != null) {
			for (Grupo grupo : usuario.getGrupos()) {

				if (grupoSelecionado.equals(grupo)) {
					throw new NegocioException("Este grupo já foi adicionado");
				}
			}
		}
		this.usuario.getGrupos().add(this.grupoSelecionado);
	}

	public void removeGrupo() {
		this.usuario.getGrupos().remove(this.grupoSelecionado);
	}

	@Transactional
	public void salvar() {
		if (this.usuario.getGrupos().size() < 1) {
			throw new NegocioException("Adicione pelo menos um Grupo para o usuário.");
		}
		this.usuario = service.salvar(this.usuario);
		limpar();

		FacesUtil.addInfoMessage("Usuário salvo com sucesso!");
	}

	public boolean isEditando() {
		return this.usuario.getId() != null;
	}
}