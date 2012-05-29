/* ========================================================
 * MimeType.java
 *
 * Author:      kenmchugh
 * Created:     Feb 24, 2011, 4:09:41 PM
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

package Goliath.Constants;

import Goliath.DynamicEnum;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Feb 24, 2011
 * @author      kenmchugh
**/
public class MimeType extends DynamicEnum
{
    /**
     * Creates a new instance of MimeType
     */
    protected MimeType(String tcValue)
    {
        super(tcValue);
    }
    
    private static MimeType g_oApplicationGSP;
    public static MimeType APPLICATION_GOLIATH_SERVER_PAGE()
    {
        if (g_oApplicationGSP == null)
        {
            g_oApplicationGSP = createEnumeration(MimeType.class, "application/gsp");
        }
        return g_oApplicationGSP;
    }

    private static MimeType g_oApplicationJava;
    public static MimeType APPLICATION_JAVA()
    {
        if (g_oApplicationJava == null)
        {
            g_oApplicationJava = createEnumeration(MimeType.class, "application/java");
        }
        return g_oApplicationJava;
    }

    private static MimeType g_oApplicationJar;
    public static MimeType APPLICATION_JAR()
    {
        if (g_oApplicationJar == null)
        {
            g_oApplicationJar = createEnumeration(MimeType.class, "application/jar");
        }
        return g_oApplicationJar;
    }
    
    private static MimeType g_oApplicationJSON;
    public static MimeType APPLICATION_JSON()
    {
        if (g_oApplicationJSON == null)
        {
            g_oApplicationJSON = createEnumeration(MimeType.class, "application/json");
        }
        return g_oApplicationJSON;
    }

    private static MimeType g_oApplicationOctetStream;
    public static MimeType APPLICATION_OCTET_STREAM()
    {
        if (g_oApplicationOctetStream == null)
        {
            g_oApplicationOctetStream = createEnumeration(MimeType.class, "application/octet-stream");
        }
        return g_oApplicationOctetStream;
    }

    private static MimeType g_oApplicationPDF;
    public static MimeType APPLICATION_PDF()
    {
        if (g_oApplicationPDF == null)
        {
            g_oApplicationPDF = createEnumeration(MimeType.class, "application/pdf");
        }
        return g_oApplicationPDF;
    }

    private static MimeType g_oApplicationXGZip;
    public static MimeType APPLICATION_X_GZIP()
    {
        if (g_oApplicationXGZip == null)
        {
            g_oApplicationXGZip = createEnumeration(MimeType.class, "application/x-gzip");
        }
        return g_oApplicationXGZip;
    }

    private static MimeType g_oXJavaApplet;
    public static MimeType APPLICATION_X_JAVA_APPLET()
    {
        if (g_oXJavaApplet == null)
        {
            g_oXJavaApplet = createEnumeration(MimeType.class, "application/x-java-applet");
        }
        return g_oXJavaApplet;
    }

    private static MimeType g_oXJavaSerializedObject;
    public static MimeType APPLICATION_X_JAVA_SERIALIZED_OBJECT()
    {
        if (g_oXJavaSerializedObject == null)
        {
            g_oXJavaSerializedObject = createEnumeration(MimeType.class, "application/x-java-serialized-object");
        }
        return g_oXJavaSerializedObject;
    }

    private static MimeType g_oApplicationXJavaURL;
    public static MimeType APPLICATION_X_JAVA_URL()
    {
        if (g_oApplicationXJavaURL == null)
        {
            g_oApplicationXJavaURL = createEnumeration(MimeType.class, "application/x-java-url");
        }
        return g_oApplicationXJavaURL;
    }

    private static MimeType g_oApplicationXJavaFileList;
    public static MimeType APPLICATION_X_JAVA_FILE_LIST()
    {
        if (g_oApplicationXJavaFileList == null)
        {
            g_oApplicationXJavaFileList = createEnumeration(MimeType.class, "application/x-java-file-list");
        }
        return g_oApplicationXJavaFileList;
    }

