import java.util.function.Consumer;
import java.util.ArrayList;
import java.util.Random;

public class ArvoreAvl<T extends Comparable<T>> {
    class Elemento {
        Elemento pai;
        Elemento esquerda;
        Elemento direita;
        T valor;

        int contar = 0;
    
        public Elemento(T valor) {
            this.valor = valor;
        }
    }
  
    private Elemento raiz;

    public int contar;
    public int piorCaso;
    public int medioCaso;
    

    public boolean isVazia() {
        return raiz == null;
    }

    public Elemento adicionar(T valor) {
        Elemento e = new Elemento(valor);
        Elemento pai = this.raiz;
        contar++;

        while (pai != null) {

            contar++;

            if (valor.compareTo(pai.valor) < 0) {
                if (pai.esquerda == null) {
                    e.pai = pai;
                    pai.esquerda = e;
                    balanceamento(pai);
                    
                    return e;
                } else {
                    pai = pai.esquerda;
                }
            } else {
                if (pai.direita == null) {
                    e.pai = pai;
                    pai.direita = e;
                    balanceamento(pai);

                    return e;
                } else {
                    pai = pai.direita;
                }
            }
        }

        this.raiz = e;
        return e;
    }

    public void balanceamento(Elemento elemento) {
        while (elemento != null) {
            int fator = fb(elemento);

            if (fator > 1) {
                if (fb(elemento.esquerda) > 0) {
                    rsd(elemento);
                } else {
                    rdd(elemento);
                }
            } else if (fator < -1) {
                if (fb(elemento.direita) < 0) {
                    rse(elemento);
                } else {
                    rde(elemento);
                }
            }

            elemento = elemento.pai;
        }
    }
    
    public Elemento adicionar(Elemento pai, T valor) {
        Elemento e = new Elemento(valor);
        
        e.pai = pai;
         
        if (pai == null) {
            raiz = e;
        }
        
        return e;
    }

    public void remover(Elemento e) {
        // if (e.esquerda != null)
        //     remover(e.esquerda);  
        
        // if (e.direita != null)
        //     remover(e.direita);
        
        // if (e.pai == null) {
        //     raiz = null;
        // } else {
        //     if (e.pai.esquerda == e) {
        //         e.pai.esquerda = null;
        //     } else {
        //         e.pai.direita = null;
        //     }
        // }
    }

    public void percorrer(Elemento e, Consumer<T> callback) {
        // if (e != null) {
        //     if (e.valor == (Integer) 5) {
        //         System.out.println("");
        //     }
            
        //     callback.accept(e.valor);
        //     percorrer(e.esquerda, callback);
        //     percorrer(e.direita, callback);
        // }
    }

    public Elemento pesquisar(Elemento e, T valor) {
        while (e != null) {
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

        while (e.pai != null) { //Enquanto não alcançamos a raiz
            contador++;
            e = e.pai;
        }

        return contador;
    }

    private int altura(Elemento e){
        int esquerda = 0,direita = 0;
    
        if (e.esquerda != null) {
            esquerda = altura(e.esquerda) + 1;
        }
    
        if (e.direita != null) {
            direita = altura(e.direita) + 1;
        }
      
        return esquerda > direita ? esquerda : direita;
    }

    private int fb(Elemento e) {
        contar++;
        int esquerda = 0,direita = 0;
      
        if (e.esquerda != null) {
            esquerda = altura(e.esquerda) + 1;
        }
    
        if (e.direita != null) {
            direita = altura(e.direita) + 1;
        }
      
        return esquerda - direita;
    }
    
    private Elemento rse(Elemento e) {
        contar++;
        Elemento pai = e.pai;
        Elemento direita = e.direita;

        if (direita.esquerda != null) {
            direita.esquerda.pai = e;
        }
    
        e.direita = direita.esquerda;
        e.pai = direita;
    
        direita.esquerda = e;
        direita.pai = pai;

        if (direita.pai == null) {
            this.raiz = direita;
        } else {
             if (pai.esquerda == e) {
                pai.esquerda = direita;
            } else {
                pai.direita = direita;
            }
        }
      
        return direita;
    }

    private Elemento rsd(Elemento e) {
        contar++;
        Elemento pai = e.pai;
        Elemento esquerda = e.esquerda;

        if (esquerda.direita != null) {
            esquerda.direita.pai = e;
        }
      
        e.esquerda = esquerda.direita;
        e.pai = esquerda;
      
        esquerda.direita = e;
        esquerda.pai = pai;

        if (esquerda.pai == null) {
            this.raiz = esquerda;
        } else {
            if (pai.esquerda == e) {
                pai.esquerda = esquerda;
            } else {
                pai.direita = esquerda;
            }
        }
      
        return esquerda;
    }
    
    private Elemento rde(Elemento e) {
        e.direita = rsd(e.direita);
        return rse(e);
    }
    
    private Elemento rdd(Elemento e) {
        e.esquerda = rse(e.esquerda);
        return rsd(e);
    }

    public static void main(String[] args) {
        ArvoreAvl<Integer> a = new ArvoreAvl<>();
        int[] DadosPiorCaso = new int[1000];
        for (int i = 0; i < 1000; i++) {
            a.adicionar(i);
            DadosPiorCaso[i] = a.contar;
        }

        int[][] Dados = new int[1000][10];
        for(int i = 0; i < 10 ; i++){
            a.contar = 0;
            a = new ArvoreAvl<>();
            ArrayList<Integer> casoMedio = new ArrayList<Integer>();

            for(int j = 0; j < 1000 ; j++){
                Random random = new Random();
                int num = random.nextInt(1000);
                
                if(casoMedio.contains(num)){
                    j = j - 1 ;
                }else{
                    a.adicionar(num);
                    casoMedio.add(num);
                    Dados[j][i] = a.contar;
                }
            }
        }

        for (int l = 0; l < DadosPiorCaso.length; l++){
            System.out.println("Elementos Ordenados " + (l+1)+ ": " + DadosPiorCaso[l]);
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