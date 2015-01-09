

package javafxgame;

import java.io.Serializable;

/**
 * Przechowuje wynik gracza.
 * @author adwin_
 */
public class Wynik implements Comparable<Wynik>,  Serializable{
    
    static final long serialVersionUID = 1L;
    static private int licznik=0;
    transient  private int idWyniku;//powinno byc static chyba
    private int liczbaPokonanychZloczyncow;
    private double czasRozgrywki;
    private String imieGracza;
    /**
     * Tworzy wynik
     */
    public Wynik() {
    }
    /**
     * Tworzy wynik
     * @param imieGracza
     * @param liczbaPokonanychZloczyncow
     * @param czasRozgrywki 
     */
    public Wynik(String imieGracza,int liczbaPokonanychZloczyncow,double czasRozgrywki){
        this.idWyniku=licznik++;
        this.liczbaPokonanychZloczyncow=liczbaPokonanychZloczyncow;
        this.imieGracza=imieGracza;
        this.czasRozgrywki=czasRozgrywki;
    }
    /**
     * Zwraca dane gracza jako String
     * @return 
     */
    public String toString(){
       return imieGracza+", "+"time: "+czasRozgrywki;
    }
    /**
     * Przyrownuje
     * @param o
     * @return 
     */
    @Override
    public int compareTo(Wynik o) {
        if(getCzasRozgrywki()>o.getCzasRozgrywki()) return -1;
        else if(czasRozgrywki<o.getCzasRozgrywki()) return 1;
        else return 0;//rowne
    }

    /**
     * @return the liczbaPokonanychZloczyncow
     */
    public int getLiczbaPokonanychZloczyncow() {
        return liczbaPokonanychZloczyncow;
    }

    /**
     * @param liczbaPokonanychZloczyncow the liczbaPokonanychZloczyncow to set
     */
    public void setLiczbaPokonanychZloczyncow(int liczbaPokonanychZloczyncow) {
        this.liczbaPokonanychZloczyncow = liczbaPokonanychZloczyncow;
    }

    /**
     * @return the czasRozgrywki
     */
    public double getCzasRozgrywki() {
        return czasRozgrywki;
    }

    /**
     * @param czasRozgrywki the czasRozgrywki to set
     */
    public void setCzasRozgrywki(double czasRozgrywki) {
        this.czasRozgrywki = czasRozgrywki;
    }

    /**
     * @return the imieGracza
     */
    public String getImieGracza() {
        return imieGracza;
    }

    /**
     * @param imieGracza the imieGracza to set
     */
    public void setImieGracza(String imieGracza) {
        this.imieGracza = imieGracza;
    }

}
