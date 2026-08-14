package br.com.fullcycle.partner;

import br.com.fullcycle.repository.InMemoryPartnerRepository;
import br.com.fullcycle.domain.partner.Partner;
import br.com.fullcycle.domain.exceptions.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CreatePartnerUseCaseTest {
    @Test
    @DisplayName("Deve criar um parceiro")
    public void testCreatePartner() {

        // given
        final var expectedCNPJ = "73.079.912/1727-06";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

        final var createInput = new CreatePartnerUseCase.Input(expectedCNPJ, expectedEmail, expectedName);

        // when
        final var partnerRepository = new InMemoryPartnerRepository();
        final var useCase = new CreatePartnerUseCase(partnerRepository);
        final var outPut = useCase.execute(createInput);

        // then
        Assertions.assertNotNull(outPut.id());
        Assertions.assertEquals(expectedCNPJ, outPut.cnpj());
        Assertions.assertEquals(expectedEmail, outPut.email());
        Assertions.assertEquals(expectedName, outPut.name());
    }

    @Test
    @DisplayName("Não deve cadastrar um parceiro com CNPJ duplicado")
    public void testCreateWithDuplicatedCNPJShouldFail() throws Exception {

        // given
        final var expectedCNPJ = "73.079.912/1727-06";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";
        final var expectedError = "Partner already exists";

        final var aPartner = Partner.newPartner(expectedName, "73.079.912/1721-06", expectedEmail);
        final var partnerRepository = new InMemoryPartnerRepository();
        partnerRepository.create(aPartner);

        final var createInput = new CreatePartnerUseCase.Input(expectedCNPJ, expectedEmail, expectedName);

        // when
        final var useCase = new CreatePartnerUseCase(partnerRepository);
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(createInput));

        // then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve cadastrar um parceiro com e-mail duplicado")
    public void testCreateWithDuplicatedEmailShouldFail() throws Exception {

        // given
        final var expectedCNPJ = "73.079.912/1727-06";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";
        final var expectedError = "Partner already exists";

        final var aPartner = Partner.newPartner(expectedName, "73.079.912/1722-06", expectedEmail);
        final var partnerRepository = new InMemoryPartnerRepository();
        partnerRepository.create(aPartner);

        final var createInput = new CreatePartnerUseCase.Input(expectedCNPJ, expectedEmail, expectedName);

        // when
        final var useCase = new CreatePartnerUseCase(partnerRepository);
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(createInput));

        // then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }
}
