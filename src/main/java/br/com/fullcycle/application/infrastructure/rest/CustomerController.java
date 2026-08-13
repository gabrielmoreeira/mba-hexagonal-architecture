package br.com.fullcycle.application.infrastructure.rest;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.usecases.customer.CreateCustomerUseCase;
import br.com.fullcycle.hexagonal.application.usecases.customer.GetCustomerByIdUseCase;
import br.com.fullcycle.application.infrastructure.dtos.NewCustomerDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Objects;

// Adapter
@RestController
@RequestMapping(value = "customers")
public class CustomerController {

    private final CreateCustomerUseCase createCustomerUseCase;
    private final GetCustomerByIdUseCase getCustomerByIdUseCase;

    public CustomerController(final CreateCustomerUseCase createCustomerUseCase,
                              final GetCustomerByIdUseCase getCustomerByIdUseCase) {
        this.createCustomerUseCase = Objects.requireNonNull(createCustomerUseCase);
        this.getCustomerByIdUseCase = Objects.requireNonNull(getCustomerByIdUseCase);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody NewCustomerDTO dto) {
        try {
            final var outPut =
                    createCustomerUseCase.execute(new CreateCustomerUseCase.Input(dto.cpf(), dto.email(), dto.name()));

            return ResponseEntity.created(URI.create("/customers/" + outPut.id())).body(outPut);
        } catch (ValidationException exception) {
            return  ResponseEntity.unprocessableEntity().body(exception.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable String id) {
        return getCustomerByIdUseCase.execute(new GetCustomerByIdUseCase.Input(id))
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }
}