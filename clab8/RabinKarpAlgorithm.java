public class RabinKarpAlgorithm {


    /**
     * This algorithm returns the starting index of the matching substring.
     * This method will return -1 if no matching substring is found, or if the input is invalid.
     */
    public static int rabinKarp(String input, String pattern) {
        RollingString x = new RollingString(input.substring(0, pattern.length()), pattern.length());
        RollingString y = new RollingString(pattern, pattern.length());
        int hsY = y.hashCode();
        for (int i = pattern.length(); i < input.length(); i++) {
            if (x.hashCode() == hsY) {
                return i - pattern.length();
            }
            x.addChar(input.charAt(i));
        }
        return -1;
    }

}
