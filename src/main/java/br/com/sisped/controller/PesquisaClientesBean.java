package br.com.sisped.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sisped.model.Cliente;
import br.com.sisped.repository.Clientes;
import br.com.sisped.repository.filter.ClienteFilter;
import br.com.sisped.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaClientesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Clientes clientes;

	private Cliente clienteSelecionado;

	private ClienteFilter filter;

	private List<Cliente> clientesFiltrados;

	public PesquisaClientesBean() {
		this.filter = new ClienteFilter();
	}

	public ClienteFilter getFilter() {
		return filter;
	}

	public void setFilter(ClienteFilter filter) {
		this.filter = filter;
	}

	public List<Cliente> getClientesFiltrados() {
		return clientesFiltrados;
	}

	public Cliente getClienteSelecionado() {
		return clienteSelecionado;
	}

	public void setClienteSelecionado(Cliente clienteSelecionado) {
		this.clienteSelecionado = clienteSelecionado;
	}

	public void pesquisar() {
		this.clientesFiltrados = clientes.filtrados(filter);
		for (Cliente cliente : clientesFiltrados) {
			System.out.println(cliente.getNome());
		}
	}

	public void excluir() {
		clientes.remover(clienteSelecionado);
		clientesFiltrados.remove(clienteSelecionado);

		FacesUtil.addInfoMessage("Cliente " + clienteSelecionado.getNome() + " excluido com sucesso.");
	}
}
