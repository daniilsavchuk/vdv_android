package com.its.vdv;

import android.graphics.PorterDuff;

import com.annimon.stream.Stream;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.its.vdv.data.Court;
import com.its.vdv.rest.wrapper.SearchRestWrapper;
import com.its.vdv.rest.wrapper.RestListener;
import com.its.vdv.views.NavigationFooterView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EActivity(R.layout.activity_map)
public class MapActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    @ViewById(R.id.footer)
    NavigationFooterView navigationFooterView;

    @FragmentById(R.id.map)
    SupportMapFragment mapFragment;
    
    @Bean
    SearchRestWrapper searchRestWrapper;
    
    private List<Court> courts = new ArrayList<>();
    
    @AfterViews
    public void init() {
        navigationFooterView.setPage(NavigationFooterView.Page.MAP);

        //TODO: FIXIT TO CURRENT USER POSITION
        searchRestWrapper.searchCourt(59.9588, 30.3039, 0.1, new RestListener<List<Court>>() {
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
            BitmapDescriptor iconic = BitmapDescriptorFactory.fromResource(R.drawable.ic_map_icon);
            /*iconic.getDrawable().setColorFilter(
                    getResources().getColor(R.color.vdv),
                    PorterDuff.Mode.SRC_IN
            );*/

            MarkerOptions markerOptions = new MarkerOptions().icon(iconic).flat(true)
                    .position(new LatLng(court.getLat(), court.getLon()))
                    .title(court.getName());
            
            googleMap.addMarker(markerOptions);
        }

        googleMap.setOnMarkerClickListener(this);
    }
    
    @Override
    public boolean onMarkerClick(Marker marker) {
        Map<String, Serializable> extras = new HashMap<>();

        extras.put("courtId", Stream.of(courts)
                .filter(it -> it.getName().equals(marker.getTitle()))
                .findFirst()
                .orElseThrow(RuntimeException::new)
                .getId()
        );

        redirect(CourtActivity_.class, 0, 0, false, extras);

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
