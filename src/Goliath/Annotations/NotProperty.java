/* =========================================================
 * NotProperty.java
 *
 * Author:      kmchugh
 * Created:     29-Jan-2008, 11:58:22
 * 
 * Description
 * --------------------------------------------------------
 * Used for marking a get or set statement as not a property
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath.Annotations;

/**
 * Used for marking a get or set statement as not a property
 *
 * @see         Goliath.DynamicCode.Java#getProperties
 * @version     1.0 29-Jan-2008
 * @author      kmchugh
**/

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface NotProperty 
{
}
