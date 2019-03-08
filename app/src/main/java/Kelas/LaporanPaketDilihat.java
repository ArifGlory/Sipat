package Kelas;

public class LaporanPaketDilihat {
    public String nama;
    public String foto;
    public String paketDilihat;
    public String paketDipesan;
    public String key;


    public LaporanPaketDilihat(String nama, String foto, String paketDilihat, String paketDipesan,String key) {
        this.nama = nama;
        this.foto = foto;
        this.paketDilihat = paketDilihat;
        this.paketDipesan = paketDipesan;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getPaketDilihat() {
        return paketDilihat;
    }

    public void setPaketDilihat(String paketDilihat) {
        this.paketDilihat = paketDilihat;
    }

    public String getPaketDipesan() {
        return paketDipesan;
    }

    public void setPaketDipesan(String paketDipesan) {
        this.paketDipesan = paketDipesan;
    }
}
