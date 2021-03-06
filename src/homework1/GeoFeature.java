package homework1;

import java.util.Iterator;
import java.util.ArrayList;

/**
 * A GeoFeature represents a route from one location to another along a
 * single geographic feature. GeoFeatures are immutable.
 * <p>
 * GeoFeature abstracts over a sequence of GeoSegments, all of which have
 * the same name, thus providing a representation for nonlinear or nonatomic
 * geographic features. As an example, a GeoFeature might represent the
 * course of a winding river, or travel along a road through intersections
 * but remaining on the same road.
 * <p>
 * GeoFeatures are immutable. New GeoFeatures can be constructed by adding
 * a segment to the end of a GeoFeature. An added segment must be properly
 * oriented; that is, its p1 field must correspond to the end of the original
 * GeoFeature, and its p2 field corresponds to the end of the new GeoFeature,
 * and the name of the GeoSegment being added must match the name of the
 * existing GeoFeature.
 * <p>
 * Because a GeoFeature is not necessarily straight, its length - the
 * distance traveled by following the path from start to end - is not
 * necessarily the same as the distance along a straight line between
 * its endpoints.
 * <p>
 * <b>The following fields are used in the specification:</b>
 * <pre>
 *   start : GeoPoint       // location of the start of the geographic feature
 *   end : GeoPoint         // location of the end of the geographic feature
 *   startHeading : angle   // direction of travel at the start of the geographic feature, in degrees
 *   endHeading : angle     // direction of travel at the end of the geographic feature, in degrees
 *   geoSegments : sequence	// a sequence of segments that make up this geographic feature
 *   name : String          // name of geographic feature
 *   length : real          // total length of the geographic feature, in kilometers
 * </pre>
 * <pre>
 * <b>
 *  Abs. Function:
 * 	  Represents a collection of segments on earth, which have the same name .
 * 	  holds the segments name, two endpoints, the sum of the all segments length,
 * 	  between them and the compasses heading of the first and last segment,
 * 	  and a list of all segments.
 *
 * Rep. Invariant:
 * 	  both end-points are not null
 * 	  name is not null
 * 	  all segments are concatenated(i.e. a[i].p2=a[i+1].p1)
 * </b>
 * </pre>
 **/
public class GeoFeature {
	
	// Implementation hint:
	// When asked to return an Iterator, consider using the iterator() method
	// in the List interface. Two nice classes that implement the List
	// interface are ArrayList and LinkedList. If comparing two Lists for
	// equality is needed, consider using the equals() method of List. More
	// info can be found at:
	//   http://docs.oracle.com/javase/8/docs/api/java/util/List.html



    final GeoPoint start, end ;         // location of the end of the geographic feature
    final double length, startHeading, endHeading;     // direction of travel at the end of the geographic feature, in degrees
    final ArrayList<GeoSegment> geoSegments; 	// a sequence of segments that make up this geographic feature
    final String name;          // name of geographic feature

	/**
     * Constructs a new GeoFeature.
     * @requires gs != null
     * @effects Constructs a new GeoFeature, r, such that
     *	        r.name = gs.name &&
     *          r.startHeading = gs.heading &&
     *          r.endHeading = gs.heading &&
     *          r.start = gs.p1 &&
     *          r.end = gs.p2
     **/
  	public GeoFeature(GeoSegment gs) {
        assert gs != null:
        "Can't build GeoFeature: one (or more) input/s are null" ;
        this.name = gs.getName();
        this.startHeading = gs.getHeading();
        this.endHeading = gs.getHeading();
        this.start = gs.getP1();
        this.end = gs.getP2();
        this.length = gs.getLength();
        this.geoSegments = new ArrayList<GeoSegment>();
        this.geoSegments.add(gs);
  	}

	/**
	 * Constructs a new GeoFeature, out of a given GeoFeature and a new GeoSegment
	 * @requires gs != null && gf != null && gs.name == gf.name
	 * @effects Constructs a new GeoFeature, r, such that
	 *	        r.name = gf.name &&
	 *          r.startHeading = gf.heading &&
	 *          r.endHeading = gs.heading &&
	 *          r.start = gf.p1 &&
	 *          r.end = gs.p2
	 **/
  	public GeoFeature(GeoFeature gf,GeoSegment gs) {
		this.name = gf.getName();
		// if gf's segments are all in length = 0, start heading will be gs.heading():
		if (gf.getStartHeading() == -1){
			this.startHeading = gs.getHeading();
		} else{
			this.startHeading = gf.getStartHeading();
		}
		// if gs is in length = 0, end heading will be gf.endHeading:
		if (gs.getHeading() == -1){
			this.endHeading = gf.getEndHeading();
		} else{
			this.endHeading = gs.getHeading();
		}
		this.start = gf.getStart();
		this.end = gs.getP2();
		this.length = gf.getLength() + gs.getLength();
		this.geoSegments = new ArrayList<GeoSegment>(gf.geoSegments);
		this.geoSegments.add(gs);
	}
  

