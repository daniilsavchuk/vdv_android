package com.its.vdv;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.its.vdv.data.Court;
import com.its.vdv.rest.wrapper.CourtsRestWrapper;
import com.its.vdv.rest.wrapper.RestListener;
import com.its.vdv.views.NavigationFooterView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@EActivity(R.layout.activity_map)
public class MapActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    @ViewById(R.id.footer)
    NavigationFooterView navigationFooterView;

    @FragmentById(R.id.map)
    SupportMapFragment mapFragment;
    
    @Bean
    CourtsRestWrapper courtsRestWrapper;
    
    private List<Court> courts = new ArrayList<>();
    
    @AfterViews
    public void init() {
        navigationFooterView.setPage(NavigationFooterView.Page.MAP);
        
        courtsRestWrapper.getAllCourts(new RestListener<List<Court>>() {
            @Override
            public void onSuccess(List<Court> courts) {
                MapActivity.this.courts = courts;
                
                initMap();
            }
        });
    }
    
    @UiThread
    void initMap() {
        mapFragment.getMapAsync(MapActivity.this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        CameraUpdate center = CameraUpdateFactory.newLatLngZoom(getMapPosition(), 13);

        googleMap.animateCamera(center);

        for (Court court : courts) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(court.getLat(), court.getLon()))
                    .title(court.getName());
            
            googleMap.addMarker(markerOptions);
        }

        googleMap.setOnMarkerClickListener(this);
    }
    
    @Override
    public boolean onMarkerClick(Marker marker) {
        redirect(CourtActivity_.class, 0, 0, false, new HashMap<>());
        return true;
    }
    
    private LatLng getMapPosition() {
        double minLat = Double.MAX_VALUE, maxLat = Double.MIN_VALUE;
        double minLon = Double.MAX_VALUE, maxLon = Double.MIN_VALUE;
        
        for (Court court : courts) {
            minLat = Math.min(court.getLat(), minLat);
            maxLat = Math.max(court.getLat(), maxLat);

            minLon = Math.min(court.getLon(), minLon);
            maxLon = Math.max(court.getLon(), maxLon);
        }

        return new LatLng((maxLat + minLat) / 2, (maxLon + minLon) / 2);
    }
}
