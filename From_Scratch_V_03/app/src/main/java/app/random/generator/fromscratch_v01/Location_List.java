package app.random.generator.fromscratch_v01;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import app.random.generator.fromscratch_v01.Adapters.MyLocations_Adapter;
import app.random.generator.fromscratch_v01.Modelo.MyLocations;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Location_List.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Location_List#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Location_List extends Fragment {

    private static final String TAG = Location_List.class.getSimpleName();
    private MyLocations_Adapter adapter;
    private RecyclerView listaLocaciones;
    private RecyclerView.LayoutManager lManager;
    private Gson gson = new Gson();

    SharedPreferences sharedPreferences;
    String channel;

    private ListView list_locations;

    private HashMap<String, String> locations_user;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Location_List() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Location_List.
     */
    // TODO: Rename and change types and number of parameters
    public static Location_List newInstance(String param1, String param2) {
        Location_List fragment = new Location_List();
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
        View v = inflater.inflate(R.layout.fragment_location__list, container, false);

        //Shared Preferences

        sharedPreferences = this.getActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
        channel = (sharedPreferences.getString("id", ""));

        /* CAMBIOS DE TIPOGRAFIA */
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/KGAlwaysAGoodTime.ttf");

        TextView titulo_locat_list = (TextView) v.findViewById(R.id.titulo_locat_list);
        titulo_locat_list.setTypeface(font);


         /* LISTA PERSONAJES */
        listaLocaciones = (RecyclerView) v.findViewById(R.id.reciclador_locaciones);
        listaLocaciones.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(getActivity());
        listaLocaciones.setLayoutManager(lManager);

        // Cargar datos en el adaptador
        cargarAdaptador();

        //Decoracion Recycler
        listaLocaciones.addItemDecoration(new android.support.v7.widget.DividerItemDecoration(getActivity(), android.support.v7.widget.DividerItemDecoration.VERTICAL));

        return v;
    }

    public void cargarAdaptador() {
        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("user_id", channel);

        JSONObject jobject = new JSONObject(map);

        Log.d(TAG, jobject.toString());

        VolleySingleton.getInstance(getContext()).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        Constantes.GET_LOCATION_USER,
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
                    JSONArray mensaje = response.getJSONArray("location_user");
                    // Parsear con Gson
                    MyLocations[] locations = gson.fromJson(mensaje.toString(), MyLocations[].class);
                    // Inicializar adaptador
                    adapter = new MyLocations_Adapter(Arrays.asList(locations), getActivity());
                    // Setear adaptador a la lista
                    listaLocaciones.setAdapter(adapter);
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


    @Override
    public void onActivityCreated (@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        /*final ListView listViewLocations = (ListView) getActivity().findViewById(R.id.list_locations);
        Resources res = getResources();
        String [] locations = res.getStringArray(R.array.locations);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, locations);
        listViewLocations.setAdapter(adapter);
        listViewLocations.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView temp = (TextView) view;
                String Value = listViewLocations.getItemAtPosition(i).toString();

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
