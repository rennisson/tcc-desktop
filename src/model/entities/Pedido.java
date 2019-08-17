package model.entities;

import java.io.Serializable;

import javafx.scene.control.Button;

public class Pedido implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer codigo;
	private String nome;
	private Integer quantidade;
	private Double precoTotal;
	private Button btnEditar;
	private Button btnExcluir;
	
	private Cliente cliente;
	
	public Pedido() {
	}

	public Pedido(Integer codigo, String nome, Integer quantidade, Double precoTotal, Cliente cliente) {
		this.codigo = codigo;
		this.nome = nome;
		this.quantidade = quantidade;
		this.precoTotal = precoTotal;
		this.cliente = cliente;
		this.btnEditar = new Button("Editar");
		this.btnExcluir = new Button("Excluir");
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

	public Button getBtnEditar() {
		return btnEditar;
	}

	public Button getBtnExcluir() {
		return btnExcluir;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
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
		return "Pedido [codigo=" + codigo + ", nome=" + nome + ", quantidade=" + quantidade + ", preço total=" + precoTotal + ", cliente=" + cliente
				+ "]";
	}	
}
