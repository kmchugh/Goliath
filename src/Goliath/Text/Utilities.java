/* ========================================================
 * Utilities.java
 *
 * Author:      kmchugh
 * Created:     May 7, 2011, 9:58:45 AM
 *
 * Description
 * --------------------------------------------------------
 * General Class Description.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * ===================================================== */

package Goliath.Text;

import java.util.regex.Matcher;

        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 May 7, 2011
 * @author      kmchugh
**/
public class Utilities extends Goliath.Object
{

    /**
     * Pads the string on the left using the character provided, ensures the string is
     * no longer than tnFinalLength after padding.
     * @param tcValue the value to pad
     * @param tcPad the pad character or string
     * @param tnFinalLength the final length of the string
     * @return the padded string
     */
    public static String padLeft(String tcValue, String tcPad, int tnFinalLength)
    {
        StringBuilder loBuilder = new StringBuilder();
        do
        {
            loBuilder.append(tcPad);
        } while (loBuilder.length() < tnFinalLength);
        Matcher loMatcher = Goliath.Utilities.getRegexMatcher("(.{" + Integer.toString(tnFinalLength) + "})$", loBuilder.toString() + tcValue);
        return loMatcher.find() ? loMatcher.group(1) : tcValue;
    }

    /**
     * Pads the string on the right using the character provided, ensures the string is
     * no longer than tnFinalLength after padding.
     * @param tcValue the value to pad
     * @param tcPad the pad character or string
     * @param tnFinalLength the final length of the string
     * @return the padded string
     */
    public static String padRight(String tcValue, String tcPad, int tnFinalLength)
    {
        StringBuilder loBuilder = new StringBuilder();
        do
        {
            loBuilder.append(tcPad);
        } while (loBuilder.length() < tnFinalLength);
        Matcher loMatcher = Goliath.Utilities.getRegexMatcher("^(.{" + Integer.toString(tnFinalLength) + "})", tcValue + loBuilder.toString());
        return loMatcher.find() ? loMatcher.group(1) : tcValue;
    }

    /**
     * Simple normalisation of the string value, this
     * helps with comparison of strings.  Pushing a string
     * through this function multiple times has no effect.  Once a string
     * has been normalised, it will always return the same value
     * @param tcValue the string value to normalise
     * @return the normalised string
     */
    public static String normalise(String tcValue)
    {
        // TODO: Implement soundexing here
        return tcValue.replaceAll("\\s", "").toLowerCase();
    }

    /**
     * Creates a new instance of Utilities
     */
    private Utilities()
    {
    }
}
