package Model;

public class Candidate_list {
    private String id;
    private String name;
    private String details;
    private String picture;
    private String elec_symbol;
    private String pname;

    public Candidate_list(String id, String name, String details, String picture, String elec_symbol, String pname) {
        this.id = id;
        this.name = name;
        this.details = details;
        this.picture = picture;
        this.elec_symbol = elec_symbol;
        this.pname = pname;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public String getPicture() {
        return picture;
    }

    public String getElec_symbol() {
        return elec_symbol;
    }

    public String getPname() {
        return pname;
    }
}