    private static MimeType g_oApplicationXJavaScript;
    public static MimeType APPLICATION_X_JAVASCRIPT()
    {
        if (g_oApplicationXJavaScript == null)
        {
            g_oApplicationXJavaScript = createEnumeration(MimeType.class, "application/x-javascript");
        }
        return g_oApplicationXJavaScript;
    }

    private static MimeType g_oApplicationXShockwaveFlash;
    public static MimeType APPLICATION_X_SHOCKWAVE_FLASH()
    {
        if (g_oApplicationXShockwaveFlash == null)
        {
            g_oApplicationXShockwaveFlash = createEnumeration(MimeType.class, "application/x-shockwave-flash");
        }
        return g_oApplicationXShockwaveFlash;
    }

    private static MimeType g_oApplicationXWWWFormURLEncoded;
    public static MimeType APPLICATION_X_WWW_FORM_URL_ENCODED()
    {
        if (g_oApplicationXWWWFormURLEncoded == null)
        {
            g_oApplicationXWWWFormURLEncoded = createEnumeration(MimeType.class, "application/x-www-form-urlencoded");
        }
        return g_oApplicationXWWWFormURLEncoded;
    }

    private static MimeType g_oApplicationXZIP;
    public static MimeType APPLICATION_X_ZIP()
    {
        if (g_oApplicationXZIP == null)
        {
            g_oApplicationXZIP = createEnumeration(MimeType.class, "application/x-zip");
        }
        return g_oApplicationXZIP;
    }
    
    private static MimeType g_oApplicationXHTML_XML;
    public static MimeType APPLICATION_XHTML_XML()
    {
        if (g_oApplicationXHTML_XML == null)
        {
            g_oApplicationXHTML_XML = createEnumeration(MimeType.class, "application/xhtml+xml");
        }
        return g_oApplicationXHTML_XML;
    }

    private static MimeType g_oApplicationXML;
    public static MimeType APPLICATION_XML()
    {
        if (g_oApplicationXML == null)
        {
            g_oApplicationXML = createEnumeration(MimeType.class, "application/xml");
        }
        return g_oApplicationXML;
    }

    private static MimeType g_oApplicationZIP;
    public static MimeType APPLICATION_ZIP()
    {
        if (g_oApplicationZIP == null)
        {
            g_oApplicationZIP = createEnumeration(MimeType.class, "application/zip");
        }
        return g_oApplicationZIP;
    }

    private static MimeType g_oAudioXWav;
    public static MimeType AUDIO_X_WAV()
    {
        if (g_oAudioXWav == null)
        {
            g_oAudioXWav = createEnumeration(MimeType.class, "audio/x-wav");
        }
        return g_oAudioXWav;
    }
    
    private static MimeType g_oAudioMpeg;
    public static MimeType AUDIO_MPEG()
    {
        if (g_oAudioMpeg == null)
        {
            g_oAudioMpeg = createEnumeration(MimeType.class, "audio/mpeg");
        }
        return g_oAudioMpeg;
    }
    
    private static MimeType g_oAudioMP4;
    public static MimeType AUDIO_MP4()
    {
        if (g_oAudioMP4 == null)
        {
            g_oAudioMP4 = createEnumeration(MimeType.class, "audio/mp4");
        }
        return g_oAudioMP4;
    }
    
    private static MimeType g_oAudioOGG;
    public static MimeType AUDIO_OGG()
    {
        if (g_oAudioOGG == null)
        {
            g_oAudioOGG = createEnumeration(MimeType.class, "audio/ogg");
        }
        return g_oAudioOGG;
    }
    
    private static MimeType g_oAudioWEBM;
    public static MimeType AUDIO_WEBM()
    {
        if (g_oAudioWEBM == null)
        {
            g_oAudioWEBM = createEnumeration(MimeType.class, "audio/webm");
        }
        return g_oAudioWEBM;
    }
    
    private static MimeType g_oAudioWAV;
    public static MimeType AUDIO_WAV()
    {
        if (g_oAudioWAV == null)
        {
            g_oAudioWAV = createEnumeration(MimeType.class, "audio/wav");
        }
        return g_oAudioWAV;
    }
    
