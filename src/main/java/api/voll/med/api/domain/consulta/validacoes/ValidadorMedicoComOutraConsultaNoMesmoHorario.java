package api.voll.med.api.domain.consulta.validacoes;

import api.voll.med.api.domain.consulta.DadosAgendamentoConsulta;
import api.voll.med.api.infra.exception.ValidacaoException;
import api.voll.med.api.repository.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class ValidadorMedicoComOutraConsultaNoMesmoHorario implements ValidadorAgendamentoDeConsulta {

    @Autowired
    private ConsultaRepository repository;

    public void validar(DadosAgendamentoConsulta dados) {
        var medicoPossuiOutraConsultaNoMesmoHorario = repository.existsByMedicoIdAndData(dados.idMedico(), dados.data());

        if (medicoPossuiOutraConsultaNoMesmoHorario) {
            throw new ValidacaoException("Médico já possui outra consulta agendada nesse mesmo horário");
        }
    }

}
