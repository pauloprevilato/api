package api.voll.med.api.domain.consulta.validacoes;

import api.voll.med.api.domain.consulta.DadosAgendamentoConsulta;
import api.voll.med.api.infra.exception.ValidacaoException;
import api.voll.med.api.repository.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class ValidadorPacienteSemOutraConsultaNoDia implements ValidadorAgendamentoDeConsulta {

    @Autowired
    private ConsultaRepository repository;

    public void validar(DadosAgendamentoConsulta dados) {
        var primeiroHorario = dados.data().withHour(7);
        var ultimoHorario = dados.data().withHour(18);
        var pacientePossuiOutraConsultaNoDia = repository.existsByPacienteIdAndDataBetween(dados.idPaciente(), primeiroHorario, ultimoHorario);

        if (pacientePossuiOutraConsultaNoDia) {
            throw new ValidacaoException("Paciente já possui uma consulta agendada nesse dia");
        }
    }
}