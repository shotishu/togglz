package org.togglz.core.manager;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Properties;
import java.util.Set;

import org.fest.assertions.core.Condition;
import org.junit.Test;
import org.togglz.core.Feature;
import org.togglz.core.group.FeatureGroup;
import org.togglz.core.metadata.FeatureMetaData;
import org.togglz.core.util.NamedFeature;

public class PropertyFeatureProviderTest {

    @Test
    public void shouldSupportDefinitionWithoutLabel() {

        Properties properties = new Properties();
        properties.setProperty("F1", "");

        PropertyFeatureProvider provider = new PropertyFeatureProvider(properties);

        Set<Feature> features = provider.getFeatures();
        assertThat(features)
            .hasSize(1)
            .areExactly(1, featureNamed("F1"));

        FeatureMetaData metadata = provider.getMetaData(new NamedFeature("F1"));
        assertThat(metadata).isNotNull();
        assertThat(metadata.getLabel()).isEqualTo("F1");
        assertThat(metadata.isEnabledByDefault()).isFalse();
        assertThat(metadata.getGroups()).isEmpty();

    }

    @Test
    public void shouldSupportDefinitionWithOnlyLabel() {

        Properties properties = new Properties();
        properties.setProperty("F1", "My Feature");

        PropertyFeatureProvider provider = new PropertyFeatureProvider(properties);

        Set<Feature> features = provider.getFeatures();
        assertThat(features)
            .hasSize(1)
            .areExactly(1, featureNamed("F1"));

        FeatureMetaData metadata = provider.getMetaData(new NamedFeature("F1"));
        assertThat(metadata).isNotNull();
        assertThat(metadata.getLabel()).isEqualTo("My Feature");
        assertThat(metadata.isEnabledByDefault()).isFalse();
        assertThat(metadata.getGroups()).isEmpty();

    }

    @Test
    public void shouldSupportDefinitionWithLabelAndDefault() {

        Properties properties = new Properties();
        properties.setProperty("F1", "My Feature;true");

        PropertyFeatureProvider provider = new PropertyFeatureProvider(properties);

        Set<Feature> features = provider.getFeatures();
        assertThat(features)
            .hasSize(1)
            .areExactly(1, featureNamed("F1"));

        FeatureMetaData metadata = provider.getMetaData(new NamedFeature("F1"));
        assertThat(metadata).isNotNull();
        assertThat(metadata.getLabel()).isEqualTo("My Feature");
        assertThat(metadata.isEnabledByDefault()).isTrue();
        assertThat(metadata.getGroups()).isEmpty();

    }

    @Test
    public void shouldSupportDefinitionWithLabelAndDefaultAndTrailingSemicolon() {

        Properties properties = new Properties();
        properties.setProperty("F1", "My Feature;true;");

        PropertyFeatureProvider provider = new PropertyFeatureProvider(properties);

        Set<Feature> features = provider.getFeatures();
        assertThat(features)
            .hasSize(1)
            .areExactly(1, featureNamed("F1"));

        FeatureMetaData metadata = provider.getMetaData(new NamedFeature("F1"));
        assertThat(metadata).isNotNull();
        assertThat(metadata.getLabel()).isEqualTo("My Feature");
        assertThat(metadata.isEnabledByDefault()).isTrue();
        assertThat(metadata.getGroups()).isEmpty();

    }

    @Test
    public void shouldSupportDefinitionWithSingleGroup() {

        Properties properties = new Properties();
        properties.setProperty("F1", "My Feature;true;Group1");

        PropertyFeatureProvider provider = new PropertyFeatureProvider(properties);

        Set<Feature> features = provider.getFeatures();
        assertThat(features)
            .hasSize(1)
            .areExactly(1, featureNamed("F1"));

        FeatureMetaData metadata = provider.getMetaData(new NamedFeature("F1"));
        assertThat(metadata).isNotNull();
        assertThat(metadata.getLabel()).isEqualTo("My Feature");
        assertThat(metadata.isEnabledByDefault()).isTrue();
        assertThat(metadata.getGroups())
            .hasSize(1)
            .areExactly(1, groupNamed("Group1"));

    }

    @Test
    public void canInitializeFromProperties() {

        Properties properties = new Properties();
        properties.setProperty("ID_1", "ID 1;true;Group 1,Group Other");
        properties.setProperty("ID_2", "ID 2;false;Group 2");

        PropertyFeatureProvider provider = new PropertyFeatureProvider(properties);

        Set<Feature> features = provider.getFeatures();

        assertThat(features)
            .hasSize(2)
            .areExactly(1, featureNamed("ID_1"))
            .areExactly(1, featureNamed("ID_2"));

        FeatureMetaData metadata1 = provider.getMetaData(new NamedFeature("ID_1"));
        assertThat(metadata1).isNotNull();
        assertThat(metadata1.getLabel()).isEqualTo("ID 1");
        assertThat(metadata1.isEnabledByDefault()).isTrue();
        assertThat(metadata1.getGroups())
            .hasSize(2)
            .areExactly(1, groupNamed("Group 1"))
            .areExactly(1, groupNamed("Group Other"));

        FeatureMetaData metadata2 = provider.getMetaData(new NamedFeature("ID_2"));
        assertThat(metadata2).isNotNull();
        assertThat(metadata2.getLabel()).isEqualTo("ID 2");
        assertThat(metadata2.isEnabledByDefault()).isFalse();
        assertThat(metadata2.getGroups())
            .hasSize(1)
            .areExactly(1, groupNamed("Group 2"));

    }

    private Condition<FeatureGroup> groupNamed(final String name) {
        return new Condition<FeatureGroup>() {
            @Override
            public boolean matches(FeatureGroup value) {
                return value != null && value.getLabel().equals(name);
            }
        };
    }

    private Condition<Feature> featureNamed(final String name) {
        return new Condition<Feature>() {
            @Override
            public boolean matches(Feature value) {
                return value != null && value.name().equals(name);
            }
        };
    }

}
