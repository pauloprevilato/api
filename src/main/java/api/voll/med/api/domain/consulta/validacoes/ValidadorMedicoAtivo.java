package api.voll.med.api.domain.consulta.validacoes;

import api.voll.med.api.domain.consulta.DadosAgendamentoConsulta;
import api.voll.med.api.domain.medico.MedicoRepository;
import api.voll.med.api.infra.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ValidadorMedicoAtivo implements ValidadorAgendamentoDeConsulta {

    @Autowired
    private MedicoRepository repository;

    public void validar(DadosAgendamentoConsulta dados) {
        // escolha do medico é opcional
        if (dados.idMedico() == null) {
            return;
        }

        var medicoEstaAtivo = repository.findAtivoById(dados.idMedico());
        if (!medicoEstaAtivo) {
            throw new ValidacaoException("Consulta não pode ser agendada com médico");
        }
    }
}
