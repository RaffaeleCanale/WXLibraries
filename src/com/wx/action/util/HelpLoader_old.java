//package com.wx.actionN.util;
//
//import com.wx.grammar.GrammarException;
//import com.wx.grammar.ast.ASTVisitors.L;
//import com.wx.grammar.ast.ASTVisitors.VN;
//import com.wx.grammar.symbol.Symbol;
//import com.wx.lexer.tokens.Token;
//import com.wx.tree.Tree;
//import com.wx.tree.Tree.Node;
//import com.wx.tree.TreeBuilder;
//import com.wx.tree.TreeVisitor.VoidVisitor;
//import com.wx.tree.visitor.TransitVisitor;
//
//import java.io.File;
//import java.io.IOException;
//
///**
// *
// * @author Raffaele Canale (raffaelecanale@gmail.com)
// * @version 0.1
// */
//public class HelpLoader_old {
//
//    private final CommandContainer.Builder resultBuilder
//            = new CommandContainer.Builder();
//    private final HelpLoaderGrammar loader = new HelpLoaderGrammar();
//
////    public static void main(String[] args) throws IOException, GrammarException {
////        String path = "build/classes/com/wx/action/cmd/Test.help";
////
////        CommandContainer cc = new HelpLoader_old().load(path);
////        System.out.println(cc);
////    }
//
//    public CommandContainer load(File file) throws IOException, GrammarException {
//
//        Tree<Symbol, Token> tree = loader.parse(file);
////        new PrintTree().print(tree);
//
//        tree.accept(buildTransitTree());
//
//        return resultBuilder.build();
//    }
//
//    private TransitVisitor<Symbol, Token> buildTransitTree() {
//        TreeBuilder<Symbol, VoidVisitor<Symbol, Token>> transitTreeBuilder
//                = new TreeBuilder<>();
//
//        Symbol cmd = loader.getSymbolCmd();
//        Symbol exLitteral = loader.getSymbolExLitteral();
//        Symbol help = loader.getSymbolCmdHelp();
//        Symbol text = loader.getSymbolText();
//        Symbol property = loader.getSymbolProperty();
//
//        // CMD NAME
//        transitTreeBuilder.add((VN) n -> resultBuilder.setCmdName(getExLitteralValue(n)),
//                cmd, exLitteral);
//        // HELP TEXT
//        transitTreeBuilder.add((VN) n -> resultBuilder.setHelp(getText(n)),
//                cmd, help, text);
//        // PROPERTIES
//        transitTreeBuilder.add((VN) n -> performPropertyTransit(n),
//                property);
//
//        return new TransitVisitor<>(transitTreeBuilder.build());
//    }
//
//    private void performPropertyTransit(Node<Symbol, Token> propertyNode) {
//        String name = getExLitteralValue(propertyNode.getNodes()[2].asNode());
//
//        resultBuilder.createNewProperty(name);
//        propertyNode.accept(buildPropertyTransitTree(loader, name));
//    }
//
//    private TransitVisitor<Symbol, Token> buildPropertyTransitTree(HelpLoaderGrammar loader,
//            String propName) {
//        Symbol exLitteral = loader.getSymbolExLitteral();
//        Symbol markers = loader.getSymbolMarkers();
//        Symbol help = loader.getSymbolPropHelp();
//        Symbol text = loader.getSymbolText();
//        Symbol def = loader.getSymbolDef();
//        Symbol count = loader.getSymbolCount();
////        Symbol type = loader.getSymbolType();
//        Symbol types = loader.getSymbolTypes();
//
//        TreeBuilder<Symbol, VoidVisitor<Symbol, Token>> propTransit
//                = new TreeBuilder<>();
//
//        propTransit.add(
//                (VN) n -> resultBuilder.property(propName).setHelpText(getText(n)),
//                help, text);
//        propTransit.add(
//                (VN) n -> resultBuilder.property(propName).addDefaultValues(getDefault(n)),
//                def);
//        propTransit.add(
//                (VN) n -> resultBuilder.property(propName).addMarkers(getExLitteralValue(n)),
//                markers, exLitteral
//        );
//        propTransit.add(
//                (VN) n -> {
//                    Tree[] nodes = n.getNodes();
//                    assert nodes.length == 5;
//                    PropertyContainer.Builder prop = resultBuilder.property(propName);
//                    prop.setMin((int) getNumberToken(nodes[2]));
//                    prop.setMax((int) getNumberToken(nodes[3]));
//                },
//                count
//        );
////        propTransit.add(
////                (VN) n -> {
////                    Tree[] nodes = n.getNodes();
////                    assert nodes.length == 4;
////                    resultBuilder.property(propName)
////                            .setId((int) getNumberToken(nodes[2]));
////                },
////                id
////        );
//        propTransit.add(
//                (VN) n -> resultBuilder.property(propName).setType(getStringToken(n.getNodes()[0]))
//                , types);
//
//        return new TransitVisitor<>(propTransit.build());
//    }
//
//    private String getDefault(Node<Symbol, Token> defaultNode) {
//        Tree[] nodes = defaultNode.getNodes();
//        if (nodes.length == 1) {
//            return getExLitteralValue(nodes[0].asNode());
//        } else {
//            assert nodes.length > 1;
//            String result = "";
//            boolean insertSpace = false;
//            for (int i = 1; i < nodes.length - 1; i++) {
//                if (insertSpace) {
//                    result += " ";
//                }
//                result += getExLitteralValue(nodes[i].asNode());
//                insertSpace = true;
//            }
//
//            return result;
//        }
//    }
//
//    private String getText(Node<Symbol, Token> textNode) {
//        Tree<Symbol, Token>[] nodes = textNode.getNodes();
//        StringBuilder text = new StringBuilder();
//
//        boolean endsWithSpace = false;
//
//        boolean insertSpace = false;
//        for (Tree<Symbol, Token> node : nodes) {
//            if (insertSpace) {
//                text.append(" ");
//            }
//            String value = getStringToken(node);
//            text.append(value);
//
//            insertSpace = !value.endsWith("\n");
//        }
//
//        if (endsWithSpace) {
//            int l = text.length();
//            text.delete(l - 1, l);
//        }
//        return text.toString();
//    }
//
//    private String getExLitteralValue(Node<Symbol, Token> exLitteralNode) {
//        Tree<Symbol, Token>[] nodes = exLitteralNode.getNodes();
//        assert nodes.length == 1;
//        return getStringToken(nodes[0]);
//    }
//
//    private String getStringToken(Tree<Symbol, Token> node) {
//        return node.accept((L<String>) n -> n.getElement().getStringValue());
//    }
//
//    private double getNumberToken(Tree<Symbol, Token> node) {
//        return node.accept((L<Double>) n -> n.getElement().getDoubleValue());
//    }
//
//}
