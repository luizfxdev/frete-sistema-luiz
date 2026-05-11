package br.com.nextlog.dashboard;

import br.com.nextlog.exception.NegocioException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DashboardBO {
  private static final Logger LOG = Logger.getLogger(DashboardBO.class.getName());
  private final DashboardDAO dashboardDAO = new DashboardDAO();

  public Map<String, Object> indicadores() {
    try {
      return dashboardDAO.indicadores();
    } catch (SQLException e) {
      LOG.log(Level.SEVERE, "Erro ao carregar indicadores", e);
      throw new NegocioException("Erro ao carregar indicadores.");
    }
  }

  public Map<String, Object> kpis() {
    try {
      return dashboardDAO.kpis();
    } catch (SQLException e) {
      LOG.log(Level.SEVERE, "Erro ao carregar KPIs", e);
      throw new NegocioException("Erro ao carregar KPIs.");
    }
  }

  public Map<String, Object> performance(LocalDate dataInicio, LocalDate dataFim) {
    if (dataInicio == null) {
      dataInicio = LocalDate.now().minusDays(30);
    }
    if (dataFim == null) {
      dataFim = LocalDate.now();
    }
    if (dataFim.isBefore(dataInicio)) {
      throw new NegocioException("Data final anterior à inicial.");
    }

    try {
      return dashboardDAO.performance(dataInicio, dataFim);
    } catch (SQLException e) {
      LOG.log(Level.SEVERE, "Erro ao carregar performance", e);
      throw new NegocioException("Erro ao carregar performance.");
    }
  }

  public Map<String, Object> performanceCompleta(LocalDate dataInicio, LocalDate dataFim) {
    if (dataInicio == null) {
      dataInicio = LocalDate.now().minusDays(30);
    }
    if (dataFim == null) {
      dataFim = LocalDate.now();
    }
    if (dataFim.isBefore(dataInicio)) {
      throw new NegocioException("Data final anterior à inicial.");
    }

    try {
      return dashboardDAO.performanceCompleta(dataInicio, dataFim);
    } catch (SQLException e) {
      LOG.log(Level.SEVERE, "Erro ao carregar performance completa", e);
      throw new NegocioException("Erro ao carregar performance.");
    }
  }

  public List<Map<String, Object>> entregasPorPeriodo(LocalDate inicio, LocalDate fim) {
    if (inicio == null) {
      inicio = LocalDate.now().minusDays(30);
    }
    if (fim == null) {
      fim = LocalDate.now();
    }
    if (fim.isBefore(inicio)) {
      throw new NegocioException("Data final anterior à inicial.");
    }

    try {
      return dashboardDAO.entregasPorPeriodo(inicio, fim);
    } catch (SQLException e) {
      LOG.log(Level.SEVERE, "Erro ao carregar entregas", e);
      throw new NegocioException("Erro ao carregar entregas.");
    }
  }

  public List<Map<String, Object>> rankingMotoristas(int limite) {
    try {
      return dashboardDAO.rankingMotoristas(limite > 0 ? limite : 10);
    } catch (SQLException e) {
      LOG.log(Level.SEVERE, "Erro ao carregar ranking de motoristas", e);
      throw new NegocioException("Erro ao carregar ranking de motoristas.");
    }
  }

  public List<Map<String, Object>> rotasMaisMovimentadas(int limite) {
    try {
      return dashboardDAO.rotasMaisMovimentadas(limite > 0 ? limite : 5);
    } catch (SQLException e) {
      LOG.log(Level.SEVERE, "Erro ao carregar rotas", e);
      throw new NegocioException("Erro ao carregar rotas.");
    }
  }
}