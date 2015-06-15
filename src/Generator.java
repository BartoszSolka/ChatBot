import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Bartosz Solka
 */
public class Generator {


    public String generateMessage(Parser parser, int degree) {
        List<String> text = new ArrayList<>();

        String prefix = parser.randomPrefix();
        Collections.addAll(text, prefix.split("\\s+"));
        while (true) {

            prefix = String.join(" ", text.subList(text.size() - degree + 1, text.size() - 1));
            String suffix = parser.getSuffix(prefix);
            if (suffix == null) {
                prefix = parser.randomPrefix();
                Collections.addAll(text, prefix.split("\\s+"));
            } else {
                text.add(suffix);
            }

            if (text.get(text.size() - 1).matches(".*[?!.]")) {
                break;
            }


        }
        return String.join(" ", text);
    }
}
