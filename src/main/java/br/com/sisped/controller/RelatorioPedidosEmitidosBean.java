package br.com.sisped.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.hibernate.Session;

import br.com.sisped.util.jsf.FacesUtil;
import br.com.sisped.util.report.ExecutorRelatorio;

@Named
@RequestScoped
public class RelatorioPedidosEmitidosBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date dataInicio;
	private Date dataFim;

	@Inject
	private FacesContext facesContext;

	@Inject
	private HttpServletResponse response;

	@Inject
	private EntityManager manager;

	public void emitir() {

		if (periodoValido()) {

			Map<String, Object> parametros = new HashMap<>();
			parametros.put("data_inicio", this.dataInicio);
			parametros.put("data_fim", this.dataFim);

			ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/relatorio_pedidos_emitidos.jasper",
					this.response, parametros, "Pedidos emitidos.pdf");

			Session session = manager.unwrap(Session.class);
			session.doWork(executor);

			if (executor.isRelatorioGerado()) {
				facesContext.responseComplete();
			} else {
				FacesUtil.addErrorMessage("A execução do relatório não retornou dados.");
			}
		} else {
			FacesUtil.addErrorMessage("Data início não pode ser maior que data final.");
		}
	}

	private boolean periodoValido() {
		boolean mesmoDia = dataInicio.getTime() == dataFim.getTime();
		return dataInicio.before(dataFim) || mesmoDia;
	}

	@NotNull
	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	@NotNull
	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

}