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

package com.github.xdcrafts.flower.tools.map;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utility methods for Map with dot separated path notation.
 */
@SuppressWarnings("unchecked")
public final class MapDotApi {

    private static final String PATH_MUST_BE_SPECIFIED = "Path must be specified!";
    private static final String SEPARATOR = ".";
    private static final String SEPARATOR_REGEX = "\\.";

    /**
     * Private constructor.
     */
    private MapDotApi() {
        // nothing
    }

    /**
     * Walks by map's nodes and extracts optional value of type T.
     * @param <T> value type
     * @param clazz type of value
     * @param map subject
     * @param pathString dot separated string with nodes of nested maps
     * @return optional value of type T
     */
    public static <T> Optional<T> dotGet(final Map map, final Class<T> clazz, final String pathString) {
        if (pathString == null || pathString.isEmpty()) {
            throw new IllegalArgumentException(PATH_MUST_BE_SPECIFIED);
        }
        if (!pathString.contains(SEPARATOR)) {
            return Optional.ofNullable((T) map.get(pathString));
        }
        final Object[] path = pathString.split(SEPARATOR_REGEX);
        return MapApi.get(map, clazz, path);
    }

    /**
     * Walks by map's nodes and extracts optional value of type T.
     * @param <T> value type
     * @param clazz type of value
     * @param map subject
     * @param pathString nodes to walk in map
     * @return value of type T
     */
    public static <T> T dotGetNullable(final Map map, final Class<T> clazz, final String pathString) {
        if (pathString == null || pathString.isEmpty()) {
            throw new IllegalArgumentException(PATH_MUST_BE_SPECIFIED);
        }
        if (!pathString.contains(SEPARATOR)) {
            return (T) map.get(pathString);
        }
        final Object[] path = pathString.split(SEPARATOR_REGEX);
        return MapApi.getUnsafe(map, clazz, path);
    }

    /**
     * Walks by map's nodes and extracts optional value of type T.
     * @param <T> value type
     * @param clazz type of value
     * @param map subject
     * @param pathString nodes to walk in map
     * @return optional value of type T
     */
    public static <T> T dotGetUnsafe(final Map map, final Class<T> clazz, final String pathString) {
        return dotGet(map, clazz, pathString).orElseThrow(() -> new IllegalAccessError(
                "Map "
                        + map
                        + " does not have value of type "
                        + clazz.getName()
                        + " by "
                        + pathString)
        );
    }

    /**
     * Get object value by path.
     * @param map subject
     * @param pathString nodes to walk in map
     * @return value
     */
    public static Optional<Object> dotGet(final Map map, final String pathString) {
        return dotGet(map, Object.class, pathString);
    }

    /**
     * Get object value by path.
     * @param map subject
     * @param pathString nodes to walk in map
     * @return value
     */
    public static Object dotGetNullable(final Map map, final String pathString) {
        return dotGetNullable(map, Object.class, pathString);
    }

    /**
     * Get object value by path.
     * @param map subject
     * @param pathString nodes to walk in map
     * @return value
     */
    public static Object dotGetUnsafe(final Map map, final String pathString) {
        return dotGetUnsafe(map, Object.class, pathString);
    }

    /**
     * Get string value by path.
     * @param map subject
     * @param pathString nodes to walk in map
     * @return value
     */
    public static Optional<String> dotGetString(final Map map, final String pathString) {
        return dotGet(map, String.class, pathString);
    }

    /**
     * Get string value by path.
     * @param map subject
     * @param pathString nodes to walk in map
     * @return value
     */
    public static String dotGetNullableString(final Map map, final String pathString) {
        return dotGetNullable(map, String.class, pathString);
    }

    /**
     * Get string value by path.
     * @param map subject
     * @param pathString nodes to walk in map
     * @return value
     */
    public static String dotGetStringUnsafe(final Map map, final String pathString) {
        return dotGetUnsafe(map, String.class, pathString);
    }

    /**
     * Get map value by path.
     * @param <A> map key type
     * @param <B> map value type
     * @param map subject
     * @param pathString nodes to walk in map
     * @return value
     */
    public static <A, B> Optional<Map<A, B>> dotGetMap(final Map map, final String pathString) {
        return dotGet(map, Map.class, pathString).map(m -> (Map<A, B>) m);
    }

    /**
     * Get map value by path.
     * @param <A> map key type
     * @param <B> map value type
     * @param map subject
     * @param pathString nodes to walk in map
     * @return value
     */
    public static <A, B> Map<A, B> dotGetNullableMap(final Map map, final String pathString) {
        return dotGetNullable(map, Map.class, pathString);
    }

    /**
     * Get map value by path.
     * @param <A> map key type
     * @param <B> map value type
     * @param map subject
     * @param pathString nodes to walk in map
     * @return value
     */
    public static <A, B> Map<A, B> dotGetMapUnsafe(final Map map, final String pathString) {
        return dotGetUnsafe(map, Map.class, pathString);
    }

