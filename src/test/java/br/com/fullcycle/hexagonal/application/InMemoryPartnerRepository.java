package br.com.fullcycle.hexagonal.application;

import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.application.domain.partner.PartnerId;
import br.com.fullcycle.hexagonal.application.repositories.PartnerRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class InMemoryPartnerRepository implements PartnerRepository {

    private final Map<String, Partner> partner;
    private final Map<String, Partner> partnerByCNPJ;
    private final Map<String, Partner> partnerByEmail;

    public InMemoryPartnerRepository() {
        this.partner = new HashMap<>();
        this.partnerByCNPJ = new HashMap<>();
        this.partnerByEmail = new HashMap<>();
    }

    @Override
    public Optional<Partner> partnerOfId(PartnerId anId) {
        return Optional.ofNullable(this.partner.get(Objects.requireNonNull(anId).value().toString()));
    }

    @Override
    public Optional<Partner> partnerOfCNPJ(String cnpj) {
        return Optional.ofNullable(this.partnerByCNPJ.get(Objects.requireNonNull(cnpj)));
    }

    @Override
    public Optional<Partner> partnerOfEmail(String email) {
        return Optional.ofNullable(this.partnerByEmail.get(Objects.requireNonNull(email)));
    }

    @Override
    public Partner create(Partner partner) {
        this.partner.put(partner.partnerId().value().toString(), partner);
        this.partnerByCNPJ.put(partner.cnpj().value(), partner);
        this.partnerByEmail.put(partner.email().value(), partner);
        return partner;
    }

    @Override
    public Partner update(Partner partner) {
        this.partner.put(partner.partnerId().value().toString(), partner);
        this.partnerByCNPJ.put(partner.cnpj().value(), partner);
        this.partnerByEmail.put(partner.email().value(), partner);
        return partner;
    }

    @Override
    public void deleteAll() {

    }
}
