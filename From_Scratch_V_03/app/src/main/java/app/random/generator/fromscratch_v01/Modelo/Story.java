package app.random.generator.fromscratch_v01.Modelo;

/**
 * Created by anali on 12/08/17.
 */

public class Story {

    private int id;
    private String name;
    private String synopsis;
    private String description;


    public Story(int id, String name, String synopsis, String description) {
        this.id = id;
        this.name = name;
        this.synopsis = synopsis;
        this.description = description;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean compararCon(Story story) {
        return this.name.compareTo(story.name) == 0 &&
                this.synopsis.compareTo(story.synopsis) == 0 &&
                this.description.compareTo(story.description) == 0;
    }
}
