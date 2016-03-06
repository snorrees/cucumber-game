package task;

import cucumber.deps.com.thoughtworks.xstream.converters.SingleValueConverter;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class ClickConverter implements SingleValueConverter {
    @Override
    public boolean canConvert(Class type) {
        return type.equals(Click.class);
    }

    @Override
    public String toString(Object o) {
        Click click = (Click) o;
        return click.x + ", " + click.y;
    }

    @Override
    public Object fromString(String string) {
        String[] values = string.split(",");
        if (values.length != 2) {
            throw new IllegalArgumentException("Postion has to be defined using the format: 'x,y' without the '");
        }
        return new Click(
                Float.parseFloat(values[0]),
                Float.parseFloat(values[1])
        );
    }
}
