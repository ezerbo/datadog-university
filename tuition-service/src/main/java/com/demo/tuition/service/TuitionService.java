package com.demo.tuition.service;

import com.demo.tuition.error.NoTuitionFoundException;
import com.demo.tuition.model.Tuition;
import com.demo.tuition.model.api.CreateTuitionRequest;
import com.demo.tuition.model.api.UpdateTuitionRequest;
import com.demo.tuition.repository.TuitionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TuitionService {

    private final TuitionRepository repository;

    public TuitionService(TuitionRepository repository) {
        this.repository = repository;
    }

    public Tuition create(CreateTuitionRequest request) {
        log.info("Creating a new tuition record: {}", request);
        if (repository.existsByStudentId(request.getStudentId())) {
            log.error("A tuition record already exists for student with id: '{}'", request.getStudentId());
            throw new RuntimeException(String.format("A tuition record already exists for student with id: %s",
                    request.getStudentId()));
        }
        Tuition tuition = Tuition.builder()
                .studentId(request.getStudentId())
                .paid(request.isPaid())
                .amount(request.getAmount())
                .build();
        tuition = repository.save(tuition);
        log.info("Created tuition record: {}", tuition);
        return tuition;
    }

    public Tuition update(Long id, UpdateTuitionRequest request) {
        log.info("Updating tuition record: {}", request);
        Tuition tuition = repository.findByStudentId(request.getStudentId())
                .orElseThrow(() -> {
                    log.error("no tuition record found with id: {}", id);
                    return new NoTuitionFoundException("No tuition record found with id: '%s'", id);
                });
        tuition.setPaid(request.isPaid());
        return repository.save(tuition);
    }

    public Tuition get(Long id) {
        log.info("Getting tuition record with id: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.error("No tuition record found with id: {}", id);
                    return new NoTuitionFoundException("No tuition record found with id: '%s'", id);
                });
    }

    public void delete(Long id) {
        log.info("Deleting tuition record with id: {}", id);
        repository.deleteById(id);
    }
}
