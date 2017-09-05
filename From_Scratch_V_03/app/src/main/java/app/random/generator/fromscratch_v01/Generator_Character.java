package app.random.generator.fromscratch_v01;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Generator_Character.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Generator_Character#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Generator_Character extends Fragment implements AdapterView.OnItemSelectedListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /*SPINNERS*/
    private Spinner sp_gender;
    private Spinner sp_genre;
    private Spinner sp_race;

    private HashMap<String, String> genders;
    private HashMap<String, String> genres;
    private HashMap<String, String> races;

    private String gl_race_id, gl_gender_id;

    private String data_name = "";
    private String data_id = "";

    private Button btn_generate;
    private TextView generate_name;

    SharedPreferences sharedPreferences;
    String channel;

    private OnFragmentInteractionListener mListener;

    public Generator_Character() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Generator_Character.
     */
    // TODO: Rename and change types and number of parameters
    public static Generator_Character newInstance(String param1, String param2) {
        Generator_Character fragment = new Generator_Character();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_generator_character, container, false);

        //Shared Preferences

        sharedPreferences = this.getActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
        channel = (sharedPreferences.getString("id", ""));

        sp_race = (Spinner) v.findViewById(R.id.race);


        //SPINNER GENDERS
        sp_gender = (Spinner) v.findViewById(R.id.gender);

        VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue
                (new JsonObjectRequest(Request.Method.POST, Constantes.GET_GENDERS, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        obtenerdataGenders(response);
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //Mensaje de Respuesta
                                Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                ));


        //SPINNER GENRES

        sp_genre = (Spinner) v.findViewById(R.id.genre);

       VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue
                (new JsonObjectRequest(Request.Method.POST, Constantes.GET_SPINNER_GENRES, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        obtenerDataGenres(response);
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //Mensaje de Respuesta
                                Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                ));


        //BUTTON GENERATE

        btn_generate = (Button) v.findViewById(R.id.generate);
        generate_name = (TextView) v.findViewById(R.id.generator_name);

        btn_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Actualizar datos en el servidor
                HashMap<String, String> data = new HashMap<>();
                data.put("race_id",  String.valueOf(gl_race_id));
                data.put("gender_id",  String.valueOf(gl_gender_id));

                JSONObject jsonObject = new JSONObject (data);

                System.out.print(jsonObject.toString());

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.GET_CHARACTER,
                                jsonObject,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        generarPersonaje(response);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8" + getParamsEncoding();
                            }
                        }
                );
            }
        });


        /* CAMBIOS DE TIPOGRAFIA */
       // Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/KGAlwaysAGoodTime.ttf");
        //generate_name.setTypeface(font);


        //BUTTON SAVE

        final Button save_character = (Button) v.findViewById(R.id.save);

        save_character.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Actualizar datos en el servidor

                HashMap <String, String> data = new LinkedHashMap<>();
                data.put("character_id",  String.valueOf(data_id));
                data.put("user_id", channel);

                //data.put("user_id",  String.valueOf(gl_gender_id));

                JSONObject jsonObject = new JSONObject (data);

                System.out.print(jsonObject.toString());

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERT_CHARACTER_USER,
                                jsonObject,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        //generarPersonaje(response);
                                        Toast.makeText(getActivity(), "Character has been saved", Toast.LENGTH_SHORT).show();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8" + getParamsEncoding();
                            }
                        }
                );

            }
        });


        /*ARREGLO DE CHARACTERS

        final Button generate_character = (Button) v.findViewById(R.id.generate);


        final String[] characters = {"Erin Whitewing", "Wyn Marblefire", "Alan Jenker",
                "Trypta", "zTali", "Silver Cathorn", "Nathaniel Fallenwalker" };


        generate_character.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int random = (int) (Math.random()*7);
                character_name.setText(characters[random]);
            }
        });*/


        return v;
    }


    /* GENERAR PERSONAJE AL HACER CLICK */

    public void generarPersonaje(JSONObject response) {
        try {
            //Obtener atributo estado
            String estado = response.getString(Constantes.ESTADO);
            switch (estado) {
                case Constantes.SUCCESS:

                    JSONArray retorno = response.getJSONArray("character");

                    for (int i = 0; i < retorno.length(); i++) {
                        JSONObject jb1 = retorno.getJSONObject(i);

                        String name = jb1.getString("name");
                        String id = jb1.getString("id");

                        data_name = name;
                        data_id = id;

                    }

                    generate_name.setText(data_name);

                    break;
                case Constantes.FAILED:
                    String mensaje = response.getString(Constantes.MENSAJE);
                    Toast.makeText(getActivity().getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /* OBTENER GENDERS EN EL SPINNER */

    public void obtenerdataGenders(JSONObject response) {
        try {
            //Obtener atributo estado
            String estado = response.getString(Constantes.ESTADO);
            switch (estado) {
                case Constantes.SUCCESS:
                    List<String> idList = new ArrayList<>();
                    genders = new HashMap<>();
                    JSONArray retorno = response.getJSONArray("genders");
                    //Iniciar Adaptador
                    for (int i = 0; i < retorno.length(); i++) {
                        JSONObject jb1 = retorno.getJSONObject(i);
                        genders.put(jb1.getString("description_gender"), jb1.getString("id"));

                    }
                    idList.addAll(genders.keySet());
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_spinner_item, idList);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_gender.setAdapter(dataAdapter);
                    sp_gender.setOnItemSelectedListener(this);
                    break;
                case Constantes.FAILED:
                    String mensaje = response.getString(Constantes.MENSAJE);
                    Toast.makeText(getActivity().getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
                    /*guardar.setEnabled(false);*/
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /* OBTENER RACES EN EL SPINNER */

    public void obtenerDataRace(JSONObject response) {
        try {
            //Obtener atributo estado
            String estado = response.getString(Constantes.ESTADO);
            switch (estado) {
                case Constantes.SUCCESS:
                    List<String> idList = new ArrayList<>();
                    races = new HashMap<>();
                    JSONArray retorno = response.getJSONArray("race");
                    //Iniciar Adaptador
                    for (int i = 0; i < retorno.length(); i++) {
                        JSONObject jb1 = retorno.getJSONObject(i);
                        races.put(jb1.getString("description"), jb1.getString("id"));

                    }
                    idList.addAll(races.keySet());
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_spinner_item, idList);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_race.setAdapter(dataAdapter);
                    sp_race.setOnItemSelectedListener(this);
                    break;
                case Constantes.FAILED:
                    String mensaje = response.getString(Constantes.MENSAJE);
                    Toast.makeText(getActivity().getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
                    /*guardar.setEnabled(false);*/
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /* OBTENER GENRES EN EL SPINNER */

    public void obtenerDataGenres(JSONObject response) {
        try {
            //Obtener atributo estado
            String estado = response.getString(Constantes.ESTADO);
            switch (estado) {
                case Constantes.SUCCESS:
                    List<String> idList = new ArrayList<>();
                    genres = new HashMap<>();
                    JSONArray retorno = response.getJSONArray("genres");
                    //Iniciar Adaptador
                    for (int i = 0; i < retorno.length(); i++) {
                        JSONObject jb1 = retorno.getJSONObject(i);
                        genres.put(jb1.getString("description"), jb1.getString("id"));

                    }
                    idList.addAll(genres.keySet());
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_spinner_item, idList);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_genre.setAdapter(dataAdapter);
                    sp_genre.setOnItemSelectedListener(this);
                    break;
                case Constantes.FAILED:
                    String mensaje = response.getString(Constantes.MENSAJE);
                    Toast.makeText(getActivity().getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
                    /*guardar.setEnabled(false);*/
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        Spinner spinner = (Spinner) parent;
        if(spinner.getId() == R.id.genre)
        {
            String genre = (String) ((TextView) view).getText();
            String genre_id = genres.get(genre);

            /*Toast.makeText(getActivity().getApplicationContext(), "Genre " + genre + "Genre_id " + genre_id, Toast.LENGTH_SHORT).show();*/

            // Actualizar datos en el servidor
            HashMap<String, String> data = new HashMap<>();
            data.put("genre_id",  String.valueOf(genre_id));

            JSONObject jsonObject = new JSONObject (data);

            System.out.print(jsonObject.toString());

            VolleySingleton.getInstance(getContext()).addToRequestQueue(
                    new JsonObjectRequest(
                            Request.Method.POST,
                            Constantes.GET_RACE,
                            jsonObject,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    obtenerDataRace(response);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                    ) {
                        @Override
                        public Map<String, String> getHeaders() {
                            Map<String, String> headers = new HashMap<String, String>();
                            headers.put("Content-Type", "application/json; charset=utf-8");
                            headers.put("Accept", "application/json");
                            return headers;
                        }

                        @Override
                        public String getBodyContentType() {
                            return "application/json; charset=utf-8" + getParamsEncoding();
                        }
                    }
            );

        }
        else if(spinner.getId() == R.id.race)
        {
            String race = (String) ((TextView) view).getText();
            String race_id = races.get(race);
            gl_race_id = race_id;
            /*Toast.makeText(getActivity().getApplicationContext(), "Race " + race + "Race_id " + race_id, Toast.LENGTH_SHORT).show();*/

        }
        else if(spinner.getId() == R.id.gender)
        {
            String gender = (String) ((TextView) view).getText();
            String gender_id = genders.get(gender);
            gl_gender_id = gender_id;
            //Toast.makeText(getActivity().getApplicationContext(), "Gender " + gender + "Gender_id " + gender_id, Toast.LENGTH_SHORT).show();

        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
