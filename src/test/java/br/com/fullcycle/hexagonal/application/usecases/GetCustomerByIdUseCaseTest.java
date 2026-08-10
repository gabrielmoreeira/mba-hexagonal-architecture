package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.infrastructure.models.Customer;
import br.com.fullcycle.hexagonal.infrastructure.services.CustomerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;

class GetCustomerByIdUseCaseTest {

    @Test()
    @DisplayName("Deve obter um cliente por id")
    public void testGetById() {
        // given
        final var expectedId = UUID.randomUUID().getMostSignificantBits();
        final var expectedCPF = "12345678901";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

        final var aCustomer = new Customer();
        aCustomer.setId(expectedId);
        aCustomer.setName(expectedName);
        aCustomer.setCpf(expectedCPF);
        aCustomer.setEmail(expectedEmail);

        final var input = new GetCustomerByIdUseCase.Input(expectedId);

        // when
        final var customerService = Mockito.mock(CustomerService.class);
        when(customerService.findById(expectedId)).thenReturn(Optional.of(aCustomer));

        final var useCase = new GetCustomerByIdUseCase(customerService);
        final var outPut = useCase.execute(input).get();

        // then
        Assertions.assertEquals(expectedId, outPut.id());
        Assertions.assertEquals(expectedCPF, outPut.cpf());
        Assertions.assertEquals(expectedEmail, outPut.email());
        Assertions.assertEquals(expectedName, outPut.name());
    }

    @Test()
    @DisplayName("Deve obter vazio ao tentar recuperar um cliente não existente por id")
    public void testGetByIdWithInvalidId() {
        // given
        final var expectedId = UUID.randomUUID().getMostSignificantBits();

        final var input = new GetCustomerByIdUseCase.Input(expectedId);

        // when
        final var customerService = Mockito.mock(CustomerService.class);
        when(customerService.findById(expectedId)).thenReturn(Optional.empty());

        final var useCase = new GetCustomerByIdUseCase(customerService);
        final var outPut = useCase.execute(input);

        // then
        Assertions.assertTrue(outPut.isEmpty());
    }
}