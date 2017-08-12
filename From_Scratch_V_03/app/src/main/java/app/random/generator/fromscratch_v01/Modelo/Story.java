package app.random.generator.fromscratch_v01.Modelo;

/**
 * Created by anali on 12/08/17.
 */

public class Story {

    private Integer idStory;
    private String name;
    private String synopsis;

    public Story(Integer idStory, String name, String synopsis) {
        this.idStory = idStory;
        this.name = name;
        this.synopsis = synopsis;
    }

    public Integer getIdStory() {
        return idStory;
    }

    public void setIdStory(Integer idStory) {
        this.idStory = idStory;
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

    public boolean compararCon(Story story) {
        return this.name.compareTo(story.name) == 0 &&
                this.synopsis.compareTo(story.synopsis) == 0;
    }
}
