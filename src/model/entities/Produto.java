package model.entities;

import java.io.Serializable;
import java.util.List;

public class Produto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nome;
	private String itens; 
	
	public Produto() {
	}

	public Produto(String nome, String itens) {
		super();
		this.nome = nome;
		this.itens = itens;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getItens() {
		return itens;
	}

	public void setItens(String lista) {
		this.itens = lista;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
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
		Produto other = (Produto) obj;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Produto [nome=" + nome + "]";
	}
	
}