    private static MimeType g_oImageAll;
    public static MimeType IMAGE_ALL()
    {
        if (g_oImageAll == null)
        {
            g_oImageAll = createEnumeration(MimeType.class, "image/*");
        }
        return g_oImageAll;
    }

    private static MimeType g_oImageGIF;
    public static MimeType IMAGE_GIF()
    {
        if (g_oImageGIF == null)
        {
            g_oImageGIF = createEnumeration(MimeType.class, "image/gif");
        }
        return g_oImageGIF;
    }

    private static MimeType g_oImageICON;
    public static MimeType IMAGE_ICON()
    {
        if (g_oImageICON == null)
        {
            g_oImageICON = createEnumeration(MimeType.class, "image/icon");
        }
        return g_oImageICON;
    }

    private static MimeType g_oImageJPG;
    public static MimeType IMAGE_JPG()
    {
        if (g_oImageJPG == null)
        {
            g_oImageJPG = createEnumeration(MimeType.class, "image/jpeg");
        }
        return g_oImageJPG;
    }

    private static MimeType g_oImagePNG;
    public static MimeType IMAGE_PNG()
    {
        if (g_oImagePNG == null)
        {
            g_oImagePNG = createEnumeration(MimeType.class, "image/png");
        }
        return g_oImagePNG;
    }

    private static MimeType g_oImageXICON;
    public static MimeType IMAGE_X_ICON()
    {
        if (g_oImageXICON == null)
        {
            g_oImageXICON = createEnumeration(MimeType.class, "image/x-icon");
        }
        return g_oImageXICON;
    }

    private static MimeType g_oImageXJavaImage;
    public static MimeType IMAGE_X_JAVA_IMAGE()
    {
        if (g_oImageXJavaImage == null)
        {
            g_oImageXJavaImage = createEnumeration(MimeType.class, "image/x-java-image");
        }
        return g_oImageXJavaImage;
    }

    private static MimeType g_oImageXPict;
    public static MimeType IMAGE_X_PICT()
    {
        if (g_oImageXPict == null)
        {
            g_oImageXPict = createEnumeration(MimeType.class, "image/x-pict");
        }
        return g_oImageXPict;
    }

    private static MimeType g_oMultipartFormData;
    public static MimeType MULTIPART_FORM_DATA()
    {
        if (g_oMultipartFormData == null)
        {
            g_oMultipartFormData = createEnumeration(MimeType.class, "multipart/form-data");
        }
        return g_oMultipartFormData;
    }

    private static MimeType g_oTextCSS;
    public static MimeType TEXT_CSS()
    {
        if (g_oTextCSS == null)
        {
            g_oTextCSS = createEnumeration(MimeType.class, "text/css");
        }
        return g_oTextCSS;
    }
    
    private static MimeType g_oTextCSSLess;
    public static MimeType TEXT_CSS_LESS()
    {
        if (g_oTextCSSLess == null)
        {
            g_oTextCSSLess = createEnumeration(MimeType.class, "text/less");
        }
        return g_oTextCSSLess;
    }
    
    private static MimeType g_oTextCacheManifest;
    public static MimeType TEXT_CACHE_MANIFEST()
    {
        if (g_oTextCacheManifest == null)
        {
            g_oTextCacheManifest = createEnumeration(MimeType.class, "text/cache-manifest");
        }
        return g_oTextCacheManifest;
    }

    private static MimeType g_oTextHTML;
    public static MimeType TEXT_HTML()
    {
        if (g_oTextHTML == null)
        {
            g_oTextHTML = createEnumeration(MimeType.class, "text/html");
        }
        return g_oTextHTML;
    }

    private static MimeType g_oTextJavaScript;
    public static MimeType TEXT_JAVASCRIPT()
    {
        if (g_oTextJavaScript == null)
        {
            g_oTextJavaScript = createEnumeration(MimeType.class, "text/javascript");
        }
        return g_oTextJavaScript;
    }

