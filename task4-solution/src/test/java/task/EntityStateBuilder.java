package task;

import com.artemis.Entity;
import com.cucumber.workshop.logic.basic.HitPoints;
import com.cucumber.workshop.logic.enemy.Enemy;
import com.cucumber.workshop.logic.enemy.Type;
import com.cucumber.workshop.logic.spatial.Position;
import com.cucumber.workshop.logic.spatial.Radius;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import static java.util.Arrays.asList;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class EntityStateBuilder {

    private final List<EntityStateMapper> mappers = asList(
            map("x", this::setX),
            map("y", this::setY),
            map("radius", this::setRadius),
            map("Hit Points", this::setHp),
            map("Enemy type", this::setEnemyType)

    );

    public void modifyEntity(Entity entity, Map<String, String> entityState) {
        entityState.entrySet()
                   .stream()
                   .forEach(e -> modifyEntity(entity, e.getKey(), e.getValue()));
    }

    private void setX(Entity e, String value) {
        e.getComponent(Position.class).x(asFloat(value));
    }

    private void setY(Entity e, String value) {
        e.getComponent(Position.class).y(asFloat(value));
    }

    private void setHp(Entity e, String value) {
        e.getComponent(HitPoints.class).hp(asFloat(value));
    }

    private void setRadius(Entity e, String value) {
        e.getComponent(Radius.class).r(asFloat(value));
    }

    private void setEnemyType(Entity e, String value) {
        Type type = Type.valueOf(value.toUpperCase());
        e.getComponent(Enemy.class).type(type);
    }

    private float asFloat(String value) {
        return Float.parseFloat(value);
    }

    EntityStateMapper map(String columnName,
                          BiConsumer<Entity, String> valueConsumer) {
        return new EntityStateMapper(columnName, valueConsumer);
    }

    private void modifyEntity(Entity entity, String columnName, String columnValue) {
        mappers.stream()
               .filter(m -> m.supports(columnName))
               .forEach(m -> m.modify(entity, columnValue));
    }

    private static class EntityStateMapper {
        String columnName;
        BiConsumer<Entity, String> valueConsumer;

        public EntityStateMapper(String columnName,
                                 BiConsumer<Entity, String> valueConsumer) {
            this.columnName = columnName;
            this.valueConsumer = valueConsumer;
        }

        public void modify(Entity entity, String value) {
            valueConsumer.accept(entity, value);
        }

        public boolean supports(String columnName) {
            return this.columnName.equals(columnName);
        }
    }
}
