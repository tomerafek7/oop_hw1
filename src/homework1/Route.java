package homework1;

import java.util.Iterator;
import java.util.ArrayList;

/**
 * A Route is a path that traverses arbitrary GeoSegments, regardless
 * of their names.
 * <p>
 * Routes are immutable. New Routes can be constructed by adding a segment 
 * to the end of a Route. An added segment must be properly oriented; that 
 * is, its p1 field must correspond to the end of the original Route, and
 * its p2 field corresponds to the end of the new Route.
 * <p>
 * Because a Route is not necessarily straight, its length - the distance
 * traveled by following the path from start to end - is not necessarily
 * the same as the distance along a straight line between its endpoints.
 * <p>
 * Lastly, a Route may be viewed as a sequence of geographical features,
 * using the <tt>getGeoFeatures()</tt> method which returns an Iterator of
 * GeoFeature objects.
 * <p>
 * <b>The following fields are used in the specification:</b>
 * <pre>
 *   start : GeoPoint            // location of the start of the route
 *   end : GeoPoint              // location of the end of the route
 *   startHeading : angle        // direction of travel at the start of the route, in degrees
 *   endHeading : angle          // direction of travel at the end of the route, in degrees
 *   geoFeatures : sequence      // a sequence of geographic features that make up this Route
 *   geoSegments : sequence      // a sequence of segments that make up this Route
 *   length : real               // total length of the route, in kilometers
 *   endingGeoSegment : GeoSegment  // last GeoSegment of the route
 * </pre>
 **/
public class Route {


	final GeoPoint start, end;
	final double startHeading, endHeading;
	final ArrayList<GeoFeature> geoFeatures;
	final ArrayList<GeoSegment> geoSegments;
	final double length;
	final GeoSegment endingGeoSegment;




 	// TODO Write abstraction function and representation invariant


  	/**
  	 * Constructs a new Route.
     * @requires gs != null
     * @effects Constructs a new Route, r, such that
     *	        r.startHeading = gs.heading &&
     *          r.endHeading = gs.heading &&
     *          r.start = gs.p1 &&
     *          r.end = gs.p2
     **/
  	public Route(GeoSegment gs) {
  		assert gs != null : "Canont Create Route: the given GeoSegment = null";
		this.start = gs.getP1();
		this.end = gs.getP2();
  		this.startHeading = gs.getHeading();
  		this.endHeading = gs.getHeading();
  		this.geoFeatures = new ArrayList<>();
//  		this.geoFeatures.add()
  		this.geoSegments = new ArrayList<>();
		this.geoSegments.add(gs);
		this.length = gs.getLength();
		this.endingGeoSegment = gs;
  	}

	/**
	 * Constructs a new Route from an existing Route & A GeoSegment to append.
	 * @requires r!=null && gs != null
	 * @effects Constructs a new Route, r, such that
	 *	        new_r.startHeading = r.startHeading &&
	 *          new_r.endHeading = gs.heading &&
	 *          new_r.start = r.start &&
	 *          new_r.end = gs.p2
	 **/
	private Route(Route r, GeoSegment gs) {
		assert gs != null && r != null : "Canont Create Route: one or more null parameters";
		this.start = r.getStart();
		this.end = gs.getP2();
		this.startHeading = r.getStartHeading();
		this.endHeading = gs.getHeading();
		this.geoFeatures = new ArrayList<>();
//  		this.geoFeatures.add()
		this.geoSegments = new ArrayList<>();
		this.endingGeoSegment = gs;

		// running through all segments of r, and append them into the new route:
		for (Iterator<GeoSegment> it = r.getGeoSegments(); it.hasNext(); ) {
			GeoSegment seg = it.next();
			this.geoSegments.add(seg);
		}
		// adding the last segment to the list:
		this.geoSegments.add(gs);
		// calc length:
		this.length = r.length + gs.getLength();
	}

    /**
     * Returns location of the start of the route.
     * @return location of the start of the route.
     **/
  	public GeoPoint getStart() {
  		return this.start;
  	}


