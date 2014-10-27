package com.trekker;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.util.AttributeSet;

import com.nutiteq.MapView;
import com.nutiteq.components.Components;
import com.nutiteq.components.MapPos;
import com.nutiteq.geometry.Line;
import com.nutiteq.geometry.Marker;
import com.nutiteq.projections.EPSG3857;
import com.nutiteq.projections.Projection;
import com.nutiteq.style.LineStyle;
import com.nutiteq.style.MarkerStyle;
import com.nutiteq.utils.UnscaledBitmapLoader;
import com.nutiteq.vectorlayers.GeometryLayer;
import com.nutiteq.vectorlayers.MarkerLayer;

public class BlackMapView extends MapView{
	private MarkerLayer markerLayer;
	private GeometryLayer geometryLayer;

	private Marker currMarkerPosition;

	public BlackMapView(Context context, AttributeSet attrs) {
		super(context, attrs);

        Projection proj = new EPSG3857();
        this.geometryLayer = new GeometryLayer(proj);
        this.markerLayer = new MarkerLayer(proj);

        Components c = new Components();
        c.options.setBackgroundPlaneColor(com.nutiteq.components.Color.BLACK);
        c.options.setBackgroundPlaneOverlayColor(com.nutiteq.components.Color.BLACK);
        c.options.setClearColor(com.nutiteq.components.Color.BLACK);

        setComponents(c);

        getLayers().setBaseLayer(this.geometryLayer);
        getLayers().addLayer(this.markerLayer);
	}

	public MarkerLayer getMarkerLayer() {
		return markerLayer;
	}

	public GeometryLayer getGeometryLayer() {
		return geometryLayer;
	}

	public void addMarker(Location loc, int id){
		Bitmap markerImg = UnscaledBitmapLoader.decodeResource(getResources(), id);
		MarkerStyle markerStyle = MarkerStyle.builder().setBitmap(markerImg).setSize(0.5f).build();

		MapPos markerLocation = this.getLayers().getBaseLayer().getProjection().fromWgs84(loc.getLongitude(),loc.getLatitude());
        markerLayer.add(new Marker(markerLocation, null, markerStyle, markerLayer));		
        markerLayer.updateVisibleElements();
	}

	public void addPath(List<Location> path){
		Location lastLoc = path.get(path.size() - 1);
		Bitmap markerImg = UnscaledBitmapLoader.decodeResource(getResources(), R.drawable.location);
		MarkerStyle markerStyle = MarkerStyle.builder().setBitmap(markerImg).setSize(0.5f).build();
		MapPos markerLocation = this.getLayers().getBaseLayer().getProjection().fromWgs84(lastLoc.getLongitude(),lastLoc.getLatitude());
   		
		if(currMarkerPosition == null){
			currMarkerPosition = new Marker(markerLocation, null, markerStyle, markerLayer);
			markerLayer.add(currMarkerPosition);
		}else{
			currMarkerPosition.setMapPos(markerLocation);
		}
    		
        markerLayer.updateVisibleElements();

		if(path.size() >= 2){
			geometryLayer.clear();
			List<MapPos> listMapPos = new LinkedList<MapPos>();
			for(Location loc: path)
				listMapPos.add(this.getLayers().getBaseLayer().getProjection().fromWgs84(loc.getLongitude(),loc.getLatitude()));
			LineStyle lStyle = LineStyle.builder().setColor(Color.WHITE).setWidth(0.1f).build();
			Line line = new Line(listMapPos,null,lStyle,null);
			geometryLayer.add(line);
			geometryLayer.updateVisibleElements();
		}
	}
}
