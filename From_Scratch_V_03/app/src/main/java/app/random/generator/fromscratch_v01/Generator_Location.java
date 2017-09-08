package app.random.generator.fromscratch_v01;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
 * {@link Generator_Location.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Generator_Location#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Generator_Location extends Fragment implements AdapterView.OnItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //SPINNERS
    private Spinner sp_genre_loc;
    private Spinner sp_type_loc;

    private HashMap<String, String> genres_loc;
    private HashMap<String, String> type_loc;

    private String gl_type_loc_id;

    private String data_name = "";
    private String data_id = "";

    private Button btn_generate_loc;
    private TextView generate_loc;

    SharedPreferences sharedPreferences;
    String channel;

    private OnFragmentInteractionListener mListener;

    public Generator_Location() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Generator_Location.
     */
    // TODO: Rename and change types and number of parameters
    public static Generator_Location newInstance(String param1, String param2) {
        Generator_Location fragment = new Generator_Location();
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
        View v = inflater.inflate(R.layout.fragment_generator__location, container, false);

        //Shared Preferences

        sharedPreferences = this.getActivity().getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
        channel = (sharedPreferences.getString("id", ""));

        sp_type_loc = (Spinner) v.findViewById(R.id.type_location);

        //SPINNER GENRES

        sp_genre_loc = (Spinner) v.findViewById(R.id.genre_location);

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

        btn_generate_loc = (Button) v.findViewById(R.id.generate_location);
        generate_loc = (TextView) v.findViewById(R.id.name_location);

        btn_generate_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Actualizar datos en el servidor
                HashMap<String, String> data = new HashMap<>();
                data.put("location_type_id",  String.valueOf(gl_type_loc_id));

                JSONObject jsonObject = new JSONObject (data);

                System.out.print(jsonObject.toString());

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.GET_LOCATION_TYPE,
                                jsonObject,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        generarLocacion(response);
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


        //BUTTON SAVE

        final Button save_loc = (Button) v.findViewById(R.id.save_location);

        save_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Actualizar datos en el servidor

                HashMap <String, String> data = new LinkedHashMap<>();
                data.put("location_id",  String.valueOf(data_id));
                data.put("user_id", channel);

                JSONObject jsonObject = new JSONObject (data);

                System.out.print(jsonObject.toString());

                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERT_LOCATION_USER,
                                jsonObject,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        //Toast.makeText(getActivity(), "Location has been saved", Toast.LENGTH_SHORT).show();
                                        final String mensaje = "Location has been saved.";
                                        new NotificationThread(mensaje).execute(5);
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

    /* GENERAR LOCACION AL HACER CLICK */

        public void generarLocacion(JSONObject response) {
            try {
                //Obtener atributo estado
                String estado = response.getString(Constantes.ESTADO);
                switch (estado) {
                    case Constantes.SUCCESS:

                        JSONArray retorno = response.getJSONArray("location");

                        for (int i = 0; i < retorno.length(); i++) {
                            JSONObject jb1 = retorno.getJSONObject(i);

                            String name = jb1.getString("name");
                            String id = jb1.getString("id");

                            data_name = name;
                            data_id = id;
                        }

                        generate_loc.setText(data_name);

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


    /* OBTENER GENRES EN EL SPINNER */

    public void obtenerDataGenres(JSONObject response) {
        try {
            //Obtener atributo estado
            String estado = response.getString(Constantes.ESTADO);
            switch (estado) {
                case Constantes.SUCCESS:
                    List<String> idList = new ArrayList<>();
                    genres_loc = new HashMap<>();
                    JSONArray retorno = response.getJSONArray("genres");
                    //Iniciar Adaptador
                    for (int i = 0; i < retorno.length(); i++) {
                        JSONObject jb1 = retorno.getJSONObject(i);
                        genres_loc.put(jb1.getString("description"), jb1.getString("id"));

                    }
                    idList.addAll(genres_loc.keySet());
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_spinner_item, idList);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_genre_loc.setAdapter(dataAdapter);
                    sp_genre_loc.setOnItemSelectedListener(this);
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

    /* OBTENER TYPE_LOCATIONS EN EL SPINNER */

    public void obtenerDataTypeLocation(JSONObject response) {
        try {
            //Obtener atributo estado
            String estado = response.getString(Constantes.ESTADO);
            switch (estado) {
                case Constantes.SUCCESS:
                    List<String> idList = new ArrayList<>();
                    type_loc = new HashMap<>();
                    JSONArray retorno = response.getJSONArray("location_type");
                    //Iniciar Adaptador
                    for (int i = 0; i < retorno.length(); i++) {
                        JSONObject jb1 = retorno.getJSONObject(i);
                        type_loc.put(jb1.getString("description"), jb1.getString("id"));

                    }
                    idList.addAll(type_loc.keySet());
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_spinner_item, idList);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_type_loc.setAdapter(dataAdapter);
                    sp_type_loc.setOnItemSelectedListener(this);
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


    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        Spinner spinner = (Spinner) parent;
        if(spinner.getId() == R.id.genre_location)
        {
            String genre = (String) ((TextView) view).getText();
            String genre_id = genres_loc.get(genre);

            /*Toast.makeText(getActivity().getApplicationContext(), "Genre " + genre + "Genre_id " + genre_id, Toast.LENGTH_SHORT).show();*/

            // Actualizar datos en el servidor
            HashMap<String, String> data = new HashMap<>();
            data.put("genre_id",  String.valueOf(genre_id));

            JSONObject jsonObject = new JSONObject (data);

            System.out.print(jsonObject.toString());

            VolleySingleton.getInstance(getContext()).addToRequestQueue(
                    new JsonObjectRequest(
                            Request.Method.POST,
                            Constantes.GET_LOCATION_GENRE,
                            jsonObject,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    obtenerDataTypeLocation(response);
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
        else if(spinner.getId() == R.id.type_location)
        {
            String type_location = (String) ((TextView) view).getText();
            String type_location_id = type_loc.get(type_location);
            gl_type_loc_id = type_location_id;

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

    public class NotificationThread extends AsyncTask<Integer, Integer, String> {

        String mensaje;

        public NotificationThread(String mensaje){
            this.mensaje = mensaje;
        }

        @Override
        protected String doInBackground(Integer... params) {
            int n = params[0];
            int myProgress = 0;
            publishProgress(myProgress++);
            for(int i=0;i<n;i++){
                try {
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        protected void onProgressUpdate(Integer... values) {
            // Executes whenever publishProgress is called from doInBackground
            // Used to update the progress indicator
            super.onProgressUpdate(values);
            //save_text.setText("Saving "+ values[0]);
            Toast.makeText(getActivity(), "Saving location...", Toast.LENGTH_SHORT).show();
        }

        protected void onPostExecute(String result) {
            Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();
            //save_text.setText(mensaje);
            //createNotification("fromScratch",mensaje);
        }

    }
}

