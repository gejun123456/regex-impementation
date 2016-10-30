package matcher;

import com.learn.regex.regexImplementation.DFAUnit;
import com.learn.regex.regexImplementation.RegexDFAMatcher;
import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * Created by bruce.ge on 2016/10/30.
 */
public class DFAMatcherTest {
    @Test
    public void testMatch(){
        DFAUnit matcher = RegexDFAMatcher.compile("(a|b)(c|d)");
        Assertions.assertThat(false).isEqualTo(RegexDFAMatcher.matchByDFA("ab",matcher));

    }
}
