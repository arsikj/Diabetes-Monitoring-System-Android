package mk.ukim.finki.nsi.dms.activities;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import mk.ukim.finki.nsi.dms.R;
import mk.ukim.finki.nsi.dms.fragments.BMIFragment;
import mk.ukim.finki.nsi.dms.fragments.BreadUnitsChartsFragment;
import mk.ukim.finki.nsi.dms.fragments.BreadUnitsFragment;
import mk.ukim.finki.nsi.dms.fragments.MeasureChartsFragment;
import mk.ukim.finki.nsi.dms.fragments.MeasureFragment;
import mk.ukim.finki.nsi.dms.fragments.ProfileFragment;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ConstraintLayout contentFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Measures");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        contentFrame = (ConstraintLayout) findViewById(R.id.content_frame);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

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
        String title = "";

        if (id == R.id.nav_measure) {
            fragment = new MeasureFragment();
            title = "Measures";
        } else if (id == R.id.nav_measure_charts) {
            fragment = new MeasureChartsFragment();
            title = "Measures chart";
        } else if (id == R.id.nav_bread_units) {
            fragment = new BreadUnitsFragment();
            title = "Bread units";
        } else if (id == R.id.nav_bread_units_charts) {
            fragment = new BreadUnitsChartsFragment();
            title = "Bread units chart";
        } else if (id == R.id.nav_bmi) {
            fragment = new BMIFragment();
            title = "BMI";
        } else if (id == R.id.nav_profile) {
            fragment = new ProfileFragment();
            title = "Profile";
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        // set the toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
