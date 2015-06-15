package br.com.sisped.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.sisped.model.Cliente;
import br.com.sisped.repository.Clientes;
import br.com.sisped.util.jpa.Transactional;

public class CadastroClienteService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Clientes clientes;

	@Transactional
	public Cliente salvar(Cliente cliente) {
		Cliente clienteExistente = clientes.porDocumento(cliente.getDocumentoReceitaFederal());

		if (clienteExistente != null && !clienteExistente.equals(cliente)) {
			throw new NegocioException("Este cliente já está cadastrado.");
		}

		return clientes.guardar(cliente);
	}

}
