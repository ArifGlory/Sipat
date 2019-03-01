package Kelas;

public class Ulasan {
    public String foto;
    public String namaUser;
    public String isiUlasan;
    public String tanggal;

    public Ulasan(String foto, String namaUser, String isiUlasan, String tanggal) {
        this.foto = foto;
        this.namaUser = namaUser;
        this.isiUlasan = isiUlasan;
        this.tanggal = tanggal;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public void setNamaUser(String namaUser) {
        this.namaUser = namaUser;
    }

    public String getIsiUlasan() {
        return isiUlasan;
    }

    public void setIsiUlasan(String isiUlasan) {
        this.isiUlasan = isiUlasan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}