 	/**
 	  * Returns name of geographic feature.
      * @return name of geographic feature
      */
  	public String getName() {
        this.checkRep();
  		return this.name;
   	}


  	/**
  	 * Returns location of the start of the geographic feature.
     * @return location of the start of the geographic feature.
     */
  	public GeoPoint getStart() {
		this.checkRep();
		return this.start;
  	}


  	/**
  	 * Returns location of the end of the geographic feature.
     * @return location of the end of the geographic feature.
     */
  	public GeoPoint getEnd() {
		this.checkRep();
		return this.end;
  	}


	/**
	 * Returns direction of travel at the start of the geographic feature, in degrees.
	 * @return direction (in compass heading) of travel at the first
	 * non-zero-length segment of the geographic feature, in degrees
	 * if all segments are zero-length, will return -1.
	 **/
  	public double getStartHeading() {
		this.checkRep();
		return this.startHeading;
  	}


	/**
	 * Returns direction of travel at the end of the geographic feature, in degrees.
	 * @return direction (in compass heading) of travel at the last
	 * non-zero-length segment of the geographic feature, in degrees.
	 * * if all segments are zero-length, will return -1.
	 **/
  	public double getEndHeading() {
		this.checkRep();
		return this.endHeading;
  	}


  	/**
  	 * Returns total length of the geographic feature, in kilometers.
     * @return total length of the geographic feature, in kilometers.
     *         NOTE: this is NOT as-the-crow-flies, but rather the total
     *         distance required to traverse the geographic feature. These
     *         values are not necessarily equal.
     */
  	public double getLength() {
		this.checkRep();
		return this.length;
  	}


  	/**
   	 * Creates a new GeoFeature that is equal to this GeoFeature with gs
   	 * appended to its end.
     * @requires gs != null && gs.p1 = this.end && gs.name = this.name.
     * @return a new GeoFeature r such that
     *         r.end = gs.p2 &&
     *         r.endHeading = gs.heading &&
     *    	   r.length = this.length + gs.length
     **/
  	public GeoFeature addSegment(GeoSegment gs) {
		this.checkRep();
		assert gs != null && gs.getP1().equals(this.end) && gs.getName().equals(this.name):
        "Can't add segment to GeoFeature: one (or more) input/s are null" ;
        return new GeoFeature(this, gs);
  	}


  	/**
     * Returns an Iterator of GeoSegment objects. The concatenation of the
     * GeoSegments, in order, is equivalent to this GeoFeature. All the
     * GeoSegments have the same name.
     * @return an Iterator of GeoSegments such that
     * <pre>
     *      this.start        = a[0].p1 &&
     *      this.startHeading = a[0].heading &&
     *      this.end          = a[a.length - 1].p2 &&
     *      this.endHeading   = a[a.length - 1].heading &&
     *      this.length       = sum(0 <= i < a.length) . a[i].length &&
     *      for all integers i
     *          (0 <= i < a.length-1 => (a[i].name == a[i+1].name &&
     *                                   a[i].p2d  == a[i+1].p1))
     * </pre>
     * where <code>a[n]</code> denotes the nth element of the Iterator.
     * @see homework1.GeoSegment
     */
  	public Iterator<GeoSegment> getGeoSegments() {
        return this.geoSegments.iterator();
  	}


  	/**
     * Compares the argument with this GeoFeature for equality.
     * @return o != null && (o instanceof GeoFeature) &&
     *         (o.geoSegments and this.geoSegments contain
     *          the same elements in the same order).
     **/
  	public boolean equals(Object o) {
		this.checkRep();
		if (o == null)
        	return false;
        if (!(o instanceof GeoFeature))
        	return false;
        GeoFeature feature = (GeoFeature)o;
        return this.geoSegments.equals(feature.geoSegments);
  	}


  	/**
     * Returns a hash code for this.
     * @return a hash code for this.
     **/
  	public int hashCode() {
    	// This implementation will work, but you may want to modify it
    	// improved performance.
		this.checkRep();
		return (int)(this.length) + this.geoSegments.size();
  	}

  	/**
  	 * Returns a string representation of this.
   	 * @return a string representation of this.
     **/
  	public String toString() {
		this.checkRep();
		return this.name;
  	}
	/**
	 * Checks if the Representation Invariant is being violated.
	 * @throws AssertionError in case it's violated.
	 **/
	private void checkRep() {
		assert this.name != null && this.length >= 0 :
				"Rep. Invariant of GeoFeature is broken";
		boolean valid = true;
		GeoSegment old, next = null;
		for (Iterator<GeoSegment> it = this.geoSegments.iterator(); it.hasNext(); ) {
			old = next;
			next = it.next();
			if(old != null){
				valid = valid && old.getP2().equals(next.getP1()) && this.name.equals(next.getName()) &&
						(it.hasNext() || next.getP2().equals(this.end));
			}else {
				valid = valid && this.name.equals(next.getName()) && next.getP1().equals(this.start);
			}
		}
		assert valid :
		"Rep. Invariant of GeoFeature is broken";
	}
}
