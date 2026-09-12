package contato;

import java.util.Comparator;

/**
 * Compara contatos apenas pelo telefone. É usado na lista que serve para
 * pesquisar/ordenar contatos por telefone e para checar duplicidade de
 * telefone (regra de negócio do programa, não da biblioteca de lista).
 */
public class ComparadorContatoPorTelefone implements Comparator<Contato> {

    @Override
    public int compare(Contato c1, Contato c2) {
        return c1.getTelefone().compareTo(c2.getTelefone());
    }
}
