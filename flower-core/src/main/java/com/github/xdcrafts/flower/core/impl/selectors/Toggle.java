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

package com.github.xdcrafts.flower.core.impl.selectors;

import java.util.Map;
import java.util.function.Predicate;

/**
 * Toggle predicate.
 */
public class Toggle implements Predicate<Map> {

    private volatile boolean isEnabled = false;

    public Toggle() {
    }

    public Toggle(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    @Override
    public boolean test(Map map) {
        return this.isEnabled;
    }

    @Override
    public String toString() {
        return "Toggle{"
                + "isEnabled=" + this.isEnabled
                + '}';
    }
}

