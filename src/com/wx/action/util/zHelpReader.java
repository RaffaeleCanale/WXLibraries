//package com.wx.action.cmd;
//
//import com.wx.io.file.LineIterator;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import static com.wx.action.cmd.HelpReaderConstants.*;
//
///**
// *
// * @author Raffaele Canale (raffaelecanale@gmail.com)
// * @version 0.1
// */
//public class zHelpReader {
//
//    public static void main(String[] args) throws IOException, CorruptedHelpException {
//        String path = "build/classes/com/wx/action/cmd/Test.help";
//        zHelpReader reader = new zHelpReader(path);
//        System.out.println(reader.read());
//    }
//
//    
//
//    private final Map<String, CommandContainer> cmds = new HashMap<>();
//
//    private final LineIterator lineReader;
//
//    public zHelpReader(String file) throws FileNotFoundException, IOException {
//        this.lineReader = new LineIterator(file);
//    }
//
//    public Map<String, CommandContainer> read() throws FileNotFoundException, IOException, CorruptedHelpException {
//        parseCommands();
//        return cmds;
//    }
//
//    private boolean consumeEmptyLines() throws IOException {
//        while (lineReader.hasNext() && lineReader.peek().isEmpty()) {
//            lineReader.next();
//        }
//
//        return lineReader.hasNext();
//    }
//
//    private void parseCommands() throws IOException, CorruptedHelpException {
//        CommandContainer cmd;
//        while ((cmd = parseCommand()) != null) {
//            cmds.put(cmd.getName(), cmd);
//        }
//    }
//
//    private CommandContainer parseCommand() throws IOException, CorruptedHelpException {
//        if (!consumeEmptyLines()) { //EOF
//            return null;
//        }
//
//        String cmdName = getValue(CMD_PREFIX);
//        String help = parseText(CMD_HELP_PREFIX);
//
//        List<PropertyContainer> properties = new LinkedList<>();
//        PropertyContainer prop;
//        while ((prop = parseProperty()) != null) {
//            properties.add(prop);
//        }
//
//        return new CommandContainer(properties, help, cmdName);
//    }
//
//    private String parseText(String prefix) throws IOException, CorruptedHelpException {
//        String result = "";
//        String line = getValue(prefix);
//
//        boolean eotReached = false;
//        do {
//            if (line.endsWith(EOT_MARKER)) {
//                eotReached = true;
//                line = line.substring(0, line.length() - EOT_MARKER.length());
//            }
//
//            result += "\n" + line;
//            if (!eotReached) {
//                line = nextLineSafe();
//            }
//        } while (!eotReached);
//
////        String line;
////        boolean eotReached = false;
////        while (!eotReached) {
////            line = nextLineSafe();
////            if (line.endsWith(EOT_MARKER)) {
////                eotReached = true;
////                line = line.substring(0, line.length() - EOT_MARKER.length());
////            }
////
////            result += "\n" + line;
////        }
//        return result;
//    }
//
//    private PropertyContainer parseProperty() throws IOException, CorruptedHelpException {
//        if (!consumeEmptyLines()) { //EOF
//            return null;
//        }
//        if (!lineReader.peek().startsWith(PROP_PREFIX)) {
//            return null;
//        }
//
//        String name = getValue(PROP_PREFIX);
//        int id = toInt(getValue(ID_PREFIX));
//
//        String[] counts = getValue(COUNT_PREFIX).replaceAll(" ( )+", " ").split(" ");
//        if (counts.length != 2) {
//            throw new CorruptedHelpException(lineReader, "Expected two integers (space separated)");
//        }
//        int min = toInt(counts[0]);
//        int max = toInt(counts[1]);
//
//        String[] markers = getValue(MARKERS_PREFIX).replaceAll(" ( )+", " ").split(" ");
//        String[] defaults = parseDefaults(parseText(DEFAULT_PREFIX));
//
//        String help = parseText(PROP_HELP_PREFIX);
//
//        return new PropertyContainer(id, min, max, markers, defaults, help, name);
//    }
//
//    private String[] parseDefaults(String value) throws CorruptedHelpException {
//        List<String> defaults = new LinkedList<>();
//
//        String buildingValue = "";
//        int state = 0;
//        Character expectedHop = null;
//        /*
//         state 0 = triming spaces
//         state 1 = building unprotected
//         state 2 = building protected
//         */
//        for (char c : value.toCharArray()) {
//            switch (state) {
//                case 0: // triming spaces
//                    if (c != ' ') {
//                        if (c == TEXT_PROTEXT_1 || c == TEXT_PROTEXT_2) {
//                            state = 2;
//                            expectedHop = c;
//                        } else {
//                            state = 1;
//                            buildingValue = "" + c;
//                        }
//                    }
//                    break;
//                case 1: // building unprotected
//                    if (c == ' ') {
//                        defaults.add(buildingValue);
//                        buildingValue = "";
//                        state = 0;
//                    } else {
//                        buildingValue += c;
//                    }
//                    break;
//                case 2: // building protected
//                    if (c == expectedHop) {
//                        defaults.add(buildingValue);
//                        buildingValue = "";
//                        expectedHop = null;
//                        state = 0;
//                    } else {
//                        buildingValue += c;
//                    }
//                    break;
//                default:
//                    throw new AssertionError();
//            }
//        }
//
//        if (expectedHop != null) {
//            throw new CorruptedHelpException(lineReader, "EOT reached but expected " + expectedHop);
//        }
//
//        return defaults.toArray(new String[defaults.size()]);
//    }
//
//    private String nextLineSafe() throws IOException, CorruptedHelpException {
//        if (!lineReader.hasNext()) {
//            throw new CorruptedHelpException(lineReader, "Unexpected EOF reached");
//        }
//        return lineReader.next();
//    }
//
//    private int toInt(String value) throws CorruptedHelpException {
//        try {
//            return Integer.parseInt(value);
//        } catch (NumberFormatException ex) {
//            throw new CorruptedHelpException(lineReader, "Expected an integer value");
//        }
//    }
//
//    private String getValue(String prefix) throws CorruptedHelpException, IOException {
//        String line = nextLineSafe();
//        if (!line.startsWith(prefix)) {
//            throw new CorruptedHelpException(lineReader, "Shoult start with " + prefix);
//        }
//
//        return line.substring(prefix.length()).trim();
//    }
//}
