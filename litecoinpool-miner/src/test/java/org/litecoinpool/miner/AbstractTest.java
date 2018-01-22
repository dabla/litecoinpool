package org.litecoinpool.miner;

import static org.fest.reflect.core.Reflection.field;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.unitils.UnitilsBlockJUnit4ClassRunner;

@RunWith(UnitilsBlockJUnit4ClassRunner.class)
public abstract class AbstractTest {
    protected static void setFinalStatic(Field field, Object newValue) throws Exception {
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, newValue);
    }

    @SuppressWarnings({"unchecked","rawtypes"})
	protected void setField(Object object, String fieldName, Class fieldClass, Object newValue) {
        field(fieldName).ofType(fieldClass).in(object).set(newValue);
    }

    @Before
    public void initAnnotations() throws Exception {
        MockitoAnnotations.initMocks(this);
    }
}
