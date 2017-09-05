package app.random.generator.fromscratch_v01;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.*;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import app.random.generator.fromscratch_v01.Adapters.MyCharacters_Adapter;
import app.random.generator.fromscratch_v01.Adapters.Story_Adapter;
import app.random.generator.fromscratch_v01.Modelo.MyCharacters;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Character_List.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Character_List#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Character_List extends Fragment {

    private static final String TAG = Character_List.class.getSimpleName();
    private MyCharacters_Adapter adapter;
    private RecyclerView listaPersonajes;
    private RecyclerView.LayoutManager lManager;
    private Gson gson = new Gson();

    private ListView list_characters;

    private HashMap<String, String> characters_user;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Character_List() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Character_List.
     */
    // TODO: Rename and change types and number of parameters
    public static Character_List newInstance(String param1, String param2) {
        Character_List fragment = new Character_List();
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
        View v = inflater.inflate(R.layout.fragment_character__list, container, false);


        /* CAMBIOS DE TIPOGRAFIA */
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/KGAlwaysAGoodTime.ttf");

        TextView titulo_char_list = (TextView) v.findViewById(R.id.titulo_char_list);
        titulo_char_list.setTypeface(font);


         /* LISTA PERSONAJES */
        listaPersonajes = (RecyclerView) v.findViewById(R.id.reciclador_personajes);
        listaPersonajes.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(getActivity());
        listaPersonajes.setLayoutManager(lManager);

        // Cargar datos en el adaptador
        cargarAdaptador();

        //Decoracion Recycler

        //listaPersonajes.addItemDecoration(new android.support.v7.widget.DividerItemDecoration(getActivity(), android.support.v7.widget.DividerItemDecoration.VERTICAL));

        return v;
    }


    public void cargarAdaptador() {
        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("user_id", "1");

        JSONObject jobject = new JSONObject(map);

        Log.d(TAG, jobject.toString());

        VolleySingleton.getInstance(getContext()).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        Constantes.GET_CHARACTER_USER,
                        jobject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesar la respuesta del servidor
                                //Toast.makeText(MainActivity.this, "Success sign in", Toast.LENGTH_LONG).show();
                                procesarRespuesta(response);
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

    private void procesarRespuesta(JSONObject response) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");

            switch (estado) {
                case "1": // EXITO
                    // Obtener array "metas" Json
                    JSONArray mensaje = response.getJSONArray("character_user");
                    // Parsear con Gson
                    MyCharacters[] characters = gson.fromJson(mensaje.toString(), MyCharacters[].class);
                    // Inicializar adaptador
                    adapter = new MyCharacters_Adapter(Arrays.asList(characters), getActivity());
                    // Setear adaptador a la lista
                    listaPersonajes.setAdapter(adapter);
                    break;
                case "2": // FALLIDO
                    String mensaje2 = response.getString("mensaje");
                    Toast.makeText(
                            getActivity(),
                            mensaje2,
                            Toast.LENGTH_LONG).show();
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /*public void obtenerDataCharactersUser(JSONObject response) {
        try {
            //Obtener atributo estado
            String estado = response.getString(Constantes.ESTADO);
            switch (estado) {
                case Constantes.SUCCESS:
                    List<String> idList = new ArrayList<>();
                    characters_user = new HashMap<>();
                    JSONArray retorno = response.getJSONArray("genders");
                    //Iniciar Adaptador
                    for (int i = 0; i < retorno.length(); i++) {
                        JSONObject jb1 = retorno.getJSONObject(i);
                        characters_user.put(jb1.getString("description_gender"), jb1.getString("id"));

                    }
                    idList.addAll(characters_user.keySet());
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_list_item_1, android.R.id.text1, idList);
                    //dataAdapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
                    list_characters.setAdapter(dataAdapter);
                    break;
                case Constantes.FAILED:
                    String mensaje = response.getString(Constantes.MENSAJE);
                    Toast.makeText(getActivity().getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onActivityCreated (@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

       /* final ListView listViewCharacters = (ListView) getActivity().findViewById(R.id.list_characters);
        Resources res = getResources();
        String [] characters = res.getStringArray(R.array.characters);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, characters);
        listViewCharacters.setAdapter(adapter);

        listViewCharacters.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView temp = (TextView) view;
                String Value = listViewCharacters.getItemAtPosition(i).toString();

                Toast.makeText(getActivity(), Value, Toast.LENGTH_SHORT).show();
            }
        });*/

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
