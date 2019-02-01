package Kelas;

import java.io.Serializable;

@SuppressWarnings("serial") //With this annotation we are going to hide compiler warnings
public class Pesanan implements Serializable {
    public String idUser;
    public String idPaket;
    public String tanggal;
    public String keterangan;
    public String key;
    public String status;
    public String namaPaket;
    public String namaPengguna;
    public String foto;

    public String getNamaPaket() {
        return namaPaket;
    }

    public void setNamaPaket(String namaPaket) {
        this.namaPaket = namaPaket;
    }

    public String getNamaPengguna() {
        return namaPengguna;
    }

    public void setNamaPengguna(String namaPengguna) {
        this.namaPengguna = namaPengguna;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJenisPaket() {
        return jenisPaket;
    }

    public void setJenisPaket(String jenisPaket) {
        this.jenisPaket = jenisPaket;
    }

    public String jenisPaket;

    public Pesanan(){

    }

    public Pesanan(String idUser, String idPaket, String tanggal, String keterangan, String key,String jenisPaket,String status
                ,String namaPaket,String namaPengguna, String foto) {
        this.idUser = idUser;
        this.idPaket = idPaket;
        this.tanggal = tanggal;
        this.keterangan = keterangan;
        this.key = key;
        this.jenisPaket = jenisPaket;
        this.status = status;
        this.namaPaket = namaPaket;
        this.namaPengguna = namaPengguna;
        this.foto = foto;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdPaket() {
        return idPaket;
    }

    public void setIdPaket(String idPaket) {
        this.idPaket = idPaket;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
