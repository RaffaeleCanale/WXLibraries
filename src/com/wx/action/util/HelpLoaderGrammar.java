//package com.wx.action.util;
//
//import com.wx.grammar.GrammarException;
//import com.wx.grammar.cf.CFGrammar;
//import com.wx.grammar.cf.CFTreeParser;
//import static com.wx.grammar.cf.element.BuilderHelper.*;
//import com.wx.grammar.cf.element.Element;
//import com.wx.grammar.symbol.NamedSymbol;
//import com.wx.grammar.symbol.Symbol;
//import com.wx.lexer.Lexer;
//import com.wx.lexer.LexerBuilder;
//import com.wx.lexer.tokens.Token;
//import com.wx.tree.Tree;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.Set;
//
///**
// *
// * @author Raffaele Canale (raffaelecanale@gmail.com)
// * @version 0.1
// */
//public class HelpLoaderGrammar {
//    private static final Set<String> KEY_WORDS = new HashSet<>(
//            Arrays.asList("COMMAND", "HELP", "PROPERTY", "COUNT",
//                    "MARKERS", "DEFAULT", "PROP_HELP", "TYPE", "String", "int")
//    );
//    private static final char[] SPECIALS = {'\n', ':', '"', '$'};
//
//    private static Element[] exLiteralElements() {
//        Element[] exLiteralElements = new Element[KEY_WORDS.size() + 2];
//
//        toArray(KEY_WORDS, exLiteralElements);
//        exLiteralElements[exLiteralElements.length - 2] = number();
//        exLiteralElements[exLiteralElements.length - 1] = litteral();
//
//        return exLiteralElements;
//    }
//
//    private static Element[] textElements() {
//        Element[] textElements = new Element[KEY_WORDS.size() + SPECIALS.length + 1];
//
//        toArray(KEY_WORDS, textElements);
//
//        int index = KEY_WORDS.size();
//        for (char s : SPECIALS) {
//            if (s != '$') {
//                textElements[index] = special(s);
//                index++;
//            }
//        }
//
//        textElements[textElements.length - 2] = number();
//        textElements[textElements.length - 1] = litteral();
//
//        return textElements;
//    }
//
//    private static void toArray(Collection<String> keywords, Element[] array) {
//        int i = 0;
//        assert array.length >= keywords.size();
//
//        for (String keyword : keywords) {
//            array[i] = keyWord(keyword);
//            i++;
//        }
//    }
//
//    private final LexerBuilder lexerBuilder = new LexerBuilder()
//            .lineCommentsMarker("//").separators(" ").specials(SPECIALS).keyWords(KEY_WORDS);
//    private final Symbol ex_literal, prop_help, def, defaults, markers,
//            count, property, text, cmd_help, cmd, desc, type, types;
//    private CFGrammar grammar;
//
//
//    public HelpLoaderGrammar() {
//        /*
//         DESC := CMD (PROPERTY)*
//         CMD := command: EXTENDED_LITERAL \n CMD_HELP
//         CMD_HELP := help: \n TEXT $ \n
//         TEXT := (LITERAL | NUMBER | {KEYWORDS} | {SPECIALS} \ {$})*
//         PROPERTY := property: EXTENDED_LITERAL \n COUNT TYPE MARKERS? DEFAULTS PROP_HELP
//         COUNT := count: NUMBER NUMBER \n
//         TYPE := type: TYPES \n
//         TYPES := String | int
//         MARKERS := markers: EXTENDED_LITERAL EXTENDED_LITERAL* \n
//         DEFAULTS := default: DEFAULT*  \n
//         DEFAULT := " EXTENDED_LITERAL* " | EXTENDED_LITERAL
//         PROP_HELP := propertyHelp: \n TEXT $ \n
//         EXTENDED_LITERAL := LITERAL | NUMBER | {KEYWORDS}
//         */
//        desc = new NamedSymbol("DESC");
//        cmd = new NamedSymbol("CMD");
//        cmd_help = new NamedSymbol("CMD_HELP");
//        text = new NamedSymbol("TEXT");
//        property = new NamedSymbol("PROPERTY");
//        count = new NamedSymbol("COUNT");
//        markers = new NamedSymbol("MARKERS");
//        defaults = new NamedSymbol("DEFAULTS");
//        def = new NamedSymbol("DEFAULT");
//        prop_help = new NamedSymbol("PROP_HELP");
//        ex_literal = new NamedSymbol("EXTENDED_LITTERAL");
//        type = new NamedSymbol("TYPE");
//        types = new NamedSymbol("TYPES");
//
//
//        CFGrammar.Builder builder = new CFGrammar.Builder(desc);
//        builder.declareRule(desc,
//                cmd, kleene(property));
//        builder.declareRule(cmd,
//                keyWord("command"), special(':'), ex_literal, special('\n'),
//                cmd_help);
//
//        builder.declareRule(cmd_help,
//                keyWord("help"), special(':'), special('\n'), text,
//                special('$'), special('\n'));
//        builder.declareRule(text,
//                kleene(or(textElements())));
//        builder.declareRule(property,
//                keyWord("property"), special(':'), ex_literal, special('\n'),
//                count, type, optional(markers), defaults, prop_help);
//        builder.declareRule(count,
//                keyWord("count"), special(':'), number(), number(), special('\n'));
//        builder.declareRule(type,
//                keyWord("type"), special(':'), types, special('\n'));
//        builder.declareRule(types,
//                or(keyWord("String"), keyWord("int")));
//        builder.declareRule(markers,
//                keyWord("markers"), special(':'), ex_literal,
//                kleene(ex_literal), special('\n'));
//        builder.declareRule(defaults,
//                keyWord("default"), special(':'), kleene(def),
//                special('\n'));
//        builder.declareRule(def,
//                or(concat(special('"'), kleene(ex_literal), special('"')), ex_literal));
//        builder.declareRule(prop_help,
//                keyWord("propertyHelp"), special(':'), special('\n'), text,
//                special('$'), special('\n'));
//        builder.declareRule(ex_literal,
//                or(exLiteralElements()));
////        try {
//            grammar = builder.build();
////        } catch (GrammarException ex) {
////            throw new RuntimeException(ex);
////        }
//    }
//
//    public CFGrammar getGrammar() {
//        return grammar;
//    }
//
//
//    public Tree<Symbol, Token> parse(File file) throws IOException, GrammarException {
//        Lexer lexer = lexerBuilder.build(new FileInputStream(file));
//
//        return new CFTreeParser(grammar).parse(lexer);
//    }
//
//    //<editor-fold defaultstate="collapsed" desc="Getters">
//    public Symbol getSymbolExLitteral() {
//        return ex_literal;
//    }
//
//    public Symbol getSymbolPropHelp() {
//        return prop_help;
//    }
//
//    public Symbol getSymbolDef() {
//        return def;
//    }
//
//    public Symbol getSymbolDefaults() {
//        return defaults;
//    }
//
//    public Symbol getSymbolMarkers() {
//        return markers;
//    }
//
//    public Symbol getSymbolCount() {
//        return count;
//    }
//
//    public Symbol getSymbolProperty() {
//        return property;
//    }
//
//    public Symbol getSymbolText() {
//        return text;
//    }
//
//    public Symbol getSymbolCmdHelp() {
//        return cmd_help;
//    }
//
//    public Symbol getSymbolCmd() {
//        return cmd;
//    }
//
//    public Symbol getSymbolDesc() {
//        return desc;
//    }
//
//    public Symbol getSymbolType() {
//        return type;
//    }
//
//    public Symbol getSymbolTypes() {
//        return types;
//    }
////</editor-fold>
//
//
//
//
//
//}
