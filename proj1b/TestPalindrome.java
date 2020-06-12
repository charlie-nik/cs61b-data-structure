import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }

    @Test
    public void testIsPalindrome() {

        assertTrue(palindrome.isPalindrome(""));
        assertTrue(palindrome.isPalindrome("a"));
        assertFalse(palindrome.isPalindrome("aA"));

        assertTrue(palindrome.isPalindrome("noon"));
        assertTrue(palindrome.isPalindrome("racecar"));

        assertFalse(palindrome.isPalindrome("spring"));
        assertFalse(palindrome.isPalindrome("loveisallyouneed"));

    }

    @Test
    public void testNewIsPalindrome() {
        OffByOne check = new OffByOne();

        assertTrue(palindrome.isPalindrome("flake", check));
        assertTrue(palindrome.isPalindrome("aab", check));

        assertFalse(palindrome.isPalindrome("kill", check));
        assertFalse(palindrome.isPalindrome("marriage", check));
    }
}
