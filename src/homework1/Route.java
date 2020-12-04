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
 * <pre>
 * <b>Abs. Function:</b>
 * Represents a route of one or more GeoSegments. holds the start&end points, start&end headings, the total length,
 * and a container of all segments.
 * <b>Rep. Invariant:</b>
 * both end-points are not null
 * 0 <= startHeading, endHeading < 360
 * for each pair of subsequent segments, g1,g2, g2.start == g1.end
 * length = SUM(gs.length for each gs in list)
 * </pre>
 */

public class Route {


	final GeoPoint start, end;
	final double startHeading, endHeading;
	final ArrayList<GeoFeature> geoFeatures;
	final ArrayList<GeoSegment> geoSegments;
	final double length;
	final GeoSegment endingGeoSegment;


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
  		this.geoFeatures.add(new GeoFeature(gs));
  		this.geoSegments = new ArrayList<>();
		this.geoSegments.add(gs);
		this.length = gs.getLength();
		this.endingGeoSegment = gs;
		this.checkRep();

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
		this.endingGeoSegment = gs;
		this.length = r.getLength() + gs.getLength(); // length calc:
		this.geoSegments = new ArrayList<>(r.geoSegments);
		// adding the last segment to the list:
		this.geoSegments.add(gs);
		this.geoFeatures = new ArrayList<>();
		// running through all features of r:
		for (Iterator<GeoFeature> it = r.getGeoFeatures(); it.hasNext(); ) {
			GeoFeature feature = it.next();
			if (it.hasNext()) { // not last element - just add the feature as is
				this.geoFeatures.add(feature);
			} else { // last element - check if the name is similar
				if (feature.getName().equals(gs.getName())) { // same name --> append gs to the last feature
					this.geoFeatures.add(feature.addSegment(gs));
				} else { // different name --> just add the feature as is and append new different feature with gs
					this.geoFeatures.add(feature);
					this.geoFeatures.add(new GeoFeature(gs));
				}
			}
		}

		this.checkRep();
	}

    /**
     * Returns location of the start of the route.
     * @return location of the start of the route.
     **/
  	public GeoPoint getStart() {
		this.checkRep();
		return this.start;
  	}


  	/**
  	 * Returns location of the end of the route.
     * @return location of the end of the route.
     **/
  	public GeoPoint getEnd() {
		this.checkRep();
		return this.end;
  	}


  	/**
  	 * Returns direction of travel at the start of the route, in degrees.
   	 * @return direction (in compass heading) of travel at the start of the
   	 *         route, in degrees.
   	 **/
  	public double getStartHeading() {
		this.checkRep();
		return this.startHeading;
  	}


  	/**
  	 * Returns direction of travel at the end of the route, in degrees.
     * @return direction (in compass heading) of travel at the end of the
     *         route, in degrees.
     **/
  	public double getEndHeading() {
		this.checkRep();
		return this.endHeading;
  	}


  	/**
  	 * Returns total length of the route.
     * @return total length of the route, in kilometers.  NOTE: this is NOT
     *         as-the-crow-flies, but rather the total distance required to
     *         traverse the route. These values are not necessarily equal.
   	 **/
  	public double getLength() {
		this.checkRep();
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
		this.checkRep();
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
		this.checkRep();
		return this.geoFeatures.iterator();
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
		this.checkRep();
		return this.geoSegments.iterator();
  	}


  	/**
     * Compares the specified Object with this Route for equality.
     * @return true iff (o instanceof Route) &&
     *         (o.geoFeatures and this.geoFeatures contain
     *          the same elements in the same order).
     **/
  	public boolean equals(Object o) {
		this.checkRep();
		if (o == null)
			return false;
		if (!(o instanceof Route))
			return false;
		// at this point we know that o != null and o's type is GeoPoint
		Route route = (Route)o;
		this.checkRep();
		return this.geoFeatures.equals(route.geoFeatures);
  	}


    /**
     * Returns a hash code for this.
     * @return a hash code for this.
     **/
  	public int hashCode() {
    	return (int)this.getLength();
  	}

    /**
     * Returns a string representation of this.
     * @return a string representation of this.
     **/
  	public String toString() {
  		return String.format("Route: \n start: %s \n end: %s \n startHeading: %f \n endHeading: %f \n",
				this.start.toString(), this.end.toString(), this.startHeading, this.endHeading);
  	}

	/**
	 * Checks if the Representation Invariant is being violated.
	 * @throws AssertionError in case it's violated.
	 **/
	private void checkRep() {
		assert this.start != null && this.end != null
				&& this.startHeading < GeoPoint.COMPASS_NUM_DEGREES && this.startHeading >= 0
				&& this.endHeading < GeoPoint.COMPASS_NUM_DEGREES && this.endHeading >= 0 :
				"Rep. Invariant of Route is broken";
		double length_sum = 0;
		GeoPoint prev_endpoint = null;
		for (GeoSegment seg : this.geoSegments) {
			if (prev_endpoint != null) {
				assert seg.getP1().equals(prev_endpoint) : "Rep. Invariant of Route is broken";
			}
			prev_endpoint = seg.getP2();
			length_sum += seg.getLength();
		}
		assert length_sum == this.length: "Rep. Invariant of Route is broken";

		}

}
