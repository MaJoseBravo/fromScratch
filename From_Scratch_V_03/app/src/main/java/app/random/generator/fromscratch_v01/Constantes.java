package app.random.generator.fromscratch_v01;

/**
 * Created by anali on 12/08/17.
 */

public class Constantes {

    /**
     * Transición Home -> Detalle
     */
    /*public static final int CODIGO_DETALLE = 100;

    /**
     * Transición Detalle -> Actualización
     */
    /*public static final int CODIGO_ACTUALIZACION = 101;

    /**
     * Puerto que utilizas para la conexión.
     * Dejalo en blanco si no has configurado esta carácteristica.
     */
    /*private static final String PUERTO_HOST = "63343";

    /**
     * Dirección IP de genymotion o AVD
     */
   /* private static final String IP = "http://10.0.3.2:";*/


    public static final String ESTADO = "estado";
    public static final String SUCCESS = "1";
    public static final String FAILED = "2";
    public static final String MENSAJE = "mensaje";


     /* URLs del Web Service
     */

    private  static final String URL = "http://www.fromscratch.mobulancer.com/Servicios/Vista";

    //CONSULTAR DATA

    public static final String GET_STORIES = URL + "/Story/get_stories.php";
    public static final String GET_RACES = URL + "/Race/get_races.php";

    public static final String GET_CHARACTER = URL + "/Character/get_character_by_race_gender.php";
    public static final String GET_CHARACTER_STORY = URL + "/Character_story/get_character_by_story.php";
    public static final String GET_CHARACTER_USER = URL + "/Character_user/get_characters_by_user.php";
    public static final String GET_GENDERS = URL + "/Gender/get_genders.php";
    public static final String GET_GENRES = URL + "/Genre/get_genres.php";
    public static final String GET_SPINNER_GENRES = URL + "/Genre/get_spinner_genres.php";
    public static final String GET_GENRE_STORY = URL + "/Genre_story/get_genre_by_story.php";
    public static final String GET_LOCATION_TYPE = URL + "/Location/get_location_by_type.php";
    public static final String GET_LOCATION_STORY = URL + "/Location_story/get_location_by_story.php";
    public static final String GET_LOCATION_GENRE = URL + "/Location_type/get_location_type_by_genre.php";
    public static final String GET_LOCATION_USER = URL + "/Location_user/get_location_by_user.php";
    public static final String GET_RACE = URL + "/Race/get_race_by_genre.php";
    public static final String GET_STORY = URL + "/Story/get_story_by_id.php";
    public static final String GET_USER_ID = URL + "/User/get_user_by_token.php";
    public static final String GET_USER_STORY = URL + "/User_story/get_story_by_user.php";


    //INSERTAR DATA

    public static final String INSERT_CHARACTER_STORY = URL + "/Character_story/insert_character_story.php";
    public static final String INSERT_CHARACTER_USER = URL + "/Character_user/insert_character_user.php";
    public static final String INSERT_GENRE_STORY = URL + "/Genre_story/insert_genre_story.php";
    public static final String INSERT_LOCATION_STORY = URL + "/Location_story/insert_location_story.php";
    public static final String INSERT_LOCATION_USER = URL + "/Location_user/insert_location_user.php";
    public static final String INSERT_STORY = URL + "/Story/insert_story.php";
    public static final String INSERT_USER = URL + "/User/insert_user.php";
    public static final String INSERT_USER_STORY = URL + "/User_story/insert_user_story.php";


    //ACTUALIZAR DATA

    public static final String UPDATE_STORY = URL + "/Story/update_story.php";


    //ELIMINAR DATA

    public static final String DELETE_CHARACTER_STORY = URL + "/Character_story/delete_character_story.php";
    public static final String DELETE_GENRE_STORY = URL + "/Genre_story/delete_genre_story.php";
    public static final String DELETE_LOCATION_STORY = URL + "/Location_story/delete_location_story.php";
    public static final String DELETE_STORY = URL + "/Story/delete_story.php";
    public static final String DELETE_STORY_BY_USER = URL + "/User_story/delete_by_story.php";


    /**
     * Clave para el valor extra que representa al identificador de una meta
     */
   /* public static final String EXTRA_ID = "IDEXTRA";*/
}
