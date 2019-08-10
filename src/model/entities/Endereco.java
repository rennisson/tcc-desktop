package model.entities;

import java.io.Serializable;

public class Endereco implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String cep;
	private String rua;
	private String estado;
	private String cidade;
	private String bairro;
	private String complemento;
	
	private Cliente cliente;
	
	public Endereco() {
	}

	public Endereco(String cep, String rua, String estado, String cidade, String bairro, String complemento,
			Cliente cliente) {
		this.cep = cep;
		this.rua = rua;
		this.estado = estado;
		this.cidade = cidade;
		this.bairro = bairro;
		this.complemento = complemento;
		this.cliente = cliente;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		this.rua = rua;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
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
		result = prime * result + ((cep == null) ? 0 : cep.hashCode());
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
		Endereco other = (Endereco) obj;
		if (cep == null) {
			if (other.cep != null)
				return false;
		} else if (!cep.equals(other.cep))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Endereco [cep=" + cep + ", rua=" + rua + ", estado=" + estado + ", cidade=" + cidade + ", bairro="
				+ bairro + ", complemento=" + complemento + ", cliente=" + cliente + "]";
	}
}
