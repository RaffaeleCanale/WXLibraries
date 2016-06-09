package com.wx.properties.property;

import java.util.Optional;

/**
 * Simple implementation of the property that will wrap another property and bufferProperty its value.
 * <p>
 * This can be useful for properties that are accessed a lot and have a read/write cost.
 * <p>
 * <p>
 * Date: Oct 10, 2013
 *
 * @param <Type> Type of the property
 *
 * @author Raffaele Canale - raffaelecanale@gmail.com
 * @version 1.0
 */
public class BufferedProperty<Type> extends Property<Type> {

    private final Property<Type> underlyingProperty;
    private final SimpleProperty<Type> bufferProperty;
    private boolean bufferedLoaded;

    /**
     * Constructs a new buffered property.
     *
     * Note that this will trigger a call to {@link Property#get()} of the given property.
     *
     * @param underlyingProperty Property to bufferProperty
     */
    public BufferedProperty(Property<Type> underlyingProperty) {
        this.underlyingProperty = underlyingProperty;
        this.bufferProperty = new SimpleProperty<>();
        this.bufferedLoaded = false;
    }

    /**
     * Set a new value to this property.
     * <p>
     * If the new value is equal to the current value, this will do nothing. Else, the sub-property will immediately be
     * updated.
     *
     * @param v New value to set
     */
    @Override
    public void set(Type v) {
        Optional<Type> newValue = Optional.of(v);
        if (bufferProperty.get().equals(newValue)) {
            return;
        }

        bufferProperty.set(v);
        underlyingProperty.set(v);
    }

    @Override
    public Optional<Type> get() {
        if (!bufferedLoaded) {
            Optional<Type> currentValue = underlyingProperty.get();
            currentValue.ifPresent(bufferProperty::set);
            bufferedLoaded = true;
        }
        return bufferProperty.get();
    }

    @Override
    public Optional<Type> clear() {
        Optional<Type> oldValue = bufferProperty.clear();

        if (!bufferedLoaded || oldValue.isPresent()) {
            underlyingProperty.clear();
            bufferedLoaded = true;
        }

        return oldValue;
    }
}
