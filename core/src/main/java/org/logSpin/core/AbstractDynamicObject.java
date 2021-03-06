package org.logSpin.core;

import groovy.lang.GroovyObject;
import groovy.lang.GroovySystem;
import groovy.lang.MetaClass;
import groovy.lang.MetaMethod;
import org.logSpin.DynamicObject;
import org.codehaus.groovy.runtime.MetaClassHelper;

public abstract class AbstractDynamicObject implements DynamicObject {
    protected Object target;

    public Object tryInvokeMethod(String name, Object[] params) {
        return tryInvokeMethod(target, name, params);
    }

    @Override
    public Object tryInvokeMethod(Object clazz, String name, Object[] params) {
            MetaClass metaClass = getMetaClass(clazz);
        MetaMethod metaMethod = lookupMethod(metaClass, name, convert(params));
        if (metaMethod != null) {
            Object result = metaMethod.doMethodInvoke(clazz, params);
            return result == null ? true : result;
        }
        return null;
    }

    private MetaClass getMetaClass(Object bean) {
        if (bean instanceof GroovyObject) {
            return ((GroovyObject) bean).getMetaClass();
        } else {
            return GroovySystem.getMetaClassRegistry().getMetaClass(bean.getClass());
        }
    }

    protected MetaMethod lookupMethod(MetaClass metaClass, String name, Class<?>[] arguments) {
        return metaClass.pickMethod(name, arguments);
    }

    private Class<?>[] convert(Object[] arguments) {
        if (arguments == null || arguments.length == 0) {
            return MetaClassHelper.EMPTY_CLASS_ARRAY;
        }
        Class<?>[] classes = new Class[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
            Object argType = arguments[i];
            if (argType == null) {
                classes[i] = null;
            } else {
                classes[i] = argType.getClass();
            }
        }
        return classes;
    }
}