    private static MimeType g_oTextPlain;
    public static MimeType TEXT_PLAIN()
    {
        if (g_oTextPlain == null)
        {
            g_oTextPlain = createEnumeration(MimeType.class, "text/plain");
        }
        return g_oTextPlain;
    }

    private static MimeType g_oTextURIList;
    public static MimeType TEXT_URI_LIST()
    {
        if (g_oTextURIList == null)
        {
            g_oTextURIList = createEnumeration(MimeType.class, "text/uri-list");
        }
        return g_oTextURIList;
    }

    private static MimeType g_oVideoQuicktime;
    public static MimeType VIDEO_QUICKTIME()
    {
        if (g_oVideoQuicktime == null)
        {
            g_oVideoQuicktime = createEnumeration(MimeType.class, "video/quicktime");
        }
        return g_oVideoQuicktime;
    }
    
    private static MimeType g_oVideoMP4;
    public static MimeType VIDEO_MP4()
    {
        if (g_oVideoMP4 == null)
        {
            g_oVideoMP4 = createEnumeration(MimeType.class, "video/mp4");
        }
        return g_oVideoMP4;
    }
    
    private static MimeType g_oVideoOGG;
    public static MimeType VIDEO_OGG()
    {
        if (g_oVideoOGG == null)
        {
            g_oVideoOGG = createEnumeration(MimeType.class, "video/ogg");
        }
        return g_oVideoOGG;
    }
    
    private static MimeType g_oVideoWEBM;
    public static MimeType VIDEO_WEBM()
    {
        if (g_oVideoWEBM == null)
        {
            g_oVideoWEBM = createEnumeration(MimeType.class, "video/webm");
        }
        return g_oVideoWEBM;
    }
    
    private static MimeType g_oALLTYPES;
    public static MimeType ALLTYPES()
    {
        if (g_oALLTYPES == null)
        {
            g_oALLTYPES = createEnumeration(MimeType.class, "*/*");
        }
        return g_oALLTYPES;
    }
    
    
    




