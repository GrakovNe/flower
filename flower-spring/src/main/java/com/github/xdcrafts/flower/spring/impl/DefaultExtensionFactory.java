/*
 * Copyright (c) 2017 Vadim Dubs https://github.com/xdcrafts
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

package com.github.xdcrafts.flower.spring.impl;

import com.github.xdcrafts.flower.core.Action;
import com.github.xdcrafts.flower.core.Middleware;
import com.github.xdcrafts.flower.core.impl.actions.DefaultAction;
import com.github.xdcrafts.flower.core.impl.extensions.DefaultExtension;
import com.github.xdcrafts.flower.tools.ClassApi;
import org.springframework.beans.factory.annotation.Required;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Spring factory bean for default extension that uses bean name as it's name.
 */
public class DefaultExtensionFactory extends AbstractActionFactoryBean<DefaultExtension> {

    private static final SecureRandom RANDOM = new SecureRandom();

    private Object action;
    private Map configuration;

    @Required
    public void setAction(Object action) {
        this.action = action;
    }

    @Required
    public void setConfiguration(Map configuration) {
        this.configuration = configuration;
    }

    @Override
    public Class<?> getObjectType() {
        return DefaultExtension.class;
    }

    @Override
    protected DefaultExtension createInstance() throws Exception {
        return new DefaultExtension(
            getBeanName(), toAction(this.action), this.configuration, getMiddleware(getBeanName())
        );
    }

    /**
     * Convert supplied object to of action if possible.
     */
    private Action toAction(Object item) {
        if (item instanceof Action) {
            return (Action) item;
        } else if (item instanceof String) {
            final String definition = (String) item;
            return new DefaultAction(
                definition + "@" + RANDOM.nextInt(),
                resolveDataFunction((String) item),
                getMiddleware(definition)
            );
        } else {
            String name;
            List<Middleware> middleware;
            try {
                name = (String) ClassApi
                    .findMethod(item.getClass(), "getName")
                    .invoke(item);
                middleware = getMiddleware(name);
            } catch (Throwable t) {
                name  = item.getClass().getName();
                middleware = Collections.emptyList();
            }
            return new DefaultAction(
                name + "@" + RANDOM.nextInt(),
                getDataFunctionExtractor().apply(item, DEFAULT_FUNCTION),
                middleware
            );
        }
    }
}
