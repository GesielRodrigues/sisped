package br.com.sisped.controller;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

import br.com.sisped.model.Usuario;
import br.com.sisped.repository.Pedidos;
import br.com.sisped.repository.Usuarios;
import br.com.sisped.security.UsuarioLogado;
import br.com.sisped.security.UsuarioSistema;

@Named
@RequestScoped
public class GraficoPedidosCriadosBean {

	private static DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM");

	@Inject
	private Pedidos pedidos;

	@Inject
	private Usuarios usuarios;

	@Inject
	@UsuarioLogado
	private UsuarioSistema usuarioLogado;

	private LineChartModel model;

	public void preRender() {
		this.model = new LineChartModel();
		this.model.setTitle("Pedidos criados nos últimos 15 dias");
		this.model.setAnimate(true);
		this.model.setLegendPosition("e");

		this.model.getAxes().put(AxisType.X, new CategoryAxis("Datas"));

		this.model.getAxes().get(AxisType.Y).setMin(0);
		this.model.getAxes().get(AxisType.Y).setLabel("Valores");

		for (Usuario usuario : usuarios.vendedores()) {
			adicionarSerie(usuario.getNome(), usuario);
		}
	}

	private void adicionarSerie(String rotulo, Usuario vendedor) {
		Map<Date, BigDecimal> valoresPorData = this.pedidos.valoresTotaisPorData(15, vendedor);

		ChartSeries series = new ChartSeries();
		series.setLabel(rotulo);

		for (Date data : valoresPorData.keySet()) {
			series.set(DATE_FORMAT.format(data), valoresPorData.get(data));
		}

		this.model.addSeries(series);
	}

	public LineChartModel getModel() {
		return model;
	}

}