    /**
     * Get list value by path.
     * @param <T> list value type
     * @param clazz type of value
     * @param map subject
     * @param pathString nodes to walk in map
     * @return value
     */
    public static <T> Optional<List<T>> dotGetList(final Map map, final String pathString, final Class<T> clazz) {
        return dotGet(map, List.class, pathString).map(c -> (List<T>) c);
    }

    /**
     * Get list value by path.
     * @param <T> list value type
     * @param clazz type of value
     * @param map subject
     * @param pathString nodes to walk in map
     * @return value
     */
    public static <T> List<T> dotGetNullableList(final Map map, final String pathString, final Class<T> clazz) {
        return (List<T>) dotGetNullable(map, List.class, pathString);
    }

    /**
     * Get list value by path.
     * @param <T> list value type
     * @param clazz type of value
     * @param map subject
     * @param pathString nodes to walk in map
     * @return value
     */
    public static <T> List<T> dotGetListUnsafe(final Map map, final String pathString, final Class<T> clazz) {
        return (List<T>) dotGetUnsafe(map, List.class, pathString);
    }

    /**
     * Get optional value by path.
     * @param <T> optional value type
     * @param clazz type of value
     * @param map subject
     * @param pathString nodes to walk in map
     * @return value
     */
    public static <T> Optional<Optional<T>> dotGetOptional(
            final Map map, final String pathString, final Class<T> clazz
    ) {
        return dotGet(map, Optional.class, pathString).map(opt -> (Optional<T>) opt);
    }

    /**
     * Get optional value by path.
     * @param <T> optional value type
     * @param clazz type of value
     * @param map subject
     * @param pathString nodes to walk in map
     * @return value
     */
    public static <T> Optional<T> dotGetNullableOptional(
            final Map map, final String pathString, final Class<T> clazz
    ) {
        return dotGetNullable(map, Optional.class, pathString);
    }

    /**
     * Get optional value by path.
     * @param <T> optional value type
     * @param clazz type of value
     * @param map subject
     * @param pathString nodes to walk in map
     * @return value
     */
    public static <T> Optional<T> dotGetOptionalUnsafe(
            final Map map, final String pathString, final Class<T> clazz
    ) {
        return dotGetUnsafe(map, Optional.class, pathString);
    }

    /**
     * Get set of string keys by path.
     * @param map subject
     * @param pathString nodes to walk in map
     * @return value
     */
    public static Set<String> dotGetKeysIn(final Map map, final String pathString) {
        return dotGetMap(map, pathString)
                .map(m -> m
                        .keySet()
                        .stream()
                        .map(Object::toString)
                        .collect(Collectors.toSet())
                ).orElse(new HashSet<>());
    }

    /**
     * Checks if specified path exists.
     * @param map subject
     * @param pathString nodes to walk in map
     * @return boolean value
     */
    public static boolean dotContains(final Map map, final String pathString) {
        return dotGet(map, pathString).isPresent();
    }

    /**
     * Associates new value in map placed at path. New nodes are created with nodeClass if needed.
     * @param map subject original map
     * @param nodeClass class for intermediate nodes
     * @param pathString nodes to walk in map path to place new value
     * @param value new value
     * @return original map
     */
    public static Map dotAssoc(
            final Map map, final Class<? extends Map> nodeClass, final String pathString, final Object value
    ) {
        if (pathString == null || pathString.isEmpty()) {
            throw new IllegalArgumentException(PATH_MUST_BE_SPECIFIED);
        }
        if (value == null) {
            return map;
        }
        if (!pathString.contains(SEPARATOR)) {
            map.put(pathString, value);
            return map;
        }
        return MapApi.assoc(map, nodeClass, pathString.split(SEPARATOR_REGEX), value);
    }

    /**
     * Associates new value in map placed at path. New nodes are created with same class as map if needed.
     * @param map subject original map
     * @param pathString nodes to walk in map path to place new value
     * @param value new value
     * @return original map
     */
    public static Map dotAssoc(final Map map, final String pathString, final Object value) {
        return dotAssoc(map, map.getClass(), pathString, value);
    }


    /**
     * Dissociates value by specified path.
     * @param map subject original map
     * @param pathString nodes to walk in map path of value
     * @return original map
     */
    public static Map dotDissoc(final Map map, final String pathString) {
        if (pathString == null || pathString.isEmpty()) {
            throw new IllegalArgumentException(PATH_MUST_BE_SPECIFIED);
        }
        if (!pathString.contains(SEPARATOR)) {
            map.remove(pathString);
            return map;
        }
        final Object[] path = pathString.split(SEPARATOR_REGEX);
        return MapApi.dissoc(map, path);
    }
}
