import java.util.function.Consumer;
import java.util.ArrayList;
import java.util.Random;

public class ArvoreRubroNegra<T extends Comparable<T>> {
    enum Cor {
        Vermelho,
        Preto
    }
    private int contar;
    private int piorCaso;
    public int getPiorCaso() {
        return piorCaso;
    }
    public void setPiorCaso(int piorCaso) {
        this.piorCaso = piorCaso;
    }

    private int medioCaso;
    public int getMedioCaso() {
        return medioCaso;
    }
    public void setMedioCaso(int medioCaso) {
        this.medioCaso = medioCaso;
    }

    class Elemento {
        Elemento pai;
        Elemento esquerda;
        Elemento direita;
        Cor cor;
        T valor;
    
        public Elemento(T valor) {
            this.valor = valor;
        }
    }

    public ArvoreRubroNegra() {
        nulo = new Elemento(null);
        nulo.cor = Cor.Preto;
        raiz = nulo;
    }
    
    private Elemento raiz;
    private Elemento nulo;

    public boolean isVazia() {
        return raiz == nulo;
    }

    public Elemento adicionar(T valor) {
        Elemento e = new Elemento(valor);
        e.cor = Cor.Vermelho;
        e.esquerda = nulo;
        e.direita = nulo;
        e.pai = nulo;
        
        Elemento pai = this.raiz;

        while (pai != nulo) {
            if (valor.compareTo(pai.valor) < 0) {
                if (pai.esquerda == nulo) {
                    e.pai = pai;
                    pai.esquerda = e;
                    balanceamento(e);
                    return e;
                } else {
                    pai = pai.esquerda;
                }
            } else {
                if (pai.direita == nulo) {
                    e.pai = pai;
                    pai.direita = e;
                    balanceamento(e);

                    return e;
                } else {
                    pai = pai.direita;
                }
            }
        }

        this.raiz = e;
        balanceamento(e);
        return e;
    }

    public void balanceamento(Elemento e) {
        while (e.pai.cor == Cor.Vermelho) { 
            Elemento pai = e.pai;
            Elemento avo = pai.pai;

            if (pai == avo.esquerda) { 
                Elemento tio = avo.direita;
                        
                if (tio.cor == Cor.Vermelho) {
                    contar++;
                    tio.cor = Cor.Preto;
                    pai.cor = Cor.Preto; 
                    avo.cor = Cor.Vermelho;
                    e = avo;
                } else {
                    if (e == pai.direita) {
                        e = pai;
                        rse(e);
                    } else {
                        contar++;
                        pai.cor = Cor.Preto;
                        avo.cor = Cor.Vermelho;
                        rsd(avo);
                    }
                }
            } else {
                Elemento tio = avo.esquerda;
                        
                if (tio.cor == Cor.Vermelho) {
                    contar++;
                    tio.cor = Cor.Preto;
                    pai.cor = Cor.Preto; 
                    avo.cor = Cor.Vermelho;
                    e = avo;
                } else {
                    if (e == pai.esquerda) {
                        e = pai;
                        rsd(e);
                    } else {
                        contar++;
                        pai.cor = Cor.Preto;
                        avo.cor = Cor.Vermelho;
                        rse(avo);
                    }
                }
            }
        }
        
        raiz.cor = Cor.Preto;
    }

    private void rse(Elemento e) {
        contar++;
        Elemento direita = e.direita;
        e.direita = direita.esquerda; 
          
        if (direita.esquerda != nulo) {
          direita.esquerda.pai = e;
        }
          
        direita.pai = e.pai;
              
        if (e.pai == nulo) {
            raiz = direita;
        } else if (e == e.pai.esquerda) {
            e.pai.esquerda = direita;
        } else {
            e.pai.direita = direita;
        }
          
        direita.esquerda = e;
        e.pai = direita;
    }

    private void rsd(Elemento e) {
        contar++;
        Elemento esquerda = e.esquerda;
        e.esquerda = esquerda.direita;
              
        if (esquerda.direita != nulo) {
            esquerda.direita.pai = e;
        }
              
        esquerda.pai = e.pai;
              
        if (e.pai == nulo) {
            raiz = esquerda;
        } else if (e == e.pai.esquerda) {
            e.pai.esquerda = esquerda;
        } else {
            e.pai.direita = esquerda;
        }
              
        esquerda.direita = e;
        e.pai = esquerda;
    }
    
