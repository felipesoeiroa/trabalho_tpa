package contato;

import java.util.Comparator;

/**
 * Compara contatos apenas pelo nome. É usado na lista que serve para
 * pesquisar/ordenar contatos por nome.
 */
public class ComparadorContatoPorNome implements Comparator<Contato> {

    @Override
    public int compare(Contato c1, Contato c2) {
        return c1.getNome().compareToIgnoreCase(c2.getNome());
    }
}
