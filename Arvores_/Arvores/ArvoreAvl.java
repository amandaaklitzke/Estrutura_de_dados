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

    public boolean isVazia() {
        return raiz == null;
    }

    public Elemento adicionar(T valor) {
        Elemento e = new Elemento(valor);
        Elemento pai = this.raiz;
        while (pai != null) {
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
                //Arvore mais profunda para esquerda, rotação para a direita
                if (fb(elemento.esquerda) > 0) {
                    rsd(elemento);
                } else {
                    rdd(elemento);
                }
            } else if (fator < -1) {
                //Arvore mais profunda para direita, rotação para a esquerda
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

    public void remover(Elemento e) {
        if (e.esquerda != null)
            remover(e.esquerda);  
        
        if (e.direita != null)
            remover(e.direita);
        
        if (e.pai == null) {
            raiz = null;
        } else {
            if (e.pai.esquerda == e) {
                e.pai.esquerda = null;
            } else {
                e.pai.direita = null;
            }
        }
    }

    public int caminho(Elemento e) {
        int contador = 1;

        while (e.pai != null) {
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
        int esquerda = 0,direita = 0;
        contar++;
        if (e.esquerda != null) {
            esquerda = altura(e.esquerda) + 1;
        }
    
        if (e.direita != null) {
            direita = altura(e.direita) + 1;
        }
      
        return esquerda - direita;
    }
    
    private Elemento rse(Elemento e) {
        Elemento pai = e.pai;
        Elemento direita = e.direita;
        contar++;
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
        Elemento pai = e.pai;
        Elemento esquerda = e.esquerda;
        contar++;
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
            a = new ArvoreAvl<>();
            ArrayList<Integer> casoMedio = new ArrayList<Integer>();
            
            for(int j = 0; j < 10 ; j++){
                Random random = new Random();
                int num = random.nextInt(1000);
                
                if(casoMedio.contains(num)){
                    j = j - 1 ;
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