    public void percorrer(Elemento e, Consumer<T> callback) {
        if (e != nulo) {
            percorrer(e.esquerda, callback);
            callback.accept(e.valor);
            percorrer(e.direita, callback);
        }
    }

    public Elemento pesquisar(Elemento e, T valor) {
        while (e != nulo) {
            if (e.valor.equals(valor)) {
                return e;
            } else if (valor.compareTo(e.valor) > 0) {
                e = e.direita;
            } else {
                e = e.esquerda;
            }
        }
        
        return null;
    }

    public int caminho(Elemento e) {
        int contador = 1;

        while (e.pai != nulo) { //Enquanto não alcançamos a raiz
            contador++;
            e = e.pai;
        }

        return contador;
    }

    public void percorrerInOrder(Elemento e, Consumer<T> callback) {
        if (e != nulo) {
            percorrerInOrder(e.esquerda, callback);
            callback.accept(e.valor);
            percorrerInOrder(e.direita, callback);
        }
    }

    public void percorrerPosOrder(Elemento e, Consumer<T> callback) {
        if (e != nulo) {
            percorrerPosOrder(e.esquerda, callback);
            percorrerPosOrder(e.direita, callback);
            callback.accept(e.valor);
        }
    }

    public void percorrer(Consumer<T> callback) {
        this.percorrer(raiz, callback);
    }

    public void percorrerInOrder(Consumer<T> callback) {
        this.percorrerInOrder(raiz, callback);
    }

    public void percorrerPosOrder(Consumer<T> callback) {
        this.percorrerPosOrder(raiz, callback);
    }

    public void percorrerLargura(Consumer<T> callback) {
        Fila<ArvoreRubroNegra<T>.Elemento> fila = new Fila<>();

        fila.adicionar(raiz);

        while (!fila.isVazia()) {
            ArvoreRubroNegra<T>.Elemento e = fila.remover();
            callback.accept(e.valor); 

            if (e.esquerda != nulo) {
                fila.adicionar(e.esquerda);
            }

            if (e.direita != nulo) {
                fila.adicionar(e.direita);
            }
        }
    }

    public void percorrerProfundidade(Consumer<T> callback) {
        Pilha<ArvoreRubroNegra<T>.Elemento> pilha = new Pilha<>();
        pilha.adicionar(raiz);

        while (!pilha.isVazia()) {
            ArvoreRubroNegra<T>.Elemento e = pilha.remover();

            callback.accept(e.valor); 

            if (e.direita != nulo) {
                pilha.adicionar(e.direita);
            }

            if (e.esquerda != nulo) {
                pilha.adicionar(e.esquerda);
            }
        }
    }

    public static void main(String args[]) {
        ArvoreRubroNegra<Integer> a = new ArvoreRubroNegra<>();
        
        
        
        a.adicionar(1);
        a.adicionar(2);
        a.adicionar(3);
        a.adicionar(4);
        a.adicionar(5);
        a.adicionar(6);
        a.adicionar(7);
        a.adicionar(8);
        a.adicionar(9);
        a.adicionar(10);

        a.piorCaso = a.contar;
        System.out.println("Pior caso: " + a.piorCaso);

        for(int i = 0; i < 10 ; i++){
            a.contar = 0;
            a = new ArvoreRubroNegra<>();
            ArrayList<Integer> casoMedio = new ArrayList<Integer>();
            
            for(int j = 0; j < 10 ; j++){
                Random random = new Random();
                int num = random.nextInt(1000);
                
                if(casoMedio.contains(num)){
                    j = j - 1 ;
                    System.out.println("Entrou aqui" );
                }else{
                    a.adicionar(num);
                    casoMedio.add(num);
                }

            }
            a.medioCaso = a.contar;
            System.out.println("Lista casoMedio: "+ i + casoMedio);
            System.out.println("CasoMedio: " + a.medioCaso);
        }
    }
}