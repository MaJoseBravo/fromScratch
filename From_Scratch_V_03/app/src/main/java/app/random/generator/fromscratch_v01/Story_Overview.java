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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Story_Overview.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Story_Overview#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Story_Overview extends Fragment {

    String idStory;

    private TextView titulo_detalle;
    private TextView synopsis_detalle;
    private TextView genre_detalle;
    private TextView genres_list_overview;

    SharedPreferences sharedPreferences;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Story_Overview() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Story_Overview.
     */
    // TODO: Rename and change types and number of parameters
    public static Story_Overview newInstance(String param1, String param2) {
        Story_Overview fragment = new Story_Overview();

        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            idStory = bundle.getString("id", "");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_story__overview, container, false);


       /* CAMBIOS DE TIPOGRAFIA */
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/KGAlwaysAGoodTime.ttf");

        titulo_detalle = (TextView) v.findViewById(R.id.titulo_story_overview);
        titulo_detalle.setTypeface(font);

        genres_list_overview = (TextView) v.findViewById(R.id.genres_list_overview);
        genres_list_overview.setTypeface(font);

        synopsis_detalle = (TextView) v.findViewById(R.id.synopsis_overview);
        genre_detalle = (TextView) v.findViewById(R.id.genre_description);

        /*CARGA DE DETALLE*/

        //Toast.makeText(this.getContext(), idStory.toString(),Toast.LENGTH_SHORT).show();
        cargarDetalleStory(idStory);

        /*Shared preferences*/

        sharedPreferences = this.getActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("idStory", idStory);
        editor.commit();


         /* ACCIONES DE BOTONES */
        Button btnEdit = (Button) v.findViewById(R.id.edit_story);
        btnEdit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Edit_Story fragment = new Edit_Story();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.Contenedor, fragment, fragment.getTag()).commit();
            }
        });

        Button btnDelete = (Button) v.findViewById(R.id.delete_story);
        btnDelete.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                // Actualizar datos en el servidor

                HashMap <String, String> data = new LinkedHashMap<>();

                data.put("id", idStory);

                JSONObject jsonObject = new JSONObject (data);

                System.out.print(jsonObject.toString());

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.DELETE_STORY,
                                jsonObject,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        //procesarRespuesta(response);
                                        //Toast.makeText(getActivity(), "Story has been update", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(getActivity().getApplicationContext(), "An error has occur. Please try again.", Toast.LENGTH_LONG).show();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        //Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

                                        Notebook fragment = new Notebook();
                                        FragmentManager manager = getFragmentManager();
                                        manager.beginTransaction().replace(R.id.Contenedor, fragment, fragment.getTag()).commit();

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

    private void cargarDetalleStory(String idStory) {

        // Actualizar datos en el servidor

        HashMap <String, String> data = new LinkedHashMap<>();
        data.put("story_id", idStory);

        JSONObject jsonObject = new JSONObject (data);

        System.out.print(jsonObject.toString());

        VolleySingleton.getInstance(getContext()).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        Constantes.GET_STORY,
                        jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                obtenerStorybyId(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getActivity().getApplicationContext(), "An error has occur. Please try again.", Toast.LENGTH_LONG).show();
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

    public void obtenerStorybyId(JSONObject response) {
        try {
            //Obtener atributo estado
            String estado = response.getString(Constantes.ESTADO);
            switch (estado) {
                case Constantes.SUCCESS:

                    JSONArray retorno = response.getJSONArray("story");
                    JSONObject jb1 = retorno.getJSONObject(0);
                    String name = jb1.getString("name");
                    String synopsis = jb1.getString("synopsis");
                    String genre = jb1.getString("description");

                    titulo_detalle.setText(name);
                    synopsis_detalle.setText(synopsis);
                    genre_detalle.setText(genre);
                    genres_list_overview.setText("Genre");

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
