public class Palindrome {

    // Transform the given word into a Deque structure and return it
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> result = new LinkedListDeque<>();
        for (int i = 0; i < word.length(); i++) {
            result.addLast(word.charAt(i));
        }
        return result;
    }

    // Return true if the given word is a palindrome, false otherwise
    public boolean isPalindrome(String word) {
        Deque<Character> d = wordToDeque(word);
        boolean result = true;
        while (d.size() > 1) {
            result = d.removeFirst() == d.removeLast() && result;
        }
        return result;
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> d = wordToDeque(word);
        boolean result = true;
        while (d.size() > 1) {
            char a = d.removeFirst();
            char b = d.removeLast();
            result = cc.equalChars(a, b) && result;
        }
        return result;
    }
}
