/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2018 Adobe
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

import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.adobe.cq.wcm.core.components.Utils;
import com.adobe.cq.wcm.core.components.context.CoreComponentTestContext;
import com.adobe.cq.wcm.core.components.models.Accordion;
import com.adobe.cq.wcm.core.components.models.ListItem;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

import static org.junit.Assert.*;

@ExtendWith(AemContextExtension.class)
class AccordionImplTest {

    private static final String TEST_BASE = "/accordion";
    private static final String TEST_ROOT_PAGE = "/content";
    private static final String TEST_ROOT_PAGE_GRID = "/accordion/jcr:content/root/responsivegrid";
    private static final String ACCORDION_1 = TEST_ROOT_PAGE + TEST_ROOT_PAGE_GRID + "/accordion-1";
    private static final String ACCORDION_2 = TEST_ROOT_PAGE + TEST_ROOT_PAGE_GRID + "/accordion-2";
    private static final String ACCORDION_3 = TEST_ROOT_PAGE + TEST_ROOT_PAGE_GRID + "/accordion-3";
    private static final String TEST_APPS_ROOT = "/apps/core/wcm/components";

    private final AemContext context = CoreComponentTestContext.newAemContext();

    @BeforeEach
    void setUp() {
        context.load().json(TEST_BASE + CoreComponentTestContext.TEST_CONTENT_JSON, TEST_ROOT_PAGE);
        context.load().json(TEST_BASE + CoreComponentTestContext.TEST_APPS_JSON, TEST_APPS_ROOT);
    }

    @Test
    void testEmptyAccordion() {
        Accordion accordion = new AccordionImpl();
        List<ListItem> items = accordion.getItems();
        assertEquals(0, items.size());
    }

    @Test
    void testAccordionWithItems() {
        Accordion accordion = getAccordionUnderTest(ACCORDION_1);
        Object[][] expectedItems = {
                {"item_1", "Accordion Item 1"},
                {"item_2", "Accordion Panel 2"},
        };
        verifyAccordionItems(expectedItems, accordion.getItems());
        assertArrayEquals(new String[]{"item_2"}, accordion.getExpandedItems());
        assertFalse(accordion.isSingleExpansion());
        Utils.testJSONExport(accordion, Utils.getTestExporterJSONPath(TEST_BASE, "accordion1"));
    }

    @Test
    void testAccordionWithNestedAccordion() {
        Accordion accordion = getAccordionUnderTest(ACCORDION_2);
        Utils.testJSONExport(accordion, Utils.getTestExporterJSONPath(TEST_BASE, "accordion2"));
    }

    @Test
    void testAccordionSingleExpansion() {
        Accordion accordion = getAccordionUnderTest(ACCORDION_3);
        Object[][] expectedItems = {
                {"item_1", "Accordion Item 1"},
                {"item_2", "Accordion Panel 2"},
        };
        verifyAccordionItems(expectedItems, accordion.getItems());
        assertArrayEquals(new String[]{"item_2"}, accordion.getExpandedItems());
        assertTrue(accordion.isSingleExpansion());
        Utils.testJSONExport(accordion, Utils.getTestExporterJSONPath(TEST_BASE, "accordion3"));
    }

    private Accordion getAccordionUnderTest(String resourcePath, Object... properties) {
        Resource resource = context.currentResource(resourcePath);
        if (resource != null && properties != null) {
            context.contentPolicyMapping(resource.getResourceType(), properties);
        }
        return context.request().adaptTo(Accordion.class);
    }

    private void verifyAccordionItems(Object[][] expectedItems, List<ListItem> items) {
        assertEquals("The accordion contains a different number of items than expected.", expectedItems.length, items.size());
        int index = 0;
        for (ListItem item : items) {
            assertEquals("The accordion item's name is not what was expected.",
                    expectedItems[index][0], item.getName());
            assertEquals("The accordion item's title is not what was expected: " + item.getTitle(),
                    expectedItems[index][1], item.getTitle());
            index++;
        }
    }
}
