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

package org.xdcrafts.flower.core;

import org.xdcrafts.flower.core.impl.DefaultMiddleware;
import org.xdcrafts.flower.tools.Named;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Middleware interface.
 */
public interface Middleware extends Named, BiFunction<Map<String, Object>, Function<Map, Map>, Function<Map, Map>> {

    /**
     * Creates default implementation of Middleware.
     */
    static Middleware middleware(
        String name,
        BiFunction<Map<String, Object>, Function<Map, Map>, Function<Map, Map>> body
    ) {
        return new DefaultMiddleware(name, body);
    }
}
