/*
 * Copyright (c) 2019 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.ditto.model.connectivity;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ditto.model.base.headers.DittoHeaders;
import org.eclipse.ditto.model.placeholders.Placeholder;
import org.eclipse.ditto.model.placeholders.PlaceholderFactory;
import org.eclipse.ditto.model.placeholders.UnresolvedPlaceholderException;
import org.eclipse.ditto.model.things.ThingId;
import org.junit.Test;
import org.mutabilitydetector.unittesting.AllowedReason;
import org.mutabilitydetector.unittesting.MutabilityAssert;
import org.mutabilitydetector.unittesting.MutabilityMatchers;

import nl.jqno.equalsverifier.EqualsVerifier;

/**
 * Tests {@link org.eclipse.ditto.model.connectivity.ImmutableEnforcementFilter}.
 */
public class ImmutableEnforcementFilterTest {

    @Test
    public void assertImmutability() {
        MutabilityAssert.assertInstancesOf(ImmutableEnforcementFilter.class, MutabilityMatchers.areImmutable(),
                AllowedReason.provided(Enforcement.class, Placeholder.class).areAlsoImmutable());
    }

    @Test
    public void testHashCodeAndEquals() {
        EqualsVerifier.forClass(ImmutableEnforcementFilter.class)
                .usingGetClass()
                .verify();
    }

    @Test
    public void testSimplePlaceholderPrefixed() {
        testSimplePlaceholder(
                "some/other/topic/{{  test:placeholder  }}",
                "some/other/topic/{{ thing:id }}",
                "eclipse:ditto",
                "eclipse:ditto");
    }

    @Test
    public void testSimplePlaceholderPostfixed() {
        testSimplePlaceholder(
                "some/other/topic/{{  test:placeholder  }}",
                "some/other/topic/{{ thing:id }}",
                "eclipse:ditto",
                "eclipse:ditto");
    }

    @Test
    public void testSimplePlaceholderPreAndPostfix() {
        testSimplePlaceholder(
                "some/topic/{{  test:placeholder  }}/topic",
                "some/topic/{{ thing:id }}/topic",
                "eclipse:ditto",
                "eclipse:ditto");
    }

    @Test(expected = ConnectionSignalIdEnforcementFailedException.class)
    public void testSimplePlaceholderPreAndPostfixFails() {
        testSimplePlaceholder(
                "some/topic/{{  test:placeholder  }}/topic",
                "some/topic/{{ thing:id }}/topic",
                "eclipse:ditto",
                "ditto:eclipse");
    }

    @Test(expected = UnresolvedPlaceholderException.class)
    public void testSimplePlaceholderWithUnresolvableInputPlaceholder() {
        testSimplePlaceholder(
                "{{  header:thing-id }}",
                "{{ thing:id }}",
                "eclipse:ditto",
                "eclipse:ditto");
    }

    @Test(expected = UnresolvedPlaceholderException.class)
    public void testSimplePlaceholderWithUnresolvableMatcherPlaceholder() {
        testSimplePlaceholder(
                "{{  header:thing-id }}",
                "{{ thing:id }}",
                "eclipse:ditto",
                "eclipse:ditto");
    }

    @Test
    public void testDeviceIdHeaderMatchesThingId() {
        final HashMap<String, String> map = new HashMap<>();
        map.put("device_id", "eclipse:ditto");
        final Enforcement enforcement = ConnectivityModelFactory.newEnforcement("{{ header:device_id }}",
                "{{ thing:name }}", // does not match
                "{{ thing:id }}");  // matches
        final EnforcementFilterFactory<Map<String, String>, CharSequence> enforcementFilterFactory =
                EnforcementFactoryFactory.newEnforcementFilterFactory(enforcement,
                        PlaceholderFactory.newHeadersPlaceholder());
        final EnforcementFilter<CharSequence> enforcementFilter = enforcementFilterFactory.getFilter(map);
        enforcementFilter.match("eclipse:ditto", DittoHeaders.empty());
        enforcementFilter.match(ThingId.of("eclipse:ditto"), DittoHeaders.empty());
    }

    private void testSimplePlaceholder(final String inputTemplate, final String filterTemplate,
            final String inputValue, final String filterValue) {
        final Enforcement enforcement = ConnectivityModelFactory.newEnforcement(inputTemplate, filterTemplate);
        final EnforcementFilterFactory<String, CharSequence> enforcementFilterFactory =
                EnforcementFactoryFactory.newEnforcementFilterFactory(enforcement, SimplePlaceholder.INSTANCE);
        final EnforcementFilter<CharSequence> enforcementFilter = enforcementFilterFactory.getFilter(inputValue);
        enforcementFilter.match(filterValue, DittoHeaders.empty());
    }

}
