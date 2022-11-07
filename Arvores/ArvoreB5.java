import java.util.Vector;
import java.util.function.Consumer;
import java.util.ArrayList;
import java.util.Random;

public class ArvoreB5<T extends Comparable<T>> {
    class Elemento {
        Elemento pai;
        Vector<Elemento> filhos;
        Vector<T> chaves;

        public Elemento() {
            filhos = new Vector<>();
            chaves = new Vector<>();
        }
    }

    private Elemento raiz;
    private int ordem;

    public int contar;
    public int piorCaso;
    public int medioCaso;

    public ArvoreB5(int ordem) {
        this.ordem = ordem;
        raiz = new Elemento();
    }

    public void percorre(Elemento e, Consumer<T> callback) {
        if (e != null) {
            int total = e.chaves.size();

            for (int i = 0; i < total; i++) {
                percorre(e.filhos.get(i), callback);
                callback.accept(e.chaves.get(i));
            }

            percorre(e.filhos.get(total), callback);
        }
    }

    private int pesquisaBinaria(Elemento e, T chave) {
        int inicio = 0, fim = e.chaves.size() - 1, meio;

        while (inicio <= fim) {
            contar++;
            meio = (inicio + fim) / 2;

            if (e.chaves.get(meio).compareTo(chave) == 0) {
                return meio;
            } else if (e.chaves.get(meio).compareTo(chave) > 0) {
                fim = meio - 1;
            } else {
                inicio = meio + 1;
            }
        }

        return inicio;
    }

    private Elemento localizaNo(T chave) {
        Elemento e = raiz;

        while (e != null) {
            contar++;
            int i = pesquisaBinaria(e, chave);
            Elemento filho = i < e.filhos.size() ? e.filhos.get(i) : null;

            if (filho != null)
                e = filho;
            else
                return e;
        }
        return null;
    }

    private void adicionaChaveNo(Elemento e, Elemento novo, T chave) {
        contar++;
        int i = pesquisaBinaria(e, chave);

        e.chaves.insertElementAt(chave, i);

        if (e.filhos.size() == 0)
            e.filhos.insertElementAt(null, i);

        e.filhos.insertElementAt(novo, i + 1);
    }

    private boolean transbordo(Elemento e) {
        contar++;
        return e.chaves.size() > ordem * 2;
    }

    private Elemento divideNo(Elemento e) {
        int meio = e.chaves.size() / 2;
        Elemento novo = new Elemento();
        novo.pai = e.pai;
        contar++;

        for (int i = meio + 1; i <= e.chaves.size(); i++) {
            if (i < e.chaves.size()) {
                T v = e.chaves.get(i);
                novo.chaves.add(v);
            }
            Elemento filho = e.filhos.get(i);
            novo.filhos.add(filho);
            if (filho != null)
                filho.pai = novo;
        }

        e.chaves.subList(meio, e.chaves.size()).clear();
        e.filhos.subList(meio + 1, e.filhos.size()).clear();
        return novo;
    }

    private void adicionaChave(T chave) {
        Elemento e = localizaNo(chave);

        adicionaChaveRecursivo(e, null, chave);
    }

    void adicionaChaveRecursivo(Elemento e, Elemento novo, T chave) {
        adicionaChaveNo(e, novo, chave);

        if (transbordo(e)) {
            T promovido = e.chaves.get(ordem);
            novo = divideNo(e);

            if (e.pai == null) {
                Elemento pai = new Elemento();
                pai.filhos.add(e);
                adicionaChaveNo(pai, novo, promovido);
                e.pai = pai;
                novo.pai = pai;
                raiz = pai;
            } else
                adicionaChaveRecursivo(e.pai, novo, promovido);
        }
    }

    public static void main(String[] args) {
        ArvoreB5<Integer> a = new ArvoreB5<>(5);
        int[] DadosPiorCaso = new int[1000];
        for (int i = 0; i < 1000; i++) {
            a.adicionaChave(i);
            DadosPiorCaso[i] = a.contar;
        }

        int[][] Dados = new int[1000][10];
        for(int i = 0; i < 10 ; i++){
            a.contar = 0;
            a = new ArvoreB5<>(5);
            ArrayList<Integer> casoMedio = new ArrayList<Integer>();

            for(int j = 0; j < 1000 ; j++){
                Random random = new Random();
                int num = random.nextInt(1000);
                
                if(casoMedio.contains(num)){
                    j = j - 1 ;
                }else{
                    a.adicionaChave(num);
                    casoMedio.add(num);
                    Dados[j][i] = a.contar;
                }
            }
        }

        for (int l = 0; l < DadosPiorCaso.length; l++){
            System.out.println(DadosPiorCaso[l]);
        }
        
        for (int l = 0; l < Dados.length; l++){
            System.out.print("Elementos Aleatorios " + (l+1) + ": " );
            for (int c = 0; c < Dados[0].length; c++){ 
                System.out.print(Dados[l][c] + " ");
            }
            System.out.println(" ");
        }
    }
}
