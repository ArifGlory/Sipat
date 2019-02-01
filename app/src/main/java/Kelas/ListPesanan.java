package Kelas;

public class ListPesanan {
    public String namaPaket;
    public String jenisPaket;
    public String namaPengguna;
    public String status;
    public String foto;
    public String key;

    public ListPesanan(){

    }

    public ListPesanan( String namaPaket, String jenisPaket, String namaPengguna, String status, String foto
                ,String key) {

        this.namaPaket = namaPaket;
        this.jenisPaket = jenisPaket;
        this.namaPengguna = namaPengguna;
        this.status = status;
        this.foto = foto;
        this.key = key;
    }



    public String getNamaPaket() {
        return namaPaket;
    }

    public void setNamaPaket(String namaPaket) {
        this.namaPaket = namaPaket;
    }

    public String getJenisPaket() {
        return jenisPaket;
    }

    public void setJenisPaket(String jenisPaket) {
        this.jenisPaket = jenisPaket;
    }

    public String getNamaPengguna() {
        return namaPengguna;
    }

    public void setNamaPengguna(String namaPengguna) {
        this.namaPengguna = namaPengguna;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
