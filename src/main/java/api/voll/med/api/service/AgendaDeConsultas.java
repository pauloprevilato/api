package api.voll.med.api.service;

import api.voll.med.api.domain.consulta.Consulta;
import api.voll.med.api.domain.consulta.DadosAgendamentoConsulta;
import api.voll.med.api.domain.consulta.DadosDetalhamentoConsulta;
import api.voll.med.api.domain.consulta.validacoes.ValidadorAgendamentoDeConsulta;
import api.voll.med.api.domain.medico.Medico;
import api.voll.med.api.domain.medico.MedicoRepository;
import api.voll.med.api.domain.paciente.PacienteRepository;
import api.voll.med.api.infra.exception.ValidacaoException;
import api.voll.med.api.repository.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
    public class AgendaDeConsultas {
        @Autowired
        private ConsultaRepository consultaRepository;

        @Autowired
        private MedicoRepository medicoRepository;

        @Autowired
        private PacienteRepository pacienteRepository;

        @Autowired
        private List<ValidadorAgendamentoDeConsulta> validadores;

        public DadosDetalhamentoConsulta agendar(DadosAgendamentoConsulta dados) {

            if (!pacienteRepository.existsById(dados.idPaciente())) {
                throw new ValidacaoException("Id do paciente informado não existe!");
            }

            if (dados.idMedico() != null && !medicoRepository.existsById(dados.idMedico())) {
                throw new ValidacaoException("Id do médico informado não existe!");
            }

            validadores.forEach(v ->  v.validar(dados));

            var paciente = pacienteRepository.getReferenceById(dados.idPaciente());
            var medico = escolherMedico(dados);
            if (medico == null) {
                throw new ValidacaoException("Não existe médico disponível nessa data!");
            }
            var consulta = new Consulta(null, medico, paciente, dados.data());
            consultaRepository.save(consulta);

            return new DadosDetalhamentoConsulta(consulta);
        }

        private Medico escolherMedico(DadosAgendamentoConsulta dados) {
            if (dados.idMedico() != null) {
                return medicoRepository.getReferenceById(dados.idMedico());
            }
            if (dados.especialidade() == null) {
                throw new ValidacaoException("Especialidade é obrigatória quando médico não for escolhido.");
            }

            return medicoRepository.escolherMedicoAleatorioLivreNaData(dados.especialidade(), dados.data());

        }



}