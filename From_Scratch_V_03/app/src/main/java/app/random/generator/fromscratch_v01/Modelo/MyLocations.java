package app.random.generator.fromscratch_v01.Modelo;

/**
 * Created by Maria Jose Bravo on 06/09/2017.
 */

public class MyLocations {

    private Integer idLocationUser;
    private String name;
    private String description;

    public MyLocations(Integer idLocationUser, String name, String description) {
        this.idLocationUser = idLocationUser;
        this.name = name;
        this.description = description;
    }

    public Integer getIdLocationUser() {
        return idLocationUser;
    }

    public void setIdLocationUser(Integer idLocationUser) {
        this.idLocationUser = idLocationUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean compararCon(MyLocations myLocations) {
        return this.name.compareTo(myLocations.name) == 0 &&
                this.description.compareTo(myLocations.description) == 0;
    }
}
