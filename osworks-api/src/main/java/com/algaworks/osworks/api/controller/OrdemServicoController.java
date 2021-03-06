package com.algaworks.osworks.api.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.osworks.api.model.OrdemServicoDTO;
import com.algaworks.osworks.api.model.OrdemServicoInput;
import com.algaworks.osworks.domain.model.OrdemServico;
import com.algaworks.osworks.domain.repository.OrdemServicoRepository;
import com.algaworks.osworks.domain.service.OrdemServicoService;

@RestController
@RequestMapping("/ordens-servico")
public class OrdemServicoController {

	@Autowired
	private OrdemServicoService ordemService;

	@Autowired
	OrdemServicoRepository ordemRepository;

	@Autowired
	private ModelMapper modelMapper;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public OrdemServicoDTO criar(@Valid @RequestBody OrdemServicoInput ordem) {
		OrdemServico ordemServico = toEntity(ordem);
		return toDTO(ordemService.criar(ordemServico));
	}

	@GetMapping
	public List<OrdemServicoDTO> listar() {
		return toCollectionDTO(ordemRepository.findAll());
	}

	@GetMapping("/{ordemId}")
	public ResponseEntity<OrdemServicoDTO> buscar(@PathVariable Long ordemId) {
		Optional<OrdemServico> ordem = ordemRepository.findById(ordemId);

		if (!ordem.isPresent()) {

			return ResponseEntity.notFound().build();
		}
		OrdemServicoDTO ordemDTO = toDTO(ordem.get());

		return ResponseEntity.ok(ordemDTO);
	}

	@PutMapping("/{ordemServicoId}/finalizacao")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void finalizar(@PathVariable Long ordemServicoId) {
		ordemService.finalizar(ordemServicoId);
	}

	@PutMapping("/{ordemServicoId}/cancelar")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void cancelar(@PathVariable Long ordemServicoId) {
		ordemService.cancelar(ordemServicoId);
	}

	private OrdemServicoDTO toDTO(OrdemServico ordemServico) {
		OrdemServicoDTO ordemDTO = modelMapper.map(ordemServico, OrdemServicoDTO.class);
		return ordemDTO;
	}

	private List<OrdemServicoDTO> toCollectionDTO(List<OrdemServico> ordensServico) {
		return ordensServico.stream().map(ordem -> toDTO(ordem)).collect(Collectors.toList());
	}

	private OrdemServico toEntity(OrdemServicoInput ordemInput) {
		return modelMapper.map(ordemInput, OrdemServico.class);
	}
}
