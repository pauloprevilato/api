package api.voll.med.api.domain.consulta.validacoes;

import api.voll.med.api.domain.consulta.DadosAgendamentoConsulta;
import api.voll.med.api.domain.paciente.PacienteRepository;
import api.voll.med.api.infra.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;

public class ValidadorPacienteAtivo implements ValidadorAgendamentoDeConsulta {

    @Autowired
    private PacienteRepository repository;

    public void validar(DadosAgendamentoConsulta dados) {

        var pacienteEstaAtivo = repository.findAtivoById(dados.idPaciente());
        if (!pacienteEstaAtivo) {
            throw new ValidacaoException("Consulta não pode ser agendada com paciente excluído");
        }

    }


}
