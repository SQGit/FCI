package net.fciapp.fciscanner;

/**
 * Created by SMK on 8/6/2016.
 */
public class __StaffData {

    public String vin_no;
    public String vin_make;
    public int vin_start;
    public int vin_end;
    public int vin_mva;
    public String mva_barcode;

    public String getVin_no() {
        return vin_no;
    }

    public void setVin_no(String vin_no) {
        this.vin_no = vin_no;
    }

    public String getVin_make() {
        return vin_make;
    }

    public void setVin_make(String vin_make) {
        this.vin_make = vin_make;
    }

    public int getVin_start() {
        return vin_start;
    }

    public void setVin_start(int vin_start) {
        this.vin_start = vin_start;
    }

    public int getVin_end() {
        return vin_end;
    }

    public void setVin_end(int vin_end) {
        this.vin_end = vin_end;
    }

    public int getVin_mva() {
        return vin_mva;
    }

    public void setVin_mva(int vin_mva) {
        this.vin_mva = vin_mva;
    }

    public void setVin_mva_barcode(String vin_mva_barcode) {
        this.mva_barcode = vin_mva_barcode;
    }

    public String getMva_barcode() {
        return mva_barcode;
    }
}