    /*
            loFileMap.put(".xslt", "application/xslt+xml");
            
            
            
            /*
            323 	text/h323
            acx 	application/internet-property-stream
            ai 	application/postscript
            aif 	audio/x-aiff
            aifc 	audio/x-aiff
            aiff 	audio/x-aiff
            asf 	video/x-ms-asf
            asr 	video/x-ms-asf
            asx 	video/x-ms-asf
            au 	audio/basic
            avi 	video/x-msvideo
            axs 	application/olescript
            bcpio 	application/x-bcpio
            bmp 	image/bmp
            cat 	application/vnd.ms-pkiseccat
            cdf 	application/x-cdf
            cer 	application/x-x509-ca-cert
            clp 	application/x-msclip
            cmx 	image/x-cmx
            cod 	image/cis-cod
            cpio 	application/x-cpio
            crd 	application/x-mscardfile
            crl 	application/pkix-crl
            crt 	application/x-x509-ca-cert
            csh 	application/x-csh
            dcr 	application/x-director
            der 	application/x-x509-ca-cert
            dir 	application/x-director
            dll 	application/x-msdownload
            doc 	application/msword
            dot 	application/msword
            dvi 	application/x-dvi
            dxr 	application/x-director
            eps 	application/postscript
            etx 	text/x-setext
            evy 	application/envoy
            fif 	application/fractals
            flr 	x-world/x-vrml
            gtar 	application/x-gtar
            hdf 	application/x-hdf
            hlp 	application/winhlp
            hqx 	application/mac-binhex40
            hta 	application/hta
            htc 	text/x-component
            htt 	text/webviewhtml
            ief 	image/ief
            iii 	application/x-iphone
            ins 	application/x-internet-signup
            isp 	application/x-internet-signup
            jfif 	image/pipeg
            latex 	application/x-latex
            lsf 	video/x-la-asf
            lsx 	video/x-la-asf
            m13 	application/x-msmediaview
            m14 	application/x-msmediaview
            m3u 	audio/x-mpegurl
            man 	application/x-troff-man
            mdb 	application/x-msaccess
            me 	application/x-troff-me
            mht 	message/rfc822
            mhtml 	message/rfc822
            mid 	audio/mid
            mny 	application/x-msmoney
            movie 	video/x-sgi-movie
            mp2 	video/mpeg
            mp3 	audio/mpeg
            mpa 	video/mpeg
            mpe 	video/mpeg
            mpeg 	video/mpeg
            mpg 	video/mpeg
            mpp 	application/vnd.ms-project
            mpv2 	video/mpeg
            ms 	application/x-troff-ms
            mvb 	application/x-msmediaview
            nws 	message/rfc822
            oda 	application/oda
            p10 	application/pkcs10
            p12 	application/x-pkcs12
            p7b 	application/x-pkcs7-certificates
            p7c 	application/x-pkcs7-mime
            p7m 	application/x-pkcs7-mime
            p7r 	application/x-pkcs7-certreqresp
            p7s 	application/x-pkcs7-signature
            pbm 	image/x-portable-bitmap
            pfx 	application/x-pkcs12
            pgm 	image/x-portable-graymap
            pko 	application/ynd.ms-pkipko
            pma 	application/x-perfmon
            pmc 	application/x-perfmon
            pml 	application/x-perfmon
            pmr 	application/x-perfmon
            pmw 	application/x-perfmon
            pnm 	image/x-portable-anymap
            pot, 	application/vnd.ms-powerpoint
            ppm 	image/x-portable-pixmap
            pps 	application/vnd.ms-powerpoint
            ppt 	application/vnd.ms-powerpoint
            prf 	application/pics-rules
            ps 	application/postscript
            pub 	application/x-mspublisher
            ra 	audio/x-pn-realaudio
            ram 	audio/x-pn-realaudio
            ras 	image/x-cmu-raster
            rgb 	image/x-rgb
            rmi 	audio/mid
            roff 	application/x-troff
            rtf 	application/rtf
            rtx 	text/richtext
            scd 	application/x-msschedule
            sct 	text/scriptlet
            setpay 	application/set-payment-initiation
            setreg 	application/set-registration-initiation
            sh 	application/x-sh
            shar 	application/x-shar
            sit 	application/x-stuffit
            snd 	audio/basic
            spc 	application/x-pkcs7-certificates
            spl 	application/futuresplash
            src 	application/x-wais-source
            sst 	application/vnd.ms-pkicertstore
            stl 	application/vnd.ms-pkistl
            svg 	image/svg+xml
            sv4cpio 	application/x-sv4cpio
            sv4crc 	application/x-sv4crc
            t 	application/x-troff
            tar 	application/x-tar
            tcl 	application/x-tcl
            tex 	application/x-tex
            texi 	application/x-texinfo
            texinfo 	application/x-texinfo
            tgz 	application/x-compressed
            tif 	image/tiff
            tiff 	image/tiff
            tr 	application/x-troff
            trm 	application/x-msterminal
            tsv 	text/tab-separated-values
            uls 	text/iuls
            ustar 	application/x-ustar
            vcf 	text/x-vcard
            vrml 	x-world/x-vrml
            wav 	audio/x-wav
            wcm 	application/vnd.ms-works
            wdb 	application/vnd.ms-works
            wks 	application/vnd.ms-works
            wmf 	application/x-msmetafile
            wps 	application/vnd.ms-works
            wri 	application/x-mswrite
            wrl 	x-world/x-vrml
            wrz 	x-world/x-vrml
            xaf 	x-world/x-vrml
            xbm 	image/x-xbitmap
            xla 	application/vnd.ms-excel
            xlc 	application/vnd.ms-excel
            xlm 	application/vnd.ms-excel
            xls 	application/vnd.ms-excel
            xlt 	application/vnd.ms-excel
            xlw 	application/vnd.ms-excel
            xof 	x-world/x-vrml
            xpm 	image/x-xpixmap
            xwd 	image/x-xwindowdump
            z 	application/x-compress
     */
}
