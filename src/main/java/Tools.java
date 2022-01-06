import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * This class is used for Tools needed by some methods
 */
public class Tools {

    /**
     * This methods checks if a Mail Address is valid by using the javax.mail.internet library
     * @param email
     * @return true if the mail address is valid, false if not
     */
    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

}
