package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.Cliente;
import model.entities.Pedido;

public class PedidoService {
	
	public List<Pedido> findAll(){
		List<Pedido> list = new ArrayList<>();
		list.add(new Pedido(1, "Bolo", 2, 120.00, new Cliente(1, "rennisson@gmail.com", "rennisson", "(11)9-8193-6306")));
		list.add(new Pedido(2, "Bolo", 2, 120.00, new Cliente(2, "gabriel@gmail.com", "gabriel", "(11)9-1234-5678")));
		list.add(new Pedido(3, "Torta", 1, 60.00, new Cliente(3, "diogo@gmail.com", "diogo", "(11)9-9876-5432")));
		return list;
	}
}