  	/**
  	 * Returns location of the end of the route.
     * @return location of the end of the route.
     **/
  	public GeoPoint getEnd() {
		return this.end;
  	}


  	/**
  	 * Returns direction of travel at the start of the route, in degrees.
   	 * @return direction (in compass heading) of travel at the start of the
   	 *         route, in degrees.
   	 **/
  	public double getStartHeading() {
  		return this.startHeading;
  	}


  	/**
  	 * Returns direction of travel at the end of the route, in degrees.
     * @return direction (in compass heading) of travel at the end of the
     *         route, in degrees.
     **/
  	public double getEndHeading() {
		return this.endHeading;
  	}


  	/**
  	 * Returns total length of the route.
     * @return total length of the route, in kilometers.  NOTE: this is NOT
     *         as-the-crow-flies, but rather the total distance required to
     *         traverse the route. These values are not necessarily equal.
   	 **/
  	public double getLength() {
  		return this.length;
  	}


  	/**
     * Creates a new route that is equal to this route with gs appended to
     * its end.
   	 * @requires gs != null && gs.p1 == this.end
     * @return a new Route r such that
     *         r.end = gs.p2 &&
     *         r.endHeading = gs.heading &&
     *         r.length = this.length + gs.length
     **/
  	public Route addSegment(GeoSegment gs) {
  		assert gs != null: "Canont Create Route: the given GeoSegment = null";
  		assert gs.getP1().equals(this.end): "Cannot Add Segment: gs.p1 != this.end";
		return new Route(this, gs); // calling (private) C'tor.
	}


    /**
     * Returns an Iterator of GeoFeature objects. The concatenation
     * of the GeoFeatures, in order, is equivalent to this route. No two
     * consecutive GeoFeature objects have the same name.
     * @return an Iterator of GeoFeatures such that
     * <pre>
     *      this.start        = a[0].start &&
     *      this.startHeading = a[0].startHeading &&
     *      this.end          = a[a.length - 1].end &&
     *      this.endHeading   = a[a.length - 1].endHeading &&
     *      this.length       = sum(0 <= i < a.length) . a[i].length &&
     *      for all integers i
     *          (0 <= i < a.length - 1 => (a[i].name != a[i+1].name &&
     *                                     a[i].end  == a[i+1].start))
     * </pre>
     * where <code>a[n]</code> denotes the nth element of the Iterator.
     * @see homework1.GeoFeature
     **/
  	public Iterator<GeoFeature> getGeoFeatures() {
  		// TODO Implement this method
  	}


  	/**
     * Returns an Iterator of GeoSegment objects. The concatenation of the
     * GeoSegments, in order, is equivalent to this route.
     * @return an Iterator of GeoSegments such that
     * <pre>
     *      this.start        = a[0].p1 &&
     *      this.startHeading = a[0].heading &&
     *      this.end          = a[a.length - 1].p2 &&
     *      this.endHeading   = a[a.length - 1].heading &&
     *      this.length       = sum (0 <= i < a.length) . a[i].length
     * </pre>
     * where <code>a[n]</code> denotes the nth element of the Iterator.
     * @see homework1.GeoSegment
     **/
  	public Iterator<GeoSegment> getGeoSegments() {
  		// TODO Implement this method
  	}


  	/**
     * Compares the specified Object with this Route for equality.
     * @return true iff (o instanceof Route) &&
     *         (o.geoFeatures and this.geoFeatures contain
     *          the same elements in the same order).
     **/
  	public boolean equals(Object o) {
  		// TODO Implement this method
  	}


    /**
     * Returns a hash code for this.
     * @return a hash code for this.
     **/
  	public int hashCode() {
    	// This implementation will work, but you may want to modify it
    	// for improved performance.

    	return 1;
  	}


    /**
     * Returns a string representation of this.
     * @return a string representation of this.
     **/
  	public String toString() {
  	}

}
