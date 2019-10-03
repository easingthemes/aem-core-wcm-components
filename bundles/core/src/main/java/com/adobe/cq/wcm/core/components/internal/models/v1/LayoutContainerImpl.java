/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2019 Adobe
 ~
 ~ Licensed under the Apache License, Version 2.0 (the "License");
 ~ you may not use this file except in compliance with the License.
 ~ You may obtain a copy of the License at
 ~
 ~     http://www.apache.org/licenses/LICENSE-2.0
 ~
 ~ Unless required by applicable law or agreed to in writing, software
 ~ distributed under the License is distributed on an "AS IS" BASIS,
 ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 ~ See the License for the specific language governing permissions and
 ~ limitations under the License.
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
package com.adobe.cq.wcm.core.components.internal.models.v1;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.jetbrains.annotations.NotNull;

import com.adobe.cq.wcm.core.components.models.LayoutContainer;

@Model(adaptables = SlingHttpServletRequest.class, adapters = LayoutContainer.class, resourceType = LayoutContainerImpl.RESOURCE_TYPE_V1)
public class LayoutContainerImpl extends AbstractContainerImpl implements LayoutContainer {

    protected static final String RESOURCE_TYPE_V1 = "core/wcm/components/container/v1/container";

    @ScriptVariable
    private Resource resource;

    private LayoutType layout;

    @PostConstruct
    protected void initModel() {
        if (resource != null) {
            ValueMap properties = resource.getValueMap();
            if (properties != null) {
                String layoutProperty = properties.get(LayoutContainer.PN_LAYOUT, String.class);
                layout = LayoutType.getLayoutType(layoutProperty);
                if (layout == null) {
                    layout = LayoutType.SIMPLE;
                }
            }
        }
    }

    @Override
    public @NotNull LayoutType getLayout() {
        return layout;
    }
}
