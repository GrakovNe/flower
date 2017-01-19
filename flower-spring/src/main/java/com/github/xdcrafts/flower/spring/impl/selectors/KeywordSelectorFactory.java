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

package com.github.xdcrafts.flower.spring.impl.selectors;

import com.github.xdcrafts.flower.core.impl.selectors.KeywordSelector;
import com.github.xdcrafts.flower.spring.impl.AbstractActionFactoryBean;
import org.springframework.beans.factory.annotation.Required;

/**
 * KeywordSelector factory bean.
 */
public class KeywordSelectorFactory extends AbstractActionFactoryBean<KeywordSelector> {

    private String keyword;
    private boolean required = true;

    @Required
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    @Override
    public Class<?> getObjectType() {
        return KeywordSelector.class;
    }

    @Override
    protected KeywordSelector createInstance() throws Exception {
        return new KeywordSelector(
            getBeanName(), this.keyword, this.required, getMiddleware(getBeanName())
        );
    }
}
