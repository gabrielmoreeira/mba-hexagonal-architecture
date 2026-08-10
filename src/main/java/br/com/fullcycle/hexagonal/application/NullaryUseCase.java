package br.com.fullcycle.hexagonal.application;

public abstract class NullaryUseCase<OUTPUT> {

    // 1. Cada caso de uso tem um INPUT e OUTPUT próprio. Não retorna a Entidade, Agregado, ou objeto de valor
    // 2. O caso de uso implementa o padrão COMMAND

    public abstract OUTPUT execute();

}
