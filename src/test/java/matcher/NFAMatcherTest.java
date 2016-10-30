package matcher;

import com.learn.regex.regexImplementation.RegexMatcher;
import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * Created by bruce.ge on 2016/10/30.
 */
public class NFAMatcherTest {
    @Test
    public void test(){
        Assertions.assertThat(false).isEqualTo(RegexMatcher.match("ab","(a|b)(c|d)"));
    }
}
