package Kelas;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DaftarPaketKelas implements Serializable {
    public String namaPeserta;
    public String email;
    public String phone;
    public String ttl;
    public String tujuanDestinasi;
    public String asalInstansi;
    public String ibuKandung;

    public DaftarPaketKelas(String namaPeserta, String email, String phone, String ttl, String tujuanDestinasi, String asalInstansi, String ibuKandung) {
        this.namaPeserta = namaPeserta;
        this.email = email;
        this.phone = phone;
        this.ttl = ttl;
        this.tujuanDestinasi = tujuanDestinasi;
        this.asalInstansi = asalInstansi;
        this.ibuKandung = ibuKandung;
    }

    public String getNamaPeserta() {
        return namaPeserta;
    }

    public void setNamaPeserta(String namaPeserta) {
        this.namaPeserta = namaPeserta;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTtl() {
        return ttl;
    }

    public void setTtl(String ttl) {
        this.ttl = ttl;
    }

    public String getTujuanDestinasi() {
        return tujuanDestinasi;
    }

    public void setTujuanDestinasi(String tujuanDestinasi) {
        this.tujuanDestinasi = tujuanDestinasi;
    }

    public String getAsalInstansi() {
        return asalInstansi;
    }

    public void setAsalInstansi(String asalInstansi) {
        this.asalInstansi = asalInstansi;
    }

    public String getIbuKandung() {
        return ibuKandung;
    }

    public void setIbuKandung(String ibuKandung) {
        this.ibuKandung = ibuKandung;
    }
}
