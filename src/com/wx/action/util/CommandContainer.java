package com.wx.action.util;

import com.wx.action.arg.ArgumentsSupplier;
import com.wx.action.arg.ObjectsSupplier;
import com.wx.action.type.CVB;
import com.wx.action.type.CVBFactory;
import com.wx.action.command.Command;
import com.wx.util.collections.CollectionsUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Raffaele Canale (raffaelecanale@gmail.com)
 * @version 0.1
 */
public class CommandContainer {

    private final List<PropertyContainer> properties; // Sorted, options first
    private final String helpText;
    private final String cmdName;


    public CommandContainer(List<PropertyContainer> properties, String helpText,
                            String cmdName) {
        this.helpText = helpText;
        this.cmdName = cmdName;
        this.properties = CollectionsUtil.safe(properties);
        Collections.sort(this.properties,
                (p1, p2) -> p1.isMarkedOption() == p2.isMarkedOption() ?
                        Integer.compare(p1.getId(), p2.getId()) :
                        Boolean.compare(p2.isMarkedOption(), p1.isMarkedOption()));
    }

    public static CommandContainer actionCommand(String cmdName, String helpText) {
        if (helpText == null) {
            helpText = "";
        }
        return new CommandContainer(new LinkedList<>(), helpText, cmdName);
    }

    /**
     * Parses the given command according to the properties described by this container. This returns an {@link
     * ArgumentsSupplier} that will supply the arguments in {@code cmd} in the same order as described by this
     * container.
     *
     * @param cmd Command to parse
     *
     * @return The {@code ArgumentsSupplier} corresponding to the given command and this container.
     */
    public ArgumentsSupplier toArguments(Command cmd) {
        CVBFactory factory = CVBFactory.instance();
        factory.notifyStart(cmd);

        final Object[] result = new Object[properties.size()];

        for (PropertyContainer property : properties) {
            final int index = property.getId();
            final CVB<Object> builder = factory.getFromType(property);
//            final CommandValueBuilder<Object> builder = CommandValueBuilder.getFromType(property);
            cmd = builder.registerFrom(cmd);

            result[index] = builder.isList() ? builder.getValues() : builder.getValue();
        }

        factory.notifyEnd(cmd);

        return new ObjectsSupplier(result);
    }


    public String getName() {
        return cmdName;
    }

    public String getHelpText() {
        return helpText;
    }

    public List<PropertyContainer> getMarkedOptions() {
        return properties.stream().filter(PropertyContainer::isMarkedOption).collect(Collectors.toList());
    }

    public List<PropertyContainer> getArguments() {
        return properties.stream().filter(p -> !p.isMarkedOption()).collect(Collectors.toList());
    }


    @Override
    public String toString() {
        String props = properties.stream()
                .map((o) -> o.toString().replaceAll("\n", "\n\t\t") + "\n" + "\n\t\t")
                .reduce("", String::concat);
        return "CMD [" + cmdName + "]\n"
                + "\t\t" + helpText.replaceAll("\n", "\n\t\t") + "\n"
                + "\t" + props;
    }


    public static class Builder {

        private final Map<String, PropertyContainer.Builder> properties = new HashMap<>();

        private String helpText;
        private String cmdName;

        public Builder setHelp(String helpText) {
            check(this.helpText, "Help text");
            this.helpText = helpText;
            return this;
        }

        public Builder setCmdName(String name) {
            check(this.cmdName, "name");
            cmdName = name;

            return this;
        }

        public PropertyContainer.Builder createNewProperty(String propName) {
            final int currentCount = properties.size();

            PropertyContainer.Builder prop = new PropertyContainer.Builder();
            prop.setName(propName);
            prop.setId(currentCount);

            properties.put(propName, prop);
            return prop;
        }

        public PropertyContainer.Builder property(String name) {
//            assert properties.containsKey(name) : "Property does not exist: " + name;
            return properties.get(name);
        }


        public CommandContainer build() {
            if (helpText == null) {
                helpText = "";
            }

            List<PropertyContainer> props = properties.values().stream()
                    .map(PropertyContainer.Builder::build)
                    .collect(Collectors.toList());
            return new CommandContainer(props, helpText, cmdName);
        }

        private void check(String value, String name) {
            if (value != null) {
                throw new IllegalStateException(name + " already set: " + value);
            }
        }
    }
}
