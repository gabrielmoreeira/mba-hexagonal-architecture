package br.com.fullcycle.hexagonal.application.usecases.Customer;

import br.com.fullcycle.hexagonal.application.InMemoryCustomerRepository;
import br.com.fullcycle.hexagonal.application.domain.customer.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class GetCustomerByIdUseCaseTest {

    @Test()
    @DisplayName("Deve obter um cliente por id")
    public void testGetById() {
        // given
        final var expectedCPF = "509.303.800-88";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

        final var aCustomer = Customer.newCustomer(expectedName, expectedCPF, expectedEmail);

        final var customerRepository = new InMemoryCustomerRepository();
        customerRepository.create(aCustomer);

        final var expectedId = aCustomer.customerId().value().toString();

        final var input = new GetCustomerByIdUseCase.Input(expectedId);

        // when
        final var useCase = new GetCustomerByIdUseCase(customerRepository);
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
        final var expectedId = UUID.randomUUID().toString();

        final var input = new GetCustomerByIdUseCase.Input(expectedId);

        // when
        final var customerRepository = new InMemoryCustomerRepository();
        final var useCase = new GetCustomerByIdUseCase(customerRepository);
        final var outPut = useCase.execute(input);

        // then
        Assertions.assertTrue(outPut.isEmpty());
    }
}