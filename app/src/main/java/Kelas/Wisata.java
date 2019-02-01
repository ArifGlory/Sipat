package Kelas;

import java.io.Serializable;

@SuppressWarnings("serial") //With this annotation we are going to hide compiler warnings
public class Wisata implements Serializable {
    public String namaWisata;
    public String key;
    public String downloadUrl;
    public String statusWisata;
    public String jenisPaket;
    public String keterangan;

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getJenisPaket() {
        return jenisPaket;
    }

    public void setJenisPaket(String jenisPaket) {
        this.jenisPaket = jenisPaket;
    }

    public Wisata(){

    }

    public Wisata(String namaWisata, String key, String downloadUrl, String statusWisata,String jenisPaket,String keterangan) {
        this.namaWisata = namaWisata;
        this.key = key;
        this.downloadUrl = downloadUrl;
        this.statusWisata = statusWisata;
        this.jenisPaket = jenisPaket;
        this.keterangan = keterangan;
    }

    public String getNamaWisata() {

        return namaWisata;
    }

    public void setNamaWisata(String namaWisata) {
        this.namaWisata = namaWisata;
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

    public String getStatusWisata() {
        return statusWisata;
    }

    public void setStatusWisata(String statusWisata) {
        this.statusWisata = statusWisata;
    }
}
