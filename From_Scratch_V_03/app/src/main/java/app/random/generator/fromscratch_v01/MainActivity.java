package app.random.generator.fromscratch_v01;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        New_Story.OnFragmentInteractionListener, New_Character.OnFragmentInteractionListener,
        New_Location.OnFragmentInteractionListener, Notebook.OnFragmentInteractionListener,
        Settings.OnFragmentInteractionListener, Help.OnFragmentInteractionListener, About.OnFragmentInteractionListener,
        Generator_Character.OnFragmentInteractionListener, Generator_Location.OnFragmentInteractionListener,
        Character_List.OnFragmentInteractionListener, Location_List.OnFragmentInteractionListener, Genre_List.OnFragmentInteractionListener,
        Story_Overview.OnFragmentInteractionListener, GoogleApiClient.OnConnectionFailedListener {


    /* VARIABLES PARA CARGAR DATOS DE GOOGLE */
    private ImageView photoImageView;
    private TextView nameTextView;
    private TextView emailTextView;

    /* VARIABLES PARA CONEXION CON GOOGLE*/
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* ELEMENTOS DEL NAVEGATION DRAWER */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        /* ELEMENTOS PARA CARGAR DATOS DE GOOGLE */

        View header = navigationView.getHeaderView(0);

        photoImageView = (ImageView) header.findViewById(R.id.photoImageView);
        nameTextView = (TextView) header.findViewById(R.id.nameTextView);
        emailTextView = (TextView) header.findViewById(R.id.emailTextView);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        /* CAMBIOS DE TIPOGRAFIA */

        Typeface font = Typeface.createFromAsset(getApplication().getAssets(), "fonts/KGAlwaysAGoodTime.ttf");

        TextView titulo_nameGoogle = (TextView) findViewById(R.id.nameTextView);
        titulo_nameGoogle.setTypeface(font);


        /* FRAGMENT PRINCIPAL EN CONTENEDOR */

        getSupportFragmentManager().beginTransaction().replace(R.id.Contenedor, new Notebook()).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (opr.isDone()) {
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    /* METODOS DE GOOGLE PARA LOGIN */

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {

            GoogleSignInAccount account = result.getSignInAccount();

            nameTextView.setText(account.getDisplayName());
            emailTextView.setText(account.getEmail());

            Glide.with(this).load(account.getPhotoUrl()).apply(RequestOptions.circleCropTransform()).into(photoImageView);

        }else{
            goLogInScreen();
        }
    }

    private void goLogInScreen() {
        Intent intent = new Intent(this, Splash_Menu.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void logOut() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    goLogInScreen();
                } else {
                    Toast.makeText(getApplicationContext(), "Couldn't sign out", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /* METODOS DEL NAVEGATION DRAWER */

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        Boolean FragmentSeleccionado = false;

        if (id == R.id.nav_new_story) {
            fragment = new New_Story();
            FragmentSeleccionado = true;

        } else if (id == R.id.nav_new_character) {
            fragment = new Generator_Character();
            FragmentSeleccionado = true;

        } else if (id == R.id.nav_new_location) {
            fragment = new Generator_Location();
            FragmentSeleccionado = true;

        } else if (id == R.id.nav_notebook) {
            fragment = new Notebook();
            FragmentSeleccionado = true;

        } else if (id == R.id.nav_settings) {
            fragment = new Settings();
            FragmentSeleccionado = true;

        } else if (id == R.id.nav_help) {
            fragment = new Help();
            FragmentSeleccionado = true;

        } else if (id == R.id.nav_about) {
            fragment = new About();
            FragmentSeleccionado = true;

        } else if (id == R.id.log_out) {
            logOut();
        }

        if (FragmentSeleccionado){
            getSupportFragmentManager().beginTransaction().replace(R.id.Contenedor, fragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
