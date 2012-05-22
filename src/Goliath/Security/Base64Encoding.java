/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Security;

import java.io.ByteArrayOutputStream;


/**
 * This class contains two static methods for BASE64 encoding and decoding.
 * @author <a href="http://izhuk.com">Igor Zhukovsky</a>
 */
public class Base64Encoding
{
    private static String BASE64_CHARSET =
		"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

    public static String encode(String tcString)
    {
        return encode(tcString.getBytes());
    }


    public static String encode(byte[] taData)
    {
        char laOutput[] = new char[4];
        int lnState = 1;
        int lnRestBits = 0;
        int lnChunks = 0;

        StringBuffer loEncoded = new StringBuffer();

    	for(int i=0; i < taData.length; i++)
        {
            int ic = (taData[i] >= 0 ? taData[i] : (taData[i] & 0x7F) + 128);
            switch (lnState)
            {
                case 1: laOutput[0] = BASE64_CHARSET.charAt(ic >>> 2);
                     lnRestBits = ic & 0x03;
                     break;
             	case 2: laOutput[1] = BASE64_CHARSET.charAt((lnRestBits << 4) | (ic >>> 4));
                     lnRestBits = ic & 0x0F;
                     break;
             	case 3: laOutput[2] = BASE64_CHARSET.charAt((lnRestBits << 2) | (ic >>> 6));
                     laOutput[3] = BASE64_CHARSET.charAt(ic & 0x3F);
                     loEncoded.append(laOutput);

                     // keep no more then 76 character per line
                     lnChunks++;
                     if ((lnChunks % 19)==0) loEncoded.append("\r\n");
                     break;
            }
            lnState = (lnState < 3 ? lnState+1 : 1);
    	} // for

    	/* finalize */
    	switch (lnState)
    	{    case 2:
             	 laOutput[1] = BASE64_CHARSET.charAt((lnRestBits << 4));
                 laOutput[2] = laOutput[3] = '=';
                 loEncoded.append(laOutput);
                 break;
             case 3:
             	 laOutput[2] = BASE64_CHARSET.charAt((lnRestBits << 2));
                 laOutput[3] = '=';
                 loEncoded.append(laOutput);
                 break;
    	}

        return loEncoded.toString();
    }

    public static byte[] decode(String encoded)
    {
        int i;
    	byte laOutput[] = new byte[3];
    	int lnState;
    	final String ILLEGAL_STRING = "Illegal BASE64 string";

        ByteArrayOutputStream loData = new ByteArrayOutputStream(encoded.length());

    	lnState = 1;
    	for(i=0; i < encoded.length(); i++)
        {
            byte c;
            {
            	char alpha = encoded.charAt(i);
            	if (Character.isWhitespace(alpha))
                {
                    continue;
                }

                if ((alpha >= 'A') && (alpha <= 'Z'))
                {
                    c = (byte)(alpha - 'A');
                }
                else if ((alpha >= 'a') && (alpha <= 'z'))
                {
                    c = (byte)(26 + (alpha - 'a'));
                }
                else if ((alpha >= '0') && (alpha <= '9'))
                {
                    c = (byte)(52 + (alpha - '0'));
                }
                else if (alpha=='+')
                {
                    c = 62;
                }
                else if (alpha=='/')
                {
                    c = 63;
                }
                else if (alpha=='=')
                {
                    break;
                }
                else
                {
                    throw new IllegalArgumentException(ILLEGAL_STRING);
                }
            }

            switch(lnState)
            {
                case 1: 
                    laOutput[0] = (byte)(c << 2);
                    break;
                case 2: 
                    laOutput[0] |= (byte)(c >>> 4);
                    laOutput[1] = (byte)((c & 0x0F) << 4);
                    break;
                case 3: 
                    laOutput[1] |= (byte)(c >>> 2);
                    laOutput[2] =  (byte)((c & 0x03) << 6);
                    break;
                case 4: 
                    laOutput[2] |= c;
                    loData.write(laOutput,0,laOutput.length);
                    break;
            }
            lnState = (lnState < 4 ? lnState+1 : 1);
    	}

        if (i < encoded.length())
        {
            /* then '=' found, but the end of string */
            switch(lnState)
            {
                case 3: 
                    loData.write(laOutput,0,1);
                    if ((encoded.charAt(i)=='=') && (encoded.charAt(i+1)=='='))
                    {
                        return loData.toByteArray();
                    }
                    else
                    {
                        throw new IllegalArgumentException(ILLEGAL_STRING);
                    }

            	case 4:
            	    loData.write(laOutput,0,2);
                    if (encoded.charAt(i)=='=')
                    {
                        return loData.toByteArray();
                    }
                    else
                    {
                        throw new IllegalArgumentException(ILLEGAL_STRING);
                    }

            	default:
            	    throw new IllegalArgumentException(ILLEGAL_STRING);
            }
        }
    	else
        {
    	    if (lnState==1)
            {
                return loData.toByteArray();
            }
    	    else
            {
                throw new IllegalArgumentException(ILLEGAL_STRING);
            }
    	}

    }
}
