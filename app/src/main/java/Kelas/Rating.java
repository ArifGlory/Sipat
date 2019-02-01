package Kelas;

import java.io.Serializable;

@SuppressWarnings("serial") //With this annotation we are going to hide compiler warnings
public class Rating implements Serializable {
    public String rate;
    public String komentar;

    public Rating(){

    }

    public Rating(String rate, String komentar) {
        this.rate = rate;
        this.komentar = komentar;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getKomentar() {
        return komentar;
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
    }
}
