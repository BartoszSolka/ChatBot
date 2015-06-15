import java.io.InputStream;
import java.util.*;

/**
 * @author Bartosz Solka
 */
public class Parser {

    private Random random = new Random(System.nanoTime());

    private Map<String, List<String>> ngrams = new HashMap<>();

    public void run(InputStream is, int degree) {

        Scanner sc = new Scanner(is);
        ArrayDeque<String> buf = new ArrayDeque<>(0);

        while (sc.hasNext()) {
            buf.addFirst(sc.next());
            if (buf.size() > degree) {
                buf.removeLast();
            }

            if (buf.size() == degree) {
                List<String> words = new ArrayList<>(buf);
                String prefix = String.join(" ", words.subList(0, degree));
                String suffix = words.get(degree - 1);
                add(prefix, suffix);
            }
        }

        sc.close();
    }

    private void add(String prefix, String suffix) {
        ngrams.putIfAbsent(prefix, new ArrayList<String>());
        ngrams.get(prefix).add(suffix);
    }

    public String randomPrefix(){
        List<String> prefixes = new ArrayList<>(ngrams.keySet());
        int i = random.nextInt(prefixes.size());
        return prefixes.get(i);
    }

    public String getSuffix(String prefix){
        if(!ngrams.containsKey(prefix)){
            return null;
        }
        List <String> suffixes = ngrams.get(prefix);
        int i = random.nextInt(suffixes.size());
        return suffixes.get(i);
    }


    public boolean isEmpty() {
        return ngrams.isEmpty();
    }
}
