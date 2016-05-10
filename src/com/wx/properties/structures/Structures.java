/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wx.properties.structures;


import com.wx.properties.structures.list.IndexedList;
import com.wx.properties.structures.list.SingleLineList;
import com.wx.properties.structures.map.RefHashMap;
import com.wx.properties.structures.set.RefSet;
import com.wx.properties.structures.set.SingleLineSet;

/**
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 0.1
 *
 * Project: WXLibraries.ANT File: Structure.java (UTF-8) Date: Jul 7, 2013
 */
public enum Structures {

    /**
     * <b>Parameters:</b>
     * <ol>
     * <li>{@code separator} Separator used to separate the elements (optional)</li>
     *
     * </ol>
     *
     * @see SingleLineList
     */
    list_single_line,
    /**
     * <b>Parameters:</b>
     * <br><i>None</i>
     *
     * @see IndexedList
     */
    list_indexed,
    /**
     * <b>Parameters:</b>
     * <ol>
     * <li>{@code separator} Separator used to separate keys from values (optional)</li>
     * </ol>
     *
     * @see RefHashMap
     */
    map_ref_hash,
    /**
     * <b>Parameters:</b>
     * <ol>
     * <li>{@code separator} Separator used to separated values with the same
     * hash (optional)</li>
     * </ol>
     *
     * @see RefSet
     */
    set_ref,
    /**
     * <b>Parameters:</b>
     * <br><i>None</i>
     *
     * @see SingleLineSet
     */
    set_single_line
}
