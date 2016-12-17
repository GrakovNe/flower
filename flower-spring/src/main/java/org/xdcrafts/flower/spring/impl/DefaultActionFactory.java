/*
 * Copyright (c) 2016 Vadim Dubs https://github.com/xdcrafts
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */

package org.xdcrafts.flower.spring.impl;

import org.xdcrafts.flower.core.AsFunction;
import org.xdcrafts.flower.core.Middleware;
import org.xdcrafts.flower.core.impl.DefaultAction;
import org.xdcrafts.flower.spring.AbstractBeanNameAwareFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Spring factory bean for default action that uses bean name as action name.
 */
public class DefaultActionFactory
    extends AbstractBeanNameAwareFactoryBean<DefaultAction> implements ApplicationContextAware {

    private static final String SPLITTER = "::";

    private final String subject;
    private final String method;
    private final List<Middleware> middlewares;
    private ApplicationContext applicationContext;

    public DefaultActionFactory(String method) {
        final String[] subjectAndMethod = method.split(SPLITTER);
        if (subjectAndMethod.length != 2) {
            throw new IllegalArgumentException();
        }
        this.subject = subjectAndMethod[0];
        this.method = subjectAndMethod[1];
        this.middlewares = new ArrayList<>();
    }

    public DefaultActionFactory(String method, List<Middleware> middlewares) {
        final String[] subjectAndMethod = method.split(SPLITTER);
        if (subjectAndMethod.length != 2) {
            throw new IllegalArgumentException();
        }
        this.subject = subjectAndMethod[0];
        this.method = subjectAndMethod[1];
        this.middlewares = middlewares;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public Class<?> getObjectType() {
        return DefaultAction.class;
    }

    @Override
    protected DefaultAction createInstance() throws Exception {
        return new DefaultAction(
            getBeanName(),
            buildActionFunction(this.subject, this.middlewares, this.method)
        );
    }


    private static Map safeVirtualInvoke(MethodHandle methodHandle, Object bean, Map context) {
        try {
            return (Map) methodHandle.invoke(bean, context);
        } catch (Throwable throwable) {
            if (throwable instanceof RuntimeException) {
                throw (RuntimeException) throwable;
            }
            throw new RuntimeException(throwable);
        }
    }

    private static Map safeStaticInvoke(MethodHandle methodHandle, Map context) {
        try {
            return (Map) methodHandle.invoke(context);
        } catch (Throwable throwable) {
            if (throwable instanceof RuntimeException) {
                throw (RuntimeException) throwable;
            }
            throw new RuntimeException(throwable);
        }
    }

    private Function<Map, Map> buildActionFunction(
        String classOrBeanName, List<Middleware> middlewareLists, String methodName
    ) {
        try {
            final Function<Function<Map, Map>, Function<Map, Map>> middleware = middlewareLists
                .stream()
                .map(AsFunction::asFunction)
                .reduce(Function.identity(), Function::andThen);
            final Object bean = this.applicationContext.containsBean(classOrBeanName)
                ? this.applicationContext.getBean(classOrBeanName)
                : null;
            final boolean isVirtual = bean != null;
            final Class clazz = isVirtual ? bean.getClass() : Class.forName(classOrBeanName);
            final MethodHandles.Lookup lookup = MethodHandles.lookup();
            final MethodType methodType = MethodType.methodType(Map.class, Map.class);
            final MethodHandle methodHandle = isVirtual
                ? lookup.findVirtual(clazz, methodName, methodType)
                : lookup.findStatic(clazz, methodName, methodType);
            final Function<Map, Map> pureFunction = isVirtual
                ? ctx -> safeVirtualInvoke(methodHandle, bean, ctx)
                : ctx -> safeStaticInvoke(methodHandle, ctx);
            return middleware.apply(pureFunction);
        } catch (Throwable throwable) {
            if (throwable instanceof RuntimeException) {
                throw (RuntimeException) throwable;
            }
            throw new RuntimeException(throwable);
        }
    }
}