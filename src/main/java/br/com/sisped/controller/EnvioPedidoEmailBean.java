package br.com.sisped.controller;

import java.io.Serializable;
import java.util.Locale;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.velocity.tools.generic.NumberTool;

import br.com.sisped.model.Pedido;
import br.com.sisped.util.jsf.FacesUtil;
import br.com.sisped.util.mail.Mailer;
import br.com.sisped.util.mail.VelocityTemplateUTF8;

import com.outjected.email.api.MailMessage;

@Named
@RequestScoped
public class EnvioPedidoEmailBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Mailer mailer;

	@Inject
	@PedidoEdicao
	private Pedido pedido;

	public void enviarPedido() {
		MailMessage message = mailer.novaMensagem();

		message.to(this.pedido.getCliente().getEmail())
				.subject("Pedido nr. " + this.pedido.getId())
				.bodyHtml(new VelocityTemplateUTF8(getClass().getResourceAsStream("/emails/pedido.template")))
				.put("pedido", this.pedido).put("numberTool", new NumberTool())
				.put("locale", new Locale("pt", "BR"))
				.charset("utf-8")
				.send();

		FacesUtil.addInfoMessage("Pedido enviado por e-mail com sucesso!");
	}

}