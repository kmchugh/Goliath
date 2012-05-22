/* =======================================================*
 * LibraryVersion.java
 *
 * Author:      kenmchugh
 * Created:     Jun 4, 2010, 12:10:21 AM
 *
 * Description
 * --------------------------------------------------------
 * <Description>
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * =======================================================*/

package Goliath;


/**
 *
 * @author kenmchugh
 */
public class LibraryVersion extends Version
{
    public LibraryVersion(String tcName, int tnMajor, int tnMinor, int tnBuild, int tnRevision, String tcRelease, String tcDescription)
    {
        super(tcName, tnMajor, tnMinor, tnBuild, tnRevision, tcRelease, tcDescription);
    }

    public LibraryVersion()
    {
        // DO NOT MANUALLY MODIFY THIS FILE MAKE THE CHANGES IN BUILD.XML INSTEAD
        super(
                "Goliath",       //||--NAME
                3,        //||--MAJOR
                1,        //||--MINOR
                1,        //||--BUILD
                3862,     //||--REVISION
                "2012/01/16 13:39:44.183",      //||--RELEASE
                "Goliath Core Library"   //||--DESCRIPTION
                );
    }
}
