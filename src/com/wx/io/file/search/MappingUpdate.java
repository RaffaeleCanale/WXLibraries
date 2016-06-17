package com.wx.io.file.search;

import com.wx.io.file.ReaderFunction;
import com.wx.lexer.Lexer;
import com.wx.lexer.LexerBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

/**
 * Created on 18/01/2015
 *
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class MappingUpdate implements ReaderFunction {

    private final Map<Integer, String> updateMap;

    public MappingUpdate(Map<Integer, String> updateMap) {
        this.updateMap = updateMap;
    }

    @Override
    public String update(String line, int lineNo) throws IOException {
        return updateMap.getOrDefault(lineNo, line);
    }

}
