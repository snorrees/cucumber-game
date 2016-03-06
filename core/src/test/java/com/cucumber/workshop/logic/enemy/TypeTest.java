package com.cucumber.workshop.logic.enemy;


import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

/**
 * @author Snorre E. Brekke - Computas
 */

@RunWith(Theories.class)
public class TypeTest {

    @DataPoints
    public static Type[] input = Type.values();

    @Theory
    public void calling_nexttype_previoustype_should_return_the_original_type(Type type) {
        assertThat(type.nextType().previousType()).isEqualTo(type);
    }
}