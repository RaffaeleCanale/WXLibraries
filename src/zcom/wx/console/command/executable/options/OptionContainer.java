package zcom.wx.console.command.executable.options;

/**
 * This class is a simple container that allows to add a description to an
 * option
 *
 * @author Raffaele Canale
 * @version 1.5
 */
class OptionContainer {

    private final AbstractArgument<?> option;
    private final String optionName;
    private final String optionDesc;

    public OptionContainer(AbstractArgument<?> option, String optionName,
            String optionDesc) {
        this.option = option;
        this.optionName = optionName;
        this.optionDesc = optionDesc;
    }

    public String getUsage(boolean brief) {
        return option.getUsage(optionName, brief);
    }

    public AbstractArgument<?> getOption() {
        return option;
    }

    public String getOptionName() {
        return optionName;
    }

    public String getOptionDesc() {
        return optionDesc;
    }

}
