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

package com.github.xdcrafts.flower.tools;

import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static com.github.xdcrafts.flower.tools.ListApi.getNullable;
import static com.github.xdcrafts.flower.tools.ListApi.getNullableString;
import static org.junit.Assert.assertEquals;

/**
 * List api functions test.
 */
public class ListApiTest {

    @Test
    public void test() {
        final List data = asList(
            asList("John", "Doe", 35),
            asList("Adam", "Smith", 22),
            asList("Yennefer", "Of Vengerberg", 120),
            asList("Triss", "Merigold", 30)
        );

        assertEquals((Integer) 35, getNullable(data, Integer.class, 0, 2));
        assertEquals("Triss", getNullableString(data, 3, 0));
    }
}