package br.com.sisped.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import br.com.sisped.model.Categoria;
import br.com.sisped.model.Produto;
import br.com.sisped.repository.Categorias;
import br.com.sisped.service.CadastroProdutoService;
import br.com.sisped.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroProdutoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Categorias categorias;

	@Inject
	private CadastroProdutoService cadastroProdutoService;

	private Produto produto;
	private Categoria categoriaPai;
	private List<Categoria> categoriasRaizes;

	public CadastroProdutoBean() {
		limpar();
	}

	private void limpar() {
		this.produto = new Produto();
		this.categoriaPai = null;
	}

	public void inicializarCategorias() {
		if (this.produto == null) {
			limpar();
		}
		this.categoriasRaizes = categorias.raizes();

		if (this.categoriaPai != null) {
			carregarSubcategorias();
		}
	}

	public void carregarSubcategorias() {
		this.categoriaPai.setSubcategorias(categorias.subcategoriasDe(categoriaPai));

	}

	public void salvar() {
		this.produto = cadastroProdutoService.salvar(this.produto);
		limpar();

		FacesUtil.addInfoMessage("Produto salvo com sucesso!");
	}

	public Produto getProduto() {
		return this.produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;

		if (this.produto != null) {
			this.categoriaPai = this.produto.getCategoria().getCategoriaPai();
		}
	}

	public List<Categoria> getCategoriasRaizes() {
		return this.categoriasRaizes;
	}

	@NotNull
	public Categoria getCategoriaPai() {
		return categoriaPai;
	}

	public void setCategoriaPai(Categoria categoriaPai) {
		this.categoriaPai = categoriaPai;
	}

	public boolean isEditando() {
		return this.produto.getId() != null;
	}

}