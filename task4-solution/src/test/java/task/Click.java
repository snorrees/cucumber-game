package task;

import cucumber.deps.com.thoughtworks.xstream.annotations.XStreamConverter;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
@XStreamConverter(ClickConverter.class)
public class Click {
    public final float x;
    public final float y;

    public Click(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
