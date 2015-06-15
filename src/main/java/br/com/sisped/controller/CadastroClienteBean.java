package br.com.sisped.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sisped.model.Cliente;
import br.com.sisped.model.Endereco;
import br.com.sisped.model.TipoPessoa;
import br.com.sisped.service.CadastroClienteService;
import br.com.sisped.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroClienteBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private CadastroClienteService service;

	private Cliente cliente;
	private Endereco endereco;

	public CadastroClienteBean() {
		limpar();
	}

	public void inicializar() {
		if (this.cliente == null) {
			limpar();
		}
	}

	private void limpar() {
		this.cliente = new Cliente();
		this.endereco = new Endereco();
	}

	public void salvar() {
		this.cliente = this.service.salvar(this.cliente);
		limpar();

		FacesUtil.addInfoMessage("Cliente salvo com sucesso!");
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public TipoPessoa[] getTipos() {
		return TipoPessoa.values();
	}

	public void adicionaEndereco() {
		this.cliente.getEnderecos().add(endereco);
		this.endereco.setCliente(cliente);

		this.endereco = new Endereco();
	}

	public boolean isEditando() {
		return this.cliente.getId() != null;
	}
}