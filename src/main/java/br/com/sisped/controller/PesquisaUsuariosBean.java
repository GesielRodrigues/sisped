package br.com.sisped.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sisped.model.Usuario;
import br.com.sisped.repository.Usuarios;
import br.com.sisped.repository.filter.UsuarioFilter;
import br.com.sisped.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaUsuariosBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Usuarios usuarios;

	private UsuarioFilter filter;

	private Usuario usuarioSelecionado;

	private List<Usuario> usuariosFiltrados;

	public PesquisaUsuariosBean() {
		this.filter = new UsuarioFilter();
	}

	public UsuarioFilter getFilter() {
		return filter;
	}

	public void setFilter(UsuarioFilter filter) {
		this.filter = filter;
	}

	public List<Usuario> getUsuariosFiltrados() {
		return usuariosFiltrados;
	}

	public Usuario getUsuarioSelecionado() {
		return usuarioSelecionado;
	}

	public void setUsuarioSelecionado(Usuario usuarioSelecionado) {
		this.usuarioSelecionado = usuarioSelecionado;
	}

	public void pesquisar() {
		this.usuariosFiltrados = usuarios.filtrados(this.filter);
	}

	public void excluir() {
		usuarios.remover(usuarioSelecionado);
		usuariosFiltrados.remove(usuarioSelecionado);

		FacesUtil.addInfoMessage("Usuário " + usuarioSelecionado.getNome() + " excluido com sucesso.");
	}
}