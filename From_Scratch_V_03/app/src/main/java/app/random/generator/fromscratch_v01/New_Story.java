package app.random.generator.fromscratch_v01;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
 * {@link New_Story.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link New_Story#newInstance} factory method to
 * create an instance of this fragment.
 */
public class New_Story extends Fragment implements AdapterView.OnItemSelectedListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private Spinner sp_genre;
    private HashMap<String, String> genres;
    private String gl_genre_id;
    private TextView titulo_story;
    private TextView desc_synopsis;

    private Button btn_save;

    SharedPreferences sharedPreferences;
    String channel;


    private OnFragmentInteractionListener mListener;

    public New_Story() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment New_Story.
     */
    // TODO: Rename and change types and number of parameters
    public static New_Story newInstance(String param1, String param2) {
        New_Story fragment = new New_Story();
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
        View v = inflater.inflate(R.layout.fragment_new__story, container, false);

        sharedPreferences = this.getActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
        channel = (sharedPreferences.getString("id", ""));


        /* CAMBIOS DE TIPOGRAFIA */
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/KGAlwaysAGoodTime.ttf");

        titulo_story = (TextView) v.findViewById(R.id.titulo_story);
        titulo_story.setTypeface(font);

        final TextView titulo_synopsis = (TextView) v.findViewById(R.id.titulo_synopsis);
        titulo_synopsis.setTypeface(font);

        desc_synopsis = (TextView) v.findViewById(R.id.desc_synopsis);

        final TextView genre = (TextView) v.findViewById(R.id.genre_title);
        genre.setTypeface(font);

        //SPINNER GENRES

        sp_genre = (Spinner) v.findViewById(R.id.spinner_genres);

        VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue
                (new JsonObjectRequest(Request.Method.POST, Constantes.GET_GENRES, null, new Response.Listener<JSONObject>() {

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


         /* ACCIONES DE BOTONES */
        Button btn_save = (Button) v.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                // Actualizar datos en el servidor

                HashMap <String, String> data = new LinkedHashMap<>();
                data.put("name", titulo_story.getText().toString());
                data.put("synopsis", desc_synopsis.getText().toString());
                data.put("genre_id", String.valueOf(gl_genre_id));
                data.put("user_id", channel);

                JSONObject jsonObject = new JSONObject (data);

                System.out.print(jsonObject.toString());

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERT_STORY,
                                jsonObject,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Toast.makeText(getActivity(), "Story has been saved", Toast.LENGTH_SHORT).show();
                                        insertarHistoria(response);
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

        return v;
    }


    private void insertarHistoria(JSONObject response) {

        try {
            // Obtener estado
            String estado = response.getString("estado");
            // Obtener mensaje
            String mensaje = response.getString("mensaje");

            switch (estado) {
                case "1":
                    // Mostrar mensaje
                   // Toast.makeText(MainActivity.this, "Welcome to From Scratch.", Toast.LENGTH_LONG).show();
                    // Enviar código de éxito
                    //MainActivity.this.setResult(Activity.RESULT_OK);
                    // Terminar actividad
                    //MainActivity.this.finish();

                    titulo_story.setText("");
                    desc_synopsis.setText("");

                    break;

                case "2":
                    // Mostrar mensaje
                    Toast.makeText(getActivity(), "An error has occur. Please try again.", Toast.LENGTH_LONG).show();
                    // Enviar código de falla
                    //MainActivity.this.setResult(Activity.RESULT_CANCELED);
                    // Terminar actividad
                    //MainActivity.this.finish();
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Spinner spinner = (Spinner) parent;

        if(spinner.getId() == R.id.spinner_genres)
        {
            String genre = (String) ((TextView) view).getText();
            String genre_id = genres.get(genre);
            gl_genre_id = genre_id;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
