package app.random.generator.fromscratch_v01.Modelo;

/**
 * Created by anali on 05/09/17.
 */

public class MyCharacters {

    private Integer idCharacterUser;
    private String name;
    private String description;
    private String description_gender;

    public MyCharacters(Integer idCharacterUser, String name, String description, String description_gender) {
        this.idCharacterUser = idCharacterUser;
        this.name = name;
        this.description = description;
        this.description_gender = description_gender;
    }

    public Integer getIdCharacterUser() {
        return idCharacterUser;
    }

    public void setIdCharacterUser(Integer idCharacterUser) {
        this.idCharacterUser = idCharacterUser;
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

    public String getDescription_gender() {
        return description_gender;
    }

    public void setDescription_gender(String description_gender) {
        this.description_gender = description_gender;
    }

    public boolean compararCon(MyCharacters myCharacters) {
        return this.name.compareTo(myCharacters.name) == 0 &&
                this.description.compareTo(myCharacters.description) == 0 &&
                this.description_gender.compareTo(myCharacters.description_gender) == 0;
    }
}
