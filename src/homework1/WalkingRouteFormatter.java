package homework1;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.lang.Math;

/**
 * A WalkingDirections class knows how to create a textual description of
 * directions from one location to another suitable for a pedestrian.
 * <p>
 * Calling <tt>computeDirections</tt> should produce directions in the
 * following form:
 * <p>
 * <tt>
 * Turn slight right onto Hankin Road and walk for 2 minutes.<br>
 * Turn slight right onto Trumpeldor Avenue and walk for 15 minutes.<br>
 * Turn left onto Hagalil and walk for 27 minutes.<br>
 * Turn sharp left onto Hanita and walk for 27 minutes.<br>
 * </tt>
 * <p>
 * Each line should correspond to a single geographic feature of the route.
 * In the first line, "Hankin Road" is the name of the first
 * geographic feature of the route, and "2 minutes" is the length of
 * time that it would take to walk along the geographic feature, assuming a
 * walking speed of 20 minutes per kilometer. The time in minutes should
 * be reported to the nearest minute. Each line should be terminated by a
 * newline and should include no extra spaces other than those shown above.
 **/
public class WalkingRouteFormatter extends RouteFormatter {

  	/**
     * Computes a single line of a multi-line directions String that
     * represents the instructions for walking along a single geographic
     * feature.
     * @requires 0 <= origHeading < 360
     * @param geoFeature the geographical feature to traverse.
     * @param origHeading the initial heading.
     * @return A newline-terminated <tt>String</tt> that gives directions
     * 		   on how to walk along this geographical feature.<br>
     * Calling <tt>computeLine</tt> with a GeoFeature instance and an
     * initial heading should produce a newline-terminated String in the
     * following form:
     * <p>
     * <tt>
     * Turn sharp left onto Hanita and walk for 27 minutes.<br>
     * </tt>
     * <p>
     * In the output above, "Hanita" represents the name of the
     * geographic feature, and "27 minutes" is the length of time that it
     * would take to walk along the geographic feature, assuming a walking
     * speed of 20 minutes per kilometer. The time in minutes should be
     * reported to the nearest minute. Each line should be terminated by a
     * newline and should include no extra spaces other than those shown
     * above.
     **/

  	final static int MIN_PER_KILOMETER = 20;

  	public String computeLine(GeoFeature geoFeature, double origHeading) {
		assert geoFeature != null && origHeading >= 0 && origHeading < 360:
				"Can't build walking direction line: one (or more) input/s are null";
		// handle first segment
		String res_str = "";
		double prev_heading = origHeading;
		for (Iterator<GeoSegment> it = geoFeature.getGeoSegments(); it.hasNext(); ) {
			GeoSegment seg = it.next();
			res_str = res_str.concat(this.CreateLine(seg.getName(), prev_heading, seg.getHeading(), seg.getLength()));
			prev_heading = seg.getHeading();
		}
		return res_str;
	}

	/**
	 * Returns a string of a given line (segment)
	 * @requires name != null
	 * @return a string of a given line (segment)
	 */
	private String CreateLine(String name, double origHeading, double newHeading, double length){
  		return this.getTurnString(origHeading, newHeading) + " onto " + name + " and walk for " +
				this.GetMinutes(length) + " minutes.\n";
	}

	/**
	 * Returns the time (in minutes) of walking the given length.
	 * @return A string of the time (in minutes) of walking the given length.
	 */
	private String GetMinutes(double length){
  		return String.valueOf(Math.round(length * MIN_PER_KILOMETER));
	}

}
