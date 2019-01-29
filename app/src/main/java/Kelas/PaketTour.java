package Kelas;

import java.io.Serializable;

@SuppressWarnings("serial") //With this annotation we are going to hide compiler warnings
public class PaketTour implements Serializable {
    public String namaPaket;
    public String hargaPaket;
    public String durasiPaket;
    public String jumlahPeserta;
    public String fasilitasPaket;
    public String key;
    public String downloadUrl;
    public String statusPaket;

    public PaketTour(){

    }

    public PaketTour(String namaPaket, String hargaPaket, String durasiPaket, String jumlahPeserta, String fasilitasPaket, String key, String downloadUrl, String statusPaket) {
        this.namaPaket = namaPaket;
        this.hargaPaket = hargaPaket;
        this.durasiPaket = durasiPaket;
        this.jumlahPeserta = jumlahPeserta;
        this.fasilitasPaket = fasilitasPaket;
        this.key = key;
        this.downloadUrl = downloadUrl;
        this.statusPaket = statusPaket;
    }

    public String getNamaPaket() {
        return namaPaket;
    }

    public void setNamaPaket(String namaPaket) {
        this.namaPaket = namaPaket;
    }

    public String getHargaPaket() {
        return hargaPaket;
    }

    public void setHargaPaket(String hargaPaket) {
        this.hargaPaket = hargaPaket;
    }

    public String getDurasiPaket() {
        return durasiPaket;
    }

    public void setDurasiPaket(String durasiPaket) {
        this.durasiPaket = durasiPaket;
    }

    public String getJumlahPeserta() {
        return jumlahPeserta;
    }

    public void setJumlahPeserta(String jumlahPeserta) {
        this.jumlahPeserta = jumlahPeserta;
    }

    public String getFasilitasPaket() {
        return fasilitasPaket;
    }

    public void setFasilitasPaket(String fasilitasPaket) {
        this.fasilitasPaket = fasilitasPaket;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getStatusPaket() {
        return statusPaket;
    }

    public void setStatusPaket(String statusPaket) {
        this.statusPaket = statusPaket;
    }
}
