# regex-impementation

一个正则表达式的实现  
目前支持 *  |  ( ) + ?  
已初步实现 . {} [] 等符号  
需要更多的测试  
使用NFA或DFA来实现查询  
NFA调用:  
`Assertions.assertThat(false).isEqualTo(RegexMatcher.match("ab","(a|b)(c|d)"))`  
DFA调用:  

    DFAUnit matcher = RegexDFAMatcher.compile("(a|b)(c|d)");  
    Assertions.assertThat(false).isEqualTo(RegexDFAMatcher.matchByDFA("ab",matcher));

