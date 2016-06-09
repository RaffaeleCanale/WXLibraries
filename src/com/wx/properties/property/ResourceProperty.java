package com.wx.properties.property;

import com.wx.properties.PropertiesManager;
import com.wx.util.representables.TypeCaster;

import java.util.Optional;

/**
 *
 * This wrapper offers an access to a precise property of a
 * {@code ResourcePage}.
 * <br><br>
 * With an instance of this object, the user can get and set a property of a
 * {@code ResourcePage} without actually having any direct access to it.
 *
 * @param <PropertyType> Type of the property to wrap
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 1.0
 *
 * Project: WXLibraries.ANT File: Property.java (UTF-8) Date: Jul 9, 2013
 */
public class ResourceProperty<PropertyType> extends Property<PropertyType> {

    private final PropertiesManager page;
    private final String key;
    private final TypeCaster<String, PropertyType> propCaster;

    /**
     * Constructs a new property.
     *
     * @param page       Page pointed by this property
     * @param key        Key of this property
     * @param propCaster Caster of this property
     */
    public ResourceProperty(PropertiesManager page, String key,
            TypeCaster<String, PropertyType> propCaster) {
        this.page = page;
        this.key = key;
        this.propCaster = propCaster;
    }

    /**
     * Set the value of this property.
     * <br><br>
     * 
     * Note that this will not save the {@code ResourcePage} and this change
     * will not be committed until then.
     * 
     * @param value Value to set for this property
     *
     */
    @Override
    public void set(PropertyType value) {
        page.setProperty(key, value, propCaster::castIn);
    }

    /**
     * 
     * @return Value of this property
     */
    @Override
    public Optional<PropertyType> get() {
        return page.getValue(key, propCaster::castOut);
    }

    /**
     * Tells if this property exists or not.
     * <br><br>
     * Note that in the case it does not exist, simply use {@link #set(Object) }
     * to create it.
     * 
     * @return {@code true} if this property exists
     */
    public boolean exists() {
        return page.containsKey(key);
    }
    
    /**
     * Remove this property from the page.
     * 
     * @return The plain old value
     */
    public Optional<PropertyType> clear() {
        page.removeProperty(key);

        return null;
    }
}
