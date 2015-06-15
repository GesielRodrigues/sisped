package br.com.sisped.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.sisped.model.Usuario;
import br.com.sisped.repository.Usuarios;
import br.com.sisped.util.jpa.Transactional;

public class CadastroUsuarioService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Usuarios usuarios;

	@Transactional
	public Usuario salvar(Usuario usuario) {
		return usuarios.guardar(usuario);
	}
}
