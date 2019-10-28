package model.entities;

import java.io.Serializable;

public class Pedido implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer codigo;
	private String nome;
	private Integer quantidade;
	private Double precoTotal;
	private String status;
	
	private Endereco endereco;
	private Produto produto;
	
	public Pedido() {
	}

	public Pedido(Integer codigo, Integer quantidade, Double precoTotal, String status, Endereco endereco, Produto produto) {
		this.codigo = codigo;
		this.quantidade = quantidade;
		this.precoTotal = precoTotal;
		this.status = status;
		this.endereco = endereco;
		this.produto = produto;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}
	
	public Double getPrecoTotal() {
		return precoTotal;
	}

	public void setPrecoTotal(Double precoTotal) {
		this.precoTotal = precoTotal;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pedido other = (Pedido) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Pedido [codigo=" + codigo + ", nome=" + nome + ", quantidade=" + quantidade + ", pre�o total=" + precoTotal + ", endereco=" + endereco
				+ ", produto=" + produto + "]";
	}	
}
